package com.example.RSS;

import android.app.IntentService;
import android.content.Intent;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;


public class MyIntentService extends IntentService {
    public static final String PRELOAD = "Loading";
    public static final String POSTLOAD = "Complete";
    public static final String ACTION = "OUT";
    public static final String ERROR = "Error";

    SAXParser saxParser;
    DefaultHandler handler;
    public MyIntentService() {
        super("rss");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String link = intent.getStringExtra("link");
        SecondPage.menuItems.clear();
        Intent response = new Intent();
        response.setAction(ACTION);
        response.addCategory(Intent.CATEGORY_DEFAULT);
        response.putExtra(ACTION, PRELOAD);
        sendBroadcast(response);

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            handler = new DefaultHandler() {

                HashMap<String, String> map = new HashMap<String, String>();
                boolean btitle = false;
                boolean blink = false;
                boolean bdescription = false;
                boolean bpubdate = false;
                boolean bitem = false;

                public void startElement(String uri, String localName,String qName,
                                         Attributes attributes) throws SAXException {

                    if (qName.equalsIgnoreCase("ITEM")) {
                        bitem = true;
                    }

                    if (qName.equalsIgnoreCase("TITLE")) {
                        btitle = true;
                    }

                    if (qName.equalsIgnoreCase("PUBDATE")) {
                        bpubdate = true;
                    }

                    if (qName.equalsIgnoreCase("DESCRIPTION")) {
                        bdescription = true;
                    }

                    if (qName.equalsIgnoreCase("LINK")) {
                        blink = true;
                    }

                }

                public void endElement(String uri, String localName, String qName) {
                    if (qName.equalsIgnoreCase("ITEM") && !map.isEmpty()) {
                        HashMap<String, String> temp = new HashMap<String, String>();
                        temp.put(SecondPage.KEY_TITLE, map.get(SecondPage.KEY_TITLE));
                        temp.put(SecondPage.KEY_LINK, map.get(SecondPage.KEY_LINK));
                        temp.put(SecondPage.KEY_DESCRIPTION, map.get(SecondPage.KEY_DESCRIPTION));
                        temp.put(SecondPage.KEY_PUBDATE, map.get(SecondPage.KEY_PUBDATE));
                        SecondPage.menuItems.add(temp);
                        bitem = false;
                        map.clear();
                    }
                }

                public void characters(char ch[], int start, int length) throws SAXException {
                    if (bitem) {
                        if (btitle) {
                            map.put(SecondPage.KEY_TITLE, new String(ch, start, length));
                            btitle = false;
                        }

                        if (blink) {
                            map.put(SecondPage.KEY_LINK, new String(ch, start, length));
                            blink = false;
                        }

                        if (bdescription) {
                            map.put(SecondPage.KEY_DESCRIPTION, new String(ch, start, length));
                            bdescription = false;
                        }

                        if (bpubdate) {
                            map.put(SecondPage.KEY_PUBDATE, new String(ch, start, length));
                            bpubdate = false;
                        }
                    }
                }

            };
            URL url = new URL(link);
            BufferedInputStream is = new BufferedInputStream(url.openStream());
            if (!"http://bash.im/rss".equals(link)) {
                Reader reader = new InputStreamReader(is, "UTF-8");
                InputSource io = new InputSource(reader);
                io.setEncoding("UTF-8");
                saxParser.parse(io, handler);
            } else {
                Reader reader = new InputStreamReader(is, "windows-1251");
                InputSource io = new InputSource(reader);
                io.setEncoding("windows-1251");
                saxParser.parse(io, handler);
            }

        } catch (Exception e) {
                response.putExtra(ACTION, ERROR);
                sendBroadcast(response);
                return;
        }
        response.putExtra(ACTION, POSTLOAD);
        sendBroadcast(response);
    }
}
