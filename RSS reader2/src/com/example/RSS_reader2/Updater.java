package com.example.RSS_reader2;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Updater extends IntentService {
    ArticleDataBase articlesDataBase;
    public static boolean isAlarm = false;
    public static boolean isLast = false;

    public Updater() {
        super("some_name");
    }

    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        isAlarm = false;
        isLast = false;
        articlesDataBase = new ArticleDataBase(this);
        articlesDataBase.open();
        articlesDataBase.deleteALLArticles();
        Cursor cursor = articlesDataBase.sqLiteDatabase.query(ArticleDataBase.TABLE_FEED_NAME, new String[] {
                ArticleDataBase.KEY_ID_FEED, ArticleDataBase.KEY_FEED_NAME, ArticleDataBase.KEY_FEED},
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );

        while (cursor.moveToNext()) {
            // GET COLUMN INDICES + VALUES OF THOSE COLUMNS
            String feed = cursor.getString(cursor.getColumnIndex(ArticleDataBase.KEY_FEED));
            if (isCorrect(feed)) {
                Intent intentForRss = new Intent(this, MainIntentWork.class);
                intentForRss.putExtra(ArticleActivity.KEY_FOR_INTENT, feed);
                startService(intentForRss);
                isAlarm = true;
            }
        }
        isLast = true;
    }

    private boolean isCorrect(String feed) {
        try {
            URL url = new URL(feed);
        } catch (MalformedURLException e) {
            return false;
        }
        return true;
    }
}
