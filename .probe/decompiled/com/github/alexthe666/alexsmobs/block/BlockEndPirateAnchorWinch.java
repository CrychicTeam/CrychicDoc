package com.github.alexthe666.alexsmobs.block;

import com.github.alexthe666.alexsmobs.tileentity.AMTileEntityRegistry;
import com.github.alexthe666.alexsmobs.tileentity.TileEntityEndPirateAnchorWinch;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockEndPirateAnchorWinch extends BaseEntityBlock implements AMSpecialRenderBlock {

    public static final BooleanProperty EASTORWEST = BooleanProperty.create("eastorwest");

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    protected static final VoxelShape FULL_AABB_EW = Block.box(3.0, 3.0, 0.0, 13.0, 13.0, 16.0);

    protected static final VoxelShape FULL_AABB_NS = Block.box(0.0, 3.0, 3.0, 16.0, 13.0, 13.0);

    protected BlockEndPirateAnchorWinch() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).friction(0.97F).strength(10.0F).lightLevel(i -> 6).sound(SoundType.STONE).noOcclusion());
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(EASTORWEST, false)).m_61124_(POWERED, false));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(EASTORWEST, POWERED);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return state.m_61143_(EASTORWEST) ? FULL_AABB_EW : FULL_AABB_NS;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean axis = context.m_43719_().getAxis() == Direction.Axis.Y ? context.m_8125_().getAxis() == Direction.Axis.X : context.m_43719_().getAxis() != Direction.Axis.X;
        return (BlockState) this.m_49966_().m_61124_(EASTORWEST, axis);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity player, ItemStack stack) {
        if (level.getBlockEntity(pos) instanceof TileEntityEndPirateAnchorWinch winch) {
            winch.recalculateChains();
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos blockPos0, boolean boolean1) {
        boolean flag = level.m_276867_(pos);
        if (level.getBlockEntity(pos) instanceof TileEntityEndPirateAnchorWinch winch && flag != (Boolean) state.m_61143_(POWERED)) {
            level.setBlock(pos, (BlockState) state.m_61124_(POWERED, flag), 3);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityEndPirateAnchorWinch(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState state, BlockEntityType<T> blockEntityTypeT1) {
        return m_152132_(blockEntityTypeT1, AMTileEntityRegistry.END_PIRATE_ANCHOR_WINCH.get(), TileEntityEndPirateAnchorWinch::commonTick);
    }
}