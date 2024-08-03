package vazkii.patchouli.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;
import vazkii.patchouli.common.item.ItemModBook;

public final class ItemStackUtil {

    private static final Gson GSON = new GsonBuilder().create();

    private ItemStackUtil() {
    }

    public static Triple<ResourceLocation, Integer, CompoundTag> parseItemStackString(String res) {
        String nbt = "";
        int nbtStart = res.indexOf("{");
        if (nbtStart > 0) {
            nbt = res.substring(nbtStart).replaceAll("([^\\\\])'", "$1\"").replaceAll("\\\\'", "'");
            res = res.substring(0, nbtStart);
        }
        String[] upper = res.split("#");
        String count = "1";
        if (upper.length > 1) {
            res = upper[0];
            count = upper[1];
        }
        String[] tokens = res.split(":");
        if (tokens.length < 2) {
            throw new RuntimeException("Malformed item ID " + res);
        } else {
            ResourceLocation key = new ResourceLocation(tokens[0], tokens[1]);
            int countn = Integer.parseInt(count);
            CompoundTag tag = null;
            if (!nbt.isEmpty()) {
                try {
                    tag = TagParser.parseTag(nbt);
                } catch (CommandSyntaxException var10) {
                    throw new RuntimeException("Failed to parse ItemStack JSON", var10);
                }
            }
            return ImmutableTriple.of(key, countn, tag);
        }
    }

    public static ItemStack loadFromParsed(Triple<ResourceLocation, Integer, CompoundTag> parsed) {
        ResourceLocation key = (ResourceLocation) parsed.getLeft();
        Integer count = (Integer) parsed.getMiddle();
        CompoundTag nbt = (CompoundTag) parsed.getRight();
        Optional<Item> maybeItem = BuiltInRegistries.ITEM.m_6612_(key);
        if (maybeItem.isEmpty()) {
            throw new RuntimeException("Unknown item ID: " + key);
        } else {
            Item item = (Item) maybeItem.get();
            ItemStack stack = new ItemStack(item, count);
            if (nbt != null) {
                stack.setTag(nbt);
            }
            return stack;
        }
    }

    public static ItemStack loadStackFromString(String res) {
        return loadFromParsed(parseItemStackString(res));
    }

    public static Ingredient loadIngredientFromString(String ingredientString) {
        return Ingredient.of((ItemStack[]) loadStackListFromString(ingredientString).toArray(new ItemStack[0]));
    }

    public static List<ItemStack> loadStackListFromString(String ingredientString) {
        String[] stacksSerialized = splitStacksFromSerializedIngredient(ingredientString);
        List<ItemStack> stacks = new ArrayList();
        for (String s : stacksSerialized) {
            if (s.startsWith("tag:")) {
                TagKey<Item> key = TagKey.create(Registries.ITEM, new ResourceLocation(s.substring(4)));
                BuiltInRegistries.ITEM.m_203431_(key).ifPresent(tag -> tag.m_203614_().forEach(item -> stacks.add(new ItemStack(item))));
            } else {
                stacks.add(loadStackFromString(s));
            }
        }
        return stacks;
    }

    public static ItemStackUtil.StackWrapper wrapStack(ItemStack stack) {
        return stack.isEmpty() ? ItemStackUtil.StackWrapper.EMPTY_WRAPPER : new ItemStackUtil.StackWrapper(stack);
    }

    @Nullable
    public static Book getBookFromStack(ItemStack stack) {
        if (stack.getItem() instanceof ItemModBook) {
            return ItemModBook.getBook(stack);
        } else {
            for (Book b : BookRegistry.INSTANCE.books.values()) {
                if (ItemStack.isSameItem(b.getBookItem(), stack)) {
                    return b;
                }
            }
            return null;
        }
    }

    private static String[] splitStacksFromSerializedIngredient(String ingredientSerialized) {
        List<String> result = new ArrayList();
        int lastIndex = 0;
        int braces = 0;
        Character insideString = null;
        for (int i = 0; i < ingredientSerialized.length(); i++) {
            switch(ingredientSerialized.charAt(i)) {
                case '"':
                    insideString = insideString == null ? '"' : null;
                    break;
                case '\'':
                    insideString = insideString == null ? '\'' : null;
                    break;
                case ',':
                    if (braces <= 0) {
                        result.add(ingredientSerialized.substring(lastIndex, i));
                        lastIndex = i + 1;
                    }
                    break;
                case '{':
                    if (insideString == null) {
                        braces++;
                    }
                    break;
                case '}':
                    if (insideString == null) {
                        braces--;
                    }
            }
        }
        result.add(ingredientSerialized.substring(lastIndex));
        return (String[]) result.toArray(new String[0]);
    }

    public static ItemStack loadStackFromJson(JsonObject json) {
        String itemName = json.get("item").getAsString();
        Item item = (Item) BuiltInRegistries.ITEM.m_6612_(new ResourceLocation(itemName)).orElseThrow(() -> new IllegalArgumentException("Unknown item '" + itemName + "'"));
        ItemStack stack = new ItemStack(item, GsonHelper.getAsInt(json, "count", 1));
        if (json.has("nbt")) {
            try {
                JsonElement element = json.get("nbt");
                CompoundTag nbt;
                if (element.isJsonObject()) {
                    nbt = TagParser.parseTag(GSON.toJson(element));
                } else {
                    nbt = TagParser.parseTag(element.getAsString());
                }
                stack.setTag(nbt);
            } catch (CommandSyntaxException var6) {
                throw new IllegalArgumentException("Invalid NBT Entry: " + var6, var6);
            }
        }
        return stack;
    }

    public static class StackWrapper {

        public static final ItemStackUtil.StackWrapper EMPTY_WRAPPER = new ItemStackUtil.StackWrapper(ItemStack.EMPTY);

        public final ItemStack stack;

        public StackWrapper(ItemStack stack) {
            this.stack = stack;
        }

        public boolean equals(Object obj) {
            return obj == this || obj instanceof ItemStackUtil.StackWrapper && ItemStack.isSameItem(this.stack, ((ItemStackUtil.StackWrapper) obj).stack);
        }

        public int hashCode() {
            return this.stack.getItem().hashCode();
        }

        public String toString() {
            return "Wrapper[" + this.stack.toString() + "]";
        }
    }
}