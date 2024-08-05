package dev.xkmc.modulargolems.content.menu.equipment;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.menu.registry.IMenuPvd;
import dev.xkmc.modulargolems.init.registrate.GolemMiscs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.network.NetworkHooks;

public record EquipmentsMenuPvd(AbstractGolemEntity<?, ?> e) implements IMenuPvd {

    @Override
    public Component getDisplayName() {
        return this.e.m_5446_();
    }

    @Override
    public void writeBuffer(FriendlyByteBuf buf) {
        buf.writeInt(this.e.m_19879_());
    }

    public void open(ServerPlayer player) {
        NetworkHooks.openScreen(player, this, this::writeBuffer);
    }

    @Override
    public AbstractContainerMenu createMenu(int wid, Inventory inv, Player player) {
        return new EquipmentsMenu((MenuType<?>) GolemMiscs.EQUIPMENTS.get(), wid, inv, this.e);
    }
}