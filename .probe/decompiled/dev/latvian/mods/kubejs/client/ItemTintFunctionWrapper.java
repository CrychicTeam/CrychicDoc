package dev.latvian.mods.kubejs.client;

import dev.latvian.mods.kubejs.item.ItemTintFunction;
import dev.latvian.mods.rhino.mod.util.color.Color;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;

public record ItemTintFunctionWrapper(ItemTintFunction function) implements ItemColor {

    @Override
    public int getColor(ItemStack stack, int index) {
        Color c = this.function.getColor(stack, index);
        return c == null ? -1 : c.getArgbJS();
    }
}