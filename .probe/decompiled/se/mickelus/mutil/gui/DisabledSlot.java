package se.mickelus.mutil.gui;

import javax.annotation.Nullable;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class DisabledSlot extends Slot {

    public DisabledSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return false;
    }

    @Override
    public boolean mayPlace(@Nullable ItemStack stack) {
        return false;
    }
}