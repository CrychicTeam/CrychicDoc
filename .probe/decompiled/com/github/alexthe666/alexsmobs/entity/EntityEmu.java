package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIHerdPanic;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIWanderRanged;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

public class EntityEmu extends Animal implements IAnimatedEntity, IHerdPanic {

    public static final Animation ANIMATION_DODGE_LEFT = Animation.create(10);

    public static final Animation ANIMATION_DODGE_RIGHT = Animation.create(10);

    public static final Animation ANIMATION_PECK_GROUND = Animation.create(25);

    public static final Animation ANIMATION_SCRATCH = Animation.create(20);

    public static final Animation ANIMATION_PUZZLED = Animation.create(30);

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityEmu.class, EntityDataSerializers.INT);

    private int animationTick;

    private Animation currentAnimation;

    private int revengeCooldown = 0;

    private boolean emuAttackedDirectly = false;

    public int timeUntilNextEgg = this.f_19796_.nextInt(6000) + 6000;

    protected EntityEmu(EntityType type, Level world) {
        super(type, world);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 20.0).add(Attributes.MOVEMENT_SPEED, 0.35F).add(Attributes.ATTACK_DAMAGE, 3.0);
    }

    public static <T extends Mob> boolean canEmuSpawn(EntityType<? extends Animal> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        boolean spawnBlock = worldIn.m_8055_(pos.below()).m_204336_(AMTagRegistry.EMU_SPAWNS);
        return spawnBlock && worldIn.m_45524_(pos, 0) > 8;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.emuSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.EMU_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.EMU_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.EMU_HURT.get();
    }

    public int getVariant() {
        return this.f_19804_.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.f_19804_.set(VARIANT, variant);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(VARIANT, 0);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new MeleeAttackGoal(this, 1.3, true) {

            @Override
            protected double getAttackReachSqr(LivingEntity attackTarget) {
                return super.getAttackReachSqr(attackTarget) + 2.5;
            }

            @Override
            public boolean canUse() {
                return super.canUse() && EntityEmu.this.revengeCooldown <= 0;
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && EntityEmu.this.revengeCooldown <= 0;
            }
        });
        this.f_21345_.addGoal(2, new AnimalAIHerdPanic(this, 1.5));
        this.f_21345_.addGoal(3, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(4, new FollowParentGoal(this, 1.1));
        this.f_21345_.addGoal(4, new TemptGoal(this, 1.1, Ingredient.of(Items.WHEAT), false));
        this.f_21345_.addGoal(5, new AnimalAIWanderRanged(this, 110, 1.0, 10, 7));
        this.f_21345_.addGoal(6, new LookAtPlayerGoal(this, Player.class, 15.0F));
        this.f_21345_.addGoal(7, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new EntityEmu.HurtByTargetGoal());
        if (AMConfig.emuTargetSkeletons) {
            this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, AbstractSkeleton.class, false));
            this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, Pillager.class, false));
        }
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        return !this.m_6162_() && super.m_6779_(target);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean prev = super.hurt(source, amount);
        if (prev) {
            double range = 15.0;
            int fleeTime = 100 + this.m_217043_().nextInt(5);
            this.revengeCooldown = fleeTime;
            for (EntityEmu emu : this.m_9236_().m_45976_(this.getClass(), this.m_20191_().inflate(range, range / 2.0, range))) {
                emu.revengeCooldown = fleeTime;
                if (emu.m_6162_() && this.f_19796_.nextInt(2) == 0) {
                    emu.emuAttackedDirectly = this.m_21188_() != null;
                    emu.revengeCooldown = emu.emuAttackedDirectly ? 10 + this.m_217043_().nextInt(30) : fleeTime;
                }
            }
            this.emuAttackedDirectly = this.m_21188_() != null;
            this.revengeCooldown = this.emuAttackedDirectly ? 10 + this.m_217043_().nextInt(30) : this.revengeCooldown;
        }
        return prev;
    }

    @Override
    public void travel(Vec3 travelVector) {
        this.m_7910_((float) this.m_21133_(Attributes.MOVEMENT_SPEED) * (this.getAnimation() != ANIMATION_PECK_GROUND && this.getAnimation() != ANIMATION_PUZZLED ? 1.0F : 0.15F) * (this.m_20077_() ? 0.2F : 1.0F));
        super.m_7023_(travelVector);
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (!this.m_9236_().isClientSide) {
            if (this.m_21188_() == null && this.m_5448_() == null && this.m_20184_().lengthSqr() < 0.03 && this.m_217043_().nextInt(190) == 0 && this.getAnimation() == NO_ANIMATION) {
                if (this.m_217043_().nextInt(3) == 0) {
                    this.setAnimation(ANIMATION_PUZZLED);
                } else if (this.m_20096_()) {
                    this.setAnimation(ANIMATION_PECK_GROUND);
                }
            }
            if (this.revengeCooldown > 0) {
                this.revengeCooldown--;
            }
            if (this.revengeCooldown <= 0 && this.m_21188_() != null && !this.emuAttackedDirectly) {
                this.m_6703_(null);
                this.revengeCooldown = 0;
            }
            LivingEntity target = this.m_5448_();
            if (this.m_6084_() && target != null && this.getAnimation() == ANIMATION_SCRATCH && this.m_20270_(target) < 4.0F && (this.getAnimationTick() == 8 || this.getAnimationTick() == 15)) {
                float f1 = this.m_146908_() * (float) (Math.PI / 180.0);
                this.m_20256_(this.m_20184_().add((double) (-Mth.sin(f1) * 0.02F), 0.0, (double) (Mth.cos(f1) * 0.02F)));
                target.knockback(0.4F, target.m_20185_() - this.m_20185_(), target.m_20189_() - this.m_20189_());
                target.hurt(this.m_269291_().mobAttack(this), (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getBaseValue());
            }
        }
        if (!this.m_9236_().isClientSide && this.m_6084_() && !this.m_6162_() && --this.timeUntilNextEgg <= 0) {
            this.m_5496_(SoundEvents.CHICKEN_EGG, 1.0F, (this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.2F + 1.0F);
            this.m_19998_(AMItemRegistry.EMU_EGG.get());
            this.timeUntilNextEgg = this.f_19796_.nextInt(6000) + 6000;
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
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
        return new Animation[] { ANIMATION_DODGE_LEFT, ANIMATION_DODGE_RIGHT, ANIMATION_PECK_GROUND, ANIMATION_SCRATCH, ANIMATION_PUZZLED };
    }

    @Override
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        this.animationTick = tick;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        EntityEmu emu = AMEntityRegistry.EMU.get().create(serverWorld);
        emu.setVariant(this.getVariant());
        return emu;
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_SCRATCH);
        }
        return true;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
        if (compound.contains("EggLayTime")) {
            this.timeUntilNextEgg = compound.getInt("EggLayTime");
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putInt("EggLayTime", this.timeUntilNextEgg);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        if (this.f_19796_.nextInt(200) == 0) {
            this.setVariant(2);
        } else if (this.f_19796_.nextInt(3) == 0) {
            this.setVariant(1);
        }
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public void onPanic() {
    }

    @Override
    public boolean canPanic() {
        return true;
    }

    class HurtByTargetGoal extends net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal {

        public HurtByTargetGoal() {
            super(EntityEmu.this);
        }

        @Override
        public void start() {
            if (!EntityEmu.this.m_6162_() && EntityEmu.this.emuAttackedDirectly) {
                super.start();
            } else {
                this.m_26047_();
                this.m_8041_();
            }
        }

        @Override
        protected void alertOther(Mob mobIn, LivingEntity targetIn) {
            if (mobIn instanceof EntityEmu && !mobIn.m_6162_() && !EntityEmu.this.emuAttackedDirectly && ((EntityEmu) mobIn).revengeCooldown <= 0) {
                super.alertOther(mobIn, targetIn);
            }
        }
    }
}