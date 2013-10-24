package ru.zulyaev.ifmo.lesson6.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Никита
 */
class BatchBindHandler<T> extends DefaultHandler {
    private final int count;
    private final BindHandler<T>[] handlers;
    private final Exception[] exceptions;

    BatchBindHandler(List<? extends BindHandler<T>> handlers) {
        this.count = handlers.size();
        this.handlers = new BindHandler[count];
        handlers.toArray(this.handlers);
        exceptions = new Exception[count];
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        for (int i = 0; i < count; ++i) {
            if (exceptions[i] != null) {
                continue;
            }
            try {
                handlers[i].startElement(uri, localName, qName, attributes);
            } catch (Exception e) {
                exceptions[i] = e;
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        for (int i = 0; i < count; ++i) {
            if (exceptions[i] != null) {
                continue;
            }
            try {
                handlers[i].endElement(uri, localName, qName);
            } catch (Exception e) {
                exceptions[i] = e;
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        for (int i = 0; i < count; ++i) {
            if (exceptions[i] != null) {
                continue;
            }
            try {
                handlers[i].characters(ch, start, length);
            } catch (Exception e) {
                exceptions[i] = e;
            }
        }
    }

    List<T> getResults() {
        List<T> results = new ArrayList<T>(count);
        for (int i = 0; i < count; ++i) {
            if (exceptions[i] != null) {
                results.add(null);
                continue;
            }
            try {
                results.add(handlers[i].getResult());
            } catch (Exception e) {
                results.add(null);
                exceptions[i] = e;
            }
        }
        return results;
    }

    List<Exception> getExceptions() {
        return Arrays.asList(exceptions);
    }
}
