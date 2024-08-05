package com.rekindled.embers.block;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.blockentity.DawnstoneAnvilBlockEntity;
import com.rekindled.embers.util.Misc;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;

public class DawnstoneAnvilBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static final VoxelShape X_AXIS_AABB = Shapes.or(Block.box(4.5, 6.5, 4.5, 11.5, 11.5, 11.5), Block.box(4.0, 0.0, 4.0, 12.0, 7.0, 12.0), Block.box(2.0, 0.0, 2.0, 14.0, 4.0, 14.0), Block.box(4.0, 11.0, 0.0, 12.0, 16.0, 16.0));

    public static final VoxelShape Z_AXIS_AABB = Shapes.or(Block.box(4.5, 6.5, 4.5, 11.5, 11.5, 11.5), Block.box(4.0, 0.0, 4.0, 12.0, 7.0, 12.0), Block.box(2.0, 0.0, 2.0, 14.0, 4.0, 14.0), Block.box(0.0, 11.0, 4.0, 16.0, 16.0, 12.0));

    public DawnstoneAnvilBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(BlockStateProperties.WATERLOGGED, false)).m_61124_(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof DawnstoneAnvilBlockEntity anvilEntity) {
            ItemStack heldItem = player.m_21120_(hand);
            if (!heldItem.isEmpty()) {
                if (heldItem.getItem() == RegistryManager.TINKER_HAMMER.get()) {
                    if (anvilEntity.onHit()) {
                        return InteractionResult.SUCCESS;
                    }
                    return InteractionResult.PASS;
                }
                for (int i = 0; i < anvilEntity.inventory.getSlots(); i++) {
                    ItemStack leftover = anvilEntity.inventory.insertItem(i, heldItem, false);
                    if (!leftover.equals(heldItem)) {
                        player.m_21008_(hand, leftover);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            for (int ix = anvilEntity.inventory.getSlots() - 1; ix >= 0; ix--) {
                if (!anvilEntity.inventory.getStackInSlot(ix).isEmpty()) {
                    level.m_7967_(new ItemEntity(level, player.m_20182_().x, player.m_20182_().y, player.m_20182_().z, anvilEntity.inventory.getStackInSlot(ix)));
                    anvilEntity.inventory.setStackInSlot(ix, ItemStack.EMPTY);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.m_60713_(newState.m_60734_())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity != null) {
                IItemHandler handler = (IItemHandler) blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).orElse(null);
                if (handler != null) {
                    Misc.spawnInventoryInWorld(level, (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, handler);
                    level.updateNeighbourForOutputSignal(pos, this);
                }
            }
            super.m_6810_(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return ((Direction) state.m_61143_(BlockStateProperties.HORIZONTAL_FACING)).getAxis() == Direction.Axis.X ? X_AXIS_AABB : Z_AXIS_AABB;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return RegistryManager.DAWNSTONE_ANVIL_ENTITY.get().create(pPos, pState);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Direction direction;
        if (pContext.m_43719_().getAxis() != Direction.Axis.Y) {
            direction = pContext.m_43719_().getOpposite();
        } else {
            direction = pContext.m_8125_();
        }
        return (BlockState) ((BlockState) super.m_5573_(pContext).m_61124_(BlockStateProperties.HORIZONTAL_FACING, direction)).m_61124_(BlockStateProperties.WATERLOGGED, pContext.m_43725_().getFluidState(pContext.getClickedPos()).getType() == Fluids.WATER);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if ((Boolean) pState.m_61143_(BlockStateProperties.WATERLOGGED)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.m_6718_(pLevel));
        }
        return super.m_7417_(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(BlockStateProperties.WATERLOGGED, BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return pState.m_61143_(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(pState);
    }
}