package dev.xkmc.l2hostility.content.menu.equipments;

import dev.xkmc.l2hostility.init.registrate.LHMiscs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public record EquipmentsMenuPvd(Mob e) implements MenuProvider {

    @Override
    public Component getDisplayName() {
        return this.e.m_5446_();
    }

    public void writeBuffer(FriendlyByteBuf buf) {
        buf.writeInt(this.e.m_19879_());
    }

    public void open(ServerPlayer player) {
        NetworkHooks.openScreen(player, this, this::writeBuffer);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int wid, Inventory inv, Player player) {
        return new EquipmentsMenu((MenuType<?>) LHMiscs.EQUIPMENTS.get(), wid, inv, this.e);
    }
}