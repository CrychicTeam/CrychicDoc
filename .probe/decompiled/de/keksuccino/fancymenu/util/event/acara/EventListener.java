package de.keksuccino.fancymenu.util.event.acara;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EventListener {

    int priority() default 0;
}