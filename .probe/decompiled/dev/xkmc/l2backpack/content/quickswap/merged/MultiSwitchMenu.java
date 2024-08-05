package dev.xkmc.l2backpack.content.quickswap.merged;

import dev.xkmc.l2backpack.content.common.BaseBagMenu;
import dev.xkmc.l2backpack.init.registrate.BackpackMenus;
import dev.xkmc.l2library.base.menu.base.SpriteManager;
import dev.xkmc.l2screentracker.screen.source.PlayerSlot;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

public class MultiSwitchMenu extends BaseBagMenu<MultiSwitchMenu> {

    public static final SpriteManager MANAGERS = new SpriteManager("l2backpack", "multi_switch");

    public static MultiSwitchMenu fromNetwork(MenuType<MultiSwitchMenu> type, int windowId, Inventory inv, FriendlyByteBuf buf) {
        PlayerSlot<?> slot = PlayerSlot.read(buf);
        UUID id = buf.readUUID();
        return new MultiSwitchMenu(windowId, inv, slot, id, null);
    }

    public MultiSwitchMenu(int windowId, Inventory inventory, PlayerSlot<?> hand, UUID uuid, @Nullable Component title) {
        super((MenuType<MultiSwitchMenu>) BackpackMenus.MT_MULTI.get(), windowId, inventory, MANAGERS, hand, uuid, 3);
        this.addSlot("arrow");
        this.addSlot("tool");
        this.addSlot("armor");
    }
}