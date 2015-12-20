package ru.fizteh.fivt.students.nmakeenkov.miniorm;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.RUNTIME)
public @interface Table {
    String name() default "";
}
