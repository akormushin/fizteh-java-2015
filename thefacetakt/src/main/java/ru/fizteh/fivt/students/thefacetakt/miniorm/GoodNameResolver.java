package ru.fizteh.fivt.students.thefacetakt.miniorm;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by thefacetakt on 15.12.15.
 */
class GoodNameResolver {
    private static Set<String> h2Keywords;
    static {
        h2Keywords = new HashSet<>();
        h2Keywords.add("CROSS");
        h2Keywords.add("CURRENT_DATE");
        h2Keywords.add("CURRENT_TIME");
        h2Keywords.add("CURRENT_TIMESTAMP");
        h2Keywords.add("DISTINCT");
        h2Keywords.add("EXCEPT");
        h2Keywords.add("EXISTS");
        h2Keywords.add("FALSE");
        h2Keywords.add("FETCH");
        h2Keywords.add("FOR");
        h2Keywords.add("FROM");
        h2Keywords.add("FULL");
        h2Keywords.add("GROUP");
        h2Keywords.add("HAVING");
        h2Keywords.add("INNER");
        h2Keywords.add("INTERSECT");
        h2Keywords.add("IS");
        h2Keywords.add("JOIN");
        h2Keywords.add("LIKE");
        h2Keywords.add("LIMIT");
        h2Keywords.add("MINUS");
        h2Keywords.add("NATURAL");
        h2Keywords.add("NOT");
        h2Keywords.add("NULL");
        h2Keywords.add("OFFSET");
        h2Keywords.add("ON");
        h2Keywords.add("ORDER");
        h2Keywords.add("PRIMARY");
        h2Keywords.add("ROWNUM");
        h2Keywords.add("SELECT");
        h2Keywords.add("SYSDATE");
        h2Keywords.add("SYSTIME");
        h2Keywords.add("SYSTIMESTAMP");
        h2Keywords.add("TODAY");
        h2Keywords.add("TRUE");
        h2Keywords.add("UNION");
        h2Keywords.add("UNIQUE");
        h2Keywords.add("WHERE");
    }

    static final Pattern p = Pattern.compile("[A-Za-z_-]*");
    public static Boolean isGood(String name) {
        return !h2Keywords.contains(name) && p.matcher(name).find();
    }
}
