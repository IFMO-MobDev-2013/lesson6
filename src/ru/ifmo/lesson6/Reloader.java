package ru.ifmo.lesson6;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: asus
 * Date: 24.10.13
 * Time: 2:44
 * To change this template use File | Settings | File Templates.
 */
public class Reloader extends IntentService {
    public Reloader(){
        super("RssLoader");
    }

    public void onCreate(){
        super.onCreate();

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ArrayList<String> urls = intent.getStringArrayListExtra("urls");
        ArrayList<ArrayList<Article>> AllFeeds = new ArrayList<ArrayList<Article>>();
        for (int i = 0; i < urls.size(); i++){
            AllFeeds.add(AndroidSaxFeedParser.parse(urls.get(i)));
        }
        // Feeds are ready for being saved                      <--------------------
    }
}
