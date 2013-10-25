package com.java.android.dronov;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.java.android.dronov.RSS.Entry;
import com.java.android.dronov.RSS.Feed;
import com.java.android.dronov.RSS.FeedBack;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: dronov
 * Date: 24.10.13
 * Time: 22:34
 * To change this template use File | Settings | File Templates.
 */
public class UpdateRSS extends IntentService {

    public static final String key = "com.java.android.dronov.UpdateRSS";
    public static final int updateTime = 15000;
    private Feed feed;
    private Exception exception = null;
    public UpdateRSS() {
        super("UpdateRss");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String link = intent.getStringExtra("link");
//        Log.d("current link", link);
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            HttpGet httpGet = new HttpGet(link);
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String xml = EntityUtils.toString(httpEntity, "UTF-8");
            InputSource is = new InputSource(new StringReader(xml));

            RSSHandler handler = new RSSHandler();
            parser.parse(is, handler);
            feed = handler.getFeed();
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            exception = new Exception("Some problems with URL");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            exception = new Exception("Some problems with Parser");
        } catch (SAXException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            exception = new Exception("Some problems with Parser");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            exception = new Exception("Some problems with Input Stream");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            exception = new Exception("Some problems with URL");
        } catch (Exception e) {
            e.printStackTrace();
            exception = new Exception("Some problems with URL");
        }
        Intent newIntent = new Intent();
        newIntent.setAction(key);
        newIntent.addCategory(Intent.CATEGORY_DEFAULT);
        newIntent.putExtra("feed", feed);
        newIntent.putExtra("exception", (Serializable)exception);
        sendBroadcast(newIntent);

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pi = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis() + updateTime, updateTime, pi);

    }
}
