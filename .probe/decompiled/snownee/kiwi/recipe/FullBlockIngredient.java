package snownee.kiwi.recipe;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.Objects;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.crafting.AbstractIngredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.common.crafting.MultiItemValue;
import org.jetbrains.annotations.NotNull;
import snownee.kiwi.Kiwi;

public class FullBlockIngredient extends AbstractIngredient {

    public static final ResourceLocation ID = Kiwi.id("full_block");

    public static final FullBlockIngredient.Serializer SERIALIZER = new FullBlockIngredient.Serializer();

    private final Ingredient example;

    protected FullBlockIngredient(Stream<? extends Ingredient.Value> itemLists, Ingredient example) {
        super(itemLists);
        this.example = example;
    }

    public static boolean isFullBlock(ItemStack stack) {
        if (!isTextureBlock(stack)) {
            return false;
        } else {
            Block block = Block.byItem(stack.getItem());
            BlockState state = block.defaultBlockState();
            try {
                if (Block.isShapeFullBlock(state.m_60768_(null, BlockPos.ZERO))) {
                    return true;
                }
            } catch (Throwable var4) {
            }
            return false;
        }
    }

    public static boolean isTextureBlock(ItemStack stack) {
        if (stack != null && !stack.isEmpty()) {
            Block block = Block.byItem(stack.getItem());
            BlockState state = block.defaultBlockState();
            return state.m_280296_() && state.m_60799_() == RenderShape.MODEL;
        } else {
            return false;
        }
    }

    @Override
    public boolean test(ItemStack stack) {
        return isFullBlock(stack);
    }

    @NotNull
    public FullBlockIngredient.Serializer getSerializer() {
        return SERIALIZER;
    }

    @NotNull
    @Override
    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", ((ResourceLocation) Objects.requireNonNull(CraftingHelper.getID(SERIALIZER))).toString());
        return jsonObject;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    public static class Serializer implements IIngredientSerializer<FullBlockIngredient> {

        public FullBlockIngredient parse(FriendlyByteBuf buffer) {
            Ingredient example = Ingredient.fromNetwork(buffer);
            MultiItemValue stackList = new MultiItemValue(ImmutableList.copyOf(example.getItems()));
            return new FullBlockIngredient(Stream.of(stackList), example);
        }

        public FullBlockIngredient parse(JsonObject json) {
            Ingredient example;
            try {
                example = CraftingHelper.getIngredient(json.get("example"), true);
            } catch (JsonSyntaxException var4) {
                example = Ingredient.EMPTY;
            }
            MultiItemValue stackList = new MultiItemValue(ImmutableList.copyOf(example.getItems()));
            return new FullBlockIngredient(Stream.of(stackList), example);
        }

        public void write(FriendlyByteBuf buffer, FullBlockIngredient ingredient) {
            ingredient.example.toNetwork(buffer);
        }
    }
}