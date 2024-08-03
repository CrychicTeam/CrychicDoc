package com.simibubi.create.foundation.ponder;

import com.simibubi.create.foundation.outliner.Outline;
import com.simibubi.create.foundation.outliner.Outliner;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public abstract class Selection implements Predicate<BlockPos> {

    public static Selection of(BoundingBox bb) {
        return new Selection.Simple(bb);
    }

    public abstract Selection add(Selection var1);

    public abstract Selection substract(Selection var1);

    public abstract Selection copy();

    public abstract Vec3 getCenter();

    public abstract void forEach(Consumer<BlockPos> var1);

    public abstract Outline.OutlineParams makeOutline(Outliner var1, Object var2);

    public Outline.OutlineParams makeOutline(Outliner outliner) {
        return this.makeOutline(outliner, this);
    }

    private static class Compound extends Selection {

        Set<BlockPos> posSet;

        Vec3 center;

        public Compound(Selection.Simple initial) {
            this.posSet = new HashSet();
            this.add(initial);
        }

        private Compound(Set<BlockPos> template) {
            this.posSet = new HashSet(template);
        }

        public boolean test(BlockPos t) {
            return this.posSet.contains(t);
        }

        @Override
        public Selection add(Selection other) {
            other.forEach(p -> this.posSet.add(p.immutable()));
            this.center = null;
            return this;
        }

        @Override
        public Selection substract(Selection other) {
            other.forEach(p -> this.posSet.remove(p.immutable()));
            this.center = null;
            return this;
        }

        @Override
        public void forEach(Consumer<BlockPos> callback) {
            this.posSet.forEach(callback);
        }

        @Override
        public Outline.OutlineParams makeOutline(Outliner outliner, Object slot) {
            return outliner.showCluster(slot, this.posSet);
        }

        @Override
        public Vec3 getCenter() {
            return this.center == null ? (this.center = this.evalCenter()) : this.center;
        }

        private Vec3 evalCenter() {
            Vec3 center = Vec3.ZERO;
            if (this.posSet.isEmpty()) {
                return center;
            } else {
                for (BlockPos blockPos : this.posSet) {
                    center = center.add(Vec3.atLowerCornerOf(blockPos));
                }
                center = center.scale((double) (1.0F / (float) this.posSet.size()));
                return center.add(new Vec3(0.5, 0.5, 0.5));
            }
        }

        @Override
        public Selection copy() {
            return new Selection.Compound(this.posSet);
        }
    }

    private static class Simple extends Selection {

        private BoundingBox bb;

        private AABB aabb;

        public Simple(BoundingBox bb) {
            this.bb = bb;
            this.aabb = this.getAABB();
        }

        public boolean test(BlockPos t) {
            return this.bb.isInside(t);
        }

        @Override
        public Selection add(Selection other) {
            return new Selection.Compound(this).add(other);
        }

        @Override
        public Selection substract(Selection other) {
            return new Selection.Compound(this).substract(other);
        }

        @Override
        public void forEach(Consumer<BlockPos> callback) {
            BlockPos.betweenClosedStream(this.bb).forEach(callback);
        }

        @Override
        public Vec3 getCenter() {
            return this.aabb.getCenter();
        }

        @Override
        public Outline.OutlineParams makeOutline(Outliner outliner, Object slot) {
            return outliner.showAABB(slot, this.aabb);
        }

        private AABB getAABB() {
            return new AABB((double) this.bb.minX(), (double) this.bb.minY(), (double) this.bb.minZ(), (double) (this.bb.maxX() + 1), (double) (this.bb.maxY() + 1), (double) (this.bb.maxZ() + 1));
        }

        @Override
        public Selection copy() {
            return new Selection.Simple(new BoundingBox(this.bb.minX(), this.bb.minY(), this.bb.minZ(), this.bb.maxX(), this.bb.maxY(), this.bb.maxZ()));
        }
    }
}