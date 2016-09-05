package android.microanswer.healthy.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.microanswer.healthy.service.HealthyService;

/**
 * 音量变化接收机
 * 由 Micro 创建于 2016/8/21.
 */

public class VolumeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intenst) {
        Intent intent = new Intent();
        intent.setClass(context, HealthyService.class);
        context.startService(intent);
    }
}
