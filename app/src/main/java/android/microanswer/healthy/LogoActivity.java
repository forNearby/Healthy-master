package android.microanswer.healthy;

import android.microanswer.healthy.bean.User;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Map;

/**
 * 闪屏，同时加载一些数据
 * Created by MicroAnswer on 2016/6/11.
 */
public class LogoActivity extends BaseActivity implements BaseActivity.OtherThreadTask {
    private static final int TIMER_THREAD_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        runOnOtherThread(this, TIMER_THREAD_ID);
    }

    @Override
    public synchronized void onOtherThreadRunEnd(int id, Message msgreslut) {
        super.onOtherThreadRunEnd(id, msgreslut);
        jumpTo(MainActivity.class, false);
        finish();
    }

    @Override
    public void onBackPressed() {
        //屏蔽掉返回事件
    }


    @Override
    public Map getTaskParams() {
        return null;
    }

    @Override
    public Message run(Map params) {
        long time = 2000;

        File f = getAppInternalWorkDir();
        if (f != null) {
            File userObjectFile = new File(f.getAbsolutePath() + "/" + LoginActivity.userObjectFileName);
            if (userObjectFile.exists()) {

                ObjectInputStream objectInputStream = null;

                try {
                    objectInputStream = new ObjectInputStream(new FileInputStream(userObjectFile));
                    User.setUser((User) objectInputStream.readObject());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (objectInputStream != null) {
                        try {
                            objectInputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (!TextUtils.isEmpty(User.getUser().getAccess_token())) {
                    time = 500;//如果登录信息还在，则显示logo时间缩短
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (User.getUser() != null && User.getUser().getAccount() != null) {
                                toast("欢迎 " + User.getUser().getAccount(), POSOTION_TOP);
                            } else {
                                toast("欢迎", POSOTION_TOP);
                            }
                        }
                    });
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toast("欢迎", POSOTION_TOP);
                    }
                });
            }
        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toast("欢迎", POSOTION_TOP);
                }
            });
        }
        SystemClock.sleep(time);
        return null;
    }
}
