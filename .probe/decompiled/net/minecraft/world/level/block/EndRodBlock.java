package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class EndRodBlock extends RodBlock {

    protected EndRodBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(f_52588_, Direction.UP));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        Direction $$1 = blockPlaceContext0.m_43719_();
        BlockState $$2 = blockPlaceContext0.m_43725_().getBlockState(blockPlaceContext0.getClickedPos().relative($$1.getOpposite()));
        return $$2.m_60713_(this) && $$2.m_61143_(f_52588_) == $$1 ? (BlockState) this.m_49966_().m_61124_(f_52588_, $$1.getOpposite()) : (BlockState) this.m_49966_().m_61124_(f_52588_, $$1);
    }

    @Override
    public void animateTick(BlockState blockState0, Level level1, BlockPos blockPos2, RandomSource randomSource3) {
        Direction $$4 = (Direction) blockState0.m_61143_(f_52588_);
        double $$5 = (double) blockPos2.m_123341_() + 0.55 - (double) (randomSource3.nextFloat() * 0.1F);
        double $$6 = (double) blockPos2.m_123342_() + 0.55 - (double) (randomSource3.nextFloat() * 0.1F);
        double $$7 = (double) blockPos2.m_123343_() + 0.55 - (double) (randomSource3.nextFloat() * 0.1F);
        double $$8 = (double) (0.4F - (randomSource3.nextFloat() + randomSource3.nextFloat()) * 0.4F);
        if (randomSource3.nextInt(5) == 0) {
            level1.addParticle(ParticleTypes.END_ROD, $$5 + (double) $$4.getStepX() * $$8, $$6 + (double) $$4.getStepY() * $$8, $$7 + (double) $$4.getStepZ() * $$8, randomSource3.nextGaussian() * 0.005, randomSource3.nextGaussian() * 0.005, randomSource3.nextGaussian() * 0.005);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(f_52588_);
    }
}