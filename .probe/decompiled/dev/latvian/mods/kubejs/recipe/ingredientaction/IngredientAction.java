package dev.latvian.mods.kubejs.recipe.ingredientaction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.architectury.hooks.item.ItemStackHooks;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public abstract class IngredientAction extends IngredientActionFilter {

    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    public static final Map<String, Function<JsonObject, IngredientAction>> FACTORY_MAP = new HashMap();

    public static List<IngredientAction> parseList(JsonElement json) {
        if (json != null && json.isJsonArray()) {
            List<IngredientAction> list = new ArrayList();
            for (JsonElement e : json.getAsJsonArray()) {
                JsonObject o = e.getAsJsonObject();
                Function<JsonObject, IngredientAction> factory = (Function<JsonObject, IngredientAction>) FACTORY_MAP.get(o.has("type") ? o.get("type").getAsString() : "");
                IngredientAction action = factory == null ? null : (IngredientAction) factory.apply(o);
                if (action != null) {
                    action.filterIndex = o.has("filter_index") ? o.get("filter_index").getAsInt() : -1;
                    action.filterIngredient = o.has("filter_ingredient") ? IngredientJS.of(o.get("filter_ingredient")) : null;
                    list.add(action);
                }
            }
            return list.isEmpty() ? List.of() : list;
        } else {
            return List.of();
        }
    }

    public static List<IngredientAction> readList(FriendlyByteBuf buf) {
        int s = buf.readVarInt();
        if (s <= 0) {
            return List.of();
        } else {
            List<IngredientAction> list = new ArrayList();
            for (int i = 0; i < s; i++) {
                Function<JsonObject, IngredientAction> factory = (Function<JsonObject, IngredientAction>) FACTORY_MAP.get(buf.readUtf());
                IngredientAction action = factory == null ? null : (IngredientAction) factory.apply((JsonObject) GSON.fromJson(buf.readUtf(), JsonObject.class));
                if (action != null) {
                    action.filterIndex = buf.readVarInt();
                    String ij = buf.readUtf();
                    action.filterIngredient = ij.isEmpty() ? null : IngredientJS.of(GSON.fromJson(ij, JsonObject.class));
                    list.add(action);
                }
            }
            return list.isEmpty() ? List.of() : list;
        }
    }

    public static void writeList(FriendlyByteBuf buf, @Nullable List<IngredientAction> list) {
        if (list != null && !list.isEmpty()) {
            buf.writeVarInt(list.size());
            for (IngredientAction action : list) {
                buf.writeUtf(action.getType());
                JsonObject json = new JsonObject();
                action.toJson(json);
                buf.writeUtf(GSON.toJson(json));
                buf.writeVarInt(action.filterIndex);
                buf.writeUtf(action.filterIngredient == null ? "" : GSON.toJson(action.filterIngredient.toJson()));
            }
        } else {
            buf.writeVarInt(0);
        }
    }

    public static ItemStack getRemaining(CraftingContainer container, int index, List<IngredientAction> ingredientActions) {
        ItemStack stack = container.m_8020_(index);
        if (stack != null && !stack.isEmpty()) {
            for (IngredientAction action : ingredientActions) {
                if (action.checkFilter(index, stack)) {
                    return action.transform(stack.copy(), index, container);
                }
            }
            return ItemStackHooks.hasCraftingRemainingItem(stack) ? ItemStackHooks.getCraftingRemainingItem(stack) : ItemStack.EMPTY;
        } else {
            return ItemStack.EMPTY;
        }
    }

    public abstract ItemStack transform(ItemStack var1, int var2, CraftingContainer var3);

    public abstract String getType();

    public void toJson(JsonObject json) {
    }

    public final JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", this.getType());
        if (this.filterIngredient != null) {
            json.add("filter_ingredient", this.filterIngredient.toJson());
        }
        if (this.filterIndex != -1) {
            json.addProperty("filter_index", this.filterIndex);
        }
        this.toJson(json);
        return json;
    }

    static {
        FACTORY_MAP.put("custom", (Function) json -> new CustomIngredientAction(json.get("id").getAsString()));
        FACTORY_MAP.put("damage", (Function) json -> new DamageAction(json.get("damage").getAsInt()));
        FACTORY_MAP.put("replace", (Function) json -> new ReplaceAction(ItemStackJS.resultFromRecipeJson(json.get("item"))));
        FACTORY_MAP.put("keep", (Function) json -> new KeepAction());
        FACTORY_MAP.put("consume", (Function) json -> new ConsumeAction());
    }
}