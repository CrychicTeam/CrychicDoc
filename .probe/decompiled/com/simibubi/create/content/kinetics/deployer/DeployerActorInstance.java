package com.simibubi.create.content.kinetics.deployer;

import com.jozufozu.flywheel.api.Material;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ActorInstance;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class DeployerActorInstance extends ActorInstance {

    private final PoseStack stack = new PoseStack();

    Direction facing;

    boolean stationaryTimer;

    float yRot;

    float xRot;

    float zRot;

    ModelData pole;

    ModelData hand;

    RotatingData shaft;

    public DeployerActorInstance(MaterialManager materialManager, VirtualRenderWorld simulationWorld, MovementContext context) {
        super(materialManager, simulationWorld, context);
        Material<ModelData> mat = materialManager.defaultSolid().material(Materials.TRANSFORMED);
        BlockState state = context.state;
        DeployerBlockEntity.Mode mode = NBTHelper.readEnum(context.blockEntityData, "Mode", DeployerBlockEntity.Mode.class);
        PartialModel handPose = DeployerRenderer.getHandPose(mode);
        this.stationaryTimer = context.data.contains("StationaryTimer");
        this.facing = (Direction) state.m_61143_(DirectionalKineticBlock.FACING);
        boolean rotatePole = (Boolean) state.m_61143_(DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE) ^ this.facing.getAxis() == Direction.Axis.Z;
        this.yRot = AngleHelper.horizontalAngle(this.facing);
        this.xRot = this.facing == Direction.UP ? 270.0F : (this.facing == Direction.DOWN ? 90.0F : 0.0F);
        this.zRot = rotatePole ? 90.0F : 0.0F;
        this.pole = (ModelData) mat.getModel(AllPartialModels.DEPLOYER_POLE, state).createInstance();
        this.hand = (ModelData) mat.getModel(handPose, state).createInstance();
        Direction.Axis axis = ((IRotate) state.m_60734_()).getRotationAxis(state);
        this.shaft = (RotatingData) materialManager.defaultSolid().material(AllMaterialSpecs.ROTATING).getModel(KineticBlockEntityInstance.shaft(axis)).createInstance();
        int blockLight = this.localBlockLight();
        this.shaft.setRotationAxis(axis).setPosition(context.localPos).setBlockLight(blockLight);
        this.pole.setBlockLight(blockLight);
        this.hand.setBlockLight(blockLight);
    }

    @Override
    public void beginFrame() {
        double factor;
        if (this.context.disabled) {
            factor = 0.0;
        } else if (!this.context.contraption.stalled && this.context.position != null && !this.context.data.contains("StationaryTimer")) {
            Vec3 center = VecHelper.getCenterOf(BlockPos.containing(this.context.position));
            double distance = this.context.position.distanceTo(center);
            double nextDistance = this.context.position.add(this.context.motion).distanceTo(center);
            factor = 0.5 - Mth.clamp(Mth.lerp((double) AnimationTickHolder.getPartialTicks(), distance, nextDistance), 0.0, 1.0);
        } else {
            factor = (double) (Mth.sin(AnimationTickHolder.getRenderTime() * 0.5F) * 0.25F + 0.25F);
        }
        Vec3 offset = Vec3.atLowerCornerOf(this.facing.getNormal()).scale(factor);
        TransformStack tstack = TransformStack.cast(this.stack);
        this.stack.setIdentity();
        ((TransformStack) tstack.translate(this.context.localPos)).translate(offset);
        transformModel(this.stack, this.pole, this.hand, this.yRot, this.xRot, this.zRot);
    }

    static void transformModel(PoseStack stack, ModelData pole, ModelData hand, float yRot, float xRot, float zRot) {
        TransformStack tstack = TransformStack.cast(stack);
        tstack.centre();
        tstack.rotate(Direction.UP, (float) ((double) (yRot / 180.0F) * Math.PI));
        tstack.rotate(Direction.EAST, (float) ((double) (xRot / 180.0F) * Math.PI));
        stack.pushPose();
        tstack.rotate(Direction.SOUTH, (float) ((double) (zRot / 180.0F) * Math.PI));
        tstack.unCentre();
        pole.setTransform(stack);
        stack.popPose();
        tstack.unCentre();
        hand.setTransform(stack);
    }
}