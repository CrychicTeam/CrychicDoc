package ca.fxco.memoryleakfix.mixinextras.lib.apache.commons;

import java.io.Serializable;

public class ObjectUtils {

    public static final ObjectUtils.Null NULL = new ObjectUtils.Null();

    @Deprecated
    public static boolean equals(Object object1, Object object2) {
        if (object1 == object2) {
            return true;
        } else {
            return object1 != null && object2 != null ? object1.equals(object2) : false;
        }
    }

    public String toString() {
        return super.toString();
    }

    public static class Null implements Serializable {

        Null() {
        }
    }
}