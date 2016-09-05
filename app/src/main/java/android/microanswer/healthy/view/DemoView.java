package android.microanswer.healthy.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * 由 Micro 创建于 2016/6/28.
 */
@Deprecated
public class DemoView extends View {
    private int widthMode = 0, widthSize, heightMode, heightSize;

    private ViewGroup vgl;
    private Paint paint;

    public DemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DemoView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setTextSize(30);
        paint.setColor(Color.BLACK);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        widthMode = MeasureSpec.getMode(widthMeasureSpec);
        widthSize = MeasureSpec.getSize(widthMeasureSpec);
        heightMode = MeasureSpec.getMode(heightMeasureSpec);
        heightSize = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(widthSize, heightSize);
    }
    //Error:Unknown host '这是在主机名解析时通常出现的暂时错误，它意味着本地服务器没有从权威服务器上收到响应。'. You may need to adjust the proxy settings in Gradle.
    //<a href="toggle.offline.mode">Enable Gradle 'offline mode' and sync project</a><br><a href="https://docs.gradle.org/current/userguide/userguide_single.html#sec:accessing_the_web_via_a_proxy">Learn about configuring HTTP proxies in Gradle</a>

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        canvas.drawLine();/






        String res1 = widthMode == MeasureSpec.AT_MOST ? "AT_MOST" : widthMode == MeasureSpec.EXACTLY ? "EXACTLY" : "UNSPECIFIED";
        String res2 = heightMode == MeasureSpec.AT_MOST ? "AT_MOST" : widthMode == MeasureSpec.EXACTLY ? "EXACTLY" : "UNSPECIFIED";
        res1="宽测量模式：" + res1+",参考尺寸："+widthSize;
        canvas.drawText(res1, 20, 40, paint);

//        canvas.drawLines();

        Rect rect = new Rect();
        paint.getTextBounds(res1,0,res1.length(),rect);
        canvas.drawLine(20,40,20+rect.width(),40,paint);
        res2="高测量模式：" + res2+",参考尺寸："+heightSize;
        canvas.drawText(res2, 20, 80, paint);

    }
}
