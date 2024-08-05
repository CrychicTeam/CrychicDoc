package net.minecraft.core;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.Util;

public class IdMapper<T> implements IdMap<T> {

    private int nextId;

    private final Object2IntMap<T> tToId;

    private final List<T> idToT;

    public IdMapper() {
        this(512);
    }

    public IdMapper(int int0) {
        this.idToT = Lists.newArrayListWithExpectedSize(int0);
        this.tToId = new Object2IntOpenCustomHashMap(int0, Util.identityStrategy());
        this.tToId.defaultReturnValue(-1);
    }

    public void addMapping(T t0, int int1) {
        this.tToId.put(t0, int1);
        while (this.idToT.size() <= int1) {
            this.idToT.add(null);
        }
        this.idToT.set(int1, t0);
        if (this.nextId <= int1) {
            this.nextId = int1 + 1;
        }
    }

    public void add(T t0) {
        this.addMapping(t0, this.nextId);
    }

    @Override
    public int getId(T t0) {
        return this.tToId.getInt(t0);
    }

    @Nullable
    @Override
    public final T byId(int int0) {
        return (T) (int0 >= 0 && int0 < this.idToT.size() ? this.idToT.get(int0) : null);
    }

    public Iterator<T> iterator() {
        return Iterators.filter(this.idToT.iterator(), Objects::nonNull);
    }

    public boolean contains(int int0) {
        return this.byId(int0) != null;
    }

    @Override
    public int size() {
        return this.tToId.size();
    }
}