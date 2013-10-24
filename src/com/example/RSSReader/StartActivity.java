package com.example.RSSReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class StartActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ListView listView = (ListView) findViewById(R.id.start_list_view);

        ArrayList<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;


        map = new HashMap<String, String>();
        map.put("name", "lenta.ru");
        items.add(map);

        map = new HashMap<String, String>();
        map.put("name", "bash.im");
        items.add(map);

        SimpleAdapter adapter = new SimpleAdapter(this, items, R.layout.start_list_view,
                new String[]{"name"},
                new int[] {R.id.text});

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> av, View view, int index, long arg3) {

                String link;

                if (index == 0) {
                    link = "http://lenta.ru/rss";
                }
                else {
                    link = "http://bash.im/rss/";
                }

                Intent intent = new Intent(StartActivity.this, RSSActivity.class);
                intent.putExtra("link", link);
                startActivity(intent);
            }
        });
    }
}
