package net.minecraft.world.level.block;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ComparatorBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ComparatorMode;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.ticks.TickPriority;

public class ComparatorBlock extends DiodeBlock implements EntityBlock {

    public static final EnumProperty<ComparatorMode> MODE = BlockStateProperties.MODE_COMPARATOR;

    public ComparatorBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(f_54117_, Direction.NORTH)).m_61124_(f_52496_, false)).m_61124_(MODE, ComparatorMode.COMPARE));
    }

    @Override
    protected int getDelay(BlockState blockState0) {
        return 2;
    }

    @Override
    protected int getOutputSignal(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2) {
        BlockEntity $$3 = blockGetter0.getBlockEntity(blockPos1);
        return $$3 instanceof ComparatorBlockEntity ? ((ComparatorBlockEntity) $$3).getOutputSignal() : 0;
    }

    private int calculateOutputSignal(Level level0, BlockPos blockPos1, BlockState blockState2) {
        int $$3 = this.getInputSignal(level0, blockPos1, blockState2);
        if ($$3 == 0) {
            return 0;
        } else {
            int $$4 = this.m_276835_(level0, blockPos1, blockState2);
            if ($$4 > $$3) {
                return 0;
            } else {
                return blockState2.m_61143_(MODE) == ComparatorMode.SUBTRACT ? $$3 - $$4 : $$3;
            }
        }
    }

    @Override
    protected boolean shouldTurnOn(Level level0, BlockPos blockPos1, BlockState blockState2) {
        int $$3 = this.getInputSignal(level0, blockPos1, blockState2);
        if ($$3 == 0) {
            return false;
        } else {
            int $$4 = this.m_276835_(level0, blockPos1, blockState2);
            return $$3 > $$4 ? true : $$3 == $$4 && blockState2.m_61143_(MODE) == ComparatorMode.COMPARE;
        }
    }

    @Override
    protected int getInputSignal(Level level0, BlockPos blockPos1, BlockState blockState2) {
        int $$3 = super.getInputSignal(level0, blockPos1, blockState2);
        Direction $$4 = (Direction) blockState2.m_61143_(f_54117_);
        BlockPos $$5 = blockPos1.relative($$4);
        BlockState $$6 = level0.getBlockState($$5);
        if ($$6.m_60807_()) {
            $$3 = $$6.m_60674_(level0, $$5);
        } else if ($$3 < 15 && $$6.m_60796_(level0, $$5)) {
            $$5 = $$5.relative($$4);
            $$6 = level0.getBlockState($$5);
            ItemFrame $$7 = this.getItemFrame(level0, $$4, $$5);
            int $$8 = Math.max($$7 == null ? Integer.MIN_VALUE : $$7.getAnalogOutput(), $$6.m_60807_() ? $$6.m_60674_(level0, $$5) : Integer.MIN_VALUE);
            if ($$8 != Integer.MIN_VALUE) {
                $$3 = $$8;
            }
        }
        return $$3;
    }

    @Nullable
    private ItemFrame getItemFrame(Level level0, Direction direction1, BlockPos blockPos2) {
        List<ItemFrame> $$3 = level0.m_6443_(ItemFrame.class, new AABB((double) blockPos2.m_123341_(), (double) blockPos2.m_123342_(), (double) blockPos2.m_123343_(), (double) (blockPos2.m_123341_() + 1), (double) (blockPos2.m_123342_() + 1), (double) (blockPos2.m_123343_() + 1)), p_289506_ -> p_289506_ != null && p_289506_.m_6350_() == direction1);
        return $$3.size() == 1 ? (ItemFrame) $$3.get(0) : null;
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        if (!player3.getAbilities().mayBuild) {
            return InteractionResult.PASS;
        } else {
            blockState0 = (BlockState) blockState0.m_61122_(MODE);
            float $$6 = blockState0.m_61143_(MODE) == ComparatorMode.SUBTRACT ? 0.55F : 0.5F;
            level1.playSound(player3, blockPos2, SoundEvents.COMPARATOR_CLICK, SoundSource.BLOCKS, 0.3F, $$6);
            level1.setBlock(blockPos2, blockState0, 2);
            this.refreshOutputState(level1, blockPos2, blockState0);
            return InteractionResult.sidedSuccess(level1.isClientSide);
        }
    }

    @Override
    protected void checkTickOnNeighbor(Level level0, BlockPos blockPos1, BlockState blockState2) {
        if (!level0.m_183326_().willTickThisTick(blockPos1, this)) {
            int $$3 = this.calculateOutputSignal(level0, blockPos1, blockState2);
            BlockEntity $$4 = level0.getBlockEntity(blockPos1);
            int $$5 = $$4 instanceof ComparatorBlockEntity ? ((ComparatorBlockEntity) $$4).getOutputSignal() : 0;
            if ($$3 != $$5 || (Boolean) blockState2.m_61143_(f_52496_) != this.shouldTurnOn(level0, blockPos1, blockState2)) {
                TickPriority $$6 = this.m_52573_(level0, blockPos1, blockState2) ? TickPriority.HIGH : TickPriority.NORMAL;
                level0.m_186464_(blockPos1, this, 2, $$6);
            }
        }
    }

    private void refreshOutputState(Level level0, BlockPos blockPos1, BlockState blockState2) {
        int $$3 = this.calculateOutputSignal(level0, blockPos1, blockState2);
        BlockEntity $$4 = level0.getBlockEntity(blockPos1);
        int $$5 = 0;
        if ($$4 instanceof ComparatorBlockEntity $$6) {
            $$5 = $$6.getOutputSignal();
            $$6.setOutputSignal($$3);
        }
        if ($$5 != $$3 || blockState2.m_61143_(MODE) == ComparatorMode.COMPARE) {
            boolean $$7 = this.shouldTurnOn(level0, blockPos1, blockState2);
            boolean $$8 = (Boolean) blockState2.m_61143_(f_52496_);
            if ($$8 && !$$7) {
                level0.setBlock(blockPos1, (BlockState) blockState2.m_61124_(f_52496_, false), 2);
            } else if (!$$8 && $$7) {
                level0.setBlock(blockPos1, (BlockState) blockState2.m_61124_(f_52496_, true), 2);
            }
            this.m_52580_(level0, blockPos1, blockState2);
        }
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        this.refreshOutputState(serverLevel1, blockPos2, blockState0);
    }

    @Override
    public boolean triggerEvent(BlockState blockState0, Level level1, BlockPos blockPos2, int int3, int int4) {
        super.m_8133_(blockState0, level1, blockPos2, int3, int4);
        BlockEntity $$5 = level1.getBlockEntity(blockPos2);
        return $$5 != null && $$5.triggerEvent(int3, int4);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return new ComparatorBlockEntity(blockPos0, blockState1);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(f_54117_, MODE, f_52496_);
    }
}