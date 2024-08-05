package dev.xkmc.modulargolems.content.menu.path;

import dev.xkmc.modulargolems.content.capability.GolemConfigEditor;
import dev.xkmc.modulargolems.content.capability.GolemConfigEntry;
import dev.xkmc.modulargolems.content.capability.PathEditor;
import dev.xkmc.modulargolems.content.menu.registry.IMenuPvd;
import dev.xkmc.modulargolems.init.registrate.GolemMiscs;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public record PathConfigMenuProvider(UUID id, int color, GolemConfigEditor editor) implements IMenuPvd {

    public static PathConfigMenuProvider fromPacket(ServerLevel level, GolemConfigEntry entry) {
        return new PathConfigMenuProvider(entry.getID(), entry.getColor(), new GolemConfigEditor.Writable(level, entry));
    }

    @Override
    public void writeBuffer(FriendlyByteBuf buf) {
        buf.writeUUID(this.id);
        buf.writeInt(this.color);
    }

    @Override
    public Component getDisplayName() {
        return this.editor().getDisplayName();
    }

    @Override
    public AbstractContainerMenu createMenu(int wid, Inventory inv, Player player) {
        PathEditor path = this.editor().getPath();
        return new PathConfigMenu((MenuType<?>) GolemMiscs.CONFIG_PATH.get(), wid, inv, path, path);
    }
}