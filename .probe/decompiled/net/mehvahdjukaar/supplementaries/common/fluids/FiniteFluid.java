package net.mehvahdjukaar.supplementaries.common.fluids;

import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class FiniteFluid extends Fluid {

    public static final IntegerProperty LEVEL = ModBlockProperties.FINITE_FLUID_LEVEL;

    private final Map<FluidState, VoxelShape> shapes = Maps.newIdentityHashMap();

    protected final int maxLayers;

    private final Supplier<? extends BucketItem> bucket;

    private final Supplier<? extends Block> block;

    public FiniteFluid(int maxLayers, Supplier<? extends Block> block, Supplier<? extends BucketItem> bucket) {
        this.maxLayers = maxLayers;
        this.block = block;
        this.bucket = bucket;
        this.m_76142_((FluidState) ((FluidState) this.f_76105_.any()).m_61124_(LEVEL, maxLayers));
    }

    public int getLayersPerBlock() {
        return this.maxLayers;
    }

    @Override
    public Item getBucket() {
        return (Item) this.bucket.get();
    }

    @Override
    protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
        builder.add(LEVEL);
    }

    @Override
    public Vec3 getFlow(BlockGetter blockReader, BlockPos pos, FluidState fluidState) {
        return Vec3.ZERO;
    }

    private void spreadToSides(Level level, BlockPos pos, FluidState fluidState, BlockState blockState) {
        int currentAmount = fluidState.getAmount();
        if (currentAmount > 1) {
            Map<Direction, Integer> map = this.getWantedSpreadDirections(level, pos, blockState);
            List<Entry<Direction, Integer>> entryList = new ArrayList(map.entrySet());
            Collections.shuffle(entryList);
            Map<Direction, Integer> var17 = new LinkedHashMap();
            for (Entry<Direction, Integer> e : entryList) {
                var17.put((Direction) e.getKey(), (Integer) e.getValue());
            }
            int initialAmount = currentAmount;
            var17.values().removeIf(i -> i >= currentAmount);
            int r = 1;
            while (currentAmount <= var17.size()) {
                int finalR = r++;
                Iterator<Entry<Direction, Integer>> iter = var17.entrySet().iterator();
                while (iter.hasNext()) {
                    Entry<Direction, Integer> e = (Entry<Direction, Integer>) iter.next();
                    if ((Integer) e.getValue() > initialAmount - finalR) {
                        iter.remove();
                    }
                    if (currentAmount > var17.size()) {
                        break;
                    }
                }
            }
            for (Entry<Direction, Integer> ex : var17.entrySet()) {
                int oldAmount = (Integer) ex.getValue();
                Direction dir = (Direction) ex.getKey();
                BlockPos facingPos = pos.relative(dir);
                BlockState s = level.getBlockState(facingPos);
                FluidState fluidstate = this.makeState(oldAmount + 1);
                this.spreadTo(level, facingPos, s, dir, fluidstate);
            }
            FluidState myNewState = this.makeState(currentAmount - var17.size());
            BlockState blockstate = myNewState.createLegacyBlock();
            level.setBlock(pos, blockstate, 2);
            level.updateNeighborsAt(pos, blockstate.m_60734_());
        }
    }

    public FluidState makeState(int level) {
        return (FluidState) this.m_76145_().m_61124_(LEVEL, level);
    }

    protected void spreadTo(LevelAccessor level, BlockPos pos, BlockState blockState, Direction direction, FluidState fluidState) {
        if (blockState.m_60734_() instanceof LiquidBlockContainer container) {
            container.placeLiquid(level, pos, blockState, fluidState);
        } else {
            if (!blockState.m_60795_()) {
                this.beforeDestroyingBlock(level, pos, blockState);
            }
            level.m_7731_(pos, fluidState.createLegacyBlock(), 3);
        }
    }

    protected Map<Direction, Integer> getWantedSpreadDirections(Level level, BlockPos pos, BlockState state) {
        Map<Direction, Integer> list = new HashMap();
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos facingPos = pos.relative(direction);
            BlockState facingState = level.getBlockState(facingPos);
            FluidState facingFluid = facingState.m_60819_();
            if (this.canHoldFluid(level, facingPos, facingState)) {
                list.put(direction, facingFluid.getAmount());
            }
        }
        return list;
    }

    @Override
    public void tick(Level level, BlockPos pos, FluidState state) {
        if (!state.isEmpty()) {
            BlockState myState = level.getBlockState(pos);
            BlockPos belowPos = pos.below();
            BlockState belowState = level.getBlockState(belowPos);
            FluidState belowFluid = belowState.m_60819_();
            if (belowFluid.getType().isSame(this)) {
                if (belowFluid.getAmount() < this.maxLayers) {
                    int belowMissing = this.maxLayers - belowFluid.getAmount();
                    int belowAddition = Math.min(belowMissing, state.getAmount());
                    int newAboveAmount = state.getAmount() - belowAddition;
                    level.setBlockAndUpdate(belowPos, ((FluidState) belowFluid.m_61124_(LEVEL, belowFluid.getAmount() + belowAddition)).createLegacyBlock());
                    if (newAboveAmount > 0) {
                        level.setBlockAndUpdate(pos, ((FluidState) state.m_61124_(LEVEL, newAboveAmount)).createLegacyBlock());
                    } else {
                        level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                    }
                } else {
                    this.spreadToSides(level, pos, state, myState);
                }
            } else {
                if (this.canHoldFluid(level, belowPos, belowState)) {
                    this.spreadTo(level, belowPos, belowState, Direction.DOWN, state);
                    level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                } else {
                    this.spreadToSides(level, pos, state, myState);
                }
            }
        }
    }

    private boolean canHoldFluid(BlockGetter level, BlockPos pos, BlockState state) {
        FluidState fluidState = state.m_60819_();
        if (!fluidState.isEmpty() && !fluidState.is(this)) {
            return false;
        } else {
            Block block = state.m_60734_();
            if (block instanceof LiquidBlockContainer lc) {
                return lc.canPlaceLiquid(level, pos, state, this);
            } else {
                return !(block instanceof DoorBlock) && !state.m_204336_(BlockTags.SIGNS) && !state.m_60713_(Blocks.LADDER) && !state.m_60713_(Blocks.SUGAR_CANE) && !state.m_60713_(Blocks.BUBBLE_COLUMN) && !state.m_60713_(Blocks.NETHER_PORTAL) && !state.m_60713_(Blocks.END_PORTAL) && !state.m_60713_(Blocks.END_GATEWAY) && !state.m_60713_(Blocks.STRUCTURE_VOID) ? !state.m_280555_() : false;
            }
        }
    }

    private static boolean hasSameAbove(FluidState fluidState, BlockGetter level, BlockPos pos) {
        return fluidState.getType().isSame(level.getFluidState(pos.above()).getType());
    }

    @Override
    public float getHeight(FluidState state, BlockGetter level, BlockPos pos) {
        return hasSameAbove(state, level, pos) ? 1.0F : state.getOwnHeight();
    }

    @Override
    public float getOwnHeight(FluidState state) {
        return 0.875F * (float) state.getAmount() / (float) this.maxLayers;
    }

    @Override
    public boolean isSource(FluidState state) {
        return true;
    }

    @Override
    public int getAmount(FluidState state) {
        return (Integer) state.m_61143_(LEVEL);
    }

    @Override
    public VoxelShape getShape(FluidState state, BlockGetter level, BlockPos pos) {
        return (VoxelShape) this.shapes.computeIfAbsent(state, arg3 -> Shapes.box(0.0, 0.0, 0.0, 1.0, (double) arg3.getHeight(level, pos), 1.0));
    }

    public boolean shouldSlowDown(FluidState state) {
        return state.getAmount() > 2;
    }

    protected void beforeDestroyingBlock(LevelAccessor level, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = state.m_155947_() ? level.m_7702_(pos) : null;
        Block.dropResources(state, level, pos, blockEntity);
    }

    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid fluid, Direction direction) {
        return true;
    }

    @Override
    public int getTickDelay(LevelReader level) {
        return 5;
    }

    @Override
    protected float getExplosionResistance() {
        return 0.0F;
    }

    @Override
    protected BlockState createLegacyBlock(FluidState state) {
        return (BlockState) ((Block) this.block.get()).defaultBlockState().m_61124_(LiquidBlock.LEVEL, this.getLegacyLevel(state));
    }

    protected int getLegacyLevel(FluidState state) {
        int amount = state.getAmount();
        return this.maxLayers - Math.min(amount, this.maxLayers);
    }
}