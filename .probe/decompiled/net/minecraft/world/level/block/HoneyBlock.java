package net.minecraft.world.level.block;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HoneyBlock extends HalfTransparentBlock {

    private static final double SLIDE_STARTS_WHEN_VERTICAL_SPEED_IS_AT_LEAST = 0.13;

    private static final double MIN_FALL_SPEED_TO_BE_CONSIDERED_SLIDING = 0.08;

    private static final double THROTTLE_SLIDE_SPEED_TO = 0.05;

    private static final int SLIDE_ADVANCEMENT_CHECK_INTERVAL = 20;

    protected static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 15.0, 15.0);

    public HoneyBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    private static boolean doesEntityDoHoneyBlockSlideEffects(Entity entity0) {
        return entity0 instanceof LivingEntity || entity0 instanceof AbstractMinecart || entity0 instanceof PrimedTnt || entity0 instanceof Boat;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE;
    }

    @Override
    public void fallOn(Level level0, BlockState blockState1, BlockPos blockPos2, Entity entity3, float float4) {
        entity3.playSound(SoundEvents.HONEY_BLOCK_SLIDE, 1.0F, 1.0F);
        if (!level0.isClientSide) {
            level0.broadcastEntityEvent(entity3, (byte) 54);
        }
        if (entity3.causeFallDamage(float4, 0.2F, level0.damageSources().fall())) {
            entity3.playSound(this.f_60446_.getFallSound(), this.f_60446_.getVolume() * 0.5F, this.f_60446_.getPitch() * 0.75F);
        }
    }

    @Override
    public void entityInside(BlockState blockState0, Level level1, BlockPos blockPos2, Entity entity3) {
        if (this.isSlidingDown(blockPos2, entity3)) {
            this.maybeDoSlideAchievement(entity3, blockPos2);
            this.doSlideMovement(entity3);
            this.maybeDoSlideEffects(level1, entity3);
        }
        super.m_7892_(blockState0, level1, blockPos2, entity3);
    }

    private boolean isSlidingDown(BlockPos blockPos0, Entity entity1) {
        if (entity1.onGround()) {
            return false;
        } else if (entity1.getY() > (double) blockPos0.m_123342_() + 0.9375 - 1.0E-7) {
            return false;
        } else if (entity1.getDeltaMovement().y >= -0.08) {
            return false;
        } else {
            double $$2 = Math.abs((double) blockPos0.m_123341_() + 0.5 - entity1.getX());
            double $$3 = Math.abs((double) blockPos0.m_123343_() + 0.5 - entity1.getZ());
            double $$4 = 0.4375 + (double) (entity1.getBbWidth() / 2.0F);
            return $$2 + 1.0E-7 > $$4 || $$3 + 1.0E-7 > $$4;
        }
    }

    private void maybeDoSlideAchievement(Entity entity0, BlockPos blockPos1) {
        if (entity0 instanceof ServerPlayer && entity0.level().getGameTime() % 20L == 0L) {
            CriteriaTriggers.HONEY_BLOCK_SLIDE.trigger((ServerPlayer) entity0, entity0.level().getBlockState(blockPos1));
        }
    }

    private void doSlideMovement(Entity entity0) {
        Vec3 $$1 = entity0.getDeltaMovement();
        if ($$1.y < -0.13) {
            double $$2 = -0.05 / $$1.y;
            entity0.setDeltaMovement(new Vec3($$1.x * $$2, -0.05, $$1.z * $$2));
        } else {
            entity0.setDeltaMovement(new Vec3($$1.x, -0.05, $$1.z));
        }
        entity0.resetFallDistance();
    }

    private void maybeDoSlideEffects(Level level0, Entity entity1) {
        if (doesEntityDoHoneyBlockSlideEffects(entity1)) {
            if (level0.random.nextInt(5) == 0) {
                entity1.playSound(SoundEvents.HONEY_BLOCK_SLIDE, 1.0F, 1.0F);
            }
            if (!level0.isClientSide && level0.random.nextInt(5) == 0) {
                level0.broadcastEntityEvent(entity1, (byte) 53);
            }
        }
    }

    public static void showSlideParticles(Entity entity0) {
        showParticles(entity0, 5);
    }

    public static void showJumpParticles(Entity entity0) {
        showParticles(entity0, 10);
    }

    private static void showParticles(Entity entity0, int int1) {
        if (entity0.level().isClientSide) {
            BlockState $$2 = Blocks.HONEY_BLOCK.defaultBlockState();
            for (int $$3 = 0; $$3 < int1; $$3++) {
                entity0.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, $$2), entity0.getX(), entity0.getY(), entity0.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }
}