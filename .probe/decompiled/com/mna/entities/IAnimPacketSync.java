package com.mna.entities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

public interface IAnimPacketSync<T extends Entity> {

    CompoundTag getPacketData();

    void handlePacketData(CompoundTag var1);
}