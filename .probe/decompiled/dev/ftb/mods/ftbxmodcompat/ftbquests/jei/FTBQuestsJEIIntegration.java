package dev.ftb.mods.ftbxmodcompat.ftbquests.jei;

import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbxmodcompat.FTBXModCompat;
import dev.ftb.mods.ftbxmodcompat.ftbquests.QuestItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.registration.IAdvancedRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@JeiPlugin
public class FTBQuestsJEIIntegration implements IModPlugin {

    private static final ResourceLocation UID = new ResourceLocation("ftbxmodcompat", "ftbquests_jei");

    public static IJeiRuntime runtime;

    @Override
    public void onRuntimeAvailable(IJeiRuntime r) {
        runtime = FTBXModCompat.isFTBQuestsLoaded ? r : null;
        if (ClientQuestFile.exists()) {
            ClientQuestFile.INSTANCE.updateLootCrates();
        }
    }

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration r) {
        if (FTBXModCompat.isFTBQuestsLoaded) {
            r.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, QuestItems.lootCrate(), (stack, uidContext) -> stack.hasTag() ? stack.getTag().getString("type") : "");
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        if (FTBXModCompat.isFTBQuestsLoaded) {
            registration.addRecipeCatalyst(new ItemStack(QuestItems.questBook()), JEIRecipeTypes.QUEST);
            registration.addRecipeCatalyst(new ItemStack(QuestItems.lootCrate()), JEIRecipeTypes.LOOT_CRATE);
        }
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration r) {
        if (FTBXModCompat.isFTBQuestsLoaded) {
            r.addRecipeCategories(new QuestCategory(r.getJeiHelpers().getGuiHelper()));
            r.addRecipeCategories(new LootCrateCategory(r.getJeiHelpers().getGuiHelper()));
        }
    }

    @Override
    public void registerAdvanced(IAdvancedRegistration registration) {
        if (FTBXModCompat.isFTBQuestsLoaded) {
            registration.addRecipeManagerPlugin(QuestRecipeManagerPlugin.INSTANCE);
            registration.addRecipeManagerPlugin(LootCrateRecipeManagerPlugin.INSTANCE);
        }
    }

    public static void showRecipes(ItemStack stack) {
        if (runtime != null) {
            runtime.getIngredientManager().getIngredientTypeChecked(stack).ifPresent(type -> runtime.getRecipesGui().show(runtime.getJeiHelpers().getFocusFactory().createFocus(RecipeIngredientRole.OUTPUT, type, stack)));
        }
    }
}