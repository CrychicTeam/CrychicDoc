package net.minecraft.world.level.chunk.storage;

import com.google.common.annotations.VisibleForTesting;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.BitSet;

public class RegionBitmap {

    private final BitSet used = new BitSet();

    public void force(int int0, int int1) {
        this.used.set(int0, int0 + int1);
    }

    public void free(int int0, int int1) {
        this.used.clear(int0, int0 + int1);
    }

    public int allocate(int int0) {
        int $$1 = 0;
        while (true) {
            int $$2 = this.used.nextClearBit($$1);
            int $$3 = this.used.nextSetBit($$2);
            if ($$3 == -1 || $$3 - $$2 >= int0) {
                this.force($$2, int0);
                return $$2;
            }
            $$1 = $$3;
        }
    }

    @VisibleForTesting
    public IntSet getUsed() {
        return (IntSet) this.used.stream().collect(IntArraySet::new, IntCollection::add, IntCollection::addAll);
    }
}