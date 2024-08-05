package com.simibubi.create.content.contraptions.bearing;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.mojang.math.Axis;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ActorInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Quaternionf;

public class StabilizedBearingInstance extends ActorInstance {

    final OrientedData topInstance;

    final RotatingData shaft;

    final Direction facing;

    final Axis rotationAxis;

    final Quaternionf blockOrientation;

    public StabilizedBearingInstance(MaterialManager materialManager, VirtualRenderWorld simulationWorld, MovementContext context) {
        super(materialManager, simulationWorld, context);
        BlockState blockState = context.state;
        this.facing = (Direction) blockState.m_61143_(BlockStateProperties.FACING);
        this.rotationAxis = Axis.of(Direction.get(Direction.AxisDirection.POSITIVE, this.facing.getAxis()).step());
        this.blockOrientation = BearingInstance.getBlockStateOrientation(this.facing);
        this.topInstance = (OrientedData) materialManager.defaultSolid().material(Materials.ORIENTED).getModel(AllPartialModels.BEARING_TOP, blockState).createInstance();
        int blockLight = this.localBlockLight();
        this.topInstance.setPosition(context.localPos).setRotation(this.blockOrientation).setBlockLight(blockLight);
        this.shaft = (RotatingData) materialManager.defaultSolid().material(AllMaterialSpecs.ROTATING).getModel(AllPartialModels.SHAFT_HALF, blockState, ((Direction) blockState.m_61143_(BlockStateProperties.FACING)).getOpposite()).createInstance();
        this.shaft.setPosition(context.localPos).setBlockLight(blockLight);
    }

    @Override
    public void beginFrame() {
        float counterRotationAngle = StabilizedBearingMovementBehaviour.getCounterRotationAngle(this.context, this.facing, AnimationTickHolder.getPartialTicks());
        Quaternionf rotation = this.rotationAxis.rotationDegrees(counterRotationAngle);
        rotation.mul(this.blockOrientation);
        this.topInstance.setRotation(rotation);
    }
}