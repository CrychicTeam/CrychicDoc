package snownee.lychee.core.recipe;

import com.google.gson.JsonObject;
import java.util.function.Function;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.def.BlockPredicateHelper;
import snownee.lychee.util.CommonProxy;

public abstract class ItemAndBlockRecipe<C extends LycheeContext> extends LycheeRecipe<C> implements BlockKeyRecipe<ItemAndBlockRecipe<C>> {

    protected Ingredient input;

    protected BlockPredicate block;

    public ItemAndBlockRecipe(ResourceLocation id) {
        super(id);
    }

    public Ingredient getInput() {
        return this.input;
    }

    @Override
    public BlockPredicate getBlock() {
        return this.block;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, this.input);
    }

    public boolean matches(LycheeContext ctx, Level pLevel) {
        Entity thisEntity = ctx.getParam(LootContextParams.THIS_ENTITY);
        ItemStack stack;
        if (thisEntity instanceof ItemEntity) {
            stack = ((ItemEntity) thisEntity).getItem();
        } else {
            stack = ctx.getItem(0);
        }
        return this.input.test(stack) && BlockPredicateHelper.fastMatch(this.block, ctx);
    }

    public int compareTo(ItemAndBlockRecipe<C> that) {
        int i = Integer.compare(this.getMaxRepeats().m_55327_() ? 1 : 0, that.getMaxRepeats().m_55327_() ? 1 : 0);
        if (i != 0) {
            return i;
        } else {
            i = Integer.compare(this.m_5598_() ? 1 : 0, that.m_5598_() ? 1 : 0);
            if (i != 0) {
                return i;
            } else {
                i = Integer.compare(this.block == BlockPredicate.ANY ? 1 : 0, that.block == BlockPredicate.ANY ? 1 : 0);
                if (i != 0) {
                    return i;
                } else {
                    i = Integer.compare(CommonProxy.isSimpleIngredient(this.input) ? 1 : 0, CommonProxy.isSimpleIngredient(that.input) ? 1 : 0);
                    return i != 0 ? i : this.m_6423_().compareTo(that.m_6423_());
                }
            }
        }
    }

    public static class Serializer<T extends ItemAndBlockRecipe<?>> extends LycheeRecipe.Serializer<T> {

        public Serializer(Function<ResourceLocation, T> factory) {
            super(factory);
        }

        public void fromJson(T pRecipe, JsonObject pSerializedRecipe) {
            pRecipe.input = parseIngredientOrAir(pSerializedRecipe.get("item_in"));
            pRecipe.block = BlockPredicateHelper.fromJson(pSerializedRecipe.get("block_in"));
        }

        public void fromNetwork(T pRecipe, FriendlyByteBuf pBuffer) {
            pRecipe.input = Ingredient.fromNetwork(pBuffer);
            pRecipe.block = BlockPredicateHelper.fromNetwork(pBuffer);
        }

        public void toNetwork0(FriendlyByteBuf pBuffer, T pRecipe) {
            pRecipe.input.toNetwork(pBuffer);
            BlockPredicateHelper.toNetwork(pRecipe.block, pBuffer);
        }
    }
}