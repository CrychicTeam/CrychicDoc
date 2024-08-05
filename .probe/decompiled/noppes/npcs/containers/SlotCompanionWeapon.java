package noppes.npcs.containers;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.roles.RoleCompanion;

class SlotCompanionWeapon extends Slot {

    final RoleCompanion role;

    public SlotCompanionWeapon(RoleCompanion role, Container iinventory, int id, int x, int y) {
        super(iinventory, id, x, y);
        this.role = role;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean mayPlace(ItemStack itemstack) {
        return NoppesUtilServer.IsItemStackNull(itemstack) ? false : this.role.canWearSword(NpcAPI.Instance().getIItemStack(itemstack));
    }
}