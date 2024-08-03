package net.minecraft.server.level;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.lighting.DynamicGraphMinFixedPoint;

public abstract class ChunkTracker extends DynamicGraphMinFixedPoint {

    protected ChunkTracker(int int0, int int1, int int2) {
        super(int0, int1, int2);
    }

    @Override
    protected boolean isSource(long long0) {
        return long0 == ChunkPos.INVALID_CHUNK_POS;
    }

    @Override
    protected void checkNeighborsAfterUpdate(long long0, int int1, boolean boolean2) {
        if (!boolean2 || int1 < this.f_75537_ - 2) {
            ChunkPos $$3 = new ChunkPos(long0);
            int $$4 = $$3.x;
            int $$5 = $$3.z;
            for (int $$6 = -1; $$6 <= 1; $$6++) {
                for (int $$7 = -1; $$7 <= 1; $$7++) {
                    long $$8 = ChunkPos.asLong($$4 + $$6, $$5 + $$7);
                    if ($$8 != long0) {
                        this.m_75593_(long0, $$8, int1, boolean2);
                    }
                }
            }
        }
    }

    @Override
    protected int getComputedLevel(long long0, long long1, int int2) {
        int $$3 = int2;
        ChunkPos $$4 = new ChunkPos(long0);
        int $$5 = $$4.x;
        int $$6 = $$4.z;
        for (int $$7 = -1; $$7 <= 1; $$7++) {
            for (int $$8 = -1; $$8 <= 1; $$8++) {
                long $$9 = ChunkPos.asLong($$5 + $$7, $$6 + $$8);
                if ($$9 == long0) {
                    $$9 = ChunkPos.INVALID_CHUNK_POS;
                }
                if ($$9 != long1) {
                    int $$10 = this.computeLevelFromNeighbor($$9, long0, this.m_6172_($$9));
                    if ($$3 > $$10) {
                        $$3 = $$10;
                    }
                    if ($$3 == 0) {
                        return $$3;
                    }
                }
            }
        }
        return $$3;
    }

    @Override
    protected int computeLevelFromNeighbor(long long0, long long1, int int2) {
        return long0 == ChunkPos.INVALID_CHUNK_POS ? this.getLevelFromSource(long1) : int2 + 1;
    }

    protected abstract int getLevelFromSource(long var1);

    public void update(long long0, int int1, boolean boolean2) {
        this.m_75576_(ChunkPos.INVALID_CHUNK_POS, long0, int1, boolean2);
    }
}