package net.minecraft.world.level.levelgen.structure.structures;

import com.mojang.serialization.Codec;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class OceanMonumentStructure extends Structure {

    public static final Codec<OceanMonumentStructure> CODEC = m_226607_(OceanMonumentStructure::new);

    public OceanMonumentStructure(Structure.StructureSettings structureStructureSettings0) {
        super(structureStructureSettings0);
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext structureGenerationContext0) {
        int $$1 = structureGenerationContext0.chunkPos().getBlockX(9);
        int $$2 = structureGenerationContext0.chunkPos().getBlockZ(9);
        for (Holder<Biome> $$4 : structureGenerationContext0.biomeSource().getBiomesWithin($$1, structureGenerationContext0.chunkGenerator().getSeaLevel(), $$2, 29, structureGenerationContext0.randomState().sampler())) {
            if (!$$4.is(BiomeTags.REQUIRED_OCEAN_MONUMENT_SURROUNDING)) {
                return Optional.empty();
            }
        }
        return m_226585_(structureGenerationContext0, Heightmap.Types.OCEAN_FLOOR_WG, p_228967_ -> generatePieces(p_228967_, structureGenerationContext0));
    }

    private static StructurePiece createTopPiece(ChunkPos chunkPos0, WorldgenRandom worldgenRandom1) {
        int $$2 = chunkPos0.getMinBlockX() - 29;
        int $$3 = chunkPos0.getMinBlockZ() - 29;
        Direction $$4 = Direction.Plane.HORIZONTAL.getRandomDirection(worldgenRandom1);
        return new OceanMonumentPieces.MonumentBuilding(worldgenRandom1, $$2, $$3, $$4);
    }

    private static void generatePieces(StructurePiecesBuilder structurePiecesBuilder0, Structure.GenerationContext structureGenerationContext1) {
        structurePiecesBuilder0.addPiece(createTopPiece(structureGenerationContext1.chunkPos(), structureGenerationContext1.random()));
    }

    public static PiecesContainer regeneratePiecesAfterLoad(ChunkPos chunkPos0, long long1, PiecesContainer piecesContainer2) {
        if (piecesContainer2.isEmpty()) {
            return piecesContainer2;
        } else {
            WorldgenRandom $$3 = new WorldgenRandom(new LegacyRandomSource(RandomSupport.generateUniqueSeed()));
            $$3.setLargeFeatureSeed(long1, chunkPos0.x, chunkPos0.z);
            StructurePiece $$4 = (StructurePiece) piecesContainer2.pieces().get(0);
            BoundingBox $$5 = $$4.getBoundingBox();
            int $$6 = $$5.minX();
            int $$7 = $$5.minZ();
            Direction $$8 = Direction.Plane.HORIZONTAL.getRandomDirection($$3);
            Direction $$9 = (Direction) Objects.requireNonNullElse($$4.getOrientation(), $$8);
            StructurePiece $$10 = new OceanMonumentPieces.MonumentBuilding($$3, $$6, $$7, $$9);
            StructurePiecesBuilder $$11 = new StructurePiecesBuilder();
            $$11.addPiece($$10);
            return $$11.build();
        }
    }

    @Override
    public StructureType<?> type() {
        return StructureType.OCEAN_MONUMENT;
    }
}