package ru.fizteh.fivt.students.akormushin.annotations.xml;

import java.lang.annotation.*;

/**
 * Created by kormushin on 22.09.15.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface PrimaryKey {

    String column() default "";

    boolean unique() default true;

}
