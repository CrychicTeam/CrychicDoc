package net.minecraft.server.level;

import net.minecraft.core.SectionPos;
import net.minecraft.world.level.lighting.DynamicGraphMinFixedPoint;

public abstract class SectionTracker extends DynamicGraphMinFixedPoint {

    protected SectionTracker(int int0, int int1, int int2) {
        super(int0, int1, int2);
    }

    @Override
    protected void checkNeighborsAfterUpdate(long long0, int int1, boolean boolean2) {
        if (!boolean2 || int1 < this.f_75537_ - 2) {
            for (int $$3 = -1; $$3 <= 1; $$3++) {
                for (int $$4 = -1; $$4 <= 1; $$4++) {
                    for (int $$5 = -1; $$5 <= 1; $$5++) {
                        long $$6 = SectionPos.offset(long0, $$3, $$4, $$5);
                        if ($$6 != long0) {
                            this.m_75593_(long0, $$6, int1, boolean2);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected int getComputedLevel(long long0, long long1, int int2) {
        int $$3 = int2;
        for (int $$4 = -1; $$4 <= 1; $$4++) {
            for (int $$5 = -1; $$5 <= 1; $$5++) {
                for (int $$6 = -1; $$6 <= 1; $$6++) {
                    long $$7 = SectionPos.offset(long0, $$4, $$5, $$6);
                    if ($$7 == long0) {
                        $$7 = Long.MAX_VALUE;
                    }
                    if ($$7 != long1) {
                        int $$8 = this.computeLevelFromNeighbor($$7, long0, this.m_6172_($$7));
                        if ($$3 > $$8) {
                            $$3 = $$8;
                        }
                        if ($$3 == 0) {
                            return $$3;
                        }
                    }
                }
            }
        }
        return $$3;
    }

    @Override
    protected int computeLevelFromNeighbor(long long0, long long1, int int2) {
        return this.m_6163_(long0) ? this.getLevelFromSource(long1) : int2 + 1;
    }

    protected abstract int getLevelFromSource(long var1);

    public void update(long long0, int int1, boolean boolean2) {
        this.m_75576_(Long.MAX_VALUE, long0, int1, boolean2);
    }
}