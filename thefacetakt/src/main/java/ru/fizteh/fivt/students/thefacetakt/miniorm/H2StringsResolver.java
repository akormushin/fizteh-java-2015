package ru.fizteh.fivt.students.thefacetakt.miniorm;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by thefacetakt on 15.12.15.
 */
class H2StringsResolver {
    private static Map<Class, String> h2Mapper;
    static {
        h2Mapper = new HashMap<>();
        h2Mapper.put(Integer.class, "INTEGER");
        h2Mapper.put(Boolean.class, "BOOLEAN");
        h2Mapper.put(Byte.class, "TINYINT");
        h2Mapper.put(Short.class, "SMALLINT");
        h2Mapper.put(Long.class, "BIGINT");
        h2Mapper.put(Double.class, "DOUBLE");
        h2Mapper.put(Float.class, "FLOAT");
        h2Mapper.put(Time.class, "TIME");
        h2Mapper.put(Date.class, "DATE");
        h2Mapper.put(Timestamp.class, "TIMESTAMP");
        h2Mapper.put(Character.class, "CHAR");
        h2Mapper.put(String.class, "CLOB");
        h2Mapper.put(UUID.class, "UUID");
    }

    public static String resolve(Class clazz) {
        if (clazz.isArray()) {
            return "ARRAY";
        }
        if (h2Mapper.containsKey(clazz)) {
            return h2Mapper.get(clazz);
        }
        return "OTHER";
    }
}
