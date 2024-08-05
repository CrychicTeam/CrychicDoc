package io.github.lightman314.lightmanscurrency.common.menus;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyStorage;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.common.core.ModMenus;
import io.github.lightman314.lightmanscurrency.common.menus.tax_collector.TaxCollectorTab;
import io.github.lightman314.lightmanscurrency.common.menus.tax_collector.tabs.AdminTab;
import io.github.lightman314.lightmanscurrency.common.menus.tax_collector.tabs.BasicSettingsTab;
import io.github.lightman314.lightmanscurrency.common.menus.tax_collector.tabs.InfoTab;
import io.github.lightman314.lightmanscurrency.common.menus.tax_collector.tabs.LogTab;
import io.github.lightman314.lightmanscurrency.common.menus.tax_collector.tabs.OwnershipTab;
import io.github.lightman314.lightmanscurrency.common.menus.validation.MenuValidator;
import io.github.lightman314.lightmanscurrency.common.player.LCAdminMode;
import io.github.lightman314.lightmanscurrency.common.taxes.TaxEntry;
import io.github.lightman314.lightmanscurrency.common.taxes.TaxSaveData;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class TaxCollectorMenu extends LazyMessageMenu {

    public final long entryID;

    private Consumer<Integer> tabChangeListener = i -> {
    };

    private int currentTab = 0;

    private final List<TaxCollectorTab> tabs = Lists.newArrayList(new TaxCollectorTab[] { new BasicSettingsTab(this), new LogTab(this), new InfoTab(this), new OwnershipTab(this), new AdminTab(this) });

    public final TaxEntry getEntry() {
        return TaxSaveData.GetTaxEntry(this.entryID, this.isClient());
    }

    public void setTabChangeListener(@Nonnull Consumer<Integer> listener) {
        this.tabChangeListener = listener;
    }

    public final List<TaxCollectorTab> getAllTabs() {
        return ImmutableList.copyOf(this.tabs);
    }

    private TaxCollectorTab getCurrentTabInternal() {
        return this.currentTab >= 0 && this.currentTab < this.tabs.size() ? (TaxCollectorTab) this.tabs.get(this.currentTab) : null;
    }

    public final TaxCollectorTab getCurrentTab() {
        if (this.currentTab < 0 || this.currentTab >= this.tabs.size()) {
            this.ChangeTab(0, true);
        }
        return this.getCurrentTabInternal();
    }

    public TaxCollectorMenu(int id, Inventory inventory, long entryID, MenuValidator validator) {
        super(ModMenus.TAX_COLLECTOR.get(), id, inventory, validator);
        this.entryID = entryID;
        this.addValidator(this::hasAccess);
        for (TaxCollectorTab tab : this.tabs) {
            tab.addMenuSlots(x$0 -> this.m_38897_(x$0));
        }
    }

    public void ChangeTab(int newTabIndex, boolean sendMessage) {
        if (newTabIndex >= 0 && newTabIndex < this.tabs.size()) {
            if (newTabIndex != this.currentTab) {
                TaxCollectorTab oldTab = this.getCurrentTabInternal();
                if (oldTab != null) {
                    oldTab.onTabClose();
                }
                this.currentTab = newTabIndex;
                TaxCollectorTab newTab = this.getCurrentTabInternal();
                if (newTab != null) {
                    newTab.onTabOpen();
                }
                if (sendMessage) {
                    this.SendMessage(LazyPacketData.builder().setInt("ChangeTab", this.currentTab));
                }
                this.tabChangeListener.accept(this.currentTab);
            }
        }
    }

    @Nonnull
    @Override
    public ItemStack quickMoveStack(@Nonnull Player player, int slot) {
        return ItemStack.EMPTY;
    }

    public void CollectStoredMoney() {
        TaxEntry entry = this.getEntry();
        if (entry != null && this.hasAccess()) {
            MoneyStorage amountToGive = entry.getStoredMoney();
            if (!amountToGive.isEmpty()) {
                entry.getStoredMoney().GiveToPlayer(this.player);
            }
            if (this.isClient()) {
                this.SendMessageToServer(LazyPacketData.simpleFlag("CollectStoredMoney"));
            }
        }
    }

    @Override
    protected void onValidationTick(@Nonnull Player player) {
        TaxCollectorTab tab = this.getCurrentTab();
        if (tab != null && !tab.canBeAccessed() && this.currentTab != 0) {
            this.ChangeTab(0, true);
        }
    }

    public boolean isServerEntry() {
        TaxEntry entry = this.getEntry();
        return entry != null ? entry.isServerEntry() : false;
    }

    public boolean hasAccess() {
        TaxEntry entry = this.getEntry();
        return entry == null ? false : entry.canAccess(this.player);
    }

    public boolean isOwner() {
        TaxEntry entry = this.getEntry();
        return entry == null ? false : entry.getOwner().isAdmin(this.player);
    }

    public boolean isAdmin() {
        return LCAdminMode.isAdminPlayer(this.player);
    }

    @Override
    public void HandleMessage(@Nonnull LazyPacketData message) {
        if (message.contains("ChangeTab")) {
            this.ChangeTab(message.getInt("ChangeTab"), false);
        } else if (message.contains("CollectStoredMoney")) {
            this.CollectStoredMoney();
        } else {
            this.getCurrentTab().receiveMessage(message);
        }
    }
}