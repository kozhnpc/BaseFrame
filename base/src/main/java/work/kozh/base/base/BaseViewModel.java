package work.kozh.base.base;

import java.util.ArrayList;
import java.util.List;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import work.kozh.base.utils.LogUtils;

/**
 * 基类的ViewModel 用于演示用法
 */
public class BaseViewModel extends ViewModel {

//    private MutableLiveData<String> mBaseData = new MutableLiveData<>();

    //**********************  这是使用指南  ***************************//

    //1 定义一个接收外部参数的变量 用于调起数据更新
    private MutableLiveData<String> mKeyword = new MutableLiveData<>();


    //2 经过BaseRepository转换过来的LiveData
    private LiveData<String> mRepositoryData = Transformations.switchMap(mKeyword, new Function<String, LiveData<String>>() {
        @Override
        public LiveData<String> apply(String input) {
            LogUtils.i("LiveData --> BaseRepository转换");
            return BaseRepository.getData(input);
        }
    });

    //3 在viewModel内部进行转换的liveData
    public MutableLiveData<List<String>> mData = (MutableLiveData<List<String>>) Transformations.map(mRepositoryData, new Function<String,
            List<String>>() {
        @Override
        public List<String> apply(String input) {
            LogUtils.i("LiveData --> map转换");
            ArrayList<String> strings = new ArrayList<>();
            //.....
            strings.add(input);
            return strings;
        }
    });

    //4 对外暴露的获取方法 k变化时，调用BaseRepository的方法获取数据
    public void getData(String k) {
        mKeyword.setValue(k);
    }

//    public LiveData<String> getData() {
//        //先从缓存中取出之前的值  没有则初始化
//        //这是为了防止Activity由于旋转等重建后，不会保存数据的问题
//        String value = mBaseData.getValue();
//        if (value == null) {
//            mBaseData.setValue("这是BaseViewModel的默认内容");
//        }
//        return mBaseData;
//    }

    //**********************  这是使用指南  ***************************//

    //**********************  通用变量封装 ***************************//
    //定义一个加载错误的liveData
    //如果发生错误 更新该变量，view层监听该变量，及时在界面更新UI
    public MutableLiveData<String> mError = new MutableLiveData<>();


    //定义一个空加载的liveData
    //如果发生空白加载 更新该变量，view层监听该变量，及时在界面更新UI
    public MutableLiveData<String> mEmpty = new MutableLiveData<>();
    
    //**********************  通用变量封装 ***************************//
}
