package com.github.alexmodguy.alexscaves.server.entity.util;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;

public interface EntityDropChanceAccessor {

    float ac_getEquipmentDropChance(EquipmentSlot var1);

    void ac_setDropChance(EquipmentSlot var1, float var2);

    void ac_dropCustomDeathLoot(DamageSource var1, int var2, boolean var3);
}