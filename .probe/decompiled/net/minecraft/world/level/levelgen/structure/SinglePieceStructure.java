package net.minecraft.world.level.levelgen.structure;

import java.util.Optional;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public abstract class SinglePieceStructure extends Structure {

    private final SinglePieceStructure.PieceConstructor constructor;

    private final int width;

    private final int depth;

    protected SinglePieceStructure(SinglePieceStructure.PieceConstructor singlePieceStructurePieceConstructor0, int int1, int int2, Structure.StructureSettings structureStructureSettings3) {
        super(structureStructureSettings3);
        this.constructor = singlePieceStructurePieceConstructor0;
        this.width = int1;
        this.depth = int2;
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext structureGenerationContext0) {
        return m_226572_(structureGenerationContext0, this.width, this.depth) < structureGenerationContext0.chunkGenerator().getSeaLevel() ? Optional.empty() : m_226585_(structureGenerationContext0, Heightmap.Types.WORLD_SURFACE_WG, p_226545_ -> this.generatePieces(p_226545_, structureGenerationContext0));
    }

    private void generatePieces(StructurePiecesBuilder structurePiecesBuilder0, Structure.GenerationContext structureGenerationContext1) {
        ChunkPos $$2 = structureGenerationContext1.chunkPos();
        structurePiecesBuilder0.addPiece(this.constructor.construct(structureGenerationContext1.random(), $$2.getMinBlockX(), $$2.getMinBlockZ()));
    }

    @FunctionalInterface
    protected interface PieceConstructor {

        StructurePiece construct(WorldgenRandom var1, int var2, int var3);
    }
}