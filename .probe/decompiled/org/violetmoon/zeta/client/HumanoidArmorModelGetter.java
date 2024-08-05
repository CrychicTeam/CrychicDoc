package org.violetmoon.zeta.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface HumanoidArmorModelGetter {

    HumanoidModel<?> getHumanoidArmorModel(LivingEntity var1, ItemStack var2, EquipmentSlot var3, HumanoidModel<?> var4);
}