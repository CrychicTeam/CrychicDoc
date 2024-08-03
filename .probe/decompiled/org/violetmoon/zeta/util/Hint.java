package org.violetmoon.zeta.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Hint {

    String value() default "";

    boolean negate() default false;

    String key() default "";

    String[] content() default { "" };
}