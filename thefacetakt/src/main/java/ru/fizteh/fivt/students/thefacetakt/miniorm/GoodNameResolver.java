package ru.fizteh.fivt.students.thefacetakt.miniorm;

/**
 * Created by thefacetakt on 15.12.15.
 */
class GoodNameResolver {
    static final String REGEX = "[A-Za-z0-9_-]*";
    public static Boolean isGood(String name) {
        return name.matches(REGEX);
    }
}
