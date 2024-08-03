package io.github.lightman314.lightmanscurrency.common.blocks;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.traders.TradeContext;
import io.github.lightman314.lightmanscurrency.api.traders.blocks.TraderBlockRotatable;
import io.github.lightman314.lightmanscurrency.common.blockentity.trader.PaygateBlockEntity;
import io.github.lightman314.lightmanscurrency.common.core.ModBlockEntities;
import io.github.lightman314.lightmanscurrency.common.items.TooltipItem;
import io.github.lightman314.lightmanscurrency.common.traders.paygate.PaygateTraderData;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class PaygateBlock extends TraderBlockRotatable {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public PaygateBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(POWERED, false));
    }

    @NotNull
    @Override
    public InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult result) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof PaygateBlockEntity paygate) {
            int tradeIndex = paygate.getValidTicketTrade(player, player.m_21120_(hand));
            if (tradeIndex >= 0) {
                PaygateTraderData trader = paygate.getTraderData();
                if (trader != null) {
                    trader.TryExecuteTrade(TradeContext.create(trader, player).build(), tradeIndex);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.m_6227_(state, level, pos, player, hand, result);
    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(POWERED);
    }

    @Override
    public boolean isSignalSource(@Nonnull BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull Direction dir) {
        return state.m_61143_(POWERED) ? 15 : 0;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable BlockGetter level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        TooltipItem.addTooltip(tooltip, LCText.TOOLTIP_PAYGATE.asTooltip());
        super.m_5871_(stack, level, tooltip, flagIn);
    }

    @Override
    protected BlockEntity makeTrader(BlockPos pos, BlockState state) {
        return new PaygateBlockEntity(pos, state);
    }

    @Override
    protected BlockEntityType<?> traderType() {
        return ModBlockEntities.PAYGATE.get();
    }
}