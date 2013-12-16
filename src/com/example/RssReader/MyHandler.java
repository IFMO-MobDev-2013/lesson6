package com.example.RssReader;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;

public class MyHandler extends DefaultHandler {

    StringBuilder sb = null;

    String[] sums, links, titles, times;
    int size = -1;
    public enum TAGS{ITEM, LINK, TITLE, SUM, TIME}
    HashMap<String, TAGS> type;





    MyHandler(String[] sums, String[] links, String[] titles, String[] times) {
        super();
        this.sums = sums;
        this.links = links;
        this.titles = titles;
        this.times = times;
        type = new HashMap<String, TAGS>();
        type.put("item", TAGS.ITEM);
        type.put("entry", TAGS.ITEM);
        type.put("link", TAGS.LINK);
        type.put("pubDate", TAGS.TIME);
        type.put("published", TAGS.TIME);
        type.put("title", TAGS.TITLE);
        type.put("description", TAGS.SUM);
        type.put("summary", TAGS.SUM);
        sb = new StringBuilder();
    }

    boolean tagEntry = false;
    String sum = null;
    String link = null;
    String title = null;
    String time = null;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attr) throws SAXException {
        super.startElement(uri, localName, qName, attr);
        TAGS nowType = type.get(qName);
        if (nowType == null) return;
        if (nowType == TAGS.ITEM) {
            tagEntry = true;
        } else {
            sb.setLength(0);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        TAGS nowType = type.get(qName);
        if (nowType == null) return;
        switch (nowType) {
            case ITEM:
                size++;
                sums[size] = sum;
                links[size] = link;
                titles[size] = title;
                times[size] = time;
                tagEntry = false;
                break;
            case SUM:
                sum = sb.toString();
                break;
            case TIME:
                time = sb.toString();
                break;
            case LINK:
                link = sb.toString();
                break;
            case TITLE:
                title = sb.toString();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        sb.append(ch, start, length);
    }

}

