package ru.zulyaev.ifmo.lesson6.xml;

import java.lang.reflect.Field;

/**
 * @author Никита
 */
class CharArrayFieldBinder extends BaseFieldBinder implements TextFieldBinder {
    CharArrayFieldBinder(Field field) {
        super(field);
    }

    @Override
    public void bind(Object object, char[] chars, int offset, int length) throws IllegalAccessException {

    }
}
