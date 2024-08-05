package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.EntityUtil;
import com.github.alexthe666.iceandfire.entity.util.IAnimalFear;
import com.github.alexthe666.iceandfire.entity.util.IBlacklistedFromStatues;
import com.github.alexthe666.iceandfire.entity.util.IHasCustomizableAttributes;
import com.github.alexthe666.iceandfire.entity.util.IMultipartEntity;
import com.github.alexthe666.iceandfire.entity.util.IVillagerFear;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class EntityHydra extends Monster implements IAnimatedEntity, IMultipartEntity, IVillagerFear, IAnimalFear, IHasCustomizableAttributes {

    public static final int HEADS = 9;

    public static final double HEAD_HEALTH_THRESHOLD = 20.0;

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityHydra.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> HEAD_COUNT = SynchedEntityData.defineId(EntityHydra.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> SEVERED_HEAD = SynchedEntityData.defineId(EntityHydra.class, EntityDataSerializers.INT);

    private static final float[][] ROTATE = new float[][] { { 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F }, { 10.0F, -10.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F }, { 10.0F, 0.0F, -10.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F }, { 25.0F, 10.0F, -10.0F, -25.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F }, { 30.0F, 15.0F, 0.0F, -15.0F, -30.0F, 0.0F, 0.0F, 0.0F, 0.0F }, { 40.0F, 25.0F, 5.0F, -5.0F, -25.0F, -40.0F, 0.0F, 0.0F, 0.0F }, { 40.0F, 30.0F, 15.0F, 0.0F, -15.0F, -30.0F, -40.0F, 0.0F, 0.0F }, { 45.0F, 30.0F, 20.0F, 5.0F, -5.0F, -20.0F, -30.0F, -45.0F, 0.0F }, { 50.0F, 37.0F, 25.0F, 15.0F, 0.0F, -15.0F, -25.0F, -37.0F, -50.0F } };

    public boolean[] isStriking = new boolean[9];

    public float[] strikingProgress = new float[9];

    public float[] prevStrikeProgress = new float[9];

    public boolean[] isBreathing = new boolean[9];

    public float[] speakingProgress = new float[9];

    public float[] prevSpeakingProgress = new float[9];

    public float[] breathProgress = new float[9];

    public float[] prevBreathProgress = new float[9];

    public int[] breathTicks = new int[9];

    public float[] headDamageTracker = new float[9];

    private int animationTick;

    private Animation currentAnimation;

    private EntityHydraHead[] headBoxes = new EntityHydraHead[81];

    private int strikeCooldown = 0;

    private int breathCooldown = 0;

    private int lastHitHead = 0;

    private int prevHeadCount = -1;

    private int regrowHeadCooldown = 0;

    private boolean onlyRegrowOneHeadNotTwo = false;

    private float headDamageThreshold = 20.0F;

    public EntityHydra(EntityType<EntityHydra> type, Level worldIn) {
        super(type, worldIn);
        this.resetParts();
        this.headDamageThreshold = Math.max(5.0F, (float) IafConfig.hydraMaxHealth * 0.08F);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, IafConfig.hydraMaxHealth).add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.ARMOR, 1.0);
    }

    @Override
    public void setConfigurableAttributes() {
        this.m_21051_(Attributes.MAX_HEALTH).setBaseValue(IafConfig.hydraMaxHealth);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new FloatGoal(this));
        this.f_21345_.addGoal(2, new MeleeAttackGoal(this, 1.0, true));
        this.f_21345_.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.f_21345_.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21345_.addGoal(7, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, LivingEntity.class, 10, true, false, new Predicate<Entity>() {

            public boolean apply(@Nullable Entity entity) {
                return entity instanceof LivingEntity && DragonUtils.isAlive((LivingEntity) entity) && !(entity instanceof EntityMutlipartPart) && !(entity instanceof Enemy) || entity instanceof IBlacklistedFromStatues && ((IBlacklistedFromStatues) entity).canBeTurnedToStone();
            }
        }));
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entityIn) {
        return false;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        LivingEntity attackTarget = this.m_5448_();
        if (attackTarget != null && this.m_142582_(attackTarget)) {
            int index = this.f_19796_.nextInt(this.getHeadCount());
            if (!this.isBreathing[index] && !this.isStriking[index]) {
                if (this.m_20270_(attackTarget) < 6.0F) {
                    if (this.strikeCooldown == 0 && this.strikingProgress[index] == 0.0F) {
                        this.isBreathing[index] = false;
                        this.isStriking[index] = true;
                        this.m_9236_().broadcastEntityEvent(this, (byte) (40 + index));
                        this.strikeCooldown = 3;
                    }
                } else if (this.f_19796_.nextBoolean() && this.breathCooldown == 0) {
                    this.isBreathing[index] = true;
                    this.isStriking[index] = false;
                    this.m_9236_().broadcastEntityEvent(this, (byte) (50 + index));
                    this.breathCooldown = 15;
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            boolean striking = this.isStriking[i];
            boolean breathing = this.isBreathing[i];
            this.prevStrikeProgress[i] = this.strikingProgress[i];
            if (striking && this.strikingProgress[i] > 9.0F) {
                this.isStriking[i] = false;
                if (attackTarget != null && this.m_20270_(attackTarget) < 6.0F) {
                    attackTarget.hurt(this.m_9236_().damageSources().mobAttack(this), (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue());
                    attackTarget.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 3, false, false));
                    attackTarget.knockback(0.25, this.m_20185_() - attackTarget.m_20185_(), this.m_20189_() - attackTarget.m_20189_());
                }
            }
            if (breathing) {
                if (this.f_19797_ % 7 == 0 && attackTarget != null && i < this.getHeadCount()) {
                    Vec3 Vector3d = this.m_20252_(1.0F);
                    if (this.f_19796_.nextFloat() < 0.2F) {
                        this.m_5496_(IafSoundRegistry.HYDRA_SPIT, this.m_6121_(), this.m_6100_());
                    }
                    double headPosX = this.headBoxes[i].m_20185_() + Vector3d.x;
                    double headPosY = this.headBoxes[i].m_20186_() + 1.3F;
                    double headPosZ = this.headBoxes[i].m_20189_() + Vector3d.z;
                    double d2 = attackTarget.m_20185_() - headPosX + this.f_19796_.nextGaussian() * 0.4;
                    double d3 = attackTarget.m_20186_() + (double) attackTarget.m_20192_() - headPosY + this.f_19796_.nextGaussian() * 0.4;
                    double d4 = attackTarget.m_20189_() - headPosZ + this.f_19796_.nextGaussian() * 0.4;
                    EntityHydraBreath entitylargefireball = new EntityHydraBreath(IafEntityRegistry.HYDRA_BREATH.get(), this.m_9236_(), this, d2, d3, d4);
                    entitylargefireball.m_6034_(headPosX, headPosY, headPosZ);
                    if (!this.m_9236_().isClientSide) {
                        this.m_9236_().m_7967_(entitylargefireball);
                    }
                }
                if (this.isBreathing[i] && (attackTarget == null || !attackTarget.isAlive() || this.breathTicks[i] > 60) && !this.m_9236_().isClientSide) {
                    this.isBreathing[i] = false;
                    this.breathTicks[i] = 0;
                    this.breathCooldown = 15;
                    this.m_9236_().broadcastEntityEvent(this, (byte) (60 + i));
                }
                this.breathTicks[i]++;
            } else {
                this.breathTicks[i] = 0;
            }
            if (striking && this.strikingProgress[i] < 10.0F) {
                this.strikingProgress[i] = this.strikingProgress[i] + 2.5F;
            } else if (!striking && this.strikingProgress[i] > 0.0F) {
                this.strikingProgress[i] = this.strikingProgress[i] - 2.5F;
            }
            this.prevSpeakingProgress[i] = this.speakingProgress[i];
            if (this.speakingProgress[i] > 0.0F) {
                this.speakingProgress[i] = this.speakingProgress[i] - 0.1F;
            }
            this.prevBreathProgress[i] = this.breathProgress[i];
            if (breathing && this.breathProgress[i] < 10.0F) {
                this.breathProgress[i]++;
            } else if (!breathing && this.breathProgress[i] > 0.0F) {
                this.breathProgress[i]--;
            }
        }
        if (this.strikeCooldown > 0) {
            this.strikeCooldown--;
        }
        if (this.breathCooldown > 0) {
            this.breathCooldown--;
        }
        if (this.getHeadCount() == 1 && this.getSeveredHead() != -1) {
            this.setSeveredHead(-1);
        }
        if (this.getHeadCount() == 1 && !this.m_6060_()) {
            this.setHeadCount(2);
            this.setSeveredHead(1);
            this.onlyRegrowOneHeadNotTwo = true;
        }
        if (this.getSeveredHead() != -1 && this.getSeveredHead() < this.getHeadCount()) {
            this.setSeveredHead(Mth.clamp(this.getSeveredHead(), 0, this.getHeadCount() - 1));
            this.regrowHeadCooldown++;
            if (this.regrowHeadCooldown >= 100) {
                this.headDamageTracker[this.getSeveredHead()] = 0.0F;
                this.setSeveredHead(-1);
                if (this.m_6060_()) {
                    this.setHeadCount(this.getHeadCount() - 1);
                } else {
                    this.m_5496_(IafSoundRegistry.HYDRA_REGEN_HEAD, this.m_6121_(), this.m_6100_());
                    if (!this.onlyRegrowOneHeadNotTwo) {
                        this.setHeadCount(this.getHeadCount() + 1);
                    }
                }
                this.onlyRegrowOneHeadNotTwo = false;
                this.regrowHeadCooldown = 0;
            }
        } else {
            this.regrowHeadCooldown = 0;
        }
    }

    public void resetParts() {
        this.clearParts();
        this.headBoxes = new EntityHydraHead[18];
        for (int i = 0; i < this.getHeadCount(); i++) {
            float maxAngle = 5.0F;
            this.headBoxes[i] = new EntityHydraHead(this, 3.2F, ROTATE[this.getHeadCount() - 1][i] * 1.1F, 1.0F, 0.75F, 1.75F, 1.0F, i, false);
            this.headBoxes[9 + i] = new EntityHydraHead(this, 2.1F, ROTATE[this.getHeadCount() - 1][i] * 1.1F, 1.0F, 0.75F, 0.75F, 1.0F, i, true);
            this.headBoxes[i].m_20359_(this);
            this.headBoxes[9 + i].m_20359_(this);
            this.headBoxes[i].setParent(this);
            this.headBoxes[9 + i].setParent(this);
        }
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (this.prevHeadCount != this.getHeadCount()) {
            this.resetParts();
        }
        float partY = 1.0F - this.f_267362_.speed() * 0.5F;
        for (int i = 0; i < this.getHeadCount(); i++) {
            this.headBoxes[i].m_6034_(this.headBoxes[i].m_20185_(), this.m_20186_() + (double) partY, this.headBoxes[i].m_20189_());
            EntityUtil.updatePart(this.headBoxes[i], this);
            this.headBoxes[9 + i].m_6034_(this.headBoxes[9 + i].m_20185_(), this.m_20186_() + (double) partY, this.headBoxes[9 + i].m_20189_());
            EntityUtil.updatePart(this.headBoxes[10], this);
        }
        if (this.getHeadCount() > 1 && !this.m_6060_() && this.m_21223_() < this.m_21233_() && this.f_19797_ % 30 == 0) {
            int level = this.getHeadCount() - 1;
            if (this.getSeveredHead() != -1) {
                level--;
            }
            this.m_7292_(new MobEffectInstance(MobEffects.REGENERATION, 30, level, false, false));
        }
        if (this.m_6060_()) {
            this.m_21195_(MobEffects.REGENERATION);
        }
        this.prevHeadCount = this.getHeadCount();
    }

    private void clearParts() {
        for (Entity entity : this.headBoxes) {
            if (entity != null) {
                entity.remove(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    public void remove(@NotNull Entity.RemovalReason reason) {
        this.clearParts();
        super.m_142687_(reason);
    }

    @Override
    protected void playHurtSound(@NotNull DamageSource source) {
        this.speakingProgress[this.f_19796_.nextInt(this.getHeadCount())] = 1.0F;
        super.m_6677_(source);
    }

    @Override
    public void playAmbientSound() {
        this.speakingProgress[this.f_19796_.nextInt(this.getHeadCount())] = 1.0F;
        super.m_8032_();
    }

    @Override
    public int getAmbientSoundInterval() {
        return 100 / this.getHeadCount();
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.m_7380_(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putInt("HeadCount", this.getHeadCount());
        compound.putInt("SeveredHead", this.getSeveredHead());
        for (int i = 0; i < 9; i++) {
            compound.putFloat("HeadDamage" + i, this.headDamageTracker[i]);
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.m_7378_(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setHeadCount(compound.getInt("HeadCount"));
        this.setSeveredHead(compound.getInt("SeveredHead"));
        for (int i = 0; i < 9; i++) {
            this.headDamageTracker[i] = compound.getFloat("HeadDamage" + i);
        }
        this.setConfigurableAttributes();
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(VARIANT, 0);
        this.f_19804_.define(HEAD_COUNT, 3);
        this.f_19804_.define(SEVERED_HEAD, -1);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (this.lastHitHead > this.getHeadCount()) {
            this.lastHitHead = this.getHeadCount() - 1;
        }
        int headIndex = this.lastHitHead;
        this.headDamageTracker[headIndex] = this.headDamageTracker[headIndex] + amount;
        if (this.headDamageTracker[headIndex] > this.headDamageThreshold && (this.getSeveredHead() == -1 || this.getSeveredHead() >= this.getHeadCount())) {
            this.headDamageTracker[headIndex] = 0.0F;
            this.regrowHeadCooldown = 0;
            this.setSeveredHead(headIndex);
            this.m_5496_(SoundEvents.GUARDIAN_FLOP, this.m_6121_(), this.m_6100_());
        }
        if (this.m_21223_() <= amount + 5.0F && this.getHeadCount() > 1 && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            amount = 0.0F;
        }
        return super.m_6469_(source, amount);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        SpawnGroupData data = super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setVariant(this.f_19796_.nextInt(3));
        return data;
    }

    @Override
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        this.animationTick = tick;
    }

    @Override
    public Animation getAnimation() {
        return this.currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[0];
    }

    @Override
    public boolean shouldAnimalsFear(Entity entity) {
        return true;
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    public int getVariant() {
        return this.f_19804_.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.f_19804_.set(VARIANT, variant);
    }

    public int getHeadCount() {
        return Mth.clamp(this.f_19804_.get(HEAD_COUNT), 1, 9);
    }

    public void setHeadCount(int count) {
        this.f_19804_.set(HEAD_COUNT, Mth.clamp(count, 1, 9));
    }

    public int getSeveredHead() {
        return Mth.clamp(this.f_19804_.get(SEVERED_HEAD), -1, 9);
    }

    public void setSeveredHead(int count) {
        this.f_19804_.set(SEVERED_HEAD, Mth.clamp(count, -1, 9));
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id >= 40 && id <= 48) {
            int index = id - 40;
            this.isStriking[Mth.clamp(index, 0, 8)] = true;
        } else if (id >= 50 && id <= 58) {
            int index = id - 50;
            this.isBreathing[Mth.clamp(index, 0, 8)] = true;
        } else if (id >= 60 && id <= 68) {
            int index = id - 60;
            this.isBreathing[Mth.clamp(index, 0, 8)] = false;
        } else {
            super.m_7822_(id);
        }
    }

    @Override
    public boolean canBeAffected(MobEffectInstance potioneffectIn) {
        return potioneffectIn.getEffect() != MobEffects.POISON && super.m_7301_(potioneffectIn);
    }

    public void onHitHead(float damage, int headIndex) {
        this.lastHitHead = headIndex;
    }

    public void triggerHeadFlags(int index) {
        this.lastHitHead = index;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.HYDRA_IDLE;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return IafSoundRegistry.HYDRA_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.HYDRA_DIE;
    }
}