package net.mehvahdjukaar.amendments.common.entity;

import net.mehvahdjukaar.amendments.Amendments;
import net.mehvahdjukaar.amendments.configs.CommonConfigs;
import net.mehvahdjukaar.amendments.integration.CompatHandler;
import net.mehvahdjukaar.amendments.integration.SuppCompat;
import net.mehvahdjukaar.amendments.reg.ModRegistry;
import net.mehvahdjukaar.moonlight.api.entity.ImprovedFallingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;

public class FallingLanternEntity extends ImprovedFallingBlockEntity {

    public FallingLanternEntity(EntityType<FallingLanternEntity> type, Level level) {
        super(type, level);
    }

    public FallingLanternEntity(Level level) {
        super((EntityType<? extends FallingBlockEntity>) ModRegistry.FALLING_LANTERN.get(), level);
    }

    public FallingLanternEntity(Level level, BlockPos pos, BlockState blockState, double yOffset) {
        super((EntityType<? extends FallingBlockEntity>) ModRegistry.FALLING_LANTERN.get(), level, pos, blockState, false);
        this.f_19855_ = (double) pos.m_123342_() + yOffset;
    }

    public static FallingBlockEntity fall(Level level, BlockPos pos, BlockState state, double yOffset) {
        FallingLanternEntity entity = new FallingLanternEntity(level, pos, state, yOffset);
        level.setBlock(pos, state.m_60819_().createLegacyBlock(), 3);
        level.m_7967_(entity);
        return entity;
    }

    @Override
    public boolean causeFallDamage(float height, float amount, DamageSource source) {
        boolean r = super.causeFallDamage(height, amount, source);
        if (((FallingLanternEntity.FallMode) CommonConfigs.FALLING_LANTERNS.get()).hasFire() && this.m_20184_().lengthSqr() > 0.16000000000000003) {
            BlockState state = this.m_31980_();
            BlockPos pos = BlockPos.containing(this.m_20185_(), this.m_20186_() + 0.25, this.m_20189_());
            Level level = this.m_9236_();
            level.m_5898_(null, 2001, pos, Block.getId(state));
            if (state.m_60791_() != 0) {
                if (CompatHandler.SUPPLEMENTARIES) {
                    SuppCompat.createMiniExplosion(level, pos, true);
                } else if (level.getBlockState(pos).m_60795_() && BaseFireBlock.canBePlacedAt(level, pos, Direction.DOWN)) {
                    level.setBlockAndUpdate(pos, BaseFireBlock.getState(level, pos));
                }
            } else {
                this.m_19998_(state.m_60734_());
            }
            this.setCancelDrop(true);
            this.m_146870_();
        }
        return r;
    }

    public static boolean canSurviveCeilingAndMaybeFall(BlockState state, BlockPos pos, LevelReader worldIn) {
        if (!Amendments.isSupportingCeiling(pos.above(), worldIn) && worldIn instanceof Level l) {
            return ((FallingLanternEntity.FallMode) CommonConfigs.FALLING_LANTERNS.get()).isOn() && l.getBlockState(pos).m_60713_(state.m_60734_()) ? createFallingLantern(state, pos, l) : false;
        } else {
            return true;
        }
    }

    public static boolean createFallingLantern(BlockState state, BlockPos pos, Level level) {
        if (FallingBlock.isFree(level.getBlockState(pos.below())) && pos.m_123342_() >= level.m_141937_() && state.m_61138_(LanternBlock.HANGING)) {
            double maxY = state.m_60808_(level, pos).bounds().maxY;
            state = (BlockState) state.m_61124_(LanternBlock.HANGING, false);
            double yOffset = maxY - state.m_60808_(level, pos).bounds().maxY;
            fall(level, pos, state, yOffset);
            return true;
        } else {
            return false;
        }
    }

    public static enum FallMode {

        ON, OFF, NO_FIRE;

        public boolean hasFire() {
            return this != NO_FIRE;
        }

        public boolean isOn() {
            return this != OFF;
        }
    }
}