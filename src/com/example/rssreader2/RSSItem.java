package com.example.rssreader2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * User: elena
 * Date: 24.10.13
 * Time: 19:07
 * To change this template use File | Settings | File Templates.
 */
public class RSSItem {
    // item title
    private String title;
    // item link
    private String link;

    private String description;


    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }

    public void setDescription(String d) {
        description = d;
    }
    public String getDescription() {
        if (description == null) return "";
        return description;
    }
    @Override
    public String toString() {
        return title;
    }
}
