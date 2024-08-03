package snownee.lychee.item_inside;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;
import snownee.lychee.RecipeSerializers;
import snownee.lychee.RecipeTypes;
import snownee.lychee.core.ItemShapelessContext;
import snownee.lychee.core.def.BlockPredicateHelper;
import snownee.lychee.core.recipe.BlockKeyRecipe;
import snownee.lychee.core.recipe.ItemShapelessRecipe;
import snownee.lychee.core.recipe.LycheeCounter;
import snownee.lychee.core.recipe.LycheeRecipe;
import snownee.lychee.core.recipe.type.LycheeRecipeType;
import snownee.lychee.util.CommonProxy;
import snownee.lychee.util.RecipeMatcher;

public class ItemInsideRecipe extends ItemShapelessRecipe<ItemInsideRecipe> implements BlockKeyRecipe<ItemInsideRecipe> {

    private int time;

    protected BlockPredicate block;

    private boolean special;

    public ItemInsideRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public LycheeRecipe.Serializer<?> getSerializer() {
        return RecipeSerializers.ITEM_INSIDE;
    }

    @Override
    public LycheeRecipeType<?, ?> getType() {
        return RecipeTypes.ITEM_INSIDE;
    }

    public int getTime() {
        return this.time;
    }

    public boolean tickOrApply(ItemShapelessContext ctx) {
        LycheeCounter entity = (LycheeCounter) ctx.getParam(LootContextParams.THIS_ENTITY);
        if (entity.lychee$getCount() >= this.time) {
            entity.lychee$setRecipeId(null);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean matches(ItemShapelessContext ctx, Level pLevel) {
        if (ctx.totalItems < this.ingredients.size()) {
            return false;
        } else if (!BlockPredicateHelper.fastMatch(this.block, ctx)) {
            return false;
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
    public BlockPredicate getBlock() {
        return this.block;
    }

    @Nullable
    public ItemInsideRecipeType.Cache buildCache(Object2FloatMap<Item> itemWeights, List<ItemInsideRecipe> specialRecipes) {
        this.special = !this.m_7527_().stream().anyMatch(CommonProxy::isSimpleIngredient);
        if (this.special) {
            specialRecipes.add(this);
            return null;
        } else {
            List<Set<Item>> mappedIngredients = this.m_7527_().stream().map(Ingredient::m_43908_).map($ -> {
                Set<Item> items = Sets.newHashSet();
                float weight = 1.0F / (float) $.length;
                for (ItemStack stack : $) {
                    items.add(stack.getItem());
                    itemWeights.merge(stack.getItem(), weight, Float::sum);
                }
                return items;
            }).toList();
            return new ItemInsideRecipeType.Cache(this, mappedIngredients);
        }
    }

    @Override
    public boolean isSpecial() {
        return this.special;
    }

    public static class Serializer extends ItemShapelessRecipe.Serializer<ItemInsideRecipe> {

        public Serializer() {
            super(ItemInsideRecipe::new);
        }

        public void fromJson(ItemInsideRecipe pRecipe, JsonObject pSerializedRecipe) {
            super.fromJson(pRecipe, pSerializedRecipe);
            pRecipe.time = GsonHelper.getAsInt(pSerializedRecipe, "time", 0);
            pRecipe.block = BlockPredicateHelper.fromJson(pSerializedRecipe.get("block_in"));
            Preconditions.checkArgument(!pRecipe.ingredients.isEmpty(), "Ingredients cannot be empty");
        }

        public void fromNetwork(ItemInsideRecipe pRecipe, FriendlyByteBuf pBuffer) {
            super.fromNetwork(pRecipe, pBuffer);
            pRecipe.time = pBuffer.readVarInt();
            pRecipe.block = BlockPredicateHelper.fromNetwork(pBuffer);
        }

        public void toNetwork0(FriendlyByteBuf pBuffer, ItemInsideRecipe pRecipe) {
            super.toNetwork0(pBuffer, pRecipe);
            pBuffer.writeVarInt(pRecipe.time);
            BlockPredicateHelper.toNetwork(pRecipe.block, pBuffer);
        }
    }
}