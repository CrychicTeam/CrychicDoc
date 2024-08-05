package dev.latvian.mods.kubejs.recipe.schema;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.DevProperties;
import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.RecipeTypeFunction;
import dev.latvian.mods.kubejs.util.JsonIO;
import dev.latvian.mods.rhino.util.RemapForJS;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class RecipeSchema {

    public static final Function<RecipeJS, String> DEFAULT_UNIQUE_ID_FUNCTION = r -> null;

    private final UUID uuid = UUID.randomUUID();

    public final Class<? extends RecipeJS> recipeType;

    public final Supplier<? extends RecipeJS> factory;

    public final RecipeKey<?>[] keys;

    private int inputCount;

    private int outputCount;

    private int minRequiredArguments;

    private Int2ObjectMap<RecipeConstructor> constructors;

    public Function<RecipeJS, String> uniqueIdFunction;

    public RecipeSchema(RecipeKey<?>... keys) {
        this(RecipeJS.class, RecipeJS::new, keys);
    }

    public RecipeSchema(Class<? extends RecipeJS> recipeType, Supplier<? extends RecipeJS> factory, RecipeKey<?>... keys) {
        this.recipeType = recipeType;
        this.factory = factory;
        this.keys = keys;
        this.minRequiredArguments = 0;
        this.inputCount = 0;
        this.outputCount = 0;
        HashSet<String> set = new HashSet();
        for (int i = 0; i < keys.length; i++) {
            if (keys[i].optional()) {
                if (this.minRequiredArguments == 0) {
                    this.minRequiredArguments = i;
                }
            } else if (this.minRequiredArguments > 0) {
                throw new IllegalArgumentException("Required key '" + keys[i].name + "' must be ahead of optional keys!");
            }
            if (!set.add(keys[i].name)) {
                throw new IllegalArgumentException("Duplicate key '" + keys[i].name + "' found!");
            }
            if (keys[i].component.role().isInput()) {
                this.inputCount++;
            } else if (keys[i].component.role().isOutput()) {
                this.outputCount++;
            }
            if (keys[i].alwaysWrite && keys[i].optional() && keys[i].optional.isDefault()) {
                throw new IllegalArgumentException("Key '" + keys[i] + "' can't have alwaysWrite() enabled with defaultOptional()!");
            }
        }
        if (this.minRequiredArguments == 0) {
            this.minRequiredArguments = keys.length;
        }
        this.uniqueIdFunction = DEFAULT_UNIQUE_ID_FUNCTION;
    }

    public UUID uuid() {
        return this.uuid;
    }

    @RemapForJS("addConstructor")
    public RecipeSchema constructor(RecipeConstructor.Factory factory, RecipeKey<?>... keys) {
        RecipeConstructor c = new RecipeConstructor(this, keys, factory);
        if (this.constructors == null) {
            this.constructors = new Int2ObjectArrayMap(keys.length - this.minRequiredArguments + 1);
        }
        if (this.constructors.put(c.keys().length, c) != null) {
            throw new IllegalStateException("Constructor with " + c.keys().length + " arguments already exists!");
        } else {
            return this;
        }
    }

    @RemapForJS("addConstructor")
    public RecipeSchema constructor(RecipeKey<?>... keys) {
        return this.constructor(RecipeConstructor.Factory.DEFAULT, keys);
    }

    public RecipeSchema uniqueId(Function<RecipeJS, String> uniqueIdFunction) {
        this.uniqueIdFunction = uniqueIdFunction;
        return this;
    }

    public static String normalizeId(String id) {
        if (id.startsWith("minecraft:")) {
            return id.substring(10);
        } else {
            return id.startsWith("kubejs:") ? id.substring(7) : id;
        }
    }

    public RecipeSchema uniqueOutputId(RecipeKey<OutputItem> resultItemKey) {
        return this.uniqueId(r -> {
            OutputItem item = r.getValue(resultItemKey);
            return item != null && !item.isEmpty() ? normalizeId(item.item.kjs$getId()).replace('/', '_') : null;
        });
    }

    public RecipeSchema uniqueOutputArrayId(RecipeKey<OutputItem[]> resultItemKey) {
        return this.uniqueId(r -> {
            OutputItem[] array = r.getValue(resultItemKey);
            if (array != null && array.length != 0) {
                StringBuilder sb = new StringBuilder();
                for (OutputItem item : array) {
                    if (!item.isEmpty()) {
                        if (!sb.isEmpty()) {
                            sb.append('_');
                        }
                        sb.append(normalizeId(item.item.kjs$getId()).replace('/', '_'));
                    }
                }
                return sb.isEmpty() ? null : sb.toString();
            } else {
                return null;
            }
        });
    }

    public RecipeSchema uniqueInputId(RecipeKey<InputItem> resultItemKey) {
        return this.uniqueId(r -> {
            InputItem ingredient = r.getValue(resultItemKey);
            ItemStack item = ingredient == null ? null : ingredient.ingredient.kjs$getFirst();
            return item != null && !item.isEmpty() ? normalizeId(item.kjs$getId()).replace('/', '_') : null;
        });
    }

    public Int2ObjectMap<RecipeConstructor> constructors() {
        if (this.constructors == null) {
            RecipeKey[] keys1 = (RecipeKey[]) Arrays.stream(this.keys).filter(RecipeKey::includeInAutoConstructors).toArray(RecipeKey[]::new);
            this.constructors = keys1.length == 0 ? new Int2ObjectArrayMap() : new Int2ObjectArrayMap(keys1.length - this.minRequiredArguments + 1);
            boolean dev = DevProperties.get().debugInfo;
            if (dev) {
                KubeJS.LOGGER.info("Generating constructors for " + new RecipeConstructor(this, keys1, RecipeConstructor.Factory.DEFAULT));
            }
            for (int a = this.minRequiredArguments; a <= keys1.length; a++) {
                RecipeKey<?>[] k = new RecipeKey[a];
                System.arraycopy(keys1, 0, k, 0, a);
                RecipeConstructor c = new RecipeConstructor(this, k, RecipeConstructor.Factory.DEFAULT);
                this.constructors.put(a, c);
                if (dev) {
                    KubeJS.LOGGER.info("> " + a + ": " + c);
                }
            }
        }
        return this.constructors;
    }

    public int minRequiredArguments() {
        return this.minRequiredArguments;
    }

    public int inputCount() {
        return this.inputCount;
    }

    public int outputCount() {
        return this.outputCount;
    }

    public RecipeJS deserialize(RecipeTypeFunction type, @Nullable ResourceLocation id, JsonObject json) {
        RecipeJS r = (RecipeJS) this.factory.get();
        r.type = type;
        r.id = id;
        r.json = json;
        r.newRecipe = id == null;
        r.initValues(id == null);
        if (id != null && DevProperties.get().debugInfo) {
            r.originalJson = (JsonObject) JsonIO.copy(json);
        }
        r.deserialize(false);
        return r;
    }
}