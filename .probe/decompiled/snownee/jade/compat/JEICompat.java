package snownee.jade.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IFocusFactory;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.api.runtime.IRecipesGui;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import snownee.jade.JadeClient;
import snownee.jade.api.Accessor;
import snownee.jade.impl.ObjectDataCenter;

@JeiPlugin
public class JEICompat implements IModPlugin {

    public static final ResourceLocation ID = new ResourceLocation("jade", "main");

    private static IJeiRuntime runtime;

    private static IJeiHelpers helpers;

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        helpers = registration.getJeiHelpers();
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime runtime) {
        JEICompat.runtime = runtime;
    }

    public static void onKeyPressed(int action) {
        if (runtime != null && JadeClient.showRecipes != null && JadeClient.showUses != null) {
            if (action == 1) {
                boolean showRecipes = JadeClient.showRecipes.consumeClick();
                boolean showUses = JadeClient.showUses.consumeClick();
                if (showRecipes || showUses) {
                    Accessor<?> accessor = ObjectDataCenter.get();
                    if (accessor != null) {
                        ItemStack stack = accessor.getPickedResult();
                        if (!stack.isEmpty()) {
                            IRecipesGui gui = runtime.getRecipesGui();
                            IFocusFactory factory = helpers.getFocusFactory();
                            gui.show(factory.createFocus(showUses ? RecipeIngredientRole.INPUT : RecipeIngredientRole.OUTPUT, VanillaTypes.ITEM_STACK, stack));
                        }
                    }
                }
            }
        }
    }
}