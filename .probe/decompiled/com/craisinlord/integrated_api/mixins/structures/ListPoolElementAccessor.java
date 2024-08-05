package com.craisinlord.integrated_api.mixins.structures;

import java.util.List;
import net.minecraft.world.level.levelgen.structure.pools.ListPoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ ListPoolElement.class })
public interface ListPoolElementAccessor {

    @Accessor("elements")
    List<StructurePoolElement> integratedapi_getElements();
}