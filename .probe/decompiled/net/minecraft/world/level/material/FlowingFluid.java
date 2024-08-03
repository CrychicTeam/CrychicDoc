package net.minecraft.world.level.material;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2ByteLinkedOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2BooleanMap;
import it.unimi.dsi.fastutil.shorts.Short2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class FlowingFluid extends Fluid {

    public static final BooleanProperty FALLING = BlockStateProperties.FALLING;

    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_FLOWING;

    private static final int CACHE_SIZE = 200;

    private static final ThreadLocal<Object2ByteLinkedOpenHashMap<Block.BlockStatePairKey>> OCCLUSION_CACHE = ThreadLocal.withInitial(() -> {
        Object2ByteLinkedOpenHashMap<Block.BlockStatePairKey> $$0 = new Object2ByteLinkedOpenHashMap<Block.BlockStatePairKey>(200) {

            protected void rehash(int p_76102_) {
            }
        };
        $$0.defaultReturnValue((byte) 127);
        return $$0;
    });

    private final Map<FluidState, VoxelShape> shapes = Maps.newIdentityHashMap();

    @Override
    protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> stateDefinitionBuilderFluidFluidState0) {
        stateDefinitionBuilderFluidFluidState0.add(FALLING);
    }

    @Override
    public Vec3 getFlow(BlockGetter blockGetter0, BlockPos blockPos1, FluidState fluidState2) {
        double $$3 = 0.0;
        double $$4 = 0.0;
        BlockPos.MutableBlockPos $$5 = new BlockPos.MutableBlockPos();
        for (Direction $$6 : Direction.Plane.HORIZONTAL) {
            $$5.setWithOffset(blockPos1, $$6);
            FluidState $$7 = blockGetter0.getFluidState($$5);
            if (this.affectsFlow($$7)) {
                float $$8 = $$7.getOwnHeight();
                float $$9 = 0.0F;
                if ($$8 == 0.0F) {
                    if (!blockGetter0.getBlockState($$5).m_280555_()) {
                        BlockPos $$10 = $$5.m_7495_();
                        FluidState $$11 = blockGetter0.getFluidState($$10);
                        if (this.affectsFlow($$11)) {
                            $$8 = $$11.getOwnHeight();
                            if ($$8 > 0.0F) {
                                $$9 = fluidState2.getOwnHeight() - ($$8 - 0.8888889F);
                            }
                        }
                    }
                } else if ($$8 > 0.0F) {
                    $$9 = fluidState2.getOwnHeight() - $$8;
                }
                if ($$9 != 0.0F) {
                    $$3 += (double) ((float) $$6.getStepX() * $$9);
                    $$4 += (double) ((float) $$6.getStepZ() * $$9);
                }
            }
        }
        Vec3 $$12 = new Vec3($$3, 0.0, $$4);
        if ((Boolean) fluidState2.m_61143_(FALLING)) {
            for (Direction $$13 : Direction.Plane.HORIZONTAL) {
                $$5.setWithOffset(blockPos1, $$13);
                if (this.isSolidFace(blockGetter0, $$5, $$13) || this.isSolidFace(blockGetter0, $$5.m_7494_(), $$13)) {
                    $$12 = $$12.normalize().add(0.0, -6.0, 0.0);
                    break;
                }
            }
        }
        return $$12.normalize();
    }

    private boolean affectsFlow(FluidState fluidState0) {
        return fluidState0.isEmpty() || fluidState0.getType().isSame(this);
    }

    protected boolean isSolidFace(BlockGetter blockGetter0, BlockPos blockPos1, Direction direction2) {
        BlockState $$3 = blockGetter0.getBlockState(blockPos1);
        FluidState $$4 = blockGetter0.getFluidState(blockPos1);
        if ($$4.getType().isSame(this)) {
            return false;
        } else if (direction2 == Direction.UP) {
            return true;
        } else {
            return $$3.m_60734_() instanceof IceBlock ? false : $$3.m_60783_(blockGetter0, blockPos1, direction2);
        }
    }

    protected void spread(Level level0, BlockPos blockPos1, FluidState fluidState2) {
        if (!fluidState2.isEmpty()) {
            BlockState $$3 = level0.getBlockState(blockPos1);
            BlockPos $$4 = blockPos1.below();
            BlockState $$5 = level0.getBlockState($$4);
            FluidState $$6 = this.getNewLiquid(level0, $$4, $$5);
            if (this.canSpreadTo(level0, blockPos1, $$3, Direction.DOWN, $$4, $$5, level0.getFluidState($$4), $$6.getType())) {
                this.spreadTo(level0, $$4, $$5, Direction.DOWN, $$6);
                if (this.sourceNeighborCount(level0, blockPos1) >= 3) {
                    this.spreadToSides(level0, blockPos1, fluidState2, $$3);
                }
            } else if (fluidState2.isSource() || !this.isWaterHole(level0, $$6.getType(), blockPos1, $$3, $$4, $$5)) {
                this.spreadToSides(level0, blockPos1, fluidState2, $$3);
            }
        }
    }

    private void spreadToSides(Level level0, BlockPos blockPos1, FluidState fluidState2, BlockState blockState3) {
        int $$4 = fluidState2.getAmount() - this.getDropOff(level0);
        if ((Boolean) fluidState2.m_61143_(FALLING)) {
            $$4 = 7;
        }
        if ($$4 > 0) {
            Map<Direction, FluidState> $$5 = this.getSpread(level0, blockPos1, blockState3);
            for (Entry<Direction, FluidState> $$6 : $$5.entrySet()) {
                Direction $$7 = (Direction) $$6.getKey();
                FluidState $$8 = (FluidState) $$6.getValue();
                BlockPos $$9 = blockPos1.relative($$7);
                BlockState $$10 = level0.getBlockState($$9);
                if (this.canSpreadTo(level0, blockPos1, blockState3, $$7, $$9, $$10, level0.getFluidState($$9), $$8.getType())) {
                    this.spreadTo(level0, $$9, $$10, $$7, $$8);
                }
            }
        }
    }

    protected FluidState getNewLiquid(Level level0, BlockPos blockPos1, BlockState blockState2) {
        int $$3 = 0;
        int $$4 = 0;
        for (Direction $$5 : Direction.Plane.HORIZONTAL) {
            BlockPos $$6 = blockPos1.relative($$5);
            BlockState $$7 = level0.getBlockState($$6);
            FluidState $$8 = $$7.m_60819_();
            if ($$8.getType().isSame(this) && this.canPassThroughWall($$5, level0, blockPos1, blockState2, $$6, $$7)) {
                if ($$8.isSource()) {
                    $$4++;
                }
                $$3 = Math.max($$3, $$8.getAmount());
            }
        }
        if (this.canConvertToSource(level0) && $$4 >= 2) {
            BlockState $$9 = level0.getBlockState(blockPos1.below());
            FluidState $$10 = $$9.m_60819_();
            if ($$9.m_280296_() || this.isSourceBlockOfThisType($$10)) {
                return this.getSource(false);
            }
        }
        BlockPos $$11 = blockPos1.above();
        BlockState $$12 = level0.getBlockState($$11);
        FluidState $$13 = $$12.m_60819_();
        if (!$$13.isEmpty() && $$13.getType().isSame(this) && this.canPassThroughWall(Direction.UP, level0, blockPos1, blockState2, $$11, $$12)) {
            return this.getFlowing(8, true);
        } else {
            int $$14 = $$3 - this.getDropOff(level0);
            return $$14 <= 0 ? Fluids.EMPTY.defaultFluidState() : this.getFlowing($$14, false);
        }
    }

    private boolean canPassThroughWall(Direction direction0, BlockGetter blockGetter1, BlockPos blockPos2, BlockState blockState3, BlockPos blockPos4, BlockState blockState5) {
        Object2ByteLinkedOpenHashMap<Block.BlockStatePairKey> $$7;
        if (!blockState3.m_60734_().hasDynamicShape() && !blockState5.m_60734_().hasDynamicShape()) {
            $$7 = (Object2ByteLinkedOpenHashMap<Block.BlockStatePairKey>) OCCLUSION_CACHE.get();
        } else {
            $$7 = null;
        }
        Block.BlockStatePairKey $$8;
        if ($$7 != null) {
            $$8 = new Block.BlockStatePairKey(blockState3, blockState5, direction0);
            byte $$9 = $$7.getAndMoveToFirst($$8);
            if ($$9 != 127) {
                return $$9 != 0;
            }
        } else {
            $$8 = null;
        }
        VoxelShape $$11 = blockState3.m_60812_(blockGetter1, blockPos2);
        VoxelShape $$12 = blockState5.m_60812_(blockGetter1, blockPos4);
        boolean $$13 = !Shapes.mergedFaceOccludes($$11, $$12, direction0);
        if ($$7 != null) {
            if ($$7.size() == 200) {
                $$7.removeLastByte();
            }
            $$7.putAndMoveToFirst($$8, (byte) ($$13 ? 1 : 0));
        }
        return $$13;
    }

    public abstract Fluid getFlowing();

    public FluidState getFlowing(int int0, boolean boolean1) {
        return (FluidState) ((FluidState) this.getFlowing().defaultFluidState().m_61124_(LEVEL, int0)).m_61124_(FALLING, boolean1);
    }

    public abstract Fluid getSource();

    public FluidState getSource(boolean boolean0) {
        return (FluidState) this.getSource().defaultFluidState().m_61124_(FALLING, boolean0);
    }

    protected abstract boolean canConvertToSource(Level var1);

    protected void spreadTo(LevelAccessor levelAccessor0, BlockPos blockPos1, BlockState blockState2, Direction direction3, FluidState fluidState4) {
        if (blockState2.m_60734_() instanceof LiquidBlockContainer) {
            ((LiquidBlockContainer) blockState2.m_60734_()).placeLiquid(levelAccessor0, blockPos1, blockState2, fluidState4);
        } else {
            if (!blockState2.m_60795_()) {
                this.beforeDestroyingBlock(levelAccessor0, blockPos1, blockState2);
            }
            levelAccessor0.m_7731_(blockPos1, fluidState4.createLegacyBlock(), 3);
        }
    }

    protected abstract void beforeDestroyingBlock(LevelAccessor var1, BlockPos var2, BlockState var3);

    private static short getCacheKey(BlockPos blockPos0, BlockPos blockPos1) {
        int $$2 = blockPos1.m_123341_() - blockPos0.m_123341_();
        int $$3 = blockPos1.m_123343_() - blockPos0.m_123343_();
        return (short) (($$2 + 128 & 0xFF) << 8 | $$3 + 128 & 0xFF);
    }

    protected int getSlopeDistance(LevelReader levelReader0, BlockPos blockPos1, int int2, Direction direction3, BlockState blockState4, BlockPos blockPos5, Short2ObjectMap<Pair<BlockState, FluidState>> shortObjectMapPairBlockStateFluidState6, Short2BooleanMap shortBooleanMap7) {
        int $$8 = 1000;
        for (Direction $$9 : Direction.Plane.HORIZONTAL) {
            if ($$9 != direction3) {
                BlockPos $$10 = blockPos1.relative($$9);
                short $$11 = getCacheKey(blockPos5, $$10);
                Pair<BlockState, FluidState> $$12 = (Pair<BlockState, FluidState>) shortObjectMapPairBlockStateFluidState6.computeIfAbsent($$11, p_284932_ -> {
                    BlockState $$3 = levelReader0.m_8055_($$10);
                    return Pair.of($$3, $$3.m_60819_());
                });
                BlockState $$13 = (BlockState) $$12.getFirst();
                FluidState $$14 = (FluidState) $$12.getSecond();
                if (this.canPassThrough(levelReader0, this.getFlowing(), blockPos1, blockState4, $$9, $$10, $$13, $$14)) {
                    boolean $$15 = shortBooleanMap7.computeIfAbsent($$11, p_192912_ -> {
                        BlockPos $$4 = $$10.below();
                        BlockState $$5 = levelReader0.m_8055_($$4);
                        return this.isWaterHole(levelReader0, this.getFlowing(), $$10, $$13, $$4, $$5);
                    });
                    if ($$15) {
                        return int2;
                    }
                    if (int2 < this.getSlopeFindDistance(levelReader0)) {
                        int $$16 = this.getSlopeDistance(levelReader0, $$10, int2 + 1, $$9.getOpposite(), $$13, blockPos5, shortObjectMapPairBlockStateFluidState6, shortBooleanMap7);
                        if ($$16 < $$8) {
                            $$8 = $$16;
                        }
                    }
                }
            }
        }
        return $$8;
    }

    private boolean isWaterHole(BlockGetter blockGetter0, Fluid fluid1, BlockPos blockPos2, BlockState blockState3, BlockPos blockPos4, BlockState blockState5) {
        if (!this.canPassThroughWall(Direction.DOWN, blockGetter0, blockPos2, blockState3, blockPos4, blockState5)) {
            return false;
        } else {
            return blockState5.m_60819_().getType().isSame(this) ? true : this.canHoldFluid(blockGetter0, blockPos4, blockState5, fluid1);
        }
    }

    private boolean canPassThrough(BlockGetter blockGetter0, Fluid fluid1, BlockPos blockPos2, BlockState blockState3, Direction direction4, BlockPos blockPos5, BlockState blockState6, FluidState fluidState7) {
        return !this.isSourceBlockOfThisType(fluidState7) && this.canPassThroughWall(direction4, blockGetter0, blockPos2, blockState3, blockPos5, blockState6) && this.canHoldFluid(blockGetter0, blockPos5, blockState6, fluid1);
    }

    private boolean isSourceBlockOfThisType(FluidState fluidState0) {
        return fluidState0.getType().isSame(this) && fluidState0.isSource();
    }

    protected abstract int getSlopeFindDistance(LevelReader var1);

    private int sourceNeighborCount(LevelReader levelReader0, BlockPos blockPos1) {
        int $$2 = 0;
        for (Direction $$3 : Direction.Plane.HORIZONTAL) {
            BlockPos $$4 = blockPos1.relative($$3);
            FluidState $$5 = levelReader0.m_6425_($$4);
            if (this.isSourceBlockOfThisType($$5)) {
                $$2++;
            }
        }
        return $$2;
    }

    protected Map<Direction, FluidState> getSpread(Level level0, BlockPos blockPos1, BlockState blockState2) {
        int $$3 = 1000;
        Map<Direction, FluidState> $$4 = Maps.newEnumMap(Direction.class);
        Short2ObjectMap<Pair<BlockState, FluidState>> $$5 = new Short2ObjectOpenHashMap();
        Short2BooleanMap $$6 = new Short2BooleanOpenHashMap();
        for (Direction $$7 : Direction.Plane.HORIZONTAL) {
            BlockPos $$8 = blockPos1.relative($$7);
            short $$9 = getCacheKey(blockPos1, $$8);
            Pair<BlockState, FluidState> $$10 = (Pair<BlockState, FluidState>) $$5.computeIfAbsent($$9, p_284929_ -> {
                BlockState $$3x = level0.getBlockState($$8);
                return Pair.of($$3x, $$3x.m_60819_());
            });
            BlockState $$11 = (BlockState) $$10.getFirst();
            FluidState $$12 = (FluidState) $$10.getSecond();
            FluidState $$13 = this.getNewLiquid(level0, $$8, $$11);
            if (this.canPassThrough(level0, $$13.getType(), blockPos1, blockState2, $$7, $$8, $$11, $$12)) {
                BlockPos $$14 = $$8.below();
                boolean $$15 = $$6.computeIfAbsent($$9, p_255612_ -> {
                    BlockState $$5x = level0.getBlockState($$14);
                    return this.isWaterHole(level0, this.getFlowing(), $$8, $$11, $$14, $$5x);
                });
                int $$16;
                if ($$15) {
                    $$16 = 0;
                } else {
                    $$16 = this.getSlopeDistance(level0, $$8, 1, $$7.getOpposite(), $$11, blockPos1, $$5, $$6);
                }
                if ($$16 < $$3) {
                    $$4.clear();
                }
                if ($$16 <= $$3) {
                    $$4.put($$7, $$13);
                    $$3 = $$16;
                }
            }
        }
        return $$4;
    }

    private boolean canHoldFluid(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2, Fluid fluid3) {
        Block $$4 = blockState2.m_60734_();
        if ($$4 instanceof LiquidBlockContainer) {
            return ((LiquidBlockContainer) $$4).canPlaceLiquid(blockGetter0, blockPos1, blockState2, fluid3);
        } else if ($$4 instanceof DoorBlock || blockState2.m_204336_(BlockTags.SIGNS) || blockState2.m_60713_(Blocks.LADDER) || blockState2.m_60713_(Blocks.SUGAR_CANE) || blockState2.m_60713_(Blocks.BUBBLE_COLUMN)) {
            return false;
        } else {
            return !blockState2.m_60713_(Blocks.NETHER_PORTAL) && !blockState2.m_60713_(Blocks.END_PORTAL) && !blockState2.m_60713_(Blocks.END_GATEWAY) && !blockState2.m_60713_(Blocks.STRUCTURE_VOID) ? !blockState2.m_280555_() : false;
        }
    }

    protected boolean canSpreadTo(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2, Direction direction3, BlockPos blockPos4, BlockState blockState5, FluidState fluidState6, Fluid fluid7) {
        return fluidState6.canBeReplacedWith(blockGetter0, blockPos4, fluid7, direction3) && this.canPassThroughWall(direction3, blockGetter0, blockPos1, blockState2, blockPos4, blockState5) && this.canHoldFluid(blockGetter0, blockPos4, blockState5, fluid7);
    }

    protected abstract int getDropOff(LevelReader var1);

    protected int getSpreadDelay(Level level0, BlockPos blockPos1, FluidState fluidState2, FluidState fluidState3) {
        return this.m_6718_(level0);
    }

    @Override
    public void tick(Level level0, BlockPos blockPos1, FluidState fluidState2) {
        if (!fluidState2.isSource()) {
            FluidState $$3 = this.getNewLiquid(level0, blockPos1, level0.getBlockState(blockPos1));
            int $$4 = this.getSpreadDelay(level0, blockPos1, fluidState2, $$3);
            if ($$3.isEmpty()) {
                fluidState2 = $$3;
                level0.setBlock(blockPos1, Blocks.AIR.defaultBlockState(), 3);
            } else if (!$$3.equals(fluidState2)) {
                fluidState2 = $$3;
                BlockState $$5 = $$3.createLegacyBlock();
                level0.setBlock(blockPos1, $$5, 2);
                level0.m_186469_(blockPos1, $$3.getType(), $$4);
                level0.updateNeighborsAt(blockPos1, $$5.m_60734_());
            }
        }
        this.spread(level0, blockPos1, fluidState2);
    }

    protected static int getLegacyLevel(FluidState fluidState0) {
        return fluidState0.isSource() ? 0 : 8 - Math.min(fluidState0.getAmount(), 8) + (fluidState0.m_61143_(FALLING) ? 8 : 0);
    }

    private static boolean hasSameAbove(FluidState fluidState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return fluidState0.getType().isSame(blockGetter1.getFluidState(blockPos2.above()).getType());
    }

    @Override
    public float getHeight(FluidState fluidState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return hasSameAbove(fluidState0, blockGetter1, blockPos2) ? 1.0F : fluidState0.getOwnHeight();
    }

    @Override
    public float getOwnHeight(FluidState fluidState0) {
        return (float) fluidState0.getAmount() / 9.0F;
    }

    @Override
    public abstract int getAmount(FluidState var1);

    @Override
    public VoxelShape getShape(FluidState fluidState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return fluidState0.getAmount() == 9 && hasSameAbove(fluidState0, blockGetter1, blockPos2) ? Shapes.block() : (VoxelShape) this.shapes.computeIfAbsent(fluidState0, p_76073_ -> Shapes.box(0.0, 0.0, 0.0, 1.0, (double) p_76073_.getHeight(blockGetter1, blockPos2), 1.0));
    }
}