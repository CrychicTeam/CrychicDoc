package net.minecraft.world.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public class WitherSkeleton extends AbstractSkeleton {

    public WitherSkeleton(EntityType<? extends WitherSkeleton> entityTypeExtendsWitherSkeleton0, Level level1) {
        super(entityTypeExtendsWitherSkeleton0, level1);
        this.m_21441_(BlockPathTypes.LAVA, 8.0F);
    }

    @Override
    protected void registerGoals() {
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, AbstractPiglin.class, true));
        super.registerGoals();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.WITHER_SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.WITHER_SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WITHER_SKELETON_DEATH;
    }

    @Override
    SoundEvent getStepSound() {
        return SoundEvents.WITHER_SKELETON_STEP;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource0, int int1, boolean boolean2) {
        super.m_7472_(damageSource0, int1, boolean2);
        if (damageSource0.getEntity() instanceof Creeper $$4 && $$4.canDropMobsSkull()) {
            $$4.increaseDroppedSkulls();
            this.m_19998_(Items.WITHER_SKELETON_SKULL);
        }
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randomSource0, DifficultyInstance difficultyInstance1) {
        this.m_8061_(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
    }

    @Override
    protected void populateDefaultEquipmentEnchantments(RandomSource randomSource0, DifficultyInstance difficultyInstance1) {
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor0, DifficultyInstance difficultyInstance1, MobSpawnType mobSpawnType2, @Nullable SpawnGroupData spawnGroupData3, @Nullable CompoundTag compoundTag4) {
        SpawnGroupData $$5 = super.finalizeSpawn(serverLevelAccessor0, difficultyInstance1, mobSpawnType2, spawnGroupData3, compoundTag4);
        this.m_21051_(Attributes.ATTACK_DAMAGE).setBaseValue(4.0);
        this.m_32164_();
        return $$5;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return 2.1F;
    }

    @Override
    public boolean doHurtTarget(Entity entity0) {
        if (!super.m_7327_(entity0)) {
            return false;
        } else {
            if (entity0 instanceof LivingEntity) {
                ((LivingEntity) entity0).addEffect(new MobEffectInstance(MobEffects.WITHER, 200), this);
            }
            return true;
        }
    }

    @Override
    protected AbstractArrow getArrow(ItemStack itemStack0, float float1) {
        AbstractArrow $$2 = super.getArrow(itemStack0, float1);
        $$2.m_20254_(100);
        return $$2;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance mobEffectInstance0) {
        return mobEffectInstance0.getEffect() == MobEffects.WITHER ? false : super.m_7301_(mobEffectInstance0);
    }
}