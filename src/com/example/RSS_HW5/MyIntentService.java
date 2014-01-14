package com.example.RSS_HW5;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;


public class MyIntentService extends IntentService {
    ArrayList<String> summaries;
    ArrayList<String> titles;
    public static final String key = "com.example.RSS_HW5.MyIntentService";

    public MyIntentService() {
        super("MyIntentService");
    }

    String link = null;

    @Override
    public void onHandleIntent(Intent intent) {
        Log.i("MyIntentService", "entered onHandleIntent");
        link = intent.getStringExtra("link");
        boolean result = false;
        try {
            summaries = new ArrayList<String>();
            titles = new ArrayList<String>();
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(link);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            String inform = EntityUtils.toString(httpResponse.getEntity());
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            saxParser.parse(new ByteArrayInputStream(inform.getBytes()), new RSSHandler(summaries, titles));
            result = true;
        } catch (SAXException e) {
            result = false;
        } catch (IOException e) {
            result = false;
        } catch (ParserConfigurationException e) {
            result = false;
        }
        Intent response = new Intent();
        response.setAction(key);
        response.addCategory(Intent.CATEGORY_DEFAULT);
        response.putExtra("result", result);
        if (result) {
            response.putExtra("titles", titles);
            response.putExtra("summaries", summaries);
        }
        sendBroadcast(response);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis() + 120000, 120000, pendingIntent);
    }
}
