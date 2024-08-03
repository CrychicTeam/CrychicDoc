package io.github.lightman314.lightmanscurrency;

import io.github.lightman314.lightmanscurrency.api.trader_interface.blockentity.TraderInterfaceBlockEntity;
import io.github.lightman314.lightmanscurrency.api.traders.TradeResult;
import io.github.lightman314.lightmanscurrency.api.traders.trade.TradeDirection;
import io.github.lightman314.lightmanscurrency.common.core.ModBlocks;
import io.github.lightman314.lightmanscurrency.common.core.ModEnchantments;
import io.github.lightman314.lightmanscurrency.common.core.ModItems;
import io.github.lightman314.lightmanscurrency.common.core.variants.Color;
import io.github.lightman314.lightmanscurrency.common.core.variants.WoodType;
import io.github.lightman314.lightmanscurrency.common.notifications.types.auction.AuctionHouseBidNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.types.auction.AuctionHouseBuyerNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.types.auction.AuctionHouseCancelNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.types.auction.AuctionHouseSellerNobidNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.types.auction.AuctionHouseSellerNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.types.bank.BankInterestNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.types.bank.BankTransferNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.types.bank.LowBalanceNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.types.ejection.OwnableBlockEjectedNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.types.settings.AddRemoveAllyNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.types.settings.AddRemoveTradeNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.types.settings.ChangeAllyPermissionNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.types.settings.ChangeCreativeNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.types.settings.ChangeNameNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.types.settings.ChangeOwnerNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.types.settings.ChangeSettingNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.types.taxes.TaxesCollectedNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.types.taxes.TaxesPaidNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.types.trader.ItemTradeNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.types.trader.OutOfStockNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.types.trader.PaygateNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.types.trader.SlotMachineTradeNotification;
import io.github.lightman314.lightmanscurrency.common.text.AdvancementTextEntry;
import io.github.lightman314.lightmanscurrency.common.text.CombinedTextEntry;
import io.github.lightman314.lightmanscurrency.common.text.MultiLineTextEntry;
import io.github.lightman314.lightmanscurrency.common.text.TextEntry;
import io.github.lightman314.lightmanscurrency.common.text.TextEntryBundle;
import io.github.lightman314.lightmanscurrency.common.text.TimeUnitTextEntry;
import io.github.lightman314.lightmanscurrency.common.traders.rules.types.FreeSample;
import io.github.lightman314.lightmanscurrency.common.traders.rules.types.PlayerDiscounts;
import io.github.lightman314.lightmanscurrency.common.traders.rules.types.PlayerListing;
import io.github.lightman314.lightmanscurrency.common.traders.rules.types.PlayerTradeLimit;
import io.github.lightman314.lightmanscurrency.common.traders.rules.types.PriceFluctuation;
import io.github.lightman314.lightmanscurrency.common.traders.rules.types.TimedSale;
import io.github.lightman314.lightmanscurrency.common.traders.rules.types.TradeLimit;
import io.github.lightman314.lightmanscurrency.common.villager_merchant.CustomProfessions;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class LCText {

    private static final String MODID = "lightmanscurrency";

    public static final TextEntry CREATIVE_GROUP_COINS = TextEntry.creativeTab("lightmanscurrency", "coins");

    public static final TextEntry CREATIVE_GROUP_MACHINES = TextEntry.creativeTab("lightmanscurrency", "machines");

    public static final TextEntry CREATIVE_GROUP_TRADING = TextEntry.creativeTab("lightmanscurrency", "trading");

    public static final TextEntry CREATIVE_GROUP_UPGRADES = TextEntry.creativeTab("lightmanscurrency", "upgrades");

    public static final TextEntry CREATIVE_GROUP_EXTRA = TextEntry.creativeTab("lightmanscurrency", "extra");

    public static final TextEntry ITEM_COIN_COPPER = TextEntry.item(ModItems.COIN_COPPER);

    public static final TextEntry ITEM_COIN_COPPER_PLURAL = TextEntry.plural(ITEM_COIN_COPPER);

    public static final TextEntry ITEM_COIN_COPPER_INITIAL = TextEntry.initial(ITEM_COIN_COPPER);

    public static final TextEntry ITEM_COIN_IRON = TextEntry.item(ModItems.COIN_IRON);

    public static final TextEntry ITEM_COIN_IRON_PLURAL = TextEntry.plural(ITEM_COIN_IRON);

    public static final TextEntry ITEM_COIN_IRON_INITIAL = TextEntry.initial(ITEM_COIN_IRON);

    public static final TextEntry ITEM_COIN_GOLD = TextEntry.item(ModItems.COIN_GOLD);

    public static final TextEntry ITEM_COIN_GOLD_PLURAL = TextEntry.plural(ITEM_COIN_GOLD);

    public static final TextEntry ITEM_COIN_GOLD_INITIAL = TextEntry.initial(ITEM_COIN_GOLD);

    public static final TextEntry ITEM_COIN_EMERALD = TextEntry.item(ModItems.COIN_EMERALD);

    public static final TextEntry ITEM_COIN_EMERALD_PLURAL = TextEntry.plural(ITEM_COIN_EMERALD);

    public static final TextEntry ITEM_COIN_EMERALD_INITIAL = TextEntry.initial(ITEM_COIN_EMERALD);

    public static final TextEntry ITEM_COIN_DIAMOND = TextEntry.item(ModItems.COIN_DIAMOND);

    public static final TextEntry ITEM_COIN_DIAMOND_PLURAL = TextEntry.plural(ITEM_COIN_DIAMOND);

    public static final TextEntry ITEM_COIN_DIAMOND_INITIAL = TextEntry.initial(ITEM_COIN_DIAMOND);

    public static final TextEntry ITEM_COIN_NETHERITE = TextEntry.item(ModItems.COIN_NETHERITE);

    public static final TextEntry ITEM_COIN_NETHERITE_PLURAL = TextEntry.plural(ITEM_COIN_NETHERITE);

    public static final TextEntry ITEM_COIN_NETHERITE_INITIAL = TextEntry.initial(ITEM_COIN_NETHERITE);

    public static final TextEntry ITEM_COIN_CHOCOLATE_COPPER = TextEntry.item(ModItems.COIN_CHOCOLATE_COPPER);

    public static final TextEntry ITEM_COIN_CHOCOLATE_COPPER_PLURAL = TextEntry.plural(ITEM_COIN_CHOCOLATE_COPPER);

    public static final TextEntry ITEM_COIN_CHOCOLATE_COPPER_INITIAL = TextEntry.initial(ITEM_COIN_CHOCOLATE_COPPER);

    public static final TextEntry ITEM_COIN_CHOCOLATE_IRON = TextEntry.item(ModItems.COIN_CHOCOLATE_IRON);

    public static final TextEntry ITEM_COIN_CHOCOLATE_IRON_PLURAL = TextEntry.plural(ITEM_COIN_CHOCOLATE_IRON);

    public static final TextEntry ITEM_COIN_CHOCOLATE_IRON_INITIAL = TextEntry.initial(ITEM_COIN_CHOCOLATE_IRON);

    public static final TextEntry ITEM_COIN_CHOCOLATE_GOLD = TextEntry.item(ModItems.COIN_CHOCOLATE_GOLD);

    public static final TextEntry ITEM_COIN_CHOCOLATE_GOLD_PLURAL = TextEntry.plural(ITEM_COIN_CHOCOLATE_GOLD);

    public static final TextEntry ITEM_COIN_CHOCOLATE_GOLD_INITIAL = TextEntry.initial(ITEM_COIN_CHOCOLATE_GOLD);

    public static final TextEntry ITEM_COIN_CHOCOLATE_EMERALD = TextEntry.item(ModItems.COIN_CHOCOLATE_EMERALD);

    public static final TextEntry ITEM_COIN_CHOCOLATE_EMERALD_PLURAL = TextEntry.plural(ITEM_COIN_CHOCOLATE_EMERALD);

    public static final TextEntry ITEM_COIN_CHOCOLATE_EMERALD_INITIAL = TextEntry.initial(ITEM_COIN_CHOCOLATE_EMERALD);

    public static final TextEntry ITEM_COIN_CHOCOLATE_DIAMOND = TextEntry.item(ModItems.COIN_CHOCOLATE_DIAMOND);

    public static final TextEntry ITEM_COIN_CHOCOLATE_DIAMOND_PLURAL = TextEntry.plural(ITEM_COIN_CHOCOLATE_DIAMOND);

    public static final TextEntry ITEM_COIN_CHOCOLATE_DIAMOND_INITIAL = TextEntry.initial(ITEM_COIN_CHOCOLATE_DIAMOND);

    public static final TextEntry ITEM_COIN_CHOCOLATE_NETHERITE = TextEntry.item(ModItems.COIN_CHOCOLATE_NETHERITE);

    public static final TextEntry ITEM_COIN_CHOCOLATE_NETHERITE_PLURAL = TextEntry.plural(ITEM_COIN_CHOCOLATE_NETHERITE);

    public static final TextEntry ITEM_COIN_CHOCOLATE_NETHERITE_INITIAL = TextEntry.initial(ITEM_COIN_CHOCOLATE_NETHERITE);

    public static final TextEntry ITEM_WALLET_COPPER = TextEntry.item(ModItems.WALLET_COPPER);

    public static final TextEntry ITEM_WALLET_IRON = TextEntry.item(ModItems.WALLET_IRON);

    public static final TextEntry ITEM_WALLET_GOLD = TextEntry.item(ModItems.WALLET_GOLD);

    public static final TextEntry ITEM_WALLET_EMERALD = TextEntry.item(ModItems.WALLET_EMERALD);

    public static final TextEntry ITEM_WALLET_DIAMOND = TextEntry.item(ModItems.WALLET_DIAMOND);

    public static final TextEntry ITEM_WALLET_NETHERITE = TextEntry.item(ModItems.WALLET_NETHERITE);

    public static final TextEntry ITEM_TRADING_CORE = TextEntry.item(ModItems.TRADING_CORE);

    public static final TextEntry ITEM_TICKET = TextEntry.item(ModItems.TICKET);

    public static final TextEntry ITEM_PASS = TextEntry.item(ModItems.TICKET_PASS);

    public static final TextEntry ITEM_MASTER_TICKET = TextEntry.item(ModItems.TICKET_MASTER);

    public static final TextEntry ITEM_TICKET_STUB = TextEntry.item(ModItems.TICKET_STUB);

    public static final TextEntry ITEM_GOLDEN_TICKET = TextEntry.item(ModItems.GOLDEN_TICKET);

    public static final TextEntry ITEM_GOLDEN_PASS = TextEntry.item(ModItems.GOLDEN_TICKET_PASS);

    public static final TextEntry ITEM_GOLDEN_MASTER_TICKET = TextEntry.item(ModItems.GOLDEN_TICKET_MASTER);

    public static final TextEntry ITEM_GOLDEN_TICKET_STUB = TextEntry.item(ModItems.GOLDEN_TICKET_STUB);

    public static final TextEntry ITEM_UPGRADE_ITEM_CAPACITY_1 = TextEntry.item(ModItems.ITEM_CAPACITY_UPGRADE_1);

    public static final TextEntry ITEM_UPGRADE_ITEM_CAPACITY_2 = TextEntry.item(ModItems.ITEM_CAPACITY_UPGRADE_2);

    public static final TextEntry ITEM_UPGRADE_ITEM_CAPACITY_3 = TextEntry.item(ModItems.ITEM_CAPACITY_UPGRADE_3);

    public static final TextEntry ITEM_UPGRADE_ITEM_CAPACITY_4 = TextEntry.item(ModItems.ITEM_CAPACITY_UPGRADE_4);

    public static final TextEntry ITEM_UPGRADE_SPEED_1 = TextEntry.item(ModItems.SPEED_UPGRADE_1);

    public static final TextEntry ITEM_UPGRADE_SPEED_2 = TextEntry.item(ModItems.SPEED_UPGRADE_2);

    public static final TextEntry ITEM_UPGRADE_SPEED_3 = TextEntry.item(ModItems.SPEED_UPGRADE_3);

    public static final TextEntry ITEM_UPGRADE_SPEED_4 = TextEntry.item(ModItems.SPEED_UPGRADE_4);

    public static final TextEntry ITEM_UPGRADE_SPEED_5 = TextEntry.item(ModItems.SPEED_UPGRADE_5);

    public static final TextEntry ITEM_UPGRADE_NETWORK = TextEntry.item(ModItems.NETWORK_UPGRADE);

    public static final TextEntry ITEM_UPGRADE_HOPPER = TextEntry.item(ModItems.HOPPER_UPGRADE);

    public static final TextEntry ITEM_UPGRADE_COIN_EXCHANGE = TextEntry.item(ModItems.COIN_CHEST_EXCHANGE_UPGRADE);

    public static final TextEntry ITEM_UPGRADE_MAGNET_1 = TextEntry.item(ModItems.COIN_CHEST_MAGNET_UPGRADE_1);

    public static final TextEntry ITEM_UPGRADE_MAGNET_2 = TextEntry.item(ModItems.COIN_CHEST_MAGNET_UPGRADE_2);

    public static final TextEntry ITEM_UPGRADE_MAGNET_3 = TextEntry.item(ModItems.COIN_CHEST_MAGNET_UPGRADE_3);

    public static final TextEntry ITEM_UPGRADE_MAGNET_4 = TextEntry.item(ModItems.COIN_CHEST_MAGNET_UPGRADE_4);

    public static final TextEntry ITEM_UPGRADE_SECURITY = TextEntry.item(ModItems.COIN_CHEST_SECURITY_UPGRADE);

    public static final TextEntry ITEM_UPGRADE_TEMPLATE = TextEntry.item(ModItems.UPGRADE_SMITHING_TEMPLATE);

    public static final TextEntry BLOCK_COINPILE_COPPER = TextEntry.block(ModBlocks.COINPILE_COPPER);

    public static final TextEntry BLOCK_COINPILE_COPPER_PLURAL = TextEntry.plural(BLOCK_COINPILE_COPPER);

    public static final TextEntry BLOCK_COINPILE_IRON = TextEntry.block(ModBlocks.COINPILE_IRON);

    public static final TextEntry BLOCK_COINPILE_IRON_PLURAL = TextEntry.plural(BLOCK_COINPILE_IRON);

    public static final TextEntry BLOCK_COINPILE_GOLD = TextEntry.block(ModBlocks.COINPILE_GOLD);

    public static final TextEntry BLOCK_COINPILE_GOLD_PLURAL = TextEntry.plural(BLOCK_COINPILE_GOLD);

    public static final TextEntry BLOCK_COINPILE_EMERALD = TextEntry.block(ModBlocks.COINPILE_EMERALD);

    public static final TextEntry BLOCK_COINPILE_EMERALD_PLURAL = TextEntry.plural(BLOCK_COINPILE_EMERALD);

    public static final TextEntry BLOCK_COINPILE_DIAMOND = TextEntry.block(ModBlocks.COINPILE_DIAMOND);

    public static final TextEntry BLOCK_COINPILE_DIAMOND_PLURAL = TextEntry.plural(BLOCK_COINPILE_DIAMOND);

    public static final TextEntry BLOCK_COINPILE_NETHERITE = TextEntry.block(ModBlocks.COINPILE_NETHERITE);

    public static final TextEntry BLOCK_COINPILE_NETHERITE_PLURAL = TextEntry.plural(BLOCK_COINPILE_NETHERITE);

    public static final TextEntry BLOCK_COINBLOCK_COPPER = TextEntry.block(ModBlocks.COINBLOCK_COPPER);

    public static final TextEntry BLOCK_COINBLOCK_COPPER_PLURAL = TextEntry.plural(BLOCK_COINBLOCK_COPPER);

    public static final TextEntry BLOCK_COINBLOCK_IRON = TextEntry.block(ModBlocks.COINBLOCK_IRON);

    public static final TextEntry BLOCK_COINBLOCK_IRON_PLURAL = TextEntry.plural(BLOCK_COINBLOCK_IRON);

    public static final TextEntry BLOCK_COINBLOCK_GOLD = TextEntry.block(ModBlocks.COINBLOCK_GOLD);

    public static final TextEntry BLOCK_COINBLOCK_GOLD_PLURAL = TextEntry.plural(BLOCK_COINBLOCK_GOLD);

    public static final TextEntry BLOCK_COINBLOCK_EMERALD = TextEntry.block(ModBlocks.COINBLOCK_EMERALD);

    public static final TextEntry BLOCK_COINBLOCK_EMERALD_PLURAL = TextEntry.plural(BLOCK_COINBLOCK_EMERALD);

    public static final TextEntry BLOCK_COINBLOCK_DIAMOND = TextEntry.block(ModBlocks.COINBLOCK_DIAMOND);

    public static final TextEntry BLOCK_COINBLOCK_DIAMOND_PLURAL = TextEntry.plural(BLOCK_COINBLOCK_DIAMOND);

    public static final TextEntry BLOCK_COINBLOCK_NETHERITE = TextEntry.block(ModBlocks.COINBLOCK_NETHERITE);

    public static final TextEntry BLOCK_COINBLOCK_NETHERITE_PLURAL = TextEntry.plural(BLOCK_COINBLOCK_NETHERITE);

    public static final TextEntry BLOCK_COINPILE_CHOCOLATE_COPPER = TextEntry.block(ModBlocks.COINPILE_CHOCOLATE_COPPER);

    public static final TextEntry BLOCK_COINPILE_CHOCOLATE_COPPER_PLURAL = TextEntry.plural(BLOCK_COINPILE_CHOCOLATE_COPPER);

    public static final TextEntry BLOCK_COINPILE_CHOCOLATE_IRON = TextEntry.block(ModBlocks.COINPILE_CHOCOLATE_IRON);

    public static final TextEntry BLOCK_COINPILE_CHOCOLATE_IRON_PLURAL = TextEntry.plural(BLOCK_COINPILE_CHOCOLATE_IRON);

    public static final TextEntry BLOCK_COINPILE_CHOCOLATE_GOLD = TextEntry.block(ModBlocks.COINPILE_CHOCOLATE_GOLD);

    public static final TextEntry BLOCK_COINPILE_CHOCOLATE_GOLD_PLURAL = TextEntry.plural(BLOCK_COINPILE_CHOCOLATE_GOLD);

    public static final TextEntry BLOCK_COINPILE_CHOCOLATE_EMERALD = TextEntry.block(ModBlocks.COINPILE_CHOCOLATE_EMERALD);

    public static final TextEntry BLOCK_COINPILE_CHOCOLATE_EMERALD_PLURAL = TextEntry.plural(BLOCK_COINPILE_CHOCOLATE_EMERALD);

    public static final TextEntry BLOCK_COINPILE_CHOCOLATE_DIAMOND = TextEntry.block(ModBlocks.COINPILE_CHOCOLATE_DIAMOND);

    public static final TextEntry BLOCK_COINPILE_CHOCOLATE_DIAMOND_PLURAL = TextEntry.plural(BLOCK_COINPILE_CHOCOLATE_DIAMOND);

    public static final TextEntry BLOCK_COINPILE_CHOCOLATE_NETHERITE = TextEntry.block(ModBlocks.COINPILE_CHOCOLATE_NETHERITE);

    public static final TextEntry BLOCK_COINPILE_CHOCOLATE_NETHERITE_PLURAL = TextEntry.plural(BLOCK_COINPILE_CHOCOLATE_NETHERITE);

    public static final TextEntry BLOCK_COINBLOCK_CHOCOLATE_COPPER = TextEntry.block(ModBlocks.COINBLOCK_CHOCOLATE_COPPER);

    public static final TextEntry BLOCK_COINBLOCK_CHOCOLATE_COPPER_PLURAL = TextEntry.plural(BLOCK_COINBLOCK_CHOCOLATE_COPPER);

    public static final TextEntry BLOCK_COINBLOCK_CHOCOLATE_IRON = TextEntry.block(ModBlocks.COINBLOCK_CHOCOLATE_IRON);

    public static final TextEntry BLOCK_COINBLOCK_CHOCOLATE_IRON_PLURAL = TextEntry.plural(BLOCK_COINBLOCK_CHOCOLATE_IRON);

    public static final TextEntry BLOCK_COINBLOCK_CHOCOLATE_GOLD = TextEntry.block(ModBlocks.COINBLOCK_CHOCOLATE_GOLD);

    public static final TextEntry BLOCK_COINBLOCK_CHOCOLATE_GOLD_PLURAL = TextEntry.plural(BLOCK_COINBLOCK_CHOCOLATE_GOLD);

    public static final TextEntry BLOCK_COINBLOCK_CHOCOLATE_EMERALD = TextEntry.block(ModBlocks.COINBLOCK_CHOCOLATE_EMERALD);

    public static final TextEntry BLOCK_COINBLOCK_CHOCOLATE_EMERALD_PLURAL = TextEntry.plural(BLOCK_COINBLOCK_CHOCOLATE_EMERALD);

    public static final TextEntry BLOCK_COINBLOCK_CHOCOLATE_DIAMOND = TextEntry.block(ModBlocks.COINBLOCK_CHOCOLATE_DIAMOND);

    public static final TextEntry BLOCK_COINBLOCK_CHOCOLATE_DIAMOND_PLURAL = TextEntry.plural(BLOCK_COINBLOCK_CHOCOLATE_DIAMOND);

    public static final TextEntry BLOCK_COINBLOCK_CHOCOLATE_NETHERITE = TextEntry.block(ModBlocks.COINBLOCK_CHOCOLATE_NETHERITE);

    public static final TextEntry BLOCK_COINBLOCK_CHOCOLATE_NETHERITE_PLURAL = TextEntry.plural(BLOCK_COINBLOCK_CHOCOLATE_NETHERITE);

    public static final TextEntry BLOCK_CASH_REGISTER = TextEntry.block(ModBlocks.CASH_REGISTER);

    public static final TextEntry BLOCK_COIN_MINT = TextEntry.block(ModBlocks.COIN_MINT);

    public static final TextEntry BLOCK_TICKET_STATION = TextEntry.block(ModBlocks.TICKET_STATION);

    public static final TextEntry BLOCK_MONEY_CHEST = TextEntry.block(ModBlocks.COIN_CHEST);

    public static final TextEntry BLOCK_DISPLAY_CASE = TextEntry.block(ModBlocks.DISPLAY_CASE);

    public static final TextEntryBundle<WoodType> BLOCK_SHELF = TextEntryBundle.of(ModBlocks.SHELF);

    public static final TextEntryBundle<WoodType> BLOCK_SHELF_2x2 = TextEntryBundle.of(ModBlocks.SHELF_2x2);

    public static final TextEntryBundle<WoodType> BLOCK_CARD_DISPLAY = TextEntryBundle.of(ModBlocks.CARD_DISPLAY);

    public static final TextEntryBundle<Color> BLOCK_VENDING_MACHINE = TextEntryBundle.of(ModBlocks.VENDING_MACHINE);

    public static final TextEntryBundle<Color> BLOCK_FREEZER = TextEntryBundle.of(ModBlocks.FREEZER);

    public static final TextEntryBundle<Color> BLOCK_LARGE_VENDING_MACHINE = TextEntryBundle.of(ModBlocks.VENDING_MACHINE_LARGE);

    public static final TextEntry BLOCK_PAYGATE = TextEntry.block(ModBlocks.PAYGATE);

    public static final TextEntry BLOCK_TICKET_KIOSK = TextEntry.block(ModBlocks.TICKET_KIOSK);

    public static final TextEntry BLOCK_SLOT_MACHINE = TextEntry.block(ModBlocks.SLOT_MACHINE);

    public static final TextEntry BLOCK_ARMOR_DISPLAY = TextEntry.block(ModBlocks.ARMOR_DISPLAY);

    public static final TextEntryBundle<WoodType> BLOCK_BOOKSHELF_TRADER = TextEntryBundle.of(ModBlocks.BOOKSHELF_TRADER);

    public static final TextEntry BLOCK_ITEM_NETWORK_TRADER_1 = TextEntry.block(ModBlocks.ITEM_NETWORK_TRADER_1);

    public static final TextEntry BLOCK_ITEM_NETWORK_TRADER_2 = TextEntry.block(ModBlocks.ITEM_NETWORK_TRADER_2);

    public static final TextEntry BLOCK_ITEM_NETWORK_TRADER_3 = TextEntry.block(ModBlocks.ITEM_NETWORK_TRADER_3);

    public static final TextEntry BLOCK_ITEM_NETWORK_TRADER_4 = TextEntry.block(ModBlocks.ITEM_NETWORK_TRADER_4);

    public static final TextEntry BLOCK_ITEM_TRADER_INTERFACE = TextEntry.block(ModBlocks.ITEM_TRADER_INTERFACE);

    public static final TextEntry BLOCK_TAX_COLLECTOR = TextEntry.block(ModBlocks.TAX_COLLECTOR);

    public static final TextEntryBundle<WoodType> BLOCK_AUCTION_STAND = TextEntryBundle.of(ModBlocks.AUCTION_STAND);

    public static final TextEntry BLOCK_JAR_PIGGY_BANK = TextEntry.block(ModBlocks.PIGGY_BANK);

    public static final TextEntry BLOCK_JAR_BLUE = TextEntry.block(ModBlocks.COINJAR_BLUE);

    public static final TextEntry BLOCK_JAR_SUS = TextEntry.block(ModBlocks.SUS_JAR);

    public static final CombinedTextEntry ITEM_BLOCK_TERMINAL = CombinedTextEntry.items(ModItems.PORTABLE_TERMINAL, ModItems.PORTABLE_GEM_TERMINAL, ModBlocks.TERMINAL, ModBlocks.GEM_TERMINAL);

    public static final CombinedTextEntry ITEM_BLOCK_ATM = CombinedTextEntry.items(ModItems.PORTABLE_ATM, ModBlocks.ATM);

    public static final TextEntry ENCHANTMENT_MONEY_MENDING = TextEntry.enchantment(ModEnchantments.MONEY_MENDING);

    public static final TextEntry ENCHANTMENT_MONEY_MENDING_DESCRIPTION = TextEntry.description(ENCHANTMENT_MONEY_MENDING);

    public static final TextEntry ENCHANTMENT_COIN_MAGNET = TextEntry.enchantment(ModEnchantments.COIN_MAGNET);

    public static final TextEntry ENCHANTMENT_COIN_MAGNET_DESCRIPTION = TextEntry.description(ENCHANTMENT_COIN_MAGNET);

    public static final TextEntry GAMERULE_KEEP_WALLET = TextEntry.gamerule("keepWallet");

    public static final TextEntry GAMERULE_COIN_DROP_PERCENT = TextEntry.gamerule("coinDropPercent");

    public static final TextEntry PROFESSION_BANKER = TextEntry.profession(CustomProfessions.BANKER);

    public static final TextEntry PROFESSION_CASHIER = TextEntry.profession(CustomProfessions.CASHIER);

    public static final TextEntry KEY_WALLET = TextEntry.keyBind("lightmanscurrency", "open_wallet");

    public static final TextEntry KEY_PORTABLE_TERMINAL = TextEntry.keyBind("lightmanscurrency", "portable_terminal");

    public static final TextEntry KEY_PORTABLE_ATM = TextEntry.keyBind("lightmanscurrency", "portable_atm");

    public static final TextEntry SOUND_COINS_CLINKING = TextEntry.sound("lightmanscurrency", "coins_clinking");

    public static final MultiLineTextEntry TOOLTIP_UPGRADE_TEMPLATE = MultiLineTextEntry.tooltip("lightmanscurrency", "upgrade_smithing_template");

    public static final TextEntry TOOLTIP_HEALING = TextEntry.tooltip("lightmanscurrency", "healing");

    public static final TextEntry TOOLTIP_BETA = TextEntry.tooltip("lightmanscurrency", "beta");

    public static final TextEntry TOOLTIP_DISABLED = TextEntry.tooltip("lightmanscurrency", "disabled");

    public static final TextEntry TOOLTIP_INFO_BLURB = TextEntry.tooltip("lightmanscurrency", "info_blurb");

    public static final TextEntry TOOLTIP_TICKET_ID = TextEntry.tooltip("lightmanscurrency", "ticket.id");

    public static final TextEntry TOOLTIP_PASS = TextEntry.tooltip("lightmanscurrency", "pass");

    public static final MultiLineTextEntry TOOLTIP_ITEM_TRADER = MultiLineTextEntry.tooltip("lightmanscurrency", "trader.item");

    public static final MultiLineTextEntry TOOLTIP_ITEM_TRADER_ARMOR = MultiLineTextEntry.tooltip("lightmanscurrency", "trader.item.armor");

    public static final MultiLineTextEntry TOOLTIP_ITEM_TRADER_TICKET = MultiLineTextEntry.tooltip("lightmanscurrency", "trader.item.ticket");

    public static final MultiLineTextEntry TOOLTIP_ITEM_TRADER_BOOK = MultiLineTextEntry.tooltip("lightmanscurrency", "trader.item.book");

    public static final MultiLineTextEntry TOOLTIP_ITEM_TRADER_NETWORK = MultiLineTextEntry.tooltip("lightmanscurrency", "trader.network.item");

    public static final MultiLineTextEntry TOOLTIP_SLOT_MACHINE = MultiLineTextEntry.tooltip("lightmanscurrency", "trader.slot_machine");

    public static final MultiLineTextEntry TOOLTIP_PAYGATE = MultiLineTextEntry.tooltip("lightmanscurrency", "paygate");

    public static final MultiLineTextEntry TOOLTIP_TERMINAL = MultiLineTextEntry.tooltip("lightmanscurrency", "terminal");

    public static final MultiLineTextEntry TOOLTIP_INTERFACE_ITEM = MultiLineTextEntry.tooltip("lightmanscurrency", "interface.item");

    public static final MultiLineTextEntry TOOLTIP_TAX_COLLECTOR = MultiLineTextEntry.tooltip("lightmanscurrency", "tax_collector");

    public static final TextEntry TOOLTIP_TAX_COLLECTOR_ADMIN_ONLY = TextEntry.tooltip("lightmanscurrency", "tax_collector.admin_only");

    public static final MultiLineTextEntry TOOLTIP_ATM = MultiLineTextEntry.tooltip("lightmanscurrency", "atm");

    public static final TextEntry TOOLTIP_COIN_MINT_MINTABLE = TextEntry.tooltip("lightmanscurrency", "coinmint.mintable");

    public static final TextEntry TOOLTIP_COIN_MINT_MELTABLE = TextEntry.tooltip("lightmanscurrency", "coinmint.meltable");

    public static final TextEntry TOOLTIP_COIN_MINT_DISABLED_TOP = TextEntry.tooltip("lightmanscurrency", "coinmint.disabled.1");

    public static final TextEntry TOOLTIP_COIN_MINT_DISABLED_BOTTOM = TextEntry.tooltip("lightmanscurrency", "coinmint.disabled.2");

    public static final MultiLineTextEntry TOOLTIP_TICKET_STATION = MultiLineTextEntry.tooltip("lightmanscurrency", "ticketmachine");

    public static final MultiLineTextEntry TOOLTIP_CASH_REGISTER = MultiLineTextEntry.tooltip("lightmanscurrency", "cash_register");

    public static final MultiLineTextEntry TOOLTIP_COIN_JAR = MultiLineTextEntry.tooltip("lightmanscurrency", "coin_jar");

    public static final TextEntry TOOLTIP_COIN_JAR_COLORED = TextEntry.tooltip("lightmanscurrency", "coin_jar.colored");

    public static final TextEntry TOOLTIP_COLORED_ITEM = TextEntry.tooltip("lightmanscurrency", "colored_item");

    public static final TextEntry TOOLTIP_ANARCHY_WARNING = TextEntry.tooltip("lightmanscurrency", "ownable.anarchy_warning");

    public static final TextEntry TOOLTIP_COIN_WORTH_DOWN = TextEntry.tooltip("lightmanscurrency", "coinworth.down");

    public static final TextEntry TOOLTIP_COIN_WORTH_UP = TextEntry.tooltip("lightmanscurrency", "coinworth.up");

    public static final TextEntry TOOLTIP_COIN_WORTH_VALUE = TextEntry.tooltip("lightmanscurrency", "coinworth.value");

    public static final TextEntry TOOLTIP_COIN_WORTH_VALUE_STACK = TextEntry.tooltip("lightmanscurrency", "coinworth.value.stack");

    public static final TextEntry TOOLTIP_COIN_ADVANCED_CHAIN = TextEntry.tooltip("lightmanscurrency", "coin.advanced.chain");

    public static final TextEntry TOOLTIP_COIN_ADVANCED_VALUE = TextEntry.tooltip("lightmanscurrency", "coin.advanced.value");

    public static final TextEntry TOOLTIP_COIN_ADVANCED_CORE_CHAIN = TextEntry.tooltip("lightmanscurrency", "coin.advanced.core_chain");

    public static final TextEntry TOOLTIP_COIN_ADVANCED_SIDE_CHAIN = TextEntry.tooltip("lightmanscurrency", "coin.advanced.side_chain");

    public static final TextEntry MESSAGE_WALLET_NONE_EQUIPPED = TextEntry.message("lightmanscurrency", "wallet.none_equipped");

    public static final TextEntry TOOLTIP_WALLET_STORED_MONEY = TextEntry.tooltip("lightmanscurrency", "wallet.storedmoney");

    public static final TextEntry TOOLTIP_WALLET_PICKUP = TextEntry.tooltip("lightmanscurrency", "wallet.pickup");

    public static final TextEntry TOOLTIP_WALLET_PICKUP_MAGNET = TextEntry.tooltip("lightmanscurrency", "wallet.pickup.magnet");

    public static final TextEntry TOOLTIP_WALLET_EXCHANGE_MANUAL = TextEntry.tooltip("lightmanscurrency", "wallet.exchange.manual");

    public static final TextEntry TOOLTIP_WALLET_EXCHANGE_AUTO = TextEntry.tooltip("lightmanscurrency", "wallet.exchange.auto");

    public static final TextEntry TOOLTIP_WALLET_EXCHANGE_AUTO_ON = TextEntry.tooltip("lightmanscurrency", "wallet.exchange.auto.on");

    public static final TextEntry TOOLTIP_WALLET_EXCHANGE_AUTO_OFF = TextEntry.tooltip("lightmanscurrency", "wallet.exchange.auto.off");

    public static final TextEntry TOOLTIP_WALLET_BANK_ACCOUNT = TextEntry.tooltip("lightmanscurrency", "wallet.bank_account");

    public static final TextEntry MESSAGE_CASH_REGISTER_NOT_LINKED = TextEntry.message("lightmanscurrency", "cash_register.not_linked");

    public static final TextEntry TOOLTIP_CASH_REGISTER_INFO = TextEntry.tooltip("lightmanscurrency", "cash_register.info");

    public static final TextEntry TOOLTIP_CASH_REGISTER_INSTRUCTIONS = TextEntry.tooltip("lightmanscurrency", "cash_register.instructions");

    public static final TextEntry TOOLTIP_CASH_REGISTER_HOLD_SHIFT = TextEntry.tooltip("lightmanscurrency", "cash_register.holdshift");

    public static final TextEntry TOOLTIP_CASH_REGISTER_DETAILS = TextEntry.tooltip("lightmanscurrency", "cash_register.details");

    public static final TextEntry TOOLTIP_COIN_JAR_HOLD_SHIFT = TextEntry.tooltip("lightmanscurrency", "coinjar.holdshift");

    public static final TextEntry TOOLTIP_COIN_JAR_CONTENTS_SINGLE = TextEntry.tooltip("lightmanscurrency", "coinjar.storedcoins.single");

    public static final TextEntry TOOLTIP_COIN_JAR_CONTENTS_MULTIPLE = TextEntry.tooltip("lightmanscurrency", "coinjar.storedcoins.multiple");

    public static final TextEntry TOOLTIP_UPGRADE_TARGETS = TextEntry.tooltip("lightmanscurrency", "upgrade.targets");

    public static final TextEntry TOOLTIP_UPGRADE_ITEM_CAPACITY = TextEntry.tooltip("lightmanscurrency", "upgrade.item_capacity");

    public static final TextEntry TOOLTIP_UPGRADE_SPEED = TextEntry.tooltip("lightmanscurrency", "upgrade.speed");

    public static final TextEntry TOOLTIP_UPGRADE_NETWORK = TextEntry.tooltip("lightmanscurrency", "upgrade.network");

    public static final TextEntry TOOLTIP_UPGRADE_HOPPER = TextEntry.tooltip("lightmanscurrency", "upgrade.hopper");

    public static final TextEntry TOOLTIP_UPGRADE_COIN_EXCHANGE = TextEntry.tooltip("lightmanscurrency", "upgrade.coin_exchange");

    public static final TextEntry TOOLTIP_UPGRADE_MAGNET = TextEntry.tooltip("lightmanscurrency", "upgrade.magnet");

    public static final MultiLineTextEntry TOOLTIP_UPGRADE_SECURITY = MultiLineTextEntry.tooltip("lightmanscurrency", "upgrade.security");

    public static final TextEntry TOOLTIP_UPGRADE_TARGET_TRADER = TextEntry.tooltip("lightmanscurrency", "upgrade.target.traders");

    public static final TextEntry TOOLTIP_UPGRADE_TARGET_TRADER_NOT_NETWORK = TextEntry.tooltip("lightmanscurrency", "upgrade.target.traders.not_network");

    public static final TextEntry TOOLTIP_UPGRADE_TARGET_TRADER_ITEM = TextEntry.tooltip("lightmanscurrency", "upgrade.target.traders.item");

    public static final TextEntry TOOLTIP_UPGRADE_TARGET_TRADER_INTERFACE = TextEntry.tooltip("lightmanscurrency", "upgrade.target.trader_interface");

    public static final TextEntry TOOLTIP_MONEY_SOURCE_BANK = TextEntry.tooltip("lightmanscurrency", "money_source.bank");

    public static final TextEntry TOOLTIP_MONEY_SOURCE_SLOTS = TextEntry.tooltip("lightmanscurrency", "money_source.slots");

    public static final TextEntry TOOLTIP_MONEY_SOURCE_PLAYER = TextEntry.tooltip("lightmanscurrency", "money_source.player");

    public static final TextEntry TOOLTIP_MONEY_SOURCE_STORAGE = TextEntry.tooltip("lightmanscurrency", "money_source.storage");

    public static final TextEntry TOOLTIP_OWNER_PLAYER = TextEntry.tooltip("lightmanscurrency", "ownership.player");

    public static final MultiLineTextEntry TOOLTIP_OWNER_TEAM = MultiLineTextEntry.tooltip("lightmanscurrency", "ownership.team");

    public static final TextEntry TOOLTIP_MONEY_MENDING_COST = TextEntry.tooltip("lightmanscurrency", "money_mending.price");

    public static final TextEntry TOOLTIP_OUT_OF_STOCK = TextEntry.tooltip("lightmanscurrency", "out_of_stock");

    public static final TextEntry TOOLTIP_OUT_OF_SPACE = TextEntry.tooltip("lightmanscurrency", "out_of_space");

    public static final TextEntry TOOLTIP_CANNOT_AFFORD = TextEntry.tooltip("lightmanscurrency", "cannot_afford");

    public static final TextEntry TOOLTIP_TAX_LIMIT = TextEntry.tooltip("lightmanscurrency", "tax_limit");

    public static final TextEntry TOOLTIP_DENIED = TextEntry.tooltip("lightmanscurrency", "denied");

    public static final TextEntry TOOLTIP_SLOT_MACHINE_TO_INTERACT = TextEntry.tooltip("lightmanscurrency", "slot_machine.to_interact");

    public static final TextEntry TOOLTIP_SLOT_MACHINE_TO_INFO = TextEntry.tooltip("lightmanscurrency", "slot_machine.to_info");

    public static final MultiLineTextEntry TOOLTIP_SLOT_MACHINE_ROLL_ONCE = MultiLineTextEntry.tooltip("lightmanscurrency", "slot_machine.roll.once");

    public static final MultiLineTextEntry TOOLTIP_SLOT_MACHINE_ROLL_MULTI = MultiLineTextEntry.tooltip("lightmanscurrency", "slot_machine.roll.multi");

    public static final TextEntry TOOLTIP_SLOT_MACHINE_NORMAL_COST = TextEntry.tooltip("lightmanscurrency", "slot_machine.roll.normal_price");

    public static final TextEntry TOOLTIP_SLOT_MACHINE_COST_FREE = TextEntry.tooltip("lightmanscurrency", "slot_machine.roll.free");

    public static final TextEntry TOOLTIP_SLOT_MACHINE_UNDEFINED = TextEntry.tooltip("lightmanscurrency", "slot_machine.undefined");

    public static final TextEntry TOOLTIP_SLOT_MACHINE_MONEY = TextEntry.tooltip("lightmanscurrency", "slot_machine.money");

    public static final TextEntry TOOLTIP_SLOT_MACHINE_WEIGHT = TextEntry.tooltip("lightmanscurrency", "slot_machine.weight");

    public static final TextEntry TOOLTIP_SLOT_MACHINE_ODDS = TextEntry.tooltip("lightmanscurrency", "slot_machine.odds");

    public static final TextEntry GUI_NETWORK_TERMINAL_TITLE = TextEntry.gui("lightmanscurrency", "network_terminal.title");

    public static final TextEntry GUI_NETWORK_TERMINAL_SEARCH = TextEntry.gui("lightmanscurrency", "network_terminal.search");

    public static final TextEntry TOOLTIP_NETWORK_TERMINAL_TRADE_COUNT = TextEntry.tooltip("lightmanscurrency", "terminal.info.trade_count");

    public static final TextEntry TOOLTIP_NETWORK_TERMINAL_OUT_OF_STOCK_COUNT = TextEntry.tooltip("lightmanscurrency", "terminal.info.trade_count.out_of_stock");

    public static final TextEntry TOOLTIP_NETWORK_TERMINAL_AUCTION_HOUSE = TextEntry.tooltip("lightmanscurrency", "terminal.info.auction_house");

    public static final TextEntry BUTTON_NOTIFICATIONS_MARK_AS_READ = TextEntry.button("lightmanscurrency", "notifications.mark_read");

    public static final TextEntry TOOLTIP_TEAM_SELECT = TextEntry.tooltip("lightmanscurrency", "team.selection");

    public static final TextEntry BUTTON_TEAM_CREATE = TextEntry.button("lightmanscurrency", "team.create");

    public static final TextEntry GUI_TEAM_SELECT = TextEntry.button("lightmanscurrency", "team.select");

    public static final TextEntry GUI_TEAM_CREATE = TextEntry.gui("lightmanscurrency", "team.create");

    public static final TextEntry TOOLTIP_TEAM_MEMBERS = TextEntry.tooltip("lightmanscurrency", "team.members");

    public static final TextEntry TOOLTIP_TEAM_NAME = TextEntry.tooltip("lightmanscurrency", "team.name");

    public static final TextEntry BUTTON_TEAM_RENAME = TextEntry.button("lightmanscurrency", "team.rename");

    public static final TextEntry GUI_TEAM_NAME_CURRENT = TextEntry.gui("lightmanscurrency", "team.name.current");

    public static final TextEntry TOOLTIP_TEAM_MEMBER_EDIT = TextEntry.tooltip("lightmanscurrency", "team.member_edit");

    public static final TextEntry BUTTON_TEAM_MEMBER_PROMOTE = TextEntry.button("lightmanscurrency", "team.member.promote");

    public static final TextEntry TOOLTIP_TEAM_BANK = TextEntry.tooltip("lightmanscurrency", "team.bank");

    public static final TextEntry BUTTON_TEAM_BANK_CREATE = TextEntry.button("lightmanscurrency", "team.bank.create");

    public static final TextEntry BUTTON_TEAM_BANK_LIMIT = TextEntry.button("lightmanscurrency", "team.bank.limit");

    public static final TextEntry TOOLTIP_TEAM_STATS = TextEntry.tooltip("lightmanscurrency", "team.stats");

    public static final TextEntry TOOLTIP_TEAM_OWNER = TextEntry.tooltip("lightmanscurrency", "team.owner");

    public static final TextEntry BUTTON_TEAM_DISBAND = TextEntry.button("lightmanscurrency", "team.disband");

    public static final TextEntry GUI_TEAM_ID = TextEntry.gui("lightmanscurrency", "team.id");

    public static final TextEntry TOOLTIP_ATM_EXCHANGE = TextEntry.tooltip("lightmanscurrency", "atm.exchange");

    public static final TextEntry TOOLTIP_ATM_SELECTION = TextEntry.tooltip("lightmanscurrency", "atm.selection");

    public static final TextEntry BUTTON_BANK_MY_ACCOUNT = TextEntry.button("lightmanscurrency", "bank.my_account");

    public static final TextEntry BUTTON_BANK_PLAYER_ACCOUNT = TextEntry.button("lightmanscurrency", "bank.player_account");

    public static final TextEntry GUI_BANK_NO_TEAMS_AVAILABLE = TextEntry.gui("lightmanscurrency", "bank.no_teams_available");

    public static final TextEntry GUI_BANK_SELECT_PLAYER_SUCCESS = TextEntry.gui("lightmanscurrency", "bank.select.player.success");

    public static final TextEntry TOOLTIP_ATM_INTERACT = TextEntry.tooltip("lightmanscurrency", "atm.interact");

    public static final TextEntry TOOLTIP_ATM_NOTIFICATIONS = TextEntry.tooltip("lightmanscurrency", "atm.notification");

    public static final TextEntry GUI_BANK_NOTIFICATIONS_DISABLED = TextEntry.gui("lightmanscurrency", "bank.notification.disabled");

    public static final TextEntry GUI_BANK_NOTIFICATIONS_DETAILS = TextEntry.gui("lightmanscurrency", "bank.notification.details");

    public static final TextEntry TOOLTIP_ATM_LOGS = TextEntry.tooltip("lightmanscurrency", "atm.log");

    public static final TextEntry TOOLTIP_ATM_TRANSFER = TextEntry.tooltip("lightmanscurrency", "atm.transfer");

    public static final TextEntry TOOLTIP_ATM_TRANSFER_MODE_PLAYER = TextEntry.tooltip("lightmanscurrency", "atm.transfer.mode.player");

    public static final TextEntry TOOLTIP_ATM_TRANSFER_MODE_TEAM = TextEntry.tooltip("lightmanscurrency", "atm.transfer.mode.team");

    public static final TextEntry BUTTON_ATM_TRANSFER_PLAYER = TextEntry.button("lightmanscurrency", "bank.transfer.player");

    public static final TextEntry BUTTON_ATM_TRANSFER_TEAM = TextEntry.button("lightmanscurrency", "bank.transfer.team");

    public static final TextEntry BUTTON_EXCHANGE_UPGRADE_EXCHANGE_WHILE_OPEN_YES = TextEntry.button("lightmanscurrency", "upgrade.coin_chest.exchange.while_open.y");

    public static final TextEntry BUTTON_EXCHANGE_UPGRADE_EXCHANGE_WHILE_OPEN_NO = TextEntry.button("lightmanscurrency", "upgrade.coin_chest.exchange.while_open.n");

    public static final TextEntry MESSAGE_COIN_CHEST_PROTECTION_WARNING = TextEntry.message("lightmanscurrency", "coin_chest.protection.warning");

    public static final TextEntry GUI_EJECTION_NO_DATA = TextEntry.gui("lightmanscurrency", "ejection_menu.no_data");

    public static final TextEntry GUI_COIN_MINT_TITLE = TextEntry.gui("lightmanscurrency", "coinmint.title");

    public static final TextEntry BUTTON_PLAYER_TRADING_PROPOSE = TextEntry.button("lightmanscurrency", "player_trading.propose");

    public static final TextEntry BUTTON_PLAYER_TRADING_ACCEPT = TextEntry.button("lightmanscurrency", "player_trading.accept");

    public static final TextEntry BUTTON_PLAYER_TRADING_CANCEL = TextEntry.button("lightmanscurrency", "player_trading.cancel");

    public static final TextEntry TOOLTIP_PLAYER_TRADING_CHAT_OPEN = TextEntry.tooltip("lightmanscurrency", "player_trading.chat.open");

    public static final TextEntry TOOLTIP_PLAYER_TRADING_CHAT_CLOSE = TextEntry.tooltip("lightmanscurrency", "player_trading.chat.close");

    public static final TextEntry MESSAGE_TAX_COLLECTOR_PLACEMENT_TRADER = TextEntry.message("lightmanscurrency", "tax_collector.placement.trader");

    public static final TextEntry MESSAGE_TAX_COLLECTOR_PLACEMENT_TRADER_SERVER_ONLY = TextEntry.message("lightmanscurrency", "tax_collector.placement.trader.server_only");

    public static final TextEntry MESSAGE_TAX_COLLECTOR_PLACEMENT_TRADER_INFO = TextEntry.message("lightmanscurrency", "tax_collector.placement.trader.info");

    public static final TextEntry GUI_TAX_COLLECTOR_DEFAULT_NAME = TextEntry.gui("lightmanscurrency", "tax_collector.default_name");

    public static final TextEntry GUI_TAX_COLLECTOR_DEFAULT_NAME_SERVER = TextEntry.gui("lightmanscurrency", "tax_collector.default_name.server");

    public static final TextEntry TOOLTIP_TAX_COLLECTOR_BASIC = TextEntry.tooltip("lightmanscurrency", "tax_collector.basic");

    public static final TextEntry GUI_TAX_COLLECTOR_ACTIVE = TextEntry.gui("lightmanscurrency", "tax_collector.active");

    public static final TextEntry GUI_TAX_COLLECTOR_RENDER_MODE_LABEL = TextEntry.gui("lightmanscurrency", "tax_collector.render_mode.label");

    public static final TextEntry GUI_TAX_COLLECTOR_RENDER_MODE_NONE = TextEntry.gui("lightmanscurrency", "tax_collector.render_mode.0");

    public static final TextEntry GUI_TAX_COLLECTOR_RENDER_MODE_MEMBERS = TextEntry.gui("lightmanscurrency", "tax_collector.render_mode.1");

    public static final TextEntry GUI_TAX_COLLECTOR_RENDER_MODE_ALL = TextEntry.gui("lightmanscurrency", "tax_collector.render_mode.2");

    public static final TextEntry GUI_TAX_COLLECTOR_TAX_RATE = TextEntry.gui("lightmanscurrency", "tax_collector.tax_rate");

    public static final TextEntry GUI_TAX_COLLECTOR_AREA_INFINITE_LABEL = TextEntry.gui("lightmanscurrency", "tax_collector.area.infinite.label");

    public static final TextEntry GUI_TAX_COLLECTOR_AREA_INFINITE_VOID = TextEntry.gui("lightmanscurrency", "tax_collector.area.infinite.void");

    public static final TextEntry GUI_TAX_COLLECTOR_AREA_INFINITE_DIMENSION = TextEntry.gui("lightmanscurrency", "tax_collector.area.infinite.dimension");

    public static final TextEntry GUI_TAX_COLLECTOR_AREA_RADIUS = TextEntry.gui("lightmanscurrency", "tax_collector.area.radius");

    public static final TextEntry GUI_TAX_COLLECTOR_AREA_HEIGHT = TextEntry.gui("lightmanscurrency", "tax_collector.area.height");

    public static final TextEntry GUI_TAX_COLLECTOR_AREA_VERTOFFSET = TextEntry.gui("lightmanscurrency", "tax_collector.area.vertOffset");

    public static final TextEntry TOOLTIP_TAX_COLLECTOR_LOGS = TextEntry.tooltip("lightmanscurrency", "tax_collector.logs");

    public static final TextEntry TOOLTIP_TAX_COLLECTOR_INFO = TextEntry.tooltip("lightmanscurrency", "tax_collector.info");

    public static final TextEntry BUTTON_TAX_COLLECTOR_STATS_CLEAR = TextEntry.button("lightmanscurrency", "tax_collector.stats.clear");

    public static final TextEntry GUI_TAX_COLLECTOR_STATS_TOTAL_COLLECTED = TextEntry.button("lightmanscurrency", "tax_collector.stats.total_collected");

    public static final TextEntry GUI_TAX_COLLECTOR_STATS_UNIQUE_TAXABLES = TextEntry.button("lightmanscurrency", "tax_collector.stats.unique_taxables");

    public static final TextEntry GUI_TAX_COLLECTOR_STATS_MOST_TAXED_LABEL = TextEntry.button("lightmanscurrency", "tax_collector.stats.most_taxed.label");

    public static final TextEntry GUI_TAX_COLLECTOR_STATS_MOST_TAXED_FORMAT = TextEntry.button("lightmanscurrency", "tax_collector.stats.most_taxed.format");

    public static final TextEntry TOOLTIP_TAX_COLLECTOR_OWNER = TextEntry.tooltip("lightmanscurrency", "tax_collector.owner");

    public static final TextEntry TOOLTIP_TAX_COLLECTOR_ADMIN = TextEntry.tooltip("lightmanscurrency", "tax_collector.admin");

    public static final TextEntry GUI_TAX_COLLECTOR_FORCE_ACCEPTANCE = TextEntry.gui("lightmanscurrency", "tax_collector.force_acceptance");

    public static final TextEntry GUI_TAX_COLLECTOR_INFINITE_RANGE = TextEntry.gui("lightmanscurrency", "tax_collector.infinite_range");

    public static final TextEntry GUI_TAX_COLLECTOR_TAXABLE_ACCEPT_COLLECTOR = TextEntry.gui("lightmanscurrency", "tax_collector.taxable.accept_collector");

    public static final TextEntry TOOLTIP_TAX_COLLECTOR_TAXABLE_FORCE_IGNORE = TextEntry.tooltip("lightmanscurrency", "tax_collector.taxable.force_ignore_collector");

    public static final TextEntry TOOLTIP_TAX_COLLECTOR_TAXABLE_PARDON_IGNORED = TextEntry.tooltip("lightmanscurrency", "tax_collector.taxable.pardon_ignored_collector");

    public static final TextEntry MESSAGE_TAX_COLLECTOR_WARNING_MISSING_DATA = TextEntry.message("lightmanscurrency", "tax_collector.warning.missing_data");

    public static final TextEntry MESSAGE_TAX_COLLECTOR_WARNING_NO_ACCESS = TextEntry.message("lightmanscurrency", "tax_collector.warning.no_access");

    public static final TextEntry GUI_TICKET_STATION_TITLE = TextEntry.gui("lightmanscurrency", "ticket_station.title");

    public static final TextEntry TOOLTIP_TICKET_STATION_RECIPE_INFO = TextEntry.tooltip("lightmanscurrency", "ticket_station.recipe_info");

    public static final TextEntry TOOLTIP_TICKET_STATION_SELECT_RECIPE = TextEntry.tooltip("lightmanscurrency", "ticket_station.select_recipe");

    public static final TextEntry TOOLTIP_TICKET_STATION_CRAFT = TextEntry.tooltip("lightmanscurrency", "ticket_station.craft_ticket");

    public static final TextEntryBundle<TraderInterfaceBlockEntity.InteractionType> GUI_INTERFACE_INTERACTION_TYPE = TextEntryBundle.of(TraderInterfaceBlockEntity.InteractionType.values(), "gui.lightmanscurrency.interface.type");

    public static final TextEntryBundle<TraderInterfaceBlockEntity.ActiveMode> GUI_INTERFACE_ACTIVE_MODE = TextEntryBundle.of(TraderInterfaceBlockEntity.ActiveMode.values(), "gui.lightmanscurrency.interface.mode");

    public static final TextEntry TOOLTIP_INTERFACE_ONLINE_MODE_ON = TextEntry.tooltip("lightmanscurrency", "interface.onlinemode.true");

    public static final TextEntry TOOLTIP_INTERFACE_ONLINE_MODE_OFF = TextEntry.tooltip("lightmanscurrency", "interface.onlinemode.false");

    public static final TextEntry TOOLTIP_INTERFACE_INFO = TextEntry.tooltip("lightmanscurrency", "interface.info");

    public static final TextEntry TOOLTIP_INTERFACE_INFO_ACCEPT_CHANGES = TextEntry.tooltip("lightmanscurrency", "interface.info.acceptchanges");

    public static final TextEntry GUI_INTERFACE_INFO_MISSING_PERMISSIONS = TextEntry.gui("lightmanscurrency", "interface.info.trader.permissions");

    public static final TextEntry GUI_INTERFACE_INFO_TRADER_NULL = TextEntry.gui("lightmanscurrency", "interface.info.trader.null");

    public static final TextEntry GUI_INTERFACE_INFO_TRADER_REMOVED = TextEntry.gui("lightmanscurrency", "interface.info.trader.removed");

    public static final TextEntry GUI_INTERFACE_INFO_TRADE_NOT_DEFINED = TextEntry.gui("lightmanscurrency", "interface.info.trade.notdefined");

    public static final TextEntry GUI_INTERFACE_INFO_TRADE_MISSING = TextEntry.gui("lightmanscurrency", "interface.info.trader.missing");

    public static final TextEntry TOOLTIP_INTERFACE_TRADER_SELECT = TextEntry.tooltip("lightmanscurrency", "interface.trader");

    public static final TextEntry TOOLTIP_INTERFACE_TRADE_SELECT = TextEntry.tooltip("lightmanscurrency", "interface.trade");

    public static final TextEntry TOOLTIP_INTERFACE_STORAGE = TextEntry.tooltip("lightmanscurrency", "interface.storage");

    public static final TextEntryBundle<TradeResult> GUI_TRADE_RESULT = TextEntryBundle.of(TradeResult.ALL_WITH_MESSAGES, "gui.lightmanscurrency.trade_result");

    public static final TextEntry GUI_TRADE_DIFFERENCE_MISSING = TextEntry.gui("lightmanscurrency", "interface.difference.missing");

    public static final TextEntry GUI_TRADE_DIFFERENCE_TYPE = TextEntry.gui("lightmanscurrency", "interface.difference.type");

    public static final TextEntry GUI_TRADE_DIFFERENCE_MONEY_TYPE = TextEntry.gui("lightmanscurrency", "interface.difference.money_type");

    public static final TextEntry GUI_TRADE_DIFFERENCE_CHEAPER = TextEntry.gui("lightmanscurrency", "interface.difference.cheaper");

    public static final TextEntry GUI_TRADE_DIFFERENCE_EXPENSIVE = TextEntry.gui("lightmanscurrency", "interface.difference.expensive");

    public static final TextEntry GUI_TRADE_DIFFERENCE_PURCHASE_CHEAPER = TextEntry.gui("lightmanscurrency", "interface.difference.purchase.cheaper");

    public static final TextEntry GUI_TRADE_DIFFERENCE_PURCHASE_EXPENSIVE = TextEntry.gui("lightmanscurrency", "interface.difference.purchase.expensive");

    public static final TextEntry GUI_TRADE_DIFFERENCE_ITEM_SELLING = TextEntry.gui("lightmanscurrency", "interface.item.difference.selling");

    public static final TextEntry GUI_TRADE_DIFFERENCE_ITEM_PURCHASING = TextEntry.gui("lightmanscurrency", "interface.item.difference.purchasing");

    public static final TextEntry GUI_TRADE_DIFFERENCE_ITEM_TYPE = TextEntry.gui("lightmanscurrency", "interface.item.difference.itemtype");

    public static final TextEntry GUI_TRADE_DIFFERENCE_ITEM_NBT = TextEntry.gui("lightmanscurrency", "interface.item.difference.itemnbt");

    public static final TextEntry GUI_TRADE_DIFFERENCE_ITEM_QUANTITY_MORE = TextEntry.gui("lightmanscurrency", "interface.item.difference.quantity.more");

    public static final TextEntry GUI_TRADE_DIFFERENCE_ITEM_QUANTITY_LESS = TextEntry.gui("lightmanscurrency", "interface.item.difference.quantity.less");

    public static final TextEntry TOOLTIP_WALLET_EXCHANGE = TextEntry.tooltip("lightmanscurrency", "wallet.exchange");

    public static final TextEntry TOOLTIP_WALLET_AUTO_EXCHANGE_ENABLE = TextEntry.tooltip("lightmanscurrency", "wallet.auto_exchange.enable");

    public static final TextEntry TOOLTIP_WALLET_AUTO_EXCHANGE_DISABLE = TextEntry.tooltip("lightmanscurrency", "wallet.auto_exchange.disable");

    public static final TextEntry TOOLTIP_WALLET_OPEN_BANK = TextEntry.tooltip("lightmanscurrency", "wallet.open_bank");

    public static final TextEntry TOOLTIP_WALLET_OPEN_WALLET = TextEntry.tooltip("lightmanscurrency", "wallet.open_wallet");

    public static final TextEntry GUI_TRADER_TITLE = TextEntry.gui("lightmanscurrency", "trader.title");

    public static final TextEntry GUI_TRADER_DEFAULT_NAME = TextEntry.gui("lightmanscurrency", "trader.default_name");

    public static final TextEntry TOOLTIP_TRADER_OPEN_STORAGE = TextEntry.tooltip("lightmanscurrency", "trader.open_storage");

    public static final TextEntry TOOLTIP_TRADER_COLLECT_COINS = TextEntry.tooltip("lightmanscurrency", "trader.collect_coins");

    public static final TextEntry TOOLTIP_TRADER_NETWORK_BACK = TextEntry.tooltip("lightmanscurrency", "trader.network.back");

    public static final TextEntry TOOLTIP_TRADER_OPEN_TRADES = TextEntry.tooltip("lightmanscurrency", "trader.open_trades");

    public static final TextEntry TOOLTIP_TRADER_STORE_COINS = TextEntry.tooltip("lightmanscurrency", "trader.store_coins");

    public static final TextEntry TOOLTIP_TRADER_TRADE_RULES_TRADER = TextEntry.tooltip("lightmanscurrency", "trader.trade_rules.trader");

    public static final TextEntry TOOLTIP_TRADER_TRADE_RULES_TRADE = TextEntry.tooltip("lightmanscurrency", "trader.trade_rules.trade");

    public static final TextEntry GUI_TRADER_NO_TRADES = TextEntry.gui("lightmanscurrency", "notrades");

    public static final TextEntry TOOLTIP_TRADER_EDIT_TRADES = TextEntry.tooltip("lightmanscurrency", "trader.edit_trades");

    public static final TextEntry TOOLTIP_TRADER_LOGS = TextEntry.tooltip("lightmanscurrency", "trader.log");

    public static final TextEntry TOOLTIP_TRADER_SETTINGS = TextEntry.tooltip("lightmanscurrency", "trader.settings");

    public static final TextEntry TOOLTIP_TRADER_SETTINGS_NAME = TextEntry.tooltip("lightmanscurrency", "trader.settings.name");

    public static final TextEntry TOOLTIP_TRADER_SETTINGS_CREATIVE_ENABLE = TextEntry.tooltip("lightmanscurrency", "trader.settings.creative.enable");

    public static final TextEntry TOOLTIP_TRADER_SETTINGS_CREATIVE_DISABLE = TextEntry.tooltip("lightmanscurrency", "trader.settings.creative.disable");

    public static final TextEntry TOOLTIP_TRADER_SETTINGS_CREATIVE_ADD_TRADE = TextEntry.tooltip("lightmanscurrency", "trader.settings.creative.add_trade");

    public static final TextEntry TOOLTIP_TRADER_SETTINGS_CREATIVE_REMOVE_TRADE = TextEntry.tooltip("lightmanscurrency", "trader.settings.creative.remove_trade");

    public static final TextEntry TOOLTIP_TRADER_SETTINGS_ALLY = TextEntry.tooltip("lightmanscurrency", "trader.settings.ally");

    public static final TextEntry TOOLTIP_TRADER_SETTINGS_ALLY_PERMS = TextEntry.tooltip("lightmanscurrency", "trader.settings.allyperms");

    public static final TextEntry TOOLTIP_TRADER_SETTINGS_NOTIFICATIONS = TextEntry.tooltip("lightmanscurrency", "trader.settings.notifications");

    public static final TextEntry GUI_TRADER_SETTINGS_NOTIFICATIONS_ENABLED = TextEntry.tooltip("lightmanscurrency", "trader.settings.notifications.enabled");

    public static final TextEntry GUI_TRADER_SETTINGS_NOTIFICATIONS_CHAT = TextEntry.tooltip("lightmanscurrency", "trader.settings.notifications.chat");

    public static final TextEntry GUI_TRADER_SETTINGS_NOTIFICATIONS_TARGET = TextEntry.tooltip("lightmanscurrency", "trader.settings.notifications.target");

    public static final TextEntry TOOLTIP_TRADER_SETTINGS_TAXES = TextEntry.tooltip("lightmanscurrency", "trader.settings.taxes");

    public static final TextEntry GUI_TRADER_SETTINGS_TAXES_ACCEPTABLE_RATE = TextEntry.tooltip("lightmanscurrency", "trader.settings.acceptabletaxrate");

    public static final TextEntry GUI_TRADER_SETTINGS_TAXES_IGNORE_TAXES = TextEntry.tooltip("lightmanscurrency", "trader.settings.ignoretaxes");

    public static final TextEntry TOOLTIP_TRADER_SETTINGS_INPUT_GENERIC = TextEntry.tooltip("lightmanscurrency", "trader.settings.input");

    public static final TextEntry TOOLTIP_TRADER_SETTINGS_INPUT_ITEM = TextEntry.tooltip("lightmanscurrency", "trader.settings.iteminput");

    public static final TextEntry TOOLTIP_TRADER_STATS = TextEntry.tooltip("lightmanscurrency", "trader.stats");

    public static final TextEntry BUTTON_TRADER_STATS_CLEAR = TextEntry.button("lightmanscurrency", "trader.stats.clear");

    public static final TextEntry GUI_TRADER_STATS_EMPTY = TextEntry.gui("lightmanscurrency", "trader.stats.empty");

    public static final TextEntry TOOLTIP_TRADER_TAXES = TextEntry.tooltip("lightmanscurrency", "trader.tax_info");

    public static final TextEntry GUI_TRADER_TAXES_TOTAL_RATE = TextEntry.tooltip("lightmanscurrency", "trader.tax_info.total_rate");

    public static final TextEntry GUI_TRADER_TAXES_NO_TAX_COLLECTORS = TextEntry.gui("lightmanscurrency", "trade.tax_info.no_tax_collectors");

    public static final TextEntry TOOLTIP_TRADER_STORAGE = TextEntry.tooltip("lightmanscurrency", "trader.storage");

    public static final TextEntry MESSAGE_TRADER_WARNING_MISSING_DATA = TextEntry.message("lightmanscurrency", "trader.warning.missing_data");

    public static final TextEntry TOOLTIP_TRADE_EDIT_PRICE = TextEntry.tooltip("lightmanscurrency", "trade.edit_price");

    public static final TextEntry TOOLTIP_TRADE_INFO_TITLE = TextEntry.tooltip("lightmanscurrency", "trade.info.title");

    public static final TextEntry TOOLTIP_TRADE_INFO_ORIGINAL_NAME = TextEntry.tooltip("lightmanscurrency", "trade.info.original_name");

    public static final TextEntry TOOLTIP_TRADE_INFO_STOCK = TextEntry.tooltip("lightmanscurrency", "trade.info.stock");

    public static final TextEntry TOOLTIP_TRADE_INFO_STOCK_INFINITE = TextEntry.tooltip("lightmanscurrency", "trade.info.stock.infinite");

    public static final TextEntry GUI_TRADER_ITEM_ENFORCE_NBT = TextEntry.gui("lightmanscurrency", "trader.item.enforce_nbt");

    public static final TextEntry TOOLTIP_TRADE_ITEM_EDIT_ITEM = TextEntry.tooltip("lightmanscurrency", "trade.item.edit_item");

    public static final TextEntry TOOLTIP_TRADE_ITEM_NBT_WARNING_PURCHASE = TextEntry.tooltip("lightmanscurrency", "trade.item.nbt_warning.purchase");

    public static final TextEntry TOOLTIP_TRADE_ITEM_NBT_WARNING_SALE = TextEntry.tooltip("lightmanscurrency", "trade.item.nbt_warning.sale");

    public static final TextEntry GUI_ITEM_EDIT_SEARCH = TextEntry.gui("lightmanscurrency", "item_edit.search");

    public static final TextEntry TOOLTIP_ITEM_EDIT_SCROLL = TextEntry.gui("lightmanscurrency", "item_edit.scroll");

    public static final TextEntry TOOLTIP_TRADER_PAYGATE_COLLECT_TICKET_STUBS = TextEntry.tooltip("lightmanscurrency", "trader.paygate.collect_ticket_stubs");

    public static final TextEntry GUI_TRADER_PAYGATE_DURATION = TextEntry.gui("lightmanscurrency", "trader.paygate.duration");

    public static final TextEntry GUI_TRADER_PAYGATE_DURATION_UNIT = TextEntry.gui("lightmanscurrency", "trader.paygate.duration.unit");

    public static final TextEntry TOOLTIP_TRADER_PAYGATE_TICKET_STUBS_KEEP = TextEntry.tooltip("lightmanscurrency", "trader.paygate.ticket_stubs.keep");

    public static final TextEntry TOOLTIP_TRADER_PAYGATE_TICKET_STUBS_GIVE = TextEntry.tooltip("lightmanscurrency", "trader.paygate.ticket_stubs.give");

    public static final TextEntry TOOLTIP_TRADER_PAYGATE_ALREADY_ACTIVE = TextEntry.tooltip("lightmanscurrency", "trader.paygate.active");

    public static final TextEntry GUI_TRADER_AUCTION_HOUSE = TextEntry.gui("lightmanscurrency", "trader.auction_house");

    public static final TextEntry GUI_TRADER_AUCTION_HOUSE_OWNER = TextEntry.gui("lightmanscurrency", "trader.auction_house.owner");

    public static final TextEntry BUTTON_TRADER_AUCTION_BID = TextEntry.button("lightmanscurrency", "trader.auction.bid");

    public static final TextEntry TOOLTIP_TRADER_AUCTION_STORAGE = TextEntry.tooltip("lightmanscurrency", "trader.auction.storage");

    public static final TextEntry GUI_TRADER_AUCTION_STORAGE_ITEMS_NONE = TextEntry.gui("lightmanscurrency", "trader.auction.storage.items.none");

    public static final TextEntry GUI_TRADER_AUCTION_STORAGE_MONEY = TextEntry.gui("lightmanscurrency", "trader.auction.storage.money");

    public static final TextEntry GUI_TRADER_AUCTION_STORAGE_MONEY_NONE = TextEntry.gui("lightmanscurrency", "trader.auction.storage.money.none");

    public static final TextEntry GUI_TRADER_AUCTION_CANCEL = TextEntry.gui("lightmanscurrency", "trader.auction.cancel");

    public static final TextEntry BUTTON_TRADER_AUCTION_CANCEL_SELF = TextEntry.button("lightmanscurrency", "trader.auction.cancel.self");

    public static final TextEntry TOOLTIP_TRADER_AUCTION_CANCEL_SELF = TextEntry.tooltip("lightmanscurrency", "trader.auction.cancel.self");

    public static final TextEntry BUTTON_TRADER_AUCTION_CANCEL_STORAGE = TextEntry.button("lightmanscurrency", "trader.auction.cancel.storage");

    public static final TextEntry TOOLTIP_TRADER_AUCTION_CANCEL_STORAGE = TextEntry.tooltip("lightmanscurrency", "trader.auction.cancel.storage");

    public static final TextEntry TOOLTIP_TRADER_AUCTION_CREATE = TextEntry.tooltip("lightmanscurrency", "trader.auction.create");

    public static final TextEntry BUTTON_TRADER_AUCTION_PRICE_MODE_STARTING_BID = TextEntry.button("lightmanscurrency", "trader.auction.price_mode.starting_bid");

    public static final TextEntry BUTTON_TRADER_AUCTION_PRICE_MODE_MIN_BID_SIZE = TextEntry.button("lightmanscurrency", "trader.auction.price_mode.min_bid_size");

    public static final TextEntry BUTTON_TRADER_AUCTION_CREATE = TextEntry.button("lightmanscurrency", "trader.auction.create");

    public static final TextEntry GUI_TRADER_AUCTION_ITEMS = TextEntry.gui("lightmanscurrency", "trader.auction.items");

    public static final TextEntry GUI_TRADER_AUCTION_CREATE_SUCCESS = TextEntry.gui("lightmanscurrency", "trader.auction.create.success");

    public static final TextEntry TOOLTIP_TRADER_AUCTION_INFO_NO_BIDDER = TextEntry.tooltip("lightmanscurrency", "trader.auction.info.no_bidder");

    public static final TextEntry TOOLTIP_TRADER_AUCTION_INFO_STARTING_BID = TextEntry.tooltip("lightmanscurrency", "trader.auction.info.starting_bid");

    public static final TextEntry TOOLTIP_TRADER_AUCTION_INFO_LAST_BIDDER = TextEntry.tooltip("lightmanscurrency", "trader.auction.info.last_bidder");

    public static final TextEntry TOOLTIP_TRADER_AUCTION_INFO_LAST_BID = TextEntry.tooltip("lightmanscurrency", "trader.auction.info.last_bid");

    public static final TextEntry TOOLTIP_TRADER_AUCTION_INFO_MIN_BID = TextEntry.tooltip("lightmanscurrency", "trader.auction.info.min_bid");

    public static final TextEntry TOOLTIP_TRADER_AUCTION_TIME_REMAINING = TextEntry.tooltip("lightmanscurrency", "trader.auction.time_remaining");

    public static final TextEntry TOOLTIP_TRADER_SLOT_MACHINE_EDIT_ENTRIES = TextEntry.tooltip("lightmanscurrency", "trader.slot_machine.edit_entries");

    public static final TextEntry GUI_TRADER_SLOT_MACHINE_WEIGHT_LABEL = TextEntry.gui("lightmanscurrency", "trader.slot_machine.weight_label");

    public static final TextEntry GUI_TRADER_SLOT_MACHINE_ENTRY_LABEL = TextEntry.gui("lightmanscurrency", "trader.slot_machine.entry_label");

    public static final TextEntry GUI_TRADER_SLOT_MACHINE_ODDS_LABEL = TextEntry.gui("lightmanscurrency", "trader.slot_machine.odds_label");

    public static final TextEntry TOOLTIP_TRADER_SLOT_MACHINE_EDIT_PRICE = TextEntry.gui("lightmanscurrency", "trader.slot_machine.edit_price");

    public static final TextEntryBundle<TradeDirection> GUI_TRADE_DIRECTION = TextEntryBundle.of(TradeDirection.values(), "gui.lightmanscurrency.trade_direction");

    public static final TextEntryBundle<TradeDirection> GUI_TRADE_DIRECTION_ACTION = TextEntryBundle.of(TradeDirection.values(), "gui.lightmanscurrency.trade_direction.action");

    public static final TextEntry GUI_TRADE_RULES_LIST = TextEntry.gui("lightmanscurrency", "trade_rule.list");

    public static final TextEntry TOOLTIP_TRADE_RULES_MANAGER = TextEntry.gui("lightmanscurrency", "trade_rule.manager");

    public static final TextEntry TRADE_RULE_PLAYER_LISTING = TextEntry.tradeRule(PlayerListing.TYPE);

    public static final TextEntry TRADE_RULE_PLAYER_LISTING_DENIAL_BLACKLIST = TextEntry.tradeRuleMessage(PlayerListing.TYPE, "denial.blacklist");

    public static final TextEntry TRADE_RULE_PLAYER_LISTING_DENIAL_WHITELIST = TextEntry.tradeRuleMessage(PlayerListing.TYPE, "denial.whitelist");

    public static final TextEntry TRADE_RULE_PLAYER_LISTING_ALLOWED = TextEntry.tradeRuleMessage(PlayerListing.TYPE, "allowed");

    public static final TextEntry BUTTON_PLAYER_LISTING_MODE_WHITELIST = TextEntry.button("lightmanscurrency", "trade_rule.player_listing.mode.whitelist");

    public static final TextEntry BUTTON_PLAYER_LISTING_MODE_BLACKLIST = TextEntry.button("lightmanscurrency", "trade_rule.player_listing.mode.blacklist");

    public static final TextEntry TRADE_RULE_PLAYER_TRADE_LIMIT = TextEntry.tradeRule(PlayerTradeLimit.TYPE);

    public static final TextEntry TRADE_RULE_PLAYER_TRADE_LIMIT_DENIAL_TIMED = TextEntry.tradeRuleMessage(PlayerTradeLimit.TYPE, "denial.timed");

    public static final TextEntry TRADE_RULE_PLAYER_TRADE_LIMIT_DENIAL = TextEntry.tradeRuleMessage(PlayerTradeLimit.TYPE, "denial");

    public static final TextEntry TRADE_RULE_PLAYER_TRADE_LIMIT_DENIAL_LIMIT = TextEntry.tradeRuleMessage(PlayerTradeLimit.TYPE, "denial.limit");

    public static final TextEntry TRADE_RULE_PLAYER_TRADE_LIMIT_INFO_TIMED = TextEntry.tradeRuleMessage(PlayerTradeLimit.TYPE, "info.timed");

    public static final TextEntry TRADE_RULE_PLAYER_TRADE_LIMIT_INFO = TextEntry.tradeRuleMessage(PlayerTradeLimit.TYPE, "info");

    public static final TextEntry TOOLTIP_TRADE_LIMIT_CLEAR_MEMORY = TextEntry.tooltip("lightmanscurrency", "trade_rule.player_trade_limit.clear_memory");

    public static final TextEntry GUI_TRADE_LIMIT_INFO = TextEntry.tooltip("lightmanscurrency", "trade_rule.trade_limit.info");

    public static final TextEntry GUI_PLAYER_TRADE_LIMIT_DURATION = TextEntry.tooltip("lightmanscurrency", "trade_rule.player_trade_limit.duration");

    public static final TextEntry GUI_PLAYER_TRADE_LIMIT_NO_DURATION = TextEntry.tooltip("lightmanscurrency", "trade_rule.player_trade_limit.no_duration");

    public static final TextEntry TRADE_RULE_PLAYER_DISCOUNTS = TextEntry.tradeRule(PlayerDiscounts.TYPE);

    public static final TextEntry TRADE_RULE_PLAYER_DISCOUNTS_INFO_SALE = TextEntry.tradeRuleMessage(PlayerDiscounts.TYPE, "info.sale");

    public static final TextEntry TRADE_RULE_PLAYER_DISCOUNTS_INFO_PURCHASE = TextEntry.tradeRuleMessage(PlayerDiscounts.TYPE, "info.purchase");

    public static final TextEntry GUI_PLAYER_DISCOUNTS_INFO = TextEntry.gui("lightmanscurrency", "trade_rule.discount_list.info");

    public static final TextEntry TRADE_RULE_TIMED_SALE = TextEntry.tradeRule(TimedSale.TYPE);

    public static final TextEntry TRADE_RULE_TIMED_SALE_INFO_SALE = TextEntry.tradeRuleMessage(TimedSale.TYPE, "info.sale");

    public static final TextEntry TRADE_RULE_TIMED_SALE_INFO_PURCHASE = TextEntry.tradeRuleMessage(TimedSale.TYPE, "info.purchase");

    public static final TextEntry GUI_TIMED_SALE_INFO_ACTIVE = TextEntry.gui("lightmanscurrency", "trade_rule.timed_sale.info.active");

    public static final TextEntry GUI_TIMED_SALE_INFO_INACTIVE = TextEntry.gui("lightmanscurrency", "trade_rule.timed_sale.info.inactive");

    public static final TextEntry BUTTON_TIMED_SALE_START = TextEntry.button("lightmanscurrency", "trade_rule.timed_sale.start");

    public static final TextEntry TOOLTIP_TIMED_SALE_START = TextEntry.tooltip("lightmanscurrency", "trade_rule.timed_sale.start");

    public static final TextEntry BUTTON_TIMED_SALE_STOP = TextEntry.button("lightmanscurrency", "trade_rule.timed_sale.stop");

    public static final TextEntry TOOLTIP_TIMED_SALE_STOP = TextEntry.tooltip("lightmanscurrency", "trade_rule.timed_sale.stop");

    public static final TextEntry TRADE_RULE_TRADE_LIMIT = TextEntry.tradeRule(TradeLimit.TYPE);

    public static final TextEntry TRADE_RULE_TRADE_LIMIT_DENIAL = TextEntry.tradeRuleMessage(TradeLimit.TYPE, "denial");

    public static final TextEntry TRADE_RULE_TRADE_LIMIT_INFO = TextEntry.tradeRuleMessage(TradeLimit.TYPE, "info");

    public static final TextEntry TRADE_RULE_FREE_SAMPLE = TextEntry.tradeRule(FreeSample.TYPE);

    public static final TextEntry TRADE_RULE_FREE_SAMPLE_INFO = TextEntry.tradeRuleMessage(FreeSample.TYPE, "info");

    public static final TextEntry BUTTON_FREE_SAMPLE_RESET = TextEntry.button("lightmanscurrency", "trade_rule.free_sample.reset");

    public static final TextEntry TOOLTIP_FREE_SAMPLE_RESET = TextEntry.tooltip("lightmanscurrency", "trade_rule.free_sample.reset");

    public static final TextEntry GUI_FREE_SAMPLE_PLAYER_COUNT = TextEntry.gui("lightmanscurrency", "trade_rule.free_sample.player_count");

    public static final TextEntry TRADE_RULE_PRICE_FLUCTUATION = TextEntry.tradeRule(PriceFluctuation.TYPE);

    public static final TextEntry GUI_PRICE_FLUCTUATION_LABEL = TextEntry.gui("lightmanscurrency", "trade_rule.price_fluctuation.label");

    public static final TextEntry GUI_PRICE_FLUCTUATION_INFO = TextEntry.gui("lightmanscurrency", "trade_rule.price_fluctuation.info");

    public static final TextEntry PERMISSION_OPEN_STORAGE = TextEntry.permission("openStorage");

    public static final TextEntry PERMISSION_CHANGE_NAME = TextEntry.permission("changeName");

    public static final TextEntry PERMISSION_EDIT_TRADES = TextEntry.permission("editTrades");

    public static final TextEntry PERMISSION_COLLECT_MONEY = TextEntry.permission("collectCoins");

    public static final TextEntry PERMISSION_STORE_MONEY = TextEntry.permission("storeCoins");

    public static final TextEntry PERMISSION_EDIT_TRADE_RULES = TextEntry.permission("editTradeRules");

    public static final TextEntry PERMISSION_EDIT_SETTINGS = TextEntry.permission("editSettings");

    public static final TextEntry PERMISSION_EDIT_ALLIES = TextEntry.permission("addRemoveAllies");

    public static final TextEntry PERMISSION_EDIT_PERMISSIONS = TextEntry.permission("editPermissions");

    public static final TextEntry PERMISSION_VIEW_LOGS = TextEntry.permission("viewLogs");

    public static final TextEntry PERMISSION_BREAK_MACHINE = TextEntry.permission("breakTrader");

    public static final TextEntry PERMISSION_BANK_LINK = TextEntry.permission("bankLink");

    public static final TextEntry PERMISSION_NOTIFICATION = TextEntry.permission("notifications");

    public static final TextEntry PERMISSION_INTERACTION_LINK = TextEntry.permission("interactionLink");

    public static final TextEntry PERMISSION_TRANSFER_OWNERSHIP = TextEntry.permission("transferOwnership");

    public static final TextEntry PERMISSION_EDIT_INPUTS = TextEntry.permission("changeExternalInputs");

    public static final TextEntry TOOLTIP_NOTIFICATION_BUTTON = TextEntry.tooltip("lightmanscurrency", "button.notification");

    public static final TextEntry TOOLTIP_TEAM_MANAGER_BUTTON = TextEntry.tooltip("lightmanscurrency", "button.team_manager");

    public static final TextEntry TOOLTIP_EJECTION_BUTTON = TextEntry.tooltip("lightmanscurrency", "button.ejection");

    public static final TextEntry TOOLTIP_CHEST_COIN_COLLECTION_BUTTON = TextEntry.tooltip("lightmanscurrency", "button.chest.coin_collection");

    public static final TextEntry BUTTON_SETTINGS_CHANGE_NAME = TextEntry.button("lightmanscurrency", "settings.change_name");

    public static final TextEntry BUTTON_SETTINGS_RESET_NAME = TextEntry.button("lightmanscurrency", "settings.reset_name");

    public static final TextEntry GUI_SETTINGS_BANK_LINK = TextEntry.gui("lightmanscurrency", "settings.banklink");

    public static final TextEntry TOOLTIP_SETTINGS_OWNER = TextEntry.tooltip("lightmanscurrency", "settings.owner");

    public static final TextEntry GUI_SETTINGS_INPUT_SIDE = TextEntry.gui("lightmanscurrency", "settings.input.side");

    public static final TextEntry GUI_SETTINGS_OUTPUT_SIDE = TextEntry.gui("lightmanscurrency", "settings.output.side");

    public static final TextEntryBundle<Direction> GUI_INPUT_SIDES = TextEntryBundle.of(Direction.values(), "gui.lightmanscurrency.settings.side");

    public static final TextEntry GUI_PERSISTENT_ID = TextEntry.gui("lightmanscurrency", "settings.persistent.id");

    public static final TextEntry GUI_PERSISTENT_OWNER = TextEntry.gui("lightmanscurrency", "settings.persistent.owner");

    public static final TextEntry TOOLTIP_PERSISTENT_CREATE_TRADER = TextEntry.tooltip("lightmanscurrency", "persistent.add.trader");

    public static final TextEntry MESSAGE_PERSISTENT_TRADER_OVERWRITE = TextEntry.message("lightmanscurrency", "persistent.trader.overwrite");

    public static final TextEntry MESSAGE_PERSISTENT_TRADER_ADD = TextEntry.message("lightmanscurrency", "persistent.trader.add");

    public static final TextEntry MESSAGE_PERSISTENT_TRADER_FAIL = TextEntry.message("lightmanscurrency", "persistent.trader.fail");

    public static final TextEntry TOOLTIP_PERSISTENT_CREATE_AUCTION = TextEntry.tooltip("lightmanscurrency", "persistent.add.auction");

    public static final TextEntry MESSAGE_PERSISTENT_AUCTION_OVERWRITE = TextEntry.message("lightmanscurrency", "persistent.auction.overwrite");

    public static final TextEntry MESSAGE_PERSISTENT_AUCTION_ADD = TextEntry.message("lightmanscurrency", "persistent.auction.add");

    public static final TextEntry MESSAGE_PERSISTENT_AUCTION_FAIL = TextEntry.message("lightmanscurrency", "persistent.auction.fail");

    public static final TextEntry GUI_BANK_BALANCE = TextEntry.gui("lightmanscurrency", "bank.balance");

    public static final TextEntry GUI_BANK_NO_SELECTED_ACCOUNT = TextEntry.gui("lightmanscurrency", "bank.null");

    public static final TextEntry BUTTON_BANK_DEPOSIT = TextEntry.button("lightmanscurrency", "bank.deposit");

    public static final TextEntry BUTTON_BANK_WITHDRAW = TextEntry.button("lightmanscurrency", "bank.withdraw");

    public static final TextEntry GUI_BANK_ACCOUNT_NAME = TextEntry.gui("lightmanscurrency", "bank_account");

    public static final TextEntry GUI_BANK_TRANSFER_ERROR_NULL_FROM = TextEntry.gui("lightmanscurrency", "bank.transfer.error.null");

    public static final TextEntry GUI_BANK_TRANSFER_ERROR_ACCESS = TextEntry.gui("lightmanscurrency", "bank.transfer.error.access");

    public static final TextEntry GUI_BANK_TRANSFER_ERROR_NULL_TARGET = TextEntry.gui("lightmanscurrency", "bank.error.null_account.target");

    public static final TextEntry GUI_BANK_TRANSFER_ERROR_AMOUNT = TextEntry.gui("lightmanscurrency", "bank.transfer.error.amount");

    public static final TextEntry GUI_BANK_TRANSFER_ERROR_SAME = TextEntry.gui("lightmanscurrency", "bank.transfer.error.same");

    public static final TextEntry GUI_BANK_TRANSFER_ERROR_NO_BALANCE = TextEntry.gui("lightmanscurrency", "bank.transfer.error.no_balance");

    public static final TextEntry GUI_BANK_TRANSFER_SUCCESS = TextEntry.gui("lightmanscurrency", "bank.transfer.success");

    public static final TextEntry BLURB_OWNERSHIP_MEMBERS = TextEntry.blurb("lightmanscurrency", "ownership.members");

    public static final TextEntry BLURB_OWNERSHIP_ADMINS = TextEntry.blurb("lightmanscurrency", "ownership.admins");

    public static final TextEntry BLURB_OWNERSHIP_OWNER = TextEntry.blurb("lightmanscurrency", "ownership.owner");

    public static final TextEntry BUTTON_OWNER_SET_PLAYER = TextEntry.button("lightmanscurrency", "ownership.set_player");

    public static final TextEntry GUI_OWNER_CURRENT = TextEntry.gui("lightmanscurrency", "owner.current");

    public static final TextEntry GUI_OWNER_NULL = TextEntry.gui("lightmanscurrency", "owner.null");

    public static final TextEntry TOOLTIP_OWNERSHIP_MODE_MANUAL = TextEntry.tooltip("lightmanscurrency", "owner_mode.manual");

    public static final TextEntry TOOLTIP_OWNERSHIP_MODE_SELECTION = TextEntry.tooltip("lightmanscurrency", "owner_mode.selection");

    public static final TextEntry GUI_MONEY_VALUE_FREE = TextEntry.gui("lightmanscurrency", "money_value.free");

    public static final TextEntry GUI_MONEY_STORAGE_EMPTY = TextEntry.gui("lightmanscurrency", "stored_money.empty");

    public static final TextEntry COIN_CHAIN_MAIN = TextEntry.chain("main");

    public static final TextEntry COIN_CHAIN_CHOCOLATE = TextEntry.chain("chocolate_coins");

    public static final TextEntry COIN_CHAIN_CHOCOLATE_DISPLAY = TextEntry.chainDisplay("chocolate_coins");

    public static final TextEntry COIN_CHAIN_CHOCOLATE_DISPLAY_WORDY = TextEntry.chainDisplayWordy("chocolate_coins");

    public static final TextEntry COIN_CHAIN_EMERALDS = TextEntry.chain("emeralds");

    public static final TextEntry COIN_CHAIN_EMERALDS_DISPLAY = TextEntry.chainDisplay("emeralds");

    public static final TextEntry COIN_CHAIN_EMERALDS_DISPLAY_WORDY = TextEntry.chainDisplayWordy("emeralds");

    public static final TextEntry BUTTON_CHANGE_NAME_ICON = TextEntry.button("lightmanscurrency", "change_name");

    public static final TextEntry GUI_NAME = TextEntry.gui("lightmanscurrency", "customname");

    public static final TextEntry TOOLTIP_WARNING_CANT_BE_UNDONE = TextEntry.tooltip("lightmanscurrency", "warning.cannot_be_undone");

    public static final TextEntry TOOLTIP_ITEM_COUNT = TextEntry.tooltip("lightmanscurrency", "item.count");

    public static final TextEntry BUTTON_ADD = TextEntry.button("lightmanscurrency", "add");

    public static final TextEntry BUTTON_REMOVE = TextEntry.button("lightmanscurrency", "remove");

    public static final TextEntry BUTTON_SET = TextEntry.button("lightmanscurrency", "set");

    public static final TextEntry BUTTON_CLEAR_MEMORY = TextEntry.button("lightmanscurrency", "clear_memory");

    public static final TextEntry MISC_GENERIC_PLURAL = new TextEntry("item.lightmanscurrency.generic.plural");

    public static final TextEntry GUI_SEPERATOR = TextEntry.gui("lightmanscurrency", "trader.title.seperator");

    public static final TextEntry GUI_ADDED = TextEntry.gui("lightmanscurrency", "added");

    public static final TextEntry GUI_REMOVED = TextEntry.gui("lightmanscurrency", "removed");

    public static final TextEntry GUI_TO = TextEntry.gui("lightmanscurrency", "to");

    public static final TextEntry GUI_FROM = TextEntry.gui("lightmanscurrency", "from");

    public static final TextEntry GUI_AND = TextEntry.gui("lightmanscurrency", "and");

    public static final TimeUnitTextEntry TIME_UNIT_DAY = TimeUnitTextEntry.of("day");

    public static final TimeUnitTextEntry TIME_UNIT_HOUR = TimeUnitTextEntry.of("hour");

    public static final TimeUnitTextEntry TIME_UNIT_MINUTE = TimeUnitTextEntry.of("minute");

    public static final TimeUnitTextEntry TIME_UNIT_SECOND = TimeUnitTextEntry.of("second");

    public static final TimeUnitTextEntry TIME_UNIT_MILLISECOND = TimeUnitTextEntry.of("millisecond");

    public static final TimeUnitTextEntry TIME_UNIT_TICK = TimeUnitTextEntry.of("tick");

    public static final TextEntry NOTIFICATION_FORMAT_GENERAL = new TextEntry("notifications.format.general");

    public static final TextEntry NOTIFICATION_FORMAT_CHAT = new TextEntry("notifications.format.chat");

    public static final TextEntry NOTIFICATION_FORMAT_CHAT_TITLE = new TextEntry("notifications.format.chat.title");

    public static final TextEntry NOTIFICATION_TIMESTAMP = new TextEntry("notifications.timestamp");

    public static final TextEntry NOTIFICATION_SOURCE_GENERAL = new TextEntry("notifications.source.general");

    public static final TextEntry NOTIFICATION_AUCTION_BID = TextEntry.notification(AuctionHouseBidNotification.TYPE);

    public static final TextEntry NOTIFICATION_AUCTION_BUYER = TextEntry.notification(AuctionHouseBuyerNotification.TYPE);

    public static final TextEntry NOTIFICATION_AUCTION_CANCEL = TextEntry.notification(AuctionHouseCancelNotification.TYPE);

    public static final TextEntry NOTIFICATION_AUCTION_SELLER_NO_BID = TextEntry.notification(AuctionHouseSellerNobidNotification.TYPE);

    public static final TextEntry NOTIFICATION_AUCTION_SELLER = TextEntry.notification(AuctionHouseSellerNotification.TYPE);

    public static final TextEntry NOTIFICATION_BANK_INTEREST = TextEntry.notification(BankInterestNotification.TYPE);

    public static final TextEntry NOTIFICATION_BANK_TRANSFER = TextEntry.notification(BankTransferNotification.TYPE);

    public static final TextEntry NOTIFICATION_BANK_DEPOSIT_WITHDRAW = TextEntry.notification(new ResourceLocation("lightmanscurrency", "bank_deposit_or_withdraw"));

    public static final TextEntry NOTIFICATION_BANK_DEPOSIT = TextEntry.notification(new ResourceLocation("lightmanscurrency", "bank_deposit"));

    public static final TextEntry NOTIFICATION_BANK_WITHDRAW = TextEntry.notification(new ResourceLocation("lightmanscurrency", "bank_withdraw"));

    public static final TextEntry NOTIFICATION_BANK_DEPOSIT_WITHDRAW_SERVER = TextEntry.notification(new ResourceLocation("lightmanscurrency", "bank_deposit_or_withdraw"), "server");

    public static final TextEntry NOTIFICATION_BANK_LOW_BALANCE = TextEntry.notification(LowBalanceNotification.TYPE);

    public static final TextEntry NOTIFICATION_EJECTION_ANARCHY = TextEntry.notification(OwnableBlockEjectedNotification.TYPE, "anarchy");

    public static final TextEntry NOTIFICATION_EJECTION_EJECTED = TextEntry.notification(OwnableBlockEjectedNotification.TYPE, "ejected");

    public static final TextEntry NOTIFICATION_EJECTION_DROPPED = TextEntry.notification(OwnableBlockEjectedNotification.TYPE, "dropped");

    public static final TextEntry NOTIFICATION_SETTINGS_ADD_REMOVE_ALLY = TextEntry.notification(AddRemoveAllyNotification.TYPE);

    public static final TextEntry NOTIFICATION_SETTINGS_ADD_REMOVE_TRADE = TextEntry.notification(AddRemoveTradeNotification.TYPE);

    public static final TextEntry NOTIFICATION_SETTINGS_CHANGE_ALLY_PERMISSIONS = TextEntry.notification(ChangeAllyPermissionNotification.TYPE);

    public static final TextEntry NOTIFICATION_SETTINGS_CHANGE_ALLY_PERMISSIONS_SIMPLE = TextEntry.notification(ChangeAllyPermissionNotification.TYPE, "simple");

    public static final TextEntry NOTIFICATION_SETTINGS_CHANGE_CREATIVE = TextEntry.notification(ChangeCreativeNotification.TYPE);

    public static final TextEntry NOTIFICATION_SETTINGS_CHANGE_CREATIVE_ENABLED = TextEntry.notification(ChangeCreativeNotification.TYPE, "enabled");

    public static final TextEntry NOTIFICATION_SETTINGS_CHANGE_CREATIVE_DISABLED = TextEntry.notification(ChangeCreativeNotification.TYPE, "disabled");

    public static final TextEntry NOTIFICATION_SETTINGS_CHANGE_NAME = TextEntry.notification(ChangeNameNotification.TYPE);

    public static final TextEntry NOTIFICATION_SETTINGS_CHANGE_NAME_SET = TextEntry.notification(ChangeNameNotification.TYPE, "set");

    public static final TextEntry NOTIFICATION_SETTINGS_CHANGE_NAME_RESET = TextEntry.notification(ChangeNameNotification.TYPE, "reset");

    public static final TextEntry NOTIFICATION_SETTINGS_CHANGE_OWNER_PASSED = TextEntry.notification(ChangeOwnerNotification.TYPE, "passed");

    public static final TextEntry NOTIFICATION_SETTINGS_CHANGE_OWNER_TAKEN = TextEntry.notification(ChangeOwnerNotification.TYPE, "taken");

    public static final TextEntry NOTIFICATION_SETTINGS_CHANGE_OWNER_TRANSFERRED = TextEntry.notification(ChangeOwnerNotification.TYPE, "transferred");

    public static final TextEntry NOTIFICATION_SETTINGS_CHANGE_SIMPLE = TextEntry.notification(ChangeSettingNotification.SIMPLE_TYPE);

    public static final TextEntry NOTIFICATION_SETTINGS_CHANGE_ADVANCED = TextEntry.notification(ChangeSettingNotification.ADVANCED_TYPE);

    public static final TextEntry NOTIFICATION_TAXES_COLLECTED = TextEntry.notification(TaxesCollectedNotification.TYPE);

    public static final TextEntry NOTIFICATION_TAXES_PAID = TextEntry.notification(TaxesPaidNotification.TYPE);

    public static final TextEntry NOTIFICATION_TAXES_PAID_NULL = TextEntry.notification(TaxesPaidNotification.TYPE, "null");

    public static final TextEntry NOTIFICATION_TRADER_OUT_OF_STOCK = TextEntry.notification(OutOfStockNotification.TYPE);

    public static final TextEntry NOTIFICATION_TRADER_OUT_OF_STOCK_INDEXLESS = TextEntry.notification(OutOfStockNotification.TYPE, "indexless");

    public static final TextEntry NOTIFICATION_TRADE_ITEM = TextEntry.notification(ItemTradeNotification.TYPE);

    public static final TextEntry NOTIFICATION_TRADE_PAYGATE_TICKET = TextEntry.notification(PaygateNotification.TYPE, "ticket");

    public static final TextEntry NOTIFICATION_TRADE_PAYGATE_PASS = TextEntry.notification(PaygateNotification.TYPE, "pass");

    public static final TextEntry NOTIFICATION_TRADE_PAYGATE_MONEY = TextEntry.notification(PaygateNotification.TYPE, "money");

    public static final TextEntry NOTIFICATION_TRADE_SLOT_MACHINE = TextEntry.notification(SlotMachineTradeNotification.TYPE);

    public static final TextEntry NOTIFICATION_ITEM_FORMAT = TextEntry.notification(new ResourceLocation("lightmanscurrency", "items"), "format");

    public static final TextEntry ARGUMENT_MONEY_VALUE_NOT_A_COIN = TextEntry.argument("money_value.not_a_coin");

    public static final TextEntry ARGUMENT_MONEY_VALUE_NO_VALUE = TextEntry.argument("money_value.no_value");

    public static final TextEntry ARGUMENT_MONEY_VALUE_NOT_EMPTY_OR_FREE = TextEntry.argument("money_value.not_free_or_empty");

    public static final TextEntry ARGUMENT_COLOR_INVALID = TextEntry.argument("color.invalid");

    public static final TextEntry ARGUMENT_TRADEID_INVALID = TextEntry.argument("tradeid.invalid");

    public static final TextEntry ARGUMENT_TRADER_NOT_FOUND = TextEntry.argument("trader.not_found");

    public static final TextEntry COMMAND_BALTOP_NO_RESULTS = TextEntry.command("lightmanscurrency", "lcbaltop.no_results");

    public static final TextEntry COMMAND_BALTOP_ERROR_PAGE = TextEntry.command("lightmanscurrency", "lcbaltop.error.page");

    public static final TextEntry COMMAND_BALTOP_TITLE = TextEntry.command("lightmanscurrency", "lcbaltop.title");

    public static final TextEntry COMMAND_BALTOP_PAGE = TextEntry.command("lightmanscurrency", "lcbaltop.page");

    public static final TextEntry COMMAND_BALTOP_ENTRY = TextEntry.command("lightmanscurrency", "lcbaltop.entry");

    public static final TextEntry COMMAND_BANK_TEAM_NULL = TextEntry.command("lightmanscurrency", "lcbank.team.null");

    public static final TextEntry COMMAND_BANK_TEAM_NO_BANK = TextEntry.command("lightmanscurrency", "lcbank.team.no_bank");

    public static final TextEntry COMMAND_BANK_GIVE_FAIL = TextEntry.command("lightmanscurrency", "lcbank.give.fail");

    public static final TextEntry COMMAND_BANK_GIVE_SUCCESS = TextEntry.command("lightmanscurrency", "lcbank.give.success");

    public static final TextEntry COMMAND_BANK_GIVE_SUCCESS_SINGLE = TextEntry.command("lightmanscurrency", "lcbank.give.success.single");

    public static final TextEntry COMMAND_BANK_TAKE_FAIL = TextEntry.command("lightmanscurrency", "lcbank.take.fail");

    public static final TextEntry COMMAND_BANK_TAKE_SUCCESS = TextEntry.command("lightmanscurrency", "lcbank.take.success");

    public static final TextEntry COMMAND_BANK_TAKE_SUCCESS_SINGLE = TextEntry.command("lightmanscurrency", "lcbank.take.success.single");

    public static final TextEntry COMMAND_CONFIG_RELOAD = TextEntry.command("lightmanscurrency", "lcconfig.reload");

    public static final TextEntry COMMAND_CONFIG_EDIT_SUCCESS = TextEntry.command("lightmanscurrency", "lcconfig.edit.success");

    public static final TextEntry COMMAND_CONFIG_EDIT_FAIL_PARSE = TextEntry.command("lightmanscurrency", "lcconfig.edit.fail.parse");

    public static final TextEntry COMMAND_CONFIG_EDIT_LIST_REMOVE_SUCCESS = TextEntry.command("lightmanscurrency", "lcconfig.edit.list.remove.success");

    public static final TextEntry COMMAND_CONFIG_VIEW = TextEntry.command("lightmanscurrency", "lcconfig.view");

    public static final TextEntry COMMAND_CONFIG_FAIL_MISSING = TextEntry.command("lightmanscurrency", "lcconfig.fail.missing");

    public static final TextEntry COMMAND_ADMIN_TOGGLE_ADMIN = TextEntry.command("lightmanscurrency", "lcadmin.toggleadmin");

    public static final TextEntry COMMAND_ADMIN_TOGGLE_ADMIN_ENABLED = TextEntry.command("lightmanscurrency", "lcadmin.toggleadmin.enabled");

    public static final TextEntry COMMAND_ADMIN_TOGGLE_ADMIN_DISABLED = TextEntry.command("lightmanscurrency", "lcadmin.toggleadmin.disabled");

    public static final TextEntry COMMAND_ADMIN_PREPARE_FOR_STRUCTURE_ERROR = TextEntry.command("lightmanscurrency", "lcadmin.prepareForStructure.error");

    public static final TextEntry COMMAND_ADMIN_PREPARE_FOR_STRUCTURE_SUCCESS = TextEntry.command("lightmanscurrency", "lcadmin.prepareForStructure.success");

    public static final TextEntry COMMAND_ADMIN_TRADERDATA_LIST_TITLE = TextEntry.command("lightmanscurrency", "lcadmin.traderdata.list.title");

    public static final TextEntry COMMAND_ADMIN_TRADERDATA_LIST_NONE = TextEntry.command("lightmanscurrency", "lcadmin.traderdata.list.none");

    public static final TextEntry COMMAND_ADMIN_TRADERDATA_LIST_TRADER_ID = TextEntry.command("lightmanscurrency", "lcadmin.traderdata.list.trader_id");

    public static final TextEntry COMMAND_ADMIN_TRADERDATA_LIST_TRADER_ID_TOOLTIP = TextEntry.command("lightmanscurrency", "lcadmin.traderdata.list.trader_id.tooltip");

    public static final TextEntry COMMAND_ADMIN_TRADERDATA_LIST_PERSISTENT_ID = TextEntry.command("lightmanscurrency", "lcadmin.traderdata.list.persistent_id");

    public static final TextEntry COMMAND_ADMIN_TRADERDATA_LIST_TYPE = TextEntry.command("lightmanscurrency", "lcadmin.traderdata.list.type");

    public static final TextEntry COMMAND_ADMIN_TRADERDATA_LIST_DIMENSION = TextEntry.command("lightmanscurrency", "lcadmin.traderdata.list.dimension");

    public static final TextEntry COMMAND_ADMIN_TRADERDATA_LIST_POSITION = TextEntry.command("lightmanscurrency", "lcadmin.traderdata.list.position");

    public static final TextEntry COMMAND_ADMIN_TRADERDATA_LIST_POSITION_TOOLTIP = TextEntry.command("lightmanscurrency", "lcadmin.traderdata.list.position.tooltip");

    public static final TextEntry COMMAND_ADMIN_TRADERDATA_LIST_NAME = TextEntry.command("lightmanscurrency", "lcadmin.traderdata.list.name");

    public static final TextEntry COMMAND_ADMIN_TRADERDATA_SEARCH_NONE = TextEntry.command("lightmanscurrency", "lcadmin.traderdata.search.none");

    public static final TextEntry COMMAND_ADMIN_TRADERDATA_DELETE_SUCCESS = TextEntry.command("lightmanscurrency", "lcadmin.traderdata.delete.success");

    public static final TextEntry COMMAND_ADMIN_TRADERDATA_ADD_TO_WHITELIST_SUCCESS = TextEntry.command("lightmanscurrency", "lcadmin.traderdata.addToWhitelist.success");

    public static final TextEntry COMMAND_ADMIN_TRADERDATA_ADD_TO_WHITELIST_MISSING = TextEntry.command("lightmanscurrency", "lcadmin.traderdata.addToWhitelist.missing");

    public static final TextEntry COMMAND_ADMIN_REPLACE_WALLET_NOT_A_WALLET = TextEntry.command("lightmanscurrency", "lcadmin.replaceWallet.not_a_wallet");

    public static final TextEntry COMMAND_ADMIN_TAXES_OPEN_SERVER_TAX_ERROR = TextEntry.command("lightmanscurrency", "lcadmin.taxes.openServerTax.error");

    public static final TextEntry COMMAND_ADMIN_TAXES_LIST_TITLE = TextEntry.command("lightmanscurrency", "lcadmin.taxes.list.title");

    public static final TextEntry COMMAND_ADMIN_TAXES_LIST_ID = TextEntry.command("lightmanscurrency", "lcadmin.taxes.list.id");

    public static final TextEntry COMMAND_ADMIN_TAXES_LIST_ID_TOOLTIP = TextEntry.command("lightmanscurrency", "lcadmin.taxes.list.id.tooltip");

    public static final TextEntry COMMAND_ADMIN_TAXES_LIST_DIMENSION = TextEntry.command("lightmanscurrency", "lcadmin.taxes.list.dimension");

    public static final TextEntry COMMAND_ADMIN_TAXES_LIST_POSITION = TextEntry.command("lightmanscurrency", "lcadmin.taxes.list.position");

    public static final TextEntry COMMAND_ADMIN_TAXES_LIST_POSITION_TOOLTIP = TextEntry.command("lightmanscurrency", "lcadmin.taxes.list.position.tooltip");

    public static final TextEntry COMMAND_ADMIN_TAXES_LIST_INFINITE_RANGE = TextEntry.command("lightmanscurrency", "lcadmin.taxes.list.infinite_range");

    public static final TextEntry COMMAND_ADMIN_TAXES_LIST_RADIUS = TextEntry.command("lightmanscurrency", "lcadmin.taxes.list.radius");

    public static final TextEntry COMMAND_ADMIN_TAXES_LIST_HEIGHT = TextEntry.command("lightmanscurrency", "lcadmin.taxes.list.height");

    public static final TextEntry COMMAND_ADMIN_TAXES_LIST_OFFSET = TextEntry.command("lightmanscurrency", "lcadmin.taxes.list.offset");

    public static final TextEntry COMMAND_ADMIN_TAXES_LIST_FORCE_ACCEPTANCE = TextEntry.command("lightmanscurrency", "lcadmin.taxes.list.force_acceptance");

    public static final TextEntry COMMAND_ADMIN_TAXES_LIST_NAME = TextEntry.command("lightmanscurrency", "lcadmin.taxes.list.name");

    public static final TextEntry COMMAND_ADMIN_TAXES_DELETE_SUCCESS = TextEntry.command("lightmanscurrency", "lcadmin.taxes.delete.success");

    public static final TextEntry COMMAND_ADMIN_TAXES_DELETE_FAIL = TextEntry.command("lightmanscurrency", "lcadmin.taxes.delete.fail");

    public static final TextEntry COMMAND_ADMIN_TAXES_FORCE_DISABLE_SUCCESS = TextEntry.command("lightmanscurrency", "lcadmin.taxes.forceDisableTaxCollectors.success");

    public static final TextEntry COMMAND_ADMIN_TAXES_FORCE_DISABLE_FAIL = TextEntry.command("lightmanscurrency", "lcadmin.taxes.forceDisableTaxCollectors.fail");

    public static final TextEntry COMMAND_ADMIN_EVENT_LIST_NONE = TextEntry.command("lightmanscurrency", "lcadmin.event.list.none");

    public static final TextEntry COMMAND_ADMIN_EVENT_UNLOCK_SUCCESS = TextEntry.command("lightmanscurrency", "lcadmin.event.unlock.success");

    public static final TextEntry COMMAND_ADMIN_EVENT_UNLOCK_FAIL = TextEntry.command("lightmanscurrency", "lcadmin.event.unlock.fail");

    public static final TextEntry COMMAND_ADMIN_EVENT_LOCK_SUCCESS = TextEntry.command("lightmanscurrency", "lcadmin.event.lock.success");

    public static final TextEntry COMMAND_ADMIN_EVENT_LOCK_FAIL = TextEntry.command("lightmanscurrency", "lcadmin.event.lock.fail");

    public static final TextEntry COMMAND_LCADMIN_DATA_OWNER_PLAYER = TextEntry.command("lightmanscurrency", "lcadmin.data.list.owner.player");

    public static final TextEntry COMMAND_LCADMIN_DATA_OWNER_TEAM = TextEntry.command("lightmanscurrency", "lcadmin.data.list.owner.team");

    public static final TextEntry COMMAND_LCADMIN_DATA_OWNER_CUSTOM = TextEntry.command("lightmanscurrency", "lcadmin.data.list.owner.custom");

    public static final TextEntry COMMAND_TRADE_SELF = TextEntry.command("lightmanscurrency", "lctrade.self");

    public static final TextEntry COMMAND_TRADE_HOST_NOTIFY = TextEntry.command("lightmanscurrency", "lctrade.host.notify");

    public static final TextEntry COMMAND_TRADE_GUEST_NOTIFY = TextEntry.command("lightmanscurrency", "lctrade.guest.notify");

    public static final TextEntry COMMAND_TRADE_GUEST_NOTIFY_PROMPT = TextEntry.command("lightmanscurrency", "lctrade.guest.notify.prompt");

    public static final TextEntry COMMAND_TRADE_ACCEPT_FAIL_OFFLINE = TextEntry.command("lightmanscurrency", "lctradeaccept.fail.offline");

    public static final TextEntry COMMAND_TRADE_ACCEPT_FAIL_DISTANCE = TextEntry.command("lightmanscurrency", "lctradeaccept.fail.distance");

    public static final TextEntry COMMAND_TRADE_ACCEPT_FAIL_DIMENSION = TextEntry.command("lightmanscurrency", "lctradeaccept.fail.dimension");

    public static final TextEntry COMMAND_TRADE_ACCEPT_ERROR = TextEntry.command("lightmanscurrency", "lctradeaccept.error");

    public static final TextEntry COMMAND_TRADE_ACCEPT_NOT_FOUND = TextEntry.command("lightmanscurrency", "lctradeaccept.not_found");

    public static final TextEntry COMMAND_TICKETS_COLOR_NOT_HELD = TextEntry.command("lightmanscurrency", "ticket.color.not_held");

    public static final TextEntry COMMAND_CLAIM_FAIL_NO_DATA = TextEntry.command("lightmanscurrency", "lcclaims.fail.no_data");

    public static final TextEntry COMMAND_CLAIM_FAIL_INVALID_PRICE = TextEntry.command("lightmanscurrency", "lcclaims.fail.invalid_price");

    public static final TextEntry COMMAND_CLAIM_INVALID = TextEntry.command("lightmanscurrency", "lcclaims.invalid");

    public static final TextEntry COMMAND_CLAIM_INFO_CLAIMS = TextEntry.command("lightmanscurrency", "lcclaims.info.claims");

    public static final TextEntry COMMAND_CLAIM_INFO_FORCELOAD = TextEntry.command("lightmanscurrency", "lcclaims.info.forceload");

    public static final TextEntry COMMAND_CLAIM_INFO_PRICE = TextEntry.command("lightmanscurrency", "lcclaims.info.price");

    public static final TextEntry COMMAND_CLAIM_INFO_DISABLED = TextEntry.command("lightmanscurrency", "lcclaims.info.disabled");

    public static final TextEntry COMMAND_CLAIM_BUY_CLAIM_DISABLED = TextEntry.command("lightmanscurrency", "lcclaims.buy.claim.disabled");

    public static final TextEntry COMMAND_CLAIM_BUY_CLAIM_LIMIT_REACHED = TextEntry.command("lightmanscurrency", "lcclaims.buy.claim.limit_reached");

    public static final TextEntry COMMAND_CLAIM_BUY_CLAIM_SUCCESS = TextEntry.command("lightmanscurrency", "lcclaims.buy.claim.success");

    public static final TextEntry COMMAND_CLAIM_BUY_CLAIM_CANNOT_AFFORD = TextEntry.command("lightmanscurrency", "lcclaims.buy.claim.cannot_afford");

    public static final TextEntry COMMAND_CLAIM_BUY_FORCELOAD_DISABLED = TextEntry.command("lightmanscurrency", "lcclaims.buy.forceload.disabled");

    public static final TextEntry COMMAND_CLAIM_BUY_FORCELOAD_LIMIT_REACHED = TextEntry.command("lightmanscurrency", "lcclaims.buy.forceload.limit_reached");

    public static final TextEntry COMMAND_CLAIM_BUY_FORCELOAD_SUCCESS = TextEntry.command("lightmanscurrency", "lcclaims.buy.forceload.success");

    public static final TextEntry COMMAND_CLAIM_BUY_FORCELOAD_CANNOT_AFFORD = TextEntry.command("lightmanscurrency", "lcclaims.buy.forceload.cannot_afford");

    public static final AdvancementTextEntry ADVANCEMENT_ROOT = AdvancementTextEntry.of("lightmanscurrency.root");

    public static final AdvancementTextEntry ADVANCEMENT_MY_FIRST_PENNY = AdvancementTextEntry.of("lightmanscurrency.myfirstpenny");

    public static final AdvancementTextEntry ADVANCEMENT_TRADING_CORE = AdvancementTextEntry.of("lightmanscurrency.trading_core");

    public static final AdvancementTextEntry ADVANCEMENT_TRADER = AdvancementTextEntry.of("lightmanscurrency.trader");

    public static final AdvancementTextEntry ADVANCEMENT_SPECIALTY_TRADER = AdvancementTextEntry.of("lightmanscurrency.specialty_trader");

    public static final AdvancementTextEntry ADVANCEMENT_NETWORK_TRADER = AdvancementTextEntry.of("lightmanscurrency.network_trader");

    public static final AdvancementTextEntry ADVANCEMENT_TRADER_INTERFACE = AdvancementTextEntry.of("lightmanscurrency.trader_interface");

    public static final AdvancementTextEntry ADVANCEMENT_TERMINAL = AdvancementTextEntry.of("lightmanscurrency.terminal");

    public static final AdvancementTextEntry ADVANCEMENT_ATM = AdvancementTextEntry.of("lightmanscurrency.atm");

    public static final AdvancementTextEntry ADVANCEMENT_BANKER_TRADE = AdvancementTextEntry.of("lightmanscurrency.banker_trade");

    public static final AdvancementTextEntry ADVANCEMENT_COIN_MINT = AdvancementTextEntry.of("lightmanscurrency.coin_mint");

    public static final AdvancementTextEntry ADVANCEMENT_WALLET_CRAFTING = AdvancementTextEntry.of("lightmanscurrency.wallet_crafting");

    public static final AdvancementTextEntry ADVANCEMENT_NETHERITE_WALLET = AdvancementTextEntry.of("lightmanscurrency.netherite_wallet");

    public static final AdvancementTextEntry ADVANCEMENT_ENCHANTED_WALLET = AdvancementTextEntry.of("lightmanscurrency.enchanted_wallet");

    public static final AdvancementTextEntry ADVANCEMENT_CASH_REGISTER = AdvancementTextEntry.of("lightmanscurrency.cash_register");

    public static final AdvancementTextEntry ADVANCEMENT_CASHIER_TRADE = AdvancementTextEntry.of("lightmanscurrency.cashier_trade");

    public static final AdvancementTextEntry ADVANCEMENT_JAR_OF_SUS = AdvancementTextEntry.of("lightmanscurrency.jar_of_sus");

    public static final AdvancementTextEntry ADVANCEMENT_EVENT_CHOCOLATE = AdvancementTextEntry.of("lightmanscurrency.event.chocolate");

    public static final AdvancementTextEntry ADVANCEMENT_EVENT_CHRISTMAS = AdvancementTextEntry.of("lightmanscurrency.event.christmas");

    public static final AdvancementTextEntry ADVANCEMENT_EVENT_VALENTINES = AdvancementTextEntry.of("lightmanscurrency.event.valentines");

    public static final TextEntry RESOURCE_PACK_RUPEES = TextEntry.resourcePack("lightmanscurrency", "rupees");

    public static final TextEntry RESOURCE_PACK_CLOSER_ITEMS = TextEntry.resourcePack("lightmanscurrency", "closer_items");

    public static final TextEntry RESOURCE_PACK_LEGACY_COINS = TextEntry.resourcePack("lightmanscurrency", "legacy_coins");

    public static final TextEntry JEI_INFO_TICKET_STUB = TextEntry.jeiInfo("lightmanscurrency", "ticket_stub");

    public static final TextEntry CURIOS_SLOT_WALLET = TextEntry.curiosSlot("wallet");
}