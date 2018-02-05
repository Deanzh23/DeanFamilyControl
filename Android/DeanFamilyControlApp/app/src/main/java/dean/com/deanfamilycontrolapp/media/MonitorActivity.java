package dean.com.deanfamilycontrolapp.media;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;
import android.widget.Toast;

import com.dean.android.framework.convenient.activity.ConvenientActivity;
import com.dean.android.framework.convenient.network.http.ConvenientHttpConnection;
import com.dean.android.framework.convenient.network.http.listener.OnHttpConnectionListener;
import com.dean.android.framework.convenient.toast.ToastUtil;
import com.dean.android.framework.convenient.view.ContentView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dean.com.deanfamilycontrolapp.Config;
import dean.com.deanfamilycontrolapp.R;
import dean.com.deanfamilycontrolapp.databinding.ActivityMonitorBinding;
import dean.com.deanfamilycontrolapp.view.JavaCVReadVideoStreamingSurfaceView;
import io.vov.vitamio.Vitamio;

/**
 * 监控 Activity
 * <p>
 * Created by dean on 2018/2/4.
 */
@ContentView(R.layout.activity_monitor)
public class MonitorActivity extends ConvenientActivity<ActivityMonitorBinding> implements JavaCVReadVideoStreamingSurfaceView.OnJavaCVReadVideoStreamingListener {

    private static String VIDEO_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "JavaCVDemo.flv";

    private long startTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 全凭界面
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 保持屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Vitamio.isInitialized(getApplicationContext());

        viewDataBinding.elasticityLoadingView.startAndHideView(viewDataBinding.monitorView);
        // 发起服务器连接请求
        new Thread(() -> requestConnect()).start();
    }

    private void requestConnect() {
        ConvenientHttpConnection connection = new ConvenientHttpConnection();
        List<String> urlParams = new ArrayList<>();
        urlParams.add("1");
        connection.sendHttpPost(Config.BASE_URL + Config.START_PHOTOGRAPH, null, urlParams, (String) null, new OnHttpConnectionListener() {
            @Override
            public void onSuccess(String s) {
                MonitorActivity.this.runOnUiThread(() -> {
                    try {
                        JSONObject response = new JSONObject(s);
                        String code = response.getString("code");

                        if ("200".equals(code)) {
                            String url = response.getString("data");
//                            viewDataBinding.monitorView.play(MonitorActivity.this, url, VIDEO_FILE_PATH, 0,
//                                    MonitorActivity.this);

                            viewDataBinding.elasticityLoadingView.stopAndShowView(viewDataBinding.videoView);
                            viewDataBinding.videoView.setVideoURI(Uri.parse(url));
                            viewDataBinding.videoView.start();

                            ToastUtil.showToast(MonitorActivity.this, url, Toast.LENGTH_LONG, ToastUtil.LOCATION_MIDDLE);
                        } else {
                            String message = response.getString("message");
                            ToastUtil.showToast(MonitorActivity.this, message, Toast.LENGTH_LONG, ToastUtil.LOCATION_MIDDLE);
                            MonitorActivity.this.finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void onError(int i) {
                MonitorActivity.this.runOnUiThread(() -> {
                    ToastUtil.showToast(MonitorActivity.this, "媒体流读取失败");
                    MonitorActivity.this.finish();
                });
            }

            @Override
            public void onTokenFailure() {
            }

            @Override
            public void onEnd() {
            }
        });
    }

    @Override
    public void onConnect() {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void onPlay() {
        viewDataBinding.elasticityLoadingView.stopAndShowView(viewDataBinding.monitorView);

        long endTime = System.currentTimeMillis();
        Toast.makeText(this, "连接用时: " + (endTime - startTime) / 1000 + "s", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(String error) {
        stop();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(error);
        builder.create().show();
    }

    private void stop() {
        // 停止视频媒体流
        viewDataBinding.monitorView.stop();

        new Thread(() -> {
            List<String> urlParams = new ArrayList<>();
            urlParams.add("1");
            ConvenientHttpConnection connection = new ConvenientHttpConnection();
            connection.sendHttpPost(Config.BASE_URL + Config.STOP_PHOTOGRAPH, null, urlParams, (String) null, null);
        }).start();
    }

    @Override
    protected void onDestroy() {
        stop();

        super.onDestroy();
    }
}
