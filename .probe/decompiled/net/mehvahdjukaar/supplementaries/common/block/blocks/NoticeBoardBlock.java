package net.mehvahdjukaar.supplementaries.common.block.blocks;

import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.tiles.NoticeBoardBlockTile;
import net.mehvahdjukaar.supplementaries.common.utils.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
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
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class NoticeBoardBlock extends Block implements EntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final BooleanProperty HAS_BOOK = BlockStateProperties.HAS_BOOK;

    public static final BooleanProperty CULLED = ModBlockProperties.CULLED;

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public NoticeBoardBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(CULLED, false)).m_61124_(POWERED, false)).m_61124_(FACING, Direction.NORTH)).m_61124_(HAS_BOOK, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HAS_BOOK, CULLED, POWERED);
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
        Direction dir = context.m_8125_().getOpposite();
        Level level = context.m_43725_();
        BlockPos pos = context.getClickedPos();
        BlockPos facingPos = pos.relative(dir);
        BlockState facingState = level.getBlockState(facingPos);
        boolean culled = facingState.m_60804_(level, pos) && facingState.m_60783_(level, facingPos, dir.getOpposite());
        boolean powered = level.m_277086_(pos) > 0;
        return (BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, dir)).m_61124_(CULLED, culled)).m_61124_(POWERED, powered);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.getBlockEntity(pos) instanceof NoticeBoardBlockTile tile && tile.isAccessibleBy(player)) {
            return tile.interact(player, handIn, pos, state, hit);
        }
        return InteractionResult.PASS;
    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos) {
        return worldIn.getBlockEntity(pos) instanceof MenuProvider menuProvider ? menuProvider : null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new NoticeBoardBlockTile(pPos, pState);
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (worldIn.getBlockEntity(pos) instanceof NoticeBoardBlockTile tile) {
            if (stack.hasCustomHoverName()) {
                tile.m_58638_(stack.getHoverName());
            }
            BlockUtil.addOptionalOwnership(placer, tile);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (facing == state.m_61143_(FACING) && level.m_7702_(currentPos) instanceof NoticeBoardBlockTile) {
            boolean culled = facingState.m_60804_(level, currentPos) && facingState.m_60783_(level, facingPos, facing.getOpposite());
            state = (BlockState) state.m_61124_(CULLED, culled);
        }
        return state;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.m_60734_() != newState.m_60734_()) {
            if (level.getBlockEntity(pos) instanceof NoticeBoardBlockTile tile) {
                Containers.dropContents(level, pos, tile);
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.m_6810_(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level world, BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof Container tile) {
            return tile.isEmpty() ? 0 : 15;
        } else {
            return 0;
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        super.m_6861_(state, world, pos, pBlock, pFromPos, pIsMoving);
        boolean powered = world.m_277086_(pos) > 0;
        if (powered != (Boolean) state.m_61143_(POWERED)) {
            if (powered && world.getBlockEntity(pos) instanceof NoticeBoardBlockTile tile) {
                tile.turnPage();
            }
            world.setBlockAndUpdate(pos, (BlockState) state.m_61124_(POWERED, powered));
        }
    }
}