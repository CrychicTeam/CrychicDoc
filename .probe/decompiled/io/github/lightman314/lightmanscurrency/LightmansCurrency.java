package io.github.lightman314.lightmanscurrency;

import io.github.lightman314.lightmanscurrency.api.capability.money.IMoneyHandler;
import io.github.lightman314.lightmanscurrency.api.config.ConfigFile;
import io.github.lightman314.lightmanscurrency.api.money.MoneyAPI;
import io.github.lightman314.lightmanscurrency.api.money.bank.BankAPI;
import io.github.lightman314.lightmanscurrency.api.money.bank.reference.builtin.PlayerBankReference;
import io.github.lightman314.lightmanscurrency.api.money.bank.reference.builtin.TeamBankReference;
import io.github.lightman314.lightmanscurrency.api.money.coins.CoinAPI;
import io.github.lightman314.lightmanscurrency.api.money.types.builtin.CoinCurrencyType;
import io.github.lightman314.lightmanscurrency.api.money.types.builtin.NullCurrencyType;
import io.github.lightman314.lightmanscurrency.api.money.value.holder.IMoneyViewer;
import io.github.lightman314.lightmanscurrency.api.notifications.NotificationAPI;
import io.github.lightman314.lightmanscurrency.api.notifications.NotificationCategory;
import io.github.lightman314.lightmanscurrency.api.ownership.Owner;
import io.github.lightman314.lightmanscurrency.api.ownership.OwnershipAPI;
import io.github.lightman314.lightmanscurrency.api.ownership.builtin.FakeOwner;
import io.github.lightman314.lightmanscurrency.api.ownership.builtin.PlayerOwner;
import io.github.lightman314.lightmanscurrency.api.ownership.builtin.TeamOwner;
import io.github.lightman314.lightmanscurrency.api.ownership.listing.builtin.PlayerOwnerProvider;
import io.github.lightman314.lightmanscurrency.api.ownership.listing.builtin.TeamOwnerProvider;
import io.github.lightman314.lightmanscurrency.api.stats.StatType;
import io.github.lightman314.lightmanscurrency.api.stats.types.IntegerStat;
import io.github.lightman314.lightmanscurrency.api.stats.types.MultiMoneyStat;
import io.github.lightman314.lightmanscurrency.api.taxes.TaxAPI;
import io.github.lightman314.lightmanscurrency.api.taxes.reference.builtin.TaxableTraderReference;
import io.github.lightman314.lightmanscurrency.api.ticket.TicketData;
import io.github.lightman314.lightmanscurrency.api.traders.TraderAPI;
import io.github.lightman314.lightmanscurrency.common.advancements.LCAdvancementTriggers;
import io.github.lightman314.lightmanscurrency.common.capability.wallet.IWalletHandler;
import io.github.lightman314.lightmanscurrency.common.core.ModItems;
import io.github.lightman314.lightmanscurrency.common.core.ModRegistries;
import io.github.lightman314.lightmanscurrency.common.crafting.condition.LCCraftingConditions;
import io.github.lightman314.lightmanscurrency.common.event_coins.ChocolateEventCoins;
import io.github.lightman314.lightmanscurrency.common.gamerule.ModGameRules;
import io.github.lightman314.lightmanscurrency.common.loot.LootManager;
import io.github.lightman314.lightmanscurrency.common.menus.validation.MenuValidatorType;
import io.github.lightman314.lightmanscurrency.common.menus.validation.types.BlockEntityValidator;
import io.github.lightman314.lightmanscurrency.common.menus.validation.types.BlockValidator;
import io.github.lightman314.lightmanscurrency.common.menus.validation.types.SimpleValidator;
import io.github.lightman314.lightmanscurrency.common.notifications.categories.AuctionHouseCategory;
import io.github.lightman314.lightmanscurrency.common.notifications.categories.BankCategory;
import io.github.lightman314.lightmanscurrency.common.notifications.categories.NullCategory;
import io.github.lightman314.lightmanscurrency.common.notifications.categories.TaxEntryCategory;
import io.github.lightman314.lightmanscurrency.common.notifications.categories.TraderCategory;
import io.github.lightman314.lightmanscurrency.common.notifications.types.TextNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.types.auction.AuctionHouseBidNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.types.auction.AuctionHouseBuyerNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.types.auction.AuctionHouseCancelNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.types.auction.AuctionHouseSellerNobidNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.types.auction.AuctionHouseSellerNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.types.bank.BankInterestNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.types.bank.BankTransferNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.types.bank.DepositWithdrawNotification;
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
import io.github.lightman314.lightmanscurrency.common.player.LCAdminMode;
import io.github.lightman314.lightmanscurrency.common.traders.auction.AuctionHouseTrader;
import io.github.lightman314.lightmanscurrency.common.traders.item.ItemTraderData;
import io.github.lightman314.lightmanscurrency.common.traders.item.ItemTraderDataArmor;
import io.github.lightman314.lightmanscurrency.common.traders.item.ItemTraderDataBook;
import io.github.lightman314.lightmanscurrency.common.traders.item.ItemTraderDataTicket;
import io.github.lightman314.lightmanscurrency.common.traders.item.tradedata.restrictions.ItemTradeRestriction;
import io.github.lightman314.lightmanscurrency.common.traders.paygate.PaygateTraderData;
import io.github.lightman314.lightmanscurrency.common.traders.rules.TradeRule;
import io.github.lightman314.lightmanscurrency.common.traders.rules.types.FreeSample;
import io.github.lightman314.lightmanscurrency.common.traders.rules.types.PlayerDiscounts;
import io.github.lightman314.lightmanscurrency.common.traders.rules.types.PlayerListing;
import io.github.lightman314.lightmanscurrency.common.traders.rules.types.PlayerTradeLimit;
import io.github.lightman314.lightmanscurrency.common.traders.rules.types.PriceFluctuation;
import io.github.lightman314.lightmanscurrency.common.traders.rules.types.TimedSale;
import io.github.lightman314.lightmanscurrency.common.traders.rules.types.TradeLimit;
import io.github.lightman314.lightmanscurrency.common.traders.slot_machine.SlotMachineTraderData;
import io.github.lightman314.lightmanscurrency.common.traders.terminal.filters.BasicSearchFilter;
import io.github.lightman314.lightmanscurrency.common.traders.terminal.filters.ItemTraderSearchFilter;
import io.github.lightman314.lightmanscurrency.common.villager_merchant.ItemListingSerializer;
import io.github.lightman314.lightmanscurrency.common.villager_merchant.VillagerTradeManager;
import io.github.lightman314.lightmanscurrency.integration.IntegrationUtil;
import io.github.lightman314.lightmanscurrency.integration.biomesoplenty.BOPCustomWoodTypes;
import io.github.lightman314.lightmanscurrency.integration.claiming.cadmus.LCCadmusIntegration;
import io.github.lightman314.lightmanscurrency.integration.claiming.flan.LCFlanIntegration;
import io.github.lightman314.lightmanscurrency.integration.claiming.ftbchunks.LCFTBChunksIntegration;
import io.github.lightman314.lightmanscurrency.integration.curios.LCCurios;
import io.github.lightman314.lightmanscurrency.integration.discord.LCDiscord;
import io.github.lightman314.lightmanscurrency.integration.immersiveengineering.LCImmersive;
import io.github.lightman314.lightmanscurrency.integration.quark.QuarkCustomWoodTypes;
import io.github.lightman314.lightmanscurrency.integration.supplementaries.LCSupplementaries;
import io.github.lightman314.lightmanscurrency.network.LightmansCurrencyPacketHandler;
import io.github.lightman314.lightmanscurrency.network.message.time.SPacketSyncTime;
import io.github.lightman314.lightmanscurrency.proxy.ClientProxy;
import io.github.lightman314.lightmanscurrency.proxy.CommonProxy;
import java.util.function.Consumer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("lightmanscurrency")
public class LightmansCurrency {

    public static final String MODID = "lightmanscurrency";

    public static final CommonProxy PROXY = (CommonProxy) DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    private static final Logger LOGGER = LogManager.getLogger();

    public static boolean isCuriosLoaded() {
        return ModList.get().isLoaded("curios");
    }

    public static boolean isCuriosValid(LivingEntity player) {
        try {
            if (isCuriosLoaded()) {
                return LCCurios.hasWalletSlot(player);
            }
        } catch (Throwable var2) {
        }
        return false;
    }

    public LightmansCurrency() {
        LootManager.registerDroplistListeners();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerCapabilities);
        LCConfig.init();
        LootManager.init();
        MinecraftForge.EVENT_BUS.register(this);
        IntegrationUtil.SafeRunIfLoaded("biomesoplenty", BOPCustomWoodTypes::setupWoodTypes, "Error setting up BOP wood types! BOP has probably changed their API!");
        IntegrationUtil.SafeRunIfLoaded("quark", QuarkCustomWoodTypes::setupWoodTypes, "Error setting up Quark wood types! Quark has probably changed their API!");
        ModRegistries.register(FMLJavaModLoadingContext.get().getModEventBus());
        MinecraftForge.EVENT_BUS.register(PROXY);
        IntegrationUtil.SafeRunIfLoaded("lightmansdiscord", LCDiscord::setup, null);
        IntegrationUtil.SafeRunIfLoaded("ftbchunks", LCFTBChunksIntegration::setup, "Error setting up FTB Chunks chunk purchasing integration!");
        IntegrationUtil.SafeRunIfLoaded("flan", LCFlanIntegration::setup, "Error setting up Flans chunk purchasing integration!");
        IntegrationUtil.SafeRunIfLoaded("immersiveengineering", LCImmersive::registerRotationBlacklists, null);
        IntegrationUtil.SafeRunIfLoaded("supplementaries", LCSupplementaries::setup, null);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        safeEnqueueWork(event, "Error during common setup!", this::commonSetupWork);
    }

    private void commonSetupWork(FMLCommonSetupEvent event) {
        ConfigFile.loadServerFiles(ConfigFile.LoadPhase.SETUP);
        IntegrationUtil.SafeRunIfLoaded("cadmus", LCCadmusIntegration::setup, null);
        CoinAPI.API.Setup();
        MoneyAPI.API.RegisterCurrencyType(CoinCurrencyType.INSTANCE);
        MoneyAPI.API.RegisterCurrencyType(NullCurrencyType.INSTANCE);
        LightmansCurrencyPacketHandler.init();
        LCCraftingConditions.register();
        OwnershipAPI.API.registerOwnerType(Owner.NULL_TYPE);
        OwnershipAPI.API.registerOwnerType(FakeOwner.TYPE);
        OwnershipAPI.API.registerOwnerType(PlayerOwner.TYPE);
        OwnershipAPI.API.registerOwnerType(TeamOwner.TYPE);
        OwnershipAPI.API.registerPotentialOwnerProvider(PlayerOwnerProvider.INSTANCE);
        OwnershipAPI.API.registerPotentialOwnerProvider(TeamOwnerProvider.INSTANCE);
        TraderAPI.registerTrader(ItemTraderData.TYPE);
        TraderAPI.registerTrader(ItemTraderDataArmor.TYPE);
        TraderAPI.registerTrader(ItemTraderDataTicket.TYPE);
        TraderAPI.registerTrader(ItemTraderDataBook.TYPE);
        TraderAPI.registerTrader(SlotMachineTraderData.TYPE);
        TraderAPI.registerTrader(PaygateTraderData.TYPE);
        TraderAPI.registerTrader(AuctionHouseTrader.TYPE);
        ModGameRules.registerRules();
        TraderAPI.registerTradeRule(PlayerListing.TYPE);
        TraderAPI.registerTradeRule(PlayerTradeLimit.TYPE);
        TraderAPI.registerTradeRule(PlayerDiscounts.TYPE);
        TraderAPI.registerTradeRule(TimedSale.TYPE);
        TraderAPI.registerTradeRule(TradeLimit.TYPE);
        TraderAPI.registerTradeRule(FreeSample.TYPE);
        TraderAPI.registerTradeRule(PriceFluctuation.TYPE);
        TradeRule.addLoadListener(PlayerListing.LISTENER);
        TradeRule.addIgnoreMissing("lightmanscurrency:whitelist");
        TradeRule.addIgnoreMissing("lightmanscurrency:blacklist");
        NotificationAPI.registerNotification(ItemTradeNotification.TYPE);
        NotificationAPI.registerNotification(PaygateNotification.TYPE);
        NotificationAPI.registerNotification(SlotMachineTradeNotification.TYPE);
        NotificationAPI.registerNotification(OutOfStockNotification.TYPE);
        NotificationAPI.registerNotification(LowBalanceNotification.TYPE);
        NotificationAPI.registerNotification(AuctionHouseSellerNotification.TYPE);
        NotificationAPI.registerNotification(AuctionHouseBuyerNotification.TYPE);
        NotificationAPI.registerNotification(AuctionHouseSellerNobidNotification.TYPE);
        NotificationAPI.registerNotification(AuctionHouseBidNotification.TYPE);
        NotificationAPI.registerNotification(AuctionHouseCancelNotification.TYPE);
        NotificationAPI.registerNotification(TextNotification.TYPE);
        NotificationAPI.registerNotification(AddRemoveAllyNotification.TYPE);
        NotificationAPI.registerNotification(AddRemoveTradeNotification.TYPE);
        NotificationAPI.registerNotification(ChangeAllyPermissionNotification.TYPE);
        NotificationAPI.registerNotification(ChangeCreativeNotification.TYPE);
        NotificationAPI.registerNotification(ChangeNameNotification.TYPE);
        NotificationAPI.registerNotification(ChangeOwnerNotification.TYPE);
        NotificationAPI.registerNotification(ChangeSettingNotification.SIMPLE_TYPE);
        NotificationAPI.registerNotification(ChangeSettingNotification.ADVANCED_TYPE);
        NotificationAPI.registerNotification(DepositWithdrawNotification.PLAYER_TYPE);
        NotificationAPI.registerNotification(DepositWithdrawNotification.TRADER_TYPE);
        NotificationAPI.registerNotification(DepositWithdrawNotification.SERVER_TYPE);
        NotificationAPI.registerNotification(BankTransferNotification.TYPE);
        NotificationAPI.registerNotification(BankInterestNotification.TYPE);
        NotificationAPI.registerNotification(TaxesCollectedNotification.TYPE);
        NotificationAPI.registerNotification(TaxesPaidNotification.TYPE);
        NotificationAPI.registerNotification(OwnableBlockEjectedNotification.TYPE);
        NotificationAPI.registerCategory(NotificationCategory.GENERAL_TYPE);
        NotificationAPI.registerCategory(NullCategory.TYPE);
        NotificationAPI.registerCategory(TraderCategory.TYPE);
        NotificationAPI.registerCategory(BankCategory.TYPE);
        NotificationAPI.registerCategory(AuctionHouseCategory.TYPE);
        NotificationAPI.registerCategory(TaxEntryCategory.TYPE);
        TraderAPI.registerSearchFilter(new BasicSearchFilter());
        TraderAPI.registerSearchFilter(new ItemTraderSearchFilter());
        TaxAPI.registerReferenceType(TaxableTraderReference.TYPE);
        BankAPI.API.RegisterReferenceType(PlayerBankReference.TYPE);
        BankAPI.API.RegisterReferenceType(TeamBankReference.TYPE);
        MenuValidatorType.register(SimpleValidator.TYPE);
        MenuValidatorType.register(BlockEntityValidator.TYPE);
        MenuValidatorType.register(BlockValidator.TYPE);
        ItemTradeRestriction.init();
        TicketData.create(ModItems.TICKET_MASTER.get(), ModItems.TICKET.get(), ModItems.TICKET_STUB.get(), LCTags.Items.TICKET_MATERIAL_PAPER);
        TicketData.create(ModItems.GOLDEN_TICKET_MASTER.get(), ModItems.GOLDEN_TICKET.get(), ModItems.GOLDEN_TICKET_STUB.get(), LCTags.Items.TICKET_MATERIAL_GOLD);
        VillagerTradeManager.registerDefaultTrades();
        ItemListingSerializer.registerDefaultSerializers();
        LootManager.addLootModifier(ChocolateEventCoins.LOOT_MODIFIER);
        StatType.register(IntegerStat.INSTANCE);
        StatType.register(MultiMoneyStat.INSTANCE);
        LCAdvancementTriggers.setup();
    }

    private void clientSetup(FMLClientSetupEvent event) {
        safeEnqueueWork(event, "Error during client setup!", PROXY::setupClient);
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IWalletHandler.class);
        event.register(IMoneyHandler.class);
        event.register(IMoneyViewer.class);
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        PacketDistributor.PacketTarget target = LightmansCurrencyPacketHandler.getTarget(event.getEntity());
        SPacketSyncTime.syncWith(target);
        LCAdminMode.sendSyncPacket(target);
    }

    @Deprecated(since = "2.2.0.0")
    public static ItemStack getWalletStack(Player player) {
        return player == null ? ItemStack.EMPTY : CoinAPI.API.getEquippedWallet(player);
    }

    public static void LogDebug(String message) {
        LOGGER.debug(message);
    }

    public static void LogDebug(String message, Object... objects) {
        LOGGER.debug(message, objects);
    }

    public static void LogInfo(String message) {
        if (LCConfig.COMMON.debugLevel.get() > 0) {
            LOGGER.debug("INFO: " + message);
        } else {
            LOGGER.info(message);
        }
    }

    public static void LogInfo(String message, Object... objects) {
        if (LCConfig.COMMON.debugLevel.get() > 0) {
            LOGGER.debug("INFO: " + message, objects);
        } else {
            LOGGER.info(message, objects);
        }
    }

    public static void LogWarning(String message) {
        if (LCConfig.COMMON.debugLevel.get() > 1) {
            LOGGER.debug("WARN: " + message);
        } else {
            LOGGER.warn(message);
        }
    }

    public static void LogWarning(String message, Object... objects) {
        if (LCConfig.COMMON.debugLevel.get() > 1) {
            LOGGER.debug("WARN: " + message, objects);
        } else {
            LOGGER.warn(message, objects);
        }
    }

    public static void LogError(String message) {
        if (LCConfig.COMMON.debugLevel.get() > 2) {
            LOGGER.debug("ERROR: " + message);
        } else {
            LOGGER.error(message);
        }
    }

    public static void LogError(String message, Object... objects) {
        if (LCConfig.COMMON.debugLevel.get() > 2) {
            LOGGER.debug("ERROR: " + message, objects);
        } else {
            LOGGER.error(message, objects);
        }
    }

    public static void safeEnqueueWork(ParallelDispatchEvent event, String errorMessage, Runnable work) {
        event.enqueueWork((Runnable) (() -> {
            try {
                work.run();
            } catch (Throwable var3) {
                LogError(errorMessage, var3);
            }
        }));
    }

    public static <T extends ParallelDispatchEvent> void safeEnqueueWork(T event, String errorMessage, Consumer<T> work) {
        event.enqueueWork((Runnable) (() -> {
            try {
                work.accept(event);
            } catch (Throwable var4) {
                LogError(errorMessage, var4);
            }
        }));
    }
}