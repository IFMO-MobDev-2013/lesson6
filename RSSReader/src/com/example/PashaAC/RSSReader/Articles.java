package com.example.PashaAC.RSSReader;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class Articles {
    private String title = "";
    private String description = "";
    private String link = "";
    private String pubdate = "";

    private void GoodDescription() {
        int ind;
        while ((ind = description.indexOf("&quot;")) != -1) {
            String tmp1 = description.substring(0, ind);
            String tmp2 = description.substring(ind + 6, description.length());
            description = tmp1 + "\"" + tmp2;
        }
        while ((ind = description.indexOf("<br>")) != -1) {
            String tmp1 = description.substring(0, ind);
            String tmp2 = description.substring(ind + 4, description.length());
            description = tmp1 + "\n" + tmp2;
        }
    }
    public Articles(String title, String description, String link, String pubdate) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.pubdate = pubdate;
        GoodDescription();
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getPubDate() {
        return pubdate.substring(0, pubdate.indexOf("+"));
    }
    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return title + "...\n" + pubdate.substring(0, pubdate.indexOf("+"));
    }
}
