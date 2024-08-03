package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.ai.GhostAICharge;
import com.github.alexthe666.iceandfire.entity.ai.GhostPathNavigator;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.IAnimalFear;
import com.github.alexthe666.iceandfire.entity.util.IBlacklistedFromStatues;
import com.github.alexthe666.iceandfire.entity.util.IHasCustomizableAttributes;
import com.github.alexthe666.iceandfire.entity.util.IHumanoid;
import com.github.alexthe666.iceandfire.entity.util.IVillagerFear;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class EntityGhost extends Monster implements IAnimatedEntity, IVillagerFear, IAnimalFear, IHumanoid, IBlacklistedFromStatues, IHasCustomizableAttributes {

    private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(EntityGhost.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> CHARGING = SynchedEntityData.defineId(EntityGhost.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> IS_DAYTIME_MODE = SynchedEntityData.defineId(EntityGhost.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> WAS_FROM_CHEST = SynchedEntityData.defineId(EntityGhost.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> DAYTIME_COUNTER = SynchedEntityData.defineId(EntityGhost.class, EntityDataSerializers.INT);

    public static Animation ANIMATION_SCARE;

    public static Animation ANIMATION_HIT;

    private int animationTick;

    private Animation currentAnimation;

    public EntityGhost(EntityType<EntityGhost> type, Level worldIn) {
        super(type, worldIn);
        ANIMATION_SCARE = Animation.create(30);
        ANIMATION_HIT = Animation.create(10);
        this.f_21342_ = new EntityGhost.MoveHelper(this);
    }

    @NotNull
    @Override
    protected ResourceLocation getDefaultLootTable() {
        return this.wasFromChest() ? BuiltInLootTables.EMPTY : this.m_6095_().getDefaultLootTable();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.GHOST_IDLE;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return IafSoundRegistry.GHOST_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.GHOST_DIE;
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, IafConfig.ghostMaxHealth).add(Attributes.FOLLOW_RANGE, 64.0).add(Attributes.MOVEMENT_SPEED, 0.15).add(Attributes.ATTACK_DAMAGE, IafConfig.ghostAttackStrength).add(Attributes.ARMOR, 1.0);
    }

    @Override
    public void setConfigurableAttributes() {
        this.m_21051_(Attributes.MAX_HEALTH).setBaseValue(IafConfig.ghostMaxHealth);
        this.m_21051_(Attributes.ATTACK_DAMAGE).setBaseValue(IafConfig.ghostAttackStrength);
    }

    @Override
    public boolean canBeAffected(MobEffectInstance potioneffectIn) {
        return potioneffectIn.getEffect() != MobEffects.POISON && potioneffectIn.getEffect() != MobEffects.WITHER && super.m_7301_(potioneffectIn);
    }

    @Override
    public boolean isInvulnerableTo(@NotNull DamageSource source) {
        return super.m_6673_(source) || source.is(DamageTypeTags.IS_FIRE) || source.is(DamageTypes.IN_WALL) || source.is(DamageTypes.CACTUS) || source.is(DamageTypes.DROWN) || source.is(DamageTypes.FALLING_BLOCK) || source.is(DamageTypes.FALLING_ANVIL) || source.is(DamageTypes.SWEET_BERRY_BUSH);
    }

    @NotNull
    @Override
    protected PathNavigation createNavigation(@NotNull Level worldIn) {
        return new GhostPathNavigator(this, worldIn);
    }

    public boolean isCharging() {
        return this.f_19804_.get(CHARGING);
    }

    public void setCharging(boolean moving) {
        this.f_19804_.set(CHARGING, moving);
    }

    public boolean isDaytimeMode() {
        return this.f_19804_.get(IS_DAYTIME_MODE);
    }

    public void setDaytimeMode(boolean moving) {
        this.f_19804_.set(IS_DAYTIME_MODE, moving);
    }

    public boolean wasFromChest() {
        return this.f_19804_.get(WAS_FROM_CHEST);
    }

    public void setFromChest(boolean moving) {
        this.f_19804_.set(WAS_FROM_CHEST, moving);
    }

    @NotNull
    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeTurnedToStone() {
        return false;
    }

    @Override
    protected void doPush(@NotNull Entity entity) {
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new FloatGoal(this));
        this.f_21345_.addGoal(2, new RestrictSunGoal(this));
        this.f_21345_.addGoal(3, new FleeSunGoal(this, 1.0));
        this.f_21345_.addGoal(3, new GhostAICharge(this));
        this.f_21345_.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F, 1.0F) {

            @Override
            public boolean canContinueToUse() {
                return this.f_25513_ != null && this.f_25513_ instanceof Player && ((Player) this.f_25513_).isCreative() ? false : super.canContinueToUse();
            }
        });
        this.f_21345_.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.6) {

            @Override
            public boolean canUse() {
                this.f_25730_ = 60;
                return super.m_8036_();
            }
        });
        this.f_21345_.addGoal(6, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, Player.class, 10, false, false, new Predicate<Entity>() {

            public boolean apply(@Nullable Entity entity) {
                return entity.isAlive();
            }
        }));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, LivingEntity.class, 10, false, false, new Predicate<Entity>() {

            public boolean apply(@Nullable Entity entity) {
                return entity instanceof LivingEntity && DragonUtils.isAlive((LivingEntity) entity) && DragonUtils.isVillager(entity);
            }
        }));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.f_19794_ = true;
        if (!this.m_9236_().isClientSide) {
            boolean day = this.isSunBurnTick() && !this.wasFromChest();
            if (day) {
                if (!this.isDaytimeMode()) {
                    this.setAnimation(ANIMATION_SCARE);
                }
                this.setDaytimeMode(true);
            } else {
                this.setDaytimeMode(false);
                this.setDaytimeCounter(0);
            }
            if (this.isDaytimeMode()) {
                this.m_20256_(Vec3.ZERO);
                this.setDaytimeCounter(this.getDaytimeCounter() + 1);
                if (this.getDaytimeCounter() >= 100) {
                    this.m_6842_(true);
                }
            } else {
                this.m_6842_(this.m_21023_(MobEffects.INVISIBILITY));
                this.setDaytimeCounter(0);
            }
        } else if (this.getAnimation() == ANIMATION_SCARE && this.getAnimationTick() == 3 && !this.isHauntedShoppingList() && this.f_19796_.nextInt(3) == 0) {
            this.m_5496_(IafSoundRegistry.GHOST_JUMPSCARE, this.m_6121_(), this.m_6100_());
            if (this.m_9236_().isClientSide) {
                IceAndFire.PROXY.spawnParticle(EnumParticles.Ghost_Appearance, this.m_20185_(), this.m_20186_(), this.m_20189_(), (double) this.m_19879_(), 0.0, 0.0);
            }
        }
        if (this.getAnimation() == ANIMATION_HIT && this.m_5448_() != null && (double) this.m_20270_(this.m_5448_()) < 1.4 && this.getAnimationTick() >= 4 && this.getAnimationTick() < 6) {
            this.m_5496_(IafSoundRegistry.GHOST_ATTACK, this.m_6121_(), this.m_6100_());
            this.m_7327_(this.m_5448_());
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public boolean isNoAi() {
        return this.isDaytimeMode() || super.m_21525_();
    }

    @Override
    public boolean isSilent() {
        return this.isDaytimeMode() || super.m_20067_();
    }

    @Override
    protected boolean isSunBurnTick() {
        if (this.m_9236_().isDay() && !this.m_9236_().isClientSide) {
            float f = (float) this.m_9236_().m_45517_(LightLayer.BLOCK, this.m_20183_());
            BlockPos blockpos = this.m_20202_() instanceof Boat ? new BlockPos(this.m_146903_(), this.m_146904_(), this.m_146907_()).above() : new BlockPos(this.m_146903_(), this.m_146904_() + 4, this.m_146907_());
            return f > 0.5F && this.m_9236_().m_45527_(blockpos);
        } else {
            return false;
        }
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @NotNull
    @Override
    public InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        if (itemstack != null && itemstack.getItem() == IafItemRegistry.MANUSCRIPT.get() && !this.isHauntedShoppingList()) {
            this.setColor(-1);
            this.m_5496_(IafSoundRegistry.BESTIARY_PAGE, 1.0F, 1.0F);
            if (!player.isCreative()) {
                itemstack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        } else {
            return super.m_6071_(player, hand);
        }
    }

    @Override
    public void travel(@NotNull Vec3 vec) {
        if (this.isDaytimeMode()) {
            super.m_7023_(Vec3.ZERO);
        } else {
            super.m_7023_(vec);
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        spawnDataIn = super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setColor(this.f_19796_.nextInt(3));
        if (this.f_19796_.nextInt(200) == 0) {
            this.setColor(-1);
        }
        return spawnDataIn;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.m_20088_().define(COLOR, 0);
        this.m_20088_().define(CHARGING, false);
        this.m_20088_().define(IS_DAYTIME_MODE, false);
        this.m_20088_().define(WAS_FROM_CHEST, false);
        this.m_20088_().define(DAYTIME_COUNTER, 0);
    }

    public int getColor() {
        return Mth.clamp(this.m_20088_().get(COLOR), -1, 2);
    }

    public void setColor(int color) {
        this.m_20088_().set(COLOR, color);
    }

    public int getDaytimeCounter() {
        return this.m_20088_().get(DAYTIME_COUNTER);
    }

    public void setDaytimeCounter(int counter) {
        this.m_20088_().set(DAYTIME_COUNTER, counter);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.m_7378_(compound);
        this.setColor(compound.getInt("Color"));
        this.setDaytimeMode(compound.getBoolean("DaytimeMode"));
        this.setDaytimeCounter(compound.getInt("DaytimeCounter"));
        this.setFromChest(compound.getBoolean("FromChest"));
        this.setConfigurableAttributes();
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.m_7380_(compound);
        compound.putInt("Color", this.getColor());
        compound.putBoolean("DaytimeMode", this.isDaytimeMode());
        compound.putInt("DaytimeCounter", this.getDaytimeCounter());
        compound.putBoolean("FromChest", this.wasFromChest());
    }

    public boolean isHauntedShoppingList() {
        return this.getColor() == -1;
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
        return new Animation[] { NO_ANIMATION, ANIMATION_SCARE, ANIMATION_HIT };
    }

    @Override
    public boolean shouldAnimalsFear(Entity entity) {
        return false;
    }

    class MoveHelper extends MoveControl {

        EntityGhost ghost;

        public MoveHelper(EntityGhost ghost) {
            super(ghost);
            this.ghost = ghost;
        }

        @Override
        public void tick() {
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                Vec3 vec3d = new Vec3(this.m_25000_() - this.ghost.m_20185_(), this.m_25001_() - this.ghost.m_20186_(), this.m_25002_() - this.ghost.m_20189_());
                double d0 = vec3d.length();
                double edgeLength = this.ghost.m_20191_().getSize();
                if (d0 < edgeLength) {
                    this.f_24981_ = MoveControl.Operation.WAIT;
                    this.ghost.m_20256_(this.ghost.m_20184_().scale(0.5));
                } else {
                    this.ghost.m_20256_(this.ghost.m_20184_().add(vec3d.scale(this.f_24978_ * 0.5 * 0.05 / d0)));
                    if (this.ghost.m_5448_() == null) {
                        Vec3 vec3d1 = this.ghost.m_20184_();
                        this.ghost.m_146922_(-((float) Mth.atan2(vec3d1.x, vec3d1.z)) * (180.0F / (float) Math.PI));
                        this.ghost.f_20883_ = this.ghost.m_146908_();
                    } else {
                        double d4 = this.ghost.m_5448_().m_20185_() - this.ghost.m_20185_();
                        double d5 = this.ghost.m_5448_().m_20189_() - this.ghost.m_20189_();
                        this.ghost.m_146922_(-((float) Mth.atan2(d4, d5)) * (180.0F / (float) Math.PI));
                        this.ghost.f_20883_ = this.ghost.m_146908_();
                    }
                }
            }
        }
    }
}