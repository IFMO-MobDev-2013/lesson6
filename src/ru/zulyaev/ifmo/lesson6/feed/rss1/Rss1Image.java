package ru.zulyaev.ifmo.lesson6.feed.rss1;

import ru.zulyaev.ifmo.lesson6.xml.Element;

import java.io.Serializable;

/**
 * @author Никита
 */
public class Rss1Image implements Serializable {
    @Element(required = true)
    private String url;

    public String getUrl() {
        return url;
    }
}
