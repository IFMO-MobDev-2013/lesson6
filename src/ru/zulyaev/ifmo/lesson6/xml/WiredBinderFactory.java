package ru.zulyaev.ifmo.lesson6.xml;

/**
 * @author Никита
 */
class WiredBinderFactory {
    @SuppressWarnings("unchecked")
    static <T> WiredBinder<T> getInstance(Class<T> clazz) throws InstantiationException, IllegalAccessException {
        if (clazz == String.class) {
            return (WiredBinder) new WiredStringBinder();
        }
        return new WiredClassBinder<T>(clazz);
    }
}
