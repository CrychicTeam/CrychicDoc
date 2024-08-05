package net.minecraft.world.level.levelgen.structure.structures;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class WoodlandMansionStructure extends Structure {

    public static final Codec<WoodlandMansionStructure> CODEC = m_226607_(WoodlandMansionStructure::new);

    public WoodlandMansionStructure(Structure.StructureSettings structureStructureSettings0) {
        super(structureStructureSettings0);
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext structureGenerationContext0) {
        Rotation $$1 = Rotation.getRandom(structureGenerationContext0.random());
        BlockPos $$2 = this.m_226582_(structureGenerationContext0, $$1);
        return $$2.m_123342_() < 60 ? Optional.empty() : Optional.of(new Structure.GenerationStub($$2, (Consumer<StructurePiecesBuilder>) (p_230240_ -> this.generatePieces(p_230240_, structureGenerationContext0, $$2, $$1))));
    }

    private void generatePieces(StructurePiecesBuilder structurePiecesBuilder0, Structure.GenerationContext structureGenerationContext1, BlockPos blockPos2, Rotation rotation3) {
        List<WoodlandMansionPieces.WoodlandMansionPiece> $$4 = Lists.newLinkedList();
        WoodlandMansionPieces.generateMansion(structureGenerationContext1.structureTemplateManager(), blockPos2, rotation3, $$4, structureGenerationContext1.random());
        $$4.forEach(structurePiecesBuilder0::m_142679_);
    }

    @Override
    public void afterPlace(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, PiecesContainer piecesContainer6) {
        BlockPos.MutableBlockPos $$7 = new BlockPos.MutableBlockPos();
        int $$8 = worldGenLevel0.m_141937_();
        BoundingBox $$9 = piecesContainer6.calculateBoundingBox();
        int $$10 = $$9.minY();
        for (int $$11 = boundingBox4.minX(); $$11 <= boundingBox4.maxX(); $$11++) {
            for (int $$12 = boundingBox4.minZ(); $$12 <= boundingBox4.maxZ(); $$12++) {
                $$7.set($$11, $$10, $$12);
                if (!worldGenLevel0.m_46859_($$7) && $$9.isInside($$7) && piecesContainer6.isInsidePiece($$7)) {
                    for (int $$13 = $$10 - 1; $$13 > $$8; $$13--) {
                        $$7.setY($$13);
                        if (!worldGenLevel0.m_46859_($$7) && !worldGenLevel0.m_8055_($$7).m_278721_()) {
                            break;
                        }
                        worldGenLevel0.m_7731_($$7, Blocks.COBBLESTONE.defaultBlockState(), 2);
                    }
                }
            }
        }
    }

    @Override
    public StructureType<?> type() {
        return StructureType.WOODLAND_MANSION;
    }
}