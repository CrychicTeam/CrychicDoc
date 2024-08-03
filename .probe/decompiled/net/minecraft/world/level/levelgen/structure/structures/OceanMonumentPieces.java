package net.minecraft.world.level.levelgen.structure.structures;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.List;
import java.util.Set;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class OceanMonumentPieces {

    private OceanMonumentPieces() {
    }

    static class FitDoubleXRoom implements OceanMonumentPieces.MonumentRoomFitter {

        @Override
        public boolean fits(OceanMonumentPieces.RoomDefinition oceanMonumentPiecesRoomDefinition0) {
            return oceanMonumentPiecesRoomDefinition0.hasOpening[Direction.EAST.get3DDataValue()] && !oceanMonumentPiecesRoomDefinition0.connections[Direction.EAST.get3DDataValue()].claimed;
        }

        @Override
        public OceanMonumentPieces.OceanMonumentPiece create(Direction direction0, OceanMonumentPieces.RoomDefinition oceanMonumentPiecesRoomDefinition1, RandomSource randomSource2) {
            oceanMonumentPiecesRoomDefinition1.claimed = true;
            oceanMonumentPiecesRoomDefinition1.connections[Direction.EAST.get3DDataValue()].claimed = true;
            return new OceanMonumentPieces.OceanMonumentDoubleXRoom(direction0, oceanMonumentPiecesRoomDefinition1);
        }
    }

    static class FitDoubleXYRoom implements OceanMonumentPieces.MonumentRoomFitter {

        @Override
        public boolean fits(OceanMonumentPieces.RoomDefinition oceanMonumentPiecesRoomDefinition0) {
            if (oceanMonumentPiecesRoomDefinition0.hasOpening[Direction.EAST.get3DDataValue()] && !oceanMonumentPiecesRoomDefinition0.connections[Direction.EAST.get3DDataValue()].claimed && oceanMonumentPiecesRoomDefinition0.hasOpening[Direction.UP.get3DDataValue()] && !oceanMonumentPiecesRoomDefinition0.connections[Direction.UP.get3DDataValue()].claimed) {
                OceanMonumentPieces.RoomDefinition $$1 = oceanMonumentPiecesRoomDefinition0.connections[Direction.EAST.get3DDataValue()];
                return $$1.hasOpening[Direction.UP.get3DDataValue()] && !$$1.connections[Direction.UP.get3DDataValue()].claimed;
            } else {
                return false;
            }
        }

        @Override
        public OceanMonumentPieces.OceanMonumentPiece create(Direction direction0, OceanMonumentPieces.RoomDefinition oceanMonumentPiecesRoomDefinition1, RandomSource randomSource2) {
            oceanMonumentPiecesRoomDefinition1.claimed = true;
            oceanMonumentPiecesRoomDefinition1.connections[Direction.EAST.get3DDataValue()].claimed = true;
            oceanMonumentPiecesRoomDefinition1.connections[Direction.UP.get3DDataValue()].claimed = true;
            oceanMonumentPiecesRoomDefinition1.connections[Direction.EAST.get3DDataValue()].connections[Direction.UP.get3DDataValue()].claimed = true;
            return new OceanMonumentPieces.OceanMonumentDoubleXYRoom(direction0, oceanMonumentPiecesRoomDefinition1);
        }
    }

    static class FitDoubleYRoom implements OceanMonumentPieces.MonumentRoomFitter {

        @Override
        public boolean fits(OceanMonumentPieces.RoomDefinition oceanMonumentPiecesRoomDefinition0) {
            return oceanMonumentPiecesRoomDefinition0.hasOpening[Direction.UP.get3DDataValue()] && !oceanMonumentPiecesRoomDefinition0.connections[Direction.UP.get3DDataValue()].claimed;
        }

        @Override
        public OceanMonumentPieces.OceanMonumentPiece create(Direction direction0, OceanMonumentPieces.RoomDefinition oceanMonumentPiecesRoomDefinition1, RandomSource randomSource2) {
            oceanMonumentPiecesRoomDefinition1.claimed = true;
            oceanMonumentPiecesRoomDefinition1.connections[Direction.UP.get3DDataValue()].claimed = true;
            return new OceanMonumentPieces.OceanMonumentDoubleYRoom(direction0, oceanMonumentPiecesRoomDefinition1);
        }
    }

    static class FitDoubleYZRoom implements OceanMonumentPieces.MonumentRoomFitter {

        @Override
        public boolean fits(OceanMonumentPieces.RoomDefinition oceanMonumentPiecesRoomDefinition0) {
            if (oceanMonumentPiecesRoomDefinition0.hasOpening[Direction.NORTH.get3DDataValue()] && !oceanMonumentPiecesRoomDefinition0.connections[Direction.NORTH.get3DDataValue()].claimed && oceanMonumentPiecesRoomDefinition0.hasOpening[Direction.UP.get3DDataValue()] && !oceanMonumentPiecesRoomDefinition0.connections[Direction.UP.get3DDataValue()].claimed) {
                OceanMonumentPieces.RoomDefinition $$1 = oceanMonumentPiecesRoomDefinition0.connections[Direction.NORTH.get3DDataValue()];
                return $$1.hasOpening[Direction.UP.get3DDataValue()] && !$$1.connections[Direction.UP.get3DDataValue()].claimed;
            } else {
                return false;
            }
        }

        @Override
        public OceanMonumentPieces.OceanMonumentPiece create(Direction direction0, OceanMonumentPieces.RoomDefinition oceanMonumentPiecesRoomDefinition1, RandomSource randomSource2) {
            oceanMonumentPiecesRoomDefinition1.claimed = true;
            oceanMonumentPiecesRoomDefinition1.connections[Direction.NORTH.get3DDataValue()].claimed = true;
            oceanMonumentPiecesRoomDefinition1.connections[Direction.UP.get3DDataValue()].claimed = true;
            oceanMonumentPiecesRoomDefinition1.connections[Direction.NORTH.get3DDataValue()].connections[Direction.UP.get3DDataValue()].claimed = true;
            return new OceanMonumentPieces.OceanMonumentDoubleYZRoom(direction0, oceanMonumentPiecesRoomDefinition1);
        }
    }

    static class FitDoubleZRoom implements OceanMonumentPieces.MonumentRoomFitter {

        @Override
        public boolean fits(OceanMonumentPieces.RoomDefinition oceanMonumentPiecesRoomDefinition0) {
            return oceanMonumentPiecesRoomDefinition0.hasOpening[Direction.NORTH.get3DDataValue()] && !oceanMonumentPiecesRoomDefinition0.connections[Direction.NORTH.get3DDataValue()].claimed;
        }

        @Override
        public OceanMonumentPieces.OceanMonumentPiece create(Direction direction0, OceanMonumentPieces.RoomDefinition oceanMonumentPiecesRoomDefinition1, RandomSource randomSource2) {
            OceanMonumentPieces.RoomDefinition $$3 = oceanMonumentPiecesRoomDefinition1;
            if (!oceanMonumentPiecesRoomDefinition1.hasOpening[Direction.NORTH.get3DDataValue()] || oceanMonumentPiecesRoomDefinition1.connections[Direction.NORTH.get3DDataValue()].claimed) {
                $$3 = oceanMonumentPiecesRoomDefinition1.connections[Direction.SOUTH.get3DDataValue()];
            }
            $$3.claimed = true;
            $$3.connections[Direction.NORTH.get3DDataValue()].claimed = true;
            return new OceanMonumentPieces.OceanMonumentDoubleZRoom(direction0, $$3);
        }
    }

    static class FitSimpleRoom implements OceanMonumentPieces.MonumentRoomFitter {

        @Override
        public boolean fits(OceanMonumentPieces.RoomDefinition oceanMonumentPiecesRoomDefinition0) {
            return true;
        }

        @Override
        public OceanMonumentPieces.OceanMonumentPiece create(Direction direction0, OceanMonumentPieces.RoomDefinition oceanMonumentPiecesRoomDefinition1, RandomSource randomSource2) {
            oceanMonumentPiecesRoomDefinition1.claimed = true;
            return new OceanMonumentPieces.OceanMonumentSimpleRoom(direction0, oceanMonumentPiecesRoomDefinition1, randomSource2);
        }
    }

    static class FitSimpleTopRoom implements OceanMonumentPieces.MonumentRoomFitter {

        @Override
        public boolean fits(OceanMonumentPieces.RoomDefinition oceanMonumentPiecesRoomDefinition0) {
            return !oceanMonumentPiecesRoomDefinition0.hasOpening[Direction.WEST.get3DDataValue()] && !oceanMonumentPiecesRoomDefinition0.hasOpening[Direction.EAST.get3DDataValue()] && !oceanMonumentPiecesRoomDefinition0.hasOpening[Direction.NORTH.get3DDataValue()] && !oceanMonumentPiecesRoomDefinition0.hasOpening[Direction.SOUTH.get3DDataValue()] && !oceanMonumentPiecesRoomDefinition0.hasOpening[Direction.UP.get3DDataValue()];
        }

        @Override
        public OceanMonumentPieces.OceanMonumentPiece create(Direction direction0, OceanMonumentPieces.RoomDefinition oceanMonumentPiecesRoomDefinition1, RandomSource randomSource2) {
            oceanMonumentPiecesRoomDefinition1.claimed = true;
            return new OceanMonumentPieces.OceanMonumentSimpleTopRoom(direction0, oceanMonumentPiecesRoomDefinition1);
        }
    }

    public static class MonumentBuilding extends OceanMonumentPieces.OceanMonumentPiece {

        private static final int WIDTH = 58;

        private static final int HEIGHT = 22;

        private static final int DEPTH = 58;

        public static final int BIOME_RANGE_CHECK = 29;

        private static final int TOP_POSITION = 61;

        private OceanMonumentPieces.RoomDefinition sourceRoom;

        private OceanMonumentPieces.RoomDefinition coreRoom;

        private final List<OceanMonumentPieces.OceanMonumentPiece> childPieces = Lists.newArrayList();

        public MonumentBuilding(RandomSource randomSource0, int int1, int int2, Direction direction3) {
            super(StructurePieceType.OCEAN_MONUMENT_BUILDING, direction3, 0, m_163541_(int1, 39, int2, direction3, 58, 23, 58));
            this.m_73519_(direction3);
            List<OceanMonumentPieces.RoomDefinition> $$4 = this.generateRoomGraph(randomSource0);
            this.sourceRoom.claimed = true;
            this.childPieces.add(new OceanMonumentPieces.OceanMonumentEntryRoom(direction3, this.sourceRoom));
            this.childPieces.add(new OceanMonumentPieces.OceanMonumentCoreRoom(direction3, this.coreRoom));
            List<OceanMonumentPieces.MonumentRoomFitter> $$5 = Lists.newArrayList();
            $$5.add(new OceanMonumentPieces.FitDoubleXYRoom());
            $$5.add(new OceanMonumentPieces.FitDoubleYZRoom());
            $$5.add(new OceanMonumentPieces.FitDoubleZRoom());
            $$5.add(new OceanMonumentPieces.FitDoubleXRoom());
            $$5.add(new OceanMonumentPieces.FitDoubleYRoom());
            $$5.add(new OceanMonumentPieces.FitSimpleTopRoom());
            $$5.add(new OceanMonumentPieces.FitSimpleRoom());
            for (OceanMonumentPieces.RoomDefinition $$6 : $$4) {
                if (!$$6.claimed && !$$6.isSpecial()) {
                    for (OceanMonumentPieces.MonumentRoomFitter $$7 : $$5) {
                        if ($$7.fits($$6)) {
                            this.childPieces.add($$7.create(direction3, $$6, randomSource0));
                            break;
                        }
                    }
                }
            }
            BlockPos $$8 = this.m_163582_(9, 0, 22);
            for (OceanMonumentPieces.OceanMonumentPiece $$9 : this.childPieces) {
                $$9.m_73547_().move($$8);
            }
            BoundingBox $$10 = BoundingBox.fromCorners(this.m_163582_(1, 1, 1), this.m_163582_(23, 8, 21));
            BoundingBox $$11 = BoundingBox.fromCorners(this.m_163582_(34, 1, 1), this.m_163582_(56, 8, 21));
            BoundingBox $$12 = BoundingBox.fromCorners(this.m_163582_(22, 13, 22), this.m_163582_(35, 17, 35));
            int $$13 = randomSource0.nextInt();
            this.childPieces.add(new OceanMonumentPieces.OceanMonumentWingRoom(direction3, $$10, $$13++));
            this.childPieces.add(new OceanMonumentPieces.OceanMonumentWingRoom(direction3, $$11, $$13++));
            this.childPieces.add(new OceanMonumentPieces.OceanMonumentPenthouse(direction3, $$12));
        }

        public MonumentBuilding(CompoundTag compoundTag0) {
            super(StructurePieceType.OCEAN_MONUMENT_BUILDING, compoundTag0);
        }

        private List<OceanMonumentPieces.RoomDefinition> generateRoomGraph(RandomSource randomSource0) {
            OceanMonumentPieces.RoomDefinition[] $$1 = new OceanMonumentPieces.RoomDefinition[75];
            for (int $$2 = 0; $$2 < 5; $$2++) {
                for (int $$3 = 0; $$3 < 4; $$3++) {
                    int $$4 = 0;
                    int $$5 = m_228889_($$2, 0, $$3);
                    $$1[$$5] = new OceanMonumentPieces.RoomDefinition($$5);
                }
            }
            for (int $$6 = 0; $$6 < 5; $$6++) {
                for (int $$7 = 0; $$7 < 4; $$7++) {
                    int $$8 = 1;
                    int $$9 = m_228889_($$6, 1, $$7);
                    $$1[$$9] = new OceanMonumentPieces.RoomDefinition($$9);
                }
            }
            for (int $$10 = 1; $$10 < 4; $$10++) {
                for (int $$11 = 0; $$11 < 2; $$11++) {
                    int $$12 = 2;
                    int $$13 = m_228889_($$10, 2, $$11);
                    $$1[$$13] = new OceanMonumentPieces.RoomDefinition($$13);
                }
            }
            this.sourceRoom = $$1[f_228820_];
            for (int $$14 = 0; $$14 < 5; $$14++) {
                for (int $$15 = 0; $$15 < 5; $$15++) {
                    for (int $$16 = 0; $$16 < 3; $$16++) {
                        int $$17 = m_228889_($$14, $$16, $$15);
                        if ($$1[$$17] != null) {
                            for (Direction $$18 : Direction.values()) {
                                int $$19 = $$14 + $$18.getStepX();
                                int $$20 = $$16 + $$18.getStepY();
                                int $$21 = $$15 + $$18.getStepZ();
                                if ($$19 >= 0 && $$19 < 5 && $$21 >= 0 && $$21 < 5 && $$20 >= 0 && $$20 < 3) {
                                    int $$22 = m_228889_($$19, $$20, $$21);
                                    if ($$1[$$22] != null) {
                                        if ($$21 == $$15) {
                                            $$1[$$17].setConnection($$18, $$1[$$22]);
                                        } else {
                                            $$1[$$17].setConnection($$18.getOpposite(), $$1[$$22]);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            OceanMonumentPieces.RoomDefinition $$23 = new OceanMonumentPieces.RoomDefinition(1003);
            OceanMonumentPieces.RoomDefinition $$24 = new OceanMonumentPieces.RoomDefinition(1001);
            OceanMonumentPieces.RoomDefinition $$25 = new OceanMonumentPieces.RoomDefinition(1002);
            $$1[f_228821_].setConnection(Direction.UP, $$23);
            $$1[f_228822_].setConnection(Direction.SOUTH, $$24);
            $$1[f_228823_].setConnection(Direction.SOUTH, $$25);
            $$23.claimed = true;
            $$24.claimed = true;
            $$25.claimed = true;
            this.sourceRoom.isSource = true;
            this.coreRoom = $$1[m_228889_(randomSource0.nextInt(4), 0, 2)];
            this.coreRoom.claimed = true;
            this.coreRoom.connections[Direction.EAST.get3DDataValue()].claimed = true;
            this.coreRoom.connections[Direction.NORTH.get3DDataValue()].claimed = true;
            this.coreRoom.connections[Direction.EAST.get3DDataValue()].connections[Direction.NORTH.get3DDataValue()].claimed = true;
            this.coreRoom.connections[Direction.UP.get3DDataValue()].claimed = true;
            this.coreRoom.connections[Direction.EAST.get3DDataValue()].connections[Direction.UP.get3DDataValue()].claimed = true;
            this.coreRoom.connections[Direction.NORTH.get3DDataValue()].connections[Direction.UP.get3DDataValue()].claimed = true;
            this.coreRoom.connections[Direction.EAST.get3DDataValue()].connections[Direction.NORTH.get3DDataValue()].connections[Direction.UP.get3DDataValue()].claimed = true;
            ObjectArrayList<OceanMonumentPieces.RoomDefinition> $$26 = new ObjectArrayList();
            for (OceanMonumentPieces.RoomDefinition $$27 : $$1) {
                if ($$27 != null) {
                    $$27.updateOpenings();
                    $$26.add($$27);
                }
            }
            $$23.updateOpenings();
            Util.shuffle($$26, randomSource0);
            int $$28 = 1;
            ObjectListIterator var34 = $$26.iterator();
            while (var34.hasNext()) {
                OceanMonumentPieces.RoomDefinition $$29 = (OceanMonumentPieces.RoomDefinition) var34.next();
                int $$30 = 0;
                int $$31 = 0;
                while ($$30 < 2 && $$31 < 5) {
                    $$31++;
                    int $$32 = randomSource0.nextInt(6);
                    if ($$29.hasOpening[$$32]) {
                        int $$33 = Direction.from3DDataValue($$32).getOpposite().get3DDataValue();
                        $$29.hasOpening[$$32] = false;
                        $$29.connections[$$32].hasOpening[$$33] = false;
                        if ($$29.findSource($$28++) && $$29.connections[$$32].findSource($$28++)) {
                            $$30++;
                        } else {
                            $$29.hasOpening[$$32] = true;
                            $$29.connections[$$32].hasOpening[$$33] = true;
                        }
                    }
                }
            }
            $$26.add($$23);
            $$26.add($$24);
            $$26.add($$25);
            return $$26;
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            int $$7 = Math.max(worldGenLevel0.m_5736_(), 64) - this.f_73383_.minY();
            this.m_228880_(worldGenLevel0, boundingBox4, 0, 0, 0, 58, $$7, 58);
            this.generateWing(false, 0, worldGenLevel0, randomSource3, boundingBox4);
            this.generateWing(true, 33, worldGenLevel0, randomSource3, boundingBox4);
            this.generateEntranceArchs(worldGenLevel0, randomSource3, boundingBox4);
            this.generateEntranceWall(worldGenLevel0, randomSource3, boundingBox4);
            this.generateRoofPiece(worldGenLevel0, randomSource3, boundingBox4);
            this.generateLowerWall(worldGenLevel0, randomSource3, boundingBox4);
            this.generateMiddleWall(worldGenLevel0, randomSource3, boundingBox4);
            this.generateUpperWall(worldGenLevel0, randomSource3, boundingBox4);
            for (int $$8 = 0; $$8 < 7; $$8++) {
                int $$9 = 0;
                while ($$9 < 7) {
                    if ($$9 == 0 && $$8 == 3) {
                        $$9 = 6;
                    }
                    int $$10 = $$8 * 9;
                    int $$11 = $$9 * 9;
                    for (int $$12 = 0; $$12 < 4; $$12++) {
                        for (int $$13 = 0; $$13 < 4; $$13++) {
                            this.m_73434_(worldGenLevel0, f_228805_, $$10 + $$12, 0, $$11 + $$13, boundingBox4);
                            this.m_73528_(worldGenLevel0, f_228805_, $$10 + $$12, -1, $$11 + $$13, boundingBox4);
                        }
                    }
                    if ($$8 != 0 && $$8 != 6) {
                        $$9 += 6;
                    } else {
                        $$9++;
                    }
                }
            }
            for (int $$14 = 0; $$14 < 5; $$14++) {
                this.m_228880_(worldGenLevel0, boundingBox4, -1 - $$14, 0 + $$14 * 2, -1 - $$14, -1 - $$14, 23, 58 + $$14);
                this.m_228880_(worldGenLevel0, boundingBox4, 58 + $$14, 0 + $$14 * 2, -1 - $$14, 58 + $$14, 23, 58 + $$14);
                this.m_228880_(worldGenLevel0, boundingBox4, 0 - $$14, 0 + $$14 * 2, -1 - $$14, 57 + $$14, 23, -1 - $$14);
                this.m_228880_(worldGenLevel0, boundingBox4, 0 - $$14, 0 + $$14 * 2, 58 + $$14, 57 + $$14, 23, 58 + $$14);
            }
            for (OceanMonumentPieces.OceanMonumentPiece $$15 : this.childPieces) {
                if ($$15.m_73547_().intersects(boundingBox4)) {
                    $$15.m_213694_(worldGenLevel0, structureManager1, chunkGenerator2, randomSource3, boundingBox4, chunkPos5, blockPos6);
                }
            }
        }

        private void generateWing(boolean boolean0, int int1, WorldGenLevel worldGenLevel2, RandomSource randomSource3, BoundingBox boundingBox4) {
            int $$5 = 24;
            if (this.m_228865_(boundingBox4, int1, 0, int1 + 23, 20)) {
                this.m_73441_(worldGenLevel2, boundingBox4, int1 + 0, 0, 0, int1 + 24, 0, 20, f_228804_, f_228804_, false);
                this.m_228880_(worldGenLevel2, boundingBox4, int1 + 0, 1, 0, int1 + 24, 10, 20);
                for (int $$6 = 0; $$6 < 4; $$6++) {
                    this.m_73441_(worldGenLevel2, boundingBox4, int1 + $$6, $$6 + 1, $$6, int1 + $$6, $$6 + 1, 20, f_228805_, f_228805_, false);
                    this.m_73441_(worldGenLevel2, boundingBox4, int1 + $$6 + 7, $$6 + 5, $$6 + 7, int1 + $$6 + 7, $$6 + 5, 20, f_228805_, f_228805_, false);
                    this.m_73441_(worldGenLevel2, boundingBox4, int1 + 17 - $$6, $$6 + 5, $$6 + 7, int1 + 17 - $$6, $$6 + 5, 20, f_228805_, f_228805_, false);
                    this.m_73441_(worldGenLevel2, boundingBox4, int1 + 24 - $$6, $$6 + 1, $$6, int1 + 24 - $$6, $$6 + 1, 20, f_228805_, f_228805_, false);
                    this.m_73441_(worldGenLevel2, boundingBox4, int1 + $$6 + 1, $$6 + 1, $$6, int1 + 23 - $$6, $$6 + 1, $$6, f_228805_, f_228805_, false);
                    this.m_73441_(worldGenLevel2, boundingBox4, int1 + $$6 + 8, $$6 + 5, $$6 + 7, int1 + 16 - $$6, $$6 + 5, $$6 + 7, f_228805_, f_228805_, false);
                }
                this.m_73441_(worldGenLevel2, boundingBox4, int1 + 4, 4, 4, int1 + 6, 4, 20, f_228804_, f_228804_, false);
                this.m_73441_(worldGenLevel2, boundingBox4, int1 + 7, 4, 4, int1 + 17, 4, 6, f_228804_, f_228804_, false);
                this.m_73441_(worldGenLevel2, boundingBox4, int1 + 18, 4, 4, int1 + 20, 4, 20, f_228804_, f_228804_, false);
                this.m_73441_(worldGenLevel2, boundingBox4, int1 + 11, 8, 11, int1 + 13, 8, 20, f_228804_, f_228804_, false);
                this.m_73434_(worldGenLevel2, f_228807_, int1 + 12, 9, 12, boundingBox4);
                this.m_73434_(worldGenLevel2, f_228807_, int1 + 12, 9, 15, boundingBox4);
                this.m_73434_(worldGenLevel2, f_228807_, int1 + 12, 9, 18, boundingBox4);
                int $$7 = int1 + (boolean0 ? 19 : 5);
                int $$8 = int1 + (boolean0 ? 5 : 19);
                for (int $$9 = 20; $$9 >= 5; $$9 -= 3) {
                    this.m_73434_(worldGenLevel2, f_228807_, $$7, 5, $$9, boundingBox4);
                }
                for (int $$10 = 19; $$10 >= 7; $$10 -= 3) {
                    this.m_73434_(worldGenLevel2, f_228807_, $$8, 5, $$10, boundingBox4);
                }
                for (int $$11 = 0; $$11 < 4; $$11++) {
                    int $$12 = boolean0 ? int1 + 24 - (17 - $$11 * 3) : int1 + 17 - $$11 * 3;
                    this.m_73434_(worldGenLevel2, f_228807_, $$12, 5, 5, boundingBox4);
                }
                this.m_73434_(worldGenLevel2, f_228807_, $$8, 5, 5, boundingBox4);
                this.m_73441_(worldGenLevel2, boundingBox4, int1 + 11, 1, 12, int1 + 13, 7, 12, f_228804_, f_228804_, false);
                this.m_73441_(worldGenLevel2, boundingBox4, int1 + 12, 1, 11, int1 + 12, 7, 13, f_228804_, f_228804_, false);
            }
        }

        private void generateEntranceArchs(WorldGenLevel worldGenLevel0, RandomSource randomSource1, BoundingBox boundingBox2) {
            if (this.m_228865_(boundingBox2, 22, 5, 35, 17)) {
                this.m_228880_(worldGenLevel0, boundingBox2, 25, 0, 0, 32, 8, 20);
                for (int $$3 = 0; $$3 < 4; $$3++) {
                    this.m_73441_(worldGenLevel0, boundingBox2, 24, 2, 5 + $$3 * 4, 24, 4, 5 + $$3 * 4, f_228805_, f_228805_, false);
                    this.m_73441_(worldGenLevel0, boundingBox2, 22, 4, 5 + $$3 * 4, 23, 4, 5 + $$3 * 4, f_228805_, f_228805_, false);
                    this.m_73434_(worldGenLevel0, f_228805_, 25, 5, 5 + $$3 * 4, boundingBox2);
                    this.m_73434_(worldGenLevel0, f_228805_, 26, 6, 5 + $$3 * 4, boundingBox2);
                    this.m_73434_(worldGenLevel0, f_228808_, 26, 5, 5 + $$3 * 4, boundingBox2);
                    this.m_73441_(worldGenLevel0, boundingBox2, 33, 2, 5 + $$3 * 4, 33, 4, 5 + $$3 * 4, f_228805_, f_228805_, false);
                    this.m_73441_(worldGenLevel0, boundingBox2, 34, 4, 5 + $$3 * 4, 35, 4, 5 + $$3 * 4, f_228805_, f_228805_, false);
                    this.m_73434_(worldGenLevel0, f_228805_, 32, 5, 5 + $$3 * 4, boundingBox2);
                    this.m_73434_(worldGenLevel0, f_228805_, 31, 6, 5 + $$3 * 4, boundingBox2);
                    this.m_73434_(worldGenLevel0, f_228808_, 31, 5, 5 + $$3 * 4, boundingBox2);
                    this.m_73441_(worldGenLevel0, boundingBox2, 27, 6, 5 + $$3 * 4, 30, 6, 5 + $$3 * 4, f_228804_, f_228804_, false);
                }
            }
        }

        private void generateEntranceWall(WorldGenLevel worldGenLevel0, RandomSource randomSource1, BoundingBox boundingBox2) {
            if (this.m_228865_(boundingBox2, 15, 20, 42, 21)) {
                this.m_73441_(worldGenLevel0, boundingBox2, 15, 0, 21, 42, 0, 21, f_228804_, f_228804_, false);
                this.m_228880_(worldGenLevel0, boundingBox2, 26, 1, 21, 31, 3, 21);
                this.m_73441_(worldGenLevel0, boundingBox2, 21, 12, 21, 36, 12, 21, f_228804_, f_228804_, false);
                this.m_73441_(worldGenLevel0, boundingBox2, 17, 11, 21, 40, 11, 21, f_228804_, f_228804_, false);
                this.m_73441_(worldGenLevel0, boundingBox2, 16, 10, 21, 41, 10, 21, f_228804_, f_228804_, false);
                this.m_73441_(worldGenLevel0, boundingBox2, 15, 7, 21, 42, 9, 21, f_228804_, f_228804_, false);
                this.m_73441_(worldGenLevel0, boundingBox2, 16, 6, 21, 41, 6, 21, f_228804_, f_228804_, false);
                this.m_73441_(worldGenLevel0, boundingBox2, 17, 5, 21, 40, 5, 21, f_228804_, f_228804_, false);
                this.m_73441_(worldGenLevel0, boundingBox2, 21, 4, 21, 36, 4, 21, f_228804_, f_228804_, false);
                this.m_73441_(worldGenLevel0, boundingBox2, 22, 3, 21, 26, 3, 21, f_228804_, f_228804_, false);
                this.m_73441_(worldGenLevel0, boundingBox2, 31, 3, 21, 35, 3, 21, f_228804_, f_228804_, false);
                this.m_73441_(worldGenLevel0, boundingBox2, 23, 2, 21, 25, 2, 21, f_228804_, f_228804_, false);
                this.m_73441_(worldGenLevel0, boundingBox2, 32, 2, 21, 34, 2, 21, f_228804_, f_228804_, false);
                this.m_73441_(worldGenLevel0, boundingBox2, 28, 4, 20, 29, 4, 21, f_228805_, f_228805_, false);
                this.m_73434_(worldGenLevel0, f_228805_, 27, 3, 21, boundingBox2);
                this.m_73434_(worldGenLevel0, f_228805_, 30, 3, 21, boundingBox2);
                this.m_73434_(worldGenLevel0, f_228805_, 26, 2, 21, boundingBox2);
                this.m_73434_(worldGenLevel0, f_228805_, 31, 2, 21, boundingBox2);
                this.m_73434_(worldGenLevel0, f_228805_, 25, 1, 21, boundingBox2);
                this.m_73434_(worldGenLevel0, f_228805_, 32, 1, 21, boundingBox2);
                for (int $$3 = 0; $$3 < 7; $$3++) {
                    this.m_73434_(worldGenLevel0, f_228806_, 28 - $$3, 6 + $$3, 21, boundingBox2);
                    this.m_73434_(worldGenLevel0, f_228806_, 29 + $$3, 6 + $$3, 21, boundingBox2);
                }
                for (int $$4 = 0; $$4 < 4; $$4++) {
                    this.m_73434_(worldGenLevel0, f_228806_, 28 - $$4, 9 + $$4, 21, boundingBox2);
                    this.m_73434_(worldGenLevel0, f_228806_, 29 + $$4, 9 + $$4, 21, boundingBox2);
                }
                this.m_73434_(worldGenLevel0, f_228806_, 28, 12, 21, boundingBox2);
                this.m_73434_(worldGenLevel0, f_228806_, 29, 12, 21, boundingBox2);
                for (int $$5 = 0; $$5 < 3; $$5++) {
                    this.m_73434_(worldGenLevel0, f_228806_, 22 - $$5 * 2, 8, 21, boundingBox2);
                    this.m_73434_(worldGenLevel0, f_228806_, 22 - $$5 * 2, 9, 21, boundingBox2);
                    this.m_73434_(worldGenLevel0, f_228806_, 35 + $$5 * 2, 8, 21, boundingBox2);
                    this.m_73434_(worldGenLevel0, f_228806_, 35 + $$5 * 2, 9, 21, boundingBox2);
                }
                this.m_228880_(worldGenLevel0, boundingBox2, 15, 13, 21, 42, 15, 21);
                this.m_228880_(worldGenLevel0, boundingBox2, 15, 1, 21, 15, 6, 21);
                this.m_228880_(worldGenLevel0, boundingBox2, 16, 1, 21, 16, 5, 21);
                this.m_228880_(worldGenLevel0, boundingBox2, 17, 1, 21, 20, 4, 21);
                this.m_228880_(worldGenLevel0, boundingBox2, 21, 1, 21, 21, 3, 21);
                this.m_228880_(worldGenLevel0, boundingBox2, 22, 1, 21, 22, 2, 21);
                this.m_228880_(worldGenLevel0, boundingBox2, 23, 1, 21, 24, 1, 21);
                this.m_228880_(worldGenLevel0, boundingBox2, 42, 1, 21, 42, 6, 21);
                this.m_228880_(worldGenLevel0, boundingBox2, 41, 1, 21, 41, 5, 21);
                this.m_228880_(worldGenLevel0, boundingBox2, 37, 1, 21, 40, 4, 21);
                this.m_228880_(worldGenLevel0, boundingBox2, 36, 1, 21, 36, 3, 21);
                this.m_228880_(worldGenLevel0, boundingBox2, 33, 1, 21, 34, 1, 21);
                this.m_228880_(worldGenLevel0, boundingBox2, 35, 1, 21, 35, 2, 21);
            }
        }

        private void generateRoofPiece(WorldGenLevel worldGenLevel0, RandomSource randomSource1, BoundingBox boundingBox2) {
            if (this.m_228865_(boundingBox2, 21, 21, 36, 36)) {
                this.m_73441_(worldGenLevel0, boundingBox2, 21, 0, 22, 36, 0, 36, f_228804_, f_228804_, false);
                this.m_228880_(worldGenLevel0, boundingBox2, 21, 1, 22, 36, 23, 36);
                for (int $$3 = 0; $$3 < 4; $$3++) {
                    this.m_73441_(worldGenLevel0, boundingBox2, 21 + $$3, 13 + $$3, 21 + $$3, 36 - $$3, 13 + $$3, 21 + $$3, f_228805_, f_228805_, false);
                    this.m_73441_(worldGenLevel0, boundingBox2, 21 + $$3, 13 + $$3, 36 - $$3, 36 - $$3, 13 + $$3, 36 - $$3, f_228805_, f_228805_, false);
                    this.m_73441_(worldGenLevel0, boundingBox2, 21 + $$3, 13 + $$3, 22 + $$3, 21 + $$3, 13 + $$3, 35 - $$3, f_228805_, f_228805_, false);
                    this.m_73441_(worldGenLevel0, boundingBox2, 36 - $$3, 13 + $$3, 22 + $$3, 36 - $$3, 13 + $$3, 35 - $$3, f_228805_, f_228805_, false);
                }
                this.m_73441_(worldGenLevel0, boundingBox2, 25, 16, 25, 32, 16, 32, f_228804_, f_228804_, false);
                this.m_73441_(worldGenLevel0, boundingBox2, 25, 17, 25, 25, 19, 25, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox2, 32, 17, 25, 32, 19, 25, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox2, 25, 17, 32, 25, 19, 32, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox2, 32, 17, 32, 32, 19, 32, f_228805_, f_228805_, false);
                this.m_73434_(worldGenLevel0, f_228805_, 26, 20, 26, boundingBox2);
                this.m_73434_(worldGenLevel0, f_228805_, 27, 21, 27, boundingBox2);
                this.m_73434_(worldGenLevel0, f_228808_, 27, 20, 27, boundingBox2);
                this.m_73434_(worldGenLevel0, f_228805_, 26, 20, 31, boundingBox2);
                this.m_73434_(worldGenLevel0, f_228805_, 27, 21, 30, boundingBox2);
                this.m_73434_(worldGenLevel0, f_228808_, 27, 20, 30, boundingBox2);
                this.m_73434_(worldGenLevel0, f_228805_, 31, 20, 31, boundingBox2);
                this.m_73434_(worldGenLevel0, f_228805_, 30, 21, 30, boundingBox2);
                this.m_73434_(worldGenLevel0, f_228808_, 30, 20, 30, boundingBox2);
                this.m_73434_(worldGenLevel0, f_228805_, 31, 20, 26, boundingBox2);
                this.m_73434_(worldGenLevel0, f_228805_, 30, 21, 27, boundingBox2);
                this.m_73434_(worldGenLevel0, f_228808_, 30, 20, 27, boundingBox2);
                this.m_73441_(worldGenLevel0, boundingBox2, 28, 21, 27, 29, 21, 27, f_228804_, f_228804_, false);
                this.m_73441_(worldGenLevel0, boundingBox2, 27, 21, 28, 27, 21, 29, f_228804_, f_228804_, false);
                this.m_73441_(worldGenLevel0, boundingBox2, 28, 21, 30, 29, 21, 30, f_228804_, f_228804_, false);
                this.m_73441_(worldGenLevel0, boundingBox2, 30, 21, 28, 30, 21, 29, f_228804_, f_228804_, false);
            }
        }

        private void generateLowerWall(WorldGenLevel worldGenLevel0, RandomSource randomSource1, BoundingBox boundingBox2) {
            if (this.m_228865_(boundingBox2, 0, 21, 6, 58)) {
                this.m_73441_(worldGenLevel0, boundingBox2, 0, 0, 21, 6, 0, 57, f_228804_, f_228804_, false);
                this.m_228880_(worldGenLevel0, boundingBox2, 0, 1, 21, 6, 7, 57);
                this.m_73441_(worldGenLevel0, boundingBox2, 4, 4, 21, 6, 4, 53, f_228804_, f_228804_, false);
                for (int $$3 = 0; $$3 < 4; $$3++) {
                    this.m_73441_(worldGenLevel0, boundingBox2, $$3, $$3 + 1, 21, $$3, $$3 + 1, 57 - $$3, f_228805_, f_228805_, false);
                }
                for (int $$4 = 23; $$4 < 53; $$4 += 3) {
                    this.m_73434_(worldGenLevel0, f_228807_, 5, 5, $$4, boundingBox2);
                }
                this.m_73434_(worldGenLevel0, f_228807_, 5, 5, 52, boundingBox2);
                for (int $$5 = 0; $$5 < 4; $$5++) {
                    this.m_73441_(worldGenLevel0, boundingBox2, $$5, $$5 + 1, 21, $$5, $$5 + 1, 57 - $$5, f_228805_, f_228805_, false);
                }
                this.m_73441_(worldGenLevel0, boundingBox2, 4, 1, 52, 6, 3, 52, f_228804_, f_228804_, false);
                this.m_73441_(worldGenLevel0, boundingBox2, 5, 1, 51, 5, 3, 53, f_228804_, f_228804_, false);
            }
            if (this.m_228865_(boundingBox2, 51, 21, 58, 58)) {
                this.m_73441_(worldGenLevel0, boundingBox2, 51, 0, 21, 57, 0, 57, f_228804_, f_228804_, false);
                this.m_228880_(worldGenLevel0, boundingBox2, 51, 1, 21, 57, 7, 57);
                this.m_73441_(worldGenLevel0, boundingBox2, 51, 4, 21, 53, 4, 53, f_228804_, f_228804_, false);
                for (int $$6 = 0; $$6 < 4; $$6++) {
                    this.m_73441_(worldGenLevel0, boundingBox2, 57 - $$6, $$6 + 1, 21, 57 - $$6, $$6 + 1, 57 - $$6, f_228805_, f_228805_, false);
                }
                for (int $$7 = 23; $$7 < 53; $$7 += 3) {
                    this.m_73434_(worldGenLevel0, f_228807_, 52, 5, $$7, boundingBox2);
                }
                this.m_73434_(worldGenLevel0, f_228807_, 52, 5, 52, boundingBox2);
                this.m_73441_(worldGenLevel0, boundingBox2, 51, 1, 52, 53, 3, 52, f_228804_, f_228804_, false);
                this.m_73441_(worldGenLevel0, boundingBox2, 52, 1, 51, 52, 3, 53, f_228804_, f_228804_, false);
            }
            if (this.m_228865_(boundingBox2, 0, 51, 57, 57)) {
                this.m_73441_(worldGenLevel0, boundingBox2, 7, 0, 51, 50, 0, 57, f_228804_, f_228804_, false);
                this.m_228880_(worldGenLevel0, boundingBox2, 7, 1, 51, 50, 10, 57);
                for (int $$8 = 0; $$8 < 4; $$8++) {
                    this.m_73441_(worldGenLevel0, boundingBox2, $$8 + 1, $$8 + 1, 57 - $$8, 56 - $$8, $$8 + 1, 57 - $$8, f_228805_, f_228805_, false);
                }
            }
        }

        private void generateMiddleWall(WorldGenLevel worldGenLevel0, RandomSource randomSource1, BoundingBox boundingBox2) {
            if (this.m_228865_(boundingBox2, 7, 21, 13, 50)) {
                this.m_73441_(worldGenLevel0, boundingBox2, 7, 0, 21, 13, 0, 50, f_228804_, f_228804_, false);
                this.m_228880_(worldGenLevel0, boundingBox2, 7, 1, 21, 13, 10, 50);
                this.m_73441_(worldGenLevel0, boundingBox2, 11, 8, 21, 13, 8, 53, f_228804_, f_228804_, false);
                for (int $$3 = 0; $$3 < 4; $$3++) {
                    this.m_73441_(worldGenLevel0, boundingBox2, $$3 + 7, $$3 + 5, 21, $$3 + 7, $$3 + 5, 54, f_228805_, f_228805_, false);
                }
                for (int $$4 = 21; $$4 <= 45; $$4 += 3) {
                    this.m_73434_(worldGenLevel0, f_228807_, 12, 9, $$4, boundingBox2);
                }
            }
            if (this.m_228865_(boundingBox2, 44, 21, 50, 54)) {
                this.m_73441_(worldGenLevel0, boundingBox2, 44, 0, 21, 50, 0, 50, f_228804_, f_228804_, false);
                this.m_228880_(worldGenLevel0, boundingBox2, 44, 1, 21, 50, 10, 50);
                this.m_73441_(worldGenLevel0, boundingBox2, 44, 8, 21, 46, 8, 53, f_228804_, f_228804_, false);
                for (int $$5 = 0; $$5 < 4; $$5++) {
                    this.m_73441_(worldGenLevel0, boundingBox2, 50 - $$5, $$5 + 5, 21, 50 - $$5, $$5 + 5, 54, f_228805_, f_228805_, false);
                }
                for (int $$6 = 21; $$6 <= 45; $$6 += 3) {
                    this.m_73434_(worldGenLevel0, f_228807_, 45, 9, $$6, boundingBox2);
                }
            }
            if (this.m_228865_(boundingBox2, 8, 44, 49, 54)) {
                this.m_73441_(worldGenLevel0, boundingBox2, 14, 0, 44, 43, 0, 50, f_228804_, f_228804_, false);
                this.m_228880_(worldGenLevel0, boundingBox2, 14, 1, 44, 43, 10, 50);
                for (int $$7 = 12; $$7 <= 45; $$7 += 3) {
                    this.m_73434_(worldGenLevel0, f_228807_, $$7, 9, 45, boundingBox2);
                    this.m_73434_(worldGenLevel0, f_228807_, $$7, 9, 52, boundingBox2);
                    if ($$7 == 12 || $$7 == 18 || $$7 == 24 || $$7 == 33 || $$7 == 39 || $$7 == 45) {
                        this.m_73434_(worldGenLevel0, f_228807_, $$7, 9, 47, boundingBox2);
                        this.m_73434_(worldGenLevel0, f_228807_, $$7, 9, 50, boundingBox2);
                        this.m_73434_(worldGenLevel0, f_228807_, $$7, 10, 45, boundingBox2);
                        this.m_73434_(worldGenLevel0, f_228807_, $$7, 10, 46, boundingBox2);
                        this.m_73434_(worldGenLevel0, f_228807_, $$7, 10, 51, boundingBox2);
                        this.m_73434_(worldGenLevel0, f_228807_, $$7, 10, 52, boundingBox2);
                        this.m_73434_(worldGenLevel0, f_228807_, $$7, 11, 47, boundingBox2);
                        this.m_73434_(worldGenLevel0, f_228807_, $$7, 11, 50, boundingBox2);
                        this.m_73434_(worldGenLevel0, f_228807_, $$7, 12, 48, boundingBox2);
                        this.m_73434_(worldGenLevel0, f_228807_, $$7, 12, 49, boundingBox2);
                    }
                }
                for (int $$8 = 0; $$8 < 3; $$8++) {
                    this.m_73441_(worldGenLevel0, boundingBox2, 8 + $$8, 5 + $$8, 54, 49 - $$8, 5 + $$8, 54, f_228804_, f_228804_, false);
                }
                this.m_73441_(worldGenLevel0, boundingBox2, 11, 8, 54, 46, 8, 54, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox2, 14, 8, 44, 43, 8, 53, f_228804_, f_228804_, false);
            }
        }

        private void generateUpperWall(WorldGenLevel worldGenLevel0, RandomSource randomSource1, BoundingBox boundingBox2) {
            if (this.m_228865_(boundingBox2, 14, 21, 20, 43)) {
                this.m_73441_(worldGenLevel0, boundingBox2, 14, 0, 21, 20, 0, 43, f_228804_, f_228804_, false);
                this.m_228880_(worldGenLevel0, boundingBox2, 14, 1, 22, 20, 14, 43);
                this.m_73441_(worldGenLevel0, boundingBox2, 18, 12, 22, 20, 12, 39, f_228804_, f_228804_, false);
                this.m_73441_(worldGenLevel0, boundingBox2, 18, 12, 21, 20, 12, 21, f_228805_, f_228805_, false);
                for (int $$3 = 0; $$3 < 4; $$3++) {
                    this.m_73441_(worldGenLevel0, boundingBox2, $$3 + 14, $$3 + 9, 21, $$3 + 14, $$3 + 9, 43 - $$3, f_228805_, f_228805_, false);
                }
                for (int $$4 = 23; $$4 <= 39; $$4 += 3) {
                    this.m_73434_(worldGenLevel0, f_228807_, 19, 13, $$4, boundingBox2);
                }
            }
            if (this.m_228865_(boundingBox2, 37, 21, 43, 43)) {
                this.m_73441_(worldGenLevel0, boundingBox2, 37, 0, 21, 43, 0, 43, f_228804_, f_228804_, false);
                this.m_228880_(worldGenLevel0, boundingBox2, 37, 1, 22, 43, 14, 43);
                this.m_73441_(worldGenLevel0, boundingBox2, 37, 12, 22, 39, 12, 39, f_228804_, f_228804_, false);
                this.m_73441_(worldGenLevel0, boundingBox2, 37, 12, 21, 39, 12, 21, f_228805_, f_228805_, false);
                for (int $$5 = 0; $$5 < 4; $$5++) {
                    this.m_73441_(worldGenLevel0, boundingBox2, 43 - $$5, $$5 + 9, 21, 43 - $$5, $$5 + 9, 43 - $$5, f_228805_, f_228805_, false);
                }
                for (int $$6 = 23; $$6 <= 39; $$6 += 3) {
                    this.m_73434_(worldGenLevel0, f_228807_, 38, 13, $$6, boundingBox2);
                }
            }
            if (this.m_228865_(boundingBox2, 15, 37, 42, 43)) {
                this.m_73441_(worldGenLevel0, boundingBox2, 21, 0, 37, 36, 0, 43, f_228804_, f_228804_, false);
                this.m_228880_(worldGenLevel0, boundingBox2, 21, 1, 37, 36, 14, 43);
                this.m_73441_(worldGenLevel0, boundingBox2, 21, 12, 37, 36, 12, 39, f_228804_, f_228804_, false);
                for (int $$7 = 0; $$7 < 4; $$7++) {
                    this.m_73441_(worldGenLevel0, boundingBox2, 15 + $$7, $$7 + 9, 43 - $$7, 42 - $$7, $$7 + 9, 43 - $$7, f_228805_, f_228805_, false);
                }
                for (int $$8 = 21; $$8 <= 36; $$8 += 3) {
                    this.m_73434_(worldGenLevel0, f_228807_, $$8, 13, 38, boundingBox2);
                }
            }
        }
    }

    interface MonumentRoomFitter {

        boolean fits(OceanMonumentPieces.RoomDefinition var1);

        OceanMonumentPieces.OceanMonumentPiece create(Direction var1, OceanMonumentPieces.RoomDefinition var2, RandomSource var3);
    }

    public static class OceanMonumentCoreRoom extends OceanMonumentPieces.OceanMonumentPiece {

        public OceanMonumentCoreRoom(Direction direction0, OceanMonumentPieces.RoomDefinition oceanMonumentPiecesRoomDefinition1) {
            super(StructurePieceType.OCEAN_MONUMENT_CORE_ROOM, 1, direction0, oceanMonumentPiecesRoomDefinition1, 2, 2, 2);
        }

        public OceanMonumentCoreRoom(CompoundTag compoundTag0) {
            super(StructurePieceType.OCEAN_MONUMENT_CORE_ROOM, compoundTag0);
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            this.m_228849_(worldGenLevel0, boundingBox4, 1, 8, 0, 14, 8, 14, f_228804_);
            int $$7 = 7;
            BlockState $$8 = f_228805_;
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 7, 0, 0, 7, 15, $$8, $$8, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 15, 7, 0, 15, 7, 15, $$8, $$8, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 7, 0, 15, 7, 0, $$8, $$8, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 7, 15, 14, 7, 15, $$8, $$8, false);
            for (int $$9 = 1; $$9 <= 6; $$9++) {
                $$8 = f_228805_;
                if ($$9 == 2 || $$9 == 6) {
                    $$8 = f_228804_;
                }
                for (int $$11 = 0; $$11 <= 15; $$11 += 15) {
                    this.m_73441_(worldGenLevel0, boundingBox4, $$11, $$9, 0, $$11, $$9, 1, $$8, $$8, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, $$11, $$9, 6, $$11, $$9, 9, $$8, $$8, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, $$11, $$9, 14, $$11, $$9, 15, $$8, $$8, false);
                }
                this.m_73441_(worldGenLevel0, boundingBox4, 1, $$9, 0, 1, $$9, 0, $$8, $$8, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 6, $$9, 0, 9, $$9, 0, $$8, $$8, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 14, $$9, 0, 14, $$9, 0, $$8, $$8, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 1, $$9, 15, 14, $$9, 15, $$8, $$8, false);
            }
            this.m_73441_(worldGenLevel0, boundingBox4, 6, 3, 6, 9, 6, 9, f_228806_, f_228806_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 7, 4, 7, 8, 5, 8, Blocks.GOLD_BLOCK.defaultBlockState(), Blocks.GOLD_BLOCK.defaultBlockState(), false);
            for (int $$12 = 3; $$12 <= 6; $$12 += 3) {
                for (int $$13 = 6; $$13 <= 9; $$13 += 3) {
                    this.m_73434_(worldGenLevel0, f_228808_, $$13, $$12, 6, boundingBox4);
                    this.m_73434_(worldGenLevel0, f_228808_, $$13, $$12, 9, boundingBox4);
                }
            }
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 1, 6, 5, 2, 6, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 1, 9, 5, 2, 9, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 10, 1, 6, 10, 2, 6, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 10, 1, 9, 10, 2, 9, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 6, 1, 5, 6, 2, 5, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 9, 1, 5, 9, 2, 5, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 6, 1, 10, 6, 2, 10, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 9, 1, 10, 9, 2, 10, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 2, 5, 5, 6, 5, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 2, 10, 5, 6, 10, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 10, 2, 5, 10, 6, 5, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 10, 2, 10, 10, 6, 10, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 7, 1, 5, 7, 6, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 10, 7, 1, 10, 7, 6, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 7, 9, 5, 7, 14, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 10, 7, 9, 10, 7, 14, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 7, 5, 6, 7, 5, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 7, 10, 6, 7, 10, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 9, 7, 5, 14, 7, 5, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 9, 7, 10, 14, 7, 10, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 2, 1, 2, 2, 1, 3, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 3, 1, 2, 3, 1, 2, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 13, 1, 2, 13, 1, 3, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 12, 1, 2, 12, 1, 2, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 2, 1, 12, 2, 1, 13, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 3, 1, 13, 3, 1, 13, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 13, 1, 12, 13, 1, 13, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 12, 1, 13, 12, 1, 13, f_228805_, f_228805_, false);
        }
    }

    public static class OceanMonumentDoubleXRoom extends OceanMonumentPieces.OceanMonumentPiece {

        public OceanMonumentDoubleXRoom(Direction direction0, OceanMonumentPieces.RoomDefinition oceanMonumentPiecesRoomDefinition1) {
            super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_X_ROOM, 1, direction0, oceanMonumentPiecesRoomDefinition1, 2, 1, 1);
        }

        public OceanMonumentDoubleXRoom(CompoundTag compoundTag0) {
            super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_X_ROOM, compoundTag0);
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            OceanMonumentPieces.RoomDefinition $$7 = this.f_228803_.connections[Direction.EAST.get3DDataValue()];
            OceanMonumentPieces.RoomDefinition $$8 = this.f_228803_;
            if (this.f_228803_.index / 25 > 0) {
                this.m_228859_(worldGenLevel0, boundingBox4, 8, 0, $$7.hasOpening[Direction.DOWN.get3DDataValue()]);
                this.m_228859_(worldGenLevel0, boundingBox4, 0, 0, $$8.hasOpening[Direction.DOWN.get3DDataValue()]);
            }
            if ($$8.connections[Direction.UP.get3DDataValue()] == null) {
                this.m_228849_(worldGenLevel0, boundingBox4, 1, 4, 1, 7, 4, 6, f_228804_);
            }
            if ($$7.connections[Direction.UP.get3DDataValue()] == null) {
                this.m_228849_(worldGenLevel0, boundingBox4, 8, 4, 1, 14, 4, 6, f_228804_);
            }
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 3, 0, 0, 3, 7, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 15, 3, 0, 15, 3, 7, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 3, 0, 15, 3, 0, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 3, 7, 14, 3, 7, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 0, 0, 2, 7, f_228804_, f_228804_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 15, 2, 0, 15, 2, 7, f_228804_, f_228804_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 2, 0, 15, 2, 0, f_228804_, f_228804_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 2, 7, 14, 2, 7, f_228804_, f_228804_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 1, 0, 0, 1, 7, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 15, 1, 0, 15, 1, 7, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 1, 0, 15, 1, 0, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 1, 7, 14, 1, 7, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 1, 0, 10, 1, 4, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 6, 2, 0, 9, 2, 3, f_228804_, f_228804_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 3, 0, 10, 3, 4, f_228805_, f_228805_, false);
            this.m_73434_(worldGenLevel0, f_228808_, 6, 2, 3, boundingBox4);
            this.m_73434_(worldGenLevel0, f_228808_, 9, 2, 3, boundingBox4);
            if ($$8.hasOpening[Direction.SOUTH.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 3, 1, 0, 4, 2, 0);
            }
            if ($$8.hasOpening[Direction.NORTH.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 3, 1, 7, 4, 2, 7);
            }
            if ($$8.hasOpening[Direction.WEST.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 0, 1, 3, 0, 2, 4);
            }
            if ($$7.hasOpening[Direction.SOUTH.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 11, 1, 0, 12, 2, 0);
            }
            if ($$7.hasOpening[Direction.NORTH.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 11, 1, 7, 12, 2, 7);
            }
            if ($$7.hasOpening[Direction.EAST.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 15, 1, 3, 15, 2, 4);
            }
        }
    }

    public static class OceanMonumentDoubleXYRoom extends OceanMonumentPieces.OceanMonumentPiece {

        public OceanMonumentDoubleXYRoom(Direction direction0, OceanMonumentPieces.RoomDefinition oceanMonumentPiecesRoomDefinition1) {
            super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_XY_ROOM, 1, direction0, oceanMonumentPiecesRoomDefinition1, 2, 2, 1);
        }

        public OceanMonumentDoubleXYRoom(CompoundTag compoundTag0) {
            super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_XY_ROOM, compoundTag0);
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            OceanMonumentPieces.RoomDefinition $$7 = this.f_228803_.connections[Direction.EAST.get3DDataValue()];
            OceanMonumentPieces.RoomDefinition $$8 = this.f_228803_;
            OceanMonumentPieces.RoomDefinition $$9 = $$8.connections[Direction.UP.get3DDataValue()];
            OceanMonumentPieces.RoomDefinition $$10 = $$7.connections[Direction.UP.get3DDataValue()];
            if (this.f_228803_.index / 25 > 0) {
                this.m_228859_(worldGenLevel0, boundingBox4, 8, 0, $$7.hasOpening[Direction.DOWN.get3DDataValue()]);
                this.m_228859_(worldGenLevel0, boundingBox4, 0, 0, $$8.hasOpening[Direction.DOWN.get3DDataValue()]);
            }
            if ($$9.connections[Direction.UP.get3DDataValue()] == null) {
                this.m_228849_(worldGenLevel0, boundingBox4, 1, 8, 1, 7, 8, 6, f_228804_);
            }
            if ($$10.connections[Direction.UP.get3DDataValue()] == null) {
                this.m_228849_(worldGenLevel0, boundingBox4, 8, 8, 1, 14, 8, 6, f_228804_);
            }
            for (int $$11 = 1; $$11 <= 7; $$11++) {
                BlockState $$12 = f_228805_;
                if ($$11 == 2 || $$11 == 6) {
                    $$12 = f_228804_;
                }
                this.m_73441_(worldGenLevel0, boundingBox4, 0, $$11, 0, 0, $$11, 7, $$12, $$12, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 15, $$11, 0, 15, $$11, 7, $$12, $$12, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 1, $$11, 0, 15, $$11, 0, $$12, $$12, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 1, $$11, 7, 14, $$11, 7, $$12, $$12, false);
            }
            this.m_73441_(worldGenLevel0, boundingBox4, 2, 1, 3, 2, 7, 4, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 3, 1, 2, 4, 7, 2, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 3, 1, 5, 4, 7, 5, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 13, 1, 3, 13, 7, 4, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 11, 1, 2, 12, 7, 2, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 11, 1, 5, 12, 7, 5, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 1, 3, 5, 3, 4, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 10, 1, 3, 10, 3, 4, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 7, 2, 10, 7, 5, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 5, 2, 5, 7, 2, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 10, 5, 2, 10, 7, 2, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 5, 5, 5, 7, 5, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 10, 5, 5, 10, 7, 5, f_228805_, f_228805_, false);
            this.m_73434_(worldGenLevel0, f_228805_, 6, 6, 2, boundingBox4);
            this.m_73434_(worldGenLevel0, f_228805_, 9, 6, 2, boundingBox4);
            this.m_73434_(worldGenLevel0, f_228805_, 6, 6, 5, boundingBox4);
            this.m_73434_(worldGenLevel0, f_228805_, 9, 6, 5, boundingBox4);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 4, 3, 6, 4, 4, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 9, 4, 3, 10, 4, 4, f_228805_, f_228805_, false);
            this.m_73434_(worldGenLevel0, f_228808_, 5, 4, 2, boundingBox4);
            this.m_73434_(worldGenLevel0, f_228808_, 5, 4, 5, boundingBox4);
            this.m_73434_(worldGenLevel0, f_228808_, 10, 4, 2, boundingBox4);
            this.m_73434_(worldGenLevel0, f_228808_, 10, 4, 5, boundingBox4);
            if ($$8.hasOpening[Direction.SOUTH.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 3, 1, 0, 4, 2, 0);
            }
            if ($$8.hasOpening[Direction.NORTH.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 3, 1, 7, 4, 2, 7);
            }
            if ($$8.hasOpening[Direction.WEST.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 0, 1, 3, 0, 2, 4);
            }
            if ($$7.hasOpening[Direction.SOUTH.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 11, 1, 0, 12, 2, 0);
            }
            if ($$7.hasOpening[Direction.NORTH.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 11, 1, 7, 12, 2, 7);
            }
            if ($$7.hasOpening[Direction.EAST.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 15, 1, 3, 15, 2, 4);
            }
            if ($$9.hasOpening[Direction.SOUTH.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 3, 5, 0, 4, 6, 0);
            }
            if ($$9.hasOpening[Direction.NORTH.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 3, 5, 7, 4, 6, 7);
            }
            if ($$9.hasOpening[Direction.WEST.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 0, 5, 3, 0, 6, 4);
            }
            if ($$10.hasOpening[Direction.SOUTH.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 11, 5, 0, 12, 6, 0);
            }
            if ($$10.hasOpening[Direction.NORTH.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 11, 5, 7, 12, 6, 7);
            }
            if ($$10.hasOpening[Direction.EAST.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 15, 5, 3, 15, 6, 4);
            }
        }
    }

    public static class OceanMonumentDoubleYRoom extends OceanMonumentPieces.OceanMonumentPiece {

        public OceanMonumentDoubleYRoom(Direction direction0, OceanMonumentPieces.RoomDefinition oceanMonumentPiecesRoomDefinition1) {
            super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_Y_ROOM, 1, direction0, oceanMonumentPiecesRoomDefinition1, 1, 2, 1);
        }

        public OceanMonumentDoubleYRoom(CompoundTag compoundTag0) {
            super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_Y_ROOM, compoundTag0);
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            if (this.f_228803_.index / 25 > 0) {
                this.m_228859_(worldGenLevel0, boundingBox4, 0, 0, this.f_228803_.hasOpening[Direction.DOWN.get3DDataValue()]);
            }
            OceanMonumentPieces.RoomDefinition $$7 = this.f_228803_.connections[Direction.UP.get3DDataValue()];
            if ($$7.connections[Direction.UP.get3DDataValue()] == null) {
                this.m_228849_(worldGenLevel0, boundingBox4, 1, 8, 1, 6, 8, 6, f_228804_);
            }
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 4, 0, 0, 4, 7, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 7, 4, 0, 7, 4, 7, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 4, 0, 6, 4, 0, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 4, 7, 6, 4, 7, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 2, 4, 1, 2, 4, 2, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 4, 2, 1, 4, 2, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 4, 1, 5, 4, 2, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 6, 4, 2, 6, 4, 2, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 2, 4, 5, 2, 4, 6, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 4, 5, 1, 4, 5, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 4, 5, 5, 4, 6, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 6, 4, 5, 6, 4, 5, f_228805_, f_228805_, false);
            OceanMonumentPieces.RoomDefinition $$8 = this.f_228803_;
            for (int $$9 = 1; $$9 <= 5; $$9 += 4) {
                int $$10 = 0;
                if ($$8.hasOpening[Direction.SOUTH.get3DDataValue()]) {
                    this.m_73441_(worldGenLevel0, boundingBox4, 2, $$9, $$10, 2, $$9 + 2, $$10, f_228805_, f_228805_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, 5, $$9, $$10, 5, $$9 + 2, $$10, f_228805_, f_228805_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, 3, $$9 + 2, $$10, 4, $$9 + 2, $$10, f_228805_, f_228805_, false);
                } else {
                    this.m_73441_(worldGenLevel0, boundingBox4, 0, $$9, $$10, 7, $$9 + 2, $$10, f_228805_, f_228805_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, 0, $$9 + 1, $$10, 7, $$9 + 1, $$10, f_228804_, f_228804_, false);
                }
                int var13 = 7;
                if ($$8.hasOpening[Direction.NORTH.get3DDataValue()]) {
                    this.m_73441_(worldGenLevel0, boundingBox4, 2, $$9, var13, 2, $$9 + 2, var13, f_228805_, f_228805_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, 5, $$9, var13, 5, $$9 + 2, var13, f_228805_, f_228805_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, 3, $$9 + 2, var13, 4, $$9 + 2, var13, f_228805_, f_228805_, false);
                } else {
                    this.m_73441_(worldGenLevel0, boundingBox4, 0, $$9, var13, 7, $$9 + 2, var13, f_228805_, f_228805_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, 0, $$9 + 1, var13, 7, $$9 + 1, var13, f_228804_, f_228804_, false);
                }
                int $$11 = 0;
                if ($$8.hasOpening[Direction.WEST.get3DDataValue()]) {
                    this.m_73441_(worldGenLevel0, boundingBox4, $$11, $$9, 2, $$11, $$9 + 2, 2, f_228805_, f_228805_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, $$11, $$9, 5, $$11, $$9 + 2, 5, f_228805_, f_228805_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, $$11, $$9 + 2, 3, $$11, $$9 + 2, 4, f_228805_, f_228805_, false);
                } else {
                    this.m_73441_(worldGenLevel0, boundingBox4, $$11, $$9, 0, $$11, $$9 + 2, 7, f_228805_, f_228805_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, $$11, $$9 + 1, 0, $$11, $$9 + 1, 7, f_228804_, f_228804_, false);
                }
                int var14 = 7;
                if ($$8.hasOpening[Direction.EAST.get3DDataValue()]) {
                    this.m_73441_(worldGenLevel0, boundingBox4, var14, $$9, 2, var14, $$9 + 2, 2, f_228805_, f_228805_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, var14, $$9, 5, var14, $$9 + 2, 5, f_228805_, f_228805_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, var14, $$9 + 2, 3, var14, $$9 + 2, 4, f_228805_, f_228805_, false);
                } else {
                    this.m_73441_(worldGenLevel0, boundingBox4, var14, $$9, 0, var14, $$9 + 2, 7, f_228805_, f_228805_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, var14, $$9 + 1, 0, var14, $$9 + 1, 7, f_228804_, f_228804_, false);
                }
                $$8 = $$7;
            }
        }
    }

    public static class OceanMonumentDoubleYZRoom extends OceanMonumentPieces.OceanMonumentPiece {

        public OceanMonumentDoubleYZRoom(Direction direction0, OceanMonumentPieces.RoomDefinition oceanMonumentPiecesRoomDefinition1) {
            super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_YZ_ROOM, 1, direction0, oceanMonumentPiecesRoomDefinition1, 1, 2, 2);
        }

        public OceanMonumentDoubleYZRoom(CompoundTag compoundTag0) {
            super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_YZ_ROOM, compoundTag0);
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            OceanMonumentPieces.RoomDefinition $$7 = this.f_228803_.connections[Direction.NORTH.get3DDataValue()];
            OceanMonumentPieces.RoomDefinition $$8 = this.f_228803_;
            OceanMonumentPieces.RoomDefinition $$9 = $$7.connections[Direction.UP.get3DDataValue()];
            OceanMonumentPieces.RoomDefinition $$10 = $$8.connections[Direction.UP.get3DDataValue()];
            if (this.f_228803_.index / 25 > 0) {
                this.m_228859_(worldGenLevel0, boundingBox4, 0, 8, $$7.hasOpening[Direction.DOWN.get3DDataValue()]);
                this.m_228859_(worldGenLevel0, boundingBox4, 0, 0, $$8.hasOpening[Direction.DOWN.get3DDataValue()]);
            }
            if ($$10.connections[Direction.UP.get3DDataValue()] == null) {
                this.m_228849_(worldGenLevel0, boundingBox4, 1, 8, 1, 6, 8, 7, f_228804_);
            }
            if ($$9.connections[Direction.UP.get3DDataValue()] == null) {
                this.m_228849_(worldGenLevel0, boundingBox4, 1, 8, 8, 6, 8, 14, f_228804_);
            }
            for (int $$11 = 1; $$11 <= 7; $$11++) {
                BlockState $$12 = f_228805_;
                if ($$11 == 2 || $$11 == 6) {
                    $$12 = f_228804_;
                }
                this.m_73441_(worldGenLevel0, boundingBox4, 0, $$11, 0, 0, $$11, 15, $$12, $$12, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 7, $$11, 0, 7, $$11, 15, $$12, $$12, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 1, $$11, 0, 6, $$11, 0, $$12, $$12, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 1, $$11, 15, 6, $$11, 15, $$12, $$12, false);
            }
            for (int $$13 = 1; $$13 <= 7; $$13++) {
                BlockState $$14 = f_228806_;
                if ($$13 == 2 || $$13 == 6) {
                    $$14 = f_228808_;
                }
                this.m_73441_(worldGenLevel0, boundingBox4, 3, $$13, 7, 4, $$13, 8, $$14, $$14, false);
            }
            if ($$8.hasOpening[Direction.SOUTH.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 3, 1, 0, 4, 2, 0);
            }
            if ($$8.hasOpening[Direction.EAST.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 7, 1, 3, 7, 2, 4);
            }
            if ($$8.hasOpening[Direction.WEST.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 0, 1, 3, 0, 2, 4);
            }
            if ($$7.hasOpening[Direction.NORTH.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 3, 1, 15, 4, 2, 15);
            }
            if ($$7.hasOpening[Direction.WEST.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 0, 1, 11, 0, 2, 12);
            }
            if ($$7.hasOpening[Direction.EAST.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 7, 1, 11, 7, 2, 12);
            }
            if ($$10.hasOpening[Direction.SOUTH.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 3, 5, 0, 4, 6, 0);
            }
            if ($$10.hasOpening[Direction.EAST.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 7, 5, 3, 7, 6, 4);
                this.m_73441_(worldGenLevel0, boundingBox4, 5, 4, 2, 6, 4, 5, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 6, 1, 2, 6, 3, 2, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 6, 1, 5, 6, 3, 5, f_228805_, f_228805_, false);
            }
            if ($$10.hasOpening[Direction.WEST.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 0, 5, 3, 0, 6, 4);
                this.m_73441_(worldGenLevel0, boundingBox4, 1, 4, 2, 2, 4, 5, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 1, 1, 2, 1, 3, 2, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 1, 1, 5, 1, 3, 5, f_228805_, f_228805_, false);
            }
            if ($$9.hasOpening[Direction.NORTH.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 3, 5, 15, 4, 6, 15);
            }
            if ($$9.hasOpening[Direction.WEST.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 0, 5, 11, 0, 6, 12);
                this.m_73441_(worldGenLevel0, boundingBox4, 1, 4, 10, 2, 4, 13, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 1, 1, 10, 1, 3, 10, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 1, 1, 13, 1, 3, 13, f_228805_, f_228805_, false);
            }
            if ($$9.hasOpening[Direction.EAST.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 7, 5, 11, 7, 6, 12);
                this.m_73441_(worldGenLevel0, boundingBox4, 5, 4, 10, 6, 4, 13, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 6, 1, 10, 6, 3, 10, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 6, 1, 13, 6, 3, 13, f_228805_, f_228805_, false);
            }
        }
    }

    public static class OceanMonumentDoubleZRoom extends OceanMonumentPieces.OceanMonumentPiece {

        public OceanMonumentDoubleZRoom(Direction direction0, OceanMonumentPieces.RoomDefinition oceanMonumentPiecesRoomDefinition1) {
            super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_Z_ROOM, 1, direction0, oceanMonumentPiecesRoomDefinition1, 1, 1, 2);
        }

        public OceanMonumentDoubleZRoom(CompoundTag compoundTag0) {
            super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_Z_ROOM, compoundTag0);
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            OceanMonumentPieces.RoomDefinition $$7 = this.f_228803_.connections[Direction.NORTH.get3DDataValue()];
            OceanMonumentPieces.RoomDefinition $$8 = this.f_228803_;
            if (this.f_228803_.index / 25 > 0) {
                this.m_228859_(worldGenLevel0, boundingBox4, 0, 8, $$7.hasOpening[Direction.DOWN.get3DDataValue()]);
                this.m_228859_(worldGenLevel0, boundingBox4, 0, 0, $$8.hasOpening[Direction.DOWN.get3DDataValue()]);
            }
            if ($$8.connections[Direction.UP.get3DDataValue()] == null) {
                this.m_228849_(worldGenLevel0, boundingBox4, 1, 4, 1, 6, 4, 7, f_228804_);
            }
            if ($$7.connections[Direction.UP.get3DDataValue()] == null) {
                this.m_228849_(worldGenLevel0, boundingBox4, 1, 4, 8, 6, 4, 14, f_228804_);
            }
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 3, 0, 0, 3, 15, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 7, 3, 0, 7, 3, 15, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 3, 0, 7, 3, 0, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 3, 15, 6, 3, 15, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 0, 0, 2, 15, f_228804_, f_228804_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 7, 2, 0, 7, 2, 15, f_228804_, f_228804_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 2, 0, 7, 2, 0, f_228804_, f_228804_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 2, 15, 6, 2, 15, f_228804_, f_228804_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 1, 0, 0, 1, 15, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 7, 1, 0, 7, 1, 15, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 1, 0, 7, 1, 0, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 1, 15, 6, 1, 15, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 1, 1, 1, 1, 2, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 6, 1, 1, 6, 1, 2, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 3, 1, 1, 3, 2, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 6, 3, 1, 6, 3, 2, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 1, 13, 1, 1, 14, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 6, 1, 13, 6, 1, 14, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 3, 13, 1, 3, 14, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 6, 3, 13, 6, 3, 14, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 2, 1, 6, 2, 3, 6, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 1, 6, 5, 3, 6, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 2, 1, 9, 2, 3, 9, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 1, 9, 5, 3, 9, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 3, 2, 6, 4, 2, 6, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 3, 2, 9, 4, 2, 9, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 2, 2, 7, 2, 2, 8, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 2, 7, 5, 2, 8, f_228805_, f_228805_, false);
            this.m_73434_(worldGenLevel0, f_228808_, 2, 2, 5, boundingBox4);
            this.m_73434_(worldGenLevel0, f_228808_, 5, 2, 5, boundingBox4);
            this.m_73434_(worldGenLevel0, f_228808_, 2, 2, 10, boundingBox4);
            this.m_73434_(worldGenLevel0, f_228808_, 5, 2, 10, boundingBox4);
            this.m_73434_(worldGenLevel0, f_228805_, 2, 3, 5, boundingBox4);
            this.m_73434_(worldGenLevel0, f_228805_, 5, 3, 5, boundingBox4);
            this.m_73434_(worldGenLevel0, f_228805_, 2, 3, 10, boundingBox4);
            this.m_73434_(worldGenLevel0, f_228805_, 5, 3, 10, boundingBox4);
            if ($$8.hasOpening[Direction.SOUTH.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 3, 1, 0, 4, 2, 0);
            }
            if ($$8.hasOpening[Direction.EAST.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 7, 1, 3, 7, 2, 4);
            }
            if ($$8.hasOpening[Direction.WEST.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 0, 1, 3, 0, 2, 4);
            }
            if ($$7.hasOpening[Direction.NORTH.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 3, 1, 15, 4, 2, 15);
            }
            if ($$7.hasOpening[Direction.WEST.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 0, 1, 11, 0, 2, 12);
            }
            if ($$7.hasOpening[Direction.EAST.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 7, 1, 11, 7, 2, 12);
            }
        }
    }

    public static class OceanMonumentEntryRoom extends OceanMonumentPieces.OceanMonumentPiece {

        public OceanMonumentEntryRoom(Direction direction0, OceanMonumentPieces.RoomDefinition oceanMonumentPiecesRoomDefinition1) {
            super(StructurePieceType.OCEAN_MONUMENT_ENTRY_ROOM, 1, direction0, oceanMonumentPiecesRoomDefinition1, 1, 1, 1);
        }

        public OceanMonumentEntryRoom(CompoundTag compoundTag0) {
            super(StructurePieceType.OCEAN_MONUMENT_ENTRY_ROOM, compoundTag0);
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 3, 0, 2, 3, 7, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 3, 0, 7, 3, 7, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 0, 1, 2, 7, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 6, 2, 0, 7, 2, 7, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 1, 0, 0, 1, 7, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 7, 1, 0, 7, 1, 7, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 1, 7, 7, 3, 7, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 1, 0, 2, 3, 0, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 1, 0, 6, 3, 0, f_228805_, f_228805_, false);
            if (this.f_228803_.hasOpening[Direction.NORTH.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 3, 1, 7, 4, 2, 7);
            }
            if (this.f_228803_.hasOpening[Direction.WEST.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 0, 1, 3, 1, 2, 4);
            }
            if (this.f_228803_.hasOpening[Direction.EAST.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 6, 1, 3, 7, 2, 4);
            }
        }
    }

    public static class OceanMonumentPenthouse extends OceanMonumentPieces.OceanMonumentPiece {

        public OceanMonumentPenthouse(Direction direction0, BoundingBox boundingBox1) {
            super(StructurePieceType.OCEAN_MONUMENT_PENTHOUSE, direction0, 1, boundingBox1);
        }

        public OceanMonumentPenthouse(CompoundTag compoundTag0) {
            super(StructurePieceType.OCEAN_MONUMENT_PENTHOUSE, compoundTag0);
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            this.m_73441_(worldGenLevel0, boundingBox4, 2, -1, 2, 11, -1, 11, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, -1, 0, 1, -1, 11, f_228804_, f_228804_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 12, -1, 0, 13, -1, 11, f_228804_, f_228804_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 2, -1, 0, 11, -1, 1, f_228804_, f_228804_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 2, -1, 12, 11, -1, 13, f_228804_, f_228804_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 0, 0, 0, 0, 13, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 13, 0, 0, 13, 0, 13, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 0, 0, 12, 0, 0, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 0, 13, 12, 0, 13, f_228805_, f_228805_, false);
            for (int $$7 = 2; $$7 <= 11; $$7 += 3) {
                this.m_73434_(worldGenLevel0, f_228808_, 0, 0, $$7, boundingBox4);
                this.m_73434_(worldGenLevel0, f_228808_, 13, 0, $$7, boundingBox4);
                this.m_73434_(worldGenLevel0, f_228808_, $$7, 0, 0, boundingBox4);
            }
            this.m_73441_(worldGenLevel0, boundingBox4, 2, 0, 3, 4, 0, 9, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 9, 0, 3, 11, 0, 9, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 4, 0, 9, 9, 0, 11, f_228805_, f_228805_, false);
            this.m_73434_(worldGenLevel0, f_228805_, 5, 0, 8, boundingBox4);
            this.m_73434_(worldGenLevel0, f_228805_, 8, 0, 8, boundingBox4);
            this.m_73434_(worldGenLevel0, f_228805_, 10, 0, 10, boundingBox4);
            this.m_73434_(worldGenLevel0, f_228805_, 3, 0, 10, boundingBox4);
            this.m_73441_(worldGenLevel0, boundingBox4, 3, 0, 3, 3, 0, 7, f_228806_, f_228806_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 10, 0, 3, 10, 0, 7, f_228806_, f_228806_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 6, 0, 10, 7, 0, 10, f_228806_, f_228806_, false);
            int $$8 = 3;
            for (int $$9 = 0; $$9 < 2; $$9++) {
                for (int $$10 = 2; $$10 <= 8; $$10 += 3) {
                    this.m_73441_(worldGenLevel0, boundingBox4, $$8, 0, $$10, $$8, 2, $$10, f_228805_, f_228805_, false);
                }
                $$8 = 10;
            }
            this.m_73441_(worldGenLevel0, boundingBox4, 5, 0, 10, 5, 2, 10, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 8, 0, 10, 8, 2, 10, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 6, -1, 7, 7, -1, 8, f_228806_, f_228806_, false);
            this.m_228880_(worldGenLevel0, boundingBox4, 6, -1, 3, 7, -1, 4);
            this.m_247483_(worldGenLevel0, boundingBox4, 6, 1, 6);
        }
    }

    protected abstract static class OceanMonumentPiece extends StructurePiece {

        protected static final BlockState BASE_GRAY = Blocks.PRISMARINE.defaultBlockState();

        protected static final BlockState BASE_LIGHT = Blocks.PRISMARINE_BRICKS.defaultBlockState();

        protected static final BlockState BASE_BLACK = Blocks.DARK_PRISMARINE.defaultBlockState();

        protected static final BlockState DOT_DECO_DATA = BASE_LIGHT;

        protected static final BlockState LAMP_BLOCK = Blocks.SEA_LANTERN.defaultBlockState();

        protected static final boolean DO_FILL = true;

        protected static final BlockState FILL_BLOCK = Blocks.WATER.defaultBlockState();

        protected static final Set<Block> FILL_KEEP = ImmutableSet.builder().add(Blocks.ICE).add(Blocks.PACKED_ICE).add(Blocks.BLUE_ICE).add(FILL_BLOCK.m_60734_()).build();

        protected static final int GRIDROOM_WIDTH = 8;

        protected static final int GRIDROOM_DEPTH = 8;

        protected static final int GRIDROOM_HEIGHT = 4;

        protected static final int GRID_WIDTH = 5;

        protected static final int GRID_DEPTH = 5;

        protected static final int GRID_HEIGHT = 3;

        protected static final int GRID_FLOOR_COUNT = 25;

        protected static final int GRID_SIZE = 75;

        protected static final int GRIDROOM_SOURCE_INDEX = getRoomIndex(2, 0, 0);

        protected static final int GRIDROOM_TOP_CONNECT_INDEX = getRoomIndex(2, 2, 0);

        protected static final int GRIDROOM_LEFTWING_CONNECT_INDEX = getRoomIndex(0, 1, 0);

        protected static final int GRIDROOM_RIGHTWING_CONNECT_INDEX = getRoomIndex(4, 1, 0);

        protected static final int LEFTWING_INDEX = 1001;

        protected static final int RIGHTWING_INDEX = 1002;

        protected static final int PENTHOUSE_INDEX = 1003;

        protected OceanMonumentPieces.RoomDefinition roomDefinition;

        protected static int getRoomIndex(int int0, int int1, int int2) {
            return int1 * 25 + int2 * 5 + int0;
        }

        public OceanMonumentPiece(StructurePieceType structurePieceType0, Direction direction1, int int2, BoundingBox boundingBox3) {
            super(structurePieceType0, int2, boundingBox3);
            this.m_73519_(direction1);
        }

        protected OceanMonumentPiece(StructurePieceType structurePieceType0, int int1, Direction direction2, OceanMonumentPieces.RoomDefinition oceanMonumentPiecesRoomDefinition3, int int4, int int5, int int6) {
            super(structurePieceType0, int1, makeBoundingBox(direction2, oceanMonumentPiecesRoomDefinition3, int4, int5, int6));
            this.m_73519_(direction2);
            this.roomDefinition = oceanMonumentPiecesRoomDefinition3;
        }

        private static BoundingBox makeBoundingBox(Direction direction0, OceanMonumentPieces.RoomDefinition oceanMonumentPiecesRoomDefinition1, int int2, int int3, int int4) {
            int $$5 = oceanMonumentPiecesRoomDefinition1.index;
            int $$6 = $$5 % 5;
            int $$7 = $$5 / 5 % 5;
            int $$8 = $$5 / 25;
            BoundingBox $$9 = m_163541_(0, 0, 0, direction0, int2 * 8, int3 * 4, int4 * 8);
            switch(direction0) {
                case NORTH:
                    $$9.move($$6 * 8, $$8 * 4, -($$7 + int4) * 8 + 1);
                    break;
                case SOUTH:
                    $$9.move($$6 * 8, $$8 * 4, $$7 * 8);
                    break;
                case WEST:
                    $$9.move(-($$7 + int4) * 8 + 1, $$8 * 4, $$6 * 8);
                    break;
                case EAST:
                default:
                    $$9.move($$7 * 8, $$8 * 4, $$6 * 8);
            }
            return $$9;
        }

        public OceanMonumentPiece(StructurePieceType structurePieceType0, CompoundTag compoundTag1) {
            super(structurePieceType0, compoundTag1);
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1) {
        }

        protected void generateWaterBox(WorldGenLevel worldGenLevel0, BoundingBox boundingBox1, int int2, int int3, int int4, int int5, int int6, int int7) {
            for (int $$8 = int3; $$8 <= int6; $$8++) {
                for (int $$9 = int2; $$9 <= int5; $$9++) {
                    for (int $$10 = int4; $$10 <= int7; $$10++) {
                        BlockState $$11 = this.m_73398_(worldGenLevel0, $$9, $$8, $$10, boundingBox1);
                        if (!FILL_KEEP.contains($$11.m_60734_())) {
                            if (this.m_73544_($$8) >= worldGenLevel0.m_5736_() && $$11 != FILL_BLOCK) {
                                this.m_73434_(worldGenLevel0, Blocks.AIR.defaultBlockState(), $$9, $$8, $$10, boundingBox1);
                            } else {
                                this.m_73434_(worldGenLevel0, FILL_BLOCK, $$9, $$8, $$10, boundingBox1);
                            }
                        }
                    }
                }
            }
        }

        protected void generateDefaultFloor(WorldGenLevel worldGenLevel0, BoundingBox boundingBox1, int int2, int int3, boolean boolean4) {
            if (boolean4) {
                this.m_73441_(worldGenLevel0, boundingBox1, int2 + 0, 0, int3 + 0, int2 + 2, 0, int3 + 8 - 1, BASE_GRAY, BASE_GRAY, false);
                this.m_73441_(worldGenLevel0, boundingBox1, int2 + 5, 0, int3 + 0, int2 + 8 - 1, 0, int3 + 8 - 1, BASE_GRAY, BASE_GRAY, false);
                this.m_73441_(worldGenLevel0, boundingBox1, int2 + 3, 0, int3 + 0, int2 + 4, 0, int3 + 2, BASE_GRAY, BASE_GRAY, false);
                this.m_73441_(worldGenLevel0, boundingBox1, int2 + 3, 0, int3 + 5, int2 + 4, 0, int3 + 8 - 1, BASE_GRAY, BASE_GRAY, false);
                this.m_73441_(worldGenLevel0, boundingBox1, int2 + 3, 0, int3 + 2, int2 + 4, 0, int3 + 2, BASE_LIGHT, BASE_LIGHT, false);
                this.m_73441_(worldGenLevel0, boundingBox1, int2 + 3, 0, int3 + 5, int2 + 4, 0, int3 + 5, BASE_LIGHT, BASE_LIGHT, false);
                this.m_73441_(worldGenLevel0, boundingBox1, int2 + 2, 0, int3 + 3, int2 + 2, 0, int3 + 4, BASE_LIGHT, BASE_LIGHT, false);
                this.m_73441_(worldGenLevel0, boundingBox1, int2 + 5, 0, int3 + 3, int2 + 5, 0, int3 + 4, BASE_LIGHT, BASE_LIGHT, false);
            } else {
                this.m_73441_(worldGenLevel0, boundingBox1, int2 + 0, 0, int3 + 0, int2 + 8 - 1, 0, int3 + 8 - 1, BASE_GRAY, BASE_GRAY, false);
            }
        }

        protected void generateBoxOnFillOnly(WorldGenLevel worldGenLevel0, BoundingBox boundingBox1, int int2, int int3, int int4, int int5, int int6, int int7, BlockState blockState8) {
            for (int $$9 = int3; $$9 <= int6; $$9++) {
                for (int $$10 = int2; $$10 <= int5; $$10++) {
                    for (int $$11 = int4; $$11 <= int7; $$11++) {
                        if (this.m_73398_(worldGenLevel0, $$10, $$9, $$11, boundingBox1) == FILL_BLOCK) {
                            this.m_73434_(worldGenLevel0, blockState8, $$10, $$9, $$11, boundingBox1);
                        }
                    }
                }
            }
        }

        protected boolean chunkIntersects(BoundingBox boundingBox0, int int1, int int2, int int3, int int4) {
            int $$5 = this.m_73392_(int1, int2);
            int $$6 = this.m_73525_(int1, int2);
            int $$7 = this.m_73392_(int3, int4);
            int $$8 = this.m_73525_(int3, int4);
            return boundingBox0.intersects(Math.min($$5, $$7), Math.min($$6, $$8), Math.max($$5, $$7), Math.max($$6, $$8));
        }

        protected void spawnElder(WorldGenLevel worldGenLevel0, BoundingBox boundingBox1, int int2, int int3, int int4) {
            BlockPos $$5 = this.m_163582_(int2, int3, int4);
            if (boundingBox1.isInside($$5)) {
                ElderGuardian $$6 = EntityType.ELDER_GUARDIAN.create(worldGenLevel0.m_6018_());
                if ($$6 != null) {
                    $$6.m_5634_($$6.m_21233_());
                    $$6.m_7678_((double) $$5.m_123341_() + 0.5, (double) $$5.m_123342_(), (double) $$5.m_123343_() + 0.5, 0.0F, 0.0F);
                    $$6.m_6518_(worldGenLevel0, worldGenLevel0.m_6436_($$6.m_20183_()), MobSpawnType.STRUCTURE, null, null);
                    worldGenLevel0.m_47205_($$6);
                }
            }
        }
    }

    public static class OceanMonumentSimpleRoom extends OceanMonumentPieces.OceanMonumentPiece {

        private int mainDesign;

        public OceanMonumentSimpleRoom(Direction direction0, OceanMonumentPieces.RoomDefinition oceanMonumentPiecesRoomDefinition1, RandomSource randomSource2) {
            super(StructurePieceType.OCEAN_MONUMENT_SIMPLE_ROOM, 1, direction0, oceanMonumentPiecesRoomDefinition1, 1, 1, 1);
            this.mainDesign = randomSource2.nextInt(3);
        }

        public OceanMonumentSimpleRoom(CompoundTag compoundTag0) {
            super(StructurePieceType.OCEAN_MONUMENT_SIMPLE_ROOM, compoundTag0);
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            if (this.f_228803_.index / 25 > 0) {
                this.m_228859_(worldGenLevel0, boundingBox4, 0, 0, this.f_228803_.hasOpening[Direction.DOWN.get3DDataValue()]);
            }
            if (this.f_228803_.connections[Direction.UP.get3DDataValue()] == null) {
                this.m_228849_(worldGenLevel0, boundingBox4, 1, 4, 1, 6, 4, 6, f_228804_);
            }
            boolean $$7 = this.mainDesign != 0 && randomSource3.nextBoolean() && !this.f_228803_.hasOpening[Direction.DOWN.get3DDataValue()] && !this.f_228803_.hasOpening[Direction.UP.get3DDataValue()] && this.f_228803_.countOpenings() > 1;
            if (this.mainDesign == 0) {
                this.m_73441_(worldGenLevel0, boundingBox4, 0, 1, 0, 2, 1, 2, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 0, 3, 0, 2, 3, 2, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 0, 0, 2, 2, f_228804_, f_228804_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 1, 2, 0, 2, 2, 0, f_228804_, f_228804_, false);
                this.m_73434_(worldGenLevel0, f_228808_, 1, 2, 1, boundingBox4);
                this.m_73441_(worldGenLevel0, boundingBox4, 5, 1, 0, 7, 1, 2, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 5, 3, 0, 7, 3, 2, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 7, 2, 0, 7, 2, 2, f_228804_, f_228804_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 5, 2, 0, 6, 2, 0, f_228804_, f_228804_, false);
                this.m_73434_(worldGenLevel0, f_228808_, 6, 2, 1, boundingBox4);
                this.m_73441_(worldGenLevel0, boundingBox4, 0, 1, 5, 2, 1, 7, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 0, 3, 5, 2, 3, 7, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 5, 0, 2, 7, f_228804_, f_228804_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 1, 2, 7, 2, 2, 7, f_228804_, f_228804_, false);
                this.m_73434_(worldGenLevel0, f_228808_, 1, 2, 6, boundingBox4);
                this.m_73441_(worldGenLevel0, boundingBox4, 5, 1, 5, 7, 1, 7, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 5, 3, 5, 7, 3, 7, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 7, 2, 5, 7, 2, 7, f_228804_, f_228804_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 5, 2, 7, 6, 2, 7, f_228804_, f_228804_, false);
                this.m_73434_(worldGenLevel0, f_228808_, 6, 2, 6, boundingBox4);
                if (this.f_228803_.hasOpening[Direction.SOUTH.get3DDataValue()]) {
                    this.m_73441_(worldGenLevel0, boundingBox4, 3, 3, 0, 4, 3, 0, f_228805_, f_228805_, false);
                } else {
                    this.m_73441_(worldGenLevel0, boundingBox4, 3, 3, 0, 4, 3, 1, f_228805_, f_228805_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, 3, 2, 0, 4, 2, 0, f_228804_, f_228804_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, 3, 1, 0, 4, 1, 1, f_228805_, f_228805_, false);
                }
                if (this.f_228803_.hasOpening[Direction.NORTH.get3DDataValue()]) {
                    this.m_73441_(worldGenLevel0, boundingBox4, 3, 3, 7, 4, 3, 7, f_228805_, f_228805_, false);
                } else {
                    this.m_73441_(worldGenLevel0, boundingBox4, 3, 3, 6, 4, 3, 7, f_228805_, f_228805_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, 3, 2, 7, 4, 2, 7, f_228804_, f_228804_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, 3, 1, 6, 4, 1, 7, f_228805_, f_228805_, false);
                }
                if (this.f_228803_.hasOpening[Direction.WEST.get3DDataValue()]) {
                    this.m_73441_(worldGenLevel0, boundingBox4, 0, 3, 3, 0, 3, 4, f_228805_, f_228805_, false);
                } else {
                    this.m_73441_(worldGenLevel0, boundingBox4, 0, 3, 3, 1, 3, 4, f_228805_, f_228805_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 3, 0, 2, 4, f_228804_, f_228804_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, 0, 1, 3, 1, 1, 4, f_228805_, f_228805_, false);
                }
                if (this.f_228803_.hasOpening[Direction.EAST.get3DDataValue()]) {
                    this.m_73441_(worldGenLevel0, boundingBox4, 7, 3, 3, 7, 3, 4, f_228805_, f_228805_, false);
                } else {
                    this.m_73441_(worldGenLevel0, boundingBox4, 6, 3, 3, 7, 3, 4, f_228805_, f_228805_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, 7, 2, 3, 7, 2, 4, f_228804_, f_228804_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, 6, 1, 3, 7, 1, 4, f_228805_, f_228805_, false);
                }
            } else if (this.mainDesign == 1) {
                this.m_73441_(worldGenLevel0, boundingBox4, 2, 1, 2, 2, 3, 2, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 2, 1, 5, 2, 3, 5, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 5, 1, 5, 5, 3, 5, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 5, 1, 2, 5, 3, 2, f_228805_, f_228805_, false);
                this.m_73434_(worldGenLevel0, f_228808_, 2, 2, 2, boundingBox4);
                this.m_73434_(worldGenLevel0, f_228808_, 2, 2, 5, boundingBox4);
                this.m_73434_(worldGenLevel0, f_228808_, 5, 2, 5, boundingBox4);
                this.m_73434_(worldGenLevel0, f_228808_, 5, 2, 2, boundingBox4);
                this.m_73441_(worldGenLevel0, boundingBox4, 0, 1, 0, 1, 3, 0, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 0, 1, 1, 0, 3, 1, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 0, 1, 7, 1, 3, 7, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 0, 1, 6, 0, 3, 6, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 6, 1, 7, 7, 3, 7, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 7, 1, 6, 7, 3, 6, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 6, 1, 0, 7, 3, 0, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 7, 1, 1, 7, 3, 1, f_228805_, f_228805_, false);
                this.m_73434_(worldGenLevel0, f_228804_, 1, 2, 0, boundingBox4);
                this.m_73434_(worldGenLevel0, f_228804_, 0, 2, 1, boundingBox4);
                this.m_73434_(worldGenLevel0, f_228804_, 1, 2, 7, boundingBox4);
                this.m_73434_(worldGenLevel0, f_228804_, 0, 2, 6, boundingBox4);
                this.m_73434_(worldGenLevel0, f_228804_, 6, 2, 7, boundingBox4);
                this.m_73434_(worldGenLevel0, f_228804_, 7, 2, 6, boundingBox4);
                this.m_73434_(worldGenLevel0, f_228804_, 6, 2, 0, boundingBox4);
                this.m_73434_(worldGenLevel0, f_228804_, 7, 2, 1, boundingBox4);
                if (!this.f_228803_.hasOpening[Direction.SOUTH.get3DDataValue()]) {
                    this.m_73441_(worldGenLevel0, boundingBox4, 1, 3, 0, 6, 3, 0, f_228805_, f_228805_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, 1, 2, 0, 6, 2, 0, f_228804_, f_228804_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, 1, 1, 0, 6, 1, 0, f_228805_, f_228805_, false);
                }
                if (!this.f_228803_.hasOpening[Direction.NORTH.get3DDataValue()]) {
                    this.m_73441_(worldGenLevel0, boundingBox4, 1, 3, 7, 6, 3, 7, f_228805_, f_228805_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, 1, 2, 7, 6, 2, 7, f_228804_, f_228804_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, 1, 1, 7, 6, 1, 7, f_228805_, f_228805_, false);
                }
                if (!this.f_228803_.hasOpening[Direction.WEST.get3DDataValue()]) {
                    this.m_73441_(worldGenLevel0, boundingBox4, 0, 3, 1, 0, 3, 6, f_228805_, f_228805_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 1, 0, 2, 6, f_228804_, f_228804_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, 0, 1, 1, 0, 1, 6, f_228805_, f_228805_, false);
                }
                if (!this.f_228803_.hasOpening[Direction.EAST.get3DDataValue()]) {
                    this.m_73441_(worldGenLevel0, boundingBox4, 7, 3, 1, 7, 3, 6, f_228805_, f_228805_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, 7, 2, 1, 7, 2, 6, f_228804_, f_228804_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, 7, 1, 1, 7, 1, 6, f_228805_, f_228805_, false);
                }
            } else if (this.mainDesign == 2) {
                this.m_73441_(worldGenLevel0, boundingBox4, 0, 1, 0, 0, 1, 7, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 7, 1, 0, 7, 1, 7, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 1, 1, 0, 6, 1, 0, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 1, 1, 7, 6, 1, 7, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 0, 0, 2, 7, f_228806_, f_228806_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 7, 2, 0, 7, 2, 7, f_228806_, f_228806_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 1, 2, 0, 6, 2, 0, f_228806_, f_228806_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 1, 2, 7, 6, 2, 7, f_228806_, f_228806_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 0, 3, 0, 0, 3, 7, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 7, 3, 0, 7, 3, 7, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 1, 3, 0, 6, 3, 0, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 1, 3, 7, 6, 3, 7, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 0, 1, 3, 0, 2, 4, f_228806_, f_228806_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 7, 1, 3, 7, 2, 4, f_228806_, f_228806_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 3, 1, 0, 4, 2, 0, f_228806_, f_228806_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 3, 1, 7, 4, 2, 7, f_228806_, f_228806_, false);
                if (this.f_228803_.hasOpening[Direction.SOUTH.get3DDataValue()]) {
                    this.m_228880_(worldGenLevel0, boundingBox4, 3, 1, 0, 4, 2, 0);
                }
                if (this.f_228803_.hasOpening[Direction.NORTH.get3DDataValue()]) {
                    this.m_228880_(worldGenLevel0, boundingBox4, 3, 1, 7, 4, 2, 7);
                }
                if (this.f_228803_.hasOpening[Direction.WEST.get3DDataValue()]) {
                    this.m_228880_(worldGenLevel0, boundingBox4, 0, 1, 3, 0, 2, 4);
                }
                if (this.f_228803_.hasOpening[Direction.EAST.get3DDataValue()]) {
                    this.m_228880_(worldGenLevel0, boundingBox4, 7, 1, 3, 7, 2, 4);
                }
            }
            if ($$7) {
                this.m_73441_(worldGenLevel0, boundingBox4, 3, 1, 3, 4, 1, 4, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 3, 2, 3, 4, 2, 4, f_228804_, f_228804_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 3, 3, 3, 4, 3, 4, f_228805_, f_228805_, false);
            }
        }
    }

    public static class OceanMonumentSimpleTopRoom extends OceanMonumentPieces.OceanMonumentPiece {

        public OceanMonumentSimpleTopRoom(Direction direction0, OceanMonumentPieces.RoomDefinition oceanMonumentPiecesRoomDefinition1) {
            super(StructurePieceType.OCEAN_MONUMENT_SIMPLE_TOP_ROOM, 1, direction0, oceanMonumentPiecesRoomDefinition1, 1, 1, 1);
        }

        public OceanMonumentSimpleTopRoom(CompoundTag compoundTag0) {
            super(StructurePieceType.OCEAN_MONUMENT_SIMPLE_TOP_ROOM, compoundTag0);
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            if (this.f_228803_.index / 25 > 0) {
                this.m_228859_(worldGenLevel0, boundingBox4, 0, 0, this.f_228803_.hasOpening[Direction.DOWN.get3DDataValue()]);
            }
            if (this.f_228803_.connections[Direction.UP.get3DDataValue()] == null) {
                this.m_228849_(worldGenLevel0, boundingBox4, 1, 4, 1, 6, 4, 6, f_228804_);
            }
            for (int $$7 = 1; $$7 <= 6; $$7++) {
                for (int $$8 = 1; $$8 <= 6; $$8++) {
                    if (randomSource3.nextInt(3) != 0) {
                        int $$9 = 2 + (randomSource3.nextInt(4) == 0 ? 0 : 1);
                        BlockState $$10 = Blocks.WET_SPONGE.defaultBlockState();
                        this.m_73441_(worldGenLevel0, boundingBox4, $$7, $$9, $$8, $$7, 3, $$8, $$10, $$10, false);
                    }
                }
            }
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 1, 0, 0, 1, 7, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 7, 1, 0, 7, 1, 7, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 1, 0, 6, 1, 0, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 1, 7, 6, 1, 7, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 2, 0, 0, 2, 7, f_228806_, f_228806_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 7, 2, 0, 7, 2, 7, f_228806_, f_228806_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 2, 0, 6, 2, 0, f_228806_, f_228806_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 2, 7, 6, 2, 7, f_228806_, f_228806_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 3, 0, 0, 3, 7, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 7, 3, 0, 7, 3, 7, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 3, 0, 6, 3, 0, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 1, 3, 7, 6, 3, 7, f_228805_, f_228805_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 0, 1, 3, 0, 2, 4, f_228806_, f_228806_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 7, 1, 3, 7, 2, 4, f_228806_, f_228806_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 3, 1, 0, 4, 2, 0, f_228806_, f_228806_, false);
            this.m_73441_(worldGenLevel0, boundingBox4, 3, 1, 7, 4, 2, 7, f_228806_, f_228806_, false);
            if (this.f_228803_.hasOpening[Direction.SOUTH.get3DDataValue()]) {
                this.m_228880_(worldGenLevel0, boundingBox4, 3, 1, 0, 4, 2, 0);
            }
        }
    }

    public static class OceanMonumentWingRoom extends OceanMonumentPieces.OceanMonumentPiece {

        private int mainDesign;

        public OceanMonumentWingRoom(Direction direction0, BoundingBox boundingBox1, int int2) {
            super(StructurePieceType.OCEAN_MONUMENT_WING_ROOM, direction0, 1, boundingBox1);
            this.mainDesign = int2 & 1;
        }

        public OceanMonumentWingRoom(CompoundTag compoundTag0) {
            super(StructurePieceType.OCEAN_MONUMENT_WING_ROOM, compoundTag0);
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            if (this.mainDesign == 0) {
                for (int $$7 = 0; $$7 < 4; $$7++) {
                    this.m_73441_(worldGenLevel0, boundingBox4, 10 - $$7, 3 - $$7, 20 - $$7, 12 + $$7, 3 - $$7, 20, f_228805_, f_228805_, false);
                }
                this.m_73441_(worldGenLevel0, boundingBox4, 7, 0, 6, 15, 0, 16, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 6, 0, 6, 6, 3, 20, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 16, 0, 6, 16, 3, 20, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 7, 1, 7, 7, 1, 20, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 15, 1, 7, 15, 1, 20, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 7, 1, 6, 9, 3, 6, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 13, 1, 6, 15, 3, 6, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 8, 1, 7, 9, 1, 7, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 13, 1, 7, 14, 1, 7, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 9, 0, 5, 13, 0, 5, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 10, 0, 7, 12, 0, 7, f_228806_, f_228806_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 8, 0, 10, 8, 0, 12, f_228806_, f_228806_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 14, 0, 10, 14, 0, 12, f_228806_, f_228806_, false);
                for (int $$8 = 18; $$8 >= 7; $$8 -= 3) {
                    this.m_73434_(worldGenLevel0, f_228808_, 6, 3, $$8, boundingBox4);
                    this.m_73434_(worldGenLevel0, f_228808_, 16, 3, $$8, boundingBox4);
                }
                this.m_73434_(worldGenLevel0, f_228808_, 10, 0, 10, boundingBox4);
                this.m_73434_(worldGenLevel0, f_228808_, 12, 0, 10, boundingBox4);
                this.m_73434_(worldGenLevel0, f_228808_, 10, 0, 12, boundingBox4);
                this.m_73434_(worldGenLevel0, f_228808_, 12, 0, 12, boundingBox4);
                this.m_73434_(worldGenLevel0, f_228808_, 8, 3, 6, boundingBox4);
                this.m_73434_(worldGenLevel0, f_228808_, 14, 3, 6, boundingBox4);
                this.m_73434_(worldGenLevel0, f_228805_, 4, 2, 4, boundingBox4);
                this.m_73434_(worldGenLevel0, f_228808_, 4, 1, 4, boundingBox4);
                this.m_73434_(worldGenLevel0, f_228805_, 4, 0, 4, boundingBox4);
                this.m_73434_(worldGenLevel0, f_228805_, 18, 2, 4, boundingBox4);
                this.m_73434_(worldGenLevel0, f_228808_, 18, 1, 4, boundingBox4);
                this.m_73434_(worldGenLevel0, f_228805_, 18, 0, 4, boundingBox4);
                this.m_73434_(worldGenLevel0, f_228805_, 4, 2, 18, boundingBox4);
                this.m_73434_(worldGenLevel0, f_228808_, 4, 1, 18, boundingBox4);
                this.m_73434_(worldGenLevel0, f_228805_, 4, 0, 18, boundingBox4);
                this.m_73434_(worldGenLevel0, f_228805_, 18, 2, 18, boundingBox4);
                this.m_73434_(worldGenLevel0, f_228808_, 18, 1, 18, boundingBox4);
                this.m_73434_(worldGenLevel0, f_228805_, 18, 0, 18, boundingBox4);
                this.m_73434_(worldGenLevel0, f_228805_, 9, 7, 20, boundingBox4);
                this.m_73434_(worldGenLevel0, f_228805_, 13, 7, 20, boundingBox4);
                this.m_73441_(worldGenLevel0, boundingBox4, 6, 0, 21, 7, 4, 21, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 15, 0, 21, 16, 4, 21, f_228805_, f_228805_, false);
                this.m_247483_(worldGenLevel0, boundingBox4, 11, 2, 16);
            } else if (this.mainDesign == 1) {
                this.m_73441_(worldGenLevel0, boundingBox4, 9, 3, 18, 13, 3, 20, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 9, 0, 18, 9, 2, 18, f_228805_, f_228805_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 13, 0, 18, 13, 2, 18, f_228805_, f_228805_, false);
                int $$9 = 9;
                int $$10 = 20;
                int $$11 = 5;
                for (int $$12 = 0; $$12 < 2; $$12++) {
                    this.m_73434_(worldGenLevel0, f_228805_, $$9, 6, 20, boundingBox4);
                    this.m_73434_(worldGenLevel0, f_228808_, $$9, 5, 20, boundingBox4);
                    this.m_73434_(worldGenLevel0, f_228805_, $$9, 4, 20, boundingBox4);
                    $$9 = 13;
                }
                this.m_73441_(worldGenLevel0, boundingBox4, 7, 3, 7, 15, 3, 14, f_228805_, f_228805_, false);
                int var14 = 10;
                for (int $$13 = 0; $$13 < 2; $$13++) {
                    this.m_73441_(worldGenLevel0, boundingBox4, var14, 0, 10, var14, 6, 10, f_228805_, f_228805_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, var14, 0, 12, var14, 6, 12, f_228805_, f_228805_, false);
                    this.m_73434_(worldGenLevel0, f_228808_, var14, 0, 10, boundingBox4);
                    this.m_73434_(worldGenLevel0, f_228808_, var14, 0, 12, boundingBox4);
                    this.m_73434_(worldGenLevel0, f_228808_, var14, 4, 10, boundingBox4);
                    this.m_73434_(worldGenLevel0, f_228808_, var14, 4, 12, boundingBox4);
                    var14 = 12;
                }
                var14 = 8;
                for (int $$14 = 0; $$14 < 2; $$14++) {
                    this.m_73441_(worldGenLevel0, boundingBox4, var14, 0, 7, var14, 2, 7, f_228805_, f_228805_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, var14, 0, 14, var14, 2, 14, f_228805_, f_228805_, false);
                    var14 = 14;
                }
                this.m_73441_(worldGenLevel0, boundingBox4, 8, 3, 8, 8, 3, 13, f_228806_, f_228806_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 14, 3, 8, 14, 3, 13, f_228806_, f_228806_, false);
                this.m_247483_(worldGenLevel0, boundingBox4, 11, 5, 13);
            }
        }
    }

    static class RoomDefinition {

        final int index;

        final OceanMonumentPieces.RoomDefinition[] connections = new OceanMonumentPieces.RoomDefinition[6];

        final boolean[] hasOpening = new boolean[6];

        boolean claimed;

        boolean isSource;

        private int scanIndex;

        public RoomDefinition(int int0) {
            this.index = int0;
        }

        public void setConnection(Direction direction0, OceanMonumentPieces.RoomDefinition oceanMonumentPiecesRoomDefinition1) {
            this.connections[direction0.get3DDataValue()] = oceanMonumentPiecesRoomDefinition1;
            oceanMonumentPiecesRoomDefinition1.connections[direction0.getOpposite().get3DDataValue()] = this;
        }

        public void updateOpenings() {
            for (int $$0 = 0; $$0 < 6; $$0++) {
                this.hasOpening[$$0] = this.connections[$$0] != null;
            }
        }

        public boolean findSource(int int0) {
            if (this.isSource) {
                return true;
            } else {
                this.scanIndex = int0;
                for (int $$1 = 0; $$1 < 6; $$1++) {
                    if (this.connections[$$1] != null && this.hasOpening[$$1] && this.connections[$$1].scanIndex != int0 && this.connections[$$1].findSource(int0)) {
                        return true;
                    }
                }
                return false;
            }
        }

        public boolean isSpecial() {
            return this.index >= 75;
        }

        public int countOpenings() {
            int $$0 = 0;
            for (int $$1 = 0; $$1 < 6; $$1++) {
                if (this.hasOpening[$$1]) {
                    $$0++;
                }
            }
            return $$0;
        }
    }
}