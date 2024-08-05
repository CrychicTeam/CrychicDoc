package com.sihenzhang.crockpot.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.PowerableMob;

public interface ChargeableMob extends PowerableMob {

    String TAG_CHARGE_TIME = "ChargeTime";

    int getRemainingPersistentChargeTime();

    void setRemainingPersistentChargeTime(int var1);

    void startPersistentChargeTimer();

    default void addPersistentChargeSaveData(CompoundTag pNbt) {
        pNbt.putInt("ChargeTime", this.getRemainingPersistentChargeTime());
    }

    default void readPersistentChargeSaveData(CompoundTag pTag) {
        this.setRemainingPersistentChargeTime(pTag.getInt("ChargeTime"));
    }

    default void updatePersistentCharge() {
        if (this.getRemainingPersistentChargeTime() > 0) {
            this.setRemainingPersistentChargeTime(this.getRemainingPersistentChargeTime() - 1);
        }
    }

    @Override
    default boolean isPowered() {
        return this.getRemainingPersistentChargeTime() > 0;
    }
}