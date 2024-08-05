package net.minecraft.world.entity;

import com.google.common.collect.Sets;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class LightningBolt extends Entity {

    private static final int START_LIFE = 2;

    private static final double DAMAGE_RADIUS = 3.0;

    private static final double DETECTION_RADIUS = 15.0;

    private int life;

    public long seed;

    private int flashes;

    private boolean visualOnly;

    @Nullable
    private ServerPlayer cause;

    private final Set<Entity> hitEntities = Sets.newHashSet();

    private int blocksSetOnFire;

    public LightningBolt(EntityType<? extends LightningBolt> entityTypeExtendsLightningBolt0, Level level1) {
        super(entityTypeExtendsLightningBolt0, level1);
        this.f_19811_ = true;
        this.life = 2;
        this.seed = this.f_19796_.nextLong();
        this.flashes = this.f_19796_.nextInt(3) + 1;
    }

    public void setVisualOnly(boolean boolean0) {
        this.visualOnly = boolean0;
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.WEATHER;
    }

    @Nullable
    public ServerPlayer getCause() {
        return this.cause;
    }

    public void setCause(@Nullable ServerPlayer serverPlayer0) {
        this.cause = serverPlayer0;
    }

    private void powerLightningRod() {
        BlockPos $$0 = this.getStrikePosition();
        BlockState $$1 = this.m_9236_().getBlockState($$0);
        if ($$1.m_60713_(Blocks.LIGHTNING_ROD)) {
            ((LightningRodBlock) $$1.m_60734_()).onLightningStrike($$1, this.m_9236_(), $$0);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.life == 2) {
            if (this.m_9236_().isClientSide()) {
                this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 10000.0F, 0.8F + this.f_19796_.nextFloat() * 0.2F, false);
                this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.WEATHER, 2.0F, 0.5F + this.f_19796_.nextFloat() * 0.2F, false);
            } else {
                Difficulty $$0 = this.m_9236_().m_46791_();
                if ($$0 == Difficulty.NORMAL || $$0 == Difficulty.HARD) {
                    this.spawnFire(4);
                }
                this.powerLightningRod();
                clearCopperOnLightningStrike(this.m_9236_(), this.getStrikePosition());
                this.m_146850_(GameEvent.LIGHTNING_STRIKE);
            }
        }
        this.life--;
        if (this.life < 0) {
            if (this.flashes == 0) {
                if (this.m_9236_() instanceof ServerLevel) {
                    List<Entity> $$1 = this.m_9236_().getEntities(this, new AABB(this.m_20185_() - 15.0, this.m_20186_() - 15.0, this.m_20189_() - 15.0, this.m_20185_() + 15.0, this.m_20186_() + 6.0 + 15.0, this.m_20189_() + 15.0), p_147140_ -> p_147140_.isAlive() && !this.hitEntities.contains(p_147140_));
                    for (ServerPlayer $$2 : ((ServerLevel) this.m_9236_()).getPlayers(p_147157_ -> p_147157_.m_20270_(this) < 256.0F)) {
                        CriteriaTriggers.LIGHTNING_STRIKE.trigger($$2, this, $$1);
                    }
                }
                this.m_146870_();
            } else if (this.life < -this.f_19796_.nextInt(10)) {
                this.flashes--;
                this.life = 1;
                this.seed = this.f_19796_.nextLong();
                this.spawnFire(0);
            }
        }
        if (this.life >= 0) {
            if (!(this.m_9236_() instanceof ServerLevel)) {
                this.m_9236_().setSkyFlashTime(2);
            } else if (!this.visualOnly) {
                List<Entity> $$3 = this.m_9236_().getEntities(this, new AABB(this.m_20185_() - 3.0, this.m_20186_() - 3.0, this.m_20189_() - 3.0, this.m_20185_() + 3.0, this.m_20186_() + 6.0 + 3.0, this.m_20189_() + 3.0), Entity::m_6084_);
                for (Entity $$4 : $$3) {
                    $$4.thunderHit((ServerLevel) this.m_9236_(), this);
                }
                this.hitEntities.addAll($$3);
                if (this.cause != null) {
                    CriteriaTriggers.CHANNELED_LIGHTNING.trigger(this.cause, $$3);
                }
            }
        }
    }

    private BlockPos getStrikePosition() {
        Vec3 $$0 = this.m_20182_();
        return BlockPos.containing($$0.x, $$0.y - 1.0E-6, $$0.z);
    }

    private void spawnFire(int int0) {
        if (!this.visualOnly && !this.m_9236_().isClientSide && this.m_9236_().getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
            BlockPos $$1 = this.m_20183_();
            BlockState $$2 = BaseFireBlock.getState(this.m_9236_(), $$1);
            if (this.m_9236_().getBlockState($$1).m_60795_() && $$2.m_60710_(this.m_9236_(), $$1)) {
                this.m_9236_().setBlockAndUpdate($$1, $$2);
                this.blocksSetOnFire++;
            }
            for (int $$3 = 0; $$3 < int0; $$3++) {
                BlockPos $$4 = $$1.offset(this.f_19796_.nextInt(3) - 1, this.f_19796_.nextInt(3) - 1, this.f_19796_.nextInt(3) - 1);
                $$2 = BaseFireBlock.getState(this.m_9236_(), $$4);
                if (this.m_9236_().getBlockState($$4).m_60795_() && $$2.m_60710_(this.m_9236_(), $$4)) {
                    this.m_9236_().setBlockAndUpdate($$4, $$2);
                    this.blocksSetOnFire++;
                }
            }
        }
    }

    private static void clearCopperOnLightningStrike(Level level0, BlockPos blockPos1) {
        BlockState $$2 = level0.getBlockState(blockPos1);
        BlockPos $$3;
        BlockState $$4;
        if ($$2.m_60713_(Blocks.LIGHTNING_ROD)) {
            $$3 = blockPos1.relative(((Direction) $$2.m_61143_(LightningRodBlock.f_52588_)).getOpposite());
            $$4 = level0.getBlockState($$3);
        } else {
            $$3 = blockPos1;
            $$4 = $$2;
        }
        if ($$4.m_60734_() instanceof WeatheringCopper) {
            level0.setBlockAndUpdate($$3, WeatheringCopper.getFirst(level0.getBlockState($$3)));
            BlockPos.MutableBlockPos $$7 = blockPos1.mutable();
            int $$8 = level0.random.nextInt(3) + 3;
            for (int $$9 = 0; $$9 < $$8; $$9++) {
                int $$10 = level0.random.nextInt(8) + 1;
                randomWalkCleaningCopper(level0, $$3, $$7, $$10);
            }
        }
    }

    private static void randomWalkCleaningCopper(Level level0, BlockPos blockPos1, BlockPos.MutableBlockPos blockPosMutableBlockPos2, int int3) {
        blockPosMutableBlockPos2.set(blockPos1);
        for (int $$4 = 0; $$4 < int3; $$4++) {
            Optional<BlockPos> $$5 = randomStepCleaningCopper(level0, blockPosMutableBlockPos2);
            if (!$$5.isPresent()) {
                break;
            }
            blockPosMutableBlockPos2.set((Vec3i) $$5.get());
        }
    }

    private static Optional<BlockPos> randomStepCleaningCopper(Level level0, BlockPos blockPos1) {
        for (BlockPos $$2 : BlockPos.randomInCube(level0.random, 10, blockPos1, 1)) {
            BlockState $$3 = level0.getBlockState($$2);
            if ($$3.m_60734_() instanceof WeatheringCopper) {
                WeatheringCopper.getPrevious($$3).ifPresent(p_147144_ -> level0.setBlockAndUpdate($$2, p_147144_));
                level0.m_46796_(3002, $$2, -1);
                return Optional.of($$2);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double double0) {
        double $$1 = 64.0 * m_20150_();
        return double0 < $$1 * $$1;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag0) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag0) {
    }

    public int getBlocksSetOnFire() {
        return this.blocksSetOnFire;
    }

    public Stream<Entity> getHitEntities() {
        return this.hitEntities.stream().filter(Entity::m_6084_);
    }
}