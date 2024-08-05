package com.github.alexmodguy.alexscaves.server.block;

import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SulfurBudBlock extends Block implements SimpleWaterloggedBlock {

    public static final IntegerProperty LIQUID_LOGGED = IntegerProperty.create("liquid_logged", 0, 2);

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    private final Map<Direction, VoxelShape> shapeMap;

    public SulfurBudBlock(int pixWidth, int pixHeight) {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).strength(1.0F, 2.0F).sound(ACSoundTypes.SULFUR).randomTicks());
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(LIQUID_LOGGED, 0)).m_61124_(FACING, Direction.UP));
        this.shapeMap = buildShapeMap(pixWidth, pixHeight);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction direction = (Direction) state.m_61143_(FACING);
        BlockPos blockpos = pos.relative(direction.getOpposite());
        return level.m_8055_(blockpos).m_60783_(level, blockpos, direction);
    }

    @Override
    public void randomTick(BlockState currentState, ServerLevel level, BlockPos blockPos, RandomSource randomSource) {
        if (randomSource.nextInt(3) == 0 && !currentState.m_60713_(ACBlockRegistry.SULFUR_CLUSTER.get())) {
            BlockPos acidAbove = blockPos.above();
            while (level.m_8055_(acidAbove).m_60795_() && acidAbove.m_123342_() < level.m_151558_()) {
                acidAbove = acidAbove.above();
            }
            BlockState acidState = level.m_8055_(acidAbove);
            Block block = null;
            if (acidState.m_60713_(ACBlockRegistry.ACIDIC_RADROCK.get()) || (Integer) currentState.m_61143_(LIQUID_LOGGED) == 2) {
                if (currentState.m_60713_(ACBlockRegistry.SULFUR_BUD_SMALL.get())) {
                    block = ACBlockRegistry.SULFUR_BUD_MEDIUM.get();
                } else if (currentState.m_60713_(ACBlockRegistry.SULFUR_BUD_MEDIUM.get())) {
                    block = ACBlockRegistry.SULFUR_BUD_LARGE.get();
                } else if (currentState.m_60713_(ACBlockRegistry.SULFUR_BUD_LARGE.get())) {
                    block = ACBlockRegistry.SULFUR_CLUSTER.get();
                }
            }
            if (block != null) {
                BlockState blockstate1 = (BlockState) ((BlockState) block.defaultBlockState().m_61124_(FACING, (Direction) currentState.m_61143_(FACING))).m_61124_(LIQUID_LOGGED, (Integer) currentState.m_61143_(LIQUID_LOGGED));
                level.m_46597_(blockPos, blockstate1);
            }
        }
    }

    public static Map<Direction, VoxelShape> buildShapeMap(int pixWidth, int pixHeight) {
        Map<Direction, VoxelShape> map = new HashMap();
        map.put(Direction.UP, Block.box((double) (8 - pixWidth / 2), 0.0, (double) (8 - pixWidth / 2), (double) (8 + pixWidth / 2), (double) pixHeight, (double) (8 + pixWidth / 2)));
        map.put(Direction.DOWN, Block.box((double) (8 - pixWidth / 2), (double) (16 - pixHeight), (double) (8 - pixWidth / 2), (double) (8 + pixWidth / 2), 16.0, (double) (8 + pixWidth / 2)));
        map.put(Direction.NORTH, Block.box((double) (8 - pixWidth / 2), (double) (8 - pixWidth / 2), 0.0, (double) (8 + pixWidth / 2), (double) (8 + pixWidth / 2), (double) pixHeight));
        map.put(Direction.SOUTH, Block.box((double) (8 - pixWidth / 2), (double) (8 - pixWidth / 2), (double) (16 - pixHeight), (double) (8 + pixWidth / 2), (double) (8 + pixWidth / 2), 16.0));
        map.put(Direction.EAST, Block.box(0.0, (double) (8 - pixWidth / 2), (double) (8 - pixWidth / 2), (double) pixHeight, (double) (8 + pixWidth / 2), (double) (8 + pixWidth / 2)));
        map.put(Direction.WEST, Block.box((double) (16 - pixHeight), (double) (8 - pixWidth / 2), (double) (8 - pixWidth / 2), 16.0, (double) (8 + pixWidth / 2), (double) (8 + pixWidth / 2)));
        return map;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        int liquidType = (Integer) state.m_61143_(LIQUID_LOGGED);
        if (liquidType == 1) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor));
        } else if (liquidType == 2) {
            levelAccessor.scheduleTick(blockPos, ACFluidRegistry.ACID_FLUID_SOURCE.get(), ACFluidRegistry.ACID_FLUID_SOURCE.get().m_6718_(levelAccessor));
        }
        if (!levelAccessor.m_5776_()) {
            levelAccessor.scheduleTick(blockPos, this, 1);
        }
        return direction == ((Direction) state.m_61143_(FACING)).getOpposite() && !state.m_60710_(levelAccessor, blockPos) ? Blocks.AIR.defaultBlockState() : super.m_7417_(state, direction, state1, levelAccessor, blockPos, blockPos1);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return (VoxelShape) this.shapeMap.get(state.m_61143_(FACING));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor levelaccessor = context.m_43725_();
        BlockPos blockpos = context.getClickedPos();
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(LIQUID_LOGGED, getLiquidType(levelaccessor.m_6425_(blockpos)))).m_61124_(FACING, context.m_43719_());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState) state.m_61124_(FACING, rotation.rotate((Direction) state.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.m_60717_(mirror.getRotation((Direction) state.m_61143_(FACING)));
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
                    levelAccessor.m_7731_(pos, (BlockState) blockState.m_61124_(LIQUID_LOGGED, 2), 3);
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

    @Override
    public FluidState getFluidState(BlockState state) {
        int liquidType = (Integer) state.m_61143_(LIQUID_LOGGED);
        return liquidType == 1 ? Fluids.WATER.getSource(false) : (liquidType == 2 ? ACFluidRegistry.ACID_FLUID_SOURCE.get().getSource(false) : super.m_5888_(state));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(LIQUID_LOGGED, FACING);
    }

    public static int getLiquidType(FluidState fluidState) {
        if (fluidState.getType() == Fluids.WATER) {
            return 1;
        } else {
            return fluidState.getFluidType() == ACFluidRegistry.ACID_FLUID_TYPE.get() && fluidState.isSource() ? 2 : 0;
        }
    }

    public PushReaction getPistonPushReaction(BlockState blockState) {
        return PushReaction.DESTROY;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource randomSource) {
    }
}