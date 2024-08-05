package net.minecraft.world.item.crafting;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;

public class RecipeManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private static final Logger LOGGER = LogUtils.getLogger();

    private Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> recipes = ImmutableMap.of();

    private Map<ResourceLocation, Recipe<?>> byName = ImmutableMap.of();

    private boolean hasErrors;

    public RecipeManager() {
        super(GSON, "recipes");
    }

    protected void apply(Map<ResourceLocation, JsonElement> mapResourceLocationJsonElement0, ResourceManager resourceManager1, ProfilerFiller profilerFiller2) {
        this.hasErrors = false;
        Map<RecipeType<?>, Builder<ResourceLocation, Recipe<?>>> $$3 = Maps.newHashMap();
        Builder<ResourceLocation, Recipe<?>> $$4 = ImmutableMap.builder();
        for (Entry<ResourceLocation, JsonElement> $$5 : mapResourceLocationJsonElement0.entrySet()) {
            ResourceLocation $$6 = (ResourceLocation) $$5.getKey();
            try {
                Recipe<?> $$7 = fromJson($$6, GsonHelper.convertToJsonObject((JsonElement) $$5.getValue(), "top element"));
                ((Builder) $$3.computeIfAbsent($$7.getType(), p_44075_ -> ImmutableMap.builder())).put($$6, $$7);
                $$4.put($$6, $$7);
            } catch (IllegalArgumentException | JsonParseException var10) {
                LOGGER.error("Parsing error loading recipe {}", $$6, var10);
            }
        }
        this.recipes = (Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>>) $$3.entrySet().stream().collect(ImmutableMap.toImmutableMap(Entry::getKey, p_44033_ -> ((Builder) p_44033_.getValue()).build()));
        this.byName = $$4.build();
        LOGGER.info("Loaded {} recipes", $$3.size());
    }

    public boolean hadErrorsLoading() {
        return this.hasErrors;
    }

    public <C extends Container, T extends Recipe<C>> Optional<T> getRecipeFor(RecipeType<T> recipeTypeT0, C c1, Level level2) {
        return this.byType(recipeTypeT0).values().stream().filter(p_220266_ -> p_220266_.matches(c1, level2)).findFirst();
    }

    public <C extends Container, T extends Recipe<C>> Optional<Pair<ResourceLocation, T>> getRecipeFor(RecipeType<T> recipeTypeT0, C c1, Level level2, @Nullable ResourceLocation resourceLocation3) {
        Map<ResourceLocation, T> $$4 = this.byType(recipeTypeT0);
        if (resourceLocation3 != null) {
            T $$5 = (T) $$4.get(resourceLocation3);
            if ($$5 != null && $$5.matches(c1, level2)) {
                return Optional.of(Pair.of(resourceLocation3, $$5));
            }
        }
        return $$4.entrySet().stream().filter(p_220245_ -> ((Recipe) p_220245_.getValue()).matches(c1, level2)).findFirst().map(p_220256_ -> Pair.of((ResourceLocation) p_220256_.getKey(), (Recipe) p_220256_.getValue()));
    }

    public <C extends Container, T extends Recipe<C>> List<T> getAllRecipesFor(RecipeType<T> recipeTypeT0) {
        return List.copyOf(this.byType(recipeTypeT0).values());
    }

    public <C extends Container, T extends Recipe<C>> List<T> getRecipesFor(RecipeType<T> recipeTypeT0, C c1, Level level2) {
        return (List<T>) this.byType(recipeTypeT0).values().stream().filter(p_220241_ -> p_220241_.matches(c1, level2)).sorted(Comparator.comparing(p_270043_ -> p_270043_.getResultItem(level2.registryAccess()).getDescriptionId())).collect(Collectors.toList());
    }

    private <C extends Container, T extends Recipe<C>> Map<ResourceLocation, T> byType(RecipeType<T> recipeTypeT0) {
        return (Map<ResourceLocation, T>) this.recipes.getOrDefault(recipeTypeT0, Collections.emptyMap());
    }

    public <C extends Container, T extends Recipe<C>> NonNullList<ItemStack> getRemainingItemsFor(RecipeType<T> recipeTypeT0, C c1, Level level2) {
        Optional<T> $$3 = this.getRecipeFor(recipeTypeT0, c1, level2);
        if ($$3.isPresent()) {
            return ((Recipe) $$3.get()).getRemainingItems(c1);
        } else {
            NonNullList<ItemStack> $$4 = NonNullList.withSize(c1.getContainerSize(), ItemStack.EMPTY);
            for (int $$5 = 0; $$5 < $$4.size(); $$5++) {
                $$4.set($$5, c1.getItem($$5));
            }
            return $$4;
        }
    }

    public Optional<? extends Recipe<?>> byKey(ResourceLocation resourceLocation0) {
        return Optional.ofNullable((Recipe) this.byName.get(resourceLocation0));
    }

    public Collection<Recipe<?>> getRecipes() {
        return (Collection<Recipe<?>>) this.recipes.values().stream().flatMap(p_220270_ -> p_220270_.values().stream()).collect(Collectors.toSet());
    }

    public Stream<ResourceLocation> getRecipeIds() {
        return this.recipes.values().stream().flatMap(p_220258_ -> p_220258_.keySet().stream());
    }

    public static Recipe<?> fromJson(ResourceLocation resourceLocation0, JsonObject jsonObject1) {
        String $$2 = GsonHelper.getAsString(jsonObject1, "type");
        return ((RecipeSerializer) BuiltInRegistries.RECIPE_SERIALIZER.getOptional(new ResourceLocation($$2)).orElseThrow(() -> new JsonSyntaxException("Invalid or unsupported recipe type '" + $$2 + "'"))).fromJson(resourceLocation0, jsonObject1);
    }

    public void replaceRecipes(Iterable<Recipe<?>> iterableRecipe0) {
        this.hasErrors = false;
        Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> $$1 = Maps.newHashMap();
        Builder<ResourceLocation, Recipe<?>> $$2 = ImmutableMap.builder();
        iterableRecipe0.forEach(p_220262_ -> {
            Map<ResourceLocation, Recipe<?>> $$3 = (Map<ResourceLocation, Recipe<?>>) $$1.computeIfAbsent(p_220262_.getType(), p_220272_ -> Maps.newHashMap());
            ResourceLocation $$4 = p_220262_.getId();
            Recipe<?> $$5 = (Recipe<?>) $$3.put($$4, p_220262_);
            $$2.put($$4, p_220262_);
            if ($$5 != null) {
                throw new IllegalStateException("Duplicate recipe ignored with ID " + $$4);
            }
        });
        this.recipes = ImmutableMap.copyOf($$1);
        this.byName = $$2.build();
    }

    public static <C extends Container, T extends Recipe<C>> RecipeManager.CachedCheck<C, T> createCheck(final RecipeType<T> recipeTypeT0) {
        return new RecipeManager.CachedCheck<C, T>() {

            @Nullable
            private ResourceLocation lastRecipe;

            @Override
            public Optional<T> getRecipeFor(C p_220278_, Level p_220279_) {
                RecipeManager $$2 = p_220279_.getRecipeManager();
                Optional<Pair<ResourceLocation, T>> $$3 = $$2.getRecipeFor(recipeTypeT0, p_220278_, p_220279_, this.lastRecipe);
                if ($$3.isPresent()) {
                    Pair<ResourceLocation, T> $$4 = (Pair<ResourceLocation, T>) $$3.get();
                    this.lastRecipe = (ResourceLocation) $$4.getFirst();
                    return Optional.of((Recipe) $$4.getSecond());
                } else {
                    return Optional.empty();
                }
            }
        };
    }

    public interface CachedCheck<C extends Container, T extends Recipe<C>> {

        Optional<T> getRecipeFor(C var1, Level var2);
    }
}