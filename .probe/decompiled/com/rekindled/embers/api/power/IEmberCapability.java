package com.rekindled.embers.api.power;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public interface IEmberCapability extends ICapabilitySerializable<CompoundTag> {

    String EMBER_CAPACITY = "embers:ember_capacity";

    String EMBER = "embers:ember";

    double getEmber();

    double getEmberCapacity();

    void setEmber(double var1);

    void setEmberCapacity(double var1);

    double addAmount(double var1, boolean var3);

    double removeAmount(double var1, boolean var3);

    void writeToNBT(CompoundTag var1);

    void onContentsChanged();

    void invalidate();

    default boolean acceptsVolatile() {
        return false;
    }
}