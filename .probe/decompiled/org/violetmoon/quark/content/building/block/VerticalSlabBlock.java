package org.violetmoon.quark.content.building.block;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.zeta.block.ext.IZetaBlockExtensions;
import org.violetmoon.zeta.registry.IZetaBlockColorProvider;
import org.violetmoon.zeta.registry.IZetaItemColorProvider;
import org.violetmoon.zeta.util.handler.FuelHandler;

public class VerticalSlabBlock extends Block implements SimpleWaterloggedBlock, IZetaBlockColorProvider, IZetaBlockExtensions, FuelHandler.ICustomWoodFuelValue {

    public static final EnumProperty<VerticalSlabBlock.VerticalSlabType> TYPE = EnumProperty.create("type", VerticalSlabBlock.VerticalSlabType.class);

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public final Supplier<Block> parent;

    public VerticalSlabBlock(Supplier<Block> parent, BlockBehaviour.Properties properties) {
        super(properties);
        this.parent = parent;
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(TYPE, VerticalSlabBlock.VerticalSlabType.NORTH)).m_61124_(WATERLOGGED, false));
    }

    @Override
    public boolean isFlammableZeta(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        BlockState parentState = ((Block) this.parent.get()).defaultBlockState();
        return Quark.ZETA.blockExtensions.get(parentState).isFlammableZeta(parentState, world, pos, face);
    }

    @Override
    public int getFlammabilityZeta(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        BlockState parentState = ((Block) this.parent.get()).defaultBlockState();
        return Quark.ZETA.blockExtensions.get(parentState).getFlammabilityZeta(parentState, world, pos, face);
    }

    @NotNull
    @Override
    public BlockState rotate(BlockState state, @NotNull Rotation rot) {
        return state.m_61143_(TYPE) == VerticalSlabBlock.VerticalSlabType.DOUBLE ? state : (BlockState) state.m_61124_(TYPE, VerticalSlabBlock.VerticalSlabType.fromDirection(rot.rotate(((VerticalSlabBlock.VerticalSlabType) state.m_61143_(TYPE)).direction)));
    }

    @NotNull
    @Override
    public BlockState mirror(BlockState state, @NotNull Mirror mirrorIn) {
        VerticalSlabBlock.VerticalSlabType type = (VerticalSlabBlock.VerticalSlabType) state.m_61143_(TYPE);
        if (type != VerticalSlabBlock.VerticalSlabType.DOUBLE && mirrorIn != Mirror.NONE) {
            return (mirrorIn != Mirror.LEFT_RIGHT || type.direction.getAxis() != Direction.Axis.Z) && (mirrorIn != Mirror.FRONT_BACK || type.direction.getAxis() != Direction.Axis.X) ? state : (BlockState) state.m_61124_(TYPE, VerticalSlabBlock.VerticalSlabType.fromDirection(((VerticalSlabBlock.VerticalSlabType) state.m_61143_(TYPE)).direction.getOpposite()));
        } else {
            return state;
        }
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return state.m_61143_(TYPE) != VerticalSlabBlock.VerticalSlabType.DOUBLE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE, WATERLOGGED);
    }

    @NotNull
    @Override
    public VoxelShape getShape(BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return ((VerticalSlabBlock.VerticalSlabType) state.m_61143_(TYPE)).shape;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = context.m_43725_().getBlockState(blockpos);
        if (blockstate.m_60734_() == this) {
            return (BlockState) ((BlockState) blockstate.m_61124_(TYPE, VerticalSlabBlock.VerticalSlabType.DOUBLE)).m_61124_(WATERLOGGED, false);
        } else {
            FluidState fluid = context.m_43725_().getFluidState(blockpos);
            BlockState retState = (BlockState) this.m_49966_().m_61124_(WATERLOGGED, fluid.getType() == Fluids.WATER);
            Direction direction = this.getDirectionForPlacement(context);
            VerticalSlabBlock.VerticalSlabType type = VerticalSlabBlock.VerticalSlabType.fromDirection(direction);
            return (BlockState) retState.m_61124_(TYPE, type);
        }
    }

    private Direction getDirectionForPlacement(BlockPlaceContext context) {
        Direction direction = context.m_43719_();
        if (direction.getAxis() != Direction.Axis.Y) {
            return direction;
        } else {
            BlockPos pos = context.getClickedPos();
            Vec3 vec = context.m_43720_().subtract(new Vec3((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_())).subtract(0.5, 0.0, 0.5);
            double angle = Math.atan2(vec.x, vec.z) * -180.0 / Math.PI;
            return Direction.fromYRot(angle).getOpposite();
        }
    }

    @Override
    public boolean canBeReplaced(BlockState state, @NotNull BlockPlaceContext useContext) {
        ItemStack itemstack = useContext.m_43722_();
        VerticalSlabBlock.VerticalSlabType slabtype = (VerticalSlabBlock.VerticalSlabType) state.m_61143_(TYPE);
        return slabtype != VerticalSlabBlock.VerticalSlabType.DOUBLE && itemstack.getItem() == this.m_5456_() && (useContext.replacingClickedOnBlock() && useContext.m_43719_() == slabtype.direction && this.getDirectionForPlacement(useContext) == slabtype.direction || !useContext.replacingClickedOnBlock() && useContext.m_43719_() != slabtype.direction);
    }

    @NotNull
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    @Override
    public boolean placeLiquid(@NotNull LevelAccessor worldIn, @NotNull BlockPos pos, BlockState state, @NotNull FluidState fluidStateIn) {
        return state.m_61143_(TYPE) != VerticalSlabBlock.VerticalSlabType.DOUBLE && SimpleWaterloggedBlock.super.placeLiquid(worldIn, pos, state, fluidStateIn);
    }

    @Override
    public boolean canPlaceLiquid(@NotNull BlockGetter worldIn, @NotNull BlockPos pos, BlockState state, @NotNull Fluid fluidIn) {
        return state.m_61143_(TYPE) != VerticalSlabBlock.VerticalSlabType.DOUBLE && SimpleWaterloggedBlock.super.canPlaceLiquid(worldIn, pos, state, fluidIn);
    }

    @NotNull
    @Override
    public BlockState updateShape(@NotNull BlockState stateIn, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor worldIn, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
        if ((Boolean) stateIn.m_61143_(WATERLOGGED)) {
            worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.m_6718_(worldIn));
        }
        return super.m_7417_(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public boolean isPathfindable(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull PathComputationType type) {
        return type == PathComputationType.WATER && worldIn.getFluidState(pos).is(FluidTags.WATER);
    }

    @Nullable
    @Override
    public String getBlockColorProviderName() {
        return this.parent.get() instanceof IZetaBlockColorProvider prov ? prov.getBlockColorProviderName() : null;
    }

    @Nullable
    @Override
    public String getItemColorProviderName() {
        return this.parent.get() instanceof IZetaItemColorProvider prov ? prov.getItemColorProviderName() : null;
    }

    @Override
    public int getBurnTimeInTicksWhenWooden() {
        return 150;
    }

    public static enum VerticalSlabType implements StringRepresentable {

        NORTH(Direction.NORTH), SOUTH(Direction.SOUTH), WEST(Direction.WEST), EAST(Direction.EAST), DOUBLE(null);

        private final String name;

        public final Direction direction;

        public final VoxelShape shape;

        private VerticalSlabType(Direction direction) {
            this.name = direction == null ? "double" : direction.getSerializedName();
            this.direction = direction;
            if (direction == null) {
                this.shape = Shapes.block();
            } else {
                double min = 0.0;
                double max = 8.0;
                if (direction.getAxisDirection() == Direction.AxisDirection.NEGATIVE) {
                    min = 8.0;
                    max = 16.0;
                }
                if (direction.getAxis() == Direction.Axis.X) {
                    this.shape = Block.box(min, 0.0, 0.0, max, 16.0, 16.0);
                } else {
                    this.shape = Block.box(0.0, 0.0, min, 16.0, 16.0, max);
                }
            }
        }

        public String toString() {
            return this.name;
        }

        @NotNull
        @Override
        public String getSerializedName() {
            return this.name;
        }

        public static VerticalSlabBlock.VerticalSlabType fromDirection(Direction direction) {
            for (VerticalSlabBlock.VerticalSlabType type : values()) {
                if (type.direction != null && direction == type.direction) {
                    return type;
                }
            }
            return null;
        }
    }
}