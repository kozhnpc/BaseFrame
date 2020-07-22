package work.kozh.base.base;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * 基类的数据仓库 用于演示用法
 */
public class BaseRepository {

    public static LiveData<String> getData(String keywords) {
        final MutableLiveData<String> mutableLiveData = new MutableLiveData<>();

        return mutableLiveData;
    }


}
