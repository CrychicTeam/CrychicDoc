package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.block.AMBlockRegistry;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIWadeSwimming;
import com.github.alexthe666.alexsmobs.entity.ai.CreatureAITargetItems;
import com.github.alexthe666.alexsmobs.entity.ai.DirectPathNavigator;
import com.github.alexthe666.alexsmobs.entity.ai.EntityAINearestTarget3D;
import com.github.alexthe666.alexsmobs.entity.ai.FlightMoveController;
import com.github.alexthe666.alexsmobs.entity.ai.GroundPathNavigatorWide;
import com.github.alexthe666.alexsmobs.entity.ai.ShoebillAIFish;
import com.github.alexthe666.alexsmobs.entity.ai.ShoebillAIFlightFlee;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityShoebill extends Animal implements IAnimatedEntity, ITargetsDroppedItems {

    public static final Animation ANIMATION_FISH = Animation.create(40);

    public static final Animation ANIMATION_BEAKSHAKE = Animation.create(20);

    public static final Animation ANIMATION_ATTACK = Animation.create(20);

    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(EntityShoebill.class, EntityDataSerializers.BOOLEAN);

    public float prevFlyProgress;

    public float flyProgress;

    public int revengeCooldown = 0;

    private int animationTick;

    private Animation currentAnimation;

    private boolean isLandNavigator;

    public int fishingCooldown = 1200 + this.f_19796_.nextInt(1200);

    public int lureLevel = 0;

    public int luckLevel = 0;

    public static final Predicate<LivingEntity> TARGET_BABY = animal -> animal.isBaby();

    protected EntityShoebill(EntityType type, Level world) {
        super(type, world);
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
        this.m_21441_(BlockPathTypes.WATER_BORDER, 0.0F);
        this.switchNavigator(false);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.shoebillSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.ATTACK_DAMAGE, 4.0).add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.SHOEBILL_HURT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.SHOEBILL_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.SHOEBILL_HURT.get();
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean prev = super.hurt(source, amount);
        if (prev && source.getEntity() != null && !(source.getEntity() instanceof AbstractFish)) {
            double range = 15.0;
            int fleeTime = 100 + this.m_217043_().nextInt(150);
            this.revengeCooldown = fleeTime;
            for (EntityShoebill gaz : this.m_9236_().m_45976_(this.getClass(), this.m_20191_().inflate(range, range / 2.0, range))) {
                gaz.revengeCooldown = fleeTime;
            }
        }
        return prev;
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = new GroundPathNavigatorWide(this, this.m_9236_());
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new FlightMoveController(this, 0.7F, false);
            this.f_21344_ = new DirectPathNavigator(this, this.m_9236_());
            this.isLandNavigator = false;
        }
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(FLYING, false);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new AnimalAIWadeSwimming(this));
        this.f_21345_.addGoal(1, new ShoebillAIFish(this));
        this.f_21345_.addGoal(3, new MeleeAttackGoal(this, 1.2, true));
        this.f_21345_.addGoal(4, new ShoebillAIFlightFlee(this));
        this.f_21345_.addGoal(5, new TemptGoal(this, 1.1, Ingredient.of(AMTagRegistry.SHOEBILL_FOODSTUFFS), false));
        this.f_21345_.addGoal(6, new RandomStrollGoal(this, 1.0, 1400));
        this.f_21345_.addGoal(7, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21346_.addGoal(1, new EntityAINearestTarget3D(this, AbstractFish.class, 30, false, true, null));
        this.f_21346_.addGoal(2, new CreatureAITargetItems(this, false, 10));
        this.f_21346_.addGoal(3, new HurtByTargetGoal(this, Player.class).setAlertOthers());
        this.f_21346_.addGoal(4, new NearestAttackableTargetGoal(this, EntityAlligatorSnappingTurtle.class, 40, false, false, TARGET_BABY));
        this.f_21346_.addGoal(5, new NearestAttackableTargetGoal(this, Turtle.class, 40, false, false, TARGET_BABY));
        this.f_21346_.addGoal(6, new NearestAttackableTargetGoal(this, EntityCrocodile.class, 40, false, false, TARGET_BABY));
        this.f_21346_.addGoal(7, new EntityAINearestTarget3D(this, EntityTerrapin.class, 100, false, true, null));
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        return this.m_9236_().m_45547_(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() != HitResult.Type.MISS;
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
        if (this.m_20069_()) {
            this.m_274367_(1.2F);
        } else {
            this.m_274367_(0.6F);
        }
        this.prevFlyProgress = this.flyProgress;
        boolean flying = this.isFlying();
        if (flying) {
            if (this.flyProgress < 5.0F) {
                this.flyProgress++;
            }
        } else if (this.flyProgress > 0.0F) {
            this.flyProgress--;
        }
        if (this.revengeCooldown > 0) {
            this.revengeCooldown--;
        }
        if (this.revengeCooldown == 0 && this.m_21188_() != null) {
            this.m_6703_(null);
        }
        if (!this.m_9236_().isClientSide) {
            if (this.fishingCooldown > 0) {
                this.fishingCooldown--;
            }
            if (this.getAnimation() == NO_ANIMATION && this.m_217043_().nextInt(700) == 0) {
                this.setAnimation(ANIMATION_BEAKSHAKE);
            }
            if (flying) {
                if (this.isLandNavigator) {
                    this.switchNavigator(false);
                }
            } else if (!this.isLandNavigator) {
                this.switchNavigator(true);
            }
            if (this.revengeCooldown > 0 && !this.isFlying() && (this.m_20096_() || this.m_20069_())) {
                this.setFlying(false);
            }
            if (this.isFlying()) {
                this.m_20242_(true);
            } else {
                this.m_20242_(false);
            }
        }
        if (!this.m_9236_().isClientSide && this.m_5448_() != null && this.getAnimation() == ANIMATION_ATTACK && this.getAnimationTick() == 9 && this.m_142582_(this.m_5448_())) {
            this.m_5448_().knockback(0.3F, this.m_5448_().m_20185_() - this.m_20185_(), this.m_5448_().m_20189_() - this.m_20189_());
            this.m_5448_().hurt(this.m_269291_().mobAttack(this), (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getBaseValue());
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Flying", this.isFlying());
        compound.putInt("FishingTimer", this.fishingCooldown);
        compound.putInt("FishingLuck", this.luckLevel);
        compound.putInt("FishingLure", this.lureLevel);
        compound.putInt("RevengeCooldownTimer", this.revengeCooldown);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setFlying(compound.getBoolean("Flying"));
        this.fishingCooldown = compound.getInt("FishingTimer");
        this.luckLevel = compound.getInt("FishingLuck");
        this.lureLevel = compound.getInt("FishingLure");
        this.revengeCooldown = compound.getInt("RevengeCooldownTimer");
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.98F;
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_ATTACK);
        }
        return true;
    }

    @Override
    public boolean isFlying() {
        return this.f_19804_.get(FLYING);
    }

    @Override
    public void setFlying(boolean flying) {
        this.f_19804_.set(FLYING, flying);
    }

    @Override
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int i) {
        this.animationTick = i;
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
        return new Animation[] { ANIMATION_FISH, ANIMATION_BEAKSHAKE, ANIMATION_ATTACK };
    }

    @Override
    public InteractionResult mobInteract(Player p_230254_1_, InteractionHand p_230254_2_) {
        ItemStack lvt_3_1_ = p_230254_1_.m_21120_(p_230254_2_);
        if (lvt_3_1_.getItem() != AMBlockRegistry.TERRAPIN_EGG.get().asItem() || !this.m_6084_()) {
            if (lvt_3_1_.getItem() != AMBlockRegistry.CROCODILE_EGG.get().asItem() || !this.m_6084_()) {
                return super.mobInteract(p_230254_1_, p_230254_2_);
            } else if (this.lureLevel >= 10) {
                if (this.getAnimation() == NO_ANIMATION) {
                    this.setAnimation(ANIMATION_BEAKSHAKE);
                }
                return InteractionResult.SUCCESS;
            } else {
                this.lureLevel = Mth.clamp(this.lureLevel + 1, 0, 10);
                this.fishingCooldown = Mth.clamp(this.fishingCooldown - 200, 200, 2400);
                for (int i = 0; i < 6 + this.f_19796_.nextInt(3); i++) {
                    double d2 = this.f_19796_.nextGaussian() * 0.02;
                    double d0 = this.f_19796_.nextGaussian() * 0.02;
                    double d1 = this.f_19796_.nextGaussian() * 0.02;
                    this.m_9236_().addParticle(new ItemParticleOption(ParticleTypes.ITEM, lvt_3_1_), this.m_20185_() + (double) (this.f_19796_.nextFloat() * this.m_20205_()) - (double) this.m_20205_() * 0.5, this.m_20186_() + (double) (this.m_20206_() * 0.5F) + (double) (this.f_19796_.nextFloat() * this.m_20206_() * 0.5F), this.m_20189_() + (double) (this.f_19796_.nextFloat() * this.m_20205_()) - (double) this.m_20205_() * 0.5, d0, d1, d2);
                }
                lvt_3_1_.shrink(1);
                this.m_146850_(GameEvent.EAT);
                this.m_5496_(SoundEvents.CAT_EAT, this.m_6121_(), this.m_6100_());
                return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
            }
        } else if (this.luckLevel >= 10) {
            if (this.getAnimation() == NO_ANIMATION) {
                this.setAnimation(ANIMATION_BEAKSHAKE);
            }
            return InteractionResult.SUCCESS;
        } else {
            this.luckLevel = Mth.clamp(this.luckLevel + 1, 0, 10);
            for (int i = 0; i < 6 + this.f_19796_.nextInt(3); i++) {
                double d2 = this.f_19796_.nextGaussian() * 0.02;
                double d0 = this.f_19796_.nextGaussian() * 0.02;
                double d1 = this.f_19796_.nextGaussian() * 0.02;
                this.m_9236_().addParticle(new ItemParticleOption(ParticleTypes.ITEM, lvt_3_1_), this.m_20185_() + (double) (this.f_19796_.nextFloat() * this.m_20205_()) - (double) this.m_20205_() * 0.5, this.m_20186_() + (double) (this.m_20206_() * 0.5F) + (double) (this.f_19796_.nextFloat() * this.m_20206_() * 0.5F), this.m_20189_() + (double) (this.f_19796_.nextFloat() * this.m_20205_()) - (double) this.m_20205_() * 0.5, d0, d1, d2);
            }
            this.m_146850_(GameEvent.EAT);
            this.m_5496_(SoundEvents.CAT_EAT, this.m_6121_(), this.m_6100_());
            lvt_3_1_.shrink(1);
            return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        return AMEntityRegistry.SHOEBILL.get().create(serverWorld);
    }

    @Override
    public boolean canTargetItem(ItemStack stack) {
        return stack.is(AMTagRegistry.SHOEBILL_FOODSTUFFS) || stack.getItem() == AMItemRegistry.BLOBFISH.get() && this.luckLevel < 10 || stack.getItem() == AMBlockRegistry.CROCODILE_EGG.get().asItem() && this.lureLevel < 10;
    }

    public void resetFishingCooldown() {
        this.fishingCooldown = Math.max(1200 + this.f_19796_.nextInt(1200) - this.lureLevel * 120, 200);
    }

    @Override
    public void onGetItem(ItemEntity e) {
        this.m_146850_(GameEvent.EAT);
        this.m_5496_(SoundEvents.CAT_EAT, this.m_6121_(), this.m_6100_());
        if (e.getItem().getItem() == AMItemRegistry.BLOBFISH.get()) {
            this.luckLevel = Mth.clamp(this.luckLevel + 1, 0, 10);
        } else if (e.getItem().getItem() == AMBlockRegistry.CROCODILE_EGG.get().asItem()) {
            this.lureLevel = Mth.clamp(this.lureLevel + 1, 0, 10);
        }
        this.m_5634_(5.0F);
    }
}