package com.example.rssreader2;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.EditText;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.ArrayList;


public class Service extends IntentService{

    public Service() {
        super("intentService");
    }

    public void onCreate() {
        super.onCreate();
    }

    private ArrayList<String> titles;
    private ArrayList<String> descriptions;

    @Override
    protected void onHandleIntent(Intent intent) {
        ResultReceiver receiver = intent.getParcelableExtra("receiver");
        Bundle bundle = new Bundle();
        boolean error = false;

        String url = intent.getStringExtra("download");
        InputSource is = null;
        String xml;
        try {
            HttpResponse httpResponse = new DefaultHttpClient().execute(new HttpGet(url));
            HttpEntity httpEntity = httpResponse.getEntity();


            //is = httpEntity.getContent();
            xml = EntityUtils.toString(httpEntity, "UTF-8");
            is = new InputSource(new StringReader(xml));





        } catch (Throwable e) {
            error = true;
        }


        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            RSSHandler handler  = new RSSHandler();

            saxParser.parse(is , handler);
            RSS rss = handler.getRSS();

            MyActivity.setRSS(rss, error);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
