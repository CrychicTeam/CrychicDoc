package dev.xkmc.l2backpack.content.drawer;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public record DrawerInvAccess(ItemStack drawerStack, DrawerItem drawerItem) implements BaseDrawerInvAccess {

    @Override
    public ServerPlayer player() {
        return null;
    }

    @Override
    public int getStoredCount() {
        return DrawerItem.getCount(this.drawerStack());
    }

    @Override
    public boolean isEmpty() {
        return this.drawerItem().canSetNewItem(this.drawerStack());
    }

    @Override
    public void setStoredCount(int count) {
        DrawerItem.setCount(this.drawerStack(), count);
    }
}