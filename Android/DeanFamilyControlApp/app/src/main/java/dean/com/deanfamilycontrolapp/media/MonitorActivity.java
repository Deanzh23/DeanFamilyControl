package dean.com.deanfamilycontrolapp.media;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import dean.com.deanfamilycontrolapp.R;
import dean.com.deanfamilycontrolapp.view.JavaCVReadVideoStreamingView;

/**
 * 监控 Activity
 * <p>
 * Created by dean on 2018/2/4.
 */
public class MonitorActivity extends AppCompatActivity implements JavaCVReadVideoStreamingView.OnJavaCVReadVideoStreamingListener {

    private static String RTMP_URL = "rtmp://192.168.0.101/live/stream";
    private static String VIDEO_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "JavaCVDemo.flv";

    private JavaCVReadVideoStreamingView monitorView;
    private ProgressDialog progressDialog;

    private long startTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        monitorView = findViewById(R.id.monitorView);
        monitorView.play(this, RTMP_URL, VIDEO_FILE_PATH, 0, this);
    }


    @Override
    public void onConnect() {
        startTime = System.currentTimeMillis();

        if (progressDialog == null)
            progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在连接...");
        progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "取消", (dialog, which) -> {
            // 停止连接视频媒体流
            monitorView.stop();
            // 关闭提示框
            progressDialog.dismiss();
            // 关闭Activity
            MonitorActivity.this.finish();
        });
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void onPlay() {
        if (progressDialog != null)
            progressDialog.dismiss();

        long endTime = System.currentTimeMillis();
        Toast.makeText(this, "连接用时: " + (endTime - startTime) / 1000 + "s", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String error) {
        // 停止视频媒体流
        monitorView.stop();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(error);
        builder.create().show();
    }

    @Override
    protected void onDestroy() {
        // 停止视频媒体流
        if (monitorView != null)
            monitorView.stop();

        super.onDestroy();
    }
}
