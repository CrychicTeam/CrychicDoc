package com.almostreliable.morejs.features.potion;

import com.almostreliable.morejs.mixin.potion.PotionBrewingAccessor;
import com.almostreliable.morejs.util.Utils;
import com.google.common.base.Preconditions;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import java.util.HashSet;
import java.util.ListIterator;
import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.commons.lang3.StringUtils;

public abstract class PotionBrewingRegisterEvent extends EventJS {

    protected void validate(Ingredient topInput, Ingredient bottomInput, ItemStack output) {
        Preconditions.checkArgument(topInput.getItems().length > 0, "Top input must have at least one item");
        Preconditions.checkArgument(bottomInput.getItems().length > 0, "Bottom input must have at least one item");
        Preconditions.checkArgument(!output.isEmpty(), "Output must not be empty");
    }

    protected void validateSimple(Potion from, Ingredient ingredient, Potion to) {
        Preconditions.checkNotNull(from, "Input potion must not be null");
        Preconditions.checkNotNull(ingredient, "Ingredient must not be null");
        Preconditions.checkNotNull(to, "Output potion must not be null");
        Preconditions.checkArgument(ingredient.getItems().length > 0, "Ingredient must have at least one item");
    }

    public abstract void addCustomBrewing(Ingredient var1, Ingredient var2, ItemStack var3);

    public abstract void addPotionBrewing(Ingredient var1, Potion var2, Potion var3);

    public void addPotionBrewing(Ingredient ingredient, Potion output) {
        this.addPotionBrewing(ingredient, Potions.WATER, output);
    }

    public void removeByPotion(@Nullable Potion input, @Nullable Ingredient ingredient, @Nullable Potion output) {
        PotionBrewingAccessor.getMixes().removeIf(mix -> {
            boolean matchesInput = input == null || this.getInputPotionFromMix(mix) == input;
            boolean matchesIngredient = ingredient == null || Utils.matchesIngredient(ingredient, mix.ingredient);
            boolean matchesOutput = output == null || this.getOutputPotionFromMix(mix) == output;
            boolean matches = matchesInput && matchesIngredient && matchesOutput;
            if (matches) {
                ConsoleJS.STARTUP.info("Removed potion brewing recipe: " + BuiltInRegistries.POTION.getKey(this.getInputPotionFromMix(mix)) + " + " + StringUtils.abbreviate(mix.ingredient.toJson().toString(), 64) + " -> " + BuiltInRegistries.POTION.getKey(this.getOutputPotionFromMix(mix)));
            }
            return matches;
        });
    }

    protected abstract Potion getInputPotionFromMix(PotionBrewing.Mix<Potion> var1);

    protected abstract Potion getOutputPotionFromMix(PotionBrewing.Mix<Potion> var1);

    protected abstract Item getOutputItemFromMix(PotionBrewing.Mix<Item> var1);

    public void removeContainer(Ingredient ingredient) {
        HashSet<Item> removed = new HashSet();
        ListIterator<Ingredient> containerIt = PotionBrewingAccessor.getAllowedContainers().listIterator();
        while (containerIt.hasNext()) {
            Ingredient ac = (Ingredient) containerIt.next();
            if (Utils.matchesIngredient(ac, ingredient)) {
                containerIt.remove();
                for (ItemStack item : ac.getItems()) {
                    removed.add(item.getItem());
                }
            }
        }
        ListIterator<PotionBrewing.Mix<Item>> mixIt = PotionBrewingAccessor.getContainerMixes().listIterator();
        while (mixIt.hasNext()) {
            PotionBrewing.Mix<Item> mix = (PotionBrewing.Mix<Item>) mixIt.next();
            Item output = this.getOutputItemFromMix(mix);
            if (ingredient.test(output.getDefaultInstance())) {
                mixIt.remove();
                removed.add(output);
            }
        }
        for (Item item : removed) {
            ConsoleJS.STARTUP.info("Removed potion container: " + BuiltInRegistries.ITEM.getKey(item));
        }
    }

    public void validateContainer(Item from, Ingredient ingredient, Item output) {
        Preconditions.checkArgument(from != null && from != Items.AIR, "Input must not be null or air");
        Preconditions.checkNotNull(ingredient, "Ingredient must not be null");
        Preconditions.checkArgument(ingredient.getItems().length > 0, "Ingredient must have at least one item");
        Preconditions.checkArgument(output != null && output != Items.AIR, "Output must not be null or air");
    }

    public abstract void addContainerRecipe(Item var1, Ingredient var2, Item var3);
}