package com.example.rssReader;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Ruslan
 * Date: 24.10.13
 * Time: 11:20
 */
public class RssParser extends DefaultHandler {
    private ArrayList<Map<String, Object>> feedList;
    private String tmpElement;
    private Map<String, Object> map;
    private String title, link, description, pubDate;

    public ArrayList<Map<String, Object>> getNews() {
        return feedList;
    }

    @Override
    public void startDocument() throws SAXException {
        feedList = new ArrayList<Map<String, Object>>();
        map = new HashMap<String, Object>();
    }

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        tmpElement = qName;
        if ("item".equals(tmpElement)) {
            map = new HashMap<String, Object>();
            title = "";
            description = "";
            link = "";
            pubDate = "";
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if ("title".equals(tmpElement)) {
            title += new String(ch, start, length);
        } else if ("description".equals(tmpElement)) {
            description += new String(ch, start, length);
        } else if ("link".equals(tmpElement)) {
            link += new String(ch, start, length);
        } else if ("pubDate".equals(tmpElement)) {
            pubDate += new String(ch, start, length);
        }
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        if ("title".equals(tmpElement))
            map.put("title", title);
        else if ("description".equals(tmpElement))
            map.put("description", description);
        else if ("link".equals(tmpElement))
            map.put("link", link);
        else if ("pubDate".equals(tmpElement)){
            map.put("pubDate", pubDate);
            feedList.add(map);
        }
    }
}