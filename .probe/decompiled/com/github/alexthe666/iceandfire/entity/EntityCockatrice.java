package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.datagen.tags.IafItemTags;
import com.github.alexthe666.iceandfire.entity.ai.CockatriceAIAggroLook;
import com.github.alexthe666.iceandfire.entity.ai.CockatriceAIFollowOwner;
import com.github.alexthe666.iceandfire.entity.ai.CockatriceAIStareAttack;
import com.github.alexthe666.iceandfire.entity.ai.CockatriceAITarget;
import com.github.alexthe666.iceandfire.entity.ai.CockatriceAITargetItems;
import com.github.alexthe666.iceandfire.entity.ai.CockatriceAIWander;
import com.github.alexthe666.iceandfire.entity.ai.EntityAIAttackMeleeNoCooldown;
import com.github.alexthe666.iceandfire.entity.ai.IAFLookHelper;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.HomePosition;
import com.github.alexthe666.iceandfire.entity.util.IBlacklistedFromStatues;
import com.github.alexthe666.iceandfire.entity.util.IHasCustomizableAttributes;
import com.github.alexthe666.iceandfire.entity.util.IVillagerFear;
import com.github.alexthe666.iceandfire.event.ServerEvents;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.google.common.base.Predicate;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class EntityCockatrice extends TamableAnimal implements IAnimatedEntity, IBlacklistedFromStatues, IVillagerFear, IHasCustomizableAttributes {

    public static final Animation ANIMATION_JUMPAT = Animation.create(30);

    public static final Animation ANIMATION_WATTLESHAKE = Animation.create(20);

    public static final Animation ANIMATION_BITE = Animation.create(15);

    public static final Animation ANIMATION_SPEAK = Animation.create(10);

    public static final Animation ANIMATION_EAT = Animation.create(20);

    public static final float VIEW_RADIUS = 0.6F;

    private static final EntityDataAccessor<Boolean> HEN = SynchedEntityData.defineId(EntityCockatrice.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> STARING = SynchedEntityData.defineId(EntityCockatrice.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> TARGET_ENTITY = SynchedEntityData.defineId(EntityCockatrice.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> TAMING_PLAYER = SynchedEntityData.defineId(EntityCockatrice.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> TAMING_LEVEL = SynchedEntityData.defineId(EntityCockatrice.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.defineId(EntityCockatrice.class, EntityDataSerializers.INT);

    private final CockatriceAIStareAttack aiStare;

    private final MeleeAttackGoal aiMelee;

    public float sitProgress;

    public float stareProgress;

    public int ticksStaring = 0;

    public HomePosition homePos;

    public boolean hasHomePosition = false;

    private int animationTick;

    private Animation currentAnimation;

    private boolean isSitting;

    private boolean isStaring;

    private boolean isMeleeMode = false;

    private LivingEntity targetedEntity;

    private int clientSideAttackTime;

    public EntityCockatrice(EntityType<EntityCockatrice> type, Level worldIn) {
        super(type, worldIn);
        this.f_21365_ = new IAFLookHelper(this);
        this.aiStare = new CockatriceAIStareAttack(this, 1.0, 0, 15.0F);
        this.aiMelee = new EntityAIAttackMeleeNoCooldown(this, 1.5, false);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, IafConfig.cockatriceMaxHealth).add(Attributes.MOVEMENT_SPEED, 0.4).add(Attributes.ATTACK_DAMAGE, 5.0).add(Attributes.FOLLOW_RANGE, 64.0).add(Attributes.ARMOR, 2.0);
    }

    @Override
    public void setConfigurableAttributes() {
        this.m_21051_(Attributes.MAX_HEALTH).setBaseValue(IafConfig.cockatriceMaxHealth);
    }

    @Override
    public int getExperienceReward() {
        return 10;
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new FloatGoal(this));
        this.f_21345_.addGoal(3, new CockatriceAIFollowOwner(this, 1.0, 7.0F, 2.0F));
        this.f_21345_.addGoal(3, new SitWhenOrderedToGoal(this));
        this.f_21345_.addGoal(3, new AvoidEntityGoal(this, LivingEntity.class, 14.0F, 1.0, 1.0, new Predicate<LivingEntity>() {

            public boolean apply(@Nullable LivingEntity entity) {
                return entity instanceof Player ? !((Player) entity).isCreative() && !entity.m_5833_() : ServerEvents.doesScareCockatrice(entity) && !ServerEvents.isChicken(entity);
            }
        }));
        this.f_21345_.addGoal(4, new CockatriceAIWander(this, 1.0));
        this.f_21345_.addGoal(5, new CockatriceAIAggroLook(this));
        this.f_21345_.addGoal(6, new LookAtPlayerGoal(this, LivingEntity.class, 6.0F));
        this.f_21345_.addGoal(7, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new CockatriceAITargetItems(this, false));
        this.f_21346_.addGoal(2, new OwnerHurtByTargetGoal(this));
        this.f_21346_.addGoal(3, new OwnerHurtTargetGoal(this));
        this.f_21346_.addGoal(4, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(5, new CockatriceAITarget(this, LivingEntity.class, true, new Predicate<Entity>() {

            public boolean apply(@Nullable Entity entity) {
                return entity instanceof Player ? !((Player) entity).isCreative() && !entity.isSpectator() : entity instanceof Enemy && EntityCockatrice.this.m_21824_() && !(entity instanceof Creeper) && !(entity instanceof ZombifiedPiglin) && !(entity instanceof EnderMan) || ServerEvents.isCockatriceTarget(entity) && !ServerEvents.isChicken(entity);
            }
        }));
    }

    @Override
    public boolean hasRestriction() {
        return this.hasHomePosition && this.getCommand() == 3 && this.getHomeDimensionName().equals(DragonUtils.getDimensionName(this.m_9236_())) || super.m_21536_();
    }

    @NotNull
    @Override
    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    @NotNull
    @Override
    public BlockPos getRestrictCenter() {
        return this.hasHomePosition && this.getCommand() == 3 && this.homePos != null ? this.homePos.getPosition() : super.m_21534_();
    }

    @Override
    public float getRestrictRadius() {
        return 30.0F;
    }

    public String getHomeDimensionName() {
        return this.homePos == null ? "" : this.homePos.getDimension();
    }

    @Override
    public boolean isAlliedTo(@NotNull Entity entityIn) {
        if (ServerEvents.isChicken(entityIn)) {
            return true;
        } else {
            if (this.m_21824_()) {
                LivingEntity livingentity = this.m_269323_();
                if (entityIn == livingentity) {
                    return true;
                }
                if (entityIn instanceof TamableAnimal) {
                    return ((TamableAnimal) entityIn).isOwnedBy(livingentity);
                }
                if (livingentity != null) {
                    return livingentity.m_7307_(entityIn);
                }
            }
            return super.isAlliedTo(entityIn);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (source.getEntity() != null && ServerEvents.doesScareCockatrice(source.getEntity())) {
            damage *= 5.0F;
        }
        return source == this.m_9236_().damageSources().inWall() ? false : super.m_6469_(source, damage);
    }

    private boolean canUseStareOn(Entity entity) {
        return (!(entity instanceof IBlacklistedFromStatues) || ((IBlacklistedFromStatues) entity).canBeTurnedToStone()) && !ServerEvents.isCockatriceTarget(entity);
    }

    private void switchAI(boolean melee) {
        if (melee) {
            this.f_21345_.removeGoal(this.aiStare);
            if (this.aiMelee != null) {
                this.f_21345_.addGoal(2, this.aiMelee);
            }
            this.isMeleeMode = true;
        } else {
            this.f_21345_.removeGoal(this.aiMelee);
            if (this.aiStare != null) {
                this.f_21345_.addGoal(2, this.aiStare);
            }
            this.isMeleeMode = false;
        }
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entityIn) {
        if (this.isStaring()) {
            return false;
        } else if (this.m_217043_().nextBoolean()) {
            if (this.getAnimation() != ANIMATION_JUMPAT && this.getAnimation() != ANIMATION_BITE) {
                this.setAnimation(ANIMATION_JUMPAT);
            }
            return false;
        } else {
            if (this.getAnimation() != ANIMATION_BITE && this.getAnimation() != ANIMATION_JUMPAT) {
                this.setAnimation(ANIMATION_BITE);
            }
            return false;
        }
    }

    public boolean canMove() {
        return !this.isOrderedToSit() && (this.getAnimation() != ANIMATION_JUMPAT || this.getAnimationTick() >= 7);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(HEN, Boolean.FALSE);
        this.f_19804_.define(STARING, Boolean.FALSE);
        this.f_19804_.define(TARGET_ENTITY, 0);
        this.f_19804_.define(TAMING_PLAYER, 0);
        this.f_19804_.define(TAMING_LEVEL, 0);
        this.f_19804_.define(COMMAND, 0);
    }

    public boolean hasTargetedEntity() {
        return this.f_19804_.get(TARGET_ENTITY) != 0;
    }

    public boolean hasTamingPlayer() {
        return this.f_19804_.get(TAMING_PLAYER) != 0;
    }

    @Nullable
    public Entity getTamingPlayer() {
        if (!this.hasTamingPlayer()) {
            return null;
        } else if (this.m_9236_().isClientSide) {
            if (this.targetedEntity != null) {
                return this.targetedEntity;
            } else {
                Entity entity = this.m_9236_().getEntity(this.f_19804_.get(TAMING_PLAYER));
                if (entity instanceof LivingEntity) {
                    this.targetedEntity = (LivingEntity) entity;
                    return this.targetedEntity;
                } else {
                    return null;
                }
            }
        } else {
            return this.m_9236_().getEntity(this.f_19804_.get(TAMING_PLAYER));
        }
    }

    public void setTamingPlayer(int entityId) {
        this.f_19804_.set(TAMING_PLAYER, entityId);
    }

    @Nullable
    public LivingEntity getTargetedEntity() {
        boolean blindness = this.m_21023_(MobEffects.BLINDNESS) || this.m_5448_() != null && this.m_5448_().hasEffect(MobEffects.BLINDNESS) || EntityGorgon.isBlindfolded(this.m_5448_());
        if (blindness) {
            return null;
        } else if (!this.hasTargetedEntity()) {
            return null;
        } else if (this.m_9236_().isClientSide) {
            if (this.targetedEntity != null) {
                return this.targetedEntity;
            } else {
                Entity entity = this.m_9236_().getEntity(this.f_19804_.get(TARGET_ENTITY));
                if (entity instanceof LivingEntity) {
                    this.targetedEntity = (LivingEntity) entity;
                    return this.targetedEntity;
                } else {
                    return null;
                }
            }
        } else {
            return this.m_5448_();
        }
    }

    public void setTargetedEntity(int entityId) {
        this.f_19804_.set(TARGET_ENTITY, entityId);
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> key) {
        super.m_7350_(key);
        if (TARGET_ENTITY.equals(key)) {
            this.clientSideAttackTime = 0;
            this.targetedEntity = null;
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Hen", this.isHen());
        tag.putBoolean("Staring", this.isStaring());
        tag.putInt("TamingLevel", this.getTamingLevel());
        tag.putInt("TamingPlayer", this.f_19804_.get(TAMING_PLAYER));
        tag.putInt("Command", this.getCommand());
        tag.putBoolean("HasHomePosition", this.hasHomePosition);
        if (this.homePos != null && this.hasHomePosition) {
            this.homePos.write(tag);
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setHen(tag.getBoolean("Hen"));
        this.setStaring(tag.getBoolean("Staring"));
        this.setTamingLevel(tag.getInt("TamingLevel"));
        this.setTamingPlayer(tag.getInt("TamingPlayer"));
        this.setCommand(tag.getInt("Command"));
        this.hasHomePosition = tag.getBoolean("HasHomePosition");
        if (this.hasHomePosition && tag.getInt("HomeAreaX") != 0 && tag.getInt("HomeAreaY") != 0 && tag.getInt("HomeAreaZ") != 0) {
            this.homePos = new HomePosition(tag, this.m_9236_());
        }
        this.setConfigurableAttributes();
    }

    @Override
    public boolean isOrderedToSit() {
        if (this.m_9236_().isClientSide) {
            boolean isSitting = (this.f_19804_.<Byte>get(f_21798_) & 1) != 0;
            this.isSitting = isSitting;
            return isSitting;
        } else {
            return this.isSitting;
        }
    }

    @Override
    public void setOrderedToSit(boolean sitting) {
        super.m_20282_(sitting);
        if (!this.m_9236_().isClientSide) {
            this.isSitting = sitting;
        }
    }

    public void fall(float distance, float damageMultiplier) {
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        spawnDataIn = super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setHen(this.m_217043_().nextBoolean());
        return spawnDataIn;
    }

    public boolean isHen() {
        return this.f_19804_.get(HEN);
    }

    public void setHen(boolean hen) {
        this.f_19804_.set(HEN, hen);
    }

    public int getTamingLevel() {
        return this.f_19804_.get(TAMING_LEVEL);
    }

    public void setTamingLevel(int level) {
        this.f_19804_.set(TAMING_LEVEL, level);
    }

    public int getCommand() {
        return this.f_19804_.get(COMMAND);
    }

    public void setCommand(int command) {
        this.f_19804_.set(COMMAND, command);
        this.setOrderedToSit(command == 1);
    }

    public boolean isStaring() {
        return this.m_9236_().isClientSide ? (this.isStaring = this.f_19804_.get(STARING)) : this.isStaring;
    }

    public void setStaring(boolean staring) {
        this.f_19804_.set(STARING, staring);
        if (!this.m_9236_().isClientSide) {
            this.isStaring = staring;
        }
    }

    public void forcePreyToLook(Mob mob) {
        mob.getLookControl().setLookAt(this.m_20185_(), this.m_20186_() + (double) this.m_20192_(), this.m_20189_(), (float) mob.getMaxHeadYRot(), (float) mob.getMaxHeadXRot());
    }

    @NotNull
    @Override
    public InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack stackInHand = player.m_21120_(hand);
        Item itemInHand = stackInHand.getItem();
        if (stackInHand.getItem() != Items.NAME_TAG && itemInHand != Items.LEAD && itemInHand != Items.POISONOUS_POTATO) {
            if (this.m_21824_() && this.m_21830_(player)) {
                if (stackInHand.is(IafItemTags.HEAL_COCKATRICE)) {
                    if (this.m_21223_() < this.m_21233_()) {
                        this.m_5634_(8.0F);
                        this.m_5496_(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
                        stackInHand.shrink(1);
                    }
                    return InteractionResult.SUCCESS;
                }
                if (stackInHand.isEmpty()) {
                    if (player.m_6144_()) {
                        if (this.hasHomePosition) {
                            this.hasHomePosition = false;
                            player.displayClientMessage(Component.translatable("cockatrice.command.remove_home"), true);
                            return InteractionResult.SUCCESS;
                        }
                        BlockPos pos = this.m_20183_();
                        this.homePos = new HomePosition(pos, this.m_9236_());
                        this.hasHomePosition = true;
                        player.displayClientMessage(Component.translatable("cockatrice.command.new_home", pos.m_123341_(), pos.m_123342_(), pos.m_123343_(), this.homePos.getDimension()), true);
                        return InteractionResult.SUCCESS;
                    }
                    this.setCommand(this.getCommand() + 1);
                    if (this.getCommand() > 3) {
                        this.setCommand(0);
                    }
                    player.displayClientMessage(Component.translatable("cockatrice.command." + this.getCommand()), true);
                    this.m_5496_(SoundEvents.ZOMBIE_INFECT, 1.0F, 1.0F);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.FAIL;
        } else {
            return super.m_6071_(player, hand);
        }
    }

    @Override
    public void aiStep() {
        super.m_8107_();
        LivingEntity attackTarget = this.m_5448_();
        if (this.m_9236_().m_46791_() == Difficulty.PEACEFUL && attackTarget instanceof Player) {
            this.m_6710_(null);
        }
        if (this.isOrderedToSit() && this.getCommand() != 1) {
            this.setOrderedToSit(false);
        }
        if (this.isOrderedToSit() && attackTarget != null) {
            this.m_6710_(null);
        }
        if (attackTarget != null && this.isAlliedTo(attackTarget)) {
            this.m_6710_(null);
        }
        if (!this.m_9236_().isClientSide) {
            if (attackTarget == null || !attackTarget.isAlive()) {
                this.setTargetedEntity(0);
            } else if (this.isStaring() || this.shouldStareAttack(attackTarget)) {
                this.setTargetedEntity(attackTarget.m_19879_());
            }
        }
        if (this.getAnimation() == ANIMATION_BITE && attackTarget != null && this.getAnimationTick() == 7) {
            double dist = this.m_20280_(attackTarget);
            if (dist < 8.0) {
                attackTarget.hurt(this.m_9236_().damageSources().mobAttack(this), (float) ((int) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue()));
            }
        }
        if (this.getAnimation() == ANIMATION_JUMPAT && attackTarget != null) {
            double dist = this.m_20280_(attackTarget);
            double d0 = attackTarget.m_20185_() - this.m_20185_();
            double d1 = attackTarget.m_20189_() - this.m_20189_();
            float leap = Mth.sqrt((float) (d0 * d0 + d1 * d1));
            if (dist < 4.0 && this.getAnimationTick() > 10) {
                attackTarget.hurt(this.m_9236_().damageSources().mobAttack(this), (float) ((int) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue()));
                if ((double) leap >= 1.0E-4) {
                    attackTarget.m_20256_(attackTarget.m_20184_().add(d0 / (double) leap * 0.8F + this.m_20184_().x * 0.2F, 0.0, d1 / (double) leap * 0.8F + this.m_20184_().z * 0.2F));
                }
            }
        }
        boolean sitting = this.isOrderedToSit();
        if (sitting && this.sitProgress < 20.0F) {
            this.sitProgress += 0.5F;
        } else if (!sitting && this.sitProgress > 0.0F) {
            this.sitProgress -= 0.5F;
        }
        boolean staring = this.isStaring();
        if (staring && this.stareProgress < 20.0F) {
            this.stareProgress += 0.5F;
        } else if (!staring && this.stareProgress > 0.0F) {
            this.stareProgress -= 0.5F;
        }
        if (!this.m_9236_().isClientSide) {
            if (staring) {
                this.ticksStaring++;
            } else {
                this.ticksStaring = 0;
            }
        }
        if (!this.m_9236_().isClientSide && staring && (attackTarget == null || this.shouldMelee())) {
            this.setStaring(false);
        }
        if (attackTarget != null) {
            this.m_21563_().setLookAt(attackTarget.m_20185_(), attackTarget.m_20186_() + (double) attackTarget.m_20192_(), attackTarget.m_20189_(), (float) this.m_8085_(), (float) this.m_8132_());
            if (!this.shouldMelee() && attackTarget instanceof Mob) {
                this.forcePreyToLook((Mob) attackTarget);
            }
        }
        boolean blindness = this.m_21023_(MobEffects.BLINDNESS) || attackTarget != null && attackTarget.hasEffect(MobEffects.BLINDNESS);
        if (blindness) {
            this.setStaring(false);
        }
        if (!this.m_9236_().isClientSide && !blindness && attackTarget != null && EntityGorgon.isEntityLookingAt(this, attackTarget, 0.6F) && EntityGorgon.isEntityLookingAt(attackTarget, this, 0.6F) && !EntityGorgon.isBlindfolded(attackTarget) && !this.shouldMelee()) {
            if (!this.isStaring()) {
                this.setStaring(true);
            } else {
                int attackStrength = this.getFriendsCount(attackTarget);
                if (this.m_9236_().m_46791_() == Difficulty.HARD) {
                    attackStrength++;
                }
                attackTarget.addEffect(new MobEffectInstance(MobEffects.WITHER, 10, 2 + Math.min(1, attackStrength)));
                attackTarget.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, Math.min(4, attackStrength)));
                attackTarget.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
                if (attackStrength >= 2 && attackTarget.f_19797_ % 40 == 0) {
                    attackTarget.hurt(this.m_9236_().damageSources().wither(), (float) (attackStrength - 1));
                }
                attackTarget.setLastHurtByMob(this);
                if (!this.m_21824_() && attackTarget instanceof Player) {
                    this.setTamingPlayer(attackTarget.m_19879_());
                    this.setTamingLevel(this.getTamingLevel() + 1);
                    if (this.getTamingLevel() % 100 == 0) {
                        this.m_9236_().broadcastEntityEvent(this, (byte) 46);
                    }
                    if (this.getTamingLevel() >= 1000) {
                        this.m_9236_().broadcastEntityEvent(this, (byte) 45);
                        if (this.getTamingPlayer() instanceof Player) {
                            this.m_21828_((Player) this.getTamingPlayer());
                        }
                        this.m_6710_(null);
                        this.setTamingPlayer(0);
                        this.setTargetedEntity(0);
                    }
                }
            }
        }
        if (!this.m_9236_().isClientSide && attackTarget == null && this.m_217043_().nextInt(300) == 0 && this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_WATTLESHAKE);
        }
        if (!this.m_9236_().isClientSide) {
            if (this.shouldMelee() && !this.isMeleeMode) {
                this.switchAI(true);
            }
            if (!this.shouldMelee() && this.isMeleeMode) {
                this.switchAI(false);
            }
        }
        if (this.m_9236_().isClientSide && this.getTargetedEntity() != null && EntityGorgon.isEntityLookingAt(this, this.getTargetedEntity(), 0.6F) && EntityGorgon.isEntityLookingAt(this.getTargetedEntity(), this, 0.6F) && this.isStaring() && this.hasTargetedEntity()) {
            if (this.clientSideAttackTime < this.getAttackDuration()) {
                this.clientSideAttackTime++;
            }
            LivingEntity livingEntity = this.getTargetedEntity();
            if (livingEntity != null) {
                this.m_21563_().setLookAt(livingEntity, 90.0F, 90.0F);
                this.m_21563_().tick();
                double d5 = (double) this.getAttackAnimationScale(0.0F);
                double d0 = livingEntity.m_20185_() - this.m_20185_();
                double d1 = livingEntity.m_20186_() + (double) (livingEntity.m_20206_() * 0.5F) - (this.m_20186_() + (double) this.m_20192_());
                double d2 = livingEntity.m_20189_() - this.m_20189_();
                double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                d0 /= d3;
                d1 /= d3;
                d2 /= d3;
                double d4 = this.f_19796_.nextDouble();
                while (d4 < d3) {
                    d4 += 1.8 - d5 + this.f_19796_.nextDouble() * (1.7 - d5);
                    this.m_9236_().addParticle(ParticleTypes.ENTITY_EFFECT, this.m_20185_() + d0 * d4, this.m_20186_() + d1 * d4 + (double) this.m_20192_(), this.m_20189_() + d2 * d4, 0.0, 0.0, 0.0);
                }
            }
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    private int getFriendsCount(LivingEntity attackTarget) {
        if (this.m_5448_() == null) {
            return 0;
        } else {
            float dist = (float) IafConfig.cockatriceChickenSearchLength;
            List<EntityCockatrice> list = this.m_9236_().m_45976_(EntityCockatrice.class, this.m_20191_().expandTowards((double) dist, (double) dist, (double) dist));
            int i = 0;
            for (EntityCockatrice cockatrice : list) {
                if (!cockatrice.m_7306_(this) && cockatrice.m_5448_() != null && cockatrice.m_5448_() == this.m_5448_()) {
                    boolean bothLooking = EntityGorgon.isEntityLookingAt(cockatrice, cockatrice.m_5448_(), 0.6F) && EntityGorgon.isEntityLookingAt(cockatrice.m_5448_(), cockatrice, 0.6F);
                    if (bothLooking) {
                        i++;
                    }
                }
            }
            return i;
        }
    }

    public float getAttackAnimationScale(float f) {
        return ((float) this.clientSideAttackTime + f) / (float) this.getAttackDuration();
    }

    public boolean shouldStareAttack(Entity entity) {
        return this.m_20270_(entity) > 5.0F;
    }

    public int getAttackDuration() {
        return 80;
    }

    private boolean shouldMelee() {
        boolean blindness = this.m_21023_(MobEffects.BLINDNESS) || this.m_5448_() != null && this.m_5448_().hasEffect(MobEffects.BLINDNESS);
        return this.m_5448_() == null ? false : (double) this.m_20270_(this.m_5448_()) < 4.0 || ServerEvents.isCockatriceTarget(this.m_5448_()) || blindness || !this.canUseStareOn(this.m_5448_());
    }

    @Override
    public void travel(@NotNull Vec3 motionVec) {
        if (!this.canMove() && !this.m_20160_()) {
            motionVec = motionVec.multiply(0.0, 1.0, 0.0);
        }
        super.m_7023_(motionVec);
    }

    @Override
    public void playAmbientSound() {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_SPEAK);
        }
        super.m_8032_();
    }

    @Override
    protected void playHurtSound(@NotNull DamageSource source) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_SPEAK);
        }
        super.m_6677_(source);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverWorld, @NotNull AgeableMob ageable) {
        return null;
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
        return new Animation[] { NO_ANIMATION, ANIMATION_JUMPAT, ANIMATION_WATTLESHAKE, ANIMATION_BITE, ANIMATION_SPEAK, ANIMATION_EAT };
    }

    @Override
    public boolean canBeTurnedToStone() {
        return false;
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        return this.m_9236_().m_45547_(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.COCKATRICE_IDLE;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return IafSoundRegistry.COCKATRICE_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.COCKATRICE_DIE;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 45) {
            this.playEffect(true);
        } else if (id == 46) {
            this.playEffect(false);
        } else {
            super.handleEntityEvent(id);
        }
    }

    protected void playEffect(boolean play) {
        ParticleOptions enumparticletypes = ParticleTypes.HEART;
        if (!play) {
            enumparticletypes = ParticleTypes.DAMAGE_INDICATOR;
        }
        for (int i = 0; i < 7; i++) {
            double d0 = this.f_19796_.nextGaussian() * 0.02;
            double d1 = this.f_19796_.nextGaussian() * 0.02;
            double d2 = this.f_19796_.nextGaussian() * 0.02;
            this.m_9236_().addParticle(enumparticletypes, this.m_20185_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), this.m_20186_() + 0.5 + (double) (this.f_19796_.nextFloat() * this.m_20206_()), this.m_20189_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), d0, d1, d2);
        }
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }
}