package com.simibubi.create.foundation.utility;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;

public class WorldHelper {

    public static ResourceLocation getDimensionID(LevelAccessor world) {
        return world.m_9598_().registryOrThrow(Registries.DIMENSION_TYPE).getKey(world.m_6042_());
    }
}