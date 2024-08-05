package net.mehvahdjukaar.supplementaries.common.worldgen;

import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.blocks.AbstractRopeBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.PulleyBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.RopeBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.TurnTableBlock;
import net.mehvahdjukaar.supplementaries.common.block.tiles.PulleyBlockTile;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModWorldgenRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftPieces;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftStructure;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.jetbrains.annotations.Nullable;

public class MineshaftElevatorPiece extends MineshaftPieces.MineShaftPiece {

    private final Direction direction;

    private final byte floor;

    private final boolean hasChain;

    public MineshaftElevatorPiece(StructurePieceSerializationContext context, CompoundTag compoundTag) {
        super((StructurePieceType) ModWorldgenRegistry.MINESHAFT_ELEVATOR.get(), compoundTag);
        this.direction = Direction.from2DDataValue(compoundTag.getInt("D"));
        this.floor = compoundTag.getByte("F");
        this.hasChain = compoundTag.getBoolean("C");
        this.m_73519_(this.direction);
    }

    public MineshaftElevatorPiece(int depth, BoundingBox boundingBox, @Nullable Direction direction, byte floor, boolean hasChain, MineshaftStructure.Type type) {
        super((StructurePieceType) ModWorldgenRegistry.MINESHAFT_ELEVATOR.get(), depth, type, boundingBox);
        this.direction = direction;
        this.floor = floor;
        this.hasChain = hasChain;
        this.m_73519_(direction);
    }

    @Nullable
    public static MineshaftPieces.MineShaftPiece getElevator(StructurePieceAccessor pieces, RandomSource random, int x, int y, int z, Direction direction, int genDepth, MineshaftStructure.Type type) {
        if (y > 48) {
            return null;
        } else {
            if ((double) random.nextFloat() < (Double) CommonConfigs.Redstone.MINESHAFT_ELEVATOR.get() && (Boolean) CommonConfigs.Redstone.PULLEY_ENABLED.get() && (Boolean) CommonConfigs.Redstone.TURN_TABLE_ENABLED.get()) {
                byte height = 12;
                int floor = random.nextInt(3);
                if (random.nextBoolean() && floor != 2) {
                    floor++;
                }
                int yOffset = floor * 4;
                BoundingBox boundingBox = switch(direction) {
                    case SOUTH ->
                        new BoundingBox(-1, -yOffset, 0, 3, height - yOffset, 4);
                    case WEST ->
                        new BoundingBox(-4, -yOffset, -1, 0, height - yOffset, 3);
                    case EAST ->
                        new BoundingBox(0, -yOffset, -1, 4, height - yOffset, 3);
                    default ->
                        new BoundingBox(-1, -yOffset, -4, 3, height - yOffset, 0);
                };
                boundingBox.move(x, y, z);
                if (pieces.findCollisionPiece(boundingBox) == null) {
                    boolean hasChain = random.nextInt(5) == 0;
                    return new MineshaftElevatorPiece(genDepth, boundingBox, direction, (byte) floor, hasChain, type);
                }
            }
            return null;
        }
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        super.addAdditionalSaveData(context, tag);
        tag.putInt("D", this.direction.get2DDataValue());
        tag.putByte("F", this.floor);
        tag.putBoolean("C", this.hasChain);
    }

    @Override
    public void addChildren(StructurePiece piece, StructurePieceAccessor pieces, RandomSource random) {
        int genDepth1 = this.m_73548_();
        for (int i = 0; i < 3; i++) {
            int y = this.f_73383_.minY() + i * 4;
            int c = 2 + Mth.abs(i - this.floor);
            if (random.nextInt(c) != 0 && (i != this.floor || this.direction != Direction.SOUTH)) {
                MineshaftPieces.generateAndAddPiece(piece, pieces, random, this.f_73383_.minX() + 1, y, this.f_73383_.minZ() - 1, Direction.NORTH, genDepth1);
            }
            if (random.nextInt(c) != 0 && (i != this.floor || this.direction != Direction.EAST)) {
                MineshaftPieces.generateAndAddPiece(piece, pieces, random, this.f_73383_.minX() - 1, y, this.f_73383_.minZ() + 1, Direction.WEST, genDepth1);
            }
            if (random.nextInt(c) != 0 && (i != this.floor || this.direction != Direction.WEST)) {
                MineshaftPieces.generateAndAddPiece(piece, pieces, random, this.f_73383_.maxX() + 1, y, this.f_73383_.minZ() + 1, Direction.EAST, genDepth1);
            }
            if (random.nextInt(c) != 0 && (i != this.floor || this.direction != Direction.NORTH)) {
                MineshaftPieces.generateAndAddPiece(piece, pieces, random, this.f_73383_.minX() + 1, y, this.f_73383_.maxZ() + 1, Direction.SOUTH, genDepth1);
            }
        }
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
        if (!this.m_227881_(level, box)) {
            BlockState plank = this.f_227864_.getPlanksState();
            int minY = this.f_73383_.minY();
            int minZ = this.f_73383_.minZ();
            int maxZ = this.f_73383_.maxZ();
            int minX = this.f_73383_.minX();
            int maxX = this.f_73383_.maxX();
            for (int f = 0; f < 3; f++) {
                int yInc = f * 4;
                this.m_73441_(level, box, minX + 1, minY + yInc, minZ, maxX - 1, minY + 3 - 1 + yInc, maxZ, f_73382_, f_73382_, false);
                this.m_73441_(level, box, minX, minY + yInc, minZ + 1, maxX, minY + 3 - 1 + yInc, maxZ - 1, f_73382_, f_73382_, false);
                this.m_73441_(level, box, minX + 1, minY + 4 + yInc - 1, minZ + 1, maxX - 1, minY + 4 + yInc - 1, maxZ - 1, f_73382_, f_73382_, false);
                this.maybePlaceCobWeb(level, box, random, 0.06F, minX, minY + yInc + 2, minZ + 1);
                this.maybePlaceCobWeb(level, box, random, 0.06F, minX, minY + yInc + 2, maxZ - 1);
                this.maybePlaceCobWeb(level, box, random, 0.06F, maxX, minY + yInc + 2, minZ + 1);
                this.maybePlaceCobWeb(level, box, random, 0.06F, maxX, minY + yInc + 2, maxZ - 1);
                this.maybePlaceCobWeb(level, box, random, 0.06F, minX + 1, minY + yInc + 2, minZ);
                this.maybePlaceCobWeb(level, box, random, 0.06F, maxX - 1, minY + yInc + 2, minZ);
                this.maybePlaceCobWeb(level, box, random, 0.06F, minX + 1, minY + yInc + 2, maxZ);
                this.maybePlaceCobWeb(level, box, random, 0.06F, maxX - 1, minY + yInc + 2, maxZ);
            }
            this.m_73441_(level, box, minX + 1, minY + 4 + 8, minZ + 1, maxX - 1, minY + 4 + 8, maxZ - 1, f_73382_, f_73382_, false);
            int maxY = this.f_73383_.maxY() - 1;
            int i = minY - 1;
            BlockState wood = this.f_227864_.getWoodState();
            boolean b1 = this.fillPillarDownOrChainUp(level, wood, minX, minZ, box);
            boolean b2 = this.fillPillarDownOrChainUp(level, wood, minX, maxZ, box);
            boolean b3 = this.fillPillarDownOrChainUp(level, wood, maxX, minZ, box);
            boolean b4 = this.fillPillarDownOrChainUp(level, wood, maxX, maxZ, box);
            if (!b1 && !b2 && !b3 && !b4) {
                wood = plank;
            }
            this.placeSidePillar(level, box, minX, minY, minZ, maxY - 1, wood);
            this.placeSidePillar(level, box, minX, minY, maxZ, maxY - 1, wood);
            this.placeSidePillar(level, box, maxX, minY, minZ, maxY - 1, wood);
            this.placeSidePillar(level, box, maxX, minY, maxZ, maxY - 1, wood);
            for (int j = minX; j <= maxX; j++) {
                for (int k = minZ; k <= maxZ; k++) {
                    this.m_227890_(level, box, plank, j, i, k);
                    if (j == minX || j == maxX || k == minZ || k == maxZ) {
                        this.m_227890_(level, box, plank, j, i + 4, k);
                        this.m_227890_(level, box, plank, j, i + 8, k);
                        this.m_73434_(level, plank, j, i + 12, k, box);
                    }
                }
            }
            this.addPulley(level, random, box, minZ, minX, maxY);
        }
    }

    private void maybePlaceCobWeb(WorldGenLevel level, BoundingBox box, RandomSource random, float chance, int x, int y, int z) {
        if (this.m_73414_(level, x, y, z, box) && random.nextFloat() < chance && this.hasSturdyNeighbours(level, box, x, y, z, 2)) {
            this.m_73434_(level, Blocks.COBWEB.defaultBlockState(), x, y, z, box);
        }
    }

    private void placeSidePillar(WorldGenLevel level, BoundingBox box, int x, int y, int z, int maxY, BlockState state) {
        if (this.m_73414_(level, x, y, z, box)) {
            this.m_73441_(level, box, x, y, z, x, maxY, z, state, f_73382_, false);
        }
    }

    protected boolean fillPillarDownOrChainUp(WorldGenLevel level, BlockState state, int x, int z, BoundingBox box) {
        int minY = this.f_73383_.minY();
        int maxY = this.f_73383_.maxY() - 1;
        if (!this.m_73414_(level, x, minY, z, box)) {
            return false;
        } else {
            BlockPos.MutableBlockPos mutableBlockPos = this.m_163582_(x, minY, z);
            int j = 1;
            boolean canKeepGoingDown = this.m_163572_(level.m_8055_(new BlockPos(x, minY, z)));
            boolean canKeepGoingUp = this.m_163572_(level.m_8055_(new BlockPos(x, maxY, z)));
            if (!canKeepGoingDown) {
                return canKeepGoingUp;
            } else {
                for (; canKeepGoingDown || canKeepGoingUp; j++) {
                    if (canKeepGoingDown) {
                        mutableBlockPos.setY(minY - j);
                        BlockState blockState = level.m_8055_(mutableBlockPos);
                        boolean canBeReplaced = this.m_163572_(blockState) && !blockState.m_60713_(Blocks.LAVA);
                        if (!canBeReplaced && blockState.m_60783_(level, mutableBlockPos, Direction.UP)) {
                            fillColumnBetween(level, state, mutableBlockPos, minY - j + 1, minY);
                            return true;
                        }
                        canKeepGoingDown = j <= 20 && canBeReplaced && mutableBlockPos.m_123342_() > level.m_141937_() + 1;
                    }
                    if (canKeepGoingUp) {
                        mutableBlockPos.setY(maxY + j);
                        BlockState blockState = level.m_8055_(mutableBlockPos);
                        boolean canBeReplaced = this.m_163572_(blockState);
                        if (!canBeReplaced && this.canHangChainBelow(level, mutableBlockPos, blockState)) {
                            level.m_7731_(mutableBlockPos.setY(maxY + 1), this.f_227864_.getFenceState(), 2);
                            BlockState chain;
                            if (maxY + 2 > getRopeCutout() && (Boolean) CommonConfigs.Functional.ROPE_ENABLED.get()) {
                                chain = (BlockState) ((BlockState) ((RopeBlock) ModRegistry.ROPE.get()).m_49966_().m_61124_(RopeBlock.UP, true)).m_61124_(RopeBlock.DOWN, true);
                            } else {
                                chain = Blocks.CHAIN.defaultBlockState();
                            }
                            fillColumnBetween(level, chain, mutableBlockPos, maxY + 2, maxY + j);
                            return false;
                        }
                        canKeepGoingUp = j <= 50 && canBeReplaced && mutableBlockPos.m_123342_() < level.m_151558_() - 1;
                    }
                }
                return false;
            }
        }
    }

    private boolean canHangChainBelow(LevelReader level, BlockPos pos, BlockState state) {
        return Block.canSupportCenter(level, pos, Direction.DOWN) && !(state.m_60734_() instanceof FallingBlock);
    }

    private static void fillColumnBetween(WorldGenLevel level, BlockState state, BlockPos.MutableBlockPos pos, int minY, int maxY) {
        for (int i = minY; i < maxY; i++) {
            level.m_7731_(pos.setY(i), state, 2);
        }
    }

    private boolean hasSturdyNeighbours(WorldGenLevel level, BoundingBox box, int x, int y, int z, int required) {
        BlockPos.MutableBlockPos mutableBlockPos = this.m_163582_(x, y, z);
        int i = 0;
        for (Direction direction : Direction.values()) {
            mutableBlockPos.move(direction);
            if (box.isInside(mutableBlockPos) && level.m_8055_(mutableBlockPos).m_60783_(level, mutableBlockPos, direction.getOpposite())) {
                if (++i >= required) {
                    return true;
                }
            }
            mutableBlockPos.move(direction.getOpposite());
        }
        return false;
    }

    public static int getRopeCutout() {
        return 22;
    }

    @Nullable
    public static BlockState getMineshaftRope() {
        Block rope = CommonConfigs.getSelectedRope();
        if (rope == null) {
            return null;
        } else {
            BlockState ropeState = rope.defaultBlockState();
            if (rope instanceof AbstractRopeBlock) {
                ropeState = (BlockState) ((BlockState) ropeState.m_61124_(RopeBlock.UP, true)).m_61124_(RopeBlock.DOWN, true);
            }
            return ropeState;
        }
    }

    private void addPulley(WorldGenLevel level, RandomSource random, BoundingBox box, int minZ, int minX, int maxY) {
        BlockState wood = this.f_227864_.getWoodState();
        BlockState plank = this.f_227864_.getPlanksState();
        Direction d = this.direction;
        BlockState ropeBlock = getMineshaftRope();
        boolean hasRope = !this.hasChain && ropeBlock != null;
        if (!hasRope) {
            ropeBlock = Blocks.CHAIN.defaultBlockState();
        }
        Item ropeItem = ropeBlock.m_60734_().asItem();
        BlockPos.MutableBlockPos contraptionPos = new BlockPos.MutableBlockPos(minX + 2, maxY + 1, minZ + 2);
        this.m_73434_(level, (BlockState) ((BlockState) ((Block) ModRegistry.PULLEY_BLOCK.get()).defaultBlockState().m_61124_(PulleyBlock.TYPE, hasRope ? ModBlockProperties.Winding.ROPE : ModBlockProperties.Winding.CHAIN)).m_61124_(PulleyBlock.f_55923_, d.getAxis()), contraptionPos.m_123341_(), contraptionPos.m_123342_(), contraptionPos.m_123343_(), box);
        if (this.f_73383_.isInside(contraptionPos) && level.m_7702_(contraptionPos) instanceof PulleyBlockTile tile) {
            tile.setDisplayedItem(new ItemStack(ropeItem, 16 + random.nextInt(8)));
        }
        contraptionPos.move(d);
        Direction dOpposite = d.getOpposite();
        this.m_73434_(level, (BlockState) ((BlockState) ((Block) ModRegistry.TURN_TABLE.get()).defaultBlockState().m_61124_(TurnTableBlock.INVERTED, d.getAxisDirection() == Direction.AxisDirection.NEGATIVE)).m_61124_(TurnTableBlock.FACING, dOpposite), contraptionPos.m_123341_(), contraptionPos.m_123342_(), contraptionPos.m_123343_(), box);
        contraptionPos.move(d.getClockWise());
        this.m_73434_(level, Blocks.TARGET.defaultBlockState(), contraptionPos.m_123341_(), contraptionPos.m_123342_(), contraptionPos.m_123343_(), box);
        contraptionPos.move(dOpposite, 2).move(d.getCounterClockWise());
        this.m_73434_(level, (BlockState) wood.m_61124_(RotatedPillarBlock.AXIS, d.getAxis()), contraptionPos.m_123341_(), contraptionPos.m_123342_(), contraptionPos.m_123343_(), box);
        contraptionPos.move(dOpposite);
        this.m_73434_(level, plank, contraptionPos.m_123341_(), contraptionPos.m_123342_(), contraptionPos.m_123343_(), box);
        contraptionPos.move(d.getClockWise());
        this.m_73434_(level, plank, contraptionPos.m_123341_(), contraptionPos.m_123342_(), contraptionPos.m_123343_(), box);
        contraptionPos.move(d.getCounterClockWise(), 2);
        this.m_73434_(level, plank, contraptionPos.m_123341_(), contraptionPos.m_123342_(), contraptionPos.m_123343_(), box);
        contraptionPos.move(d, 4);
        this.m_73434_(level, plank, contraptionPos.m_123341_(), contraptionPos.m_123342_(), contraptionPos.m_123343_(), box);
        contraptionPos.move(d.getClockWise());
        this.m_73434_(level, plank, contraptionPos.m_123341_(), contraptionPos.m_123342_(), contraptionPos.m_123343_(), box);
        contraptionPos.move(d.getClockWise());
        this.m_73434_(level, plank, contraptionPos.m_123341_(), contraptionPos.m_123342_(), contraptionPos.m_123343_(), box);
        this.m_73434_(level, ropeBlock, minX + 2, maxY, minZ + 2, box);
        this.m_73434_(level, ropeBlock, minX + 2, maxY - 1, minZ + 2, box);
        BlockState chest = (hasRope ? (Block) ModRegistry.SACK.get() : Blocks.CHEST).defaultBlockState();
        this.m_73434_(level, chest, minX + 2, maxY - 2, minZ + 2, box);
        if (this.m_73414_(level, minX + 2, maxY - 2, minZ + 2, box) && level.m_7702_(new BlockPos(minX + 2, maxY - 2, minZ + 2)) instanceof RandomizableContainerBlockEntity tile) {
            tile.setLootTable(BuiltInLootTables.ABANDONED_MINESHAFT, random.nextLong());
        }
    }
}