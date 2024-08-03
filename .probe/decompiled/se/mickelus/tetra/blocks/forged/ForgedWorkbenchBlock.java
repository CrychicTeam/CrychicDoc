package se.mickelus.tetra.blocks.forged;

import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import se.mickelus.tetra.blocks.workbench.AbstractWorkbenchBlock;

@ParametersAreNonnullByDefault
public class ForgedWorkbenchBlock extends AbstractWorkbenchBlock implements SimpleWaterloggedBlock {

    public static final String identifier = "forged_workbench";

    public static final EnumProperty<Direction.Axis> axis = BlockStateProperties.HORIZONTAL_AXIS;

    private static final VoxelShape zShape = Shapes.or(m_49796_(1.0, 0.0, 3.0, 15.0, 2.0, 13.0), m_49796_(2.0, 2.0, 4.0, 14.0, 9.0, 12.0), m_49796_(0.0, 9.0, 2.0, 16.0, 16.0, 14.0));

    private static final VoxelShape xShape = Shapes.or(m_49796_(3.0, 0.0, 1.0, 13.0, 2.0, 15.0), m_49796_(4.0, 2.0, 2.0, 12.0, 9.0, 14.0), m_49796_(2.0, 9.0, 0.0, 14.0, 16.0, 16.0));

    public ForgedWorkbenchBlock() {
        super(ForgedBlockCommon.propertiesSolid);
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(BlockStateProperties.WATERLOGGED, false)).m_61124_(axis, Direction.Axis.X));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag advanced) {
        tooltip.add(ForgedBlockCommon.locationTooltip);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        Direction.Axis axis = (Direction.Axis) state.m_61143_(ForgedWorkbenchBlock.axis);
        return axis == Direction.Axis.Z ? zShape : xShape;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.m_7926_(builder);
        builder.add(BlockStateProperties.WATERLOGGED, axis);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(BlockStateProperties.WATERLOGGED, context.m_43725_().getFluidState(context.getClickedPos()).getType() == Fluids.WATER)).m_61124_(axis, context.m_8125_().getAxis());
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if ((Boolean) stateIn.m_61143_(BlockStateProperties.WATERLOGGED)) {
            worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.m_6718_(worldIn));
        }
        return super.m_7417_(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        switch(rotation) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch((Direction.Axis) blockState.m_61143_(axis)) {
                    case Z:
                        return (BlockState) blockState.m_61124_(axis, Direction.Axis.X);
                    case X:
                        return (BlockState) blockState.m_61124_(axis, Direction.Axis.Z);
                    default:
                        return blockState;
                }
            default:
                return blockState;
        }
    }
}