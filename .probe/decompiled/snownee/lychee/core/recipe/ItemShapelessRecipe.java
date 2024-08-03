package snownee.lychee.core.recipe;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import snownee.lychee.core.ItemShapelessContext;
import snownee.lychee.util.RecipeMatcher;

public abstract class ItemShapelessRecipe<T extends ItemShapelessRecipe<T>> extends LycheeRecipe<ItemShapelessContext> implements Comparable<T> {

    public static final int MAX_INGREDIENTS = 27;

    protected NonNullList<Ingredient> ingredients = NonNullList.create();

    public ItemShapelessRecipe(ResourceLocation id) {
        super(id);
    }

    public boolean matches(ItemShapelessContext ctx, Level pLevel) {
        if (ctx.totalItems < this.ingredients.size()) {
            return false;
        } else if (this.ingredients.isEmpty()) {
            return true;
        } else {
            List<ItemEntity> itemEntities = ctx.itemEntities.stream().filter($ -> this.ingredients.stream().anyMatch(ingredient -> ingredient.test($.getItem()))).limit(27L).toList();
            List<ItemStack> items = itemEntities.stream().map(ItemEntity::m_32055_).toList();
            int[] amount = items.stream().mapToInt(ItemStack::m_41613_).toArray();
            Optional<RecipeMatcher<ItemStack>> match = RecipeMatcher.findMatches(items, this.ingredients, amount);
            if (match.isEmpty()) {
                return false;
            } else {
                ctx.filteredItems = itemEntities;
                ctx.setMatch((RecipeMatcher<ItemStack>) match.get());
                return true;
            }
        }
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public int compareTo(T that) {
        int i = Integer.compare(this.getMaxRepeats().m_55327_() ? 1 : 0, that.getMaxRepeats().m_55327_() ? 1 : 0);
        if (i != 0) {
            return i;
        } else {
            i = Integer.compare(this.m_5598_() ? 1 : 0, that.m_5598_() ? 1 : 0);
            if (i != 0) {
                return i;
            } else {
                i = -Integer.compare(this.ingredients.size(), that.ingredients.size());
                return i != 0 ? i : this.m_6423_().compareTo(that.m_6423_());
            }
        }
    }

    public static class Serializer<T extends ItemShapelessRecipe<T>> extends LycheeRecipe.Serializer<T> {

        public Serializer(Function<ResourceLocation, T> factory) {
            super(factory);
        }

        public void fromJson(T pRecipe, JsonObject pSerializedRecipe) {
            if (pSerializedRecipe.has("item_in")) {
                JsonElement itemIn = pSerializedRecipe.get("item_in");
                if (itemIn.isJsonArray()) {
                    itemIn.getAsJsonArray().forEach($ -> pRecipe.ingredients.add(Ingredient.fromJson($)));
                } else {
                    pRecipe.ingredients.add(Ingredient.fromJson(itemIn));
                }
            }
            Preconditions.checkArgument(pRecipe.ingredients.size() <= 27, "Ingredients cannot be more than %s", 27);
        }

        public void fromNetwork(T pRecipe, FriendlyByteBuf pBuffer) {
            pBuffer.readCollection(i -> pRecipe.ingredients, Ingredient::m_43940_);
        }

        public void toNetwork0(FriendlyByteBuf pBuffer, T pRecipe) {
            pBuffer.writeCollection((Collection<T>) pRecipe.ingredients, (b, i) -> i.toNetwork(b));
        }
    }
}