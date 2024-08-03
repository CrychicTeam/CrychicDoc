package dev.xkmc.modulargolems.content.menu.config;

import dev.xkmc.l2library.base.menu.base.BaseContainerMenu;
import dev.xkmc.l2library.base.menu.base.SpriteManager;
import dev.xkmc.modulargolems.content.capability.GolemConfigEditor;
import dev.xkmc.modulargolems.content.item.golem.GolemHolder;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

public class ToggleGolemConfigMenu extends BaseContainerMenu<ToggleGolemConfigMenu> {

    protected static final SpriteManager MANAGER = new SpriteManager("modulargolems", "config_toggle");

    final GolemConfigEditor editor;

    public static ToggleGolemConfigMenu fromNetwork(MenuType<ToggleGolemConfigMenu> type, int wid, Inventory inv, FriendlyByteBuf buf) {
        UUID id = buf.readUUID();
        int color = buf.readInt();
        return new ToggleGolemConfigMenu(type, wid, inv, GolemConfigEditor.readable(id, color));
    }

    protected ToggleGolemConfigMenu(MenuType<?> type, int wid, Inventory plInv, GolemConfigEditor editor) {
        super(type, wid, plInv, MANAGER, menu -> new BaseContainerMenu.BaseContainer<>(1, menu), true);
        this.editor = editor;
        this.addSlot("hand", e -> e.getItem() instanceof GolemHolder);
    }

    @Override
    protected void securedServerSlotChange(Container cont) {
        GolemHolder.setGolemConfig(cont.getItem(0), this.editor.entry().getID(), this.editor.entry().getColor());
    }
}