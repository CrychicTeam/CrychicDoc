package com.yungnickyoung.minecraft.yungsapi.api;

import com.yungnickyoung.minecraft.yungsapi.world.structure.jigsaw.JigsawManager;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class YungJigsawManager {

    public static Optional<Structure.GenerationStub> assembleJigsawStructure(Structure.GenerationContext generationContext, Holder<StructureTemplatePool> startPool, Optional<ResourceLocation> startJigsawNameOptional, int maxDepth, BlockPos startPos, boolean useExpansionHack, Optional<Heightmap.Types> projectStartToHeightmap, int maxDistanceFromCenter, Optional<Integer> maxY, Optional<Integer> minY) {
        return JigsawManager.assembleJigsawStructure(generationContext, startPool, startJigsawNameOptional, maxDepth, startPos, useExpansionHack, projectStartToHeightmap, maxDistanceFromCenter, maxY, minY);
    }
}