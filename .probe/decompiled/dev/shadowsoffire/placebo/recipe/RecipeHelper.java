package dev.shadowsoffire.placebo.recipe;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.shadowsoffire.placebo.Placebo;
import dev.shadowsoffire.placebo.util.RunnableReloader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.ApiStatus.Internal;

@Deprecated
public final class RecipeHelper {

    private static final Multimap<String, Consumer<RecipeHelper.RecipeFactory>> PROVIDERS = HashMultimap.create();

    protected String modid;

    public RecipeHelper(String modid) {
        this.modid = modid;
    }

    public void registerProvider(Consumer<RecipeHelper.RecipeFactory> provider) {
        synchronized (PROVIDERS) {
            if (provider == null) {
                Placebo.LOGGER.error("Mod {} has attempted to add a null recipe provider.", this.modid);
                Thread.dumpStack();
            }
            PROVIDERS.put(this.modid, provider);
        }
    }

    public static ItemStack makeStack(Object thing) {
        if (thing instanceof ItemStack) {
            return (ItemStack) thing;
        } else if (thing instanceof ItemLike il) {
            return new ItemStack(il);
        } else if (thing instanceof RegistryObject<?> ro) {
            return new ItemStack((ItemLike) ro.get());
        } else {
            throw new IllegalArgumentException("Attempted to create an ItemStack from something that cannot be converted: " + thing);
        }
    }

    public static NonNullList<Ingredient> createInput(String modid, boolean allowEmpty, Object... inputArr) {
        NonNullList<Ingredient> inputL = NonNullList.create();
        for (int i = 0; i < inputArr.length; i++) {
            Object input = inputArr[i];
            if (input instanceof TagKey tag) {
                inputL.add(i, Ingredient.of(tag));
            } else if (input instanceof String str) {
                inputL.add(i, Ingredient.of(ItemTags.create(new ResourceLocation(str))));
            } else {
                if (input instanceof ItemStack) {
                    ItemStack stack = (ItemStack) input;
                    if (!stack.isEmpty()) {
                        inputL.add(i, Ingredient.of(stack));
                        continue;
                    }
                }
                if (input instanceof ItemLike || input instanceof RegistryObject) {
                    inputL.add(i, Ingredient.of(makeStack(input)));
                } else if (input instanceof Ingredient ing) {
                    inputL.add(i, ing);
                } else {
                    if (!allowEmpty) {
                        throw new UnsupportedOperationException("Attempted to add invalid recipe.  Complain to the author of " + modid + ". (Input " + input + " not allowed.)");
                    }
                    inputL.add(i, Ingredient.EMPTY);
                }
            }
        }
        return inputL;
    }

    @Internal
    public static PreparableReloadListener getReloader(RecipeManager mgr) {
        return RunnableReloader.of(() -> {
            mutableManager(mgr);
            addRecipes(mgr);
        });
    }

    private static void addRecipes(RecipeManager mgr) {
        PROVIDERS.forEach((modid, provider) -> {
            RecipeHelper.RecipeFactory factory = new RecipeHelper.RecipeFactory(modid);
            provider.accept(factory);
            factory.registerAll(mgr);
        });
        Placebo.LOGGER.info("Registered {} additional recipes.", RecipeHelper.RecipeFactory.totalRecipes);
        RecipeHelper.RecipeFactory.resetCaches();
    }

    private static void mutableManager(RecipeManager mgr) {
        mgr.byName = new HashMap(mgr.byName);
        mgr.recipes = new HashMap(mgr.recipes);
        for (RecipeType<?> type : mgr.recipes.keySet()) {
            mgr.recipes.put(type, new HashMap((Map) mgr.recipes.get(type)));
        }
    }

    public static final class RecipeFactory {

        private final String modid;

        private final List<Recipe<?>> recipes = new ArrayList();

        private static final Multimap<String, String> MODID_TO_NAMES = HashMultimap.create();

        private static int totalRecipes = 0;

        private RecipeFactory(String modid) {
            this.modid = modid;
        }

        public void addRecipe(Recipe<?> rec) {
            if (rec == null || rec.getId() == null || rec.getSerializer() == null || ForgeRegistries.RECIPE_SERIALIZERS.getKey(rec.getSerializer()) == null) {
                Placebo.LOGGER.error("Attempted to add an invalid recipe {}.", rec);
                Thread.dumpStack();
            }
            this.recipes.add(rec);
        }

        public void addShapeless(Object output, Object... inputs) {
            ItemStack out = RecipeHelper.makeStack(output);
            this.addRecipe(new ShapelessRecipe(this.name(out), this.modid, CraftingBookCategory.MISC, out, RecipeHelper.createInput(this.modid, false, inputs)));
        }

        public void addShaped(Object output, int width, int height, Object... input) {
            this.addRecipe(this.genShaped(RecipeHelper.makeStack(output), width, height, input));
        }

        private ShapedRecipe genShaped(ItemStack output, int width, int height, Object... input) {
            if (width * height != input.length) {
                throw new UnsupportedOperationException("Attempted to add invalid shaped recipe.  Complain to the author of " + this.modid);
            } else {
                return new ShapedRecipe(this.name(output), this.modid, CraftingBookCategory.MISC, width, height, RecipeHelper.createInput(this.modid, true, input), output);
            }
        }

        private ResourceLocation name(ItemStack out) {
            String name = ForgeRegistries.ITEMS.getKey(out.getItem()).getPath();
            while (MODID_TO_NAMES.get(this.modid).contains(name)) {
                name = name + "_";
            }
            MODID_TO_NAMES.put(this.modid, name);
            return new ResourceLocation(this.modid, name);
        }

        private void registerAll(RecipeManager mgr) {
            this.recipes.forEach(r -> {
                Map<ResourceLocation, Recipe<?>> map = (Map<ResourceLocation, Recipe<?>>) mgr.recipes.computeIfAbsent(r.getType(), t -> new HashMap());
                Recipe<?> old = (Recipe<?>) map.get(r.getId());
                if (old == null) {
                    map.put(r.getId(), r);
                    mgr.byName.put(r.getId(), r);
                    totalRecipes++;
                } else {
                    Placebo.LOGGER.debug("Skipping registration for code recipe {} as a json recipe already exists with that ID.", r.getId());
                }
            });
        }

        private static void resetCaches() {
            MODID_TO_NAMES.clear();
            totalRecipes = 0;
        }
    }
}