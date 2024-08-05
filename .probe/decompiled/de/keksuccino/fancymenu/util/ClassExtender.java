package de.keksuccino.fancymenu.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface ClassExtender {

    Class<?>[] value();
}