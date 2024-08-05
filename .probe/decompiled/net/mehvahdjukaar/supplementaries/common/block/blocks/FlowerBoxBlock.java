package net.mehvahdjukaar.supplementaries.common.block.blocks;

import net.mehvahdjukaar.moonlight.api.block.WaterBlock;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.tiles.FlowerBoxBlockTile;
import net.mehvahdjukaar.supplementaries.common.utils.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class FlowerBoxBlock extends WaterBlock implements EntityBlock {

    protected static final VoxelShape SHAPE_SOUTH = Block.box(0.0, 0.0, 0.0, 16.0, 6.0, 6.0);

    protected static final VoxelShape SHAPE_NORTH = Block.box(0.0, 0.0, 10.0, 16.0, 6.0, 16.0);

    protected static final VoxelShape SHAPE_EAST = Block.box(0.0, 0.0, 0.0, 6.0, 6.0, 16.0);

    protected static final VoxelShape SHAPE_WEST = Block.box(10.0, 0.0, 0.0, 16.0, 6.0, 16.0);

    protected static final VoxelShape SHAPE_NORTH_FLOOR = Block.box(0.0, 0.0, 5.0, 16.0, 6.0, 11.0);

    protected static final VoxelShape SHAPE_WEST_FLOOR = Block.box(5.0, 0.0, 0.0, 11.0, 6.0, 16.0);

    public static final IntegerProperty LIGHT_LEVEL = ModBlockProperties.LIGHT_LEVEL_0_15;

    public static final BooleanProperty FLOOR = ModBlockProperties.FLOOR;

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public FlowerBoxBlock(BlockBehaviour.Properties properties) {
        super(properties.lightLevel(s -> (Integer) s.m_61143_(LIGHT_LEVEL)));
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH)).m_61124_(WATERLOGGED, false)).m_61124_(FLOOR, false)).m_61124_(LIGHT_LEVEL, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, FLOOR, LIGHT_LEVEL);
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
        Direction dir = context.m_43719_();
        return (BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(WATERLOGGED, flag)).m_61124_(FLOOR, dir == Direction.UP)).m_61124_(FACING, dir.getAxis().isVertical() ? context.m_8125_().getOpposite() : dir);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.getBlockEntity(pos) instanceof FlowerBoxBlockTile tile && tile.isAccessibleBy(player)) {
            Direction dir = (Direction) state.m_61143_(FACING);
            Vec3 v = hit.m_82450_();
            v = v.subtract((double) pos.m_123341_() - 1.0, 0.0, (double) pos.m_123343_() - 1.0);
            int ind;
            if (dir.getAxis() == Direction.Axis.X) {
                double normalizedZ = Math.abs(v.z % 1.0);
                if (v.z >= 2.0) {
                    ind = 2;
                } else {
                    ind = (int) (normalizedZ / 0.3333333333333333);
                }
                if (dir.getStepX() < 0) {
                    ind = 2 - ind;
                }
            } else {
                double normalizedX = Math.abs(v.x % 1.0);
                if (v.x >= 2.0) {
                    ind = 2;
                } else {
                    ind = (int) (normalizedX / 0.3333333333333333);
                }
                if (dir.getStepZ() > 0) {
                    ind = 2 - ind;
                }
            }
            return tile.interact(player, handIn, ind);
        }
        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new FlowerBoxBlockTile(pPos, pState);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.m_60734_() != newState.m_60734_()) {
            if (world.getBlockEntity(pos) instanceof FlowerBoxBlockTile tile) {
                Containers.dropContents(world, pos, tile);
                world.updateNeighbourForOutputSignal(pos, this);
            }
            super.m_6810_(state, world, pos, newState, isMoving);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        boolean wall = !(Boolean) state.m_61143_(FLOOR);
        return switch((Direction) state.m_61143_(FACING)) {
            case SOUTH ->
                wall ? SHAPE_SOUTH : SHAPE_NORTH_FLOOR;
            case EAST ->
                wall ? SHAPE_EAST : SHAPE_WEST_FLOOR;
            case WEST ->
                wall ? SHAPE_WEST : SHAPE_WEST_FLOOR;
            default ->
                wall ? SHAPE_NORTH : SHAPE_NORTH_FLOOR;
        };
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        BlockUtil.addOptionalOwnership(placer, world, pos);
    }
}