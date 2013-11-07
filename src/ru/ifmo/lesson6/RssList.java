package ru.ifmo.lesson6;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class RssList extends Activity {
    /**
     * Called when the activity is first created.
     */

    private ArrayList<String> rssNames = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private ArrayList<String> urls = new ArrayList<String>();

    public void addSite(String name, String url){
        rssNames.add(name);
        urls.add(url);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss_list);

        ListView listView = (ListView) findViewById(R.id.rssList);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, rssNames);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                Intent intent = new Intent(RssList.this, ArticleList.class);
                intent.putExtra("XmlUrl", urls.get(position));
                startActivity(intent);
            }
        });

        addSite("lenta.ru", "http://lenta.ru/rss");
        addSite("bash.im", "http://bash.im/rss");
        addSite("stackoverflow.com", "http://stackoverflow.com/feeds/tag/android");
        addSite("Телеканал 5", "http://5-tv.ru/news/rss/");

        Updater updater = new Updater(urls);
        updater.start(RssList.this);
    }
}
