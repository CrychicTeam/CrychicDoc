package se.mickelus.mutil.gui;

import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ToggleableSlot extends SlotItemHandler {

    private boolean isEnabled = true;

    private int realX;

    private int realY;

    public ToggleableSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.realX = xPosition;
        this.realY = yPosition;
    }

    public void toggle(boolean enabled) {
        this.isEnabled = enabled;
    }

    @Override
    public boolean isActive() {
        return this.isEnabled;
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return this.isEnabled;
    }

    @Override
    public boolean mayPlace(@Nullable ItemStack stack) {
        return this.isEnabled;
    }
}