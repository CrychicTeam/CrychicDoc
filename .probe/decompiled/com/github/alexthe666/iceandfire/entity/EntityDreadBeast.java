package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.entity.ai.DreadAITargetNonDread;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.IAnimalFear;
import com.github.alexthe666.iceandfire.entity.util.IDreadMob;
import com.github.alexthe666.iceandfire.entity.util.IVillagerFear;
import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class EntityDreadBeast extends EntityDreadMob implements IAnimatedEntity, IVillagerFear, IAnimalFear {

    private static final EntityDataAccessor<Float> SCALE = SynchedEntityData.defineId(EntityDreadBeast.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityDreadBeast.class, EntityDataSerializers.INT);

    private static final float INITIAL_WIDTH = 1.2F;

    private static final float INITIAL_HEIGHT = 0.9F;

    public static Animation ANIMATION_SPAWN = Animation.create(40);

    public static Animation ANIMATION_BITE = Animation.create(15);

    private int animationTick;

    private Animation currentAnimation;

    private final int hostileTicks = 0;

    private float firstWidth = 1.0F;

    private float firstHeight = 1.0F;

    public EntityDreadBeast(EntityType type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new FloatGoal(this));
        this.f_21345_.addGoal(2, new MeleeAttackGoal(this, 1.0, true));
        this.f_21345_.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.f_21345_.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21345_.addGoal(7, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this, IDreadMob.class));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, new Predicate<LivingEntity>() {

            public boolean apply(@Nullable LivingEntity entity) {
                return DragonUtils.canHostilesTarget(entity);
            }
        }));
        this.f_21346_.addGoal(3, new DreadAITargetNonDread(this, LivingEntity.class, false, new Predicate<LivingEntity>() {

            public boolean apply(LivingEntity entity) {
                return entity instanceof LivingEntity && DragonUtils.canHostilesTarget(entity);
            }
        }));
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0).add(Attributes.MOVEMENT_SPEED, 0.45).add(Attributes.ATTACK_DAMAGE, 4.0).add(Attributes.FOLLOW_RANGE, 128.0).add(Attributes.ARMOR, 1.0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(VARIANT, 0);
        this.f_19804_.define(SCALE, 1.0F);
    }

    @Override
    public float getScale() {
        return this.getSize();
    }

    public float getSize() {
        return this.f_19804_.get(SCALE);
    }

    public void setSize(float scale) {
        this.f_19804_.set(SCALE, scale);
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entityIn) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_BITE);
        }
        return true;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (Math.abs(this.firstWidth - 1.2F * this.getSize()) > 0.01F || Math.abs(this.firstHeight - 0.9F * this.getSize()) > 0.01F) {
            this.firstWidth = 1.2F * this.getSize();
            this.firstHeight = 0.9F * this.getSize();
        }
        if (this.getAnimation() == ANIMATION_SPAWN && this.getAnimationTick() < 30) {
            BlockState belowBlock = this.m_9236_().getBlockState(this.m_20183_().below());
            if (belowBlock.m_60734_() != Blocks.AIR) {
                for (int i = 0; i < 5; i++) {
                    this.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, belowBlock), this.m_20185_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), this.m_20191_().minY, this.m_20189_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), this.f_19796_.nextGaussian() * 0.02, this.f_19796_.nextGaussian() * 0.02, this.f_19796_.nextGaussian() * 0.02);
                }
            }
            this.m_20334_(0.0, this.m_20184_().y, 0.0);
        }
        if (this.m_5448_() != null && this.m_20270_(this.m_5448_()) < 4.0F && this.m_142582_(this.m_5448_())) {
            if (this.getAnimation() == NO_ANIMATION) {
                this.setAnimation(ANIMATION_BITE);
            }
            this.m_21391_(this.m_5448_(), 360.0F, 80.0F);
            if (this.getAnimation() == ANIMATION_BITE && this.getAnimationTick() == 6) {
                this.m_5448_().hurt(this.m_9236_().damageSources().mobAttack(this), (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue());
                if (this.m_5448_() != null) {
                    this.m_5448_().knockback(0.25, this.m_20185_() - this.m_5448_().m_20185_(), this.m_20189_() - this.m_5448_().m_20189_());
                }
            }
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putFloat("DreadScale", this.getSize());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setSize(compound.getFloat("DreadScale"));
    }

    public int getVariant() {
        return this.f_19804_.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.f_19804_.set(VARIANT, variant);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        SpawnGroupData data = super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setAnimation(ANIMATION_SPAWN);
        this.setVariant(this.f_19796_.nextInt(2));
        this.setSize(0.85F + this.f_19796_.nextFloat() * 0.5F);
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
        return new Animation[] { ANIMATION_SPAWN, ANIMATION_BITE };
    }

    @Override
    public boolean shouldAnimalsFear(Entity entity) {
        return true;
    }

    @Override
    public boolean shouldFear() {
        return true;
    }

    @Override
    public Entity getCommander() {
        return null;
    }

    @Override
    public boolean isAlliedTo(Entity entityIn) {
        return entityIn instanceof IDreadMob || super.isAlliedTo(entityIn);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.WOLF_GROWL;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return SoundEvents.WOLF_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WOLF_DEATH;
    }

    @Override
    public float getVoicePitch() {
        return super.m_6100_() * 0.7F;
    }
}