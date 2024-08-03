package org.violetmoon.zeta.event.play;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.collect.Streams;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.event.bus.IZetaPlayEvent;
import org.violetmoon.zeta.util.RegistryUtil;

public abstract class ZRecipeCrawl implements IZetaPlayEvent {

    public static class Digest extends ZRecipeCrawl {

        private final Multimap<Item, ItemStack> digestion;

        private final Multimap<Item, ItemStack> backwardsDigestion;

        public Digest(Multimap<Item, ItemStack> digestion, Multimap<Item, ItemStack> backwardsDigestion) {
            this.digestion = digestion;
            this.backwardsDigestion = backwardsDigestion;
        }

        public boolean has(Item item, boolean backwards) {
            return (backwards ? this.backwardsDigestion : this.digestion).containsKey(item);
        }

        public Collection<ItemStack> get(Item item, boolean backwards) {
            return (backwards ? this.backwardsDigestion : this.digestion).get(item);
        }

        public void recursivelyFindCraftedItemsFromStrings(@Nullable Collection<String> derivationList, @Nullable Collection<String> whitelist, @Nullable Collection<String> blacklist, Consumer<Item> callback) {
            List<Item> parsedDerivationList = derivationList == null ? null : RegistryUtil.massRegistryGet(derivationList, BuiltInRegistries.ITEM);
            List<Item> parsedWhitelist = whitelist == null ? null : RegistryUtil.massRegistryGet(whitelist, BuiltInRegistries.ITEM);
            List<Item> parsedBlacklist = blacklist == null ? null : RegistryUtil.massRegistryGet(blacklist, BuiltInRegistries.ITEM);
            this.recursivelyFindCraftedItems(parsedDerivationList, parsedWhitelist, parsedBlacklist, callback);
        }

        public void recursivelyFindCraftedItems(@Nullable Collection<Item> derivationList, @Nullable Collection<Item> whitelist, @Nullable Collection<Item> blacklist, Consumer<Item> callback) {
            Collection<Item> trueDerivationList = (Collection<Item>) (derivationList == null ? Lists.newArrayList() : derivationList);
            Collection<Item> trueWhitelist = (Collection<Item>) (whitelist == null ? Lists.newArrayList() : whitelist);
            Collection<Item> trueBlacklist = (Collection<Item>) (blacklist == null ? Lists.newArrayList() : blacklist);
            Streams.concat(new Stream[] { trueDerivationList.stream(), trueWhitelist.stream() }).forEach(callback);
            Set<Item> scanned = Sets.newHashSet(trueDerivationList);
            List<Item> toScan = Lists.newArrayList(trueDerivationList);
            while (!toScan.isEmpty()) {
                Item scan = (Item) toScan.remove(0);
                if (this.digestion.containsKey(scan)) {
                    for (ItemStack digestedStack : this.digestion.get(scan)) {
                        Item candidate = digestedStack.getItem();
                        if (!scanned.contains(candidate)) {
                            scanned.add(candidate);
                            toScan.add(candidate);
                            if (!trueBlacklist.contains(candidate)) {
                                callback.accept(candidate);
                            }
                        }
                    }
                }
            }
        }
    }

    public static class Reset extends ZRecipeCrawl {
    }

    public static class Starting extends ZRecipeCrawl {
    }

    public abstract static class Visit<T extends Recipe<?>> extends ZRecipeCrawl {

        public final T recipe;

        public final ResourceLocation recipeID;

        public final ItemStack output;

        public final NonNullList<Ingredient> ingredients;

        public Visit(T recipe, RegistryAccess access) {
            this.recipe = recipe;
            this.recipeID = recipe.getId();
            this.output = recipe.getResultItem(access);
            this.ingredients = recipe.getIngredients();
        }

        public static class Cooking extends ZRecipeCrawl.Visit<AbstractCookingRecipe> {

            public Cooking(AbstractCookingRecipe recipe, RegistryAccess access) {
                super(recipe, access);
            }
        }

        public static class Custom extends ZRecipeCrawl.Visit<CustomRecipe> {

            public Custom(CustomRecipe recipe, RegistryAccess access) {
                super(recipe, access);
            }
        }

        public static class Misc extends ZRecipeCrawl.Visit<Recipe<?>> {

            public Misc(Recipe<?> recipe, RegistryAccess access) {
                super(recipe, access);
            }
        }

        public static class Shaped extends ZRecipeCrawl.Visit<ShapedRecipe> {

            public Shaped(ShapedRecipe recipe, RegistryAccess access) {
                super(recipe, access);
            }
        }

        public static class Shapeless extends ZRecipeCrawl.Visit<ShapelessRecipe> {

            public Shapeless(ShapelessRecipe recipe, RegistryAccess access) {
                super(recipe, access);
            }
        }
    }
}