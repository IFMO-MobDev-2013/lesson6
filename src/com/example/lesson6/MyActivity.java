package com.example.lesson6;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

public class MyActivity extends Activity {
    public static RSSItem selectedRssItem = null;
    String feedUrl = "";
    ListView rssListView = null;
    public static ArrayList<RSSItem> rssItems = new ArrayList<RSSItem>();
    public static MyArrayAdapter myArrayAdapter;
    public static ProgressBar progressBar;
    public static EditText rssURLTV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        rssURLTV = (EditText) findViewById(R.id.rssURL);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        Button fetchRss = (Button) findViewById(R.id.fetchRss);
        Button listRss = (Button) findViewById(R.id.butListRSS);
        rssListView = (ListView) findViewById(R.id.rssListView);
        myArrayAdapter = new MyArrayAdapter(this, rssItems);
        rssListView.setAdapter(myArrayAdapter);
        //aa = new ArrayAdapter<RSSItem>(this, R.layout.list_item, R.id.label, rssItems);
        //rssListView.setAdapter(aa);
        feedUrl = rssURLTV.getText().toString();

        listRss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),ListRSS.class);
                startActivity(intent);
            }
        });

        fetchRss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedUrl = rssURLTV.getText().toString();
                //aa.notifyDataSetChanged();
                myArrayAdapter.notifyDataSetChanged();
                refreshRssList();
            }
        });

        rssListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> av, View view, int index,long arg3) {
                selectedRssItem = rssItems.get(index);
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),RSSItemDisplayer.class);
                startActivity(intent);

            }
        });


    }

    private void refreshRssList() {
        rssItems.clear();
        RSSItem.getRssItems(feedUrl);
        Intent intent = new Intent(this,MyIntentServ.class);
        startService(intent.putExtra("task","fetch").putExtra("link",feedUrl));
        //Intent intent = new Intent(this,MyReceiver.class);
    }
}
