package com.example.RSS_reader2;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import org.xml.sax.InputSource;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainIntentWork extends IntentService {
    public static final String ACTION_MyIntentService = "com.example.My_Super_RSSReader.RESPONSE";
    public static final String EXTRA_KEY_OUT_FOR_MESSAGE = "EXTRA_OUT_FOR_MESSAGE";
    public static String URLAdress;

    public MainIntentWork() throws Exception {
        super("Pavel");
    }

    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        URLAdress = intent.getStringExtra(ArticleActivity.KEY_FOR_INTENT);
        URL url;
        HttpURLConnection httpConnection;
        InputStream inputStream;
        Reader reader;
        InputSource is;
        byte[] b = new byte[50];
        String encoding = "";
        String sms = "";
        try {
            url = new URL(URLAdress);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setConnectTimeout(15000);
            inputStream = httpConnection.getInputStream();
            inputStream.read(b);
            for (int i = 0; i < 50; i++) encoding = encoding + (char)b[i];
            // utf-8 or windows-1251 or ...
            encoding = encoding.substring(encoding.indexOf("encoding=\"") + 10 , encoding.indexOf("\"?>"));
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(b);
            // bonding inputStreams
            inputStream = new SequenceInputStream(byteArrayInputStream, inputStream);
            // next work
            reader = new InputStreamReader(inputStream, encoding);
            is = new InputSource(reader);
            is.setEncoding(encoding);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(false);
            SAXParser parser = factory.newSAXParser();
            SAXXMLParser saxXMLParser = new SAXXMLParser();
            parser.parse(is, saxXMLParser);
            sms = "Several articles for you:";
        } catch(Exception e) {
            e.printStackTrace();
            sms = e.getMessage();
            Log.d("Handle Intent", "Warning");
        }
        Intent intentResponse = new Intent();
        intentResponse.setAction(ACTION_MyIntentService);
        intentResponse.addCategory(Intent.CATEGORY_DEFAULT);
        intentResponse.putExtra(EXTRA_KEY_OUT_FOR_MESSAGE, sms);
        sendBroadcast(intentResponse);
    }
    public void onDestroy() {
        super.onDestroy();
    }
}
