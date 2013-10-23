package ru.zulyaev.ifmo.lesson6.feed.rss1;

import ru.zulyaev.ifmo.lesson6.xml.Element;

/**
 * @author Никита
 */
public class Rss1Channel {
    @Element(required = true)
    private String title;
    @Element(required = true)
    private String description;
    @Element(required = true)
    private String link;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }
}
