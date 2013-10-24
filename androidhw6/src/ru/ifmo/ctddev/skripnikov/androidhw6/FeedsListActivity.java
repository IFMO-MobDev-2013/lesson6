package ru.ifmo.ctddev.skripnikov.androidhw6;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class FeedsListActivity extends Activity {
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        listView = (ListView) findViewById(R.id.listView);
        final String[] feeds = new String[] {
                "StackOverflow", "Habrahabr", "Lenta", "Bash"
        };
        final String[] feedsUtl = new String[] {
                "http://stackoverflow.com/feeds/tag/android",
                "http://habrahabr.ru/rss/",
                "http://lenta.ru/rss",
                "http://bash.im/rss"
        };
        final String[] feedsEncoding = new String[] {
                "UTF-8", "UTF-8", "UTF-8", "windows-1251"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, feeds);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                Intent intent = new Intent(getBaseContext(), ItemsListActivity.class);
                intent.putExtra("url", feedsUtl[position]);
                intent.putExtra("encoding", feedsEncoding[position]);
                startActivity(intent);
            }
        });
    }
}
