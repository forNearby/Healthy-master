package android.microanswer.healthy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.microanswer.healthy.bean.User;
import android.microanswer.healthy.tools.BaseTools;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;

/**
 * 该程序的基本Activity类，在类中实现了一些常用的方法
 * <br/>
 * 本程序的所有Activity都因该继承自本Activity
 * <br/>
 * 范雪蛟
 */
public abstract class BaseActivity extends AppCompatActivity {
    public final static String TYPE_LORE = "lore";
    public final static String TYPE_INFO = "info";
    public final static String TYPE_ASK = "ask";
    public final static String TYPE_BOOK="book";
    public final static String TYPE_FOOD ="food";
    public final static String TYPE_COOK = "cook";
    public static final String userObjectFileName = "user.data";

    public final static int POSOTION_TOP = 1;
    public final static int POSOTION_BOTTOM = 2;

    public static final String client_id = "1569107";
    public static final String client_secret = "b197106453b5ea7323720d343d671acb";
    public static final String redirect_uri = "http://www.tngou.net/api/oauth2/response";
    protected static final String APP_WORKDIR = "android.microanswer.healthy";

    protected int screenWidth;
    protected int screenHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
    }


    public boolean isTextEmpty(String text) {
        return TextUtils.isEmpty(text);
    }


    /**
     * 弹出一个错误提示框
     * @param message
     * @param context
     * @return
     */
    public static AlertDialog errorDialog(String message, Context context) {
        AlertDialog dialog = null;
        AlertDialog.Builder b = new AlertDialog.Builder(context);
        b.setTitle("出现错误");
        b.setIcon(android.R.drawable.ic_dialog_info);
        b.setMessage(message);
        b.setPositiveButton("确定", null);
        dialog = b.show();
        return dialog;
    }

    protected ActionBar suitToolBar(int toolbarId) {
        Toolbar toolbar = (Toolbar) findViewById(toolbarId);
        setSupportActionBar(toolbar);
        return getSupportActionBar();
    }

    /**
     * 开启ToolBar返回按钮
     */
    protected void setToolBarBackEnable() {
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeButtonEnabled(true);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            return onHomeButtonClick();
        }
        return super.onOptionsItemSelected(item);
    }

    protected boolean onHomeButtonClick() {
        return true;
    }


    /**
     * 一个没有用的方法
     *
     */
    public Object noUseMethd(Object... args){


        return args;
    }

//    @Override
//    public void setSupportActionBar(@Nullable Toolbar toolbar) {
//        super.setSupportActionBar(toolbar);//abc_ic_ab_back_mtrl_am_alpha[lib23.2.0以前]
//        final Drawable upArrow = ContextCompat.getDrawable(this,android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
//        upArrow.setColorFilter(getResources().getColor(R.color.whilte), PorterDuff.Mode.SRC_ATOP);
//        getSupportActionBar().setHomeAsUpIndicator(upArrow);
//    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    /**
     * 快速弹出一个Toast提示窗口,{短时间显示}
     *
     * @param message
     * @return 弹出的这个Toast
     */
    public Toast toast(String message, int position) {
        Toast t = new Toast(this);
        TextView tv = (TextView) View.inflate(this, R.layout.toast_view, null);
        t.setView(tv);
        tv.setText(message);
        t.setDuration(Toast.LENGTH_SHORT);
        int ox = 0, oy = BaseTools.Dp2Px(this, 90f);
        switch (position) {
            case POSOTION_BOTTOM:
                t.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, ox, oy);
                break;
            case POSOTION_TOP:
                t.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, ox, oy);
                break;
        }


        t.show();
        return t;
    }

    /**
     * 获取程序在手机里面的运行目录
     *
     * @return
     */
    public File getAppInternalWorkDir() {


        File externalStorageDirectory = Environment.getExternalStorageDirectory();

        File f = new File(externalStorageDirectory.getAbsolutePath() + "/Android/data/" + APP_WORKDIR);

        if (f.exists()) {
            return f;
        } else {
            if (f.mkdirs())
                return f;
            else return null;
        }
    }


    /**
     * 将用户信息保存到文件
     */
    public void saveUserInFile() {
        File appInternalWorkDir = getAppInternalWorkDir();
        ObjectOutputStream objectOutputStream = null;
        try {
            if (appInternalWorkDir != null) {
                objectOutputStream = new ObjectOutputStream(new FileOutputStream(appInternalWorkDir.getAbsolutePath() + "/" + userObjectFileName));
                objectOutputStream.writeObject(User.getUser());
                objectOutputStream.flush();
            } else {
                Log.i("将登录信息写入文件", "文件目录创建失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (objectOutputStream != null)
                    objectOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 快速打印一条LogCat到控制台
     */
    public void i(String message) {
        Log.i(this.getClass().getSimpleName(), message);
    }


    /**
     * 弹出一个弹窗,请追加show()方法显示出弹窗
     *
     * @param title   标题
     * @param message 菜单项目
     * @return 弹出的窗口
     */
    public AlertDialog alertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(getString(R.string.sure), null);
        return builder.create();
    }

    public AlertDialog alertDialog(String title, String message, DialogInterface.OnClickListener ok) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("确定", ok);
//        builder.setNegativeButton("取消", null);
        return builder.create();
    }


    public AlertDialog alertDialog(String title, String message, DialogInterface.OnClickListener ok,DialogInterface.OnClickListener ca) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("确定", ok);
        builder.setNegativeButton("取消", ca);
        return builder.create();
    }


    /**
     * 弹出一个菜单，请追加show()方法显示出弹窗
     *
     * @param title           标题
     * @param items           菜单项目
     * @param onClickListener 菜单项点击事件
     * @return 弹出的窗口
     */

    public AlertDialog alertMenu(String title, String[] items, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setItems(items, onClickListener);
        return builder.create();
    }


    /**
     * 跳转到指定的Activity
     *
     * @param targetActivityclass
     * @param suitHeightVersion   是否适配高版本机型
     */
    public void jumpTo(Class<? extends Activity> targetActivityclass, boolean suitHeightVersion) {
        Intent intent = new Intent();
        intent.setClass(this, targetActivityclass);
        startActivity(intent, suitHeightVersion);
    }


    /**
     * 跳转到指定的Activity,并从该Activity获得结果
     *
     * @param targetActivityclass
     * @param suitHeightVersion   是否适配高版本机型
     */
    public void jumpForResultTo(Class<? extends BaseActivity> targetActivityclass, boolean suitHeightVersion, int reqerstcode) {
        Intent intent = new Intent();
        intent.setClass(this, targetActivityclass);
        startActivityForResult(intent, reqerstcode, suitHeightVersion);
    }

    public void startActivityForResult(Intent intent, int reqerstcode, boolean suitHeightVersion) {
        if (suitHeightVersion) {
            if (Build.VERSION.SDK_INT >= 21) {
                super.startActivityForResult(intent, reqerstcode, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
                return;
            }
        }
        super.startActivityForResult(intent, reqerstcode);
    }


    /**
     * 将一个任务在非主线程中完成，即在子线程中运行
     *
     * @param otherThreadTask 要运行的任务
     * @param id              为本异步操作指定一个id ，因为这一次异步操作完成后会调用：onOtherThreadRunEnd方法
     * @return 新开的线程
     */
    public void runOnOtherThread(OtherThreadTask otherThreadTask, int id) {

        if (threadSparseArray == null) {
            threadSparseArray = new SparseArray<>();
        }

        threadSparseArray.put(threadSparseArray.size() + 1, new OtherThread(otherThreadTask, id).execute(otherThreadTask.getTaskParams()));
    }

    private SparseArray<AsyncTask> threadSparseArray;


    /**
     * 关闭所有正在进行的线程
     */
    public void shutDownAllOtherThread() {
        if (threadSparseArray != null) {
            for (int i = 0; i < threadSparseArray.size(); i++) {
                int i1 = threadSparseArray.keyAt(i);
                AsyncTask asyncTask = threadSparseArray.get(i1);
                if (asyncTask != null) {
                    asyncTask.cancel(true);
                }
            }
        }
    }

    /**
     * 开启一个新界面
     *
     * @param intent
     * @param suitHeightVersion 是否适配高版本机型
     */
    public void startActivity(Intent intent, boolean suitHeightVersion) {
        if (suitHeightVersion) {
            if (Build.VERSION.SDK_INT >= 21) {
                super.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
                return;
            }
        }
        super.startActivity(intent);
    }

    /**
     * 该方法无需手动调用，在runOnOtherThread指定的任务执行完成的时候会自动调用
     * <br/>
     * 这个方法会在ui线程中运行
     *
     * @param id        指定任务的id
     * @param msgResult 运行结果
     */
    public void onOtherThreadRunEnd(int id, Message msgResult) {
        //...
    }

    private class OtherThread extends AsyncTask<Map<Object, Object>, Void, Message> {
        private int id;//指定这一次异步操作的id
        private OtherThreadTask otherThreadTask;//指定这一次异步操作要做的事情

        private OtherThread(OtherThreadTask otherThreadTask, int id) {
            this.otherThreadTask = otherThreadTask;
            this.id = id;
        }

        @Override
        protected Message doInBackground(Map... objects) {
            i("开启一个id为" + id + " 的子线程");
            return otherThreadTask.run(objects[0]);
        }

        @Override
        protected void onPostExecute(Message msg) {
            super.onPostExecute(msg);
            i("id为" + id + " 的子线程执行完成");
            onOtherThreadRunEnd(OtherThread.this.id, msg);
            if (otherThreadTask instanceof BaseOtherThread) {
                ((BaseOtherThread) otherThreadTask).onOtherThreadRunEnd(msg);
            }
        }
    }

    /**
     * 更方便的执行其他子线程结束的时候的方法
     */
    public abstract class BaseOtherThread implements OtherThreadTask {
        /**
         * 该方法在主线程运行
         *
         * @param msg
         */
        abstract void onOtherThreadRunEnd(Message msg);
    }


    /**
     * 该类规范了本程序中的异步任务执行标准，方便的在执行异步的时候传入参数，以及执行结束时获得结果
     */
    public interface OtherThreadTask<p extends Object, o extends Object> {

        /**
         * 该方法使子线程知道要使用那些参数(在主线程中运行)
         *
         * @return
         */
        public abstract Map<p, o> getTaskParams();


        /**
         * 该方法在子线程中运行,运行时传入getTaskParams方法返回的参数
         *
         * @param params 子线程在运行的时候传入getTaskParams方法返回的参数
         * @return 返回的Message会被发送给在UI线程中运行的onOtherThreadRunEnd方法
         */
        public abstract Message run(Map<p, o> params);
    }

}
