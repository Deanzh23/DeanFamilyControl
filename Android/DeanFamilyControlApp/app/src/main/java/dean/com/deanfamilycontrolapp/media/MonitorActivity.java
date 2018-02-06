package dean.com.deanfamilycontrolapp.media;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
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
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;

/**
 * 监控 Activity
 * <p>
 * Created by dean on 2018/2/4.
 */
@ContentView(R.layout.activity_monitor)
public class MonitorActivity extends ConvenientActivity<ActivityMonitorBinding> implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnInfoListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 全凭界面
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 保持屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Vitamio.isInitialized(getApplicationContext());
        viewDataBinding.elasticityLoadingView.startAndHideView(viewDataBinding.videoLayout);

        viewDataBinding.videoView.setOnBufferingUpdateListener(this);
        viewDataBinding.videoView.setOnInfoListener(this);

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
                    MonitorActivity.this.onError("连接服务器失败");
                });
            }

            @Override
            public void onTokenFailure() {
            }

            @Override
            public void onEnd() {
                MonitorActivity.this.runOnUiThread(() -> viewDataBinding.elasticityLoadingView.stopAndShowView(viewDataBinding.videoLayout));
            }
        });
    }

    /**
     * 服务器相关错误
     *
     * @param error
     */
    private void onError(String error) {
        stop();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(error);
        builder.setPositiveButton("退出", (dialog, which) -> MonitorActivity.this.finish());
        builder.setCancelable(false);
        builder.create().show();
    }

    private void stop() {
        // 停止视频媒体流
        viewDataBinding.videoView.stopPlayback();

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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int percent) {
        viewDataBinding.loadTextView.setText(percent + "%");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                if (viewDataBinding.videoView.isPlaying()) {
                    viewDataBinding.videoView.pause();
                    viewDataBinding.scheduleProgressBar.setVisibility(View.VISIBLE);
                    viewDataBinding.downloadTextView.setText("");
                    viewDataBinding.loadTextView.setText("");
                    viewDataBinding.downloadTextView.setVisibility(View.VISIBLE);
                    viewDataBinding.loadTextView.setVisibility(View.VISIBLE);
                }
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                viewDataBinding.videoView.start();
                viewDataBinding.scheduleProgressBar.setVisibility(View.GONE);
                viewDataBinding.downloadTextView.setVisibility(View.GONE);
                viewDataBinding.loadTextView.setVisibility(View.GONE);
                break;
            case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                viewDataBinding.downloadTextView.setText(extra + "kb/s" + "  ");
                break;
        }

        return true;
    }
}
