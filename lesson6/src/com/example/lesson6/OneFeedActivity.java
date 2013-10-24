package com.example.lesson6;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: slavian
 * Date: 24.10.13
 * Time: 12:10
 * To change this template use File | Settings | File Templates.
 */



public class OneFeedActivity extends Activity {

    MyBroadcastReceiver myBroadcastReceiver = null;
    FeedAdapter adapter = null;
    ListView lv = null;
    ArrayList<FeedItem> feed = null;
    AlarmManager am = null;

    public static final String KEY_TITLE = "title";
    public static final String KEY_DESC = "description";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.one_feed);

        lv = (ListView) findViewById(R.id.feed);
        lv.setEmptyView((View) findViewById(R.layout.empty));
        myBroadcastReceiver = new MyBroadcastReceiver();

        am = (AlarmManager) getSystemService(ALARM_SERVICE);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(OneFeedActivity.this, ShowActivity.class);
                FeedItem fi = adapter.feed.get(i);
                intent.putExtra(KEY_TITLE, fi.getTitle());
                intent.putExtra(KEY_DESC, fi.getDesc());

                startActivity(intent);
            }
        });

        Intent intentMyIntentService = new Intent(this, MyIntentService.class);
        String s = getIntent().getStringExtra(MyActivity.QUERY);
        //startService(intentMyIntentService.putExtra(MyActivity.QUERY, getIntent().getStringExtra(MyActivity.QUERY)));


        IntentFilter intentFilter = new IntentFilter(MyIntentService.ACTION_MyIntentService);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(myBroadcastReceiver, intentFilter);

        start(s);
    }

    void start(String q)
    {
        Intent intentMyIntentService = new Intent(this, MyIntentService.class);
        intentMyIntentService.putExtra(MyActivity.QUERY, q);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, intentMyIntentService, 0);

        am.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 300, 5000, pIntent);

        startService(intentMyIntentService);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(myBroadcastReceiver);
    }


    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String xml = intent.getStringExtra(MyIntentService.KEY_DATA);

            Toast.makeText(OneFeedActivity.this, "updated successfully", Toast.LENGTH_SHORT).show();
            //feed = intent.getParcelableArrayListExtra(MyIntentService.KEY_LIST);

            try {
                SAXXMLParser sxp = new SAXXMLParser();
                feed = sxp.parse(xml);

            } catch (Exception e) {   // TODO: check what the exceptions i should catch
                e.printStackTrace();


                return;
            }


            adapter = new FeedAdapter(OneFeedActivity.this, feed);
            lv.setAdapter(adapter);
        }
    }




}
