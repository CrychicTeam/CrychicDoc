package dev.xkmc.modulargolems.content.menu.target;

import dev.xkmc.l2library.base.menu.base.SpriteManager;
import dev.xkmc.modulargolems.content.capability.GolemConfigEditor;
import dev.xkmc.modulargolems.content.capability.TargetFilterEditor;
import dev.xkmc.modulargolems.content.capability.TargetFilterLine;
import dev.xkmc.modulargolems.content.item.card.TargetFilterCard;
import dev.xkmc.modulargolems.content.menu.ghost.GhostItemMenu;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

public class TargetConfigMenu extends GhostItemMenu {

    public static final SpriteManager MANAGER = new SpriteManager("modulargolems", "config_target");

    protected final TargetFilterEditor editor;

    public static TargetConfigMenu fromNetwork(MenuType<TargetConfigMenu> type, int wid, Inventory plInv, FriendlyByteBuf buf) {
        UUID id = buf.readUUID();
        int color = buf.readInt();
        return new TargetConfigMenu(type, wid, plInv, new SimpleContainer(36), GolemConfigEditor.readable(id, color).target());
    }

    protected TargetConfigMenu(MenuType<?> type, int wid, Inventory plInv, Container container, TargetFilterEditor editor) {
        super(type, wid, plInv, MANAGER, container);
        this.editor = editor;
        this.addSlot("hostile", e -> e.getItem() instanceof TargetFilterCard);
        this.addSlot("friendly", e -> e.getItem() instanceof TargetFilterCard);
    }

    protected TargetFilterLine getContainer(int slot) {
        return slot < 18 ? this.getConfig().targetHostile() : this.getConfig().targetFriendly();
    }

    protected TargetFilterEditor getConfig() {
        return this.editor;
    }
}