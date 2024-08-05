package mezz.jei.library.plugins.vanilla.brewing;

import java.util.List;
import java.util.Objects;
import mezz.jei.api.recipe.vanilla.IJeiBrewingRecipe;
import mezz.jei.common.platform.IPlatformRegistry;
import mezz.jei.common.platform.Services;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;

public class JeiBrewingRecipe implements IJeiBrewingRecipe {

    private final List<ItemStack> ingredients;

    private final List<ItemStack> potionInputs;

    private final ItemStack potionOutput;

    private final BrewingRecipeUtil brewingRecipeUtil;

    private final int hashCode;

    public JeiBrewingRecipe(List<ItemStack> ingredients, List<ItemStack> potionInputs, ItemStack potionOutput, BrewingRecipeUtil brewingRecipeUtil) {
        this.ingredients = List.copyOf(ingredients);
        this.potionInputs = List.copyOf(potionInputs);
        this.potionOutput = potionOutput;
        this.brewingRecipeUtil = brewingRecipeUtil;
        brewingRecipeUtil.addRecipe(potionInputs, potionOutput);
        this.hashCode = Objects.hash(new Object[] { ingredients.stream().map(ItemStack::m_41720_).toList(), potionInputs.stream().map(ItemStack::m_41720_).toList(), potionOutput.getItem() });
    }

    @Override
    public List<ItemStack> getPotionInputs() {
        return this.potionInputs;
    }

    @Override
    public List<ItemStack> getIngredients() {
        return this.ingredients;
    }

    @Override
    public ItemStack getPotionOutput() {
        return this.potionOutput;
    }

    public boolean equals(Object obj) {
        if (obj instanceof JeiBrewingRecipe other) {
            for (int i = 0; i < this.potionInputs.size(); i++) {
                ItemStack potionInput = (ItemStack) this.potionInputs.get(i);
                ItemStack otherPotionInput = (ItemStack) other.potionInputs.get(i);
                if (!arePotionsEqual(potionInput, otherPotionInput)) {
                    return false;
                }
            }
            if (!arePotionsEqual(other.potionOutput, this.potionOutput)) {
                return false;
            } else if (this.ingredients.size() != other.ingredients.size()) {
                return false;
            } else {
                for (int ix = 0; ix < this.ingredients.size(); ix++) {
                    if (!ItemStack.matches((ItemStack) this.ingredients.get(ix), (ItemStack) other.ingredients.get(ix))) {
                        return false;
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    private static boolean arePotionsEqual(ItemStack potion1, ItemStack potion2) {
        if (potion1.getItem() != potion2.getItem()) {
            return false;
        } else {
            Potion type1 = PotionUtils.getPotion(potion1);
            Potion type2 = PotionUtils.getPotion(potion2);
            IPlatformRegistry<Potion> potionRegistry = Services.PLATFORM.getRegistry(Registries.POTION);
            ResourceLocation key1 = (ResourceLocation) potionRegistry.getRegistryName(type1).orElse(null);
            ResourceLocation key2 = (ResourceLocation) potionRegistry.getRegistryName(type2).orElse(null);
            return Objects.equals(key1, key2);
        }
    }

    @Override
    public int getBrewingSteps() {
        return this.brewingRecipeUtil.getBrewingSteps(this.potionOutput);
    }

    public int hashCode() {
        return this.hashCode;
    }

    public String toString() {
        Potion inputType = PotionUtils.getPotion((ItemStack) this.potionInputs.get(0));
        Potion outputType = PotionUtils.getPotion(this.potionOutput);
        return this.ingredients + " + [" + ((ItemStack) this.potionInputs.get(0)).getItem() + " " + inputType.getName("") + "] = [" + this.potionOutput + " " + outputType.getName("") + "]";
    }
}