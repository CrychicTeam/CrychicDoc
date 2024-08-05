package com.rekindled.embers.api.augment;

import java.util.Collection;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface IAugmentUtil {

    String HEAT_TAG = "embers:heat_tag";

    Collection<IAugment> getAllAugments();

    IAugment getAugment(ResourceLocation var1);

    List<IAugment> getAugments(ItemStack var1);

    int getTotalAugmentLevel(ItemStack var1);

    boolean hasAugment(ItemStack var1, IAugment var2);

    void addAugment(ItemStack var1, ItemStack var2, IAugment var3);

    List<ItemStack> removeAllAugments(ItemStack var1);

    void addAugmentLevel(ItemStack var1, IAugment var2, int var3);

    void setAugmentLevel(ItemStack var1, IAugment var2, int var3);

    int getAugmentLevel(ItemStack var1, IAugment var2);

    boolean hasHeat(ItemStack var1);

    void addHeat(ItemStack var1, float var2);

    void setHeat(ItemStack var1, float var2);

    float getHeat(ItemStack var1);

    float getMaxHeat(ItemStack var1);

    int getLevel(ItemStack var1);

    void setLevel(ItemStack var1, int var2);

    int getArmorAugmentLevel(LivingEntity var1, IAugment var2);

    IAugment registerAugment(IAugment var1);
}