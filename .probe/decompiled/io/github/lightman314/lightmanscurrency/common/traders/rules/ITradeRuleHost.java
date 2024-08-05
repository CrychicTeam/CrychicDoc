package io.github.lightman314.lightmanscurrency.common.traders.rules;

import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;

public interface ITradeRuleHost {

    default boolean allowTradeRule(@Nonnull TradeRule rule) {
        return true;
    }

    boolean isTrader();

    boolean isTrade();

    default boolean canMoneyBeRelevant() {
        return true;
    }

    default boolean isMoneyRelevant() {
        return this.canMoneyBeRelevant();
    }

    void markTradeRulesDirty();

    @Nonnull
    List<TradeRule> getRules();

    default TradeRule getRuleOfType(@Nonnull ResourceLocation ruleType) {
        return TradeRule.GetTradeRule(this.getRules(), ruleType);
    }

    default void validateRuleStates() {
        TradeRule.ValidateTradeRuleActiveStates(this.getRules());
    }

    default void HandleRuleUpdate(@Nonnull ResourceLocation type, @Nonnull LazyPacketData updateData) {
        TradeRule rule = this.getRuleOfType(type);
        if (rule != null) {
            rule.receiveUpdateMessage(updateData);
            this.markTradeRulesDirty();
        }
    }
}