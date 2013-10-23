package ru.zulyaev.ifmo.lesson6.feed.atom;

import ru.zulyaev.ifmo.lesson6.feed.Feed;
import ru.zulyaev.ifmo.lesson6.xml.Element;
import ru.zulyaev.ifmo.lesson6.xml.ElementList;
import ru.zulyaev.ifmo.lesson6.xml.Root;

import java.util.List;

/**
 * @author Никита
 */
@Root(name = "feed")
public class AtomFeed implements Feed {
    @Element(required = true)
    private String title;
    @ElementList(entry = "entry", inline = true)
    private List<AtomEntry> entries;
    @Element
    private String link;

    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getLink() {
        return link;
    }

    public List<AtomEntry> getEntries() {
        return entries;
    }

    @Override
    public String getLanguage() {
        return null;
    }

    @Override
    public String getImage() {
        return null;
    }
}
