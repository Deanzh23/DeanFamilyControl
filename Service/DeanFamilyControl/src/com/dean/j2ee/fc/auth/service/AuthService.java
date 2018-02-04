package com.dean.j2ee.fc.auth.service;

import com.dean.j2ee.fc.auth.db.AuthDB;
import com.dean.j2ee.fc.auth.model.AuthEntity;
import com.dean.j2ee.framework.command.CommandUtils;
import com.dean.j2ee.framework.service.ConvenientService;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService extends ConvenientService {

    @Autowired
    private AuthDB authDB;

    /**
     * 用户认证登陆
     *
     * @param username
     * @param password
     * @return
     */
    public Object login(String username, String password) {
        AuthEntity authEntity = authDB.find(username, password);

        if (authEntity != null) {
            // 更新一下用户认证登陆日期时间
            authEntity.setLoginDateTime(System.currentTimeMillis());
            authDB.saveOrUpdate(authEntity);
        }

        return getResponseJSON(authEntity != null ? RESPONSE_SUCCESS : RESPONSE_PARAMETER_ERROR);
    }

    /**
     * 调试command
     */
    private void cmdTest() {
        try {
            List<String> command = new ArrayList<>();
            command.add("cd /Users/dean/");
            command.add("ls");
//            command.add("mkdir /Users/dean/java/");
//            command.add("ls");

            // 打开red5流媒体服务
//            command.add("cd /Users/dean/Desktop/red5-server/");
//            command.add("./red5.sh");

            CommandUtils.execute(command);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试JavaVC
     */
    private void testJavaVC() {
        try {
            OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
            // 开始获取摄像头数据
            grabber.start();
            // 新建一个窗口
            CanvasFrame canvas = new CanvasFrame("Dean's Family Control Camera");
            canvas.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            canvas.setAlwaysOnTop(true);

            while (true) {
                //窗口是否关闭
                if (!canvas.isDisplayable()) {
                    //停止抓取
                    grabber.stop();
                    //退出
                    System.exit(2);
                }
                // 获取摄像头图像并放到窗口上显示， 这里的Frame frame=grabber.grab(); frame是一帧视频图像
                canvas.showImage(grabber.grab());
                // 50毫秒刷新一次图像
                Thread.sleep(50);
            }
        } catch (FrameGrabber.Exception | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
