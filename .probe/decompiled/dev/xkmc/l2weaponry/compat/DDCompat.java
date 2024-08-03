package dev.xkmc.l2weaponry.compat;

import com.kyanite.deeperdarker.content.DDItems;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2weaponry.init.data.RecipeGen;
import dev.xkmc.l2weaponry.init.materials.LWToolMats;
import dev.xkmc.l2weaponry.init.materials.LWToolTypes;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;

public class DDCompat {

    public static void onRecipeGen(RegistrateRecipeProvider pvd) {
        LWToolMats mat = LWToolMats.SCULKIUM;
        LWToolMats base = LWToolMats.NETHERITE;
        Consumer<FinishedRecipe> cond = mat.getProvider(pvd, new ICondition[] { new ModLoadedCondition("deeperdarker") });
        for (LWToolTypes t : LWToolTypes.values()) {
            if (mat.hasTool(t) && base.hasTool(t)) {
                RecipeGen.<SmithingTransformRecipeBuilder>unlock(pvd, SmithingTransformRecipeBuilder.smithing(Ingredient.of((ItemLike) DDItems.WARDEN_UPGRADE_SMITHING_TEMPLATE.get()), Ingredient.of(base.getTool(t)), Ingredient.of((ItemLike) DDItems.REINFORCED_ECHO_SHARD.get()), RecipeCategory.COMBAT, mat.getTool(t))::m_266439_, mat.getBlock()).save(cond, RecipeGen.getID(mat.getTool(t)));
            }
        }
    }
}