package noppes.npcs.containers;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.roles.RoleFollower;

class SlotNpcMercenaryCurrency extends Slot {

    RoleFollower role;

    public SlotNpcMercenaryCurrency(RoleFollower role, Container inv, int i, int j, int k) {
        super(inv, i, j, k);
        this.role = role;
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public boolean mayPlace(ItemStack itemstack) {
        Item item = itemstack.getItem();
        for (ItemStack is : this.role.inventory.items) {
            if (item == is.getItem()) {
                return true;
            }
        }
        return false;
    }
}