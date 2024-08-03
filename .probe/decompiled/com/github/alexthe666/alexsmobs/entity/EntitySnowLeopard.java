package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIHurtByTargetNotBaby;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIPanicBaby;
import com.github.alexthe666.alexsmobs.entity.ai.CreatureAITargetItems;
import com.github.alexthe666.alexsmobs.entity.ai.SnowLeopardAIMelee;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class EntitySnowLeopard extends Animal implements IAnimatedEntity, ITargetsDroppedItems {

    public static final Animation ANIMATION_ATTACK_R = Animation.create(13);

    public static final Animation ANIMATION_ATTACK_L = Animation.create(13);

    private int animationTick;

    private Animation currentAnimation;

    public float prevSneakProgress;

    public float sneakProgress;

    public float prevTackleProgress;

    public float tackleProgress;

    public float prevSitProgress;

    public float sitProgress;

    private static final EntityDataAccessor<Boolean> TACKLING = SynchedEntityData.defineId(EntitySnowLeopard.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(EntitySnowLeopard.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(EntitySnowLeopard.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SL_SNEAKING = SynchedEntityData.defineId(EntitySnowLeopard.class, EntityDataSerializers.BOOLEAN);

    private boolean hasSlowedDown = false;

    private int sittingTime = 0;

    private int maxSitTime = 75;

    public float prevSleepProgress;

    public float sleepProgress;

    protected EntitySnowLeopard(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.m_274367_(2.0F);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.snowLeopardSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static <T extends Mob> boolean canSnowLeopardSpawn(EntityType<EntitySnowLeopard> snowleperd, LevelAccessor worldIn, MobSpawnType reason, BlockPos p_223317_3_, RandomSource random) {
        BlockState blockstate = worldIn.m_8055_(p_223317_3_.below());
        return (blockstate.m_204336_(BlockTags.BASE_STONE_OVERWORLD) || blockstate.m_60713_(Blocks.DIRT) || blockstate.m_60713_(Blocks.GRASS_BLOCK)) && worldIn.m_45524_(p_223317_3_, 0) > 8;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == AMItemRegistry.MOOSE_RIBS.get() || stack.getItem() == AMItemRegistry.COOKED_MOOSE_RIBS.get();
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new FloatGoal(this));
        this.f_21345_.addGoal(2, new AnimalAIPanicBaby(this, 1.25));
        this.f_21345_.addGoal(3, new SnowLeopardAIMelee(this));
        this.f_21345_.addGoal(5, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(6, new FollowParentGoal(this, 1.1));
        this.f_21345_.addGoal(7, new RandomStrollGoal(this, 1.0, 70));
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, Player.class, 15.0F));
        this.f_21345_.addGoal(8, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new AnimalAIHurtByTargetNotBaby(this));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, LivingEntity.class, 10, false, true, AMEntityRegistry.buildPredicateFromTag(AMTagRegistry.SNOW_LEOPARD_TARGETS)));
        this.f_21346_.addGoal(3, new CreatureAITargetItems(this, false, 30));
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 30.0).add(Attributes.ATTACK_DAMAGE, 6.0).add(Attributes.MOVEMENT_SPEED, 0.35F).add(Attributes.FOLLOW_RANGE, 64.0);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.SNOW_LEOPARD_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.SNOW_LEOPARD_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.SNOW_LEOPARD_HURT.get();
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(SITTING, false);
        this.f_19804_.define(SLEEPING, false);
        this.f_19804_.define(SL_SNEAKING, false);
        this.f_19804_.define(TACKLING, false);
    }

    public boolean isSitting() {
        return this.f_19804_.get(SITTING);
    }

    public void setSitting(boolean bar) {
        this.f_19804_.set(SITTING, bar);
    }

    public boolean isTackling() {
        return this.f_19804_.get(TACKLING);
    }

    public void setTackling(boolean bar) {
        this.f_19804_.set(TACKLING, bar);
    }

    public boolean isSLSneaking() {
        return this.f_19804_.get(SL_SNEAKING);
    }

    public void setSlSneaking(boolean bar) {
        this.f_19804_.set(SL_SNEAKING, bar);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        return AMEntityRegistry.SNOW_LEOPARD.get().create(serverWorld);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevSitProgress = this.sitProgress;
        this.prevSneakProgress = this.sneakProgress;
        this.prevTackleProgress = this.tackleProgress;
        this.prevSleepProgress = this.sleepProgress;
        boolean sitting = this.isSitting();
        boolean slSneaking = this.isSLSneaking();
        boolean tackling = this.isTackling();
        boolean sleeping = this.isSleeping();
        if (sitting) {
            if (this.sitProgress < 5.0F) {
                this.sitProgress += 0.5F;
            }
        } else if (this.sitProgress > 0.0F) {
            this.sitProgress -= 0.5F;
        }
        if (slSneaking) {
            if (this.sneakProgress < 5.0F) {
                this.sneakProgress += 0.5F;
            }
        } else if (this.sneakProgress > 0.0F) {
            this.sneakProgress -= 0.5F;
        }
        if (tackling) {
            if (this.tackleProgress < 3.0F) {
                this.tackleProgress++;
            }
        } else if (this.tackleProgress > 0.0F) {
            this.tackleProgress--;
        }
        if (sleeping) {
            if (this.sleepProgress < 5.0F) {
                this.sleepProgress += 0.5F;
            }
        } else if (this.sleepProgress > 0.0F) {
            this.sleepProgress -= 0.5F;
        }
        if (slSneaking && !this.hasSlowedDown) {
            this.hasSlowedDown = true;
            this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue(0.25);
        }
        if (!slSneaking && this.hasSlowedDown) {
            this.hasSlowedDown = false;
            this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue(0.35F);
        }
        if (tackling) {
            this.f_20883_ = this.m_146908_();
        }
        if (!this.m_9236_().isClientSide) {
            if (this.m_5448_() != null && (this.isSitting() || this.isSleeping())) {
                this.setSitting(false);
                this.setSleeping(false);
            }
            if ((this.isSitting() || this.isSleeping()) && (++this.sittingTime > this.maxSitTime || this.m_5448_() != null || this.m_27593_() || this.m_20072_())) {
                this.setSitting(false);
                this.setSleeping(false);
                this.sittingTime = 0;
                this.maxSitTime = 100 + this.f_19796_.nextInt(50);
            }
            if (this.m_5448_() == null && this.m_20184_().lengthSqr() < 0.03 && this.getAnimation() == NO_ANIMATION && !this.isSleeping() && !this.isSitting() && !this.m_20072_() && this.f_19796_.nextInt(340) == 0) {
                this.sittingTime = 0;
                if (this.m_217043_().nextInt(2) != 0) {
                    this.maxSitTime = 200 + this.f_19796_.nextInt(800);
                    this.setSitting(true);
                    this.setSleeping(false);
                } else {
                    this.maxSitTime = 2000 + this.f_19796_.nextInt(2600);
                    this.setSitting(false);
                    this.setSleeping(true);
                }
            }
        }
        LivingEntity attackTarget = this.m_5448_();
        if (attackTarget != null && (double) this.m_20270_(attackTarget) < (double) (attackTarget.m_20205_() + this.m_20205_()) + 0.6 && this.m_142582_(attackTarget)) {
            if (this.getAnimation() == ANIMATION_ATTACK_L && this.getAnimationTick() == 7) {
                this.m_7327_(attackTarget);
                float rot = this.m_146908_() + 90.0F;
                attackTarget.knockback(0.5, (double) Mth.sin(rot * (float) (Math.PI / 180.0)), (double) (-Mth.cos(rot * (float) (Math.PI / 180.0))));
            }
            if (this.getAnimation() == ANIMATION_ATTACK_R && this.getAnimationTick() == 7) {
                this.m_7327_(attackTarget);
                float rot = this.m_146908_() - 90.0F;
                attackTarget.knockback(0.5, (double) Mth.sin(rot * (float) (Math.PI / 180.0)), (double) (-Mth.cos(rot * (float) (Math.PI / 180.0))));
            }
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean prev = super.hurt(source, amount);
        if (prev) {
            this.sittingTime = 0;
            this.setSleeping(false);
            this.setSitting(false);
        }
        return prev;
    }

    @Override
    public void travel(Vec3 vec3d) {
        if (this.isSitting() || this.isSleeping()) {
            if (this.m_21573_().getPath() != null) {
                this.m_21573_().stop();
            }
            vec3d = Vec3.ZERO;
        }
        super.m_7023_(vec3d);
    }

    @Override
    protected boolean isImmobile() {
        return super.m_6107_();
    }

    @Override
    public boolean isSleeping() {
        return this.f_19804_.get(SLEEPING);
    }

    public void setSleeping(boolean sleeping) {
        this.f_19804_.set(SLEEPING, sleeping);
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
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        this.animationTick = tick;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_ATTACK_L, ANIMATION_ATTACK_R };
    }

    @Override
    public boolean canTargetItem(ItemStack stack) {
        return stack.getItem().isEdible() && stack.getItem().getFoodProperties() != null && stack.getItem().getFoodProperties().isMeat();
    }

    @Override
    public void onGetItem(ItemEntity e) {
        this.m_5634_(5.0F);
    }
}