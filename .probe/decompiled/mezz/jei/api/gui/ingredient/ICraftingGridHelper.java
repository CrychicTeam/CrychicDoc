package mezz.jei.api.gui.ingredient;

import java.util.List;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface ICraftingGridHelper {

    default List<IRecipeSlotBuilder> createAndSetInputs(IRecipeLayoutBuilder builder, List<List<ItemStack>> inputs, int width, int height) {
        return this.createAndSetInputs(builder, VanillaTypes.ITEM_STACK, inputs, width, height);
    }

    <T> List<IRecipeSlotBuilder> createAndSetInputs(IRecipeLayoutBuilder var1, IIngredientType<T> var2, List<List<T>> var3, int var4, int var5);

    <T> void setInputs(List<IRecipeSlotBuilder> var1, IIngredientType<T> var2, List<List<T>> var3, int var4, int var5);

    default IRecipeSlotBuilder createAndSetOutputs(IRecipeLayoutBuilder builder, @Nullable List<ItemStack> outputs) {
        return this.createAndSetOutputs(builder, VanillaTypes.ITEM_STACK, outputs);
    }

    <T> IRecipeSlotBuilder createAndSetOutputs(IRecipeLayoutBuilder var1, IIngredientType<T> var2, @Nullable List<T> var3);
}