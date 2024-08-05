package snownee.lychee.compat.rei.display;

import com.google.common.collect.Lists;
import dev.architectury.fluid.FluidStack;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import snownee.lychee.core.def.BlockPredicateHelper;
import snownee.lychee.core.post.PostAction;
import snownee.lychee.core.recipe.ILycheeRecipe;
import snownee.lychee.core.recipe.LycheeRecipe;

public class BaseREIDisplay<T extends LycheeRecipe<?>> implements Display {

    public final T recipe;

    private final CategoryIdentifier<?> categoryId;

    public BaseREIDisplay(T recipe, CategoryIdentifier<?> categoryId) {
        this.recipe = recipe;
        this.categoryId = categoryId;
    }

    public CategoryIdentifier<?> getCategoryIdentifier() {
        return this.categoryId;
    }

    public List<EntryIngredient> getInputEntries() {
        List<EntryIngredient> ingredients = Lists.newArrayList(EntryIngredients.ofIngredients(this.recipe.m_7527_()));
        this.recipe.getBlockInputs().stream().map(BlockPredicateHelper::getMatchedFluids).flatMap(Collection::stream).distinct().map($ -> EntryIngredients.of(FluidStack.create($, FluidStack.bucketAmount()))).forEach(ingredients::add);
        this.recipe.getBlockInputs().stream().map(BlockPredicateHelper::getMatchedBlocks).flatMap(Collection::stream).map(ItemLike::m_5456_).filter(Predicate.not(Items.AIR::equals)).distinct().map(EntryIngredients::of).forEach(ingredients::add);
        return ingredients;
    }

    public static List<EntryIngredient> getOutputEntries(ILycheeRecipe<?> recipe) {
        List<EntryIngredient> ingredients = Lists.newArrayList();
        ILycheeRecipe.filterHidden(recipe.getAllActions()).map(PostAction::getItemOutputs).flatMap(Collection::stream).map(EntryIngredients::of).forEach(ingredients::add);
        recipe.getBlockOutputs().stream().map(BlockPredicateHelper::getMatchedFluids).flatMap(Collection::stream).distinct().map($ -> EntryIngredients.of(FluidStack.create($, FluidStack.bucketAmount()))).forEach(ingredients::add);
        return ingredients;
    }

    public List<EntryIngredient> getOutputEntries() {
        return getOutputEntries(this.recipe);
    }

    public Optional<ResourceLocation> getDisplayLocation() {
        return Optional.of(this.recipe.getId());
    }
}