package net.minecraft.world.level.levelgen.structure.structures;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.ScatteredFeaturePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class DesertPyramidPiece extends ScatteredFeaturePiece {

    public static final int WIDTH = 21;

    public static final int DEPTH = 21;

    private final boolean[] hasPlacedChest = new boolean[4];

    private final List<BlockPos> potentialSuspiciousSandWorldPositions = new ArrayList();

    private BlockPos randomCollapsedRoofPos = BlockPos.ZERO;

    public DesertPyramidPiece(RandomSource randomSource0, int int1, int int2) {
        super(StructurePieceType.DESERT_PYRAMID_PIECE, int1, 64, int2, 21, 15, 21, m_226760_(randomSource0));
    }

    public DesertPyramidPiece(CompoundTag compoundTag0) {
        super(StructurePieceType.DESERT_PYRAMID_PIECE, compoundTag0);
        this.hasPlacedChest[0] = compoundTag0.getBoolean("hasPlacedChest0");
        this.hasPlacedChest[1] = compoundTag0.getBoolean("hasPlacedChest1");
        this.hasPlacedChest[2] = compoundTag0.getBoolean("hasPlacedChest2");
        this.hasPlacedChest[3] = compoundTag0.getBoolean("hasPlacedChest3");
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1) {
        super.addAdditionalSaveData(structurePieceSerializationContext0, compoundTag1);
        compoundTag1.putBoolean("hasPlacedChest0", this.hasPlacedChest[0]);
        compoundTag1.putBoolean("hasPlacedChest1", this.hasPlacedChest[1]);
        compoundTag1.putBoolean("hasPlacedChest2", this.hasPlacedChest[2]);
        compoundTag1.putBoolean("hasPlacedChest3", this.hasPlacedChest[3]);
    }

    @Override
    public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
        if (this.m_192467_(worldGenLevel0, -randomSource3.nextInt(3))) {
            this.m_73441_(worldGenLevel0, boundingBox4, 0, -4, 0, this.f_72787_ - 1, 0, this.f_72789_ - 1, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
            for (int $$7 = 1; $$7 <= 9; $$7++) {
                this.m_73441_(worldGenLevel0, boundingBox4, $$7, $$7, $$7, this.f_72787_ - 1 - $$7, $$7, this.f_72789_ - 1 - $$7, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
                this.m_73441_(worldGenLevel0, boundingBox4, $$7 + 1, $$7, $$7 + 1, this.f_72787_ - 2 - $$7, $$7, this.f_72789_ - 2 - $$7, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            }
            for (int $$8 = 0; $$8 < this.f_72787_; $$8++) {
                for (int $$9 = 0; $$9 < this.f_72789_; $$9++) {
                    int $$10 = -5;
                    this.m_73528_(worldGenLevel0, Blocks.SANDSTONE.defaultBlockState(), $$8, -5, $$9, boundingBox4);
                }
            }
            BlockState $$11 = (BlockState) Blocks.SANDSTONE_STAIRS.defaultBlockState().m_61124_(StairBlock.FACING, Direction.NORTH);
            BlockState $$12 = (BlockState) Blocks.SANDSTONE_STAIRS.defaultBlockState().m_61124_(StairBlock.FACING, Direction.SOUTH);
            BlockState $$13 = (BlockState) Blocks.SANDSTONE_STAIRS.defaultBlockState().m_61124_(StairBlock.FACING, Direction.EAST);
            BlockState $$14 = (BlockState) Blocks.SANDSTONE_STAIRS.defaultBlockState().m_61124_(StairBlock.FACING, Direction.WEST);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 0, 0, 4, 9, 4, Blocks.SANDSTONE.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 10, 1, 3, 10, 3, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
            this.m_73434_(worldGenLevel0, $$11, 2, 10, 0, boundingBox4);
            this.m_73434_(worldGenLevel0, $$12, 2, 10, 4, boundingBox4);
            this.m_73434_(worldGenLevel0, $$13, 0, 10, 2, boundingBox4);
            this.m_73434_(worldGenLevel0, $$14, 4, 10, 2, boundingBox4);
            this.m_73441_(worldGenLevel0, boundingBox4, this.f_72787_ - 5, 0, 0, this.f_72787_ - 1, 9, 4, Blocks.SANDSTONE.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, this.f_72787_ - 4, 10, 1, this.f_72787_ - 2, 10, 3, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
            this.m_73434_(worldGenLevel0, $$11, this.f_72787_ - 3, 10, 0, boundingBox4);
            this.m_73434_(worldGenLevel0, $$12, this.f_72787_ - 3, 10, 4, boundingBox4);
            this.m_73434_(worldGenLevel0, $$13, this.f_72787_ - 5, 10, 2, boundingBox4);
            this.m_73434_(worldGenLevel0, $$14, this.f_72787_ - 1, 10, 2, boundingBox4);
            this.m_73441_(worldGenLevel0, boundingBox4, 8, 0, 0, 12, 4, 4, Blocks.SANDSTONE.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 9, 1, 0, 11, 3, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.m_73434_(worldGenLevel0, Blocks.CUT_SANDSTONE.defaultBlockState(), 9, 1, 1, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.CUT_SANDSTONE.defaultBlockState(), 9, 2, 1, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.CUT_SANDSTONE.defaultBlockState(), 9, 3, 1, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.CUT_SANDSTONE.defaultBlockState(), 10, 3, 1, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.CUT_SANDSTONE.defaultBlockState(), 11, 3, 1, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.CUT_SANDSTONE.defaultBlockState(), 11, 2, 1, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.CUT_SANDSTONE.defaultBlockState(), 11, 1, 1, boundingBox4);
            this.m_73441_(worldGenLevel0, boundingBox4, 4, 1, 1, 8, 3, 3, Blocks.SANDSTONE.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 4, 1, 2, 8, 2, 2, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 12, 1, 1, 16, 3, 3, Blocks.SANDSTONE.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 12, 1, 2, 16, 2, 2, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 4, 5, this.f_72787_ - 6, 4, this.f_72789_ - 6, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 9, 4, 9, 11, 4, 11, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 8, 1, 8, 8, 3, 8, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 12, 1, 8, 12, 3, 8, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 8, 1, 12, 8, 3, 12, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 12, 1, 12, 12, 3, 12, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 1, 5, 4, 4, 11, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, this.f_72787_ - 5, 1, 5, this.f_72787_ - 2, 4, 11, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 6, 7, 9, 6, 7, 11, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, this.f_72787_ - 7, 7, 9, this.f_72787_ - 7, 7, 11, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 5, 9, 5, 7, 11, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, this.f_72787_ - 6, 5, 9, this.f_72787_ - 6, 7, 11, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
            this.m_73434_(worldGenLevel0, Blocks.AIR.defaultBlockState(), 5, 5, 10, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.AIR.defaultBlockState(), 5, 6, 10, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.AIR.defaultBlockState(), 6, 6, 10, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.AIR.defaultBlockState(), this.f_72787_ - 6, 5, 10, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.AIR.defaultBlockState(), this.f_72787_ - 6, 6, 10, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.AIR.defaultBlockState(), this.f_72787_ - 7, 6, 10, boundingBox4);
            this.m_73441_(worldGenLevel0, boundingBox4, 2, 4, 4, 2, 6, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, this.f_72787_ - 3, 4, 4, this.f_72787_ - 3, 6, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.m_73434_(worldGenLevel0, $$11, 2, 4, 5, boundingBox4);
            this.m_73434_(worldGenLevel0, $$11, 2, 3, 4, boundingBox4);
            this.m_73434_(worldGenLevel0, $$11, this.f_72787_ - 3, 4, 5, boundingBox4);
            this.m_73434_(worldGenLevel0, $$11, this.f_72787_ - 3, 3, 4, boundingBox4);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 1, 3, 2, 2, 3, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, this.f_72787_ - 3, 1, 3, this.f_72787_ - 2, 2, 3, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
            this.m_73434_(worldGenLevel0, Blocks.SANDSTONE.defaultBlockState(), 1, 1, 2, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.SANDSTONE.defaultBlockState(), this.f_72787_ - 2, 1, 2, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.SANDSTONE_SLAB.defaultBlockState(), 1, 2, 2, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.SANDSTONE_SLAB.defaultBlockState(), this.f_72787_ - 2, 2, 2, boundingBox4);
            this.m_73434_(worldGenLevel0, $$14, 2, 1, 2, boundingBox4);
            this.m_73434_(worldGenLevel0, $$13, this.f_72787_ - 3, 1, 2, boundingBox4);
            this.m_73441_(worldGenLevel0, boundingBox4, 4, 3, 5, 4, 3, 17, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, this.f_72787_ - 5, 3, 5, this.f_72787_ - 5, 3, 17, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 3, 1, 5, 4, 2, 16, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, this.f_72787_ - 6, 1, 5, this.f_72787_ - 5, 2, 16, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            for (int $$15 = 5; $$15 <= 17; $$15 += 2) {
                this.m_73434_(worldGenLevel0, Blocks.CUT_SANDSTONE.defaultBlockState(), 4, 1, $$15, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.CHISELED_SANDSTONE.defaultBlockState(), 4, 2, $$15, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.CUT_SANDSTONE.defaultBlockState(), this.f_72787_ - 5, 1, $$15, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.CHISELED_SANDSTONE.defaultBlockState(), this.f_72787_ - 5, 2, $$15, boundingBox4);
            }
            this.m_73434_(worldGenLevel0, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 10, 0, 7, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 10, 0, 8, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 9, 0, 9, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 11, 0, 9, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 8, 0, 10, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 12, 0, 10, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 7, 0, 10, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 13, 0, 10, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 9, 0, 11, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 11, 0, 11, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 10, 0, 12, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 10, 0, 13, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.BLUE_TERRACOTTA.defaultBlockState(), 10, 0, 10, boundingBox4);
            for (int $$16 = 0; $$16 <= this.f_72787_ - 1; $$16 += this.f_72787_ - 1) {
                this.m_73434_(worldGenLevel0, Blocks.CUT_SANDSTONE.defaultBlockState(), $$16, 2, 1, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), $$16, 2, 2, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.CUT_SANDSTONE.defaultBlockState(), $$16, 2, 3, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.CUT_SANDSTONE.defaultBlockState(), $$16, 3, 1, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), $$16, 3, 2, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.CUT_SANDSTONE.defaultBlockState(), $$16, 3, 3, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), $$16, 4, 1, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.CHISELED_SANDSTONE.defaultBlockState(), $$16, 4, 2, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), $$16, 4, 3, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.CUT_SANDSTONE.defaultBlockState(), $$16, 5, 1, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), $$16, 5, 2, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.CUT_SANDSTONE.defaultBlockState(), $$16, 5, 3, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), $$16, 6, 1, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.CHISELED_SANDSTONE.defaultBlockState(), $$16, 6, 2, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), $$16, 6, 3, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), $$16, 7, 1, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), $$16, 7, 2, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), $$16, 7, 3, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.CUT_SANDSTONE.defaultBlockState(), $$16, 8, 1, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.CUT_SANDSTONE.defaultBlockState(), $$16, 8, 2, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.CUT_SANDSTONE.defaultBlockState(), $$16, 8, 3, boundingBox4);
            }
            for (int $$17 = 2; $$17 <= this.f_72787_ - 3; $$17 += this.f_72787_ - 3 - 2) {
                this.m_73434_(worldGenLevel0, Blocks.CUT_SANDSTONE.defaultBlockState(), $$17 - 1, 2, 0, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), $$17, 2, 0, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.CUT_SANDSTONE.defaultBlockState(), $$17 + 1, 2, 0, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.CUT_SANDSTONE.defaultBlockState(), $$17 - 1, 3, 0, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), $$17, 3, 0, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.CUT_SANDSTONE.defaultBlockState(), $$17 + 1, 3, 0, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), $$17 - 1, 4, 0, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.CHISELED_SANDSTONE.defaultBlockState(), $$17, 4, 0, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), $$17 + 1, 4, 0, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.CUT_SANDSTONE.defaultBlockState(), $$17 - 1, 5, 0, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), $$17, 5, 0, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.CUT_SANDSTONE.defaultBlockState(), $$17 + 1, 5, 0, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), $$17 - 1, 6, 0, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.CHISELED_SANDSTONE.defaultBlockState(), $$17, 6, 0, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), $$17 + 1, 6, 0, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), $$17 - 1, 7, 0, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), $$17, 7, 0, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), $$17 + 1, 7, 0, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.CUT_SANDSTONE.defaultBlockState(), $$17 - 1, 8, 0, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.CUT_SANDSTONE.defaultBlockState(), $$17, 8, 0, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.CUT_SANDSTONE.defaultBlockState(), $$17 + 1, 8, 0, boundingBox4);
            }
            this.m_73441_(worldGenLevel0, boundingBox4, 8, 4, 0, 12, 6, 0, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
            this.m_73434_(worldGenLevel0, Blocks.AIR.defaultBlockState(), 8, 6, 0, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.AIR.defaultBlockState(), 12, 6, 0, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 9, 5, 0, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.CHISELED_SANDSTONE.defaultBlockState(), 10, 5, 0, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 11, 5, 0, boundingBox4);
            this.m_73441_(worldGenLevel0, boundingBox4, 8, -14, 8, 12, -11, 12, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 8, -10, 8, 12, -10, 12, Blocks.CHISELED_SANDSTONE.defaultBlockState(), Blocks.CHISELED_SANDSTONE.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 8, -9, 8, 12, -9, 12, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 8, -8, 8, 12, -1, 12, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 9, -11, 9, 11, -1, 11, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.m_73434_(worldGenLevel0, Blocks.STONE_PRESSURE_PLATE.defaultBlockState(), 10, -11, 10, boundingBox4);
            this.m_73441_(worldGenLevel0, boundingBox4, 9, -13, 9, 11, -13, 11, Blocks.TNT.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.m_73434_(worldGenLevel0, Blocks.AIR.defaultBlockState(), 8, -11, 10, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.AIR.defaultBlockState(), 8, -10, 10, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.CHISELED_SANDSTONE.defaultBlockState(), 7, -10, 10, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.CUT_SANDSTONE.defaultBlockState(), 7, -11, 10, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.AIR.defaultBlockState(), 12, -11, 10, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.AIR.defaultBlockState(), 12, -10, 10, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.CHISELED_SANDSTONE.defaultBlockState(), 13, -10, 10, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.CUT_SANDSTONE.defaultBlockState(), 13, -11, 10, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.AIR.defaultBlockState(), 10, -11, 8, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.AIR.defaultBlockState(), 10, -10, 8, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.CHISELED_SANDSTONE.defaultBlockState(), 10, -10, 7, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.CUT_SANDSTONE.defaultBlockState(), 10, -11, 7, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.AIR.defaultBlockState(), 10, -11, 12, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.AIR.defaultBlockState(), 10, -10, 12, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.CHISELED_SANDSTONE.defaultBlockState(), 10, -10, 13, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.CUT_SANDSTONE.defaultBlockState(), 10, -11, 13, boundingBox4);
            for (Direction $$18 : Direction.Plane.HORIZONTAL) {
                if (!this.hasPlacedChest[$$18.get2DDataValue()]) {
                    int $$19 = $$18.getStepX() * 2;
                    int $$20 = $$18.getStepZ() * 2;
                    this.hasPlacedChest[$$18.get2DDataValue()] = this.m_213787_(worldGenLevel0, boundingBox4, randomSource3, 10 + $$19, -11, 10 + $$20, BuiltInLootTables.DESERT_PYRAMID);
                }
            }
            this.addCellar(worldGenLevel0, boundingBox4);
        }
    }

    private void addCellar(WorldGenLevel worldGenLevel0, BoundingBox boundingBox1) {
        BlockPos $$2 = new BlockPos(16, -4, 13);
        this.addCellarStairs($$2, worldGenLevel0, boundingBox1);
        this.addCellarRoom($$2, worldGenLevel0, boundingBox1);
    }

    private void addCellarStairs(BlockPos blockPos0, WorldGenLevel worldGenLevel1, BoundingBox boundingBox2) {
        int $$3 = blockPos0.m_123341_();
        int $$4 = blockPos0.m_123342_();
        int $$5 = blockPos0.m_123343_();
        BlockState $$6 = Blocks.SANDSTONE_STAIRS.defaultBlockState();
        this.m_73434_(worldGenLevel1, $$6.m_60717_(Rotation.COUNTERCLOCKWISE_90), 13, -1, 17, boundingBox2);
        this.m_73434_(worldGenLevel1, $$6.m_60717_(Rotation.COUNTERCLOCKWISE_90), 14, -2, 17, boundingBox2);
        this.m_73434_(worldGenLevel1, $$6.m_60717_(Rotation.COUNTERCLOCKWISE_90), 15, -3, 17, boundingBox2);
        BlockState $$7 = Blocks.SAND.defaultBlockState();
        BlockState $$8 = Blocks.SANDSTONE.defaultBlockState();
        boolean $$9 = worldGenLevel1.m_213780_().nextBoolean();
        this.m_73434_(worldGenLevel1, $$7, $$3 - 4, $$4 + 4, $$5 + 4, boundingBox2);
        this.m_73434_(worldGenLevel1, $$7, $$3 - 3, $$4 + 4, $$5 + 4, boundingBox2);
        this.m_73434_(worldGenLevel1, $$7, $$3 - 2, $$4 + 4, $$5 + 4, boundingBox2);
        this.m_73434_(worldGenLevel1, $$7, $$3 - 1, $$4 + 4, $$5 + 4, boundingBox2);
        this.m_73434_(worldGenLevel1, $$7, $$3, $$4 + 4, $$5 + 4, boundingBox2);
        this.m_73434_(worldGenLevel1, $$7, $$3 - 2, $$4 + 3, $$5 + 4, boundingBox2);
        this.m_73434_(worldGenLevel1, $$9 ? $$7 : $$8, $$3 - 1, $$4 + 3, $$5 + 4, boundingBox2);
        this.m_73434_(worldGenLevel1, !$$9 ? $$7 : $$8, $$3, $$4 + 3, $$5 + 4, boundingBox2);
        this.m_73434_(worldGenLevel1, $$7, $$3 - 1, $$4 + 2, $$5 + 4, boundingBox2);
        this.m_73434_(worldGenLevel1, $$8, $$3, $$4 + 2, $$5 + 4, boundingBox2);
        this.m_73434_(worldGenLevel1, $$7, $$3, $$4 + 1, $$5 + 4, boundingBox2);
    }

    private void addCellarRoom(BlockPos blockPos0, WorldGenLevel worldGenLevel1, BoundingBox boundingBox2) {
        int $$3 = blockPos0.m_123341_();
        int $$4 = blockPos0.m_123342_();
        int $$5 = blockPos0.m_123343_();
        BlockState $$6 = Blocks.CUT_SANDSTONE.defaultBlockState();
        BlockState $$7 = Blocks.CHISELED_SANDSTONE.defaultBlockState();
        this.m_73441_(worldGenLevel1, boundingBox2, $$3 - 3, $$4 + 1, $$5 - 3, $$3 - 3, $$4 + 1, $$5 + 2, $$6, $$6, true);
        this.m_73441_(worldGenLevel1, boundingBox2, $$3 + 3, $$4 + 1, $$5 - 3, $$3 + 3, $$4 + 1, $$5 + 2, $$6, $$6, true);
        this.m_73441_(worldGenLevel1, boundingBox2, $$3 - 3, $$4 + 1, $$5 - 3, $$3 + 3, $$4 + 1, $$5 - 2, $$6, $$6, true);
        this.m_73441_(worldGenLevel1, boundingBox2, $$3 - 3, $$4 + 1, $$5 + 3, $$3 + 3, $$4 + 1, $$5 + 3, $$6, $$6, true);
        this.m_73441_(worldGenLevel1, boundingBox2, $$3 - 3, $$4 + 2, $$5 - 3, $$3 - 3, $$4 + 2, $$5 + 2, $$7, $$7, true);
        this.m_73441_(worldGenLevel1, boundingBox2, $$3 + 3, $$4 + 2, $$5 - 3, $$3 + 3, $$4 + 2, $$5 + 2, $$7, $$7, true);
        this.m_73441_(worldGenLevel1, boundingBox2, $$3 - 3, $$4 + 2, $$5 - 3, $$3 + 3, $$4 + 2, $$5 - 2, $$7, $$7, true);
        this.m_73441_(worldGenLevel1, boundingBox2, $$3 - 3, $$4 + 2, $$5 + 3, $$3 + 3, $$4 + 2, $$5 + 3, $$7, $$7, true);
        this.m_73441_(worldGenLevel1, boundingBox2, $$3 - 3, -1, $$5 - 3, $$3 - 3, -1, $$5 + 2, $$6, $$6, true);
        this.m_73441_(worldGenLevel1, boundingBox2, $$3 + 3, -1, $$5 - 3, $$3 + 3, -1, $$5 + 2, $$6, $$6, true);
        this.m_73441_(worldGenLevel1, boundingBox2, $$3 - 3, -1, $$5 - 3, $$3 + 3, -1, $$5 - 2, $$6, $$6, true);
        this.m_73441_(worldGenLevel1, boundingBox2, $$3 - 3, -1, $$5 + 3, $$3 + 3, -1, $$5 + 3, $$6, $$6, true);
        this.placeSandBox($$3 - 2, $$4 + 1, $$5 - 2, $$3 + 2, $$4 + 3, $$5 + 2);
        this.placeCollapsedRoof(worldGenLevel1, boundingBox2, $$3 - 2, $$4 + 4, $$5 - 2, $$3 + 2, $$5 + 2);
        BlockState $$8 = Blocks.ORANGE_TERRACOTTA.defaultBlockState();
        BlockState $$9 = Blocks.BLUE_TERRACOTTA.defaultBlockState();
        this.m_73434_(worldGenLevel1, $$9, $$3, $$4, $$5, boundingBox2);
        this.m_73434_(worldGenLevel1, $$8, $$3 + 1, $$4, $$5 - 1, boundingBox2);
        this.m_73434_(worldGenLevel1, $$8, $$3 + 1, $$4, $$5 + 1, boundingBox2);
        this.m_73434_(worldGenLevel1, $$8, $$3 - 1, $$4, $$5 - 1, boundingBox2);
        this.m_73434_(worldGenLevel1, $$8, $$3 - 1, $$4, $$5 + 1, boundingBox2);
        this.m_73434_(worldGenLevel1, $$8, $$3 + 2, $$4, $$5, boundingBox2);
        this.m_73434_(worldGenLevel1, $$8, $$3 - 2, $$4, $$5, boundingBox2);
        this.m_73434_(worldGenLevel1, $$8, $$3, $$4, $$5 + 2, boundingBox2);
        this.m_73434_(worldGenLevel1, $$8, $$3, $$4, $$5 - 2, boundingBox2);
        this.m_73434_(worldGenLevel1, $$8, $$3 + 3, $$4, $$5, boundingBox2);
        this.placeSand($$3 + 3, $$4 + 1, $$5);
        this.placeSand($$3 + 3, $$4 + 2, $$5);
        this.m_73434_(worldGenLevel1, $$6, $$3 + 4, $$4 + 1, $$5, boundingBox2);
        this.m_73434_(worldGenLevel1, $$7, $$3 + 4, $$4 + 2, $$5, boundingBox2);
        this.m_73434_(worldGenLevel1, $$8, $$3 - 3, $$4, $$5, boundingBox2);
        this.placeSand($$3 - 3, $$4 + 1, $$5);
        this.placeSand($$3 - 3, $$4 + 2, $$5);
        this.m_73434_(worldGenLevel1, $$6, $$3 - 4, $$4 + 1, $$5, boundingBox2);
        this.m_73434_(worldGenLevel1, $$7, $$3 - 4, $$4 + 2, $$5, boundingBox2);
        this.m_73434_(worldGenLevel1, $$8, $$3, $$4, $$5 + 3, boundingBox2);
        this.placeSand($$3, $$4 + 1, $$5 + 3);
        this.placeSand($$3, $$4 + 2, $$5 + 3);
        this.m_73434_(worldGenLevel1, $$8, $$3, $$4, $$5 - 3, boundingBox2);
        this.placeSand($$3, $$4 + 1, $$5 - 3);
        this.placeSand($$3, $$4 + 2, $$5 - 3);
        this.m_73434_(worldGenLevel1, $$6, $$3, $$4 + 1, $$5 - 4, boundingBox2);
        this.m_73434_(worldGenLevel1, $$7, $$3, -2, $$5 - 4, boundingBox2);
    }

    private void placeSand(int int0, int int1, int int2) {
        BlockPos $$3 = this.m_163582_(int0, int1, int2);
        this.potentialSuspiciousSandWorldPositions.add($$3);
    }

    private void placeSandBox(int int0, int int1, int int2, int int3, int int4, int int5) {
        for (int $$6 = int1; $$6 <= int4; $$6++) {
            for (int $$7 = int0; $$7 <= int3; $$7++) {
                for (int $$8 = int2; $$8 <= int5; $$8++) {
                    this.placeSand($$7, $$6, $$8);
                }
            }
        }
    }

    private void placeCollapsedRoofPiece(WorldGenLevel worldGenLevel0, int int1, int int2, int int3, BoundingBox boundingBox4) {
        if (worldGenLevel0.m_213780_().nextFloat() < 0.33F) {
            BlockState $$5 = Blocks.SANDSTONE.defaultBlockState();
            this.m_73434_(worldGenLevel0, $$5, int1, int2, int3, boundingBox4);
        } else {
            BlockState $$6 = Blocks.SAND.defaultBlockState();
            this.m_73434_(worldGenLevel0, $$6, int1, int2, int3, boundingBox4);
        }
    }

    private void placeCollapsedRoof(WorldGenLevel worldGenLevel0, BoundingBox boundingBox1, int int2, int int3, int int4, int int5, int int6) {
        for (int $$7 = int2; $$7 <= int5; $$7++) {
            for (int $$8 = int4; $$8 <= int6; $$8++) {
                this.placeCollapsedRoofPiece(worldGenLevel0, $$7, int3, $$8, boundingBox1);
            }
        }
        RandomSource $$9 = RandomSource.create(worldGenLevel0.getSeed()).forkPositional().at(this.m_163582_(int2, int3, int4));
        int $$10 = $$9.nextIntBetweenInclusive(int2, int5);
        int $$11 = $$9.nextIntBetweenInclusive(int4, int6);
        this.randomCollapsedRoofPos = new BlockPos(this.m_73392_($$10, $$11), this.m_73544_(int3), this.m_73525_($$10, $$11));
    }

    public List<BlockPos> getPotentialSuspiciousSandWorldPositions() {
        return this.potentialSuspiciousSandWorldPositions;
    }

    public BlockPos getRandomCollapsedRoofPos() {
        return this.randomCollapsedRoofPos;
    }
}