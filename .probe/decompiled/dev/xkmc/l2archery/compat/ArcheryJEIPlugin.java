package dev.xkmc.l2archery.compat;

import dev.xkmc.l2archery.content.item.GenericBowItem;
import dev.xkmc.l2archery.content.upgrade.Upgrade;
import dev.xkmc.l2archery.content.upgrade.UpgradeItem;
import dev.xkmc.l2archery.events.GenericEventHandler;
import dev.xkmc.l2archery.init.registrate.ArcheryItems;
import dev.xkmc.l2archery.init.registrate.ArcheryRegister;
import dev.xkmc.l2library.base.NamedEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@JeiPlugin
public class ArcheryJEIPlugin implements IModPlugin {

    public static final ResourceLocation ID = new ResourceLocation("l2archery", "main");

    public static final ResourceLocation NONE = new ResourceLocation("l2archery", "empty");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter((Item) ArcheryItems.UPGRADE.get(), (stack, ctx) -> ((ResourceLocation) Optional.ofNullable(UpgradeItem.getUpgrade(stack)).map(NamedEntry::getRegistryName).orElse(NONE)).toString());
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<IJeiAnvilRecipe> recipes = new ArrayList();
        this.addUpgradeRecipes(recipes, registration.getVanillaRecipeFactory());
        registration.addRecipes(RecipeTypes.ANVIL, recipes);
    }

    private void addUpgradeRecipes(List<IJeiAnvilRecipe> recipes, IVanillaRecipeFactory factory) {
        for (Upgrade upgrade : ArcheryRegister.UPGRADE.get().getValues()) {
            ItemStack stack = ArcheryItems.UPGRADE.asStack();
            UpgradeItem.setUpgrade(stack, upgrade);
            for (GenericBowItem bow : ArcheryItems.BOW_LIKE) {
                ItemStack left = bow.m_7968_();
                if (GenericEventHandler.allowUpgrade(bow, left, upgrade)) {
                    ItemStack right = bow.m_7968_();
                    GenericBowItem.addUpgrade(right, upgrade);
                    recipes.add(factory.createAnvilRecipe(left, List.of(stack), List.of(right)));
                }
            }
        }
    }
}