package net.minecraft.world.level.levelgen.structure.structures;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class NetherFortressPieces {

    private static final int MAX_DEPTH = 30;

    private static final int LOWEST_Y_POSITION = 10;

    public static final int MAGIC_START_Y = 64;

    static final NetherFortressPieces.PieceWeight[] BRIDGE_PIECE_WEIGHTS = new NetherFortressPieces.PieceWeight[] { new NetherFortressPieces.PieceWeight(NetherFortressPieces.BridgeStraight.class, 30, 0, true), new NetherFortressPieces.PieceWeight(NetherFortressPieces.BridgeCrossing.class, 10, 4), new NetherFortressPieces.PieceWeight(NetherFortressPieces.RoomCrossing.class, 10, 4), new NetherFortressPieces.PieceWeight(NetherFortressPieces.StairsRoom.class, 10, 3), new NetherFortressPieces.PieceWeight(NetherFortressPieces.MonsterThrone.class, 5, 2), new NetherFortressPieces.PieceWeight(NetherFortressPieces.CastleEntrance.class, 5, 1) };

    static final NetherFortressPieces.PieceWeight[] CASTLE_PIECE_WEIGHTS = new NetherFortressPieces.PieceWeight[] { new NetherFortressPieces.PieceWeight(NetherFortressPieces.CastleSmallCorridorPiece.class, 25, 0, true), new NetherFortressPieces.PieceWeight(NetherFortressPieces.CastleSmallCorridorCrossingPiece.class, 15, 5), new NetherFortressPieces.PieceWeight(NetherFortressPieces.CastleSmallCorridorRightTurnPiece.class, 5, 10), new NetherFortressPieces.PieceWeight(NetherFortressPieces.CastleSmallCorridorLeftTurnPiece.class, 5, 10), new NetherFortressPieces.PieceWeight(NetherFortressPieces.CastleCorridorStairsPiece.class, 10, 3, true), new NetherFortressPieces.PieceWeight(NetherFortressPieces.CastleCorridorTBalconyPiece.class, 7, 2), new NetherFortressPieces.PieceWeight(NetherFortressPieces.CastleStalkRoom.class, 5, 2) };

    static NetherFortressPieces.NetherBridgePiece findAndCreateBridgePieceFactory(NetherFortressPieces.PieceWeight netherFortressPiecesPieceWeight0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2, int int3, int int4, int int5, Direction direction6, int int7) {
        Class<? extends NetherFortressPieces.NetherBridgePiece> $$8 = netherFortressPiecesPieceWeight0.pieceClass;
        NetherFortressPieces.NetherBridgePiece $$9 = null;
        if ($$8 == NetherFortressPieces.BridgeStraight.class) {
            $$9 = NetherFortressPieces.BridgeStraight.createPiece(structurePieceAccessor1, randomSource2, int3, int4, int5, direction6, int7);
        } else if ($$8 == NetherFortressPieces.BridgeCrossing.class) {
            $$9 = NetherFortressPieces.BridgeCrossing.createPiece(structurePieceAccessor1, int3, int4, int5, direction6, int7);
        } else if ($$8 == NetherFortressPieces.RoomCrossing.class) {
            $$9 = NetherFortressPieces.RoomCrossing.createPiece(structurePieceAccessor1, int3, int4, int5, direction6, int7);
        } else if ($$8 == NetherFortressPieces.StairsRoom.class) {
            $$9 = NetherFortressPieces.StairsRoom.createPiece(structurePieceAccessor1, int3, int4, int5, int7, direction6);
        } else if ($$8 == NetherFortressPieces.MonsterThrone.class) {
            $$9 = NetherFortressPieces.MonsterThrone.createPiece(structurePieceAccessor1, int3, int4, int5, int7, direction6);
        } else if ($$8 == NetherFortressPieces.CastleEntrance.class) {
            $$9 = NetherFortressPieces.CastleEntrance.createPiece(structurePieceAccessor1, randomSource2, int3, int4, int5, direction6, int7);
        } else if ($$8 == NetherFortressPieces.CastleSmallCorridorPiece.class) {
            $$9 = NetherFortressPieces.CastleSmallCorridorPiece.createPiece(structurePieceAccessor1, int3, int4, int5, direction6, int7);
        } else if ($$8 == NetherFortressPieces.CastleSmallCorridorRightTurnPiece.class) {
            $$9 = NetherFortressPieces.CastleSmallCorridorRightTurnPiece.createPiece(structurePieceAccessor1, randomSource2, int3, int4, int5, direction6, int7);
        } else if ($$8 == NetherFortressPieces.CastleSmallCorridorLeftTurnPiece.class) {
            $$9 = NetherFortressPieces.CastleSmallCorridorLeftTurnPiece.createPiece(structurePieceAccessor1, randomSource2, int3, int4, int5, direction6, int7);
        } else if ($$8 == NetherFortressPieces.CastleCorridorStairsPiece.class) {
            $$9 = NetherFortressPieces.CastleCorridorStairsPiece.createPiece(structurePieceAccessor1, int3, int4, int5, direction6, int7);
        } else if ($$8 == NetherFortressPieces.CastleCorridorTBalconyPiece.class) {
            $$9 = NetherFortressPieces.CastleCorridorTBalconyPiece.createPiece(structurePieceAccessor1, int3, int4, int5, direction6, int7);
        } else if ($$8 == NetherFortressPieces.CastleSmallCorridorCrossingPiece.class) {
            $$9 = NetherFortressPieces.CastleSmallCorridorCrossingPiece.createPiece(structurePieceAccessor1, int3, int4, int5, direction6, int7);
        } else if ($$8 == NetherFortressPieces.CastleStalkRoom.class) {
            $$9 = NetherFortressPieces.CastleStalkRoom.createPiece(structurePieceAccessor1, int3, int4, int5, direction6, int7);
        }
        return $$9;
    }

    public static class BridgeCrossing extends NetherFortressPieces.NetherBridgePiece {

        private static final int WIDTH = 19;

        private static final int HEIGHT = 10;

        private static final int DEPTH = 19;

        public BridgeCrossing(int int0, BoundingBox boundingBox1, Direction direction2) {
            super(StructurePieceType.NETHER_FORTRESS_BRIDGE_CROSSING, int0, boundingBox1);
            this.m_73519_(direction2);
        }

        protected BridgeCrossing(int int0, int int1, Direction direction2) {
            super(StructurePieceType.NETHER_FORTRESS_BRIDGE_CROSSING, 0, StructurePiece.makeBoundingBox(int0, 64, int1, direction2, 19, 10, 19));
            this.m_73519_(direction2);
        }

        protected BridgeCrossing(StructurePieceType structurePieceType0, CompoundTag compoundTag1) {
            super(structurePieceType0, compoundTag1);
        }

        public BridgeCrossing(CompoundTag compoundTag0) {
            this(StructurePieceType.NETHER_FORTRESS_BRIDGE_CROSSING, compoundTag0);
        }

        @Override
        public void addChildren(StructurePiece structurePiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2) {
            this.m_228401_((NetherFortressPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 8, 3, false);
            this.m_228420_((NetherFortressPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 3, 8, false);
            this.m_228427_((NetherFortressPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 3, 8, false);
        }

        public static NetherFortressPieces.BridgeCrossing createPiece(StructurePieceAccessor structurePieceAccessor0, int int1, int int2, int int3, Direction direction4, int int5) {
            BoundingBox $$6 = BoundingBox.orientBox(int1, int2, int3, -8, -3, 0, 19, 10, 19, direction4);
            return m_228386_($$6) && structurePieceAccessor0.findCollisionPiece($$6) == null ? new NetherFortressPieces.BridgeCrossing(int5, $$6, direction4) : null;
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            this.m_73441_(worldGenLevel0, boundingBox4, 7, 3, 0, 11, 4, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 3, 7, 18, 4, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 8, 5, 0, 10, 7, 18, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 5, 8, 18, 7, 10, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 7, 5, 0, 7, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 7, 5, 11, 7, 5, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 11, 5, 0, 11, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 11, 5, 11, 11, 5, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 5, 7, 7, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 11, 5, 7, 18, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 5, 11, 7, 5, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 11, 5, 11, 18, 5, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 7, 2, 0, 11, 2, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 7, 2, 13, 11, 2, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 7, 0, 0, 11, 1, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 7, 0, 15, 11, 1, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            for (int $$7 = 7; $$7 <= 11; $$7++) {
                for (int $$8 = 0; $$8 <= 2; $$8++) {
                    this.m_73528_(worldGenLevel0, Blocks.NETHER_BRICKS.defaultBlockState(), $$7, -1, $$8, boundingBox4);
                    this.m_73528_(worldGenLevel0, Blocks.NETHER_BRICKS.defaultBlockState(), $$7, -1, 18 - $$8, boundingBox4);
                }
            }
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 7, 5, 2, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 13, 2, 7, 18, 2, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 0, 7, 3, 1, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 15, 0, 7, 18, 1, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            for (int $$9 = 0; $$9 <= 2; $$9++) {
                for (int $$10 = 7; $$10 <= 11; $$10++) {
                    this.m_73528_(worldGenLevel0, Blocks.NETHER_BRICKS.defaultBlockState(), $$9, -1, $$10, boundingBox4);
                    this.m_73528_(worldGenLevel0, Blocks.NETHER_BRICKS.defaultBlockState(), 18 - $$9, -1, $$10, boundingBox4);
                }
            }
        }
    }

    public static class BridgeEndFiller extends NetherFortressPieces.NetherBridgePiece {

        private static final int WIDTH = 5;

        private static final int HEIGHT = 10;

        private static final int DEPTH = 8;

        private final int selfSeed;

        public BridgeEndFiller(int int0, RandomSource randomSource1, BoundingBox boundingBox2, Direction direction3) {
            super(StructurePieceType.NETHER_FORTRESS_BRIDGE_END_FILLER, int0, boundingBox2);
            this.m_73519_(direction3);
            this.selfSeed = randomSource1.nextInt();
        }

        public BridgeEndFiller(CompoundTag compoundTag0) {
            super(StructurePieceType.NETHER_FORTRESS_BRIDGE_END_FILLER, compoundTag0);
            this.selfSeed = compoundTag0.getInt("Seed");
        }

        public static NetherFortressPieces.BridgeEndFiller createPiece(StructurePieceAccessor structurePieceAccessor0, RandomSource randomSource1, int int2, int int3, int int4, Direction direction5, int int6) {
            BoundingBox $$7 = BoundingBox.orientBox(int2, int3, int4, -1, -3, 0, 5, 10, 8, direction5);
            return m_228386_($$7) && structurePieceAccessor0.findCollisionPiece($$7) == null ? new NetherFortressPieces.BridgeEndFiller(int6, randomSource1, $$7, direction5) : null;
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1) {
            super.addAdditionalSaveData(structurePieceSerializationContext0, compoundTag1);
            compoundTag1.putInt("Seed", this.selfSeed);
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            RandomSource $$7 = RandomSource.create((long) this.selfSeed);
            for (int $$8 = 0; $$8 <= 4; $$8++) {
                for (int $$9 = 3; $$9 <= 4; $$9++) {
                    int $$10 = $$7.nextInt(8);
                    this.m_73441_(worldGenLevel0, boundingBox4, $$8, $$9, 0, $$8, $$9, $$10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
                }
            }
            int $$11 = $$7.nextInt(8);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 5, 0, 0, 5, $$11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            $$11 = $$7.nextInt(8);
            this.m_73441_(worldGenLevel0, boundingBox4, 4, 5, 0, 4, 5, $$11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            for (int $$13 = 0; $$13 <= 4; $$13++) {
                int $$14 = $$7.nextInt(5);
                this.m_73441_(worldGenLevel0, boundingBox4, $$13, 2, 0, $$13, 2, $$14, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            }
            for (int $$15 = 0; $$15 <= 4; $$15++) {
                for (int $$16 = 0; $$16 <= 1; $$16++) {
                    int $$17 = $$7.nextInt(3);
                    this.m_73441_(worldGenLevel0, boundingBox4, $$15, $$16, 0, $$15, $$16, $$17, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
                }
            }
        }
    }

    public static class BridgeStraight extends NetherFortressPieces.NetherBridgePiece {

        private static final int WIDTH = 5;

        private static final int HEIGHT = 10;

        private static final int DEPTH = 19;

        public BridgeStraight(int int0, RandomSource randomSource1, BoundingBox boundingBox2, Direction direction3) {
            super(StructurePieceType.NETHER_FORTRESS_BRIDGE_STRAIGHT, int0, boundingBox2);
            this.m_73519_(direction3);
        }

        public BridgeStraight(CompoundTag compoundTag0) {
            super(StructurePieceType.NETHER_FORTRESS_BRIDGE_STRAIGHT, compoundTag0);
        }

        @Override
        public void addChildren(StructurePiece structurePiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2) {
            this.m_228401_((NetherFortressPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 1, 3, false);
        }

        public static NetherFortressPieces.BridgeStraight createPiece(StructurePieceAccessor structurePieceAccessor0, RandomSource randomSource1, int int2, int int3, int int4, Direction direction5, int int6) {
            BoundingBox $$7 = BoundingBox.orientBox(int2, int3, int4, -1, -3, 0, 5, 10, 19, direction5);
            return m_228386_($$7) && structurePieceAccessor0.findCollisionPiece($$7) == null ? new NetherFortressPieces.BridgeStraight(int6, randomSource1, $$7, direction5) : null;
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 3, 0, 4, 4, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 5, 0, 3, 7, 18, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 5, 0, 0, 5, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 4, 5, 0, 4, 5, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 0, 4, 2, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 13, 4, 2, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 0, 0, 4, 1, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 0, 15, 4, 1, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            for (int $$7 = 0; $$7 <= 4; $$7++) {
                for (int $$8 = 0; $$8 <= 2; $$8++) {
                    this.m_73528_(worldGenLevel0, Blocks.NETHER_BRICKS.defaultBlockState(), $$7, -1, $$8, boundingBox4);
                    this.m_73528_(worldGenLevel0, Blocks.NETHER_BRICKS.defaultBlockState(), $$7, -1, 18 - $$8, boundingBox4);
                }
            }
            BlockState $$9 = (BlockState) ((BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52309_, true)).m_61124_(FenceBlock.f_52311_, true);
            BlockState $$10 = (BlockState) $$9.m_61124_(FenceBlock.f_52310_, true);
            BlockState $$11 = (BlockState) $$9.m_61124_(FenceBlock.f_52312_, true);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 1, 1, 0, 4, 1, $$10, $$10, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 3, 4, 0, 4, 4, $$10, $$10, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 3, 14, 0, 4, 14, $$10, $$10, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 1, 17, 0, 4, 17, $$10, $$10, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 4, 1, 1, 4, 4, 1, $$11, $$11, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 4, 3, 4, 4, 4, 4, $$11, $$11, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 4, 3, 14, 4, 4, 14, $$11, $$11, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 4, 1, 17, 4, 4, 17, $$11, $$11, false);
        }
    }

    public static class CastleCorridorStairsPiece extends NetherFortressPieces.NetherBridgePiece {

        private static final int WIDTH = 5;

        private static final int HEIGHT = 14;

        private static final int DEPTH = 10;

        public CastleCorridorStairsPiece(int int0, BoundingBox boundingBox1, Direction direction2) {
            super(StructurePieceType.NETHER_FORTRESS_CASTLE_CORRIDOR_STAIRS, int0, boundingBox1);
            this.m_73519_(direction2);
        }

        public CastleCorridorStairsPiece(CompoundTag compoundTag0) {
            super(StructurePieceType.NETHER_FORTRESS_CASTLE_CORRIDOR_STAIRS, compoundTag0);
        }

        @Override
        public void addChildren(StructurePiece structurePiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2) {
            this.m_228401_((NetherFortressPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 1, 0, true);
        }

        public static NetherFortressPieces.CastleCorridorStairsPiece createPiece(StructurePieceAccessor structurePieceAccessor0, int int1, int int2, int int3, Direction direction4, int int5) {
            BoundingBox $$6 = BoundingBox.orientBox(int1, int2, int3, -1, -7, 0, 5, 14, 10, direction4);
            return m_228386_($$6) && structurePieceAccessor0.findCollisionPiece($$6) == null ? new NetherFortressPieces.CastleCorridorStairsPiece(int5, $$6, direction4) : null;
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            BlockState $$7 = (BlockState) Blocks.NETHER_BRICK_STAIRS.defaultBlockState().m_61124_(StairBlock.FACING, Direction.SOUTH);
            BlockState $$8 = (BlockState) ((BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52309_, true)).m_61124_(FenceBlock.f_52311_, true);
            for (int $$9 = 0; $$9 <= 9; $$9++) {
                int $$10 = Math.max(1, 7 - $$9);
                int $$11 = Math.min(Math.max($$10 + 5, 14 - $$9), 13);
                int $$12 = $$9;
                this.m_73441_(worldGenLevel0, boundingBox4, 0, 0, $$9, 4, $$10, $$9, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
                this.m_73441_(worldGenLevel0, boundingBox4, 1, $$10 + 1, $$9, 3, $$11 - 1, $$9, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
                if ($$9 <= 6) {
                    this.m_73434_(worldGenLevel0, $$7, 1, $$10 + 1, $$9, boundingBox4);
                    this.m_73434_(worldGenLevel0, $$7, 2, $$10 + 1, $$9, boundingBox4);
                    this.m_73434_(worldGenLevel0, $$7, 3, $$10 + 1, $$9, boundingBox4);
                }
                this.m_73441_(worldGenLevel0, boundingBox4, 0, $$11, $$9, 4, $$11, $$9, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
                this.m_73441_(worldGenLevel0, boundingBox4, 0, $$10 + 1, $$9, 0, $$11 - 1, $$9, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
                this.m_73441_(worldGenLevel0, boundingBox4, 4, $$10 + 1, $$9, 4, $$11 - 1, $$9, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
                if (($$9 & 1) == 0) {
                    this.m_73441_(worldGenLevel0, boundingBox4, 0, $$10 + 2, $$9, 0, $$10 + 3, $$9, $$8, $$8, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, 4, $$10 + 2, $$9, 4, $$10 + 3, $$9, $$8, $$8, false);
                }
                for (int $$13 = 0; $$13 <= 4; $$13++) {
                    this.m_73528_(worldGenLevel0, Blocks.NETHER_BRICKS.defaultBlockState(), $$13, -1, $$12, boundingBox4);
                }
            }
        }
    }

    public static class CastleCorridorTBalconyPiece extends NetherFortressPieces.NetherBridgePiece {

        private static final int WIDTH = 9;

        private static final int HEIGHT = 7;

        private static final int DEPTH = 9;

        public CastleCorridorTBalconyPiece(int int0, BoundingBox boundingBox1, Direction direction2) {
            super(StructurePieceType.NETHER_FORTRESS_CASTLE_CORRIDOR_T_BALCONY, int0, boundingBox1);
            this.m_73519_(direction2);
        }

        public CastleCorridorTBalconyPiece(CompoundTag compoundTag0) {
            super(StructurePieceType.NETHER_FORTRESS_CASTLE_CORRIDOR_T_BALCONY, compoundTag0);
        }

        @Override
        public void addChildren(StructurePiece structurePiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2) {
            int $$3 = 1;
            Direction $$4 = this.m_73549_();
            if ($$4 == Direction.WEST || $$4 == Direction.NORTH) {
                $$3 = 5;
            }
            this.m_228420_((NetherFortressPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 0, $$3, randomSource2.nextInt(8) > 0);
            this.m_228427_((NetherFortressPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 0, $$3, randomSource2.nextInt(8) > 0);
        }

        public static NetherFortressPieces.CastleCorridorTBalconyPiece createPiece(StructurePieceAccessor structurePieceAccessor0, int int1, int int2, int int3, Direction direction4, int int5) {
            BoundingBox $$6 = BoundingBox.orientBox(int1, int2, int3, -3, 0, 0, 9, 7, 9, direction4);
            return m_228386_($$6) && structurePieceAccessor0.findCollisionPiece($$6) == null ? new NetherFortressPieces.CastleCorridorTBalconyPiece(int5, $$6, direction4) : null;
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            BlockState $$7 = (BlockState) ((BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52309_, true)).m_61124_(FenceBlock.f_52311_, true);
            BlockState $$8 = (BlockState) ((BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52312_, true)).m_61124_(FenceBlock.f_52310_, true);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 0, 0, 8, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 0, 8, 5, 8, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 6, 0, 8, 6, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 0, 2, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 6, 2, 0, 8, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 3, 0, 1, 4, 0, $$8, $$8, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 7, 3, 0, 7, 4, 0, $$8, $$8, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 4, 8, 2, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 1, 4, 2, 2, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 6, 1, 4, 7, 2, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 3, 8, 7, 3, 8, $$8, $$8, false);
            this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52310_, true)).m_61124_(FenceBlock.f_52311_, true), 0, 3, 8, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52312_, true)).m_61124_(FenceBlock.f_52311_, true), 8, 3, 8, boundingBox4);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 3, 6, 0, 3, 7, $$7, $$7, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 8, 3, 6, 8, 3, 7, $$7, $$7, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 3, 4, 0, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 8, 3, 4, 8, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 3, 5, 2, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 6, 3, 5, 7, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 4, 5, 1, 5, 5, $$8, $$8, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 7, 4, 5, 7, 5, 5, $$8, $$8, false);
            for (int $$9 = 0; $$9 <= 5; $$9++) {
                for (int $$10 = 0; $$10 <= 8; $$10++) {
                    this.m_73528_(worldGenLevel0, Blocks.NETHER_BRICKS.defaultBlockState(), $$10, -1, $$9, boundingBox4);
                }
            }
        }
    }

    public static class CastleEntrance extends NetherFortressPieces.NetherBridgePiece {

        private static final int WIDTH = 13;

        private static final int HEIGHT = 14;

        private static final int DEPTH = 13;

        public CastleEntrance(int int0, RandomSource randomSource1, BoundingBox boundingBox2, Direction direction3) {
            super(StructurePieceType.NETHER_FORTRESS_CASTLE_ENTRANCE, int0, boundingBox2);
            this.m_73519_(direction3);
        }

        public CastleEntrance(CompoundTag compoundTag0) {
            super(StructurePieceType.NETHER_FORTRESS_CASTLE_ENTRANCE, compoundTag0);
        }

        @Override
        public void addChildren(StructurePiece structurePiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2) {
            this.m_228401_((NetherFortressPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 5, 3, true);
        }

        public static NetherFortressPieces.CastleEntrance createPiece(StructurePieceAccessor structurePieceAccessor0, RandomSource randomSource1, int int2, int int3, int int4, Direction direction5, int int6) {
            BoundingBox $$7 = BoundingBox.orientBox(int2, int3, int4, -5, -3, 0, 13, 14, 13, direction5);
            return m_228386_($$7) && structurePieceAccessor0.findCollisionPiece($$7) == null ? new NetherFortressPieces.CastleEntrance(int6, randomSource1, $$7, direction5) : null;
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 3, 0, 12, 4, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 5, 0, 12, 13, 12, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 5, 0, 1, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 11, 5, 0, 12, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 2, 5, 11, 4, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 8, 5, 11, 10, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 9, 11, 7, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 2, 5, 0, 4, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 8, 5, 0, 10, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 9, 0, 7, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 2, 11, 2, 10, 12, 10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 8, 0, 7, 8, 0, Blocks.NETHER_BRICK_FENCE.defaultBlockState(), Blocks.NETHER_BRICK_FENCE.defaultBlockState(), false);
            BlockState $$7 = (BlockState) ((BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52312_, true)).m_61124_(FenceBlock.f_52310_, true);
            BlockState $$8 = (BlockState) ((BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52309_, true)).m_61124_(FenceBlock.f_52311_, true);
            for (int $$9 = 1; $$9 <= 11; $$9 += 2) {
                this.m_73441_(worldGenLevel0, boundingBox4, $$9, 10, 0, $$9, 11, 0, $$7, $$7, false);
                this.m_73441_(worldGenLevel0, boundingBox4, $$9, 10, 12, $$9, 11, 12, $$7, $$7, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 0, 10, $$9, 0, 11, $$9, $$8, $$8, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 12, 10, $$9, 12, 11, $$9, $$8, $$8, false);
                this.m_73434_(worldGenLevel0, Blocks.NETHER_BRICKS.defaultBlockState(), $$9, 13, 0, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.NETHER_BRICKS.defaultBlockState(), $$9, 13, 12, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.NETHER_BRICKS.defaultBlockState(), 0, 13, $$9, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.NETHER_BRICKS.defaultBlockState(), 12, 13, $$9, boundingBox4);
                if ($$9 != 11) {
                    this.m_73434_(worldGenLevel0, $$7, $$9 + 1, 13, 0, boundingBox4);
                    this.m_73434_(worldGenLevel0, $$7, $$9 + 1, 13, 12, boundingBox4);
                    this.m_73434_(worldGenLevel0, $$8, 0, 13, $$9 + 1, boundingBox4);
                    this.m_73434_(worldGenLevel0, $$8, 12, 13, $$9 + 1, boundingBox4);
                }
            }
            this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52309_, true)).m_61124_(FenceBlock.f_52310_, true), 0, 13, 0, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52311_, true)).m_61124_(FenceBlock.f_52310_, true), 0, 13, 12, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52311_, true)).m_61124_(FenceBlock.f_52312_, true), 12, 13, 12, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52309_, true)).m_61124_(FenceBlock.f_52312_, true), 12, 13, 0, boundingBox4);
            for (int $$10 = 3; $$10 <= 9; $$10 += 2) {
                this.m_73441_(worldGenLevel0, boundingBox4, 1, 7, $$10, 1, 8, $$10, (BlockState) $$8.m_61124_(FenceBlock.f_52312_, true), (BlockState) $$8.m_61124_(FenceBlock.f_52312_, true), false);
                this.m_73441_(worldGenLevel0, boundingBox4, 11, 7, $$10, 11, 8, $$10, (BlockState) $$8.m_61124_(FenceBlock.f_52310_, true), (BlockState) $$8.m_61124_(FenceBlock.f_52310_, true), false);
            }
            this.m_73441_(worldGenLevel0, boundingBox4, 4, 2, 0, 8, 2, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 4, 12, 2, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 4, 0, 0, 8, 1, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 4, 0, 9, 8, 1, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 0, 4, 3, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 9, 0, 4, 12, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            for (int $$11 = 4; $$11 <= 8; $$11++) {
                for (int $$12 = 0; $$12 <= 2; $$12++) {
                    this.m_73528_(worldGenLevel0, Blocks.NETHER_BRICKS.defaultBlockState(), $$11, -1, $$12, boundingBox4);
                    this.m_73528_(worldGenLevel0, Blocks.NETHER_BRICKS.defaultBlockState(), $$11, -1, 12 - $$12, boundingBox4);
                }
            }
            for (int $$13 = 0; $$13 <= 2; $$13++) {
                for (int $$14 = 4; $$14 <= 8; $$14++) {
                    this.m_73528_(worldGenLevel0, Blocks.NETHER_BRICKS.defaultBlockState(), $$13, -1, $$14, boundingBox4);
                    this.m_73528_(worldGenLevel0, Blocks.NETHER_BRICKS.defaultBlockState(), 12 - $$13, -1, $$14, boundingBox4);
                }
            }
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 5, 5, 7, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 6, 1, 6, 6, 4, 6, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.m_73434_(worldGenLevel0, Blocks.NETHER_BRICKS.defaultBlockState(), 6, 0, 6, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.LAVA.defaultBlockState(), 6, 5, 6, boundingBox4);
            BlockPos $$15 = this.m_163582_(6, 5, 6);
            if (boundingBox4.isInside($$15)) {
                worldGenLevel0.m_186469_($$15, Fluids.LAVA, 0);
            }
        }
    }

    public static class CastleSmallCorridorCrossingPiece extends NetherFortressPieces.NetherBridgePiece {

        private static final int WIDTH = 5;

        private static final int HEIGHT = 7;

        private static final int DEPTH = 5;

        public CastleSmallCorridorCrossingPiece(int int0, BoundingBox boundingBox1, Direction direction2) {
            super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_CROSSING, int0, boundingBox1);
            this.m_73519_(direction2);
        }

        public CastleSmallCorridorCrossingPiece(CompoundTag compoundTag0) {
            super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_CROSSING, compoundTag0);
        }

        @Override
        public void addChildren(StructurePiece structurePiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2) {
            this.m_228401_((NetherFortressPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 1, 0, true);
            this.m_228420_((NetherFortressPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 0, 1, true);
            this.m_228427_((NetherFortressPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 0, 1, true);
        }

        public static NetherFortressPieces.CastleSmallCorridorCrossingPiece createPiece(StructurePieceAccessor structurePieceAccessor0, int int1, int int2, int int3, Direction direction4, int int5) {
            BoundingBox $$6 = BoundingBox.orientBox(int1, int2, int3, -1, 0, 0, 5, 7, 5, direction4);
            return m_228386_($$6) && structurePieceAccessor0.findCollisionPiece($$6) == null ? new NetherFortressPieces.CastleSmallCorridorCrossingPiece(int5, $$6, direction4) : null;
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 0, 4, 5, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 0, 0, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 4, 2, 0, 4, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 4, 0, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 4, 2, 4, 4, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            for (int $$7 = 0; $$7 <= 4; $$7++) {
                for (int $$8 = 0; $$8 <= 4; $$8++) {
                    this.m_73528_(worldGenLevel0, Blocks.NETHER_BRICKS.defaultBlockState(), $$7, -1, $$8, boundingBox4);
                }
            }
        }
    }

    public static class CastleSmallCorridorLeftTurnPiece extends NetherFortressPieces.NetherBridgePiece {

        private static final int WIDTH = 5;

        private static final int HEIGHT = 7;

        private static final int DEPTH = 5;

        private boolean isNeedingChest;

        public CastleSmallCorridorLeftTurnPiece(int int0, RandomSource randomSource1, BoundingBox boundingBox2, Direction direction3) {
            super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_LEFT_TURN, int0, boundingBox2);
            this.m_73519_(direction3);
            this.isNeedingChest = randomSource1.nextInt(3) == 0;
        }

        public CastleSmallCorridorLeftTurnPiece(CompoundTag compoundTag0) {
            super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_LEFT_TURN, compoundTag0);
            this.isNeedingChest = compoundTag0.getBoolean("Chest");
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1) {
            super.addAdditionalSaveData(structurePieceSerializationContext0, compoundTag1);
            compoundTag1.putBoolean("Chest", this.isNeedingChest);
        }

        @Override
        public void addChildren(StructurePiece structurePiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2) {
            this.m_228420_((NetherFortressPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 0, 1, true);
        }

        public static NetherFortressPieces.CastleSmallCorridorLeftTurnPiece createPiece(StructurePieceAccessor structurePieceAccessor0, RandomSource randomSource1, int int2, int int3, int int4, Direction direction5, int int6) {
            BoundingBox $$7 = BoundingBox.orientBox(int2, int3, int4, -1, 0, 0, 5, 7, 5, direction5);
            return m_228386_($$7) && structurePieceAccessor0.findCollisionPiece($$7) == null ? new NetherFortressPieces.CastleSmallCorridorLeftTurnPiece(int6, randomSource1, $$7, direction5) : null;
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 0, 4, 5, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            BlockState $$7 = (BlockState) ((BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52312_, true)).m_61124_(FenceBlock.f_52310_, true);
            BlockState $$8 = (BlockState) ((BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52309_, true)).m_61124_(FenceBlock.f_52311_, true);
            this.m_73441_(worldGenLevel0, boundingBox4, 4, 2, 0, 4, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 4, 3, 1, 4, 4, 1, $$8, $$8, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 4, 3, 3, 4, 4, 3, $$8, $$8, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 0, 0, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 4, 3, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 3, 4, 1, 4, 4, $$7, $$7, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 3, 3, 4, 3, 4, 4, $$7, $$7, false);
            if (this.isNeedingChest && boundingBox4.isInside(this.m_163582_(3, 2, 3))) {
                this.isNeedingChest = false;
                this.m_213787_(worldGenLevel0, boundingBox4, randomSource3, 3, 2, 3, BuiltInLootTables.NETHER_BRIDGE);
            }
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            for (int $$9 = 0; $$9 <= 4; $$9++) {
                for (int $$10 = 0; $$10 <= 4; $$10++) {
                    this.m_73528_(worldGenLevel0, Blocks.NETHER_BRICKS.defaultBlockState(), $$9, -1, $$10, boundingBox4);
                }
            }
        }
    }

    public static class CastleSmallCorridorPiece extends NetherFortressPieces.NetherBridgePiece {

        private static final int WIDTH = 5;

        private static final int HEIGHT = 7;

        private static final int DEPTH = 5;

        public CastleSmallCorridorPiece(int int0, BoundingBox boundingBox1, Direction direction2) {
            super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR, int0, boundingBox1);
            this.m_73519_(direction2);
        }

        public CastleSmallCorridorPiece(CompoundTag compoundTag0) {
            super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR, compoundTag0);
        }

        @Override
        public void addChildren(StructurePiece structurePiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2) {
            this.m_228401_((NetherFortressPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 1, 0, true);
        }

        public static NetherFortressPieces.CastleSmallCorridorPiece createPiece(StructurePieceAccessor structurePieceAccessor0, int int1, int int2, int int3, Direction direction4, int int5) {
            BoundingBox $$6 = BoundingBox.orientBox(int1, int2, int3, -1, 0, 0, 5, 7, 5, direction4);
            return m_228386_($$6) && structurePieceAccessor0.findCollisionPiece($$6) == null ? new NetherFortressPieces.CastleSmallCorridorPiece(int5, $$6, direction4) : null;
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 0, 4, 5, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            BlockState $$7 = (BlockState) ((BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52309_, true)).m_61124_(FenceBlock.f_52311_, true);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 0, 0, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 4, 2, 0, 4, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 3, 1, 0, 4, 1, $$7, $$7, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 3, 3, 0, 4, 3, $$7, $$7, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 4, 3, 1, 4, 4, 1, $$7, $$7, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 4, 3, 3, 4, 4, 3, $$7, $$7, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            for (int $$8 = 0; $$8 <= 4; $$8++) {
                for (int $$9 = 0; $$9 <= 4; $$9++) {
                    this.m_73528_(worldGenLevel0, Blocks.NETHER_BRICKS.defaultBlockState(), $$8, -1, $$9, boundingBox4);
                }
            }
        }
    }

    public static class CastleSmallCorridorRightTurnPiece extends NetherFortressPieces.NetherBridgePiece {

        private static final int WIDTH = 5;

        private static final int HEIGHT = 7;

        private static final int DEPTH = 5;

        private boolean isNeedingChest;

        public CastleSmallCorridorRightTurnPiece(int int0, RandomSource randomSource1, BoundingBox boundingBox2, Direction direction3) {
            super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_RIGHT_TURN, int0, boundingBox2);
            this.m_73519_(direction3);
            this.isNeedingChest = randomSource1.nextInt(3) == 0;
        }

        public CastleSmallCorridorRightTurnPiece(CompoundTag compoundTag0) {
            super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_RIGHT_TURN, compoundTag0);
            this.isNeedingChest = compoundTag0.getBoolean("Chest");
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1) {
            super.addAdditionalSaveData(structurePieceSerializationContext0, compoundTag1);
            compoundTag1.putBoolean("Chest", this.isNeedingChest);
        }

        @Override
        public void addChildren(StructurePiece structurePiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2) {
            this.m_228427_((NetherFortressPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 0, 1, true);
        }

        public static NetherFortressPieces.CastleSmallCorridorRightTurnPiece createPiece(StructurePieceAccessor structurePieceAccessor0, RandomSource randomSource1, int int2, int int3, int int4, Direction direction5, int int6) {
            BoundingBox $$7 = BoundingBox.orientBox(int2, int3, int4, -1, 0, 0, 5, 7, 5, direction5);
            return m_228386_($$7) && structurePieceAccessor0.findCollisionPiece($$7) == null ? new NetherFortressPieces.CastleSmallCorridorRightTurnPiece(int6, randomSource1, $$7, direction5) : null;
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 0, 4, 5, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            BlockState $$7 = (BlockState) ((BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52312_, true)).m_61124_(FenceBlock.f_52310_, true);
            BlockState $$8 = (BlockState) ((BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52309_, true)).m_61124_(FenceBlock.f_52311_, true);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 0, 0, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 3, 1, 0, 4, 1, $$8, $$8, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 3, 3, 0, 4, 3, $$8, $$8, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 4, 2, 0, 4, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 2, 4, 4, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 3, 4, 1, 4, 4, $$7, $$7, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 3, 3, 4, 3, 4, 4, $$7, $$7, false);
            if (this.isNeedingChest && boundingBox4.isInside(this.m_163582_(1, 2, 3))) {
                this.isNeedingChest = false;
                this.m_213787_(worldGenLevel0, boundingBox4, randomSource3, 1, 2, 3, BuiltInLootTables.NETHER_BRIDGE);
            }
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            for (int $$9 = 0; $$9 <= 4; $$9++) {
                for (int $$10 = 0; $$10 <= 4; $$10++) {
                    this.m_73528_(worldGenLevel0, Blocks.NETHER_BRICKS.defaultBlockState(), $$9, -1, $$10, boundingBox4);
                }
            }
        }
    }

    public static class CastleStalkRoom extends NetherFortressPieces.NetherBridgePiece {

        private static final int WIDTH = 13;

        private static final int HEIGHT = 14;

        private static final int DEPTH = 13;

        public CastleStalkRoom(int int0, BoundingBox boundingBox1, Direction direction2) {
            super(StructurePieceType.NETHER_FORTRESS_CASTLE_STALK_ROOM, int0, boundingBox1);
            this.m_73519_(direction2);
        }

        public CastleStalkRoom(CompoundTag compoundTag0) {
            super(StructurePieceType.NETHER_FORTRESS_CASTLE_STALK_ROOM, compoundTag0);
        }

        @Override
        public void addChildren(StructurePiece structurePiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2) {
            this.m_228401_((NetherFortressPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 5, 3, true);
            this.m_228401_((NetherFortressPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 5, 11, true);
        }

        public static NetherFortressPieces.CastleStalkRoom createPiece(StructurePieceAccessor structurePieceAccessor0, int int1, int int2, int int3, Direction direction4, int int5) {
            BoundingBox $$6 = BoundingBox.orientBox(int1, int2, int3, -5, -3, 0, 13, 14, 13, direction4);
            return m_228386_($$6) && structurePieceAccessor0.findCollisionPiece($$6) == null ? new NetherFortressPieces.CastleStalkRoom(int5, $$6, direction4) : null;
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 3, 0, 12, 4, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 5, 0, 12, 13, 12, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 5, 0, 1, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 11, 5, 0, 12, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 2, 5, 11, 4, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 8, 5, 11, 10, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 9, 11, 7, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 2, 5, 0, 4, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 8, 5, 0, 10, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 9, 0, 7, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 2, 11, 2, 10, 12, 10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            BlockState $$7 = (BlockState) ((BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52312_, true)).m_61124_(FenceBlock.f_52310_, true);
            BlockState $$8 = (BlockState) ((BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52309_, true)).m_61124_(FenceBlock.f_52311_, true);
            BlockState $$9 = (BlockState) $$8.m_61124_(FenceBlock.f_52312_, true);
            BlockState $$10 = (BlockState) $$8.m_61124_(FenceBlock.f_52310_, true);
            for (int $$11 = 1; $$11 <= 11; $$11 += 2) {
                this.m_73441_(worldGenLevel0, boundingBox4, $$11, 10, 0, $$11, 11, 0, $$7, $$7, false);
                this.m_73441_(worldGenLevel0, boundingBox4, $$11, 10, 12, $$11, 11, 12, $$7, $$7, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 0, 10, $$11, 0, 11, $$11, $$8, $$8, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 12, 10, $$11, 12, 11, $$11, $$8, $$8, false);
                this.m_73434_(worldGenLevel0, Blocks.NETHER_BRICKS.defaultBlockState(), $$11, 13, 0, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.NETHER_BRICKS.defaultBlockState(), $$11, 13, 12, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.NETHER_BRICKS.defaultBlockState(), 0, 13, $$11, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.NETHER_BRICKS.defaultBlockState(), 12, 13, $$11, boundingBox4);
                if ($$11 != 11) {
                    this.m_73434_(worldGenLevel0, $$7, $$11 + 1, 13, 0, boundingBox4);
                    this.m_73434_(worldGenLevel0, $$7, $$11 + 1, 13, 12, boundingBox4);
                    this.m_73434_(worldGenLevel0, $$8, 0, 13, $$11 + 1, boundingBox4);
                    this.m_73434_(worldGenLevel0, $$8, 12, 13, $$11 + 1, boundingBox4);
                }
            }
            this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52309_, true)).m_61124_(FenceBlock.f_52310_, true), 0, 13, 0, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52311_, true)).m_61124_(FenceBlock.f_52310_, true), 0, 13, 12, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52311_, true)).m_61124_(FenceBlock.f_52312_, true), 12, 13, 12, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52309_, true)).m_61124_(FenceBlock.f_52312_, true), 12, 13, 0, boundingBox4);
            for (int $$12 = 3; $$12 <= 9; $$12 += 2) {
                this.m_73441_(worldGenLevel0, boundingBox4, 1, 7, $$12, 1, 8, $$12, $$9, $$9, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 11, 7, $$12, 11, 8, $$12, $$10, $$10, false);
            }
            BlockState $$13 = (BlockState) Blocks.NETHER_BRICK_STAIRS.defaultBlockState().m_61124_(StairBlock.FACING, Direction.NORTH);
            for (int $$14 = 0; $$14 <= 6; $$14++) {
                int $$15 = $$14 + 4;
                for (int $$16 = 5; $$16 <= 7; $$16++) {
                    this.m_73434_(worldGenLevel0, $$13, $$16, 5 + $$14, $$15, boundingBox4);
                }
                if ($$15 >= 5 && $$15 <= 8) {
                    this.m_73441_(worldGenLevel0, boundingBox4, 5, 5, $$15, 7, $$14 + 4, $$15, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
                } else if ($$15 >= 9 && $$15 <= 10) {
                    this.m_73441_(worldGenLevel0, boundingBox4, 5, 8, $$15, 7, $$14 + 4, $$15, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
                }
                if ($$14 >= 1) {
                    this.m_73441_(worldGenLevel0, boundingBox4, 5, 6 + $$14, $$15, 7, 9 + $$14, $$15, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
                }
            }
            for (int $$17 = 5; $$17 <= 7; $$17++) {
                this.m_73434_(worldGenLevel0, $$13, $$17, 12, 11, boundingBox4);
            }
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 6, 7, 5, 7, 7, $$10, $$10, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 7, 6, 7, 7, 7, 7, $$9, $$9, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 13, 12, 7, 13, 12, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 2, 5, 2, 3, 5, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 2, 5, 9, 3, 5, 10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 2, 5, 4, 2, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 9, 5, 2, 10, 5, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 9, 5, 9, 10, 5, 10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 10, 5, 4, 10, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            BlockState $$18 = (BlockState) $$13.m_61124_(StairBlock.FACING, Direction.EAST);
            BlockState $$19 = (BlockState) $$13.m_61124_(StairBlock.FACING, Direction.WEST);
            this.m_73434_(worldGenLevel0, $$19, 4, 5, 2, boundingBox4);
            this.m_73434_(worldGenLevel0, $$19, 4, 5, 3, boundingBox4);
            this.m_73434_(worldGenLevel0, $$19, 4, 5, 9, boundingBox4);
            this.m_73434_(worldGenLevel0, $$19, 4, 5, 10, boundingBox4);
            this.m_73434_(worldGenLevel0, $$18, 8, 5, 2, boundingBox4);
            this.m_73434_(worldGenLevel0, $$18, 8, 5, 3, boundingBox4);
            this.m_73434_(worldGenLevel0, $$18, 8, 5, 9, boundingBox4);
            this.m_73434_(worldGenLevel0, $$18, 8, 5, 10, boundingBox4);
            this.m_73441_(worldGenLevel0, boundingBox4, 3, 4, 4, 4, 4, 8, Blocks.SOUL_SAND.defaultBlockState(), Blocks.SOUL_SAND.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 8, 4, 4, 9, 4, 8, Blocks.SOUL_SAND.defaultBlockState(), Blocks.SOUL_SAND.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 3, 5, 4, 4, 5, 8, Blocks.NETHER_WART.defaultBlockState(), Blocks.NETHER_WART.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 8, 5, 4, 9, 5, 8, Blocks.NETHER_WART.defaultBlockState(), Blocks.NETHER_WART.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 4, 2, 0, 8, 2, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 4, 12, 2, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 4, 0, 0, 8, 1, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 4, 0, 9, 8, 1, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 0, 4, 3, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 9, 0, 4, 12, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            for (int $$20 = 4; $$20 <= 8; $$20++) {
                for (int $$21 = 0; $$21 <= 2; $$21++) {
                    this.m_73528_(worldGenLevel0, Blocks.NETHER_BRICKS.defaultBlockState(), $$20, -1, $$21, boundingBox4);
                    this.m_73528_(worldGenLevel0, Blocks.NETHER_BRICKS.defaultBlockState(), $$20, -1, 12 - $$21, boundingBox4);
                }
            }
            for (int $$22 = 0; $$22 <= 2; $$22++) {
                for (int $$23 = 4; $$23 <= 8; $$23++) {
                    this.m_73528_(worldGenLevel0, Blocks.NETHER_BRICKS.defaultBlockState(), $$22, -1, $$23, boundingBox4);
                    this.m_73528_(worldGenLevel0, Blocks.NETHER_BRICKS.defaultBlockState(), 12 - $$22, -1, $$23, boundingBox4);
                }
            }
        }
    }

    public static class MonsterThrone extends NetherFortressPieces.NetherBridgePiece {

        private static final int WIDTH = 7;

        private static final int HEIGHT = 8;

        private static final int DEPTH = 9;

        private boolean hasPlacedSpawner;

        public MonsterThrone(int int0, BoundingBox boundingBox1, Direction direction2) {
            super(StructurePieceType.NETHER_FORTRESS_MONSTER_THRONE, int0, boundingBox1);
            this.m_73519_(direction2);
        }

        public MonsterThrone(CompoundTag compoundTag0) {
            super(StructurePieceType.NETHER_FORTRESS_MONSTER_THRONE, compoundTag0);
            this.hasPlacedSpawner = compoundTag0.getBoolean("Mob");
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1) {
            super.addAdditionalSaveData(structurePieceSerializationContext0, compoundTag1);
            compoundTag1.putBoolean("Mob", this.hasPlacedSpawner);
        }

        public static NetherFortressPieces.MonsterThrone createPiece(StructurePieceAccessor structurePieceAccessor0, int int1, int int2, int int3, int int4, Direction direction5) {
            BoundingBox $$6 = BoundingBox.orientBox(int1, int2, int3, -2, 0, 0, 7, 8, 9, direction5);
            return m_228386_($$6) && structurePieceAccessor0.findCollisionPiece($$6) == null ? new NetherFortressPieces.MonsterThrone(int4, $$6, direction5) : null;
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 0, 6, 7, 7, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 0, 0, 5, 1, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 2, 1, 5, 2, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 3, 2, 5, 3, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 4, 3, 5, 4, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 2, 0, 1, 4, 2, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 2, 0, 5, 4, 2, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 5, 2, 1, 5, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 5, 2, 5, 5, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 5, 3, 0, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 6, 5, 3, 6, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 5, 8, 5, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            BlockState $$7 = (BlockState) ((BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52312_, true)).m_61124_(FenceBlock.f_52310_, true);
            BlockState $$8 = (BlockState) ((BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52309_, true)).m_61124_(FenceBlock.f_52311_, true);
            this.m_73434_(worldGenLevel0, (BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52312_, true), 1, 6, 3, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52310_, true), 5, 6, 3, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52310_, true)).m_61124_(FenceBlock.f_52309_, true), 0, 6, 3, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52312_, true)).m_61124_(FenceBlock.f_52309_, true), 6, 6, 3, boundingBox4);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 6, 4, 0, 6, 7, $$8, $$8, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 6, 6, 4, 6, 6, 7, $$8, $$8, false);
            this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52310_, true)).m_61124_(FenceBlock.f_52311_, true), 0, 6, 8, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52312_, true)).m_61124_(FenceBlock.f_52311_, true), 6, 6, 8, boundingBox4);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 6, 8, 5, 6, 8, $$7, $$7, false);
            this.m_73434_(worldGenLevel0, (BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52310_, true), 1, 7, 8, boundingBox4);
            this.m_73441_(worldGenLevel0, boundingBox4, 2, 7, 8, 4, 7, 8, $$7, $$7, false);
            this.m_73434_(worldGenLevel0, (BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52312_, true), 5, 7, 8, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52310_, true), 2, 8, 8, boundingBox4);
            this.m_73434_(worldGenLevel0, $$7, 3, 8, 8, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52312_, true), 4, 8, 8, boundingBox4);
            if (!this.hasPlacedSpawner) {
                BlockPos $$9 = this.m_163582_(3, 5, 5);
                if (boundingBox4.isInside($$9)) {
                    this.hasPlacedSpawner = true;
                    worldGenLevel0.m_7731_($$9, Blocks.SPAWNER.defaultBlockState(), 2);
                    if (worldGenLevel0.m_7702_($$9) instanceof SpawnerBlockEntity $$11) {
                        $$11.setEntityId(EntityType.BLAZE, randomSource3);
                    }
                }
            }
            for (int $$12 = 0; $$12 <= 6; $$12++) {
                for (int $$13 = 0; $$13 <= 6; $$13++) {
                    this.m_73528_(worldGenLevel0, Blocks.NETHER_BRICKS.defaultBlockState(), $$12, -1, $$13, boundingBox4);
                }
            }
        }
    }

    abstract static class NetherBridgePiece extends StructurePiece {

        protected NetherBridgePiece(StructurePieceType structurePieceType0, int int1, BoundingBox boundingBox2) {
            super(structurePieceType0, int1, boundingBox2);
        }

        public NetherBridgePiece(StructurePieceType structurePieceType0, CompoundTag compoundTag1) {
            super(structurePieceType0, compoundTag1);
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1) {
        }

        private int updatePieceWeight(List<NetherFortressPieces.PieceWeight> listNetherFortressPiecesPieceWeight0) {
            boolean $$1 = false;
            int $$2 = 0;
            for (NetherFortressPieces.PieceWeight $$3 : listNetherFortressPiecesPieceWeight0) {
                if ($$3.maxPlaceCount > 0 && $$3.placeCount < $$3.maxPlaceCount) {
                    $$1 = true;
                }
                $$2 += $$3.weight;
            }
            return $$1 ? $$2 : -1;
        }

        private NetherFortressPieces.NetherBridgePiece generatePiece(NetherFortressPieces.StartPiece netherFortressPiecesStartPiece0, List<NetherFortressPieces.PieceWeight> listNetherFortressPiecesPieceWeight1, StructurePieceAccessor structurePieceAccessor2, RandomSource randomSource3, int int4, int int5, int int6, Direction direction7, int int8) {
            int $$9 = this.updatePieceWeight(listNetherFortressPiecesPieceWeight1);
            boolean $$10 = $$9 > 0 && int8 <= 30;
            int $$11 = 0;
            while ($$11 < 5 && $$10) {
                $$11++;
                int $$12 = randomSource3.nextInt($$9);
                for (NetherFortressPieces.PieceWeight $$13 : listNetherFortressPiecesPieceWeight1) {
                    $$12 -= $$13.weight;
                    if ($$12 < 0) {
                        if (!$$13.doPlace(int8) || $$13 == netherFortressPiecesStartPiece0.previousPiece && !$$13.allowInRow) {
                            break;
                        }
                        NetherFortressPieces.NetherBridgePiece $$14 = NetherFortressPieces.findAndCreateBridgePieceFactory($$13, structurePieceAccessor2, randomSource3, int4, int5, int6, direction7, int8);
                        if ($$14 != null) {
                            $$13.placeCount++;
                            netherFortressPiecesStartPiece0.previousPiece = $$13;
                            if (!$$13.isValid()) {
                                listNetherFortressPiecesPieceWeight1.remove($$13);
                            }
                            return $$14;
                        }
                    }
                }
            }
            return NetherFortressPieces.BridgeEndFiller.createPiece(structurePieceAccessor2, randomSource3, int4, int5, int6, direction7, int8);
        }

        private StructurePiece generateAndAddPiece(NetherFortressPieces.StartPiece netherFortressPiecesStartPiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2, int int3, int int4, int int5, @Nullable Direction direction6, int int7, boolean boolean8) {
            if (Math.abs(int3 - netherFortressPiecesStartPiece0.m_73547_().minX()) <= 112 && Math.abs(int5 - netherFortressPiecesStartPiece0.m_73547_().minZ()) <= 112) {
                List<NetherFortressPieces.PieceWeight> $$9 = netherFortressPiecesStartPiece0.availableBridgePieces;
                if (boolean8) {
                    $$9 = netherFortressPiecesStartPiece0.availableCastlePieces;
                }
                StructurePiece $$10 = this.generatePiece(netherFortressPiecesStartPiece0, $$9, structurePieceAccessor1, randomSource2, int3, int4, int5, direction6, int7 + 1);
                if ($$10 != null) {
                    structurePieceAccessor1.addPiece($$10);
                    netherFortressPiecesStartPiece0.pendingChildren.add($$10);
                }
                return $$10;
            } else {
                return NetherFortressPieces.BridgeEndFiller.createPiece(structurePieceAccessor1, randomSource2, int3, int4, int5, direction6, int7);
            }
        }

        @Nullable
        protected StructurePiece generateChildForward(NetherFortressPieces.StartPiece netherFortressPiecesStartPiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2, int int3, int int4, boolean boolean5) {
            Direction $$6 = this.m_73549_();
            if ($$6 != null) {
                switch($$6) {
                    case NORTH:
                        return this.generateAndAddPiece(netherFortressPiecesStartPiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() + int3, this.f_73383_.minY() + int4, this.f_73383_.minZ() - 1, $$6, this.m_73548_(), boolean5);
                    case SOUTH:
                        return this.generateAndAddPiece(netherFortressPiecesStartPiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() + int3, this.f_73383_.minY() + int4, this.f_73383_.maxZ() + 1, $$6, this.m_73548_(), boolean5);
                    case WEST:
                        return this.generateAndAddPiece(netherFortressPiecesStartPiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() - 1, this.f_73383_.minY() + int4, this.f_73383_.minZ() + int3, $$6, this.m_73548_(), boolean5);
                    case EAST:
                        return this.generateAndAddPiece(netherFortressPiecesStartPiece0, structurePieceAccessor1, randomSource2, this.f_73383_.maxX() + 1, this.f_73383_.minY() + int4, this.f_73383_.minZ() + int3, $$6, this.m_73548_(), boolean5);
                }
            }
            return null;
        }

        @Nullable
        protected StructurePiece generateChildLeft(NetherFortressPieces.StartPiece netherFortressPiecesStartPiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2, int int3, int int4, boolean boolean5) {
            Direction $$6 = this.m_73549_();
            if ($$6 != null) {
                switch($$6) {
                    case NORTH:
                        return this.generateAndAddPiece(netherFortressPiecesStartPiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() - 1, this.f_73383_.minY() + int3, this.f_73383_.minZ() + int4, Direction.WEST, this.m_73548_(), boolean5);
                    case SOUTH:
                        return this.generateAndAddPiece(netherFortressPiecesStartPiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() - 1, this.f_73383_.minY() + int3, this.f_73383_.minZ() + int4, Direction.WEST, this.m_73548_(), boolean5);
                    case WEST:
                        return this.generateAndAddPiece(netherFortressPiecesStartPiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() + int4, this.f_73383_.minY() + int3, this.f_73383_.minZ() - 1, Direction.NORTH, this.m_73548_(), boolean5);
                    case EAST:
                        return this.generateAndAddPiece(netherFortressPiecesStartPiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() + int4, this.f_73383_.minY() + int3, this.f_73383_.minZ() - 1, Direction.NORTH, this.m_73548_(), boolean5);
                }
            }
            return null;
        }

        @Nullable
        protected StructurePiece generateChildRight(NetherFortressPieces.StartPiece netherFortressPiecesStartPiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2, int int3, int int4, boolean boolean5) {
            Direction $$6 = this.m_73549_();
            if ($$6 != null) {
                switch($$6) {
                    case NORTH:
                        return this.generateAndAddPiece(netherFortressPiecesStartPiece0, structurePieceAccessor1, randomSource2, this.f_73383_.maxX() + 1, this.f_73383_.minY() + int3, this.f_73383_.minZ() + int4, Direction.EAST, this.m_73548_(), boolean5);
                    case SOUTH:
                        return this.generateAndAddPiece(netherFortressPiecesStartPiece0, structurePieceAccessor1, randomSource2, this.f_73383_.maxX() + 1, this.f_73383_.minY() + int3, this.f_73383_.minZ() + int4, Direction.EAST, this.m_73548_(), boolean5);
                    case WEST:
                        return this.generateAndAddPiece(netherFortressPiecesStartPiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() + int4, this.f_73383_.minY() + int3, this.f_73383_.maxZ() + 1, Direction.SOUTH, this.m_73548_(), boolean5);
                    case EAST:
                        return this.generateAndAddPiece(netherFortressPiecesStartPiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() + int4, this.f_73383_.minY() + int3, this.f_73383_.maxZ() + 1, Direction.SOUTH, this.m_73548_(), boolean5);
                }
            }
            return null;
        }

        protected static boolean isOkBox(BoundingBox boundingBox0) {
            return boundingBox0 != null && boundingBox0.minY() > 10;
        }
    }

    static class PieceWeight {

        public final Class<? extends NetherFortressPieces.NetherBridgePiece> pieceClass;

        public final int weight;

        public int placeCount;

        public final int maxPlaceCount;

        public final boolean allowInRow;

        public PieceWeight(Class<? extends NetherFortressPieces.NetherBridgePiece> classExtendsNetherFortressPiecesNetherBridgePiece0, int int1, int int2, boolean boolean3) {
            this.pieceClass = classExtendsNetherFortressPiecesNetherBridgePiece0;
            this.weight = int1;
            this.maxPlaceCount = int2;
            this.allowInRow = boolean3;
        }

        public PieceWeight(Class<? extends NetherFortressPieces.NetherBridgePiece> classExtendsNetherFortressPiecesNetherBridgePiece0, int int1, int int2) {
            this(classExtendsNetherFortressPiecesNetherBridgePiece0, int1, int2, false);
        }

        public boolean doPlace(int int0) {
            return this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount;
        }

        public boolean isValid() {
            return this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount;
        }
    }

    public static class RoomCrossing extends NetherFortressPieces.NetherBridgePiece {

        private static final int WIDTH = 7;

        private static final int HEIGHT = 9;

        private static final int DEPTH = 7;

        public RoomCrossing(int int0, BoundingBox boundingBox1, Direction direction2) {
            super(StructurePieceType.NETHER_FORTRESS_ROOM_CROSSING, int0, boundingBox1);
            this.m_73519_(direction2);
        }

        public RoomCrossing(CompoundTag compoundTag0) {
            super(StructurePieceType.NETHER_FORTRESS_ROOM_CROSSING, compoundTag0);
        }

        @Override
        public void addChildren(StructurePiece structurePiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2) {
            this.m_228401_((NetherFortressPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 2, 0, false);
            this.m_228420_((NetherFortressPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 0, 2, false);
            this.m_228427_((NetherFortressPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 0, 2, false);
        }

        public static NetherFortressPieces.RoomCrossing createPiece(StructurePieceAccessor structurePieceAccessor0, int int1, int int2, int int3, Direction direction4, int int5) {
            BoundingBox $$6 = BoundingBox.orientBox(int1, int2, int3, -2, 0, 0, 7, 9, 7, direction4);
            return m_228386_($$6) && structurePieceAccessor0.findCollisionPiece($$6) == null ? new NetherFortressPieces.RoomCrossing(int5, $$6, direction4) : null;
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 0, 0, 6, 1, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 0, 6, 7, 6, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 0, 1, 6, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 6, 1, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 2, 0, 6, 6, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 2, 6, 6, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 0, 0, 6, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 5, 0, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 6, 2, 0, 6, 6, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 6, 2, 5, 6, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            BlockState $$7 = (BlockState) ((BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52312_, true)).m_61124_(FenceBlock.f_52310_, true);
            BlockState $$8 = (BlockState) ((BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52309_, true)).m_61124_(FenceBlock.f_52311_, true);
            this.m_73441_(worldGenLevel0, boundingBox4, 2, 6, 0, 4, 6, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 2, 5, 0, 4, 5, 0, $$7, $$7, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 2, 6, 6, 4, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 2, 5, 6, 4, 5, 6, $$7, $$7, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 6, 2, 0, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 5, 2, 0, 5, 4, $$8, $$8, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 6, 6, 2, 6, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 6, 5, 2, 6, 5, 4, $$8, $$8, false);
            for (int $$9 = 0; $$9 <= 6; $$9++) {
                for (int $$10 = 0; $$10 <= 6; $$10++) {
                    this.m_73528_(worldGenLevel0, Blocks.NETHER_BRICKS.defaultBlockState(), $$9, -1, $$10, boundingBox4);
                }
            }
        }
    }

    public static class StairsRoom extends NetherFortressPieces.NetherBridgePiece {

        private static final int WIDTH = 7;

        private static final int HEIGHT = 11;

        private static final int DEPTH = 7;

        public StairsRoom(int int0, BoundingBox boundingBox1, Direction direction2) {
            super(StructurePieceType.NETHER_FORTRESS_STAIRS_ROOM, int0, boundingBox1);
            this.m_73519_(direction2);
        }

        public StairsRoom(CompoundTag compoundTag0) {
            super(StructurePieceType.NETHER_FORTRESS_STAIRS_ROOM, compoundTag0);
        }

        @Override
        public void addChildren(StructurePiece structurePiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2) {
            this.m_228427_((NetherFortressPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 6, 2, false);
        }

        public static NetherFortressPieces.StairsRoom createPiece(StructurePieceAccessor structurePieceAccessor0, int int1, int int2, int int3, int int4, Direction direction5) {
            BoundingBox $$6 = BoundingBox.orientBox(int1, int2, int3, -2, 0, 0, 7, 11, 7, direction5);
            return m_228386_($$6) && structurePieceAccessor0.findCollisionPiece($$6) == null ? new NetherFortressPieces.StairsRoom(int4, $$6, direction5) : null;
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 0, 0, 6, 1, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 0, 6, 10, 6, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 0, 1, 8, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 2, 0, 6, 8, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 1, 0, 8, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 6, 2, 1, 6, 8, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 2, 6, 5, 8, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            BlockState $$7 = (BlockState) ((BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52312_, true)).m_61124_(FenceBlock.f_52310_, true);
            BlockState $$8 = (BlockState) ((BlockState) Blocks.NETHER_BRICK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52309_, true)).m_61124_(FenceBlock.f_52311_, true);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 3, 2, 0, 5, 4, $$8, $$8, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 6, 3, 2, 6, 5, 2, $$8, $$8, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 6, 3, 4, 6, 5, 4, $$8, $$8, false);
            this.m_73434_(worldGenLevel0, Blocks.NETHER_BRICKS.defaultBlockState(), 5, 2, 5, boundingBox4);
            this.m_73441_(worldGenLevel0, boundingBox4, 4, 2, 5, 4, 3, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 3, 2, 5, 3, 4, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 2, 2, 5, 2, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 2, 5, 1, 6, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 7, 1, 5, 7, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 6, 8, 2, 6, 8, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 2, 6, 0, 4, 8, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 2, 5, 0, 4, 5, 0, $$7, $$7, false);
            for (int $$9 = 0; $$9 <= 6; $$9++) {
                for (int $$10 = 0; $$10 <= 6; $$10++) {
                    this.m_73528_(worldGenLevel0, Blocks.NETHER_BRICKS.defaultBlockState(), $$9, -1, $$10, boundingBox4);
                }
            }
        }
    }

    public static class StartPiece extends NetherFortressPieces.BridgeCrossing {

        public NetherFortressPieces.PieceWeight previousPiece;

        public List<NetherFortressPieces.PieceWeight> availableBridgePieces;

        public List<NetherFortressPieces.PieceWeight> availableCastlePieces;

        public final List<StructurePiece> pendingChildren = Lists.newArrayList();

        public StartPiece(RandomSource randomSource0, int int1, int int2) {
            super(int1, int2, m_226760_(randomSource0));
            this.availableBridgePieces = Lists.newArrayList();
            for (NetherFortressPieces.PieceWeight $$3 : NetherFortressPieces.BRIDGE_PIECE_WEIGHTS) {
                $$3.placeCount = 0;
                this.availableBridgePieces.add($$3);
            }
            this.availableCastlePieces = Lists.newArrayList();
            for (NetherFortressPieces.PieceWeight $$4 : NetherFortressPieces.CASTLE_PIECE_WEIGHTS) {
                $$4.placeCount = 0;
                this.availableCastlePieces.add($$4);
            }
        }

        public StartPiece(CompoundTag compoundTag0) {
            super(StructurePieceType.NETHER_FORTRESS_START, compoundTag0);
        }
    }
}