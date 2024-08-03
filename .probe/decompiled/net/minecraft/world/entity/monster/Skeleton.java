package net.minecraft.world.entity.monster;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class Skeleton extends AbstractSkeleton {

    private static final int TOTAL_CONVERSION_TIME = 300;

    private static final EntityDataAccessor<Boolean> DATA_STRAY_CONVERSION_ID = SynchedEntityData.defineId(Skeleton.class, EntityDataSerializers.BOOLEAN);

    public static final String CONVERSION_TAG = "StrayConversionTime";

    private int inPowderSnowTime;

    private int conversionTime;

    public Skeleton(EntityType<? extends Skeleton> entityTypeExtendsSkeleton0, Level level1) {
        super(entityTypeExtendsSkeleton0, level1);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.m_20088_().define(DATA_STRAY_CONVERSION_ID, false);
    }

    public boolean isFreezeConverting() {
        return this.m_20088_().get(DATA_STRAY_CONVERSION_ID);
    }

    public void setFreezeConverting(boolean boolean0) {
        this.f_19804_.set(DATA_STRAY_CONVERSION_ID, boolean0);
    }

    @Override
    public boolean isShaking() {
        return this.isFreezeConverting();
    }

    @Override
    public void tick() {
        if (!this.m_9236_().isClientSide && this.m_6084_() && !this.m_21525_()) {
            if (this.f_146808_) {
                if (this.isFreezeConverting()) {
                    this.conversionTime--;
                    if (this.conversionTime < 0) {
                        this.doFreezeConversion();
                    }
                } else {
                    this.inPowderSnowTime++;
                    if (this.inPowderSnowTime >= 140) {
                        this.startFreezeConversion(300);
                    }
                }
            } else {
                this.inPowderSnowTime = -1;
                this.setFreezeConverting(false);
            }
        }
        super.m_8119_();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7380_(compoundTag0);
        compoundTag0.putInt("StrayConversionTime", this.isFreezeConverting() ? this.conversionTime : -1);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        if (compoundTag0.contains("StrayConversionTime", 99) && compoundTag0.getInt("StrayConversionTime") > -1) {
            this.startFreezeConversion(compoundTag0.getInt("StrayConversionTime"));
        }
    }

    private void startFreezeConversion(int int0) {
        this.conversionTime = int0;
        this.setFreezeConverting(true);
    }

    protected void doFreezeConversion() {
        this.m_21406_(EntityType.STRAY, true);
        if (!this.m_20067_()) {
            this.m_9236_().m_5898_(null, 1048, this.m_20183_(), 0);
        }
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }

    @Override
    SoundEvent getStepSound() {
        return SoundEvents.SKELETON_STEP;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource0, int int1, boolean boolean2) {
        super.m_7472_(damageSource0, int1, boolean2);
        if (damageSource0.getEntity() instanceof Creeper $$4 && $$4.canDropMobsSkull()) {
            $$4.increaseDroppedSkulls();
            this.m_19998_(Items.SKELETON_SKULL);
        }
    }
}