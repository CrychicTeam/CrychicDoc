package dev.xkmc.l2library.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE })
public @interface DataGenOnly {
}