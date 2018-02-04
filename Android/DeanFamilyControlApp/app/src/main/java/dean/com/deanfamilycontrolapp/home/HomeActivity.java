package dean.com.deanfamilycontrolapp.home;

import android.content.Intent;

import com.dean.android.framework.convenient.activity.ConvenientActivity;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.android.framework.convenient.view.OnClick;

import dean.com.deanfamilycontrolapp.R;
import dean.com.deanfamilycontrolapp.databinding.ActivityHomeBinding;
import dean.com.deanfamilycontrolapp.media.MonitorActivity;

/**
 * Home Activity
 * <p>
 * Created by dean on 2018/2/4.
 */
@ContentView(R.layout.activity_home)
public class HomeActivity extends ConvenientActivity<ActivityHomeBinding> {

    /**
     * 跳转监控界面
     */
    @OnClick(R.id.monitorTextView)
    public void toMonitor() {
        startActivity(new Intent(this, MonitorActivity.class));
    }

}
