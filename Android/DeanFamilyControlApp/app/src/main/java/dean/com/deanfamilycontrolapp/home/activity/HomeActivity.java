package dean.com.deanfamilycontrolapp.home.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dean.android.framework.convenient.activity.ConvenientActivity;
import com.dean.android.framework.convenient.permission.annotations.Permission;
import com.dean.android.framework.convenient.view.ContentView;

import java.util.ArrayList;
import java.util.List;

import dean.com.deanfamilycontrolapp.R;
import dean.com.deanfamilycontrolapp.databinding.ActivityHomeBinding;
import dean.com.deanfamilycontrolapp.home.adapter.HomeFunctionAdapter;
import dean.com.deanfamilycontrolapp.home.model.FunctionModel;
import dean.com.deanfamilycontrolapp.media.model.MonitorModel;

/**
 * Home Activity
 * <p>
 * Created by dean on 2018/2/4.
 */
@Permission(value = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
@ContentView(R.layout.activity_home)
public class HomeActivity extends ConvenientActivity<ActivityHomeBinding> {

    private HomeFunctionAdapter homeFunctionAdapter;
    private List<FunctionModel> functionModels;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置Title
        viewDataBinding.toolbar.setTitle("Home Control");
        setSupportActionBar(viewDataBinding.toolbar);
        // 初始化功能模块
        initFunctionModel();
    }

    /**
     * 初始化功能模块
     */
    private void initFunctionModel() {
        functionModels = new ArrayList<>();
        MonitorModel monitorModel = new MonitorModel();
        functionModels.add(monitorModel);

        homeFunctionAdapter = new HomeFunctionAdapter(this, functionModels);
        viewDataBinding.gridView.setAdapter(homeFunctionAdapter);
    }

}
