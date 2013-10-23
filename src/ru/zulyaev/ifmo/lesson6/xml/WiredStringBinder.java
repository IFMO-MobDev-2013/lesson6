package ru.zulyaev.ifmo.lesson6.xml;

/**
 * @author Никита
 */
class WiredStringBinder implements WiredBinder<String> {
    private StringBuilder result = new StringBuilder();

    @Override
    public void bindAttribute(String name, Object value) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public void bindElement(String name, Object value) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public void bindText(char[] chars, int offset, int length) throws Exception {
        result.append(chars, offset, length);
    }

    @Override
    public boolean hasAttribute(String name) {
        return false;
    }

    @Override
    public boolean hasElement(String name) {
        return false;
    }

    @Override
    public boolean hasText() {
        return true;
    }

    @Override
    public WiredBinder<?> getNestedBinder(String element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getResult() {
        return result.toString();
    }
}
