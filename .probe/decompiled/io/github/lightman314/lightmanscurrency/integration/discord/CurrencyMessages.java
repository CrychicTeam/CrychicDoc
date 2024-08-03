package io.github.lightman314.lightmanscurrency.integration.discord;

import io.github.lightman314.lightmansdiscord.events.LoadMessageEntriesEvent;
import io.github.lightman314.lightmansdiscord.message.MessageEntry;
import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CurrencyMessages {

    private static final List<MessageEntry> ENTRIES = new ArrayList();

    public static final MessageEntry M_HELP_LC_NOTIFICATIONS = MessageEntry.create(ENTRIES, "help_lc_notifications", "Help message for lightman's currency !notifications.", "Handle private currency notifications.", new String[0]);

    public static final MessageEntry M_HELP_LC_SEARCH1 = MessageEntry.create(ENTRIES, "help_lc_search1", "Help message for lightman's currency !search <sales|purchases|barters|trades>.", "List all universal trades selling items containing the searchText.", new String[0]);

    public static final MessageEntry M_HELP_LC_SEARCH2 = MessageEntry.create(ENTRIES, "help_lc_search2", "Help message for lightman's currency !search <players|shops>.", "List all trades for universal traders with player/shop names containing the searchText. Leave searchText empty to see all traders trades.", new String[0]);

    public static final MessageEntry M_HELP_LC_SEARCH3 = MessageEntry.create(ENTRIES, "help_lc_search3", "Help message for lightman's currency !search <all>.", "List all trades.", new String[0]);

    public static final MessageEntry M_NOTIFICATIONS_ENABLED = MessageEntry.create(ENTRIES, "command_notifications_enabled", "Message sent when running !messages help while notifications are enabled.", "Personal notifications are enabled.", new String[0]);

    public static final MessageEntry M_NOTIFICATIONS_DISABLED = MessageEntry.create(ENTRIES, "command_notifications_disabled", "Message sent when running !messages help while notifications are disabled.", "Personal notifications are disabled.", new String[0]);

    public static final MessageEntry M_NOTIFICATIONS_NOTLINKED = MessageEntry.create(ENTRIES, "command_notifications_not_linked", "Message sent when running !messages help when their account is not linked.", "Your account must be linked in order to set your notification preferences.", new String[0]);

    public static final MessageEntry M_NOTIFICATIONS_HELP = MessageEntry.create(ENTRIES, "command_notifications_help", "Remaining message sent when running !messages help.", "If personal notifications are enabled you will receive copies of your in-game notifications via Discord PM.", new String[0]);

    public static final MessageEntry M_NOTIFICATIONS_ENABLE_SUCCESS = MessageEntry.create(ENTRIES, "command_notifications_enable_successs", "Message sent when running !messages enable successfully.", "Personal notifications are now enabled.", new String[0]);

    public static final MessageEntry M_NOTIFICATIONS_ENABLE_FAIL = MessageEntry.create(ENTRIES, "command_notifications_enable_fail", "Message sent when failing to run !messages enable.", "Personal notifications were already enabled.", new String[0]);

    public static final MessageEntry M_NOTIFICATIONS_DISABLE_SUCCESS = MessageEntry.create(ENTRIES, "command_notifications_disable_successs", "Message sent when running !messages disable successfully.", "Personal notifications are now disabled.", new String[0]);

    public static final MessageEntry M_NOTIFICATIONS_DISABLE_FAIL = MessageEntry.create(ENTRIES, "command_notifications_disable_fail", "Message sent when failing to run !messages disable.", "Personal notifications were already disabled.", new String[0]);

    public static final MessageEntry M_SEARCH_BAD_INPUT = MessageEntry.create(ENTRIES, "command_search_badinput", "Message sent when !search is run with an invalid sub-command (sales,purchases,players, etc.).", "Invalid search type.", new String[0]);

    public static final MessageEntry M_SEARCH_NORESULTS = MessageEntry.create(ENTRIES, "command_search_noresults", "Message sent when !search is run and no search results were found.", "No results found.", new String[0]);

    public static final MessageEntry M_SEARCH_TRADER_NAME = MessageEntry.create(ENTRIES, "command_search_trader_title", "Format of a traders title when displaying the results of the search.\n{owner} for the name of the traders owner.\n{trader} for the name of the trader.", "--{owner}'s **{trader}**--", new String[] { "owner", "trader" });

    public static final MessageEntry M_SEARCH_TRADE_STOCK = MessageEntry.create(ENTRIES, "command_search_trade_stock", "Format of a trades stock count when displaying the results of the search.\n{stock} for the number of trades left in stock.", "*{stock} trades left in stock", new String[] { "stock" });

    public static final MessageEntry M_SEARCH_TRADE_ITEM_SALE = MessageEntry.create(ENTRIES, "command_search_trade_item_sale", "Format of an item sale when displaying the results of the search.\n{items} for the items being sold.\n{price} for the price.", "Selling {items} for {price}", new String[] { "items", "price" });

    public static final MessageEntry M_SEARCH_TRADE_ITEM_PURCHASE = MessageEntry.create(ENTRIES, "command_search_trade_item_purchase", "Format of an item purchase when displaying the results of the search.\n{items} for the items being purchased.\n{price} for the price.", "Purchasing {items} for {price}", new String[] { "items", "price" });

    public static final MessageEntry M_SEARCH_TRADE_ITEM_BARTER = MessageEntry.create(ENTRIES, "command_search_trade_item_barter", "Format of an item barter when displaying the results of the search.\n{saleItems} for the items being sold.\n{barterItems} for the items paid.", "Bartering {barterItems} for {saleItems}", new String[] { "barterItems", "saleItems" });

    public static final MessageEntry M_SEARCH_TRADE_ITEM_SINGLE = MessageEntry.create(ENTRIES, "command_search_trade_item_single", "Format of an item when displaying the results of the search.\n{count} for the item count.\n{item} for the items name.", "{count}x {item}", new String[] { "count", "item" });

    public static final MessageEntry M_SEARCH_TRADE_ITEM_DOUBLE = MessageEntry.create(ENTRIES, "command_search_trade_item_double", "Splitter between exactly two items being displayed in a row.", " and ", new String[0]);

    public static final MessageEntry M_SEARCH_TRADE_ITEM_LIST = MessageEntry.create(ENTRIES, "command_search_trade_item_list", "Splitter between more than two items being displayed in a row.", ", ", new String[0]);

    public static final MessageEntry M_NEWTRADER = MessageEntry.create(ENTRIES, "lightmanscurrency_newtrader", "Announcement made in the currency bot channel when a new network trader has been made.\n{player} for the traders owner name.", "{player} has made a new Trading Server!", new String[] { "player" });

    public static final MessageEntry M_NEWTRADER_NAMED = MessageEntry.create(ENTRIES, "lightmanscurrency_newtrader_named", "Announcement made in the currency bot channel when a new network trader with a custom name has been made.\n{player} for the traders owner name.\n{trader} for the traders custom name.", "{player} has made a new Trading Server '{trader}'!", new String[] { "player", "trader" });

    public static final MessageEntry M_NEWAUCTION = MessageEntry.create(ENTRIES, "lightmanscurrency_newauction", "Announcement made in the currency bot channel when a new auction is made.\n{player} for the auctions owner name.\n{items} for the items being sold.\n{startingBid} for the starting bid.\n{minBid} for the minimum bid difference.", "{player} has created an auction selling {items} with a starting bid of {startingBid}!", new String[] { "player", "items", "startingBid", "minBid" });

    public static final MessageEntry M_NEWAUCTION_PERSISTENT = MessageEntry.create(ENTRIES, "lightmanscurrency_newauction_persistent", "Announcement made in the currency bot channel when a new persistent auction is made.\n{items} for the items being sold.\n{startingBid} for the starting bid.\n{minBid} for the minimum bid difference.", "The server has created an auction selling {items} with a starting bid of {startingBid}!", new String[] { "items", "startingBid", "minBid" });

    public static final MessageEntry M_CANCELAUCTION = MessageEntry.create(ENTRIES, "lightmanscurrency_cancelauction", "Announcement made in the currency bot channel when an auction is canceled.\n{player} for the person who canceled the auction.\n{items} for the items being sold.\n", "The auction for {items} has been cancelled!", new String[] { "player", "items" });

    public static final MessageEntry M_WINAUCTION = MessageEntry.create(ENTRIES, "lightmanscurrency_winauction", "Announcement made in the currency bot channel when a player wins an auction.\n{player} for the highest bidder that won the auction.\n{items} for the items being sold.\n{bid} for the amount paid to win the bid.", "{player} won the auction for {items} with a bid of {bid}!", new String[] { "player", "items", "bid" });

    @SubscribeEvent
    public static void registerMessages(LoadMessageEntriesEvent event) {
        event.register(ENTRIES);
    }
}