package android.microanswer.healthy.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.microanswer.healthy.service.HealthyService;
import android.util.Log;

/**
 * 由 Micro 创建于 2016/8/21.
 */

public class HealthyKillReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("HealthyKillReceiver", "接收到服务被关闭广播");
        Intent inteant = new Intent();
        inteant.setClass(context, HealthyService.class);
        context.startService(inteant);
    }
}
