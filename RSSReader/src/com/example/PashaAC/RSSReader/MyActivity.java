package com.example.PashaAC.RSSReader;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.*;
import android.view.WindowManager;

import java.util.ArrayList;

public class MyActivity extends Activity {
    ArrayList<String> sites = new ArrayList<String>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        sites.add("http://www.saotron.ru/news/rss.xml");
        sites.add("http://news.yandex.ru/games.rss");
        sites.add("http://www.rusconsultgroup.ru/news/rss/");
        sites.add("http://static.feed.rbc.ru/rbc/internal/rss.rbc.ru/cnews.ru/mainnews.rss");
        sites.add("http://news.yandex.ru/movies.rss");
        sites.add("http://bash.im/rss");
        sites.add("http://news.yandex.ru/computers.rss");
        sites.add("http://news.yandex.ru/world.rss");
        sites.add("http://news.yandex.ru/index.rss");
        sites.add("http://news.turizm.ru/news.rss");
        sites.add("http://news.yandex.ru/internet.rss");
        sites.add("http://www.saotron.ru/news/rss.xml");
        sites.add("http://news.yandex.ru/science.rss");
        sites.add("http://news.yandex.ru/politics.rss");
        sites.add("http://lenta.ru/rss");
        sites.add("http://news.yandex.ru/software.rss");
        sites.add("http://news.yandex.ru/sport.rss");
        sites.add("http://news.yandex.ru/business.rss");

        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,	android.R.layout.simple_list_item_1, sites);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int index, long number) {
                EditText editText = (EditText) findViewById(R.id.editText);
                char[] chars = new char[sites.get(index).length()];
                for (int i = 0; i < sites.get(index).length(); i++)
                    chars[i] = sites.get(index).charAt(i);
                editText.setText(chars, 0, sites.get(index).length());
            }
        });

        Button send = (Button)  findViewById(R.id.ok);
        send.setOnClickListener(new View.OnClickListener() {
            EditText inputText = (EditText) findViewById(R.id.editText);
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivity.this, WorkActivity.class);
                intent.putExtra("key", inputText.getText().toString());
                startActivity(intent);
            }
        });
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}

