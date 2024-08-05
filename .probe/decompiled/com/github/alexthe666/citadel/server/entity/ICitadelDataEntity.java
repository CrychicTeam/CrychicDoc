package com.github.alexthe666.citadel.server.entity;

import net.minecraft.nbt.CompoundTag;

public interface ICitadelDataEntity {

    CompoundTag getCitadelEntityData();

    void setCitadelEntityData(CompoundTag var1);
}