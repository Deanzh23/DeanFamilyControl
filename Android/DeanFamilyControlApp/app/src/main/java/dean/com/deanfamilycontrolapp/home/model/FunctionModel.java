package dean.com.deanfamilycontrolapp.home.model;

import java.io.Serializable;

/**
 * 功能 类型
 * <p>
 * Created by dean on 2018/2/6.
 */
public class FunctionModel implements Serializable {

    /**
     * 图标资源ID
     */
    private int logoResId;
    /**
     * 名称
     */
    private String name;

    public int getLogoResId() {
        return logoResId;
    }

    public String getName() {
        return name;
    }

}
