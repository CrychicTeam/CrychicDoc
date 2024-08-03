package me.shedaniel.clothconfig2.impl;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface GameOptionsHooks {

    void cloth_setKeysAll(KeyMapping[] var1);
}