package ru.zulyaev.ifmo.lesson6.feed.rss2;

import ru.zulyaev.ifmo.lesson6.feed.Feed;
import ru.zulyaev.ifmo.lesson6.xml.Element;
import ru.zulyaev.ifmo.lesson6.xml.ElementList;

import java.util.List;

/**
 * @author Никита
 */
public class Rss2Channel implements Feed {
    @Element(required = true)
    private String title;
    @Element(required = true)
    private String description;
    @Element(required = true)
    private String link;
    @ElementList(entry = "item", inline = true)
    private List<Rss2Item> entries;

    @Element
    private String language;
    @Element
    private Rss2Image image;

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

    @Override
    public List<Rss2Item> getEntries() {
        return entries;
    }

    @Override
    public String getLanguage() {
        return language;
    }

    @Override
    public String getImage() {
        return image.getUrl();
    }
}
