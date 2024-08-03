package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.entity.ai.DreadAITargetNonDread;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.IAnimalFear;
import com.github.alexthe666.iceandfire.entity.util.IDreadMob;
import com.github.alexthe666.iceandfire.entity.util.IHasArmorVariant;
import com.github.alexthe666.iceandfire.entity.util.IVillagerFear;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class EntityDreadThrall extends EntityDreadMob implements IAnimatedEntity, IVillagerFear, IAnimalFear, IHasArmorVariant {

    private static final EntityDataAccessor<Boolean> CUSTOM_ARMOR_HEAD = SynchedEntityData.defineId(EntityDreadThrall.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> CUSTOM_ARMOR_CHEST = SynchedEntityData.defineId(EntityDreadThrall.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> CUSTOM_ARMOR_LEGS = SynchedEntityData.defineId(EntityDreadThrall.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> CUSTOM_ARMOR_FEET = SynchedEntityData.defineId(EntityDreadThrall.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> CUSTOM_ARMOR_INDEX = SynchedEntityData.defineId(EntityDreadThrall.class, EntityDataSerializers.INT);

    public static Animation ANIMATION_SPAWN = Animation.create(40);

    private int animationTick;

    private Animation currentAnimation;

    public EntityDreadThrall(EntityType type, Level worldIn) {
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
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0).add(Attributes.MOVEMENT_SPEED, 0.2).add(Attributes.ATTACK_DAMAGE, 2.0).add(Attributes.FOLLOW_RANGE, 128.0).add(Attributes.ARMOR, 2.0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(CUSTOM_ARMOR_INDEX, 0);
        this.f_19804_.define(CUSTOM_ARMOR_HEAD, false);
        this.f_19804_.define(CUSTOM_ARMOR_CHEST, false);
        this.f_19804_.define(CUSTOM_ARMOR_LEGS, false);
        this.f_19804_.define(CUSTOM_ARMOR_FEET, false);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.getAnimation() == ANIMATION_SPAWN && this.getAnimationTick() < 30) {
            BlockState belowBlock = this.m_9236_().getBlockState(this.m_20183_().below());
            if (belowBlock.m_60734_() != Blocks.AIR) {
                for (int i = 0; i < 5; i++) {
                    this.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, belowBlock), this.m_20185_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), this.m_20191_().minY, this.m_20189_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), this.f_19796_.nextGaussian() * 0.02, this.f_19796_.nextGaussian() * 0.02, this.f_19796_.nextGaussian() * 0.02);
                }
            }
            this.m_20334_(0.0, this.m_20184_().y, 0.0);
        }
        if (this.m_21205_().getItem() == Items.BOW) {
            this.m_21008_(InteractionHand.MAIN_HAND, new ItemStack(Items.BONE));
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randomSource, @NotNull DifficultyInstance difficulty) {
        super.m_213945_(randomSource, difficulty);
        if (this.f_19796_.nextFloat() < 0.75F) {
            double chance = (double) this.f_19796_.nextFloat();
            if (chance < 0.0025F) {
                this.m_8061_(EquipmentSlot.MAINHAND, new ItemStack(IafItemRegistry.DRAGONSTEEL_ICE_SWORD.get()));
            }
            if (chance < 0.01F) {
                this.m_8061_(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
            }
            if (chance < 0.1F) {
                this.m_8061_(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
            }
            if (chance < 0.75) {
                this.m_8061_(EquipmentSlot.MAINHAND, new ItemStack(IafItemRegistry.DREAD_SWORD.get()));
            }
        }
        if (this.f_19796_.nextFloat() < 0.75F) {
            this.m_8061_(EquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
            this.setCustomArmorHead(this.f_19796_.nextInt(8) != 0);
        }
        if (this.f_19796_.nextFloat() < 0.75F) {
            this.m_8061_(EquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
            this.setCustomArmorChest(this.f_19796_.nextInt(8) != 0);
        }
        if (this.f_19796_.nextFloat() < 0.75F) {
            this.m_8061_(EquipmentSlot.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
            this.setCustomArmorLegs(this.f_19796_.nextInt(8) != 0);
        }
        if (this.f_19796_.nextFloat() < 0.75F) {
            this.m_8061_(EquipmentSlot.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
            this.setCustomArmorFeet(this.f_19796_.nextInt(8) != 0);
        }
        this.setBodyArmorVariant(this.f_19796_.nextInt(8));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        SpawnGroupData data = super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setAnimation(ANIMATION_SPAWN);
        this.populateDefaultEquipmentSlots(worldIn.m_213780_(), difficultyIn);
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
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("ArmorVariant", this.getBodyArmorVariant());
        compound.putBoolean("HasCustomHelmet", this.hasCustomArmorHead());
        compound.putBoolean("HasCustomChestplate", this.hasCustomArmorChest());
        compound.putBoolean("HasCustomLeggings", this.hasCustomArmorLegs());
        compound.putBoolean("HasCustomBoots", this.hasCustomArmorFeet());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setBodyArmorVariant(compound.getInt("ArmorVariant"));
        this.setCustomArmorHead(compound.getBoolean("HasCustomHelmet"));
        this.setCustomArmorChest(compound.getBoolean("HasCustomChestplate"));
        this.setCustomArmorLegs(compound.getBoolean("HasCustomLeggings"));
        this.setCustomArmorFeet(compound.getBoolean("HasCustomBoots"));
    }

    @Override
    public Animation getAnimation() {
        return this.currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
    }

    public boolean hasCustomArmorHead() {
        return this.f_19804_.get(CUSTOM_ARMOR_HEAD);
    }

    public void setCustomArmorHead(boolean head) {
        this.f_19804_.set(CUSTOM_ARMOR_HEAD, head);
    }

    public boolean hasCustomArmorChest() {
        return this.f_19804_.get(CUSTOM_ARMOR_CHEST);
    }

    public void setCustomArmorChest(boolean head) {
        this.f_19804_.set(CUSTOM_ARMOR_CHEST, head);
    }

    public boolean hasCustomArmorLegs() {
        return this.f_19804_.get(CUSTOM_ARMOR_LEGS);
    }

    public void setCustomArmorLegs(boolean head) {
        this.f_19804_.set(CUSTOM_ARMOR_LEGS, head);
    }

    public boolean hasCustomArmorFeet() {
        return this.f_19804_.get(CUSTOM_ARMOR_FEET);
    }

    public void setCustomArmorFeet(boolean head) {
        this.f_19804_.set(CUSTOM_ARMOR_FEET, head);
    }

    @Override
    public int getBodyArmorVariant() {
        return this.f_19804_.get(CUSTOM_ARMOR_INDEX);
    }

    @Override
    public void setBodyArmorVariant(int variant) {
        this.f_19804_.set(CUSTOM_ARMOR_INDEX, variant);
    }

    @Override
    public int getLegArmorVariant() {
        return 0;
    }

    @Override
    public void setLegArmorVariant(int variant) {
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_SPAWN };
    }

    @Override
    public boolean shouldAnimalsFear(Entity entity) {
        return true;
    }

    @Override
    public boolean shouldFear() {
        return true;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.STRAY_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return SoundEvents.STRAY_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.STRAY_DEATH;
    }

    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.m_5496_(SoundEvents.STRAY_STEP, 0.15F, 1.0F);
    }
}