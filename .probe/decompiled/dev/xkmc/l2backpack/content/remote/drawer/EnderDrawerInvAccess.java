package dev.xkmc.l2backpack.content.remote.drawer;

import dev.xkmc.l2backpack.content.drawer.BaseDrawerInvAccess;
import dev.xkmc.l2backpack.content.drawer.BaseDrawerItem;
import dev.xkmc.l2backpack.content.remote.common.DrawerAccess;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public record EnderDrawerInvAccess(ItemStack drawerStack, EnderDrawerItem drawerItem, ServerPlayer player) implements BaseDrawerInvAccess {

    @Override
    public int getStoredCount() {
        return DrawerAccess.of(this.player().m_9236_(), this.drawerStack()).getCount();
    }

    @Override
    public boolean isEmpty() {
        return BaseDrawerItem.getItem(this.drawerStack) == Items.AIR;
    }

    @Override
    public void setStoredCount(int count) {
        DrawerAccess.of(this.player().m_9236_(), this.drawerStack()).setCount(count);
    }
}