package net.minecraft.world.item.crafting;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class ShapedRecipe implements CraftingRecipe {

    final int width;

    final int height;

    final NonNullList<Ingredient> recipeItems;

    final ItemStack result;

    private final ResourceLocation id;

    final String group;

    final CraftingBookCategory category;

    final boolean showNotification;

    public ShapedRecipe(ResourceLocation resourceLocation0, String string1, CraftingBookCategory craftingBookCategory2, int int3, int int4, NonNullList<Ingredient> nonNullListIngredient5, ItemStack itemStack6, boolean boolean7) {
        this.id = resourceLocation0;
        this.group = string1;
        this.category = craftingBookCategory2;
        this.width = int3;
        this.height = int4;
        this.recipeItems = nonNullListIngredient5;
        this.result = itemStack6;
        this.showNotification = boolean7;
    }

    public ShapedRecipe(ResourceLocation resourceLocation0, String string1, CraftingBookCategory craftingBookCategory2, int int3, int int4, NonNullList<Ingredient> nonNullListIngredient5, ItemStack itemStack6) {
        this(resourceLocation0, string1, craftingBookCategory2, int3, int4, nonNullListIngredient5, itemStack6, true);
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.SHAPED_RECIPE;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public CraftingBookCategory category() {
        return this.category;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess0) {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.recipeItems;
    }

    @Override
    public boolean showNotification() {
        return this.showNotification;
    }

    @Override
    public boolean canCraftInDimensions(int int0, int int1) {
        return int0 >= this.width && int1 >= this.height;
    }

    public boolean matches(CraftingContainer craftingContainer0, Level level1) {
        for (int $$2 = 0; $$2 <= craftingContainer0.getWidth() - this.width; $$2++) {
            for (int $$3 = 0; $$3 <= craftingContainer0.getHeight() - this.height; $$3++) {
                if (this.matches(craftingContainer0, $$2, $$3, true)) {
                    return true;
                }
                if (this.matches(craftingContainer0, $$2, $$3, false)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean matches(CraftingContainer craftingContainer0, int int1, int int2, boolean boolean3) {
        for (int $$4 = 0; $$4 < craftingContainer0.getWidth(); $$4++) {
            for (int $$5 = 0; $$5 < craftingContainer0.getHeight(); $$5++) {
                int $$6 = $$4 - int1;
                int $$7 = $$5 - int2;
                Ingredient $$8 = Ingredient.EMPTY;
                if ($$6 >= 0 && $$7 >= 0 && $$6 < this.width && $$7 < this.height) {
                    if (boolean3) {
                        $$8 = this.recipeItems.get(this.width - $$6 - 1 + $$7 * this.width);
                    } else {
                        $$8 = this.recipeItems.get($$6 + $$7 * this.width);
                    }
                }
                if (!$$8.test(craftingContainer0.m_8020_($$4 + $$5 * craftingContainer0.getWidth()))) {
                    return false;
                }
            }
        }
        return true;
    }

    public ItemStack assemble(CraftingContainer craftingContainer0, RegistryAccess registryAccess1) {
        return this.getResultItem(registryAccess1).copy();
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    static NonNullList<Ingredient> dissolvePattern(String[] string0, Map<String, Ingredient> mapStringIngredient1, int int2, int int3) {
        NonNullList<Ingredient> $$4 = NonNullList.withSize(int2 * int3, Ingredient.EMPTY);
        Set<String> $$5 = Sets.newHashSet(mapStringIngredient1.keySet());
        $$5.remove(" ");
        for (int $$6 = 0; $$6 < string0.length; $$6++) {
            for (int $$7 = 0; $$7 < string0[$$6].length(); $$7++) {
                String $$8 = string0[$$6].substring($$7, $$7 + 1);
                Ingredient $$9 = (Ingredient) mapStringIngredient1.get($$8);
                if ($$9 == null) {
                    throw new JsonSyntaxException("Pattern references symbol '" + $$8 + "' but it's not defined in the key");
                }
                $$5.remove($$8);
                $$4.set($$7 + int2 * $$6, $$9);
            }
        }
        if (!$$5.isEmpty()) {
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + $$5);
        } else {
            return $$4;
        }
    }

    @VisibleForTesting
    static String[] shrink(String... string0) {
        int $$1 = Integer.MAX_VALUE;
        int $$2 = 0;
        int $$3 = 0;
        int $$4 = 0;
        for (int $$5 = 0; $$5 < string0.length; $$5++) {
            String $$6 = string0[$$5];
            $$1 = Math.min($$1, firstNonSpace($$6));
            int $$7 = lastNonSpace($$6);
            $$2 = Math.max($$2, $$7);
            if ($$7 < 0) {
                if ($$3 == $$5) {
                    $$3++;
                }
                $$4++;
            } else {
                $$4 = 0;
            }
        }
        if (string0.length == $$4) {
            return new String[0];
        } else {
            String[] $$8 = new String[string0.length - $$4 - $$3];
            for (int $$9 = 0; $$9 < $$8.length; $$9++) {
                $$8[$$9] = string0[$$9 + $$3].substring($$1, $$2 + 1);
            }
            return $$8;
        }
    }

    @Override
    public boolean isIncomplete() {
        NonNullList<Ingredient> $$0 = this.getIngredients();
        return $$0.isEmpty() || $$0.stream().filter(p_151277_ -> !p_151277_.isEmpty()).anyMatch(p_151273_ -> p_151273_.getItems().length == 0);
    }

    private static int firstNonSpace(String string0) {
        int $$1 = 0;
        while ($$1 < string0.length() && string0.charAt($$1) == ' ') {
            $$1++;
        }
        return $$1;
    }

    private static int lastNonSpace(String string0) {
        int $$1 = string0.length() - 1;
        while ($$1 >= 0 && string0.charAt($$1) == ' ') {
            $$1--;
        }
        return $$1;
    }

    static String[] patternFromJson(JsonArray jsonArray0) {
        String[] $$1 = new String[jsonArray0.size()];
        if ($$1.length > 3) {
            throw new JsonSyntaxException("Invalid pattern: too many rows, 3 is maximum");
        } else if ($$1.length == 0) {
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        } else {
            for (int $$2 = 0; $$2 < $$1.length; $$2++) {
                String $$3 = GsonHelper.convertToString(jsonArray0.get($$2), "pattern[" + $$2 + "]");
                if ($$3.length() > 3) {
                    throw new JsonSyntaxException("Invalid pattern: too many columns, 3 is maximum");
                }
                if ($$2 > 0 && $$1[0].length() != $$3.length()) {
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                }
                $$1[$$2] = $$3;
            }
            return $$1;
        }
    }

    static Map<String, Ingredient> keyFromJson(JsonObject jsonObject0) {
        Map<String, Ingredient> $$1 = Maps.newHashMap();
        for (Entry<String, JsonElement> $$2 : jsonObject0.entrySet()) {
            if (((String) $$2.getKey()).length() != 1) {
                throw new JsonSyntaxException("Invalid key entry: '" + (String) $$2.getKey() + "' is an invalid symbol (must be 1 character only).");
            }
            if (" ".equals($$2.getKey())) {
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            }
            $$1.put((String) $$2.getKey(), Ingredient.fromJson((JsonElement) $$2.getValue(), false));
        }
        $$1.put(" ", Ingredient.EMPTY);
        return $$1;
    }

    public static ItemStack itemStackFromJson(JsonObject jsonObject0) {
        Item $$1 = itemFromJson(jsonObject0);
        if (jsonObject0.has("data")) {
            throw new JsonParseException("Disallowed data tag found");
        } else {
            int $$2 = GsonHelper.getAsInt(jsonObject0, "count", 1);
            if ($$2 < 1) {
                throw new JsonSyntaxException("Invalid output count: " + $$2);
            } else {
                return new ItemStack($$1, $$2);
            }
        }
    }

    public static Item itemFromJson(JsonObject jsonObject0) {
        String $$1 = GsonHelper.getAsString(jsonObject0, "item");
        Item $$2 = (Item) BuiltInRegistries.ITEM.m_6612_(ResourceLocation.tryParse($$1)).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + $$1 + "'"));
        if ($$2 == Items.AIR) {
            throw new JsonSyntaxException("Empty ingredient not allowed here");
        } else {
            return $$2;
        }
    }

    public static class Serializer implements RecipeSerializer<ShapedRecipe> {

        public ShapedRecipe fromJson(ResourceLocation resourceLocation0, JsonObject jsonObject1) {
            String $$2 = GsonHelper.getAsString(jsonObject1, "group", "");
            CraftingBookCategory $$3 = (CraftingBookCategory) CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(jsonObject1, "category", null), CraftingBookCategory.MISC);
            Map<String, Ingredient> $$4 = ShapedRecipe.keyFromJson(GsonHelper.getAsJsonObject(jsonObject1, "key"));
            String[] $$5 = ShapedRecipe.shrink(ShapedRecipe.patternFromJson(GsonHelper.getAsJsonArray(jsonObject1, "pattern")));
            int $$6 = $$5[0].length();
            int $$7 = $$5.length;
            NonNullList<Ingredient> $$8 = ShapedRecipe.dissolvePattern($$5, $$4, $$6, $$7);
            ItemStack $$9 = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject1, "result"));
            boolean $$10 = GsonHelper.getAsBoolean(jsonObject1, "show_notification", true);
            return new ShapedRecipe(resourceLocation0, $$2, $$3, $$6, $$7, $$8, $$9, $$10);
        }

        public ShapedRecipe fromNetwork(ResourceLocation resourceLocation0, FriendlyByteBuf friendlyByteBuf1) {
            int $$2 = friendlyByteBuf1.readVarInt();
            int $$3 = friendlyByteBuf1.readVarInt();
            String $$4 = friendlyByteBuf1.readUtf();
            CraftingBookCategory $$5 = friendlyByteBuf1.readEnum(CraftingBookCategory.class);
            NonNullList<Ingredient> $$6 = NonNullList.withSize($$2 * $$3, Ingredient.EMPTY);
            for (int $$7 = 0; $$7 < $$6.size(); $$7++) {
                $$6.set($$7, Ingredient.fromNetwork(friendlyByteBuf1));
            }
            ItemStack $$8 = friendlyByteBuf1.readItem();
            boolean $$9 = friendlyByteBuf1.readBoolean();
            return new ShapedRecipe(resourceLocation0, $$4, $$5, $$2, $$3, $$6, $$8, $$9);
        }

        public void toNetwork(FriendlyByteBuf friendlyByteBuf0, ShapedRecipe shapedRecipe1) {
            friendlyByteBuf0.writeVarInt(shapedRecipe1.width);
            friendlyByteBuf0.writeVarInt(shapedRecipe1.height);
            friendlyByteBuf0.writeUtf(shapedRecipe1.group);
            friendlyByteBuf0.writeEnum(shapedRecipe1.category);
            for (Ingredient $$2 : shapedRecipe1.recipeItems) {
                $$2.toNetwork(friendlyByteBuf0);
            }
            friendlyByteBuf0.writeItem(shapedRecipe1.result);
            friendlyByteBuf0.writeBoolean(shapedRecipe1.showNotification);
        }
    }
}