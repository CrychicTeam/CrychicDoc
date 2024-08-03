package io.github.lightman314.lightmanscurrency.common.traderinterface;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.api.traders.trade.TradeData;
import io.github.lightman314.lightmanscurrency.util.DebugUtil;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;

public class NetworkTradeReference extends NetworkTraderReference {

    private final Function<CompoundTag, TradeData> tradeDeserializer;

    private int tradeIndex = -1;

    private TradeData tradeData = null;

    public int getTradeIndex() {
        return this.tradeIndex;
    }

    public boolean hasTrade() {
        return this.tradeIndex >= 0 && this.tradeData != null;
    }

    public TradeData getLocalTrade() {
        return this.tradeData;
    }

    public void setTrade(int tradeIndex) {
        this.tradeIndex = tradeIndex;
        if (tradeIndex < 0) {
            this.tradeData = null;
        } else {
            this.tradeData = this.copyTrade(this.getTrueTrade());
            if (this.tradeData == null) {
                LightmansCurrency.LogWarning("Trade index of '" + this.tradeIndex + "' does not result in a valid trade on the " + DebugUtil.getSideText(this.isClient()) + ". Resetting back to no trade selected.");
                this.tradeIndex = -1;
            }
        }
    }

    public void refreshTrade() {
        if (this.hasTrade()) {
            TradeData newTrade = this.copyTrade(this.getTrueTrade());
            if (newTrade != null) {
                this.tradeData = newTrade;
            }
        }
    }

    public TradeData copyTrade(TradeData trade) {
        return trade == null ? null : (TradeData) this.tradeDeserializer.apply(trade.getAsNBT());
    }

    public NetworkTradeReference(Supplier<Boolean> clientCheck, Function<CompoundTag, TradeData> tradeDeserializer) {
        super(clientCheck);
        this.tradeDeserializer = tradeDeserializer;
    }

    public TradeData getTrueTrade() {
        if (this.tradeIndex < 0) {
            return null;
        } else {
            TraderData trader = this.getTrader();
            return trader != null ? trader.getTrade(this.tradeIndex) : null;
        }
    }

    @Override
    public CompoundTag save() {
        CompoundTag compound = super.save();
        if (this.tradeData != null && this.tradeIndex >= 0) {
            compound.putInt("TradeIndex", this.tradeIndex);
            compound.put("Trade", this.tradeData.getAsNBT());
        }
        return compound;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        if (compound.contains("tradeIndex", 3)) {
            this.tradeIndex = compound.getInt("tradeIndex");
        }
        if (compound.contains("TradeIndex", 3)) {
            this.tradeIndex = compound.getInt("TradeIndex");
        }
        if (compound.contains("trade", 10)) {
            this.tradeData = (TradeData) this.tradeDeserializer.apply(compound.getCompound("trade"));
        } else if (compound.contains("Trade", 10)) {
            this.tradeData = (TradeData) this.tradeDeserializer.apply(compound.getCompound("Trade"));
        }
    }
}