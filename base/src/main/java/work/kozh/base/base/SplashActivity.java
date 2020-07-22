package work.kozh.base.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import work.kozh.base.R;
import work.kozh.base.utils.LogUtils;
import work.kozh.base.utils.ToastUtils;
import work.kozh.base.utils.UIUtils;

/**
 * 这里进行与业务逻辑相关的全局初始化
 * 它的外面还需要一层Activity 进行与业务逻辑无关的操作 如申请权限 和一些通用的库初始化 写法与这个很相似 保证视觉上一致
 * <p>
 * 外部可以继承实现
 */
public abstract class SplashActivity extends AppCompatActivity implements View.OnClickListener {


    private FrameLayout mFlSplash;
    private ImageView mIvSplash;
    private Button mBtnSkip;

    //设定倒计时时间
    private int skipTime = 3;

    private Timer mTimer_normal;


    //初始化handler
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mBtnSkip.setVisibility(View.VISIBLE);
                    mBtnSkip.setText("跳过（" + skipTime + "）");
                    break;
                case 1:
                    LogUtils.i("splash页面结束");

//                    //判断是否需要显示引导页  默认情况下  没有记录 所以返回值是 true
//                    boolean showIndicator = SpUtils.getBoolean(SplashActivity.this, Constant.IS_SHOW_INDICATOR, true);
//                    if (showIndicator) {
//                        SpUtils.putBoolean(SplashActivity.this, Constant.IS_SHOW_INDICATOR, false);
//                        startActivity(new Intent(UIUtils.getContext(), IndicatorActivity.class));
//                    } else {
//                        startActivity(new Intent(UIUtils.getContext(), MainActivity.class));
//                    }

                    finish();
                    break;
                case 2:
                    mBtnSkip.setVisibility(View.GONE);
                    break;
            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //加入动画效果
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
        setContentView(R.layout.activity_splash);

        findView();

        initData();


    }

    private void findView() {

        mFlSplash = findViewById(R.id.fl_splash);
        mIvSplash = findViewById(R.id.iv_splash);
        mBtnSkip = findViewById(R.id.btn_skip);

        mBtnSkip.setOnClickListener(this);
        mIvSplash.setOnClickListener(this);

    }

    private void initData() {
        //暴露外子类的实现方法  加载splash图片
        loadSplashPicture();
        //暴露外子类的实现方法  用于外部进行一些初始化工作
        init();

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_splash) {
            ToastUtils.success(UIUtils.getContext(), "BiuBiu~~~");
        } else if (id == R.id.btn_skip) {
            LogUtils.i("点击了跳过");
            mHandler.removeCallbacksAndMessages(null);
            //点击直接跳过
            mHandler.sendEmptyMessage(1);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mTimer_normal = new Timer(true);
        mTimer_normal.schedule(new TimerTask() {
            @Override
            public void run() {
                skipTime--;
                mHandler.sendEmptyMessage((skipTime == -1 ? 1 : 0));
            }
        }, 1500, 1000);


    }


    @Override
    protected void onPause() {
        //取消定时任务
        if (mTimer_normal != null) {
            mTimer_normal.cancel();
        }

        super.onPause();
    }


    @Override
    public void onBackPressed() {
        //屏蔽返回键
    }

    public abstract void loadSplashPicture();

    public abstract void init();


}
