package com.simibubi.create.content.processing.burner;

import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlazeBurnerMovementBehaviour implements MovementBehaviour {

    @Override
    public boolean renderAsNormalBlockEntity() {
        return false;
    }

    @Override
    public ItemStack canBeDisabledVia(MovementContext context) {
        return null;
    }

    @Override
    public void tick(MovementContext context) {
        if (context.world.isClientSide()) {
            if (this.shouldRender(context)) {
                RandomSource r = context.world.getRandom();
                Vec3 c = context.position;
                Vec3 v = c.add(VecHelper.offsetRandomly(Vec3.ZERO, r, 0.125F).multiply(1.0, 0.0, 1.0));
                if (r.nextInt(3) == 0 && context.motion.length() < 0.015625) {
                    context.world.addParticle(ParticleTypes.LARGE_SMOKE, v.x, v.y, v.z, 0.0, 0.0, 0.0);
                }
                LerpedFloat headAngle = this.getHeadAngle(context);
                boolean quickTurn = this.shouldRenderHat(context) && !Mth.equal(context.relativeMotion.length(), 0.0);
                headAngle.chase((double) (headAngle.getValue() + AngleHelper.getShortestAngleDiff((double) headAngle.getValue(), (double) this.getTargetAngle(context))), 0.5, quickTurn ? LerpedFloat.Chaser.EXP : LerpedFloat.Chaser.exp(5.0));
                headAngle.tickChaser();
            }
        }
    }

    public void invalidate(MovementContext context) {
        context.data.remove("Conductor");
    }

    private boolean shouldRender(MovementContext context) {
        return context.state.m_61145_(BlazeBurnerBlock.HEAT_LEVEL).orElse(BlazeBurnerBlock.HeatLevel.NONE) != BlazeBurnerBlock.HeatLevel.NONE;
    }

    private LerpedFloat getHeadAngle(MovementContext context) {
        if (!(context.temporaryData instanceof LerpedFloat)) {
            context.temporaryData = LerpedFloat.angular().startWithValue((double) this.getTargetAngle(context));
        }
        return (LerpedFloat) context.temporaryData;
    }

    private float getTargetAngle(MovementContext context) {
        if (this.shouldRenderHat(context) && !Mth.equal(context.relativeMotion.length(), 0.0) && context.contraption.entity instanceof CarriageContraptionEntity cce) {
            float angle = AngleHelper.deg(-Mth.atan2(context.relativeMotion.x, context.relativeMotion.z));
            return cce.getInitialOrientation().getAxis() == Direction.Axis.X ? angle + 180.0F : angle;
        } else {
            Entity player = Minecraft.getInstance().cameraEntity;
            if (player != null && !player.isInvisible() && context.position != null) {
                Vec3 applyRotation = context.contraption.entity.reverseRotation(player.position().subtract(context.position), 1.0F);
                double dx = applyRotation.x;
                double dz = applyRotation.z;
                return AngleHelper.deg(-Mth.atan2(dz, dx)) - 90.0F;
            } else {
                return 0.0F;
            }
        }
    }

    private boolean shouldRenderHat(MovementContext context) {
        CompoundTag data = context.data;
        if (!data.contains("Conductor")) {
            data.putBoolean("Conductor", this.determineIfConducting(context));
        }
        return data.getBoolean("Conductor") && context.contraption.entity instanceof CarriageContraptionEntity cce && cce.hasSchedule();
    }

    private boolean determineIfConducting(MovementContext context) {
        if (context.contraption instanceof CarriageContraption carriageContraption) {
            Direction assemblyDirection = carriageContraption.getAssemblyDirection();
            for (Direction direction : Iterate.directionsInAxis(assemblyDirection.getAxis())) {
                if (carriageContraption.inControl(context.localPos, direction)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld, ContraptionMatrices matrices, MultiBufferSource buffer) {
        if (this.shouldRender(context)) {
            BlazeBurnerRenderer.renderInContraption(context, renderWorld, matrices, buffer, this.getHeadAngle(context), this.shouldRenderHat(context));
        }
    }
}