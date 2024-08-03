package io.github.lightman314.lightmanscurrency.proxy;

import com.google.common.base.Suppliers;
import com.mojang.authlib.GameProfile;
import io.github.lightman314.lightmanscurrency.LCConfig;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.config.ConfigFile;
import io.github.lightman314.lightmanscurrency.api.events.NotificationEvent;
import io.github.lightman314.lightmanscurrency.api.misc.blocks.IOwnableBlock;
import io.github.lightman314.lightmanscurrency.api.money.bank.reference.BankReference;
import io.github.lightman314.lightmanscurrency.api.money.coins.CoinAPI;
import io.github.lightman314.lightmanscurrency.api.money.coins.data.ChainData;
import io.github.lightman314.lightmanscurrency.api.notifications.Notification;
import io.github.lightman314.lightmanscurrency.api.notifications.NotificationData;
import io.github.lightman314.lightmanscurrency.client.data.ClientBankData;
import io.github.lightman314.lightmanscurrency.client.data.ClientEjectionData;
import io.github.lightman314.lightmanscurrency.client.data.ClientNotificationData;
import io.github.lightman314.lightmanscurrency.client.data.ClientTaxData;
import io.github.lightman314.lightmanscurrency.client.data.ClientTeamData;
import io.github.lightman314.lightmanscurrency.client.data.ClientTraderData;
import io.github.lightman314.lightmanscurrency.client.gui.screen.NetworkTerminalScreen;
import io.github.lightman314.lightmanscurrency.client.gui.screen.NotificationScreen;
import io.github.lightman314.lightmanscurrency.client.gui.screen.TeamManagerScreen;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.ATMScreen;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.CoinChestScreen;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.EjectionRecoveryScreen;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.MintScreen;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.PlayerTradeScreen;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.SlotMachineScreen;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.TaxCollectorScreen;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.TicketStationScreen;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.TraderInterfaceScreen;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.TraderScreen;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.TraderStorageScreen;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.WalletBankScreen;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.WalletScreen;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.coin_management.CoinManagementScreen;
import io.github.lightman314.lightmanscurrency.client.gui.widget.ItemEditWidget;
import io.github.lightman314.lightmanscurrency.client.renderer.LCItemRenderer;
import io.github.lightman314.lightmanscurrency.client.renderer.blockentity.AuctionStandBlockEntityRenderer;
import io.github.lightman314.lightmanscurrency.client.renderer.blockentity.BookTraderBlockEntityRenderer;
import io.github.lightman314.lightmanscurrency.client.renderer.blockentity.CoinChestRenderer;
import io.github.lightman314.lightmanscurrency.client.renderer.blockentity.FreezerTraderBlockEntityRenderer;
import io.github.lightman314.lightmanscurrency.client.renderer.blockentity.ItemTraderBlockEntityRenderer;
import io.github.lightman314.lightmanscurrency.client.renderer.blockentity.SlotMachineBlockEntityRenderer;
import io.github.lightman314.lightmanscurrency.client.renderer.blockentity.book.BookRenderer;
import io.github.lightman314.lightmanscurrency.client.renderer.blockentity.book.renderers.EnchantedBookRenderer;
import io.github.lightman314.lightmanscurrency.client.renderer.blockentity.book.renderers.NormalBookRenderer;
import io.github.lightman314.lightmanscurrency.common.blockentity.CoinChestBlockEntity;
import io.github.lightman314.lightmanscurrency.common.capability.event_unlocks.CapabilityEventUnlocks;
import io.github.lightman314.lightmanscurrency.common.capability.event_unlocks.IEventUnlocks;
import io.github.lightman314.lightmanscurrency.common.core.ModBlockEntities;
import io.github.lightman314.lightmanscurrency.common.core.ModBlocks;
import io.github.lightman314.lightmanscurrency.common.core.ModEnchantments;
import io.github.lightman314.lightmanscurrency.common.core.ModMenus;
import io.github.lightman314.lightmanscurrency.common.core.ModSounds;
import io.github.lightman314.lightmanscurrency.common.enchantments.MoneyMendingEnchantment;
import io.github.lightman314.lightmanscurrency.common.items.TicketItem;
import io.github.lightman314.lightmanscurrency.common.menus.PlayerTradeMenu;
import io.github.lightman314.lightmanscurrency.common.player.LCAdminMode;
import io.github.lightman314.lightmanscurrency.common.playertrading.ClientPlayerTrade;
import io.github.lightman314.lightmanscurrency.integration.curios.client.LCCuriosClient;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientProxy extends CommonProxy {

    boolean openTeamManager = false;

    boolean openNotifications = false;

    private long timeOffset = 0L;

    private final Supplier<CoinChestBlockEntity> coinChestBE = Suppliers.memoize(() -> new CoinChestBlockEntity(BlockPos.ZERO, ModBlocks.COIN_CHEST.get().defaultBlockState()));

    @Override
    public boolean isClient() {
        return true;
    }

    @Override
    public void setupClient() {
        ConfigFile.loadClientFiles(ConfigFile.LoadPhase.SETUP);
        MenuScreens.register(ModMenus.ATM.get(), ATMScreen::new);
        MenuScreens.register(ModMenus.MINT.get(), MintScreen::new);
        MenuScreens.register(ModMenus.NETWORK_TERMINAL.get(), NetworkTerminalScreen::new);
        MenuScreens.register(ModMenus.TRADER.get(), TraderScreen::new);
        MenuScreens.register(ModMenus.TRADER_BLOCK.get(), TraderScreen::new);
        MenuScreens.register(ModMenus.TRADER_NETWORK_ALL.get(), TraderScreen::new);
        MenuScreens.register(ModMenus.TRADER_STORAGE.get(), TraderStorageScreen::new);
        MenuScreens.register(ModMenus.SLOT_MACHINE.get(), SlotMachineScreen::new);
        MenuScreens.register(ModMenus.WALLET.get(), WalletScreen::new);
        MenuScreens.register(ModMenus.WALLET_BANK.get(), WalletBankScreen::new);
        MenuScreens.register(ModMenus.TICKET_MACHINE.get(), TicketStationScreen::new);
        MenuScreens.register(ModMenus.TRADER_INTERFACE.get(), TraderInterfaceScreen::new);
        MenuScreens.register(ModMenus.EJECTION_RECOVERY.get(), EjectionRecoveryScreen::new);
        MenuScreens.register(ModMenus.PLAYER_TRADE.get(), PlayerTradeScreen::new);
        MenuScreens.register(ModMenus.COIN_CHEST.get(), CoinChestScreen::new);
        MenuScreens.register(ModMenus.TAX_COLLECTOR.get(), TaxCollectorScreen::new);
        MenuScreens.register(ModMenus.COIN_MANAGEMENT.get(), CoinManagementScreen::new);
        BlockEntityRenderers.register(ModBlockEntities.ITEM_TRADER.get(), ItemTraderBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.FREEZER_TRADER.get(), FreezerTraderBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.SLOT_MACHINE_TRADER.get(), SlotMachineBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.BOOK_TRADER.get(), BookTraderBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.AUCTION_STAND.get(), AuctionStandBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.COIN_CHEST.get(), CoinChestRenderer::new);
        ItemEditWidget.BlacklistCreativeTabs(CreativeModeTabs.HOTBAR, CreativeModeTabs.INVENTORY, CreativeModeTabs.SEARCH, CreativeModeTabs.OP_BLOCKS);
        ItemEditWidget.BlacklistItem(s -> s.getItem() instanceof TicketItem);
        ItemEditWidget.AddExtraItemAfter(new ItemStack(Items.WRITTEN_BOOK), Items.WRITABLE_BOOK);
        BookRenderer.register(NormalBookRenderer.GENERATOR);
        BookRenderer.register(EnchantedBookRenderer.GENERATOR);
        LCItemRenderer.registerBlockEntitySource(this::checkForCoinChest);
        if (LightmansCurrency.isCuriosLoaded()) {
            LCCuriosClient.registerRenderLayers();
        }
    }

    private BlockEntity checkForCoinChest(Block block) {
        return block == ModBlocks.COIN_CHEST.get() ? (BlockEntity) this.coinChestBE.get() : null;
    }

    @Override
    public void clearClientTraders() {
        ClientTraderData.ClearTraders();
    }

    @Override
    public void updateTrader(CompoundTag compound) {
        ClientTraderData.UpdateTrader(compound);
    }

    @Override
    public void removeTrader(long traderID) {
        ClientTraderData.RemoveTrader(traderID);
    }

    @Override
    public void clearTeams() {
        ClientTeamData.ClearTeams();
    }

    @Override
    public void updateTeam(CompoundTag compound) {
        ClientTeamData.UpdateTeam(compound);
    }

    @Override
    public void removeTeam(long teamID) {
        ClientTeamData.RemoveTeam(teamID);
    }

    @Override
    public void clearBankAccounts() {
        ClientBankData.ClearBankAccounts();
    }

    @Override
    public void updateBankAccount(UUID player, CompoundTag compound) {
        ClientBankData.UpdateBankAccount(player, compound);
    }

    @Override
    public void receiveEmergencyEjectionData(CompoundTag compound) {
        ClientEjectionData.UpdateEjectionData(compound);
    }

    @Override
    public void updateNotifications(NotificationData data) {
        ClientNotificationData.UpdateNotifications(data);
    }

    @Override
    public void receiveNotification(Notification notification) {
        Minecraft mc = Minecraft.getInstance();
        assert mc.player != null;
        if (!MinecraftForge.EVENT_BUS.post(new NotificationEvent.NotificationReceivedOnClient(mc.player.m_20148_(), ClientNotificationData.GetNotifications(), notification))) {
            if (LCConfig.CLIENT.pushNotificationsToChat.get()) {
                mc.gui.getChat().addMessage(notification.getChatMessage());
            }
        }
    }

    @Override
    public void receiveSelectedBankAccount(BankReference selectedAccount) {
        ClientBankData.UpdateLastSelectedAccount(selectedAccount);
    }

    @Override
    public void updateTaxEntries(CompoundTag compound) {
        ClientTaxData.UpdateEntry(compound);
    }

    @Override
    public void removeTaxEntry(long id) {
        ClientTaxData.RemoveEntry(id);
    }

    @Override
    public void openNotificationScreen() {
        this.openNotifications = true;
    }

    @Override
    public void openTeamManager() {
        this.openTeamManager = true;
    }

    @Override
    public void createTeamResponse(long teamID) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen instanceof TeamManagerScreen screen) {
            screen.setActiveTeam(teamID);
        }
    }

    @Override
    public long getTimeDesync() {
        return this.timeOffset;
    }

    @Override
    public void setTimeDesync(long serverTime) {
        this.timeOffset = serverTime - System.currentTimeMillis();
        this.timeOffset = this.timeOffset / 1000L * 1000L;
        if (this.timeOffset < 10000L) {
            this.timeOffset = 0L;
        }
    }

    @Override
    public void loadAdminPlayers(List<UUID> serverAdminList) {
        LCAdminMode.loadAdminPlayers(serverAdminList);
    }

    @SubscribeEvent
    public void openScreenOnRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (this.openTeamManager) {
                this.openTeamManager = false;
                Minecraft.getInstance().setScreen(new TeamManagerScreen());
            } else if (this.openNotifications) {
                this.openNotifications = false;
                Minecraft.getInstance().setScreen(new NotificationScreen());
            }
        }
    }

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        if (event.getEntity() != null && !CoinAPI.API.NoDataAvailable()) {
            ItemStack stack = event.getItemStack();
            if (CoinAPI.API.IsCoin(stack, true)) {
                ChainData.addCoinTooltips(event.getItemStack(), event.getToolTip(), event.getFlags(), event.getEntity());
            }
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(event.getItemStack());
            if ((Integer) enchantments.getOrDefault(ModEnchantments.MONEY_MENDING.get(), 0) > 0) {
                event.getToolTip().add(LCText.TOOLTIP_MONEY_MENDING_COST.get(MoneyMendingEnchantment.getRepairCost(stack).getText()).withStyle(ChatFormatting.YELLOW));
            }
            if (LCConfig.SERVER.isLoaded() && LCConfig.SERVER.anarchyMode.get() && stack.getItem() instanceof BlockItem bi) {
                Block b = bi.getBlock();
                if (b instanceof IOwnableBlock) {
                    event.getToolTip().add(LCText.TOOLTIP_ANARCHY_WARNING.get().withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.RED));
                }
            }
        }
    }

    @Override
    public void playCoinSound() {
        if (LCConfig.CLIENT.moneyMendingClink.get()) {
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.getSoundManager().play(SimpleSoundInstance.forUI(ModSounds.COINS_CLINKING.get(), 1.0F, 0.4F));
        }
    }

    @Nonnull
    @Override
    public Level safeGetDummyLevel() throws Exception {
        Level level = this.getDummyLevelFromServer();
        if (level == null) {
            level = Minecraft.getInstance().level;
        }
        if (level != null) {
            return level;
        } else {
            throw new Exception("Could not get dummy level from client, as there is no active level!");
        }
    }

    @Override
    public void loadPlayerTrade(ClientPlayerTrade trade) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player.f_36096_ instanceof PlayerTradeMenu menu) {
            menu.reloadTrade(trade);
        }
    }

    @Override
    public void syncEventUnlocks(@Nonnull List<String> unlocksList) {
        Minecraft mc = Minecraft.getInstance();
        IEventUnlocks unlocks = CapabilityEventUnlocks.getCapability(mc.player);
        if (unlocks != null) {
            unlocks.sync(unlocksList);
        }
    }

    @Override
    public void sendClientMessage(@Nonnull Component message) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            player.m_213846_(message);
        }
    }

    @Override
    public List<GameProfile> getPlayerList(boolean logicalClient) {
        return !logicalClient ? super.getPlayerList(logicalClient) : Minecraft.getInstance().getConnection().getOnlinePlayers().stream().map(PlayerInfo::m_105312_).toList();
    }
}