package com.example.lesson6;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.example.lesson6.Feed;
import com.example.lesson6.RSSDownloader;

import java.util.Vector;

public class FeedService extends IntentService {

    private final String[] CHANNELS = {"http://lenta.ru/rss/", "http://stackoverflow.com/feeds/tag/android/", "http://bash.im/rss/"};
    private final String[] CHANNEL_ENCODINGS = {"utf-8", "utf-8", "CP1251"};

    public FeedService(String name) {
        super(name);
    }

    public FeedService() {
        super("default_name_");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        for (int i = 0; i < CHANNELS.length; i++) {
            Vector<Feed> answer = RSSDownloader.downloadFromURL(CHANNELS[i], CHANNEL_ENCODINGS[i]);
        }
    }
}