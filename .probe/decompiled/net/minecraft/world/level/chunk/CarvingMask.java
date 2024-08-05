package net.minecraft.world.level.chunk;

import java.util.BitSet;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;

public class CarvingMask {

    private final int minY;

    private final BitSet mask;

    private CarvingMask.Mask additionalMask = (p_196713_, p_196714_, p_196715_) -> false;

    public CarvingMask(int int0, int int1) {
        this.minY = int1;
        this.mask = new BitSet(256 * int0);
    }

    public void setAdditionalMask(CarvingMask.Mask carvingMaskMask0) {
        this.additionalMask = carvingMaskMask0;
    }

    public CarvingMask(long[] long0, int int1) {
        this.minY = int1;
        this.mask = BitSet.valueOf(long0);
    }

    private int getIndex(int int0, int int1, int int2) {
        return int0 & 15 | (int2 & 15) << 4 | int1 - this.minY << 8;
    }

    public void set(int int0, int int1, int int2) {
        this.mask.set(this.getIndex(int0, int1, int2));
    }

    public boolean get(int int0, int int1, int int2) {
        return this.additionalMask.test(int0, int1, int2) || this.mask.get(this.getIndex(int0, int1, int2));
    }

    public Stream<BlockPos> stream(ChunkPos chunkPos0) {
        return this.mask.stream().mapToObj(p_196709_ -> {
            int $$2 = p_196709_ & 15;
            int $$3 = p_196709_ >> 4 & 15;
            int $$4 = p_196709_ >> 8;
            return chunkPos0.getBlockAt($$2, $$4 + this.minY, $$3);
        });
    }

    public long[] toArray() {
        return this.mask.toLongArray();
    }

    public interface Mask {

        boolean test(int var1, int var2, int var3);
    }
}