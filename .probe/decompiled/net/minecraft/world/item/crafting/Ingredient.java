package net.minecraft.world.item.crafting;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public final class Ingredient implements Predicate<ItemStack> {

    public static final Ingredient EMPTY = new Ingredient(Stream.empty());

    private final Ingredient.Value[] values;

    @Nullable
    private ItemStack[] itemStacks;

    @Nullable
    private IntList stackingIds;

    private Ingredient(Stream<? extends Ingredient.Value> streamExtendsIngredientValue0) {
        this.values = (Ingredient.Value[]) streamExtendsIngredientValue0.toArray(Ingredient.Value[]::new);
    }

    public ItemStack[] getItems() {
        if (this.itemStacks == null) {
            this.itemStacks = (ItemStack[]) Arrays.stream(this.values).flatMap(p_43916_ -> p_43916_.getItems().stream()).distinct().toArray(ItemStack[]::new);
        }
        return this.itemStacks;
    }

    public boolean test(@Nullable ItemStack itemStack0) {
        if (itemStack0 == null) {
            return false;
        } else if (this.isEmpty()) {
            return itemStack0.isEmpty();
        } else {
            for (ItemStack $$1 : this.getItems()) {
                if ($$1.is(itemStack0.getItem())) {
                    return true;
                }
            }
            return false;
        }
    }

    public IntList getStackingIds() {
        if (this.stackingIds == null) {
            ItemStack[] $$0 = this.getItems();
            this.stackingIds = new IntArrayList($$0.length);
            for (ItemStack $$1 : $$0) {
                this.stackingIds.add(StackedContents.getStackingIndex($$1));
            }
            this.stackingIds.sort(IntComparators.NATURAL_COMPARATOR);
        }
        return this.stackingIds;
    }

    public void toNetwork(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeCollection(Arrays.asList(this.getItems()), FriendlyByteBuf::m_130055_);
    }

    public JsonElement toJson() {
        if (this.values.length == 1) {
            return this.values[0].serialize();
        } else {
            JsonArray $$0 = new JsonArray();
            for (Ingredient.Value $$1 : this.values) {
                $$0.add($$1.serialize());
            }
            return $$0;
        }
    }

    public boolean isEmpty() {
        return this.values.length == 0;
    }

    private static Ingredient fromValues(Stream<? extends Ingredient.Value> streamExtendsIngredientValue0) {
        Ingredient $$1 = new Ingredient(streamExtendsIngredientValue0);
        return $$1.isEmpty() ? EMPTY : $$1;
    }

    public static Ingredient of() {
        return EMPTY;
    }

    public static Ingredient of(ItemLike... itemLike0) {
        return of(Arrays.stream(itemLike0).map(ItemStack::new));
    }

    public static Ingredient of(ItemStack... itemStack0) {
        return of(Arrays.stream(itemStack0));
    }

    public static Ingredient of(Stream<ItemStack> streamItemStack0) {
        return fromValues(streamItemStack0.filter(p_43944_ -> !p_43944_.isEmpty()).map(Ingredient.ItemValue::new));
    }

    public static Ingredient of(TagKey<Item> tagKeyItem0) {
        return fromValues(Stream.of(new Ingredient.TagValue(tagKeyItem0)));
    }

    public static Ingredient fromNetwork(FriendlyByteBuf friendlyByteBuf0) {
        return fromValues(friendlyByteBuf0.readList(FriendlyByteBuf::m_130267_).stream().map(Ingredient.ItemValue::new));
    }

    public static Ingredient fromJson(@Nullable JsonElement jsonElement0) {
        return fromJson(jsonElement0, true);
    }

    public static Ingredient fromJson(@Nullable JsonElement jsonElement0, boolean boolean1) {
        if (jsonElement0 == null || jsonElement0.isJsonNull()) {
            throw new JsonSyntaxException("Item cannot be null");
        } else if (jsonElement0.isJsonObject()) {
            return fromValues(Stream.of(valueFromJson(jsonElement0.getAsJsonObject())));
        } else if (jsonElement0.isJsonArray()) {
            JsonArray $$2 = jsonElement0.getAsJsonArray();
            if ($$2.size() == 0 && !boolean1) {
                throw new JsonSyntaxException("Item array cannot be empty, at least one item must be defined");
            } else {
                return fromValues(StreamSupport.stream($$2.spliterator(), false).map(p_289756_ -> valueFromJson(GsonHelper.convertToJsonObject(p_289756_, "item"))));
            }
        } else {
            throw new JsonSyntaxException("Expected item to be object or array of objects");
        }
    }

    private static Ingredient.Value valueFromJson(JsonObject jsonObject0) {
        if (jsonObject0.has("item") && jsonObject0.has("tag")) {
            throw new JsonParseException("An ingredient entry is either a tag or an item, not both");
        } else if (jsonObject0.has("item")) {
            Item $$1 = ShapedRecipe.itemFromJson(jsonObject0);
            return new Ingredient.ItemValue(new ItemStack($$1));
        } else if (jsonObject0.has("tag")) {
            ResourceLocation $$2 = new ResourceLocation(GsonHelper.getAsString(jsonObject0, "tag"));
            TagKey<Item> $$3 = TagKey.create(Registries.ITEM, $$2);
            return new Ingredient.TagValue($$3);
        } else {
            throw new JsonParseException("An ingredient entry needs either a tag or an item");
        }
    }

    static class ItemValue implements Ingredient.Value {

        private final ItemStack item;

        ItemValue(ItemStack itemStack0) {
            this.item = itemStack0;
        }

        @Override
        public Collection<ItemStack> getItems() {
            return Collections.singleton(this.item);
        }

        @Override
        public JsonObject serialize() {
            JsonObject $$0 = new JsonObject();
            $$0.addProperty("item", BuiltInRegistries.ITEM.getKey(this.item.getItem()).toString());
            return $$0;
        }
    }

    static class TagValue implements Ingredient.Value {

        private final TagKey<Item> tag;

        TagValue(TagKey<Item> tagKeyItem0) {
            this.tag = tagKeyItem0;
        }

        @Override
        public Collection<ItemStack> getItems() {
            List<ItemStack> $$0 = Lists.newArrayList();
            for (Holder<Item> $$1 : BuiltInRegistries.ITEM.m_206058_(this.tag)) {
                $$0.add(new ItemStack($$1));
            }
            return $$0;
        }

        @Override
        public JsonObject serialize() {
            JsonObject $$0 = new JsonObject();
            $$0.addProperty("tag", this.tag.location().toString());
            return $$0;
        }
    }

    interface Value {

        Collection<ItemStack> getItems();

        JsonObject serialize();
    }
}