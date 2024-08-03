package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.ai.DreadAITargetNonDread;
import com.github.alexthe666.iceandfire.entity.ai.DreadLichAIStrife;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.IAnimalFear;
import com.github.alexthe666.iceandfire.entity.util.IDreadMob;
import com.github.alexthe666.iceandfire.entity.util.IVillagerFear;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
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
import net.minecraft.util.Mth;
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
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class EntityDreadLich extends EntityDreadMob implements IAnimatedEntity, IVillagerFear, IAnimalFear, RangedAttackMob {

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityDreadLich.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> MINION_COUNT = SynchedEntityData.defineId(EntityDreadLich.class, EntityDataSerializers.INT);

    public static Animation ANIMATION_SPAWN = Animation.create(40);

    public static Animation ANIMATION_SUMMON = Animation.create(15);

    private final DreadLichAIStrife aiArrowAttack = new DreadLichAIStrife(this, 1.0, 20, 15.0F);

    private final MeleeAttackGoal aiAttackOnCollide = new MeleeAttackGoal(this, 1.0, false);

    private int animationTick;

    private Animation currentAnimation;

    private int fireCooldown = 0;

    private int minionCooldown = 0;

    public EntityDreadLich(EntityType<? extends EntityDreadMob> type, Level worldIn) {
        super(type, worldIn);
    }

    public static boolean canLichSpawnOn(EntityType<? extends Mob> typeIn, ServerLevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        BlockPos blockpos = pos.below();
        return reason == MobSpawnType.SPAWNER || worldIn.m_8055_(blockpos).m_60643_(worldIn, blockpos, typeIn) && randomIn.nextInt(IafConfig.lichSpawnChance) == 0;
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new FloatGoal(this));
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
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 50.0).add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.ATTACK_DAMAGE, 1.0).add(Attributes.FOLLOW_RANGE, 128.0).add(Attributes.ARMOR, 2.0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(VARIANT, 0);
        this.f_19804_.define(MINION_COUNT, 0);
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
        if (this.m_9236_().isClientSide && this.getAnimation() == ANIMATION_SUMMON) {
            double d0 = 0.0;
            double d1 = 0.0;
            double d2 = 0.0;
            float f = this.f_20883_ * (float) (Math.PI / 180.0) + Mth.cos((float) this.f_19797_ * 0.6662F) * 0.25F;
            float f1 = Mth.cos(f);
            float f2 = Mth.sin(f);
            IceAndFire.PROXY.spawnParticle(EnumParticles.Dread_Torch, this.m_20185_() + (double) f1 * 0.6, this.m_20186_() + 1.8, this.m_20189_() + (double) f2 * 0.6, d0, d1, d2);
            IceAndFire.PROXY.spawnParticle(EnumParticles.Dread_Torch, this.m_20185_() - (double) f1 * 0.6, this.m_20186_() + 1.8, this.m_20189_() - (double) f2 * 0.6, d0, d1, d2);
        }
        if (this.fireCooldown > 0) {
            this.fireCooldown--;
        }
        if (this.minionCooldown > 0) {
            this.minionCooldown--;
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        super.m_213945_(pRandom, pDifficulty);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(IafItemRegistry.LICH_STAFF.get()));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        SpawnGroupData data = super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setAnimation(ANIMATION_SPAWN);
        this.populateDefaultEquipmentSlots(worldIn.m_213780_(), difficultyIn);
        this.setVariant(this.f_19796_.nextInt(5));
        this.setCombatTask();
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
        compound.putInt("Variant", this.getVariant());
        compound.putInt("MinionCount", this.getMinionCount());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setMinionCount(compound.getInt("MinionCount"));
        this.setCombatTask();
    }

    public int getVariant() {
        return this.f_19804_.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.f_19804_.set(VARIANT, variant);
    }

    public int getMinionCount() {
        return this.f_19804_.get(MINION_COUNT);
    }

    public void setMinionCount(int minions) {
        this.f_19804_.set(MINION_COUNT, minions);
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
        return new Animation[] { ANIMATION_SPAWN, ANIMATION_SUMMON };
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
    public void setItemSlot(@NotNull EquipmentSlot slotIn, @NotNull ItemStack stack) {
        super.m_8061_(slotIn, stack);
        if (!this.m_9236_().isClientSide && slotIn == EquipmentSlot.MAINHAND) {
            this.setCombatTask();
        }
    }

    public void setCombatTask() {
        if (this.m_9236_() != null && !this.m_9236_().isClientSide) {
            this.f_21345_.removeGoal(this.aiAttackOnCollide);
            this.f_21345_.removeGoal(this.aiArrowAttack);
            ItemStack itemstack = this.m_21205_();
            if (itemstack.getItem() == IafItemRegistry.LICH_STAFF.get()) {
                int i = 100;
                this.aiArrowAttack.setAttackCooldown(i);
                this.f_21345_.addGoal(4, this.aiArrowAttack);
            } else {
                this.f_21345_.addGoal(4, this.aiAttackOnCollide);
            }
        }
    }

    @Override
    public void performRangedAttack(@NotNull LivingEntity target, float distanceFactor) {
        boolean flag = false;
        if (this.getMinionCount() < 5 && this.minionCooldown == 0) {
            this.setAnimation(ANIMATION_SUMMON);
            this.m_5496_(IafSoundRegistry.DREAD_LICH_SUMMON, this.m_6121_(), this.m_6100_());
            Mob minion = this.getRandomNewMinion();
            int x = (int) this.m_20185_() - 5 + this.f_19796_.nextInt(10);
            int z = (int) this.m_20189_() - 5 + this.f_19796_.nextInt(10);
            double y = this.getHeightFromXZ(x, z);
            minion.m_7678_((double) x + 0.5, y, (double) z + 0.5, this.m_146908_(), this.m_146909_());
            minion.setTarget(target);
            Level currentLevel = this.m_9236_();
            if (currentLevel instanceof ServerLevelAccessor) {
                minion.finalizeSpawn((ServerLevelAccessor) currentLevel, currentLevel.getCurrentDifficultyAt(this.m_20183_()), MobSpawnType.MOB_SUMMONED, null, null);
            }
            if (minion instanceof EntityDreadMob) {
                ((EntityDreadMob) minion).setCommanderId(this.m_20148_());
            }
            if (!currentLevel.isClientSide) {
                currentLevel.m_7967_(minion);
            }
            this.minionCooldown = 100;
            this.setMinionCount(this.getMinionCount() + 1);
            flag = true;
        }
        if (this.fireCooldown == 0 && !flag) {
            this.m_6674_(InteractionHand.MAIN_HAND);
            this.m_5496_(SoundEvents.ZOMBIE_INFECT, this.m_6121_(), this.m_6100_());
            EntityDreadLichSkull skull = new EntityDreadLichSkull(IafEntityRegistry.DREAD_LICH_SKULL.get(), this.m_9236_(), this, 6.0);
            double d0 = target.m_20185_() - this.m_20185_();
            double d1 = target.m_20191_().minY + (double) (target.m_20206_() * 2.0F) - skull.m_20186_();
            double d2 = target.m_20189_() - this.m_20189_();
            double d3 = Math.sqrt((double) ((float) (d0 * d0 + d2 * d2)));
            skull.m_6686_(d0, d1 + d3 * 0.2F, d2, 0.0F, (float) (14 - this.m_9236_().m_46791_().getId() * 4));
            this.m_9236_().m_7967_(skull);
            this.fireCooldown = 100;
        }
    }

    private Mob getRandomNewMinion() {
        float chance = this.f_19796_.nextFloat();
        if (chance > 0.5F) {
            return new EntityDreadThrall(IafEntityRegistry.DREAD_THRALL.get(), this.m_9236_());
        } else if (chance > 0.35F) {
            return new EntityDreadGhoul(IafEntityRegistry.DREAD_GHOUL.get(), this.m_9236_());
        } else {
            return (Mob) (chance > 0.15F ? new EntityDreadBeast(IafEntityRegistry.DREAD_BEAST.get(), this.m_9236_()) : new EntityDreadScuttler(IafEntityRegistry.DREAD_SCUTTLER.get(), this.m_9236_()));
        }
    }

    private double getHeightFromXZ(int x, int z) {
        BlockPos thisPos = new BlockPos(x, (int) (this.m_20186_() + 7.0), z);
        while (this.m_9236_().m_46859_(thisPos) && thisPos.m_123342_() > 2) {
            thisPos = thisPos.below();
        }
        return (double) thisPos.m_123342_() + 1.0;
    }

    @Override
    public boolean isAlliedTo(Entity entityIn) {
        return entityIn instanceof IDreadMob || super.isAlliedTo(entityIn);
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