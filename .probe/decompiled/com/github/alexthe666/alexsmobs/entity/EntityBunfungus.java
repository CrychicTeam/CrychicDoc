package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.client.particle.AMParticleRegistry;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAILeapRandomly;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIWanderRanged;
import com.github.alexthe666.alexsmobs.entity.ai.BunfungusAIBeg;
import com.github.alexthe666.alexsmobs.entity.ai.BunfungusAIMelee;
import com.github.alexthe666.alexsmobs.entity.ai.GroundPathNavigatorWide;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class EntityBunfungus extends PathfinderMob implements IAnimatedEntity {

    public static final Animation ANIMATION_SLAM = Animation.create(20);

    public static final Animation ANIMATION_BELLY = Animation.create(10);

    public static final Animation ANIMATION_EAT = Animation.create(20);

    private static final EntityDataAccessor<Boolean> JUMP_ACTIVE = SynchedEntityData.defineId(EntityBunfungus.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(EntityBunfungus.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> BEGGING = SynchedEntityData.defineId(EntityBunfungus.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> CARROTED = SynchedEntityData.defineId(EntityBunfungus.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> TRANSFORMS_IN = SynchedEntityData.defineId(EntityBunfungus.class, EntityDataSerializers.INT);

    public float jumpProgress;

    public float prevJumpProgress;

    public float reboundProgress;

    public float prevReboundProgress;

    public float sleepProgress;

    public float prevSleepProgress;

    public float interestedProgress;

    public float prevInterestedProgress;

    private int animationTick;

    private Animation currentAnimation;

    public int prevTransformTime;

    public static final int MAX_TRANSFORM_TIME = 50;

    protected EntityBunfungus(EntityType t, Level lvl) {
        super(t, lvl);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 80.0).add(Attributes.ATTACK_DAMAGE, 8.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.MOVEMENT_SPEED, 0.21F);
    }

    @Override
    public void playAmbientSound() {
        if (!this.isSleeping()) {
            super.m_8032_();
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.BUNFUNGUS_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.BUNFUNGUS_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.BUNFUNGUS_HURT.get();
    }

    @Override
    public boolean removeWhenFarAway(double double0) {
        return false;
    }

    public static boolean canBunfungusSpawn(EntityType type, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        return worldIn.m_8055_(pos.below()).m_60815_();
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.mungusSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new GroundPathNavigatorWide(this, worldIn);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new BunfungusAIMelee(this));
        this.f_21345_.addGoal(2, new BunfungusAIBeg(this, 1.0));
        this.f_21345_.addGoal(3, new AnimalAIWanderRanged(this, 60, 1.0, 16, 7) {

            @Override
            public boolean canUse() {
                return super.canUse() && EntityBunfungus.this.canUseComplexAI();
            }
        });
        this.f_21345_.addGoal(4, new AnimalAILeapRandomly(this, 60, 7) {

            @Override
            public boolean canUse() {
                return super.canUse() && EntityBunfungus.this.canUseComplexAI();
            }
        });
        this.f_21345_.addGoal(9, new LookAtPlayerGoal(this, Player.class, 10.0F) {

            @Override
            public boolean canUse() {
                return super.canUse() && EntityBunfungus.this.canUseComplexAI();
            }
        });
        this.f_21345_.addGoal(10, new RandomLookAroundGoal(this) {

            @Override
            public boolean canUse() {
                return super.canUse() && EntityBunfungus.this.canUseComplexAI();
            }
        });
        this.f_21346_.addGoal(2, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, Mob.class, 5, false, false, mob -> mob instanceof Enemy && !(mob instanceof Creeper) && (mob.getMobType() != MobType.WATER || !mob.m_20072_()) && !mob.m_6095_().is(AMTagRegistry.BUNFUNGUS_IGNORES)));
    }

    private boolean canUseComplexAI() {
        return !this.isRabbitForm() && !this.isSleeping();
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.98F;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(JUMP_ACTIVE, false);
        this.f_19804_.define(SLEEPING, false);
        this.f_19804_.define(BEGGING, false);
        this.f_19804_.define(CARROTED, false);
        this.f_19804_.define(TRANSFORMS_IN, 0);
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevJumpProgress = this.jumpProgress;
        this.prevReboundProgress = this.reboundProgress;
        this.prevSleepProgress = this.sleepProgress;
        this.prevInterestedProgress = this.interestedProgress;
        this.prevTransformTime = this.transformsIn();
        if (!this.m_9236_().isClientSide) {
            this.f_19804_.set(JUMP_ACTIVE, !this.m_20096_());
        }
        if (this.f_19804_.get(JUMP_ACTIVE) && !this.m_20072_()) {
            if (this.jumpProgress < 5.0F) {
                this.jumpProgress += 0.5F;
                if (this.reboundProgress > 0.0F) {
                    this.reboundProgress--;
                }
            }
            if (this.jumpProgress >= 5.0F && this.reboundProgress < 5.0F) {
                this.reboundProgress += 0.5F;
            }
        } else {
            if (this.reboundProgress > 0.0F) {
                this.reboundProgress = Math.max(this.reboundProgress - 1.0F, 0.0F);
            }
            if (this.jumpProgress > 0.0F) {
                this.jumpProgress = Math.max(this.jumpProgress - 1.0F, 0.0F);
            }
        }
        if (this.isSleepingPose()) {
            if (this.sleepProgress < 5.0F) {
                this.sleepProgress++;
            }
        } else if (this.sleepProgress > 0.0F) {
            this.sleepProgress--;
        }
        if (this.isBegging()) {
            if (this.interestedProgress < 5.0F) {
                this.interestedProgress++;
            }
        } else if (this.interestedProgress > 0.0F) {
            this.interestedProgress--;
        }
        if (!this.m_9236_().isClientSide) {
            LivingEntity target = this.m_5448_();
            if (target != null && target.isAlive()) {
                if (this.isSleeping()) {
                    this.setSleeping(false);
                }
                double dist = (double) this.m_20270_(target);
                boolean flag = false;
                if (this.getAnimationTick() == 5) {
                    if (dist < 3.5 && this.getAnimation() == ANIMATION_BELLY) {
                        for (LivingEntity entity : this.m_9236_().m_45976_(LivingEntity.class, this.m_20191_().inflate(2.0))) {
                            if ((entity == target || entity instanceof Monster) && !entity.m_6095_().is(AMTagRegistry.BUNFUNGUS_IGNORE_AOE_ATTACKS)) {
                                flag = true;
                                this.launch(entity);
                                entity.hurt(this.m_269291_().mobAttack(this), (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getBaseValue());
                            }
                        }
                    } else if (dist < 2.5 && this.getAnimation() == ANIMATION_SLAM) {
                        for (LivingEntity entityx : this.m_9236_().m_45976_(LivingEntity.class, this.m_20191_().inflate(2.0))) {
                            if ((entityx == target || entityx instanceof Monster) && !entityx.m_6095_().is(AMTagRegistry.BUNFUNGUS_IGNORE_AOE_ATTACKS)) {
                                flag = true;
                                entityx.knockback(0.2F, entityx.m_20185_() - this.m_20185_(), entityx.m_20189_() - this.m_20189_());
                                entityx.hurt(this.m_269291_().mobAttack(this), (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getBaseValue());
                            }
                        }
                    }
                }
                if (flag) {
                    this.m_5496_(AMSoundRegistry.BUNFUNGUS_ATTACK.get(), this.m_6121_(), this.m_6100_());
                }
            }
            if (this.f_19797_ % 40 == 0) {
                this.m_5634_(1.0F);
            }
        }
        if (this.getAnimation() == NO_ANIMATION && this.isCarrot(this.m_21120_(InteractionHand.MAIN_HAND))) {
            this.setAnimation(ANIMATION_EAT);
        }
        if (this.getAnimation() == ANIMATION_EAT) {
            if (this.getAnimationTick() % 4 == 0) {
                this.m_146850_(GameEvent.EAT);
                this.m_5496_(SoundEvents.GENERIC_EAT, this.m_6121_(), this.m_6100_());
            }
            if (this.getAnimationTick() >= 18) {
                ItemStack stack = this.m_21120_(InteractionHand.MAIN_HAND);
                if (!stack.isEmpty()) {
                    stack.shrink(1);
                    this.setCarroted(true);
                    this.m_7292_(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1000));
                    this.m_7292_(new MobEffectInstance(MobEffects.REGENERATION, 1000, 1));
                    this.m_5634_(8.0F);
                }
            } else {
                for (int i = 0; i < 3; i++) {
                    double d2 = this.f_19796_.nextGaussian() * 0.02;
                    double d0 = this.f_19796_.nextGaussian() * 0.02;
                    double d1 = this.f_19796_.nextGaussian() * 0.02;
                    this.m_9236_().addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.m_21120_(InteractionHand.MAIN_HAND)), this.m_20185_() + (double) (this.f_19796_.nextFloat() * this.m_20205_()) - (double) this.m_20205_() * 0.5, this.m_20186_() + (double) (this.m_20206_() * 0.5F) + (double) (this.f_19796_.nextFloat() * this.m_20206_() * 0.5F), this.m_20189_() + (double) (this.f_19796_.nextFloat() * this.m_20205_()) - (double) this.m_20205_() * 0.5, d0, d1, d2);
                }
            }
        }
        if (!this.m_9236_().isClientSide && this.transformsIn() > 0) {
            this.setTransformsIn(this.transformsIn() - 1);
        }
        if (this.m_9236_().isClientSide) {
            if (this.isRabbitForm()) {
                for (int i = 0; i < 3; i++) {
                    double d2 = this.f_19796_.nextGaussian() * 0.02;
                    double d0 = this.f_19796_.nextGaussian() * 0.02;
                    double d1 = this.f_19796_.nextGaussian() * 0.02;
                    float f1 = (float) (50 - this.transformsIn()) / 50.0F;
                    float scale = f1 * 0.5F + 0.15F;
                    this.m_9236_().addParticle(AMParticleRegistry.BUNFUNGUS_TRANSFORMATION.get(), this.m_20208_((double) scale), this.m_20227_(this.f_19796_.nextDouble() * (double) scale), this.m_20262_((double) scale), d0, d1, d2);
                }
            }
            if (this.isSleeping() && this.f_19796_.nextFloat() < 0.3F) {
                double d0 = this.f_19796_.nextGaussian() * 0.02;
                float radius = this.m_20205_() * (0.7F + this.f_19796_.nextFloat() * 0.1F);
                float angle = (float) (Math.PI / 180.0) * this.f_20883_;
                double extraX = (double) (radius * Mth.sin((float) Math.PI + angle) + this.f_19796_.nextFloat() * 0.5F - 0.25F);
                double extraZ = (double) (radius * Mth.cos(angle) + this.f_19796_.nextFloat() * 0.5F - 0.25F);
                ParticleOptions data = this.f_19796_.nextFloat() < 0.3F ? AMParticleRegistry.BUNFUNGUS_TRANSFORMATION.get() : AMParticleRegistry.FUNGUS_BUBBLE.get();
                this.m_9236_().addParticle(data, this.m_20185_() + extraX, this.m_20186_() + (double) (this.f_19796_.nextFloat() * 0.1F), this.m_20189_() + extraZ, 0.0, d0, 0.0);
            }
        } else if (this.m_9236_().isDay() && this.m_5448_() == null && !this.isBegging() && !this.m_20072_()) {
            if (this.f_19797_ % 10 == 0 && this.m_217043_().nextInt(300) == 0) {
                this.setSleeping(true);
            }
        } else if (this.isSleeping()) {
            this.setSleeping(false);
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    private void launch(LivingEntity target) {
        if (target.m_20096_()) {
            double d0 = target.m_20185_() - this.m_20185_();
            double d1 = target.m_20189_() - this.m_20189_();
            double d2 = Math.max(d0 * d0 + d1 * d1, 0.001);
            float f = 6.0F + this.f_19796_.nextFloat() * 2.0F;
            target.m_5997_(d0 / d2 * (double) f, (double) (0.6F + this.f_19796_.nextFloat() * 0.7F), d1 / d2 * (double) f);
        }
    }

    @Override
    public boolean isSleeping() {
        return this.f_19804_.get(SLEEPING);
    }

    public void setSleeping(boolean sleeping) {
        this.f_19804_.set(SLEEPING, sleeping);
    }

    public boolean isSleepingPose() {
        return this.isSleeping() || this.getAnimation() == ANIMATION_SLAM && this.getAnimationTick() < 10;
    }

    public boolean isCarroted() {
        return this.f_19804_.get(CARROTED);
    }

    public void setCarroted(boolean head) {
        this.f_19804_.set(CARROTED, head);
    }

    public boolean isBegging() {
        return this.f_19804_.get(BEGGING) && this.getAnimation() != ANIMATION_EAT;
    }

    public void setBegging(boolean begging) {
        this.f_19804_.set(BEGGING, begging);
    }

    public int transformsIn() {
        return Math.min(this.f_19804_.get(TRANSFORMS_IN), 50);
    }

    public boolean isRabbitForm() {
        return this.transformsIn() > 0;
    }

    public void setTransformsIn(int time) {
        this.f_19804_.set(TRANSFORMS_IN, time);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        InteractionResult type = super.m_6071_(player, hand);
        InteractionResult interactionresult = itemstack.interactLivingEntity(player, this, hand);
        if (interactionresult != InteractionResult.SUCCESS && type != InteractionResult.SUCCESS && this.m_21120_(InteractionHand.MAIN_HAND).isEmpty() && this.isCarrot(itemstack) && this.m_21205_().isEmpty()) {
            ItemStack cop = itemstack.copy();
            cop.setCount(1);
            this.m_21008_(InteractionHand.MAIN_HAND, cop);
            if (!player.isCreative()) {
                itemstack.shrink(1);
            }
        }
        return type;
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (!this.isRabbitForm() && !this.isSleeping()) {
            super.m_7023_(travelVector);
        } else {
            super.m_7023_(Vec3.ZERO);
        }
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
        return new Animation[] { ANIMATION_EAT, ANIMATION_BELLY, ANIMATION_SLAM };
    }

    public boolean isCarrot(ItemStack stack) {
        return stack.getItem() == Items.CARROT || stack.getItem() == Items.GOLDEN_CARROT;
    }

    public boolean defendsMungusAgainst(LivingEntity lastHurtByMob) {
        return !(lastHurtByMob instanceof Player) || this.isCarroted();
    }

    public void onJump() {
    }
}