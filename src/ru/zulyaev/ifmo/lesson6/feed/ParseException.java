package ru.zulyaev.ifmo.lesson6.feed;

import java.io.IOException;

/**
 * @author Никита
 */
public class ParseException extends IOException {
    public ParseException(String detailMessage) {
        super(detailMessage);
    }
}
