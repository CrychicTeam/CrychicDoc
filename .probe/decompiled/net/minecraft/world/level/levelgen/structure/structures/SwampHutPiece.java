package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.ScatteredFeaturePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class SwampHutPiece extends ScatteredFeaturePiece {

    private boolean spawnedWitch;

    private boolean spawnedCat;

    public SwampHutPiece(RandomSource randomSource0, int int1, int int2) {
        super(StructurePieceType.SWAMPLAND_HUT, int1, 64, int2, 7, 7, 9, m_226760_(randomSource0));
    }

    public SwampHutPiece(CompoundTag compoundTag0) {
        super(StructurePieceType.SWAMPLAND_HUT, compoundTag0);
        this.spawnedWitch = compoundTag0.getBoolean("Witch");
        this.spawnedCat = compoundTag0.getBoolean("Cat");
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1) {
        super.addAdditionalSaveData(structurePieceSerializationContext0, compoundTag1);
        compoundTag1.putBoolean("Witch", this.spawnedWitch);
        compoundTag1.putBoolean("Cat", this.spawnedCat);
    }

    @Override
    public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
        if (this.m_72803_(worldGenLevel0, boundingBox4, 0)) {
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 1, 1, 5, 1, 7, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 4, 2, 5, 4, 7, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 2, 1, 0, 4, 1, 0, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 2, 2, 2, 3, 3, 2, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 2, 3, 1, 3, 6, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 2, 3, 5, 3, 6, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 2, 2, 7, 4, 3, 7, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 0, 2, 1, 3, 2, Blocks.OAK_LOG.defaultBlockState(), Blocks.OAK_LOG.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 0, 2, 5, 3, 2, Blocks.OAK_LOG.defaultBlockState(), Blocks.OAK_LOG.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 0, 7, 1, 3, 7, Blocks.OAK_LOG.defaultBlockState(), Blocks.OAK_LOG.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 0, 7, 5, 3, 7, Blocks.OAK_LOG.defaultBlockState(), Blocks.OAK_LOG.defaultBlockState(), false);
            this.m_73434_(worldGenLevel0, Blocks.OAK_FENCE.defaultBlockState(), 2, 3, 2, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.OAK_FENCE.defaultBlockState(), 3, 3, 7, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.AIR.defaultBlockState(), 1, 3, 4, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.AIR.defaultBlockState(), 5, 3, 4, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.AIR.defaultBlockState(), 5, 3, 5, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.POTTED_RED_MUSHROOM.defaultBlockState(), 1, 3, 5, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.CRAFTING_TABLE.defaultBlockState(), 3, 2, 6, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.CAULDRON.defaultBlockState(), 4, 2, 6, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.OAK_FENCE.defaultBlockState(), 1, 2, 1, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.OAK_FENCE.defaultBlockState(), 5, 2, 1, boundingBox4);
            BlockState $$7 = (BlockState) Blocks.SPRUCE_STAIRS.defaultBlockState().m_61124_(StairBlock.FACING, Direction.NORTH);
            BlockState $$8 = (BlockState) Blocks.SPRUCE_STAIRS.defaultBlockState().m_61124_(StairBlock.FACING, Direction.EAST);
            BlockState $$9 = (BlockState) Blocks.SPRUCE_STAIRS.defaultBlockState().m_61124_(StairBlock.FACING, Direction.WEST);
            BlockState $$10 = (BlockState) Blocks.SPRUCE_STAIRS.defaultBlockState().m_61124_(StairBlock.FACING, Direction.SOUTH);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 4, 1, 6, 4, 1, $$7, $$7, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 4, 2, 0, 4, 7, $$8, $$8, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 6, 4, 2, 6, 4, 7, $$9, $$9, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 4, 8, 6, 4, 8, $$10, $$10, false);
            this.m_73434_(worldGenLevel0, (BlockState) $$7.m_61124_(StairBlock.SHAPE, StairsShape.OUTER_RIGHT), 0, 4, 1, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) $$7.m_61124_(StairBlock.SHAPE, StairsShape.OUTER_LEFT), 6, 4, 1, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) $$10.m_61124_(StairBlock.SHAPE, StairsShape.OUTER_LEFT), 0, 4, 8, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) $$10.m_61124_(StairBlock.SHAPE, StairsShape.OUTER_RIGHT), 6, 4, 8, boundingBox4);
            for (int $$11 = 2; $$11 <= 7; $$11 += 5) {
                for (int $$12 = 1; $$12 <= 5; $$12 += 4) {
                    this.m_73528_(worldGenLevel0, Blocks.OAK_LOG.defaultBlockState(), $$12, -1, $$11, boundingBox4);
                }
            }
            if (!this.spawnedWitch) {
                BlockPos $$13 = this.m_163582_(2, 2, 5);
                if (boundingBox4.isInside($$13)) {
                    this.spawnedWitch = true;
                    Witch $$14 = EntityType.WITCH.create(worldGenLevel0.m_6018_());
                    if ($$14 != null) {
                        $$14.m_21530_();
                        $$14.m_7678_((double) $$13.m_123341_() + 0.5, (double) $$13.m_123342_(), (double) $$13.m_123343_() + 0.5, 0.0F, 0.0F);
                        $$14.m_6518_(worldGenLevel0, worldGenLevel0.m_6436_($$13), MobSpawnType.STRUCTURE, null, null);
                        worldGenLevel0.m_47205_($$14);
                    }
                }
            }
            this.spawnCat(worldGenLevel0, boundingBox4);
        }
    }

    private void spawnCat(ServerLevelAccessor serverLevelAccessor0, BoundingBox boundingBox1) {
        if (!this.spawnedCat) {
            BlockPos $$2 = this.m_163582_(2, 2, 5);
            if (boundingBox1.isInside($$2)) {
                this.spawnedCat = true;
                Cat $$3 = EntityType.CAT.create(serverLevelAccessor0.getLevel());
                if ($$3 != null) {
                    $$3.m_21530_();
                    $$3.m_7678_((double) $$2.m_123341_() + 0.5, (double) $$2.m_123342_(), (double) $$2.m_123343_() + 0.5, 0.0F, 0.0F);
                    $$3.finalizeSpawn(serverLevelAccessor0, serverLevelAccessor0.m_6436_($$2), MobSpawnType.STRUCTURE, null, null);
                    serverLevelAccessor0.addFreshEntityWithPassengers($$3);
                }
            }
        }
    }
}