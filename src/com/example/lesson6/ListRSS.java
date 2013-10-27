package com.example.lesson6;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ListRSS extends Activity {
    private ArrayList<String> rssURL = new ArrayList<String>();
    private ArrayAdapter<String> aa = null;
    ListView listRss;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_rss);

        listRss = (ListView) findViewById(R.id.listRss);
        rssURL.add("http://bash.im/rss/");
        rssURL.add("http://lenta.ru/rss");
        aa = new ArrayAdapter<String>(this,R.layout.list_item,R.id.label,rssURL);
        listRss.setAdapter(aa);

        listRss.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View view, int index,long arg3) {
                String select = rssURL.get(index);
                MyActivity.rssURLTV.setText(select);
                close();
            }
        });

    }

    private void close(){
        super.onBackPressed();
    }
}
