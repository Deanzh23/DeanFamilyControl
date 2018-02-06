package dean.com.deanfamilycontrolapp.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.widget.RelativeLayout;

import com.dean.android.framework.convenient.adapter.ConvenientAdapter;

import java.util.List;

import dean.com.deanfamilycontrolapp.R;
import dean.com.deanfamilycontrolapp.databinding.AdapterHomeFunctionBinding;
import dean.com.deanfamilycontrolapp.home.model.FunctionModel;
import dean.com.deanfamilycontrolapp.media.activity.MonitorActivity;
import dean.com.deanfamilycontrolapp.media.model.MonitorModel;

/**
 * Home界面功能项 Adapter
 * <p>
 * Created by dean on 2018/2/6.
 */
public class HomeFunctionAdapter extends ConvenientAdapter<AdapterHomeFunctionBinding> {

    private Context context;
    private List<FunctionModel> functionModels;

    public HomeFunctionAdapter(Context context, List<FunctionModel> functionModels) {
        this.context = context;
        this.functionModels = functionModels;
    }

    @Override
    public int setItemLayoutId() {
        return R.layout.adapter_home_function;
    }

    @Override
    public void setItemView(AdapterHomeFunctionBinding adapterHomeFunctionBinding, int i) {
        FunctionModel functionModel = functionModels.get(i);

        // 设置图标
        adapterHomeFunctionBinding.imageView.setImageResource(functionModel.getLogoResId());
        adapterHomeFunctionBinding.imageView.post(() -> {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) adapterHomeFunctionBinding.imageView.getLayoutParams();
            layoutParams.height = layoutParams.width;
            adapterHomeFunctionBinding.imageView.setLayoutParams(layoutParams);
        });
        // 设置点击事件
        adapterHomeFunctionBinding.getRoot().setOnClickListener(v -> {
            Intent intent = new Intent();
            // 监控类型
            if (functionModel instanceof MonitorModel) {
                intent.setClass(context, MonitorActivity.class);
            }

            context.startActivity(intent);
        });
    }

    @Override
    public int getCount() {
        return functionModels == null ? 0 : functionModels.size();
    }

    @Override
    public Object getItem(int position) {
        return functionModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
