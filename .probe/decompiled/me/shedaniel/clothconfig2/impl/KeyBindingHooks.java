package me.shedaniel.clothconfig2.impl;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface KeyBindingHooks {

    void cloth_setId(String var1);
}