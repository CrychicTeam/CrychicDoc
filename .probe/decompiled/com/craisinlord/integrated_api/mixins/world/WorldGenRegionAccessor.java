package com.craisinlord.integrated_api.mixins.world;

import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.StructureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ WorldGenRegion.class })
public interface WorldGenRegionAccessor {

    @Accessor("structureManager")
    StructureManager getStructureManager();
}