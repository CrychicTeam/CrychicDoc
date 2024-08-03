package com.rekindled.embers.block;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.blockentity.AlchemyPedestalBlockEntity;
import com.rekindled.embers.blockentity.AlchemyPedestalTopBlockEntity;
import com.rekindled.embers.util.Misc;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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

public class AlchemyPedestalBlock extends DoubleTallMachineBlock {

    protected static final VoxelShape BASE_AABB = Shapes.or(Block.box(1.0, 0.0, 1.0, 6.0, 4.0, 6.0), Block.box(10.0, 0.0, 10.0, 15.0, 4.0, 15.0), Block.box(10.0, 0.0, 1.0, 15.0, 4.0, 6.0), Block.box(1.0, 0.0, 10.0, 6.0, 4.0, 15.0), Block.box(3.0, 0.0, 3.0, 13.0, 16.0, 13.0));

    protected static final VoxelShape TOP_AABB = Shapes.joinUnoptimized(Shapes.or(Block.box(1.0, 0.0, 1.0, 6.0, 4.0, 6.0), Block.box(10.0, 0.0, 10.0, 15.0, 4.0, 15.0), Block.box(10.0, 0.0, 1.0, 15.0, 4.0, 6.0), Block.box(1.0, 0.0, 10.0, 6.0, 4.0, 15.0), Block.box(3.0, 0.0, 3.0, 13.0, 4.0, 13.0), Block.box(4.0, 4.0, 4.0, 12.0, 6.0, 12.0), Block.box(2.0, 6.0, 2.0, 14.0, 8.0, 14.0)), Block.box(6.0, 4.0, 6.0, 10.0, 16.0, 10.0), BooleanOp.ONLY_FIRST);

    public AlchemyPedestalBlock(BlockBehaviour.Properties properties, SoundType topSound) {
        super(properties, topSound);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return level.getBlockEntity(pos) instanceof AlchemyPedestalBlockEntity pedestalEntity ? Misc.useItemOnInventory(pedestalEntity.inventory, level, player, hand) : InteractionResult.PASS;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? BASE_AABB : TOP_AABB;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return (BlockEntity) (pState.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? RegistryManager.ALCHEMY_PEDESTAL_ENTITY.get().create(pPos, pState) : RegistryManager.ALCHEMY_PEDESTAL_TOP_ENTITY.get().create(pPos, pState));
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide && pState.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER ? m_152132_(pBlockEntityType, RegistryManager.ALCHEMY_PEDESTAL_TOP_ENTITY.get(), AlchemyPedestalTopBlockEntity::clientTick) : null;
    }
}