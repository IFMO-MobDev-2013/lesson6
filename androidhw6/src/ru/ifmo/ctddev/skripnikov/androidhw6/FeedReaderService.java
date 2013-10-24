package ru.ifmo.ctddev.skripnikov.androidhw6;

import android.app.IntentService;
import android.content.Intent;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class FeedReaderService extends IntentService {

    private ArrayList<FeedItem> feed;

    public FeedReaderService(String name) {
        super(name);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        String url = intent.getStringExtra("url");
        String encoding = intent.getStringExtra("encoding");
        try {
            setFeedByURL(url, encoding);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("FEED_FETCHED");
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra("feed",  feed.toArray(new FeedItem[feed.size()]));
        sendBroadcast(broadcastIntent);
    }

    private void setFeedByURL(String stringUrl, String encoding) throws IOException {
        URL url = new URL(stringUrl);
        HttpURLConnection connection = null;
        InputStream is = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            is = connection.getInputStream();
            setFeedFromInputStreamAsSAX(is, encoding);
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } finally {
            if (is != null)
                is.close();
            if (connection != null)
                connection.disconnect();
        }
    }

    private void setFeedFromInputStreamAsSAX(InputStream is, String encoding) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        DefaultHandler handler = new DefaultHandler() {
            private String link = "";
            private String title = "";
            private String description = "";
            private boolean channelIsOpen = false;
            private boolean feedIsOpen = false;
            private boolean linkIsOpen = false;
            private boolean titleIsOpen = false;
            private boolean itemIsOpen = false;
            private boolean descriptionIsOpen = false;

            public void startElement(String uri, String localName, String qName,
                                     Attributes attributes) throws SAXException {
                if (itemIsOpen) {
                    if (qName.equalsIgnoreCase("TITLE")) {
                        titleIsOpen = true;
                    } else if (qName.equalsIgnoreCase("LINK")) {
                        linkIsOpen = true;
                        if (feedIsOpen) {
                            link = attributes.getValue("href");
                        }
                    } else if (qName.equalsIgnoreCase("DESCRIPTION") || qName.equalsIgnoreCase("SUMMARY")) {
                        descriptionIsOpen = true;
                    }
                } else if (qName.equalsIgnoreCase("ITEM") || qName.equalsIgnoreCase("ENTRY")) {
                    itemIsOpen = true;
                } else if (qName.equalsIgnoreCase("CHANNEL")) {
                    channelIsOpen = true;
                } else if (qName.equalsIgnoreCase("FEED")) {
                    feedIsOpen = true;
                }
            }

            public void endElement(String uri, String localName,
                                   String qName) throws SAXException {
                if (qName.equalsIgnoreCase("DESCRIPTION") || qName.equalsIgnoreCase("SUMMARY")) {
                    descriptionIsOpen = false;
                } else if (qName.equalsIgnoreCase("TITLE")) {
                    titleIsOpen = false;
                } else if (qName.equalsIgnoreCase("LINK")) {
                    linkIsOpen = false;
                } else if (qName.equalsIgnoreCase("ITEM") || qName.equalsIgnoreCase("ENTRY")) {
                    feed.add(new FeedItem(link, title, description));
                    link = "";
                    title = "";
                    description = "";
                    itemIsOpen = false;
                }
            }

            public void characters(char ch[], int start, int length) throws SAXException {
                if (itemIsOpen) {
                    if (titleIsOpen) {
                        title += new String(ch, start, length);
                    } else if (descriptionIsOpen) {
                        description += new String(ch, start, length);
                    } else if (linkIsOpen) {
                        if (channelIsOpen) {
                            link += new String(ch, start, length);
                        }
                    }
                }
            }
        };
        feed = new ArrayList<FeedItem>();
        Reader reader = new InputStreamReader(is, encoding);
        InputSource nis = new InputSource(reader);
        nis.setEncoding(encoding);
        saxParser.parse(nis, handler);
    }
}
