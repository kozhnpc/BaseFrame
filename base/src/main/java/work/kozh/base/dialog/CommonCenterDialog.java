package work.kozh.base.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.lxj.xpopup.core.CenterPopupView;

import androidx.annotation.NonNull;
import work.kozh.base.R;


/**
 * 通用的居中显示的dialog
 * <p>
 * 使用方法
 * new XPopup.Builder(getActivity())
 * .asCustom(new ShowGuideDescDialog(getActivity(), "扫描结果", result))
 * .show();
 */
public class CommonCenterDialog extends CenterPopupView implements View.OnClickListener {


    public String mDesc;
    public String mTitle;

    private TextView mTvTitle;
    private TextView mTvGuideContent;
    private TextView mTvConfirm;

//    public CommonCenterDialog(@NonNull Context context) {
//        super(context);
//    }

    public CommonCenterDialog(@NonNull Context context, String title, String description) {
        super(context);
        this.mTitle = title;
        this.mDesc = description;
    }

    // 返回自定义弹窗的布局
    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_common_center;
    }

    // 执行初始化操作，比如：findView，设置点击，或者任何你弹窗内的业务逻辑
    @Override
    protected void onCreate() {
        super.onCreate();

        mTvTitle = findViewById(R.id.tv_title);
        mTvGuideContent = findViewById(R.id.tv_guide_content);
        mTvConfirm = findViewById(R.id.tv_confirm);

        mTvConfirm.setOnClickListener(this);

        mTvGuideContent.setText(mDesc);
        mTvTitle.setText(mTitle);
    }

    public void setTitle(String title) {
        if (mTitle != null) {
            this.mTvTitle.setText(title);
        }

    }

    public void setConfirmText(String confirmText) {
        if (mTvConfirm != null) {
            this.mTvConfirm.setText(confirmText);
        }
    }

    public void setConfirmTextVisibility(int visibility) {
        if (mTvConfirm != null) {
            this.mTvConfirm.setVisibility(visibility);
        }
    }

    public void setContent(String content) {
        mTvGuideContent.setText(content);
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }
}
