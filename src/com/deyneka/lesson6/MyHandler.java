package com.deyneka.lesson6;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class MyHandler extends DefaultHandler {

    StringBuilder stringBuilder = null;
    HashMap<String, String> position;
    ArrayList<String> texts,refs,titles;

    MyHandler(ArrayList<String> texts, ArrayList<String> refs, ArrayList<String> titles) {
        super();
        this.texts = texts;
        this.refs = refs;
        this.titles = titles;
        insertInPosition();
        stringBuilder = new StringBuilder();
    }

    boolean isInside = false;
    String summary = null;
    String link = null;
    String title = null;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attr) throws SAXException {
        super.startElement(uri, localName, qName, attr);
        String tag = position.get(qName);
        if (tag == null) {
            return;
        }
        if (tag == "e") {
            isInside = true;
        } else {
            stringBuilder.setLength(0);
        }
    }

    public void insertInPosition()
    {
        position = new HashMap<String, String>();
        position.put("entry", "e");
        position.put("item", "e");
        position.put("summary", "s");
        position.put("description", "s");
        position.put("id", "l");
        position.put("link", "l");
        position.put("title", "t");
    }

    @Override
    public void characters(char[] ch, int s, int l) throws SAXException {
        stringBuilder.append(ch, s, l);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        String tag = position.get(qName);
        if (tag == null) {
            return;
        }
        switchtag(tag);

    }
    public void switchtag(String tag)
    {
        if (tag.equals("e"))
        {
            texts.add(summary);
            refs.add(link);
            titles.add(title);
            isInside = false;
        }
        if (tag.equals("s"))
            summary = stringBuilder.toString();
        if (tag.equals("l"))
            link = stringBuilder.toString();
        if (tag.equals("t"))
            title = stringBuilder.toString();
    }
}
