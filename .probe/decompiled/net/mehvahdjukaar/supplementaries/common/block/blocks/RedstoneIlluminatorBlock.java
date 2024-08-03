package net.mehvahdjukaar.supplementaries.common.block.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public class RedstoneIlluminatorBlock extends Block {

    public static final IntegerProperty POWER = BlockStateProperties.POWER;

    public RedstoneIlluminatorBlock(BlockBehaviour.Properties properties) {
        super(properties.lightLevel(state -> 15 - (Integer) state.m_61143_(POWER)));
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(POWER, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWER);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos fromPos, boolean moving) {
        super.m_6861_(state, level, pos, neighborBlock, fromPos, moving);
        if (!level.isClientSide) {
            int pow = level.m_277086_(pos);
            level.setBlock(pos, (BlockState) state.m_61124_(POWER, Mth.clamp(pow, 0, 15)), 2);
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) super.getStateForPlacement(context).m_61124_(POWER, context.m_43725_().m_277086_(context.getClickedPos()));
    }
}