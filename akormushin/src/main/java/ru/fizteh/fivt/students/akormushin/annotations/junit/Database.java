package ru.fizteh.fivt.students.akormushin.annotations.junit;

import ru.fizteh.fivt.students.akormushin.annotations.xml.PrimaryKey;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kormushin on 22.09.15.
 */
public class Database<K, V> {

    private final Map<K, V> values = new HashMap<>();

    public void insert(V object) throws IllegalAccessException {
        System.out.println("Call remote database...");

        values.put(getPrimaryKey(object), object);
    }

    public V select(K key) throws IllegalAccessException {
        System.out.println("Call remote database...");

        return values.get(key);
    }

    public Collection<V> select() throws IllegalAccessException {
        System.out.println("Call remote database...");

        return new ArrayList<>(values.values());
    }

    public boolean exists(V object) throws IllegalAccessException {
        System.out.println("Call remote database...");

        return values.containsKey(getPrimaryKey(object));
    }

    private K getPrimaryKey(V object) throws IllegalAccessException {
        K primaryKey = null;
        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.getAnnotation(PrimaryKey.class) != null) {
                field.setAccessible(true);
                primaryKey = (K) field.get(object);

                break;
            }
        }

        if (primaryKey == null) {
            throw new IllegalArgumentException("PrimaryKey field not found");
        }

        return primaryKey;
    }

}
