package com.yungnickyoung.minecraft.yungsapi.mixin.accessor;

import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ StructureTemplatePool.class })
public interface StructureTemplatePoolAccessor {

    @Accessor
    List<Pair<StructurePoolElement, Integer>> getRawTemplates();
}