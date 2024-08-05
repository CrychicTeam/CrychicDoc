package net.minecraft.world.level.levelgen.structure.structures;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.Set;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.util.SortedArraySet;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.SinglePieceStructure;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class DesertPyramidStructure extends SinglePieceStructure {

    public static final Codec<DesertPyramidStructure> CODEC = m_226607_(DesertPyramidStructure::new);

    public DesertPyramidStructure(Structure.StructureSettings structureStructureSettings0) {
        super(DesertPyramidPiece::new, 21, 21, structureStructureSettings0);
    }

    @Override
    public void afterPlace(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, PiecesContainer piecesContainer6) {
        Set<BlockPos> $$7 = SortedArraySet.<BlockPos>create(Vec3i::compareTo);
        for (StructurePiece $$8 : piecesContainer6.pieces()) {
            if ($$8 instanceof DesertPyramidPiece $$9) {
                $$7.addAll($$9.getPotentialSuspiciousSandWorldPositions());
                placeSuspiciousSand(boundingBox4, worldGenLevel0, $$9.getRandomCollapsedRoofPos());
            }
        }
        ObjectArrayList<BlockPos> $$10 = new ObjectArrayList($$7.stream().toList());
        RandomSource $$11 = RandomSource.create(worldGenLevel0.getSeed()).forkPositional().at(piecesContainer6.calculateBoundingBox().getCenter());
        Util.shuffle($$10, $$11);
        int $$12 = Math.min($$7.size(), $$11.nextInt(5, 8));
        ObjectListIterator var12 = $$10.iterator();
        while (var12.hasNext()) {
            BlockPos $$13 = (BlockPos) var12.next();
            if ($$12 > 0) {
                $$12--;
                placeSuspiciousSand(boundingBox4, worldGenLevel0, $$13);
            } else if (boundingBox4.isInside($$13)) {
                worldGenLevel0.m_7731_($$13, Blocks.SAND.defaultBlockState(), 2);
            }
        }
    }

    private static void placeSuspiciousSand(BoundingBox boundingBox0, WorldGenLevel worldGenLevel1, BlockPos blockPos2) {
        if (boundingBox0.isInside(blockPos2)) {
            worldGenLevel1.m_7731_(blockPos2, Blocks.SUSPICIOUS_SAND.defaultBlockState(), 2);
            worldGenLevel1.m_141902_(blockPos2, BlockEntityType.BRUSHABLE_BLOCK).ifPresent(p_277328_ -> p_277328_.setLootTable(BuiltInLootTables.DESERT_PYRAMID_ARCHAEOLOGY, blockPos2.asLong()));
        }
    }

    @Override
    public StructureType<?> type() {
        return StructureType.DESERT_PYRAMID;
    }
}