package com.deyneka.lesson6;

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

    String channel = null;

    @Override
    public void onHandleIntent(Intent intent) {
        ArrayList<String> texts,refs,titles;
        texts = new ArrayList<String>();
        refs = new ArrayList<String>();
        titles = new ArrayList<String>();
        channel = intent.getStringExtra("channel");
        String result = "";
        parcing();
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            HttpResponse httpResponse = new DefaultHttpClient().execute(new HttpGet(channel));
            HttpEntity httpEntity = httpResponse.getEntity();
            String xml = EntityUtils.toString(httpEntity, "UTF-8");
            InputSource is = new InputSource(new StringReader(xml));
            parser.parse(is, new MyHandler(texts, refs, titles));
            result = "good";
        } catch (Exception e) {
            result = e.getMessage();
        }
        Intent response = new Intent();
        response.setAction(key);
        response.addCategory(Intent.CATEGORY_DEFAULT);
        response.putExtra("result", result);
        if ("good".equals(result)) {
            response.putExtra("summaries", texts);
            response.putExtra("links", refs);
            response.putExtra("titles", titles);
        }
        sendBroadcast(response);
        setAlarmManager(intent);
    }

    void setAlarmManager(Intent intent)
    {
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pi = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis() + 150000, 150000, pi);
    }
    public static final String key = "com.deyneka.lesson6";

    public MyIntentService() {
        super("MyIntentService");
    }

    public void parcing()
    {
        try{
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            HttpResponse httpResponse = new DefaultHttpClient().execute(new HttpGet(channel));
            HttpEntity httpEntity = httpResponse.getEntity();
            String xml = EntityUtils.toString(httpEntity, "UTF-8");
            InputSource is = new InputSource(new StringReader(xml));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
