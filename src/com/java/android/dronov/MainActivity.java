package com.java.android.dronov;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private ArrayAdapter<String> adapter;
    private ArrayList<String> rss = new ArrayList<String>();
    private ListView listView = null;
    private EditText text;
    private Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_rss);
        listView = (ListView) findViewById(R.id.listView);
        button = (Button) findViewById(R.id.button);
        text = (EditText) findViewById(R.id.editText);

        rss.add("http://stackoverflow.com/feeds/tag/android");
        rss.add("http://news.yandex.ru/index.rss");
        rss.add("http://bash.im/rss");
        rss.add("http://lenta.ru/rss");
        adapter = new ArrayAdapter<String>(this, R.layout.view_layout, rss);
        listView.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    String url = text.getText().toString();
                    text.setText("");
                    rss.add(url);
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String url = rss.get(i);
                Intent intent = new Intent(view.getContext(), ListRssActivity.class);
                intent.putExtra("link", url);
                startActivity(intent);
            }
        });
    }

}
