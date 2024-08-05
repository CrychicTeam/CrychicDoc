package net.minecraft.world.level.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LiquidBlock extends Block implements BucketPickup {

    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL;

    protected final FlowingFluid fluid;

    private final List<FluidState> stateCache;

    public static final VoxelShape STABLE_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);

    public static final ImmutableList<Direction> POSSIBLE_FLOW_DIRECTIONS = ImmutableList.of(Direction.DOWN, Direction.SOUTH, Direction.NORTH, Direction.EAST, Direction.WEST);

    protected LiquidBlock(FlowingFluid flowingFluid0, BlockBehaviour.Properties blockBehaviourProperties1) {
        super(blockBehaviourProperties1);
        this.fluid = flowingFluid0;
        this.stateCache = Lists.newArrayList();
        this.stateCache.add(flowingFluid0.getSource(false));
        for (int $$2 = 1; $$2 < 8; $$2++) {
            this.stateCache.add(flowingFluid0.getFlowing(8 - $$2, false));
        }
        this.stateCache.add(flowingFluid0.getFlowing(8, true));
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(LEVEL, 0));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return collisionContext3.isAbove(STABLE_SHAPE, blockPos2, true) && blockState0.m_61143_(LEVEL) == 0 && collisionContext3.canStandOnFluid(blockGetter1.getFluidState(blockPos2.above()), blockState0.m_60819_()) ? STABLE_SHAPE : Shapes.empty();
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState0) {
        return blockState0.m_60819_().isRandomlyTicking();
    }

    @Override
    public void randomTick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        blockState0.m_60819_().randomTick(serverLevel1, blockPos2, randomSource3);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return false;
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return !this.fluid.m_205067_(FluidTags.LAVA);
    }

    @Override
    public FluidState getFluidState(BlockState blockState0) {
        int $$1 = (Integer) blockState0.m_61143_(LEVEL);
        return (FluidState) this.stateCache.get(Math.min($$1, 8));
    }

    @Override
    public boolean skipRendering(BlockState blockState0, BlockState blockState1, Direction direction2) {
        return blockState1.m_60819_().getType().isSame(this.fluid);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState0) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public List<ItemStack> getDrops(BlockState blockState0, LootParams.Builder lootParamsBuilder1) {
        return Collections.emptyList();
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return Shapes.empty();
    }

    @Override
    public void onPlace(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (this.shouldSpreadLiquid(level1, blockPos2, blockState0)) {
            level1.m_186469_(blockPos2, blockState0.m_60819_().getType(), this.fluid.m_6718_(level1));
        }
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if (blockState0.m_60819_().isSource() || blockState2.m_60819_().isSource()) {
            levelAccessor3.scheduleTick(blockPos4, blockState0.m_60819_().getType(), this.fluid.m_6718_(levelAccessor3));
        }
        return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public void neighborChanged(BlockState blockState0, Level level1, BlockPos blockPos2, Block block3, BlockPos blockPos4, boolean boolean5) {
        if (this.shouldSpreadLiquid(level1, blockPos2, blockState0)) {
            level1.m_186469_(blockPos2, blockState0.m_60819_().getType(), this.fluid.m_6718_(level1));
        }
    }

    private boolean shouldSpreadLiquid(Level level0, BlockPos blockPos1, BlockState blockState2) {
        if (this.fluid.m_205067_(FluidTags.LAVA)) {
            boolean $$3 = level0.getBlockState(blockPos1.below()).m_60713_(Blocks.SOUL_SOIL);
            UnmodifiableIterator var5 = POSSIBLE_FLOW_DIRECTIONS.iterator();
            while (var5.hasNext()) {
                Direction $$4 = (Direction) var5.next();
                BlockPos $$5 = blockPos1.relative($$4.getOpposite());
                if (level0.getFluidState($$5).is(FluidTags.WATER)) {
                    Block $$6 = level0.getFluidState(blockPos1).isSource() ? Blocks.OBSIDIAN : Blocks.COBBLESTONE;
                    level0.setBlockAndUpdate(blockPos1, $$6.defaultBlockState());
                    this.fizz(level0, blockPos1);
                    return false;
                }
                if ($$3 && level0.getBlockState($$5).m_60713_(Blocks.BLUE_ICE)) {
                    level0.setBlockAndUpdate(blockPos1, Blocks.BASALT.defaultBlockState());
                    this.fizz(level0, blockPos1);
                    return false;
                }
            }
        }
        return true;
    }

    private void fizz(LevelAccessor levelAccessor0, BlockPos blockPos1) {
        levelAccessor0.levelEvent(1501, blockPos1, 0);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(LEVEL);
    }

    @Override
    public ItemStack pickupBlock(LevelAccessor levelAccessor0, BlockPos blockPos1, BlockState blockState2) {
        if ((Integer) blockState2.m_61143_(LEVEL) == 0) {
            levelAccessor0.m_7731_(blockPos1, Blocks.AIR.defaultBlockState(), 11);
            return new ItemStack(this.fluid.m_6859_());
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return this.fluid.m_142520_();
    }
}