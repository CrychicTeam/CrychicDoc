package net.minecraft.world.level.lighting;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.longs.Long2LongLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongLinkedOpenHashSet;
import java.util.NoSuchElementException;
import net.minecraft.util.Mth;

public class SpatialLongSet extends LongLinkedOpenHashSet {

    private final SpatialLongSet.InternalMap map;

    public SpatialLongSet(int int0, float float1) {
        super(int0, float1);
        this.map = new SpatialLongSet.InternalMap(int0 / 64, float1);
    }

    public boolean add(long long0) {
        return this.map.addBit(long0);
    }

    public boolean rem(long long0) {
        return this.map.removeBit(long0);
    }

    public long removeFirstLong() {
        return this.map.removeFirstBit();
    }

    public int size() {
        throw new UnsupportedOperationException();
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    protected static class InternalMap extends Long2LongLinkedOpenHashMap {

        private static final int X_BITS = Mth.log2(60000000);

        private static final int Z_BITS = Mth.log2(60000000);

        private static final int Y_BITS = 64 - X_BITS - Z_BITS;

        private static final int Y_OFFSET = 0;

        private static final int Z_OFFSET = Y_BITS;

        private static final int X_OFFSET = Y_BITS + Z_BITS;

        private static final long OUTER_MASK = 3L << X_OFFSET | 3L | 3L << Z_OFFSET;

        private int lastPos = -1;

        private long lastOuterKey;

        private final int minSize;

        public InternalMap(int int0, float float1) {
            super(int0, float1);
            this.minSize = int0;
        }

        static long getOuterKey(long long0) {
            return long0 & ~OUTER_MASK;
        }

        static int getInnerKey(long long0) {
            int $$1 = (int) (long0 >>> X_OFFSET & 3L);
            int $$2 = (int) (long0 >>> 0 & 3L);
            int $$3 = (int) (long0 >>> Z_OFFSET & 3L);
            return $$1 << 4 | $$3 << 2 | $$2;
        }

        static long getFullKey(long long0, int int1) {
            long0 |= (long) (int1 >>> 4 & 3) << X_OFFSET;
            long0 |= (long) (int1 >>> 2 & 3) << Z_OFFSET;
            return long0 | (long) (int1 >>> 0 & 3) << 0;
        }

        public boolean addBit(long long0) {
            long $$1 = getOuterKey(long0);
            int $$2 = getInnerKey(long0);
            long $$3 = 1L << $$2;
            int $$4;
            if ($$1 == 0L) {
                if (this.containsNullKey) {
                    return this.replaceBit(this.n, $$3);
                }
                this.containsNullKey = true;
                $$4 = this.n;
            } else {
                if (this.lastPos != -1 && $$1 == this.lastOuterKey) {
                    return this.replaceBit(this.lastPos, $$3);
                }
                long[] $$5 = this.key;
                $$4 = (int) HashCommon.mix($$1) & this.mask;
                for (long $$7 = $$5[$$4]; $$7 != 0L; $$7 = $$5[$$4]) {
                    if ($$7 == $$1) {
                        this.lastPos = $$4;
                        this.lastOuterKey = $$1;
                        return this.replaceBit($$4, $$3);
                    }
                    $$4 = $$4 + 1 & this.mask;
                }
            }
            this.key[$$4] = $$1;
            this.value[$$4] = $$3;
            if (this.size == 0) {
                this.first = this.last = $$4;
                this.link[$$4] = -1L;
            } else {
                this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ (long) $$4 & 4294967295L) & 4294967295L;
                this.link[$$4] = ((long) this.last & 4294967295L) << 32 | 4294967295L;
                this.last = $$4;
            }
            if (this.size++ >= this.maxFill) {
                this.rehash(HashCommon.arraySize(this.size + 1, this.f));
            }
            return false;
        }

        private boolean replaceBit(int int0, long long1) {
            boolean $$2 = (this.value[int0] & long1) != 0L;
            this.value[int0] = this.value[int0] | long1;
            return $$2;
        }

        public boolean removeBit(long long0) {
            long $$1 = getOuterKey(long0);
            int $$2 = getInnerKey(long0);
            long $$3 = 1L << $$2;
            if ($$1 == 0L) {
                return this.containsNullKey ? this.removeFromNullEntry($$3) : false;
            } else if (this.lastPos != -1 && $$1 == this.lastOuterKey) {
                return this.removeFromEntry(this.lastPos, $$3);
            } else {
                long[] $$4 = this.key;
                int $$5 = (int) HashCommon.mix($$1) & this.mask;
                for (long $$6 = $$4[$$5]; $$6 != 0L; $$6 = $$4[$$5]) {
                    if ($$1 == $$6) {
                        this.lastPos = $$5;
                        this.lastOuterKey = $$1;
                        return this.removeFromEntry($$5, $$3);
                    }
                    $$5 = $$5 + 1 & this.mask;
                }
                return false;
            }
        }

        private boolean removeFromNullEntry(long long0) {
            if ((this.value[this.n] & long0) == 0L) {
                return false;
            } else {
                this.value[this.n] = this.value[this.n] & ~long0;
                if (this.value[this.n] != 0L) {
                    return true;
                } else {
                    this.containsNullKey = false;
                    this.size--;
                    this.fixPointers(this.n);
                    if (this.size < this.maxFill / 4 && this.n > 16) {
                        this.rehash(this.n / 2);
                    }
                    return true;
                }
            }
        }

        private boolean removeFromEntry(int int0, long long1) {
            if ((this.value[int0] & long1) == 0L) {
                return false;
            } else {
                this.value[int0] = this.value[int0] & ~long1;
                if (this.value[int0] != 0L) {
                    return true;
                } else {
                    this.lastPos = -1;
                    this.size--;
                    this.fixPointers(int0);
                    this.shiftKeys(int0);
                    if (this.size < this.maxFill / 4 && this.n > 16) {
                        this.rehash(this.n / 2);
                    }
                    return true;
                }
            }
        }

        public long removeFirstBit() {
            if (this.size == 0) {
                throw new NoSuchElementException();
            } else {
                int $$0 = this.first;
                long $$1 = this.key[$$0];
                int $$2 = Long.numberOfTrailingZeros(this.value[$$0]);
                this.value[$$0] = this.value[$$0] & ~(1L << $$2);
                if (this.value[$$0] == 0L) {
                    this.removeFirstLong();
                    this.lastPos = -1;
                }
                return getFullKey($$1, $$2);
            }
        }

        protected void rehash(int int0) {
            if (int0 > this.minSize) {
                super.rehash(int0);
            }
        }
    }
}