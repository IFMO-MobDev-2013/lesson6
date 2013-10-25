package com.java.android.dronov.RSS;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: dronov
 * Date: 17.10.13
 * Time: 20:59
 * To change this template use File | Settings | File Templates.
 */
public class FeedBack implements Serializable {
    private Exception exception;
    private ArrayList<Entry> array;

    public FeedBack(ArrayList<Entry> array, Exception exception) {
        this.exception = exception;
        this.array = array;
    }

    public Exception getException() {
        return exception;
    }

    public ArrayList<Entry> getArray() {
        return array;
    }

    public void setArray(ArrayList<Entry> array) {
        this.array = array;
    }
}
