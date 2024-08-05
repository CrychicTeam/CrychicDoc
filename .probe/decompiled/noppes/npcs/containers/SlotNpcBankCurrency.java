package noppes.npcs.containers;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.NoppesUtilServer;

public class SlotNpcBankCurrency extends Slot {

    public ItemStack item = ItemStack.EMPTY;

    public SlotNpcBankCurrency(ContainerNPCBankInterface containerplayer, Container iinventory, int i, int j, int k) {
        super(iinventory, i, j, k);
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public boolean mayPlace(ItemStack itemstack) {
        return NoppesUtilServer.IsItemStackNull(itemstack) ? false : this.item.getItem() == itemstack.getItem();
    }
}