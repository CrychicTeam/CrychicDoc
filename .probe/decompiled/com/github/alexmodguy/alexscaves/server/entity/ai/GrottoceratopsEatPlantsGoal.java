package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.DoublePlantWithRotationBlock;
import com.github.alexmodguy.alexscaves.server.entity.living.GrottoceratopsEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.Vec3;

public class GrottoceratopsEatPlantsGoal extends MoveToBlockGoal {

    private final GrottoceratopsEntity grottoceratops;

    public GrottoceratopsEatPlantsGoal(GrottoceratopsEntity entity, int range) {
        super(entity, 1.0, range, 6);
        this.grottoceratops = entity;
    }

    @Override
    protected int nextStartTick(PathfinderMob mob) {
        return m_186073_(200 + this.grottoceratops.m_217043_().nextInt(200));
    }

    @Override
    public boolean canUse() {
        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && this.grottoceratops.m_21120_(InteractionHand.MAIN_HAND).isEmpty();
    }

    @Override
    public double acceptedDistance() {
        return (double) (this.grottoceratops.m_20205_() + 1.0F);
    }

    @Override
    public void tick() {
        super.tick();
        BlockPos target = this.m_6669_();
        if (target != null) {
            this.grottoceratops.m_7618_(EntityAnchorArgument.Anchor.EYES, Vec3.atCenterOf(target));
            if (this.m_25625_()) {
                if (this.grottoceratops.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                    this.grottoceratops.setAnimation(GrottoceratopsEntity.ANIMATION_CHEW_FROM_GROUND);
                } else if (this.grottoceratops.getAnimation() == GrottoceratopsEntity.ANIMATION_CHEW_FROM_GROUND && this.grottoceratops.getAnimationTick() > 15) {
                    BlockState state = this.grottoceratops.m_9236_().getBlockState(target);
                    this.grottoceratops.m_9236_().m_46961_(target, false);
                    if (state.m_60713_(ACBlockRegistry.CURLY_FERN.get())) {
                        this.grottoceratops.m_9236_().setBlockAndUpdate(target, ACBlockRegistry.FIDDLEHEAD.get().defaultBlockState());
                    }
                }
            }
        }
    }

    @Override
    public void stop() {
        super.m_8041_();
        this.f_25602_ = BlockPos.ZERO;
    }

    @Override
    protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
        if (pos != null) {
            BlockState state = worldIn.m_8055_(pos.above());
            return state.m_60713_(ACBlockRegistry.CURLY_FERN.get()) ? state.m_61143_(DoublePlantWithRotationBlock.f_52858_) == DoubleBlockHalf.LOWER : state.m_204336_(ACTagRegistry.GROTTOCERATOPS_FOOD_BLOCKS);
        } else {
            return false;
        }
    }
}