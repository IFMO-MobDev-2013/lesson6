package ru.zulyaev.ifmo.lesson6.xml;

import java.io.IOException;

/**
 * @author Никита
 */
public class ParseException extends IOException {
    public ParseException(String detailMessage) {
        super(detailMessage);
    }
}
