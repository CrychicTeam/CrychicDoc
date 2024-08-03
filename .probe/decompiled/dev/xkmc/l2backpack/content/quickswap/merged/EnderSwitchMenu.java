package dev.xkmc.l2backpack.content.quickswap.merged;

import dev.xkmc.l2backpack.content.common.BaseBagMenu;
import dev.xkmc.l2backpack.init.registrate.BackpackMenus;
import dev.xkmc.l2library.base.menu.base.SpriteManager;
import dev.xkmc.l2screentracker.screen.source.PlayerSlot;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class EnderSwitchMenu extends BaseBagMenu<EnderSwitchMenu> {

    public static final SpriteManager MANAGERS = new SpriteManager("l2backpack", "ender_switch");

    private int enderAdded = 0;

    public static EnderSwitchMenu fromNetwork(MenuType<EnderSwitchMenu> type, int windowId, Inventory inv, FriendlyByteBuf buf) {
        PlayerSlot<?> slot = PlayerSlot.read(buf);
        UUID id = buf.readUUID();
        return new EnderSwitchMenu(windowId, inv, new SimpleContainer(27), slot, id, null);
    }

    public EnderSwitchMenu(int windowId, Inventory inventory, Container ender, PlayerSlot<?> hand, UUID uuid, @Nullable Component title) {
        super((MenuType<EnderSwitchMenu>) BackpackMenus.MT_ES.get(), windowId, inventory, MANAGERS, hand, uuid, 3);
        this.addEnderSlot(ender);
        this.addSlot("arrow");
        this.addSlot("tool");
        this.addSlot("armor");
    }

    protected void addEnderSlot(Container ender) {
        this.sprite.get().getSlot("ender", (x, y) -> new Slot(ender, this.enderAdded++, x, y), (x$0, x$1, x$2, x$3) -> this.addSlot(x$0, x$1, x$2, x$3));
    }

    @Override
    public ItemStack quickMoveStack(Player pl, int id) {
        ItemStack stack = ((Slot) this.f_38839_.get(id)).getItem();
        if (id >= 36) {
            this.m_38903_(stack, 0, 36, true);
        } else if (!this.m_38903_(stack, 63, 90, false)) {
            this.m_38903_(stack, 36, 63, false);
        }
        ((Slot) this.f_38839_.get(id)).setChanged();
        return ItemStack.EMPTY;
    }
}