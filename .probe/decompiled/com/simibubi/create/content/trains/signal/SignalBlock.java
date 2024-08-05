package com.simibubi.create.content.trains.signal;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Lang;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class SignalBlock extends Block implements IBE<SignalBlockEntity>, IWrenchable {

    public static final EnumProperty<SignalBlock.SignalType> TYPE = EnumProperty.create("type", SignalBlock.SignalType.class);

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public SignalBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(TYPE, SignalBlock.SignalType.ENTRY_SIGNAL)).m_61124_(POWERED, false));
    }

    @Override
    public Class<SignalBlockEntity> getBlockEntityClass() {
        return SignalBlockEntity.class;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(TYPE, POWERED));
    }

    public boolean shouldCheckWeakPower(BlockState state, SignalGetter level, BlockPos pos, Direction side) {
        return false;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return (BlockState) this.m_49966_().m_61124_(POWERED, pContext.m_43725_().m_276867_(pContext.getClickedPos()));
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        if (!pLevel.isClientSide) {
            boolean powered = (Boolean) pState.m_61143_(POWERED);
            if (powered != pLevel.m_276867_(pPos)) {
                if (powered) {
                    pLevel.m_186460_(pPos, this, 4);
                } else {
                    pLevel.setBlock(pPos, (BlockState) pState.m_61122_(POWERED), 2);
                }
            }
        }
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRand) {
        if ((Boolean) pState.m_61143_(POWERED) && !pLevel.m_276867_(pPos)) {
            pLevel.m_7731_(pPos, (BlockState) pState.m_61122_(POWERED), 2);
        }
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, worldIn, pos, newState);
    }

    @Override
    public BlockEntityType<? extends SignalBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends SignalBlockEntity>) AllBlockEntityTypes.TRACK_SIGNAL.get();
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            this.withBlockEntityDo(level, pos, ste -> {
                SignalBoundary signal = ste.getSignal();
                Player player = context.getPlayer();
                if (signal != null) {
                    signal.cycleSignalType(pos);
                    if (player != null) {
                        player.displayClientMessage(Lang.translateDirect("track_signal.mode_change." + signal.getTypeFor(pos).getSerializedName()), true);
                    }
                } else if (player != null) {
                    player.displayClientMessage(Lang.translateDirect("track_signal.cannot_change_mode"), true);
                }
            });
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState pState, Level blockAccess, BlockPos pPos) {
        return (Integer) this.getBlockEntityOptional(blockAccess, pPos).filter(SignalBlockEntity::isPowered).map($ -> 15).orElse(0);
    }

    public static enum SignalType implements StringRepresentable {

        ENTRY_SIGNAL, CROSS_SIGNAL;

        @Override
        public String getSerializedName() {
            return Lang.asId(this.name());
        }
    }
}