package io.github.lightman314.lightmanscurrency.common.menus.traderinterface.base;

import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.trader_interface.menu.TraderInterfaceClientTab;
import io.github.lightman314.lightmanscurrency.api.trader_interface.menu.TraderInterfaceTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.TraderInterfaceScreen;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderinterface.TraderSelectClientTab;
import io.github.lightman314.lightmanscurrency.common.menus.TraderInterfaceMenu;
import java.util.function.Function;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TraderSelectTab extends TraderInterfaceTab {

    public TraderSelectTab(TraderInterfaceMenu menu) {
        super(menu);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public TraderInterfaceClientTab<?> createClientTab(TraderInterfaceScreen screen) {
        return new TraderSelectClientTab(screen, this);
    }

    @Override
    public boolean canOpen(Player player) {
        return true;
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

    public void setTrader(long traderID) {
        this.menu.getBE().setTrader(traderID);
        if (this.menu.isClient()) {
            if (traderID >= 0L) {
                this.menu.SendMessage(LazyPacketData.simpleLong("NewTrader", traderID));
            } else {
                this.menu.SendMessage(LazyPacketData.simpleFlag("NullTrader"));
            }
        }
    }

    @Override
    public void handleMessage(@Nonnull LazyPacketData message) {
        if (message.contains("NewTrader")) {
            this.setTrader(message.getLong("NewTrader"));
        } else if (message.contains("NullTrader")) {
            this.setTrader(-1L);
        }
    }
}