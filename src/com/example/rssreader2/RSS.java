package com.example.rssreader2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class RSS {
    private List<RSSItem> items;

    private boolean isDownloaded;

    public void setDownloaded() {
        isDownloaded = true;
    }

    public boolean downloaded() {
        return isDownloaded;
    }

    private ArrayList<String> titles;
    private ArrayList<String> descriptions;

    public RSS() {
        items = new ArrayList<>();
        titles = new ArrayList<>();
        descriptions = new ArrayList<>();
        isDownloaded = false;
    }

    public void addItem(RSSItem it) {
        items.add(it);
        titles.add(it.getTitle());
        descriptions.add(it.getDescription());
    }

    public ArrayList<String> getTitles() {
        return titles;
    }

    public ArrayList<String> getDescriptions() {
        return descriptions;
    }

}
