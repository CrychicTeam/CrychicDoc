package net.minecraft.world.level.block;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class RedstoneTorchBlock extends TorchBlock {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    private static final Map<BlockGetter, List<RedstoneTorchBlock.Toggle>> RECENT_TOGGLES = new WeakHashMap();

    public static final int RECENT_TOGGLE_TIMER = 60;

    public static final int MAX_RECENT_TOGGLES = 8;

    public static final int RESTART_DELAY = 160;

    private static final int TOGGLE_DELAY = 2;

    protected RedstoneTorchBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0, DustParticleOptions.REDSTONE);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(LIT, true));
    }

    @Override
    public void onPlace(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        for (Direction $$5 : Direction.values()) {
            level1.updateNeighborsAt(blockPos2.relative($$5), this);
        }
    }

    @Override
    public void onRemove(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!boolean4) {
            for (Direction $$5 : Direction.values()) {
                level1.updateNeighborsAt(blockPos2.relative($$5), this);
            }
        }
    }

    @Override
    public int getSignal(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, Direction direction3) {
        return blockState0.m_61143_(LIT) && Direction.UP != direction3 ? 15 : 0;
    }

    protected boolean hasNeighborSignal(Level level0, BlockPos blockPos1, BlockState blockState2) {
        return level0.m_276987_(blockPos1.below(), Direction.DOWN);
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        boolean $$4 = this.hasNeighborSignal(serverLevel1, blockPos2, blockState0);
        List<RedstoneTorchBlock.Toggle> $$5 = (List<RedstoneTorchBlock.Toggle>) RECENT_TOGGLES.get(serverLevel1);
        while ($$5 != null && !$$5.isEmpty() && serverLevel1.m_46467_() - ((RedstoneTorchBlock.Toggle) $$5.get(0)).when > 60L) {
            $$5.remove(0);
        }
        if ((Boolean) blockState0.m_61143_(LIT)) {
            if ($$4) {
                serverLevel1.m_7731_(blockPos2, (BlockState) blockState0.m_61124_(LIT, false), 3);
                if (isToggledTooFrequently(serverLevel1, blockPos2, true)) {
                    serverLevel1.m_46796_(1502, blockPos2, 0);
                    serverLevel1.m_186460_(blockPos2, serverLevel1.m_8055_(blockPos2).m_60734_(), 160);
                }
            }
        } else if (!$$4 && !isToggledTooFrequently(serverLevel1, blockPos2, false)) {
            serverLevel1.m_7731_(blockPos2, (BlockState) blockState0.m_61124_(LIT, true), 3);
        }
    }

    @Override
    public void neighborChanged(BlockState blockState0, Level level1, BlockPos blockPos2, Block block3, BlockPos blockPos4, boolean boolean5) {
        if ((Boolean) blockState0.m_61143_(LIT) == this.hasNeighborSignal(level1, blockPos2, blockState0) && !level1.m_183326_().willTickThisTick(blockPos2, this)) {
            level1.m_186460_(blockPos2, this, 2);
        }
    }

    @Override
    public int getDirectSignal(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, Direction direction3) {
        return direction3 == Direction.DOWN ? blockState0.m_60746_(blockGetter1, blockPos2, direction3) : 0;
    }

    @Override
    public boolean isSignalSource(BlockState blockState0) {
        return true;
    }

    @Override
    public void animateTick(BlockState blockState0, Level level1, BlockPos blockPos2, RandomSource randomSource3) {
        if ((Boolean) blockState0.m_61143_(LIT)) {
            double $$4 = (double) blockPos2.m_123341_() + 0.5 + (randomSource3.nextDouble() - 0.5) * 0.2;
            double $$5 = (double) blockPos2.m_123342_() + 0.7 + (randomSource3.nextDouble() - 0.5) * 0.2;
            double $$6 = (double) blockPos2.m_123343_() + 0.5 + (randomSource3.nextDouble() - 0.5) * 0.2;
            level1.addParticle(this.f_57488_, $$4, $$5, $$6, 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(LIT);
    }

    private static boolean isToggledTooFrequently(Level level0, BlockPos blockPos1, boolean boolean2) {
        List<RedstoneTorchBlock.Toggle> $$3 = (List<RedstoneTorchBlock.Toggle>) RECENT_TOGGLES.computeIfAbsent(level0, p_55680_ -> Lists.newArrayList());
        if (boolean2) {
            $$3.add(new RedstoneTorchBlock.Toggle(blockPos1.immutable(), level0.getGameTime()));
        }
        int $$4 = 0;
        for (int $$5 = 0; $$5 < $$3.size(); $$5++) {
            RedstoneTorchBlock.Toggle $$6 = (RedstoneTorchBlock.Toggle) $$3.get($$5);
            if ($$6.pos.equals(blockPos1)) {
                if (++$$4 >= 8) {
                    return true;
                }
            }
        }
        return false;
    }

    public static class Toggle {

        final BlockPos pos;

        final long when;

        public Toggle(BlockPos blockPos0, long long1) {
            this.pos = blockPos0;
            this.when = long1;
        }
    }
}