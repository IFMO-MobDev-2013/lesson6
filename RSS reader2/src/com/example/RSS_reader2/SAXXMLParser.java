package com.example.RSS_reader2;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class SAXXMLParser extends DefaultHandler {
    private boolean isTitle = false;
    private boolean isDescription = false;
    private boolean isLink = false;
    private boolean isPubDate = false;

    private String title = "";
    private String description = "";
    private String link = "";
    private String pubdate = "";

    public static final String TITLE = "TITLE";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String LINK = "LINK";
    public static final String PUBDATE = "PUBDATE";
    public static final String ITEM = "ITEM";
    public static int number;

    @Override
    public void startDocument() throws SAXException {
        number = 0;
        super.startDocument();
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, name, attributes);
        if (name.equalsIgnoreCase(TITLE)) {
            title = "";
            isTitle = true;
        }
        if (name.equalsIgnoreCase(DESCRIPTION)) {
            description = "";
            isDescription = true;
        }
        if (name.equalsIgnoreCase(LINK)) {
            link = "";
            isLink = true;
        }
        if (name.equalsIgnoreCase(PUBDATE)) {
            pubdate = "";
            isPubDate = true;
        }
    }

    @Override
    public void endElement(String uri, String localName,
                           String name) throws SAXException {
        if (name.equalsIgnoreCase(TITLE)) {
            isTitle = false;
        }
        if (name.equalsIgnoreCase(DESCRIPTION)) {
            isDescription = false;
        }
        if (name.equalsIgnoreCase(LINK)) {
            isLink = false;
        }
        if (name.equalsIgnoreCase(PUBDATE)) {
            isPubDate = false;
        }
        if (name.equalsIgnoreCase(ITEM)) {
            MainActivity.articlesDataBase.insertArticle(title, description, MainIntentWork.URLAdress, link, pubdate);
        }
    }
    @Override
    public void characters(char chars[], int start, int length) throws SAXException {
        if (isTitle == true) {
            title += new String(chars, start, length);
        }
        if (isDescription == true) {
            description += new String(chars, start, length);
        }
        if (isLink == true) {
            link += new String(chars, start, length);
        }
        if (isPubDate == true) {
            pubdate += new String(chars, start, length);
        }
    }

    @Override
    public void endDocument() throws SAXException {
        ++number;
        super.endDocument();
    }
}

