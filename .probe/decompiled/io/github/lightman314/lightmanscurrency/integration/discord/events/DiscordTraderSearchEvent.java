package io.github.lightman314.lightmanscurrency.integration.discord.events;

import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.api.traders.trade.TradeData;
import io.github.lightman314.lightmanscurrency.integration.discord.listeners.CurrencyListener;
import java.util.List;
import net.minecraftforge.eventbus.api.Event;

public class DiscordTraderSearchEvent extends Event {

    private final TraderData trader;

    private final String searchText;

    private final CurrencyListener.SearchCategory searchType;

    private final List<String> output;

    public final TraderData getTrader() {
        return this.trader;
    }

    public final String getSearchText() {
        return this.searchText;
    }

    public final boolean filterByTrades() {
        return this.searchType.filterByTrade();
    }

    public final boolean acceptTradeType(TradeData trade) {
        return this.searchType.acceptTradeType(trade);
    }

    public final boolean acceptTrader(TraderData trader) {
        return this.searchType.acceptTrader(trader, this.searchText);
    }

    public DiscordTraderSearchEvent(TraderData trader, String searchText, CurrencyListener.SearchCategory searchType, List<String> outputList) {
        this.trader = trader;
        this.searchText = searchText;
        this.searchType = searchType;
        this.output = outputList;
    }

    public void addToOutput(String line) {
        this.output.add(line);
    }
}