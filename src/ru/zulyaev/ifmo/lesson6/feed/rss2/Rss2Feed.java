package ru.zulyaev.ifmo.lesson6.feed.rss2;

import ru.zulyaev.ifmo.lesson6.feed.Feed;
import ru.zulyaev.ifmo.lesson6.xml.Element;
import ru.zulyaev.ifmo.lesson6.xml.Root;

import java.util.List;

/**
 * @author Никита
 */
@Root(name = "rss")
public class Rss2Feed implements Feed {
    @Element(required = true)
    private Rss2Channel channel;

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
    public List<Rss2Item> getEntries() {
        return channel.getEntries();
    }

    @Override
    public String getLanguage() {
        return channel.getLanguage();
    }

    @Override
    public String getImage() {
        return channel.getImage();
    }
}
