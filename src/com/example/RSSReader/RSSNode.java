package com.example.RSSReader;

import android.os.Parcel;
import android.os.Parcelable;

public class RSSNode implements Parcelable {

    private String title;
    private String description;
    private String date;

    RSSNode () {}

    RSSNode (String title, String description, String date) {
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public int describeContents() {
        return 0;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void writeToParcel(Parcel parcel, int flags) {;
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(date);
    }

    public static final Parcelable.Creator<RSSNode> CREATOR = new Parcelable.Creator<RSSNode>() {
        public RSSNode createFromParcel(Parcel in) {
            return new RSSNode(in);
        }

        public RSSNode[] newArray(int size) {
            return new RSSNode[size];
        }
    };

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    private RSSNode(Parcel parcel) {
        this.title = parcel.readString();
        this.description = parcel.readString();
        this.date = parcel.readString();
    }
}
