package com.dean.j2ee.fc.media.service;

import com.dean.j2ee.fc.Config;
import com.dean.j2ee.fc.auth.db.TokenDB;
import com.dean.j2ee.framework.media.MediaUtils;
import com.dean.j2ee.framework.service.ConvenientService;
import com.dean.j2ee.framework.utils.TextUils;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 媒体服务
 */
@Service
public class MediaService extends ConvenientService {

    @Autowired
    private TokenDB tokenDB;

    /**
     * 开启摄像头
     *
     * @param token
     * @return
     */
    public Object startCamera(String token) {
        // 验证Token
        if (TextUils.isEmpty(token) || tokenDB == null || !tokenDB.adopt(token))
            return getResponseJSON(RESPONSE_TOKEN_LOSE_EFFICACY).toString();

        // TODO 添加打开摄像头模块儿代码

        return getResponseJSON(RESPONSE_SUCCESS).toString();
    }

    /**
     * 开始摄像
     *
     * @param token
     * @return
     */
    public Object startPhotograph(String token) {
        // 验证Token
        if (TextUils.isEmpty(token) || tokenDB == null || !tokenDB.adopt(token))
            return getResponseJSON(RESPONSE_TOKEN_LOSE_EFFICACY).toString();

        JSONObject response = new JSONObject();

        try {
            MediaUtils.startPhotograph(token, Config.Media.RTMP_URL, Config.Media.FRAME_RATE, new MediaUtils.OnMediaListener() {
                @Override
                public void onSuccess() {
                    System.out.println("FrameRecorder start success.");
                }

                @Override
                public void onFailure() {
                    System.out.println("FrameRecorder start failure!");
                }
            });

            response.put("code", RESPONSE_SUCCESS);
            response.put("data", Config.Media.RTMP_URL);
        } catch (FrameGrabber.Exception | FrameRecorder.Exception | InterruptedException e) {
            e.printStackTrace();

            response.put("code", RESPONSE_UN_KNOW_ERROR);
            response.put("message", e.getMessage());
        }

        return response.toString();
    }

    /**
     * 停止视频媒体流
     *
     * @param token
     * @return
     */
    public Object stopPhotograph(String token) {
        // 验证Token
        if (TextUils.isEmpty(token) || tokenDB == null || !tokenDB.adopt(token))
            return getResponseJSON(RESPONSE_TOKEN_LOSE_EFFICACY).toString();

        // 将自己的token从正在播放媒体流统计中移除，表示自己停止播放媒体流
        List<String> tokens = MediaUtils.getUserStopRecord();
        if (tokens != null && tokens.size() > 0)
            tokens.remove(token);

        return getResponseJSON(RESPONSE_SUCCESS).toString();
    }

}
