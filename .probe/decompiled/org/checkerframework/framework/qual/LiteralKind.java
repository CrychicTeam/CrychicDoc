package org.checkerframework.framework.qual;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum LiteralKind {

    NULL,
    INT,
    LONG,
    FLOAT,
    DOUBLE,
    BOOLEAN,
    CHAR,
    STRING,
    ALL,
    PRIMITIVE;

    public static List<LiteralKind> allLiteralKinds() {
        List<LiteralKind> list = new ArrayList(Arrays.asList(values()));
        list.remove(ALL);
        list.remove(PRIMITIVE);
        return list;
    }

    public static List<LiteralKind> primitiveLiteralKinds() {
        return Arrays.asList(INT, LONG, FLOAT, DOUBLE, BOOLEAN, CHAR);
    }
}