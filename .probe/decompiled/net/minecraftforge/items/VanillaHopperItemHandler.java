package net.minecraftforge.items;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;

public class VanillaHopperItemHandler extends InvWrapper {

    private final HopperBlockEntity hopper;

    public VanillaHopperItemHandler(HopperBlockEntity hopper) {
        super(hopper);
        this.hopper = hopper;
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (simulate) {
            return super.insertItem(slot, stack, simulate);
        } else {
            boolean wasEmpty = this.getInv().isEmpty();
            int originalStackSize = stack.getCount();
            stack = super.insertItem(slot, stack, simulate);
            if (wasEmpty && originalStackSize > stack.getCount() && !this.hopper.isOnCustomCooldown()) {
                this.hopper.setCooldown(8);
            }
            return stack;
        }
    }
}