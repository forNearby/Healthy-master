package android.microanswer.healthy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.microanswer.healthy.R;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 这个类用于显示一个类似设置界面的一个条目
 * Created by Micro on 2016/6/20.
 */

@Deprecated
public class ItemView extends RelativeLayout {
    private View rootview;
    private TextView title, smalltitle;
    private RelativeLayout relativeLayout;
    private ViewGroup dataview;

    public ItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        rootview = View.inflate(context, R.layout.view_item, this);
        title = (TextView) rootview.findViewById(R.id.item_view_title);
        smalltitle = (TextView) rootview.findViewById(R.id.item_view_small_title);
        relativeLayout = (RelativeLayout) rootview.findViewById(R.id.item_view_dataview);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemView);
        String titlestring = typedArray.getString(R.styleable.ItemView_item_title);
        String smalltitlestring = typedArray.getString(R.styleable.ItemView_item_smallTitle);
        if (titlestring != null) {
            setTitle(titlestring);
        }
        if (smalltitlestring != null) {
            setSmalltitle(smalltitlestring);
        }
        typedArray.recycle();
        setClickable(true);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int childCount = getChildCount();
        if (childCount > 2) {//只允许该控件中除了item_view布局控件后只能有一个子控件
            return;
        }
        for (int i = 0; i < childCount; i++) {
            View cv = getChildAt(i);
            if (cv.getId() != R.id.item_view_root) {
                ViewGroup vg = (ViewGroup) cv.getParent();
                LayoutParams layoutParams = (LayoutParams) cv.getLayoutParams();
                vg.removeView(cv);
                cv.setLayoutParams(layoutParams);
                addDataView(cv);
            } else {
            }
        }


    }

    public RelativeLayout getRelativeLayout() {
        return relativeLayout;
    }

    public void addDataView(View v) {
        if (relativeLayout.getChildCount() == 0) {
            this.relativeLayout.addView(v);
        }
    }

    public View getDataView() {
        return this.relativeLayout;
    }

    public TextView getSmalltitle() {
        return smalltitle;
    }

    public void setSmalltitle(String smalltitle) {
        this.smalltitle.setVisibility(View.VISIBLE);
        this.smalltitle.setText(smalltitle);

    }

    public TextView getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public View getRootview() {
        return rootview;
    }

    public void setRootview(View rootview) {
        this.rootview = rootview;
    }
}
