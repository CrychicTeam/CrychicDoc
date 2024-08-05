package com.simibubi.create.content.kinetics.base;

import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public abstract class KineticBlock extends Block implements IRotate {

    public KineticBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (worldIn.getBlockEntity(pos) instanceof KineticBlockEntity kineticBlockEntity) {
            kineticBlockEntity.preventSpeedUpdate = 0;
            if (oldState.m_60734_() != state.m_60734_()) {
                return;
            }
            if (state.m_155947_() != oldState.m_155947_()) {
                return;
            }
            if (!this.areStatesKineticallyEquivalent(oldState, state)) {
                return;
            }
            kineticBlockEntity.preventSpeedUpdate = 2;
        }
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        IBE.onRemove(pState, pLevel, pPos, pNewState);
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return false;
    }

    protected boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState newState) {
        return oldState.m_60734_() != newState.m_60734_() ? false : this.getRotationAxis(newState) == this.getRotationAxis(oldState);
    }

    @Override
    public void updateIndirectNeighbourShapes(BlockState stateIn, LevelAccessor worldIn, BlockPos pos, int flags, int count) {
        if (!worldIn.m_5776_()) {
            if (worldIn.m_7702_(pos) instanceof KineticBlockEntity kbe) {
                if (kbe.preventSpeedUpdate <= 0) {
                    kbe.warnOfMovement();
                    kbe.clearKineticInformation();
                    kbe.updateSpeed = true;
                }
            }
        }
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        AdvancementBehaviour.setPlacedBy(worldIn, pos, placer);
        if (!worldIn.isClientSide) {
            if (worldIn.getBlockEntity(pos) instanceof KineticBlockEntity kbe) {
                kbe.effects.queueRotationIndicators();
            }
        }
    }

    public float getParticleTargetRadius() {
        return 0.65F;
    }

    public float getParticleInitialRadius() {
        return 0.75F;
    }
}