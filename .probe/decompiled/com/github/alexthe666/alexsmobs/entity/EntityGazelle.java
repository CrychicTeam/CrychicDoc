package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIHerdPanic;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIWanderRanged;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;

public class EntityGazelle extends Animal implements IAnimatedEntity, IHerdPanic {

    private int animationTick;

    private Animation currentAnimation;

    public static final Animation ANIMATION_FLICK_EARS = Animation.create(20);

    public static final Animation ANIMATION_FLICK_TAIL = Animation.create(14);

    public static final Animation ANIMATION_EAT_GRASS = Animation.create(30);

    private boolean hasSpedUp = false;

    private int revengeCooldown = 0;

    private static final EntityDataAccessor<Boolean> RUNNING = SynchedEntityData.defineId(EntityGazelle.class, EntityDataSerializers.BOOLEAN);

    protected EntityGazelle(EntityType type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new AnimalAIHerdPanic(this, 1.1));
        this.f_21345_.addGoal(2, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(4, new FollowParentGoal(this, 1.1));
        this.f_21345_.addGoal(4, new TemptGoal(this, 1.1, Ingredient.of(Items.WHEAT), false));
        this.f_21345_.addGoal(5, new AnimalAIWanderRanged(this, 100, 1.0, 25, 7));
        this.f_21345_.addGoal(6, new LookAtPlayerGoal(this, Player.class, 15.0F));
        this.f_21345_.addGoal(7, new RandomLookAroundGoal(this));
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.GAZELLE_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.GAZELLE_HURT.get();
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.gazelleSpawnRolls, this.m_217043_(), spawnReasonIn) && super.m_5545_(worldIn, spawnReasonIn);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 8;
    }

    @Override
    public boolean isMaxGroupSizeReached(int sizeIn) {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean prev = super.hurt(source, amount);
        if (prev) {
            double range = 15.0;
            int fleeTime = 100 + this.m_217043_().nextInt(150);
            this.revengeCooldown = fleeTime;
            for (EntityGazelle gaz : this.m_9236_().m_45976_(this.getClass(), this.m_20191_().inflate(range, range / 2.0, range))) {
                gaz.revengeCooldown = fleeTime;
            }
        }
        return prev;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(RUNNING, false);
    }

    public boolean isRunning() {
        return this.f_19804_.get(RUNNING);
    }

    public void setRunning(boolean running) {
        this.f_19804_.set(RUNNING, running);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == Items.WHEAT || stack.getItem() == AMItemRegistry.ACACIA_BLOSSOM.get();
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
    public void tick() {
        super.m_8119_();
        if (!this.m_9236_().isClientSide) {
            if (this.getAnimation() == NO_ANIMATION && this.m_217043_().nextInt(70) == 0 && (this.m_21188_() == null || this.m_20270_(this.m_21188_()) > 30.0F)) {
                if (this.m_9236_().getBlockState(this.m_20183_().below()).m_60713_(Blocks.GRASS_BLOCK) && this.m_217043_().nextInt(3) == 0) {
                    this.setAnimation(ANIMATION_EAT_GRASS);
                } else {
                    this.setAnimation(this.m_217043_().nextBoolean() ? ANIMATION_FLICK_EARS : ANIMATION_FLICK_TAIL);
                }
            }
            if (this.revengeCooldown >= 0) {
                this.revengeCooldown--;
            }
            if (this.revengeCooldown == 0 && this.m_21188_() != null) {
                this.m_6703_(null);
            }
            this.setRunning(this.revengeCooldown > 0);
            if (this.isRunning() && !this.hasSpedUp) {
                this.hasSpedUp = true;
                this.m_6858_(true);
                this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue(0.475F);
            }
            if (!this.isRunning() && this.hasSpedUp) {
                this.hasSpedUp = false;
                this.m_6858_(false);
                this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue(0.25);
            }
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("GazelleRunning", this.isRunning());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setRunning(compound.getBoolean("GazelleRunning"));
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
        return new Animation[] { ANIMATION_FLICK_EARS, ANIMATION_FLICK_TAIL, ANIMATION_EAT_GRASS };
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 8.0).add(Attributes.ATTACK_DAMAGE, 2.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_241840_1_, AgeableMob p_241840_2_) {
        return AMEntityRegistry.GAZELLE.get().create(p_241840_1_);
    }

    @Override
    public void onPanic() {
    }

    @Override
    public boolean canPanic() {
        return true;
    }
}