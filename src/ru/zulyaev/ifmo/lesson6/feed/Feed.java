package ru.zulyaev.ifmo.lesson6.feed;

import java.util.List;

/**
 * @author Никита
 */
public interface Feed {
    /**
     * required
     * @return Feed title
     */
    String getTitle();

    /**
     * required
     * @return Feed description
     */
    String getDescription();

    /**
     * required
     * @return Feed website link
     */
    String getLink();

    /**
     * required
     * @return List of feed entries
     */
    List<? extends Entry> getEntries();


    /**
     * <b>not</b> required
     * @return Feed language
     */
    String getLanguage();

    /**
     * <b>not</b> required
     * @return Feed image
     */
    String getImage();
}
