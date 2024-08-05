package net.minecraft.world.level.levelgen.structure.structures;

import com.mojang.serialization.Codec;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class IglooStructure extends Structure {

    public static final Codec<IglooStructure> CODEC = m_226607_(IglooStructure::new);

    public IglooStructure(Structure.StructureSettings structureStructureSettings0) {
        super(structureStructureSettings0);
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext structureGenerationContext0) {
        return m_226585_(structureGenerationContext0, Heightmap.Types.WORLD_SURFACE_WG, p_227598_ -> this.generatePieces(p_227598_, structureGenerationContext0));
    }

    private void generatePieces(StructurePiecesBuilder structurePiecesBuilder0, Structure.GenerationContext structureGenerationContext1) {
        ChunkPos $$2 = structureGenerationContext1.chunkPos();
        WorldgenRandom $$3 = structureGenerationContext1.random();
        BlockPos $$4 = new BlockPos($$2.getMinBlockX(), 90, $$2.getMinBlockZ());
        Rotation $$5 = Rotation.getRandom($$3);
        IglooPieces.addPieces(structureGenerationContext1.structureTemplateManager(), $$4, $$5, structurePiecesBuilder0, $$3);
    }

    @Override
    public StructureType<?> type() {
        return StructureType.IGLOO;
    }
}