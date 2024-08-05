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

public class ArmorBagMenu extends BaseBagMenu<ArmorBagMenu> {

    public static final SpriteManager MANAGERS = new SpriteManager("l2backpack", "backpack_1");

    public static ArmorBagMenu fromNetwork(MenuType<ArmorBagMenu> type, int windowId, Inventory inv, FriendlyByteBuf buf) {
        PlayerSlot<?> slot = PlayerSlot.read(buf);
        UUID id = buf.readUUID();
        return new ArmorBagMenu(windowId, inv, slot, id, null);
    }

    public ArmorBagMenu(int windowId, Inventory inventory, PlayerSlot<?> hand, UUID uuid, @Nullable Component title) {
        super((MenuType<ArmorBagMenu>) BackpackMenus.MT_ARMOR.get(), windowId, inventory, MANAGERS, hand, uuid, 1);
    }
}