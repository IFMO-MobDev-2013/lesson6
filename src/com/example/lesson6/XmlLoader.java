package com.example.lesson6;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

public class XmlLoader extends Thread {
    static class RssParser extends DefaultHandler
    {
        private Vector<Entry> entries;
        private Entry current = null;
        private boolean shouldDefine = true;
        private int type = -1;
        private int t = -1;
        private StringBuilder sb;
        private String tag = "";

        public RssParser()
        {
            entries = new Vector<Entry>();
            sb = new StringBuilder();
        }

        public Vector<Entry> getEntries()
        {
            return entries;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
        {
            if (shouldDefine)
            {
                if (qName.equals("channel"))
                {
                    type = 0;
                    shouldDefine = false;
                    Console.print("It is a channel");
                }
                else if (qName.equals("feed"))
                {
                    type = 1;
                    shouldDefine = false;
                    Console.print("It is a feed");
                }
            }
            else
            {
                Console.print("<"+qName+">");
                if (current == null)
                {
                    if (qName.equals("item"))
                    {
                        current = new Entry();
                        Console.print("NEW ITEM");
                    }
                }
                else
                {
                    sb = new StringBuilder();
                    if (qName.equals("link"))
                    {
                        if (type == 1) //feed
                        {
                            current.link = attributes.getValue("href");
                        }
                    }
                }
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            String text = new String(ch, start, length);
            Console.print("Text: "+text);
            sb.append(text);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException
        {
            if (current != null)
            {
                if (qName.equals("title"))
                {
                    current.title = sb.toString();
                }
                else if (qName.equals("link") && type != 1)
                {
                    current.link = sb.toString();
                }
                else if( qName.equals("description"))
                {
                    current.description = sb.toString();
                }
            }
            if (qName.equals("item"))
            {
                Console.print("</"+qName+">");
                entries.add(current);
                current = null;
            }
        }
    }
    private String path = "";
    ArticlesActivity program;
    private HttpClient client = new DefaultHttpClient();

    public XmlLoader(String url, ArticlesActivity program)
    {
        this.path = url;
        this.program = program;
    }
    @Override
    public void run() {
        super.run();
        try {
            HttpGet request = new HttpGet(path);
            HttpResponse response = client.execute(request);
            HttpEntity entiny = response.getEntity();
            Reader reader = new InputStreamReader(entiny.getContent(), EntityUtils.getContentCharSet(entiny));
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            InputSource is = new InputSource(reader);
            RssParser rp = new RssParser();
            parser.parse(is, rp);
            program.onEntries(rp.getEntries());
        }
        catch (Exception e)
        {
            program.onException(e);
        }
    }
}
