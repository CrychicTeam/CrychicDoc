package net.minecraft.nbt;

import java.util.AbstractList;

public abstract class CollectionTag<T extends Tag> extends AbstractList<T> implements Tag {

    public abstract T set(int int0, T t1);

    public abstract void add(int int0, T t1);

    public abstract T remove(int int0);

    public abstract boolean setTag(int var1, Tag var2);

    public abstract boolean addTag(int var1, Tag var2);

    public abstract byte getElementType();
}