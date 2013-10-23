package ru.zulyaev.ifmo.lesson6.xml;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Никита
 */
class ListWiredBinder<T> implements WiredBinder<List<T>> {
    private final String entry;
    private final Class<T> clazz;
    private final List<T> result = new ArrayList<T>();

    static <T> ListWiredBinder<T> newInstance(String entry, Class<T> clazz) {
        return new ListWiredBinder<T>(entry, clazz);
    }

    private ListWiredBinder(String entry, Class<T> clazz) {
        this.entry = entry;
        this.clazz = clazz;
    }

    @Override
    public void bindAttribute(String name, Object value) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void bindElement(String name, Object value) throws Exception {
        if (!entry.equals(name)) {
            throw new UnsupportedOperationException();
        }
        result.add((T)value);
    }

    @Override
    public void bindText(char[] chars, int offset, int length) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public WiredBinder<?> getNestedBinder(String element) throws Exception {
        return WiredBinderFactory.getInstance(clazz);
    }

    @Override
    public boolean hasAttribute(String name) {
        return false;
    }

    @Override
    public boolean hasElement(String name) {
        return entry.equals(name);
    }

    @Override
    public boolean hasText() {
        return false;
    }

    @Override
    public List<T> getResult() {
        return result;
    }
}
