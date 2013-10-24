package com.tourist.RSSReader;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

public class MyActivity extends Activity {

    String channel = null;

    ListView lvMain;
    TextView tvMain;
    ArrayAdapter<String> adapter;
    MyBroadcastReceiver mbr;
    IntentFilter intentFilter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Intent intent = getIntent();
        channel = intent.getStringExtra("channel");
        lvMain = (ListView) findViewById(R.id.lvMain);
        tvMain = (TextView) findViewById(R.id.tvMain);
        Intent newIntent = new Intent(this, MyIntentService.class);
        newIntent.putExtra("channel", channel);
        startService(newIntent);
        mbr = new MyBroadcastReceiver();
        intentFilter = new IntentFilter(MyIntentService.key);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(mbr, intentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mbr);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mbr, intentFilter);
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra("result");
            if ("good".equals(result)) {
                summaries = (ArrayList<String>) intent.getSerializableExtra("summaries");
                links = (ArrayList<String>) intent.getSerializableExtra("links");
                titles = (ArrayList<String>) intent.getSerializableExtra("titles");
                showRSS();
            } else {
                showBadLuck();
            }
        }
    }

    ArrayList<String> summaries;
    ArrayList<String> links;
    ArrayList<String> titles;

    String badLuck = "Check your Internet connection and try again!";

    public void showBadLuck() {
        tvMain.setText(badLuck);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, new ArrayList<String>());
        lvMain.setAdapter(adapter);
    }

    public void showRSS() {
        tvMain.setText("Downloaded!");
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, titles);
        lvMain.setAdapter(adapter);
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showRecord(position);
            }
        });
    }

    public void showRecord(int position) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("summary", summaries.get(position));
        intent.putExtra("link", links.get(position));
        intent.putExtra("title", titles.get(position));
        startActivity(intent);
    }

}
