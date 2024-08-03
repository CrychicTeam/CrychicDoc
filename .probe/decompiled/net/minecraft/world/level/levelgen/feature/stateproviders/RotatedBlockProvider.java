package net.minecraft.world.level.levelgen.feature.stateproviders;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class RotatedBlockProvider extends BlockStateProvider {

    public static final Codec<RotatedBlockProvider> CODEC = BlockState.CODEC.fieldOf("state").xmap(BlockBehaviour.BlockStateBase::m_60734_, Block::m_49966_).xmap(RotatedBlockProvider::new, p_68793_ -> p_68793_.block).codec();

    private final Block block;

    public RotatedBlockProvider(Block block0) {
        this.block = block0;
    }

    @Override
    protected BlockStateProviderType<?> type() {
        return BlockStateProviderType.ROTATED_BLOCK_PROVIDER;
    }

    @Override
    public BlockState getState(RandomSource randomSource0, BlockPos blockPos1) {
        Direction.Axis $$2 = Direction.Axis.getRandom(randomSource0);
        return (BlockState) this.block.defaultBlockState().m_61124_(RotatedPillarBlock.AXIS, $$2);
    }
}