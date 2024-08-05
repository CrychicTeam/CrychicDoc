package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public class CoralFanBlock extends BaseCoralFanBlock {

    private final Block deadBlock;

    protected CoralFanBlock(Block block0, BlockBehaviour.Properties blockBehaviourProperties1) {
        super(blockBehaviourProperties1);
        this.deadBlock = block0;
    }

    @Override
    public void onPlace(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        this.m_49164_(blockState0, level1, blockPos2);
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (!m_49186_(blockState0, serverLevel1, blockPos2)) {
            serverLevel1.m_7731_(blockPos2, (BlockState) this.deadBlock.defaultBlockState().m_61124_(f_49158_, false), 2);
        }
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if (direction1 == Direction.DOWN && !blockState0.m_60710_(levelAccessor3, blockPos4)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            this.m_49164_(blockState0, levelAccessor3, blockPos4);
            if ((Boolean) blockState0.m_61143_(f_49158_)) {
                levelAccessor3.scheduleTick(blockPos4, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor3));
            }
            return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
        }
    }
}