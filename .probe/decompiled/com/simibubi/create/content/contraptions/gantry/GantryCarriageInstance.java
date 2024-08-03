package com.simibubi.create.content.contraptions.gantry;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.materials.FlatLit;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.base.ShaftInstance;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

public class GantryCarriageInstance extends ShaftInstance<GantryCarriageBlockEntity> implements DynamicInstance {

    private final ModelData gantryCogs;

    final Direction facing;

    final Boolean alongFirst;

    final Direction.Axis rotationAxis;

    final float rotationMult;

    final BlockPos visualPos;

    private float lastAngle = Float.NaN;

    public GantryCarriageInstance(MaterialManager materialManager, GantryCarriageBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        this.gantryCogs = (ModelData) this.getTransformMaterial().getModel(AllPartialModels.GANTRY_COGS, this.blockState).createInstance();
        this.facing = (Direction) this.blockState.m_61143_(GantryCarriageBlock.FACING);
        this.alongFirst = (Boolean) this.blockState.m_61143_(GantryCarriageBlock.AXIS_ALONG_FIRST_COORDINATE);
        this.rotationAxis = KineticBlockEntityRenderer.getRotationAxisOf(blockEntity);
        this.rotationMult = getRotationMultiplier(this.getGantryAxis(), this.facing);
        this.visualPos = this.facing.getAxisDirection() == Direction.AxisDirection.POSITIVE ? blockEntity.m_58899_() : blockEntity.m_58899_().relative(this.facing.getOpposite());
        this.animateCogs(this.getCogAngle());
    }

    public void beginFrame() {
        float cogAngle = this.getCogAngle();
        if (!Mth.equal(cogAngle, this.lastAngle)) {
            this.animateCogs(cogAngle);
        }
    }

    private float getCogAngle() {
        return GantryCarriageRenderer.getAngleForBE((KineticBlockEntity) this.blockEntity, this.visualPos, this.rotationAxis) * this.rotationMult;
    }

    private void animateCogs(float cogAngle) {
        ((ModelData) ((ModelData) ((ModelData) ((ModelData) ((ModelData) ((ModelData) this.gantryCogs.loadIdentity().translate(this.getInstancePosition())).centre()).rotateY((double) AngleHelper.horizontalAngle(this.facing))).rotateX(this.facing == Direction.UP ? 0.0 : (this.facing == Direction.DOWN ? 180.0 : 90.0))).rotateY(this.alongFirst ^ this.facing.getAxis() == Direction.Axis.X ? 0.0 : 90.0)).translate(0.0, -0.5625, 0.0).rotateX((double) (-cogAngle))).translate(0.0, 0.5625, 0.0).unCentre();
    }

    static float getRotationMultiplier(Direction.Axis gantryAxis, Direction facing) {
        float multiplier = 1.0F;
        if (gantryAxis == Direction.Axis.X && facing == Direction.UP) {
            multiplier *= -1.0F;
        }
        if (gantryAxis == Direction.Axis.Y && (facing == Direction.NORTH || facing == Direction.EAST)) {
            multiplier *= -1.0F;
        }
        return multiplier;
    }

    private Direction.Axis getGantryAxis() {
        Direction.Axis gantryAxis = Direction.Axis.X;
        for (Direction.Axis axis : Iterate.axes) {
            if (axis != this.rotationAxis && axis != this.facing.getAxis()) {
                gantryAxis = axis;
            }
        }
        return gantryAxis;
    }

    @Override
    public void updateLight() {
        this.relight(this.pos, new FlatLit[] { this.gantryCogs, this.rotatingModel });
    }

    @Override
    public void remove() {
        super.remove();
        this.gantryCogs.delete();
    }
}