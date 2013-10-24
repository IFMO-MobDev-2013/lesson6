package com.example.lesson6;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParserFactory;

import android.*;
import android.R;
import android.util.Log;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 * Created with IntelliJ IDEA.
 * User: slavian
 * Date: 24.10.13
 * Time: 17:43
 * To change this template use File | Settings | File Templates.
 */
public class SAXXMLParser {
    public static ArrayList<FeedItem> parse(String xml) {
        ArrayList<FeedItem> feed = null;


        try {

            XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();

            SAXXMLHandler saxHandler = new SAXXMLHandler();

            xmlReader.setContentHandler(saxHandler);

            InputSource inputSource = new InputSource();
            inputSource.setCharacterStream(new StringReader(xml));
            xmlReader.parse(inputSource);
            feed = saxHandler.getFeed();


        } catch (Exception ex) {
            Log.d("XML", "SAXXMLParser: parse() failed");   // TODO: unhandled exception
        }

        return feed;
    }
}
