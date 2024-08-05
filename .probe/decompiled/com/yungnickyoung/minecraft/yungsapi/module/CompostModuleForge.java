package com.yungnickyoung.minecraft.yungsapi.module;

import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;

public class CompostModuleForge {

    public static final Object2FloatMap<ItemLike> COMPOSTABLES = new Object2FloatOpenHashMap();

    public static void registerCompostables() {
        ComposterBlock.COMPOSTABLES.putAll(COMPOSTABLES);
    }
}