package work.kozh.base.base;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;
import work.kozh.base.R;
import work.kozh.base.utils.LogUtils;
import work.kozh.base.widget.LoadingView;

public abstract class BaseActivity extends AppCompatActivity {

    private LoadingView loadingView;
    private BaseViewModel mBaseViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadingView = new LoadingView(getApplicationContext(), getLoadingState()) {
            @Override
            public View onLoadSuccessView() {
                return BaseActivity.this.onLoadSuccessView();
            }

            @Override
            public State onLoad() {
                return BaseActivity.this.onLoad();
            }
        };


        //加入动画效果
       /* getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        overridePendingTransition(R.transition.scale_in, R.transition.scale_out);*/

        //将LoadingView的布局显示给子类用
        setContentView(loadingView);

        loadData();

        //提取 基类的  ViewModel  liveData
        //ViewModelProviders.of(this).get(MyViewModel.class); 已被弃用
//                        mBaseViewModel = new ViewModelProvider(BaseActivity.this).get(BaseViewModel.class);
        mBaseViewModel = ViewModelProviders.of(BaseActivity.this).get(BaseViewModel.class);
        mBaseViewModel.mData.observe(BaseActivity.this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                //监听数据变化 更新UI
                LogUtils.i("BaseActivity 正在监听数据变化 更新UI");
            }
        });

//        applyPermission(); //权限申请代码示例

    }


    //设置默认动画进场效果
    //设置进入动画  从右进入，从左退出
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.transition.set_right_in, R.transition.set_left_out);
    }

    //设置退出动画  从右退出，从左进入
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.transition.set_left_in, R.transition.set_right_out);
    }

    private void applyPermission() {

        List<PermissionItem> permissionItems = new ArrayList<PermissionItem>();
        permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "写入文件", R.drawable.permission_ic_storage));
        permissionItems.add(new PermissionItem(Manifest.permission.READ_EXTERNAL_STORAGE, "读取文件", R.drawable.permission_ic_storage));
        HiPermission.create(this)
                .title("权限申请")
                .permissions(permissionItems)
                .filterColor(ResourcesCompat.getColor(getResources(), R.color.common_yellow, getTheme()))//图标的颜色
                .msg("为了您能正常使用本软件，需要以下权限：\n\n访问手机上的音乐资料，需要读取手机存储的权限；\n\n使用App内的扫码功能，需要调用相机的权限。")
                .style(R.style.PermissionDefaultBlueStyle)//设置主题
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onDeny(String permission, int position) {

                    }

                    @Override
                    public void onGuarantee(String permission, int position) {

                    }
                });

    }


    //暴露给子类的方法，用于子类主动调用LoadingView的加载网络结果的方法，只能在外界的子类去调用才能调出其他的方法，从而显示布局
    public void loadData() {
        loadingView.loadData();
    }

    //一个抽象方法，让子类自己去加载网络数据
    public abstract LoadingView.State onLoad();

    //一个抽象方法，让子类自己去实现加载成功后的界面布局
    public abstract View onLoadSuccessView();

    //一个抽象方法，让子类自己去实现，获取当前页面需要的loading界面状态码
    public abstract LoadingView.State getLoadingState();

}
