package net.minecraft.client.renderer.chunk;

import it.unimi.dsi.fastutil.ints.IntArrayFIFOQueue;
import it.unimi.dsi.fastutil.ints.IntPriorityQueue;
import java.util.BitSet;
import java.util.EnumSet;
import java.util.Set;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class VisGraph {

    private static final int SIZE_IN_BITS = 4;

    private static final int LEN = 16;

    private static final int MASK = 15;

    private static final int SIZE = 4096;

    private static final int X_SHIFT = 0;

    private static final int Z_SHIFT = 4;

    private static final int Y_SHIFT = 8;

    private static final int DX = (int) Math.pow(16.0, 0.0);

    private static final int DZ = (int) Math.pow(16.0, 1.0);

    private static final int DY = (int) Math.pow(16.0, 2.0);

    private static final int INVALID_INDEX = -1;

    private static final Direction[] DIRECTIONS = Direction.values();

    private final BitSet bitSet = new BitSet(4096);

    private static final int[] INDEX_OF_EDGES = Util.make(new int[1352], p_112974_ -> {
        int $$1 = 0;
        int $$2 = 15;
        int $$3 = 0;
        for (int $$4 = 0; $$4 < 16; $$4++) {
            for (int $$5 = 0; $$5 < 16; $$5++) {
                for (int $$6 = 0; $$6 < 16; $$6++) {
                    if ($$4 == 0 || $$4 == 15 || $$5 == 0 || $$5 == 15 || $$6 == 0 || $$6 == 15) {
                        p_112974_[$$3++] = getIndex($$4, $$5, $$6);
                    }
                }
            }
        }
    });

    private int empty = 4096;

    public void setOpaque(BlockPos blockPos0) {
        this.bitSet.set(getIndex(blockPos0), true);
        this.empty--;
    }

    private static int getIndex(BlockPos blockPos0) {
        return getIndex(blockPos0.m_123341_() & 15, blockPos0.m_123342_() & 15, blockPos0.m_123343_() & 15);
    }

    private static int getIndex(int int0, int int1, int int2) {
        return int0 << 0 | int1 << 8 | int2 << 4;
    }

    public VisibilitySet resolve() {
        VisibilitySet $$0 = new VisibilitySet();
        if (4096 - this.empty < 256) {
            $$0.setAll(true);
        } else if (this.empty == 0) {
            $$0.setAll(false);
        } else {
            for (int $$1 : INDEX_OF_EDGES) {
                if (!this.bitSet.get($$1)) {
                    $$0.add(this.floodFill($$1));
                }
            }
        }
        return $$0;
    }

    private Set<Direction> floodFill(int int0) {
        Set<Direction> $$1 = EnumSet.noneOf(Direction.class);
        IntPriorityQueue $$2 = new IntArrayFIFOQueue();
        $$2.enqueue(int0);
        this.bitSet.set(int0, true);
        while (!$$2.isEmpty()) {
            int $$3 = $$2.dequeueInt();
            this.addEdges($$3, $$1);
            for (Direction $$4 : DIRECTIONS) {
                int $$5 = this.getNeighborIndexAtFace($$3, $$4);
                if ($$5 >= 0 && !this.bitSet.get($$5)) {
                    this.bitSet.set($$5, true);
                    $$2.enqueue($$5);
                }
            }
        }
        return $$1;
    }

    private void addEdges(int int0, Set<Direction> setDirection1) {
        int $$2 = int0 >> 0 & 15;
        if ($$2 == 0) {
            setDirection1.add(Direction.WEST);
        } else if ($$2 == 15) {
            setDirection1.add(Direction.EAST);
        }
        int $$3 = int0 >> 8 & 15;
        if ($$3 == 0) {
            setDirection1.add(Direction.DOWN);
        } else if ($$3 == 15) {
            setDirection1.add(Direction.UP);
        }
        int $$4 = int0 >> 4 & 15;
        if ($$4 == 0) {
            setDirection1.add(Direction.NORTH);
        } else if ($$4 == 15) {
            setDirection1.add(Direction.SOUTH);
        }
    }

    private int getNeighborIndexAtFace(int int0, Direction direction1) {
        switch(direction1) {
            case DOWN:
                if ((int0 >> 8 & 15) == 0) {
                    return -1;
                }
                return int0 - DY;
            case UP:
                if ((int0 >> 8 & 15) == 15) {
                    return -1;
                }
                return int0 + DY;
            case NORTH:
                if ((int0 >> 4 & 15) == 0) {
                    return -1;
                }
                return int0 - DZ;
            case SOUTH:
                if ((int0 >> 4 & 15) == 15) {
                    return -1;
                }
                return int0 + DZ;
            case WEST:
                if ((int0 >> 0 & 15) == 0) {
                    return -1;
                }
                return int0 - DX;
            case EAST:
                if ((int0 >> 0 & 15) == 15) {
                    return -1;
                }
                return int0 + DX;
            default:
                return -1;
        }
    }
}