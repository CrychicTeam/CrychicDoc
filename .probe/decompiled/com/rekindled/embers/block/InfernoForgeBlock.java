package com.rekindled.embers.block;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.blockentity.InfernoForgeBottomBlockEntity;
import com.rekindled.embers.blockentity.InfernoForgeTopBlockEntity;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.util.Misc;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;

public class InfernoForgeBlock extends DoubleTallMachineBlock implements SimpleWaterloggedBlock {

    public static final VoxelShape BOTTOM_AABB = Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0), Block.box(12.0, 6.0, 0.0, 16.0, 16.0, 4.0), Block.box(0.0, 6.0, 0.0, 4.0, 16.0, 4.0), Block.box(12.0, 6.0, 12.0, 16.0, 16.0, 16.0), Block.box(0.0, 6.0, 12.0, 4.0, 16.0, 16.0));

    public static final VoxelShape TOP_AABB = Block.box(0.0, 10.0, 0.0, 16.0, 12.0, 16.0);

    public InfernoForgeBlock(BlockBehaviour.Properties pProperties, SoundType topSound) {
        super(pProperties, topSound);
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(BlockStateProperties.WATERLOGGED, false)).m_61124_(BlockStateProperties.HORIZONTAL_AXIS, Direction.Axis.Z));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? BOTTOM_AABB : TOP_AABB;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player.isSecondaryUseActive() || !(level.getBlockEntity(pos) instanceof InfernoForgeTopBlockEntity hatch) || !(level.getBlockEntity(pos.below()) instanceof InfernoForgeBottomBlockEntity forge) || forge.progress != 0) {
            return InteractionResult.PASS;
        } else if (hatch.open && forge.capability.getEmber() <= 0.0) {
            if (level.isClientSide()) {
                player.m_213846_(Component.translatable("embers.tooltip.forge.cannot_start"));
            }
            return InteractionResult.CONSUME;
        } else {
            if (!level.isClientSide()) {
                hatch.open = !hatch.open;
                hatch.lastToggle = level.getGameTime();
                if (hatch.open) {
                    level.playSound(null, pos, EmbersSounds.INFERNO_FORGE_OPEN.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                } else {
                    level.playSound(null, pos, EmbersSounds.INFERNO_FORGE_CLOSE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                hatch.setChanged();
            }
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
            if (level.getBlockEntity(pos) instanceof InfernoForgeTopBlockEntity hatch && hatch.open) {
                return Shapes.empty();
            }
            return TOP_AABB;
        } else {
            return super.m_5939_(state, level, pos, context);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.m_60713_(newState.m_60734_())) {
            for (MechEdgeBlockBase.MechEdge edge : MechEdgeBlockBase.MechEdge.values()) {
                BlockPos cornerPos = pos.subtract(edge.centerPos);
                if (level.getBlockState(cornerPos).m_60734_() instanceof MechEdgeBlockBase) {
                    level.m_46961_(cornerPos, false);
                }
            }
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity != null) {
                IItemHandler handler = (IItemHandler) blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).orElse(null);
                if (handler != null) {
                    Misc.spawnInventoryInWorld(level, (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, handler);
                    level.updateNeighbourForOutputSignal(pos, this);
                }
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState state = super.getStateForPlacement(pContext);
        if (state == null) {
            return null;
        } else {
            boolean lower = state.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER;
            for (MechEdgeBlockBase.MechEdge edge : MechEdgeBlockBase.MechEdge.values()) {
                if (!pContext.m_43725_().getBlockState(pContext.getClickedPos().subtract(edge.centerPos)).m_60629_(pContext)) {
                    return null;
                }
                if (lower && !pContext.m_43725_().getBlockState(pContext.getClickedPos().subtract(edge.centerPos.below())).m_60629_(pContext)) {
                    return null;
                }
            }
            return (BlockState) ((BlockState) state.m_61124_(BlockStateProperties.HORIZONTAL_AXIS, pContext.m_8125_().getAxis())).m_61124_(BlockStateProperties.WATERLOGGED, pContext.m_43725_().getFluidState(pContext.getClickedPos()).getType() == Fluids.WATER);
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        DoubleBlockHalf half = (DoubleBlockHalf) state.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF);
        for (MechEdgeBlockBase.MechEdge edge : MechEdgeBlockBase.MechEdge.values()) {
            BlockState edgeState = (BlockState) RegistryManager.INFERNO_FORGE_EDGE.get().defaultBlockState().m_61124_(BlockStateProperties.WATERLOGGED, level.getFluidState(pos.subtract(edge.centerPos)).getType() == Fluids.WATER);
            level.setBlock(pos.subtract(edge.centerPos), (BlockState) ((BlockState) edgeState.m_61124_(MechEdgeBlockBase.EDGE, edge)).m_61124_(BlockStateProperties.DOUBLE_BLOCK_HALF, half), 3);
        }
        if (state.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER) {
            BlockState topState = (BlockState) ((BlockState) this.m_49966_().m_61124_(BlockStateProperties.HORIZONTAL_AXIS, (Direction.Axis) state.m_61143_(BlockStateProperties.HORIZONTAL_AXIS))).m_61124_(BlockStateProperties.WATERLOGGED, level.getFluidState(pos.above()).getType() == Fluids.WATER);
            level.setBlock(pos.above(), (BlockState) topState.m_61124_(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER), 3);
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return (BlockEntity) (pState.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? RegistryManager.INFERNO_FORGE_BOTTOM_ENTITY.get().create(pPos, pState) : RegistryManager.INFERNO_FORGE_TOP_ENTITY.get().create(pPos, pState));
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pState.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER) {
            return pLevel.isClientSide ? m_152132_(pBlockEntityType, RegistryManager.INFERNO_FORGE_BOTTOM_ENTITY.get(), InfernoForgeBottomBlockEntity::clientTick) : m_152132_(pBlockEntityType, RegistryManager.INFERNO_FORGE_BOTTOM_ENTITY.get(), InfernoForgeBottomBlockEntity::serverTick);
        } else {
            return null;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(BlockStateProperties.HORIZONTAL_AXIS);
    }
}