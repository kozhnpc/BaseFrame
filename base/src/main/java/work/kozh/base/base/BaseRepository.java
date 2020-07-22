package work.kozh.base.base;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import work.kozh.base.utils.LogUtils;

/**
 * 基类的数据仓库 用于演示用法
 */
public class BaseRepository {

    //**********************  这是使用指南  ***************************//
    public static LiveData<String> getData(String keywords) {
        LogUtils.i("LiveData --> BaseRepository获取数据");
        final MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue("haha");
        onError("出现错误啦！！！");
        return mutableLiveData;
    }
    //**********************  这是使用指南  ***************************//


    //**********************  通用变量封装 ***************************//
    //如果获取数据时发生错误
    public static void onError(String msg) {
        BaseViewModel.mError.setValue(msg);//在Repository中可以传递错误信息
    }

    //如果获取数据时发现为空


    //**********************  通用变量封装 ***************************//

}
