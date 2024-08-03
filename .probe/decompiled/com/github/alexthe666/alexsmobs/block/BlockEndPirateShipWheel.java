package com.github.alexthe666.alexsmobs.block;

import com.github.alexthe666.alexsmobs.tileentity.AMTileEntityRegistry;
import com.github.alexthe666.alexsmobs.tileentity.TileEntityEndPirateShipWheel;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockEndPirateShipWheel extends BaseEntityBlock implements AMSpecialRenderBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    private static final VoxelShape SOUTH_AABB = Block.box(-2.0, -2.0, 0.0, 18.0, 18.0, 3.0);

    private static final VoxelShape NORTH_AABB = Block.box(-2.0, -2.0, 13.0, 18.0, 18.0, 16.0);

    private static final VoxelShape EAST_AABB = Block.box(0.0, -2.0, -2.0, 3.0, 18.0, 18.0);

    private static final VoxelShape WEST_AABB = Block.box(13.0, -2.0, -2.0, 16.0, 18.0, 18.0);

    private static final VoxelShape UP_AABB = Block.box(-2.0, 0.0, -2.0, 18.0, 3.0, 18.0);

    private static final VoxelShape DOWN_AABB = Block.box(-2.0, 13.0, -2.0, 16.0, 16.0, 18.0);

    public BlockEndPirateShipWheel() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE).noOcclusion().sound(SoundType.ANCIENT_DEBRIS).strength(1.0F).lightLevel(i -> 3).noCollission().requiresCorrectToolForDrops());
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor level, BlockPos pos, BlockPos blockPos0) {
        return !state.m_60710_(level, pos) ? Blocks.AIR.defaultBlockState() : super.m_7417_(state, direction, state2, level, pos, blockPos0);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return switch((Direction) blockState0.m_61143_(FACING)) {
            case NORTH ->
                NORTH_AABB;
            case SOUTH ->
                SOUTH_AABB;
            case EAST ->
                EAST_AABB;
            case WEST ->
                WEST_AABB;
            case UP ->
                UP_AABB;
            default ->
                DOWN_AABB;
        };
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        boolean remove = false;
        Direction dir = ((Direction) state.m_61143_(FACING)).getOpposite();
        BlockPos offset = pos.relative(dir);
        return remove || world.m_8055_(offset).m_60783_(world, offset, dir.getOpposite());
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.getBlockEntity(pos) instanceof TileEntityEndPirateShipWheel wheel) {
            boolean clockwise = false;
            Vec3 offset = hit.m_82450_().subtract((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_());
            switch((Direction) state.m_61143_(FACING)) {
                case NORTH:
                    clockwise = offset.x <= 0.5;
                    break;
                case SOUTH:
                    clockwise = offset.x >= 0.5;
                    break;
                case EAST:
                    clockwise = offset.z <= 0.5;
                    break;
                case WEST:
                    clockwise = offset.z >= 0.5;
            }
            wheel.rotate(clockwise);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityEndPirateShipWheel(pos, state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) this.m_49966_().m_61124_(FACING, context.m_8125_().getOpposite());
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState blockState1, BlockEntityType<T> blockEntityTypeT2) {
        return m_152132_(blockEntityTypeT2, AMTileEntityRegistry.END_PIRATE_SHIP_WHEEL.get(), TileEntityEndPirateShipWheel::commonTick);
    }
}