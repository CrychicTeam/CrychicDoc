package net.minecraft.world.level.levelgen.structure.structures;

import com.mojang.serialization.Codec;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class BuriedTreasureStructure extends Structure {

    public static final Codec<BuriedTreasureStructure> CODEC = m_226607_(BuriedTreasureStructure::new);

    public BuriedTreasureStructure(Structure.StructureSettings structureStructureSettings0) {
        super(structureStructureSettings0);
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext structureGenerationContext0) {
        return m_226585_(structureGenerationContext0, Heightmap.Types.OCEAN_FLOOR_WG, p_227390_ -> generatePieces(p_227390_, structureGenerationContext0));
    }

    private static void generatePieces(StructurePiecesBuilder structurePiecesBuilder0, Structure.GenerationContext structureGenerationContext1) {
        BlockPos $$2 = new BlockPos(structureGenerationContext1.chunkPos().getBlockX(9), 90, structureGenerationContext1.chunkPos().getBlockZ(9));
        structurePiecesBuilder0.addPiece(new BuriedTreasurePieces.BuriedTreasurePiece($$2));
    }

    @Override
    public StructureType<?> type() {
        return StructureType.BURIED_TREASURE;
    }
}