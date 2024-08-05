package de.keksuccino.konkrete.json.jsonpath;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class TypeRef<T> implements Comparable<TypeRef<T>> {

    protected final Type type;

    protected TypeRef() {
        Type superClass = this.getClass().getGenericSuperclass();
        if (superClass instanceof Class) {
            throw new IllegalArgumentException("No type info in TypeRef");
        } else {
            this.type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
        }
    }

    public Type getType() {
        return this.type;
    }

    public int compareTo(TypeRef<T> o) {
        return 0;
    }
}