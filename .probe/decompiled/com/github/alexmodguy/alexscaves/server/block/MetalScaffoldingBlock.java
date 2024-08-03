package com.github.alexmodguy.alexscaves.server.block;

import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.ScaffoldingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MetalScaffoldingBlock extends Block implements BucketPickup, LiquidBlockContainer {

    private static final VoxelShape STABLE_SHAPE;

    private static final VoxelShape UNSTABLE_SHAPE;

    private static final VoxelShape UNSTABLE_SHAPE_BOTTOM = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);

    private static final VoxelShape BELOW_BLOCK = Shapes.block().move(0.0, -1.0, 0.0);

    public static final int STABILITY_MAX_DISTANCE = 12;

    public static final IntegerProperty DISTANCE = IntegerProperty.create("distance", 0, 12);

    public static final IntegerProperty LIQUID_LOGGED = IntegerProperty.create("liquid_logged", 0, 2);

    public static final BooleanProperty BOTTOM = BlockStateProperties.BOTTOM;

    public MetalScaffoldingBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().noOcclusion().strength(5.0F, 15.0F).sound(ACSoundTypes.METAL_SCAFFOLDING));
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(DISTANCE, 12)).m_61124_(LIQUID_LOGGED, 0)).m_61124_(BOTTOM, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DISTANCE, LIQUID_LOGGED, BOTTOM);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return Shapes.block();
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter getter, BlockPos pos) {
        return Shapes.block();
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return this.isScaffoldingItem(context.m_43722_());
    }

    public boolean isScaffoldingItem(ItemStack stack) {
        return stack.getItem() instanceof BlockItem blockItem ? blockItem.getBlock().defaultBlockState().m_204336_(ACTagRegistry.SCAFFOLDING) : false;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        Level level = context.m_43725_();
        int i = getDistance(level, blockpos);
        int fluid = this.getLiquidType(level.getFluidState(blockpos));
        BlockState defaultState = fluid == 2 ? ACBlockRegistry.RUSTY_SCAFFOLDING.get().defaultBlockState() : this.m_49966_();
        return (BlockState) ((BlockState) ((BlockState) defaultState.m_61124_(LIQUID_LOGGED, fluid)).m_61124_(DISTANCE, i)).m_61124_(BOTTOM, this.isBottom(level, blockpos, i));
    }

    private int getLiquidType(FluidState fluidState) {
        if (fluidState.getType() == Fluids.WATER) {
            return 1;
        } else {
            return fluidState.getFluidType() == ACFluidRegistry.ACID_FLUID_TYPE.get() && fluidState.isSource() ? 2 : 0;
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState blockState, boolean forced) {
        if (!level.isClientSide) {
            level.m_186460_(pos, this, 1);
        }
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState1, LevelAccessor levelAccessor, BlockPos pos, BlockPos pos1) {
        int liquidType = (Integer) blockState.m_61143_(LIQUID_LOGGED);
        if (liquidType == 1) {
            levelAccessor.scheduleTick(pos, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor));
        } else if (liquidType == 2) {
            levelAccessor.scheduleTick(pos, ACFluidRegistry.ACID_FLUID_SOURCE.get(), ACFluidRegistry.ACID_FLUID_SOURCE.get().m_6718_(levelAccessor));
        }
        if (!levelAccessor.m_5776_()) {
            levelAccessor.scheduleTick(pos, this, 1);
        }
        return blockState;
    }

    @Override
    public void tick(BlockState blockState, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        int i = getDistance(level, pos);
        BlockState blockstate = (BlockState) ((BlockState) blockState.m_61124_(DISTANCE, i)).m_61124_(BOTTOM, this.isBottom(level, pos, i));
        if ((Integer) blockstate.m_61143_(DISTANCE) == 12) {
            if ((Integer) blockState.m_61143_(DISTANCE) == 12) {
                FallingBlockEntity.fall(level, pos, blockstate);
            } else {
                level.m_46961_(pos, true);
            }
        } else if (blockState != blockstate) {
            level.m_7731_(pos, blockstate, 3);
        }
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        return getDistance(levelReader1, blockPos2) < 12;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        if (context.isAbove(Shapes.block(), pos, true) && !context.isDescending()) {
            return STABLE_SHAPE;
        } else {
            return state.m_61143_(DISTANCE) != 0 && state.m_61143_(BOTTOM) && context.isAbove(BELOW_BLOCK, pos, true) ? UNSTABLE_SHAPE_BOTTOM : Shapes.empty();
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        int liquidType = (Integer) state.m_61143_(LIQUID_LOGGED);
        return liquidType == 1 ? Fluids.WATER.getSource(false) : (liquidType == 2 ? ACFluidRegistry.ACID_FLUID_SOURCE.get().getSource(false) : super.m_5888_(state));
    }

    private boolean isBottom(BlockGetter getter, BlockPos pos, int dist) {
        return dist > 0 && !getter.getBlockState(pos.below()).m_60713_(this);
    }

    public boolean isScaffolding(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
        return true;
    }

    public static int getDistance(BlockGetter getter, BlockPos pos) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = pos.mutable().move(Direction.DOWN);
        BlockState blockstate = getter.getBlockState(blockpos$mutableblockpos);
        int i = 12;
        if (blockstate.m_60713_(Blocks.SCAFFOLDING)) {
            i = (Integer) blockstate.m_61143_(ScaffoldingBlock.DISTANCE);
        } else if (blockstate.m_60734_() instanceof MetalScaffoldingBlock) {
            i = (Integer) blockstate.m_61143_(DISTANCE);
        } else if (blockstate.m_204336_(ACTagRegistry.SCAFFOLDING) || blockstate.m_60783_(getter, blockpos$mutableblockpos, Direction.UP)) {
            return 0;
        }
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockState blockstate1 = getter.getBlockState(blockpos$mutableblockpos.setWithOffset(pos, direction));
            if (blockstate1.m_204336_(ACTagRegistry.SCAFFOLDING)) {
                if (blockstate1.m_60734_() instanceof MetalScaffoldingBlock) {
                    i = Math.min(i, (Integer) blockstate1.m_61143_(DISTANCE) + 1);
                } else if (blockstate1.m_60734_() instanceof ScaffoldingBlock) {
                    i = Math.min(i, (Integer) blockstate1.m_61143_(ScaffoldingBlock.DISTANCE) + 1);
                }
                if (i == 1) {
                    break;
                }
            }
        }
        return i;
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter getter, BlockPos blockPos, BlockState blockState, Fluid fluid) {
        return fluid == Fluids.WATER || fluid.getFluidType() == ACFluidRegistry.ACID_FLUID_TYPE.get();
    }

    @Override
    public boolean placeLiquid(LevelAccessor levelAccessor, BlockPos pos, BlockState blockState, FluidState fluidState) {
        int liquidType = (Integer) blockState.m_61143_(LIQUID_LOGGED);
        if (liquidType == 0) {
            if (!levelAccessor.m_5776_()) {
                if (fluidState.getType() == Fluids.WATER) {
                    levelAccessor.m_7731_(pos, (BlockState) blockState.m_61124_(LIQUID_LOGGED, 1), 3);
                } else if (fluidState.getFluidType() == ACFluidRegistry.ACID_FLUID_TYPE.get()) {
                    BlockState state = blockState;
                    if (blockState.m_60734_() == ACBlockRegistry.METAL_SCAFFOLDING.get()) {
                        levelAccessor.levelEvent(1501, pos, 0);
                        state = (BlockState) ACBlockRegistry.RUSTY_SCAFFOLDING.get().defaultBlockState().m_61124_(DISTANCE, (Integer) blockState.m_61143_(DISTANCE));
                    }
                    levelAccessor.m_7731_(pos, (BlockState) state.m_61124_(LIQUID_LOGGED, 2), 3);
                }
                levelAccessor.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(levelAccessor));
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ItemStack pickupBlock(LevelAccessor levelAccessor, BlockPos blockPos, BlockState state) {
        int liquidType = (Integer) state.m_61143_(LIQUID_LOGGED);
        if (liquidType > 0) {
            levelAccessor.m_7731_(blockPos, (BlockState) state.m_61124_(LIQUID_LOGGED, 0), 3);
            if (!state.m_60710_(levelAccessor, blockPos)) {
                levelAccessor.m_46961_(blockPos, true);
            }
            return new ItemStack((ItemLike) (liquidType == 1 ? Items.WATER_BUCKET : ACItemRegistry.ACID_BUCKET.get()));
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Fluids.WATER.m_142520_();
    }

    static {
        VoxelShape voxelshape = Block.box(0.0, 14.0, 0.0, 16.0, 16.0, 16.0);
        VoxelShape voxelshape1 = Block.box(0.0, 0.0, 0.0, 2.0, 16.0, 2.0);
        VoxelShape voxelshape2 = Block.box(14.0, 0.0, 0.0, 16.0, 16.0, 2.0);
        VoxelShape voxelshape3 = Block.box(0.0, 0.0, 14.0, 2.0, 16.0, 16.0);
        VoxelShape voxelshape4 = Block.box(14.0, 0.0, 14.0, 16.0, 16.0, 16.0);
        STABLE_SHAPE = Shapes.or(voxelshape, voxelshape1, voxelshape2, voxelshape3, voxelshape4);
        VoxelShape voxelshape5 = Block.box(0.0, 0.0, 0.0, 2.0, 2.0, 16.0);
        VoxelShape voxelshape6 = Block.box(14.0, 0.0, 0.0, 16.0, 2.0, 16.0);
        VoxelShape voxelshape7 = Block.box(0.0, 0.0, 14.0, 16.0, 2.0, 16.0);
        VoxelShape voxelshape8 = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 2.0);
        UNSTABLE_SHAPE = Shapes.or(MetalScaffoldingBlock.UNSTABLE_SHAPE_BOTTOM, STABLE_SHAPE, voxelshape6, voxelshape5, voxelshape8, voxelshape7);
    }
}