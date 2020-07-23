package work.kozh.baseframe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import work.kozh.base.base.BaseRepository;
import work.kozh.base.base.BaseViewModel;
import work.kozh.base.utils.LogUtils;

/**
 * 演示如何使用 Repository
 */
public class MyRepository extends BaseRepository {

    public MyRepository(BaseViewModel viewModel) {
        super(viewModel);
    }

    public LiveData<String> getMyData(String keywords) {
        LogUtils.i("LiveData --> MyRepository获取数据");
        final MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue("MyRepository获取数据");
        onError("出现错误啦！！！");
        return mutableLiveData;
    }

}
