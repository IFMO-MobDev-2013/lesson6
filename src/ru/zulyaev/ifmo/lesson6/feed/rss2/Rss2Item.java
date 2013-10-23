package ru.zulyaev.ifmo.lesson6.feed.rss2;

import ru.zulyaev.ifmo.lesson6.feed.Entry;
import ru.zulyaev.ifmo.lesson6.xml.Element;

/**
 * @author Никита
 */
public class Rss2Item implements Entry {
    @Element
    private String title;
    @Element
    private String description;
    @Element
    private String link;

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
