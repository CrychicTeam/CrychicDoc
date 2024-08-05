package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.entity.ai.MobTarget3DGoal;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class BoundroidEntity extends Monster {

    private static final EntityDataAccessor<Optional<UUID>> WINCH_UUID = SynchedEntityData.defineId(BoundroidEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Integer> WINCH_ID = SynchedEntityData.defineId(BoundroidEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> SLAMMING = SynchedEntityData.defineId(BoundroidEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SCARED = SynchedEntityData.defineId(BoundroidEntity.class, EntityDataSerializers.BOOLEAN);

    private float groundProgress;

    private float prevGroundProgress;

    public boolean draggedClimable = false;

    public boolean stopGravity = false;

    public int stopSlammingFor = 0;

    private int stayOnGroundFor = 0;

    private static final AttributeModifier REMOVED_GRAVITY_MODIFIER = new AttributeModifier(UUID.fromString("B5B6CF2A-2F7C-31EF-9022-7C3E7D5E6BBA"), "remove gravity reduction", -0.08, AttributeModifier.Operation.ADDITION);

    public BoundroidEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.ATTACK_DAMAGE, 5.0).add(Attributes.ARMOR, 20.0).add(Attributes.MOVEMENT_SPEED, 0.2).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.MAX_HEALTH, 20.0);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new LookAtPlayerGoal(this, Player.class, 15.0F));
        this.f_21345_.addGoal(1, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new MobTarget3DGoal(this, Player.class, true));
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(WINCH_UUID, Optional.empty());
        this.f_19804_.define(WINCH_ID, -1);
        this.f_19804_.define(SCARED, false);
        this.f_19804_.define(SLAMMING, false);
    }

    @Nullable
    public UUID getWinchUUID() {
        return (UUID) this.f_19804_.get(WINCH_UUID).orElse(null);
    }

    public void setWinchUUID(@Nullable UUID uniqueId) {
        this.f_19804_.set(WINCH_UUID, Optional.ofNullable(uniqueId));
    }

    public Entity getWinch() {
        if (!this.m_9236_().isClientSide) {
            UUID id = this.getWinchUUID();
            return id == null ? null : ((ServerLevel) this.m_9236_()).getEntity(id);
        } else {
            int id = this.f_19804_.get(WINCH_ID);
            return id == -1 ? null : this.m_9236_().getEntity(id);
        }
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevGroundProgress = this.groundProgress;
        this.f_20883_ = this.m_146908_();
        if (this.m_20096_() && this.groundProgress < 5.0F) {
            this.groundProgress++;
        }
        if (!this.m_20096_() && this.groundProgress > 0.0F) {
            this.groundProgress--;
        }
        if (!this.m_9236_().isClientSide) {
            Entity winch = this.getWinch();
            if (winch == null) {
                LivingEntity created = this.createWinch();
                this.m_9236_().m_7967_(created);
                this.setWinchUUID(created.m_20148_());
                this.f_19804_.set(WINCH_ID, created.m_19879_());
            } else if (winch instanceof BoundroidWinchEntity winchEntity) {
                winchEntity.linkWithHead(this);
            }
            LivingEntity target = this.m_5448_();
            if (target != null && target.isAlive()) {
                Vec3 distanceVec = target.m_20182_().subtract(this.m_20182_());
                if (distanceVec.horizontalDistance() < 1.2 && this.stopSlammingFor == 0) {
                    this.setSlamming(true);
                }
            }
            if (this.isSlamming()) {
                this.m_20256_(new Vec3(this.m_20184_().x, -1.0, this.m_20184_().z));
                if (this.m_20096_()) {
                    this.setSlamming(false);
                    this.m_216990_(ACSoundRegistry.BOUNDROID_SLAM.get());
                    this.stayOnGroundFor = 10;
                    this.stopSlammingFor = 30 + this.f_19796_.nextInt(20);
                    this.m_9236_().broadcastEntityEvent(this, (byte) 45);
                    AABB bashBox = new AABB(-1.5, -2.0, -1.5, 1.5, 1.0, 1.5);
                    bashBox = bashBox.move(this.m_20182_());
                    for (Entity entity : this.m_9236_().m_45976_(LivingEntity.class, bashBox)) {
                        if (!this.m_7307_(entity) && !(entity instanceof BoundroidEntity) && !(entity instanceof BoundroidWinchEntity)) {
                            entity.hurt(this.m_269291_().mobAttack(this), 5.0F);
                        }
                    }
                }
            }
        } else if (this.m_6084_()) {
            AlexsCaves.PROXY.playWorldSound(this, (byte) 12);
        }
        if (this.stopSlammingFor > 0) {
            this.stopSlammingFor--;
        }
        if (this.stayOnGroundFor > 0) {
            this.stayOnGroundFor--;
        }
    }

    @Override
    public void handleEntityEvent(byte b) {
        if (b == 45) {
            this.spawnGroundEffects();
        } else {
            super.m_7822_(b);
        }
    }

    @Override
    public void remove(Entity.RemovalReason removalReason) {
        AlexsCaves.PROXY.clearSoundCacheFor(this);
        super.m_142687_(removalReason);
    }

    public void spawnGroundEffects() {
        float radius = 1.35F;
        for (int i = 0; i < 4; i++) {
            for (int i1 = 0; i1 < 10 + this.f_19796_.nextInt(10); i1++) {
                double motionX = this.m_217043_().nextGaussian() * 0.07;
                double motionY = this.m_217043_().nextGaussian() * 0.07;
                double motionZ = this.m_217043_().nextGaussian() * 0.07;
                float angle = (float) (Math.PI / 180.0) * this.f_20883_ + (float) i1;
                double extraX = (double) (radius * Mth.sin((float) (Math.PI + (double) angle)));
                double extraY = 0.8F;
                double extraZ = (double) (radius * Mth.cos(angle));
                Vec3 center = this.m_20182_().add(new Vec3(0.0, 3.0, 0.0).yRot((float) Math.toRadians((double) (-this.f_20883_))));
                BlockPos ground = BlockPos.containing(ACMath.getGroundBelowPosition(this.m_9236_(), new Vec3((double) Mth.floor(center.x + extraX), (double) (Mth.floor(center.y + extraY) - 1), (double) Mth.floor(center.z + extraZ))));
                BlockState state = this.m_9236_().getBlockState(ground);
                if (state.m_280296_() && this.m_9236_().isClientSide) {
                    this.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), true, center.x + extraX, (double) ground.m_123342_() + extraY, center.z + extraZ, motionX, motionY, motionZ);
                }
            }
        }
    }

    @Override
    public boolean onClimbable() {
        return super.m_6147_() || this.f_19862_ && this.draggedClimable;
    }

    public float getGroundProgress(float partialTicks) {
        return (this.prevGroundProgress + (this.groundProgress - this.prevGroundProgress) * partialTicks) * 0.2F;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return super.m_6673_(damageSource) || damageSource.is(DamageTypes.IN_WALL);
    }

    public boolean isScared() {
        return this.f_19804_.get(SCARED);
    }

    public void setScared(boolean scared) {
        this.f_19804_.set(SCARED, scared);
    }

    public boolean isSlamming() {
        return this.f_19804_.get(SLAMMING);
    }

    public boolean stopPullingUp() {
        return this.isSlamming() || this.stayOnGroundFor > 0;
    }

    public void setSlamming(boolean slamming) {
        this.f_19804_.set(SLAMMING, slamming);
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effectInstance) {
        return super.m_7301_(effectInstance) && effectInstance.getEffect() != ACEffectRegistry.MAGNETIZING.get();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        if (compound.hasUUID("WinchUUID")) {
            this.setWinchUUID(compound.getUUID("WinchUUID"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        if (this.getWinchUUID() != null) {
            compound.putUUID("WinchUUID", this.getWinchUUID());
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ACSoundRegistry.BOUNDROID_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ACSoundRegistry.BOUNDROID_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSoundRegistry.BOUNDROID_DEATH.get();
    }

    private LivingEntity createWinch() {
        return new BoundroidWinchEntity(this);
    }
}