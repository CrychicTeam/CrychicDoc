package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.trade_rules;

import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.traders.rules.TradeRuleType;
import io.github.lightman314.lightmanscurrency.common.traders.rules.ITradeRuleHost;
import io.github.lightman314.lightmanscurrency.common.traders.rules.TradeRule;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.network.chat.MutableComponent;

public abstract class TradeRuleSubTab<T extends TradeRule> extends TradeRulesClientSubTab {

    public final TradeRuleType<T> ruleType;

    public TradeRuleSubTab(@Nonnull TradeRulesClientTab<?> parent, @Nonnull TradeRuleType<T> ruleType) {
        super(parent);
        this.ruleType = ruleType;
    }

    @Nullable
    protected final T getRule() {
        ITradeRuleHost host = this.commonTab.getHost();
        if (host != null) {
            try {
                return (T) host.getRuleOfType(this.ruleType.type);
            } catch (Throwable var3) {
            }
        }
        return null;
    }

    @Override
    public boolean isVisible() {
        T rule = this.getRule();
        return rule != null ? rule.isActive() : false;
    }

    public MutableComponent getTooltip() {
        return TradeRule.nameOfType(this.ruleType.type);
    }

    public void sendUpdateMessage(@Nonnull LazyPacketData.Builder updateInfo) {
        this.commonTab.EditTradeRule(this.ruleType, updateInfo);
    }
}