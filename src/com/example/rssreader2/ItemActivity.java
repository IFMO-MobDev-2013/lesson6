package com.example.rssreader2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class ItemActivity extends Activity {

    ArrayAdapter<String> adapter;
    ArrayList<String> items;
    ArrayList<String> descriptions;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items);

        items = getIntent().getStringArrayListExtra("titles");
        descriptions = getIntent().getStringArrayListExtra("descriptions");
        boolean downloaded = getIntent().getBooleanExtra("state", true);
        boolean error = getIntent().getBooleanExtra("error", true);
        ListView listView = (ListView) findViewById(R.id.lw);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                Intent i = new Intent(ItemActivity.this, WebActivity.class);
                i.putExtra("text", descriptions.get(position));
                i.putStringArrayListExtra("titles", items);
                i.putStringArrayListExtra("descriptions", descriptions);
                startActivity(i);
                finish();
            }
        });
    }

    private void printListOfItems() {
        adapter.notifyDataSetChanged();
    }

    public void onBackPressed() {
        Intent i = new Intent(ItemActivity.this, MyActivity.class);
        finish();
    }


}