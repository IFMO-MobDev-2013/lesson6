package com.ifmomd.igushkin.rss_reader;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergey on 10/24/13.
 */
public class FeedFetchingService extends IntentService {

    public FeedFetchingService(String name) {
        super(name);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    List<RSSItem>    items    = new ArrayList<RSSItem>();
    List<RSSChannel> channels = new ArrayList<RSSChannel>();

    @Override

    protected void onHandleIntent(Intent intent) {
        new RSSFetcher(items, channels).execute(intent.getStringExtra("url"));
    }

    void onFetchFailed() {
        Log.e(getResources().getString(R.string.app_name), "Fetching feed in background have failed");
    }

    class RSSFetcher extends RSSGetter {
        List<RSSItem>    workingList;
        List<RSSChannel> channels;

        RSSFetcher(List<RSSItem> workingList, List<RSSChannel> channels) {
            this.workingList = workingList;
            this.channels = channels;
        }

        @Override
        protected void onPostExecute(List<RSSItem> rssItems) {
            super.onPostExecute(null);
            workingList.clear();
            if (items == null || items.size() == 0) {
                onFetchFailed();
            } else {
                workingList.addAll(items);
                channels.clear();
                channels.addAll(channels);
            }
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("FEED_FETCHED" /*some magic const here*/);
            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
            broadcastIntent.putExtra("items",items.toArray());
            broadcastIntent.putExtra("channels",channels.toArray());
            sendBroadcast(broadcastIntent);
        }
    }
}
