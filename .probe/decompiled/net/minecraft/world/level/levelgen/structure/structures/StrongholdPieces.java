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
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.EndPortalFrameBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class StrongholdPieces {

    private static final int SMALL_DOOR_WIDTH = 3;

    private static final int SMALL_DOOR_HEIGHT = 3;

    private static final int MAX_DEPTH = 50;

    private static final int LOWEST_Y_POSITION = 10;

    private static final boolean CHECK_AIR = true;

    public static final int MAGIC_START_Y = 64;

    private static final StrongholdPieces.PieceWeight[] STRONGHOLD_PIECE_WEIGHTS = new StrongholdPieces.PieceWeight[] { new StrongholdPieces.PieceWeight(StrongholdPieces.Straight.class, 40, 0), new StrongholdPieces.PieceWeight(StrongholdPieces.PrisonHall.class, 5, 5), new StrongholdPieces.PieceWeight(StrongholdPieces.LeftTurn.class, 20, 0), new StrongholdPieces.PieceWeight(StrongholdPieces.RightTurn.class, 20, 0), new StrongholdPieces.PieceWeight(StrongholdPieces.RoomCrossing.class, 10, 6), new StrongholdPieces.PieceWeight(StrongholdPieces.StraightStairsDown.class, 5, 5), new StrongholdPieces.PieceWeight(StrongholdPieces.StairsDown.class, 5, 5), new StrongholdPieces.PieceWeight(StrongholdPieces.FiveCrossing.class, 5, 4), new StrongholdPieces.PieceWeight(StrongholdPieces.ChestCorridor.class, 5, 4), new StrongholdPieces.PieceWeight(StrongholdPieces.Library.class, 10, 2) {

        @Override
        public boolean doPlace(int p_229450_) {
            return super.doPlace(p_229450_) && p_229450_ > 4;
        }
    }, new StrongholdPieces.PieceWeight(StrongholdPieces.PortalRoom.class, 20, 1) {

        @Override
        public boolean doPlace(int p_229456_) {
            return super.doPlace(p_229456_) && p_229456_ > 5;
        }
    } };

    private static List<StrongholdPieces.PieceWeight> currentPieces;

    static Class<? extends StrongholdPieces.StrongholdPiece> imposedPiece;

    private static int totalWeight;

    static final StrongholdPieces.SmoothStoneSelector SMOOTH_STONE_SELECTOR = new StrongholdPieces.SmoothStoneSelector();

    public static void resetPieces() {
        currentPieces = Lists.newArrayList();
        for (StrongholdPieces.PieceWeight $$0 : STRONGHOLD_PIECE_WEIGHTS) {
            $$0.placeCount = 0;
            currentPieces.add($$0);
        }
        imposedPiece = null;
    }

    private static boolean updatePieceWeight() {
        boolean $$0 = false;
        totalWeight = 0;
        for (StrongholdPieces.PieceWeight $$1 : currentPieces) {
            if ($$1.maxPlaceCount > 0 && $$1.placeCount < $$1.maxPlaceCount) {
                $$0 = true;
            }
            totalWeight = totalWeight + $$1.weight;
        }
        return $$0;
    }

    private static StrongholdPieces.StrongholdPiece findAndCreatePieceFactory(Class<? extends StrongholdPieces.StrongholdPiece> classExtendsStrongholdPiecesStrongholdPiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2, int int3, int int4, int int5, @Nullable Direction direction6, int int7) {
        StrongholdPieces.StrongholdPiece $$8 = null;
        if (classExtendsStrongholdPiecesStrongholdPiece0 == StrongholdPieces.Straight.class) {
            $$8 = StrongholdPieces.Straight.createPiece(structurePieceAccessor1, randomSource2, int3, int4, int5, direction6, int7);
        } else if (classExtendsStrongholdPiecesStrongholdPiece0 == StrongholdPieces.PrisonHall.class) {
            $$8 = StrongholdPieces.PrisonHall.createPiece(structurePieceAccessor1, randomSource2, int3, int4, int5, direction6, int7);
        } else if (classExtendsStrongholdPiecesStrongholdPiece0 == StrongholdPieces.LeftTurn.class) {
            $$8 = StrongholdPieces.LeftTurn.createPiece(structurePieceAccessor1, randomSource2, int3, int4, int5, direction6, int7);
        } else if (classExtendsStrongholdPiecesStrongholdPiece0 == StrongholdPieces.RightTurn.class) {
            $$8 = StrongholdPieces.RightTurn.createPiece(structurePieceAccessor1, randomSource2, int3, int4, int5, direction6, int7);
        } else if (classExtendsStrongholdPiecesStrongholdPiece0 == StrongholdPieces.RoomCrossing.class) {
            $$8 = StrongholdPieces.RoomCrossing.createPiece(structurePieceAccessor1, randomSource2, int3, int4, int5, direction6, int7);
        } else if (classExtendsStrongholdPiecesStrongholdPiece0 == StrongholdPieces.StraightStairsDown.class) {
            $$8 = StrongholdPieces.StraightStairsDown.createPiece(structurePieceAccessor1, randomSource2, int3, int4, int5, direction6, int7);
        } else if (classExtendsStrongholdPiecesStrongholdPiece0 == StrongholdPieces.StairsDown.class) {
            $$8 = StrongholdPieces.StairsDown.createPiece(structurePieceAccessor1, randomSource2, int3, int4, int5, direction6, int7);
        } else if (classExtendsStrongholdPiecesStrongholdPiece0 == StrongholdPieces.FiveCrossing.class) {
            $$8 = StrongholdPieces.FiveCrossing.createPiece(structurePieceAccessor1, randomSource2, int3, int4, int5, direction6, int7);
        } else if (classExtendsStrongholdPiecesStrongholdPiece0 == StrongholdPieces.ChestCorridor.class) {
            $$8 = StrongholdPieces.ChestCorridor.createPiece(structurePieceAccessor1, randomSource2, int3, int4, int5, direction6, int7);
        } else if (classExtendsStrongholdPiecesStrongholdPiece0 == StrongholdPieces.Library.class) {
            $$8 = StrongholdPieces.Library.createPiece(structurePieceAccessor1, randomSource2, int3, int4, int5, direction6, int7);
        } else if (classExtendsStrongholdPiecesStrongholdPiece0 == StrongholdPieces.PortalRoom.class) {
            $$8 = StrongholdPieces.PortalRoom.createPiece(structurePieceAccessor1, int3, int4, int5, direction6, int7);
        }
        return $$8;
    }

    private static StrongholdPieces.StrongholdPiece generatePieceFromSmallDoor(StrongholdPieces.StartPiece strongholdPiecesStartPiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2, int int3, int int4, int int5, Direction direction6, int int7) {
        if (!updatePieceWeight()) {
            return null;
        } else {
            if (imposedPiece != null) {
                StrongholdPieces.StrongholdPiece $$8 = findAndCreatePieceFactory(imposedPiece, structurePieceAccessor1, randomSource2, int3, int4, int5, direction6, int7);
                imposedPiece = null;
                if ($$8 != null) {
                    return $$8;
                }
            }
            int $$9 = 0;
            while ($$9 < 5) {
                $$9++;
                int $$10 = randomSource2.nextInt(totalWeight);
                for (StrongholdPieces.PieceWeight $$11 : currentPieces) {
                    $$10 -= $$11.weight;
                    if ($$10 < 0) {
                        if (!$$11.doPlace(int7) || $$11 == strongholdPiecesStartPiece0.previousPiece) {
                            break;
                        }
                        StrongholdPieces.StrongholdPiece $$12 = findAndCreatePieceFactory($$11.pieceClass, structurePieceAccessor1, randomSource2, int3, int4, int5, direction6, int7);
                        if ($$12 != null) {
                            $$11.placeCount++;
                            strongholdPiecesStartPiece0.previousPiece = $$11;
                            if (!$$11.isValid()) {
                                currentPieces.remove($$11);
                            }
                            return $$12;
                        }
                    }
                }
            }
            BoundingBox $$13 = StrongholdPieces.FillerCorridor.findPieceBox(structurePieceAccessor1, randomSource2, int3, int4, int5, direction6);
            return $$13 != null && $$13.minY() > 1 ? new StrongholdPieces.FillerCorridor(int7, $$13, direction6) : null;
        }
    }

    static StructurePiece generateAndAddPiece(StrongholdPieces.StartPiece strongholdPiecesStartPiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2, int int3, int int4, int int5, @Nullable Direction direction6, int int7) {
        if (int7 > 50) {
            return null;
        } else if (Math.abs(int3 - strongholdPiecesStartPiece0.m_73547_().minX()) <= 112 && Math.abs(int5 - strongholdPiecesStartPiece0.m_73547_().minZ()) <= 112) {
            StructurePiece $$8 = generatePieceFromSmallDoor(strongholdPiecesStartPiece0, structurePieceAccessor1, randomSource2, int3, int4, int5, direction6, int7 + 1);
            if ($$8 != null) {
                structurePieceAccessor1.addPiece($$8);
                strongholdPiecesStartPiece0.pendingChildren.add($$8);
            }
            return $$8;
        } else {
            return null;
        }
    }

    public static class ChestCorridor extends StrongholdPieces.StrongholdPiece {

        private static final int WIDTH = 5;

        private static final int HEIGHT = 5;

        private static final int DEPTH = 7;

        private boolean hasPlacedChest;

        public ChestCorridor(int int0, RandomSource randomSource1, BoundingBox boundingBox2, Direction direction3) {
            super(StructurePieceType.STRONGHOLD_CHEST_CORRIDOR, int0, boundingBox2);
            this.m_73519_(direction3);
            this.f_229872_ = this.m_229899_(randomSource1);
        }

        public ChestCorridor(CompoundTag compoundTag0) {
            super(StructurePieceType.STRONGHOLD_CHEST_CORRIDOR, compoundTag0);
            this.hasPlacedChest = compoundTag0.getBoolean("Chest");
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1) {
            super.addAdditionalSaveData(structurePieceSerializationContext0, compoundTag1);
            compoundTag1.putBoolean("Chest", this.hasPlacedChest);
        }

        @Override
        public void addChildren(StructurePiece structurePiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2) {
            this.m_229893_((StrongholdPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 1, 1);
        }

        public static StrongholdPieces.ChestCorridor createPiece(StructurePieceAccessor structurePieceAccessor0, RandomSource randomSource1, int int2, int int3, int int4, Direction direction5, int int6) {
            BoundingBox $$7 = BoundingBox.orientBox(int2, int3, int4, -1, -1, 0, 5, 5, 7, direction5);
            return m_229888_($$7) && structurePieceAccessor0.findCollisionPiece($$7) == null ? new StrongholdPieces.ChestCorridor(int6, randomSource1, $$7, direction5) : null;
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            this.m_226776_(worldGenLevel0, boundingBox4, 0, 0, 0, 4, 4, 6, true, randomSource3, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.m_229880_(worldGenLevel0, randomSource3, boundingBox4, this.f_229872_, 1, 1, 0);
            this.m_229880_(worldGenLevel0, randomSource3, boundingBox4, StrongholdPieces.StrongholdPiece.SmallDoorType.OPENING, 1, 1, 6);
            this.m_73441_(worldGenLevel0, boundingBox4, 3, 1, 2, 3, 1, 4, Blocks.STONE_BRICKS.defaultBlockState(), Blocks.STONE_BRICKS.defaultBlockState(), false);
            this.m_73434_(worldGenLevel0, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 3, 1, 1, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 3, 1, 5, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 3, 2, 2, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 3, 2, 4, boundingBox4);
            for (int $$7 = 2; $$7 <= 4; $$7++) {
                this.m_73434_(worldGenLevel0, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 2, 1, $$7, boundingBox4);
            }
            if (!this.hasPlacedChest && boundingBox4.isInside(this.m_163582_(3, 2, 3))) {
                this.hasPlacedChest = true;
                this.m_213787_(worldGenLevel0, boundingBox4, randomSource3, 3, 2, 3, BuiltInLootTables.STRONGHOLD_CORRIDOR);
            }
        }
    }

    public static class FillerCorridor extends StrongholdPieces.StrongholdPiece {

        private final int steps;

        public FillerCorridor(int int0, BoundingBox boundingBox1, Direction direction2) {
            super(StructurePieceType.STRONGHOLD_FILLER_CORRIDOR, int0, boundingBox1);
            this.m_73519_(direction2);
            this.steps = direction2 != Direction.NORTH && direction2 != Direction.SOUTH ? boundingBox1.getXSpan() : boundingBox1.getZSpan();
        }

        public FillerCorridor(CompoundTag compoundTag0) {
            super(StructurePieceType.STRONGHOLD_FILLER_CORRIDOR, compoundTag0);
            this.steps = compoundTag0.getInt("Steps");
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1) {
            super.addAdditionalSaveData(structurePieceSerializationContext0, compoundTag1);
            compoundTag1.putInt("Steps", this.steps);
        }

        public static BoundingBox findPieceBox(StructurePieceAccessor structurePieceAccessor0, RandomSource randomSource1, int int2, int int3, int int4, Direction direction5) {
            int $$6 = 3;
            BoundingBox $$7 = BoundingBox.orientBox(int2, int3, int4, -1, -1, 0, 5, 5, 4, direction5);
            StructurePiece $$8 = structurePieceAccessor0.findCollisionPiece($$7);
            if ($$8 == null) {
                return null;
            } else {
                if ($$8.getBoundingBox().minY() == $$7.minY()) {
                    for (int $$9 = 2; $$9 >= 1; $$9--) {
                        $$7 = BoundingBox.orientBox(int2, int3, int4, -1, -1, 0, 5, 5, $$9, direction5);
                        if (!$$8.getBoundingBox().intersects($$7)) {
                            return BoundingBox.orientBox(int2, int3, int4, -1, -1, 0, 5, 5, $$9 + 1, direction5);
                        }
                    }
                }
                return null;
            }
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            for (int $$7 = 0; $$7 < this.steps; $$7++) {
                this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 0, 0, $$7, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 1, 0, $$7, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 2, 0, $$7, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 3, 0, $$7, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 4, 0, $$7, boundingBox4);
                for (int $$8 = 1; $$8 <= 3; $$8++) {
                    this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 0, $$8, $$7, boundingBox4);
                    this.m_73434_(worldGenLevel0, Blocks.CAVE_AIR.defaultBlockState(), 1, $$8, $$7, boundingBox4);
                    this.m_73434_(worldGenLevel0, Blocks.CAVE_AIR.defaultBlockState(), 2, $$8, $$7, boundingBox4);
                    this.m_73434_(worldGenLevel0, Blocks.CAVE_AIR.defaultBlockState(), 3, $$8, $$7, boundingBox4);
                    this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 4, $$8, $$7, boundingBox4);
                }
                this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 0, 4, $$7, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 1, 4, $$7, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 2, 4, $$7, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 3, 4, $$7, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 4, 4, $$7, boundingBox4);
            }
        }
    }

    public static class FiveCrossing extends StrongholdPieces.StrongholdPiece {

        protected static final int WIDTH = 10;

        protected static final int HEIGHT = 9;

        protected static final int DEPTH = 11;

        private final boolean leftLow;

        private final boolean leftHigh;

        private final boolean rightLow;

        private final boolean rightHigh;

        public FiveCrossing(int int0, RandomSource randomSource1, BoundingBox boundingBox2, Direction direction3) {
            super(StructurePieceType.STRONGHOLD_FIVE_CROSSING, int0, boundingBox2);
            this.m_73519_(direction3);
            this.f_229872_ = this.m_229899_(randomSource1);
            this.leftLow = randomSource1.nextBoolean();
            this.leftHigh = randomSource1.nextBoolean();
            this.rightLow = randomSource1.nextBoolean();
            this.rightHigh = randomSource1.nextInt(3) > 0;
        }

        public FiveCrossing(CompoundTag compoundTag0) {
            super(StructurePieceType.STRONGHOLD_FIVE_CROSSING, compoundTag0);
            this.leftLow = compoundTag0.getBoolean("leftLow");
            this.leftHigh = compoundTag0.getBoolean("leftHigh");
            this.rightLow = compoundTag0.getBoolean("rightLow");
            this.rightHigh = compoundTag0.getBoolean("rightHigh");
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1) {
            super.addAdditionalSaveData(structurePieceSerializationContext0, compoundTag1);
            compoundTag1.putBoolean("leftLow", this.leftLow);
            compoundTag1.putBoolean("leftHigh", this.leftHigh);
            compoundTag1.putBoolean("rightLow", this.rightLow);
            compoundTag1.putBoolean("rightHigh", this.rightHigh);
        }

        @Override
        public void addChildren(StructurePiece structurePiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2) {
            int $$3 = 3;
            int $$4 = 5;
            Direction $$5 = this.m_73549_();
            if ($$5 == Direction.WEST || $$5 == Direction.NORTH) {
                $$3 = 8 - $$3;
                $$4 = 8 - $$4;
            }
            this.m_229893_((StrongholdPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 5, 1);
            if (this.leftLow) {
                this.m_229901_((StrongholdPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, $$3, 1);
            }
            if (this.leftHigh) {
                this.m_229901_((StrongholdPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, $$4, 7);
            }
            if (this.rightLow) {
                this.m_229907_((StrongholdPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, $$3, 1);
            }
            if (this.rightHigh) {
                this.m_229907_((StrongholdPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, $$4, 7);
            }
        }

        public static StrongholdPieces.FiveCrossing createPiece(StructurePieceAccessor structurePieceAccessor0, RandomSource randomSource1, int int2, int int3, int int4, Direction direction5, int int6) {
            BoundingBox $$7 = BoundingBox.orientBox(int2, int3, int4, -4, -3, 0, 10, 9, 11, direction5);
            return m_229888_($$7) && structurePieceAccessor0.findCollisionPiece($$7) == null ? new StrongholdPieces.FiveCrossing(int6, randomSource1, $$7, direction5) : null;
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            this.m_226776_(worldGenLevel0, boundingBox4, 0, 0, 0, 9, 8, 10, true, randomSource3, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.m_229880_(worldGenLevel0, randomSource3, boundingBox4, this.f_229872_, 4, 3, 0);
            if (this.leftLow) {
                this.m_73441_(worldGenLevel0, boundingBox4, 0, 3, 1, 0, 5, 3, f_73382_, f_73382_, false);
            }
            if (this.rightLow) {
                this.m_73441_(worldGenLevel0, boundingBox4, 9, 3, 1, 9, 5, 3, f_73382_, f_73382_, false);
            }
            if (this.leftHigh) {
                this.m_73441_(worldGenLevel0, boundingBox4, 0, 5, 7, 0, 7, 9, f_73382_, f_73382_, false);
            }
            if (this.rightHigh) {
                this.m_73441_(worldGenLevel0, boundingBox4, 9, 5, 7, 9, 7, 9, f_73382_, f_73382_, false);
            }
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 1, 10, 7, 3, 10, f_73382_, f_73382_, false);
            this.m_226776_(worldGenLevel0, boundingBox4, 1, 2, 1, 8, 2, 6, false, randomSource3, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 4, 1, 5, 4, 4, 9, false, randomSource3, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 8, 1, 5, 8, 4, 9, false, randomSource3, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 1, 4, 7, 3, 4, 9, false, randomSource3, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 1, 3, 5, 3, 3, 6, false, randomSource3, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 3, 4, 3, 3, 4, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 4, 6, 3, 4, 6, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
            this.m_226776_(worldGenLevel0, boundingBox4, 5, 1, 7, 7, 1, 8, false, randomSource3, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 1, 9, 7, 1, 9, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 2, 7, 7, 2, 7, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 4, 5, 7, 4, 5, 9, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 8, 5, 7, 8, 5, 9, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 5, 7, 7, 5, 9, (BlockState) Blocks.SMOOTH_STONE_SLAB.defaultBlockState().m_61124_(SlabBlock.TYPE, SlabType.DOUBLE), (BlockState) Blocks.SMOOTH_STONE_SLAB.defaultBlockState().m_61124_(SlabBlock.TYPE, SlabType.DOUBLE), false);
            this.m_73434_(worldGenLevel0, (BlockState) Blocks.WALL_TORCH.defaultBlockState().m_61124_(WallTorchBlock.FACING, Direction.SOUTH), 6, 5, 6, boundingBox4);
        }
    }

    public static class LeftTurn extends StrongholdPieces.Turn {

        public LeftTurn(int int0, RandomSource randomSource1, BoundingBox boundingBox2, Direction direction3) {
            super(StructurePieceType.STRONGHOLD_LEFT_TURN, int0, boundingBox2);
            this.m_73519_(direction3);
            this.f_229872_ = this.m_229899_(randomSource1);
        }

        public LeftTurn(CompoundTag compoundTag0) {
            super(StructurePieceType.STRONGHOLD_LEFT_TURN, compoundTag0);
        }

        @Override
        public void addChildren(StructurePiece structurePiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2) {
            Direction $$3 = this.m_73549_();
            if ($$3 != Direction.NORTH && $$3 != Direction.EAST) {
                this.m_229907_((StrongholdPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 1, 1);
            } else {
                this.m_229901_((StrongholdPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 1, 1);
            }
        }

        public static StrongholdPieces.LeftTurn createPiece(StructurePieceAccessor structurePieceAccessor0, RandomSource randomSource1, int int2, int int3, int int4, Direction direction5, int int6) {
            BoundingBox $$7 = BoundingBox.orientBox(int2, int3, int4, -1, -1, 0, 5, 5, 5, direction5);
            return m_229888_($$7) && structurePieceAccessor0.findCollisionPiece($$7) == null ? new StrongholdPieces.LeftTurn(int6, randomSource1, $$7, direction5) : null;
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            this.m_226776_(worldGenLevel0, boundingBox4, 0, 0, 0, 4, 4, 4, true, randomSource3, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.m_229880_(worldGenLevel0, randomSource3, boundingBox4, this.f_229872_, 1, 1, 0);
            Direction $$7 = this.m_73549_();
            if ($$7 != Direction.NORTH && $$7 != Direction.EAST) {
                this.m_73441_(worldGenLevel0, boundingBox4, 4, 1, 1, 4, 3, 3, f_73382_, f_73382_, false);
            } else {
                this.m_73441_(worldGenLevel0, boundingBox4, 0, 1, 1, 0, 3, 3, f_73382_, f_73382_, false);
            }
        }
    }

    public static class Library extends StrongholdPieces.StrongholdPiece {

        protected static final int WIDTH = 14;

        protected static final int HEIGHT = 6;

        protected static final int TALL_HEIGHT = 11;

        protected static final int DEPTH = 15;

        private final boolean isTall;

        public Library(int int0, RandomSource randomSource1, BoundingBox boundingBox2, Direction direction3) {
            super(StructurePieceType.STRONGHOLD_LIBRARY, int0, boundingBox2);
            this.m_73519_(direction3);
            this.f_229872_ = this.m_229899_(randomSource1);
            this.isTall = boundingBox2.getYSpan() > 6;
        }

        public Library(CompoundTag compoundTag0) {
            super(StructurePieceType.STRONGHOLD_LIBRARY, compoundTag0);
            this.isTall = compoundTag0.getBoolean("Tall");
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1) {
            super.addAdditionalSaveData(structurePieceSerializationContext0, compoundTag1);
            compoundTag1.putBoolean("Tall", this.isTall);
        }

        public static StrongholdPieces.Library createPiece(StructurePieceAccessor structurePieceAccessor0, RandomSource randomSource1, int int2, int int3, int int4, Direction direction5, int int6) {
            BoundingBox $$7 = BoundingBox.orientBox(int2, int3, int4, -4, -1, 0, 14, 11, 15, direction5);
            if (!m_229888_($$7) || structurePieceAccessor0.findCollisionPiece($$7) != null) {
                $$7 = BoundingBox.orientBox(int2, int3, int4, -4, -1, 0, 14, 6, 15, direction5);
                if (!m_229888_($$7) || structurePieceAccessor0.findCollisionPiece($$7) != null) {
                    return null;
                }
            }
            return new StrongholdPieces.Library(int6, randomSource1, $$7, direction5);
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            int $$7 = 11;
            if (!this.isTall) {
                $$7 = 6;
            }
            this.m_226776_(worldGenLevel0, boundingBox4, 0, 0, 0, 13, $$7 - 1, 14, true, randomSource3, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.m_229880_(worldGenLevel0, randomSource3, boundingBox4, this.f_229872_, 4, 1, 0);
            this.m_226788_(worldGenLevel0, boundingBox4, randomSource3, 0.07F, 2, 1, 1, 11, 4, 13, Blocks.COBWEB.defaultBlockState(), Blocks.COBWEB.defaultBlockState(), false, false);
            int $$8 = 1;
            int $$9 = 12;
            for (int $$10 = 1; $$10 <= 13; $$10++) {
                if (($$10 - 1) % 4 == 0) {
                    this.m_73441_(worldGenLevel0, boundingBox4, 1, 1, $$10, 1, 4, $$10, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
                    this.m_73441_(worldGenLevel0, boundingBox4, 12, 1, $$10, 12, 4, $$10, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
                    this.m_73434_(worldGenLevel0, (BlockState) Blocks.WALL_TORCH.defaultBlockState().m_61124_(WallTorchBlock.FACING, Direction.EAST), 2, 3, $$10, boundingBox4);
                    this.m_73434_(worldGenLevel0, (BlockState) Blocks.WALL_TORCH.defaultBlockState().m_61124_(WallTorchBlock.FACING, Direction.WEST), 11, 3, $$10, boundingBox4);
                    if (this.isTall) {
                        this.m_73441_(worldGenLevel0, boundingBox4, 1, 6, $$10, 1, 9, $$10, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
                        this.m_73441_(worldGenLevel0, boundingBox4, 12, 6, $$10, 12, 9, $$10, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
                    }
                } else {
                    this.m_73441_(worldGenLevel0, boundingBox4, 1, 1, $$10, 1, 4, $$10, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
                    this.m_73441_(worldGenLevel0, boundingBox4, 12, 1, $$10, 12, 4, $$10, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
                    if (this.isTall) {
                        this.m_73441_(worldGenLevel0, boundingBox4, 1, 6, $$10, 1, 9, $$10, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
                        this.m_73441_(worldGenLevel0, boundingBox4, 12, 6, $$10, 12, 9, $$10, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
                    }
                }
            }
            for (int $$11 = 3; $$11 < 12; $$11 += 2) {
                this.m_73441_(worldGenLevel0, boundingBox4, 3, 1, $$11, 4, 3, $$11, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
                this.m_73441_(worldGenLevel0, boundingBox4, 6, 1, $$11, 7, 3, $$11, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
                this.m_73441_(worldGenLevel0, boundingBox4, 9, 1, $$11, 10, 3, $$11, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
            }
            if (this.isTall) {
                this.m_73441_(worldGenLevel0, boundingBox4, 1, 5, 1, 3, 5, 13, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
                this.m_73441_(worldGenLevel0, boundingBox4, 10, 5, 1, 12, 5, 13, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
                this.m_73441_(worldGenLevel0, boundingBox4, 4, 5, 1, 9, 5, 2, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
                this.m_73441_(worldGenLevel0, boundingBox4, 4, 5, 12, 9, 5, 13, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
                this.m_73434_(worldGenLevel0, Blocks.OAK_PLANKS.defaultBlockState(), 9, 5, 11, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.OAK_PLANKS.defaultBlockState(), 8, 5, 11, boundingBox4);
                this.m_73434_(worldGenLevel0, Blocks.OAK_PLANKS.defaultBlockState(), 9, 5, 10, boundingBox4);
                BlockState $$12 = (BlockState) ((BlockState) Blocks.OAK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52312_, true)).m_61124_(FenceBlock.f_52310_, true);
                BlockState $$13 = (BlockState) ((BlockState) Blocks.OAK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52309_, true)).m_61124_(FenceBlock.f_52311_, true);
                this.m_73441_(worldGenLevel0, boundingBox4, 3, 6, 3, 3, 6, 11, $$13, $$13, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 10, 6, 3, 10, 6, 9, $$13, $$13, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 4, 6, 2, 9, 6, 2, $$12, $$12, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 4, 6, 12, 7, 6, 12, $$12, $$12, false);
                this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.OAK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52309_, true)).m_61124_(FenceBlock.f_52310_, true), 3, 6, 2, boundingBox4);
                this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.OAK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52311_, true)).m_61124_(FenceBlock.f_52310_, true), 3, 6, 12, boundingBox4);
                this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.OAK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52309_, true)).m_61124_(FenceBlock.f_52312_, true), 10, 6, 2, boundingBox4);
                for (int $$14 = 0; $$14 <= 2; $$14++) {
                    this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.OAK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52311_, true)).m_61124_(FenceBlock.f_52312_, true), 8 + $$14, 6, 12 - $$14, boundingBox4);
                    if ($$14 != 2) {
                        this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.OAK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52309_, true)).m_61124_(FenceBlock.f_52310_, true), 8 + $$14, 6, 11 - $$14, boundingBox4);
                    }
                }
                BlockState $$15 = (BlockState) Blocks.LADDER.defaultBlockState().m_61124_(LadderBlock.FACING, Direction.SOUTH);
                this.m_73434_(worldGenLevel0, $$15, 10, 1, 13, boundingBox4);
                this.m_73434_(worldGenLevel0, $$15, 10, 2, 13, boundingBox4);
                this.m_73434_(worldGenLevel0, $$15, 10, 3, 13, boundingBox4);
                this.m_73434_(worldGenLevel0, $$15, 10, 4, 13, boundingBox4);
                this.m_73434_(worldGenLevel0, $$15, 10, 5, 13, boundingBox4);
                this.m_73434_(worldGenLevel0, $$15, 10, 6, 13, boundingBox4);
                this.m_73434_(worldGenLevel0, $$15, 10, 7, 13, boundingBox4);
                int $$16 = 7;
                int $$17 = 7;
                BlockState $$18 = (BlockState) Blocks.OAK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52310_, true);
                this.m_73434_(worldGenLevel0, $$18, 6, 9, 7, boundingBox4);
                BlockState $$19 = (BlockState) Blocks.OAK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52312_, true);
                this.m_73434_(worldGenLevel0, $$19, 7, 9, 7, boundingBox4);
                this.m_73434_(worldGenLevel0, $$18, 6, 8, 7, boundingBox4);
                this.m_73434_(worldGenLevel0, $$19, 7, 8, 7, boundingBox4);
                BlockState $$20 = (BlockState) ((BlockState) $$13.m_61124_(FenceBlock.f_52312_, true)).m_61124_(FenceBlock.f_52310_, true);
                this.m_73434_(worldGenLevel0, $$20, 6, 7, 7, boundingBox4);
                this.m_73434_(worldGenLevel0, $$20, 7, 7, 7, boundingBox4);
                this.m_73434_(worldGenLevel0, $$18, 5, 7, 7, boundingBox4);
                this.m_73434_(worldGenLevel0, $$19, 8, 7, 7, boundingBox4);
                this.m_73434_(worldGenLevel0, (BlockState) $$18.m_61124_(FenceBlock.f_52309_, true), 6, 7, 6, boundingBox4);
                this.m_73434_(worldGenLevel0, (BlockState) $$18.m_61124_(FenceBlock.f_52311_, true), 6, 7, 8, boundingBox4);
                this.m_73434_(worldGenLevel0, (BlockState) $$19.m_61124_(FenceBlock.f_52309_, true), 7, 7, 6, boundingBox4);
                this.m_73434_(worldGenLevel0, (BlockState) $$19.m_61124_(FenceBlock.f_52311_, true), 7, 7, 8, boundingBox4);
                BlockState $$21 = Blocks.TORCH.defaultBlockState();
                this.m_73434_(worldGenLevel0, $$21, 5, 8, 7, boundingBox4);
                this.m_73434_(worldGenLevel0, $$21, 8, 8, 7, boundingBox4);
                this.m_73434_(worldGenLevel0, $$21, 6, 8, 6, boundingBox4);
                this.m_73434_(worldGenLevel0, $$21, 6, 8, 8, boundingBox4);
                this.m_73434_(worldGenLevel0, $$21, 7, 8, 6, boundingBox4);
                this.m_73434_(worldGenLevel0, $$21, 7, 8, 8, boundingBox4);
            }
            this.m_213787_(worldGenLevel0, boundingBox4, randomSource3, 3, 3, 5, BuiltInLootTables.STRONGHOLD_LIBRARY);
            if (this.isTall) {
                this.m_73434_(worldGenLevel0, f_73382_, 12, 9, 1, boundingBox4);
                this.m_213787_(worldGenLevel0, boundingBox4, randomSource3, 12, 8, 1, BuiltInLootTables.STRONGHOLD_LIBRARY);
            }
        }
    }

    static class PieceWeight {

        public final Class<? extends StrongholdPieces.StrongholdPiece> pieceClass;

        public final int weight;

        public int placeCount;

        public final int maxPlaceCount;

        public PieceWeight(Class<? extends StrongholdPieces.StrongholdPiece> classExtendsStrongholdPiecesStrongholdPiece0, int int1, int int2) {
            this.pieceClass = classExtendsStrongholdPiecesStrongholdPiece0;
            this.weight = int1;
            this.maxPlaceCount = int2;
        }

        public boolean doPlace(int int0) {
            return this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount;
        }

        public boolean isValid() {
            return this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount;
        }
    }

    public static class PortalRoom extends StrongholdPieces.StrongholdPiece {

        protected static final int WIDTH = 11;

        protected static final int HEIGHT = 8;

        protected static final int DEPTH = 16;

        private boolean hasPlacedSpawner;

        public PortalRoom(int int0, BoundingBox boundingBox1, Direction direction2) {
            super(StructurePieceType.STRONGHOLD_PORTAL_ROOM, int0, boundingBox1);
            this.m_73519_(direction2);
        }

        public PortalRoom(CompoundTag compoundTag0) {
            super(StructurePieceType.STRONGHOLD_PORTAL_ROOM, compoundTag0);
            this.hasPlacedSpawner = compoundTag0.getBoolean("Mob");
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1) {
            super.addAdditionalSaveData(structurePieceSerializationContext0, compoundTag1);
            compoundTag1.putBoolean("Mob", this.hasPlacedSpawner);
        }

        @Override
        public void addChildren(StructurePiece structurePiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2) {
            if (structurePiece0 != null) {
                ((StrongholdPieces.StartPiece) structurePiece0).portalRoomPiece = this;
            }
        }

        public static StrongholdPieces.PortalRoom createPiece(StructurePieceAccessor structurePieceAccessor0, int int1, int int2, int int3, Direction direction4, int int5) {
            BoundingBox $$6 = BoundingBox.orientBox(int1, int2, int3, -4, -1, 0, 11, 8, 16, direction4);
            return m_229888_($$6) && structurePieceAccessor0.findCollisionPiece($$6) == null ? new StrongholdPieces.PortalRoom(int5, $$6, direction4) : null;
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            this.m_226776_(worldGenLevel0, boundingBox4, 0, 0, 0, 10, 7, 15, false, randomSource3, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.m_229880_(worldGenLevel0, randomSource3, boundingBox4, StrongholdPieces.StrongholdPiece.SmallDoorType.GRATES, 4, 1, 0);
            int $$7 = 6;
            this.m_226776_(worldGenLevel0, boundingBox4, 1, 6, 1, 1, 6, 14, false, randomSource3, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 9, 6, 1, 9, 6, 14, false, randomSource3, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 2, 6, 1, 8, 6, 2, false, randomSource3, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 2, 6, 14, 8, 6, 14, false, randomSource3, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 1, 1, 1, 2, 1, 4, false, randomSource3, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 8, 1, 1, 9, 1, 4, false, randomSource3, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 1, 1, 1, 1, 3, Blocks.LAVA.defaultBlockState(), Blocks.LAVA.defaultBlockState(), false);
            this.m_73441_(worldGenLevel0, boundingBox4, 9, 1, 1, 9, 1, 3, Blocks.LAVA.defaultBlockState(), Blocks.LAVA.defaultBlockState(), false);
            this.m_226776_(worldGenLevel0, boundingBox4, 3, 1, 8, 7, 1, 12, false, randomSource3, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.m_73441_(worldGenLevel0, boundingBox4, 4, 1, 9, 6, 1, 11, Blocks.LAVA.defaultBlockState(), Blocks.LAVA.defaultBlockState(), false);
            BlockState $$8 = (BlockState) ((BlockState) Blocks.IRON_BARS.defaultBlockState().m_61124_(IronBarsBlock.f_52309_, true)).m_61124_(IronBarsBlock.f_52311_, true);
            BlockState $$9 = (BlockState) ((BlockState) Blocks.IRON_BARS.defaultBlockState().m_61124_(IronBarsBlock.f_52312_, true)).m_61124_(IronBarsBlock.f_52310_, true);
            for (int $$10 = 3; $$10 < 14; $$10 += 2) {
                this.m_73441_(worldGenLevel0, boundingBox4, 0, 3, $$10, 0, 4, $$10, $$8, $$8, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 10, 3, $$10, 10, 4, $$10, $$8, $$8, false);
            }
            for (int $$11 = 2; $$11 < 9; $$11 += 2) {
                this.m_73441_(worldGenLevel0, boundingBox4, $$11, 3, 15, $$11, 4, 15, $$9, $$9, false);
            }
            BlockState $$12 = (BlockState) Blocks.STONE_BRICK_STAIRS.defaultBlockState().m_61124_(StairBlock.FACING, Direction.NORTH);
            this.m_226776_(worldGenLevel0, boundingBox4, 4, 1, 5, 6, 1, 7, false, randomSource3, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 4, 2, 6, 6, 2, 7, false, randomSource3, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 4, 3, 7, 6, 3, 7, false, randomSource3, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            for (int $$13 = 4; $$13 <= 6; $$13++) {
                this.m_73434_(worldGenLevel0, $$12, $$13, 1, 4, boundingBox4);
                this.m_73434_(worldGenLevel0, $$12, $$13, 2, 5, boundingBox4);
                this.m_73434_(worldGenLevel0, $$12, $$13, 3, 6, boundingBox4);
            }
            BlockState $$14 = (BlockState) Blocks.END_PORTAL_FRAME.defaultBlockState().m_61124_(EndPortalFrameBlock.FACING, Direction.NORTH);
            BlockState $$15 = (BlockState) Blocks.END_PORTAL_FRAME.defaultBlockState().m_61124_(EndPortalFrameBlock.FACING, Direction.SOUTH);
            BlockState $$16 = (BlockState) Blocks.END_PORTAL_FRAME.defaultBlockState().m_61124_(EndPortalFrameBlock.FACING, Direction.EAST);
            BlockState $$17 = (BlockState) Blocks.END_PORTAL_FRAME.defaultBlockState().m_61124_(EndPortalFrameBlock.FACING, Direction.WEST);
            boolean $$18 = true;
            boolean[] $$19 = new boolean[12];
            for (int $$20 = 0; $$20 < $$19.length; $$20++) {
                $$19[$$20] = randomSource3.nextFloat() > 0.9F;
                $$18 &= $$19[$$20];
            }
            this.m_73434_(worldGenLevel0, (BlockState) $$14.m_61124_(EndPortalFrameBlock.HAS_EYE, $$19[0]), 4, 3, 8, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) $$14.m_61124_(EndPortalFrameBlock.HAS_EYE, $$19[1]), 5, 3, 8, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) $$14.m_61124_(EndPortalFrameBlock.HAS_EYE, $$19[2]), 6, 3, 8, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) $$15.m_61124_(EndPortalFrameBlock.HAS_EYE, $$19[3]), 4, 3, 12, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) $$15.m_61124_(EndPortalFrameBlock.HAS_EYE, $$19[4]), 5, 3, 12, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) $$15.m_61124_(EndPortalFrameBlock.HAS_EYE, $$19[5]), 6, 3, 12, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) $$16.m_61124_(EndPortalFrameBlock.HAS_EYE, $$19[6]), 3, 3, 9, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) $$16.m_61124_(EndPortalFrameBlock.HAS_EYE, $$19[7]), 3, 3, 10, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) $$16.m_61124_(EndPortalFrameBlock.HAS_EYE, $$19[8]), 3, 3, 11, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) $$17.m_61124_(EndPortalFrameBlock.HAS_EYE, $$19[9]), 7, 3, 9, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) $$17.m_61124_(EndPortalFrameBlock.HAS_EYE, $$19[10]), 7, 3, 10, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) $$17.m_61124_(EndPortalFrameBlock.HAS_EYE, $$19[11]), 7, 3, 11, boundingBox4);
            if ($$18) {
                BlockState $$21 = Blocks.END_PORTAL.defaultBlockState();
                this.m_73434_(worldGenLevel0, $$21, 4, 3, 9, boundingBox4);
                this.m_73434_(worldGenLevel0, $$21, 5, 3, 9, boundingBox4);
                this.m_73434_(worldGenLevel0, $$21, 6, 3, 9, boundingBox4);
                this.m_73434_(worldGenLevel0, $$21, 4, 3, 10, boundingBox4);
                this.m_73434_(worldGenLevel0, $$21, 5, 3, 10, boundingBox4);
                this.m_73434_(worldGenLevel0, $$21, 6, 3, 10, boundingBox4);
                this.m_73434_(worldGenLevel0, $$21, 4, 3, 11, boundingBox4);
                this.m_73434_(worldGenLevel0, $$21, 5, 3, 11, boundingBox4);
                this.m_73434_(worldGenLevel0, $$21, 6, 3, 11, boundingBox4);
            }
            if (!this.hasPlacedSpawner) {
                BlockPos $$22 = this.m_163582_(5, 3, 6);
                if (boundingBox4.isInside($$22)) {
                    this.hasPlacedSpawner = true;
                    worldGenLevel0.m_7731_($$22, Blocks.SPAWNER.defaultBlockState(), 2);
                    if (worldGenLevel0.m_7702_($$22) instanceof SpawnerBlockEntity $$24) {
                        $$24.setEntityId(EntityType.SILVERFISH, randomSource3);
                    }
                }
            }
        }
    }

    public static class PrisonHall extends StrongholdPieces.StrongholdPiece {

        protected static final int WIDTH = 9;

        protected static final int HEIGHT = 5;

        protected static final int DEPTH = 11;

        public PrisonHall(int int0, RandomSource randomSource1, BoundingBox boundingBox2, Direction direction3) {
            super(StructurePieceType.STRONGHOLD_PRISON_HALL, int0, boundingBox2);
            this.m_73519_(direction3);
            this.f_229872_ = this.m_229899_(randomSource1);
        }

        public PrisonHall(CompoundTag compoundTag0) {
            super(StructurePieceType.STRONGHOLD_PRISON_HALL, compoundTag0);
        }

        @Override
        public void addChildren(StructurePiece structurePiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2) {
            this.m_229893_((StrongholdPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 1, 1);
        }

        public static StrongholdPieces.PrisonHall createPiece(StructurePieceAccessor structurePieceAccessor0, RandomSource randomSource1, int int2, int int3, int int4, Direction direction5, int int6) {
            BoundingBox $$7 = BoundingBox.orientBox(int2, int3, int4, -1, -1, 0, 9, 5, 11, direction5);
            return m_229888_($$7) && structurePieceAccessor0.findCollisionPiece($$7) == null ? new StrongholdPieces.PrisonHall(int6, randomSource1, $$7, direction5) : null;
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            this.m_226776_(worldGenLevel0, boundingBox4, 0, 0, 0, 8, 4, 10, true, randomSource3, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.m_229880_(worldGenLevel0, randomSource3, boundingBox4, this.f_229872_, 1, 1, 0);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 1, 10, 3, 3, 10, f_73382_, f_73382_, false);
            this.m_226776_(worldGenLevel0, boundingBox4, 4, 1, 1, 4, 3, 1, false, randomSource3, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 4, 1, 3, 4, 3, 3, false, randomSource3, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 4, 1, 7, 4, 3, 7, false, randomSource3, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 4, 1, 9, 4, 3, 9, false, randomSource3, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            for (int $$7 = 1; $$7 <= 3; $$7++) {
                this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.IRON_BARS.defaultBlockState().m_61124_(IronBarsBlock.f_52309_, true)).m_61124_(IronBarsBlock.f_52311_, true), 4, $$7, 4, boundingBox4);
                this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) ((BlockState) Blocks.IRON_BARS.defaultBlockState().m_61124_(IronBarsBlock.f_52309_, true)).m_61124_(IronBarsBlock.f_52311_, true)).m_61124_(IronBarsBlock.f_52310_, true), 4, $$7, 5, boundingBox4);
                this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.IRON_BARS.defaultBlockState().m_61124_(IronBarsBlock.f_52309_, true)).m_61124_(IronBarsBlock.f_52311_, true), 4, $$7, 6, boundingBox4);
                this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.IRON_BARS.defaultBlockState().m_61124_(IronBarsBlock.f_52312_, true)).m_61124_(IronBarsBlock.f_52310_, true), 5, $$7, 5, boundingBox4);
                this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.IRON_BARS.defaultBlockState().m_61124_(IronBarsBlock.f_52312_, true)).m_61124_(IronBarsBlock.f_52310_, true), 6, $$7, 5, boundingBox4);
                this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.IRON_BARS.defaultBlockState().m_61124_(IronBarsBlock.f_52312_, true)).m_61124_(IronBarsBlock.f_52310_, true), 7, $$7, 5, boundingBox4);
            }
            this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.IRON_BARS.defaultBlockState().m_61124_(IronBarsBlock.f_52309_, true)).m_61124_(IronBarsBlock.f_52311_, true), 4, 3, 2, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.IRON_BARS.defaultBlockState().m_61124_(IronBarsBlock.f_52309_, true)).m_61124_(IronBarsBlock.f_52311_, true), 4, 3, 8, boundingBox4);
            BlockState $$8 = (BlockState) Blocks.IRON_DOOR.defaultBlockState().m_61124_(DoorBlock.FACING, Direction.WEST);
            BlockState $$9 = (BlockState) ((BlockState) Blocks.IRON_DOOR.defaultBlockState().m_61124_(DoorBlock.FACING, Direction.WEST)).m_61124_(DoorBlock.HALF, DoubleBlockHalf.UPPER);
            this.m_73434_(worldGenLevel0, $$8, 4, 1, 2, boundingBox4);
            this.m_73434_(worldGenLevel0, $$9, 4, 2, 2, boundingBox4);
            this.m_73434_(worldGenLevel0, $$8, 4, 1, 8, boundingBox4);
            this.m_73434_(worldGenLevel0, $$9, 4, 2, 8, boundingBox4);
        }
    }

    public static class RightTurn extends StrongholdPieces.Turn {

        public RightTurn(int int0, RandomSource randomSource1, BoundingBox boundingBox2, Direction direction3) {
            super(StructurePieceType.STRONGHOLD_RIGHT_TURN, int0, boundingBox2);
            this.m_73519_(direction3);
            this.f_229872_ = this.m_229899_(randomSource1);
        }

        public RightTurn(CompoundTag compoundTag0) {
            super(StructurePieceType.STRONGHOLD_RIGHT_TURN, compoundTag0);
        }

        @Override
        public void addChildren(StructurePiece structurePiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2) {
            Direction $$3 = this.m_73549_();
            if ($$3 != Direction.NORTH && $$3 != Direction.EAST) {
                this.m_229901_((StrongholdPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 1, 1);
            } else {
                this.m_229907_((StrongholdPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 1, 1);
            }
        }

        public static StrongholdPieces.RightTurn createPiece(StructurePieceAccessor structurePieceAccessor0, RandomSource randomSource1, int int2, int int3, int int4, Direction direction5, int int6) {
            BoundingBox $$7 = BoundingBox.orientBox(int2, int3, int4, -1, -1, 0, 5, 5, 5, direction5);
            return m_229888_($$7) && structurePieceAccessor0.findCollisionPiece($$7) == null ? new StrongholdPieces.RightTurn(int6, randomSource1, $$7, direction5) : null;
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            this.m_226776_(worldGenLevel0, boundingBox4, 0, 0, 0, 4, 4, 4, true, randomSource3, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.m_229880_(worldGenLevel0, randomSource3, boundingBox4, this.f_229872_, 1, 1, 0);
            Direction $$7 = this.m_73549_();
            if ($$7 != Direction.NORTH && $$7 != Direction.EAST) {
                this.m_73441_(worldGenLevel0, boundingBox4, 0, 1, 1, 0, 3, 3, f_73382_, f_73382_, false);
            } else {
                this.m_73441_(worldGenLevel0, boundingBox4, 4, 1, 1, 4, 3, 3, f_73382_, f_73382_, false);
            }
        }
    }

    public static class RoomCrossing extends StrongholdPieces.StrongholdPiece {

        protected static final int WIDTH = 11;

        protected static final int HEIGHT = 7;

        protected static final int DEPTH = 11;

        protected final int type;

        public RoomCrossing(int int0, RandomSource randomSource1, BoundingBox boundingBox2, Direction direction3) {
            super(StructurePieceType.STRONGHOLD_ROOM_CROSSING, int0, boundingBox2);
            this.m_73519_(direction3);
            this.f_229872_ = this.m_229899_(randomSource1);
            this.type = randomSource1.nextInt(5);
        }

        public RoomCrossing(CompoundTag compoundTag0) {
            super(StructurePieceType.STRONGHOLD_ROOM_CROSSING, compoundTag0);
            this.type = compoundTag0.getInt("Type");
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1) {
            super.addAdditionalSaveData(structurePieceSerializationContext0, compoundTag1);
            compoundTag1.putInt("Type", this.type);
        }

        @Override
        public void addChildren(StructurePiece structurePiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2) {
            this.m_229893_((StrongholdPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 4, 1);
            this.m_229901_((StrongholdPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 1, 4);
            this.m_229907_((StrongholdPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 1, 4);
        }

        public static StrongholdPieces.RoomCrossing createPiece(StructurePieceAccessor structurePieceAccessor0, RandomSource randomSource1, int int2, int int3, int int4, Direction direction5, int int6) {
            BoundingBox $$7 = BoundingBox.orientBox(int2, int3, int4, -4, -1, 0, 11, 7, 11, direction5);
            return m_229888_($$7) && structurePieceAccessor0.findCollisionPiece($$7) == null ? new StrongholdPieces.RoomCrossing(int6, randomSource1, $$7, direction5) : null;
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            this.m_226776_(worldGenLevel0, boundingBox4, 0, 0, 0, 10, 6, 10, true, randomSource3, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.m_229880_(worldGenLevel0, randomSource3, boundingBox4, this.f_229872_, 4, 1, 0);
            this.m_73441_(worldGenLevel0, boundingBox4, 4, 1, 10, 6, 3, 10, f_73382_, f_73382_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 1, 4, 0, 3, 6, f_73382_, f_73382_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 10, 1, 4, 10, 3, 6, f_73382_, f_73382_, false);
            switch(this.type) {
                case 0:
                    this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 5, 1, 5, boundingBox4);
                    this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 5, 2, 5, boundingBox4);
                    this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 5, 3, 5, boundingBox4);
                    this.m_73434_(worldGenLevel0, (BlockState) Blocks.WALL_TORCH.defaultBlockState().m_61124_(WallTorchBlock.FACING, Direction.WEST), 4, 3, 5, boundingBox4);
                    this.m_73434_(worldGenLevel0, (BlockState) Blocks.WALL_TORCH.defaultBlockState().m_61124_(WallTorchBlock.FACING, Direction.EAST), 6, 3, 5, boundingBox4);
                    this.m_73434_(worldGenLevel0, (BlockState) Blocks.WALL_TORCH.defaultBlockState().m_61124_(WallTorchBlock.FACING, Direction.SOUTH), 5, 3, 4, boundingBox4);
                    this.m_73434_(worldGenLevel0, (BlockState) Blocks.WALL_TORCH.defaultBlockState().m_61124_(WallTorchBlock.FACING, Direction.NORTH), 5, 3, 6, boundingBox4);
                    this.m_73434_(worldGenLevel0, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 4, 1, 4, boundingBox4);
                    this.m_73434_(worldGenLevel0, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 4, 1, 5, boundingBox4);
                    this.m_73434_(worldGenLevel0, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 4, 1, 6, boundingBox4);
                    this.m_73434_(worldGenLevel0, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 6, 1, 4, boundingBox4);
                    this.m_73434_(worldGenLevel0, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 6, 1, 5, boundingBox4);
                    this.m_73434_(worldGenLevel0, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 6, 1, 6, boundingBox4);
                    this.m_73434_(worldGenLevel0, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 5, 1, 4, boundingBox4);
                    this.m_73434_(worldGenLevel0, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 5, 1, 6, boundingBox4);
                    break;
                case 1:
                    for (int $$7 = 0; $$7 < 5; $$7++) {
                        this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 3, 1, 3 + $$7, boundingBox4);
                        this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 7, 1, 3 + $$7, boundingBox4);
                        this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 3 + $$7, 1, 3, boundingBox4);
                        this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 3 + $$7, 1, 7, boundingBox4);
                    }
                    this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 5, 1, 5, boundingBox4);
                    this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 5, 2, 5, boundingBox4);
                    this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 5, 3, 5, boundingBox4);
                    this.m_73434_(worldGenLevel0, Blocks.WATER.defaultBlockState(), 5, 4, 5, boundingBox4);
                    break;
                case 2:
                    for (int $$8 = 1; $$8 <= 9; $$8++) {
                        this.m_73434_(worldGenLevel0, Blocks.COBBLESTONE.defaultBlockState(), 1, 3, $$8, boundingBox4);
                        this.m_73434_(worldGenLevel0, Blocks.COBBLESTONE.defaultBlockState(), 9, 3, $$8, boundingBox4);
                    }
                    for (int $$9 = 1; $$9 <= 9; $$9++) {
                        this.m_73434_(worldGenLevel0, Blocks.COBBLESTONE.defaultBlockState(), $$9, 3, 1, boundingBox4);
                        this.m_73434_(worldGenLevel0, Blocks.COBBLESTONE.defaultBlockState(), $$9, 3, 9, boundingBox4);
                    }
                    this.m_73434_(worldGenLevel0, Blocks.COBBLESTONE.defaultBlockState(), 5, 1, 4, boundingBox4);
                    this.m_73434_(worldGenLevel0, Blocks.COBBLESTONE.defaultBlockState(), 5, 1, 6, boundingBox4);
                    this.m_73434_(worldGenLevel0, Blocks.COBBLESTONE.defaultBlockState(), 5, 3, 4, boundingBox4);
                    this.m_73434_(worldGenLevel0, Blocks.COBBLESTONE.defaultBlockState(), 5, 3, 6, boundingBox4);
                    this.m_73434_(worldGenLevel0, Blocks.COBBLESTONE.defaultBlockState(), 4, 1, 5, boundingBox4);
                    this.m_73434_(worldGenLevel0, Blocks.COBBLESTONE.defaultBlockState(), 6, 1, 5, boundingBox4);
                    this.m_73434_(worldGenLevel0, Blocks.COBBLESTONE.defaultBlockState(), 4, 3, 5, boundingBox4);
                    this.m_73434_(worldGenLevel0, Blocks.COBBLESTONE.defaultBlockState(), 6, 3, 5, boundingBox4);
                    for (int $$10 = 1; $$10 <= 3; $$10++) {
                        this.m_73434_(worldGenLevel0, Blocks.COBBLESTONE.defaultBlockState(), 4, $$10, 4, boundingBox4);
                        this.m_73434_(worldGenLevel0, Blocks.COBBLESTONE.defaultBlockState(), 6, $$10, 4, boundingBox4);
                        this.m_73434_(worldGenLevel0, Blocks.COBBLESTONE.defaultBlockState(), 4, $$10, 6, boundingBox4);
                        this.m_73434_(worldGenLevel0, Blocks.COBBLESTONE.defaultBlockState(), 6, $$10, 6, boundingBox4);
                    }
                    this.m_73434_(worldGenLevel0, Blocks.WALL_TORCH.defaultBlockState(), 5, 3, 5, boundingBox4);
                    for (int $$11 = 2; $$11 <= 8; $$11++) {
                        this.m_73434_(worldGenLevel0, Blocks.OAK_PLANKS.defaultBlockState(), 2, 3, $$11, boundingBox4);
                        this.m_73434_(worldGenLevel0, Blocks.OAK_PLANKS.defaultBlockState(), 3, 3, $$11, boundingBox4);
                        if ($$11 <= 3 || $$11 >= 7) {
                            this.m_73434_(worldGenLevel0, Blocks.OAK_PLANKS.defaultBlockState(), 4, 3, $$11, boundingBox4);
                            this.m_73434_(worldGenLevel0, Blocks.OAK_PLANKS.defaultBlockState(), 5, 3, $$11, boundingBox4);
                            this.m_73434_(worldGenLevel0, Blocks.OAK_PLANKS.defaultBlockState(), 6, 3, $$11, boundingBox4);
                        }
                        this.m_73434_(worldGenLevel0, Blocks.OAK_PLANKS.defaultBlockState(), 7, 3, $$11, boundingBox4);
                        this.m_73434_(worldGenLevel0, Blocks.OAK_PLANKS.defaultBlockState(), 8, 3, $$11, boundingBox4);
                    }
                    BlockState $$12 = (BlockState) Blocks.LADDER.defaultBlockState().m_61124_(LadderBlock.FACING, Direction.WEST);
                    this.m_73434_(worldGenLevel0, $$12, 9, 1, 3, boundingBox4);
                    this.m_73434_(worldGenLevel0, $$12, 9, 2, 3, boundingBox4);
                    this.m_73434_(worldGenLevel0, $$12, 9, 3, 3, boundingBox4);
                    this.m_213787_(worldGenLevel0, boundingBox4, randomSource3, 3, 4, 8, BuiltInLootTables.STRONGHOLD_CROSSING);
            }
        }
    }

    static class SmoothStoneSelector extends StructurePiece.BlockSelector {

        @Override
        public void next(RandomSource randomSource0, int int1, int int2, int int3, boolean boolean4) {
            if (boolean4) {
                float $$5 = randomSource0.nextFloat();
                if ($$5 < 0.2F) {
                    this.f_73553_ = Blocks.CRACKED_STONE_BRICKS.defaultBlockState();
                } else if ($$5 < 0.5F) {
                    this.f_73553_ = Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
                } else if ($$5 < 0.55F) {
                    this.f_73553_ = Blocks.INFESTED_STONE_BRICKS.defaultBlockState();
                } else {
                    this.f_73553_ = Blocks.STONE_BRICKS.defaultBlockState();
                }
            } else {
                this.f_73553_ = Blocks.CAVE_AIR.defaultBlockState();
            }
        }
    }

    public static class StairsDown extends StrongholdPieces.StrongholdPiece {

        private static final int WIDTH = 5;

        private static final int HEIGHT = 11;

        private static final int DEPTH = 5;

        private final boolean isSource;

        public StairsDown(StructurePieceType structurePieceType0, int int1, int int2, int int3, Direction direction4) {
            super(structurePieceType0, int1, m_163541_(int2, 64, int3, direction4, 5, 11, 5));
            this.isSource = true;
            this.m_73519_(direction4);
            this.f_229872_ = StrongholdPieces.StrongholdPiece.SmallDoorType.OPENING;
        }

        public StairsDown(int int0, RandomSource randomSource1, BoundingBox boundingBox2, Direction direction3) {
            super(StructurePieceType.STRONGHOLD_STAIRS_DOWN, int0, boundingBox2);
            this.isSource = false;
            this.m_73519_(direction3);
            this.f_229872_ = this.m_229899_(randomSource1);
        }

        public StairsDown(StructurePieceType structurePieceType0, CompoundTag compoundTag1) {
            super(structurePieceType0, compoundTag1);
            this.isSource = compoundTag1.getBoolean("Source");
        }

        public StairsDown(CompoundTag compoundTag0) {
            this(StructurePieceType.STRONGHOLD_STAIRS_DOWN, compoundTag0);
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1) {
            super.addAdditionalSaveData(structurePieceSerializationContext0, compoundTag1);
            compoundTag1.putBoolean("Source", this.isSource);
        }

        @Override
        public void addChildren(StructurePiece structurePiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2) {
            if (this.isSource) {
                StrongholdPieces.imposedPiece = StrongholdPieces.FiveCrossing.class;
            }
            this.m_229893_((StrongholdPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 1, 1);
        }

        public static StrongholdPieces.StairsDown createPiece(StructurePieceAccessor structurePieceAccessor0, RandomSource randomSource1, int int2, int int3, int int4, Direction direction5, int int6) {
            BoundingBox $$7 = BoundingBox.orientBox(int2, int3, int4, -1, -7, 0, 5, 11, 5, direction5);
            return m_229888_($$7) && structurePieceAccessor0.findCollisionPiece($$7) == null ? new StrongholdPieces.StairsDown(int6, randomSource1, $$7, direction5) : null;
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            this.m_226776_(worldGenLevel0, boundingBox4, 0, 0, 0, 4, 10, 4, true, randomSource3, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.m_229880_(worldGenLevel0, randomSource3, boundingBox4, this.f_229872_, 1, 7, 0);
            this.m_229880_(worldGenLevel0, randomSource3, boundingBox4, StrongholdPieces.StrongholdPiece.SmallDoorType.OPENING, 1, 1, 4);
            this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 2, 6, 1, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 1, 5, 1, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 1, 6, 1, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 1, 5, 2, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 1, 4, 3, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 1, 5, 3, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 2, 4, 3, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 3, 3, 3, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 3, 4, 3, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 3, 3, 2, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 3, 2, 1, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 3, 3, 1, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 2, 2, 1, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 1, 1, 1, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 1, 2, 1, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 1, 1, 2, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 1, 1, 3, boundingBox4);
        }
    }

    public static class StartPiece extends StrongholdPieces.StairsDown {

        public StrongholdPieces.PieceWeight previousPiece;

        @Nullable
        public StrongholdPieces.PortalRoom portalRoomPiece;

        public final List<StructurePiece> pendingChildren = Lists.newArrayList();

        public StartPiece(RandomSource randomSource0, int int1, int int2) {
            super(StructurePieceType.STRONGHOLD_START, 0, int1, int2, m_226760_(randomSource0));
        }

        public StartPiece(CompoundTag compoundTag0) {
            super(StructurePieceType.STRONGHOLD_START, compoundTag0);
        }

        @Override
        public BlockPos getLocatorPosition() {
            return this.portalRoomPiece != null ? this.portalRoomPiece.m_142171_() : super.m_142171_();
        }
    }

    public static class Straight extends StrongholdPieces.StrongholdPiece {

        private static final int WIDTH = 5;

        private static final int HEIGHT = 5;

        private static final int DEPTH = 7;

        private final boolean leftChild;

        private final boolean rightChild;

        public Straight(int int0, RandomSource randomSource1, BoundingBox boundingBox2, Direction direction3) {
            super(StructurePieceType.STRONGHOLD_STRAIGHT, int0, boundingBox2);
            this.m_73519_(direction3);
            this.f_229872_ = this.m_229899_(randomSource1);
            this.leftChild = randomSource1.nextInt(2) == 0;
            this.rightChild = randomSource1.nextInt(2) == 0;
        }

        public Straight(CompoundTag compoundTag0) {
            super(StructurePieceType.STRONGHOLD_STRAIGHT, compoundTag0);
            this.leftChild = compoundTag0.getBoolean("Left");
            this.rightChild = compoundTag0.getBoolean("Right");
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1) {
            super.addAdditionalSaveData(structurePieceSerializationContext0, compoundTag1);
            compoundTag1.putBoolean("Left", this.leftChild);
            compoundTag1.putBoolean("Right", this.rightChild);
        }

        @Override
        public void addChildren(StructurePiece structurePiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2) {
            this.m_229893_((StrongholdPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 1, 1);
            if (this.leftChild) {
                this.m_229901_((StrongholdPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 1, 2);
            }
            if (this.rightChild) {
                this.m_229907_((StrongholdPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 1, 2);
            }
        }

        public static StrongholdPieces.Straight createPiece(StructurePieceAccessor structurePieceAccessor0, RandomSource randomSource1, int int2, int int3, int int4, Direction direction5, int int6) {
            BoundingBox $$7 = BoundingBox.orientBox(int2, int3, int4, -1, -1, 0, 5, 5, 7, direction5);
            return m_229888_($$7) && structurePieceAccessor0.findCollisionPiece($$7) == null ? new StrongholdPieces.Straight(int6, randomSource1, $$7, direction5) : null;
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            this.m_226776_(worldGenLevel0, boundingBox4, 0, 0, 0, 4, 4, 6, true, randomSource3, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.m_229880_(worldGenLevel0, randomSource3, boundingBox4, this.f_229872_, 1, 1, 0);
            this.m_229880_(worldGenLevel0, randomSource3, boundingBox4, StrongholdPieces.StrongholdPiece.SmallDoorType.OPENING, 1, 1, 6);
            BlockState $$7 = (BlockState) Blocks.WALL_TORCH.defaultBlockState().m_61124_(WallTorchBlock.FACING, Direction.EAST);
            BlockState $$8 = (BlockState) Blocks.WALL_TORCH.defaultBlockState().m_61124_(WallTorchBlock.FACING, Direction.WEST);
            this.m_226803_(worldGenLevel0, boundingBox4, randomSource3, 0.1F, 1, 2, 1, $$7);
            this.m_226803_(worldGenLevel0, boundingBox4, randomSource3, 0.1F, 3, 2, 1, $$8);
            this.m_226803_(worldGenLevel0, boundingBox4, randomSource3, 0.1F, 1, 2, 5, $$7);
            this.m_226803_(worldGenLevel0, boundingBox4, randomSource3, 0.1F, 3, 2, 5, $$8);
            if (this.leftChild) {
                this.m_73441_(worldGenLevel0, boundingBox4, 0, 1, 2, 0, 3, 4, f_73382_, f_73382_, false);
            }
            if (this.rightChild) {
                this.m_73441_(worldGenLevel0, boundingBox4, 4, 1, 2, 4, 3, 4, f_73382_, f_73382_, false);
            }
        }
    }

    public static class StraightStairsDown extends StrongholdPieces.StrongholdPiece {

        private static final int WIDTH = 5;

        private static final int HEIGHT = 11;

        private static final int DEPTH = 8;

        public StraightStairsDown(int int0, RandomSource randomSource1, BoundingBox boundingBox2, Direction direction3) {
            super(StructurePieceType.STRONGHOLD_STRAIGHT_STAIRS_DOWN, int0, boundingBox2);
            this.m_73519_(direction3);
            this.f_229872_ = this.m_229899_(randomSource1);
        }

        public StraightStairsDown(CompoundTag compoundTag0) {
            super(StructurePieceType.STRONGHOLD_STRAIGHT_STAIRS_DOWN, compoundTag0);
        }

        @Override
        public void addChildren(StructurePiece structurePiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2) {
            this.m_229893_((StrongholdPieces.StartPiece) structurePiece0, structurePieceAccessor1, randomSource2, 1, 1);
        }

        public static StrongholdPieces.StraightStairsDown createPiece(StructurePieceAccessor structurePieceAccessor0, RandomSource randomSource1, int int2, int int3, int int4, Direction direction5, int int6) {
            BoundingBox $$7 = BoundingBox.orientBox(int2, int3, int4, -1, -7, 0, 5, 11, 8, direction5);
            return m_229888_($$7) && structurePieceAccessor0.findCollisionPiece($$7) == null ? new StrongholdPieces.StraightStairsDown(int6, randomSource1, $$7, direction5) : null;
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            this.m_226776_(worldGenLevel0, boundingBox4, 0, 0, 0, 4, 10, 7, true, randomSource3, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.m_229880_(worldGenLevel0, randomSource3, boundingBox4, this.f_229872_, 1, 7, 0);
            this.m_229880_(worldGenLevel0, randomSource3, boundingBox4, StrongholdPieces.StrongholdPiece.SmallDoorType.OPENING, 1, 1, 7);
            BlockState $$7 = (BlockState) Blocks.COBBLESTONE_STAIRS.defaultBlockState().m_61124_(StairBlock.FACING, Direction.SOUTH);
            for (int $$8 = 0; $$8 < 6; $$8++) {
                this.m_73434_(worldGenLevel0, $$7, 1, 6 - $$8, 1 + $$8, boundingBox4);
                this.m_73434_(worldGenLevel0, $$7, 2, 6 - $$8, 1 + $$8, boundingBox4);
                this.m_73434_(worldGenLevel0, $$7, 3, 6 - $$8, 1 + $$8, boundingBox4);
                if ($$8 < 5) {
                    this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 1, 5 - $$8, 1 + $$8, boundingBox4);
                    this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 2, 5 - $$8, 1 + $$8, boundingBox4);
                    this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), 3, 5 - $$8, 1 + $$8, boundingBox4);
                }
            }
        }
    }

    abstract static class StrongholdPiece extends StructurePiece {

        protected StrongholdPieces.StrongholdPiece.SmallDoorType entryDoor = StrongholdPieces.StrongholdPiece.SmallDoorType.OPENING;

        protected StrongholdPiece(StructurePieceType structurePieceType0, int int1, BoundingBox boundingBox2) {
            super(structurePieceType0, int1, boundingBox2);
        }

        public StrongholdPiece(StructurePieceType structurePieceType0, CompoundTag compoundTag1) {
            super(structurePieceType0, compoundTag1);
            this.entryDoor = StrongholdPieces.StrongholdPiece.SmallDoorType.valueOf(compoundTag1.getString("EntryDoor"));
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1) {
            compoundTag1.putString("EntryDoor", this.entryDoor.name());
        }

        protected void generateSmallDoor(WorldGenLevel worldGenLevel0, RandomSource randomSource1, BoundingBox boundingBox2, StrongholdPieces.StrongholdPiece.SmallDoorType strongholdPiecesStrongholdPieceSmallDoorType3, int int4, int int5, int int6) {
            switch(strongholdPiecesStrongholdPieceSmallDoorType3) {
                case OPENING:
                    this.m_73441_(worldGenLevel0, boundingBox2, int4, int5, int6, int4 + 3 - 1, int5 + 3 - 1, int6, f_73382_, f_73382_, false);
                    break;
                case WOOD_DOOR:
                    this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), int4, int5, int6, boundingBox2);
                    this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), int4, int5 + 1, int6, boundingBox2);
                    this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), int4, int5 + 2, int6, boundingBox2);
                    this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), int4 + 1, int5 + 2, int6, boundingBox2);
                    this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), int4 + 2, int5 + 2, int6, boundingBox2);
                    this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), int4 + 2, int5 + 1, int6, boundingBox2);
                    this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), int4 + 2, int5, int6, boundingBox2);
                    this.m_73434_(worldGenLevel0, Blocks.OAK_DOOR.defaultBlockState(), int4 + 1, int5, int6, boundingBox2);
                    this.m_73434_(worldGenLevel0, (BlockState) Blocks.OAK_DOOR.defaultBlockState().m_61124_(DoorBlock.HALF, DoubleBlockHalf.UPPER), int4 + 1, int5 + 1, int6, boundingBox2);
                    break;
                case GRATES:
                    this.m_73434_(worldGenLevel0, Blocks.CAVE_AIR.defaultBlockState(), int4 + 1, int5, int6, boundingBox2);
                    this.m_73434_(worldGenLevel0, Blocks.CAVE_AIR.defaultBlockState(), int4 + 1, int5 + 1, int6, boundingBox2);
                    this.m_73434_(worldGenLevel0, (BlockState) Blocks.IRON_BARS.defaultBlockState().m_61124_(IronBarsBlock.f_52312_, true), int4, int5, int6, boundingBox2);
                    this.m_73434_(worldGenLevel0, (BlockState) Blocks.IRON_BARS.defaultBlockState().m_61124_(IronBarsBlock.f_52312_, true), int4, int5 + 1, int6, boundingBox2);
                    this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.IRON_BARS.defaultBlockState().m_61124_(IronBarsBlock.f_52310_, true)).m_61124_(IronBarsBlock.f_52312_, true), int4, int5 + 2, int6, boundingBox2);
                    this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.IRON_BARS.defaultBlockState().m_61124_(IronBarsBlock.f_52310_, true)).m_61124_(IronBarsBlock.f_52312_, true), int4 + 1, int5 + 2, int6, boundingBox2);
                    this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.IRON_BARS.defaultBlockState().m_61124_(IronBarsBlock.f_52310_, true)).m_61124_(IronBarsBlock.f_52312_, true), int4 + 2, int5 + 2, int6, boundingBox2);
                    this.m_73434_(worldGenLevel0, (BlockState) Blocks.IRON_BARS.defaultBlockState().m_61124_(IronBarsBlock.f_52310_, true), int4 + 2, int5 + 1, int6, boundingBox2);
                    this.m_73434_(worldGenLevel0, (BlockState) Blocks.IRON_BARS.defaultBlockState().m_61124_(IronBarsBlock.f_52310_, true), int4 + 2, int5, int6, boundingBox2);
                    break;
                case IRON_DOOR:
                    this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), int4, int5, int6, boundingBox2);
                    this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), int4, int5 + 1, int6, boundingBox2);
                    this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), int4, int5 + 2, int6, boundingBox2);
                    this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), int4 + 1, int5 + 2, int6, boundingBox2);
                    this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), int4 + 2, int5 + 2, int6, boundingBox2);
                    this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), int4 + 2, int5 + 1, int6, boundingBox2);
                    this.m_73434_(worldGenLevel0, Blocks.STONE_BRICKS.defaultBlockState(), int4 + 2, int5, int6, boundingBox2);
                    this.m_73434_(worldGenLevel0, Blocks.IRON_DOOR.defaultBlockState(), int4 + 1, int5, int6, boundingBox2);
                    this.m_73434_(worldGenLevel0, (BlockState) Blocks.IRON_DOOR.defaultBlockState().m_61124_(DoorBlock.HALF, DoubleBlockHalf.UPPER), int4 + 1, int5 + 1, int6, boundingBox2);
                    this.m_73434_(worldGenLevel0, (BlockState) Blocks.STONE_BUTTON.defaultBlockState().m_61124_(ButtonBlock.f_54117_, Direction.NORTH), int4 + 2, int5 + 1, int6 + 1, boundingBox2);
                    this.m_73434_(worldGenLevel0, (BlockState) Blocks.STONE_BUTTON.defaultBlockState().m_61124_(ButtonBlock.f_54117_, Direction.SOUTH), int4 + 2, int5 + 1, int6 - 1, boundingBox2);
            }
        }

        protected StrongholdPieces.StrongholdPiece.SmallDoorType randomSmallDoor(RandomSource randomSource0) {
            int $$1 = randomSource0.nextInt(5);
            switch($$1) {
                case 0:
                case 1:
                default:
                    return StrongholdPieces.StrongholdPiece.SmallDoorType.OPENING;
                case 2:
                    return StrongholdPieces.StrongholdPiece.SmallDoorType.WOOD_DOOR;
                case 3:
                    return StrongholdPieces.StrongholdPiece.SmallDoorType.GRATES;
                case 4:
                    return StrongholdPieces.StrongholdPiece.SmallDoorType.IRON_DOOR;
            }
        }

        @Nullable
        protected StructurePiece generateSmallDoorChildForward(StrongholdPieces.StartPiece strongholdPiecesStartPiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2, int int3, int int4) {
            Direction $$5 = this.m_73549_();
            if ($$5 != null) {
                switch($$5) {
                    case NORTH:
                        return StrongholdPieces.generateAndAddPiece(strongholdPiecesStartPiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() + int3, this.f_73383_.minY() + int4, this.f_73383_.minZ() - 1, $$5, this.m_73548_());
                    case SOUTH:
                        return StrongholdPieces.generateAndAddPiece(strongholdPiecesStartPiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() + int3, this.f_73383_.minY() + int4, this.f_73383_.maxZ() + 1, $$5, this.m_73548_());
                    case WEST:
                        return StrongholdPieces.generateAndAddPiece(strongholdPiecesStartPiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() - 1, this.f_73383_.minY() + int4, this.f_73383_.minZ() + int3, $$5, this.m_73548_());
                    case EAST:
                        return StrongholdPieces.generateAndAddPiece(strongholdPiecesStartPiece0, structurePieceAccessor1, randomSource2, this.f_73383_.maxX() + 1, this.f_73383_.minY() + int4, this.f_73383_.minZ() + int3, $$5, this.m_73548_());
                }
            }
            return null;
        }

        @Nullable
        protected StructurePiece generateSmallDoorChildLeft(StrongholdPieces.StartPiece strongholdPiecesStartPiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2, int int3, int int4) {
            Direction $$5 = this.m_73549_();
            if ($$5 != null) {
                switch($$5) {
                    case NORTH:
                        return StrongholdPieces.generateAndAddPiece(strongholdPiecesStartPiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() - 1, this.f_73383_.minY() + int3, this.f_73383_.minZ() + int4, Direction.WEST, this.m_73548_());
                    case SOUTH:
                        return StrongholdPieces.generateAndAddPiece(strongholdPiecesStartPiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() - 1, this.f_73383_.minY() + int3, this.f_73383_.minZ() + int4, Direction.WEST, this.m_73548_());
                    case WEST:
                        return StrongholdPieces.generateAndAddPiece(strongholdPiecesStartPiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() + int4, this.f_73383_.minY() + int3, this.f_73383_.minZ() - 1, Direction.NORTH, this.m_73548_());
                    case EAST:
                        return StrongholdPieces.generateAndAddPiece(strongholdPiecesStartPiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() + int4, this.f_73383_.minY() + int3, this.f_73383_.minZ() - 1, Direction.NORTH, this.m_73548_());
                }
            }
            return null;
        }

        @Nullable
        protected StructurePiece generateSmallDoorChildRight(StrongholdPieces.StartPiece strongholdPiecesStartPiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2, int int3, int int4) {
            Direction $$5 = this.m_73549_();
            if ($$5 != null) {
                switch($$5) {
                    case NORTH:
                        return StrongholdPieces.generateAndAddPiece(strongholdPiecesStartPiece0, structurePieceAccessor1, randomSource2, this.f_73383_.maxX() + 1, this.f_73383_.minY() + int3, this.f_73383_.minZ() + int4, Direction.EAST, this.m_73548_());
                    case SOUTH:
                        return StrongholdPieces.generateAndAddPiece(strongholdPiecesStartPiece0, structurePieceAccessor1, randomSource2, this.f_73383_.maxX() + 1, this.f_73383_.minY() + int3, this.f_73383_.minZ() + int4, Direction.EAST, this.m_73548_());
                    case WEST:
                        return StrongholdPieces.generateAndAddPiece(strongholdPiecesStartPiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() + int4, this.f_73383_.minY() + int3, this.f_73383_.maxZ() + 1, Direction.SOUTH, this.m_73548_());
                    case EAST:
                        return StrongholdPieces.generateAndAddPiece(strongholdPiecesStartPiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() + int4, this.f_73383_.minY() + int3, this.f_73383_.maxZ() + 1, Direction.SOUTH, this.m_73548_());
                }
            }
            return null;
        }

        protected static boolean isOkBox(BoundingBox boundingBox0) {
            return boundingBox0 != null && boundingBox0.minY() > 10;
        }

        protected static enum SmallDoorType {

            OPENING, WOOD_DOOR, GRATES, IRON_DOOR
        }
    }

    public abstract static class Turn extends StrongholdPieces.StrongholdPiece {

        protected static final int WIDTH = 5;

        protected static final int HEIGHT = 5;

        protected static final int DEPTH = 5;

        protected Turn(StructurePieceType structurePieceType0, int int1, BoundingBox boundingBox2) {
            super(structurePieceType0, int1, boundingBox2);
        }

        public Turn(StructurePieceType structurePieceType0, CompoundTag compoundTag1) {
            super(structurePieceType0, compoundTag1);
        }
    }
}