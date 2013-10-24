package com.example.rssreader2;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;


public class RSSHandler extends DefaultHandler{
    // List of items parsed
    private RSS rssItems;

    private RSSItem currentItem;

    private boolean parsingTitle;

    private boolean parsingLink;

    private boolean isParsingDescription;

    public RSSHandler() {
        rssItems = new RSS();
    }

    // The StartElement method creates an empty RssItem object when an item start tag is being processed. When a title or link tag are being processed appropriate indicators are set to true.
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("item".equals(qName) || "entry".equals(qName)) {
            currentItem = new RSSItem();
        } else if ("title".equals(qName)) {
            parsingTitle = true;
        } else if ("link".equals(qName)) {
            parsingLink = true;
        } else if ("summary".equals(qName) || "description".equals(qName)) {
            isParsingDescription = true;
        }
    }
    // The EndElement method adds the  current RssItem to the list when a closing item tag is processed. It sets appropriate indicators to false -  when title and link closing tags are processed
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("item".equals(qName) || "entry".equals(qName)) {
            rssItems.addItem(currentItem);
            currentItem = null;
        } else if ("title".equals(qName)) {
            parsingTitle = false;
        } else if ("link".equals(qName)) {
            parsingLink = false;
        } else if ("summary".equals(qName) || "description".equals(qName)) {
            isParsingDescription = false;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (parsingTitle) {
            if (currentItem != null)
                currentItem.setTitle(new String(ch, start, length));
        } else if (parsingLink) {
            if (currentItem != null) {
                currentItem.setLink(new String(ch, start, length));
                parsingLink = false;
            }
        } else if (isParsingDescription) {
            if (currentItem != null) {
//                if (length > 10)
                currentItem.setDescription(currentItem.getDescription() + new String(ch, start, length));
            }
        }
    }

    public RSS getRSS() {
        RSS res = rssItems;

        rssItems = null;
        return res;
    }
}
