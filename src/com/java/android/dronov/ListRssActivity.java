package com.java.android.dronov;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.java.android.dronov.RSS.Entry;
import com.java.android.dronov.RSS.Feed;

import java.util.ArrayList;

public class ListRssActivity extends Activity {

    public static ArrayAdapter<Entry> adapter;
    public static ArrayList<Entry> links;
    private ListView listView = null;
    private MyAlarmManager myAlarmManager;
    private IntentFilter intentFilter;
    private Intent request;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_links);
        Intent intent = getIntent();
        String link = intent.getStringExtra("link");
        listView = (ListView) findViewById(R.id.listView);
        links = new ArrayList<Entry>();
        adapter = new ArrayAdapter<Entry>(this, R.layout.view_layout, links);
        listView.setAdapter(adapter);

//        Log.d("request link", link);
        request = new Intent(this, UpdateRSS.class);
        request.putExtra("link", link);
        startService(request);

        myAlarmManager = new MyAlarmManager();
        intentFilter = new IntentFilter(UpdateRSS.key);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(myAlarmManager, intentFilter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Entry entry = links.get(i);
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), WebActivity.class);
                intent.putExtra("link", entry.getLink());
                if (entry.getDescription() != null) {
                    intent.putExtra("Content", entry.getTitle() + "<br>" + entry.getDescription());
                }
                startActivity(intent);
            }

        });
    }

    @Override
    protected void onPause() {
        unregisterReceiver(myAlarmManager);
        super.onPause();
    }

    @Override
    protected void onResume() {
        registerReceiver(myAlarmManager, intentFilter);
//        stopService(request);
        super.onResume();
    }

    public void updateFiels(Feed feed, Exception exception) {
        if (exception != null) {
            final Activity activity = this;
            new AlertDialog.Builder(activity)
                    .setMessage("It's not a RSS. Please check your URL")
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    activity.finish();
                                }
                            }).create().show();

        } else {
            ArrayList<Entry> array = feed.getArray();
            links.clear();
            for (int i = 0; i < array.size(); i++)
                links.add(array.get(i));
            adapter.notifyDataSetChanged();
        }
    }

    public class MyAlarmManager extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent broadcastIntent) {
            Feed feed = (Feed) broadcastIntent.getSerializableExtra("feed");
            Exception exception = (Exception) broadcastIntent.getSerializableExtra("exception");
            updateFiels(feed, exception);
        }
    }
}
