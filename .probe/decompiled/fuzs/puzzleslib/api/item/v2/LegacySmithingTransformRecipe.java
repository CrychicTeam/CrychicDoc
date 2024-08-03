package fuzs.puzzleslib.api.item.v2;

import com.google.gson.JsonObject;
import java.util.Objects;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
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

@Deprecated(forRemoval = true)
public class LegacySmithingTransformRecipe extends CustomRecipe {

    public static final String RECIPE_SERIALIZER_ID = "legacy_smithing_transform";

    private final Ingredient base;

    private final Ingredient addition;

    private final ItemStack result;

    public LegacySmithingTransformRecipe(ResourceLocation id, CraftingBookCategory category, Ingredient base, Ingredient addition, ItemStack result) {
        super(id, category);
        this.base = base;
        this.addition = addition;
        this.result = result;
    }

    public static RecipeSerializer<?> getModSerializer(String modId) {
        RecipeSerializer<?> recipeSerializer = BuiltInRegistries.RECIPE_SERIALIZER.get(new ResourceLocation(modId, "legacy_smithing_transform"));
        Objects.requireNonNull(recipeSerializer, "legacy smithing transform recipe serializer for %s is null".formatted(modId));
        return recipeSerializer;
    }

    public boolean matches(CraftingContainer container, Level level) {
        int baseItemCount = 0;
        int additionItemCount = 0;
        for (int k = 0; k < container.m_6643_(); k++) {
            ItemStack itemStack = container.m_8020_(k);
            if (!itemStack.isEmpty()) {
                if (this.base.test(itemStack)) {
                    baseItemCount++;
                } else {
                    if (!this.addition.test(itemStack)) {
                        return false;
                    }
                    additionItemCount++;
                }
                if (additionItemCount > 1 || baseItemCount > 1) {
                    return false;
                }
            }
        }
        return baseItemCount == 1 && additionItemCount == 1;
    }

    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        ItemStack result = this.result.copy();
        for (int i = 0; i < container.m_6643_(); i++) {
            ItemStack itemStack = container.m_8020_(i);
            if (this.base.test(itemStack)) {
                CompoundTag compoundTag = itemStack.getTag();
                if (compoundTag != null) {
                    result.setTag(compoundTag.copy());
                }
                return result;
            }
        }
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonNullList = NonNullList.create();
        nonNullList.add(this.base);
        nonNullList.add(this.addition);
        return nonNullList;
    }

    @Override
    public boolean isSpecial() {
        return false;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return getModSerializer(this.m_6423_().getNamespace());
    }

    public static class Serializer implements RecipeSerializer<LegacySmithingTransformRecipe> {

        public LegacySmithingTransformRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            CraftingBookCategory craftingBookCategory = (CraftingBookCategory) CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(jsonObject, "category", null), CraftingBookCategory.MISC);
            Ingredient base = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, "base"));
            Ingredient addition = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, "addition"));
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
            return new LegacySmithingTransformRecipe(resourceLocation, craftingBookCategory, base, addition, result);
        }

        public LegacySmithingTransformRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
            CraftingBookCategory craftingBookCategory = friendlyByteBuf.readEnum(CraftingBookCategory.class);
            Ingredient base = Ingredient.fromNetwork(friendlyByteBuf);
            Ingredient addition = Ingredient.fromNetwork(friendlyByteBuf);
            ItemStack result = friendlyByteBuf.readItem();
            return new LegacySmithingTransformRecipe(resourceLocation, craftingBookCategory, base, addition, result);
        }

        public void toNetwork(FriendlyByteBuf friendlyByteBuf, LegacySmithingTransformRecipe smithingTransformRecipe) {
            friendlyByteBuf.writeEnum(smithingTransformRecipe.m_245232_());
            smithingTransformRecipe.base.toNetwork(friendlyByteBuf);
            smithingTransformRecipe.addition.toNetwork(friendlyByteBuf);
            friendlyByteBuf.writeItem(smithingTransformRecipe.result);
        }
    }
}