package io.github.lightman314.lightmanscurrency.common.menus;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.capability.money.IMoneyHandler;
import io.github.lightman314.lightmanscurrency.api.money.MoneyAPI;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyView;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.traders.TradeContext;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.api.traders.menu.IMoneyCollectionMenu;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.ITraderStorageMenu;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.TraderStorageTab;
import io.github.lightman314.lightmanscurrency.common.core.ModMenus;
import io.github.lightman314.lightmanscurrency.common.menus.slots.CoinSlot;
import io.github.lightman314.lightmanscurrency.common.menus.slots.SimpleSlot;
import io.github.lightman314.lightmanscurrency.common.menus.traderstorage.TaxInfoTab;
import io.github.lightman314.lightmanscurrency.common.menus.traderstorage.TraderStatsTab;
import io.github.lightman314.lightmanscurrency.common.menus.traderstorage.logs.TraderLogTab;
import io.github.lightman314.lightmanscurrency.common.menus.traderstorage.settings.TraderSettingsTab;
import io.github.lightman314.lightmanscurrency.common.menus.traderstorage.trade_rules.TradeRulesTab;
import io.github.lightman314.lightmanscurrency.common.menus.traderstorage.trades_basic.BasicTradeEditTab;
import io.github.lightman314.lightmanscurrency.common.menus.validation.IValidatedMenu;
import io.github.lightman314.lightmanscurrency.common.menus.validation.MenuValidator;
import io.github.lightman314.lightmanscurrency.common.traders.TraderSaveData;
import io.github.lightman314.lightmanscurrency.common.traders.permissions.Permissions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TraderStorageMenu extends LazyMessageMenu implements IValidatedMenu, ITraderStorageMenu, IMoneyCollectionMenu {

    private final Supplier<TraderData> traderSource;

    public static final int SLOT_OFFSET = 15;

    private final IMoneyHandler coinSlotHandler;

    private final Container coinSlotContainer;

    private boolean coinSlotsVisible = true;

    List<CoinSlot> coinSlots = new ArrayList();

    private boolean canEditTabs;

    Map<Integer, TraderStorageTab> availableTabs = new HashMap();

    int currentTab = 0;

    private final List<Consumer<LazyPacketData>> listeners = new ArrayList();

    private TradeContext context = null;

    private final MenuValidator validator;

    @Nonnull
    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public final TraderData getTrader() {
        return (TraderData) this.traderSource.get();
    }

    public boolean areCoinSlotsVisible() {
        return this.coinSlotsVisible;
    }

    public List<CoinSlot> getCoinSlots() {
        return this.coinSlots;
    }

    public Map<Integer, TraderStorageTab> getAllTabs() {
        return this.availableTabs;
    }

    @Override
    public void setTab(int key, @Nonnull TraderStorageTab tab) {
        if (this.canEditTabs) {
            this.availableTabs.put(key, tab);
        } else {
            LightmansCurrency.LogError("Attempted to define the tab in " + key + " but the tabs have been locked.");
        }
    }

    @Override
    public void clearTab(int key) {
        if (this.canEditTabs) {
            if (key == 0) {
                LightmansCurrency.LogError("Attempted to clear the basic trade tab!\nTabs at this index cannot be removed, as there must be one present at all times.\nIf you wish to replace this tab with your own use setTab!");
            } else {
                this.availableTabs.remove(key);
            }
        } else {
            LightmansCurrency.LogError("Attempted to clear the tab in " + key + " but the tabs have been locked.");
        }
    }

    public int getCurrentTabIndex() {
        return this.currentTab;
    }

    public TraderStorageTab getCurrentTab() {
        return (TraderStorageTab) this.availableTabs.get(this.currentTab);
    }

    @Nonnull
    @Override
    public TradeContext getContext() {
        TraderData trader = (TraderData) this.traderSource.get();
        if (this.context == null || this.context.getTrader() != trader) {
            if (this.context != null) {
                this.context.clearCache();
            }
            this.context = TradeContext.createStorageMode(trader);
        }
        return this.context;
    }

    @Nonnull
    @Override
    public ItemStack getHeldItem() {
        return this.m_142621_();
    }

    @Override
    public void setHeldItem(@Nonnull ItemStack stack) {
        this.m_142503_(stack);
    }

    @Nonnull
    @Override
    public MenuValidator getValidator() {
        return this.validator;
    }

    public TraderStorageMenu(int windowID, Inventory inventory, long traderID, @Nonnull MenuValidator validator) {
        this(ModMenus.TRADER_STORAGE.get(), windowID, inventory, () -> TraderSaveData.GetTrader(inventory.player.m_9236_().isClientSide, traderID), validator);
    }

    protected TraderStorageMenu(MenuType<?> type, int windowID, Inventory inventory, Supplier<TraderData> traderSource, @Nonnull MenuValidator validator) {
        super(type, windowID, inventory);
        this.validator = validator;
        this.traderSource = traderSource;
        this.coinSlotContainer = new SimpleContainer(5);
        this.coinSlotHandler = MoneyAPI.API.GetContainersMoneyHandler(this.coinSlotContainer, inventory.player);
        this.addValidator(() -> this.hasPermission("openStorage"));
        this.addValidator(this.validator);
        this.canEditTabs = true;
        TraderData trader = (TraderData) this.traderSource.get();
        this.setTab(0, new BasicTradeEditTab(this));
        this.setTab(10, new TraderLogTab(this));
        this.setTab(11, new TraderSettingsTab(this));
        this.setTab(12, new TraderStatsTab(this));
        this.setTab(100, new TradeRulesTab.Trader(this));
        this.setTab(101, new TradeRulesTab.Trade(this));
        this.setTab(50, new TaxInfoTab(this));
        if (trader != null) {
            trader.initStorageTabs(this);
        }
        this.canEditTabs = false;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.m_38897_(new Slot(inventory, x + y * 9 + 9, 23 + x * 18, 154 + y * 18));
            }
        }
        for (int x = 0; x < 9; x++) {
            this.m_38897_(new Slot(inventory, x, 23 + x * 18, 212));
        }
        for (int x = 0; x < this.coinSlotContainer.getContainerSize(); x++) {
            CoinSlot newSlot = new CoinSlot(this.coinSlotContainer, x, 23 + (x + 4) * 18, 122);
            this.coinSlots.add(newSlot);
            this.m_38897_(newSlot);
        }
        this.availableTabs.forEach((key, tab) -> tab.addStorageMenuSlots(x$0 -> this.m_38897_(x$0)));
        try {
            this.getCurrentTab().onTabOpen();
        } catch (Throwable var9) {
            LightmansCurrency.LogError("Error opening storage tab.", var9);
        }
        this.getTrader().userOpen(this.player);
    }

    @Override
    public void removed(@Nonnull Player player) {
        super.m_6877_(player);
        this.m_150411_(player, this.coinSlotContainer);
        this.availableTabs.forEach((key, tab) -> tab.onMenuClose());
        TraderData trader = this.getTrader();
        if (trader != null) {
            trader.userClose(player);
        }
        if (this.context != null) {
            this.context.clearCache();
        }
    }

    @Override
    public void clearContainer(@Nonnull Container container) {
        this.m_150411_(this.player, container);
    }

    public void validateCoinSlots() {
        boolean canAddCoins = this.hasCoinSlotAccess();
        for (CoinSlot slot : this.coinSlots) {
            slot.active = canAddCoins && this.coinSlotsVisible;
        }
    }

    private boolean hasCoinSlotAccess() {
        TraderData trader = this.getTrader();
        return trader != null && trader.hasPermission(this.player, "storeCoins") && !trader.hasBankAccount();
    }

    @NotNull
    @Override
    public ItemStack quickMoveStack(@NotNull Player playerEntity, int index) {
        ItemStack clickedStack = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            clickedStack = slotStack.copy();
            if (index < 36) {
                if (!this.getCurrentTab().quickMoveStack(slotStack)) {
                    if (this.hasCoinSlotAccess()) {
                        if (!this.m_38903_(slotStack, 36, 36 + this.coinSlots.size(), false) && !this.m_38903_(slotStack, 36 + this.coinSlots.size(), this.f_38839_.size(), false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.m_38903_(slotStack, 36 + this.coinSlots.size(), this.f_38839_.size(), false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (index < this.f_38839_.size() && !this.m_38903_(slotStack, 0, 36, false)) {
                return ItemStack.EMPTY;
            }
            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return clickedStack;
    }

    @Override
    public int getPermissionLevel(@Nonnull String permission) {
        TraderData trader = this.getTrader();
        return trader != null ? trader.getPermissionLevel(this.player, permission) : 0;
    }

    @Override
    public void changeTab(int key) {
        if (this.currentTab != key) {
            if (this.availableTabs.containsKey(key)) {
                if (((TraderStorageTab) this.availableTabs.get(key)).canOpen(this.player)) {
                    this.getCurrentTab().onTabClose();
                    this.currentTab = key;
                    this.getCurrentTab().onTabOpen();
                }
            } else {
                LightmansCurrency.LogWarning("Trader Storage Menu doesn't have a tab defined for " + key);
            }
        }
    }

    @Override
    public void changeTab(int key, @Nullable LazyPacketData.Builder message) {
        this.changeTab(key);
        if (message != null) {
            this.HandleMessage(message.build());
        }
        this.SendMessage(this.createTabChangeMessage(key, message));
    }

    @Nonnull
    @Override
    public LazyPacketData.Builder createTabChangeMessage(int newTab) {
        return this.createTabChangeMessage(newTab, null);
    }

    @Nonnull
    @Override
    public LazyPacketData.Builder createTabChangeMessage(int newTab, @Nullable LazyPacketData.Builder extraData) {
        LazyPacketData.Builder message = extraData == null ? LazyPacketData.builder() : extraData;
        message.setInt("ChangeTab", newTab);
        return message;
    }

    @Override
    public void SetCoinSlotsActive(boolean nowActive) {
        this.coinSlotsVisible = nowActive;
        SimpleSlot.SetActive(this.coinSlots, nowActive);
        if (this.isClient()) {
            this.SendMessage(this.createCoinSlotActiveMessage(nowActive, null));
        }
    }

    public LazyPacketData.Builder createCoinSlotActiveMessage(boolean nowActive, @Nullable LazyPacketData.Builder extraData) {
        LazyPacketData.Builder message = extraData == null ? LazyPacketData.builder() : extraData;
        message.setBoolean("SetCoinSlotsActive", nowActive);
        return message;
    }

    @Override
    public void HandleMessage(@Nonnull LazyPacketData message) {
        if (message.contains("ChangeTab", (byte) 2)) {
            this.changeTab(message.getInt("ChangeTab"));
        }
        if (message.contains("SetCoinSlotsActive", (byte) 1)) {
            this.SetCoinSlotsActive(message.getBoolean("SetCoinSlotsActive"));
        }
        try {
            this.getCurrentTab().receiveMessage(message);
        } catch (Throwable var6) {
        }
        for (Consumer<LazyPacketData> listener : this.listeners) {
            try {
                listener.accept(message);
            } catch (Throwable var5) {
            }
        }
    }

    public void addListener(Consumer<LazyPacketData> listener) {
        if (!this.listeners.contains(listener) && listener != null) {
            this.listeners.add(listener);
        }
    }

    public boolean HasCoinsToAdd() {
        return !this.coinSlotHandler.getStoredMoney().isEmpty();
    }

    @Override
    public void CollectStoredMoney() {
        TraderData trader = this.getTrader();
        if (trader == null) {
            this.player.closeContainer();
        } else {
            trader.CollectStoredMoney(this.player);
        }
    }

    public void AddCoins() {
        TraderData trader = this.getTrader();
        if (trader == null) {
            this.player.closeContainer();
        } else {
            if (trader.hasPermission(this.player, "storeCoins")) {
                MoneyView addAmount = this.coinSlotHandler.getStoredMoney();
                for (MoneyValue value : addAmount.allValues()) {
                    trader.addStoredMoney(value, false);
                }
                this.coinSlotContainer.m_6211_();
            } else {
                Permissions.PermissionWarning(this.player, "store coins", "storeCoins");
            }
        }
    }
}