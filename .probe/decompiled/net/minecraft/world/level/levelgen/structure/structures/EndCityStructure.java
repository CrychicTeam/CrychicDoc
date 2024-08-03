package net.minecraft.world.level.levelgen.structure.structures;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class EndCityStructure extends Structure {

    public static final Codec<EndCityStructure> CODEC = m_226607_(EndCityStructure::new);

    public EndCityStructure(Structure.StructureSettings structureStructureSettings0) {
        super(structureStructureSettings0);
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext structureGenerationContext0) {
        Rotation $$1 = Rotation.getRandom(structureGenerationContext0.random());
        BlockPos $$2 = this.m_226582_(structureGenerationContext0, $$1);
        return $$2.m_123342_() < 60 ? Optional.empty() : Optional.of(new Structure.GenerationStub($$2, (Consumer<StructurePiecesBuilder>) (p_227538_ -> this.generatePieces(p_227538_, $$2, $$1, structureGenerationContext0))));
    }

    private void generatePieces(StructurePiecesBuilder structurePiecesBuilder0, BlockPos blockPos1, Rotation rotation2, Structure.GenerationContext structureGenerationContext3) {
        List<StructurePiece> $$4 = Lists.newArrayList();
        EndCityPieces.startHouseTower(structureGenerationContext3.structureTemplateManager(), blockPos1, rotation2, $$4, structureGenerationContext3.random());
        $$4.forEach(structurePiecesBuilder0::m_142679_);
    }

    @Override
    public StructureType<?> type() {
        return StructureType.END_CITY;
    }
}