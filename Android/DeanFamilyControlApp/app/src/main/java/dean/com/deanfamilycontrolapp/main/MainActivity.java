package dean.com.deanfamilycontrolapp.main;

import android.Manifest;
import android.content.Intent;

import com.dean.android.framework.convenient.activity.ConvenientMainActivity;
import com.dean.android.framework.convenient.file.download.listener.FileDownloadListener;
import com.dean.android.framework.convenient.permission.annotations.Permission;
import com.dean.android.framework.convenient.version.VersionUpdate;
import com.dean.android.framework.convenient.view.ContentView;

import dean.com.deanfamilycontrolapp.R;
import dean.com.deanfamilycontrolapp.databinding.ActivityMainBinding;
import dean.com.deanfamilycontrolapp.home.HomeActivity;

@Permission(value = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
@ContentView(R.layout.activity_main)
public class MainActivity extends ConvenientMainActivity<ActivityMainBinding> {

    @Override
    protected boolean isUseDefaultDownloadDialog() {
        return true;
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
