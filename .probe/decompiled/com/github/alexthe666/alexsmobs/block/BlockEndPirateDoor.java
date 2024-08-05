package com.github.alexthe666.alexsmobs.block;

import com.github.alexthe666.alexsmobs.tileentity.AMTileEntityRegistry;
import com.github.alexthe666.alexsmobs.tileentity.TileEntityEndPirateDoor;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockEndPirateDoor extends BaseEntityBlock {

    public static final DirectionProperty HORIZONTAL_FACING = HorizontalDirectionalBlock.FACING;

    public static final BooleanProperty OPEN = BooleanProperty.create("open");

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public static final EnumProperty<DoorHingeSide> HINGE = BlockStateProperties.DOOR_HINGE;

    public static final IntegerProperty SEGMENT = IntegerProperty.create("segment", 0, 2);

    protected static final float AABB_DOOR_THICKNESS = 3.0F;

    protected static final VoxelShape SOUTH_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 2.0);

    protected static final VoxelShape NORTH_AABB = Block.box(0.0, 0.0, 14.0, 16.0, 16.0, 16.0);

    protected static final VoxelShape WEST_AABB = Block.box(14.0, 0.0, 0.0, 16.0, 16.0, 16.0);

    protected static final VoxelShape EAST_AABB = Block.box(0.0, 0.0, 0.0, 2.0, 16.0, 16.0);

    public BlockEndPirateDoor() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).noOcclusion().sound(SoundType.GLASS).lightLevel(state -> 3).requiresCorrectToolForDrops().strength(1.5F));
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(SEGMENT, 0)).m_61124_(OPEN, false)).m_61124_(HINGE, DoorHingeSide.RIGHT)).m_61124_(HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        Direction direction = (Direction) blockState0.m_61143_(HORIZONTAL_FACING);
        boolean flag = !(Boolean) blockState0.m_61143_(OPEN);
        boolean flag1 = blockState0.m_61143_(HINGE) == DoorHingeSide.RIGHT;
        return switch(direction) {
            case SOUTH ->
                flag ? SOUTH_AABB : (flag1 ? EAST_AABB : WEST_AABB);
            case WEST ->
                flag ? WEST_AABB : (flag1 ? SOUTH_AABB : NORTH_AABB);
            case NORTH ->
                flag ? NORTH_AABB : (flag1 ? WEST_AABB : EAST_AABB);
            default ->
                flag ? EAST_AABB : (flag1 ? NORTH_AABB : SOUTH_AABB);
        };
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor level, BlockPos pos, BlockPos blockPos0) {
        if ((Integer) state.m_61143_(SEGMENT) != 0) {
            return !state.m_60710_(level, pos) ? Blocks.AIR.defaultBlockState() : super.m_7417_(state, direction, state2, level, pos, blockPos0);
        } else {
            return state.m_60710_(level, pos) && level.m_8055_(pos.above()).m_60713_(this) && level.m_8055_(pos.above(2)).m_60713_(this) ? super.m_7417_(state, direction, state2, level, pos, blockPos0) : Blocks.AIR.defaultBlockState();
        }
    }

    @Override
    public BlockState rotate(BlockState p_185499_1_, Rotation p_185499_2_) {
        return (BlockState) p_185499_1_.m_61124_(HORIZONTAL_FACING, p_185499_2_.rotate((Direction) p_185499_1_.m_61143_(HORIZONTAL_FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mir) {
        return state.m_60717_(mir.getRotation((Direction) state.m_61143_(HORIZONTAL_FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SEGMENT, HORIZONTAL_FACING, OPEN, HINGE, POWERED);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return state.m_61143_(SEGMENT) == 0 ? RenderShape.ENTITYBLOCK_ANIMATED : RenderShape.INVISIBLE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.m_61143_(SEGMENT) == 0 ? new TileEntityEndPirateDoor(pos, state) : null;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos blockPos0, boolean boolean1) {
        boolean flag = level.m_276867_(pos);
        flag = switch(state.m_61143_(SEGMENT)) {
            case 0 ->
                flag || level.m_276867_(pos.above()) || level.m_276867_(pos.above(2));
            case 1 ->
                flag || level.m_276867_(pos.below()) || level.m_276867_(pos.above());
            case 2 ->
                flag || level.m_276867_(pos.below()) || level.m_276867_(pos.below(2));
            default ->
                flag;
        };
        if (!this.m_49966_().m_60713_(block) && flag != (Boolean) state.m_61143_(POWERED)) {
            if (flag != (Boolean) state.m_61143_(OPEN)) {
            }
            Direction swap = state.m_61143_(HINGE) == DoorHingeSide.LEFT ? ((Direction) state.m_61143_(HORIZONTAL_FACING)).getClockWise() : ((Direction) state.m_61143_(HORIZONTAL_FACING)).getCounterClockWise();
            BlockPos relative = pos.relative(swap);
            BlockState neighbor = level.getBlockState(relative);
            if (neighbor.m_60734_() == this && state.m_61143_(HINGE) != neighbor.m_61143_(HINGE)) {
                openDoorAt(level, relative, flag, flag);
            }
            openDoorAt(level, pos, flag, flag);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        boolean open = (Boolean) state.m_61143_(OPEN);
        boolean powered = (Boolean) state.m_61143_(POWERED);
        Direction swap = state.m_61143_(HINGE) == DoorHingeSide.LEFT ? ((Direction) state.m_61143_(HORIZONTAL_FACING)).getClockWise() : ((Direction) state.m_61143_(HORIZONTAL_FACING)).getCounterClockWise();
        BlockPos relative = pos.relative(swap);
        BlockState neighbor = worldIn.getBlockState(relative);
        if (neighbor.m_60734_() == this && state.m_61143_(HINGE) != neighbor.m_61143_(HINGE)) {
            openDoorAt(worldIn, relative, !open, powered);
        }
        openDoorAt(worldIn, pos, !open, powered);
        return InteractionResult.sidedSuccess(worldIn.isClientSide);
    }

    public static void openDoorAt(Level worldIn, BlockPos pos, boolean open, boolean powered) {
        TileEntityEndPirateDoor te = getDoorTE(worldIn, pos);
        if (te != null) {
            BlockPos bottom = te.m_58899_();
            for (int i = 0; i <= 2; i++) {
                BlockPos up = bottom.above(i);
                if (worldIn.getBlockState(up).m_60734_() instanceof BlockEndPirateDoor) {
                    worldIn.setBlock(up, (BlockState) ((BlockState) worldIn.getBlockState(up).m_61124_(OPEN, open)).m_61124_(POWERED, powered), 10);
                }
            }
        }
    }

    public static TileEntityEndPirateDoor getDoorTE(Level worldIn, BlockPos pos) {
        for (int i = 0; i <= 2; i++) {
            BlockEntity var4 = worldIn.getBlockEntity(pos.below(i));
            if (var4 instanceof TileEntityEndPirateDoor) {
                return (TileEntityEndPirateDoor) var4;
            }
        }
        return null;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        BlockPos blockpos = blockPlaceContext0.getClickedPos();
        Level level = blockPlaceContext0.m_43725_();
        if (blockpos.m_123342_() < level.m_151558_() - 1 && level.getBlockState(blockpos.above()).m_60629_(blockPlaceContext0) && level.getBlockState(blockpos.above(2)).m_60629_(blockPlaceContext0)) {
            boolean flag = level.m_276867_(blockpos) || level.m_276867_(blockpos.above());
            return (BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(HORIZONTAL_FACING, blockPlaceContext0.m_8125_())).m_61124_(HINGE, this.getHinge(blockPlaceContext0))).m_61124_(OPEN, flag)).m_61124_(SEGMENT, 0);
        } else {
            return null;
        }
    }

    @Override
    public void setPlacedBy(Level level0, BlockPos blockPos1, BlockState blockState2, LivingEntity livingEntity3, ItemStack itemStack4) {
        level0.setBlock(blockPos1.above(), (BlockState) blockState2.m_61124_(SEGMENT, 1), 3);
        level0.setBlock(blockPos1.above(2), (BlockState) blockState2.m_61124_(SEGMENT, 2), 3);
    }

    private DoorHingeSide getHinge(BlockPlaceContext blockPlaceContext0) {
        BlockGetter blockgetter = blockPlaceContext0.m_43725_();
        BlockPos blockpos = blockPlaceContext0.getClickedPos();
        Direction direction = blockPlaceContext0.m_8125_();
        BlockPos blockpos1 = blockpos.above();
        Direction direction1 = direction.getCounterClockWise();
        BlockPos blockpos2 = blockpos.relative(direction1);
        BlockState blockstate = blockgetter.getBlockState(blockpos2);
        BlockPos blockpos3 = blockpos1.relative(direction1);
        BlockState blockstate1 = blockgetter.getBlockState(blockpos3);
        Direction direction2 = direction.getClockWise();
        BlockPos blockpos4 = blockpos.relative(direction2);
        BlockState blockstate2 = blockgetter.getBlockState(blockpos4);
        BlockPos blockpos5 = blockpos1.relative(direction2);
        BlockState blockstate3 = blockgetter.getBlockState(blockpos5);
        int i = (blockstate.m_60838_(blockgetter, blockpos2) ? -1 : 0) + (blockstate1.m_60838_(blockgetter, blockpos3) ? -1 : 0) + (blockstate2.m_60838_(blockgetter, blockpos4) ? 1 : 0) + (blockstate3.m_60838_(blockgetter, blockpos5) ? 1 : 0);
        boolean flag = blockstate.m_60713_(this) && (Integer) blockstate.m_61143_(SEGMENT) == 0;
        boolean flag1 = blockstate2.m_60713_(this) && (Integer) blockstate2.m_61143_(SEGMENT) == 0;
        if ((!flag || flag1) && i <= 0) {
            if ((!flag1 || flag) && i >= 0) {
                int j = direction.getStepX();
                int k = direction.getStepZ();
                Vec3 vec3 = blockPlaceContext0.m_43720_();
                double d0 = vec3.x - (double) blockpos.m_123341_();
                double d1 = vec3.z - (double) blockpos.m_123343_();
                return j < 0 && d1 < 0.5 || j > 0 && d1 > 0.5 || k < 0 && d0 > 0.5 || k > 0 && d0 < 0.5 ? DoorHingeSide.RIGHT : DoorHingeSide.LEFT;
            } else {
                return DoorHingeSide.LEFT;
            }
        } else {
            return DoorHingeSide.RIGHT;
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return switch(state.m_61143_(SEGMENT)) {
            case 0 ->
                world.m_8055_(pos.below()).m_60783_(world, pos.below(), Direction.UP);
            case 1 ->
                world.m_8055_(pos.below()).m_60713_(this) && world.m_8055_(pos.above()).m_60713_(this);
            case 2 ->
                world.m_8055_(pos.below()).m_60713_(this) && world.m_8055_(pos.below(2)).m_60713_(this);
            default ->
                false;
        };
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState state, BlockEntityType<T> blockEntityTypeT1) {
        return state.m_61143_(SEGMENT) == 0 ? m_152132_(blockEntityTypeT1, AMTileEntityRegistry.END_PIRATE_DOOR.get(), TileEntityEndPirateDoor::commonTick) : null;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        return state.m_61143_(SEGMENT) == 0 ? super.m_49635_(state, builder) : Collections.emptyList();
    }
}