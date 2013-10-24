package com.example.lesson6;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends ListActivity
{
    /** Called when the activity is first created. */
    private ListView channelsList;
    private final String[] channels = {"lenta.ru/rss/news",
            "lenta.ru/rss/top7",
            "lenta.ru/rss/last24",
            "lenta.ru/rss/articles",
            "lenta.ru/rss/columns",
            "bash.im/rss"};
    private ArrayAdapter<String> feedAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);

        Intent startServiceIntent = new Intent(this, UpdateArticles.class);
        //this.startService(startServiceIntent);
        //Toast.makeText(this, "Service started", 2000);
        UpdateArticles.articles = new ArrayList<>();
        UpdateArticles.descriptions = new ArrayList<>();
        for (int i = 0; i < channels.length; i++) {
            UpdateArticles.articles.add(new ArrayList<String>());
            UpdateArticles.descriptions.add(new ArrayList<String>());
        }
        startService(startServiceIntent);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, PendingIntent.getService(this, 0, startServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT) );
        channelsList = (ListView)findViewById(R.id.channelsList);
        feedAdapter = new ArrayAdapter<>(this, R.layout.channelentry, channels);
        //channelsList.setAdapter(feedAdapter);
        setListAdapter(feedAdapter);
    }
    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        Intent myintent = new Intent(this, FeedActivity.class);
        myintent.putExtra("Index", position);
        startActivity(myintent);

    }
}
