package com.pr.parser.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

public class PHPSerializer {

    private static final Set<Object> visitedObjects = Collections.newSetFromMap(new IdentityHashMap<>());

    private PHPSerializer() {
    }

    public static String serializeObject(Object obj) throws IllegalAccessException {
        if (obj == null) {
            return "N;";
        }

        if (visitedObjects.contains(obj)) {
            return "N;";
        }
        visitedObjects.add(obj);

        StringBuilder serialized = new StringBuilder();
        Class<?> clazz = obj.getClass();

        if (obj instanceof String) {
            return serializeString((String) obj);
        } else if (obj instanceof Integer || obj instanceof Long || obj instanceof Short || obj instanceof Byte) {
            return serializeNumber((Number) obj);
        } else if (obj instanceof Float || obj instanceof Double) {
            return serializeDouble((Double) obj);
        } else if (obj instanceof Boolean) {
            return serializeBoolean((Boolean) obj);
        } else if (obj instanceof BigDecimal || obj instanceof BigInteger) {
            return serializeString(obj.toString());
        } else if (obj instanceof Instant) {
            return serializeString(((Instant) obj).toString());
        } else if (obj instanceof Map) {
            return serializeMap((Map<?, ?>) obj);
        } else if (obj.getClass().isArray()) {
            return serializeArray(obj);
        } else if (obj instanceof Collection) {
            return serializeCollection((Collection<?>) obj);
        }

        Field[] fields = clazz.getDeclaredFields();
        serialized.append("a:").append(fields.length).append(":{");

        for (Field field : fields) {
            field.setAccessible(true);

            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            String fieldName = field.getName();
            Object fieldValue = field.get(obj);

            serialized.append("s:").append(fieldName.length()).append(":\"").append(fieldName).append("\";");
            serialized.append(serializeValue(fieldValue));
        }

        serialized.append("}");
        visitedObjects.remove(obj);
        return serialized.toString();
    }

    private static String serializeValue(Object value) throws IllegalAccessException {
        if (value == null) {
            return "N;";
        }

        return serializeObject(value);
    }

    private static String serializeString(String value) {
        return "s:" + value.length() + ":\"" + value + "\";";
    }

    private static String serializeNumber(Number value) {
        return "i:" + value + ";";
    }

    // Serialization helper for floating-point numbers (Double)
    private static String serializeDouble(Double value) {
        return "d:" + value + ";";
    }

    private static String serializeBoolean(Boolean value) {
        return "b:" + (value ? "1" : "0") + ";";
    }

    private static String serializeMap(Map<?, ?> map) throws IllegalAccessException {
        StringBuilder serialized = new StringBuilder();
        serialized.append("a:").append(map.size()).append(":{");

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            serialized.append(serializeValue(entry.getKey()));
            serialized.append(serializeValue(entry.getValue()));
        }

        serialized.append("}");
        return serialized.toString();
    }

    private static String serializeArray(Object array) throws IllegalAccessException {
        int length = Array.getLength(array);
        StringBuilder serialized = new StringBuilder();
        serialized.append("a:").append(length).append(":{");

        for (int i = 0; i < length; i++) {
            serialized.append("i:").append(i).append(";");
            Object element = Array.get(array, i);
            serialized.append(serializeValue(element));
        }

        serialized.append("}");
        return serialized.toString();
    }

    private static String serializeCollection(Collection<?> collection) throws IllegalAccessException {
        StringBuilder serialized = new StringBuilder();
        serialized.append("a:").append(collection.size()).append(":{");

        int index = 0;
        for (Object element : collection) {
            serialized.append("i:").append(index++).append(";");
            serialized.append(serializeValue(element));
        }

        serialized.append("}");
        return serialized.toString();
    }
}
