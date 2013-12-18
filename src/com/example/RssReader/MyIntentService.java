package com.example.RssReader;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xml.sax.InputSource;
import javax.xml.parsers.*;
import java.io.StringReader;


public class MyIntentService extends IntentService {

    public MyIntentService() {
        super("lalka");
    }

    String[] sums;
    String[] links;
    String[] titles, times;

    String url = null;

    @Override
    public void onHandleIntent(Intent intent) {
        url = intent.getStringExtra("url");
        String error = "nope";
        try {
            sums = new String[2000];
            links = new String[2000];
            titles = new String[2000];
            times = new String[2000];
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            HttpResponse httpResponse = new DefaultHttpClient().execute(new HttpGet(url));
            HttpEntity httpEntity = httpResponse.getEntity();
            String xml = EntityUtils.toString(httpEntity, "UTF-8");
            InputSource input = new InputSource(new StringReader(xml));
            parser.parse(input, new MyHandler(sums, links, titles, times));
        } catch (Exception e) {
            error = e.getMessage();
        }
        Intent response = new Intent();
        response.setAction("downloadResponse");
        response.addCategory(Intent.CATEGORY_DEFAULT);
        response.putExtra("error", error);
        response.putExtra("sums", sums);
        response.putExtra("links", links);
        response.putExtra("news", titles);
        response.putExtra("times", times);


        sendBroadcast(response);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pi = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME, System.currentTimeMillis() + 100000, 100000, pi);
    }
}

