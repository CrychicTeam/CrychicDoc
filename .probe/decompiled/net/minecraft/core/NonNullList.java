package net.minecraft.core;

import com.google.common.collect.Lists;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.lang3.Validate;

public class NonNullList<E> extends AbstractList<E> {

    private final List<E> list;

    @Nullable
    private final E defaultValue;

    public static <E> NonNullList<E> create() {
        return new NonNullList<>(Lists.newArrayList(), null);
    }

    public static <E> NonNullList<E> createWithCapacity(int int0) {
        return new NonNullList<>(Lists.newArrayListWithCapacity(int0), null);
    }

    public static <E> NonNullList<E> withSize(int int0, E e1) {
        Validate.notNull(e1);
        Object[] $$2 = new Object[int0];
        Arrays.fill($$2, e1);
        return new NonNullList<>(Arrays.asList($$2), e1);
    }

    @SafeVarargs
    public static <E> NonNullList<E> of(E e0, E... e1) {
        return new NonNullList<>(Arrays.asList(e1), e0);
    }

    protected NonNullList(List<E> listE0, @Nullable E e1) {
        this.list = listE0;
        this.defaultValue = e1;
    }

    @Nonnull
    public E get(int int0) {
        return (E) this.list.get(int0);
    }

    public E set(int int0, E e1) {
        Validate.notNull(e1);
        return (E) this.list.set(int0, e1);
    }

    public void add(int int0, E e1) {
        Validate.notNull(e1);
        this.list.add(int0, e1);
    }

    public E remove(int int0) {
        return (E) this.list.remove(int0);
    }

    public int size() {
        return this.list.size();
    }

    public void clear() {
        if (this.defaultValue == null) {
            super.clear();
        } else {
            for (int $$0 = 0; $$0 < this.size(); $$0++) {
                this.set($$0, this.defaultValue);
            }
        }
    }
}