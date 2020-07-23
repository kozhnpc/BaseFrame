package work.kozh.baseframe;

import android.view.View;
import android.widget.TextView;

import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import work.kozh.base.base.BaseViewModel;
import work.kozh.base.base.CommonActivity;
import work.kozh.base.utils.LogUtils;
import work.kozh.base.utils.ToastUtils;
import work.kozh.base.utils.UIUtils;

/**
 * 演示如何使用 CommonActivity
 */
public class MyActivity extends CommonActivity {

    private MyViewModel mMyViewModel;
    private TextView mTv;

    @Override
    protected void init() {
        findView();
        mMyViewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        mMyViewModel.mData.observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                //监听数据变化 更新UI
                for (String string : strings) {
                    LogUtils.i("MyActivity 正在监听数据变化 更新UI --> " + string);
                }

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected BaseViewModel getViewModel() {
        return mMyViewModel;
    }

    @Override
    protected void onError(String msg) {
        ToastUtils.error(UIUtils.getContext(), "MyActivity中发现:" + msg);
        LogUtils.i("检测到获取数据时发生了错误-->" + msg);
    }

    @Override
    protected void onEmpty() {
        LogUtils.i("检测到获取数据时发现为空");
    }

    private void findView() {
        mTv = mView.findViewById(R.id.tv_title);
    }

    public void onClick(View view) {
        //测试MVVM ViewModel LiveData
        mMyViewModel.getData("");

    }

}
