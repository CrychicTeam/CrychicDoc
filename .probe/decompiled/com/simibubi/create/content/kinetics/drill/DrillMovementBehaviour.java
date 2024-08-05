package com.simibubi.create.content.kinetics.drill;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ActorInstance;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.content.contraptions.render.ContraptionRenderDispatcher;
import com.simibubi.create.content.kinetics.base.BlockBreakingMovementBehaviour;
import com.simibubi.create.foundation.damageTypes.CreateDamageSources;
import com.simibubi.create.foundation.utility.VecHelper;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DrillMovementBehaviour extends BlockBreakingMovementBehaviour {

    @Override
    public boolean isActive(MovementContext context) {
        return super.isActive(context) && !VecHelper.isVecPointingTowards(context.relativeMotion, ((Direction) context.state.m_61143_(DrillBlock.FACING)).getOpposite());
    }

    @Override
    public Vec3 getActiveAreaOffset(MovementContext context) {
        return Vec3.atLowerCornerOf(((Direction) context.state.m_61143_(DrillBlock.FACING)).getNormal()).scale(0.65F);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld, ContraptionMatrices matrices, MultiBufferSource buffer) {
        if (!ContraptionRenderDispatcher.canInstance()) {
            DrillRenderer.renderInContraption(context, renderWorld, matrices, buffer);
        }
    }

    @Override
    public boolean hasSpecialInstancedRendering() {
        return true;
    }

    @Nullable
    @Override
    public ActorInstance createInstance(MaterialManager materialManager, VirtualRenderWorld simulationWorld, MovementContext context) {
        return new DrillActorInstance(materialManager, simulationWorld, context);
    }

    @Override
    protected DamageSource getDamageSource(Level level) {
        return CreateDamageSources.drill(level);
    }

    @Override
    public boolean canBreak(Level world, BlockPos breakingPos, BlockState state) {
        return super.canBreak(world, breakingPos, state) && !state.m_60812_(world, breakingPos).isEmpty() && !AllTags.AllBlockTags.TRACKS.matches(state);
    }
}