package net.minecraft.world.level.block.entity;

import com.google.common.annotations.VisibleForTesting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SculkCatalystBlock;
import net.minecraft.world.level.block.SculkSpreader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.Vec3;

public class SculkCatalystBlockEntity extends BlockEntity implements GameEventListener.Holder<SculkCatalystBlockEntity.CatalystListener> {

    private final SculkCatalystBlockEntity.CatalystListener catalystListener;

    public SculkCatalystBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(BlockEntityType.SCULK_CATALYST, blockPos0, blockState1);
        this.catalystListener = new SculkCatalystBlockEntity.CatalystListener(blockState1, new BlockPositionSource(blockPos0));
    }

    public static void serverTick(Level level0, BlockPos blockPos1, BlockState blockState2, SculkCatalystBlockEntity sculkCatalystBlockEntity3) {
        sculkCatalystBlockEntity3.catalystListener.getSculkSpreader().updateCursors(level0, blockPos1, level0.getRandom(), true);
    }

    @Override
    public void load(CompoundTag compoundTag0) {
        this.catalystListener.sculkSpreader.load(compoundTag0);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag0) {
        this.catalystListener.sculkSpreader.save(compoundTag0);
        super.saveAdditional(compoundTag0);
    }

    public SculkCatalystBlockEntity.CatalystListener getListener() {
        return this.catalystListener;
    }

    public static class CatalystListener implements GameEventListener {

        public static final int PULSE_TICKS = 8;

        final SculkSpreader sculkSpreader;

        private final BlockState blockState;

        private final PositionSource positionSource;

        public CatalystListener(BlockState blockState0, PositionSource positionSource1) {
            this.blockState = blockState0;
            this.positionSource = positionSource1;
            this.sculkSpreader = SculkSpreader.createLevelSpreader();
        }

        @Override
        public PositionSource getListenerSource() {
            return this.positionSource;
        }

        @Override
        public int getListenerRadius() {
            return 8;
        }

        @Override
        public GameEventListener.DeliveryMode getDeliveryMode() {
            return GameEventListener.DeliveryMode.BY_DISTANCE;
        }

        @Override
        public boolean handleGameEvent(ServerLevel serverLevel0, GameEvent gameEvent1, GameEvent.Context gameEventContext2, Vec3 vec3) {
            if (gameEvent1 == GameEvent.ENTITY_DIE && gameEventContext2.sourceEntity() instanceof LivingEntity $$4) {
                if (!$$4.wasExperienceConsumed()) {
                    int $$5 = $$4.getExperienceReward();
                    if ($$4.shouldDropExperience() && $$5 > 0) {
                        this.sculkSpreader.addCursors(BlockPos.containing(vec3.relative(Direction.UP, 0.5)), $$5);
                        this.tryAwardItSpreadsAdvancement(serverLevel0, $$4);
                    }
                    $$4.skipDropExperience();
                    this.positionSource.getPosition(serverLevel0).ifPresent(p_289513_ -> this.bloom(serverLevel0, BlockPos.containing(p_289513_), this.blockState, serverLevel0.m_213780_()));
                }
                return true;
            } else {
                return false;
            }
        }

        @VisibleForTesting
        public SculkSpreader getSculkSpreader() {
            return this.sculkSpreader;
        }

        private void bloom(ServerLevel serverLevel0, BlockPos blockPos1, BlockState blockState2, RandomSource randomSource3) {
            serverLevel0.m_7731_(blockPos1, (BlockState) blockState2.m_61124_(SculkCatalystBlock.PULSE, true), 3);
            serverLevel0.m_186460_(blockPos1, blockState2.m_60734_(), 8);
            serverLevel0.sendParticles(ParticleTypes.SCULK_SOUL, (double) blockPos1.m_123341_() + 0.5, (double) blockPos1.m_123342_() + 1.15, (double) blockPos1.m_123343_() + 0.5, 2, 0.2, 0.0, 0.2, 0.0);
            serverLevel0.m_5594_(null, blockPos1, SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.BLOCKS, 2.0F, 0.6F + randomSource3.nextFloat() * 0.4F);
        }

        private void tryAwardItSpreadsAdvancement(Level level0, LivingEntity livingEntity1) {
            if (livingEntity1.getLastHurtByMob() instanceof ServerPlayer $$3) {
                DamageSource $$4 = livingEntity1.getLastDamageSource() == null ? level0.damageSources().playerAttack($$3) : livingEntity1.getLastDamageSource();
                CriteriaTriggers.KILL_MOB_NEAR_SCULK_CATALYST.trigger($$3, livingEntity1, $$4);
            }
        }
    }
}