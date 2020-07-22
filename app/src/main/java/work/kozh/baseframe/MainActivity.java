package work.kozh.baseframe;

import android.view.View;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;

import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import work.kozh.base.base.BaseViewModel;
import work.kozh.base.base.CommonActivity;
import work.kozh.base.dialog.CommonCenterDialog;
import work.kozh.base.utils.LogUtils;
import work.kozh.base.utils.ToastUtils;
import work.kozh.request.retrofit.Network;
import work.kozh.request.retrofit.utils.NetLogUtils;

/**
 * 提供基本的初始框架 loading初始化界面 网络解锁等
 * 基于 MVVM 框架
 */
public class MainActivity extends CommonActivity {

    private BaseViewModel mBaseViewModel;
    private TextView mTv;

    public void onClick(View view) {
        //测试MVVM ViewModel LiveData
        mBaseViewModel.getData("");
        CommonCenterDialog centerDialog = new CommonCenterDialog(this, "这是新的标题", "这是内容");
        new XPopup.Builder(this).asCustom(centerDialog).show();
        centerDialog.setConfirmText("确定");
        centerDialog.setConfirmTextVisibility(View.VISIBLE);

        //测试网络请求
        Network.get("http://103.71.238.176/app/cms/public/?service=App.Mov.SearchVod&key=鬼吹灯", Video.class,
                new Network.NetResult<Video>() {
                    @Override
                    public void onSuccess(Video video) {
                        NetLogUtils.i("网络结果：" + video.getData().size());
                    }

                    @Override
                    public void onError(String msg) {
                        LogUtils.i(msg);
                    }
                },
                this);

    }

    private void findView() {
        mTv = mView.findViewById(R.id.tv_title);
    }

    @Override
    protected void init() {
        findView();
        mBaseViewModel = ViewModelProviders.of(this).get(BaseViewModel.class);
        mBaseViewModel.mData.observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                //监听数据变化 更新UI
                for (String string : strings) {
                    LogUtils.i("BaseActivity 正在监听数据变化 更新UI --> " + string);
                }
                mTv.setText("数据发生变化-->" + strings.get(0));

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onError(String msg) {
        LogUtils.i("在主页检测到获取数据时发生了错误-->" + msg);
        ToastUtils.error(this, msg);
    }

    @Override
    protected void onEmpty() {

    }
}
