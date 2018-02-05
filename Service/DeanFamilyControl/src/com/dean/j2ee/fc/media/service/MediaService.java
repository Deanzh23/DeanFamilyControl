package com.dean.j2ee.fc.media.service;

import com.dean.j2ee.fc.Config;
import com.dean.j2ee.fc.auth.db.TokenDB;
import com.dean.j2ee.framework.media.MediaUtils;
import com.dean.j2ee.framework.service.ConvenientService;
import com.dean.j2ee.framework.utils.TextUils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
            // 启动red5服务
            startRed5Service();
            // 等待red5服务开启完成
            // red5服务开启成功
            if (red5Started()) {
                System.out.println("=========================================================");
                System.out.println("RED5 Service start success.");

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
            }
            // red5服务开启失败
            else {
                System.out.println("=========================================================");
                System.out.println("RED5服务开启失败！");

                response.put("code", RESPONSE_UN_KNOW_ERROR);
                response.put("message", "RED5 Service start failure!");

                return response.toString();
            }


        } catch (InterruptedException | IOException e) {
            e.printStackTrace();

            new Thread(() -> stopRed5Service()).start();

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

    /**
     * 启动Red5服务
     *
     * @throws IOException
     * @throws InterruptedException
     */
    private void startRed5Service() throws IOException, InterruptedException {
//        File red5LogFile = new File(Config.Media.RED5_PATH + Config.Media.RED5_LOG_PATH);
//        if (red5LogFile.exists())
//            red5LogFile.delete();
//
//        List<String> cmds = new ArrayList<>();
//        cmds.add("cd " + Config.Media.RED5_PATH);
//        cmds.add("./red5.sh");
//        CommandUtils.execute(cmds);
    }

    private void stopRed5Service() {
//        List<String> cmds = new ArrayList<>();
//        cmds.add("cd " + Config.Media.RED5_PATH);
//        cmds.add("./red5-shutdown.sh ");
//        try {
//            CommandUtils.execute(cmds);
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 读取red5日志文件，判定是否成功开启red5服务
     *
     * @return
     */
    private boolean red5Started() {
//        File red5LogFile = new File(Config.Media.RED5_PATH + Config.Media.RED5_LOG_PATH);
//        if (!red5LogFile.exists())
//            return false;
//
//        FileReader fileReader = null;
//        BufferedReader reader = null;
//        try {
//            fileReader = new FileReader(red5LogFile);
//            reader = new BufferedReader(fileReader);
//
//            System.out.println("=========================================================");
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//
//                if (line.contains("Installer service created"))
//                    return true;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (reader != null)
//                    reader.close();
//                if (fileReader != null)
//                    fileReader.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return false;

        return true;
    }

}
