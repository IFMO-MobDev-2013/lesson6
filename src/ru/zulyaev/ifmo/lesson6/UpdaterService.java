package ru.zulyaev.ifmo.lesson6;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import ru.zulyaev.ifmo.lesson6.feed.Feed;
import ru.zulyaev.ifmo.lesson6.feed.FeedParser;
import ru.zulyaev.ifmo.lesson6.xml.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * @author Никита
 */
public class UpdaterService extends IntentService {
    public static final String REQUEST_INDEX = UpdaterService.class + "_REQUEST";
    public static final String FEED_MAP_INDEX = UpdaterService.class + "_FEED_MAP";
    public static final String ACTION = UpdaterService.class + "_ACTION";

    private static final long DELAY = 5000;

    private FeedParser parser = new FeedParser();
    private HttpClient client = new DefaultHttpClient();

    public UpdaterService() {
        super("Background updater");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Intent broadcast = new Intent();
        Bundle map = new Bundle();
        for (String url : intent.getExtras().getStringArrayList(REQUEST_INDEX)) {
            try {
                Feed feed = processUrl(url);
                map.putSerializable(url, feed);
            } catch (ParseException e) {
                map.putSerializable(url, new Exception(getString(R.string.error_parse)));
            } catch (IOException e) {
                map.putSerializable(url, new Exception(getString(R.string.error_net)));
            } catch (Exception e) {
                map.putSerializable(url, new Exception(getString(R.string.error_unknown)));
            }
        }
        broadcast.putExtra(FEED_MAP_INDEX, map);
        broadcast.setAction(ACTION);
        sendStickyBroadcast(broadcast);

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, DELAY, DELAY, PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
    }

    private Feed processUrl(String url) throws Exception {
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        Reader reader = new InputStreamReader(entity.getContent(), EntityUtils.getContentCharSet(entity));
        return parser.parse(reader);
    }
}
