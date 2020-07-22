package work.kozh.base.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import work.kozh.base.R;
import work.kozh.base.utils.DensityUtils;

//一个最普通的选择圆角dialog


public class MyNormalCircleDialog extends Dialog {
    public TextView mTitle;//定义标题文字
    public TextView mCancel;
    public TextView mConfirm;
    public TextView mSubTitle;

    public MyNormalCircleDialog(@NonNull Context context) {
        super(context, R.style.MyCircleDialog);
        //通过LayoutInflater获取布局
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_my_normal, null);
        mTitle = (TextView) view.findViewById(R.id.title);
        mSubTitle = (TextView) view.findViewById(R.id.tv_subtitle);
        mCancel = (TextView) view.findViewById(R.id.tv_cancel);
        mConfirm = (TextView) view.findViewById(R.id.tv_confirm);
//        setContentView(view);  //设置显示的视图
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = (int) (DensityUtils.getScreenWidth(context) * 0.8);
        setContentView(view, params);

    }


    //设置标题
    public MyNormalCircleDialog setTitle(String content) {
        this.mTitle.setText(content);
        return this;
    }

    //设置消息内容
    public MyNormalCircleDialog setSubTitle(String content) {
        this.mSubTitle.setText(content);
        return this;
    }

    //取消监听
    public MyNormalCircleDialog setOnCancelListener(View.OnClickListener listener) {
        this.mCancel.setOnClickListener(listener);
        return this;
    }

    //确认监听
    public MyNormalCircleDialog setOnConfirmListener(View.OnClickListener listener) {
        this.mConfirm.setOnClickListener(listener);
        return this;
    }


}
