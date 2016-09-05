package android.microanswer.healthy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.microanswer.healthy.R;
import android.microanswer.healthy.tools.BaseTools;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 由 Micro 创建于 2016/7/27.
 */
public class ItemView2 extends ViewGroup {
    private final String tag = "ItemView2";


    private TextView mTitle;
    //    private Paint paint;
    private GradientDrawable gradientDrawable;
    private Rect gradientDrawableBound;
    private float underlinepadding;

    private boolean underline = false;


    public ItemView2(Context context) {
        super(context);
        init(context, null);
    }

    public ItemView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        gradientDrawable = new GradientDrawable();
        gradientDrawableBound = new Rect();
        gradientDrawable.setColor(context.getResources().getColor(R.color.line_color));
//        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        underlinepadding = (BaseTools.Dp2Px(context, 8f));
//        paint.setColor(Color.GRAY);
//        paint.setStyle(Paint.Style.STROKE);
        setDrawingCacheQuality(DRAWING_CACHE_QUALITY_HIGH);
        mTitle = new TextView(context);
        mTitle.setGravity(Gravity.CENTER);
        mTitle.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(mTitle);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemView2);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = typedArray.getIndex(i);
            switch (index) {
                case R.styleable.ItemView2_item_title_padding:
                    int dimension = (int) typedArray.getDimension(index, BaseTools.Dp2Px(context, 5f));
                    mTitle.setPadding(dimension, dimension, dimension, dimension);
                    break;
                case R.styleable.ItemView2_itemtitle:
                    mTitle.setText(typedArray.getString(index));
                    break;
                case R.styleable.ItemView2_item_title_text_color:
                    mTitle.setTextColor(typedArray.getColor(index, Color.GRAY));
                    gradientDrawable.setColor(mTitle.getCurrentTextColor());
                    break;
                case R.styleable.ItemView2_under_line_enable:
                    underline = typedArray.getBoolean(index, false);
                    break;
                case R.styleable.ItemView2_under_line_padding:
                    underlinepadding = typedArray.getDimension(index, BaseTools.Dp2Px(getContext(), 8f));
                    break;
                case R.styleable.ItemView2_item_title_size:
                    mTitle.setTextSize(typedArray.getDimension(index, mTitle.getTextSize()));
                    break;
            }
        }
        typedArray.recycle();
        setClickable(true);
    }

    /**
     * 获取内容View
     *
     * @return
     */
    public <T> T getContentView(Class<T> clazz) {
        return (T) getChildAt(1);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        l = 0;
        t = 0;

        mTitle.layout(l, t, l + mTitle.getMeasuredWidth(), t + getMeasuredHeight());
        View childAt = getChildAt(1);
        if (childAt != null)
            childAt.layout(mTitle.getRight(), t, r, t + getMeasuredHeight());

        gradientDrawableBound.set((int) (l + underlinepadding), t + getMeasuredHeight() - BaseTools.Dp2Px(getContext(), 0.5f), r - (int) underlinepadding, b);
        gradientDrawable.setBounds(gradientDrawableBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int h = 0;

        View title = getChildAt(0);

        title.measure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.AT_MOST), heightMeasureSpec);

        int titleH = title.getMeasuredHeight();

        View content = getChildAt(1);
        if (content != null) {

            int widthSize = title.getMeasuredWidth();

            content.measure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec) - widthSize, MeasureSpec.AT_MOST), heightMeasureSpec);

            int contentH = content.getMeasuredHeight();


            if (titleH >= contentH) {
                h = titleH;
                content.measure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec) - widthSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY));
            } else {
                h = contentH;
                title.measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY));
            }
        }
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (underline)
            gradientDrawable.draw(canvas);
    }
}
