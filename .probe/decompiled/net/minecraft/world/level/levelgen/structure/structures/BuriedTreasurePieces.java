package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class BuriedTreasurePieces {

    public static class BuriedTreasurePiece extends StructurePiece {

        public BuriedTreasurePiece(BlockPos blockPos0) {
            super(StructurePieceType.BURIED_TREASURE_PIECE, 0, new BoundingBox(blockPos0));
        }

        public BuriedTreasurePiece(CompoundTag compoundTag0) {
            super(StructurePieceType.BURIED_TREASURE_PIECE, compoundTag0);
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1) {
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            int $$7 = worldGenLevel0.m_6924_(Heightmap.Types.OCEAN_FLOOR_WG, this.f_73383_.minX(), this.f_73383_.minZ());
            BlockPos.MutableBlockPos $$8 = new BlockPos.MutableBlockPos(this.f_73383_.minX(), $$7, this.f_73383_.minZ());
            while ($$8.m_123342_() > worldGenLevel0.m_141937_()) {
                BlockState $$9 = worldGenLevel0.m_8055_($$8);
                BlockState $$10 = worldGenLevel0.m_8055_($$8.m_7495_());
                if ($$10 == Blocks.SANDSTONE.defaultBlockState() || $$10 == Blocks.STONE.defaultBlockState() || $$10 == Blocks.ANDESITE.defaultBlockState() || $$10 == Blocks.GRANITE.defaultBlockState() || $$10 == Blocks.DIORITE.defaultBlockState()) {
                    BlockState $$11 = !$$9.m_60795_() && !this.isLiquid($$9) ? $$9 : Blocks.SAND.defaultBlockState();
                    for (Direction $$12 : Direction.values()) {
                        BlockPos $$13 = $$8.m_121945_($$12);
                        BlockState $$14 = worldGenLevel0.m_8055_($$13);
                        if ($$14.m_60795_() || this.isLiquid($$14)) {
                            BlockPos $$15 = $$13.below();
                            BlockState $$16 = worldGenLevel0.m_8055_($$15);
                            if (($$16.m_60795_() || this.isLiquid($$16)) && $$12 != Direction.UP) {
                                worldGenLevel0.m_7731_($$13, $$10, 3);
                            } else {
                                worldGenLevel0.m_7731_($$13, $$11, 3);
                            }
                        }
                    }
                    this.f_73383_ = new BoundingBox($$8);
                    this.m_226762_(worldGenLevel0, boundingBox4, randomSource3, $$8, BuiltInLootTables.BURIED_TREASURE, null);
                    return;
                }
                $$8.move(0, -1, 0);
            }
        }

        private boolean isLiquid(BlockState blockState0) {
            return blockState0 == Blocks.WATER.defaultBlockState() || blockState0 == Blocks.LAVA.defaultBlockState();
        }
    }
}