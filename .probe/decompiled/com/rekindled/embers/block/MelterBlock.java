package com.rekindled.embers.block;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.blockentity.MelterBottomBlockEntity;
import com.rekindled.embers.blockentity.MelterTopBlockEntity;
import com.rekindled.embers.util.Misc;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class MelterBlock extends DoubleTallMachineBlock {

    protected static final VoxelShape BASE_AABB = Shapes.or(Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0), Block.box(0.0, 8.0, 0.0, 4.0, 16.0, 4.0), Block.box(0.0, 8.0, 12.0, 4.0, 16.0, 16.0), Block.box(12.0, 8.0, 0.0, 16.0, 16.0, 4.0), Block.box(12.0, 8.0, 12.0, 16.0, 16.0, 16.0), Block.box(1.0, 0.0, 1.0, 4.0, 8.0, 4.0), Block.box(1.0, 0.0, 12.0, 4.0, 8.0, 15.0), Block.box(12.0, 0.0, 1.0, 15.0, 8.0, 4.0), Block.box(12.0, 0.0, 12.0, 15.0, 8.0, 15.0));

    protected static final VoxelShape TOP_AABB = Shapes.join(Shapes.block(), Block.box(4.0, 0.0, 4.0, 12.0, 16.0, 12.0), BooleanOp.ONLY_FIRST);

    public MelterBlock(BlockBehaviour.Properties properties, SoundType topSound) {
        super(properties, topSound);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (state.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) != DoubleBlockHalf.LOWER && level.getBlockEntity(pos) instanceof MelterTopBlockEntity melterEntity) {
            ItemStack heldItem = player.m_21120_(hand);
            if (!heldItem.isEmpty()) {
                IFluidHandler cap = melterEntity.getCapability(ForgeCapabilities.FLUID_HANDLER, hit.getDirection()).orElse(null);
                if (cap != null && FluidUtil.interactWithFluidHandler(player, hand, cap)) {
                    return InteractionResult.SUCCESS;
                }
            }
            return Misc.useItemOnInventory(melterEntity.inventory, level, player, hand);
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? BASE_AABB : TOP_AABB;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return (BlockEntity) (pState.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? RegistryManager.MELTER_BOTTOM_ENTITY.get().create(pPos, pState) : RegistryManager.MELTER_TOP_ENTITY.get().create(pPos, pState));
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pState.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER) {
            return pLevel.isClientSide ? m_152132_(pBlockEntityType, RegistryManager.MELTER_BOTTOM_ENTITY.get(), MelterBottomBlockEntity::clientTick) : m_152132_(pBlockEntityType, RegistryManager.MELTER_BOTTOM_ENTITY.get(), MelterBottomBlockEntity::serverTick);
        } else {
            return pLevel.isClientSide ? m_152132_(pBlockEntityType, RegistryManager.MELTER_TOP_ENTITY.get(), MelterTopBlockEntity::clientTick) : m_152132_(pBlockEntityType, RegistryManager.MELTER_TOP_ENTITY.get(), MelterTopBlockEntity::serverTick);
        }
    }
}