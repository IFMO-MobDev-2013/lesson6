package com.java.android.dronov.RSS;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: dronov
 * Date: 23.10.13
 * Time: 20:07
 * To change this template use File | Settings | File Templates.
 */
public class Feed implements Serializable {
    private String title = null;
    private String description = null;
    private String pubDate = null;
    private ArrayList<Entry> array = new ArrayList<Entry>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.title = description;
    }

    public ArrayList<Entry> getArray() {
        return array;
    }

    public void setArray(ArrayList<Entry> array) {
        this.array = array;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void add(Entry entry) {
        array.add(entry);
    }

}
