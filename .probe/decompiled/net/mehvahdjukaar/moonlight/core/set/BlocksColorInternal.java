package net.mehvahdjukaar.moonlight.core.set;

import com.google.common.base.Stopwatch;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import net.mehvahdjukaar.moonlight.api.MoonlightRegistry;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class BlocksColorInternal {

    public static final List<DyeColor> VANILLA_COLORS = List.of(DyeColor.WHITE, DyeColor.ORANGE, DyeColor.MAGENTA, DyeColor.LIGHT_BLUE, DyeColor.YELLOW, DyeColor.LIME, DyeColor.PINK, DyeColor.GRAY, DyeColor.LIGHT_GRAY, DyeColor.CYAN, DyeColor.PURPLE, DyeColor.BLUE, DyeColor.BROWN, DyeColor.GREEN, DyeColor.RED, DyeColor.BLACK);

    public static final List<DyeColor> MODDED_COLORS = List.of((DyeColor[]) Arrays.stream(DyeColor.values()).filter(v -> !VANILLA_COLORS.contains(v)).toArray(DyeColor[]::new));

    private static final Map<String, BlocksColorInternal.ColoredSet<Block>> BLOCK_COLOR_SETS = new HashMap();

    private static final Map<String, BlocksColorInternal.ColoredSet<Item>> ITEM_COLOR_SETS = new HashMap();

    private static final Object2ObjectOpenHashMap<Object, DyeColor> OBJ_TO_COLORS = new Object2ObjectOpenHashMap();

    private static final Object2ObjectOpenHashMap<Object, String> OBJ_TO_TYPE = new Object2ObjectOpenHashMap();

    public static void setup() {
        Stopwatch sw = Stopwatch.createStarted();
        Map<String, DyeColor> colors = new HashMap();
        VANILLA_COLORS.forEach(d -> colors.put(d.getName(), d));
        List<String> colorPriority = new ArrayList(colors.keySet().stream().toList());
        addColoredFromRegistry(colors, colorPriority, BuiltInRegistries.BLOCK, BLOCK_COLOR_SETS);
        addColoredFromRegistry(colors, colorPriority, BuiltInRegistries.ITEM, ITEM_COLOR_SETS);
        Moonlight.LOGGER.info("Initialized color sets in {}ms", sw.elapsed().toMillis());
    }

    public static void registerBlockColorSet(ResourceLocation key, EnumMap<DyeColor, Block> blocks, @Nullable Block defaultBlock) {
        BLOCK_COLOR_SETS.put(key.toString(), new BlocksColorInternal.ColoredSet<>(key, blocks, BuiltInRegistries.BLOCK, defaultBlock));
    }

    public static void registerItemColorSet(ResourceLocation key, EnumMap<DyeColor, Item> items, @Nullable Item defaultItem) {
        ITEM_COLOR_SETS.put(key.toString(), new BlocksColorInternal.ColoredSet<>(key, items, BuiltInRegistries.ITEM, defaultItem));
    }

    private static <T> void addColoredFromRegistry(Map<String, DyeColor> colors, List<String> colorPriority, Registry<T> registry, Map<String, BlocksColorInternal.ColoredSet<T>> colorSetMap) {
        Map<ResourceLocation, EnumMap<DyeColor, T>> groupedByType = new HashMap();
        colorPriority.sort(Comparator.comparingInt(String::length));
        Collections.reverse(colorPriority);
        for (Entry<ResourceKey<T>, T> e : registry.entrySet()) {
            ResourceLocation id = ((ResourceKey) e.getKey()).location();
            String name = id.getPath();
            if (name.contains("_")) {
                for (String c : colorPriority) {
                    ResourceLocation newId = null;
                    if (name.startsWith(c + "_")) {
                        newId = new ResourceLocation(id.getNamespace(), name.substring((c + "_").length()));
                    }
                    if (name.endsWith("_" + c)) {
                        newId = new ResourceLocation(id.getNamespace(), name.substring(0, name.length() - ("_" + c).length()));
                    }
                    if (newId != null) {
                        DyeColor dyeColor = (DyeColor) colors.get(c);
                        ((EnumMap) groupedByType.computeIfAbsent(newId, a -> new EnumMap(DyeColor.class))).put(dyeColor, e.getValue());
                        break;
                    }
                }
            }
        }
        for (Entry<ResourceLocation, EnumMap<DyeColor, T>> j : groupedByType.entrySet()) {
            EnumMap<DyeColor, T> map = (EnumMap<DyeColor, T>) j.getValue();
            ResourceLocation id = (ResourceLocation) j.getKey();
            if (!isBlacklisted(id) && map.keySet().containsAll(VANILLA_COLORS)) {
                BlocksColorInternal.ColoredSet<T> set = new BlocksColorInternal.ColoredSet<>(id, map, registry);
                colorSetMap.put(id.toString(), set);
                for (Entry<DyeColor, T> v : set.colorsToObj.entrySet()) {
                    OBJ_TO_COLORS.put(v.getValue(), (DyeColor) v.getKey());
                    OBJ_TO_TYPE.put(v.getValue(), id.toString());
                }
                OBJ_TO_TYPE.put(set.defaultObj, id.toString());
            }
        }
    }

    private static boolean isBlacklisted(ResourceLocation id) {
        String modId = id.getNamespace();
        return modId.equals("energeticsheep") || modId.equals("xycraft_world") || modId.equals("botania") || modId.equals("spectrum");
    }

    @Nullable
    public static DyeColor getColor(Block block) {
        return (DyeColor) OBJ_TO_COLORS.get(block);
    }

    @Nullable
    public static DyeColor getColor(Item item) {
        return (DyeColor) OBJ_TO_COLORS.get(item);
    }

    @Nullable
    public static Item getColoredItem(String key, @Nullable DyeColor color) {
        BlocksColorInternal.ColoredSet<Item> set = getItemSet(key);
        return set != null ? set.with(color) : null;
    }

    @Nullable
    public static Block getColoredBlock(String key, @Nullable DyeColor color) {
        BlocksColorInternal.ColoredSet<Block> set = getBlockSet(key);
        return set != null ? set.with(color) : null;
    }

    public static Set<String> getBlockKeys() {
        return BLOCK_COLOR_SETS.keySet();
    }

    public static Set<String> getItemKeys() {
        return ITEM_COLOR_SETS.keySet();
    }

    @Nullable
    public static Block changeColor(Block old, @Nullable DyeColor newColor) {
        if (old.builtInRegistryHolder().is(MoonlightRegistry.NON_RECOLORABLE_BLOCKS_TAG)) {
            return null;
        } else {
            String key = getKey(old);
            if (key != null) {
                BlocksColorInternal.ColoredSet<Block> set = getBlockSet(key);
                if (set != null) {
                    Block b = set.with(newColor);
                    if (b != old) {
                        return b;
                    }
                }
            }
            return null;
        }
    }

    @Nullable
    public static Item changeColor(Item old, @Nullable DyeColor newColor) {
        if (old.builtInRegistryHolder().is(MoonlightRegistry.NON_RECOLORABLE_ITEMS_TAG)) {
            return null;
        } else {
            String key = getKey(old);
            if (key != null) {
                BlocksColorInternal.ColoredSet<Item> set = getItemSet(key);
                if (set != null) {
                    Item i = set.with(newColor);
                    if (i != old) {
                        return i;
                    }
                }
            }
            return null;
        }
    }

    @Nullable
    public static String getKey(Block block) {
        return (String) OBJ_TO_TYPE.get(block);
    }

    @Nullable
    public static String getKey(Item item) {
        return (String) OBJ_TO_TYPE.get(item);
    }

    @Nullable
    private static BlocksColorInternal.ColoredSet<Block> getBlockSet(String key) {
        key = new ResourceLocation(key).toString();
        return (BlocksColorInternal.ColoredSet<Block>) BLOCK_COLOR_SETS.get(key);
    }

    @Nullable
    private static BlocksColorInternal.ColoredSet<Item> getItemSet(String key) {
        key = new ResourceLocation(key).toString();
        return (BlocksColorInternal.ColoredSet<Item>) ITEM_COLOR_SETS.get(key);
    }

    @Nullable
    public static HolderSet<Block> getBlockHolderSet(String key) {
        BlocksColorInternal.ColoredSet<Block> set = getBlockSet(key);
        return set != null ? set.makeHolderSet(BuiltInRegistries.BLOCK) : null;
    }

    @Nullable
    public static HolderSet<Item> getItemHolderSet(String key) {
        BlocksColorInternal.ColoredSet<Item> set = getItemSet(key);
        return set != null ? set.makeHolderSet(BuiltInRegistries.ITEM) : null;
    }

    private static class ColoredSet<T> {

        private final ResourceLocation id;

        private final Map<DyeColor, T> colorsToObj;

        private final T defaultObj;

        private ColoredSet(ResourceLocation id, EnumMap<DyeColor, T> map, Registry<T> registry) {
            this(id, map, registry, null);
        }

        private ColoredSet(ResourceLocation id, EnumMap<DyeColor, T> map, Registry<T> registry, @Nullable T defBlock) {
            this.colorsToObj = map;
            this.id = id;
            List<String> newColorMods = List.of("tinted", "dye_depot", "dyenamics");
            label37: for (DyeColor c : BlocksColorInternal.MODDED_COLORS) {
                String namespace = id.getNamespace();
                String path = id.getPath();
                for (String mod : newColorMods) {
                    for (String s : new String[] { namespace + ":" + path + "_%s", namespace + ":%s_" + path, mod + ":" + path + "_%s", mod + ":%s_" + path }) {
                        Optional<T> o = registry.getOptional(new ResourceLocation(String.format(s, c.getName())));
                        if (o.isPresent()) {
                            this.colorsToObj.put(c, o.get());
                            continue label37;
                        }
                    }
                }
            }
            this.defaultObj = defBlock == null ? this.computeDefault(id, registry) : defBlock;
        }

        private T computeDefault(ResourceLocation id, Registry<T> registry) {
            if (id.getNamespace().equals("minecraft") && id.getPath().contains("stained_glass")) {
                id = new ResourceLocation(id.getPath().replace("stained_", ""));
            } else if (id.getNamespace().equals("quark")) {
                if (id.getPath().equals("rune")) {
                    id = new ResourceLocation("quark", "blank_rune");
                } else if (id.getPath().equals("shard")) {
                    id = new ResourceLocation("quark", "clear_shard");
                }
            } else if (id.equals(new ResourceLocation("suppsquared:sack"))) {
                id = new ResourceLocation("supplementaries:sack");
            }
            Optional<T> o = registry.getOptional(id);
            return (T) (o.isEmpty() ? registry.getOptional(new ResourceLocation(id.getPath())).orElseGet(() -> this.colorsToObj.get(DyeColor.WHITE)) : o.get());
        }

        private HolderSet<T> makeHolderSet(Registry<T> registry) {
            Optional<HolderSet.Named<T>> v = registry.getTag(TagKey.create(registry.key(), new ResourceLocation(this.id.getNamespace(), this.id.getPath() + "s")));
            if (v.isEmpty()) {
                v = registry.getTag(TagKey.create(registry.key(), new ResourceLocation(PlatHelper.getPlatform().isForge() ? "forge" : "c", this.id.getPath() + "s")));
            }
            if (v.isPresent()) {
                HolderSet.Named<T> tag = (HolderSet.Named<T>) v.get();
                boolean success = true;
                for (T t : this.colorsToObj.values()) {
                    if (!tag.contains(registry.getHolderOrThrow((ResourceKey<T>) registry.getResourceKey(t).get()))) {
                        success = false;
                        break;
                    }
                }
                if (success) {
                    return tag;
                }
            }
            return HolderSet.direct(tx -> registry.getHolderOrThrow((ResourceKey<T>) registry.getResourceKey((T) tx).get()), new ArrayList(this.colorsToObj.values()));
        }

        @Nullable
        private T with(@Nullable DyeColor newColor) {
            return (T) (newColor != null && !this.colorsToObj.containsKey(newColor) ? null : this.colorsToObj.getOrDefault(newColor, this.defaultObj));
        }
    }
}