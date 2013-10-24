package ru.zulyaev.ifmo.lesson6.xml;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author Никита
 */
class ListFieldBinder extends BaseFieldBinder implements MemberBinder {
    ListFieldBinder(Field field) {
        super(field);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void bind(Object object, Object value) throws IllegalAccessException {
        List list = (List) field.get(object);
        list.add(value);
    }
}
