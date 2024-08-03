package dev.xkmc.modulargolems.content.menu.filter;

import dev.xkmc.modulargolems.content.capability.GolemConfigEditor;
import dev.xkmc.modulargolems.content.capability.GolemConfigEntry;
import dev.xkmc.modulargolems.content.capability.PickupFilterEditor;
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

public record ItemConfigMenuProvider(UUID id, int color, GolemConfigEditor editor) implements IMenuPvd {

    public static ItemConfigMenuProvider fromPacket(ServerLevel level, GolemConfigEntry entry) {
        return new ItemConfigMenuProvider(entry.getID(), entry.getColor(), new GolemConfigEditor.Writable(level, entry));
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
        PickupFilterEditor filter = this.editor().getFilter();
        return new ItemConfigMenu((MenuType<?>) GolemMiscs.CONFIG_PICKUP.get(), wid, inv, filter, filter);
    }
}