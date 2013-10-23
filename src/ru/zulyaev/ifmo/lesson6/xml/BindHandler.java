package ru.zulyaev.ifmo.lesson6.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Queue;

/**
 * @author Никита
 */
class BindHandler<T> extends DefaultHandler {
    private final Queue<WiredBinder<?>> binders = Collections.asLifoQueue(new ArrayDeque<WiredBinder<?>>());
    private final WiredBinder<T> rootBinder;

    private String skipTag;
    private int skipCount;

    public BindHandler(Class<T> clazz) throws Exception {
        this.rootBinder = new RootBinder<T>(clazz);
        this.binders.add(rootBinder);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            if (skipCount > 0) {
                if (skipTag.equals(qName)) {
                    skipCount++;
                }
                return;
            }
            WiredBinder<?> parent = binders.peek();
            if (!parent.hasElement(qName)) {
                skipCount = 1;
                skipTag = qName;
                return;
            }
            WiredBinder<?> child = parent.getNestedBinder(qName);
            for (int i = 0, len = attributes.getLength(); i < len; ++i) {
                String name = attributes.getQName(i);
                if (parent.hasAttribute(name)) {
                    parent.bindAttribute(name, attributes.getValue(i));
                }
            }
            binders.add(child);
        } catch (Exception e) {
            throw new SAXException(e);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        try {
            if (skipCount > 0) {
                if (skipTag.equals(qName)) {
                    --skipCount;
                }
                return;
            }
            WiredBinder<?> child = binders.poll();
            WiredBinder<?> parent = binders.peek();
            parent.bindElement(qName, child.getResult());
        } catch (Exception e) {
            throw new SAXException(e);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        try {
            if (skipCount > 0) {
                return;
            }
            WiredBinder<?> binder = binders.peek();
            if (binder.hasText()) {
                binder.bindText(ch, start, length);
            }
        } catch (Exception e) {
            throw new SAXException(e);
        }
    }

    public T getResult() {
        T result = rootBinder.getResult();
        if (result == null) {
            throw new IllegalStateException("No binding done");
        }
        return result;
    }
}
