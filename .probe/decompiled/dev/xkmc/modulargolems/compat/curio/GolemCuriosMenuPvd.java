package dev.xkmc.modulargolems.compat.curio;

import dev.xkmc.l2tabs.compat.CuriosWrapper;
import dev.xkmc.modulargolems.content.menu.registry.IMenuPvd;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public record GolemCuriosMenuPvd(LivingEntity e, int page) implements IMenuPvd {

    @Override
    public Component getDisplayName() {
        return this.e.m_5446_();
    }

    @Override
    public void writeBuffer(FriendlyByteBuf buf) {
        buf.writeInt(this.e.m_19879_());
        buf.writeInt(this.page);
    }

    @Override
    public AbstractContainerMenu createMenu(int wid, Inventory inv, Player player) {
        CurioCompatRegistry compat = CurioCompatRegistry.get();
        return compat == null ? null : new GolemCuriosListMenu((MenuType<?>) compat.menuType.get(), wid, inv, new CuriosWrapper(this.e, this.page));
    }
}