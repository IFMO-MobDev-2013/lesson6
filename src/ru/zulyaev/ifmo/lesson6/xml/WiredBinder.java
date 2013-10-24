package ru.zulyaev.ifmo.lesson6.xml;

/**
 * @author Никита
 */
interface WiredBinder<T> {
    void bindAttribute(String name, Object value) throws Exception;
    void bindElement(String name, Object value) throws Exception;
    void bindText(char[] chars, int offset, int length) throws Exception;

    WiredBinder<?> getNestedBinder(String element) throws Exception;

    boolean hasAttribute(String name);
    boolean hasElement(String name);
    boolean hasText();

    T getResult();
}
