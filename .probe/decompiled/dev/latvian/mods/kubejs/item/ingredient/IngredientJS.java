package dev.latvian.mods.kubejs.item.ingredient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.core.IngredientSupplierKJS;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.platform.IngredientPlatformHelper;
import dev.latvian.mods.kubejs.platform.RecipePlatformHelper;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.util.ListJS;
import dev.latvian.mods.kubejs.util.MapJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.Wrapper;
import dev.latvian.mods.rhino.mod.util.NBTUtils;
import dev.latvian.mods.rhino.regexp.NativeRegExp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

public interface IngredientJS {

    static Ingredient of(@Nullable Object o) {
        while (o instanceof Wrapper) {
            Wrapper w = (Wrapper) o;
            o = w.unwrap();
        }
        if (o == null || o == ItemStack.EMPTY || o == Items.AIR || o == Ingredient.EMPTY) {
            return Ingredient.EMPTY;
        } else if (o instanceof IngredientSupplierKJS ingr) {
            return ingr.kjs$asIngredient();
        } else if (o instanceof TagKey<?> tag) {
            return Ingredient.of(TagKey.create(Registries.ITEM, tag.location()));
        } else if (o instanceof Pattern || o instanceof NativeRegExp) {
            Pattern reg = UtilsJS.parseRegex(o);
            return reg != null ? IngredientPlatformHelper.get().regex(reg) : Ingredient.EMPTY;
        } else if (o instanceof JsonElement json) {
            return ofJson(json);
        } else if (o instanceof CharSequence) {
            return parse(o.toString());
        } else {
            List<?> list = ListJS.of(o);
            if (list != null) {
                ArrayList<Ingredient> inList = new ArrayList(list.size());
                for (Object o1 : list) {
                    Ingredient ingredient = of(o1);
                    if (ingredient != Ingredient.EMPTY) {
                        inList.add(ingredient);
                    }
                }
                if (inList.isEmpty()) {
                    return Ingredient.EMPTY;
                } else {
                    return inList.size() == 1 ? (Ingredient) inList.get(0) : IngredientPlatformHelper.get().or((Ingredient[]) inList.toArray(new Ingredient[0]));
                }
            } else {
                Map<?, ?> map = MapJS.of(o);
                if (map == null) {
                    return ItemStackJS.of(o).kjs$asIngredient();
                } else {
                    Ingredient in = Ingredient.EMPTY;
                    boolean val = map.containsKey("value");
                    if (map.containsKey("type")) {
                        if ("forge:nbt".equals(map.get("type"))) {
                            in = ItemStackJS.of(map.get("item")).kjs$withNBT(NBTUtils.toTagCompound(map.get("nbt"))).kjs$strongNBT();
                        } else {
                            JsonObject json = MapJS.json(o);
                            if (json == null) {
                                throw new RecipeExceptionJS("Failed to parse custom ingredient (" + o + " is not a json object");
                            }
                            try {
                                in = RecipePlatformHelper.get().getCustomIngredient(json);
                            } catch (Exception var7) {
                                throw new RecipeExceptionJS("Failed to parse custom ingredient (" + json.get("type") + ") from " + json + ": " + var7);
                            }
                        }
                    } else if (!val && !map.containsKey("ingredient")) {
                        if (map.containsKey("tag")) {
                            in = IngredientPlatformHelper.get().tag(map.get("tag").toString());
                        } else if (map.containsKey("item")) {
                            in = ItemStackJS.of(map).getItem().kjs$asIngredient();
                        }
                    } else {
                        in = of(val ? map.get("value") : map.get("ingredient"));
                    }
                    return in;
                }
            }
        }
    }

    static Ingredient parse(String s) {
        if (s.isEmpty() || s.equals("-") || s.equals("air") || s.equals("minecraft:air")) {
            return Ingredient.EMPTY;
        } else if (s.equals("*")) {
            return IngredientPlatformHelper.get().wildcard();
        } else if (s.startsWith("#")) {
            return IngredientPlatformHelper.get().tag(s.substring(1));
        } else if (s.startsWith("@")) {
            return IngredientPlatformHelper.get().mod(s.substring(1));
        } else if (s.startsWith("%")) {
            CreativeModeTab group = UtilsJS.findCreativeTab(new ResourceLocation(s.substring(1)));
            if (group != null) {
                return IngredientPlatformHelper.get().creativeTab(group);
            } else if (RecipeJS.itemErrors) {
                throw new RecipeExceptionJS("Item group '" + s.substring(1) + "' not found!").error();
            } else {
                return Ingredient.EMPTY;
            }
        } else {
            Pattern reg = UtilsJS.parseRegex(s);
            if (reg != null) {
                return IngredientPlatformHelper.get().regex(reg);
            } else {
                Item item = RegistryInfo.ITEM.getValue(new ResourceLocation(s));
                return item != null && item != Items.AIR ? item.kjs$asIngredient() : Ingredient.EMPTY;
            }
        }
    }

    static Ingredient ofJson(JsonElement json) {
        if (json != null && !json.isJsonNull() && (!json.isJsonArray() || !json.getAsJsonArray().isEmpty())) {
            return json.isJsonPrimitive() ? of(json.getAsString()) : Ingredient.fromJson(json);
        } else {
            return Ingredient.EMPTY;
        }
    }

    static Ingredient ofNetwork(FriendlyByteBuf buf) {
        return Ingredient.fromNetwork(buf);
    }
}