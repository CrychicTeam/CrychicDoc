package com.simibubi.create.foundation.blockEntity.behaviour;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.function.Function;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;

public abstract class ValueBoxTransform {

    protected float scale = this.getScale();

    public abstract Vec3 getLocalOffset(BlockState var1);

    public abstract void rotate(BlockState var1, PoseStack var2);

    public boolean testHit(BlockState state, Vec3 localHit) {
        Vec3 offset = this.getLocalOffset(state);
        return offset == null ? false : localHit.distanceTo(offset) < (double) (this.scale / 2.0F);
    }

    public void transform(BlockState state, PoseStack ms) {
        Vec3 position = this.getLocalOffset(state);
        if (position != null) {
            ms.translate(position.x, position.y, position.z);
            this.rotate(state, ms);
            ms.scale(this.scale, this.scale, this.scale);
        }
    }

    public boolean shouldRender(BlockState state) {
        return !state.m_60795_() && this.getLocalOffset(state) != null;
    }

    public int getOverrideColor() {
        return -1;
    }

    protected Vec3 rotateHorizontally(BlockState state, Vec3 vec) {
        float yRot = 0.0F;
        if (state.m_61138_(BlockStateProperties.FACING)) {
            yRot = AngleHelper.horizontalAngle((Direction) state.m_61143_(BlockStateProperties.FACING));
        }
        if (state.m_61138_(BlockStateProperties.HORIZONTAL_FACING)) {
            yRot = AngleHelper.horizontalAngle((Direction) state.m_61143_(BlockStateProperties.HORIZONTAL_FACING));
        }
        return VecHelper.rotateCentered(vec, (double) yRot, Direction.Axis.Y);
    }

    public float getScale() {
        return 0.5F;
    }

    public float getFontScale() {
        return 0.015625F;
    }

    public abstract static class Dual extends ValueBoxTransform {

        protected boolean first;

        public Dual(boolean first) {
            this.first = first;
        }

        public boolean isFirst() {
            return this.first;
        }

        public static Pair<ValueBoxTransform, ValueBoxTransform> makeSlots(Function<Boolean, ? extends ValueBoxTransform.Dual> factory) {
            return Pair.of((ValueBoxTransform) factory.apply(true), (ValueBoxTransform) factory.apply(false));
        }

        @Override
        public boolean testHit(BlockState state, Vec3 localHit) {
            Vec3 offset = this.getLocalOffset(state);
            return offset == null ? false : localHit.distanceTo(offset) < (double) (this.scale / 3.5F);
        }
    }

    public abstract static class Sided extends ValueBoxTransform {

        protected Direction direction = Direction.UP;

        public ValueBoxTransform.Sided fromSide(Direction direction) {
            this.direction = direction;
            return this;
        }

        @Override
        public Vec3 getLocalOffset(BlockState state) {
            Vec3 location = this.getSouthLocation();
            location = VecHelper.rotateCentered(location, (double) AngleHelper.horizontalAngle(this.getSide()), Direction.Axis.Y);
            return VecHelper.rotateCentered(location, (double) AngleHelper.verticalAngle(this.getSide()), Direction.Axis.X);
        }

        protected abstract Vec3 getSouthLocation();

        @Override
        public void rotate(BlockState state, PoseStack ms) {
            float yRot = AngleHelper.horizontalAngle(this.getSide()) + 180.0F;
            float xRot = this.getSide() == Direction.UP ? 90.0F : (this.getSide() == Direction.DOWN ? 270.0F : 0.0F);
            ((TransformStack) TransformStack.cast(ms).rotateY((double) yRot)).rotateX((double) xRot);
        }

        @Override
        public boolean shouldRender(BlockState state) {
            return super.shouldRender(state) && this.isSideActive(state, this.getSide());
        }

        @Override
        public boolean testHit(BlockState state, Vec3 localHit) {
            return this.isSideActive(state, this.getSide()) && super.testHit(state, localHit);
        }

        protected boolean isSideActive(BlockState state, Direction direction) {
            return true;
        }

        public Direction getSide() {
            return this.direction;
        }
    }
}