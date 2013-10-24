package ru.zulyaev.ifmo.lesson6.xml;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Никита
 */
class ClassBinder<T> {
    private static final ConcurrentHashMap<Class, ClassBinder> CLASS_BINDER_CACHE = new ConcurrentHashMap<Class, ClassBinder>();
    private static final ConcurrentHashMap<Field, MemberBinder> FIELD_BINDER_CACHE = new ConcurrentHashMap<Field, MemberBinder>();
    private static final ConcurrentHashMap<Field, TextFieldBinder> TEXT_BINDER_CACHE = new ConcurrentHashMap<Field, TextFieldBinder>();

    static <T> ClassBinder<T> getInstance(Class<T> clazz) {
        @SuppressWarnings("unchecked")
        ClassBinder<T> result = CLASS_BINDER_CACHE.get(clazz);
        if (result == null) {
            CLASS_BINDER_CACHE.put(clazz, result = new ClassBinder<T>(clazz));
        }
        return result;
    }

    private final Map<String, MemberBinder> elements = new HashMap<String, MemberBinder>();
    private final Map<String, MemberBinder> attributes = new HashMap<String, MemberBinder>();
    private final Map<String, String> elementLists = new HashMap<String, String>();
    private final List<Field> lists = new ArrayList<Field>();
    private TextFieldBinder textBinder;
    private final Set<Field> requiredFields = new HashSet<Field>();

    private final Map<String, Class<?>> types = new HashMap<String, Class<?>>();

    private ClassBinder(Class<T> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            Class<?> type = field.getType();
            String fieldName = field.getName();
            boolean required = false;
            {
                Element element = field.getAnnotation(Element.class);
                if (element != null) {
                    String name = chooseName(element.name(), fieldName);
                    elements.put(name, getFieldBinder(field));
                    types.put(name, type);
                    required |= element.required();
                }
            }
            {
                Attribute attribute = field.getAnnotation(Attribute.class);
                if (attribute != null) {
                    String name = chooseName(attribute.name(), fieldName);
                    attributes.put(name, getFieldBinder(field));
                    types.put(name, type);
                    required |= attribute.required();
                }
            }
            {
                ElementList elementList = field.getAnnotation(ElementList.class);
                if (elementList != null) {
                    ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                    Class<?> entryType = (Class<?>) parameterizedType.getActualTypeArguments()[0];

                    String name = chooseName(elementList.name(), fieldName);
                    String entry = elementList.entry();
                    boolean inline = elementList.inline();

                    if (inline) {
                        elements.put(entry, getListFieldBinder(field));
                        types.put(entry, entryType);
                    } else {
                        elementLists.put(name, entry);
                        elements.put(name, getFieldBinder(field));
                        types.put(name, entryType);
                    }
                    lists.add(field);
                }
            }
            {
                Text text = field.getAnnotation(Text.class);
                if (text != null) {
                    textBinder = getTextFieldBinder(field, type);
                    required |= text.required();
                }
            }
            if (required) {
                requiredFields.add(field);
            }
        }
    }

    private MemberBinder getFieldBinder(Field field) {
        MemberBinder result = FIELD_BINDER_CACHE.get(field);
        if (result == null) {
            FIELD_BINDER_CACHE.put(field, result = new FieldBinder(field));
        }
        return result;
    }

    private MemberBinder getListFieldBinder(Field field) {
        MemberBinder result = FIELD_BINDER_CACHE.get(field);
        if (result == null) {
            FIELD_BINDER_CACHE.put(field, result = new ListFieldBinder(field));
        }
        return result;
    }

    private TextFieldBinder getTextFieldBinder(Field field, Class<?> clazz) {
        TextFieldBinder result = TEXT_BINDER_CACHE.get(field);
        if (result == null) {
            if (clazz == String.class) {
                result = new StringFieldBinder(field);
            } else if (clazz == char[].class) {
                result = new CharArrayFieldBinder(field);
            } else {
                throw new IllegalArgumentException("Cannot create text binder for class: " + clazz);
            }
            TEXT_BINDER_CACHE.put(field, result);
        }
        return result;
    }

    private static String chooseName(String a, String b) {
        return a != null && !a.isEmpty() ? a : b;
    }

    WiredBinder<?> makeWiredBinderForElement(String name) throws Exception {
        Class<?> type = types.get(name);
        if (elementLists.containsKey(name)) {
            String entry = elementLists.get(name);
            return ListWiredBinder.newInstance(entry, type);
        }
        return WiredBinderFactory.getInstance(type);
    }

    void bindAttribute(T self, String name, Object value) throws Exception {
        attributes.get(name).bind(self, value);
    }

    void bindElement(T self, String name, Object value) throws Exception {
        elements.get(name).bind(self, value);
    }

    void bindText(T self, char[] chars, int offset, int length) throws Exception {
        textBinder.bind(self, chars, offset, length);
    }

    boolean hasAttribute(String name) {
        return attributes.containsKey(name);
    }

    boolean hasElement(String name) {
        return elements.containsKey(name);
    }

    boolean hasText() {
        return textBinder != null;
    }

    void init(T result) throws IllegalAccessException {
        for (Field list : lists) {
            list.setAccessible(true);
            list.set(result, new ArrayList());
        }
    }

    boolean isBindingSuccessful(T object) throws IllegalAccessException {
        for (Field required : requiredFields) {
            if (required.get(object) == null) {
                return false;
            }
        }
        return true;
    }
}
