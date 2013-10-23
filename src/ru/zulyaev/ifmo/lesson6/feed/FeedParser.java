package ru.zulyaev.ifmo.lesson6.feed;

import android.util.Log;
import ru.zulyaev.ifmo.lesson6.feed.atom.AtomFeed;
import ru.zulyaev.ifmo.lesson6.feed.rss1.Rss1Feed;
import ru.zulyaev.ifmo.lesson6.feed.rss2.Rss2Feed;
import ru.zulyaev.ifmo.lesson6.xml.XmlBinder;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * @author Никита
 */
public class FeedParser {
    @SuppressWarnings("unchecked")
    public static final List<Class<? extends Feed>> DEFAULT_FORMATS = Arrays.asList(
            AtomFeed.class,
            Rss1Feed.class,
            Rss2Feed.class
    );

    private List<Class<? extends Feed>> formats;

    public FeedParser(List<Class<? extends Feed>> formats) {
        this.formats = formats;
    }

    public FeedParser() {
        this(DEFAULT_FORMATS);
    }


    public Feed parse(String xml) throws IOException {
        XmlBinder binder = new XmlBinder();
        for (Class<? extends Feed> format : formats) {
            try {
                return binder.bind(format, new StringReader(xml));
            } catch (Exception e) {
                Log.d(FeedParser.class.toString(), "Not " + format.getName(), e);
            }
        }
        throw new ParseException("None of standards applies to this stream");
    }
}
