package com.example.lesson6;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: satori
 * Date: 10/24/13
 * Time: 7:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class UpdateArticles extends IntentService {
    public static ArrayList<ArrayList<String> > articles;
    public static ArrayList<ArrayList<String> > descriptions;
    private final String[] urls = {"http://lenta.ru/rss/news",
            "http://lenta.ru/rss/top7",
            "http://lenta.ru/rss/last24",
            "http://lenta.ru/rss/articles",
            "http://lenta.ru/rss/columns",
            "http://bash.im/rss/"};
    public int index = 0;
    public UpdateArticles() {
        super("UpdateArticles");
        index = 0;



    }
    @Override
    protected void onHandleIntent(Intent intent) {
        index = 0;
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {

                boolean item = false;
                boolean title = false;
                boolean description = false;
                StringBuffer buffer;
                public void startElement(String uri, String localName,
                                         String qName, Attributes attributes)
                        throws SAXException {
                    buffer = new StringBuffer();
                    //System.out.println("Start Element :" + qName);
                    System.out.println("StartTag: " + qName);
                    if (qName.equals("item")) {
                        item = true;
                    }

                    if (qName.equals("title")) {
                        title = true;
                    }

                    if (qName.equals("description")) {
                        description = true;
                    }


                }

                public void endElement(String uri, String localName,
                                       String qName)
                        throws SAXException {
                    String all = buffer.toString();
                    if (item && description && qName.equals("description")) {
                        descriptions.get(index).add(all);
//                        item = false;
                        //  description = false;
                    }
                    System.out.println("EndTag: " + qName);
                    //System.out.println("End Element :" + qName);
                    if (qName.equals("item")) {
                        item = false;
                    }

                    if (qName.equals("title")) {
                        title = false;
                    }

                    if (qName.equals("description")) {
                        description = false;
                    }

                }

                public void characters(char ch[], int start, int length)
                        throws SAXException {

                    //System.out.println(new String(ch, start, length));


                    if (item &&  title) {
                        System.out.println("First Name : "
                                + new String(ch, start, length));
                        //bfname = false;
                        articles.get(index).add(new String(ch, start, length));
                    }
                    else {


                    if(buffer != null) buffer.append(new String(ch, start, length));
                    }

                    //System.out.println("Last Name : "
//                            + new String(ch, start, length));
                        //blname = false;
                    //d

                }
            };
            for (String myurl : urls) {
                String coding = index == 5 ? "CP1251" : "UTF-8";
                System.out.println("Dowloading URL:" + myurl);
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(myurl);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream is = httpEntity.getContent();
                Reader reader = new InputStreamReader(is,coding);
                InputSource isource = new InputSource(reader);
                isource.setEncoding(coding);
                saxParser.parse(isource, handler);

                is.close();
                index++;
            }
            FeedActivity.arrayAdapter.notifyDataSetChanged();

        //scheduleNextUpdate();
        } catch (Exception e) {
            //Log.d("Error", "Exception while loading xml");
            System.out.println("Error while parsing");
            e.printStackTrace();
        }


    }


}

