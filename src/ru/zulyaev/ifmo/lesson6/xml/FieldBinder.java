package ru.zulyaev.ifmo.lesson6.xml;

import java.lang.reflect.Field;

/**
 * @author Никита
 */
class FieldBinder extends BaseFieldBinder implements MemberBinder {
    FieldBinder(Field field) {
        super(field);
    }

    @Override
    public void bind(Object object, Object value) throws IllegalAccessException {
        field.set(object, value);
    }
}
