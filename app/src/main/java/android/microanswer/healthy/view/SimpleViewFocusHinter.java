package android.microanswer.healthy.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.microanswer.healthy.BaseActivity;
import android.microanswer.healthy.R;
import android.microanswer.healthy.tools.BaseTools;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * 控件小提示，该类的开发初衷是为Edittext在获取焦点的时候显示一个小弹窗提示信息
 * Created by Micro on 2016/6/17.
 */

public class SimpleViewFocusHinter {
    private Context context;//上下文
    private LinearLayout root;//布局跟容器
    private LinearLayout rootLinearLayout;//控件跟容器
    private ImageView icon_iv;//提示图标
    private ImageView icon_cursor;//箭头图标
    private TextView message_tv;//提示文字

    private String message;//提示文字

    private boolean isshow = false;

    private View brotherView;

//    private WindowManager windowManager;


    private PopupWindow popupWindow;//弹出框

    public SimpleViewFocusHinter(Context context, View brotherView, int iconResId, String message) {
        this.context = context;

//        this.windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        this.root = new LinearLayout(context);
        this.root.setOrientation(LinearLayout.VERTICAL);
        this.rootLinearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
        this.root.setLayoutParams(params);
        this.rootLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        this.rootLinearLayout.setLayoutParams(params);

        this.rootLinearLayout.setBackgroundResource(R.drawable.edittext_bg);

        if (iconResId != 0) {
            icon_iv = new ImageView(context);
            icon_iv.setMaxWidth(brotherView.getWidth());
            icon_iv.setMaxHeight(brotherView.getHeight());
            icon_iv.setImageResource(iconResId);
            rootLinearLayout.addView(icon_iv);
        }

        if (message != null) {
            message_tv = new TextView(context);
            int f = BaseTools.Dp2Px(context, 2f);
            message_tv.setPadding(f * 3, f, f * 3, f);
            message_tv.setTextSize(BaseTools.sp2px(context, 3.5f));
            message_tv.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            message_tv.setText(message);
            rootLinearLayout.addView(message_tv);
        }
        root.addView(rootLinearLayout);

        LinearLayout root2 = new LinearLayout(context);
        root2.setOrientation(LinearLayout.HORIZONTAL);
        root2.setLayoutParams(params);
        LinearLayout.LayoutParams pt = new LinearLayout.LayoutParams(BaseTools.Dp2Px(context, 10), 1);
        TextView t = new TextView(context);
        t.setLayoutParams(pt);
        root2.addView(t);

        icon_cursor = new ImageView(context);
        int w = BaseTools.Dp2Px(context, 10);
        icon_cursor.setMaxWidth(w);
        icon_cursor.setMaxHeight(w);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(w, w);
        icon_cursor.setLayoutParams(p);
        icon_cursor.setPadding(0, 0, 0, BaseTools.Dp2Px(context, 5));
        icon_cursor.setScaleType(ImageView.ScaleType.FIT_XY);
        icon_cursor.setImageResource(R.mipmap.down);
        root2.addView(icon_cursor);
        root.addView(root2);

        this.brotherView = brotherView;
        popupWindow = new PopupWindow(root, brotherView.getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT, false);
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
    }

    public SimpleViewFocusHinter(Context context, View brotherView, int iconResId, int messageResId) {
        this(context, brotherView, iconResId, context.getString(messageResId));
    }

    /**
     * 设置消息类容
     *
     * @param message
     */
    public void setMessage(String message) {
        if (this.message_tv != null) {
            this.message = message;
            this.message_tv.setText(this.message);
        }
    }


    /**
     * 显示
     *
     * @return
     */
    public SimpleViewFocusHinter show() {






        if(isShowing()){
            dismiss();
            return null;
        }

        int pos[] = new int[2];
        brotherView.getLocationInWindow(pos);

//        WindowManager.LayoutParams p = new WindowManager.LayoutParams();
//        p.flags = WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH  ;
//        p.format = PixelFormat.TRANSLUCENT;
//        p.type = WindowManager.LayoutParams.TYPE_APPLICATION;
//        p.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        p.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        p.gravity = Gravity.LEFT|Gravity.TOP;
//        p.windowAnimations = android.R.style.Animation_InputMethod;
//        p.x = pos[0];
//        p.y = pos[1] - brotherView.getHeight() / 3 - BaseTools.getStatusBarHeight(context);

//        windowManager.addView(root,p);

        isshow = true;
        popupWindow.showAtLocation((View) brotherView.getParent(), Gravity.NO_GRAVITY, pos[0], pos[1] - brotherView.getHeight() / 3);
        return this;
    }

    /**
     * 取消显示
     */
    public void dismiss() {
        popupWindow.dismiss();
//        windowManager.removeView(root);
        isshow = false;
    }

    public boolean isShowing() {
        return isshow;
    }

    private static SimpleViewFocusHinter ttSima = null;








    //-------------------------------------------
    public static void dismassMaked() {
        if (ttSima != null) {
            ttSima.dismiss();
        }
        ttSima = null;
    }

    /**
     * 快捷创建一个弹窗
     *
     * @param context
     * @param broview
     * @param message
     * @return
     */
    public static SimpleViewFocusHinter makeSimpleViewFocusHinter(Context context, View broview, String message) {
        if (ttSima != null) {
            ttSima.dismiss();
            ttSima = null;
        }
        ttSima = new SimpleViewFocusHinter(context, broview, 0, message);
        return ttSima;
    }

    /**
     * 快捷创建一个弹窗
     *
     * @param context
     * @param broview
     * @param messageResId
     * @return
     */
    public static SimpleViewFocusHinter makeSimpleViewFocusHinter(Context context, View broview, int messageResId) {
        return makeSimpleViewFocusHinter(context, broview, context.getString(messageResId));
    }


}
