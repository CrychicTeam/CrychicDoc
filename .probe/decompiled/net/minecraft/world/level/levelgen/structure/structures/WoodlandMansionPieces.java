package net.minecraft.world.level.levelgen.structure.structures;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class WoodlandMansionPieces {

    public static void generateMansion(StructureTemplateManager structureTemplateManager0, BlockPos blockPos1, Rotation rotation2, List<WoodlandMansionPieces.WoodlandMansionPiece> listWoodlandMansionPiecesWoodlandMansionPiece3, RandomSource randomSource4) {
        WoodlandMansionPieces.MansionGrid $$5 = new WoodlandMansionPieces.MansionGrid(randomSource4);
        WoodlandMansionPieces.MansionPiecePlacer $$6 = new WoodlandMansionPieces.MansionPiecePlacer(structureTemplateManager0, randomSource4);
        $$6.createMansion(blockPos1, rotation2, listWoodlandMansionPiecesWoodlandMansionPiece3, $$5);
    }

    public static void main(String[] string0) {
        RandomSource $$1 = RandomSource.create();
        long $$2 = $$1.nextLong();
        System.out.println("Seed: " + $$2);
        $$1.setSeed($$2);
        WoodlandMansionPieces.MansionGrid $$3 = new WoodlandMansionPieces.MansionGrid($$1);
        $$3.print();
    }

    static class FirstFloorRoomCollection extends WoodlandMansionPieces.FloorRoomCollection {

        @Override
        public String get1x1(RandomSource randomSource0) {
            return "1x1_a" + (randomSource0.nextInt(5) + 1);
        }

        @Override
        public String get1x1Secret(RandomSource randomSource0) {
            return "1x1_as" + (randomSource0.nextInt(4) + 1);
        }

        @Override
        public String get1x2SideEntrance(RandomSource randomSource0, boolean boolean1) {
            return "1x2_a" + (randomSource0.nextInt(9) + 1);
        }

        @Override
        public String get1x2FrontEntrance(RandomSource randomSource0, boolean boolean1) {
            return "1x2_b" + (randomSource0.nextInt(5) + 1);
        }

        @Override
        public String get1x2Secret(RandomSource randomSource0) {
            return "1x2_s" + (randomSource0.nextInt(2) + 1);
        }

        @Override
        public String get2x2(RandomSource randomSource0) {
            return "2x2_a" + (randomSource0.nextInt(4) + 1);
        }

        @Override
        public String get2x2Secret(RandomSource randomSource0) {
            return "2x2_s1";
        }
    }

    abstract static class FloorRoomCollection {

        public abstract String get1x1(RandomSource var1);

        public abstract String get1x1Secret(RandomSource var1);

        public abstract String get1x2SideEntrance(RandomSource var1, boolean var2);

        public abstract String get1x2FrontEntrance(RandomSource var1, boolean var2);

        public abstract String get1x2Secret(RandomSource var1);

        public abstract String get2x2(RandomSource var1);

        public abstract String get2x2Secret(RandomSource var1);
    }

    static class MansionGrid {

        private static final int DEFAULT_SIZE = 11;

        private static final int CLEAR = 0;

        private static final int CORRIDOR = 1;

        private static final int ROOM = 2;

        private static final int START_ROOM = 3;

        private static final int TEST_ROOM = 4;

        private static final int BLOCKED = 5;

        private static final int ROOM_1x1 = 65536;

        private static final int ROOM_1x2 = 131072;

        private static final int ROOM_2x2 = 262144;

        private static final int ROOM_ORIGIN_FLAG = 1048576;

        private static final int ROOM_DOOR_FLAG = 2097152;

        private static final int ROOM_STAIRS_FLAG = 4194304;

        private static final int ROOM_CORRIDOR_FLAG = 8388608;

        private static final int ROOM_TYPE_MASK = 983040;

        private static final int ROOM_ID_MASK = 65535;

        private final RandomSource random;

        final WoodlandMansionPieces.SimpleGrid baseGrid;

        final WoodlandMansionPieces.SimpleGrid thirdFloorGrid;

        final WoodlandMansionPieces.SimpleGrid[] floorRooms;

        final int entranceX;

        final int entranceY;

        public MansionGrid(RandomSource randomSource0) {
            this.random = randomSource0;
            int $$1 = 11;
            this.entranceX = 7;
            this.entranceY = 4;
            this.baseGrid = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
            this.baseGrid.set(this.entranceX, this.entranceY, this.entranceX + 1, this.entranceY + 1, 3);
            this.baseGrid.set(this.entranceX - 1, this.entranceY, this.entranceX - 1, this.entranceY + 1, 2);
            this.baseGrid.set(this.entranceX + 2, this.entranceY - 2, this.entranceX + 3, this.entranceY + 3, 5);
            this.baseGrid.set(this.entranceX + 1, this.entranceY - 2, this.entranceX + 1, this.entranceY - 1, 1);
            this.baseGrid.set(this.entranceX + 1, this.entranceY + 2, this.entranceX + 1, this.entranceY + 3, 1);
            this.baseGrid.set(this.entranceX - 1, this.entranceY - 1, 1);
            this.baseGrid.set(this.entranceX - 1, this.entranceY + 2, 1);
            this.baseGrid.set(0, 0, 11, 1, 5);
            this.baseGrid.set(0, 9, 11, 11, 5);
            this.recursiveCorridor(this.baseGrid, this.entranceX, this.entranceY - 2, Direction.WEST, 6);
            this.recursiveCorridor(this.baseGrid, this.entranceX, this.entranceY + 3, Direction.WEST, 6);
            this.recursiveCorridor(this.baseGrid, this.entranceX - 2, this.entranceY - 1, Direction.WEST, 3);
            this.recursiveCorridor(this.baseGrid, this.entranceX - 2, this.entranceY + 2, Direction.WEST, 3);
            while (this.cleanEdges(this.baseGrid)) {
            }
            this.floorRooms = new WoodlandMansionPieces.SimpleGrid[3];
            this.floorRooms[0] = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
            this.floorRooms[1] = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
            this.floorRooms[2] = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
            this.identifyRooms(this.baseGrid, this.floorRooms[0]);
            this.identifyRooms(this.baseGrid, this.floorRooms[1]);
            this.floorRooms[0].set(this.entranceX + 1, this.entranceY, this.entranceX + 1, this.entranceY + 1, 8388608);
            this.floorRooms[1].set(this.entranceX + 1, this.entranceY, this.entranceX + 1, this.entranceY + 1, 8388608);
            this.thirdFloorGrid = new WoodlandMansionPieces.SimpleGrid(this.baseGrid.width, this.baseGrid.height, 5);
            this.setupThirdFloor();
            this.identifyRooms(this.thirdFloorGrid, this.floorRooms[2]);
        }

        public static boolean isHouse(WoodlandMansionPieces.SimpleGrid woodlandMansionPiecesSimpleGrid0, int int1, int int2) {
            int $$3 = woodlandMansionPiecesSimpleGrid0.get(int1, int2);
            return $$3 == 1 || $$3 == 2 || $$3 == 3 || $$3 == 4;
        }

        public boolean isRoomId(WoodlandMansionPieces.SimpleGrid woodlandMansionPiecesSimpleGrid0, int int1, int int2, int int3, int int4) {
            return (this.floorRooms[int3].get(int1, int2) & 65535) == int4;
        }

        @Nullable
        public Direction get1x2RoomDirection(WoodlandMansionPieces.SimpleGrid woodlandMansionPiecesSimpleGrid0, int int1, int int2, int int3, int int4) {
            for (Direction $$5 : Direction.Plane.HORIZONTAL) {
                if (this.isRoomId(woodlandMansionPiecesSimpleGrid0, int1 + $$5.getStepX(), int2 + $$5.getStepZ(), int3, int4)) {
                    return $$5;
                }
            }
            return null;
        }

        private void recursiveCorridor(WoodlandMansionPieces.SimpleGrid woodlandMansionPiecesSimpleGrid0, int int1, int int2, Direction direction3, int int4) {
            if (int4 > 0) {
                woodlandMansionPiecesSimpleGrid0.set(int1, int2, 1);
                woodlandMansionPiecesSimpleGrid0.setif(int1 + direction3.getStepX(), int2 + direction3.getStepZ(), 0, 1);
                for (int $$5 = 0; $$5 < 8; $$5++) {
                    Direction $$6 = Direction.from2DDataValue(this.random.nextInt(4));
                    if ($$6 != direction3.getOpposite() && ($$6 != Direction.EAST || !this.random.nextBoolean())) {
                        int $$7 = int1 + direction3.getStepX();
                        int $$8 = int2 + direction3.getStepZ();
                        if (woodlandMansionPiecesSimpleGrid0.get($$7 + $$6.getStepX(), $$8 + $$6.getStepZ()) == 0 && woodlandMansionPiecesSimpleGrid0.get($$7 + $$6.getStepX() * 2, $$8 + $$6.getStepZ() * 2) == 0) {
                            this.recursiveCorridor(woodlandMansionPiecesSimpleGrid0, int1 + direction3.getStepX() + $$6.getStepX(), int2 + direction3.getStepZ() + $$6.getStepZ(), $$6, int4 - 1);
                            break;
                        }
                    }
                }
                Direction $$9 = direction3.getClockWise();
                Direction $$10 = direction3.getCounterClockWise();
                woodlandMansionPiecesSimpleGrid0.setif(int1 + $$9.getStepX(), int2 + $$9.getStepZ(), 0, 2);
                woodlandMansionPiecesSimpleGrid0.setif(int1 + $$10.getStepX(), int2 + $$10.getStepZ(), 0, 2);
                woodlandMansionPiecesSimpleGrid0.setif(int1 + direction3.getStepX() + $$9.getStepX(), int2 + direction3.getStepZ() + $$9.getStepZ(), 0, 2);
                woodlandMansionPiecesSimpleGrid0.setif(int1 + direction3.getStepX() + $$10.getStepX(), int2 + direction3.getStepZ() + $$10.getStepZ(), 0, 2);
                woodlandMansionPiecesSimpleGrid0.setif(int1 + direction3.getStepX() * 2, int2 + direction3.getStepZ() * 2, 0, 2);
                woodlandMansionPiecesSimpleGrid0.setif(int1 + $$9.getStepX() * 2, int2 + $$9.getStepZ() * 2, 0, 2);
                woodlandMansionPiecesSimpleGrid0.setif(int1 + $$10.getStepX() * 2, int2 + $$10.getStepZ() * 2, 0, 2);
            }
        }

        private boolean cleanEdges(WoodlandMansionPieces.SimpleGrid woodlandMansionPiecesSimpleGrid0) {
            boolean $$1 = false;
            for (int $$2 = 0; $$2 < woodlandMansionPiecesSimpleGrid0.height; $$2++) {
                for (int $$3 = 0; $$3 < woodlandMansionPiecesSimpleGrid0.width; $$3++) {
                    if (woodlandMansionPiecesSimpleGrid0.get($$3, $$2) == 0) {
                        int $$4 = 0;
                        $$4 += isHouse(woodlandMansionPiecesSimpleGrid0, $$3 + 1, $$2) ? 1 : 0;
                        $$4 += isHouse(woodlandMansionPiecesSimpleGrid0, $$3 - 1, $$2) ? 1 : 0;
                        $$4 += isHouse(woodlandMansionPiecesSimpleGrid0, $$3, $$2 + 1) ? 1 : 0;
                        $$4 += isHouse(woodlandMansionPiecesSimpleGrid0, $$3, $$2 - 1) ? 1 : 0;
                        if ($$4 >= 3) {
                            woodlandMansionPiecesSimpleGrid0.set($$3, $$2, 2);
                            $$1 = true;
                        } else if ($$4 == 2) {
                            int $$5 = 0;
                            $$5 += isHouse(woodlandMansionPiecesSimpleGrid0, $$3 + 1, $$2 + 1) ? 1 : 0;
                            $$5 += isHouse(woodlandMansionPiecesSimpleGrid0, $$3 - 1, $$2 + 1) ? 1 : 0;
                            $$5 += isHouse(woodlandMansionPiecesSimpleGrid0, $$3 + 1, $$2 - 1) ? 1 : 0;
                            $$5 += isHouse(woodlandMansionPiecesSimpleGrid0, $$3 - 1, $$2 - 1) ? 1 : 0;
                            if ($$5 <= 1) {
                                woodlandMansionPiecesSimpleGrid0.set($$3, $$2, 2);
                                $$1 = true;
                            }
                        }
                    }
                }
            }
            return $$1;
        }

        private void setupThirdFloor() {
            List<Tuple<Integer, Integer>> $$0 = Lists.newArrayList();
            WoodlandMansionPieces.SimpleGrid $$1 = this.floorRooms[1];
            for (int $$2 = 0; $$2 < this.thirdFloorGrid.height; $$2++) {
                for (int $$3 = 0; $$3 < this.thirdFloorGrid.width; $$3++) {
                    int $$4 = $$1.get($$3, $$2);
                    int $$5 = $$4 & 983040;
                    if ($$5 == 131072 && ($$4 & 2097152) == 2097152) {
                        $$0.add(new Tuple<>($$3, $$2));
                    }
                }
            }
            if ($$0.isEmpty()) {
                this.thirdFloorGrid.set(0, 0, this.thirdFloorGrid.width, this.thirdFloorGrid.height, 5);
            } else {
                Tuple<Integer, Integer> $$6 = (Tuple<Integer, Integer>) $$0.get(this.random.nextInt($$0.size()));
                int $$7 = $$1.get($$6.getA(), $$6.getB());
                $$1.set($$6.getA(), $$6.getB(), $$7 | 4194304);
                Direction $$8 = this.get1x2RoomDirection(this.baseGrid, $$6.getA(), $$6.getB(), 1, $$7 & 65535);
                int $$9 = $$6.getA() + $$8.getStepX();
                int $$10 = $$6.getB() + $$8.getStepZ();
                for (int $$11 = 0; $$11 < this.thirdFloorGrid.height; $$11++) {
                    for (int $$12 = 0; $$12 < this.thirdFloorGrid.width; $$12++) {
                        if (!isHouse(this.baseGrid, $$12, $$11)) {
                            this.thirdFloorGrid.set($$12, $$11, 5);
                        } else if ($$12 == $$6.getA() && $$11 == $$6.getB()) {
                            this.thirdFloorGrid.set($$12, $$11, 3);
                        } else if ($$12 == $$9 && $$11 == $$10) {
                            this.thirdFloorGrid.set($$12, $$11, 3);
                            this.floorRooms[2].set($$12, $$11, 8388608);
                        }
                    }
                }
                List<Direction> $$13 = Lists.newArrayList();
                for (Direction $$14 : Direction.Plane.HORIZONTAL) {
                    if (this.thirdFloorGrid.get($$9 + $$14.getStepX(), $$10 + $$14.getStepZ()) == 0) {
                        $$13.add($$14);
                    }
                }
                if ($$13.isEmpty()) {
                    this.thirdFloorGrid.set(0, 0, this.thirdFloorGrid.width, this.thirdFloorGrid.height, 5);
                    $$1.set($$6.getA(), $$6.getB(), $$7);
                } else {
                    Direction $$15 = (Direction) $$13.get(this.random.nextInt($$13.size()));
                    this.recursiveCorridor(this.thirdFloorGrid, $$9 + $$15.getStepX(), $$10 + $$15.getStepZ(), $$15, 4);
                    while (this.cleanEdges(this.thirdFloorGrid)) {
                    }
                }
            }
        }

        private void identifyRooms(WoodlandMansionPieces.SimpleGrid woodlandMansionPiecesSimpleGrid0, WoodlandMansionPieces.SimpleGrid woodlandMansionPiecesSimpleGrid1) {
            ObjectArrayList<Tuple<Integer, Integer>> $$2 = new ObjectArrayList();
            for (int $$3 = 0; $$3 < woodlandMansionPiecesSimpleGrid0.height; $$3++) {
                for (int $$4 = 0; $$4 < woodlandMansionPiecesSimpleGrid0.width; $$4++) {
                    if (woodlandMansionPiecesSimpleGrid0.get($$4, $$3) == 2) {
                        $$2.add(new Tuple<>($$4, $$3));
                    }
                }
            }
            Util.shuffle($$2, this.random);
            int $$5 = 10;
            ObjectListIterator var20 = $$2.iterator();
            while (var20.hasNext()) {
                Tuple<Integer, Integer> $$6 = (Tuple<Integer, Integer>) var20.next();
                int $$7 = $$6.getA();
                int $$8 = $$6.getB();
                if (woodlandMansionPiecesSimpleGrid1.get($$7, $$8) == 0) {
                    int $$9 = $$7;
                    int $$10 = $$7;
                    int $$11 = $$8;
                    int $$12 = $$8;
                    int $$13 = 65536;
                    if (woodlandMansionPiecesSimpleGrid1.get($$7 + 1, $$8) == 0 && woodlandMansionPiecesSimpleGrid1.get($$7, $$8 + 1) == 0 && woodlandMansionPiecesSimpleGrid1.get($$7 + 1, $$8 + 1) == 0 && woodlandMansionPiecesSimpleGrid0.get($$7 + 1, $$8) == 2 && woodlandMansionPiecesSimpleGrid0.get($$7, $$8 + 1) == 2 && woodlandMansionPiecesSimpleGrid0.get($$7 + 1, $$8 + 1) == 2) {
                        $$10 = $$7 + 1;
                        $$12 = $$8 + 1;
                        $$13 = 262144;
                    } else if (woodlandMansionPiecesSimpleGrid1.get($$7 - 1, $$8) == 0 && woodlandMansionPiecesSimpleGrid1.get($$7, $$8 + 1) == 0 && woodlandMansionPiecesSimpleGrid1.get($$7 - 1, $$8 + 1) == 0 && woodlandMansionPiecesSimpleGrid0.get($$7 - 1, $$8) == 2 && woodlandMansionPiecesSimpleGrid0.get($$7, $$8 + 1) == 2 && woodlandMansionPiecesSimpleGrid0.get($$7 - 1, $$8 + 1) == 2) {
                        $$9 = $$7 - 1;
                        $$12 = $$8 + 1;
                        $$13 = 262144;
                    } else if (woodlandMansionPiecesSimpleGrid1.get($$7 - 1, $$8) == 0 && woodlandMansionPiecesSimpleGrid1.get($$7, $$8 - 1) == 0 && woodlandMansionPiecesSimpleGrid1.get($$7 - 1, $$8 - 1) == 0 && woodlandMansionPiecesSimpleGrid0.get($$7 - 1, $$8) == 2 && woodlandMansionPiecesSimpleGrid0.get($$7, $$8 - 1) == 2 && woodlandMansionPiecesSimpleGrid0.get($$7 - 1, $$8 - 1) == 2) {
                        $$9 = $$7 - 1;
                        $$11 = $$8 - 1;
                        $$13 = 262144;
                    } else if (woodlandMansionPiecesSimpleGrid1.get($$7 + 1, $$8) == 0 && woodlandMansionPiecesSimpleGrid0.get($$7 + 1, $$8) == 2) {
                        $$10 = $$7 + 1;
                        $$13 = 131072;
                    } else if (woodlandMansionPiecesSimpleGrid1.get($$7, $$8 + 1) == 0 && woodlandMansionPiecesSimpleGrid0.get($$7, $$8 + 1) == 2) {
                        $$12 = $$8 + 1;
                        $$13 = 131072;
                    } else if (woodlandMansionPiecesSimpleGrid1.get($$7 - 1, $$8) == 0 && woodlandMansionPiecesSimpleGrid0.get($$7 - 1, $$8) == 2) {
                        $$9 = $$7 - 1;
                        $$13 = 131072;
                    } else if (woodlandMansionPiecesSimpleGrid1.get($$7, $$8 - 1) == 0 && woodlandMansionPiecesSimpleGrid0.get($$7, $$8 - 1) == 2) {
                        $$11 = $$8 - 1;
                        $$13 = 131072;
                    }
                    int $$14 = this.random.nextBoolean() ? $$9 : $$10;
                    int $$15 = this.random.nextBoolean() ? $$11 : $$12;
                    int $$16 = 2097152;
                    if (!woodlandMansionPiecesSimpleGrid0.edgesTo($$14, $$15, 1)) {
                        $$14 = $$14 == $$9 ? $$10 : $$9;
                        $$15 = $$15 == $$11 ? $$12 : $$11;
                        if (!woodlandMansionPiecesSimpleGrid0.edgesTo($$14, $$15, 1)) {
                            $$15 = $$15 == $$11 ? $$12 : $$11;
                            if (!woodlandMansionPiecesSimpleGrid0.edgesTo($$14, $$15, 1)) {
                                $$14 = $$14 == $$9 ? $$10 : $$9;
                                $$15 = $$15 == $$11 ? $$12 : $$11;
                                if (!woodlandMansionPiecesSimpleGrid0.edgesTo($$14, $$15, 1)) {
                                    $$16 = 0;
                                    $$14 = $$9;
                                    $$15 = $$11;
                                }
                            }
                        }
                    }
                    for (int $$17 = $$11; $$17 <= $$12; $$17++) {
                        for (int $$18 = $$9; $$18 <= $$10; $$18++) {
                            if ($$18 == $$14 && $$17 == $$15) {
                                woodlandMansionPiecesSimpleGrid1.set($$18, $$17, 1048576 | $$16 | $$13 | $$5);
                            } else {
                                woodlandMansionPiecesSimpleGrid1.set($$18, $$17, $$13 | $$5);
                            }
                        }
                    }
                    $$5++;
                }
            }
        }

        public void print() {
            for (int $$0 = 0; $$0 < 2; $$0++) {
                WoodlandMansionPieces.SimpleGrid $$1 = $$0 == 0 ? this.baseGrid : this.thirdFloorGrid;
                for (int $$2 = 0; $$2 < $$1.height; $$2++) {
                    for (int $$3 = 0; $$3 < $$1.width; $$3++) {
                        int $$4 = $$1.get($$3, $$2);
                        if ($$4 == 1) {
                            System.out.print("+");
                        } else if ($$4 == 4) {
                            System.out.print("x");
                        } else if ($$4 == 2) {
                            System.out.print("X");
                        } else if ($$4 == 3) {
                            System.out.print("O");
                        } else if ($$4 == 5) {
                            System.out.print("#");
                        } else {
                            System.out.print(" ");
                        }
                    }
                    System.out.println("");
                }
                System.out.println("");
            }
        }
    }

    static class MansionPiecePlacer {

        private final StructureTemplateManager structureTemplateManager;

        private final RandomSource random;

        private int startX;

        private int startY;

        public MansionPiecePlacer(StructureTemplateManager structureTemplateManager0, RandomSource randomSource1) {
            this.structureTemplateManager = structureTemplateManager0;
            this.random = randomSource1;
        }

        public void createMansion(BlockPos blockPos0, Rotation rotation1, List<WoodlandMansionPieces.WoodlandMansionPiece> listWoodlandMansionPiecesWoodlandMansionPiece2, WoodlandMansionPieces.MansionGrid woodlandMansionPiecesMansionGrid3) {
            WoodlandMansionPieces.PlacementData $$4 = new WoodlandMansionPieces.PlacementData();
            $$4.position = blockPos0;
            $$4.rotation = rotation1;
            $$4.wallType = "wall_flat";
            WoodlandMansionPieces.PlacementData $$5 = new WoodlandMansionPieces.PlacementData();
            this.entrance(listWoodlandMansionPiecesWoodlandMansionPiece2, $$4);
            $$5.position = $$4.position.above(8);
            $$5.rotation = $$4.rotation;
            $$5.wallType = "wall_window";
            if (!listWoodlandMansionPiecesWoodlandMansionPiece2.isEmpty()) {
            }
            WoodlandMansionPieces.SimpleGrid $$6 = woodlandMansionPiecesMansionGrid3.baseGrid;
            WoodlandMansionPieces.SimpleGrid $$7 = woodlandMansionPiecesMansionGrid3.thirdFloorGrid;
            this.startX = woodlandMansionPiecesMansionGrid3.entranceX + 1;
            this.startY = woodlandMansionPiecesMansionGrid3.entranceY + 1;
            int $$8 = woodlandMansionPiecesMansionGrid3.entranceX + 1;
            int $$9 = woodlandMansionPiecesMansionGrid3.entranceY;
            this.traverseOuterWalls(listWoodlandMansionPiecesWoodlandMansionPiece2, $$4, $$6, Direction.SOUTH, this.startX, this.startY, $$8, $$9);
            this.traverseOuterWalls(listWoodlandMansionPiecesWoodlandMansionPiece2, $$5, $$6, Direction.SOUTH, this.startX, this.startY, $$8, $$9);
            WoodlandMansionPieces.PlacementData $$10 = new WoodlandMansionPieces.PlacementData();
            $$10.position = $$4.position.above(19);
            $$10.rotation = $$4.rotation;
            $$10.wallType = "wall_window";
            boolean $$11 = false;
            for (int $$12 = 0; $$12 < $$7.height && !$$11; $$12++) {
                for (int $$13 = $$7.width - 1; $$13 >= 0 && !$$11; $$13--) {
                    if (WoodlandMansionPieces.MansionGrid.isHouse($$7, $$13, $$12)) {
                        $$10.position = $$10.position.relative(rotation1.rotate(Direction.SOUTH), 8 + ($$12 - this.startY) * 8);
                        $$10.position = $$10.position.relative(rotation1.rotate(Direction.EAST), ($$13 - this.startX) * 8);
                        this.traverseWallPiece(listWoodlandMansionPiecesWoodlandMansionPiece2, $$10);
                        this.traverseOuterWalls(listWoodlandMansionPiecesWoodlandMansionPiece2, $$10, $$7, Direction.SOUTH, $$13, $$12, $$13, $$12);
                        $$11 = true;
                    }
                }
            }
            this.createRoof(listWoodlandMansionPiecesWoodlandMansionPiece2, blockPos0.above(16), rotation1, $$6, $$7);
            this.createRoof(listWoodlandMansionPiecesWoodlandMansionPiece2, blockPos0.above(27), rotation1, $$7, null);
            if (!listWoodlandMansionPiecesWoodlandMansionPiece2.isEmpty()) {
            }
            WoodlandMansionPieces.FloorRoomCollection[] $$14 = new WoodlandMansionPieces.FloorRoomCollection[] { new WoodlandMansionPieces.FirstFloorRoomCollection(), new WoodlandMansionPieces.SecondFloorRoomCollection(), new WoodlandMansionPieces.ThirdFloorRoomCollection() };
            for (int $$15 = 0; $$15 < 3; $$15++) {
                BlockPos $$16 = blockPos0.above(8 * $$15 + ($$15 == 2 ? 3 : 0));
                WoodlandMansionPieces.SimpleGrid $$17 = woodlandMansionPiecesMansionGrid3.floorRooms[$$15];
                WoodlandMansionPieces.SimpleGrid $$18 = $$15 == 2 ? $$7 : $$6;
                String $$19 = $$15 == 0 ? "carpet_south_1" : "carpet_south_2";
                String $$20 = $$15 == 0 ? "carpet_west_1" : "carpet_west_2";
                for (int $$21 = 0; $$21 < $$18.height; $$21++) {
                    for (int $$22 = 0; $$22 < $$18.width; $$22++) {
                        if ($$18.get($$22, $$21) == 1) {
                            BlockPos $$23 = $$16.relative(rotation1.rotate(Direction.SOUTH), 8 + ($$21 - this.startY) * 8);
                            $$23 = $$23.relative(rotation1.rotate(Direction.EAST), ($$22 - this.startX) * 8);
                            listWoodlandMansionPiecesWoodlandMansionPiece2.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "corridor_floor", $$23, rotation1));
                            if ($$18.get($$22, $$21 - 1) == 1 || ($$17.get($$22, $$21 - 1) & 8388608) == 8388608) {
                                listWoodlandMansionPiecesWoodlandMansionPiece2.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "carpet_north", $$23.relative(rotation1.rotate(Direction.EAST), 1).above(), rotation1));
                            }
                            if ($$18.get($$22 + 1, $$21) == 1 || ($$17.get($$22 + 1, $$21) & 8388608) == 8388608) {
                                listWoodlandMansionPiecesWoodlandMansionPiece2.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "carpet_east", $$23.relative(rotation1.rotate(Direction.SOUTH), 1).relative(rotation1.rotate(Direction.EAST), 5).above(), rotation1));
                            }
                            if ($$18.get($$22, $$21 + 1) == 1 || ($$17.get($$22, $$21 + 1) & 8388608) == 8388608) {
                                listWoodlandMansionPiecesWoodlandMansionPiece2.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, $$19, $$23.relative(rotation1.rotate(Direction.SOUTH), 5).relative(rotation1.rotate(Direction.WEST), 1), rotation1));
                            }
                            if ($$18.get($$22 - 1, $$21) == 1 || ($$17.get($$22 - 1, $$21) & 8388608) == 8388608) {
                                listWoodlandMansionPiecesWoodlandMansionPiece2.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, $$20, $$23.relative(rotation1.rotate(Direction.WEST), 1).relative(rotation1.rotate(Direction.NORTH), 1), rotation1));
                            }
                        }
                    }
                }
                String $$24 = $$15 == 0 ? "indoors_wall_1" : "indoors_wall_2";
                String $$25 = $$15 == 0 ? "indoors_door_1" : "indoors_door_2";
                List<Direction> $$26 = Lists.newArrayList();
                for (int $$27 = 0; $$27 < $$18.height; $$27++) {
                    for (int $$28 = 0; $$28 < $$18.width; $$28++) {
                        boolean $$29 = $$15 == 2 && $$18.get($$28, $$27) == 3;
                        if ($$18.get($$28, $$27) == 2 || $$29) {
                            int $$30 = $$17.get($$28, $$27);
                            int $$31 = $$30 & 983040;
                            int $$32 = $$30 & 65535;
                            $$29 = $$29 && ($$30 & 8388608) == 8388608;
                            $$26.clear();
                            if (($$30 & 2097152) == 2097152) {
                                for (Direction $$33 : Direction.Plane.HORIZONTAL) {
                                    if ($$18.get($$28 + $$33.getStepX(), $$27 + $$33.getStepZ()) == 1) {
                                        $$26.add($$33);
                                    }
                                }
                            }
                            Direction $$34 = null;
                            if (!$$26.isEmpty()) {
                                $$34 = (Direction) $$26.get(this.random.nextInt($$26.size()));
                            } else if (($$30 & 1048576) == 1048576) {
                                $$34 = Direction.UP;
                            }
                            BlockPos $$35 = $$16.relative(rotation1.rotate(Direction.SOUTH), 8 + ($$27 - this.startY) * 8);
                            $$35 = $$35.relative(rotation1.rotate(Direction.EAST), -1 + ($$28 - this.startX) * 8);
                            if (WoodlandMansionPieces.MansionGrid.isHouse($$18, $$28 - 1, $$27) && !woodlandMansionPiecesMansionGrid3.isRoomId($$18, $$28 - 1, $$27, $$15, $$32)) {
                                listWoodlandMansionPiecesWoodlandMansionPiece2.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, $$34 == Direction.WEST ? $$25 : $$24, $$35, rotation1));
                            }
                            if ($$18.get($$28 + 1, $$27) == 1 && !$$29) {
                                BlockPos $$36 = $$35.relative(rotation1.rotate(Direction.EAST), 8);
                                listWoodlandMansionPiecesWoodlandMansionPiece2.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, $$34 == Direction.EAST ? $$25 : $$24, $$36, rotation1));
                            }
                            if (WoodlandMansionPieces.MansionGrid.isHouse($$18, $$28, $$27 + 1) && !woodlandMansionPiecesMansionGrid3.isRoomId($$18, $$28, $$27 + 1, $$15, $$32)) {
                                BlockPos $$37 = $$35.relative(rotation1.rotate(Direction.SOUTH), 7);
                                $$37 = $$37.relative(rotation1.rotate(Direction.EAST), 7);
                                listWoodlandMansionPiecesWoodlandMansionPiece2.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, $$34 == Direction.SOUTH ? $$25 : $$24, $$37, rotation1.getRotated(Rotation.CLOCKWISE_90)));
                            }
                            if ($$18.get($$28, $$27 - 1) == 1 && !$$29) {
                                BlockPos $$38 = $$35.relative(rotation1.rotate(Direction.NORTH), 1);
                                $$38 = $$38.relative(rotation1.rotate(Direction.EAST), 7);
                                listWoodlandMansionPiecesWoodlandMansionPiece2.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, $$34 == Direction.NORTH ? $$25 : $$24, $$38, rotation1.getRotated(Rotation.CLOCKWISE_90)));
                            }
                            if ($$31 == 65536) {
                                this.addRoom1x1(listWoodlandMansionPiecesWoodlandMansionPiece2, $$35, rotation1, $$34, $$14[$$15]);
                            } else if ($$31 == 131072 && $$34 != null) {
                                Direction $$39 = woodlandMansionPiecesMansionGrid3.get1x2RoomDirection($$18, $$28, $$27, $$15, $$32);
                                boolean $$40 = ($$30 & 4194304) == 4194304;
                                this.addRoom1x2(listWoodlandMansionPiecesWoodlandMansionPiece2, $$35, rotation1, $$39, $$34, $$14[$$15], $$40);
                            } else if ($$31 == 262144 && $$34 != null && $$34 != Direction.UP) {
                                Direction $$41 = $$34.getClockWise();
                                if (!woodlandMansionPiecesMansionGrid3.isRoomId($$18, $$28 + $$41.getStepX(), $$27 + $$41.getStepZ(), $$15, $$32)) {
                                    $$41 = $$41.getOpposite();
                                }
                                this.addRoom2x2(listWoodlandMansionPiecesWoodlandMansionPiece2, $$35, rotation1, $$41, $$34, $$14[$$15]);
                            } else if ($$31 == 262144 && $$34 == Direction.UP) {
                                this.addRoom2x2Secret(listWoodlandMansionPiecesWoodlandMansionPiece2, $$35, rotation1, $$14[$$15]);
                            }
                        }
                    }
                }
            }
        }

        private void traverseOuterWalls(List<WoodlandMansionPieces.WoodlandMansionPiece> listWoodlandMansionPiecesWoodlandMansionPiece0, WoodlandMansionPieces.PlacementData woodlandMansionPiecesPlacementData1, WoodlandMansionPieces.SimpleGrid woodlandMansionPiecesSimpleGrid2, Direction direction3, int int4, int int5, int int6, int int7) {
            int $$8 = int4;
            int $$9 = int5;
            Direction $$10 = direction3;
            do {
                if (!WoodlandMansionPieces.MansionGrid.isHouse(woodlandMansionPiecesSimpleGrid2, $$8 + direction3.getStepX(), $$9 + direction3.getStepZ())) {
                    this.traverseTurn(listWoodlandMansionPiecesWoodlandMansionPiece0, woodlandMansionPiecesPlacementData1);
                    direction3 = direction3.getClockWise();
                    if ($$8 != int6 || $$9 != int7 || $$10 != direction3) {
                        this.traverseWallPiece(listWoodlandMansionPiecesWoodlandMansionPiece0, woodlandMansionPiecesPlacementData1);
                    }
                } else if (WoodlandMansionPieces.MansionGrid.isHouse(woodlandMansionPiecesSimpleGrid2, $$8 + direction3.getStepX(), $$9 + direction3.getStepZ()) && WoodlandMansionPieces.MansionGrid.isHouse(woodlandMansionPiecesSimpleGrid2, $$8 + direction3.getStepX() + direction3.getCounterClockWise().getStepX(), $$9 + direction3.getStepZ() + direction3.getCounterClockWise().getStepZ())) {
                    this.traverseInnerTurn(listWoodlandMansionPiecesWoodlandMansionPiece0, woodlandMansionPiecesPlacementData1);
                    $$8 += direction3.getStepX();
                    $$9 += direction3.getStepZ();
                    direction3 = direction3.getCounterClockWise();
                } else {
                    $$8 += direction3.getStepX();
                    $$9 += direction3.getStepZ();
                    if ($$8 != int6 || $$9 != int7 || $$10 != direction3) {
                        this.traverseWallPiece(listWoodlandMansionPiecesWoodlandMansionPiece0, woodlandMansionPiecesPlacementData1);
                    }
                }
            } while ($$8 != int6 || $$9 != int7 || $$10 != direction3);
        }

        private void createRoof(List<WoodlandMansionPieces.WoodlandMansionPiece> listWoodlandMansionPiecesWoodlandMansionPiece0, BlockPos blockPos1, Rotation rotation2, WoodlandMansionPieces.SimpleGrid woodlandMansionPiecesSimpleGrid3, @Nullable WoodlandMansionPieces.SimpleGrid woodlandMansionPiecesSimpleGrid4) {
            for (int $$5 = 0; $$5 < woodlandMansionPiecesSimpleGrid3.height; $$5++) {
                for (int $$6 = 0; $$6 < woodlandMansionPiecesSimpleGrid3.width; $$6++) {
                    BlockPos $$27 = blockPos1.relative(rotation2.rotate(Direction.SOUTH), 8 + ($$5 - this.startY) * 8);
                    $$27 = $$27.relative(rotation2.rotate(Direction.EAST), ($$6 - this.startX) * 8);
                    boolean $$8 = woodlandMansionPiecesSimpleGrid4 != null && WoodlandMansionPieces.MansionGrid.isHouse(woodlandMansionPiecesSimpleGrid4, $$6, $$5);
                    if (WoodlandMansionPieces.MansionGrid.isHouse(woodlandMansionPiecesSimpleGrid3, $$6, $$5) && !$$8) {
                        listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof", $$27.above(3), rotation2));
                        if (!WoodlandMansionPieces.MansionGrid.isHouse(woodlandMansionPiecesSimpleGrid3, $$6 + 1, $$5)) {
                            BlockPos $$9 = $$27.relative(rotation2.rotate(Direction.EAST), 6);
                            listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_front", $$9, rotation2));
                        }
                        if (!WoodlandMansionPieces.MansionGrid.isHouse(woodlandMansionPiecesSimpleGrid3, $$6 - 1, $$5)) {
                            BlockPos $$10 = $$27.relative(rotation2.rotate(Direction.EAST), 0);
                            $$10 = $$10.relative(rotation2.rotate(Direction.SOUTH), 7);
                            listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_front", $$10, rotation2.getRotated(Rotation.CLOCKWISE_180)));
                        }
                        if (!WoodlandMansionPieces.MansionGrid.isHouse(woodlandMansionPiecesSimpleGrid3, $$6, $$5 - 1)) {
                            BlockPos $$11 = $$27.relative(rotation2.rotate(Direction.WEST), 1);
                            listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_front", $$11, rotation2.getRotated(Rotation.COUNTERCLOCKWISE_90)));
                        }
                        if (!WoodlandMansionPieces.MansionGrid.isHouse(woodlandMansionPiecesSimpleGrid3, $$6, $$5 + 1)) {
                            BlockPos $$12 = $$27.relative(rotation2.rotate(Direction.EAST), 6);
                            $$12 = $$12.relative(rotation2.rotate(Direction.SOUTH), 6);
                            listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_front", $$12, rotation2.getRotated(Rotation.CLOCKWISE_90)));
                        }
                    }
                }
            }
            if (woodlandMansionPiecesSimpleGrid4 != null) {
                for (int $$13 = 0; $$13 < woodlandMansionPiecesSimpleGrid3.height; $$13++) {
                    for (int $$14 = 0; $$14 < woodlandMansionPiecesSimpleGrid3.width; $$14++) {
                        BlockPos var17 = blockPos1.relative(rotation2.rotate(Direction.SOUTH), 8 + ($$13 - this.startY) * 8);
                        var17 = var17.relative(rotation2.rotate(Direction.EAST), ($$14 - this.startX) * 8);
                        boolean $$16 = WoodlandMansionPieces.MansionGrid.isHouse(woodlandMansionPiecesSimpleGrid4, $$14, $$13);
                        if (WoodlandMansionPieces.MansionGrid.isHouse(woodlandMansionPiecesSimpleGrid3, $$14, $$13) && $$16) {
                            if (!WoodlandMansionPieces.MansionGrid.isHouse(woodlandMansionPiecesSimpleGrid3, $$14 + 1, $$13)) {
                                BlockPos $$17 = var17.relative(rotation2.rotate(Direction.EAST), 7);
                                listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall", $$17, rotation2));
                            }
                            if (!WoodlandMansionPieces.MansionGrid.isHouse(woodlandMansionPiecesSimpleGrid3, $$14 - 1, $$13)) {
                                BlockPos $$18 = var17.relative(rotation2.rotate(Direction.WEST), 1);
                                $$18 = $$18.relative(rotation2.rotate(Direction.SOUTH), 6);
                                listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall", $$18, rotation2.getRotated(Rotation.CLOCKWISE_180)));
                            }
                            if (!WoodlandMansionPieces.MansionGrid.isHouse(woodlandMansionPiecesSimpleGrid3, $$14, $$13 - 1)) {
                                BlockPos $$19 = var17.relative(rotation2.rotate(Direction.WEST), 0);
                                $$19 = $$19.relative(rotation2.rotate(Direction.NORTH), 1);
                                listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall", $$19, rotation2.getRotated(Rotation.COUNTERCLOCKWISE_90)));
                            }
                            if (!WoodlandMansionPieces.MansionGrid.isHouse(woodlandMansionPiecesSimpleGrid3, $$14, $$13 + 1)) {
                                BlockPos $$20 = var17.relative(rotation2.rotate(Direction.EAST), 6);
                                $$20 = $$20.relative(rotation2.rotate(Direction.SOUTH), 7);
                                listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall", $$20, rotation2.getRotated(Rotation.CLOCKWISE_90)));
                            }
                            if (!WoodlandMansionPieces.MansionGrid.isHouse(woodlandMansionPiecesSimpleGrid3, $$14 + 1, $$13)) {
                                if (!WoodlandMansionPieces.MansionGrid.isHouse(woodlandMansionPiecesSimpleGrid3, $$14, $$13 - 1)) {
                                    BlockPos $$21 = var17.relative(rotation2.rotate(Direction.EAST), 7);
                                    $$21 = $$21.relative(rotation2.rotate(Direction.NORTH), 2);
                                    listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall_corner", $$21, rotation2));
                                }
                                if (!WoodlandMansionPieces.MansionGrid.isHouse(woodlandMansionPiecesSimpleGrid3, $$14, $$13 + 1)) {
                                    BlockPos $$22 = var17.relative(rotation2.rotate(Direction.EAST), 8);
                                    $$22 = $$22.relative(rotation2.rotate(Direction.SOUTH), 7);
                                    listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall_corner", $$22, rotation2.getRotated(Rotation.CLOCKWISE_90)));
                                }
                            }
                            if (!WoodlandMansionPieces.MansionGrid.isHouse(woodlandMansionPiecesSimpleGrid3, $$14 - 1, $$13)) {
                                if (!WoodlandMansionPieces.MansionGrid.isHouse(woodlandMansionPiecesSimpleGrid3, $$14, $$13 - 1)) {
                                    BlockPos $$23 = var17.relative(rotation2.rotate(Direction.WEST), 2);
                                    $$23 = $$23.relative(rotation2.rotate(Direction.NORTH), 1);
                                    listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall_corner", $$23, rotation2.getRotated(Rotation.COUNTERCLOCKWISE_90)));
                                }
                                if (!WoodlandMansionPieces.MansionGrid.isHouse(woodlandMansionPiecesSimpleGrid3, $$14, $$13 + 1)) {
                                    BlockPos $$24 = var17.relative(rotation2.rotate(Direction.WEST), 1);
                                    $$24 = $$24.relative(rotation2.rotate(Direction.SOUTH), 8);
                                    listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall_corner", $$24, rotation2.getRotated(Rotation.CLOCKWISE_180)));
                                }
                            }
                        }
                    }
                }
            }
            for (int $$25 = 0; $$25 < woodlandMansionPiecesSimpleGrid3.height; $$25++) {
                for (int $$26 = 0; $$26 < woodlandMansionPiecesSimpleGrid3.width; $$26++) {
                    BlockPos var19 = blockPos1.relative(rotation2.rotate(Direction.SOUTH), 8 + ($$25 - this.startY) * 8);
                    var19 = var19.relative(rotation2.rotate(Direction.EAST), ($$26 - this.startX) * 8);
                    boolean $$28 = woodlandMansionPiecesSimpleGrid4 != null && WoodlandMansionPieces.MansionGrid.isHouse(woodlandMansionPiecesSimpleGrid4, $$26, $$25);
                    if (WoodlandMansionPieces.MansionGrid.isHouse(woodlandMansionPiecesSimpleGrid3, $$26, $$25) && !$$28) {
                        if (!WoodlandMansionPieces.MansionGrid.isHouse(woodlandMansionPiecesSimpleGrid3, $$26 + 1, $$25)) {
                            BlockPos $$29 = var19.relative(rotation2.rotate(Direction.EAST), 6);
                            if (!WoodlandMansionPieces.MansionGrid.isHouse(woodlandMansionPiecesSimpleGrid3, $$26, $$25 + 1)) {
                                BlockPos $$30 = $$29.relative(rotation2.rotate(Direction.SOUTH), 6);
                                listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_corner", $$30, rotation2));
                            } else if (WoodlandMansionPieces.MansionGrid.isHouse(woodlandMansionPiecesSimpleGrid3, $$26 + 1, $$25 + 1)) {
                                BlockPos $$31 = $$29.relative(rotation2.rotate(Direction.SOUTH), 5);
                                listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_inner_corner", $$31, rotation2));
                            }
                            if (!WoodlandMansionPieces.MansionGrid.isHouse(woodlandMansionPiecesSimpleGrid3, $$26, $$25 - 1)) {
                                listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_corner", $$29, rotation2.getRotated(Rotation.COUNTERCLOCKWISE_90)));
                            } else if (WoodlandMansionPieces.MansionGrid.isHouse(woodlandMansionPiecesSimpleGrid3, $$26 + 1, $$25 - 1)) {
                                BlockPos $$32 = var19.relative(rotation2.rotate(Direction.EAST), 9);
                                $$32 = $$32.relative(rotation2.rotate(Direction.NORTH), 2);
                                listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_inner_corner", $$32, rotation2.getRotated(Rotation.CLOCKWISE_90)));
                            }
                        }
                        if (!WoodlandMansionPieces.MansionGrid.isHouse(woodlandMansionPiecesSimpleGrid3, $$26 - 1, $$25)) {
                            BlockPos $$33 = var19.relative(rotation2.rotate(Direction.EAST), 0);
                            $$33 = $$33.relative(rotation2.rotate(Direction.SOUTH), 0);
                            if (!WoodlandMansionPieces.MansionGrid.isHouse(woodlandMansionPiecesSimpleGrid3, $$26, $$25 + 1)) {
                                BlockPos $$34 = $$33.relative(rotation2.rotate(Direction.SOUTH), 6);
                                listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_corner", $$34, rotation2.getRotated(Rotation.CLOCKWISE_90)));
                            } else if (WoodlandMansionPieces.MansionGrid.isHouse(woodlandMansionPiecesSimpleGrid3, $$26 - 1, $$25 + 1)) {
                                BlockPos $$35 = $$33.relative(rotation2.rotate(Direction.SOUTH), 8);
                                $$35 = $$35.relative(rotation2.rotate(Direction.WEST), 3);
                                listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_inner_corner", $$35, rotation2.getRotated(Rotation.COUNTERCLOCKWISE_90)));
                            }
                            if (!WoodlandMansionPieces.MansionGrid.isHouse(woodlandMansionPiecesSimpleGrid3, $$26, $$25 - 1)) {
                                listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_corner", $$33, rotation2.getRotated(Rotation.CLOCKWISE_180)));
                            } else if (WoodlandMansionPieces.MansionGrid.isHouse(woodlandMansionPiecesSimpleGrid3, $$26 - 1, $$25 - 1)) {
                                BlockPos $$36 = $$33.relative(rotation2.rotate(Direction.SOUTH), 1);
                                listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_inner_corner", $$36, rotation2.getRotated(Rotation.CLOCKWISE_180)));
                            }
                        }
                    }
                }
            }
        }

        private void entrance(List<WoodlandMansionPieces.WoodlandMansionPiece> listWoodlandMansionPiecesWoodlandMansionPiece0, WoodlandMansionPieces.PlacementData woodlandMansionPiecesPlacementData1) {
            Direction $$2 = woodlandMansionPiecesPlacementData1.rotation.rotate(Direction.WEST);
            listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "entrance", woodlandMansionPiecesPlacementData1.position.relative($$2, 9), woodlandMansionPiecesPlacementData1.rotation));
            woodlandMansionPiecesPlacementData1.position = woodlandMansionPiecesPlacementData1.position.relative(woodlandMansionPiecesPlacementData1.rotation.rotate(Direction.SOUTH), 16);
        }

        private void traverseWallPiece(List<WoodlandMansionPieces.WoodlandMansionPiece> listWoodlandMansionPiecesWoodlandMansionPiece0, WoodlandMansionPieces.PlacementData woodlandMansionPiecesPlacementData1) {
            listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, woodlandMansionPiecesPlacementData1.wallType, woodlandMansionPiecesPlacementData1.position.relative(woodlandMansionPiecesPlacementData1.rotation.rotate(Direction.EAST), 7), woodlandMansionPiecesPlacementData1.rotation));
            woodlandMansionPiecesPlacementData1.position = woodlandMansionPiecesPlacementData1.position.relative(woodlandMansionPiecesPlacementData1.rotation.rotate(Direction.SOUTH), 8);
        }

        private void traverseTurn(List<WoodlandMansionPieces.WoodlandMansionPiece> listWoodlandMansionPiecesWoodlandMansionPiece0, WoodlandMansionPieces.PlacementData woodlandMansionPiecesPlacementData1) {
            woodlandMansionPiecesPlacementData1.position = woodlandMansionPiecesPlacementData1.position.relative(woodlandMansionPiecesPlacementData1.rotation.rotate(Direction.SOUTH), -1);
            listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "wall_corner", woodlandMansionPiecesPlacementData1.position, woodlandMansionPiecesPlacementData1.rotation));
            woodlandMansionPiecesPlacementData1.position = woodlandMansionPiecesPlacementData1.position.relative(woodlandMansionPiecesPlacementData1.rotation.rotate(Direction.SOUTH), -7);
            woodlandMansionPiecesPlacementData1.position = woodlandMansionPiecesPlacementData1.position.relative(woodlandMansionPiecesPlacementData1.rotation.rotate(Direction.WEST), -6);
            woodlandMansionPiecesPlacementData1.rotation = woodlandMansionPiecesPlacementData1.rotation.getRotated(Rotation.CLOCKWISE_90);
        }

        private void traverseInnerTurn(List<WoodlandMansionPieces.WoodlandMansionPiece> listWoodlandMansionPiecesWoodlandMansionPiece0, WoodlandMansionPieces.PlacementData woodlandMansionPiecesPlacementData1) {
            woodlandMansionPiecesPlacementData1.position = woodlandMansionPiecesPlacementData1.position.relative(woodlandMansionPiecesPlacementData1.rotation.rotate(Direction.SOUTH), 6);
            woodlandMansionPiecesPlacementData1.position = woodlandMansionPiecesPlacementData1.position.relative(woodlandMansionPiecesPlacementData1.rotation.rotate(Direction.EAST), 8);
            woodlandMansionPiecesPlacementData1.rotation = woodlandMansionPiecesPlacementData1.rotation.getRotated(Rotation.COUNTERCLOCKWISE_90);
        }

        private void addRoom1x1(List<WoodlandMansionPieces.WoodlandMansionPiece> listWoodlandMansionPiecesWoodlandMansionPiece0, BlockPos blockPos1, Rotation rotation2, Direction direction3, WoodlandMansionPieces.FloorRoomCollection woodlandMansionPiecesFloorRoomCollection4) {
            Rotation $$5 = Rotation.NONE;
            String $$6 = woodlandMansionPiecesFloorRoomCollection4.get1x1(this.random);
            if (direction3 != Direction.EAST) {
                if (direction3 == Direction.NORTH) {
                    $$5 = $$5.getRotated(Rotation.COUNTERCLOCKWISE_90);
                } else if (direction3 == Direction.WEST) {
                    $$5 = $$5.getRotated(Rotation.CLOCKWISE_180);
                } else if (direction3 == Direction.SOUTH) {
                    $$5 = $$5.getRotated(Rotation.CLOCKWISE_90);
                } else {
                    $$6 = woodlandMansionPiecesFloorRoomCollection4.get1x1Secret(this.random);
                }
            }
            BlockPos $$7 = StructureTemplate.getZeroPositionWithTransform(new BlockPos(1, 0, 0), Mirror.NONE, $$5, 7, 7);
            $$5 = $$5.getRotated(rotation2);
            $$7 = $$7.rotate(rotation2);
            BlockPos $$8 = blockPos1.offset($$7.m_123341_(), 0, $$7.m_123343_());
            listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, $$6, $$8, $$5));
        }

        private void addRoom1x2(List<WoodlandMansionPieces.WoodlandMansionPiece> listWoodlandMansionPiecesWoodlandMansionPiece0, BlockPos blockPos1, Rotation rotation2, Direction direction3, Direction direction4, WoodlandMansionPieces.FloorRoomCollection woodlandMansionPiecesFloorRoomCollection5, boolean boolean6) {
            if (direction4 == Direction.EAST && direction3 == Direction.SOUTH) {
                BlockPos $$7 = blockPos1.relative(rotation2.rotate(Direction.EAST), 1);
                listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, woodlandMansionPiecesFloorRoomCollection5.get1x2SideEntrance(this.random, boolean6), $$7, rotation2));
            } else if (direction4 == Direction.EAST && direction3 == Direction.NORTH) {
                BlockPos $$8 = blockPos1.relative(rotation2.rotate(Direction.EAST), 1);
                $$8 = $$8.relative(rotation2.rotate(Direction.SOUTH), 6);
                listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, woodlandMansionPiecesFloorRoomCollection5.get1x2SideEntrance(this.random, boolean6), $$8, rotation2, Mirror.LEFT_RIGHT));
            } else if (direction4 == Direction.WEST && direction3 == Direction.NORTH) {
                BlockPos $$9 = blockPos1.relative(rotation2.rotate(Direction.EAST), 7);
                $$9 = $$9.relative(rotation2.rotate(Direction.SOUTH), 6);
                listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, woodlandMansionPiecesFloorRoomCollection5.get1x2SideEntrance(this.random, boolean6), $$9, rotation2.getRotated(Rotation.CLOCKWISE_180)));
            } else if (direction4 == Direction.WEST && direction3 == Direction.SOUTH) {
                BlockPos $$10 = blockPos1.relative(rotation2.rotate(Direction.EAST), 7);
                listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, woodlandMansionPiecesFloorRoomCollection5.get1x2SideEntrance(this.random, boolean6), $$10, rotation2, Mirror.FRONT_BACK));
            } else if (direction4 == Direction.SOUTH && direction3 == Direction.EAST) {
                BlockPos $$11 = blockPos1.relative(rotation2.rotate(Direction.EAST), 1);
                listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, woodlandMansionPiecesFloorRoomCollection5.get1x2SideEntrance(this.random, boolean6), $$11, rotation2.getRotated(Rotation.CLOCKWISE_90), Mirror.LEFT_RIGHT));
            } else if (direction4 == Direction.SOUTH && direction3 == Direction.WEST) {
                BlockPos $$12 = blockPos1.relative(rotation2.rotate(Direction.EAST), 7);
                listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, woodlandMansionPiecesFloorRoomCollection5.get1x2SideEntrance(this.random, boolean6), $$12, rotation2.getRotated(Rotation.CLOCKWISE_90)));
            } else if (direction4 == Direction.NORTH && direction3 == Direction.WEST) {
                BlockPos $$13 = blockPos1.relative(rotation2.rotate(Direction.EAST), 7);
                $$13 = $$13.relative(rotation2.rotate(Direction.SOUTH), 6);
                listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, woodlandMansionPiecesFloorRoomCollection5.get1x2SideEntrance(this.random, boolean6), $$13, rotation2.getRotated(Rotation.CLOCKWISE_90), Mirror.FRONT_BACK));
            } else if (direction4 == Direction.NORTH && direction3 == Direction.EAST) {
                BlockPos $$14 = blockPos1.relative(rotation2.rotate(Direction.EAST), 1);
                $$14 = $$14.relative(rotation2.rotate(Direction.SOUTH), 6);
                listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, woodlandMansionPiecesFloorRoomCollection5.get1x2SideEntrance(this.random, boolean6), $$14, rotation2.getRotated(Rotation.COUNTERCLOCKWISE_90)));
            } else if (direction4 == Direction.SOUTH && direction3 == Direction.NORTH) {
                BlockPos $$15 = blockPos1.relative(rotation2.rotate(Direction.EAST), 1);
                $$15 = $$15.relative(rotation2.rotate(Direction.NORTH), 8);
                listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, woodlandMansionPiecesFloorRoomCollection5.get1x2FrontEntrance(this.random, boolean6), $$15, rotation2));
            } else if (direction4 == Direction.NORTH && direction3 == Direction.SOUTH) {
                BlockPos $$16 = blockPos1.relative(rotation2.rotate(Direction.EAST), 7);
                $$16 = $$16.relative(rotation2.rotate(Direction.SOUTH), 14);
                listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, woodlandMansionPiecesFloorRoomCollection5.get1x2FrontEntrance(this.random, boolean6), $$16, rotation2.getRotated(Rotation.CLOCKWISE_180)));
            } else if (direction4 == Direction.WEST && direction3 == Direction.EAST) {
                BlockPos $$17 = blockPos1.relative(rotation2.rotate(Direction.EAST), 15);
                listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, woodlandMansionPiecesFloorRoomCollection5.get1x2FrontEntrance(this.random, boolean6), $$17, rotation2.getRotated(Rotation.CLOCKWISE_90)));
            } else if (direction4 == Direction.EAST && direction3 == Direction.WEST) {
                BlockPos $$18 = blockPos1.relative(rotation2.rotate(Direction.WEST), 7);
                $$18 = $$18.relative(rotation2.rotate(Direction.SOUTH), 6);
                listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, woodlandMansionPiecesFloorRoomCollection5.get1x2FrontEntrance(this.random, boolean6), $$18, rotation2.getRotated(Rotation.COUNTERCLOCKWISE_90)));
            } else if (direction4 == Direction.UP && direction3 == Direction.EAST) {
                BlockPos $$19 = blockPos1.relative(rotation2.rotate(Direction.EAST), 15);
                listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, woodlandMansionPiecesFloorRoomCollection5.get1x2Secret(this.random), $$19, rotation2.getRotated(Rotation.CLOCKWISE_90)));
            } else if (direction4 == Direction.UP && direction3 == Direction.SOUTH) {
                BlockPos $$20 = blockPos1.relative(rotation2.rotate(Direction.EAST), 1);
                $$20 = $$20.relative(rotation2.rotate(Direction.NORTH), 0);
                listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, woodlandMansionPiecesFloorRoomCollection5.get1x2Secret(this.random), $$20, rotation2));
            }
        }

        private void addRoom2x2(List<WoodlandMansionPieces.WoodlandMansionPiece> listWoodlandMansionPiecesWoodlandMansionPiece0, BlockPos blockPos1, Rotation rotation2, Direction direction3, Direction direction4, WoodlandMansionPieces.FloorRoomCollection woodlandMansionPiecesFloorRoomCollection5) {
            int $$6 = 0;
            int $$7 = 0;
            Rotation $$8 = rotation2;
            Mirror $$9 = Mirror.NONE;
            if (direction4 == Direction.EAST && direction3 == Direction.SOUTH) {
                $$6 = -7;
            } else if (direction4 == Direction.EAST && direction3 == Direction.NORTH) {
                $$6 = -7;
                $$7 = 6;
                $$9 = Mirror.LEFT_RIGHT;
            } else if (direction4 == Direction.NORTH && direction3 == Direction.EAST) {
                $$6 = 1;
                $$7 = 14;
                $$8 = rotation2.getRotated(Rotation.COUNTERCLOCKWISE_90);
            } else if (direction4 == Direction.NORTH && direction3 == Direction.WEST) {
                $$6 = 7;
                $$7 = 14;
                $$8 = rotation2.getRotated(Rotation.COUNTERCLOCKWISE_90);
                $$9 = Mirror.LEFT_RIGHT;
            } else if (direction4 == Direction.SOUTH && direction3 == Direction.WEST) {
                $$6 = 7;
                $$7 = -8;
                $$8 = rotation2.getRotated(Rotation.CLOCKWISE_90);
            } else if (direction4 == Direction.SOUTH && direction3 == Direction.EAST) {
                $$6 = 1;
                $$7 = -8;
                $$8 = rotation2.getRotated(Rotation.CLOCKWISE_90);
                $$9 = Mirror.LEFT_RIGHT;
            } else if (direction4 == Direction.WEST && direction3 == Direction.NORTH) {
                $$6 = 15;
                $$7 = 6;
                $$8 = rotation2.getRotated(Rotation.CLOCKWISE_180);
            } else if (direction4 == Direction.WEST && direction3 == Direction.SOUTH) {
                $$6 = 15;
                $$9 = Mirror.FRONT_BACK;
            }
            BlockPos $$10 = blockPos1.relative(rotation2.rotate(Direction.EAST), $$6);
            $$10 = $$10.relative(rotation2.rotate(Direction.SOUTH), $$7);
            listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, woodlandMansionPiecesFloorRoomCollection5.get2x2(this.random), $$10, $$8, $$9));
        }

        private void addRoom2x2Secret(List<WoodlandMansionPieces.WoodlandMansionPiece> listWoodlandMansionPiecesWoodlandMansionPiece0, BlockPos blockPos1, Rotation rotation2, WoodlandMansionPieces.FloorRoomCollection woodlandMansionPiecesFloorRoomCollection3) {
            BlockPos $$4 = blockPos1.relative(rotation2.rotate(Direction.EAST), 1);
            listWoodlandMansionPiecesWoodlandMansionPiece0.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, woodlandMansionPiecesFloorRoomCollection3.get2x2Secret(this.random), $$4, rotation2, Mirror.NONE));
        }
    }

    static class PlacementData {

        public Rotation rotation;

        public BlockPos position;

        public String wallType;
    }

    static class SecondFloorRoomCollection extends WoodlandMansionPieces.FloorRoomCollection {

        @Override
        public String get1x1(RandomSource randomSource0) {
            return "1x1_b" + (randomSource0.nextInt(4) + 1);
        }

        @Override
        public String get1x1Secret(RandomSource randomSource0) {
            return "1x1_as" + (randomSource0.nextInt(4) + 1);
        }

        @Override
        public String get1x2SideEntrance(RandomSource randomSource0, boolean boolean1) {
            return boolean1 ? "1x2_c_stairs" : "1x2_c" + (randomSource0.nextInt(4) + 1);
        }

        @Override
        public String get1x2FrontEntrance(RandomSource randomSource0, boolean boolean1) {
            return boolean1 ? "1x2_d_stairs" : "1x2_d" + (randomSource0.nextInt(5) + 1);
        }

        @Override
        public String get1x2Secret(RandomSource randomSource0) {
            return "1x2_se" + (randomSource0.nextInt(1) + 1);
        }

        @Override
        public String get2x2(RandomSource randomSource0) {
            return "2x2_b" + (randomSource0.nextInt(5) + 1);
        }

        @Override
        public String get2x2Secret(RandomSource randomSource0) {
            return "2x2_s1";
        }
    }

    static class SimpleGrid {

        private final int[][] grid;

        final int width;

        final int height;

        private final int valueIfOutside;

        public SimpleGrid(int int0, int int1, int int2) {
            this.width = int0;
            this.height = int1;
            this.valueIfOutside = int2;
            this.grid = new int[int0][int1];
        }

        public void set(int int0, int int1, int int2) {
            if (int0 >= 0 && int0 < this.width && int1 >= 0 && int1 < this.height) {
                this.grid[int0][int1] = int2;
            }
        }

        public void set(int int0, int int1, int int2, int int3, int int4) {
            for (int $$5 = int1; $$5 <= int3; $$5++) {
                for (int $$6 = int0; $$6 <= int2; $$6++) {
                    this.set($$6, $$5, int4);
                }
            }
        }

        public int get(int int0, int int1) {
            return int0 >= 0 && int0 < this.width && int1 >= 0 && int1 < this.height ? this.grid[int0][int1] : this.valueIfOutside;
        }

        public void setif(int int0, int int1, int int2, int int3) {
            if (this.get(int0, int1) == int2) {
                this.set(int0, int1, int3);
            }
        }

        public boolean edgesTo(int int0, int int1, int int2) {
            return this.get(int0 - 1, int1) == int2 || this.get(int0 + 1, int1) == int2 || this.get(int0, int1 + 1) == int2 || this.get(int0, int1 - 1) == int2;
        }
    }

    static class ThirdFloorRoomCollection extends WoodlandMansionPieces.SecondFloorRoomCollection {
    }

    public static class WoodlandMansionPiece extends TemplateStructurePiece {

        public WoodlandMansionPiece(StructureTemplateManager structureTemplateManager0, String string1, BlockPos blockPos2, Rotation rotation3) {
            this(structureTemplateManager0, string1, blockPos2, rotation3, Mirror.NONE);
        }

        public WoodlandMansionPiece(StructureTemplateManager structureTemplateManager0, String string1, BlockPos blockPos2, Rotation rotation3, Mirror mirror4) {
            super(StructurePieceType.WOODLAND_MANSION_PIECE, 0, structureTemplateManager0, makeLocation(string1), string1, makeSettings(mirror4, rotation3), blockPos2);
        }

        public WoodlandMansionPiece(StructureTemplateManager structureTemplateManager0, CompoundTag compoundTag1) {
            super(StructurePieceType.WOODLAND_MANSION_PIECE, compoundTag1, structureTemplateManager0, p_230220_ -> makeSettings(Mirror.valueOf(compoundTag1.getString("Mi")), Rotation.valueOf(compoundTag1.getString("Rot"))));
        }

        @Override
        protected ResourceLocation makeTemplateLocation() {
            return makeLocation(this.f_163658_);
        }

        private static ResourceLocation makeLocation(String string0) {
            return new ResourceLocation("woodland_mansion/" + string0);
        }

        private static StructurePlaceSettings makeSettings(Mirror mirror0, Rotation rotation1) {
            return new StructurePlaceSettings().setIgnoreEntities(true).setRotation(rotation1).setMirror(mirror0).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1) {
            super.addAdditionalSaveData(structurePieceSerializationContext0, compoundTag1);
            compoundTag1.putString("Rot", this.f_73657_.getRotation().name());
            compoundTag1.putString("Mi", this.f_73657_.getMirror().name());
        }

        @Override
        protected void handleDataMarker(String string0, BlockPos blockPos1, ServerLevelAccessor serverLevelAccessor2, RandomSource randomSource3, BoundingBox boundingBox4) {
            if (string0.startsWith("Chest")) {
                Rotation $$5 = this.f_73657_.getRotation();
                BlockState $$6 = Blocks.CHEST.defaultBlockState();
                if ("ChestWest".equals(string0)) {
                    $$6 = (BlockState) $$6.m_61124_(ChestBlock.FACING, $$5.rotate(Direction.WEST));
                } else if ("ChestEast".equals(string0)) {
                    $$6 = (BlockState) $$6.m_61124_(ChestBlock.FACING, $$5.rotate(Direction.EAST));
                } else if ("ChestSouth".equals(string0)) {
                    $$6 = (BlockState) $$6.m_61124_(ChestBlock.FACING, $$5.rotate(Direction.SOUTH));
                } else if ("ChestNorth".equals(string0)) {
                    $$6 = (BlockState) $$6.m_61124_(ChestBlock.FACING, $$5.rotate(Direction.NORTH));
                }
                this.m_226762_(serverLevelAccessor2, boundingBox4, randomSource3, blockPos1, BuiltInLootTables.WOODLAND_MANSION, $$6);
            } else {
                List<Mob> $$7 = new ArrayList();
                switch(string0) {
                    case "Mage":
                        $$7.add(EntityType.EVOKER.create(serverLevelAccessor2.getLevel()));
                        break;
                    case "Warrior":
                        $$7.add(EntityType.VINDICATOR.create(serverLevelAccessor2.getLevel()));
                        break;
                    case "Group of Allays":
                        int $$8 = serverLevelAccessor2.m_213780_().nextInt(3) + 1;
                        for (int $$9 = 0; $$9 < $$8; $$9++) {
                            $$7.add(EntityType.ALLAY.create(serverLevelAccessor2.getLevel()));
                        }
                        break;
                    default:
                        return;
                }
                for (Mob $$10 : $$7) {
                    if ($$10 != null) {
                        $$10.setPersistenceRequired();
                        $$10.m_20035_(blockPos1, 0.0F, 0.0F);
                        $$10.finalizeSpawn(serverLevelAccessor2, serverLevelAccessor2.m_6436_($$10.m_20183_()), MobSpawnType.STRUCTURE, null, null);
                        serverLevelAccessor2.addFreshEntityWithPassengers($$10);
                        serverLevelAccessor2.m_7731_(blockPos1, Blocks.AIR.defaultBlockState(), 2);
                    }
                }
            }
        }
    }
}