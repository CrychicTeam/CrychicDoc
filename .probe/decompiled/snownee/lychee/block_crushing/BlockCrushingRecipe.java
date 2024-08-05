package snownee.lychee.block_crushing;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Optional;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import snownee.lychee.RecipeSerializers;
import snownee.lychee.RecipeTypes;
import snownee.lychee.core.def.BlockPredicateHelper;
import snownee.lychee.core.recipe.BlockKeyRecipe;
import snownee.lychee.core.recipe.LycheeRecipe;
import snownee.lychee.core.recipe.type.LycheeRecipeType;
import snownee.lychee.mixin.BlockPredicateAccess;
import snownee.lychee.util.RecipeMatcher;

public class BlockCrushingRecipe extends LycheeRecipe<BlockCrushingContext> implements BlockKeyRecipe<BlockCrushingRecipe> {

    public static final BlockPredicate ANVIL = BlockPredicate.Builder.block().of(BlockTags.ANVIL).build();

    protected BlockPredicate fallingBlock = ANVIL;

    protected BlockPredicate landingBlock = BlockPredicate.ANY;

    protected NonNullList<Ingredient> ingredients = NonNullList.create();

    public BlockCrushingRecipe(ResourceLocation id) {
        super(id);
    }

    public boolean matches(BlockCrushingContext ctx, Level pLevel) {
        if (ctx.totalItems < this.ingredients.size()) {
            return false;
        } else if (!BlockPredicateHelper.fastMatch(this.landingBlock, ctx)) {
            return false;
        } else if (!this.matchesFallingBlock(ctx.fallingBlock.getBlockState(), ctx.fallingBlock.blockData)) {
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

    public boolean matchesFallingBlock(BlockState blockstate, CompoundTag nbt) {
        if (this.fallingBlock == BlockPredicate.ANY) {
            return true;
        } else {
            BlockPredicateAccess access = (BlockPredicateAccess) this.fallingBlock;
            if (access.getTag() != null && !blockstate.m_204336_(access.getTag())) {
                return false;
            } else if (access.getBlocks() != null && !access.getBlocks().contains(blockstate.m_60734_())) {
                return false;
            } else if (!access.getProperties().matches(blockstate)) {
                return false;
            } else {
                return access.getNbt() == NbtPredicate.ANY ? true : nbt != null && access.getNbt().matches(nbt);
            }
        }
    }

    @Override
    public LycheeRecipe.Serializer<?> getSerializer() {
        return RecipeSerializers.BLOCK_CRUSHING;
    }

    @Override
    public LycheeRecipeType<?, ?> getType() {
        return RecipeTypes.BLOCK_CRUSHING;
    }

    @Override
    public BlockPredicate getBlock() {
        return this.fallingBlock;
    }

    public BlockPredicate getLandingBlock() {
        return this.landingBlock;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    @Override
    public List<BlockPredicate> getBlockInputs() {
        return List.of(this.fallingBlock, this.landingBlock);
    }

    public int compareTo(BlockCrushingRecipe that) {
        int i = Integer.compare(this.getMaxRepeats().m_55327_() ? 1 : 0, that.getMaxRepeats().m_55327_() ? 1 : 0);
        if (i != 0) {
            return i;
        } else {
            i = Integer.compare(this.m_5598_() ? 1 : 0, that.m_5598_() ? 1 : 0);
            if (i != 0) {
                return i;
            } else {
                i = Integer.compare(this.landingBlock == BlockPredicate.ANY ? 1 : 0, that.landingBlock == BlockPredicate.ANY ? 1 : 0);
                if (i != 0) {
                    return i;
                } else {
                    i = -Integer.compare(this.ingredients.size(), that.ingredients.size());
                    return i != 0 ? i : this.m_6423_().compareTo(that.m_6423_());
                }
            }
        }
    }

    public static class Serializer extends LycheeRecipe.Serializer<BlockCrushingRecipe> {

        public Serializer() {
            super(BlockCrushingRecipe::new);
        }

        public void fromJson(BlockCrushingRecipe pRecipe, JsonObject pSerializedRecipe) {
            if (pSerializedRecipe.has("falling_block")) {
                pRecipe.fallingBlock = BlockPredicateHelper.fromJson(pSerializedRecipe.get("falling_block"));
            }
            if (pSerializedRecipe.has("landing_block")) {
                pRecipe.landingBlock = BlockPredicateHelper.fromJson(pSerializedRecipe.get("landing_block"));
            }
            if (pSerializedRecipe.has("item_in")) {
                JsonElement itemIn = pSerializedRecipe.get("item_in");
                if (itemIn.isJsonArray()) {
                    itemIn.getAsJsonArray().forEach($ -> pRecipe.ingredients.add(Ingredient.fromJson($)));
                } else {
                    pRecipe.ingredients.add(Ingredient.fromJson(itemIn));
                }
                Preconditions.checkArgument(pRecipe.ingredients.size() <= 27, "Ingredients cannot be more than %s", 27);
            }
        }

        public void fromNetwork(BlockCrushingRecipe pRecipe, FriendlyByteBuf pBuffer) {
            pRecipe.fallingBlock = BlockPredicateHelper.fromNetwork(pBuffer);
            pRecipe.landingBlock = BlockPredicateHelper.fromNetwork(pBuffer);
            pBuffer.readCollection(i -> pRecipe.ingredients, Ingredient::m_43940_);
        }

        public void toNetwork0(FriendlyByteBuf pBuffer, BlockCrushingRecipe pRecipe) {
            BlockPredicateHelper.toNetwork(pRecipe.fallingBlock, pBuffer);
            BlockPredicateHelper.toNetwork(pRecipe.landingBlock, pBuffer);
            pBuffer.writeCollection(pRecipe.ingredients, (b, i) -> i.toNetwork(b));
        }
    }
}