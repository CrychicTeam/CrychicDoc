package fuzs.puzzleslib.api.init.v2;

import fuzs.puzzleslib.impl.core.CommonFactories;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.Ingredient;

public interface PotionBrewingRegistry {

    PotionBrewingRegistry INSTANCE = CommonFactories.INSTANCE.getPotionBrewingRegistry();

    void registerPotionContainer(PotionItem var1);

    default void registerContainerRecipe(PotionItem from, Item ingredient, PotionItem to) {
        this.registerContainerRecipe(from, Ingredient.of(ingredient), to);
    }

    void registerContainerRecipe(PotionItem var1, Ingredient var2, PotionItem var3);

    default void registerPotionRecipe(Potion from, Item ingredient, Potion to) {
        this.registerPotionRecipe(from, Ingredient.of(ingredient), to);
    }

    void registerPotionRecipe(Potion var1, Ingredient var2, Potion var3);
}