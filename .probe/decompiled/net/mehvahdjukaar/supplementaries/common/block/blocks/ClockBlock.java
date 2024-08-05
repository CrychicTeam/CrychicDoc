package net.mehvahdjukaar.supplementaries.common.block.blocks;

import net.mehvahdjukaar.moonlight.api.block.WaterBlock;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.moonlight.api.util.math.MthUtils;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.tiles.ClockBlockTile;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClockBlock extends WaterBlock implements EntityBlock {

    protected static final VoxelShape SHAPE_NORTH = Block.box(0.0, 0.0, 1.0, 16.0, 16.0, 16.0);

    protected static final VoxelShape SHAPE_SOUTH = MthUtils.rotateVoxelShape(SHAPE_NORTH, Direction.SOUTH);

    protected static final VoxelShape SHAPE_EAST = MthUtils.rotateVoxelShape(SHAPE_NORTH, Direction.EAST);

    protected static final VoxelShape SHAPE_WEST = MthUtils.rotateVoxelShape(SHAPE_NORTH, Direction.WEST);

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final IntegerProperty HOUR = ModBlockProperties.HOUR;

    public ClockBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(WATERLOGGED, false)).m_61124_(FACING, Direction.NORTH));
    }

    public static void displayCurrentHour(Level world, Player player) {
        int time = (int) (world.getDayTime() + 6000L) % 24000;
        int m = (int) ((float) time % 1000.0F / 1000.0F * 60.0F);
        int h = time / 1000;
        String a = "";
        String ob = "";
        String br = "";
        if (!world.dimensionType().natural()) {
            time = world.random.nextInt(24000);
            ob = ob + ChatFormatting.OBFUSCATED;
            br = br + ChatFormatting.RESET;
        }
        if (!(Boolean) ClientConfigs.Blocks.CLOCK_24H.get()) {
            a = time < 12000 ? " AM" : " PM";
            h %= 12;
            if (h == 0) {
                h = 12;
            }
        }
        String text = ob + h + br + ":" + ob + (m < 10 ? "0" : "") + m + br + a;
        player.displayClientMessage(Component.literal(text), true);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.isClientSide()) {
            displayCurrentHour(worldIn, player);
        }
        return InteractionResult.sidedSuccess(worldIn.isClientSide);
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
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean flag = context.m_43725_().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(WATERLOGGED, flag)).m_61124_(FACING, context.m_8125_().getOpposite());
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

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ClockBlockTile(pPos, pState);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.m_60734_() != newState.m_60734_()) {
            if (world.getBlockEntity(pos) instanceof ClockBlockTile) {
                world.updateNeighbourForOutputSignal(pos, this);
            }
            super.m_6810_(state, world, pos, newState, isMoving);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HOUR, FACING, WATERLOGGED);
    }

    @Override
    public boolean hasAnalogOutputSignal(@NotNull BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level world, BlockPos pos) {
        return world.dimensionType().natural() && world.getBlockEntity(pos) instanceof ClockBlockTile tile ? tile.getPower() : 0;
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.m_6807_(state, worldIn, pos, oldState, isMoving);
        if (worldIn.getBlockEntity(pos) instanceof ClockBlockTile tile) {
            tile.updateInitialTime(worldIn, state, pos);
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return Utils.getTicker(pBlockEntityType, (BlockEntityType) ModRegistry.CLOCK_BLOCK_TILE.get(), ClockBlockTile::tick);
    }
}