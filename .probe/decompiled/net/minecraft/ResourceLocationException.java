package net.minecraft;

import org.apache.commons.lang3.StringEscapeUtils;

public class ResourceLocationException extends RuntimeException {

    public ResourceLocationException(String string0) {
        super(StringEscapeUtils.escapeJava(string0));
    }

    public ResourceLocationException(String string0, Throwable throwable1) {
        super(StringEscapeUtils.escapeJava(string0), throwable1);
    }
}