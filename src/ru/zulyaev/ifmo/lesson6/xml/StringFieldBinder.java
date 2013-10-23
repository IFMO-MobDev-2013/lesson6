package ru.zulyaev.ifmo.lesson6.xml;

import java.lang.reflect.Field;

/**
 * @author Никита
 */
class StringFieldBinder extends BaseFieldBinder implements TextFieldBinder {
    StringFieldBinder(Field field) {
        super(field);
    }

    @Override
    public void bind(Object object, char[] chars, int offset, int length) throws IllegalAccessException {
        field.set(object, new String(chars, offset, length));
    }
}
