package com.example.RSS_reader2;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ArticleActivity extends Activity {
    public static ArticleDataBase articlesDataBase;
    public static final String KEY_FOR_INTENT = "key_for_intent";
    private String mainLinkName;
    private boolean isWasBroadcast = false;
    static String printSMS = "List of articles for you";
    ListView listView;
    TextView message;
    ImageView imageView;
    ArrayList<Article> rssArticles = new ArrayList<Article>();
    public MyBroadcastReceiver myBroadcastReceiver;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_rss);
        articlesDataBase = new ArticleDataBase(this);
        articlesDataBase.open();
        mainLinkName = getIntent().getStringExtra(MainActivity.KEY_FOR_WORKACTIVITY).toString();
        listView = (ListView) findViewById(R.id.listView);
        imageView = (ImageView) findViewById(R.id.imageView);
        message = (TextView) findViewById(R.id.textView);
        imageView.setImageResource(R.drawable.renew);

        IntentFilter intentFilter = new IntentFilter(MainIntentWork.ACTION_MyIntentService);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        myBroadcastReceiver = new MyBroadcastReceiver();
        registerReceiver(myBroadcastReceiver, intentFilter);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intentMyIntentService = new Intent(ArticleActivity.this, MainIntentWork.class);
                    intentMyIntentService.putExtra(KEY_FOR_INTENT, mainLinkName);
                    startService(intentMyIntentService);
                    //register of BroadcastReceiver
                     printSMS = "List of articles was updated";
                   } catch (Exception e) {
                }
            }
        });
        if (articlesDataBase.isSuchArticles(mainLinkName) == true) {
            isWasBroadcast = false;
            readFromDateBase();
        } else {
            isWasBroadcast = true;
            Intent intentMyIntentService = new Intent(ArticleActivity.this, MainIntentWork.class);
            intentMyIntentService.putExtra(KEY_FOR_INTENT, mainLinkName);
            startService(intentMyIntentService);
            //register of BroadcastReceiver
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long number) {
                Intent intent = new Intent(ArticleActivity.this, SomeArticle.class);
                intent.putExtra("Description", rssArticles.get(index).getDescription());
                intent.putExtra("Link", rssArticles.get(index).getLink());
                intent.putExtra("Title", rssArticles.get(index).getTitle());
                intent.putExtra("PubDate", rssArticles.get(index).getPubDate());
                startActivity(intent);
            }
        });
    }
    /*
    @Override
    protected void onDestroy() {
    //     articlesDataBase.close();
        super.onDestroy();
        if (isWasBroadcast == true)
            unregisterReceiver(myBroadcastReceiver);
    }
    */
    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Updater.isLast == true && Updater.isAlarm == true || Updater.isLast == false && Updater.isAlarm == false) {
                readFromDateBase();
                Updater.isAlarm = false;
                Updater.isLast = false;
            }
        }
    }

    public void PrintAll() {
        ArrayList<HashMap<String, String>> rssPrint = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;
        for (int i = 0; i < rssArticles.size(); i++) {
            map = new HashMap<String, String>();
            map.put(MainActivity.KEY_URL_NAME, rssArticles.get(i).getTitle());
            map.put(MainActivity.KEY_URL, rssArticles.get(i).getPubDate());
            rssPrint.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, rssPrint, R.layout.row, new String[] {MainActivity.KEY_URL_NAME, MainActivity.KEY_URL}, new int[] {R.id.Colname, R.id.Colurl});
        listView.setAdapter(adapter);
        //Toast toast = Toast.makeText(ArticleActivity.this, printSMS, 3500);
        //toast.show();
        //printSMS = "List of articles for you";
    }

    private void readFromDateBase() {
        rssArticles = new ArrayList<Article>();
        boolean isRss = false;
        Cursor cursor = articlesDataBase.sqLiteDatabase.query(ArticleDataBase.TABLE_ARTICLE_NAME, new String[] {
                ArticleDataBase.KEY_ID_ARTICLE, ArticleDataBase.KEY_TITLE_ARTICLE, ArticleDataBase.KEY_DESCRIPTION_ARTICLE, ArticleDataBase.KEY_LINKNAME_ARTICLE, ArticleDataBase.KEY_LINK_ARTICLE, ArticleDataBase.KEY_PUBDATE_ARTICLE },
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        while (cursor.moveToNext()) {
            // GET COLUMN INDICES + VALUES OF THOSE COLUMNS
            int id = cursor.getInt(cursor.getColumnIndex(ArticleDataBase.KEY_ID_ARTICLE));
            String title = cursor.getString(cursor.getColumnIndex(ArticleDataBase.KEY_TITLE_ARTICLE));
            String description = cursor.getString(cursor.getColumnIndex(ArticleDataBase.KEY_DESCRIPTION_ARTICLE));
            String link = cursor.getString(cursor.getColumnIndex(ArticleDataBase.KEY_LINK_ARTICLE));
            String linkname = cursor.getString(cursor.getColumnIndex(ArticleDataBase.KEY_LINKNAME_ARTICLE));
            String pubdate = cursor.getString(cursor.getColumnIndex(ArticleDataBase.KEY_PUBDATE_ARTICLE));
            if (mainLinkName.equals(linkname)) {
                rssArticles.add(new Article(title, description, link, pubdate));
                isRss = true;
            }
            Log.i("LOG_TAG", "ROW " + id + " HAS DATAS " + title + " " + link);
        }
        if (isRss) {
            PrintAll();
            message.setText("Several articles for you:");
        }
    }
}

