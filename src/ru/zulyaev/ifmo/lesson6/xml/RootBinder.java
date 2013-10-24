package ru.zulyaev.ifmo.lesson6.xml;

/**
 * @author Никита
 */
class RootBinder<T> implements WiredBinder<T> {
    private final Class<? extends T> clazz;
    private final String name;
    private T result;

    RootBinder(Class<? extends T> clazz) {
        this.clazz = clazz;
        Root root = clazz.getAnnotation(Root.class);
        if (root == null) {
            throw new IllegalArgumentException("It's not a root");
        }
        name = root.name().isEmpty() ? clazz.getName() : root.name();
    }

    @Override
    public void bindAttribute(String name, Object value) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public void bindElement(String name, Object value) throws Exception {
        if (this.name.equals(name)) {
            result = (T) value;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void bindText(char[] chars, int offset, int length) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public WiredBinder<?> getNestedBinder(String element) throws Exception {
        if (!name.equals(element)) {
            throw new IllegalArgumentException();
        }
        return WiredBinderFactory.getInstance(clazz);
    }

    @Override
    public boolean hasAttribute(String name) {
        return false;
    }

    @Override
    public boolean hasElement(String name) {
        return this.name.equals(name);
    }

    @Override
    public boolean hasText() {
        return false;
    }

    @Override
    public T getResult() {
        return result;
    }
}
