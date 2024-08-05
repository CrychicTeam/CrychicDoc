package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class RedstoneLampBlock extends Block {

    public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;

    public RedstoneLampBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(LIT, false));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        return (BlockState) this.m_49966_().m_61124_(LIT, blockPlaceContext0.m_43725_().m_276867_(blockPlaceContext0.getClickedPos()));
    }

    @Override
    public void neighborChanged(BlockState blockState0, Level level1, BlockPos blockPos2, Block block3, BlockPos blockPos4, boolean boolean5) {
        if (!level1.isClientSide) {
            boolean $$6 = (Boolean) blockState0.m_61143_(LIT);
            if ($$6 != level1.m_276867_(blockPos2)) {
                if ($$6) {
                    level1.m_186460_(blockPos2, this, 4);
                } else {
                    level1.setBlock(blockPos2, (BlockState) blockState0.m_61122_(LIT), 2);
                }
            }
        }
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if ((Boolean) blockState0.m_61143_(LIT) && !serverLevel1.m_276867_(blockPos2)) {
            serverLevel1.m_7731_(blockPos2, (BlockState) blockState0.m_61122_(LIT), 2);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(LIT);
    }
}