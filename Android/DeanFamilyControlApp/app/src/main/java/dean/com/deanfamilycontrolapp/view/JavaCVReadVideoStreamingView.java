package dean.com.deanfamilycontrolapp.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;

/**
 * JavaCV 取流器View
 * <p>
 * Created by dean on 2018/2/4.
 */
@SuppressLint({"AppCompatCustomView", "ViewConstructor"})
public class JavaCVReadVideoStreamingView extends ImageView {

    private boolean stop = false;

    /**
     * 监听器
     */
    public interface OnJavaCVReadVideoStreamingListener {

        /**
         * 正在连接
         */
        void onConnect();

        /**
         * 开始播放
         */
        void onPlay();

        /**
         * 错误信息
         *
         * @param error
         */
        void onError(String error);
    }

    public JavaCVReadVideoStreamingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 读取视频流并显示
     *
     * @param activity
     * @param rtmp                               读取视频流地址
     * @param outputFilePath                     保存视频流到本地地址
     * @param audioChannel                       是否使用声音
     * @param onJavaCVReadVideoStreamingListener 监听器
     */
    public void play(Activity activity, String rtmp, String outputFilePath, int audioChannel, OnJavaCVReadVideoStreamingListener onJavaCVReadVideoStreamingListener) {
        Loader.load(opencv_objdetect.class);

        stop = false;

        new Thread(() -> {
            // 正在连接
            if (activity != null && onJavaCVReadVideoStreamingListener != null)
                activity.runOnUiThread(() -> onJavaCVReadVideoStreamingListener.onConnect());

            // 获取流媒体源
            FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(rtmp);
            // 流媒体输出地址，分辨率（长，宽），是否录制音频（0：不录制 / 1：录制）
            FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputFilePath, 1280, 720, audioChannel);

            try {
                // 开始读取视频流
                grabber.start();
                recorder.start();

                if (activity != null && onJavaCVReadVideoStreamingListener != null)
                    activity.runOnUiThread(() -> onJavaCVReadVideoStreamingListener.onPlay());

                Frame frame;
                while (!stop && (frame = grabber.grabFrame()) != null) {
                    // 输出到本地文件
//                    if (!TextUtils.isEmpty(outputFilePath))
//                        recorder.record(frame);

                    // 输出到播放器
                    setVideoDisplay(activity, frame);

                    grabber.flush();
                }

                // stop以后停止视频流接收
                recorder.stop();
                grabber.stop();
            } catch (FrameGrabber.Exception | FrameRecorder.Exception e) {
                e.printStackTrace();

                if (activity != null && onJavaCVReadVideoStreamingListener != null)
                    activity.runOnUiThread(() -> onJavaCVReadVideoStreamingListener.onError(e.getMessage()));
            } finally {
                try {
                    grabber.stop();
                } catch (FrameGrabber.Exception e) {
                    e.printStackTrace();

                    if (activity != null && onJavaCVReadVideoStreamingListener != null)
                        activity.runOnUiThread(() -> onJavaCVReadVideoStreamingListener.onError(e.getMessage()));
                }
            }
        }).start();
    }

    /**
     * 停止播放
     */
    public void stop() {
        this.stop = true;
    }

    /**
     * 设置视频显示
     *
     * @param activity
     * @param frame
     */
    private void setVideoDisplay(Activity activity, Frame frame) {
        if (activity == null)
            return;

        AndroidFrameConverter androidFrameConverter = new AndroidFrameConverter();
        Bitmap bitmap = androidFrameConverter.convert(frame);
        activity.runOnUiThread(() -> {
            JavaCVReadVideoStreamingView.this.setBackground(new BitmapDrawable(bitmap));
        });
    }

}
