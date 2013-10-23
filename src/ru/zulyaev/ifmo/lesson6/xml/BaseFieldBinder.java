package ru.zulyaev.ifmo.lesson6.xml;

import java.lang.reflect.Field;

/**
 * @author Никита
 */
class BaseFieldBinder {
    final Field field;

    BaseFieldBinder(Field field) {
        this.field = field;
        field.setAccessible(true);
    }
}
