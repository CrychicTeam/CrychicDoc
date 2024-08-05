package com.simibubi.create.content.kinetics.simpleRelays;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.materials.FlatLit;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class BracketedKineticBlockEntityInstance extends SingleRotatingInstance<BracketedKineticBlockEntity> {

    protected RotatingData additionalShaft;

    public BracketedKineticBlockEntityInstance(MaterialManager materialManager, BracketedKineticBlockEntity blockEntity) {
        super(materialManager, blockEntity);
    }

    @Override
    public void init() {
        super.init();
        if (ICogWheel.isLargeCog(((BracketedKineticBlockEntity) this.blockEntity).m_58900_())) {
            float speed = ((BracketedKineticBlockEntity) this.blockEntity).getSpeed();
            Direction.Axis axis = KineticBlockEntityRenderer.getRotationAxisOf((KineticBlockEntity) this.blockEntity);
            BlockPos pos = ((BracketedKineticBlockEntity) this.blockEntity).m_58899_();
            float offset = BracketedKineticBlockEntityRenderer.getShaftAngleOffset(axis, pos);
            Direction facing = Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE);
            Instancer<RotatingData> half = this.getRotatingMaterial().getModel(AllPartialModels.COGWHEEL_SHAFT, this.blockState, facing, () -> this.rotateToAxis(axis));
            this.additionalShaft = this.setup((RotatingData) half.createInstance(), speed);
            this.additionalShaft.setRotationOffset(offset);
        }
    }

    @Override
    protected Instancer<RotatingData> getModel() {
        if (!ICogWheel.isLargeCog(((BracketedKineticBlockEntity) this.blockEntity).m_58900_())) {
            return super.getModel();
        } else {
            Direction.Axis axis = KineticBlockEntityRenderer.getRotationAxisOf((KineticBlockEntity) this.blockEntity);
            Direction facing = Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE);
            return this.getRotatingMaterial().getModel(AllPartialModels.SHAFTLESS_LARGE_COGWHEEL, this.blockState, facing, () -> this.rotateToAxis(axis));
        }
    }

    private PoseStack rotateToAxis(Direction.Axis axis) {
        Direction facing = Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE);
        PoseStack poseStack = new PoseStack();
        ((TransformStack) ((TransformStack) ((TransformStack) TransformStack.cast(poseStack).centre()).rotateToFace(facing)).multiply(Axis.XN.rotationDegrees(-90.0F))).unCentre();
        return poseStack;
    }

    @Override
    public void update() {
        super.update();
        if (this.additionalShaft != null) {
            this.updateRotation(this.additionalShaft);
            this.additionalShaft.setRotationOffset(BracketedKineticBlockEntityRenderer.getShaftAngleOffset(this.axis, this.pos));
        }
    }

    @Override
    public void updateLight() {
        super.updateLight();
        if (this.additionalShaft != null) {
            this.relight(this.pos, new FlatLit[] { this.additionalShaft });
        }
    }

    @Override
    public void remove() {
        super.remove();
        if (this.additionalShaft != null) {
            this.additionalShaft.delete();
        }
    }
}