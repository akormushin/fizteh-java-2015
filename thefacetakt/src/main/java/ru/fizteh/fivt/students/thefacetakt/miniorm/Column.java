package ru.fizteh.fivt.students.thefacetakt.miniorm;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by thefacetakt on 15.12.15.
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String name() default "";
}
