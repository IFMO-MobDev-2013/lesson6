package com.example.RSSReader;

import android.app.IntentService;
import android.content.Intent;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class RSSTape extends IntentService {

    private ArrayList<RSSNode> nodes;
    private static String link;

    public RSSTape() {
        super("MyName");
    }

    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String request;

        String task = intent.getStringExtra("task");
        if ("load".equals(task))  {
            link = intent.getStringExtra("link");
            request = link;
        }
        else {
            request = link;
        }

        if (link == null) {
            return;
        }

        InputStream inputStream = null;

        URL url;
        HttpURLConnection connect;

        try {
            url = new URL(request);
            connect = (HttpURLConnection) url.openConnection();

            inputStream = connect.getInputStream();
            Reader reader;
            InputSource is;

            if ("http://lenta.ru/rss".equals(request)) {
                reader = new InputStreamReader(inputStream, "UTF-8");
                is = new InputSource(reader);
                is.setEncoding("UTF-8");
            }
            else {
                reader = new InputStreamReader(inputStream, "windows-1251");
                is = new InputSource(reader);
                is.setEncoding("windows-1251");
            }

            SAXParserFactory factory = SAXParserFactory.newInstance();


            factory.setNamespaceAware(false);
            SAXParser parser;

            parser = factory.newSAXParser();

            RSSParser rssParser = new RSSParser();

            parser.parse(is, rssParser);

            nodes = rssParser.getResult();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {}
        }

        Intent intentResponse = new Intent("com.example.RSSReader.RESPONSE");
        intentResponse.addCategory(Intent.CATEGORY_DEFAULT);
        intentResponse.putParcelableArrayListExtra("nodes", nodes);
        //sendBroadcast(intentResponse);
    }

    private class RSSParser extends DefaultHandler {

        private ArrayList<RSSNode> result;
        private boolean isItem;
        private boolean isDate;
        private boolean isTitle;
        private boolean isDescription;
        private RSSNode rssNode;
        String ans;

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            result = new ArrayList<RSSNode>();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            ans = "";
            if (qName.equals("item")) {
                isItem = true;
                rssNode = new RSSNode();
            }

            if (qName.equals("title")) {
                isTitle = true;
            }
            if (qName.equals("pubDate")) {
                isDate = true;
            }
            if (qName.equals("description")) {
                isDescription = true;
            }
            super.startElement(uri, localName, qName, attributes);

        }

        @Override
        public void characters(char[] c, int start, int length) throws SAXException {
            super.characters(c, start,  length);
            String s = new String(c, start, length);

            if (isItem) {
                ans += s;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (qName.equals("item")) {
                isItem = false;
                result.add(rssNode);
            }

            if (isItem) {
                if (qName.equals("title")) {
                    rssNode.setTitle(ans);
                    ans = "";
                    isTitle = false;
                }
                if (qName.equals("pubDate")) {
                    rssNode.setDate(ans);
                    ans = "";
                    isDate = false;
                }
                if (qName.equals("description")) {
                    rssNode.setDescription(ans);
                    ans = "";
                    isDescription = false;
                }
            }

            super.endElement(uri,localName, qName);
        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
        }

        public ArrayList<RSSNode> getResult() {
            return this.result;
        }
    }
}
