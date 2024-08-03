package net.mehvahdjukaar.supplementaries.common.block.placeable_book;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.common.items.AntiqueInkItem;
import net.mehvahdjukaar.supplementaries.integration.CompatObjects;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class PlaceableBookManager {

    private static final Map<String, BookType> BY_NAME = new HashMap();

    private static final Multimap<Item, BookType> BY_ITEM = HashMultimap.create();

    public static void register(BookType type, @Nullable Item item) {
        if (item != null) {
            BY_ITEM.put(item, type);
            BY_NAME.put(type.name(), type);
        }
    }

    public static void registerDefault(DyeColor color) {
        register(new BookType(color), Items.BOOK);
    }

    public static void registerDefault(DyeColor color, int angle) {
        register(new BookType(color, (float) angle, false), Items.BOOK);
    }

    public static void registerDefault(String name, int color) {
        register(new BookType(name, color, false), Items.BOOK);
    }

    public static void setup() {
        registerDefault(DyeColor.BROWN, 1);
        registerDefault(DyeColor.WHITE, 1);
        registerDefault(DyeColor.BLACK, 1);
        registerDefault(DyeColor.LIGHT_GRAY);
        registerDefault(DyeColor.GRAY);
        registerDefault(DyeColor.ORANGE);
        registerDefault(DyeColor.YELLOW);
        registerDefault(DyeColor.LIME);
        registerDefault("green", 3129655);
        registerDefault("cyan", 1502399);
        registerDefault(DyeColor.LIGHT_BLUE);
        registerDefault(DyeColor.BLUE);
        registerDefault(DyeColor.PURPLE);
        registerDefault(DyeColor.MAGENTA);
        registerDefault(DyeColor.PINK);
        registerDefault(DyeColor.RED);
        register(new BookType("enchanted", 0, 1.0F, true), Items.ENCHANTED_BOOK);
        register(new BookType("and_quill", 0, 1.0F, false), Items.WRITABLE_BOOK);
        register(new BookType("written", 0, 1.0F, false), Items.WRITTEN_BOOK);
        register(new BookType("tattered", 0, 1.0F, false), null);
        register(new BookType("tome", 0, 1.0F, true), (Item) CompatObjects.TOME.get());
        register(new BookType("gene", 0, 1.0F, false), (Item) CompatObjects.GENE_BOOK.get());
    }

    public static BookType rand(Random r) {
        ArrayList<BookType> all = getAll();
        return (BookType) all.get(r.nextInt(all.size()));
    }

    public static ArrayList<BookType> getAll() {
        return new ArrayList(BY_ITEM.values());
    }

    public static BookType getByName(String name) {
        BookType b = (BookType) BY_NAME.get(name);
        return b == null ? (BookType) BY_NAME.get("brown") : b;
    }

    public static ArrayList<BookType> getByItem(ItemStack stack) {
        if (AntiqueInkItem.hasAntiqueInk(stack)) {
            return new ArrayList(List.of(getByName("tattered")));
        } else {
            Item item = stack.getItem();
            if (Utils.getID(item).getNamespace().equals("inspirations")) {
                String colName = Utils.getID(item).getPath().replace("_book", "");
                return new ArrayList(List.of(getByName(colName)));
            } else {
                return new ArrayList(BY_ITEM.get(item));
            }
        }
    }
}