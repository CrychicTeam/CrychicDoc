package net.minecraftforge.common.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.Arrays;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

public class DifferenceIngredient extends AbstractIngredient {

    private final Ingredient base;

    private final Ingredient subtracted;

    private ItemStack[] filteredMatchingStacks;

    private IntList packedMatchingStacks;

    protected DifferenceIngredient(Ingredient base, Ingredient subtracted) {
        this.base = base;
        this.subtracted = subtracted;
    }

    public static DifferenceIngredient of(Ingredient base, Ingredient subtracted) {
        return new DifferenceIngredient(base, subtracted);
    }

    @Override
    public boolean test(@Nullable ItemStack stack) {
        return stack != null && !stack.isEmpty() ? this.base.test(stack) && !this.subtracted.test(stack) : false;
    }

    @Override
    public ItemStack[] getItems() {
        if (this.filteredMatchingStacks == null) {
            this.filteredMatchingStacks = (ItemStack[]) Arrays.stream(this.base.getItems()).filter(stack -> !this.subtracted.test(stack)).toArray(ItemStack[]::new);
        }
        return this.filteredMatchingStacks;
    }

    @Override
    public boolean isEmpty() {
        return this.base.isEmpty();
    }

    @Override
    public boolean isSimple() {
        return this.base.isSimple() && this.subtracted.isSimple();
    }

    protected void invalidate() {
        super.invalidate();
        this.filteredMatchingStacks = null;
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
        json.addProperty("type", CraftingHelper.getID(DifferenceIngredient.Serializer.INSTANCE).toString());
        json.add("base", this.base.toJson());
        json.add("subtracted", this.subtracted.toJson());
        return json;
    }

    @Override
    public IIngredientSerializer<DifferenceIngredient> getSerializer() {
        return DifferenceIngredient.Serializer.INSTANCE;
    }

    public static class Serializer implements IIngredientSerializer<DifferenceIngredient> {

        public static final IIngredientSerializer<DifferenceIngredient> INSTANCE = new DifferenceIngredient.Serializer();

        public DifferenceIngredient parse(JsonObject json) {
            Ingredient base = Ingredient.fromJson(json.get("base"), false);
            Ingredient without = Ingredient.fromJson(json.get("subtracted"), false);
            return new DifferenceIngredient(base, without);
        }

        public DifferenceIngredient parse(FriendlyByteBuf buffer) {
            Ingredient base = Ingredient.fromNetwork(buffer);
            Ingredient without = Ingredient.fromNetwork(buffer);
            return new DifferenceIngredient(base, without);
        }

        public void write(FriendlyByteBuf buffer, DifferenceIngredient ingredient) {
            ingredient.base.toNetwork(buffer);
            ingredient.subtracted.toNetwork(buffer);
        }
    }
}