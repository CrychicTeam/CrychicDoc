package snownee.kiwi.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.Objects;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.common.crafting.AbstractIngredient;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.common.crafting.conditions.ConditionContext;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import snownee.kiwi.Kiwi;
import snownee.kiwi.mixin.RecipeManagerAccess;
import snownee.kiwi.util.Util;

public class AlternativesIngredient extends AbstractIngredient {

    public static final ResourceLocation ID = Kiwi.id("alternatives");

    public static final AlternativesIngredient.Serializer SERIALIZER = new AlternativesIngredient.Serializer();

    @Nullable
    private JsonArray options;

    private Ingredient cached;

    public AlternativesIngredient(@Nullable JsonArray options) {
        this.options = options;
    }

    @Override
    public boolean test(ItemStack stack) {
        return this.internal().test(stack);
    }

    @NotNull
    @Override
    public ItemStack[] getItems() {
        return this.internal().getItems();
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public Ingredient internal() {
        if (this.cached == null) {
            Objects.requireNonNull(this.options, "Options in AlternativesIngredient is null");
            this.cached = Ingredient.EMPTY;
            for (JsonElement option : this.options) {
                try {
                    this.cached = this.getIngredient(option);
                    if (!this.cached.isEmpty()) {
                        this.options = null;
                        break;
                    }
                } catch (IllegalStateException var4) {
                    Kiwi.LOGGER.info("Failed to parse ingredient: %s".formatted(this.options), var4);
                    this.cached = null;
                    return Ingredient.EMPTY;
                } catch (Exception var5) {
                }
            }
        }
        return this.cached;
    }

    private Ingredient getIngredient(JsonElement element) {
        if (element.isJsonObject()) {
            JsonObject jsonObject = element.getAsJsonObject();
            if (jsonObject.size() == 1 && jsonObject.has("tag")) {
                RecipeManager recipeManager = Util.getRecipeManager();
                if (recipeManager == null) {
                    throw new IllegalStateException("Unable to get recipe manager");
                }
                ICondition.IContext ctx = ((RecipeManagerAccess) recipeManager).getContext();
                if (!(ctx instanceof ConditionContext)) {
                    throw new IllegalStateException("Unable to get real condition context");
                }
                String s = jsonObject.get("tag").getAsString();
                TagKey<Item> tagKey = TagKey.create(Registries.ITEM, new ResourceLocation(s));
                if (ctx.getTag(tagKey).isEmpty()) {
                    throw new JsonSyntaxException("Tag not found: " + s);
                }
            }
            return CraftingHelper.getIngredient(element, false);
        } else if (!element.isJsonArray()) {
            throw new JsonSyntaxException("Expected item to be object or array of objects");
        } else {
            JsonArray jsonArray = element.getAsJsonArray();
            if (jsonArray.isEmpty()) {
                throw new JsonSyntaxException("Empty array");
            } else {
                Ingredient[] ingredients = new Ingredient[jsonArray.size()];
                for (int i = 0; i < jsonArray.size(); i++) {
                    ingredients[i] = this.getIngredient(jsonArray.get(i));
                }
                return CompoundIngredient.of(ingredients);
            }
        }
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return SERIALIZER;
    }

    @NotNull
    @Override
    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", ((ResourceLocation) Objects.requireNonNull(CraftingHelper.getID(SERIALIZER))).toString());
        jsonObject.add("options", (JsonElement) Objects.requireNonNull(this.options));
        return jsonObject;
    }

    public static class Serializer implements IIngredientSerializer<AlternativesIngredient> {

        @NotNull
        public AlternativesIngredient parse(FriendlyByteBuf buf) {
            Ingredient internal = Ingredient.fromNetwork(buf);
            AlternativesIngredient ingredient = new AlternativesIngredient(null);
            ingredient.cached = internal;
            return ingredient;
        }

        public AlternativesIngredient parse(JsonObject json) {
            AlternativesIngredient ingredient = new AlternativesIngredient(GsonHelper.getAsJsonArray(json, "options"));
            ingredient.internal();
            return ingredient;
        }

        public void write(FriendlyByteBuf buf, AlternativesIngredient ingredient) {
            ingredient.internal().toNetwork(buf);
        }
    }
}