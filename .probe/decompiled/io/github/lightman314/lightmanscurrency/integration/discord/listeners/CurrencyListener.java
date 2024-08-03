package io.github.lightman314.lightmanscurrency.integration.discord.listeners;

import com.google.common.collect.Lists;
import io.github.lightman314.lightmanscurrency.LCConfig;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.events.AuctionHouseEvent;
import io.github.lightman314.lightmanscurrency.api.events.NotificationEvent;
import io.github.lightman314.lightmanscurrency.api.events.TraderEvent;
import io.github.lightman314.lightmanscurrency.api.misc.player.PlayerReference;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.api.traders.trade.TradeData;
import io.github.lightman314.lightmanscurrency.api.traders.trade.TradeDirection;
import io.github.lightman314.lightmanscurrency.common.traders.TraderSaveData;
import io.github.lightman314.lightmanscurrency.common.traders.auction.tradedata.AuctionTradeData;
import io.github.lightman314.lightmanscurrency.common.traders.item.ItemTraderData;
import io.github.lightman314.lightmanscurrency.common.traders.item.tradedata.ItemTradeData;
import io.github.lightman314.lightmanscurrency.integration.discord.CurrencyMessages;
import io.github.lightman314.lightmanscurrency.integration.discord.data.CurrencyBotData;
import io.github.lightman314.lightmanscurrency.integration.discord.data.CurrencyBotSaveData;
import io.github.lightman314.lightmanscurrency.integration.discord.events.DiscordTraderSearchEvent;
import io.github.lightman314.lightmansdiscord.LightmansDiscordIntegration;
import io.github.lightman314.lightmansdiscord.api.jda.data.SafeMemberReference;
import io.github.lightman314.lightmansdiscord.api.jda.data.SafeUserReference;
import io.github.lightman314.lightmansdiscord.api.jda.data.channels.SafeMessageChannelReference;
import io.github.lightman314.lightmansdiscord.api.jda.data.channels.SafePrivateChannelReference;
import io.github.lightman314.lightmansdiscord.api.jda.data.messages.SafeMessageReference;
import io.github.lightman314.lightmansdiscord.api.jda.listeners.SafeSingleChannelListener;
import io.github.lightman314.lightmansdiscord.discord.links.AccountManager;
import io.github.lightman314.lightmansdiscord.discord.links.LinkedAccount;
import io.github.lightman314.lightmansdiscord.message.MessageManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import net.dv8tion.jda.api.entities.User;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CurrencyListener extends SafeSingleChannelListener {

    private final Timer timer;

    private static final long PENDING_MESSAGE_TIMER = 300000L;

    private static final long ANNOUCEMENT_DELAY = 60000L;

    Map<String, List<String>> pendingMessages = new HashMap();

    protected boolean listenToPrivateMessages() {
        return true;
    }

    public CurrencyListener(Supplier<String> consoleChannel) {
        super(consoleChannel::get);
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new CurrencyListener.NotifyTraderOwnerTask(this), 0L, 300000L);
    }

    protected void OnPrivateMessage(SafePrivateChannelReference channel, SafeUserReference user, SafeMessageReference message) {
        this.handleMessage(channel, message, user);
    }

    public void OnTextChannelMessage(SafeMemberReference member, SafeMessageReference message) {
        this.handleMessage(this.getChannel(), message, member);
    }

    private void handleMessage(SafeMessageChannelReference channel, SafeMessageReference message, SafeUserReference user) {
        if (!user.isBot()) {
            String input = message.getDisplay();
            String prefix = LCConfig.SERVER.ldiCurrencyCommandPrefix.get();
            if (input.startsWith(prefix)) {
                String command = input.substring(prefix.length());
                if (command.startsWith("help")) {
                    List<String> output = new ArrayList();
                    output.add(prefix + "notifications <help|enable|disable> - " + CurrencyMessages.M_HELP_LC_NOTIFICATIONS.get());
                    output.add(prefix + "search <sales|purchases|barters|trades> [searchText] - " + CurrencyMessages.M_HELP_LC_SEARCH1.get());
                    output.add(prefix + "search <players|shops> [searchText] - " + CurrencyMessages.M_HELP_LC_SEARCH2.get());
                    output.add(prefix + "search all - " + CurrencyMessages.M_HELP_LC_SEARCH3.get());
                    channel.sendMessage(output);
                } else if (command.startsWith("notifications ")) {
                    String subcommand = command.substring(14);
                    if (subcommand.startsWith("help")) {
                        List<String> output = new ArrayList();
                        LinkedAccount account = AccountManager.getLinkedAccountFromUser(user);
                        if (account == null) {
                            output.add(CurrencyMessages.M_NOTIFICATIONS_NOTLINKED.get());
                        } else if (CurrencyBotSaveData.getDataFor(account).sendNotificationsToDiscord()) {
                            output.add(CurrencyMessages.M_NOTIFICATIONS_ENABLED.get());
                        } else {
                            output.add(CurrencyMessages.M_NOTIFICATIONS_DISABLED.get());
                        }
                        output.addAll(Lists.newArrayList(CurrencyMessages.M_NOTIFICATIONS_HELP.get().split("\n")));
                        channel.sendMessage(output);
                    } else if (subcommand.startsWith("enable")) {
                        LinkedAccount account = AccountManager.getLinkedAccountFromUser(user);
                        if (account == null) {
                            channel.sendMessage(MessageManager.M_ERROR_NOTLINKEDSELF.get());
                        } else {
                            CurrencyBotData data = CurrencyBotSaveData.getDataFor(account);
                            if (data.sendNotificationsToDiscord()) {
                                channel.sendMessage(CurrencyMessages.M_NOTIFICATIONS_ENABLE_FAIL.get());
                            } else {
                                data.setNotificationsToDiscord(true);
                                channel.sendMessage(CurrencyMessages.M_NOTIFICATIONS_ENABLE_SUCCESS.get());
                            }
                        }
                    } else if (subcommand.startsWith("disable")) {
                        LinkedAccount account = AccountManager.getLinkedAccountFromUser(user);
                        if (account == null) {
                            channel.sendMessage(MessageManager.M_ERROR_NOTLINKEDSELF.get());
                        } else {
                            CurrencyBotData data = CurrencyBotSaveData.getDataFor(account);
                            if (!data.sendNotificationsToDiscord()) {
                                channel.sendMessage(CurrencyMessages.M_NOTIFICATIONS_DISABLE_FAIL.get());
                            } else {
                                data.setNotificationsToDiscord(false);
                                channel.sendMessage(CurrencyMessages.M_NOTIFICATIONS_DISABLE_SUCCESS.get());
                            }
                        }
                    }
                } else if (command.startsWith("search ")) {
                    String subcommand = command.substring(7);
                    String text = "";
                    CurrencyListener.SearchCategory type = null;
                    if (subcommand.startsWith("sales")) {
                        type = CurrencyListener.SearchCategory.TRADE_SALE;
                        if (subcommand.length() > 6) {
                            text = subcommand.substring(6).toLowerCase();
                        }
                    } else if (subcommand.startsWith("purchases")) {
                        type = CurrencyListener.SearchCategory.TRADE_PURCHASE;
                        if (subcommand.length() > 10) {
                            text = subcommand.substring(10).toLowerCase();
                        }
                    } else if (subcommand.startsWith("barters")) {
                        type = CurrencyListener.SearchCategory.TRADE_BARTER;
                        if (subcommand.length() > 10) {
                            text = subcommand.substring(10).toLowerCase();
                        }
                    } else if (subcommand.startsWith("trades")) {
                        type = CurrencyListener.SearchCategory.TRADE_ANY;
                        if (subcommand.length() > 7) {
                            text = subcommand.substring(7).toLowerCase();
                        }
                    } else if (subcommand.startsWith("players")) {
                        type = CurrencyListener.SearchCategory.TRADER_OWNER;
                        if (subcommand.length() > 8) {
                            text = subcommand.substring(8).toLowerCase();
                        }
                    } else if (subcommand.startsWith("shops")) {
                        type = CurrencyListener.SearchCategory.TRADER_NAME;
                        if (subcommand.length() > 6) {
                            text = subcommand.substring(6).toLowerCase();
                        }
                    } else if (subcommand.startsWith("all")) {
                        type = CurrencyListener.SearchCategory.TRADER_ANY;
                    }
                    if (type == null) {
                        channel.sendMessage(CurrencyMessages.M_SEARCH_BAD_INPUT.get());
                        return;
                    }
                    CurrencyListener.SearchCategory searchType = type;
                    String searchText = text;
                    List<String> output = new ArrayList();
                    List<TraderData> traderList = LCConfig.SERVER.ldiLimitSearchToNetworkTraders.get() ? TraderSaveData.GetAllTerminalTraders(false) : TraderSaveData.GetAllTraders(false);
                    traderList.forEach(trader -> {
                        try {
                            if (searchType.acceptTrader(trader, searchText)) {
                                MinecraftForge.EVENT_BUS.post(new DiscordTraderSearchEvent(trader, searchText, searchType, output));
                            }
                        } catch (Throwable var5x) {
                            LightmansCurrency.LogError("Error during the DiscordTraderSearchEvent!", var5x);
                        }
                    });
                    if (!output.isEmpty()) {
                        channel.sendMessage(output);
                    } else {
                        channel.sendMessage(CurrencyMessages.M_SEARCH_NORESULTS.get());
                    }
                }
            }
        }
    }

    private static String getItemName(ItemStack item, String customName) {
        if (item.isEmpty()) {
            return "";
        } else {
            StringBuffer itemName = new StringBuffer();
            if (customName.isEmpty()) {
                itemName.append(item.getHoverName().getString());
            } else {
                itemName.append("*").append(customName).append("*");
            }
            AtomicBoolean firstEnchantment = new AtomicBoolean(true);
            EnchantmentHelper.getEnchantments(item).forEach((enchantment, level) -> {
                if (firstEnchantment.get()) {
                    itemName.append(" [").append(enchantment.getFullname(level).getString());
                    firstEnchantment.set(false);
                } else {
                    itemName.append(", ").append(enchantment.getFullname(level).getString());
                }
            });
            if (!firstEnchantment.get()) {
                itemName.append("]");
            }
            return itemName.toString();
        }
    }

    private static String getItemNamesAndCounts(List<ItemStack> items) {
        List<String> itemEntries = new ArrayList();
        for (ItemStack item : items) {
            if (!item.isEmpty()) {
                itemEntries.add(CurrencyMessages.M_SEARCH_TRADE_ITEM_SINGLE.format(new Object[] { item.getCount(), getItemName(item, "") }));
            }
        }
        if (itemEntries.isEmpty()) {
            return "NULL";
        } else if (itemEntries.size() == 2) {
            return (String) itemEntries.get(0) + CurrencyMessages.M_SEARCH_TRADE_ITEM_DOUBLE + (String) itemEntries.get(1);
        } else {
            StringBuilder buffer = new StringBuilder();
            for (int i = 0; i < itemEntries.size(); i++) {
                if (i != 0) {
                    buffer.append(CurrencyMessages.M_SEARCH_TRADE_ITEM_LIST);
                }
                if (i == itemEntries.size() - 1 && itemEntries.size() > 1) {
                    buffer.append(CurrencyMessages.M_SEARCH_TRADE_ITEM_DOUBLE);
                }
                buffer.append((String) itemEntries.get(i));
            }
            return buffer.toString();
        }
    }

    public static String getItemNamesAndCount(ItemStack item1, String customName1, ItemStack item2, String customName2) {
        if (item1.isEmpty() && !item2.isEmpty()) {
            return CurrencyMessages.M_SEARCH_TRADE_ITEM_SINGLE.format(new Object[] { item2.getCount(), getItemName(item2, customName2) });
        } else {
            return item2.isEmpty() && !item1.isEmpty() ? CurrencyMessages.M_SEARCH_TRADE_ITEM_SINGLE.format(new Object[] { item1.getCount(), getItemName(item1, customName1) }) : CurrencyMessages.M_SEARCH_TRADE_ITEM_SINGLE.format(new Object[] { item1.getCount(), getItemName(item1, customName1) }) + CurrencyMessages.M_SEARCH_TRADE_ITEM_DOUBLE.get() + CurrencyMessages.M_SEARCH_TRADE_ITEM_SINGLE.format(new Object[] { item2.getCount(), getItemName(item2, customName2) });
        }
    }

    @SubscribeEvent
    public void onNotification(NotificationEvent.NotificationSent.Post event) {
        try {
            LinkedAccount account = AccountManager.getLinkedAccountFromPlayerID(event.getPlayerID());
            if (account != null) {
                SafeUserReference user = account.getUser();
                if (CurrencyBotSaveData.getDataFor(account).sendNotificationsToDiscord()) {
                    this.addPendingMessage(user.getUser(), event.getNotification().getGeneralMessage().getString());
                }
            }
        } catch (Exception var4) {
            LightmansCurrency.LogError("Error processing notification to bot:", var4);
        }
    }

    @SubscribeEvent
    public void onTraderSearch(DiscordTraderSearchEvent event) {
        TraderData trader = event.getTrader();
        String searchText = event.getSearchText();
        if (trader instanceof ItemTraderData itemTrader) {
            boolean showStock = !itemTrader.isCreative();
            boolean firstTrade = true;
            for (int i = 0; i < itemTrader.getTradeCount(); i++) {
                ItemTradeData trade = itemTrader.getTrade(i);
                if (trade.isValid() && event.acceptTradeType(trade)) {
                    if (trade.isSale()) {
                        String itemName1 = getItemName(trade.getSellItem(0), trade.getCustomName(0));
                        String itemName2 = getItemName(trade.getSellItem(1), trade.getCustomName(1));
                        if (searchText.isEmpty() || itemName1.toLowerCase().contains(searchText) || itemName2.toLowerCase().contains(searchText)) {
                            if (firstTrade) {
                                event.addToOutput(CurrencyMessages.M_SEARCH_TRADER_NAME.format(new Object[] { itemTrader.getOwner().getName(), trader.getName() }));
                                firstTrade = false;
                            }
                            String priceText = trade.getCost().getString();
                            event.addToOutput(CurrencyMessages.M_SEARCH_TRADE_ITEM_SALE.format(new Object[] { getItemNamesAndCount(trade.getSellItem(0), trade.getCustomName(0), trade.getSellItem(1), trade.getCustomName(1)), priceText }));
                            if (showStock) {
                                event.addToOutput(CurrencyMessages.M_SEARCH_TRADE_STOCK.format(new Object[] { trade.stockCount(itemTrader) }));
                            }
                        }
                    } else if (trade.isPurchase()) {
                        String itemName1 = getItemName(trade.getSellItem(0), "");
                        String itemName2 = getItemName(trade.getSellItem(1), "");
                        if (searchText.isEmpty() || itemName1.toLowerCase().contains(searchText) || itemName2.toLowerCase().contains(searchText)) {
                            if (firstTrade) {
                                event.addToOutput(CurrencyMessages.M_SEARCH_TRADER_NAME.format(new Object[] { itemTrader.getOwner().getName(), trader.getName() }));
                                firstTrade = false;
                            }
                            event.addToOutput(CurrencyMessages.M_SEARCH_TRADE_ITEM_PURCHASE.format(new Object[] { getItemNamesAndCount(trade.getSellItem(0), "", trade.getSellItem(1), ""), trade.getCost().getString() }));
                            if (showStock) {
                                event.addToOutput(CurrencyMessages.M_SEARCH_TRADE_STOCK.format(new Object[] { trade.stockCount(itemTrader) }));
                            }
                        }
                    } else if (trade.isBarter()) {
                        String itemName1 = getItemName(trade.getSellItem(0), trade.getCustomName(0));
                        String itemName2 = getItemName(trade.getSellItem(1), trade.getCustomName(1));
                        String itemName3 = getItemName(trade.getBarterItem(0), "");
                        String itemName4 = getItemName(trade.getBarterItem(1), "");
                        if (searchText.isEmpty() || itemName1.toLowerCase().contains(searchText) || itemName2.toLowerCase().contains(searchText) || itemName3.toLowerCase().contains(searchText) || itemName4.toLowerCase().contains(searchText)) {
                            if (firstTrade) {
                                event.addToOutput(CurrencyMessages.M_SEARCH_TRADER_NAME.format(new Object[] { itemTrader.getOwner().getName(), trader.getName() }));
                                firstTrade = false;
                            }
                            event.addToOutput(CurrencyMessages.M_SEARCH_TRADE_ITEM_BARTER.format(new Object[] { getItemNamesAndCount(trade.getBarterItem(0), "", trade.getBarterItem(1), ""), getItemNamesAndCount(trade.getSellItem(0), trade.getCustomName(0), trade.getSellItem(1), trade.getCustomName(1)) }));
                            if (showStock) {
                                event.addToOutput(CurrencyMessages.M_SEARCH_TRADE_STOCK.format(new Object[] { trade.stockCount(itemTrader) }));
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onAuctionCreated(AuctionHouseEvent.AuctionEvent.CreateAuctionEvent.Post event) {
        if (LCConfig.SERVER.ldiAuctionCreateNotification.get()) {
            if (!event.isPersistent() || LCConfig.SERVER.ldiAuctionPersistentCreateNotification.get()) {
                AuctionTradeData auction = event.getAuction();
                String itemText = getItemNamesAndCounts(auction.getAuctionItems());
                String startingBid = auction.getLastBidAmount().getString();
                String minBid = auction.getMinBidDifference().getString();
                if (event.isPersistent()) {
                    this.sendMessage(CurrencyMessages.M_NEWAUCTION_PERSISTENT.format(new Object[] { itemText, startingBid, minBid }));
                } else {
                    PlayerReference owner = auction.getOwner();
                    String ownerName = owner != null ? owner.getName(false) : "NULL";
                    this.sendMessage(CurrencyMessages.M_NEWAUCTION.format(new Object[] { ownerName, itemText, startingBid, minBid }));
                }
            }
        }
    }

    @SubscribeEvent
    public void onAuctionCanceled(AuctionHouseEvent.AuctionEvent.CancelAuctionEvent event) {
        if (LCConfig.SERVER.ldiAuctionCancelNotification.get()) {
            this.sendMessage(CurrencyMessages.M_CANCELAUCTION.format(new Object[] { event.getPlayer().getDisplayName().getString(), getItemNamesAndCounts(event.getAuction().getAuctionItems()) }));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onAuctionCompleted(AuctionHouseEvent.AuctionEvent.AuctionCompletedEvent event) {
        if (LCConfig.SERVER.ldiAuctionWinNotification.get() && event.hadBidder()) {
            AuctionTradeData auction = event.getAuction();
            if (auction.getLastBidPlayer() != null) {
                String winner = auction.getLastBidPlayer().getName(false);
                String itemText = getItemNamesAndCounts(auction.getAuctionItems());
                String price = auction.getLastBidAmount().getString();
                this.sendMessage(CurrencyMessages.M_WINAUCTION.format(new Object[] { winner, itemText, price }));
            }
        }
    }

    public void addPendingMessage(User user, String message) {
        this.addPendingMessage(user, Lists.newArrayList(new String[] { message }));
    }

    public void addPendingMessage(User user, List<String> messages) {
        String userId = user.getId();
        List<String> pendingMessages = (List<String>) (this.pendingMessages.containsKey(userId) ? (List) this.pendingMessages.get(userId) : Lists.newArrayList());
        pendingMessages.addAll(messages);
        this.pendingMessages.put(userId, pendingMessages);
    }

    public void sendPendingMessages() {
        this.pendingMessages.forEach((userId, messages) -> {
            try {
                User user = LightmansDiscordIntegration.PROXY.getJDA().getUserById(userId);
                if (user != null) {
                    SafeUserReference.of(user).sendPrivateMessage(messages);
                }
            } catch (Throwable var3) {
                LightmansCurrency.LogError("Error sending messages!", var3);
            }
        });
        this.pendingMessages.clear();
    }

    @SubscribeEvent
    public void onUniversalTraderRegistered(TraderEvent.CreateNetworkTraderEvent event) {
        if (LCConfig.SERVER.ldiNetworkTraderNotification.get()) {
            new Timer().schedule(new CurrencyListener.AnnouncementTask(this, event), 60000L);
        }
    }

    @SubscribeEvent
    public void onServerStop(ServerStoppingEvent event) {
        this.timer.cancel();
        this.sendPendingMessages();
    }

    private static class AnnouncementTask extends TimerTask {

        private final CurrencyListener cl;

        private final TraderEvent.CreateNetworkTraderEvent event;

        public AnnouncementTask(CurrencyListener cl, TraderEvent.CreateNetworkTraderEvent event) {
            this.cl = cl;
            this.event = event;
        }

        public void run() {
            try {
                TraderData trader = this.event.getTrader();
                if (trader == null) {
                    return;
                }
                if (trader.hasCustomName()) {
                    this.cl.sendMessage(CurrencyMessages.M_NEWTRADER_NAMED.format(new Object[] { trader.getOwner().getName(), trader.getCustomName() }));
                } else {
                    this.cl.sendMessage(CurrencyMessages.M_NEWTRADER.format(new Object[] { trader.getOwner().getName() }));
                }
            } catch (Exception var2) {
                LightmansCurrency.LogError("Error sending New Trader Announcement", var2);
            }
        }
    }

    private static class NotifyTraderOwnerTask extends TimerTask {

        private final CurrencyListener cl;

        public NotifyTraderOwnerTask(CurrencyListener cl) {
            this.cl = cl;
        }

        public void run() {
            this.cl.sendPendingMessages();
        }
    }

    public static enum SearchCategory {

        TRADE_SALE(trade -> trade.getTradeDirection() == TradeDirection.SALE),
        TRADE_PURCHASE(trade -> trade.getTradeDirection() == TradeDirection.PURCHASE),
        TRADE_BARTER(trade -> trade.getTradeDirection() == TradeDirection.BARTER),
        TRADE_ANY(trade -> true),
        TRADER_OWNER((trader, search) -> search.isEmpty() || trader.getOwner().getName().getString().toLowerCase().contains(search)),
        TRADER_NAME((trader, search) -> search.isEmpty() || trader.getName().getString().toLowerCase().contains(search)),
        TRADER_ANY((trader, search) -> true);

        private final boolean filterByTrade;

        private final Function<TradeData, Boolean> tradeFilter;

        private final BiFunction<TraderData, String, Boolean> acceptTrader;

        public boolean filterByTrade() {
            return this.filterByTrade;
        }

        public boolean acceptTradeType(TradeData trade) {
            return (Boolean) this.tradeFilter.apply(trade);
        }

        public boolean acceptTrader(TraderData trader, String searchText) {
            return (Boolean) this.acceptTrader.apply(trader, searchText);
        }

        private SearchCategory(Function<TradeData, Boolean> tradeFilter) {
            this.filterByTrade = true;
            this.tradeFilter = tradeFilter;
            this.acceptTrader = (t, s) -> true;
        }

        private SearchCategory(BiFunction<TraderData, String, Boolean> acceptTrader) {
            this.filterByTrade = false;
            this.tradeFilter = t -> true;
            this.acceptTrader = acceptTrader;
        }
    }
}