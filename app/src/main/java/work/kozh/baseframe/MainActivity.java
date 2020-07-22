package work.kozh.baseframe;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 提供基本的初始框架 loading初始化界面 网络解锁等
 * 基于 MVVM 框架
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
