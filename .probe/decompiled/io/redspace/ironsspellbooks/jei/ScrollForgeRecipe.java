package io.redspace.ironsspellbooks.jei;

import java.util.List;
import net.minecraft.world.item.ItemStack;

public record ScrollForgeRecipe(List<ItemStack> inkInputs, ItemStack paperInput, ItemStack focusInput, List<ItemStack> scrollOutputs) {

    public ScrollForgeRecipe(List<ItemStack> inkInputs, ItemStack paperInput, ItemStack focusInput, List<ItemStack> scrollOutputs) {
        this.inkInputs = List.copyOf(inkInputs);
        this.paperInput = paperInput;
        this.focusInput = focusInput;
        this.scrollOutputs = List.copyOf(scrollOutputs);
    }

    public boolean isValid() {
        return !this.inkInputs.isEmpty() && !this.scrollOutputs.isEmpty() && !this.paperInput.isEmpty() && !this.focusInput.isEmpty();
    }
}