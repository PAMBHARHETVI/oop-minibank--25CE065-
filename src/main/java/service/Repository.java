package service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import model.annotation.Id;

public class Repository<T> {
    private final Map<String, T> data = new HashMap<>();

    public synchronized T save(T entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity cannot be null.");
        String id = extractId(entity);
        data.put(id, entity);
        return entity;
    }

    @SafeVarargs
    public final synchronized void saveAll(T... items) {
        if (items == null)
            throw new IllegalArgumentException("Items cannot be null.");
        for (T item : items)
            save(item);
    }

    public synchronized T findById(String id) {
        return data.get(id);
    }

    public synchronized ArrayList<T> findAll() {
        return new ArrayList<>(data.values());
    }

    public synchronized boolean delete(String id) {
        return data.remove(id) != null;
    }

    private String extractId(T entity) {
        for (Class<?> type = entity.getClass(); type != null; type = type.getSuperclass()) {
            for (Field field : type.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    try {
                        field.setAccessible(true);
                        Object value = field.get(entity);
                        if (value == null)
                            throw new IllegalArgumentException("@Id value cannot be null.");
                        return value.toString();
                    } catch (ReflectiveOperationException e) {
                        throw new IllegalStateException("Could not read @Id field.", e);
                    }
                }
            }
        }
        throw new IllegalArgumentException("Entity type " + entity.getClass().getName() + " has no @Id field.");
    }

    public static <N extends Number> double average(N[] values) {
        if (values == null || values.length == 0)
            return 0.0;
        double sum = 0;
        for (N value : values) {
            if (value == null)
                throw new IllegalArgumentException("Values cannot contain null.");
            sum += value.doubleValue();
        }
        return sum / values.length;
    }
}
