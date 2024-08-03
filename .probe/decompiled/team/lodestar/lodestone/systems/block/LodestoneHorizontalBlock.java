package team.lodestar.lodestone.systems.block;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class LodestoneHorizontalBlock extends HorizontalDirectionalBlock {

    public LodestoneHorizontalBlock(BlockBehaviour.Properties builder) {
        super(builder);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(f_54117_, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return (BlockState) this.m_49966_().m_61124_(f_54117_, pContext.m_8125_().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(f_54117_);
    }
}