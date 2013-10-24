package com.example.rssReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created with IntelliJ IDEA.
 * User: Ruslan
 * Date: 24.10.13
 * Time: 11:36
 */
public class FeedActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedScreen);
        ListView feedList = (ListView)findViewById(R.id.listView);

        feedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent()
            }
        });
    }
}