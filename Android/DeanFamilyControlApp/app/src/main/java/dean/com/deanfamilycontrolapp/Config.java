package dean.com.deanfamilycontrolapp;

/**
 * 配置
 * <p>
 * Created by dean on 2018/2/4.
 */
public interface Config {

    String BASE_URL = "http://192.168.0.101:8101/fc/";
    /**
     * 开启监控媒体流
     */
    String START_PHOTOGRAPH = "media/startPhotograph/";
    /**
     * 关闭监控媒体流
     */
    String STOP_PHOTOGRAPH = "media/stopPhotograph/";

}
