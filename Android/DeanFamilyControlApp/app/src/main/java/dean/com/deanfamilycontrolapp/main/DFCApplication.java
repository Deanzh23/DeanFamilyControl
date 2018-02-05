package dean.com.deanfamilycontrolapp.main;

import com.dean.android.framework.convenient.application.ConvenientApplication;

/**
 * Family Control Application
 * <p>
 * Created by dean on 2018/2/4.
 */
public class DFCApplication extends ConvenientApplication {

    @Override
    protected void initConfigAndData() {
//        Vitamio.initialize(this);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String setVersionUpdateDownloadLocalPath() {
        return null;
    }

    @Override
    protected String checkVersionUrl() {
        return null;
    }
}
