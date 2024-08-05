package noppes.npcs.containers;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.NoppesUtilServer;

public class InventoryNpcTrader implements Container {

    private String inventoryTitle;

    private int slotsCount;

    public final NonNullList<ItemStack> inventoryContents;

    private ContainerNPCTrader con;

    public InventoryNpcTrader(String s, int i, ContainerNPCTrader con) {
        this.con = con;
        this.inventoryTitle = s;
        this.slotsCount = i;
        this.inventoryContents = NonNullList.withSize(i, ItemStack.EMPTY);
    }

    @Override
    public ItemStack getItem(int i) {
        ItemStack toBuy = this.inventoryContents.get(i);
        return NoppesUtilServer.IsItemStackNull(toBuy) ? ItemStack.EMPTY : toBuy.copy();
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        ItemStack stack = this.inventoryContents.get(i);
        return !NoppesUtilServer.IsItemStackNull(stack) ? stack.copy() : ItemStack.EMPTY;
    }

    @Override
    public void setItem(int i, ItemStack itemstack) {
        if (!itemstack.isEmpty()) {
            this.inventoryContents.set(i, itemstack.copy());
        }
        this.setChanged();
    }

    @Override
    public int getContainerSize() {
        return this.slotsCount;
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public boolean stillValid(Player entityplayer) {
        return true;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        return this.inventoryContents.set(i, ItemStack.EMPTY);
    }

    @Override
    public boolean canPlaceItem(int i, ItemStack itemstack) {
        return true;
    }

    @Override
    public void setChanged() {
        this.con.m_6199_(this);
    }

    @Override
    public void startOpen(Player player) {
    }

    @Override
    public void stopOpen(Player player) {
    }

    @Override
    public boolean isEmpty() {
        for (int slot = 0; slot < this.getContainerSize(); slot++) {
            ItemStack item = this.getItem(slot);
            if (!NoppesUtilServer.IsItemStackNull(item) && !item.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void clearContent() {
    }
}