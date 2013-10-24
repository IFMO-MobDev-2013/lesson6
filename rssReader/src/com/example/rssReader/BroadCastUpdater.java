package com.example.rssReader;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created with IntelliJ IDEA.
 * User: Руслан
 * Date: 24.10.13
 * Time: 22:06
 * To change this template use File | Settings | File Templates.
 */
public class BroadCastUpdater extends BroadcastReceiver {
    private static String boot_act = "android.intent.action.BOOT_COMPLETED";
    private String link;

    public BroadCastUpdater(String link){
        super();
        this.link = link;
    }

    public BroadCastUpdater(){
        super();
    }

    @Override
    public void onReceive(Context context, Intent broadcastIntent){
        if (broadcastIntent.getAction().equals(boot_act))
            BroadCastUpdater.this.begin(context);
        Intent intent = new Intent(context, FeedRenew.class);
        context.startService(intent);
    }

    public void begin(Context context){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, FeedRenew.class), 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), 15000, pendingIntent);
    }

}
