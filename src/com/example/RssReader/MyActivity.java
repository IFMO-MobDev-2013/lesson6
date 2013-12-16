package com.example.RssReader;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyActivity extends Activity {

    ListView listView;
    public String[] sums;
    public String[] authors;
    public String[] times;
    public String[] news;
    public String stream;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        stream = getIntent().getStringExtra("str");
        String rssUrl = getIntent().getStringExtra("link");
        Intent bb = new Intent(this, MyIntentService.class);
        bb.putExtra("stream", stream);
        bb.putExtra("url", rssUrl);
        startService(bb);

        myBroadCast mybroad = new myBroadCast();
        IntentFilter intentFilter = new IntentFilter("downloadResponse");
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(mybroad, intentFilter);

    }

    private void listInsert() {
        listView = (ListView) findViewById(R.id.listView);
        ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>(
                news.length);
        Map<String, String> m;
        for (int i = 0; i < 20; i++) {
            m = new HashMap<String, String>();
            m.put("title", news[i]);
            m.put("date", times[i]);
            data.add(m);
        }
        String[] from = {"title", "date"};
        int[] to = {R.id.title, R.id.date};
        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.item, from, to);
        View header = getLayoutInflater().inflate(R.layout.listview_header_row, null);
        if (stream.equals("stack")) header = getLayoutInflater().inflate(R.layout.stack, null);
        if (stream.equals("lenta")) header = getLayoutInflater().inflate(R.layout.lenta, null);
        if (listView.getHeaderViewsCount() == 0) listView.addHeaderView(header);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> l, View view,
                                    int position, long id) {
                Intent intent = new Intent(MyActivity.this, webweb.class);
                intent.putExtra("summary", sums[position - 1]);
                startActivity(intent);
            }
        });
    }

    public class myBroadCast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            sums = intent.getStringArrayExtra("sums");
            authors = intent.getStringArrayExtra("authors");
            news = intent.getStringArrayExtra("news");
            times = intent.getStringArrayExtra("times");
            listInsert();
        }
    }



}
