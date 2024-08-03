package com.github.alexthe666.iceandfire.world.structure;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.config.BiomeConfig;
import com.github.alexthe666.iceandfire.datagen.IafBiomeTagGenerator;
import com.github.alexthe666.iceandfire.datagen.IafStructurePieces;
import com.github.alexthe666.iceandfire.world.IafStructureTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.HashMap;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class GorgonTempleStructure extends IafStructure {

    public static final Codec<GorgonTempleStructure> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(m_226567_(instance), StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool), ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter(structure -> structure.startJigsawName), Codec.intRange(0, 30).fieldOf("size").forGetter(structure -> structure.size), HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight), Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(structure -> structure.projectStartToHeightmap), Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter(structure -> structure.maxDistanceFromCenter)).apply(instance, GorgonTempleStructure::new)).codec();

    public GorgonTempleStructure(Structure.StructureSettings config, Holder<StructureTemplatePool> startPool, Optional<ResourceLocation> startJigsawName, int size, HeightProvider startHeight, Optional<Heightmap.Types> projectStartToHeightmap, int maxDistanceFromCenter) {
        super(config, startPool, startJigsawName, size, startHeight, projectStartToHeightmap, maxDistanceFromCenter);
    }

    @Override
    protected Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext pContext) {
        if (!IafConfig.generateGorgonTemple) {
            return Optional.empty();
        } else {
            ChunkPos pos = pContext.chunkPos();
            BlockPos blockpos = pos.getMiddleBlockPosition(1);
            return !this.isBiomeValid(pContext, BiomeConfig.gorgonTempleBiomes, blockpos) ? Optional.empty() : JigsawPlacement.addPieces(pContext, this.startPool, this.startJigsawName, this.size, blockpos, false, this.projectStartToHeightmap, this.maxDistanceFromCenter);
        }
    }

    @Override
    public StructureType<?> type() {
        return IafStructureTypes.GORGON_TEMPLE.get();
    }

    public static GorgonTempleStructure buildStructureConfig(BootstapContext<Structure> context) {
        HolderGetter<StructureTemplatePool> templatePoolHolderGetter = context.lookup(Registries.TEMPLATE_POOL);
        Holder<StructureTemplatePool> graveyardHolder = templatePoolHolderGetter.getOrThrow(IafStructurePieces.GORGON_TEMPLE_START);
        return new GorgonTempleStructure(new Structure.StructureSettings(context.lookup(Registries.BIOME).getOrThrow(IafBiomeTagGenerator.HAS_GORGON_TEMPLE), new HashMap(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_THIN), graveyardHolder, Optional.empty(), 2, ConstantHeight.ZERO, Optional.of(Heightmap.Types.WORLD_SURFACE_WG), 16);
    }
}