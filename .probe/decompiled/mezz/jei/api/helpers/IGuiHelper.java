package mezz.jei.api.helpers;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableBuilder;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public interface IGuiHelper {

    default IDrawableStatic createDrawable(ResourceLocation resourceLocation, int u, int v, int width, int height) {
        return this.drawableBuilder(resourceLocation, u, v, width, height).build();
    }

    IDrawableBuilder drawableBuilder(ResourceLocation var1, int var2, int var3, int var4, int var5);

    IDrawableAnimated createAnimatedDrawable(IDrawableStatic var1, int var2, IDrawableAnimated.StartDirection var3, boolean var4);

    IDrawableStatic getSlotDrawable();

    IDrawableStatic createBlankDrawable(int var1, int var2);

    default IDrawable createDrawableItemStack(ItemStack ingredient) {
        return this.createDrawableIngredient(VanillaTypes.ITEM_STACK, ingredient);
    }

    <V> IDrawable createDrawableIngredient(IIngredientType<V> var1, V var2);

    ICraftingGridHelper createCraftingGridHelper();

    ITickTimer createTickTimer(int var1, int var2, boolean var3);
}