package dev.xkmc.l2backpack.content.quickswap.armorswap;

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

public class ArmorSetBagMenu extends BaseBagMenu<ArmorSetBagMenu> {

    public static final SpriteManager MANAGERS = new SpriteManager("l2backpack", "backpack_4");

    public static ArmorSetBagMenu fromNetwork(MenuType<ArmorSetBagMenu> type, int windowId, Inventory inv, FriendlyByteBuf buf) {
        PlayerSlot<?> slot = PlayerSlot.read(buf);
        UUID id = buf.readUUID();
        return new ArmorSetBagMenu(windowId, inv, slot, id, null);
    }

    public ArmorSetBagMenu(int windowId, Inventory inventory, PlayerSlot<?> hand, UUID uuid, @Nullable Component title) {
        super((MenuType<ArmorSetBagMenu>) BackpackMenus.MT_ARMOR_SET.get(), windowId, inventory, MANAGERS, hand, uuid, 4);
    }

    @Override
    protected void addSlot(String name) {
        this.sprite.get().getSlot(name, (x, y) -> new ArmorSetBagSlot(this.handler, this.added++, x, y), (x$0, x$1, x$2, x$3) -> this.addSlot(x$0, x$1, x$2, x$3));
    }
}