package snownee.lychee.core.recipe.type;

import it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.Nullable;
import snownee.lychee.RecipeTypes;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.recipe.LycheeRecipe;
import snownee.lychee.util.CommonProxy;

public class LycheeRecipeType<C extends LycheeContext, T extends LycheeRecipe<C>> implements RecipeType<T> {

    public final ResourceLocation id;

    public ResourceLocation categoryId;

    public final Class<? extends T> clazz;

    public final LootContextParamSet contextParamSet;

    private boolean empty = true;

    protected List<T> recipes;

    public boolean requiresClient;

    public boolean compactInputs;

    public boolean canPreventConsumeInputs;

    public boolean hasStandaloneCategory = true;

    public static final Component DEFAULT_PREVENT_TIP = Component.translatable("tip.lychee.preventDefault.default").withStyle(ChatFormatting.YELLOW);

    public LycheeRecipeType(String name, Class<T> clazz, @Nullable LootContextParamSet contextParamSet) {
        this.id = this.categoryId = name.contains(":") ? new ResourceLocation(name) : new ResourceLocation("lychee", name);
        this.clazz = clazz;
        this.contextParamSet = contextParamSet == null ? LootContextParamSets.get(this.id) : contextParamSet;
        Objects.requireNonNull(this.contextParamSet);
        RecipeTypes.ALL.add(this);
    }

    public String toString() {
        return this.id.toString();
    }

    public <D extends Container> Optional<T> tryMatch(Recipe<D> pRecipe, Level pLevel, D pContainer) {
        T lycheeRecipe = (T) pRecipe;
        return pRecipe.matches(pContainer, pLevel) && lycheeRecipe.checkConditions(lycheeRecipe, (LycheeContext) pContainer, 1) > 0 ? Optional.of(lycheeRecipe) : Optional.empty();
    }

    public List<T> recipes() {
        return this.recipes;
    }

    public List<T> inViewerRecipes() {
        return CommonProxy.recipes(this).stream().filter(LycheeRecipe::showInRecipeViewer).toList();
    }

    public void updateEmptyState() {
        this.empty = this.recipes.isEmpty();
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public void buildCache() {
        Stream<T> stream = CommonProxy.recipes(this).stream().filter($ -> !$.ghost);
        if (this.clazz.isAssignableFrom(Comparable.class)) {
            stream = stream.sorted();
        }
        this.recipes = stream.toList();
    }

    public Optional<T> findFirst(C ctx, Level level) {
        return this.recipes.stream().flatMap($ -> this.tryMatch($, level, ctx).stream()).findFirst();
    }

    public Component getPreventDefaultDescription(LycheeRecipe<?> recipe) {
        return DEFAULT_PREVENT_TIP;
    }

    public static class ValidItemCache {

        private IntSet validItems = IntSets.emptySet();

        public void buildCache(List<? extends Recipe<?>> recipes) {
            this.validItems = new IntAVLTreeSet(recipes.stream().flatMap($ -> $.getIngredients().stream()).flatMapToInt($ -> $.getStackingIds().intStream()).toArray());
        }

        public boolean contains(ItemStack stack) {
            return this.validItems.contains(StackedContents.getStackingIndex(stack));
        }
    }
}