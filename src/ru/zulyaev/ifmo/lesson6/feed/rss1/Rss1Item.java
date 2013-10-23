package ru.zulyaev.ifmo.lesson6.feed.rss1;

import ru.zulyaev.ifmo.lesson6.feed.Entry;
import ru.zulyaev.ifmo.lesson6.xml.Element;

/**
 * @author Никита
 */
public class Rss1Item implements Entry {
    @Element(required = true)
    private String title;
    @Element(required = true)
    private String link;
    @Element
    private String description;

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getLink() {
        return link;
    }
}
