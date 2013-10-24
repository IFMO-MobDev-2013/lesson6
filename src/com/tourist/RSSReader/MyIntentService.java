package com.tourist.RSSReader;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class MyIntentService extends IntentService {

    public static final String key = "com.tourist.RSSReader.MyIntentService";

    public MyIntentService() {
        super("MyIntentService");
    }

    ArrayList<String> summaries;
    ArrayList<String> links;
    ArrayList<String> titles;

    String channel = null;

    @Override
    public void onHandleIntent(Intent intent) {
        Log.i("MyIntentService", "entered onHandleIntent");
        channel = intent.getStringExtra("channel");
        String result = "";
        try {
            summaries = new ArrayList<String>();
            links = new ArrayList<String>();
            titles = new ArrayList<String>();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            HttpResponse httpResponse = new DefaultHttpClient().execute(new HttpGet(channel));
            HttpEntity httpEntity = httpResponse.getEntity();
            String xml = EntityUtils.toString(httpEntity, "UTF-8");
            InputSource is = new InputSource(new StringReader(xml));
            parser.parse(is, new MyHandler(summaries, links, titles));
            result = "good";
        } catch (SAXException e) {
            result = e.getMessage();
        } catch (IOException e) {
            result = e.getMessage();
        } catch (ParserConfigurationException e) {
            result = e.getMessage();
        }
        Intent response = new Intent();
        response.setAction(key);
        response.addCategory(Intent.CATEGORY_DEFAULT);
        response.putExtra("result", result);
        if ("good".equals(result)) {
            response.putExtra("summaries", summaries);
            response.putExtra("links", links);
            response.putExtra("titles", titles);
        }
        sendBroadcast(response);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pi = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis() + 120000, 120000, pi);
    }
}
