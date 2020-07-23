package work.kozh.baseframe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import work.kozh.base.base.BaseRepository;
import work.kozh.base.base.BaseViewModel;
import work.kozh.base.utils.LogUtils;

public class MainRepository extends BaseRepository {

    public MainRepository(BaseViewModel viewModel) {
        super(viewModel);
    }

    public  LiveData<String> getMainData(String keywords) {
        LogUtils.i("LiveData --> MainRepository获取数据");
        final MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue("MainRepository获取数据");
        onError("出现错误啦！！！");
        return mutableLiveData;
    }

}
