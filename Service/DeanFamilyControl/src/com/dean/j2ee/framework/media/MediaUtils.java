package com.dean.j2ee.framework.media;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacv.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class MediaUtils {

    /**
     * 摄像头图像抓取器
     */
    private static FrameGrabber frameGrabber;
    /**
     * 连接进来到用户
     */
    private static List<String> userStopRecord = new ArrayList<>();

    public static List<String> getUserStopRecord() {
        return userStopRecord;
    }

    /**
     * 媒体监听器
     */
    public interface OnMediaListener {

        void onSuccess();

        void onFailure();
    }

    /**
     * 获取摄像头实例抓取器实例
     *
     * @return
     * @throws FrameGrabber.Exception
     */
    private static FrameGrabber getFrameGrabberInstance() throws FrameGrabber.Exception {
        if (frameGrabber == null) {
            Loader.load(opencv_objdetect.class);
            // 本机摄像头默认是0
            frameGrabber = FrameGrabber.createDefault(0);
        }

        return frameGrabber;
    }

    /**
     * 记录摄像头数据
     *
     * @param key        开启流分支标识
     * @param outputFile 输出文件
     * @param frameRate  比率
     * @throws FrameGrabber.Exception
     */
    public static void startPhotograph(String key, String outputFile, double frameRate, OnMediaListener onMediaListener) throws FrameGrabber.Exception,
            FrameRecorder.Exception, InterruptedException {

        // ==== 如果已经开启视频媒体流 ==========================================================================================================
        if (userStopRecord != null && userStopRecord.size() > 0) {
            userStopRecord.add(key);

            if (onMediaListener != null)
                onMediaListener.onSuccess();

            return;
        }

        // ==== 如果没有开启视频媒体流 ===========================================================================================================

        userStopRecord.add(key);

        getFrameGrabberInstance();
        // 开启抓取器
        frameGrabber.start();

        // 实例化转换器
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        // 抓取一帧视频并转换为图像
        final opencv_core.IplImage[] iplImage = {converter.convert(frameGrabber.grab())};

        int width = iplImage[0].width();
        int height = iplImage[0].height();
        // 实例化录像器
        FrameRecorder frameRecorder = FrameRecorder.createDefault(outputFile, width, height);
        // 编码
        frameRecorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        // 格式封装
        frameRecorder.setFormat("flv");
        frameRecorder.setFrameRate(frameRate);

        // 开启录像器
        frameRecorder.start();

        final long[] startTime = {0};

        // 实例化图像显示窗体对象
        CanvasFrame canvasFrame = new CanvasFrame("camera", CanvasFrame.getDefaultGamma() / frameGrabber.getGamma());
        canvasFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        canvasFrame.setAlwaysOnTop(false);
        canvasFrame.setCanvasSize(width, height);

        final org.bytedeco.javacv.Frame[] frame = new org.bytedeco.javacv.Frame[1];

        if (onMediaListener != null)
            onMediaListener.onSuccess();

        new Thread(() -> {
            try {
                while ((userStopRecord != null && userStopRecord.size() > 0) && (iplImage[0] = converter.convert(frameGrabber.grab())) != null) {
                    System.out.println("当前监控在线人数:" + (userStopRecord == null ? 0 : userStopRecord.size()) + " 人");

                    frame[0] = converter.convert(iplImage[0]);
                    // 在窗体上显示
                    canvasFrame.showImage(frame[0]);

                    if (startTime[0] == 0)
                        startTime[0] = System.currentTimeMillis();
                    frameRecorder.setTimestamp(1000 * (System.currentTimeMillis() - startTime[0]));
                    frameRecorder.record(frame[0]);

//                    frameGrabber.flush();

                    Thread.sleep(10);
                }
            } catch (FrameGrabber.Exception | FrameRecorder.Exception | InterruptedException e) {
                e.printStackTrace();
                if (onMediaListener != null)
                    onMediaListener.onSuccess();
            }

            try {
                canvasFrame.dispose();
                frameRecorder.stop();
                frameRecorder.release();

                /** 目前不知道为什么调用次方法关闭摄像头抓取器会造成JVM异常崩溃 **/
//                frameGrabber.stop();
            } catch (FrameRecorder.Exception e) {
                e.printStackTrace();
                if (onMediaListener != null)
                    onMediaListener.onFailure();
            }
        }).start();
    }

}
