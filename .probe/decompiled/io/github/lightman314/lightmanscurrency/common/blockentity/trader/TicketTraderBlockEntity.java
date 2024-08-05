package io.github.lightman314.lightmanscurrency.common.blockentity.trader;

import io.github.lightman314.lightmanscurrency.common.core.ModBlockEntities;
import io.github.lightman314.lightmanscurrency.common.traders.item.ItemTraderData;
import io.github.lightman314.lightmanscurrency.common.traders.item.ItemTraderDataTicket;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TicketTraderBlockEntity extends ItemTraderBlockEntity {

    public TicketTraderBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TICKET_TRADER.get(), pos, state);
    }

    public TicketTraderBlockEntity(BlockPos pos, BlockState state, int tradeCount) {
        super(ModBlockEntities.TICKET_TRADER.get(), pos, state, tradeCount);
    }

    @Nonnull
    @Override
    public ItemTraderData buildNewTrader() {
        return new ItemTraderDataTicket(this.tradeCount, this.f_58857_, this.f_58858_);
    }
}