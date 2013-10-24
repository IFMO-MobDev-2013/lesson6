package ru.ifmo.rain.loboda.rss;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RSSParser {
    InputSource inputSource;

    RSSParser(InputStream inputStream, String charset) throws IOException {
        Reader reader;
        if (charset != null) {
            reader = new BufferedReader(new InputStreamReader(inputStream, charset));
        } else {
            reader = new BufferedReader(new InputStreamReader(inputStream));
        }
        inputSource = new InputSource(reader);
    }

    public List<RSSRecord> parse() throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        Handler handler = new Handler();
        saxParser.parse(inputSource, handler);
        List<RSSRecord> list = handler.getRecords();
        return list;
    }

    private class Handler extends DefaultHandler {
        List<RSSRecord> records;
        RSSRecord record;
        boolean needToRead;
        StringBuilder stringBuilder;

        public Handler() {
            records = new ArrayList<RSSRecord>();
            stringBuilder = new StringBuilder();
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            if (needToRead) {
                stringBuilder.append(ch, start, length);
            }
        }

        @Override
        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
            name = name.toLowerCase();
            super.startElement(uri, localName, name, attributes);
            if (name.equals("item") || name.equals("entry")) {
                record = new RSSRecord();
                return;
            }
            if (record != null) {
                if (name.equals("author")) {
                    record.setAuthor();
                    needToRead = true;
                }

                if (name.equals("description") || name.equals("content") || name.equals("summary")) {
                    record.setDescription();
                    needToRead = true;
                }

                if (name.equals("title")) {
                    record.setAnnotation();
                    needToRead = true;
                }

                if (name.equals("published") || name.equals("pubdate")) {
                    record.setDate();
                    needToRead = true;
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String name) throws SAXException {
            super.endElement(uri, localName, name);
            name = name.toLowerCase();
            if (needToRead) {
                String s = stringBuilder.toString();
                s = s.trim();
                needToRead = false;
                record.set(s);
                stringBuilder.setLength(0);
            }
            if (name.equals("entry") || name.equals("item")) {
                records.add(record);
                record = null;
            }
        }

        @Override
        public void endDocument() throws SAXException {
            if (records.size() == 0) {
                throw new SAXException();
            }
        }

        public List<RSSRecord> getRecords() {
            return records;
        }
    }
}
