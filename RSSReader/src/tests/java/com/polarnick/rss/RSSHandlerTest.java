package com.polarnick.rss;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.List;

/**
 * Date: 13.10.13
 *
 * @author Nickolay Polyarniy aka PolarNick
 */
public class RSSHandlerTest {

    @Test
    public void testBashIm() throws Exception  {
        testFeedByUrl("http://bash.im/rss/");
    }

    @Test
    public void testLentaRu() throws Exception  {
        testFeedByUrl("http://lenta.ru/rss");
    }

    public void testFeedByUrl(String url) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();

        RSSHandler handler = new RSSHandler();
        HttpGet getRequest = new HttpGet(url);
        HttpResponse httpResponse = new DefaultHttpClient().execute(getRequest);
        parser.parse(httpResponse.getEntity().getContent(), handler);

        Feed feed = handler.retrieveFeed();
        Assert.assertNotNull(feed);

        checkStringNotEmpty(feed.getDescription());
        checkStringNotEmpty(feed.getTitle());

        List<FeedEntry> entries = feed.getEntries();
        Assert.assertNotNull(entries);
        Assert.assertTrue(entries.size() > 0);
        for(FeedEntry entry : entries)  {
            checkStringNotEmpty(entry.getLink());
            checkStringNotEmpty(entry.getTitle());
            checkStringNotEmpty(entry.getDescription());
            Assert.assertNotNull(entry.getPublishedDate());
        }
    }

    private void checkStringNotEmpty(String value) {
        Assert.assertNotNull(value);
        Assert.assertFalse(value.isEmpty());
    }
}
