package com.github.alexmodguy.alexscaves.server.block;

import com.github.alexmodguy.alexscaves.server.block.blockentity.ACBlockEntityRegistry;
import com.github.alexmodguy.alexscaves.server.block.blockentity.TeslaBulbBlockEntity;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TeslaBulbBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final BooleanProperty DOWN = BooleanProperty.create("down");

    public static final VoxelShape SHAPE_DOWN = Block.box(3.0, 1.0, 3.0, 11.0, 15.0, 11.0);

    public static final VoxelShape SHAPE_UP = Block.box(3.0, 1.0, 3.0, 11.0, 15.0, 11.0);

    public TeslaBulbBlock() {
        super(BlockBehaviour.Properties.of().mapColor(DyeColor.WHITE).strength(3.0F, 10.0F).sound(SoundType.GLASS).lightLevel(i -> 15).emissiveRendering((state, level, pos) -> true));
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(WATERLOGGED, false)).m_61124_(DOWN, false));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState blockState1, BlockEntityType<T> blockEntityTypeT2) {
        return m_152132_(blockEntityTypeT2, ACBlockEntityRegistry.TESLA_BULB.get(), TeslaBulbBlockEntity::tick);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean down = context.m_43719_() == Direction.DOWN;
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(DOWN, down)).m_61124_(WATERLOGGED, context.m_43725_().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
    }

    @Override
    public void onProjectileHit(Level level, BlockState blockState, BlockHitResult hitResult, Projectile projectile) {
        if (level.getBlockEntity(hitResult.getBlockPos()) instanceof TeslaBulbBlockEntity teslaBulb) {
            teslaBulb.explode();
        }
    }

    @Override
    public void attack(BlockState blockState, Level level, BlockPos blockPos, Player player) {
        if (level.getBlockEntity(blockPos) instanceof TeslaBulbBlockEntity teslaBulb) {
            teslaBulb.explode();
        }
    }

    @Deprecated
    @Override
    public boolean canSurvive(BlockState state, LevelReader levelAccessor, BlockPos blockPos) {
        BlockState above = levelAccessor.m_8055_(blockPos.above());
        BlockState below = levelAccessor.m_8055_(blockPos.below());
        return state.m_61143_(DOWN) ? above.m_60783_(levelAccessor, blockPos.above(), Direction.UP) || GalenaSpireBlock.isGalenaSpireConnectable(above, true) : below.m_60783_(levelAccessor, blockPos.below(), Direction.DOWN) || GalenaSpireBlock.isGalenaSpireConnectable(below, false);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        if ((Boolean) state.m_61143_(WATERLOGGED)) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor));
        }
        return state.m_60710_(levelAccessor, blockPos) ? super.m_7417_(state, direction, state1, levelAccessor, blockPos, blockPos1) : Blocks.AIR.defaultBlockState();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return state.m_61143_(DOWN) ? SHAPE_DOWN : SHAPE_UP;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TeslaBulbBlockEntity(pos, state);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(DOWN, WATERLOGGED);
    }
}