package net.minecraft.world.level.block.entity;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ConduitBlockEntity extends BlockEntity {

    private static final int BLOCK_REFRESH_RATE = 2;

    private static final int EFFECT_DURATION = 13;

    private static final float ROTATION_SPEED = -0.0375F;

    private static final int MIN_ACTIVE_SIZE = 16;

    private static final int MIN_KILL_SIZE = 42;

    private static final int KILL_RANGE = 8;

    private static final Block[] VALID_BLOCKS = new Block[] { Blocks.PRISMARINE, Blocks.PRISMARINE_BRICKS, Blocks.SEA_LANTERN, Blocks.DARK_PRISMARINE };

    public int tickCount;

    private float activeRotation;

    private boolean isActive;

    private boolean isHunting;

    private final List<BlockPos> effectBlocks = Lists.newArrayList();

    @Nullable
    private LivingEntity destroyTarget;

    @Nullable
    private UUID destroyTargetUUID;

    private long nextAmbientSoundActivation;

    public ConduitBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(BlockEntityType.CONDUIT, blockPos0, blockState1);
    }

    @Override
    public void load(CompoundTag compoundTag0) {
        super.load(compoundTag0);
        if (compoundTag0.hasUUID("Target")) {
            this.destroyTargetUUID = compoundTag0.getUUID("Target");
        } else {
            this.destroyTargetUUID = null;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag0) {
        super.saveAdditional(compoundTag0);
        if (this.destroyTarget != null) {
            compoundTag0.putUUID("Target", this.destroyTarget.m_20148_());
        }
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    public static void clientTick(Level level0, BlockPos blockPos1, BlockState blockState2, ConduitBlockEntity conduitBlockEntity3) {
        conduitBlockEntity3.tickCount++;
        long $$4 = level0.getGameTime();
        List<BlockPos> $$5 = conduitBlockEntity3.effectBlocks;
        if ($$4 % 40L == 0L) {
            conduitBlockEntity3.isActive = updateShape(level0, blockPos1, $$5);
            updateHunting(conduitBlockEntity3, $$5);
        }
        updateClientTarget(level0, blockPos1, conduitBlockEntity3);
        animationTick(level0, blockPos1, $$5, conduitBlockEntity3.destroyTarget, conduitBlockEntity3.tickCount);
        if (conduitBlockEntity3.isActive()) {
            conduitBlockEntity3.activeRotation++;
        }
    }

    public static void serverTick(Level level0, BlockPos blockPos1, BlockState blockState2, ConduitBlockEntity conduitBlockEntity3) {
        conduitBlockEntity3.tickCount++;
        long $$4 = level0.getGameTime();
        List<BlockPos> $$5 = conduitBlockEntity3.effectBlocks;
        if ($$4 % 40L == 0L) {
            boolean $$6 = updateShape(level0, blockPos1, $$5);
            if ($$6 != conduitBlockEntity3.isActive) {
                SoundEvent $$7 = $$6 ? SoundEvents.CONDUIT_ACTIVATE : SoundEvents.CONDUIT_DEACTIVATE;
                level0.playSound(null, blockPos1, $$7, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            conduitBlockEntity3.isActive = $$6;
            updateHunting(conduitBlockEntity3, $$5);
            if ($$6) {
                applyEffects(level0, blockPos1, $$5);
                updateDestroyTarget(level0, blockPos1, blockState2, $$5, conduitBlockEntity3);
            }
        }
        if (conduitBlockEntity3.isActive()) {
            if ($$4 % 80L == 0L) {
                level0.playSound(null, blockPos1, SoundEvents.CONDUIT_AMBIENT, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            if ($$4 > conduitBlockEntity3.nextAmbientSoundActivation) {
                conduitBlockEntity3.nextAmbientSoundActivation = $$4 + 60L + (long) level0.getRandom().nextInt(40);
                level0.playSound(null, blockPos1, SoundEvents.CONDUIT_AMBIENT_SHORT, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    private static void updateHunting(ConduitBlockEntity conduitBlockEntity0, List<BlockPos> listBlockPos1) {
        conduitBlockEntity0.setHunting(listBlockPos1.size() >= 42);
    }

    private static boolean updateShape(Level level0, BlockPos blockPos1, List<BlockPos> listBlockPos2) {
        listBlockPos2.clear();
        for (int $$3 = -1; $$3 <= 1; $$3++) {
            for (int $$4 = -1; $$4 <= 1; $$4++) {
                for (int $$5 = -1; $$5 <= 1; $$5++) {
                    BlockPos $$6 = blockPos1.offset($$3, $$4, $$5);
                    if (!level0.m_46801_($$6)) {
                        return false;
                    }
                }
            }
        }
        for (int $$7 = -2; $$7 <= 2; $$7++) {
            for (int $$8 = -2; $$8 <= 2; $$8++) {
                for (int $$9 = -2; $$9 <= 2; $$9++) {
                    int $$10 = Math.abs($$7);
                    int $$11 = Math.abs($$8);
                    int $$12 = Math.abs($$9);
                    if (($$10 > 1 || $$11 > 1 || $$12 > 1) && ($$7 == 0 && ($$11 == 2 || $$12 == 2) || $$8 == 0 && ($$10 == 2 || $$12 == 2) || $$9 == 0 && ($$10 == 2 || $$11 == 2))) {
                        BlockPos $$13 = blockPos1.offset($$7, $$8, $$9);
                        BlockState $$14 = level0.getBlockState($$13);
                        for (Block $$15 : VALID_BLOCKS) {
                            if ($$14.m_60713_($$15)) {
                                listBlockPos2.add($$13);
                            }
                        }
                    }
                }
            }
        }
        return listBlockPos2.size() >= 16;
    }

    private static void applyEffects(Level level0, BlockPos blockPos1, List<BlockPos> listBlockPos2) {
        int $$3 = listBlockPos2.size();
        int $$4 = $$3 / 7 * 16;
        int $$5 = blockPos1.m_123341_();
        int $$6 = blockPos1.m_123342_();
        int $$7 = blockPos1.m_123343_();
        AABB $$8 = new AABB((double) $$5, (double) $$6, (double) $$7, (double) ($$5 + 1), (double) ($$6 + 1), (double) ($$7 + 1)).inflate((double) $$4).expandTowards(0.0, (double) level0.m_141928_(), 0.0);
        List<Player> $$9 = level0.m_45976_(Player.class, $$8);
        if (!$$9.isEmpty()) {
            for (Player $$10 : $$9) {
                if (blockPos1.m_123314_($$10.m_20183_(), (double) $$4) && $$10.m_20070_()) {
                    $$10.m_7292_(new MobEffectInstance(MobEffects.CONDUIT_POWER, 260, 0, true, true));
                }
            }
        }
    }

    private static void updateDestroyTarget(Level level0, BlockPos blockPos1, BlockState blockState2, List<BlockPos> listBlockPos3, ConduitBlockEntity conduitBlockEntity4) {
        LivingEntity $$5 = conduitBlockEntity4.destroyTarget;
        int $$6 = listBlockPos3.size();
        if ($$6 < 42) {
            conduitBlockEntity4.destroyTarget = null;
        } else if (conduitBlockEntity4.destroyTarget == null && conduitBlockEntity4.destroyTargetUUID != null) {
            conduitBlockEntity4.destroyTarget = findDestroyTarget(level0, blockPos1, conduitBlockEntity4.destroyTargetUUID);
            conduitBlockEntity4.destroyTargetUUID = null;
        } else if (conduitBlockEntity4.destroyTarget == null) {
            List<LivingEntity> $$7 = level0.m_6443_(LivingEntity.class, getDestroyRangeAABB(blockPos1), p_289511_ -> p_289511_ instanceof Enemy && p_289511_.m_20070_());
            if (!$$7.isEmpty()) {
                conduitBlockEntity4.destroyTarget = (LivingEntity) $$7.get(level0.random.nextInt($$7.size()));
            }
        } else if (!conduitBlockEntity4.destroyTarget.isAlive() || !blockPos1.m_123314_(conduitBlockEntity4.destroyTarget.m_20183_(), 8.0)) {
            conduitBlockEntity4.destroyTarget = null;
        }
        if (conduitBlockEntity4.destroyTarget != null) {
            level0.playSound(null, conduitBlockEntity4.destroyTarget.m_20185_(), conduitBlockEntity4.destroyTarget.m_20186_(), conduitBlockEntity4.destroyTarget.m_20189_(), SoundEvents.CONDUIT_ATTACK_TARGET, SoundSource.BLOCKS, 1.0F, 1.0F);
            conduitBlockEntity4.destroyTarget.hurt(level0.damageSources().magic(), 4.0F);
        }
        if ($$5 != conduitBlockEntity4.destroyTarget) {
            level0.sendBlockUpdated(blockPos1, blockState2, blockState2, 2);
        }
    }

    private static void updateClientTarget(Level level0, BlockPos blockPos1, ConduitBlockEntity conduitBlockEntity2) {
        if (conduitBlockEntity2.destroyTargetUUID == null) {
            conduitBlockEntity2.destroyTarget = null;
        } else if (conduitBlockEntity2.destroyTarget == null || !conduitBlockEntity2.destroyTarget.m_20148_().equals(conduitBlockEntity2.destroyTargetUUID)) {
            conduitBlockEntity2.destroyTarget = findDestroyTarget(level0, blockPos1, conduitBlockEntity2.destroyTargetUUID);
            if (conduitBlockEntity2.destroyTarget == null) {
                conduitBlockEntity2.destroyTargetUUID = null;
            }
        }
    }

    private static AABB getDestroyRangeAABB(BlockPos blockPos0) {
        int $$1 = blockPos0.m_123341_();
        int $$2 = blockPos0.m_123342_();
        int $$3 = blockPos0.m_123343_();
        return new AABB((double) $$1, (double) $$2, (double) $$3, (double) ($$1 + 1), (double) ($$2 + 1), (double) ($$3 + 1)).inflate(8.0);
    }

    @Nullable
    private static LivingEntity findDestroyTarget(Level level0, BlockPos blockPos1, UUID uUID2) {
        List<LivingEntity> $$3 = level0.m_6443_(LivingEntity.class, getDestroyRangeAABB(blockPos1), p_289510_ -> p_289510_.m_20148_().equals(uUID2));
        return $$3.size() == 1 ? (LivingEntity) $$3.get(0) : null;
    }

    private static void animationTick(Level level0, BlockPos blockPos1, List<BlockPos> listBlockPos2, @Nullable Entity entity3, int int4) {
        RandomSource $$5 = level0.random;
        double $$6 = (double) (Mth.sin((float) (int4 + 35) * 0.1F) / 2.0F + 0.5F);
        $$6 = ($$6 * $$6 + $$6) * 0.3F;
        Vec3 $$7 = new Vec3((double) blockPos1.m_123341_() + 0.5, (double) blockPos1.m_123342_() + 1.5 + $$6, (double) blockPos1.m_123343_() + 0.5);
        for (BlockPos $$8 : listBlockPos2) {
            if ($$5.nextInt(50) == 0) {
                BlockPos $$9 = $$8.subtract(blockPos1);
                float $$10 = -0.5F + $$5.nextFloat() + (float) $$9.m_123341_();
                float $$11 = -2.0F + $$5.nextFloat() + (float) $$9.m_123342_();
                float $$12 = -0.5F + $$5.nextFloat() + (float) $$9.m_123343_();
                level0.addParticle(ParticleTypes.NAUTILUS, $$7.x, $$7.y, $$7.z, (double) $$10, (double) $$11, (double) $$12);
            }
        }
        if (entity3 != null) {
            Vec3 $$13 = new Vec3(entity3.getX(), entity3.getEyeY(), entity3.getZ());
            float $$14 = (-0.5F + $$5.nextFloat()) * (3.0F + entity3.getBbWidth());
            float $$15 = -1.0F + $$5.nextFloat() * entity3.getBbHeight();
            float $$16 = (-0.5F + $$5.nextFloat()) * (3.0F + entity3.getBbWidth());
            Vec3 $$17 = new Vec3((double) $$14, (double) $$15, (double) $$16);
            level0.addParticle(ParticleTypes.NAUTILUS, $$13.x, $$13.y, $$13.z, $$17.x, $$17.y, $$17.z);
        }
    }

    public boolean isActive() {
        return this.isActive;
    }

    public boolean isHunting() {
        return this.isHunting;
    }

    private void setHunting(boolean boolean0) {
        this.isHunting = boolean0;
    }

    public float getActiveRotation(float float0) {
        return (this.activeRotation + float0) * -0.0375F;
    }
}