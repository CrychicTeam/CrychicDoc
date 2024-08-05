package io.github.lightman314.lightmanscurrency.common.menus.traderstorage.logs;

import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.TraderStorageTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.logs.TraderLogClientTab;
import io.github.lightman314.lightmanscurrency.common.menus.TraderStorageMenu;
import java.util.function.Function;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TraderLogTab extends TraderStorageTab {

    public TraderLogTab(TraderStorageMenu menu) {
        super(menu);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Object createClientTab(Object screen) {
        return new TraderLogClientTab(screen, this);
    }

    @Override
    public boolean canOpen(Player player) {
        return this.menu.hasPermission("viewLogs");
    }

    @Override
    public void onTabOpen() {
    }

    @Override
    public void onTabClose() {
    }

    @Override
    public void addStorageMenuSlots(Function<Slot, Slot> addSlot) {
    }

    @Override
    public void receiveMessage(LazyPacketData message) {
    }
}