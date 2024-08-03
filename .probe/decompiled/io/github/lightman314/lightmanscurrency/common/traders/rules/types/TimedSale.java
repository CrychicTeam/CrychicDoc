package io.github.lightman314.lightmanscurrency.common.traders.rules.types;

import com.google.gson.JsonObject;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.events.TradeEvent;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.traders.rules.TradeRuleType;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.trade_rules.TradeRulesClientSubTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.trade_rules.TradeRulesClientTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.trade_rules.rule_tabs.TimedSaleTab;
import io.github.lightman314.lightmanscurrency.common.traders.rules.PriceTweakingTradeRule;
import io.github.lightman314.lightmanscurrency.util.MathUtil;
import io.github.lightman314.lightmanscurrency.util.TimeUtil;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TimedSale extends PriceTweakingTradeRule {

    public static final TradeRuleType<TimedSale> TYPE = new TradeRuleType<>(new ResourceLocation("lightmanscurrency", "timed_sale"), TimedSale::new);

    long startTime = 0L;

    long duration = 0L;

    int discount = 10;

    public void setStartTime(long time) {
        this.startTime = time;
    }

    public boolean timerActive() {
        return this.startTime != 0L;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long duration) {
        this.duration = MathUtil.clamp(duration, 1000L, Long.MAX_VALUE);
    }

    public int getDiscount() {
        return this.discount;
    }

    public void setDiscount(int discount) {
        this.discount = MathUtil.clamp(discount, 1, 100);
    }

    private TimedSale() {
        super(TYPE);
    }

    @Override
    public void beforeTrade(TradeEvent.PreTradeEvent event) {
        if (this.timerActive() && TimeUtil.compareTime(this.duration, this.startTime)) {
            switch(event.getTrade().getTradeDirection()) {
                case SALE:
                    event.addHelpful(LCText.TRADE_RULE_TIMED_SALE_INFO_SALE.get(this.discount, this.getTimeRemaining().getString()));
                    break;
                case PURCHASE:
                    event.addHelpful(LCText.TRADE_RULE_TIMED_SALE_INFO_PURCHASE.get(this.discount, this.getTimeRemaining().getString()));
            }
        }
    }

    @Override
    public void tradeCost(TradeEvent.TradeCostEvent event) {
        if (this.timerActive() && TimeUtil.compareTime(this.duration, this.startTime)) {
            switch(event.getTrade().getTradeDirection()) {
                case SALE:
                    event.giveDiscount(this.discount);
                    break;
                case PURCHASE:
                    event.hikePrice(this.discount);
            }
        }
    }

    @Override
    public void afterTrade(TradeEvent.PostTradeEvent event) {
        if (this.confirmStillActive()) {
            event.markDirty();
        }
    }

    private boolean confirmStillActive() {
        if (!this.timerActive()) {
            return false;
        } else if (!TimeUtil.compareTime(this.duration, this.startTime)) {
            this.startTime = 0L;
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag compound) {
        compound.putLong("startTime", this.startTime);
        compound.putLong("duration", this.duration);
        compound.putInt("discount", this.discount);
    }

    @Override
    public JsonObject saveToJson(@Nonnull JsonObject json) {
        json.addProperty("duration", this.duration);
        json.addProperty("discount", this.discount);
        return json;
    }

    @Override
    protected void loadAdditional(@Nonnull CompoundTag compound) {
        if (compound.contains("startTime", 4)) {
            this.startTime = compound.getLong("startTime");
        }
        if (compound.contains("duration", 4)) {
            this.duration = compound.getLong("duration");
        }
        if (compound.contains("discount", 3)) {
            this.discount = compound.getInt("discount");
        }
    }

    @Override
    public void loadFromJson(@Nonnull JsonObject json) {
        if (json.has("duration")) {
            this.duration = json.get("duration").getAsLong();
        }
        if (json.has("discount")) {
            this.discount = MathUtil.clamp(this.discount, 0, 100);
        }
    }

    @Override
    public void handleUpdateMessage(@Nonnull LazyPacketData updateInfo) {
        if (updateInfo.contains("Discount")) {
            this.discount = updateInfo.getInt("Discount");
        } else if (updateInfo.contains("Duration")) {
            this.duration = updateInfo.getLong("Duration");
        } else if (updateInfo.contains("StartSale")) {
            if (this.timerActive() == updateInfo.getBoolean("StartSale")) {
                return;
            }
            if (this.timerActive()) {
                this.startTime = 0L;
            } else {
                this.startTime = TimeUtil.getCurrentTime();
            }
        }
    }

    @Override
    public CompoundTag savePersistentData() {
        CompoundTag compound = new CompoundTag();
        compound.putLong("startTime", this.startTime);
        return compound;
    }

    @Override
    public void loadPersistentData(CompoundTag data) {
        if (data.contains("startTime", 4)) {
            this.startTime = data.getLong("startTime");
        }
    }

    public TimeUtil.TimeData getTimeRemaining() {
        return !this.timerActive() ? new TimeUtil.TimeData(0L) : new TimeUtil.TimeData(this.startTime + this.duration - TimeUtil.getCurrentTime());
    }

    @Nonnull
    @OnlyIn(Dist.CLIENT)
    @Override
    public TradeRulesClientSubTab createTab(TradeRulesClientTab<?> parent) {
        return new TimedSaleTab(parent);
    }
}