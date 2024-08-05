package com.github.alexmodguy.alexscaves.server.level.structure.piece;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.ForsakenIdolBlock;
import com.github.alexmodguy.alexscaves.server.block.ThornwoodBranchBlock;
import com.github.alexmodguy.alexscaves.server.level.structure.ForlornBridgeStructure;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

public class ForlornBridgeStructurePiece extends StructurePiece {

    protected final BlockPos bridgePos;

    protected final int sectionId;

    protected final int maxSections;

    protected final Direction direction;

    public ForlornBridgeStructurePiece(BlockPos bridgePos, int sectionId, int maxSections, Direction direction) {
        super(ACStructurePieceRegistry.FORLORN_BRIDGE.get(), 0, createBoundingBox(bridgePos, direction));
        this.bridgePos = bridgePos;
        this.sectionId = sectionId;
        this.maxSections = maxSections;
        this.direction = direction;
    }

    public ForlornBridgeStructurePiece(CompoundTag tag) {
        super(ACStructurePieceRegistry.FORLORN_BRIDGE.get(), tag);
        this.bridgePos = new BlockPos(tag.getInt("TPX"), tag.getInt("TPY"), tag.getInt("TPZ"));
        this.sectionId = tag.getInt("Section");
        this.maxSections = tag.getInt("MaxSections");
        this.direction = Direction.from2DDataValue(tag.getInt("Direction"));
    }

    public ForlornBridgeStructurePiece(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag tag) {
        this(tag);
    }

    private static BoundingBox createBoundingBox(BlockPos origin, Direction direction) {
        int i = ForlornBridgeStructure.BRIDGE_SECTION_WIDTH / 2;
        int j = ForlornBridgeStructure.BRIDGE_SECTION_LENGTH - i;
        int dirX = i + j * Math.abs(direction.getStepX());
        int dirZ = i + j * Math.abs(direction.getStepZ());
        return new BoundingBox(origin.m_123341_() - dirX, -64, origin.m_123343_() - dirZ, origin.m_123341_() + dirX, 256, origin.m_123343_() + dirZ);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putInt("TPX", this.bridgePos.m_123341_());
        tag.putInt("TPY", this.bridgePos.m_123342_());
        tag.putInt("TPZ", this.bridgePos.m_123343_());
        tag.putInt("Section", this.sectionId);
        tag.putInt("MaxSections", this.maxSections);
        tag.putInt("Direction", this.direction.get2DDataValue());
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager featureManager, ChunkGenerator chunkGen, RandomSource random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        Set<BlockPos> supports = new HashSet();
        Set<BlockPos> specialSupports = new HashSet();
        int j = ForlornBridgeStructure.BRIDGE_SECTION_WIDTH / 2;
        for (int width = -j; width < j; width++) {
            pos.set(this.bridgePos);
            pos.move(this.direction.getClockWise(), width);
            int startLength = 0;
            int endLength = ForlornBridgeStructure.BRIDGE_SECTION_LENGTH;
            if (this.sectionId == 0) {
                startLength = random.nextInt(ForlornBridgeStructure.BRIDGE_SECTION_LENGTH - 1);
            }
            if (this.sectionId == this.maxSections) {
                endLength = random.nextInt(ForlornBridgeStructure.BRIDGE_SECTION_LENGTH);
            }
            for (int length = startLength; length < endLength; length++) {
                pos.move(this.direction);
                BlockState prior = this.checkedGetBlock(level, pos);
                if (prior.m_60795_() || !prior.m_60819_().isEmpty()) {
                    BlockState state = (BlockState) ACBlockRegistry.THORNWOOD_PLANKS_SLAB.get().defaultBlockState().m_61124_(SlabBlock.TYPE, SlabType.TOP);
                    if (width == -j || width == j - 1) {
                        if (length % 3 != startLength) {
                            boolean left = width == j - 1;
                            state = (BlockState) ((BlockState) ACBlockRegistry.THORNWOOD_PLANKS_STAIRS.get().defaultBlockState().m_61124_(StairBlock.HALF, Half.TOP)).m_61124_(StairBlock.FACING, left ? this.direction.getCounterClockWise() : this.direction.getClockWise());
                        } else {
                            state = Blocks.STRIPPED_DARK_OAK_LOG.defaultBlockState();
                            supports.add(pos.immutable());
                            if (this.sectionId == 0 && length == startLength || this.sectionId == this.maxSections && length != startLength) {
                                specialSupports.add(pos.immutable());
                            }
                        }
                    }
                    this.checkedSetBlock(level, pos, state);
                }
            }
        }
        for (BlockPos support : supports) {
            this.checkedSetBlock(level, support.above(), Blocks.STRIPPED_DARK_OAK_LOG.defaultBlockState());
            this.checkedSetBlock(level, support.above().relative(this.direction), (BlockState) ACBlockRegistry.THORNWOOD_BRANCH.get().defaultBlockState().m_61124_(ThornwoodBranchBlock.FACING, this.direction));
            this.checkedSetBlock(level, support.above().relative(this.direction.getOpposite()), (BlockState) ACBlockRegistry.THORNWOOD_BRANCH.get().defaultBlockState().m_61124_(ThornwoodBranchBlock.FACING, this.direction.getOpposite()));
            if (specialSupports.contains(support)) {
                boolean end = this.maxSections == this.sectionId;
                this.checkedSetBlock(level, support.above(2), (BlockState) ACBlockRegistry.FORSAKEN_IDOL.get().defaultBlockState().m_61124_(ForsakenIdolBlock.FACING, end ? this.direction : this.direction.getOpposite()));
            } else {
                this.checkedSetBlock(level, support.above(2), Blocks.STRIPPED_DARK_OAK_LOG.defaultBlockState());
                this.checkedSetBlock(level, support.above(3), ACBlockRegistry.THORNWOOD_PLANKS_FENCE.get().defaultBlockState());
                if (random.nextBoolean()) {
                    this.buildChain(level, support.above(4));
                }
            }
            if (random.nextBoolean() || this.sectionId == 0 || this.sectionId == this.maxSections) {
                this.buildBeam(level, support.below());
            }
        }
    }

    private void buildBeam(WorldGenLevel level, BlockPos below) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        pos.set(below);
        while (!this.checkedGetBlock(level, pos).m_280296_() && pos.m_123342_() > level.m_141937_()) {
            this.checkedSetBlock(level, pos, Blocks.STRIPPED_DARK_OAK_LOG.defaultBlockState());
            pos.move(0, -1, 0);
        }
    }

    private void buildChain(WorldGenLevel level, BlockPos above) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        pos.set(above);
        if (!level.m_45527_(pos) && level.m_6924_(Heightmap.Types.WORLD_SURFACE_WG, above.m_123341_(), above.m_123343_()) < level.m_151558_()) {
            while (!this.checkedGetBlock(level, pos).m_280296_() && pos.m_123342_() < level.m_151558_()) {
                this.checkedSetBlock(level, pos, Blocks.CHAIN.defaultBlockState());
                pos.move(0, 1, 0);
            }
        }
    }

    public void checkedSetBlock(WorldGenLevel level, BlockPos position, BlockState state) {
        if (this.m_73547_().isInside(position)) {
            level.m_7731_(position, state, 128);
        }
    }

    public BlockState checkedGetBlock(WorldGenLevel level, BlockPos position) {
        return this.m_73547_().isInside(position) ? level.m_8055_(position) : Blocks.VOID_AIR.defaultBlockState();
    }
}