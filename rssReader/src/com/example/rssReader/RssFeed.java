package com.example.rssReader;

import android.os.AsyncTask;
import android.util.Xml;
import com.example.rssReader.RssParser;
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

public class RssFeed extends AsyncTask<Void, Void, ArrayList<Map<String, Object>>> {
    String link;

    public RssFeed(String link){
        this.link = link;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Map<String, Object>> doInBackground(Void... params){
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(link);
        HttpResponse response;
        InputStream inputStream = null;
        ArrayList<Map<String, Object>> data = null;
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
        }
        return data;
    }

    protected ArrayList<Map<String, Object>> onPostExecute(ArrayList<Map<String, Object>>... result){
        super.onPostExecute(result[0]);
        return result[0];
    }
}