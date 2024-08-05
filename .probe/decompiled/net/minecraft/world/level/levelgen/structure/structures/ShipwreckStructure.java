package net.minecraft.world.level.levelgen.structure.structures;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class ShipwreckStructure extends Structure {

    public static final Codec<ShipwreckStructure> CODEC = RecordCodecBuilder.create(p_229401_ -> p_229401_.group(m_226567_(p_229401_), Codec.BOOL.fieldOf("is_beached").forGetter(p_229399_ -> p_229399_.isBeached)).apply(p_229401_, ShipwreckStructure::new));

    public final boolean isBeached;

    public ShipwreckStructure(Structure.StructureSettings structureStructureSettings0, boolean boolean1) {
        super(structureStructureSettings0);
        this.isBeached = boolean1;
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext structureGenerationContext0) {
        Heightmap.Types $$1 = this.isBeached ? Heightmap.Types.WORLD_SURFACE_WG : Heightmap.Types.OCEAN_FLOOR_WG;
        return m_226585_(structureGenerationContext0, $$1, p_229394_ -> this.generatePieces(p_229394_, structureGenerationContext0));
    }

    private void generatePieces(StructurePiecesBuilder structurePiecesBuilder0, Structure.GenerationContext structureGenerationContext1) {
        Rotation $$2 = Rotation.getRandom(structureGenerationContext1.random());
        BlockPos $$3 = new BlockPos(structureGenerationContext1.chunkPos().getMinBlockX(), 90, structureGenerationContext1.chunkPos().getMinBlockZ());
        ShipwreckPieces.addPieces(structureGenerationContext1.structureTemplateManager(), $$3, $$2, structurePiecesBuilder0, structureGenerationContext1.random(), this.isBeached);
    }

    @Override
    public StructureType<?> type() {
        return StructureType.SHIPWRECK;
    }
}