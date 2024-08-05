package net.mehvahdjukaar.supplementaries.common.block.blocks;

import net.mehvahdjukaar.moonlight.api.block.WaterBlock;
import net.mehvahdjukaar.moonlight.api.util.math.MthUtils;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CrystalDisplayBlock extends WaterBlock {

    public static final IntegerProperty POWER = BlockStateProperties.POWER;

    public static final BooleanProperty ATTACHED = BlockStateProperties.ATTACHED;

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    protected static final VoxelShape SHAPE_NORTH = Block.box(0.0, 0.0, 12.0, 16.0, 16.0, 16.0);

    protected static final VoxelShape SHAPE_SOUTH = MthUtils.rotateVoxelShape(SHAPE_NORTH, Direction.SOUTH);

    protected static final VoxelShape SHAPE_EAST = MthUtils.rotateVoxelShape(SHAPE_NORTH, Direction.EAST);

    protected static final VoxelShape SHAPE_WEST = MthUtils.rotateVoxelShape(SHAPE_NORTH, Direction.WEST);

    public CrystalDisplayBlock(BlockBehaviour.Properties properties) {
        super(properties.lightLevel(state -> state.m_61143_(POWER) != 0 ? 6 : 0));
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(WATERLOGGED, false)).m_61124_(ATTACHED, false)).m_61124_(FACING, Direction.NORTH)).m_61124_(POWER, 0));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean flag = context.m_43725_().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, context.m_8125_().getOpposite())).m_61124_(WATERLOGGED, flag);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWER, FACING, WATERLOGGED, ATTACHED);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return (BlockState) state.m_61124_(FACING, rot.rotate((Direction) state.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.m_60717_(mirrorIn.getRotation((Direction) state.m_61143_(FACING)));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch((Direction) state.m_61143_(FACING)) {
            case SOUTH ->
                SHAPE_SOUTH;
            case EAST ->
                SHAPE_EAST;
            case WEST ->
                SHAPE_WEST;
            default ->
                SHAPE_NORTH;
        };
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        this.updatePower(state, worldIn, pos);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos fromPos, boolean moving) {
        super.m_6861_(state, level, pos, neighborBlock, fromPos, moving);
        this.updatePower(state, level, pos);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    private void updatePower(BlockState state, Level world, BlockPos pos) {
        if (!world.isClientSide) {
            int power = Mth.clamp(world.m_277086_(pos), 0, 15);
            Direction dir = (Direction) state.m_61143_(FACING);
            state = (BlockState) state.m_61124_(ATTACHED, false);
            if ((Boolean) CommonConfigs.Redstone.CRYSTAL_DISPLAY_CHAINED.get()) {
                if (power > 10) {
                    BlockPos slavePos = pos.relative(dir.getClockWise());
                    BlockState slaveState = world.getBlockState(slavePos);
                    if (slaveState.m_60713_(this) && slaveState.m_61143_(FACING) == dir && !world.m_276987_(slavePos.relative(dir.getOpposite()), dir)) {
                        power %= 10;
                    }
                } else {
                    BlockPos masterPos = pos.relative(dir.getCounterClockWise());
                    if (world.getBlockState(masterPos).m_60713_(this) && world.getBlockState(masterPos).m_61143_(FACING) == dir && !world.m_276987_(pos.relative(dir.getOpposite()), dir)) {
                        int masterPower = world.m_277086_(masterPos);
                        if (masterPower >= 10) {
                            power = Math.max(power, masterPower / 10);
                            state = (BlockState) state.m_61124_(ATTACHED, true);
                        }
                    }
                }
            }
            world.setBlock(pos, (BlockState) state.m_61124_(POWER, power), 3);
        }
    }
}