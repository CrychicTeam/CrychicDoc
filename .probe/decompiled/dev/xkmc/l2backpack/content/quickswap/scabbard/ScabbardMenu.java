package dev.xkmc.l2backpack.content.quickswap.scabbard;

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

public class ScabbardMenu extends BaseBagMenu<ScabbardMenu> {

    public static final SpriteManager MANAGERS = new SpriteManager("l2backpack", "backpack_1");

    public static ScabbardMenu fromNetwork(MenuType<ScabbardMenu> type, int windowId, Inventory inv, FriendlyByteBuf buf) {
        PlayerSlot<?> slot = PlayerSlot.read(buf);
        UUID id = buf.readUUID();
        return new ScabbardMenu(windowId, inv, slot, id, null);
    }

    public ScabbardMenu(int windowId, Inventory inventory, PlayerSlot<?> hand, UUID uuid, @Nullable Component title) {
        super((MenuType<ScabbardMenu>) BackpackMenus.MT_TOOL.get(), windowId, inventory, MANAGERS, hand, uuid, 1);
    }
}