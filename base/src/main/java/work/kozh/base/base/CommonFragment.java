package work.kozh.base.base;

import android.view.LayoutInflater;
import android.view.View;

import work.kozh.base.widget.LoadingView;

public abstract class CommonFragment extends BaseFragment {

    protected View mView;

    //这里默认就实现loading界面返回成功代码 直接进入页面
    //如果需要在加载页面之前获取一些数据或耗时操作，可以在子类中重写该方法
    @Override
    public LoadingView.State onLoad() {
        return LoadingView.State.STATE_SUCCESS;
    }

    //返回页面的实际布局，以及初始化的入口
    @Override
    public View onLoadSuccessView() {
        mView = LayoutInflater.from(getActivity().getBaseContext()).inflate(getLayoutId(), null);
        init();
        return mView;
    }


    //****************************  抽取到子类实现的方法 简化子类的写法 *******************

    //子类实现 初始化入口
    protected abstract void init();

    //子类实现 传入layout布局ID
    protected abstract int getLayoutId();


}



