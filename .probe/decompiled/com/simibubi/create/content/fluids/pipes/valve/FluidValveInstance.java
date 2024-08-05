package com.simibubi.create.content.fluids.pipes.valve;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.FlatLit;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.base.ShaftInstance;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

public class FluidValveInstance extends ShaftInstance<FluidValveBlockEntity> implements DynamicInstance {

    protected ModelData pointer;

    protected boolean settled;

    protected final double xRot;

    protected final double yRot;

    protected final int pointerRotationOffset;

    public FluidValveInstance(MaterialManager dispatcher, FluidValveBlockEntity blockEntity) {
        super(dispatcher, blockEntity);
        Direction facing = (Direction) this.blockState.m_61143_(FluidValveBlock.FACING);
        this.yRot = (double) AngleHelper.horizontalAngle(facing);
        this.xRot = facing == Direction.UP ? 0.0 : (facing == Direction.DOWN ? 180.0 : 90.0);
        Direction.Axis pipeAxis = FluidValveBlock.getPipeAxis(this.blockState);
        Direction.Axis shaftAxis = KineticBlockEntityRenderer.getRotationAxisOf(blockEntity);
        boolean twist = pipeAxis.isHorizontal() && shaftAxis == Direction.Axis.X || pipeAxis.isVertical();
        this.pointerRotationOffset = twist ? 90 : 0;
        this.settled = false;
        this.pointer = (ModelData) this.materialManager.defaultSolid().material(Materials.TRANSFORMED).getModel(AllPartialModels.FLUID_VALVE_POINTER, this.blockState).createInstance();
        this.transformPointer();
    }

    public void beginFrame() {
        if (!((FluidValveBlockEntity) this.blockEntity).pointer.settled() || !this.settled) {
            this.transformPointer();
        }
    }

    private void transformPointer() {
        float value = ((FluidValveBlockEntity) this.blockEntity).pointer.getValue(AnimationTickHolder.getPartialTicks());
        float pointerRotation = Mth.lerp(value, 0.0F, -90.0F);
        this.settled = (value == 0.0F || value == 1.0F) && ((FluidValveBlockEntity) this.blockEntity).pointer.settled();
        ((ModelData) ((ModelData) ((ModelData) ((ModelData) ((ModelData) this.pointer.loadIdentity().translate(this.getInstancePosition())).centre()).rotateY(this.yRot)).rotateX(this.xRot)).rotateY((double) ((float) this.pointerRotationOffset + pointerRotation))).unCentre();
    }

    @Override
    public void updateLight() {
        super.updateLight();
        this.relight(this.pos, new FlatLit[] { this.pointer });
    }

    @Override
    public void remove() {
        super.remove();
        this.pointer.delete();
    }
}