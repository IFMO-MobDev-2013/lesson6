package com.example.RSS_reader2;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Updater extends IntentService {
    ArticleDataBase articlesDataBase;
    public static int goodArticle = 0;
    public static ProgressDialog updater_dialog;
    public Updater() {
        super("some_name");
    }

    public void onCreate() {
        super.onCreate();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //updater_dialog = ProgressDialog.show(Updater.this, "Обновление данных. Пожалуйста, подождите...", null, true);
        //updater_dialog.show();
        if (isNetworkAvailable() == false) {
            return;
        }

        goodArticle = 0;
        articlesDataBase = new ArticleDataBase(this);
        articlesDataBase.open();
        articlesDataBase.deleteALLArticles();
        int t = -1;
        while (t != 0) {
            t = articlesDataBase.getCountArticles();
        }

        Cursor cursor = articlesDataBase.sqLiteDatabase.query(ArticleDataBase.TABLE_FEED_NAME, new String[] {
                ArticleDataBase.KEY_ID_FEED, ArticleDataBase.KEY_FEED_NAME, ArticleDataBase.KEY_FEED},
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );

        while (cursor.moveToNext()) {
            String feed = cursor.getString(cursor.getColumnIndex(ArticleDataBase.KEY_FEED));
            if (isCorrect(feed)) {
                ++goodArticle;
            }
        }

        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            String feed = cursor.getString(cursor.getColumnIndex(ArticleDataBase.KEY_FEED));
            if (isCorrect(feed)) {
                Intent intentForRss = new Intent(this, MainIntentWork.class);
                intentForRss.putExtra(ArticleActivity.KEY_FOR_INTENT, feed);
                startService(intentForRss);
            }
        }
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
