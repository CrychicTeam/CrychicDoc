package com.github.alexthe666.iceandfire.world.structure;

import com.github.alexthe666.citadel.config.biome.SpawnBiomeData;
import com.github.alexthe666.iceandfire.config.BiomeConfig;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.apache.commons.lang3.tuple.Pair;

public class IafStructure extends Structure {

    protected final Holder<StructureTemplatePool> startPool;

    protected final Optional<ResourceLocation> startJigsawName;

    protected final int size;

    protected final HeightProvider startHeight;

    protected final Optional<Heightmap.Types> projectStartToHeightmap;

    protected final int maxDistanceFromCenter;

    public IafStructure(Structure.StructureSettings config, Holder<StructureTemplatePool> startPool, Optional<ResourceLocation> startJigsawName, int size, HeightProvider startHeight, Optional<Heightmap.Types> projectStartToHeightmap, int maxDistanceFromCenter) {
        super(config);
        this.startPool = startPool;
        this.startJigsawName = startJigsawName;
        this.size = size;
        this.startHeight = startHeight;
        this.projectStartToHeightmap = projectStartToHeightmap;
        this.maxDistanceFromCenter = maxDistanceFromCenter;
    }

    protected boolean isBiomeValid(Structure.GenerationContext pContext, Pair<String, SpawnBiomeData> validBiomes, BlockPos blockPos) {
        boolean validBiome = false;
        for (Holder<Biome> biome : pContext.chunkGenerator().getBiomeSource().getBiomesWithin(blockPos.m_123341_(), blockPos.m_123342_(), blockPos.m_123343_(), this.maxDistanceFromCenter, pContext.randomState().sampler())) {
            if (BiomeConfig.test(validBiomes, biome)) {
                validBiome = true;
                break;
            }
        }
        return validBiome;
    }

    @Override
    protected Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext pContext) {
        return Optional.empty();
    }

    @Override
    public StructureType<?> type() {
        return null;
    }
}