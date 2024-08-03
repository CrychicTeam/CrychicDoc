package net.mehvahdjukaar.moonlight.api.set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Stream;
import net.mehvahdjukaar.moonlight.core.set.BlocksColorInternal;
import net.minecraft.Util;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class BlocksColorAPI {

    public static final List<DyeColor> SORTED_COLORS = Util.make(() -> {
        Builder<DyeColor> b = ImmutableList.builder();
        List<DyeColor> l = List.of(DyeColor.WHITE, DyeColor.LIGHT_GRAY, DyeColor.GRAY, DyeColor.BLACK, DyeColor.BROWN, DyeColor.RED, DyeColor.ORANGE, DyeColor.YELLOW, DyeColor.LIME, DyeColor.GREEN, DyeColor.CYAN, DyeColor.LIGHT_BLUE, DyeColor.BLUE, DyeColor.PURPLE, DyeColor.MAGENTA, DyeColor.PINK);
        b.addAll(l);
        for (DyeColor v : DyeColor.values()) {
            if (!l.contains(v)) {
                b.add(v);
            }
        }
        return b.build();
    });

    @Nullable
    public static DyeColor getColor(Block block) {
        return BlocksColorInternal.getColor(block);
    }

    @Nullable
    public static DyeColor getColor(Item item) {
        return BlocksColorInternal.getColor(item);
    }

    @Nullable
    public static Item getColoredItem(String key, @Nullable DyeColor color) {
        return BlocksColorInternal.getColoredItem(key, color);
    }

    @Nullable
    public static Block getColoredBlock(String key, @Nullable DyeColor color) {
        return BlocksColorInternal.getColoredBlock(key, color);
    }

    public static boolean isDefaultColor(Block block) {
        String key = getKey(block);
        return key == null ? true : getColoredBlock(key, null) == block;
    }

    public static boolean isDefaultColor(Item item) {
        String key = getKey(item);
        return key == null ? true : getColoredItem(key, null) == item;
    }

    @Nullable
    public static Block changeColor(Block old, @Nullable DyeColor newColor) {
        return BlocksColorInternal.changeColor(old, newColor);
    }

    @Nullable
    public static Item changeColor(Item old, @Nullable DyeColor newColor) {
        return BlocksColorInternal.changeColor(old, newColor);
    }

    @Nullable
    public static String getKey(Block block) {
        return BlocksColorInternal.getKey(block);
    }

    @Nullable
    public static String getKey(Item item) {
        return BlocksColorInternal.getKey(item);
    }

    public static Set<String> getBlockKeys() {
        return BlocksColorInternal.getBlockKeys();
    }

    public static Set<String> getItemKeys() {
        return BlocksColorInternal.getItemKeys();
    }

    @Nullable
    public static HolderSet<Block> getBlockHolderSet(String key) {
        return BlocksColorInternal.getBlockHolderSet(key);
    }

    @Nullable
    public static HolderSet<Item> getItemHolderSet(String key) {
        return BlocksColorInternal.getItemHolderSet(key);
    }

    public static void registerBlockColorSet(ResourceLocation key, EnumMap<DyeColor, Block> blocks, @Nullable Block defaultBlock) {
        BlocksColorInternal.registerBlockColorSet(key, blocks, defaultBlock);
    }

    public static void registerItemColorSet(ResourceLocation key, EnumMap<DyeColor, Item> items, @Nullable Item defaultItem) {
        BlocksColorInternal.registerItemColorSet(key, items, defaultItem);
    }

    public static <T> Stream<T> ordered(Map<DyeColor, T> map) {
        return map.entrySet().stream().sorted(Comparator.comparing(entry -> SORTED_COLORS.indexOf(entry.getKey()))).map(Entry::getValue);
    }
}