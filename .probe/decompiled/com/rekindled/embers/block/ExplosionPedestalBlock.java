package com.rekindled.embers.block;

import com.rekindled.embers.compat.curios.CuriosCompat;
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

public class ExplosionPedestalBlock extends AlchemyPedestalBlock {

    protected static final VoxelShape TOP_AABB = Shapes.joinUnoptimized(Shapes.or(Block.box(1.0, 0.0, 1.0, 6.0, 4.0, 6.0), Block.box(10.0, 0.0, 10.0, 15.0, 4.0, 15.0), Block.box(10.0, 0.0, 1.0, 15.0, 4.0, 6.0), Block.box(1.0, 0.0, 10.0, 6.0, 4.0, 15.0), Block.box(3.0, 0.0, 3.0, 13.0, 4.0, 13.0), Block.box(4.0, 4.0, 4.0, 12.0, 6.0, 12.0)), Block.box(6.0, 4.0, 6.0, 10.0, 16.0, 10.0), BooleanOp.ONLY_FIRST);

    public ExplosionPedestalBlock(BlockBehaviour.Properties properties, SoundType topSound) {
        super(properties, topSound);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return InteractionResult.PASS;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? BASE_AABB : TOP_AABB;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return pState.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? null : CuriosCompat.EXPLOSION_PEDESTAL_ENTITY.get().create(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return null;
    }
}