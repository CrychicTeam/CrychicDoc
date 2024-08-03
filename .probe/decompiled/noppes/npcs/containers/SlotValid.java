package noppes.npcs.containers;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SlotValid extends Slot {

    private boolean canPutIn = true;

    public SlotValid(Container par1iInventory, int limbSwingAmount, int par3, int par4) {
        super(par1iInventory, limbSwingAmount, par3, par4);
    }

    public SlotValid(Container par1iInventory, int limbSwingAmount, int par3, int par4, boolean bo) {
        super(par1iInventory, limbSwingAmount, par3, par4);
        this.canPutIn = bo;
    }

    @Override
    public boolean mayPlace(ItemStack itemstack) {
        return this.canPutIn && this.f_40218_.canPlaceItem(0, itemstack);
    }
}