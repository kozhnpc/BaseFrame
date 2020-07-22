package work.kozh.base.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import work.kozh.base.R;

/**
 * 预加载页面
 *
 * @author KOZH
 * @time 2020/1/19 10:47
 */
public abstract class LoadingView extends FrameLayout implements View.OnClickListener {

    private View success;

    private LinearLayout mLoadingViewStub;
    private LinearLayout mLlLoadingLoadPic;
//    private LottieAnimationView mLoadingView1;
    private LinearLayout mLlLoadingPage;
//    private LottieAnimationView mAnimationViewBox;
    private LinearLayout mEmpty;
    private LinearLayout mError;


    //记录加载页面的状态
    public enum State {
        STATE_LOADING_LOADPICS, STATE_ERROR, STATE_SUCCESS, STATE_EMPTY, STATE_LOADING_PAGE;
    }

    //监听对象
    private OnRetryListener mListener;
    //当前状态码
    public State mCurrentState = State.STATE_LOADING_LOADPICS;

    public LoadingView(@NonNull Context context, State state) {
        super(context);
        initialView(context, state);
    }

    public LoadingView(@NonNull Context context) {
        super(context);
        initialView(context);
    }

    public LoadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialView(context);
    }

    public LoadingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialView(context);
    }

    //初始化loadingview的界面
    private void initialView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.widget_loading_view, this);
        findView();
//        setOnClickListener(this);
        showLoadingView();

    }

    private void findView() {

        mLoadingViewStub = findViewById(R.id.loading_view_stub);
        mLlLoadingLoadPic = findViewById(R.id.ll_loading_loadPic);
//        mLoadingView1 = findViewById(R.id.loading_view_1);
        mLlLoadingPage = findViewById(R.id.ll_loading_page);
//        mAnimationViewBox = findViewById(R.id.animation_view_box);
        mEmpty = findViewById(R.id.empty);
        mError = findViewById(R.id.error);

    }

    private void initialView(Context context, State state) {
        LayoutInflater.from(context).inflate(R.layout.widget_loading_view, this);
        findView();
        setOnClickListener(this);
        mCurrentState = state;
        showLoadingView();

    }

    //根据状态码显示LoadingView的界面布局
    public void showLoadingView() {
        switch (mCurrentState) {
            case STATE_LOADING_LOADPICS:
                setVisibility(View.VISIBLE);
                mLlLoadingLoadPic.setVisibility(View.VISIBLE);
                mEmpty.setVisibility(View.GONE);
                mError.setVisibility(View.GONE);
                mLlLoadingPage.setVisibility(GONE);
                break;
            case STATE_LOADING_PAGE:
                setVisibility(View.VISIBLE);
                mLlLoadingPage.setVisibility(VISIBLE);
                mLlLoadingLoadPic.setVisibility(View.GONE);
                mEmpty.setVisibility(View.GONE);
                mError.setVisibility(View.GONE);
                break;

            case STATE_EMPTY:
                setVisibility(View.VISIBLE);
                mLlLoadingLoadPic.setVisibility(View.GONE);
                mEmpty.setVisibility(View.VISIBLE);
                mError.setVisibility(View.GONE);
                mLlLoadingPage.setVisibility(GONE);
                break;
            case STATE_ERROR:
                setVisibility(View.VISIBLE);
                mLlLoadingLoadPic.setVisibility(View.GONE);
                mEmpty.setVisibility(View.GONE);
                mError.setVisibility(View.VISIBLE);
                mLlLoadingPage.setVisibility(GONE);
                break;
            case STATE_SUCCESS:
                setVisibility(View.VISIBLE);
                mLlLoadingLoadPic.setVisibility(View.GONE);
                mLlLoadingPage.setVisibility(GONE);
                mEmpty.setVisibility(View.GONE);
                mError.setVisibility(View.GONE);
                //要去掉下面的判断，否则在刷新的时候，无法自动更新界面
                success = onLoadSuccessView();
                if (success != null) {
                    addView(success);
                    success.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    //用于加载网络数据，获得状态码，该方法只能由子类的acticity或fragment去调用
    public void loadData() {
        final Handler handler = new Handler();
        //访问网络，具体的方法由子类去实现
        new Thread(new Runnable() {
            @Override
            public void run() {
                mCurrentState = onLoad();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //获取到状态码后，更新界面
                        showLoadingView();
                    }
                });
//                UIUtils.runOnUIThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        //获取到状态码后，更新界面
//                        showLoadingView();
//                    }
//                });

            }
        }).start();

    }

    //用于在网络错误或无数据返回时重新刷新
    @Override
    public void onClick(View view) {
        if (mListener != null && mCurrentState == State.STATE_ERROR) {
            mListener.onRetry();
        }
    }


    //设置监听器接口
    public void setOnRetryListener(OnRetryListener onRetryListener) {
        this.mListener = onRetryListener;
    }

    //自定义接口，用于监听重新加载
    public interface OnRetryListener {
        void onRetry();
    }

    //加载成功后显示子类的页面布局
    public abstract View onLoadSuccessView();

    //子类请求网络后得到状态码，从而传给LoadingView更新布局显示
    public abstract State onLoad();


}
