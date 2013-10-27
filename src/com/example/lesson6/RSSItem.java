package com.example.lesson6;

public class RSSItem {

    private String title;
    private String description;
    private String pubDate;
    private String link;
    private boolean open;

    public RSSItem(String title, String description, String pubDate, String link) {
        this.title = title;
        this.description = description;
        this.pubDate = pubDate;
        this.link = link;
        this.open = false;
    }

    public void setOpen(){
        this.open = true;
    }

    public boolean getOpen(){
        return this.open;
    }

    public String getTitle()
    {
        return this.title;
    }

    public String getLink()
    {
        return this.link;
    }

    public String getDescription()
    {
        return this.description;
    }

    public String getPubDate()
    {
        return this.pubDate;
    }

    @Override
    public String toString() {
        return getTitle() + "  ( " + getPubDate() + " )";
    }

    public static void getRssItems(String feedUrl) {
        new MyAsyncTask().execute(feedUrl);
    }

}