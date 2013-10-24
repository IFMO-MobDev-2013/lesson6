package com.example.lesson6;


import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.Vector;

public class ChannelsActivity extends Activity
{
    public ChannelsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channels);
        Vector<Channel> e = new Vector<Channel>();
        e.add(new Channel("http://lenta.ru/rss/"));
        e.add(new Channel("http://bash.im/rss/"));
        adapter = new ChannelsAdapter(this, e, this);
        ListView list_view = (ListView) findViewById(R.id.channelsList);
        list_view.setAdapter(adapter);

        Reloader r = new Reloader(e);
        r.start(this);
    }
}
