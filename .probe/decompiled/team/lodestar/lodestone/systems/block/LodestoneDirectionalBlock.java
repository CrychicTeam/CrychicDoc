package team.lodestar.lodestone.systems.block;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class LodestoneDirectionalBlock extends DirectionalBlock {

    public LodestoneDirectionalBlock(BlockBehaviour.Properties builder) {
        super(builder);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(f_52588_, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return context.m_43723_() != null && context.m_43723_().m_6144_() ? (BlockState) this.m_49966_().m_61124_(f_52588_, context.getNearestLookingDirection()) : (BlockState) this.m_49966_().m_61124_(f_52588_, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(f_52588_);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return (BlockState) state.m_61124_(f_52588_, rot.rotate((Direction) state.m_61143_(f_52588_)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return (BlockState) state.m_61124_(f_52588_, mirrorIn.mirror((Direction) state.m_61143_(f_52588_)));
    }
}