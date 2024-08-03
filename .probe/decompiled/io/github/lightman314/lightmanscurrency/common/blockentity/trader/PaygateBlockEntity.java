package io.github.lightman314.lightmanscurrency.common.blockentity.trader;

import io.github.lightman314.lightmanscurrency.api.traders.TradeContext;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.api.traders.blockentity.TraderBlockEntity;
import io.github.lightman314.lightmanscurrency.common.blocks.PaygateBlock;
import io.github.lightman314.lightmanscurrency.common.core.ModBlockEntities;
import io.github.lightman314.lightmanscurrency.common.items.TicketItem;
import io.github.lightman314.lightmanscurrency.common.traders.paygate.PaygateTraderData;
import io.github.lightman314.lightmanscurrency.common.traders.paygate.tradedata.PaygateTradeData;
import io.github.lightman314.lightmanscurrency.util.BlockEntityUtil;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class PaygateBlockEntity extends TraderBlockEntity<PaygateTraderData> {

    private int timer = 0;

    public PaygateBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.PAYGATE.get(), pos, state);
    }

    protected PaygateBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Nullable
    protected PaygateTraderData castOrNullify(@Nonnull TraderData trader) {
        return trader instanceof PaygateTraderData ? (PaygateTraderData) trader : null;
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compound) {
        super.saveAdditional(compound);
        this.saveTimer(compound);
    }

    public final CompoundTag saveTimer(CompoundTag compound) {
        compound.putInt("Timer", Math.max(this.timer, 0));
        return compound;
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        if (compound.contains("Timer", 3)) {
            this.timer = Math.max(compound.getInt("Timer"), 0);
        }
        super.load(compound);
    }

    public boolean isActive() {
        return this.timer > 0;
    }

    public void activate(int duration) {
        this.timer = duration;
        this.f_58857_.setBlockAndUpdate(this.f_58858_, (BlockState) this.f_58857_.getBlockState(this.f_58858_).m_61124_(PaygateBlock.POWERED, true));
        this.markTimerDirty();
    }

    @Override
    public void serverTick() {
        super.serverTick();
        if (this.timer > 0) {
            this.timer--;
            this.markTimerDirty();
            if (this.timer <= 0) {
                this.f_58857_.setBlockAndUpdate(this.f_58858_, (BlockState) this.f_58857_.getBlockState(this.f_58858_).m_61124_(PaygateBlock.POWERED, false));
            }
        }
    }

    public void markTimerDirty() {
        this.m_6596_();
        if (!this.f_58857_.isClientSide) {
            BlockEntityUtil.sendUpdatePacket(this, this.saveTimer(new CompoundTag()));
        }
    }

    public int getValidTicketTrade(Player player, ItemStack heldItem) {
        PaygateTraderData trader = this.getTraderData();
        if (TicketItem.isTicketOrPass(heldItem)) {
            long ticketID = TicketItem.GetTicketID(heldItem);
            if (ticketID >= -1L) {
                TradeContext context = TradeContext.create(trader, player).build();
                for (int i = 0; i < trader.getTradeCount(); i++) {
                    PaygateTradeData trade = trader.getTrade(i);
                    if (trade.isTicketTrade() && trade.getTicketID() == ticketID && !trader.runPreTradeEvent(trade, context).isCanceled()) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    @Nonnull
    protected PaygateTraderData buildNewTrader() {
        return new PaygateTraderData(this.f_58857_, this.f_58858_);
    }
}