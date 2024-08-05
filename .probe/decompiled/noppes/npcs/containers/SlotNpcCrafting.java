package noppes.npcs.containers;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.NoppesUtilServer;

public class SlotNpcCrafting extends ResultSlot {

    private final CraftingContainer craftMatrix;

    public SlotNpcCrafting(Player player, CraftingContainer craftingInventory, Container inventory, int slotIndex, int x, int y) {
        super(player, craftingInventory, inventory, slotIndex, x, y);
        this.craftMatrix = craftingInventory;
    }

    @Override
    public void onTake(Player player, ItemStack itemStack) {
        this.m_5845_(itemStack);
        for (int i = 0; i < this.craftMatrix.m_6643_(); i++) {
            ItemStack itemstack1 = this.craftMatrix.m_8020_(i);
            if (!NoppesUtilServer.IsItemStackNull(itemstack1)) {
                this.craftMatrix.m_7407_(i, 1);
                if (itemstack1.getItem().hasCraftingRemainingItem(itemstack1)) {
                    ItemStack itemstack2 = itemstack1.getItem().getCraftingRemainingItem(itemstack1);
                    if ((NoppesUtilServer.IsItemStackNull(itemstack2) || !itemstack2.isDamageableItem() || itemstack2.getDamageValue() <= itemstack2.getMaxDamage()) && !player.getInventory().add(itemstack2)) {
                        if (NoppesUtilServer.IsItemStackNull(this.craftMatrix.m_8020_(i))) {
                            this.craftMatrix.m_6836_(i, itemstack2);
                        } else {
                            player.drop(itemstack2, false);
                        }
                    }
                }
            }
        }
    }
}