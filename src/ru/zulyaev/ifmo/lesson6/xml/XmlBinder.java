package ru.zulyaev.ifmo.lesson6.xml;

import org.xml.sax.InputSource;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.Reader;

/**
 * @author Никита
 */
public class XmlBinder {
    private SAXParserFactory factory = SAXParserFactory.newInstance();
    {
        factory.setValidating(false);
    }

    public <T> T bind(Class<T> clazz, Reader source) throws Exception {
        SAXParser parser = factory.newSAXParser();
        BindHandler<T> handler = new BindHandler<T>(clazz);
        parser.parse(new InputSource(source), handler);
        return handler.getResult();
    }
}
