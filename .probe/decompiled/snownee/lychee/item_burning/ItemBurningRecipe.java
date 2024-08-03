package snownee.lychee.item_burning;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import snownee.lychee.RecipeSerializers;
import snownee.lychee.RecipeTypes;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.input.ItemHolderCollection;
import snownee.lychee.core.recipe.LycheeRecipe;
import snownee.lychee.core.recipe.type.LycheeRecipeType;

public class ItemBurningRecipe extends LycheeRecipe<LycheeContext> {

    protected Ingredient input;

    public ItemBurningRecipe(ResourceLocation id) {
        super(id);
    }

    public boolean matches(LycheeContext ctx, Level pLevel) {
        ItemStack stack = ((ItemEntity) ctx.getParam(LootContextParams.THIS_ENTITY)).getItem();
        return this.input.test(stack);
    }

    @Override
    public LycheeRecipe.Serializer<?> getSerializer() {
        return RecipeSerializers.ITEM_BURNING;
    }

    @Override
    public LycheeRecipeType<?, ?> getType() {
        return RecipeTypes.ITEM_BURNING;
    }

    public Ingredient getInput() {
        return this.input;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, this.input);
    }

    public static void on(ItemEntity entity) {
        LycheeContext.Builder<LycheeContext> builder = new LycheeContext.Builder<>(entity.m_9236_());
        builder.withParameter(LootContextParams.ORIGIN, entity.m_20182_());
        builder.withParameter(LootContextParams.THIS_ENTITY, entity);
        LycheeContext ctx = builder.create(RecipeTypes.ITEM_BURNING.contextParamSet);
        RecipeTypes.ITEM_BURNING.findFirst(ctx, entity.m_9236_()).ifPresent($ -> {
            int times = $.getRandomRepeats(entity.getItem().getCount(), ctx);
            ctx.itemHolders = ItemHolderCollection.InWorld.of(entity);
            $.applyPostActions(ctx, times);
            ctx.itemHolders.postApply(true, times);
        });
    }

    public static class Serializer extends LycheeRecipe.Serializer<ItemBurningRecipe> {

        public Serializer() {
            super(ItemBurningRecipe::new);
        }

        public void fromJson(ItemBurningRecipe pRecipe, JsonObject pSerializedRecipe) {
            pRecipe.input = Ingredient.fromJson(pSerializedRecipe.get("item_in"));
        }

        public void fromNetwork(ItemBurningRecipe pRecipe, FriendlyByteBuf pBuffer) {
            pRecipe.input = Ingredient.fromNetwork(pBuffer);
        }

        public void toNetwork0(FriendlyByteBuf pBuffer, ItemBurningRecipe pRecipe) {
            pRecipe.input.toNetwork(pBuffer);
        }
    }
}