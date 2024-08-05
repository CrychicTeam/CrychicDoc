package mezz.jei.common.platform;

import java.util.List;
import mezz.jei.api.helpers.IStackHelper;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public interface IPlatformIngredientHelper {

    Ingredient createShulkerDyeIngredient(DyeColor var1);

    Ingredient createNbtIngredient(ItemStack var1, IStackHelper var2);

    List<Ingredient> getPotionContainers();
}