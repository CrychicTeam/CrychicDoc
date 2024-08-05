package com.github.alexthe666.alexsmobs.block;

import com.github.alexthe666.alexsmobs.tileentity.AMTileEntityRegistry;
import com.github.alexthe666.alexsmobs.tileentity.TileEntityVoidWormBeak;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
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
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockVoidWormBeak extends BaseEntityBlock {

    public static final DirectionProperty FACING = DirectionalBlock.FACING;

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    private static final VoxelShape AABB = Block.box(0.0, 4.0, 0.0, 16.0, 12.0, 16.0);

    private static final VoxelShape AABB_VERTICAL = Block.box(0.0, 0.0, 4.0, 16.0, 16.0, 12.0);

    public BlockVoidWormBeak() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).noOcclusion().sound(SoundType.ANCIENT_DEBRIS).strength(1.0F).noCollission().requiresCorrectToolForDrops());
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH)).m_61124_(POWERED, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return ((Direction) state.m_61143_(FACING)).getAxis() == Direction.Axis.Y ? AABB_VERTICAL : AABB;
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isClientSide) {
            this.updateState(state, worldIn, pos, blockIn);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        if (!worldIn.f_46443_) {
            this.updateState(state, worldIn, pos, state.m_60734_());
        }
    }

    public void updateState(BlockState state, Level worldIn, BlockPos pos, Block blockIn) {
        boolean flag = (Boolean) state.m_61143_(POWERED);
        boolean flag1 = worldIn.m_276867_(pos);
        if (flag1 != flag) {
            worldIn.setBlock(pos, (BlockState) state.m_61124_(POWERED, flag1), 3);
            worldIn.updateNeighborsAt(pos.below(), this);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityVoidWormBeak(pos, state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, context.m_43719_())).m_61124_(POWERED, context.m_43725_().m_276867_(context.getClickedPos()));
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
        builder.add(FACING, POWERED);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState blockState1, BlockEntityType<T> blockEntityTypeT2) {
        return m_152132_(blockEntityTypeT2, AMTileEntityRegistry.VOID_WORM_BEAK.get(), TileEntityVoidWormBeak::commonTick);
    }
}