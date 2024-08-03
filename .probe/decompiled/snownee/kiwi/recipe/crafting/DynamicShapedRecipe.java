package snownee.kiwi.recipe.crafting;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.chars.Char2ObjectArrayMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import snownee.kiwi.mixin.ShapedRecipeAccess;

public abstract class DynamicShapedRecipe extends CustomRecipe {

    private int width;

    private int height;

    private NonNullList<Ingredient> recipeItems;

    public String pattern;

    public boolean differentInputs;

    public ItemStack recipeOutput;

    private String group;

    public DynamicShapedRecipe(ResourceLocation idIn, CraftingBookCategory category) {
        super(idIn, category);
    }

    public boolean matches(CraftingContainer inv, Level worldIn) {
        return this.search(inv) != null;
    }

    @Nullable
    public int[] search(CraftingContainer inv) {
        for (int x = 0; x <= inv.getWidth() - this.getRecipeWidth(); x++) {
            for (int y = 0; y <= inv.getHeight() - this.getRecipeHeight(); y++) {
                if (this.checkMatch(inv, x, y) && this.checkEmpty(inv, x, y)) {
                    return new int[] { x, y };
                }
            }
        }
        return null;
    }

    public ItemStack item(char key, CraftingContainer inv, int[] matchPos) {
        int i = this.pattern.indexOf(key);
        if (i != -1) {
            int x = matchPos[0] + i % this.width;
            int y = matchPos[1] + i / this.width;
            return inv.m_8020_(x + y * inv.getWidth());
        } else {
            return ItemStack.EMPTY;
        }
    }

    public List<ItemStack> items(char key, CraftingContainer inv, int[] matchPos) {
        List<ItemStack> items = Lists.newArrayList();
        for (int i = 0; i < this.pattern.length(); i++) {
            if (key == this.pattern.charAt(i)) {
                int x = matchPos[0] + i % this.width;
                int y = matchPos[1] + i / this.width;
                items.add(inv.m_8020_(x + y * inv.getWidth()));
            }
        }
        return items;
    }

    public abstract ItemStack assemble(CraftingContainer var1, RegistryAccess var2);

    public int getRecipeWidth() {
        return this.width;
    }

    public int getRecipeHeight() {
        return this.height;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= this.getRecipeWidth() && height >= this.getRecipeHeight();
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.recipeItems;
    }

    @Override
    public abstract RecipeSerializer<?> getSerializer();

    protected boolean checkMatch(CraftingContainer inv, int startX, int startY) {
        Char2ObjectArrayMap<ItemStack> ingredientsArrayMap = null;
        if (!this.differentInputs) {
            ingredientsArrayMap = new Char2ObjectArrayMap();
        }
        for (int y = startY; y < startY + this.getRecipeHeight(); y++) {
            for (int x = startX; x < startX + this.getRecipeWidth(); x++) {
                int rx = x - startX;
                int ry = y - startY;
                if (!this.matches(inv, x, y, rx, ry)) {
                    return false;
                }
                if (!this.differentInputs) {
                    int i = rx + ry * this.getRecipeWidth();
                    char key = this.pattern.charAt(i);
                    if (key != ' ') {
                        ItemStack stack0 = inv.m_8020_(x + y * inv.getWidth());
                        ItemStack stack1 = (ItemStack) ingredientsArrayMap.get(key);
                        if (stack1 == null) {
                            ingredientsArrayMap.put(key, stack0);
                        } else if (!ItemStack.isSameItemSameTags(stack1, stack0)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean matches(CraftingContainer inv, int x, int y, int rx, int ry) {
        Ingredient ingredient = this.recipeItems.get(rx + ry * this.getRecipeWidth());
        return ingredient.test(inv.m_8020_(x + y * inv.getWidth()));
    }

    protected boolean checkEmpty(CraftingContainer inv, int startX, int startY) {
        for (int y = 0; y < inv.getHeight(); y++) {
            int subY = y - startY;
            for (int x = 0; x < inv.getWidth(); x++) {
                int subX = x - startX;
                if ((subX < 0 || subY < 0 || subX >= this.getRecipeWidth() || subY >= this.getRecipeHeight()) && !this.getEmpty().test(inv.m_8020_(x + y * inv.getWidth()))) {
                    return false;
                }
            }
        }
        return true;
    }

    protected Predicate<ItemStack> getEmpty() {
        return Ingredient.EMPTY;
    }

    public abstract static class Serializer<T extends DynamicShapedRecipe> implements RecipeSerializer<T> {

        public static void fromJson(DynamicShapedRecipe recipe, JsonObject json) {
            recipe.group = GsonHelper.getAsString(json, "group", "");
            Map<String, Ingredient> ingredientMap = ShapedRecipeAccess.callKeyFromJson(GsonHelper.getAsJsonObject(json, "key"));
            String[] pattern = ShapedRecipeAccess.callShrink(ShapedRecipeAccess.callPatternFromJson(GsonHelper.getAsJsonArray(json, "pattern")));
            recipe.pattern = String.join("", pattern);
            recipe.width = pattern[0].length();
            recipe.height = pattern.length;
            recipe.recipeItems = ShapedRecipeAccess.callDissolvePattern(pattern, ingredientMap, recipe.width, recipe.height);
            recipe.recipeOutput = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            recipe.differentInputs = GsonHelper.getAsBoolean(json, "differentInputs", false);
        }

        public static void fromNetwork(DynamicShapedRecipe recipe, FriendlyByteBuf buffer) {
            recipe.width = buffer.readVarInt();
            recipe.height = buffer.readVarInt();
            recipe.group = buffer.readUtf(256);
            int size = recipe.width * recipe.height;
            recipe.recipeItems = NonNullList.withSize(size, Ingredient.EMPTY);
            for (int k = 0; k < size; k++) {
                recipe.recipeItems.set(k, Ingredient.fromNetwork(buffer));
            }
            recipe.recipeOutput = buffer.readItem();
            recipe.pattern = buffer.readUtf(size);
            recipe.differentInputs = buffer.readBoolean();
        }

        public void toNetwork(FriendlyByteBuf buffer, T recipe) {
            buffer.writeVarInt(recipe.getRecipeWidth());
            buffer.writeVarInt(recipe.getRecipeHeight());
            buffer.writeUtf(recipe.getGroup(), 256);
            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.toNetwork(buffer);
            }
            buffer.writeItem(recipe.recipeOutput);
            buffer.writeUtf(recipe.pattern, recipe.getIngredients().size());
            buffer.writeBoolean(recipe.differentInputs);
        }
    }
}