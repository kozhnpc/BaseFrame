package work.kozh.base.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * 去除左右滑动时的动画效果
 *
 * @author KOZH
 * @time 2020/1/19 10:34
 */
public class NoAnimatorViewPager extends ViewPager {
    public NoAnimatorViewPager(@NonNull Context context) {
        super(context);
    }

    public NoAnimatorViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);
    }
}
