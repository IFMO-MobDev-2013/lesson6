package ru.zulyaev.ifmo.lesson6.feed;

/**
 * @author Никита
 */
public interface Entry {
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
