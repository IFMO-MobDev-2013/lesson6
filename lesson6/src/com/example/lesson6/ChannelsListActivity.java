package com.example.lesson6;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.lesson6.FeedService;

import java.util.Vector;

public class ChannelsListActivity extends Activity {
   

    private final String[] CHANNELS = {"http://lenta.ru/rss/", "http://stackoverflow.com/feeds/tag/android/", "http://bash.im/rss/"};
    private final String[] CHANNEL_ENCODINGS = {"utf-8", "utf-8", "CP1251"};

    private ListView channelsList;
    private ChannelAdapter channelAdapter;
    private Context context;

    public class ChannelAdapter extends BaseAdapter {

        private Vector<RSSChannel> rssChannels;

        public class RSSChannel {
            public View title;
            public String url;
            public String encoding;

            public RSSChannel(String url, String encoding) {
                TextView textView = new TextView(context);
                textView.setText(url);
                textView.setTextSize(20);
                title = textView;
                this.url = url;
                this.encoding = encoding;
            }
        }

        public ChannelAdapter() {
            rssChannels = new Vector<RSSChannel>();
        }

        public void addChannel(String channel, String encoding) {
            rssChannels.add(new RSSChannel(channel, encoding));
        }

        public String getChannelEncoding(int position) {
            return rssChannels.get(position).encoding;
        }

        public String getChannelURL(int position) {
            return rssChannels.get(position).url;
        }

        @Override
        public int getCount() {
            return rssChannels.size();
        }

        @Override
        public Object getItem(int position) {
            return rssChannels.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return rssChannels.get(position).title;
        }
    }

    private void onChannelClick(int position) {
        Intent intent = new Intent(this, FeedListActivity.class);
        intent.putExtra("channel", channelAdapter.getChannelURL(position));
        intent.putExtra("encoding", channelAdapter.getChannelEncoding(position));
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channels);
        context = this;
        channelAdapter = new ChannelAdapter();
        for (int i = 0; i < CHANNELS.length; i++) {
            channelAdapter.addChannel(CHANNELS[i], CHANNEL_ENCODINGS[i]);
        }
        channelsList = (ListView) findViewById(R.id.channelList);
        channelsList.setAdapter(channelAdapter);
        channelsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onChannelClick(position);
            }
        });
        Intent intent = new Intent(this, FeedService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
        AlarmManager manager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME, 0, 60000, pendingIntent);
    }
}