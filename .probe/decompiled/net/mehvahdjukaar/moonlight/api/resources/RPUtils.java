package net.mehvahdjukaar.moonlight.api.resources;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.management.openmbean.InvalidOpenTypeException;
import net.mehvahdjukaar.moonlight.api.client.TextureCache;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicTexturePack;
import net.mehvahdjukaar.moonlight.api.resources.recipe.IRecipeTemplate;
import net.mehvahdjukaar.moonlight.api.resources.recipe.TemplateRecipeManager;
import net.mehvahdjukaar.moonlight.api.set.BlockType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class RPUtils {

    public static String serializeJson(JsonElement json) throws IOException {
        try {
            StringWriter stringWriter = new StringWriter();
            String var3;
            try {
                JsonWriter jsonWriter = new JsonWriter(stringWriter);
                try {
                    jsonWriter.setLenient(true);
                    jsonWriter.setIndent("  ");
                    Streams.write(json, jsonWriter);
                    var3 = stringWriter.toString();
                } catch (Throwable var7) {
                    try {
                        jsonWriter.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }
                    throw var7;
                }
                jsonWriter.close();
            } catch (Throwable var8) {
                try {
                    stringWriter.close();
                } catch (Throwable var5) {
                    var8.addSuppressed(var5);
                }
                throw var8;
            }
            stringWriter.close();
            return var3;
        } catch (Exception var9) {
            throw new RuntimeException(var9);
        }
    }

    public static JsonObject deserializeJson(InputStream stream) {
        return GsonHelper.parse(new InputStreamReader(stream, StandardCharsets.UTF_8));
    }

    public static ResourceLocation findFirstBlockTextureLocation(ResourceManager manager, Block block) throws FileNotFoundException {
        return findFirstBlockTextureLocation(manager, block, t -> true);
    }

    public static ResourceLocation findFirstBlockTextureLocation(ResourceManager manager, Block block, Predicate<String> texturePredicate) throws FileNotFoundException {
        String cached = TextureCache.getCached(block, texturePredicate);
        if (cached != null) {
            return new ResourceLocation(cached);
        } else {
            ResourceLocation blockId = Utils.getID(block);
            Optional<Resource> blockState = manager.m_213713_(ResType.BLOCKSTATES.getPath(blockId));
            try {
                label77: {
                    InputStream bsStream = ((Resource) blockState.orElseThrow()).open();
                    ResourceLocation var14;
                    label68: {
                        try {
                            JsonElement bsElement = deserializeJson(bsStream);
                            for (String modelPath : findAllResourcesInJsonRecursive(bsElement.getAsJsonObject(), s -> s.equals("model"))) {
                                for (String t : findAllTexturesInModelRecursive(manager, modelPath)) {
                                    TextureCache.add(block, t);
                                    if (texturePredicate.test(t)) {
                                        var14 = new ResourceLocation(t);
                                        break label68;
                                    }
                                }
                            }
                        } catch (Throwable var16) {
                            if (bsStream != null) {
                                try {
                                    bsStream.close();
                                } catch (Throwable var15) {
                                    var16.addSuppressed(var15);
                                }
                            }
                            throw var16;
                        }
                        if (bsStream != null) {
                            bsStream.close();
                        }
                        break label77;
                    }
                    if (bsStream != null) {
                        bsStream.close();
                    }
                    return var14;
                }
            } catch (Exception var17) {
            }
            for (String tx : guessBlockTextureLocation(blockId, block)) {
                TextureCache.add(block, tx);
                if (texturePredicate.test(tx)) {
                    return new ResourceLocation(tx);
                }
            }
            throw new FileNotFoundException("Could not find any texture associated to the given block " + blockId);
        }
    }

    private static Set<String> guessItemTextureLocation(ResourceLocation id, Item item) {
        return Set.of(id.getNamespace() + ":item/" + item);
    }

    private static List<String> guessBlockTextureLocation(ResourceLocation id, Block block) {
        String name = id.getPath();
        List<String> textures = new ArrayList();
        WoodType w = WoodTypeRegistry.INSTANCE.getBlockTypeOf(block);
        if (w != null) {
            String key = w.getChildKey(block);
            if (Objects.equals(key, "log") || Objects.equals(key, "stripped_log")) {
                textures.add(id.getNamespace() + ":block/" + name + "_top");
                textures.add(id.getNamespace() + ":block/" + name + "_side");
            }
        }
        textures.add(id.getNamespace() + ":block/" + name);
        return textures;
    }

    @NotNull
    private static List<String> findAllTexturesInModelRecursive(ResourceManager manager, String modelPath) throws Exception {
        JsonObject modelElement;
        try {
            InputStream modelStream = ((Resource) manager.m_213713_(ResType.MODELS.getPath(modelPath)).get()).open();
            try {
                modelElement = deserializeJson(modelStream).getAsJsonObject();
            } catch (Throwable var7) {
                if (modelStream != null) {
                    try {
                        modelStream.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }
                }
                throw var7;
            }
            if (modelStream != null) {
                modelStream.close();
            }
        } catch (Exception var8) {
            throw new Exception("Failed to parse model at " + modelPath);
        }
        ArrayList<String> textures = new ArrayList(findAllResourcesInJsonRecursive(modelElement.getAsJsonObject("textures")));
        if (textures.isEmpty() && modelElement.has("parent")) {
            String parentPath = modelElement.get("parent").getAsString();
            textures.addAll(findAllTexturesInModelRecursive(manager, parentPath));
        }
        return textures;
    }

    public static ResourceLocation findFirstItemTextureLocation(ResourceManager manager, Item block) throws FileNotFoundException {
        return findFirstItemTextureLocation(manager, block, t -> true);
    }

    public static ResourceLocation findFirstItemTextureLocation(ResourceManager manager, Item item, Predicate<String> texturePredicate) throws FileNotFoundException {
        String cached = TextureCache.getCached(item, texturePredicate);
        if (cached != null) {
            return new ResourceLocation(cached);
        } else {
            ResourceLocation itemId = Utils.getID(item);
            Optional<Resource> itemModel = manager.m_213713_(ResType.ITEM_MODELS.getPath(itemId));
            Set<String> textures;
            try {
                InputStream stream = ((Resource) itemModel.orElseThrow()).open();
                try {
                    JsonElement bsElement = deserializeJson(stream);
                    textures = findAllResourcesInJsonRecursive(bsElement.getAsJsonObject().getAsJsonObject("textures"));
                } catch (Throwable var11) {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (Throwable var10) {
                            var11.addSuppressed(var10);
                        }
                    }
                    throw var11;
                }
                if (stream != null) {
                    stream.close();
                }
            } catch (Exception var12) {
                textures = guessItemTextureLocation(itemId, item);
            }
            for (String t : textures) {
                TextureCache.add(item, t);
                if (texturePredicate.test(t)) {
                    return new ResourceLocation(t);
                }
            }
            throw new FileNotFoundException("Could not find any texture associated to the given item " + itemId);
        }
    }

    public static String findFirstResourceInJsonRecursive(JsonElement element) throws NoSuchElementException {
        if (element instanceof JsonArray array) {
            return findFirstResourceInJsonRecursive(array.get(0));
        } else if (element instanceof JsonObject) {
            Set<Entry<String, JsonElement>> entries = element.getAsJsonObject().entrySet();
            JsonElement child = (JsonElement) ((Entry) entries.stream().findAny().get()).getValue();
            return findFirstResourceInJsonRecursive(child);
        } else {
            return element.getAsString();
        }
    }

    public static Set<String> findAllResourcesInJsonRecursive(JsonElement element) {
        return findAllResourcesInJsonRecursive(element, s -> true);
    }

    public static Set<String> findAllResourcesInJsonRecursive(JsonElement element, Predicate<String> filter) {
        if (element instanceof JsonArray array) {
            Set<String> list = new HashSet();
            array.forEach(e -> list.addAll(findAllResourcesInJsonRecursive(e, filter)));
            return list;
        } else if (!(element instanceof JsonObject json)) {
            return Set.of(element.getAsString());
        } else {
            Set<Entry<String, JsonElement>> entries = json.entrySet();
            Set<String> list = new HashSet();
            for (Entry<String, JsonElement> c : entries) {
                if (!((JsonElement) c.getValue()).isJsonPrimitive() || filter.test((String) c.getKey())) {
                    Set<String> l = findAllResourcesInJsonRecursive((JsonElement) c.getValue(), filter);
                    list.addAll(l);
                }
            }
            return list;
        }
    }

    public static Recipe<?> readRecipe(ResourceManager manager, String location) {
        return readRecipe(manager, ResType.RECIPES.getPath(location));
    }

    public static Recipe<?> readRecipe(ResourceManager manager, ResourceLocation location) {
        Optional<Resource> resource = manager.m_213713_(location);
        try {
            InputStream stream = ((Resource) resource.orElseThrow()).open();
            Recipe var5;
            try {
                JsonObject element = deserializeJson(stream);
                var5 = RecipeManager.fromJson(location, element);
            } catch (Throwable var7) {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }
                }
                throw var7;
            }
            if (stream != null) {
                stream.close();
            }
            return var5;
        } catch (Exception var8) {
            throw new InvalidOpenTypeException(String.format("Failed to get recipe at %s: %s", location, var8));
        }
    }

    public static IRecipeTemplate<?> readRecipeAsTemplate(ResourceManager manager, String location) {
        return readRecipeAsTemplate(manager, ResType.RECIPES.getPath(location));
    }

    public static IRecipeTemplate<?> readRecipeAsTemplate(ResourceManager manager, ResourceLocation location) {
        Optional<Resource> resource = manager.m_213713_(location);
        try {
            InputStream stream = ((Resource) resource.orElseThrow()).open();
            IRecipeTemplate e;
            try {
                JsonObject element = deserializeJson(stream);
                try {
                    e = TemplateRecipeManager.read(element);
                } catch (Exception var7) {
                    Moonlight.LOGGER.error(element);
                    Moonlight.LOGGER.error(location);
                    throw var7;
                }
            } catch (Throwable var8) {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (Throwable var6) {
                        var8.addSuppressed(var6);
                    }
                }
                throw var8;
            }
            if (stream != null) {
                stream.close();
            }
            return e;
        } catch (Exception var9) {
            throw new InvalidOpenTypeException(String.format("Failed to get recipe at %s: %s", location, var9));
        }
    }

    public static <T extends BlockType> Recipe<?> makeSimilarRecipe(Recipe<?> original, T originalMat, T destinationMat, String baseID) {
        if (original instanceof ShapedRecipe or) {
            List<Ingredient> newList = new ArrayList();
            for (Ingredient ingredient : or.getIngredients()) {
                if (ingredient != null && ingredient.getItems().length > 0) {
                    ItemLike i = BlockType.changeItemType(ingredient.getItems()[0].getItem(), originalMat, destinationMat);
                    if (i != null) {
                        newList.add(Ingredient.of(i));
                    }
                }
            }
            Item originalRes = or.getResultItem(RegistryAccess.EMPTY).getItem();
            ItemLike newRes = BlockType.changeItemType(originalRes, originalMat, destinationMat);
            if (newRes == null) {
                throw new UnsupportedOperationException("Failed to convert recipe");
            } else {
                ItemStack result = newRes.asItem().getDefaultInstance();
                ResourceLocation newId = new ResourceLocation(baseID + "/" + destinationMat.getAppendableId());
                NonNullList<Ingredient> ingredients = NonNullList.of(Ingredient.EMPTY, (Ingredient[]) newList.toArray(Ingredient[]::new));
                return new ShapedRecipe(newId, or.getGroup(), or.category(), or.getWidth(), or.getHeight(), ingredients, result);
            }
        } else if (original instanceof ShapelessRecipe or) {
            List<Ingredient> newList = new ArrayList();
            for (Ingredient ingredientx : or.getIngredients()) {
                if (ingredientx != null && ingredientx.getItems().length > 0) {
                    ItemLike i = BlockType.changeItemType(ingredientx.getItems()[0].getItem(), originalMat, destinationMat);
                    if (i != null) {
                        newList.add(Ingredient.of(i));
                    }
                }
            }
            Item originalRes = or.getResultItem(RegistryAccess.EMPTY).getItem();
            ItemLike newRes = BlockType.changeItemType(originalRes, originalMat, destinationMat);
            if (newRes == null) {
                throw new UnsupportedOperationException("Failed to convert recipe");
            } else {
                ItemStack result = newRes.asItem().getDefaultInstance();
                ResourceLocation newId = new ResourceLocation(baseID + "/" + destinationMat.getAppendableId());
                NonNullList<Ingredient> ingredients = NonNullList.of(Ingredient.EMPTY, (Ingredient[]) newList.toArray(Ingredient[]::new));
                return new ShapelessRecipe(newId, or.getGroup(), or.category(), result, ingredients);
            }
        } else {
            throw new UnsupportedOperationException(String.format("Original recipe %s must be Shaped or Shapeless", original));
        }
    }

    public static void appendModelOverride(ResourceManager manager, DynamicTexturePack pack, ResourceLocation modelRes, Consumer<RPUtils.OverrideAppender> modelConsumer) {
        Optional<Resource> o = manager.m_213713_(ResType.ITEM_MODELS.getPath(modelRes));
        if (o.isPresent()) {
            try {
                InputStream model = ((Resource) o.get()).open();
                try {
                    JsonObject json = deserializeJson(model);
                    JsonArray overrides;
                    if (json.has("overrides")) {
                        overrides = json.getAsJsonArray("overrides");
                    } else {
                        overrides = new JsonArray();
                    }
                    modelConsumer.accept((RPUtils.OverrideAppender) ov -> overrides.add(serializeOverride(ov)));
                    json.add("overrides", overrides);
                    pack.addItemModel(modelRes, json);
                } catch (Throwable var9) {
                    if (model != null) {
                        try {
                            model.close();
                        } catch (Throwable var8) {
                            var9.addSuppressed(var8);
                        }
                    }
                    throw var9;
                }
                if (model != null) {
                    model.close();
                }
            } catch (Exception var10) {
            }
        }
    }

    private static JsonObject serializeOverride(ItemOverride override) {
        JsonObject json = new JsonObject();
        json.addProperty("model", override.getModel().toString());
        JsonObject predicates = new JsonObject();
        override.getPredicates().forEach(p -> predicates.addProperty(p.getProperty().toString(), p.getValue()));
        json.add("predicate", predicates);
        return json;
    }

    @FunctionalInterface
    public interface OverrideAppender {

        void add(ItemOverride var1);
    }
}