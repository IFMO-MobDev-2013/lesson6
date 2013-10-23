package ru.zulyaev.ifmo.lesson6.feed.rss1;

import ru.zulyaev.ifmo.lesson6.feed.Entry;
import ru.zulyaev.ifmo.lesson6.feed.Feed;
import ru.zulyaev.ifmo.lesson6.xml.Element;
import ru.zulyaev.ifmo.lesson6.xml.ElementList;
import ru.zulyaev.ifmo.lesson6.xml.Root;

import java.util.List;

/**
 * @author Никита
 */
@Root(name = "rdf:RDF")
public class Rss1Feed implements Feed {
    @Element(required = true)
    private Rss1Channel channel;
    @Element
    private Rss1Image image;
    @ElementList(entry = "item", inline = true)
    private List<Rss1Item> items;

    @Override
    public String getTitle() {
        return channel.getTitle();
    }

    @Override
    public String getDescription() {
        return channel.getDescription();
    }

    @Override
    public String getLink() {
        return channel.getLink();
    }

    @Override
    public List<? extends Entry> getEntries() {
        return items;
    }

    @Override
    public String getLanguage() {
        return null;
    }

    @Override
    public String getImage() {
        return image.getUrl();
    }
}
