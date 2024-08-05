package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.entity.ai.DreadAIRideHorse;
import com.github.alexthe666.iceandfire.entity.ai.DreadAITargetNonDread;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.IAnimalFear;
import com.github.alexthe666.iceandfire.entity.util.IDreadMob;
import com.github.alexthe666.iceandfire.entity.util.IVillagerFear;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.recipe.IafBannerPatterns;
import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
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
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BannerPatterns;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class EntityDreadKnight extends EntityDreadMob implements IAnimatedEntity, IVillagerFear, IAnimalFear {

    public static final ItemStack SHIELD = generateShield();

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityDreadKnight.class, EntityDataSerializers.INT);

    public static Animation ANIMATION_SPAWN = Animation.create(40);

    private int animationTick;

    private Animation currentAnimation;

    public EntityDreadKnight(EntityType type, Level worldIn) {
        super(type, worldIn);
    }

    private static ItemStack generateShield() {
        ItemStack itemstack = new ItemStack(Items.CYAN_BANNER);
        CompoundTag compoundnbt = itemstack.getOrCreateTagElement("BlockEntityTag");
        ListTag listnbt = new BannerPattern.Builder().addPattern(BannerPatterns.BASE, DyeColor.CYAN).addPattern(Holder.direct(IafBannerPatterns.PATTERN_DREAD.get()), DyeColor.WHITE).toListTag();
        compoundnbt.put("Patterns", listnbt);
        ItemStack shield = new ItemStack(Items.SHIELD, 1);
        shield.setTag(itemstack.getTag());
        return shield;
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 40.0).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.ATTACK_DAMAGE, 2.0).add(Attributes.FOLLOW_RANGE, 128.0).add(Attributes.ARMOR, 20.0);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new DreadAIRideHorse(this));
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

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(VARIANT, 0);
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
            this.m_20334_(0.0, this.m_20184_().y, this.m_20184_().z);
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        super.m_213945_(pRandom, pDifficulty);
        this.m_8061_(EquipmentSlot.MAINHAND, new ItemStack(IafItemRegistry.DREAD_KNIGHT_SWORD.get()));
        if (this.f_19796_.nextBoolean()) {
            this.m_8061_(EquipmentSlot.OFFHAND, SHIELD.copy());
        }
        this.setArmorVariant(this.f_19796_.nextInt(3));
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
        compound.putInt("ArmorVariant", this.getArmorVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setArmorVariant(compound.getInt("ArmorVariant"));
    }

    @Override
    public Animation getAnimation() {
        return this.currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
    }

    public int getArmorVariant() {
        return this.f_19804_.get(VARIANT);
    }

    public void setArmorVariant(int variant) {
        this.f_19804_.set(VARIANT, variant);
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

    @Override
    public double getMyRidingOffset() {
        return -0.6;
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

    @Override
    public float getVoicePitch() {
        return super.m_6100_() * 0.75F;
    }
}