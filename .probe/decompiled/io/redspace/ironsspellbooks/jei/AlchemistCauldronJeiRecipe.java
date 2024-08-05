package io.redspace.ironsspellbooks.jei;

import java.util.List;
import net.minecraft.world.item.ItemStack;

public record AlchemistCauldronJeiRecipe(List<ItemStack> inputs, List<ItemStack> outputs, List<ItemStack> catalysts) {

    public AlchemistCauldronJeiRecipe(List<ItemStack> inputs, List<ItemStack> outputs, List<ItemStack> catalysts) {
        this.inputs = List.copyOf(inputs);
        this.outputs = List.copyOf(outputs);
        this.catalysts = List.copyOf(catalysts);
    }
}