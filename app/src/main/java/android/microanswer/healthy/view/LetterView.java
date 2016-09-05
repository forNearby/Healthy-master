package android.microanswer.healthy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.microanswer.healthy.R;
import android.microanswer.healthy.tools.BaseTools;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * 0---------------------
 * 该类不能独立运行，依赖于自定义属性。属性内容如下：
 *
 *<declare-styleable name="LetterView">
 *<attr name="text_size" format="dimension"/><!-- 文本大小 -->
 *<attr name="text_color" format="color"/><!-- 文本颜色 -->
 *<attr name="text_current_color" format="color"/><!-- 当前选中文本颜色 -->
 *<attr name="under_line" format="boolean"/> <!-- 是否下划线 -->
 *<attr name="hint_text_size" format="dimension"/><!-- 提示文本大小 -->
 *<attr name="hint_text_color" format="color"/><!-- 提示文本颜色 -->
 *<attr name="hint_background" format="color|reference"/><!-- 提示背景 -->
 *</declare-styleable>
 * 以上属性应写在自定义属性xml文件里
 *这样，在使用该View的时候，可以直接在xml里面设置想要的效果
 *
 *
 * 0---------------------
 * 字母索引自定义View
 * 由 Micro 创建于 2016/7/21.
 */

public class LetterView extends View {

    /**
     * 显示到view的字母
     */
    private char letters[] = {'*',
            'A', 'B', 'C',
            'D', 'E', 'F',
            'G', 'H', 'I',
            'J', 'K', 'L',
            'M', 'N', 'O',
            'P', 'Q', 'R',
            'S', 'T', 'U',
            'V', 'W', 'X',
            'Y', 'Z', '#'};

    private Paint paint;
    private Rect letterRect;
    private TextView popwtv;
    private int currentindex;
    private int currentcolor = Color.BLACK;
    private WindowManager.LayoutParams layoutParams;
    private int scW, scH;//屏幕宽高

    private WindowManager wm;

    public LetterView(Context context) {
        super(context);
        intit(context, null);
    }

    public LetterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        intit(context, attrs);
    }

    public LetterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intit(context, attrs);
    }

    private void intit(Context context, AttributeSet attrs) {
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        scH = wm.getDefaultDisplay().getHeight();
        scW = wm.getDefaultDisplay().getWidth();
        initWindowManagerLayoutManager();
        letterRect = new Rect();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LetterView);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);//消除锯齿
        paint.setStyle(Paint.Style.STROKE);
        popwtv = new TextView(context);
        popwtv.setGravity(Gravity.CENTER);
        popwtv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, -2));
        popwtv.setTextSize(BaseTools.sp2px(context, 20f));
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.LetterView_under_line:
                    paint.setUnderlineText(typedArray.getBoolean(attr, false));
                    break;
                case R.styleable.LetterView_text_size:
                    paint.setTextSize(typedArray.getDimension(attr, 12.0f));
                    break;
                case R.styleable.LetterView_text_color:
                    paint.setColor(typedArray.getColor(attr, 0x000));
                    break;
                case R.styleable.LetterView_hint_text_size:
                    popwtv.setTextSize(typedArray.getDimension(attr, BaseTools.sp2px(context, 20f)));
                    break;
                case R.styleable.LetterView_text_current_color:
                    currentcolor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.LetterView_hint_text_color:
                    popwtv.setTextColor(typedArray.getColor(attr, Color.GRAY));
                    break;
                case R.styleable.LetterView_hint_background:
                    popwtv.setBackgroundDrawable(typedArray.getDrawable(attr));
                    break;
            }
        }
        typedArray.recycle();
    }

    private void initWindowManagerLayoutManager() {
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = scW / 3;
        layoutParams.height = layoutParams.width;
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.format = PixelFormat.TRANSLUCENT;
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = 0;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        }
        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
            width = (int) paint.getTextSize() + getPaddingLeft() + getPaddingRight();
        }
        setMeasuredDimension(width, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < letters.length; i++) {
            paint.getTextBounds(letters[i] + "", 0, 1, letterRect);
            float w = letterRect.width();//字母的宽度
            float x = (getWidth() - w) / 2f;
            float h = getHeight() / (float) (letters.length);//字母的高度
            float y = (h * (i + 1f)) - ((h - letterRect.height()) / 2f) - paint.getStrokeWidth();
            int c = paint.getColor();
            if (i == currentindex) {
                paint.setColor(currentcolor);
            }
            canvas.drawText(letters[i] + "", x, y, paint);
            paint.setColor(c);
        }
    }

    private int listenerflag = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        int h = getHeight() / letters.length;//每个字母的空间高度
        int index = (Math.round(event.getY()) / h);
        if (index < letters.length && index >= 0) {
            popwtv.setText(letters[index] + "");
            currentindex = index;
            popwtv.invalidate();
            invalidate();
            if (onLetterSelectListener != null && listenerflag != index) {
                listenerflag = index;
                onLetterSelectListener.onLetterSelected(index, letters[index]);
            }
        }


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                wm.addView(popwtv, layoutParams);
                break;
            case MotionEvent.ACTION_MOVE:
                wm.updateViewLayout(popwtv, layoutParams);
                break;
            case MotionEvent.ACTION_UP:
                wm.removeView(popwtv);
                break;
        }
        return true;
    }


    private void setCurrentindex(int index) {
        this.currentindex = index;
        invalidate();
    }

    /**
     * 设置当前所处的字母
     */
    public void setCurrentLetter(char letter) {
        if (letter == '*' || (letter >= 'a' && letter <= 'z') || letter == '#') {
            letter = String.valueOf(letter).toUpperCase().charAt(0);
            for (int i = 0; i < letters.length; i++) {
                if (letters[i] == letter) {
                    setCurrentindex(i);
                    return;
                }
            }
        }
    }

    private OnLetterSelectListener onLetterSelectListener;

    public OnLetterSelectListener getOnLetterSelectListener() {
        return onLetterSelectListener;
    }

    public void setOnLetterSelectListener(OnLetterSelectListener onLetterSelectListener) {
        this.onLetterSelectListener = onLetterSelectListener;
    }

    public interface OnLetterSelectListener {
        /**
         * 某字母被选中时调用
         *
         * @param index  该字母所处的下标
         * @param letter 选中的字母
         */
        void onLetterSelected(int index, char letter);
    }
}
