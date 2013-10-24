package ru.zulyaev.ifmo.lesson6.feed.atom;

import ru.zulyaev.ifmo.lesson6.feed.Entry;
import ru.zulyaev.ifmo.lesson6.xml.Element;

/**
 * @author Никита
 */
public class AtomEntry implements Entry {
    @Element
    private String title;
    @Element
    private String summary;
    @Element
    private String link;

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return summary;
    }

    @Override
    public String getLink() {
        return link;
    }
}
