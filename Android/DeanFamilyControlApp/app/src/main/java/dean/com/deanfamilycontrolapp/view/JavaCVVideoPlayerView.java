package dean.com.deanfamilycontrolapp.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;

/**
 * JavaCV框架下的视频播放器View
 * <p>
 * Created by dean on 2018/2/7.
 */
@SuppressLint("AppCompatCustomView")
public class JavaCVVideoPlayerView extends ImageView {

    public interface OnVideoPlayerListener {

        void onConnect();

        void onStart();

        void onError(String error);
    }

    public JavaCVVideoPlayerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void start(Activity activity, String url, OnVideoPlayerListener onVideoPlayerListener) {
        final boolean[] isStart = {false};

        new Thread(() -> {
            // 开始连接
            if (activity != null && onVideoPlayerListener != null)
                activity.runOnUiThread(() -> onVideoPlayerListener.onConnect());

            // 初始化视频源
            FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(url);
            // 流媒体输出地址，分辨率（长、宽），是否录制音频（0：不录制；1：录制）
            FFmpegFrameRecorder recorder = new FFmpegFrameRecorder("", 1280, 720, 0);

            // 开始读取
            try {
                grabber.start();
                recorder.start();

                Frame frame;
                while ((frame = grabber.grab()) != null) {
                    if (!isStart[0]) {
                        isStart[0] = true;

                        if (activity != null && onVideoPlayerListener != null)
                            activity.runOnUiThread(() -> onVideoPlayerListener.onStart());
                    }

                    setImage(activity, frame);
                }

                recorder.stop();
                grabber.stop();
            } catch (FrameGrabber.Exception | FrameRecorder.Exception e) {
                e.printStackTrace();

                if (activity != null && onVideoPlayerListener != null)
                    activity.runOnUiThread(() -> onVideoPlayerListener.onError(e.getMessage()));
            }
        }).start();
    }

    private AndroidFrameConverter androidFrameConverter = new AndroidFrameConverter();

    private void setImage(Activity activity, Frame frame) {
        activity.runOnUiThread(() -> JavaCVVideoPlayerView.this.setImageBitmap(androidFrameConverter.convert(frame)));
    }

}
