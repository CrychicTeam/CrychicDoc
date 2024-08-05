package com.simibubi.create.content.contraptions.actors.trainControls;

import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import java.util.Collection;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ControlsMovementBehaviour implements MovementBehaviour {

    @Override
    public ItemStack canBeDisabledVia(MovementContext context) {
        return null;
    }

    @Override
    public void stopMoving(MovementContext context) {
        context.contraption.entity.stopControlling(context.localPos);
        MovementBehaviour.super.stopMoving(context);
    }

    @Override
    public void tick(MovementContext context) {
        MovementBehaviour.super.tick(context);
        if (context.world.isClientSide) {
            if (!(context.temporaryData instanceof ControlsMovementBehaviour.LeverAngles)) {
                context.temporaryData = new ControlsMovementBehaviour.LeverAngles();
            }
            ControlsMovementBehaviour.LeverAngles angles = (ControlsMovementBehaviour.LeverAngles) context.temporaryData;
            angles.steering.tickChaser();
            angles.speed.tickChaser();
            angles.equipAnimation.tickChaser();
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld, ContraptionMatrices matrices, MultiBufferSource buffer) {
        if (context.temporaryData instanceof ControlsMovementBehaviour.LeverAngles angles) {
            AbstractContraptionEntity entity = context.contraption.entity;
            if (entity instanceof CarriageContraptionEntity cce) {
                StructureTemplate.StructureBlockInfo info = (StructureTemplate.StructureBlockInfo) context.contraption.getBlocks().get(context.localPos);
                Direction initialOrientation = cce.getInitialOrientation().getCounterClockWise();
                boolean inverted = false;
                if (info != null && info.state().m_61138_(ControlsBlock.f_54117_)) {
                    inverted = !((Direction) info.state().m_61143_(ControlsBlock.f_54117_)).equals(initialOrientation);
                }
                if (ControlsHandler.getContraption() == entity && ControlsHandler.getControlsPos() != null && ControlsHandler.getControlsPos().equals(context.localPos)) {
                    Collection<Integer> pressed = ControlsHandler.currentlyPressed;
                    angles.equipAnimation.chase(1.0, 0.2F, LerpedFloat.Chaser.EXP);
                    angles.steering.chase((double) ((pressed.contains(3) ? 1 : 0) + (pressed.contains(2) ? -1 : 0)), 0.2F, LerpedFloat.Chaser.EXP);
                    float f = cce.movingBackwards ^ inverted ? -1.0F : 1.0F;
                    angles.speed.chase(Math.min(context.motion.length(), 0.5) * (double) f, 0.2F, LerpedFloat.Chaser.EXP);
                } else {
                    angles.equipAnimation.chase(0.0, 0.2F, LerpedFloat.Chaser.EXP);
                    angles.steering.chase(0.0, 0.0, LerpedFloat.Chaser.EXP);
                    angles.speed.chase(0.0, 0.0, LerpedFloat.Chaser.EXP);
                }
                float pt = AnimationTickHolder.getPartialTicks(context.world);
                ControlsRenderer.render(context, renderWorld, matrices, buffer, angles.equipAnimation.getValue(pt), angles.speed.getValue(pt), angles.steering.getValue(pt));
            }
        }
    }

    static class LeverAngles {

        LerpedFloat steering = LerpedFloat.linear();

        LerpedFloat speed = LerpedFloat.linear();

        LerpedFloat equipAnimation = LerpedFloat.linear();
    }
}