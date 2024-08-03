package dev.xkmc.l2hostility.compat.curios;

import dev.xkmc.l2hostility.init.registrate.LHMiscs;
import dev.xkmc.l2tabs.compat.CuriosWrapper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.network.NetworkHooks;
import top.theillusivec4.curios.api.CuriosApi;

public record EntityCuriosMenuPvd(LivingEntity e, int page) implements MenuProvider {

    @Override
    public Component getDisplayName() {
        return this.e.m_5446_();
    }

    public void writeBuffer(FriendlyByteBuf buf) {
        buf.writeInt(this.e.m_19879_());
        buf.writeInt(this.page);
    }

    @Override
    public AbstractContainerMenu createMenu(int wid, Inventory inv, Player player) {
        return new EntityCuriosListMenu((MenuType<?>) LHMiscs.CURIOS.get(), wid, inv, new CuriosWrapper(this.e, this.page));
    }

    public void open(ServerPlayer player) {
        if (CuriosApi.getCuriosInventory(this.e).resolve().isPresent()) {
            NetworkHooks.openScreen(player, this, this::writeBuffer);
        }
    }
}