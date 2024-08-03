package com.github.alexmodguy.alexscaves.server.level.structure;

import com.github.alexmodguy.alexscaves.server.level.structure.piece.AbyssalRuinsStructurePiece;
import com.mojang.serialization.Codec;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class AbyssalRuinsStructure extends Structure {

    public static final Codec<AbyssalRuinsStructure> CODEC = m_226607_(settings -> new AbyssalRuinsStructure(settings));

    private static final ResourceLocation[] RUINS_NBT = new ResourceLocation[] { new ResourceLocation("alexscaves", "abyssal_ruins_0"), new ResourceLocation("alexscaves", "abyssal_ruins_1"), new ResourceLocation("alexscaves", "abyssal_ruins_2"), new ResourceLocation("alexscaves", "abyssal_ruins_3") };

    public AbyssalRuinsStructure(Structure.StructureSettings settings) {
        super(settings);
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        Rotation rotation = Rotation.getRandom(context.random());
        LevelHeightAccessor levelHeight = context.heightAccessor();
        int y = context.chunkGenerator().getBaseHeight(context.chunkPos().getMinBlockX(), context.chunkPos().getMinBlockZ(), Heightmap.Types.OCEAN_FLOOR_WG, levelHeight, context.randomState());
        if (y > context.chunkGenerator().getSeaLevel()) {
            return Optional.empty();
        } else {
            BlockPos blockpos = new BlockPos(context.chunkPos().getMinBlockX(), context.chunkGenerator().getMinY() + 15, context.chunkPos().getMinBlockZ());
            ResourceLocation res = Util.getRandom(RUINS_NBT, context.random());
            return Optional.of(new Structure.GenerationStub(blockpos, (Consumer<StructurePiecesBuilder>) (piecesBuilder -> piecesBuilder.addPiece(new AbyssalRuinsStructurePiece(context.structureTemplateManager(), res, blockpos, rotation)))));
        }
    }

    @Override
    public StructureType<?> type() {
        return ACStructureRegistry.ABYSSAL_RUINS.get();
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.UNDERGROUND_STRUCTURES;
    }
}