package ru.zulyaev.ifmo.lesson6.xml;

import org.xml.sax.InputSource;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

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

    public <T> List<T> bind(List<Class<? extends T>> classes, Reader source) throws Exception {
        SAXParser parser = factory.newSAXParser();
        List<BindHandler<T>> handlers = new ArrayList<BindHandler<T>>();
        for (Class<? extends T> clazz : classes) {
            handlers.add(new BindHandler<T>(clazz));
        }
        BatchBindHandler<T> batch = new BatchBindHandler<T>(handlers);
        parser.parse(new InputSource(source), batch);
        return batch.getResults();
    }

    public <T> T bindFirst(List<Class<? extends T>> classes, Reader source) throws Exception {
        for (T t : bind(classes, source)) {
            if (t != null) {
                return t;
            }
        }
        throw new ParseException("None of standards applies to this stream");
    }
}
