package ru.skipor.RssReader;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Vladimir Skipor on 11/7/13.
 * Email: vladimirskipor@gmail.com
 */
public class FeedActivity extends Activity {

        private static String stackFeed = "http://stackoverflow.com/feeds/tag/android";
    private static String lentaFeed = "http://lenta.ru/rss/articles";
private static String bashFeed = "http://bash.im/rss/";
    public static final long UPDATE_PERIOD_SECONDS = 10;
   private PendingIntent pIntent;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_feed);
        setUpdateTimer(UPDATE_PERIOD_SECONDS);
        Toast.makeText(this, "main created", Toast.LENGTH_LONG).show();
    }

    public void stackOnClick(View view) {
        openFeed(stackFeed);
    }

    private void openFeed(String rssFeed) {
        Intent intent = new Intent(this, TitlesActivity.class);
        intent.putExtra(TitlesActivity.EXTRA_MESSAGE, rssFeed);
        startActivity(intent);
    }

    public void lentaOnClick(View view) {
        openFeed(lentaFeed);
    }

    public void bashOnClick(View view) {
        openFeed(bashFeed);

    }


    private void setUpdateTimer(long periodSeconds) {
        Intent intent = new Intent(this, RSSUpdateService.class);

        pIntent = PendingIntent.getService(this, 0, intent, 0);

        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //for 30 mint 60*60*1000
        alarm.setRepeating(AlarmManager.ELAPSED_REALTIME, 0,
                1000*periodSeconds, pIntent);


    }

    @Override
    protected void onDestroy() {
//        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        alarm.cancel(pIntent);

        super.onDestroy();
    }
}