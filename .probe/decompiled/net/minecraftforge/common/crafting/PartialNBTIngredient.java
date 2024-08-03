package net.minecraftforge.common.crafting;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class PartialNBTIngredient extends AbstractIngredient {

    private final Set<Item> items;

    private final CompoundTag nbt;

    private final NbtPredicate predicate;

    protected PartialNBTIngredient(Set<Item> items, CompoundTag nbt) {
        super(items.stream().map(item -> {
            ItemStack stack = new ItemStack(item);
            stack.setTag(nbt.copy());
            return new Ingredient.ItemValue(stack);
        }));
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Cannot create a PartialNBTIngredient with no items");
        } else {
            this.items = Collections.unmodifiableSet(items);
            this.nbt = nbt;
            this.predicate = new NbtPredicate(nbt);
        }
    }

    public static PartialNBTIngredient of(CompoundTag nbt, ItemLike... items) {
        return new PartialNBTIngredient((Set<Item>) Arrays.stream(items).map(ItemLike::m_5456_).collect(Collectors.toSet()), nbt);
    }

    public static PartialNBTIngredient of(ItemLike item, CompoundTag nbt) {
        return new PartialNBTIngredient(Set.of(item.asItem()), nbt);
    }

    @Override
    public boolean test(@Nullable ItemStack input) {
        return input == null ? false : this.items.contains(input.getItem()) && this.predicate.matches(input.getShareTag());
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return PartialNBTIngredient.Serializer.INSTANCE;
    }

    @Override
    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", CraftingHelper.getID(PartialNBTIngredient.Serializer.INSTANCE).toString());
        if (this.items.size() == 1) {
            json.addProperty("item", ForgeRegistries.ITEMS.getKey((Item) this.items.iterator().next()).toString());
        } else {
            JsonArray items = new JsonArray();
            this.items.stream().map(ForgeRegistries.ITEMS::getKey).sorted().forEach(name -> items.add(name.toString()));
            json.add("items", items);
        }
        json.addProperty("nbt", this.nbt.toString());
        return json;
    }

    public static class Serializer implements IIngredientSerializer<PartialNBTIngredient> {

        public static final PartialNBTIngredient.Serializer INSTANCE = new PartialNBTIngredient.Serializer();

        public PartialNBTIngredient parse(JsonObject json) {
            Set<Item> items;
            if (json.has("item")) {
                items = Set.of(CraftingHelper.getItem(GsonHelper.getAsString(json, "item"), true));
            } else {
                if (!json.has("items")) {
                    throw new JsonSyntaxException("Must set either 'item' or 'items'");
                }
                Builder<Item> builder = ImmutableSet.builder();
                JsonArray itemArray = GsonHelper.getAsJsonArray(json, "items");
                for (int i = 0; i < itemArray.size(); i++) {
                    builder.add(CraftingHelper.getItem(GsonHelper.convertToString(itemArray.get(i), "items[" + i + "]"), true));
                }
                items = builder.build();
            }
            if (!json.has("nbt")) {
                throw new JsonSyntaxException("Missing nbt, expected to find a String or JsonObject");
            } else {
                CompoundTag nbt = CraftingHelper.getNBT(json.get("nbt"));
                return new PartialNBTIngredient(items, nbt);
            }
        }

        public PartialNBTIngredient parse(FriendlyByteBuf buffer) {
            Set<Item> items = (Set<Item>) Stream.generate(() -> (Item) buffer.readRegistryIdUnsafe(ForgeRegistries.ITEMS)).limit((long) buffer.readVarInt()).collect(Collectors.toSet());
            CompoundTag nbt = buffer.readNbt();
            return new PartialNBTIngredient(items, (CompoundTag) Objects.requireNonNull(nbt));
        }

        public void write(FriendlyByteBuf buffer, PartialNBTIngredient ingredient) {
            buffer.writeVarInt(ingredient.items.size());
            for (Item item : ingredient.items) {
                buffer.writeRegistryIdUnsafe(ForgeRegistries.ITEMS, item);
            }
            buffer.writeNbt(ingredient.nbt);
        }
    }
}