package snownee.kiwi.customization.block.family;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableListMultimap.Builder;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import snownee.kiwi.Kiwi;
import snownee.kiwi.customization.CustomizationHooks;
import snownee.kiwi.util.KHolder;
import snownee.kiwi.util.resource.OneTimeLoader;

public class BlockFamilies {

    private static ImmutableListMultimap<Item, KHolder<BlockFamily>> byItem = ImmutableListMultimap.of();

    private static ImmutableList<KHolder<BlockFamily>> fromResources = ImmutableList.of();

    private static ImmutableMap<ResourceLocation, KHolder<BlockFamily>> byId = ImmutableMap.of();

    private static ImmutableListMultimap<Item, KHolder<BlockFamily>> byStonecutterSource = ImmutableListMultimap.of();

    public static Collection<KHolder<BlockFamily>> find(Item item) {
        return (Collection<KHolder<BlockFamily>>) (item == Items.AIR ? List.of() : byItem.get(item));
    }

    public static List<KHolder<BlockFamily>> findQuickSwitch(Item item, boolean creative) {
        Stream<KHolder<BlockFamily>> stream = find(item).stream();
        if (creative) {
            stream = stream.filter(f -> ((BlockFamily) f.value()).switchAttrs().enabled());
        } else {
            stream = stream.filter(f -> ((BlockFamily) f.value()).switchAttrs().enabled() && !((BlockFamily) f.value()).switchAttrs().creativeOnly());
        }
        return stream.toList();
    }

    public static Collection<KHolder<BlockFamily>> findByStonecutterSource(Item item) {
        return byStonecutterSource.get(item);
    }

    public static void reloadResources(ResourceManager resourceManager, OneTimeLoader.Context context) {
        Map<ResourceLocation, BlockFamily> families = OneTimeLoader.load(resourceManager, "kiwi/family", BlockFamily.CODEC, context);
        fromResources = (ImmutableList<KHolder<BlockFamily>>) families.entrySet().stream().map(e -> new KHolder<>((ResourceLocation) e.getKey(), (BlockFamily) e.getValue())).collect(ImmutableList.toImmutableList());
    }

    public static int reloadTags() {
        reloadComplete(List.of());
        if (CustomizationHooks.kswitch) {
            reloadComplete(new BlockFamilyInferrer().generate());
        }
        return byId.size();
    }

    private static void reloadComplete(Collection<KHolder<BlockFamily>> additional) {
        Map<ResourceLocation, KHolder<BlockFamily>> byIdBuilder = Maps.newHashMapWithExpectedSize(fromResources.size() + additional.size());
        Builder<Item, KHolder<BlockFamily>> byItemBuilder = ImmutableListMultimap.builder();
        Builder<Item, KHolder<BlockFamily>> byStonecutterBuilder = ImmutableListMultimap.builder();
        for (KHolder<BlockFamily> family : Iterables.concat(fromResources, additional)) {
            KHolder<BlockFamily> old = (KHolder<BlockFamily>) byIdBuilder.putIfAbsent(family.key(), family);
            if (old != null) {
                Kiwi.LOGGER.error("Duplicate family {}", family);
            } else {
                for (Holder.Reference<Item> item : family.value().itemHolders()) {
                    byItemBuilder.put(item.value(), family);
                }
                Optional<Holder.Reference<Item>> stonecutterSource = family.value().stonecutterSource();
                if (stonecutterSource.isPresent()) {
                    byStonecutterBuilder.put((Item) ((Holder.Reference) stonecutterSource.get()).value(), family);
                }
            }
        }
        byId = ImmutableMap.copyOf(byIdBuilder);
        byItem = byItemBuilder.build();
        byStonecutterSource = byStonecutterBuilder.build();
        StonecutterRecipeMaker.invalidateCache();
    }

    public static BlockFamily get(ResourceLocation id) {
        KHolder<BlockFamily> holder = (KHolder<BlockFamily>) byId.get(id);
        return holder == null ? null : holder.value();
    }

    public static Collection<KHolder<BlockFamily>> all() {
        return byId.values();
    }

    public static float getConvertRatio(Item item) {
        Block block = Block.byItem(item);
        if (block == Blocks.AIR) {
            return 1.0F;
        } else {
            Holder<Block> holder = BuiltInRegistries.BLOCK.m_263177_(block);
            if (holder.is(BlockTags.SLABS)) {
                return 0.5F;
            } else if (holder.is(BlockTags.DOORS)) {
                return 2.0F;
            } else if (holder.is(BlockTags.TRAPDOORS)) {
                return 3.0F;
            } else if (holder.is(BlockTags.FENCE_GATES)) {
                return 4.0F;
            } else {
                return holder.is(BlockTags.PRESSURE_PLATES) ? 2.0F : 1.0F;
            }
        }
    }
}