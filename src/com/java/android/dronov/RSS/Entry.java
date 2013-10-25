package com.java.android.dronov.RSS;

import org.w3c.dom.Element;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: dronov
 * Date: 16.10.13
 * Time: 16:23
 * To change this template use File | Settings | File Templates.
 */

public class Entry implements Serializable {
    private String title = null;
    private String description = null;
    private String link = null;
    private String publishedDate = null;

    public Entry() {}

    public Entry(String title, String description, String link, String publishedDate) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.publishedDate = publishedDate;
    }
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPublishedDate() {
        return this.publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
