package com.ifmomd.igushkin.rss_reader;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Sergey on 10/24/13.
 */
public class Receiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        AlarmManager m = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, FeedFetchingService.class);
        i.putExtra("url","http://bash.im/rss");
        PendingIntent pi = PendingIntent.getService(context,0,i,Intent.FILL_IN_DATA);
        m.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,System.currentTimeMillis(),5000000,pi);
    }
}
