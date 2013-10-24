package ru.zulyaev.ifmo.lesson6.xml;

/**
 * @author Никита
 */
class WiredClassBinder<T> implements WiredBinder<T> {
    private final ClassBinder<T> binder;
    private final T result;

    WiredClassBinder(Class<T> clazz, T result) throws IllegalAccessException {
        this.binder = ClassBinder.getInstance(clazz);
        this.result = result;
        this.binder.init(result);
    }

    WiredClassBinder(Class<T> clazz) throws IllegalAccessException, InstantiationException {
        this(clazz, clazz.newInstance());
    }

    @Override
    public void bindAttribute(String name, Object value) throws Exception {
        binder.bindAttribute(result, name, value);
    }

    @Override
    public void bindElement(String name, Object value) throws Exception {
        binder.bindElement(result, name, value);
    }

    @Override
    public void bindText(char[] chars, int offset, int length) throws Exception {
        binder.bindText(result, chars, offset, length);
    }

    @Override
    public boolean hasAttribute(String name) {
        return binder.hasAttribute(name);
    }

    @Override
    public boolean hasElement(String name) {
        return binder.hasElement(name);
    }

    @Override
    public boolean hasText() {
        return binder.hasText();
    }

    @Override
    public WiredBinder<?> getNestedBinder(String element) throws Exception {
        return binder.makeWiredBinderForElement(element);
    }

    @Override
    public T getResult() {
        try {
            if (!binder.isBindingSuccessful(result)) {
                throw new IllegalStateException("Bind was not successful");
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Bind was not successful");
        }
        return result;
    }
}
