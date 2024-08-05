package net.mehvahdjukaar.supplementaries.common.block.blocks;

import net.mehvahdjukaar.moonlight.api.block.WaterBlock;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public class CannonballBlock extends WaterBlock {

    public static final IntegerProperty BALLS = ModBlockProperties.BALLS;

    public CannonballBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(BALLS, 1)).m_61124_(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BALLS);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return !useContext.m_7078_() && useContext.m_43722_().is(this.m_5456_()) && (Integer) state.m_61143_(BALLS) < 4 || super.m_6864_(state, useContext);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockState = context.m_43725_().getBlockState(context.getClickedPos());
        return blockState.m_60713_(this) ? (BlockState) blockState.m_61124_(BALLS, Math.min(4, (Integer) blockState.m_61143_(BALLS) + 1)) : super.getStateForPlacement(context);
    }
}