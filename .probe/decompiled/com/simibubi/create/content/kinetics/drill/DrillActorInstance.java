package com.simibubi.create.content.kinetics.drill;

import com.jozufozu.flywheel.api.Material;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.contraptions.actors.flwdata.ActorData;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ActorInstance;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaternionf;

public class DrillActorInstance extends ActorInstance {

    ActorData drillHead;

    private final Direction facing;

    public DrillActorInstance(MaterialManager materialManager, VirtualRenderWorld contraption, MovementContext context) {
        super(materialManager, contraption, context);
        Material<ActorData> material = materialManager.defaultSolid().material(AllMaterialSpecs.ACTORS);
        BlockState state = context.state;
        this.facing = (Direction) state.m_61143_(DrillBlock.FACING);
        Direction.Axis axis = this.facing.getAxis();
        float eulerX = AngleHelper.verticalAngle(this.facing);
        float eulerY;
        if (axis == Direction.Axis.Y) {
            eulerY = 0.0F;
        } else {
            eulerY = this.facing.toYRot() + (float) (axis == Direction.Axis.X ? 180 : 0);
        }
        this.drillHead = (ActorData) material.getModel(AllPartialModels.DRILL_HEAD, state).createInstance();
        this.drillHead.setPosition(context.localPos).setBlockLight(this.localBlockLight()).setRotationOffset(0.0F).setRotationAxis(0.0F, 0.0F, 1.0F).setLocalRotation(new Quaternionf().rotationXYZ(eulerX * (float) (Math.PI / 180.0), eulerY * (float) (Math.PI / 180.0), 0.0F)).setSpeed(this.getSpeed(this.facing));
    }

    @Override
    public void beginFrame() {
        this.drillHead.setSpeed(this.getSpeed(this.facing));
    }

    protected float getSpeed(Direction facing) {
        return !this.context.contraption.stalled && VecHelper.isVecPointingTowards(this.context.relativeMotion, facing.getOpposite()) ? 0.0F : this.context.getAnimationSpeed();
    }
}