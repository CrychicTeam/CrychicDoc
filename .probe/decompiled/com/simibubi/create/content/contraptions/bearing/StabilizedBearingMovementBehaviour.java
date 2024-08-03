package com.simibubi.create.content.contraptions.bearing;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.mojang.math.Axis;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.ControlledContraptionEntity;
import com.simibubi.create.content.contraptions.OrientedContraptionEntity;
import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ActorInstance;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.content.contraptions.render.ContraptionRenderDispatcher;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;

public class StabilizedBearingMovementBehaviour implements MovementBehaviour {

    @Override
    public ItemStack canBeDisabledVia(MovementContext context) {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld, ContraptionMatrices matrices, MultiBufferSource buffer) {
        if (!ContraptionRenderDispatcher.canInstance()) {
            Direction facing = (Direction) context.state.m_61143_(BlockStateProperties.FACING);
            PartialModel top = AllPartialModels.BEARING_TOP;
            SuperByteBuffer superBuffer = CachedBufferer.partial(top, context.state);
            float renderPartialTicks = AnimationTickHolder.getPartialTicks();
            Quaternionf orientation = BearingInstance.getBlockStateOrientation(facing);
            float angle = getCounterRotationAngle(context, facing, renderPartialTicks) * (float) facing.getAxisDirection().getStep();
            Quaternionf rotation = Axis.of(facing.step()).rotationDegrees(angle);
            rotation.mul(orientation);
            superBuffer.transform(matrices.getModel());
            superBuffer.rotateCentered(rotation);
            superBuffer.light(matrices.getWorld(), ContraptionRenderDispatcher.getContraptionWorldLight(context, renderWorld)).renderInto(matrices.getViewProjection(), buffer.getBuffer(RenderType.solid()));
        }
    }

    @Override
    public boolean hasSpecialInstancedRendering() {
        return true;
    }

    @Nullable
    @Override
    public ActorInstance createInstance(MaterialManager materialManager, VirtualRenderWorld simulationWorld, MovementContext context) {
        return new StabilizedBearingInstance(materialManager, simulationWorld, context);
    }

    static float getCounterRotationAngle(MovementContext context, Direction facing, float renderPartialTicks) {
        if (!context.contraption.canBeStabilized(facing, context.localPos)) {
            return 0.0F;
        } else {
            float offset = 0.0F;
            Direction.Axis axis = facing.getAxis();
            AbstractContraptionEntity entity = context.contraption.entity;
            if (entity instanceof ControlledContraptionEntity controlledCE) {
                if (context.contraption.canBeStabilized(facing, context.localPos)) {
                    offset = -controlledCE.getAngle(renderPartialTicks);
                }
            } else if (entity instanceof OrientedContraptionEntity orientedCE) {
                if (axis.isVertical()) {
                    offset = -orientedCE.getViewYRot(renderPartialTicks);
                } else if (orientedCE.isInitialOrientationPresent() && orientedCE.getInitialOrientation().getAxis() == axis) {
                    offset = -orientedCE.getViewXRot(renderPartialTicks);
                }
            }
            return offset;
        }
    }
}