package com.github.alexmodguy.alexscaves.server.level.structure;

import com.github.alexmodguy.alexscaves.server.level.structure.piece.UndergroundCabinStructurePiece;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.mojang.serialization.Codec;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class UndergroundCabinStructure extends Structure {

    public static final Codec<UndergroundCabinStructure> CODEC = m_226607_(settings -> new UndergroundCabinStructure(settings));

    private static final ResourceLocation[] CABIN_NBT = new ResourceLocation[] { new ResourceLocation("alexscaves", "underground_cabin_0"), new ResourceLocation("alexscaves", "underground_cabin_1"), new ResourceLocation("alexscaves", "underground_cabin_2"), new ResourceLocation("alexscaves", "underground_cabin_3"), new ResourceLocation("alexscaves", "underground_cabin_4"), new ResourceLocation("alexscaves", "underground_cabin_5"), new ResourceLocation("alexscaves", "underground_cabin_6") };

    public UndergroundCabinStructure(Structure.StructureSettings settings) {
        super(settings);
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        Rotation rotation = Rotation.getRandom(context.random());
        LevelHeightAccessor levelHeight = context.heightAccessor();
        int y = context.chunkGenerator().getBaseHeight(context.chunkPos().getMinBlockX(), context.chunkPos().getMinBlockZ(), Heightmap.Types.OCEAN_FLOOR_WG, levelHeight, context.randomState()) - 20;
        int maxHeight = y - 14 - context.random().m_188503_(15);
        BlockPos blockpos = new BlockPos(context.chunkPos().getMinBlockX(), maxHeight, context.chunkPos().getMinBlockZ());
        for (Holder<Biome> holder : context.biomeSource().getBiomesWithin(blockpos.m_123341_(), blockpos.m_123342_(), blockpos.m_123343_(), 63, context.randomState().sampler())) {
            if (holder.is(ACTagRegistry.HAS_NO_UNDERGROUND_CABINS)) {
                return Optional.empty();
            }
        }
        ResourceLocation res = Util.getRandom(CABIN_NBT, context.random());
        return Optional.of(new Structure.GenerationStub(blockpos, (Consumer<StructurePiecesBuilder>) (piecesBuilder -> piecesBuilder.addPiece(new UndergroundCabinStructurePiece(context.structureTemplateManager(), res, blockpos, rotation)))));
    }

    @Override
    public StructureType<?> type() {
        return ACStructureRegistry.UNDERGROUND_CABIN.get();
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.UNDERGROUND_STRUCTURES;
    }
}