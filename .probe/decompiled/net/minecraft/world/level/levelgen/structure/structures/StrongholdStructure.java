package net.minecraft.world.level.levelgen.structure.structures;

import com.mojang.serialization.Codec;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class StrongholdStructure extends Structure {

    public static final Codec<StrongholdStructure> CODEC = m_226607_(StrongholdStructure::new);

    public StrongholdStructure(Structure.StructureSettings structureStructureSettings0) {
        super(structureStructureSettings0);
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext structureGenerationContext0) {
        return Optional.of(new Structure.GenerationStub(structureGenerationContext0.chunkPos().getWorldPosition(), (Consumer<StructurePiecesBuilder>) (p_229944_ -> generatePieces(p_229944_, structureGenerationContext0))));
    }

    private static void generatePieces(StructurePiecesBuilder structurePiecesBuilder0, Structure.GenerationContext structureGenerationContext1) {
        int $$2 = 0;
        StrongholdPieces.StartPiece $$3;
        do {
            structurePiecesBuilder0.clear();
            structureGenerationContext1.random().setLargeFeatureSeed(structureGenerationContext1.seed() + (long) ($$2++), structureGenerationContext1.chunkPos().x, structureGenerationContext1.chunkPos().z);
            StrongholdPieces.resetPieces();
            $$3 = new StrongholdPieces.StartPiece(structureGenerationContext1.random(), structureGenerationContext1.chunkPos().getBlockX(2), structureGenerationContext1.chunkPos().getBlockZ(2));
            structurePiecesBuilder0.addPiece($$3);
            $$3.m_214092_($$3, structurePiecesBuilder0, structureGenerationContext1.random());
            List<StructurePiece> $$4 = $$3.pendingChildren;
            while (!$$4.isEmpty()) {
                int $$5 = structureGenerationContext1.random().m_188503_($$4.size());
                StructurePiece $$6 = (StructurePiece) $$4.remove($$5);
                $$6.addChildren($$3, structurePiecesBuilder0, structureGenerationContext1.random());
            }
            structurePiecesBuilder0.moveBelowSeaLevel(structureGenerationContext1.chunkGenerator().getSeaLevel(), structureGenerationContext1.chunkGenerator().getMinY(), structureGenerationContext1.random(), 10);
        } while (structurePiecesBuilder0.isEmpty() || $$3.portalRoomPiece == null);
    }

    @Override
    public StructureType<?> type() {
        return StructureType.STRONGHOLD;
    }
}