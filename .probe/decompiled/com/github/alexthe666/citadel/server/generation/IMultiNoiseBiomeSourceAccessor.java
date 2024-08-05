package com.github.alexthe666.citadel.server.generation;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public interface IMultiNoiseBiomeSourceAccessor {

    void setLastSampledSeed(long var1);

    void setLastSampledDimension(ResourceKey<Level> var1);
}