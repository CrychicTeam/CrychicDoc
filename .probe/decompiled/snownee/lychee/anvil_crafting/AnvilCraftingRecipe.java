package snownee.lychee.anvil_crafting;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import snownee.lychee.RecipeSerializers;
import snownee.lychee.RecipeTypes;
import snownee.lychee.core.def.IntBoundsHelper;
import snownee.lychee.core.post.PostAction;
import snownee.lychee.core.recipe.LycheeRecipe;
import snownee.lychee.core.recipe.type.LycheeRecipeType;
import snownee.lychee.util.json.JsonPointer;

public class AnvilCraftingRecipe extends LycheeRecipe<AnvilContext> implements Comparable<AnvilCraftingRecipe> {

    protected Ingredient left;

    protected Ingredient right = Ingredient.EMPTY;

    protected int levelCost;

    protected int materialCost;

    protected ItemStack output;

    private List<PostAction> assembling = Collections.EMPTY_LIST;

    public AnvilCraftingRecipe(ResourceLocation id) {
        super(id);
        this.maxRepeats = IntBoundsHelper.ONE;
    }

    public boolean matches(AnvilContext ctx, Level pLevel) {
        return !this.right.isEmpty() && ctx.right.getCount() < this.materialCost ? false : this.left.test(ctx.left) && this.right.test(ctx.right);
    }

    public ItemStack assemble(AnvilContext ctx, RegistryAccess registryAccess) {
        ctx.levelCost = this.levelCost;
        ctx.materialCost = this.materialCost;
        ctx.itemHolders.replace(2, this.getResultItem(registryAccess));
        ctx.enqueueActions(this.assembling.stream(), 1, true);
        ctx.runtime.run(this, ctx);
        return ctx.m_8020_(2);
    }

    public Ingredient getLeft() {
        return this.left;
    }

    public Ingredient getRight() {
        return this.right;
    }

    public int getMaterialCost() {
        return this.materialCost;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.getResultItem();
    }

    public ItemStack getResultItem() {
        return this.output.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.right.isEmpty() ? NonNullList.of(Ingredient.EMPTY, this.left) : NonNullList.of(Ingredient.EMPTY, this.left, this.right);
    }

    @Override
    public IntList getItemIndexes(JsonPointer pointer) {
        if (pointer.size() == 1) {
            if (pointer.getString(0).equals("item_out")) {
                return IntList.of(2);
            }
            if (pointer.getString(0).equals("item_in")) {
                return this.right.isEmpty() ? IntList.of(0) : IntList.of(0, 1);
            }
        }
        if (pointer.size() == 2 && pointer.getString(0).equals("item_in")) {
            try {
                int i = pointer.getInt(1);
                if (i >= 0 && i < 2) {
                    return IntList.of(i);
                }
            } catch (NumberFormatException var3) {
            }
        }
        return IntList.of();
    }

    @Override
    public JsonPointer defaultItemPointer() {
        return ITEM_OUT;
    }

    @Override
    public LycheeRecipe.Serializer<?> getSerializer() {
        return RecipeSerializers.ANVIL_CRAFTING;
    }

    @Override
    public LycheeRecipeType<?, ?> getType() {
        return RecipeTypes.ANVIL_CRAFTING;
    }

    public int compareTo(AnvilCraftingRecipe that) {
        return Integer.compare(this.m_5598_() ? 1 : 0, that.m_5598_() ? 1 : 0);
    }

    public void addAssemblingAction(PostAction action) {
        Objects.requireNonNull(action);
        if (this.assembling == Collections.EMPTY_LIST) {
            this.assembling = Lists.newArrayList();
        }
        this.assembling.add(action);
    }

    @Override
    public Stream<PostAction> getAllActions() {
        return Stream.concat(this.getPostActions(), this.assembling.stream());
    }

    @Override
    public boolean isActionPath(JsonPointer pointer) {
        if (pointer.isRoot()) {
            return false;
        } else {
            String token = pointer.getString(0);
            return "assembling".equals(token) || "post".equals(token);
        }
    }

    @Override
    public Map<JsonPointer, List<PostAction>> getActionGroups() {
        return Map.of(POST, this.actions, new JsonPointer("/assembling"), this.assembling);
    }

    public static class Serializer extends LycheeRecipe.Serializer<AnvilCraftingRecipe> {

        public Serializer() {
            super(AnvilCraftingRecipe::new);
        }

        public void fromJson(AnvilCraftingRecipe pRecipe, JsonObject pSerializedRecipe) {
            JsonElement itemIn = pSerializedRecipe.get("item_in");
            if (itemIn.isJsonArray()) {
                JsonArray array = itemIn.getAsJsonArray();
                pRecipe.left = Ingredient.fromJson(array.get(0));
                if (array.size() > 0) {
                    pRecipe.right = Ingredient.fromJson(array.get(1));
                }
            } else {
                pRecipe.left = Ingredient.fromJson(itemIn);
            }
            PostAction.parseActions(pSerializedRecipe.get("assembling"), pRecipe::addAssemblingAction);
            pRecipe.output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "item_out"));
            pRecipe.levelCost = GsonHelper.getAsInt(pSerializedRecipe, "level_cost", 1);
            Preconditions.checkArgument(pRecipe.levelCost > 0, "level_cost must be greater than 0");
            pRecipe.materialCost = GsonHelper.getAsInt(pSerializedRecipe, "material_cost", 1);
        }

        public void fromNetwork(AnvilCraftingRecipe pRecipe, FriendlyByteBuf pBuffer) {
            pRecipe.left = Ingredient.fromNetwork(pBuffer);
            pRecipe.right = Ingredient.fromNetwork(pBuffer);
            pRecipe.output = pBuffer.readItem();
            pRecipe.levelCost = pBuffer.readVarInt();
            pRecipe.materialCost = pBuffer.readVarInt();
        }

        public void toNetwork0(FriendlyByteBuf pBuffer, AnvilCraftingRecipe pRecipe) {
            pRecipe.left.toNetwork(pBuffer);
            pRecipe.right.toNetwork(pBuffer);
            pBuffer.writeItem(pRecipe.output);
            pBuffer.writeVarInt(pRecipe.levelCost);
            pBuffer.writeVarInt(pRecipe.materialCost);
        }
    }
}