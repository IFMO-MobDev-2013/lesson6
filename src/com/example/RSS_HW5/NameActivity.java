package com.example.RSS_HW5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class NameActivity extends Activity {
    ArrayList<String> name;
    ArrayList<String> link;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = new ArrayList<String>();
        link = new ArrayList<String>();
        setContentView(R.layout.title);
        setChannel();
        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, name);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(NameActivity.this, ChannelActivity.class);
                intent.putExtra("name", name.get(i));
                intent.putExtra("link", link.get(i));
                startActivity(intent);
            }
        });
    }

    public void setChannel() {
        name.add("Lenta.ru: Новости");
        link.add("http://lenta.ru/rss");
        name.add("Bash.im");
        link.add("http://bash.im/rss");
        name.add("Хабрахабр");
        link.add("http://habrahabr.ru/rss/hubs/");
        name.add("BBC News");
        link.add("http://feeds.bbci.co.uk/news/world/europe/rss.xml");
        name.add("РиаНовости: события в мире");
        link.add("http://ria.ru/export/rss2/world/index.xml");
        name.add("РиаНовости: спорт");
        link.add("http://ria.ru/export/rss2/sport/index.xml");
        name.add("Stack Overflow: Android");
        link.add("http://stackoverflow.com/feeds/tag/android");
    }
}
