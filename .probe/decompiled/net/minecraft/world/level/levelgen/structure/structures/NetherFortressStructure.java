package net.minecraft.world.level.levelgen.structure.structures;

import com.mojang.serialization.Codec;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class NetherFortressStructure extends Structure {

    public static final WeightedRandomList<MobSpawnSettings.SpawnerData> FORTRESS_ENEMIES = WeightedRandomList.create(new MobSpawnSettings.SpawnerData(EntityType.BLAZE, 10, 2, 3), new MobSpawnSettings.SpawnerData(EntityType.ZOMBIFIED_PIGLIN, 5, 4, 4), new MobSpawnSettings.SpawnerData(EntityType.WITHER_SKELETON, 8, 5, 5), new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 2, 5, 5), new MobSpawnSettings.SpawnerData(EntityType.MAGMA_CUBE, 3, 4, 4));

    public static final Codec<NetherFortressStructure> CODEC = m_226607_(NetherFortressStructure::new);

    public NetherFortressStructure(Structure.StructureSettings structureStructureSettings0) {
        super(structureStructureSettings0);
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext structureGenerationContext0) {
        ChunkPos $$1 = structureGenerationContext0.chunkPos();
        BlockPos $$2 = new BlockPos($$1.getMinBlockX(), 64, $$1.getMinBlockZ());
        return Optional.of(new Structure.GenerationStub($$2, (Consumer<StructurePiecesBuilder>) (p_228526_ -> generatePieces(p_228526_, structureGenerationContext0))));
    }

    private static void generatePieces(StructurePiecesBuilder structurePiecesBuilder0, Structure.GenerationContext structureGenerationContext1) {
        NetherFortressPieces.StartPiece $$2 = new NetherFortressPieces.StartPiece(structureGenerationContext1.random(), structureGenerationContext1.chunkPos().getBlockX(2), structureGenerationContext1.chunkPos().getBlockZ(2));
        structurePiecesBuilder0.addPiece($$2);
        $$2.m_214092_($$2, structurePiecesBuilder0, structureGenerationContext1.random());
        List<StructurePiece> $$3 = $$2.pendingChildren;
        while (!$$3.isEmpty()) {
            int $$4 = structureGenerationContext1.random().m_188503_($$3.size());
            StructurePiece $$5 = (StructurePiece) $$3.remove($$4);
            $$5.addChildren($$2, structurePiecesBuilder0, structureGenerationContext1.random());
        }
        structurePiecesBuilder0.moveInsideHeights(structureGenerationContext1.random(), 48, 70);
    }

    @Override
    public StructureType<?> type() {
        return StructureType.FORTRESS;
    }
}