package net.minecraftforge.common.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

public class IntersectionIngredient extends AbstractIngredient {

    private final List<Ingredient> children;

    private final boolean isSimple;

    private ItemStack[] intersectedMatchingStacks = null;

    private IntList packedMatchingStacks = null;

    protected IntersectionIngredient(List<Ingredient> children) {
        if (children.size() < 2) {
            throw new IllegalArgumentException("Cannot create an IntersectionIngredient with one or no children");
        } else {
            this.children = Collections.unmodifiableList(children);
            this.isSimple = children.stream().allMatch(Ingredient::isSimple);
        }
    }

    public static Ingredient of(Ingredient... ingredients) {
        if (ingredients.length == 0) {
            throw new IllegalArgumentException("Cannot create an IntersectionIngredient with no children, use Ingredient.of() to create an empty ingredient");
        } else {
            return (Ingredient) (ingredients.length == 1 ? ingredients[0] : new IntersectionIngredient(Arrays.asList(ingredients)));
        }
    }

    @Override
    public boolean test(@Nullable ItemStack stack) {
        if (stack != null && !stack.isEmpty()) {
            for (Ingredient ingredient : this.children) {
                if (!ingredient.test(stack)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ItemStack[] getItems() {
        if (this.intersectedMatchingStacks == null) {
            this.intersectedMatchingStacks = (ItemStack[]) Arrays.stream(((Ingredient) this.children.get(0)).getItems()).filter(stack -> {
                for (int i = 1; i < this.children.size(); i++) {
                    if (!((Ingredient) this.children.get(i)).test(stack)) {
                        return false;
                    }
                }
                return true;
            }).toArray(ItemStack[]::new);
        }
        return this.intersectedMatchingStacks;
    }

    @Override
    public boolean isEmpty() {
        return this.children.stream().anyMatch(Ingredient::m_43947_);
    }

    @Override
    public boolean isSimple() {
        return this.isSimple;
    }

    protected void invalidate() {
        super.invalidate();
        this.intersectedMatchingStacks = null;
        this.packedMatchingStacks = null;
    }

    @Override
    public IntList getStackingIds() {
        if (this.packedMatchingStacks == null || this.checkInvalidation()) {
            this.markValid();
            ItemStack[] matchingStacks = this.getItems();
            this.packedMatchingStacks = new IntArrayList(matchingStacks.length);
            for (ItemStack stack : matchingStacks) {
                this.packedMatchingStacks.add(StackedContents.getStackingIndex(stack));
            }
            this.packedMatchingStacks.sort(IntComparators.NATURAL_COMPARATOR);
        }
        return this.packedMatchingStacks;
    }

    @Override
    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", CraftingHelper.getID(IntersectionIngredient.Serializer.INSTANCE).toString());
        JsonArray array = new JsonArray();
        for (Ingredient ingredient : this.children) {
            array.add(ingredient.toJson());
        }
        json.add("children", array);
        return json;
    }

    @Override
    public IIngredientSerializer<IntersectionIngredient> getSerializer() {
        return IntersectionIngredient.Serializer.INSTANCE;
    }

    public static class Serializer implements IIngredientSerializer<IntersectionIngredient> {

        public static final IIngredientSerializer<IntersectionIngredient> INSTANCE = new IntersectionIngredient.Serializer();

        public IntersectionIngredient parse(JsonObject json) {
            JsonArray children = GsonHelper.getAsJsonArray(json, "children");
            if (children.size() < 2) {
                throw new JsonSyntaxException("Must have at least two children for an intersection ingredient");
            } else {
                return new IntersectionIngredient(IntStream.range(0, children.size()).mapToObj(i -> Ingredient.fromJson(children.get(i), false)).toList());
            }
        }

        public IntersectionIngredient parse(FriendlyByteBuf buffer) {
            return new IntersectionIngredient(Stream.generate(() -> Ingredient.fromNetwork(buffer)).limit((long) buffer.readVarInt()).toList());
        }

        public void write(FriendlyByteBuf buffer, IntersectionIngredient intersection) {
            buffer.writeVarInt(intersection.children.size());
            for (Ingredient ingredient : intersection.children) {
                ingredient.toNetwork(buffer);
            }
        }
    }
}