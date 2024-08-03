package net.mehvahdjukaar.supplementaries.common.fluids;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FiniteLiquidBlock extends Block implements BucketPickup {

    public static final VoxelShape STABLE_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);

    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL;

    private final List<FluidState> stateCache;

    private final FiniteFluid fluid;

    public final int maxLevel;

    private boolean fluidStateCacheInitialized = false;

    public FiniteLiquidBlock(Supplier<? extends FiniteFluid> supplier, BlockBehaviour.Properties arg) {
        super(arg);
        this.fluid = (FiniteFluid) supplier.get();
        this.maxLevel = this.fluid.maxLayers;
        assert this.maxLevel <= 16;
        this.stateCache = Lists.newArrayList();
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(LEVEL, 0));
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        int i = (Integer) state.m_61143_(LEVEL);
        if (!this.fluidStateCacheInitialized) {
            this.initFluidStateCache();
        }
        return (FluidState) this.stateCache.get(Math.min(i, this.maxLevel));
    }

    protected synchronized void initFluidStateCache() {
        if (!this.fluidStateCacheInitialized) {
            this.stateCache.add(this.fluid.makeState(this.maxLevel));
            for (int i = 1; i < this.maxLevel; i++) {
                this.stateCache.add(this.fluid.makeState(this.maxLevel - i));
            }
            this.fluidStateCacheInitialized = true;
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return context.isAbove(STABLE_SHAPE, pos, true) && state.m_61143_(LEVEL) == 0 && context.canStandOnFluid(level.getFluidState(pos.above()), state.m_60819_()) ? STABLE_SHAPE : Shapes.empty();
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.m_60819_().isRandomlyTicking();
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        state.m_60819_().randomTick(level, pos, random);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return true;
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction direction) {
        return adjacentBlockState.m_60819_().getType().isSame(this.fluid);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public List<ItemStack> getDrops(BlockState arg, LootParams.Builder arg2) {
        return Collections.emptyList();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        level.m_186469_(pos, state.m_60819_().getType(), this.fluid.getTickDelay(level));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (state.m_60819_().isSource() || neighborState.m_60819_().isSource()) {
            level.scheduleTick(currentPos, state.m_60819_().getType(), this.fluid.getTickDelay(level));
        }
        return super.m_7417_(state, direction, neighborState, level, currentPos, neighborPos);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        level.m_186469_(pos, state.m_60819_().getType(), this.fluid.getTickDelay(level));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }

    @Override
    public ItemStack pickupBlock(LevelAccessor level, BlockPos pos, BlockState state) {
        AtomicInteger currentLevel = new AtomicInteger(state.m_60819_().getAmount());
        Map<BlockPos, Integer> posList = new HashMap();
        posList.put(pos, 0);
        this.findConnectedFluids(level, pos, posList, currentLevel);
        if (currentLevel.get() < this.maxLevel) {
            return ItemStack.EMPTY;
        } else {
            for (Entry<BlockPos, Integer> entry : posList.entrySet()) {
                BlockPos p = (BlockPos) entry.getKey();
                Integer value = (Integer) entry.getValue();
                if (value == 0) {
                    level.m_7731_(p, Blocks.AIR.defaultBlockState(), 11);
                } else {
                    level.m_7731_(p, this.fluid.makeState(value).createLegacyBlock(), 11);
                }
            }
            return new ItemStack(this.fluid.getBucket());
        }
    }

    private void findConnectedFluids(LevelAccessor level, BlockPos pos, Map<BlockPos, Integer> remainders, AtomicInteger currentLevel) {
        Queue<BlockPos> queue = new LinkedList();
        queue.offer(pos);
        while (!queue.isEmpty()) {
            BlockPos currentPos = (BlockPos) queue.poll();
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                if (currentLevel.get() >= this.maxLevel) {
                    return;
                }
                BlockPos newPos = currentPos.relative(direction);
                if (!remainders.containsKey(newPos)) {
                    BlockState state = level.m_8055_(newPos);
                    if (state.m_60734_() instanceof FiniteLiquidBlock) {
                        int l = state.m_60819_().getAmount();
                        if (l > 0) {
                            currentLevel.addAndGet(l);
                            remainders.put(newPos, Math.max(0, currentLevel.get() - this.maxLevel));
                            queue.offer(newPos);
                        }
                    }
                }
            }
        }
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return this.fluid.m_142520_();
    }
}