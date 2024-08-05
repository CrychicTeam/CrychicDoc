package dev.xkmc.modulargolems.content.menu.path;

import dev.xkmc.l2library.base.menu.base.SpriteManager;
import dev.xkmc.modulargolems.content.capability.GolemConfigEditor;
import dev.xkmc.modulargolems.content.capability.PathEditor;
import dev.xkmc.modulargolems.content.menu.ghost.GhostItemMenu;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

public class PathConfigMenu extends GhostItemMenu {

    public static final SpriteManager MANAGER = new SpriteManager("modulargolems", "config_path");

    protected final PathEditor editor;

    public static PathConfigMenu fromNetwork(MenuType<PathConfigMenu> type, int wid, Inventory plInv, FriendlyByteBuf buf) {
        UUID id = buf.readUUID();
        int color = buf.readInt();
        return new PathConfigMenu(type, wid, plInv, new SimpleContainer(27), GolemConfigEditor.readable(id, color).getPath());
    }

    protected PathConfigMenu(MenuType<?> type, int wid, Inventory plInv, Container container, PathEditor editor) {
        super(type, wid, plInv, MANAGER, container);
        this.editor = editor;
        this.addSlot("grid", e -> true);
    }

    protected PathEditor getContainer(int slot) {
        return this.editor;
    }

    protected PathEditor getConfig() {
        return this.editor;
    }
}