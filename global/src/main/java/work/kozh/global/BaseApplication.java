package work.kozh.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;


/**
 * Created by 00115702 on 2018/12/12.
 */

public class BaseApplication extends Application {

    public static Context ctx;
    public static int mainThreadId;
    private static Handler handler;

    private static BaseApplication sInstance;


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

        sInstance = this;

        //获取全局变量   用这个 context可以避免内存泄漏  尽量使用
        ctx = getApplicationContext();

        mainThreadId = android.os.Process.myTid();
        handler = new Handler();

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


