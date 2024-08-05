package com.simibubi.create.content.redstone.diodes;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class BrassDiodeBlock extends AbstractDiodeBlock implements IBE<BrassDiodeBlockEntity> {

    public static final BooleanProperty POWERING = BooleanProperty.create("powering");

    public static final BooleanProperty INVERTED = BooleanProperty.create("inverted");

    public BrassDiodeBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(f_52496_, false)).m_61124_(POWERING, false)).m_61124_(INVERTED, false));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player player, InteractionHand pHand, BlockHitResult pHit) {
        return this.toggle(pLevel, pPos, pState, player, pHand);
    }

    public InteractionResult toggle(Level pLevel, BlockPos pPos, BlockState pState, Player player, InteractionHand pHand) {
        if (!player.mayBuild()) {
            return InteractionResult.PASS;
        } else if (player.m_6144_()) {
            return InteractionResult.PASS;
        } else if (AllItems.WRENCH.isIn(player.m_21120_(pHand))) {
            return InteractionResult.PASS;
        } else if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            pLevel.setBlock(pPos, (BlockState) pState.m_61122_(INVERTED), 3);
            float f = !pState.m_61143_(INVERTED) ? 0.6F : 0.5F;
            pLevel.playSound(null, pPos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, f);
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(f_52496_, POWERING, f_54117_, INVERTED);
        super.m_7926_(builder);
    }

    @Override
    protected int getOutputSignal(BlockGetter worldIn, BlockPos pos, BlockState state) {
        return state.m_61143_(POWERING) ^ state.m_61143_(INVERTED) ? 15 : 0;
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return blockState.m_61143_(f_54117_) == side ? this.getOutputSignal(blockAccess, pos, blockState) : 0;
    }

    @Override
    protected int getDelay(BlockState p_196346_1_) {
        return 2;
    }

    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, Direction side) {
        return side == null ? false : side.getAxis() == ((Direction) state.m_61143_(f_54117_)).getAxis();
    }

    @Override
    public Class<BrassDiodeBlockEntity> getBlockEntityClass() {
        return BrassDiodeBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends BrassDiodeBlockEntity> getBlockEntityType() {
        return AllBlocks.PULSE_EXTENDER.is(this) ? (BlockEntityType) AllBlockEntityTypes.PULSE_EXTENDER.get() : (BlockEntityType) AllBlockEntityTypes.PULSE_REPEATER.get();
    }
}