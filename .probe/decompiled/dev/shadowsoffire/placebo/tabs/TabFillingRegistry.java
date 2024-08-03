package dev.shadowsoffire.placebo.tabs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import org.jetbrains.annotations.ApiStatus.Internal;

public class TabFillingRegistry {

    private static final Map<ResourceKey<CreativeModeTab>, List<ITabFiller>> FILLERS = new IdentityHashMap();

    @SafeVarargs
    public static void register(ITabFiller filler, ResourceKey<CreativeModeTab>... tabs) {
        for (ResourceKey<CreativeModeTab> tab : tabs) {
            registerInternal(tab, filler);
        }
    }

    public static void register(ResourceKey<CreativeModeTab> tab, ITabFiller... fillers) {
        for (ITabFiller filler : fillers) {
            registerInternal(tab, filler);
        }
    }

    @SafeVarargs
    public static void registerSimple(ItemLike item, ResourceKey<CreativeModeTab>... tabs) {
        for (ResourceKey<CreativeModeTab> tab : tabs) {
            registerInternal(tab, ITabFiller.simple(item));
        }
    }

    public static void registerSimple(ResourceKey<CreativeModeTab> tab, ItemLike... items) {
        for (ItemLike item : items) {
            registerInternal(tab, ITabFiller.simple(item));
        }
    }

    @SafeVarargs
    public static void register(Supplier<? extends ItemLike> item, ResourceKey<CreativeModeTab>... tabs) {
        for (ResourceKey<CreativeModeTab> tab : tabs) {
            registerInternal(tab, ITabFiller.delegating(item));
        }
    }

    @SafeVarargs
    public static void register(ResourceKey<CreativeModeTab> tab, Supplier<? extends ItemLike>... items) {
        for (Supplier<? extends ItemLike> item : items) {
            registerInternal(tab, ITabFiller.delegating(item));
        }
    }

    @Internal
    public static void fillTabs(BuildCreativeModeTabContentsEvent e) {
        ((List) FILLERS.getOrDefault(e.getTabKey(), Collections.emptyList())).forEach(f -> f.fillItemCategory(e.getTab(), e));
    }

    private static void registerInternal(ResourceKey<CreativeModeTab> tab, ITabFiller filler) {
        ((List) FILLERS.computeIfAbsent(tab, k -> new ArrayList())).add(filler);
    }
}