package com.example.rssreader2;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.Calendar;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private ArrayList<String> feeders;
    private ArrayAdapter<String> adapter;
    private EditText editText;
    private ResultReceiver receiver;
    private static ArrayList<RSS> rss;
    private static boolean error;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        error = false;
        rss = new ArrayList<RSS>();
        editText = (EditText) findViewById(R.id.EditText);
        ListView listView = (ListView) findViewById(R.id.listview);

        if (feeders == null) {
            feeders = new ArrayList<>();
        }


        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, feeders);
        listView.setAdapter(adapter);
        final Intent i = new Intent(MyActivity.this, ItemActivity.class);

        Calendar cal = Calendar.getInstance();
        PendingIntent pintent = PendingIntent.getService(this, 0, i, 0);
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 60*1000 * 10, pintent);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                TextView textView = (TextView) itemClicked;
                String strText = textView.getText().toString();






                i.putExtra("url", strText);
                i.putStringArrayListExtra("titles", rss.get(position).getTitles());
                i.putStringArrayListExtra("descriptions", rss.get(position).getDescriptions());
                i.putExtra("descriptions", rss.get(position).getDescriptions());
                i.putExtra("state", rss.get(0).downloaded());
                i.putExtra("error", error);
                startActivity(i);
            }
        });

    }

    public void addRss(View v) {
        RSS r = new RSS();
        rss.add(0, r);
        String url = editText.getText().toString();
        feeders.add(0, url);
        adapter.notifyDataSetChanged();
        editText.setText("");

        Intent intent = new Intent(this, Service.class);
        intent.putExtra("download", url);

        intent.putExtra("receiver", receiver);
        startService(intent);
    }

    public static void setRSS(RSS r, boolean err) {
        rss.remove(0);
        r.setDownloaded();
        rss.add(0, r);
    }
}
