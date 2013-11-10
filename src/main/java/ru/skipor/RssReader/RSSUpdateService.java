package ru.skipor.RssReader;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import ru.skipor.RssReader.RSSFeedReader.RSSFeed;
import ru.skipor.RssReader.RSSFeedReader.RSSFeedReader;
import ru.skipor.RssReader.RSSFeedReader.RSSFeedReaderException;
import ru.skipor.RssReader.RSSFeedReader.SAXRSSReader;

/**
 * Created by Vladimir Skipor on 11/9/13.
 * Email: vladimirskipor@gmail.com
 */
public class RSSUpdateService extends IntentService {
    private static final String TAG = "RSSUpdateService";
    RSSFeedReader feedReader;


    Handler handler = new Handler(Looper.getMainLooper());

    private static final String UPDATE_MESSAGE = "RSS feeds are updated";

    private static String[] feedsLinks = {"http://stackoverflow.com/feeds/tag/android", "http://lenta.ru/rss/articles", "http://bash.im/rss/"};
    private static RSSFeed[] feeds = new RSSFeed[feedsLinks.length];

//    private static String stackFeed = "http://stackoverflow.com/feeds/tag/android";
//    private static String lentaFeed = "http://lenta.ru/rss/articles";
//    private static String bashFeed = "http://bash.im/rss/";


    public RSSUpdateService() {
        super("ru.skipor.RssReader.RSSUpdateService");
        feedReader = new SAXRSSReader();
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        for (int i = 0; i < feedsLinks.length; i++) {
            try {
                feeds[i] = feedReader.parse(feedsLinks[i]);
            } catch (RSSFeedReaderException e) {
                Log.e(TAG, "Error during update " + feedsLinks[i] + " feed", e);
            }
        }


        Log.i(TAG, UPDATE_MESSAGE);
        handler.post(new MakeToast(UPDATE_MESSAGE, Toast.LENGTH_LONG));


    }

    class MakeToast implements Runnable {
        private final String toastText;
        private final int toastDuration;

        MakeToast(String toastText, int toastDuration) {
            this.toastDuration = toastDuration;
            this.toastText = toastText;
        }

        @Override
        public void run() {
            Toast.makeText(getApplicationContext(), toastText, toastDuration).show();

        }
    }
}
