package dev.xkmc.modulargolems.content.menu.filter;

import dev.xkmc.l2library.base.menu.base.SpriteManager;
import dev.xkmc.modulargolems.content.capability.GolemConfigEditor;
import dev.xkmc.modulargolems.content.capability.PickupFilterEditor;
import dev.xkmc.modulargolems.content.menu.ghost.GhostItemMenu;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

public class ItemConfigMenu extends GhostItemMenu {

    public static final SpriteManager MANAGER = new SpriteManager("modulargolems", "config_pickup");

    protected final PickupFilterEditor editor;

    public static ItemConfigMenu fromNetwork(MenuType<ItemConfigMenu> type, int wid, Inventory plInv, FriendlyByteBuf buf) {
        UUID id = buf.readUUID();
        int color = buf.readInt();
        return new ItemConfigMenu(type, wid, plInv, new SimpleContainer(27), GolemConfigEditor.readable(id, color).getFilter());
    }

    protected ItemConfigMenu(MenuType<?> type, int wid, Inventory plInv, Container container, PickupFilterEditor editor) {
        super(type, wid, plInv, MANAGER, container);
        this.editor = editor;
        this.addSlot("grid", e -> true);
    }

    protected PickupFilterEditor getContainer(int slot) {
        return this.editor;
    }

    protected PickupFilterEditor getConfig() {
        return this.editor;
    }
}