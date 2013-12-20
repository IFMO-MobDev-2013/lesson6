package com.example.RSS_reader2;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;

public class Article implements Parcelable, Serializable {
    private final String title;
    private final String description;
    private final String link;
    private final String pubdate;

    private String GoodTextWithoutHTMLTags(String someString) {
        if (someString.indexOf("<p>") != -1)  someString = someString.replaceAll("<p>", "");
        if (someString.indexOf("</p>") != -1)  someString = someString.replaceAll("</p>", "");
        if (someString.indexOf("<br>") != -1)  someString =someString.replaceAll("<br>", "\n");
        if (someString.indexOf("<br/>") != -1)  someString = someString.replaceAll("<br/>", "\n");
        if (someString.indexOf("&amp;") != -1)  someString = someString.replaceAll("&amp;", "&");
        if (someString.indexOf("&quot;") != -1) someString = someString.replaceAll("&quot;", "\"");
        if (someString.indexOf("&gt;") != -1)  someString = someString.replaceAll("&gt;", ">");
        if (someString.indexOf("&lt;") != -1)  someString = someString.replaceAll("&lt;", "<");
        if (someString.indexOf("&apos;") != -1)  someString = someString.replaceAll("&apos;", "'");
        return someString;
    }
    public Article(String title, String description, String link, String pubdate) {
        this.title = GoodTextWithoutHTMLTags(title);
        this.description = GoodTextWithoutHTMLTags(description);
        this.link = link;
        if (pubdate.indexOf("+") != -1)
            this.pubdate = pubdate.substring(0, pubdate.indexOf(" +"));
        else
            this.pubdate = pubdate;
    }
    public final String getTitle() {
        return title;
    }
    public final String getDescription() {
        return description;
    }
    public final String getPubDate() {
        return pubdate;
    }
    public final String getLink() {
        return link;
    }

    public int describeContents() {
        return 0;
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        // unpacking objecst from Parcel
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(link);
        parcel.writeString(pubdate);
    }

    public Article(Parcel parcel) {
        Log.d("constructor of Articles", "MyObject(Parcel parcel)");
        title = parcel.readString();
        description = parcel.readString();
        link = parcel.readString();
        pubdate = parcel.readString();
    }

    @Override
    public String toString() {
        return title + "...\n" + pubdate;
    }
}