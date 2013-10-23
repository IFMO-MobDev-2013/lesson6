package ru.zulyaev.ifmo.lesson6.util;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Никита
 */
public class Utils {
    public static <T> ArrayList<T> asArrayList(T... ts) {
        ArrayList<T> result = new ArrayList<T>(ts.length);
        Collections.addAll(result, ts);
        return result;
    }
}
