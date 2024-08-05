package com.github.alexthe666.iceandfire.util;

import com.google.common.collect.AbstractIterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class ShapeBuilder {

    Iterable<BlockPos> blocks;

    ShapeBuilder() {
    }

    public static ShapeBuilder start() {
        return new ShapeBuilder();
    }

    public ShapeBuilder getAllInSphereMutable(int radius, BlockPos center) {
        return this.getAllInSphereMutable(radius, center.m_123341_(), center.m_123342_(), center.m_123343_());
    }

    public ShapeBuilder getAllInSphereMutable(int radius, int c1, int c2, int c3) {
        return this.getAllInCutOffSphereMutable(radius, radius, c1, c2, c3);
    }

    public ShapeBuilder getAllInCutOffSphereMutable(int radiusX, int yCutOff, BlockPos center) {
        return this.getAllInCutOffSphereMutable(radiusX, yCutOff, yCutOff, center.m_123341_(), center.m_123342_(), center.m_123343_());
    }

    public ShapeBuilder getAllInCutOffSphereMutable(int radiusX, int yCutOff, int c1, int c2, int c3) {
        return this.getAllInCutOffSphereMutable(radiusX, yCutOff, yCutOff, c1, c2, c3);
    }

    public ShapeBuilder getAllInCutOffSphereMutable(int radiusX, int yCutOffMax, int yCutOffMin, BlockPos center) {
        return this.getAllInCutOffSphereMutable(radiusX, yCutOffMax, yCutOffMin, center.m_123341_(), center.m_123342_(), center.m_123343_());
    }

    public ShapeBuilder getAllInCutOffSphereMutable(int radiusX, int yCutOffMax, int yCutOffMin, int c1, int c2, int c3) {
        int r2 = radiusX * radiusX;
        this.blocks = () -> new AbstractIterator<BlockPos>() {

            private final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

            private int currRX = radiusX;

            private int currRY = yCutOffMax;

            private int offset = 0;

            private int phase = 1;

            protected BlockPos computeNext() {
                if (-this.currRY > yCutOffMin) {
                    return (BlockPos) this.endOfData();
                } else if (this.isWithinRange(this.currRX, this.currRY, this.phase, this.offset, r2)) {
                    BlockPos pos = this.mutablePos.set(c1 + this.currRX, c2 + this.currRY, c3 + this.phase * this.offset);
                    this.offset++;
                    return pos;
                } else {
                    if (this.phase == 1) {
                        this.phase = -1;
                        this.offset = 1;
                    } else if (this.phase == -1) {
                        this.phase = 1;
                        this.offset = 0;
                        this.currRX--;
                    }
                    if (-this.currRX > radiusX) {
                        this.currRY--;
                        this.currRX = radiusX;
                    }
                    return this.computeNext();
                }
            }

            private boolean isWithinRange(int currentRadiusX, int currentRadiusY, int phase, int offset, int radius2) {
                return Math.round((double) currentRadiusX * (double) currentRadiusX + (double) (currentRadiusY * currentRadiusY) + (double) (phase * offset * phase * offset)) <= (long) radius2;
            }
        };
        return this;
    }

    public ShapeBuilder getAllInRandomlyDistributedRangeYCutOffSphereMutable(int maxRadiusX, int minRadiusX, int yCutOff, RandomSource rand, BlockPos center) {
        return this.getAllInRandomlyDistributedRangeYCutOffSphereMutable(maxRadiusX, minRadiusX, yCutOff, rand, center.m_123341_(), center.m_123342_(), center.m_123343_());
    }

    public ShapeBuilder getAllInRandomlyDistributedRangeYCutOffSphereMutable(int maxRadiusX, int minRadiusX, int ycutoffmin, RandomSource rand, int c1, int c2, int c3) {
        return this.getAllInRandomlyDistributedRangeYCutOffSphereMutable(maxRadiusX, minRadiusX, ycutoffmin, ycutoffmin, rand, c1, c2, c3);
    }

    public ShapeBuilder getAllInRandomlyDistributedRangeYCutOffSphereMutable(int maxRadiusX, int minRadiusX, int yCutOffMax, int yCutOffMin, RandomSource rand, int c1, int c2, int c3) {
        int maxr2 = maxRadiusX * maxRadiusX;
        int minr2 = minRadiusX * minRadiusX;
        float rDifference = (float) minRadiusX / (float) maxRadiusX;
        this.blocks = () -> new AbstractIterator<BlockPos>() {

            private final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

            private int currRX = maxRadiusX;

            private int currRY = yCutOffMax;

            private int offset = 0;

            private int phase = 1;

            protected BlockPos computeNext() {
                if (-this.currRY > yCutOffMin) {
                    return (BlockPos) this.endOfData();
                } else {
                    int distance = this.distance(this.currRX, this.currRY, this.phase, this.offset);
                    if (distance <= minr2 || (float) distance <= (float) maxr2 * Mth.clamp(rand.nextFloat(), rDifference, 1.0F)) {
                        BlockPos pos = this.mutablePos.set(c1 + this.currRX, c2 + this.currRY, c3 + this.phase * this.offset);
                        this.offset++;
                        return pos;
                    } else if (distance <= maxr2) {
                        this.offset++;
                        return this.computeNext();
                    } else {
                        if (this.phase == 1) {
                            this.phase = -1;
                            this.offset = 1;
                        } else if (this.phase == -1) {
                            this.phase = 1;
                            this.offset = 0;
                            this.currRX--;
                        }
                        if (-this.currRX > maxRadiusX) {
                            this.currRY--;
                            this.currRX = maxRadiusX;
                        }
                        return this.computeNext();
                    }
                }
            }

            private int distance(int currentRadiusX, int currentRadiusY, int phase, int offset) {
                return (int) Math.round((double) currentRadiusX * (double) currentRadiusX + (double) (currentRadiusY * currentRadiusY) + (double) (phase * offset * phase * offset));
            }
        };
        return this;
    }

    public ShapeBuilder getAllInCircleMutable(int radius, int c1, int c2, int c3) {
        int r2 = radius * radius;
        this.blocks = () -> new AbstractIterator<BlockPos>() {

            private final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

            private int totalAmount;

            private int currR = radius;

            private int offset = 0;

            private int phase = 1;

            protected BlockPos computeNext() {
                if (-this.currR > radius) {
                    return (BlockPos) this.endOfData();
                } else if (this.isWithinRange(this.currR, this.phase, this.offset, r2)) {
                    BlockPos pos = this.mutablePos.set(c1 + this.currR, c2, c3 + this.phase * this.offset);
                    this.offset++;
                    return pos;
                } else {
                    if (this.phase == 1) {
                        this.phase = -1;
                        this.offset = 1;
                    } else if (this.phase == -1) {
                        this.phase = 1;
                        this.offset = 0;
                        this.currR--;
                    }
                    return this.computeNext();
                }
            }

            private boolean isWithinRange(int currentRadius, int phase, int offset, int radius2) {
                return Math.floor((double) currentRadius * (double) currentRadius + (double) (phase * offset * phase * offset)) <= (double) radius2;
            }
        };
        return this;
    }

    public Stream<BlockPos> toStream(boolean parallel) {
        return StreamSupport.stream(this.blocks.spliterator(), parallel);
    }

    public Iterable<BlockPos> toIterable() {
        return this.blocks;
    }
}