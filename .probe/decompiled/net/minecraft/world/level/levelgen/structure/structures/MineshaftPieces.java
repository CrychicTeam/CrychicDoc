package net.minecraft.world.level.levelgen.structure.structures;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.MinecartChest;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.slf4j.Logger;

public class MineshaftPieces {

    static final Logger LOGGER = LogUtils.getLogger();

    private static final int DEFAULT_SHAFT_WIDTH = 3;

    private static final int DEFAULT_SHAFT_HEIGHT = 3;

    private static final int DEFAULT_SHAFT_LENGTH = 5;

    private static final int MAX_PILLAR_HEIGHT = 20;

    private static final int MAX_CHAIN_HEIGHT = 50;

    private static final int MAX_DEPTH = 8;

    public static final int MAGIC_START_Y = 50;

    private static MineshaftPieces.MineShaftPiece createRandomShaftPiece(StructurePieceAccessor structurePieceAccessor0, RandomSource randomSource1, int int2, int int3, int int4, @Nullable Direction direction5, int int6, MineshaftStructure.Type mineshaftStructureType7) {
        int $$8 = randomSource1.nextInt(100);
        if ($$8 >= 80) {
            BoundingBox $$9 = MineshaftPieces.MineShaftCrossing.findCrossing(structurePieceAccessor0, randomSource1, int2, int3, int4, direction5);
            if ($$9 != null) {
                return new MineshaftPieces.MineShaftCrossing(int6, $$9, direction5, mineshaftStructureType7);
            }
        } else if ($$8 >= 70) {
            BoundingBox $$10 = MineshaftPieces.MineShaftStairs.findStairs(structurePieceAccessor0, randomSource1, int2, int3, int4, direction5);
            if ($$10 != null) {
                return new MineshaftPieces.MineShaftStairs(int6, $$10, direction5, mineshaftStructureType7);
            }
        } else {
            BoundingBox $$11 = MineshaftPieces.MineShaftCorridor.findCorridorSize(structurePieceAccessor0, randomSource1, int2, int3, int4, direction5);
            if ($$11 != null) {
                return new MineshaftPieces.MineShaftCorridor(int6, randomSource1, $$11, direction5, mineshaftStructureType7);
            }
        }
        return null;
    }

    static MineshaftPieces.MineShaftPiece generateAndAddPiece(StructurePiece structurePiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2, int int3, int int4, int int5, Direction direction6, int int7) {
        if (int7 > 8) {
            return null;
        } else if (Math.abs(int3 - structurePiece0.getBoundingBox().minX()) <= 80 && Math.abs(int5 - structurePiece0.getBoundingBox().minZ()) <= 80) {
            MineshaftStructure.Type $$8 = ((MineshaftPieces.MineShaftPiece) structurePiece0).type;
            MineshaftPieces.MineShaftPiece $$9 = createRandomShaftPiece(structurePieceAccessor1, randomSource2, int3, int4, int5, direction6, int7 + 1, $$8);
            if ($$9 != null) {
                structurePieceAccessor1.addPiece($$9);
                $$9.m_214092_(structurePiece0, structurePieceAccessor1, randomSource2);
            }
            return $$9;
        } else {
            return null;
        }
    }

    public static class MineShaftCorridor extends MineshaftPieces.MineShaftPiece {

        private final boolean hasRails;

        private final boolean spiderCorridor;

        private boolean hasPlacedSpider;

        private final int numSections;

        public MineShaftCorridor(CompoundTag compoundTag0) {
            super(StructurePieceType.MINE_SHAFT_CORRIDOR, compoundTag0);
            this.hasRails = compoundTag0.getBoolean("hr");
            this.spiderCorridor = compoundTag0.getBoolean("sc");
            this.hasPlacedSpider = compoundTag0.getBoolean("hps");
            this.numSections = compoundTag0.getInt("Num");
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1) {
            super.addAdditionalSaveData(structurePieceSerializationContext0, compoundTag1);
            compoundTag1.putBoolean("hr", this.hasRails);
            compoundTag1.putBoolean("sc", this.spiderCorridor);
            compoundTag1.putBoolean("hps", this.hasPlacedSpider);
            compoundTag1.putInt("Num", this.numSections);
        }

        public MineShaftCorridor(int int0, RandomSource randomSource1, BoundingBox boundingBox2, Direction direction3, MineshaftStructure.Type mineshaftStructureType4) {
            super(StructurePieceType.MINE_SHAFT_CORRIDOR, int0, mineshaftStructureType4, boundingBox2);
            this.m_73519_(direction3);
            this.hasRails = randomSource1.nextInt(3) == 0;
            this.spiderCorridor = !this.hasRails && randomSource1.nextInt(23) == 0;
            if (this.m_73549_().getAxis() == Direction.Axis.Z) {
                this.numSections = boundingBox2.getZSpan() / 5;
            } else {
                this.numSections = boundingBox2.getXSpan() / 5;
            }
        }

        @Nullable
        public static BoundingBox findCorridorSize(StructurePieceAccessor structurePieceAccessor0, RandomSource randomSource1, int int2, int int3, int int4, Direction direction5) {
            for (int $$6 = randomSource1.nextInt(3) + 2; $$6 > 0; $$6--) {
                int $$7 = $$6 * 5;
                BoundingBox $$11 = switch(direction5) {
                    default ->
                        new BoundingBox(0, 0, -($$7 - 1), 2, 2, 0);
                    case SOUTH ->
                        new BoundingBox(0, 0, 0, 2, 2, $$7 - 1);
                    case WEST ->
                        new BoundingBox(-($$7 - 1), 0, 0, 0, 2, 2);
                    case EAST ->
                        new BoundingBox(0, 0, 0, $$7 - 1, 2, 2);
                };
                $$11.move(int2, int3, int4);
                if (structurePieceAccessor0.findCollisionPiece($$11) == null) {
                    return $$11;
                }
            }
            return null;
        }

        @Override
        public void addChildren(StructurePiece structurePiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2) {
            int $$3 = this.m_73548_();
            int $$4 = randomSource2.nextInt(4);
            Direction $$5 = this.m_73549_();
            if ($$5 != null) {
                switch($$5) {
                    case NORTH:
                    default:
                        if ($$4 <= 1) {
                            MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX(), this.f_73383_.minY() - 1 + randomSource2.nextInt(3), this.f_73383_.minZ() - 1, $$5, $$3);
                        } else if ($$4 == 2) {
                            MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() - 1, this.f_73383_.minY() - 1 + randomSource2.nextInt(3), this.f_73383_.minZ(), Direction.WEST, $$3);
                        } else {
                            MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.maxX() + 1, this.f_73383_.minY() - 1 + randomSource2.nextInt(3), this.f_73383_.minZ(), Direction.EAST, $$3);
                        }
                        break;
                    case SOUTH:
                        if ($$4 <= 1) {
                            MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX(), this.f_73383_.minY() - 1 + randomSource2.nextInt(3), this.f_73383_.maxZ() + 1, $$5, $$3);
                        } else if ($$4 == 2) {
                            MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() - 1, this.f_73383_.minY() - 1 + randomSource2.nextInt(3), this.f_73383_.maxZ() - 3, Direction.WEST, $$3);
                        } else {
                            MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.maxX() + 1, this.f_73383_.minY() - 1 + randomSource2.nextInt(3), this.f_73383_.maxZ() - 3, Direction.EAST, $$3);
                        }
                        break;
                    case WEST:
                        if ($$4 <= 1) {
                            MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() - 1, this.f_73383_.minY() - 1 + randomSource2.nextInt(3), this.f_73383_.minZ(), $$5, $$3);
                        } else if ($$4 == 2) {
                            MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX(), this.f_73383_.minY() - 1 + randomSource2.nextInt(3), this.f_73383_.minZ() - 1, Direction.NORTH, $$3);
                        } else {
                            MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX(), this.f_73383_.minY() - 1 + randomSource2.nextInt(3), this.f_73383_.maxZ() + 1, Direction.SOUTH, $$3);
                        }
                        break;
                    case EAST:
                        if ($$4 <= 1) {
                            MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.maxX() + 1, this.f_73383_.minY() - 1 + randomSource2.nextInt(3), this.f_73383_.minZ(), $$5, $$3);
                        } else if ($$4 == 2) {
                            MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.maxX() - 3, this.f_73383_.minY() - 1 + randomSource2.nextInt(3), this.f_73383_.minZ() - 1, Direction.NORTH, $$3);
                        } else {
                            MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.maxX() - 3, this.f_73383_.minY() - 1 + randomSource2.nextInt(3), this.f_73383_.maxZ() + 1, Direction.SOUTH, $$3);
                        }
                }
            }
            if ($$3 < 8) {
                if ($$5 != Direction.NORTH && $$5 != Direction.SOUTH) {
                    for (int $$8 = this.f_73383_.minX() + 3; $$8 + 3 <= this.f_73383_.maxX(); $$8 += 5) {
                        int $$9 = randomSource2.nextInt(5);
                        if ($$9 == 0) {
                            MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, $$8, this.f_73383_.minY(), this.f_73383_.minZ() - 1, Direction.NORTH, $$3 + 1);
                        } else if ($$9 == 1) {
                            MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, $$8, this.f_73383_.minY(), this.f_73383_.maxZ() + 1, Direction.SOUTH, $$3 + 1);
                        }
                    }
                } else {
                    for (int $$6 = this.f_73383_.minZ() + 3; $$6 + 3 <= this.f_73383_.maxZ(); $$6 += 5) {
                        int $$7 = randomSource2.nextInt(5);
                        if ($$7 == 0) {
                            MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() - 1, this.f_73383_.minY(), $$6, Direction.WEST, $$3 + 1);
                        } else if ($$7 == 1) {
                            MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.maxX() + 1, this.f_73383_.minY(), $$6, Direction.EAST, $$3 + 1);
                        }
                    }
                }
            }
        }

        @Override
        protected boolean createChest(WorldGenLevel worldGenLevel0, BoundingBox boundingBox1, RandomSource randomSource2, int int3, int int4, int int5, ResourceLocation resourceLocation6) {
            BlockPos $$7 = this.m_163582_(int3, int4, int5);
            if (boundingBox1.isInside($$7) && worldGenLevel0.m_8055_($$7).m_60795_() && !worldGenLevel0.m_8055_($$7.below()).m_60795_()) {
                BlockState $$8 = (BlockState) Blocks.RAIL.defaultBlockState().m_61124_(RailBlock.SHAPE, randomSource2.nextBoolean() ? RailShape.NORTH_SOUTH : RailShape.EAST_WEST);
                this.m_73434_(worldGenLevel0, $$8, int3, int4, int5, boundingBox1);
                MinecartChest $$9 = new MinecartChest(worldGenLevel0.m_6018_(), (double) $$7.m_123341_() + 0.5, (double) $$7.m_123342_() + 0.5, (double) $$7.m_123343_() + 0.5);
                $$9.m_38236_(resourceLocation6, randomSource2.nextLong());
                worldGenLevel0.m_7967_($$9);
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            if (!this.m_227881_(worldGenLevel0, boundingBox4)) {
                int $$7 = 0;
                int $$8 = 2;
                int $$9 = 0;
                int $$10 = 2;
                int $$11 = this.numSections * 5 - 1;
                BlockState $$12 = this.f_227864_.getPlanksState();
                this.m_73441_(worldGenLevel0, boundingBox4, 0, 0, 0, 2, 1, $$11, f_73382_, f_73382_, false);
                this.m_226788_(worldGenLevel0, boundingBox4, randomSource3, 0.8F, 0, 2, 0, 2, 2, $$11, f_73382_, f_73382_, false, false);
                if (this.spiderCorridor) {
                    this.m_226788_(worldGenLevel0, boundingBox4, randomSource3, 0.6F, 0, 0, 0, 2, 1, $$11, Blocks.COBWEB.defaultBlockState(), f_73382_, false, true);
                }
                for (int $$13 = 0; $$13 < this.numSections; $$13++) {
                    int $$14 = 2 + $$13 * 5;
                    this.placeSupport(worldGenLevel0, boundingBox4, 0, 0, $$14, 2, 2, randomSource3);
                    this.maybePlaceCobWeb(worldGenLevel0, boundingBox4, randomSource3, 0.1F, 0, 2, $$14 - 1);
                    this.maybePlaceCobWeb(worldGenLevel0, boundingBox4, randomSource3, 0.1F, 2, 2, $$14 - 1);
                    this.maybePlaceCobWeb(worldGenLevel0, boundingBox4, randomSource3, 0.1F, 0, 2, $$14 + 1);
                    this.maybePlaceCobWeb(worldGenLevel0, boundingBox4, randomSource3, 0.1F, 2, 2, $$14 + 1);
                    this.maybePlaceCobWeb(worldGenLevel0, boundingBox4, randomSource3, 0.05F, 0, 2, $$14 - 2);
                    this.maybePlaceCobWeb(worldGenLevel0, boundingBox4, randomSource3, 0.05F, 2, 2, $$14 - 2);
                    this.maybePlaceCobWeb(worldGenLevel0, boundingBox4, randomSource3, 0.05F, 0, 2, $$14 + 2);
                    this.maybePlaceCobWeb(worldGenLevel0, boundingBox4, randomSource3, 0.05F, 2, 2, $$14 + 2);
                    if (randomSource3.nextInt(100) == 0) {
                        this.createChest(worldGenLevel0, boundingBox4, randomSource3, 2, 0, $$14 - 1, BuiltInLootTables.ABANDONED_MINESHAFT);
                    }
                    if (randomSource3.nextInt(100) == 0) {
                        this.createChest(worldGenLevel0, boundingBox4, randomSource3, 0, 0, $$14 + 1, BuiltInLootTables.ABANDONED_MINESHAFT);
                    }
                    if (this.spiderCorridor && !this.hasPlacedSpider) {
                        int $$15 = 1;
                        int $$16 = $$14 - 1 + randomSource3.nextInt(3);
                        BlockPos $$17 = this.m_163582_(1, 0, $$16);
                        if (boundingBox4.isInside($$17) && this.m_73414_(worldGenLevel0, 1, 0, $$16, boundingBox4)) {
                            this.hasPlacedSpider = true;
                            worldGenLevel0.m_7731_($$17, Blocks.SPAWNER.defaultBlockState(), 2);
                            if (worldGenLevel0.m_7702_($$17) instanceof SpawnerBlockEntity $$19) {
                                $$19.setEntityId(EntityType.CAVE_SPIDER, randomSource3);
                            }
                        }
                    }
                }
                for (int $$20 = 0; $$20 <= 2; $$20++) {
                    for (int $$21 = 0; $$21 <= $$11; $$21++) {
                        this.m_227890_(worldGenLevel0, boundingBox4, $$12, $$20, -1, $$21);
                    }
                }
                int $$22 = 2;
                this.placeDoubleLowerOrUpperSupport(worldGenLevel0, boundingBox4, 0, -1, 2);
                if (this.numSections > 1) {
                    int $$23 = $$11 - 2;
                    this.placeDoubleLowerOrUpperSupport(worldGenLevel0, boundingBox4, 0, -1, $$23);
                }
                if (this.hasRails) {
                    BlockState $$24 = (BlockState) Blocks.RAIL.defaultBlockState().m_61124_(RailBlock.SHAPE, RailShape.NORTH_SOUTH);
                    for (int $$25 = 0; $$25 <= $$11; $$25++) {
                        BlockState $$26 = this.m_73398_(worldGenLevel0, 1, -1, $$25, boundingBox4);
                        if (!$$26.m_60795_() && $$26.m_60804_(worldGenLevel0, this.m_163582_(1, -1, $$25))) {
                            float $$27 = this.m_73414_(worldGenLevel0, 1, 0, $$25, boundingBox4) ? 0.7F : 0.9F;
                            this.m_226803_(worldGenLevel0, boundingBox4, randomSource3, $$27, 1, 0, $$25, $$24);
                        }
                    }
                }
            }
        }

        private void placeDoubleLowerOrUpperSupport(WorldGenLevel worldGenLevel0, BoundingBox boundingBox1, int int2, int int3, int int4) {
            BlockState $$5 = this.f_227864_.getWoodState();
            BlockState $$6 = this.f_227864_.getPlanksState();
            if (this.m_73398_(worldGenLevel0, int2, int3, int4, boundingBox1).m_60713_($$6.m_60734_())) {
                this.fillPillarDownOrChainUp(worldGenLevel0, $$5, int2, int3, int4, boundingBox1);
            }
            if (this.m_73398_(worldGenLevel0, int2 + 2, int3, int4, boundingBox1).m_60713_($$6.m_60734_())) {
                this.fillPillarDownOrChainUp(worldGenLevel0, $$5, int2 + 2, int3, int4, boundingBox1);
            }
        }

        @Override
        protected void fillColumnDown(WorldGenLevel worldGenLevel0, BlockState blockState1, int int2, int int3, int int4, BoundingBox boundingBox5) {
            BlockPos.MutableBlockPos $$6 = this.m_163582_(int2, int3, int4);
            if (boundingBox5.isInside($$6)) {
                int $$7 = $$6.m_123342_();
                while (this.m_163572_(worldGenLevel0.m_8055_($$6)) && $$6.m_123342_() > worldGenLevel0.m_141937_() + 1) {
                    $$6.move(Direction.DOWN);
                }
                if (this.canPlaceColumnOnTopOf(worldGenLevel0, $$6, worldGenLevel0.m_8055_($$6))) {
                    while ($$6.m_123342_() < $$7) {
                        $$6.move(Direction.UP);
                        worldGenLevel0.m_7731_($$6, blockState1, 2);
                    }
                }
            }
        }

        protected void fillPillarDownOrChainUp(WorldGenLevel worldGenLevel0, BlockState blockState1, int int2, int int3, int int4, BoundingBox boundingBox5) {
            BlockPos.MutableBlockPos $$6 = this.m_163582_(int2, int3, int4);
            if (boundingBox5.isInside($$6)) {
                int $$7 = $$6.m_123342_();
                int $$8 = 1;
                boolean $$9 = true;
                for (boolean $$10 = true; $$9 || $$10; $$8++) {
                    if ($$9) {
                        $$6.setY($$7 - $$8);
                        BlockState $$11 = worldGenLevel0.m_8055_($$6);
                        boolean $$12 = this.m_163572_($$11) && !$$11.m_60713_(Blocks.LAVA);
                        if (!$$12 && this.canPlaceColumnOnTopOf(worldGenLevel0, $$6, $$11)) {
                            fillColumnBetween(worldGenLevel0, blockState1, $$6, $$7 - $$8 + 1, $$7);
                            return;
                        }
                        $$9 = $$8 <= 20 && $$12 && $$6.m_123342_() > worldGenLevel0.m_141937_() + 1;
                    }
                    if ($$10) {
                        $$6.setY($$7 + $$8);
                        BlockState $$13 = worldGenLevel0.m_8055_($$6);
                        boolean $$14 = this.m_163572_($$13);
                        if (!$$14 && this.canHangChainBelow(worldGenLevel0, $$6, $$13)) {
                            worldGenLevel0.m_7731_($$6.setY($$7 + 1), this.f_227864_.getFenceState(), 2);
                            fillColumnBetween(worldGenLevel0, Blocks.CHAIN.defaultBlockState(), $$6, $$7 + 2, $$7 + $$8);
                            return;
                        }
                        $$10 = $$8 <= 50 && $$14 && $$6.m_123342_() < worldGenLevel0.m_151558_() - 1;
                    }
                }
            }
        }

        private static void fillColumnBetween(WorldGenLevel worldGenLevel0, BlockState blockState1, BlockPos.MutableBlockPos blockPosMutableBlockPos2, int int3, int int4) {
            for (int $$5 = int3; $$5 < int4; $$5++) {
                worldGenLevel0.m_7731_(blockPosMutableBlockPos2.setY($$5), blockState1, 2);
            }
        }

        private boolean canPlaceColumnOnTopOf(LevelReader levelReader0, BlockPos blockPos1, BlockState blockState2) {
            return blockState2.m_60783_(levelReader0, blockPos1, Direction.UP);
        }

        private boolean canHangChainBelow(LevelReader levelReader0, BlockPos blockPos1, BlockState blockState2) {
            return Block.canSupportCenter(levelReader0, blockPos1, Direction.DOWN) && !(blockState2.m_60734_() instanceof FallingBlock);
        }

        private void placeSupport(WorldGenLevel worldGenLevel0, BoundingBox boundingBox1, int int2, int int3, int int4, int int5, int int6, RandomSource randomSource7) {
            if (this.m_227874_(worldGenLevel0, boundingBox1, int2, int6, int5, int4)) {
                BlockState $$8 = this.f_227864_.getPlanksState();
                BlockState $$9 = this.f_227864_.getFenceState();
                this.m_73441_(worldGenLevel0, boundingBox1, int2, int3, int4, int2, int5 - 1, int4, (BlockState) $$9.m_61124_(FenceBlock.f_52312_, true), f_73382_, false);
                this.m_73441_(worldGenLevel0, boundingBox1, int6, int3, int4, int6, int5 - 1, int4, (BlockState) $$9.m_61124_(FenceBlock.f_52310_, true), f_73382_, false);
                if (randomSource7.nextInt(4) == 0) {
                    this.m_73441_(worldGenLevel0, boundingBox1, int2, int5, int4, int2, int5, int4, $$8, f_73382_, false);
                    this.m_73441_(worldGenLevel0, boundingBox1, int6, int5, int4, int6, int5, int4, $$8, f_73382_, false);
                } else {
                    this.m_73441_(worldGenLevel0, boundingBox1, int2, int5, int4, int6, int5, int4, $$8, f_73382_, false);
                    this.m_226803_(worldGenLevel0, boundingBox1, randomSource7, 0.05F, int2 + 1, int5, int4 - 1, (BlockState) Blocks.WALL_TORCH.defaultBlockState().m_61124_(WallTorchBlock.FACING, Direction.SOUTH));
                    this.m_226803_(worldGenLevel0, boundingBox1, randomSource7, 0.05F, int2 + 1, int5, int4 + 1, (BlockState) Blocks.WALL_TORCH.defaultBlockState().m_61124_(WallTorchBlock.FACING, Direction.NORTH));
                }
            }
        }

        private void maybePlaceCobWeb(WorldGenLevel worldGenLevel0, BoundingBox boundingBox1, RandomSource randomSource2, float float3, int int4, int int5, int int6) {
            if (this.m_73414_(worldGenLevel0, int4, int5, int6, boundingBox1) && randomSource2.nextFloat() < float3 && this.hasSturdyNeighbours(worldGenLevel0, boundingBox1, int4, int5, int6, 2)) {
                this.m_73434_(worldGenLevel0, Blocks.COBWEB.defaultBlockState(), int4, int5, int6, boundingBox1);
            }
        }

        private boolean hasSturdyNeighbours(WorldGenLevel worldGenLevel0, BoundingBox boundingBox1, int int2, int int3, int int4, int int5) {
            BlockPos.MutableBlockPos $$6 = this.m_163582_(int2, int3, int4);
            int $$7 = 0;
            for (Direction $$8 : Direction.values()) {
                $$6.move($$8);
                if (boundingBox1.isInside($$6) && worldGenLevel0.m_8055_($$6).m_60783_(worldGenLevel0, $$6, $$8.getOpposite())) {
                    if (++$$7 >= int5) {
                        return true;
                    }
                }
                $$6.move($$8.getOpposite());
            }
            return false;
        }
    }

    public static class MineShaftCrossing extends MineshaftPieces.MineShaftPiece {

        private final Direction direction;

        private final boolean isTwoFloored;

        public MineShaftCrossing(CompoundTag compoundTag0) {
            super(StructurePieceType.MINE_SHAFT_CROSSING, compoundTag0);
            this.isTwoFloored = compoundTag0.getBoolean("tf");
            this.direction = Direction.from2DDataValue(compoundTag0.getInt("D"));
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1) {
            super.addAdditionalSaveData(structurePieceSerializationContext0, compoundTag1);
            compoundTag1.putBoolean("tf", this.isTwoFloored);
            compoundTag1.putInt("D", this.direction.get2DDataValue());
        }

        public MineShaftCrossing(int int0, BoundingBox boundingBox1, @Nullable Direction direction2, MineshaftStructure.Type mineshaftStructureType3) {
            super(StructurePieceType.MINE_SHAFT_CROSSING, int0, mineshaftStructureType3, boundingBox1);
            this.direction = direction2;
            this.isTwoFloored = boundingBox1.getYSpan() > 3;
        }

        @Nullable
        public static BoundingBox findCrossing(StructurePieceAccessor structurePieceAccessor0, RandomSource randomSource1, int int2, int int3, int int4, Direction direction5) {
            int $$6;
            if (randomSource1.nextInt(4) == 0) {
                $$6 = 6;
            } else {
                $$6 = 2;
            }
            BoundingBox $$11 = switch(direction5) {
                default ->
                    new BoundingBox(-1, 0, -4, 3, $$6, 0);
                case SOUTH ->
                    new BoundingBox(-1, 0, 0, 3, $$6, 4);
                case WEST ->
                    new BoundingBox(-4, 0, -1, 0, $$6, 3);
                case EAST ->
                    new BoundingBox(0, 0, -1, 4, $$6, 3);
            };
            $$11.move(int2, int3, int4);
            return structurePieceAccessor0.findCollisionPiece($$11) != null ? null : $$11;
        }

        @Override
        public void addChildren(StructurePiece structurePiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2) {
            int $$3 = this.m_73548_();
            switch(this.direction) {
                case NORTH:
                default:
                    MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() + 1, this.f_73383_.minY(), this.f_73383_.minZ() - 1, Direction.NORTH, $$3);
                    MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() - 1, this.f_73383_.minY(), this.f_73383_.minZ() + 1, Direction.WEST, $$3);
                    MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.maxX() + 1, this.f_73383_.minY(), this.f_73383_.minZ() + 1, Direction.EAST, $$3);
                    break;
                case SOUTH:
                    MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() + 1, this.f_73383_.minY(), this.f_73383_.maxZ() + 1, Direction.SOUTH, $$3);
                    MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() - 1, this.f_73383_.minY(), this.f_73383_.minZ() + 1, Direction.WEST, $$3);
                    MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.maxX() + 1, this.f_73383_.minY(), this.f_73383_.minZ() + 1, Direction.EAST, $$3);
                    break;
                case WEST:
                    MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() + 1, this.f_73383_.minY(), this.f_73383_.minZ() - 1, Direction.NORTH, $$3);
                    MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() + 1, this.f_73383_.minY(), this.f_73383_.maxZ() + 1, Direction.SOUTH, $$3);
                    MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() - 1, this.f_73383_.minY(), this.f_73383_.minZ() + 1, Direction.WEST, $$3);
                    break;
                case EAST:
                    MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() + 1, this.f_73383_.minY(), this.f_73383_.minZ() - 1, Direction.NORTH, $$3);
                    MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() + 1, this.f_73383_.minY(), this.f_73383_.maxZ() + 1, Direction.SOUTH, $$3);
                    MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.maxX() + 1, this.f_73383_.minY(), this.f_73383_.minZ() + 1, Direction.EAST, $$3);
            }
            if (this.isTwoFloored) {
                if (randomSource2.nextBoolean()) {
                    MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() + 1, this.f_73383_.minY() + 3 + 1, this.f_73383_.minZ() - 1, Direction.NORTH, $$3);
                }
                if (randomSource2.nextBoolean()) {
                    MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() - 1, this.f_73383_.minY() + 3 + 1, this.f_73383_.minZ() + 1, Direction.WEST, $$3);
                }
                if (randomSource2.nextBoolean()) {
                    MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.maxX() + 1, this.f_73383_.minY() + 3 + 1, this.f_73383_.minZ() + 1, Direction.EAST, $$3);
                }
                if (randomSource2.nextBoolean()) {
                    MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() + 1, this.f_73383_.minY() + 3 + 1, this.f_73383_.maxZ() + 1, Direction.SOUTH, $$3);
                }
            }
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            if (!this.m_227881_(worldGenLevel0, boundingBox4)) {
                BlockState $$7 = this.f_227864_.getPlanksState();
                if (this.isTwoFloored) {
                    this.m_73441_(worldGenLevel0, boundingBox4, this.f_73383_.minX() + 1, this.f_73383_.minY(), this.f_73383_.minZ(), this.f_73383_.maxX() - 1, this.f_73383_.minY() + 3 - 1, this.f_73383_.maxZ(), f_73382_, f_73382_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, this.f_73383_.minX(), this.f_73383_.minY(), this.f_73383_.minZ() + 1, this.f_73383_.maxX(), this.f_73383_.minY() + 3 - 1, this.f_73383_.maxZ() - 1, f_73382_, f_73382_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, this.f_73383_.minX() + 1, this.f_73383_.maxY() - 2, this.f_73383_.minZ(), this.f_73383_.maxX() - 1, this.f_73383_.maxY(), this.f_73383_.maxZ(), f_73382_, f_73382_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, this.f_73383_.minX(), this.f_73383_.maxY() - 2, this.f_73383_.minZ() + 1, this.f_73383_.maxX(), this.f_73383_.maxY(), this.f_73383_.maxZ() - 1, f_73382_, f_73382_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, this.f_73383_.minX() + 1, this.f_73383_.minY() + 3, this.f_73383_.minZ() + 1, this.f_73383_.maxX() - 1, this.f_73383_.minY() + 3, this.f_73383_.maxZ() - 1, f_73382_, f_73382_, false);
                } else {
                    this.m_73441_(worldGenLevel0, boundingBox4, this.f_73383_.minX() + 1, this.f_73383_.minY(), this.f_73383_.minZ(), this.f_73383_.maxX() - 1, this.f_73383_.maxY(), this.f_73383_.maxZ(), f_73382_, f_73382_, false);
                    this.m_73441_(worldGenLevel0, boundingBox4, this.f_73383_.minX(), this.f_73383_.minY(), this.f_73383_.minZ() + 1, this.f_73383_.maxX(), this.f_73383_.maxY(), this.f_73383_.maxZ() - 1, f_73382_, f_73382_, false);
                }
                this.placeSupportPillar(worldGenLevel0, boundingBox4, this.f_73383_.minX() + 1, this.f_73383_.minY(), this.f_73383_.minZ() + 1, this.f_73383_.maxY());
                this.placeSupportPillar(worldGenLevel0, boundingBox4, this.f_73383_.minX() + 1, this.f_73383_.minY(), this.f_73383_.maxZ() - 1, this.f_73383_.maxY());
                this.placeSupportPillar(worldGenLevel0, boundingBox4, this.f_73383_.maxX() - 1, this.f_73383_.minY(), this.f_73383_.minZ() + 1, this.f_73383_.maxY());
                this.placeSupportPillar(worldGenLevel0, boundingBox4, this.f_73383_.maxX() - 1, this.f_73383_.minY(), this.f_73383_.maxZ() - 1, this.f_73383_.maxY());
                int $$8 = this.f_73383_.minY() - 1;
                for (int $$9 = this.f_73383_.minX(); $$9 <= this.f_73383_.maxX(); $$9++) {
                    for (int $$10 = this.f_73383_.minZ(); $$10 <= this.f_73383_.maxZ(); $$10++) {
                        this.m_227890_(worldGenLevel0, boundingBox4, $$7, $$9, $$8, $$10);
                    }
                }
            }
        }

        private void placeSupportPillar(WorldGenLevel worldGenLevel0, BoundingBox boundingBox1, int int2, int int3, int int4, int int5) {
            if (!this.m_73398_(worldGenLevel0, int2, int5 + 1, int4, boundingBox1).m_60795_()) {
                this.m_73441_(worldGenLevel0, boundingBox1, int2, int3, int4, int2, int5, int4, this.f_227864_.getPlanksState(), f_73382_, false);
            }
        }
    }

    abstract static class MineShaftPiece extends StructurePiece {

        protected MineshaftStructure.Type type;

        public MineShaftPiece(StructurePieceType structurePieceType0, int int1, MineshaftStructure.Type mineshaftStructureType2, BoundingBox boundingBox3) {
            super(structurePieceType0, int1, boundingBox3);
            this.type = mineshaftStructureType2;
        }

        public MineShaftPiece(StructurePieceType structurePieceType0, CompoundTag compoundTag1) {
            super(structurePieceType0, compoundTag1);
            this.type = MineshaftStructure.Type.byId(compoundTag1.getInt("MST"));
        }

        @Override
        protected boolean canBeReplaced(LevelReader levelReader0, int int1, int int2, int int3, BoundingBox boundingBox4) {
            BlockState $$5 = this.m_73398_(levelReader0, int1, int2, int3, boundingBox4);
            return !$$5.m_60713_(this.type.getPlanksState().m_60734_()) && !$$5.m_60713_(this.type.getWoodState().m_60734_()) && !$$5.m_60713_(this.type.getFenceState().m_60734_()) && !$$5.m_60713_(Blocks.CHAIN);
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1) {
            compoundTag1.putInt("MST", this.type.ordinal());
        }

        protected boolean isSupportingBox(BlockGetter blockGetter0, BoundingBox boundingBox1, int int2, int int3, int int4, int int5) {
            for (int $$6 = int2; $$6 <= int3; $$6++) {
                if (this.m_73398_(blockGetter0, $$6, int4 + 1, int5, boundingBox1).m_60795_()) {
                    return false;
                }
            }
            return true;
        }

        protected boolean isInInvalidLocation(LevelAccessor levelAccessor0, BoundingBox boundingBox1) {
            int $$2 = Math.max(this.f_73383_.minX() - 1, boundingBox1.minX());
            int $$3 = Math.max(this.f_73383_.minY() - 1, boundingBox1.minY());
            int $$4 = Math.max(this.f_73383_.minZ() - 1, boundingBox1.minZ());
            int $$5 = Math.min(this.f_73383_.maxX() + 1, boundingBox1.maxX());
            int $$6 = Math.min(this.f_73383_.maxY() + 1, boundingBox1.maxY());
            int $$7 = Math.min(this.f_73383_.maxZ() + 1, boundingBox1.maxZ());
            BlockPos.MutableBlockPos $$8 = new BlockPos.MutableBlockPos(($$2 + $$5) / 2, ($$3 + $$6) / 2, ($$4 + $$7) / 2);
            if (levelAccessor0.m_204166_($$8).is(BiomeTags.MINESHAFT_BLOCKING)) {
                return true;
            } else {
                for (int $$9 = $$2; $$9 <= $$5; $$9++) {
                    for (int $$10 = $$4; $$10 <= $$7; $$10++) {
                        if (levelAccessor0.m_8055_($$8.set($$9, $$3, $$10)).m_278721_()) {
                            return true;
                        }
                        if (levelAccessor0.m_8055_($$8.set($$9, $$6, $$10)).m_278721_()) {
                            return true;
                        }
                    }
                }
                for (int $$11 = $$2; $$11 <= $$5; $$11++) {
                    for (int $$12 = $$3; $$12 <= $$6; $$12++) {
                        if (levelAccessor0.m_8055_($$8.set($$11, $$12, $$4)).m_278721_()) {
                            return true;
                        }
                        if (levelAccessor0.m_8055_($$8.set($$11, $$12, $$7)).m_278721_()) {
                            return true;
                        }
                    }
                }
                for (int $$13 = $$4; $$13 <= $$7; $$13++) {
                    for (int $$14 = $$3; $$14 <= $$6; $$14++) {
                        if (levelAccessor0.m_8055_($$8.set($$2, $$14, $$13)).m_278721_()) {
                            return true;
                        }
                        if (levelAccessor0.m_8055_($$8.set($$5, $$14, $$13)).m_278721_()) {
                            return true;
                        }
                    }
                }
                return false;
            }
        }

        protected void setPlanksBlock(WorldGenLevel worldGenLevel0, BoundingBox boundingBox1, BlockState blockState2, int int3, int int4, int int5) {
            if (this.m_73414_(worldGenLevel0, int3, int4, int5, boundingBox1)) {
                BlockPos $$6 = this.m_163582_(int3, int4, int5);
                BlockState $$7 = worldGenLevel0.m_8055_($$6);
                if (!$$7.m_60783_(worldGenLevel0, $$6, Direction.UP)) {
                    worldGenLevel0.m_7731_($$6, blockState2, 2);
                }
            }
        }
    }

    public static class MineShaftRoom extends MineshaftPieces.MineShaftPiece {

        private final List<BoundingBox> childEntranceBoxes = Lists.newLinkedList();

        public MineShaftRoom(int int0, RandomSource randomSource1, int int2, int int3, MineshaftStructure.Type mineshaftStructureType4) {
            super(StructurePieceType.MINE_SHAFT_ROOM, int0, mineshaftStructureType4, new BoundingBox(int2, 50, int3, int2 + 7 + randomSource1.nextInt(6), 54 + randomSource1.nextInt(6), int3 + 7 + randomSource1.nextInt(6)));
            this.f_227864_ = mineshaftStructureType4;
        }

        public MineShaftRoom(CompoundTag compoundTag0) {
            super(StructurePieceType.MINE_SHAFT_ROOM, compoundTag0);
            BoundingBox.CODEC.listOf().parse(NbtOps.INSTANCE, compoundTag0.getList("Entrances", 11)).resultOrPartial(MineshaftPieces.LOGGER::error).ifPresent(this.childEntranceBoxes::addAll);
        }

        @Override
        public void addChildren(StructurePiece structurePiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2) {
            int $$3 = this.m_73548_();
            int $$4 = this.f_73383_.getYSpan() - 3 - 1;
            if ($$4 <= 0) {
                $$4 = 1;
            }
            int $$5 = 0;
            while ($$5 < this.f_73383_.getXSpan()) {
                $$5 += randomSource2.nextInt(this.f_73383_.getXSpan());
                if ($$5 + 3 > this.f_73383_.getXSpan()) {
                    break;
                }
                MineshaftPieces.MineShaftPiece $$6 = MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() + $$5, this.f_73383_.minY() + randomSource2.nextInt($$4) + 1, this.f_73383_.minZ() - 1, Direction.NORTH, $$3);
                if ($$6 != null) {
                    BoundingBox $$7 = $$6.m_73547_();
                    this.childEntranceBoxes.add(new BoundingBox($$7.minX(), $$7.minY(), this.f_73383_.minZ(), $$7.maxX(), $$7.maxY(), this.f_73383_.minZ() + 1));
                }
                $$5 += 4;
            }
            $$5 = 0;
            while ($$5 < this.f_73383_.getXSpan()) {
                $$5 += randomSource2.nextInt(this.f_73383_.getXSpan());
                if ($$5 + 3 > this.f_73383_.getXSpan()) {
                    break;
                }
                MineshaftPieces.MineShaftPiece $$8 = MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() + $$5, this.f_73383_.minY() + randomSource2.nextInt($$4) + 1, this.f_73383_.maxZ() + 1, Direction.SOUTH, $$3);
                if ($$8 != null) {
                    BoundingBox $$9 = $$8.m_73547_();
                    this.childEntranceBoxes.add(new BoundingBox($$9.minX(), $$9.minY(), this.f_73383_.maxZ() - 1, $$9.maxX(), $$9.maxY(), this.f_73383_.maxZ()));
                }
                $$5 += 4;
            }
            $$5 = 0;
            while ($$5 < this.f_73383_.getZSpan()) {
                $$5 += randomSource2.nextInt(this.f_73383_.getZSpan());
                if ($$5 + 3 > this.f_73383_.getZSpan()) {
                    break;
                }
                MineshaftPieces.MineShaftPiece $$10 = MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() - 1, this.f_73383_.minY() + randomSource2.nextInt($$4) + 1, this.f_73383_.minZ() + $$5, Direction.WEST, $$3);
                if ($$10 != null) {
                    BoundingBox $$11 = $$10.m_73547_();
                    this.childEntranceBoxes.add(new BoundingBox(this.f_73383_.minX(), $$11.minY(), $$11.minZ(), this.f_73383_.minX() + 1, $$11.maxY(), $$11.maxZ()));
                }
                $$5 += 4;
            }
            $$5 = 0;
            while ($$5 < this.f_73383_.getZSpan()) {
                $$5 += randomSource2.nextInt(this.f_73383_.getZSpan());
                if ($$5 + 3 > this.f_73383_.getZSpan()) {
                    break;
                }
                StructurePiece $$12 = MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.maxX() + 1, this.f_73383_.minY() + randomSource2.nextInt($$4) + 1, this.f_73383_.minZ() + $$5, Direction.EAST, $$3);
                if ($$12 != null) {
                    BoundingBox $$13 = $$12.getBoundingBox();
                    this.childEntranceBoxes.add(new BoundingBox(this.f_73383_.maxX() - 1, $$13.minY(), $$13.minZ(), this.f_73383_.maxX(), $$13.maxY(), $$13.maxZ()));
                }
                $$5 += 4;
            }
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            if (!this.m_227881_(worldGenLevel0, boundingBox4)) {
                this.m_73441_(worldGenLevel0, boundingBox4, this.f_73383_.minX(), this.f_73383_.minY() + 1, this.f_73383_.minZ(), this.f_73383_.maxX(), Math.min(this.f_73383_.minY() + 3, this.f_73383_.maxY()), this.f_73383_.maxZ(), f_73382_, f_73382_, false);
                for (BoundingBox $$7 : this.childEntranceBoxes) {
                    this.m_73441_(worldGenLevel0, boundingBox4, $$7.minX(), $$7.maxY() - 2, $$7.minZ(), $$7.maxX(), $$7.maxY(), $$7.maxZ(), f_73382_, f_73382_, false);
                }
                this.m_73453_(worldGenLevel0, boundingBox4, this.f_73383_.minX(), this.f_73383_.minY() + 4, this.f_73383_.minZ(), this.f_73383_.maxX(), this.f_73383_.maxY(), this.f_73383_.maxZ(), f_73382_, false);
            }
        }

        @Override
        public void move(int int0, int int1, int int2) {
            super.m_6324_(int0, int1, int2);
            for (BoundingBox $$3 : this.childEntranceBoxes) {
                $$3.move(int0, int1, int2);
            }
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1) {
            super.addAdditionalSaveData(structurePieceSerializationContext0, compoundTag1);
            BoundingBox.CODEC.listOf().encodeStart(NbtOps.INSTANCE, this.childEntranceBoxes).resultOrPartial(MineshaftPieces.LOGGER::error).ifPresent(p_227930_ -> compoundTag1.put("Entrances", p_227930_));
        }
    }

    public static class MineShaftStairs extends MineshaftPieces.MineShaftPiece {

        public MineShaftStairs(int int0, BoundingBox boundingBox1, Direction direction2, MineshaftStructure.Type mineshaftStructureType3) {
            super(StructurePieceType.MINE_SHAFT_STAIRS, int0, mineshaftStructureType3, boundingBox1);
            this.m_73519_(direction2);
        }

        public MineShaftStairs(CompoundTag compoundTag0) {
            super(StructurePieceType.MINE_SHAFT_STAIRS, compoundTag0);
        }

        @Nullable
        public static BoundingBox findStairs(StructurePieceAccessor structurePieceAccessor0, RandomSource randomSource1, int int2, int int3, int int4, Direction direction5) {
            BoundingBox $$9 = switch(direction5) {
                default ->
                    new BoundingBox(0, -5, -8, 2, 2, 0);
                case SOUTH ->
                    new BoundingBox(0, -5, 0, 2, 2, 8);
                case WEST ->
                    new BoundingBox(-8, -5, 0, 0, 2, 2);
                case EAST ->
                    new BoundingBox(0, -5, 0, 8, 2, 2);
            };
            $$9.move(int2, int3, int4);
            return structurePieceAccessor0.findCollisionPiece($$9) != null ? null : $$9;
        }

        @Override
        public void addChildren(StructurePiece structurePiece0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2) {
            int $$3 = this.m_73548_();
            Direction $$4 = this.m_73549_();
            if ($$4 != null) {
                switch($$4) {
                    case NORTH:
                    default:
                        MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX(), this.f_73383_.minY(), this.f_73383_.minZ() - 1, Direction.NORTH, $$3);
                        break;
                    case SOUTH:
                        MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX(), this.f_73383_.minY(), this.f_73383_.maxZ() + 1, Direction.SOUTH, $$3);
                        break;
                    case WEST:
                        MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.minX() - 1, this.f_73383_.minY(), this.f_73383_.minZ(), Direction.WEST, $$3);
                        break;
                    case EAST:
                        MineshaftPieces.generateAndAddPiece(structurePiece0, structurePieceAccessor1, randomSource2, this.f_73383_.maxX() + 1, this.f_73383_.minY(), this.f_73383_.minZ(), Direction.EAST, $$3);
                }
            }
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            if (!this.m_227881_(worldGenLevel0, boundingBox4)) {
                this.m_73441_(worldGenLevel0, boundingBox4, 0, 5, 0, 2, 7, 1, f_73382_, f_73382_, false);
                this.m_73441_(worldGenLevel0, boundingBox4, 0, 0, 7, 2, 2, 8, f_73382_, f_73382_, false);
                for (int $$7 = 0; $$7 < 5; $$7++) {
                    this.m_73441_(worldGenLevel0, boundingBox4, 0, 5 - $$7 - ($$7 < 4 ? 1 : 0), 2 + $$7, 2, 7 - $$7, 2 + $$7, f_73382_, f_73382_, false);
                }
            }
        }
    }
}