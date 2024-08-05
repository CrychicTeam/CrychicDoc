package io.github.lightman314.lightmanscurrency.common.menus.traderinterface.base;

import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.trader_interface.menu.TraderInterfaceClientTab;
import io.github.lightman314.lightmanscurrency.api.trader_interface.menu.TraderInterfaceTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.TraderInterfaceScreen;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderinterface.TradeSelectClientTab;
import io.github.lightman314.lightmanscurrency.common.menus.TraderInterfaceMenu;
import java.util.function.Function;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TradeSelectTab extends TraderInterfaceTab {

    public TradeSelectTab(TraderInterfaceMenu menu) {
        super(menu);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public TraderInterfaceClientTab<?> createClientTab(TraderInterfaceScreen screen) {
        return new TradeSelectClientTab(screen, this);
    }

    @Override
    public boolean canOpen(Player player) {
        return this.menu.getBE().getInteractionType().trades && this.menu.getBE().getTrader() != null;
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

    public void setTradeIndex(int tradeIndex) {
        if (this.menu.getBE().canAccess(this.menu.player)) {
            this.menu.getBE().setTradeIndex(tradeIndex);
            if (this.menu.isClient()) {
                this.menu.SendMessage(LazyPacketData.simpleInt("NewTradeIndex", tradeIndex));
            }
        }
    }

    @Override
    public void handleMessage(@Nonnull LazyPacketData message) {
        if (message.contains("NewTradeIndex")) {
            this.setTradeIndex(message.getInt("NewTradeIndex"));
        }
    }
}