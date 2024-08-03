package dev.xkmc.modulargolems.content.menu.target;

import dev.xkmc.modulargolems.content.capability.GolemConfigEditor;
import dev.xkmc.modulargolems.content.capability.GolemConfigEntry;
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

public record TargetConfigMenuProvider(UUID id, int color, GolemConfigEditor editor) implements IMenuPvd {

    public static TargetConfigMenuProvider fromPacket(ServerLevel level, GolemConfigEntry entry) {
        return new TargetConfigMenuProvider(entry.getID(), entry.getColor(), new GolemConfigEditor.Writable(level, entry));
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
        return new TargetConfigMenu((MenuType<?>) GolemMiscs.CONFIG_TARGET.get(), wid, inv, this.editor.target(), this.editor.target());
    }
}