package com.mna.api.entities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IEntityHelper {

    Entity createPresentItemEntity(Level var1, double var2, double var4, double var6, ItemStack var8);

    Entity createManaweavePatternEntity(Level var1, Player var2, double var3, double var5, double var7, ResourceLocation var9);
}