package ru.zulyaev.ifmo.lesson6.feed;

import java.io.Serializable;

/**
 * @author Никита
 */
public interface Entry extends Serializable {
    /**
     * At least one of title and description is required
     * @return Entry title
     */
    String getTitle();

    /**
     * At least one of title and description is required
     * @return Description title
     */
    String getDescription();

    /**
     * <b>not</b> required
     * @return Link to the entry
     */
    String getLink();
}
