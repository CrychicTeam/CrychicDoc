package io.github.lightman314.lightmanscurrency.common.traders.rules.types;

import com.google.gson.JsonObject;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.events.TradeEvent;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.traders.rules.TradeRuleType;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.trade_rules.TradeRulesClientSubTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.trade_rules.TradeRulesClientTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.trade_rules.rule_tabs.PriceFluctuationTab;
import io.github.lightman314.lightmanscurrency.common.traders.rules.PriceTweakingTradeRule;
import io.github.lightman314.lightmanscurrency.util.MathUtil;
import io.github.lightman314.lightmanscurrency.util.TimeUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PriceFluctuation extends PriceTweakingTradeRule {

    public static final TradeRuleType<PriceFluctuation> TYPE = new TradeRuleType<>(new ResourceLocation("lightmanscurrency", "price_fluctuation"), PriceFluctuation::new);

    long duration = 86400000L;

    int fluctuation = 10;

    private static final List<Long> debuggedSeeds = new ArrayList();

    private static final List<Long> debuggedTraderFactors = new ArrayList();

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long duration) {
        this.duration = MathUtil.clamp(duration, 1000L, Long.MAX_VALUE);
    }

    public int getFluctuation() {
        return this.fluctuation;
    }

    public void setFluctuation(int fluctuation) {
        this.fluctuation = MathUtil.clamp(fluctuation, 1, Integer.MAX_VALUE);
    }

    public PriceFluctuation() {
        super(TYPE);
    }

    private static void debugTraderFactor(long factor, long traderID, int tradeIndex) {
        if (!debuggedTraderFactors.contains(factor)) {
            LightmansCurrency.LogDebug("Trader Seed Factor for trader with id '" + traderID + "' and trade index '" + tradeIndex + "' is " + factor);
            debuggedTraderFactors.add(factor);
        }
    }

    private static void debugFlux(long seed, int maxFlux, int flux) {
        if (!debuggedSeeds.contains(seed)) {
            LightmansCurrency.LogDebug("Price Fluctuation for trade with seed '" + seed + "' and max fluctuation of " + maxFlux + "% is " + flux + "%");
            debuggedSeeds.add(seed);
        }
    }

    private long getTraderSeedFactor(TradeEvent.TradeCostEvent event) {
        long traderID = event.getTrader().getID();
        int tradeIndex = event.getTradeIndex();
        long factor = (traderID + 1L << 32) + (long) tradeIndex;
        debugTraderFactor(factor, traderID, tradeIndex);
        return factor;
    }

    private int randomizePriceMultiplier(long traderSeedFactor) {
        long seed = TimeUtil.getCurrentTime() / this.duration;
        int fluct = new Random(seed * traderSeedFactor).nextInt(-this.fluctuation, this.fluctuation + 1);
        debugFlux(seed * traderSeedFactor, this.fluctuation, fluct);
        return fluct;
    }

    @Override
    public void tradeCost(TradeEvent.TradeCostEvent event) {
        event.giveDiscount(this.randomizePriceMultiplier(this.getTraderSeedFactor(event)));
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag compound) {
        compound.putLong("Duration", this.duration);
        compound.putInt("Fluctuation", this.fluctuation);
    }

    @Override
    protected void loadAdditional(@Nonnull CompoundTag compound) {
        this.duration = compound.getLong("Duration");
        if (this.duration <= 0L) {
            this.duration = 86400000L;
        }
        this.fluctuation = compound.getInt("Fluctuation");
    }

    @Override
    public JsonObject saveToJson(@Nonnull JsonObject json) {
        json.addProperty("Duration", this.duration);
        json.addProperty("Fluctuation", this.fluctuation);
        return json;
    }

    @Override
    public void loadFromJson(@Nonnull JsonObject json) {
        if (json.has("Duration")) {
            this.duration = json.get("Duration").getAsLong();
        }
        if (json.has("Fluctuation")) {
            this.fluctuation = json.get("Fluctuation").getAsInt();
        }
    }

    @Override
    public CompoundTag savePersistentData() {
        return null;
    }

    @Override
    public void loadPersistentData(CompoundTag data) {
    }

    @Override
    protected void handleUpdateMessage(@Nonnull LazyPacketData updateInfo) {
        if (updateInfo.contains("Duration")) {
            this.setDuration(updateInfo.getLong("Duration"));
        }
        if (updateInfo.contains("Fluctuation")) {
            this.setFluctuation(updateInfo.getInt("Fluctuation"));
        }
    }

    @Nonnull
    @OnlyIn(Dist.CLIENT)
    @Override
    public TradeRulesClientSubTab createTab(TradeRulesClientTab<?> parent) {
        return new PriceFluctuationTab(parent);
    }
}