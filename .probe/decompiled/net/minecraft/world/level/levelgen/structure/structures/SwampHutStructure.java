package net.minecraft.world.level.levelgen.structure.structures;

import com.mojang.serialization.Codec;
import java.util.Optional;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class SwampHutStructure extends Structure {

    public static final Codec<SwampHutStructure> CODEC = m_226607_(SwampHutStructure::new);

    public SwampHutStructure(Structure.StructureSettings structureStructureSettings0) {
        super(structureStructureSettings0);
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext structureGenerationContext0) {
        return m_226585_(structureGenerationContext0, Heightmap.Types.WORLD_SURFACE_WG, p_229979_ -> generatePieces(p_229979_, structureGenerationContext0));
    }

    private static void generatePieces(StructurePiecesBuilder structurePiecesBuilder0, Structure.GenerationContext structureGenerationContext1) {
        structurePiecesBuilder0.addPiece(new SwampHutPiece(structureGenerationContext1.random(), structureGenerationContext1.chunkPos().getMinBlockX(), structureGenerationContext1.chunkPos().getMinBlockZ()));
    }

    @Override
    public StructureType<?> type() {
        return StructureType.SWAMP_HUT;
    }
}