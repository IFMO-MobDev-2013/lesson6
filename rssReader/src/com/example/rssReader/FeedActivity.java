package com.example.rssReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created with IntelliJ IDEA.
 * User: Ruslan
 * Date: 24.10.13
 * Time: 11:36
 */
public class FeedActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedscreen);
        ListView feedList = (ListView)findViewById(R.id.listView);
        Intent intent = getIntent();
        String link = intent.getStringExtra("link");
        RssFeed rssFeed = new RssFeed(link);
        ArrayList<Map<String, Object>> data = null;
        try {
             data = rssFeed.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        String[] keys = {"title", "description", "pubDate"};
        int[] layouts = {R.id.title, R.id.description, R.id.date};
        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.listnode, keys, layouts);
        feedList.setAdapter(adapter);
        final ArrayList<Map<String, Object>> fdata = data;
        feedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String link = (String)fdata.get(position).get("link");
                Intent intent = new Intent(FeedActivity.this, ContentActivity.class);
                intent.putExtra("link", link);
                startActivity(intent);
            }
        });

        BroadCastUpdater updater = new BroadCastUpdater(link);
        updater.begin(FeedActivity.this);
    }
}