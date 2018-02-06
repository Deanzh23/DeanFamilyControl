package dean.com.deanfamilycontrolapp.main;

import android.content.Intent;

import com.dean.android.framework.convenient.activity.ConvenientMainActivity;
import com.dean.android.framework.convenient.file.download.listener.FileDownloadListener;
import com.dean.android.framework.convenient.version.VersionUpdate;
import com.dean.android.framework.convenient.view.ContentView;

import dean.com.deanfamilycontrolapp.R;
import dean.com.deanfamilycontrolapp.databinding.ActivityMainBinding;
import dean.com.deanfamilycontrolapp.home.activity.HomeActivity;

/**
 * 启动界面
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends ConvenientMainActivity<ActivityMainBinding> {

    @Override
    protected boolean isUseDefaultDownloadDialog() {
        return false;
    }

    @Override
    protected FileDownloadListener getFileDownloadListener() {
        return null;
    }

    @Override
    protected void showUpdateDownload(VersionUpdate versionUpdate) {
    }

    @Override
    protected void closeMainToHomeActivity() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
