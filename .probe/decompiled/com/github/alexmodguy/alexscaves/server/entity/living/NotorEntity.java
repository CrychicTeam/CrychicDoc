package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.entity.ai.NotorFlightGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.NotorHologramGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.NotorScanGoal;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class NotorEntity extends PathfinderMob {

    private float groundProgress;

    private float prevGroundProgress;

    private float beamProgress;

    private float prevBeamProgress;

    private float hologramProgress;

    private float prevHologramProgress;

    private float propellerRot;

    private float prevPropellerRot;

    public int stopScanningFor = 80 + this.f_19796_.nextInt(220);

    private static final EntityDataAccessor<Boolean> SHOWING_HOLOGRAM = SynchedEntityData.defineId(NotorEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Optional<BlockPos>> HOLOGRAM_POS = SynchedEntityData.defineId(NotorEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);

    private static final EntityDataAccessor<Integer> SCANNING_ID = SynchedEntityData.defineId(NotorEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Optional<UUID>> HOLOGRAM_ENTITY_UUID = SynchedEntityData.defineId(NotorEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Integer> HOLOGRAM_ENTITY_ID = SynchedEntityData.defineId(NotorEntity.class, EntityDataSerializers.INT);

    public static final Predicate<LivingEntity> SCAN_TARGET = mob -> mob.isAlive() && !mob.m_6095_().is(ACTagRegistry.NOTOR_IGNORES) && !mob.m_20145_();

    private int flyingSoundTimer;

    public NotorEntity(EntityType entityType, Level level) {
        super(entityType, level);
        this.f_21342_ = new NotorEntity.FlightMoveHelper(this);
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new FlyingPathNavigation(this, worldIn) {

            @Override
            public boolean isStableDestination(BlockPos blockPos) {
                return this.f_26495_.getBlockState(blockPos).m_60795_();
            }
        };
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new NotorHologramGoal(this));
        this.f_21345_.addGoal(2, new NotorScanGoal(this));
        this.f_21345_.addGoal(3, new NotorFlightGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.15).add(Attributes.MAX_HEALTH, 6.0);
    }

    @Override
    public float getWalkTargetValue(BlockPos blockPos, LevelReader levelReader) {
        return levelReader.m_8055_(blockPos).m_60795_() ? 10.0F : 0.0F;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.m_20088_().define(HOLOGRAM_POS, Optional.empty());
        this.f_19804_.define(SHOWING_HOLOGRAM, false);
        this.f_19804_.define(SCANNING_ID, -1);
        this.f_19804_.define(HOLOGRAM_ENTITY_UUID, Optional.empty());
        this.f_19804_.define(HOLOGRAM_ENTITY_ID, -1);
    }

    public static boolean checkNotorSpawnRules(EntityType<NotorEntity> notor, LevelAccessor level, MobSpawnType spawnType, BlockPos blockPos, RandomSource randomSource) {
        if (blockPos.m_123342_() >= level.m_5736_()) {
            return false;
        } else {
            int i = level.m_46803_(blockPos);
            int j = 4;
            return i > randomSource.nextInt(j) ? false : m_217057_(notor, level, spawnType, blockPos, randomSource);
        }
    }

    @Override
    public void tick() {
        super.m_8119_();
        Entity hologram = this.getHologramEntity();
        this.prevGroundProgress = this.groundProgress;
        this.prevBeamProgress = this.beamProgress;
        this.prevHologramProgress = this.hologramProgress;
        this.prevPropellerRot = this.propellerRot;
        if (this.m_20096_() && this.groundProgress < 5.0F) {
            this.groundProgress++;
        }
        if (!this.m_20096_() && this.groundProgress > 0.0F) {
            this.groundProgress--;
        }
        Entity scanningMob = this.getScanningMob();
        boolean hasHologram = hologram != null && this.showingHologram();
        boolean hasBeam = scanningMob != null || hasHologram;
        if (hasBeam && this.beamProgress < 5.0F) {
            if (this.beamProgress == 0.0F && (hasHologram || scanningMob != null && this.m_142582_(scanningMob))) {
                this.m_216990_(ACSoundRegistry.HOLOGRAM_STOP.get());
            }
            this.beamProgress++;
        }
        if (hasBeam && this.m_6084_()) {
            AlexsCaves.PROXY.playWorldSound(this, (byte) 2);
        }
        if (!hasBeam && this.beamProgress > 0.0F) {
            if (this.beamProgress == 5.0F) {
                this.m_216990_(ACSoundRegistry.HOLOGRAM_STOP.get());
            }
            if (this.hologramProgress > 0.0F) {
                this.hologramProgress--;
            } else {
                this.beamProgress--;
            }
        }
        if (hasHologram && this.beamProgress >= 5.0F && this.hologramProgress < 5.0F) {
            this.hologramProgress++;
        }
        if (!hasHologram && this.hologramProgress > 0.0F) {
            this.hologramProgress--;
        }
        double speed = this.m_20184_().horizontalDistance();
        if (!this.m_20096_()) {
            this.m_20256_(this.m_20184_().multiply(0.9F, 0.9F, 0.9F));
            this.propellerRot = (float) ((double) this.propellerRot + Math.max(speed * 10.0, 3.0) * 20.0);
            if (this.flyingSoundTimer++ >= 10) {
                if (!this.m_9236_().isClientSide && !this.m_20067_()) {
                    this.m_5496_(ACSoundRegistry.NOTOR_FLYING.get(), 0.4F, 1.0F);
                }
                this.flyingSoundTimer = 0;
            }
        } else if (Mth.wrapDegrees(this.propellerRot) != 0.0F) {
            this.flyingSoundTimer = 0;
            this.propellerRot = Mth.approachDegrees(this.propellerRot, 0.0F, 15.0F);
        }
        if (!this.m_9236_().isClientSide) {
            if (hologram != null) {
                this.f_19804_.set(HOLOGRAM_ENTITY_ID, hologram.getId());
            } else {
                this.f_19804_.set(HOLOGRAM_ENTITY_ID, -1);
            }
            if (this.stopScanningFor > 0) {
                this.stopScanningFor--;
            }
        }
    }

    @Override
    public void remove(Entity.RemovalReason removalReason) {
        AlexsCaves.PROXY.clearSoundCacheFor(this);
        super.m_142687_(removalReason);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float f1 = (float) Mth.length(this.m_20185_() - this.f_19854_, this.m_20186_() - this.f_19855_, this.m_20189_() - this.f_19856_);
        float f2 = Math.min(f1 * 6.0F, 1.0F);
        this.f_267362_.update(f2, 0.4F);
    }

    public float getGroundProgress(float partialTick) {
        return (this.prevGroundProgress + (this.groundProgress - this.prevGroundProgress) * partialTick) * 0.2F;
    }

    public float getBeamProgress(float partialTick) {
        return (this.prevBeamProgress + (this.beamProgress - this.prevBeamProgress) * partialTick) * 0.2F;
    }

    public float getPropellerAngle(float partialTick) {
        return this.prevPropellerRot + (this.propellerRot - this.prevPropellerRot) * partialTick;
    }

    public void setScanningId(int i) {
        this.f_19804_.set(SCANNING_ID, i);
    }

    public Entity getScanningMob() {
        int id = this.getScanningId();
        return id == -1 ? null : this.m_9236_().getEntity(id);
    }

    @Nullable
    public UUID getHologramUUID() {
        return (UUID) this.f_19804_.get(HOLOGRAM_ENTITY_UUID).orElse(null);
    }

    public void setHologramUUID(@Nullable UUID hologram) {
        this.f_19804_.set(HOLOGRAM_ENTITY_UUID, Optional.ofNullable(hologram));
    }

    public Entity getHologramEntity() {
        if (!this.m_9236_().isClientSide) {
            UUID id = this.getHologramUUID();
            return id == null ? null : ((ServerLevel) this.m_9236_()).getEntity(id);
        } else {
            int id = this.f_19804_.get(HOLOGRAM_ENTITY_ID);
            return id == -1 ? null : this.m_9236_().getEntity(id);
        }
    }

    public int getScanningId() {
        return this.f_19804_.get(SCANNING_ID);
    }

    public void setHologramPos(@Nullable BlockPos pos) {
        this.m_20088_().set(HOLOGRAM_POS, Optional.ofNullable(pos));
    }

    public boolean showingHologram() {
        return this.f_19804_.get(SHOWING_HOLOGRAM);
    }

    public void setShowingHologram(boolean showingHologram) {
        this.f_19804_.set(SHOWING_HOLOGRAM, showingHologram);
    }

    @Nullable
    public BlockPos getHologramPos() {
        return (BlockPos) this.m_20088_().get(HOLOGRAM_POS).orElse((BlockPos) null);
    }

    public Vec3 getBeamEndPosition(float partialTicks) {
        Entity scanning = this.getScanningMob();
        if (scanning != null) {
            float f = (float) Math.abs(Math.sin((double) (((float) this.f_19797_ + partialTicks) * 0.1F)));
            return scanning.getPosition(partialTicks).add(0.0, (double) (scanning.getBbHeight() * f), 0.0);
        } else {
            BlockPos pos = (BlockPos) this.m_20088_().get(HOLOGRAM_POS).orElse((BlockPos) null);
            return pos == null ? this.m_20318_(partialTicks).add(0.0, -3.0, 0.0) : Vec3.atCenterOf(pos);
        }
    }

    public float getHologramProgress(float partialTicks) {
        return (this.prevHologramProgress + (this.hologramProgress - this.prevHologramProgress) * partialTicks) * 0.2F;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        compound.putBoolean("ShowingHologram", this.showingHologram());
        if (this.getHologramUUID() != null) {
            compound.putUUID("HologramUUID", this.getHologramUUID());
        }
        compound.putInt("StopScanningTime", this.stopScanningFor);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        this.setShowingHologram(compound.getBoolean("ShowingHologram"));
        if (compound.hasUUID("HologramUUID")) {
            this.setHologramUUID(compound.getUUID("HologramUUID"));
        }
        this.stopScanningFor = compound.getInt("StopScanningTime");
    }

    @Override
    public boolean removeWhenFarAway(double dist) {
        return !this.m_8077_();
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effectInstance) {
        return super.m_7301_(effectInstance) && effectInstance.getEffect() != ACEffectRegistry.MAGNETIZING.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ACSoundRegistry.NOTOR_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ACSoundRegistry.NOTOR_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSoundRegistry.NOTOR_DEATH.get();
    }

    class FlightMoveHelper extends MoveControl {

        private final NotorEntity parentEntity;

        public FlightMoveHelper(NotorEntity bird) {
            super(bird);
            this.parentEntity = bird;
        }

        @Override
        public void tick() {
            boolean gravity = true;
            if (this.parentEntity.getScanningId() != -1 || this.parentEntity.getHologramEntity() != null) {
                gravity = false;
                float angle = (float) (Math.PI / 180.0) * (this.parentEntity.f_20883_ + 90.0F);
                float radius = (float) Math.sin((double) ((float) this.parentEntity.f_19797_ * 0.2F)) * 2.0F;
                double extraX = (double) (radius * Mth.sin((float) (Math.PI + (double) angle)));
                double extraZ = (double) (radius * Mth.cos(angle));
                Vec3 strafPlus = new Vec3(extraX, 0.0, extraZ);
                this.parentEntity.m_20256_(this.parentEntity.m_20184_().add(strafPlus.scale(0.01)));
            }
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                Vec3 vector3d = new Vec3(this.f_24975_ - this.parentEntity.m_20185_(), this.f_24976_ - this.parentEntity.m_20186_(), this.f_24977_ - this.parentEntity.m_20189_());
                double d5 = vector3d.length();
                if (d5 < this.parentEntity.m_20191_().getSize()) {
                    this.f_24981_ = MoveControl.Operation.WAIT;
                    this.parentEntity.m_20256_(this.parentEntity.m_20184_().scale(0.5));
                } else {
                    gravity = false;
                    this.parentEntity.m_20256_(this.parentEntity.m_20184_().add(vector3d.scale(this.f_24978_ * 0.05 / d5)));
                    Vec3 vector3d1 = this.parentEntity.m_20184_();
                    float f = -((float) Mth.atan2(vector3d1.x, vector3d1.z)) * 180.0F / (float) Math.PI;
                    this.parentEntity.m_146922_(Mth.approachDegrees(this.parentEntity.m_146908_(), f, 20.0F));
                    this.parentEntity.f_20883_ = this.parentEntity.m_146908_();
                }
            }
            this.parentEntity.m_20242_(!gravity);
        }
    }
}