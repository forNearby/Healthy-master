package android.microanswer.healthy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by MicroAnswer on 2016/7/18.
 */

public class AutoMoveTextView extends TextView {
    public AutoMoveTextView(Context context) {
        super(context);
        setSingleLine();
    }

    public AutoMoveTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSingleLine();
    }

    public AutoMoveTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setSingleLine();
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
