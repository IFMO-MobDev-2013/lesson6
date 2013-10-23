package ru.zulyaev.ifmo.lesson6.xml;

/**
 * @author Никита
 */
interface TextFieldBinder {
    void bind(Object object, char[] chars, int offset, int length) throws IllegalAccessException;
}
