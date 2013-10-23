package ru.georgeee.android.gfeedreader.utility.model;

import android.os.SystemClock;
import org.apache.http.impl.cookie.DateUtils;
import ru.georgeee.android.gfeedreader.utility.xml.FeedReaderTask;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: georgeee
 * Date: 12.10.13
 * Time: 4:11
 * To change this template use File | Settings | File Templates.
 */
public class Feed implements Serializable{
    protected WebString title;
    protected String url;
    protected String iconUrl;
    protected String logoUrl;
    protected WebString description;

    protected ArrayList<Entry> entries = new ArrayList<Entry>();

    public void addEntry(Entry entry){
        entries.add(entry);
    }

    public Date getLastUpdated(){
        return new Date(SystemClock.currentThreadTimeMillis());
    }

    public Entry[] getAllEntries(){
        Entry[] entriesArray = new Entry[entries.size()];
        return entries.toArray(entriesArray);
    }

    public WebString getTitle() {
        return title;
    }

    public void setTitle(WebString title) {
        this.title = title;
    }
    public void setTitle(String description) {
        setTitle(new WebString(description));
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public WebString getDescription() {
        return description;
    }

    public void setDescription(WebString description) {
        this.description = description;
    }
    public void setDescription(String description) {
        setDescription(new WebString(description));
    }

    public void save(){
        feeds.add(this);
    }

    @Override
    public String toString() {
        return "Feed{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", logoUrl='" + logoUrl + '\'' +
                ", description='" + description + '\'' +
                ", entries=" + entries +
                '}';
    }

    static protected ArrayList<Feed> feeds = new ArrayList<Feed>();
    public static Feed[] getAllFeeds(){
        Feed[] feedsArray = new Feed[feeds.size()];
        return feeds.toArray(feedsArray);
    }

}
