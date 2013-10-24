package ru.zulyaev.ifmo.lesson6;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import ru.zulyaev.ifmo.lesson6.feed.Entry;
import ru.zulyaev.ifmo.lesson6.feed.Feed;

import java.io.Serializable;
import java.util.List;

/**
 * @author Никита
 */
public class FeedActivity extends Activity {
    private static final IntentFilter INTENT_FILTER = new IntentFilter(UpdaterService.ACTION);
    private ArrayAdapter<String> adapter;

    private Receiver receiver;
    private Feed feed;
    private final AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(FeedActivity.this, ArticleActivity.class);
            intent.putExtra(ArticleActivity.HTML_INDEX, feed.getEntries().get(position).getDescription());
            startActivity(intent);
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.feed);

        adapter = new ArrayAdapter<String>(this, R.layout.list_item);
        ListView listView = (ListView) findViewById(R.id.feed);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onItemClickListener);

        String url = getIntent().getStringExtra(MainActivity.URL_INDEX);
        receiver = new Receiver(url);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(receiver, INTENT_FILTER);
    }

    private void setFeed(Feed feed) {
        this.feed = feed;
        List<? extends Entry> entries = feed.getEntries();
        int count = entries.size();
        String[] items = new String[count];
        for (int i = 0; i < count; ++i) {
            items[i] = entries.get(i).getTitle();
        }
        adapter.clear();
        adapter.addAll(items);
    }

    private class Receiver extends BroadcastReceiver {
        private final String url;

        private Receiver(String url) {
            this.url = url;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle data = intent.getExtras().getBundle(UpdaterService.FEED_MAP_INDEX);
            Serializable serializable = data.getSerializable(url);
            if (serializable instanceof Exception) {
                Toast.makeText(FeedActivity.this, ((Exception) serializable).getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                setFeed((Feed)serializable);
            }
        }
    }
}