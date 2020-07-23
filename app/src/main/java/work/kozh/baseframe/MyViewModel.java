package work.kozh.baseframe;

import java.util.ArrayList;
import java.util.List;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import work.kozh.base.base.BaseViewModel;
import work.kozh.base.utils.LogUtils;

/**
 * 演示如何使用 ViewModel
 */
public class MyViewModel extends BaseViewModel {
    @Override
    public MutableLiveData<String> getErrorLiveData() {
        return new MutableLiveData<String>();
    }

    @Override
    public MutableLiveData<String> getEmptyLiveData() {
        return new MutableLiveData<String>();
    }

    //1 定义一个接收外部参数的变量 用于调起数据更新
    private MutableLiveData<String> mKeyword = new MutableLiveData<>();


    //2 经过BaseRepository转换过来的LiveData  如果viewModel 不需要额外的处理 view层可以使用这些数据了
    private LiveData<String> mRepositoryData = Transformations.switchMap(mKeyword, new Function<String, LiveData<String>>() {
        @Override
        public LiveData<String> apply(String input) {
            LogUtils.i("LiveData --> MyViewModel BaseRepository转换，这是viewModel需要的数据");
            return new MyRepository(MyViewModel.this).getMyData(input);
//                    BaseRepository.getData(MyViewModel.this, input);
        }
    });

    //3 在viewModel内部进行转换的liveData 如果viewModel 需要额外的处理 view层才可以使用这些数据
    //建议在这里做一次判断，以处理错误/空白等情况
    //空白好处理 size = 0 即可   在这里就可以处理了 判断返回的数据有效性
    // 关键是错误如何来体现？
    // 使用一个static变量来绑定 发生错误时，Repository 修改该变量以达到更新UI的目的
    // BaseViewModel.mError.setValue("出现错误啦！！！");
    public MutableLiveData<List<String>> mData = (MutableLiveData<List<String>>) Transformations.map(mRepositoryData, new Function<String,
            List<String>>() {
        @Override
        public List<String> apply(String input) {
            LogUtils.i("LiveData --> map转换，这是view需要的数据");
            ArrayList<String> strings = new ArrayList<>();
            //.....
            strings.add(input);
            onEmpty();//判空
            return strings;
        }
    });

    //4 对外暴露的获取方法 k变化时，调用BaseRepository的方法获取数据
    public void getData(String k) {
        mKeyword.setValue(k);
    }


}
