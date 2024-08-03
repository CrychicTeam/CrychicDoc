package net.minecraft.world.phys.shapes;

import com.google.common.collect.Lists;
import com.google.common.math.DoubleMath;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.AxisCycle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public abstract class VoxelShape {

    protected final DiscreteVoxelShape shape;

    @Nullable
    private VoxelShape[] faces;

    VoxelShape(DiscreteVoxelShape discreteVoxelShape0) {
        this.shape = discreteVoxelShape0;
    }

    public double min(Direction.Axis directionAxis0) {
        int $$1 = this.shape.firstFull(directionAxis0);
        return $$1 >= this.shape.getSize(directionAxis0) ? Double.POSITIVE_INFINITY : this.get(directionAxis0, $$1);
    }

    public double max(Direction.Axis directionAxis0) {
        int $$1 = this.shape.lastFull(directionAxis0);
        return $$1 <= 0 ? Double.NEGATIVE_INFINITY : this.get(directionAxis0, $$1);
    }

    public AABB bounds() {
        if (this.isEmpty()) {
            throw (UnsupportedOperationException) Util.pauseInIde(new UnsupportedOperationException("No bounds for empty shape."));
        } else {
            return new AABB(this.min(Direction.Axis.X), this.min(Direction.Axis.Y), this.min(Direction.Axis.Z), this.max(Direction.Axis.X), this.max(Direction.Axis.Y), this.max(Direction.Axis.Z));
        }
    }

    protected double get(Direction.Axis directionAxis0, int int1) {
        return this.getCoords(directionAxis0).getDouble(int1);
    }

    protected abstract DoubleList getCoords(Direction.Axis var1);

    public boolean isEmpty() {
        return this.shape.isEmpty();
    }

    public VoxelShape move(double double0, double double1, double double2) {
        return (VoxelShape) (this.isEmpty() ? Shapes.empty() : new ArrayVoxelShape(this.shape, new OffsetDoubleList(this.getCoords(Direction.Axis.X), double0), new OffsetDoubleList(this.getCoords(Direction.Axis.Y), double1), new OffsetDoubleList(this.getCoords(Direction.Axis.Z), double2)));
    }

    public VoxelShape optimize() {
        VoxelShape[] $$0 = new VoxelShape[] { Shapes.empty() };
        this.forAllBoxes((p_83275_, p_83276_, p_83277_, p_83278_, p_83279_, p_83280_) -> $$0[0] = Shapes.joinUnoptimized($$0[0], Shapes.box(p_83275_, p_83276_, p_83277_, p_83278_, p_83279_, p_83280_), BooleanOp.OR));
        return $$0[0];
    }

    public void forAllEdges(Shapes.DoubleLineConsumer shapesDoubleLineConsumer0) {
        this.shape.forAllEdges((p_83228_, p_83229_, p_83230_, p_83231_, p_83232_, p_83233_) -> shapesDoubleLineConsumer0.consume(this.get(Direction.Axis.X, p_83228_), this.get(Direction.Axis.Y, p_83229_), this.get(Direction.Axis.Z, p_83230_), this.get(Direction.Axis.X, p_83231_), this.get(Direction.Axis.Y, p_83232_), this.get(Direction.Axis.Z, p_83233_)), true);
    }

    public void forAllBoxes(Shapes.DoubleLineConsumer shapesDoubleLineConsumer0) {
        DoubleList $$1 = this.getCoords(Direction.Axis.X);
        DoubleList $$2 = this.getCoords(Direction.Axis.Y);
        DoubleList $$3 = this.getCoords(Direction.Axis.Z);
        this.shape.forAllBoxes((p_83239_, p_83240_, p_83241_, p_83242_, p_83243_, p_83244_) -> shapesDoubleLineConsumer0.consume($$1.getDouble(p_83239_), $$2.getDouble(p_83240_), $$3.getDouble(p_83241_), $$1.getDouble(p_83242_), $$2.getDouble(p_83243_), $$3.getDouble(p_83244_)), true);
    }

    public List<AABB> toAabbs() {
        List<AABB> $$0 = Lists.newArrayList();
        this.forAllBoxes((p_83267_, p_83268_, p_83269_, p_83270_, p_83271_, p_83272_) -> $$0.add(new AABB(p_83267_, p_83268_, p_83269_, p_83270_, p_83271_, p_83272_)));
        return $$0;
    }

    public double min(Direction.Axis directionAxis0, double double1, double double2) {
        Direction.Axis $$3 = AxisCycle.FORWARD.cycle(directionAxis0);
        Direction.Axis $$4 = AxisCycle.BACKWARD.cycle(directionAxis0);
        int $$5 = this.findIndex($$3, double1);
        int $$6 = this.findIndex($$4, double2);
        int $$7 = this.shape.firstFull(directionAxis0, $$5, $$6);
        return $$7 >= this.shape.getSize(directionAxis0) ? Double.POSITIVE_INFINITY : this.get(directionAxis0, $$7);
    }

    public double max(Direction.Axis directionAxis0, double double1, double double2) {
        Direction.Axis $$3 = AxisCycle.FORWARD.cycle(directionAxis0);
        Direction.Axis $$4 = AxisCycle.BACKWARD.cycle(directionAxis0);
        int $$5 = this.findIndex($$3, double1);
        int $$6 = this.findIndex($$4, double2);
        int $$7 = this.shape.lastFull(directionAxis0, $$5, $$6);
        return $$7 <= 0 ? Double.NEGATIVE_INFINITY : this.get(directionAxis0, $$7);
    }

    protected int findIndex(Direction.Axis directionAxis0, double double1) {
        return Mth.binarySearch(0, this.shape.getSize(directionAxis0) + 1, p_166066_ -> double1 < this.get(directionAxis0, p_166066_)) - 1;
    }

    @Nullable
    public BlockHitResult clip(Vec3 vec0, Vec3 vec1, BlockPos blockPos2) {
        if (this.isEmpty()) {
            return null;
        } else {
            Vec3 $$3 = vec1.subtract(vec0);
            if ($$3.lengthSqr() < 1.0E-7) {
                return null;
            } else {
                Vec3 $$4 = vec0.add($$3.scale(0.001));
                return this.shape.isFullWide(this.findIndex(Direction.Axis.X, $$4.x - (double) blockPos2.m_123341_()), this.findIndex(Direction.Axis.Y, $$4.y - (double) blockPos2.m_123342_()), this.findIndex(Direction.Axis.Z, $$4.z - (double) blockPos2.m_123343_())) ? new BlockHitResult($$4, Direction.getNearest($$3.x, $$3.y, $$3.z).getOpposite(), blockPos2, true) : AABB.clip(this.toAabbs(), vec0, vec1, blockPos2);
            }
        }
    }

    public Optional<Vec3> closestPointTo(Vec3 vec0) {
        if (this.isEmpty()) {
            return Optional.empty();
        } else {
            Vec3[] $$1 = new Vec3[1];
            this.forAllBoxes((p_166072_, p_166073_, p_166074_, p_166075_, p_166076_, p_166077_) -> {
                double $$8 = Mth.clamp(vec0.x(), p_166072_, p_166075_);
                double $$9 = Mth.clamp(vec0.y(), p_166073_, p_166076_);
                double $$10 = Mth.clamp(vec0.z(), p_166074_, p_166077_);
                if ($$1[0] == null || vec0.distanceToSqr($$8, $$9, $$10) < vec0.distanceToSqr($$1[0])) {
                    $$1[0] = new Vec3($$8, $$9, $$10);
                }
            });
            return Optional.of($$1[0]);
        }
    }

    public VoxelShape getFaceShape(Direction direction0) {
        if (!this.isEmpty() && this != Shapes.block()) {
            if (this.faces != null) {
                VoxelShape $$1 = this.faces[direction0.ordinal()];
                if ($$1 != null) {
                    return $$1;
                }
            } else {
                this.faces = new VoxelShape[6];
            }
            VoxelShape $$2 = this.calculateFace(direction0);
            this.faces[direction0.ordinal()] = $$2;
            return $$2;
        } else {
            return this;
        }
    }

    private VoxelShape calculateFace(Direction direction0) {
        Direction.Axis $$1 = direction0.getAxis();
        DoubleList $$2 = this.getCoords($$1);
        if ($$2.size() == 2 && DoubleMath.fuzzyEquals($$2.getDouble(0), 0.0, 1.0E-7) && DoubleMath.fuzzyEquals($$2.getDouble(1), 1.0, 1.0E-7)) {
            return this;
        } else {
            Direction.AxisDirection $$3 = direction0.getAxisDirection();
            int $$4 = this.findIndex($$1, $$3 == Direction.AxisDirection.POSITIVE ? 0.9999999 : 1.0E-7);
            return new SliceShape(this, $$1, $$4);
        }
    }

    public double collide(Direction.Axis directionAxis0, AABB aABB1, double double2) {
        return this.collideX(AxisCycle.between(directionAxis0, Direction.Axis.X), aABB1, double2);
    }

    protected double collideX(AxisCycle axisCycle0, AABB aABB1, double double2) {
        if (this.isEmpty()) {
            return double2;
        } else if (Math.abs(double2) < 1.0E-7) {
            return 0.0;
        } else {
            AxisCycle $$3 = axisCycle0.inverse();
            Direction.Axis $$4 = $$3.cycle(Direction.Axis.X);
            Direction.Axis $$5 = $$3.cycle(Direction.Axis.Y);
            Direction.Axis $$6 = $$3.cycle(Direction.Axis.Z);
            double $$7 = aABB1.max($$4);
            double $$8 = aABB1.min($$4);
            int $$9 = this.findIndex($$4, $$8 + 1.0E-7);
            int $$10 = this.findIndex($$4, $$7 - 1.0E-7);
            int $$11 = Math.max(0, this.findIndex($$5, aABB1.min($$5) + 1.0E-7));
            int $$12 = Math.min(this.shape.getSize($$5), this.findIndex($$5, aABB1.max($$5) - 1.0E-7) + 1);
            int $$13 = Math.max(0, this.findIndex($$6, aABB1.min($$6) + 1.0E-7));
            int $$14 = Math.min(this.shape.getSize($$6), this.findIndex($$6, aABB1.max($$6) - 1.0E-7) + 1);
            int $$15 = this.shape.getSize($$4);
            if (double2 > 0.0) {
                for (int $$16 = $$10 + 1; $$16 < $$15; $$16++) {
                    for (int $$17 = $$11; $$17 < $$12; $$17++) {
                        for (int $$18 = $$13; $$18 < $$14; $$18++) {
                            if (this.shape.isFullWide($$3, $$16, $$17, $$18)) {
                                double $$19 = this.get($$4, $$16) - $$7;
                                if ($$19 >= -1.0E-7) {
                                    double2 = Math.min(double2, $$19);
                                }
                                return double2;
                            }
                        }
                    }
                }
            } else if (double2 < 0.0) {
                for (int $$20 = $$9 - 1; $$20 >= 0; $$20--) {
                    for (int $$21 = $$11; $$21 < $$12; $$21++) {
                        for (int $$22 = $$13; $$22 < $$14; $$22++) {
                            if (this.shape.isFullWide($$3, $$20, $$21, $$22)) {
                                double $$23 = this.get($$4, $$20 + 1) - $$8;
                                if ($$23 <= 1.0E-7) {
                                    double2 = Math.max(double2, $$23);
                                }
                                return double2;
                            }
                        }
                    }
                }
            }
            return double2;
        }
    }

    public String toString() {
        return this.isEmpty() ? "EMPTY" : "VoxelShape[" + this.bounds() + "]";
    }
}