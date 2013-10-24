package com.example.lesson6;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Vector;

public class Reloader extends BroadcastReceiver
{
    private static Vector<Channel> list;

    public Reloader()
    {
        super();
    }

    public Reloader(Vector<Channel> list)
    {
        super();
        this. list = list;
    }

    @Override
    public void onReceive(Context context, Intent broadcastIntent)
    {
        if (broadcastIntent.getAction().equalsIgnoreCase("android.intent.action.BOOT_COMPLETED"))
        {
            Reloader.this.start(context);
        }
        Intent intentService = new Intent(context, Reloader.class);
        ArrayList<String> arr = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++)
        {
            arr.add(list.get(i).url);
        }
        intentService.putExtra("URLS", arr);
        context.startService(intentService);
    }

    public void start(Context context)
    {
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Reloader.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), 3600000, pi);
    }
}
