package dev.xkmc.l2backpack.content.click;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class DoubleClickHandler {

    public static boolean handle(AbstractContainerMenu self, Slot slot, ItemStack stack, Player player, int btn) {
        if (!stack.isEmpty() && (!slot.hasItem() || !slot.mayPickup(player))) {
            if (!(stack.getItem() instanceof DoubleClickItem clicker)) {
                return false;
            } else {
                int start = btn == 0 ? 0 : self.slots.size() - 1;
                int dir = btn == 0 ? 1 : -1;
                for (int index = start; index >= 0 && index < self.slots.size() && clicker.remainingSpace(stack) > 0; index += dir) {
                    Slot src = self.slots.get(index);
                    if (src.hasItem() && clicker.canAbsorb(src, stack) && src.mayPickup(player) && self.canTakeItemForPickAll(stack, src)) {
                        ItemStack srcStack = src.getItem();
                        ItemStack taken = src.safeTake(srcStack.getCount(), clicker.remainingSpace(stack), player);
                        clicker.mergeStack(stack, taken);
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }
}