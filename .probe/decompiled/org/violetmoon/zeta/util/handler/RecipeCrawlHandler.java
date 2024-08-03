package org.violetmoon.zeta.util.handler;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.violetmoon.zeta.Zeta;
import org.violetmoon.zeta.event.bus.IZetaPlayEvent;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.load.ZAddReloadListener;
import org.violetmoon.zeta.event.load.ZTagsUpdated;
import org.violetmoon.zeta.event.play.ZRecipeCrawl;
import org.violetmoon.zeta.event.play.ZServerTick;
import org.violetmoon.zeta.util.zetalist.ZetaList;

@Internal
public class RecipeCrawlHandler {

    private static final List<Recipe<?>> vanillaRecipesToLazyDigest = new ArrayList();

    private static final Multimap<Item, ItemStack> vanillaRecipeDigestion = HashMultimap.create();

    private static final Multimap<Item, ItemStack> backwardsVanillaDigestion = HashMultimap.create();

    private static final Object mutex = new Object();

    private static boolean needsCrawl = false;

    private static boolean mayCrawl = false;

    @LoadEvent
    public static void addListener(ZAddReloadListener event) {
        event.addListener(new SimplePreparableReloadListener<Void>() {

            protected Void prepare(ResourceManager mgr, ProfilerFiller prof) {
                RecipeCrawlHandler.clear();
                return null;
            }

            protected void apply(Void what, ResourceManager mgr, ProfilerFiller prof) {
                RecipeCrawlHandler.needsCrawl = true;
            }
        });
    }

    @LoadEvent
    public static void tagsHaveUpdated(ZTagsUpdated event) {
        mayCrawl = true;
    }

    private static void clear() {
        mayCrawl = false;
        fire(new ZRecipeCrawl.Reset());
    }

    private static void fire(IZetaPlayEvent event) {
        ZetaList.INSTANCE.fireEvent(event);
    }

    private static void load(RecipeManager manager, RegistryAccess access) {
        if (!manager.getRecipes().isEmpty()) {
            fire(new ZRecipeCrawl.Starting());
            vanillaRecipesToLazyDigest.clear();
            vanillaRecipeDigestion.clear();
            backwardsVanillaDigestion.clear();
            for (Recipe<?> recipe : manager.getRecipes()) {
                try {
                    if (recipe == null) {
                        throw new IllegalStateException("Recipe is null");
                    }
                    if (recipe.getIngredients() == null) {
                        throw new IllegalStateException("Recipe ingredients are null");
                    }
                    if (recipe.getResultItem(access) == null) {
                        throw new IllegalStateException("Recipe getResultItem is null");
                    }
                    ZRecipeCrawl.Visit<?> event;
                    if (recipe instanceof ShapedRecipe sr) {
                        event = new ZRecipeCrawl.Visit.Shaped(sr, access);
                    } else if (recipe instanceof ShapelessRecipe sr) {
                        event = new ZRecipeCrawl.Visit.Shapeless(sr, access);
                    } else if (recipe instanceof CustomRecipe cr) {
                        event = new ZRecipeCrawl.Visit.Custom(cr, access);
                    } else if (recipe instanceof AbstractCookingRecipe acr) {
                        event = new ZRecipeCrawl.Visit.Cooking(acr, access);
                    } else {
                        event = new ZRecipeCrawl.Visit.Misc(recipe, access);
                    }
                    if (!(event instanceof ZRecipeCrawl.Visit.Misc)) {
                        vanillaRecipesToLazyDigest.add(recipe);
                    }
                    fire(event);
                } catch (Exception var9) {
                    if (recipe == null) {
                        Zeta.GLOBAL_LOG.error("Encountered null recipe in RecipeManager.getRecipes. This is not good");
                    } else {
                        Zeta.GLOBAL_LOG.error("Failed to scan recipe " + recipe.getId() + ". This should be reported to " + recipe.getId().getNamespace() + "!", var9);
                    }
                }
            }
        }
    }

    @PlayEvent
    public static void onTick(ZServerTick.Start tick) {
        synchronized (mutex) {
            if (mayCrawl && needsCrawl) {
                RecipeManager manager = tick.getServer().getRecipeManager();
                RegistryAccess access = tick.getServer().registryAccess();
                load(manager, access);
                needsCrawl = false;
            }
            if (!vanillaRecipesToLazyDigest.isEmpty()) {
                vanillaRecipeDigestion.clear();
                backwardsVanillaDigestion.clear();
                for (Recipe<?> recipe : vanillaRecipesToLazyDigest) {
                    digest(recipe, tick.getServer().registryAccess());
                }
                vanillaRecipesToLazyDigest.clear();
                fire(new ZRecipeCrawl.Digest(vanillaRecipeDigestion, backwardsVanillaDigestion));
            }
        }
    }

    private static void digest(Recipe<?> recipe, RegistryAccess access) {
        ItemStack out = recipe.getResultItem(access);
        Item outItem = out.getItem();
        for (Ingredient ingredient : recipe.getIngredients()) {
            for (ItemStack inStack : ingredient.getItems()) {
                if (inStack.getCraftingRemainingItem().isEmpty()) {
                    vanillaRecipeDigestion.put(inStack.getItem(), out);
                    backwardsVanillaDigestion.put(outItem, inStack);
                }
            }
        }
    }

    @Deprecated(forRemoval = true)
    public void recursivelyFindCraftedItemsFromStrings(@Nullable Collection<String> derivationList, @Nullable Collection<String> whitelist, @Nullable Collection<String> blacklist, Consumer<Item> callback) {
    }

    @Deprecated(forRemoval = true)
    public void recursivelyFindCraftedItems(@Nullable Collection<Item> derivationList, @Nullable Collection<Item> whitelist, @Nullable Collection<Item> blacklist, Consumer<Item> callback) {
    }
}