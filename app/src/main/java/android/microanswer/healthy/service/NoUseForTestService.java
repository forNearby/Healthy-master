package android.microanswer.healthy.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * 由 Micro 创建于 2016/7/20.
 */

public class NoUseForTestService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Binder() {


        };
    }

}
