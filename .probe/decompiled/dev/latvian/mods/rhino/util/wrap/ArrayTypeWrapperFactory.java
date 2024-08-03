package dev.latvian.mods.rhino.util.wrap;

import dev.latvian.mods.rhino.Context;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;

public class ArrayTypeWrapperFactory<T> implements TypeWrapperFactory<T[]> {

    public final TypeWrapper<T> typeWrapper;

    public final Class<T> target;

    public final Class<T[]> arrayTarget;

    private final T[] emptyArray;

    public ArrayTypeWrapperFactory(TypeWrapper<T> tw, Class<T> t, Class<T[]> at) {
        this.typeWrapper = tw;
        this.target = t;
        this.arrayTarget = at;
        this.emptyArray = (T[]) ((Object[]) Array.newInstance(this.target, 0));
    }

    public T[] wrap(Context cx, Object o) {
        if (o == null) {
            return this.emptyArray;
        } else if (!(o instanceof Iterable)) {
            if (o.getClass().isArray()) {
                int size = Array.getLength(o);
                if (size == 0) {
                    return this.emptyArray;
                } else {
                    T[] array = (T[]) ((Object[]) Array.newInstance(this.target, size));
                    int index = 0;
                    for (int i = 0; i < array.length; i++) {
                        Object o1 = Array.get(o, i);
                        if (this.typeWrapper.validator.test(o1)) {
                            array[index] = this.typeWrapper.factory.wrap(cx, o1);
                            index++;
                        }
                    }
                    return (T[]) (index == 0 ? this.emptyArray : (index == array.length ? array : Arrays.copyOf(array, index, this.arrayTarget)));
                }
            } else if (this.typeWrapper.validator.test(o)) {
                T[] array = (T[]) ((Object[]) Array.newInstance(this.target, 1));
                array[0] = this.typeWrapper.factory.wrap(cx, o);
                return array;
            } else {
                return this.emptyArray;
            }
        } else {
            int size;
            if (o instanceof Collection) {
                size = ((Collection) o).size();
            } else {
                size = 0;
                for (Object o1 : (Iterable) o) {
                    size++;
                }
            }
            if (size == 0) {
                return this.emptyArray;
            } else {
                T[] array = (T[]) ((Object[]) Array.newInstance(this.target, size));
                int index = 0;
                for (Object o1 : (Iterable) o) {
                    if (this.typeWrapper.validator.test(o1)) {
                        array[index] = this.typeWrapper.factory.wrap(cx, o1);
                        index++;
                    }
                }
                return (T[]) (index == 0 ? this.emptyArray : (index == array.length ? array : Arrays.copyOf(array, index, this.arrayTarget)));
            }
        }
    }
}