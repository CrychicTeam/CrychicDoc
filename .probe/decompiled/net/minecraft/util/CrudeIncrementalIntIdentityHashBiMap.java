package net.minecraft.util;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import java.util.Arrays;
import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.core.IdMap;

public class CrudeIncrementalIntIdentityHashBiMap<K> implements IdMap<K> {

    private static final int NOT_FOUND = -1;

    private static final Object EMPTY_SLOT = null;

    private static final float LOADFACTOR = 0.8F;

    private K[] keys;

    private int[] values;

    private K[] byId;

    private int nextId;

    private int size;

    private CrudeIncrementalIntIdentityHashBiMap(int int0) {
        this.keys = (K[]) (new Object[int0]);
        this.values = new int[int0];
        this.byId = (K[]) (new Object[int0]);
    }

    private CrudeIncrementalIntIdentityHashBiMap(K[] k0, int[] int1, K[] k2, int int3, int int4) {
        this.keys = k0;
        this.values = int1;
        this.byId = k2;
        this.nextId = int3;
        this.size = int4;
    }

    public static <A> CrudeIncrementalIntIdentityHashBiMap<A> create(int int0) {
        return new CrudeIncrementalIntIdentityHashBiMap((int) ((float) int0 / 0.8F));
    }

    @Override
    public int getId(@Nullable K k0) {
        return this.getValue(this.indexOf(k0, this.hash(k0)));
    }

    @Nullable
    @Override
    public K byId(int int0) {
        return int0 >= 0 && int0 < this.byId.length ? this.byId[int0] : null;
    }

    private int getValue(int int0) {
        return int0 == -1 ? -1 : this.values[int0];
    }

    public boolean contains(K k0) {
        return this.getId(k0) != -1;
    }

    public boolean contains(int int0) {
        return this.byId(int0) != null;
    }

    public int add(K k0) {
        int $$1 = this.nextId();
        this.addMapping(k0, $$1);
        return $$1;
    }

    private int nextId() {
        while (this.nextId < this.byId.length && this.byId[this.nextId] != null) {
            this.nextId++;
        }
        return this.nextId;
    }

    private void grow(int int0) {
        K[] $$1 = this.keys;
        int[] $$2 = this.values;
        CrudeIncrementalIntIdentityHashBiMap<K> $$3 = new CrudeIncrementalIntIdentityHashBiMap<>(int0);
        for (int $$4 = 0; $$4 < $$1.length; $$4++) {
            if ($$1[$$4] != null) {
                $$3.addMapping($$1[$$4], $$2[$$4]);
            }
        }
        this.keys = $$3.keys;
        this.values = $$3.values;
        this.byId = $$3.byId;
        this.nextId = $$3.nextId;
        this.size = $$3.size;
    }

    public void addMapping(K k0, int int1) {
        int $$2 = Math.max(int1, this.size + 1);
        if ((float) $$2 >= (float) this.keys.length * 0.8F) {
            int $$3 = this.keys.length << 1;
            while ($$3 < int1) {
                $$3 <<= 1;
            }
            this.grow($$3);
        }
        int $$4 = this.findEmpty(this.hash(k0));
        this.keys[$$4] = k0;
        this.values[$$4] = int1;
        this.byId[int1] = k0;
        this.size++;
        if (int1 == this.nextId) {
            this.nextId++;
        }
    }

    private int hash(@Nullable K k0) {
        return (Mth.murmurHash3Mixer(System.identityHashCode(k0)) & 2147483647) % this.keys.length;
    }

    private int indexOf(@Nullable K k0, int int1) {
        for (int $$2 = int1; $$2 < this.keys.length; $$2++) {
            if (this.keys[$$2] == k0) {
                return $$2;
            }
            if (this.keys[$$2] == EMPTY_SLOT) {
                return -1;
            }
        }
        for (int $$3 = 0; $$3 < int1; $$3++) {
            if (this.keys[$$3] == k0) {
                return $$3;
            }
            if (this.keys[$$3] == EMPTY_SLOT) {
                return -1;
            }
        }
        return -1;
    }

    private int findEmpty(int int0) {
        for (int $$1 = int0; $$1 < this.keys.length; $$1++) {
            if (this.keys[$$1] == EMPTY_SLOT) {
                return $$1;
            }
        }
        for (int $$2 = 0; $$2 < int0; $$2++) {
            if (this.keys[$$2] == EMPTY_SLOT) {
                return $$2;
            }
        }
        throw new RuntimeException("Overflowed :(");
    }

    public Iterator<K> iterator() {
        return Iterators.filter(Iterators.forArray(this.byId), Predicates.notNull());
    }

    public void clear() {
        Arrays.fill(this.keys, null);
        Arrays.fill(this.byId, null);
        this.nextId = 0;
        this.size = 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    public CrudeIncrementalIntIdentityHashBiMap<K> copy() {
        return new CrudeIncrementalIntIdentityHashBiMap<>((K[]) ((Object[]) this.keys.clone()), (int[]) this.values.clone(), (K[]) ((Object[]) this.byId.clone()), this.nextId, this.size);
    }
}