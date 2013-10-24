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

    public ArrayList<Map<String, Object>> getNews() {
        return feedList;
    }

    @Override
    public void startDocument() throws SAXException {
        feedList = new ArrayList<Map<String, Object>>();
    }

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        tmpElement = qName;
        map = new HashMap<String, Object>();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        Map<String, Object> map = new HashMap<String, Object>();
        if ("title".equals(tmpElement))
            map.put("title", new String(ch, start, length));
        else if ("description".equals(tmpElement))
            map.put("description", new String(ch, start, length));
        else if ("link".equals(tmpElement))
            map.put("link", new String(ch, start, length));
        else if ("pubDate".equals(tmpElement))
            map.put("pubDate", new String(ch, start, length));
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        feedList.add(map);
    }

    @Override
    public void endDocument() {

    }
}