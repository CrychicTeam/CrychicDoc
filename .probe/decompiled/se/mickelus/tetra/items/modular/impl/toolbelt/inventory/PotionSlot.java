package se.mickelus.tetra.items.modular.impl.toolbelt.inventory;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.item.ItemStack;

@ParametersAreNonnullByDefault
public class PotionSlot extends PredicateSlot {

    public PotionSlot(PotionsInventory inventory, int index, int x, int y) {
        super(inventory, index, x, y, inventory::isItemValid);
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 64;
    }

    @Override
    public ItemStack remove(int amount) {
        ItemStack currentItem = this.m_7993_();
        return !currentItem.isEmpty() ? super.m_6201_(Math.min(currentItem.getMaxStackSize(), amount)) : super.m_6201_(amount);
    }
}