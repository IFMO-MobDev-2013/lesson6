package ru.ifmo.rssreader;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;


import java.io.Serializable;
import java.util.List;

public class MyIntentService extends IntentService {

    static String key = "ru.ifmo.rssreader.RECEIVER";
    String url;

    public MyIntentService() {
        super("myIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String task = intent.getStringExtra("task");
        if (task.equals("new")) {
            url = intent.getStringExtra("url");
        }
        List<Article> articles = XMLParser.parse(URLReader.read(url));
        Intent intentResponse = new Intent();
        intentResponse.setAction(key);
        intentResponse.addCategory(Intent.CATEGORY_DEFAULT);
        intentResponse.putExtra("articles", (Serializable) articles);
        sendBroadcast(intentResponse);
    }
}