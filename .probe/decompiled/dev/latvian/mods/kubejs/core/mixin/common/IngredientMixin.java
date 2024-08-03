package dev.latvian.mods.kubejs.core.mixin.common;

import dev.latvian.mods.kubejs.core.IngredientKJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@RemapPrefixForJS("kjs$")
@Mixin({ Ingredient.class })
public abstract class IngredientMixin implements IngredientKJS {

    @Override
    public Ingredient kjs$self() {
        return (Ingredient) this;
    }

    @Shadow
    @HideFromJS
    public abstract ItemStack[] getItems();
}