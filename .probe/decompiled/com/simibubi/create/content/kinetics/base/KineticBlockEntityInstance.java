package com.simibubi.create.content.kinetics.base;

import com.jozufozu.flywheel.api.Material;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public abstract class KineticBlockEntityInstance<T extends KineticBlockEntity> extends BlockEntityInstance<T> {

    protected final Direction.Axis axis;

    public KineticBlockEntityInstance(MaterialManager materialManager, T blockEntity) {
        super(materialManager, blockEntity);
        this.axis = this.blockState.m_60734_() instanceof IRotate irotate ? irotate.getRotationAxis(this.blockState) : Direction.Axis.Y;
    }

    protected final void updateRotation(RotatingData instance) {
        this.updateRotation(instance, this.getRotationAxis(), this.getBlockEntitySpeed());
    }

    protected final void updateRotation(RotatingData instance, Direction.Axis axis) {
        this.updateRotation(instance, axis, this.getBlockEntitySpeed());
    }

    protected final void updateRotation(RotatingData instance, float speed) {
        this.updateRotation(instance, this.getRotationAxis(), speed);
    }

    protected final void updateRotation(RotatingData instance, Direction.Axis axis, float speed) {
        instance.setRotationAxis(axis).setRotationOffset(this.getRotationOffset(axis)).setRotationalSpeed(speed).setColor((KineticBlockEntity) this.blockEntity);
    }

    protected final RotatingData setup(RotatingData key) {
        return this.setup(key, this.getRotationAxis(), this.getBlockEntitySpeed());
    }

    protected final RotatingData setup(RotatingData key, Direction.Axis axis) {
        return this.setup(key, axis, this.getBlockEntitySpeed());
    }

    protected final RotatingData setup(RotatingData key, float speed) {
        return this.setup(key, this.getRotationAxis(), speed);
    }

    protected final RotatingData setup(RotatingData key, Direction.Axis axis, float speed) {
        key.setRotationAxis(axis).setRotationalSpeed(speed).setRotationOffset(this.getRotationOffset(axis)).setColor((KineticBlockEntity) this.blockEntity).setPosition(this.getInstancePosition());
        return key;
    }

    protected float getRotationOffset(Direction.Axis axis) {
        float offset = ICogWheel.isLargeCog(this.blockState) ? 11.25F : 0.0F;
        double d = (double) (((axis == Direction.Axis.X ? 0 : this.pos.m_123341_()) + (axis == Direction.Axis.Y ? 0 : this.pos.m_123342_()) + (axis == Direction.Axis.Z ? 0 : this.pos.m_123343_())) % 2);
        if (d == 0.0) {
            offset = 22.5F;
        }
        return offset;
    }

    protected Direction.Axis getRotationAxis() {
        return this.axis;
    }

    protected float getBlockEntitySpeed() {
        return ((KineticBlockEntity) this.blockEntity).getSpeed();
    }

    protected BlockState shaft() {
        return shaft(this.getRotationAxis());
    }

    protected Material<RotatingData> getRotatingMaterial() {
        return this.materialManager.defaultSolid().material(AllMaterialSpecs.ROTATING);
    }

    public static BlockState shaft(Direction.Axis axis) {
        return (BlockState) AllBlocks.SHAFT.getDefaultState().m_61124_(ShaftBlock.AXIS, axis);
    }
}