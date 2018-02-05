package com.dean.j2ee.fc;

public class Config {

    public interface Media {
        // 媒体流服务地址
        String RTMP_URL = "rtmp://192.168.0.101/live/stream";
        // 比率
        double FRAME_RATE = 20;
        // red5根路径
        String RED5_PATH = "/Users/dean/Desktop/red5-server/";
        // red5 log文件路径
        String RED5_LOG_PATH = "log/red5.log";
    }

}
