package dean.com.deanfamilycontrolapp.media.model;

import dean.com.deanfamilycontrolapp.R;
import dean.com.deanfamilycontrolapp.home.model.FunctionModel;

/**
 * 监控 类型
 * <p>
 * Created by dean on 2018/2/6.
 */
public class MonitorModel extends FunctionModel {

    /**
     * 图标资源ID
     */
    private int logoResId = R.drawable.ic_monitor;
    /**
     * 名称
     */
    private String name = "监控";

    public int getLogoResId() {
        return logoResId;
    }

    public String getName() {
        return name;
    }
}
