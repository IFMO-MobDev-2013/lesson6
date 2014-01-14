package com.example.RSS_HW5;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class RSSHandler extends DefaultHandler {
    ArrayList<String> summaries;
    ArrayList<String> titles;
    String buffer = "";
    boolean item;
    boolean title;
    boolean description;
    boolean ATOM;
    String ITEM = "item";
    String TITLE = "title";
    String DESCRIPTION = "description";
    String CONTENT = "";
    String allDescription;

    RSSHandler(ArrayList<String> summaries, ArrayList<String> titles) {
        super();
        this.titles = titles;
        this.summaries = summaries;
    }

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attrs) throws SAXException {
        buffer = "";
        if (localName.equals("feed")) {
            ITEM = "entry";
            TITLE = "title";
            DESCRIPTION = "summary";
            CONTENT = "content";
            ATOM = true;
        }

        if (localName.equals(ITEM)) {
            item = true;
        }

        if (localName.equals(TITLE)) {
            title = true;
        }

        if (localName.equals(DESCRIPTION) || localName.equals(CONTENT)) {
            description = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equals(ITEM)) {
            item = false;
            summaries.add(allDescription.trim().replaceAll("\n", "<br>"));
            allDescription = "";
        }

        if (localName.equals(TITLE)) {
            title = false;
            if (item == true)
                titles.add(buffer);
        }

        if (localName.equals(DESCRIPTION)) {
            description = false;
            if (item) {
                allDescription += buffer;
            }
        }

        if (localName.equals(CONTENT)) {
            description = false;
            if (item)
                allDescription += buffer;
        }
        buffer = "";
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if (item == true) {
            buffer += new String(ch, start, length);
        }
    }
}