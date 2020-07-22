package work.kozh.base.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import androidx.multidex.MultiDex;
import work.kozh.base.utils.CrashUtils;
import work.kozh.base.utils.FileUtils;
import work.kozh.base.utils.LogUtils;


/**
 * Created by 00115702 on 2018/12/12.
 */

public class BaseApplication extends Application {

    public static Context ctx;
    public static int mainThreadId;
    private static Handler handler;
    public static int count = 0;

    //标记  判断当前界面是否是应用界面  通过进出+-的方式来    public static int count = 0;
    public static List<Activity> activities = new ArrayList<>();
    public static int ActivityCount = 0;

    private static BaseApplication sInstance;
    private SensorManager mSensorManager;
    //    private AccelerometerSensorEventListener mListener;

    //  ARouter 调试开关
    private boolean isDebugARouter = true;

    private long mStartTime;


    /**
     * 获取Application对象
     *
     * @return
     */
    public static synchronized BaseApplication getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }


    @Override
    public void onCreate() {
        super.onCreate();

        mStartTime = System.currentTimeMillis();
        LogUtils.i("Application开始启动：" + mStartTime);

        sInstance = this;


        //获取全局变量   用这个 context可以避免内存泄漏  尽量使用
        ctx = getApplicationContext();

        mainThreadId = android.os.Process.myTid();
        handler = new Handler();

        //初始化CrashUtils
        CrashUtils.CrashCat.getInstance(this, FileUtils.getAppDir(), "crash.txt").start();

        //为了兼容，处理过多的方法数
        MultiDex.install(this);

        //设置监听 判断当前界面是否是应用界面
        registerActivityListener();

        long endTime = System.currentTimeMillis();
        LogUtils.i("Application启动完毕，共耗时：" + (endTime - mStartTime) + " ms");

    }


  /*  private void registerAccelerometerSensor() {
        //初始化传感器  并注册监听
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mListener = new AccelerometerSensorEventListener();
        mSensorManager.registerListener(mListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        LogUtils.i(">>>>>>>>>>>>>>>>>>> 注册传感器监听");

    }*/


    private void registerActivityListener() {

        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                //activity开始创建时，加入到缓存中，记录其数量+1
                activities.add(activity);
                ActivityCount++;
            }

            @Override
            public void onActivityStarted(Activity activity) {
                if (count == 0) { //后台切换到前台
                    LogUtils.i(">>>>>>>>>>>>>>>>>>>App切到前台");
                }
                count++;
            }

            @Override
            public void onActivityResumed(Activity activity) {


            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                count--;
                if (count == 0) {
                    LogUtils.i(">>>>>>>>>>>>>>>>>>>App切到后台");
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                ActivityCount--;
                activities.remove(activity);
                if (ActivityCount == 0) {
                    LogUtils.i(">>>>>>>>>>>>>>>>>>>APP 关闭");
                    //注销 传感器 监听
//                    mSensorManager.unregisterListener(mListener);
//                    LogUtils.i(">>>>>>>>>>>>>>>>>>>APP 关闭 注销传感器监听");
                }
            }
        });
    }



    public static Context getCtx() {
        return ctx;
    }

    public static int getMainThreadId() {
        return mainThreadId;
    }


    public static Handler getHandler() {
        return handler;
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}


