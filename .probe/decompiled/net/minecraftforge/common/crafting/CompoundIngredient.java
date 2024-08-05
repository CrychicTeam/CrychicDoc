package net.minecraftforge.common.crafting;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CompoundIngredient extends AbstractIngredient {

    private List<Ingredient> children;

    private ItemStack[] stacks;

    private IntList itemIds;

    private final boolean isSimple;

    protected CompoundIngredient(List<Ingredient> children) {
        this.children = Collections.unmodifiableList(children);
        this.isSimple = children.stream().allMatch(Ingredient::isSimple);
    }

    public static Ingredient of(Ingredient... children) {
        if (children.length == 0) {
            throw new IllegalArgumentException("Cannot create a compound ingredient with no children, use Ingredient.of() to create an empty ingredient");
        } else if (children.length == 1) {
            return children[0];
        } else {
            List<Ingredient> vanillaIngredients = new ArrayList();
            List<Ingredient> allIngredients = new ArrayList();
            for (Ingredient child : children) {
                if (child.getSerializer() == VanillaIngredientSerializer.INSTANCE) {
                    vanillaIngredients.add(child);
                } else {
                    allIngredients.add(child);
                }
            }
            if (!vanillaIngredients.isEmpty()) {
                allIngredients.add(merge(vanillaIngredients));
            }
            return (Ingredient) (allIngredients.size() == 1 ? (Ingredient) allIngredients.get(0) : new CompoundIngredient(allIngredients));
        }
    }

    @NotNull
    @Override
    public ItemStack[] getItems() {
        if (this.stacks == null) {
            List<ItemStack> tmp = Lists.newArrayList();
            for (Ingredient child : this.children) {
                Collections.addAll(tmp, child.getItems());
            }
            this.stacks = (ItemStack[]) tmp.toArray(new ItemStack[tmp.size()]);
        }
        return this.stacks;
    }

    @NotNull
    @Override
    public IntList getStackingIds() {
        boolean childrenNeedInvalidation = false;
        for (Ingredient child : this.children) {
            childrenNeedInvalidation |= child.checkInvalidation();
        }
        if (childrenNeedInvalidation || this.itemIds == null || this.checkInvalidation()) {
            this.markValid();
            this.itemIds = new IntArrayList();
            for (Ingredient child : this.children) {
                this.itemIds.addAll(child.getStackingIds());
            }
            this.itemIds.sort(IntComparators.NATURAL_COMPARATOR);
        }
        return this.itemIds;
    }

    @Override
    public boolean test(@Nullable ItemStack target) {
        return target == null ? false : this.children.stream().anyMatch(c -> c.test(target));
    }

    protected void invalidate() {
        this.itemIds = null;
        this.stacks = null;
    }

    @Override
    public boolean isSimple() {
        return this.isSimple;
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return CompoundIngredient.Serializer.INSTANCE;
    }

    @NotNull
    public Collection<Ingredient> getChildren() {
        return this.children;
    }

    @Override
    public JsonElement toJson() {
        if (this.children.size() == 1) {
            return ((Ingredient) this.children.get(0)).toJson();
        } else {
            JsonArray json = new JsonArray();
            this.children.stream().forEach(e -> json.add(e.toJson()));
            return json;
        }
    }

    @Override
    public boolean isEmpty() {
        return this.children.stream().allMatch(Ingredient::m_43947_);
    }

    public static class Serializer implements IIngredientSerializer<CompoundIngredient> {

        public static final CompoundIngredient.Serializer INSTANCE = new CompoundIngredient.Serializer();

        public CompoundIngredient parse(FriendlyByteBuf buffer) {
            return new CompoundIngredient((List<Ingredient>) Stream.generate(() -> Ingredient.fromNetwork(buffer)).limit((long) buffer.readVarInt()).collect(Collectors.toList()));
        }

        public CompoundIngredient parse(JsonObject json) {
            throw new JsonSyntaxException("CompoundIngredient should not be directly referenced in json, just use an array of ingredients.");
        }

        public void write(FriendlyByteBuf buffer, CompoundIngredient ingredient) {
            buffer.writeVarInt(ingredient.children.size());
            ingredient.children.forEach(c -> c.toNetwork(buffer));
        }
    }
}