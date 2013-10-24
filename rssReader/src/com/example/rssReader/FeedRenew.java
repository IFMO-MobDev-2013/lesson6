package com.example.rssReader;

import android.app.IntentService;
import android.content.Intent;
import android.util.Xml;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Руслан
 * Date: 24.10.13
 * Time: 21:42
 * To change this template use File | Settings | File Templates.
 */
public class FeedRenew extends IntentService {
    private String link;
    private ArrayList<Map<String, Object>> data;
    public FeedRenew(String link) {
        super("FeedRenew");
        this.link = link;
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }

    public ArrayList<Map<String, Object>> getData(){
        return data;
    }

    @Override
    protected void onHandleIntent(Intent intent){
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(link);
        HttpResponse response;
        InputStream inputStream = null;
        try{
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            inputStream = entity.getContent();
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            SAXParser parser = parserFactory.newSAXParser();
            RssParser rssParser = new RssParser();
            String enc = "UTF-8";
            if ("http://bash.im/rss".equals(link))
                enc = "windows-1251";
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, enc);
            Xml.parse(inputStreamReader, rssParser);
            data = rssParser.getNews();
        } catch (Exception e){

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            stopSelf();
        }
    }
}
