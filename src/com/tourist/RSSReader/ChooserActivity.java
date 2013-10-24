package com.tourist.RSSReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ChooserActivity extends Activity {

    public static final String[] channels = {"Stack Overflow: Android",
                                             "Lenta.ru: Новости",
                                             "Bash.im",
                                             "IT happens",
                                             "Они задолбали!",
                                             "Хабрахабр",
                                             "BBC News - Europe"};
    public static final String[] channelURLs = {"http://stackoverflow.com/feeds/tag/android",
                                                "http://lenta.ru/rss",
                                                "http://bash.im/rss",
                                                "http://ithappens.ru/rss",
                                                "http://zadolba.li/rss",
                                                "http://habrahabr.ru/rss/hubs/",
                                                "http://feeds.bbci.co.uk/news/world/europe/rss.xml"};

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chooser);
        ListView lvChooser = (ListView) findViewById(R.id.lvChooser);
        ArrayAdapter<String> adapterC = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, channels);
        lvChooser.setAdapter(adapterC);
        lvChooser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), MyActivity.class);
                intent.putExtra("channel", channelURLs[position]);
                startActivity(intent);
            }
        });
    }
}