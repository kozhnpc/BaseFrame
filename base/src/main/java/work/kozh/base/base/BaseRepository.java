package work.kozh.base.base;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import work.kozh.base.utils.LogUtils;

/**
 * 基类的数据仓库 用于演示用法
 */
public class BaseRepository {

    public static LiveData<String> getData(String keywords) {
        LogUtils.i("LiveData --> BaseRepository获取数据");
        final MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue("haha");
        return mutableLiveData;
    }


}
