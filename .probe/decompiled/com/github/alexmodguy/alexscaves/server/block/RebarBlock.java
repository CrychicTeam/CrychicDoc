package com.github.alexmodguy.alexscaves.server.block;

import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RebarBlock extends Block implements BucketPickup, LiquidBlockContainer {

    public static final BooleanProperty CONNECT_X = BooleanProperty.create("connect_x");

    public static final BooleanProperty CONNECT_Y = BooleanProperty.create("connect_y");

    public static final BooleanProperty CONNECT_Z = BooleanProperty.create("connect_z");

    public final Map<BlockState, VoxelShape> shapeMap = new HashMap();

    public static final VoxelShape CENTER_SHAPE = Block.box(7.0, 7.0, 7.0, 9.0, 9.0, 9.0);

    public static final VoxelShape X_SHAPE = Block.box(0.0, 7.0, 7.0, 16.0, 9.0, 9.0);

    public static final VoxelShape Y_SHAPE = Block.box(7.0, 0.0, 7.0, 9.0, 16.0, 9.0);

    public static final VoxelShape Z_SHAPE = Block.box(7.0, 7.0, 0.0, 9.0, 9.0, 16.0);

    public static final IntegerProperty LIQUID_LOGGED = IntegerProperty.create("liquid_logged", 0, 2);

    public RebarBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(2.0F).sound(ACSoundTypes.SCRAP_METAL));
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(CONNECT_X, false)).m_61124_(CONNECT_Y, true)).m_61124_(CONNECT_Z, false)).m_61124_(LIQUID_LOGGED, 0));
    }

    protected VoxelShape getRebarShape(BlockState state) {
        if (this.shapeMap.containsKey(state)) {
            return (VoxelShape) this.shapeMap.get(state);
        } else {
            VoxelShape merge = CENTER_SHAPE;
            if ((Boolean) state.m_61143_(CONNECT_X)) {
                merge = Shapes.join(merge, X_SHAPE, BooleanOp.OR);
            }
            if ((Boolean) state.m_61143_(CONNECT_Y)) {
                merge = Shapes.join(merge, Y_SHAPE, BooleanOp.OR);
            }
            if ((Boolean) state.m_61143_(CONNECT_Z)) {
                merge = Shapes.join(merge, Z_SHAPE, BooleanOp.OR);
            }
            this.shapeMap.put(state, merge);
            return merge;
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return this.getRebarShape(state);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        BlockState desired = this.getDesiredRebarState(state, context.m_43719_().getAxis());
        return !context.m_7078_() && context.m_43722_().is(this.m_5456_()) && (state.m_61143_(CONNECT_X) != desired.m_61143_(CONNECT_X) || state.m_61143_(CONNECT_Y) != desired.m_61143_(CONNECT_Y) || state.m_61143_(CONNECT_Z) != desired.m_61143_(CONNECT_Z)) || super.m_6864_(state, context);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = context.m_43725_().getBlockState(context.getClickedPos());
        int fluid = this.getLiquidType(context.m_43725_().getFluidState(context.getClickedPos()));
        return (BlockState) this.getDesiredRebarState(blockstate, context.m_43719_().getAxis()).m_61124_(LIQUID_LOGGED, fluid);
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
    public FluidState getFluidState(BlockState state) {
        int liquidType = (Integer) state.m_61143_(LIQUID_LOGGED);
        return liquidType == 1 ? Fluids.WATER.getSource(false) : (liquidType == 2 ? ACFluidRegistry.ACID_FLUID_SOURCE.get().getSource(false) : super.m_5888_(state));
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
                    if (blockState.m_60734_() == ACBlockRegistry.METAL_REBAR.get()) {
                        levelAccessor.levelEvent(1501, pos, 0);
                        state = (BlockState) ((BlockState) ((BlockState) ACBlockRegistry.RUSTY_REBAR.get().defaultBlockState().m_61124_(CONNECT_X, (Boolean) blockState.m_61143_(CONNECT_X))).m_61124_(CONNECT_Y, (Boolean) blockState.m_61143_(CONNECT_Y))).m_61124_(CONNECT_Z, (Boolean) blockState.m_61143_(CONNECT_Z));
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
        levelAccessor.m_7731_(blockPos, (BlockState) state.m_61124_(LIQUID_LOGGED, 0), 3);
        return liquidType > 0 ? new ItemStack((ItemLike) (liquidType == 1 ? Items.WATER_BUCKET : ACItemRegistry.ACID_BUCKET.get())) : ItemStack.EMPTY;
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Fluids.WATER.m_142520_();
    }

    private int getLiquidType(FluidState fluidState) {
        if (fluidState.getType() == Fluids.WATER) {
            return 1;
        } else {
            return fluidState.getFluidType() == ACFluidRegistry.ACID_FLUID_TYPE.get() && fluidState.isSource() ? 2 : 0;
        }
    }

    public BlockState getDesiredRebarState(BlockState blockstate, Direction.Axis clickedAxis) {
        boolean xAxis = false;
        boolean yAxis = false;
        boolean zAxis = false;
        if (blockstate.m_60713_(this)) {
            xAxis = (Boolean) blockstate.m_61143_(CONNECT_X);
            yAxis = (Boolean) blockstate.m_61143_(CONNECT_Y);
            zAxis = (Boolean) blockstate.m_61143_(CONNECT_Z);
        }
        if (clickedAxis == Direction.Axis.X) {
            xAxis = true;
        } else if (clickedAxis == Direction.Axis.Y) {
            yAxis = true;
        } else if (clickedAxis == Direction.Axis.Z) {
            zAxis = true;
        }
        return (BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(CONNECT_X, xAxis)).m_61124_(CONNECT_Y, yAxis)).m_61124_(CONNECT_Z, zAxis);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack stack) {
        super.playerDestroy(level, player, blockPos, blockState, blockEntity, stack);
        Vec3 findDirOf = player.m_146892_().subtract(Vec3.atCenterOf(blockPos));
        BlockState set = blockState;
        if ((Boolean) blockState.m_61143_(CONNECT_X)) {
            set = (BlockState) blockState.m_61124_(CONNECT_X, false);
        } else if ((Boolean) blockState.m_61143_(CONNECT_Y)) {
            set = (BlockState) blockState.m_61124_(CONNECT_Y, false);
        } else if ((Boolean) blockState.m_61143_(CONNECT_Z)) {
            set = (BlockState) blockState.m_61124_(CONNECT_Z, false);
        }
        if (!(Boolean) set.m_61143_(CONNECT_X) && !(Boolean) set.m_61143_(CONNECT_Y) && !(Boolean) set.m_61143_(CONNECT_Z)) {
            set = Blocks.AIR.defaultBlockState();
        }
        level.setBlock(blockPos, set, 2);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        BlockState def = (BlockState) ((BlockState) this.m_49966_().m_61124_(CONNECT_X, false)).m_61124_(CONNECT_Z, false);
        if (rotation == Rotation.CLOCKWISE_90 || rotation == Rotation.COUNTERCLOCKWISE_90) {
            if ((Boolean) state.m_61143_(CONNECT_X)) {
                def = (BlockState) def.m_61124_(CONNECT_Z, true);
            }
            if ((Boolean) state.m_61143_(CONNECT_Z)) {
                def = (BlockState) def.m_61124_(CONNECT_X, true);
            }
        }
        return def;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CONNECT_X, CONNECT_Y, CONNECT_Z, LIQUID_LOGGED);
    }
}