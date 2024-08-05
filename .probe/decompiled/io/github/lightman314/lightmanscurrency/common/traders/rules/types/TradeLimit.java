package io.github.lightman314.lightmanscurrency.common.traders.rules.types;

import com.google.gson.JsonObject;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.events.TradeEvent;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.traders.rules.TradeRuleType;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.trade_rules.TradeRulesClientSubTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.trade_rules.TradeRulesClientTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.trade_rules.rule_tabs.TradeLimitTab;
import io.github.lightman314.lightmanscurrency.common.traders.rules.TradeRule;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TradeLimit extends TradeRule {

    public static final TradeRuleType<TradeLimit> TYPE = new TradeRuleType<>(new ResourceLocation("lightmanscurrency", "trade_limit"), TradeLimit::new);

    private int limit = 1;

    int count = 0;

    public int getLimit() {
        return this.limit;
    }

    public void setLimit(int newLimit) {
        this.limit = newLimit;
    }

    public void resetCount() {
        this.count = 0;
    }

    private TradeLimit() {
        super(TYPE);
    }

    @Override
    public void beforeTrade(TradeEvent.PreTradeEvent event) {
        if (this.count >= this.limit) {
            event.addDenial(LCText.TRADE_RULE_TRADE_LIMIT_DENIAL.get(this.count));
            event.addDenial(LCText.TRADE_RULE_PLAYER_TRADE_LIMIT_DENIAL_LIMIT.get(this.limit));
        } else {
            event.addHelpful(LCText.TRADE_RULE_TRADE_LIMIT_INFO.get(this.count, this.limit));
        }
    }

    @Override
    public void afterTrade(TradeEvent.PostTradeEvent event) {
        this.count++;
        event.markDirty();
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag compound) {
        compound.putInt("Limit", this.limit);
        compound.putInt("Count", this.count);
    }

    @Override
    public JsonObject saveToJson(@Nonnull JsonObject json) {
        json.addProperty("Limit", this.limit);
        return json;
    }

    @Override
    protected void loadAdditional(@Nonnull CompoundTag compound) {
        if (compound.contains("Limit", 3)) {
            this.limit = compound.getInt("Limit");
        }
        if (compound.contains("Count", 3)) {
            this.count = compound.getInt("Count");
        }
    }

    @Override
    public void loadFromJson(@Nonnull JsonObject json) {
        if (json.has("Limit")) {
            this.limit = json.get("Limit").getAsInt();
        }
    }

    @Override
    public void handleUpdateMessage(@Nonnull LazyPacketData updateInfo) {
        if (updateInfo.contains("Limit")) {
            this.limit = updateInfo.getInt("Limit");
        } else if (updateInfo.contains("ClearMemory")) {
            this.count = 0;
        }
    }

    @Override
    public CompoundTag savePersistentData() {
        CompoundTag data = new CompoundTag();
        data.putInt("Count", this.count);
        return data;
    }

    @Override
    public void loadPersistentData(CompoundTag data) {
        if (data.contains("Count", 3)) {
            this.count = data.getInt("Count");
        }
    }

    @Nonnull
    @OnlyIn(Dist.CLIENT)
    @Override
    public TradeRulesClientSubTab createTab(TradeRulesClientTab<?> parent) {
        return new TradeLimitTab(parent);
    }
}