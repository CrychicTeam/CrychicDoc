package snownee.lychee.block_exploding;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import snownee.lychee.RecipeSerializers;
import snownee.lychee.RecipeTypes;
import snownee.lychee.core.def.BlockPredicateHelper;
import snownee.lychee.core.recipe.BlockKeyRecipe;
import snownee.lychee.core.recipe.LycheeRecipe;
import snownee.lychee.core.recipe.type.LycheeRecipeType;

public class BlockExplodingRecipe extends LycheeRecipe<BlockExplodingContext> implements BlockKeyRecipe<BlockExplodingRecipe> {

    private BlockPredicate block;

    public BlockExplodingRecipe(ResourceLocation id) {
        super(id);
    }

    public boolean matches(BlockExplodingContext ctx, Level pLevel) {
        return BlockPredicateHelper.fastMatch(this.block, ctx);
    }

    @Override
    public BlockPredicate getBlock() {
        return this.block;
    }

    @Override
    public LycheeRecipe.Serializer<?> getSerializer() {
        return RecipeSerializers.BLOCK_EXPLODING;
    }

    @Override
    public LycheeRecipeType<?, ?> getType() {
        return RecipeTypes.BLOCK_EXPLODING;
    }

    public int compareTo(BlockExplodingRecipe that) {
        int i = Integer.compare(this.m_5598_() ? 1 : 0, that.m_5598_() ? 1 : 0);
        if (i != 0) {
            return i;
        } else {
            i = Integer.compare(this.block == BlockPredicate.ANY ? 1 : 0, that.block == BlockPredicate.ANY ? 1 : 0);
            return i != 0 ? i : this.m_6423_().compareTo(that.m_6423_());
        }
    }

    public static class Serializer extends LycheeRecipe.Serializer<BlockExplodingRecipe> {

        public Serializer() {
            super(BlockExplodingRecipe::new);
        }

        public void fromJson(BlockExplodingRecipe pRecipe, JsonObject pSerializedRecipe) {
            pRecipe.block = BlockPredicateHelper.fromJson(pSerializedRecipe.get("block_in"));
        }

        public void fromNetwork(BlockExplodingRecipe pRecipe, FriendlyByteBuf pBuffer) {
            pRecipe.block = BlockPredicateHelper.fromNetwork(pBuffer);
        }

        public void toNetwork0(FriendlyByteBuf pBuffer, BlockExplodingRecipe pRecipe) {
            BlockPredicateHelper.toNetwork(pRecipe.block, pBuffer);
        }
    }
}