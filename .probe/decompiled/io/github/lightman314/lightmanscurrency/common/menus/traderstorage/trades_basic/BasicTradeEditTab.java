package io.github.lightman314.lightmanscurrency.common.menus.traderstorage.trades_basic;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.TraderStorageTab;
import io.github.lightman314.lightmanscurrency.api.traders.trade.TradeData;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.BasicTradeEditClientTab;
import io.github.lightman314.lightmanscurrency.common.menus.TraderStorageMenu;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BasicTradeEditTab extends TraderStorageTab {

    public static final int INTERACTION_INPUT = 0;

    public static final int INTERACTION_OUTPUT = 1;

    public static final int INTERACTION_OTHER = 2;

    Consumer<CompoundTag> oldClientHandler = null;

    Consumer<LazyPacketData.Builder> clientHandler = null;

    public BasicTradeEditTab(TraderStorageMenu menu) {
        super(menu);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Object createClientTab(Object screen) {
        return new BasicTradeEditClientTab<>(screen, this);
    }

    public void setClient(Consumer<LazyPacketData.Builder> clientHandler) {
        this.clientHandler = clientHandler;
        if (this.oldClientHandler == null) {
            this.oldClientHandler = c -> this.clientHandler.accept(LazyPacketData.simpleTag("OldMessage", c));
        }
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

    public void sendOpenTabMessage(int newTab, @Nullable LazyPacketData.Builder additionalData) {
        LazyPacketData.Builder message = this.menu.createTabChangeMessage(newTab, additionalData);
        if (this.clientHandler != null) {
            this.clientHandler.accept(message);
        }
        this.menu.SendMessage(message);
    }

    public void sendInputInteractionMessage(int tradeIndex, int interactionIndex, int button, ItemStack heldItem) {
        this.menu.SendMessage(LazyPacketData.builder().setInt("TradeIndex", tradeIndex).setInt("InteractionType", 0).setInt("InteractionIndex", interactionIndex).setInt("Button", button).setCompound("HeldItem", heldItem.save(new CompoundTag())));
    }

    public void sendOutputInteractionMessage(int tradeIndex, int interactionIndex, int button, ItemStack heldItem) {
        this.menu.SendMessage(LazyPacketData.builder().setInt("TradeIndex", tradeIndex).setInt("InteractionType", 1).setInt("InteractionIndex", interactionIndex).setInt("Button", button).setCompound("HeldItem", heldItem.save(new CompoundTag())));
    }

    public void sendOtherInteractionMessage(int tradeIndex, int mouseX, int mouseY, int button, ItemStack heldItem) {
        this.menu.SendMessage(LazyPacketData.builder().setInt("TradeIndex", tradeIndex).setInt("InteractionType", 2).setInt("Button", button).setInt("MouseX", mouseX).setInt("MouseY", mouseY).setCompound("HeldItem", heldItem.save(new CompoundTag())));
    }

    public void addTrade() {
        if (this.menu.getTrader() != null) {
            this.menu.getTrader().addTrade(this.menu.getPlayer());
            if (this.menu.isClient()) {
                this.menu.SendMessage(LazyPacketData.simpleFlag("AddTrade"));
            }
        }
    }

    public void removeTrade() {
        if (this.menu.getTrader() != null) {
            this.menu.getTrader().removeTrade(this.menu.getPlayer());
            if (this.menu.isClient()) {
                this.menu.SendMessage(LazyPacketData.simpleFlag("RemoveTrade"));
            }
        }
    }

    @Override
    public void receiveMessage(LazyPacketData message) {
        if (message.contains("TradeIndex", (byte) 2)) {
            if (!this.menu.hasPermission("editTrades")) {
                return;
            }
            int tradeIndex = message.getInt("TradeIndex");
            int interaction = message.getInt("InteractionType");
            int interactionIndex = message.contains("InteractionIndex", (byte) 2) ? message.getInt("InteractionIndex") : 0;
            int button = message.getInt("Button");
            int mouseX = message.contains("MouseX", (byte) 2) ? message.getInt("MouseX") : 0;
            int mouseY = message.contains("MouseY", (byte) 2) ? message.getInt("MouseY") : 0;
            ItemStack heldItem = ItemStack.of(message.getNBT("HeldItem"));
            TradeData trade = (TradeData) this.menu.getTrader().getTradeData().get(tradeIndex);
            switch(interaction) {
                case 0:
                    trade.OnInputDisplayInteraction(this, this.clientHandler, interactionIndex, button, heldItem);
                    break;
                case 1:
                    trade.OnOutputDisplayInteraction(this, this.clientHandler, interactionIndex, button, heldItem);
                    break;
                case 2:
                    trade.OnInteraction(this, this.clientHandler, mouseX, mouseY, button, heldItem);
                    break;
                default:
                    LightmansCurrency.LogWarning("Interaction Type " + interaction + " is not a valid interaction.");
            }
            this.menu.getTrader().markTradesDirty();
        }
        if (message.contains("AddTrade")) {
            this.addTrade();
        }
        if (message.contains("RemoveTrade")) {
            this.removeTrade();
        }
    }
}