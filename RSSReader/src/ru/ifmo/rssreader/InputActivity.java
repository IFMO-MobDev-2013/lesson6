package ru.ifmo.rssreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class InputActivity extends Activity {

    Activity a = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        String[] feeds = {"lenta.ru", "bash.im"};
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, feeds);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                Intent intent = new Intent(a, ShowTitlesActivity.class);
                intent.putExtra("url", position == 0 ? "http://lenta.ru/rss" : "http://bash.im/rss");
                startActivity(intent);
            }

        });
        listView.setAdapter(adapter);
    }
}
