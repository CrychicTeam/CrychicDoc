package io.github.lightman314.lightmanscurrency.common.menus.tax_collector;

import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.common.menus.TaxCollectorMenu;
import io.github.lightman314.lightmanscurrency.common.taxes.TaxEntry;
import io.github.lightman314.lightmanscurrency.common.util.IClientTracker;
import java.util.function.Function;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public abstract class TaxCollectorTab implements IClientTracker {

    public final TaxCollectorMenu menu;

    @Override
    public boolean isClient() {
        return this.menu.isClient();
    }

    public final TaxEntry getEntry() {
        return this.menu.getEntry();
    }

    public final boolean hasAccess() {
        return this.menu.hasAccess();
    }

    public final boolean isAdmin() {
        return this.menu.isAdmin();
    }

    public final boolean isOwner() {
        return this.menu.isOwner();
    }

    public final boolean isServerEntry() {
        return this.menu.isServerEntry();
    }

    protected TaxCollectorTab(TaxCollectorMenu menu) {
        this.menu = menu;
    }

    public abstract Object createClientTab(Object var1);

    public boolean canBeAccessed() {
        return true;
    }

    public abstract void onTabOpen();

    public abstract void onTabClose();

    public void addMenuSlots(Function<Slot, Slot> addSlot) {
    }

    public boolean quickMoveStack(ItemStack stack) {
        return false;
    }

    public abstract void receiveMessage(LazyPacketData var1);
}