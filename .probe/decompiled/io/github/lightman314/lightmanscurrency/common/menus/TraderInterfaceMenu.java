package io.github.lightman314.lightmanscurrency.common.menus;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.trader_interface.blockentity.TraderInterfaceBlockEntity;
import io.github.lightman314.lightmanscurrency.api.trader_interface.menu.TraderInterfaceTab;
import io.github.lightman314.lightmanscurrency.api.traders.TradeContext;
import io.github.lightman314.lightmanscurrency.common.core.ModMenus;
import io.github.lightman314.lightmanscurrency.common.menus.traderinterface.base.InfoTab;
import io.github.lightman314.lightmanscurrency.common.menus.traderinterface.base.OwnershipTab;
import io.github.lightman314.lightmanscurrency.common.menus.traderinterface.base.TradeSelectTab;
import io.github.lightman314.lightmanscurrency.common.menus.traderinterface.base.TraderSelectTab;
import io.github.lightman314.lightmanscurrency.common.menus.validation.types.BlockEntityValidator;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class TraderInterfaceMenu extends LazyMessageMenu {

    private final TraderInterfaceBlockEntity blockEntity;

    public static final int SLOT_OFFSET = 15;

    private boolean canEditTabs;

    Map<Integer, TraderInterfaceTab> availableTabs = new HashMap();

    int currentTab = 0;

    public final TraderInterfaceBlockEntity getBE() {
        return this.blockEntity;
    }

    public Map<Integer, TraderInterfaceTab> getAllTabs() {
        return this.availableTabs;
    }

    public void setTab(int key, TraderInterfaceTab tab) {
        if (this.canEditTabs && tab != null) {
            this.availableTabs.put(key, tab);
        } else if (tab == null) {
            LightmansCurrency.LogError("Attempted to set a null storage tab in slot " + key);
        } else {
            LightmansCurrency.LogError("Attempted to define the tab in " + key + " but the tabs have been locked.");
        }
    }

    public int getCurrentTabIndex() {
        return this.currentTab;
    }

    public TraderInterfaceTab getCurrentTab() {
        return (TraderInterfaceTab) this.availableTabs.get(this.currentTab);
    }

    public TraderInterfaceMenu(int windowID, Inventory inventory, TraderInterfaceBlockEntity blockEntity) {
        super(ModMenus.TRADER_INTERFACE.get(), windowID, inventory);
        this.blockEntity = blockEntity;
        this.canEditTabs = true;
        this.addValidator(BlockEntityValidator.of(this.blockEntity));
        this.addValidator(this.blockEntity::canAccess);
        this.setTab(0, new InfoTab(this));
        this.setTab(2, new TraderSelectTab(this));
        this.setTab(3, new TradeSelectTab(this));
        this.setTab(100, new OwnershipTab(this));
        if (this.blockEntity != null) {
            this.blockEntity.initMenuTabs(this);
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
        this.availableTabs.forEach((key, tab) -> tab.addStorageMenuSlots(x$0 -> this.m_38897_(x$0)));
        try {
            this.getCurrentTab().onTabOpen();
        } catch (Throwable var6) {
            LightmansCurrency.LogError("Error opening tab!", var6);
        }
    }

    @Override
    public void removed(@Nonnull Player player) {
        super.m_6877_(player);
        this.availableTabs.forEach((key, tab) -> tab.onMenuClose());
    }

    public TradeContext getTradeContext() {
        return this.blockEntity.getTradeContext();
    }

    @Nonnull
    @Override
    public ItemStack quickMoveStack(@Nonnull Player playerEntity, int index) {
        ItemStack clickedStack = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            clickedStack = slotStack.copy();
            if (index < 36) {
                if (!this.getCurrentTab().quickMoveStack(slotStack) && !this.m_38903_(slotStack, 36, this.f_38839_.size(), false)) {
                    return ItemStack.EMPTY;
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

    public void changeTab(int key) {
        if (this.currentTab != key) {
            if (this.availableTabs.containsKey(key)) {
                if (((TraderInterfaceTab) this.availableTabs.get(key)).canOpen(this.player)) {
                    this.getCurrentTab().onTabClose();
                    this.currentTab = key;
                    this.getCurrentTab().onTabOpen();
                }
            } else {
                LightmansCurrency.LogWarning("Trader Storage Menu doesn't have a tab defined for " + key);
            }
        }
    }

    public void changeMode(TraderInterfaceBlockEntity.ActiveMode newMode) {
        this.blockEntity.setMode(newMode);
        if (this.isClient()) {
            CompoundTag message = new CompoundTag();
            message.putInt("ModeChange", newMode.index);
            this.sendMessage(message);
        }
    }

    public void setOnlineMode(boolean newMode) {
        this.blockEntity.setOnlineMode(newMode);
        if (this.isClient()) {
            CompoundTag message = new CompoundTag();
            message.putBoolean("OnlineModeChange", newMode);
            this.sendMessage(message);
        }
    }

    public CompoundTag createTabChangeMessage(int newTab, @Nullable CompoundTag extraData) {
        CompoundTag message = extraData == null ? new CompoundTag() : extraData;
        message.putInt("ChangeTab", newTab);
        return message;
    }

    @Deprecated(since = "2.2.1.4")
    public void sendMessage(CompoundTag message) {
        if (this.isClient()) {
            this.SendMessageToServer(LazyPacketData.simpleTag("OldMessage", message));
        }
    }

    @Deprecated(since = "2.2.1.4")
    private void receiveMessage(CompoundTag message) {
        if (message.contains("ChangeTab", 3)) {
            this.changeTab(message.getInt("ChangeTab"));
        }
        if (message.contains("ModeChange")) {
            this.changeMode(TraderInterfaceBlockEntity.ActiveMode.fromIndex(message.getInt("ModeChange")));
        }
        if (message.contains("OnlineModeChange")) {
            this.setOnlineMode(message.getBoolean("OnlineModeChange"));
        }
        try {
            this.getCurrentTab().receiveMessage(message);
        } catch (Throwable var3) {
        }
    }

    @Override
    public void HandleMessage(@Nonnull LazyPacketData message) {
        if (message.contains("ChangeTab")) {
            this.changeTab(message.getInt("ChangeTab"));
        }
        if (message.contains("ModeChange")) {
            this.changeMode(TraderInterfaceBlockEntity.ActiveMode.fromIndex(message.getInt("ModeChange")));
        }
        if (message.contains("OnlineModeChange")) {
            this.setOnlineMode(message.getBoolean("OnlineModeChange"));
        }
        if (message.contains("OldMessage")) {
            this.receiveMessage(message.getNBT("OldMessage"));
        }
        try {
            this.getCurrentTab().handleMessage(message);
        } catch (Throwable var3) {
            LightmansCurrency.LogError("Error handling Trader Interface Message!", var3);
        }
    }
}