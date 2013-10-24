package ru.ifmo.rssreader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {
    public static List<Article> parse(String xml) {
        if (xml == null) {
            return null;
        }
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            return null;
        }
        Document document;
        try {
            document = documentBuilder.parse(new InputSource(new StringReader(xml)));
        } catch (SAXException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        document.getDocumentElement().normalize();
        List<Article> result = tryRSS(document);
        if (result.size() == 0) {
            result = tryAtom(document);
        }
        return result;
    }

    private static List<Article> tryRSS(Document document) {
        return tryFormat(document, new String[]{"item", "title", "description"});
    }

    private static List<Article> tryAtom(Document document) {
        return tryFormat(document, new String[]{"entry", "title", "summary"});
    }

    private static List<Article> tryFormat(Document document, String[] keys) {
        NodeList nodeList = document.getElementsByTagName(keys[0]);
        ArrayList<Article> articles = new ArrayList<Article>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            NodeList titles = element.getElementsByTagName(keys[1]);
            NodeList text = element.getElementsByTagName(keys[2]);
            String article;
            try {
                article = text.item(0).getChildNodes().item(0).getNodeValue();
            } catch (NullPointerException exception) {
                article = "No description available";
            }
            articles.add(new Article(titles.item(0).getChildNodes().item(0).getNodeValue(), article));
        }
        return articles;
    }
}
