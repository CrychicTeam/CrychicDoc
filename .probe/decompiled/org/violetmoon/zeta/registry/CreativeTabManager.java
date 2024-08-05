package org.violetmoon.zeta.registry;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.util.MutableHashedLinkedMap;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import org.violetmoon.zeta.Zeta;
import org.violetmoon.zeta.config.ZetaGeneralConfig;
import org.violetmoon.zeta.module.IDisableable;

public class CreativeTabManager {

    private static final Object MUTEX = new Object();

    private static final Map<ItemLike, Item> itemLikeCache = new HashMap();

    private static final Map<ResourceKey<CreativeModeTab>, CreativeTabManager.CreativeTabAdditions> additions = new HashMap();

    private static final Multimap<ItemLike, ResourceKey<CreativeModeTab>> mappedItems = HashMultimap.create();

    private static boolean daisyChainMode = false;

    private static CreativeTabManager.ItemSet daisyChainedSet = null;

    public static void daisyChain() {
        daisyChainMode = true;
        daisyChainedSet = null;
    }

    public static void endDaisyChain() {
        daisyChainMode = false;
        daisyChainedSet = null;
    }

    public static void addToCreativeTab(ResourceKey<CreativeModeTab> tab, ItemLike item) {
        if (daisyChainMode) {
            if (daisyChainedSet == null) {
                throw new IllegalArgumentException("Must start daisy chain with addToCreativeTabNextTo");
            }
            addToDaisyChain(item);
        } else {
            getForTab(tab).appendToEnd.add(item);
        }
        mappedItems.put(item, tab);
    }

    public static void addToCreativeTabNextTo(ResourceKey<CreativeModeTab> tab, ItemLike item, ItemLike target, boolean behind) {
        tab = guessTab(target, tab);
        CreativeTabManager.CreativeTabAdditions additions = getForTab(tab);
        Map<CreativeTabManager.ItemSet, ItemLike> map = behind ? additions.appendBehind : additions.appendInFront;
        CreativeTabManager.ItemSet toAdd = null;
        if (daisyChainMode) {
            boolean newSet = daisyChainedSet == null;
            CreativeTabManager.ItemSet set = addToDaisyChain(item);
            if (newSet) {
                toAdd = set;
            }
        } else {
            toAdd = new CreativeTabManager.ItemSet(item);
        }
        if (toAdd != null) {
            map.put(toAdd, target);
        }
        mappedItems.put(item, tab);
    }

    private static CreativeTabManager.ItemSet addToDaisyChain(ItemLike item) {
        if (daisyChainMode && daisyChainedSet != null) {
            daisyChainedSet.items.add(item);
            return daisyChainedSet;
        } else {
            CreativeTabManager.ItemSet set = new CreativeTabManager.ItemSet(item);
            if (daisyChainMode) {
                daisyChainedSet = set;
            }
            return set;
        }
    }

    private static ResourceKey<CreativeModeTab> guessTab(ItemLike parent, ResourceKey<CreativeModeTab> tab) {
        if (parent != null && mappedItems.containsKey(parent)) {
            tab = (ResourceKey<CreativeModeTab>) mappedItems.get(parent).iterator().next();
        }
        return tab;
    }

    private static CreativeTabManager.CreativeTabAdditions getForTab(ResourceKey<CreativeModeTab> tab) {
        return (CreativeTabManager.CreativeTabAdditions) additions.computeIfAbsent(tab, tabRk -> new CreativeTabManager.CreativeTabAdditions());
    }

    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        synchronized (MUTEX) {
            ResourceKey<CreativeModeTab> tabKey = event.getTabKey();
            MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> entries = event.getEntries();
            if (additions.containsKey(tabKey)) {
                CreativeTabManager.CreativeTabAdditions add = (CreativeTabManager.CreativeTabAdditions) additions.get(tabKey);
                for (ItemLike item : add.appendToEnd) {
                    acceptItem(event, item);
                }
                if (!ZetaGeneralConfig.forceCreativeTabAppends) {
                    Map<CreativeTabManager.ItemSet, ItemLike> front = new LinkedHashMap(add.appendInFront);
                    Map<CreativeTabManager.ItemSet, ItemLike> behind = new LinkedHashMap(add.appendBehind);
                    int failsafe = 100;
                    int printThreshold = 90;
                    int misses = 0;
                    boolean failsafing = false;
                    do {
                        boolean missed = false;
                        logVerbose(() -> "front empty=" + front.isEmpty() + " / behind empty=" + behind.isEmpty());
                        if (entries.isEmpty()) {
                            Zeta.GLOBAL_LOG.error("entries map for tab " + tabKey + " is empty, this should never happen");
                            return;
                        }
                        if (!front.isEmpty()) {
                            missed = appendNextTo(tabKey, entries, front, false, failsafing);
                        }
                        if (!behind.isEmpty()) {
                            missed |= appendNextTo(tabKey, entries, behind, true, failsafing);
                        }
                        if (missed) {
                            int fMisses = misses;
                            logVerbose(() -> "Missed " + fMisses + "times out of 100");
                            misses++;
                        }
                        if (misses > 100) {
                            logVerbose(() -> {
                                StringBuilder sb = new StringBuilder();
                                for (Entry<ItemStack, CreativeModeTab.TabVisibility> entry : entries) {
                                    sb.append(entry.getKey());
                                    sb.append("; ");
                                }
                                return sb.toString();
                            });
                            new RuntimeException("Creative tab placement misses exceeded failsafe, aborting logic").printStackTrace();
                            return;
                        }
                        if (misses > 90) {
                            failsafing = true;
                        }
                    } while (!front.isEmpty() || !behind.isEmpty());
                } else {
                    for (CreativeTabManager.ItemSet itemset : add.appendInFront.keySet()) {
                        for (ItemLike item : itemset.items) {
                            acceptItem(event, item);
                        }
                    }
                    for (CreativeTabManager.ItemSet itemset : add.appendBehind.keySet()) {
                        for (ItemLike item : itemset.items) {
                            acceptItem(event, item);
                        }
                    }
                }
            }
        }
    }

    private static boolean isItemEnabled(ItemLike item) {
        return item instanceof IDisableable<?> id ? id.isEnabled() : true;
    }

    private static void acceptItem(BuildCreativeModeTabContentsEvent event, ItemLike item) {
        if (isItemEnabled(item)) {
            if (item instanceof CreativeTabManager.AppendsUniquely au) {
                event.m_246601_(au.appendItemsToCreativeTab());
            } else {
                event.m_246326_(item);
            }
        }
    }

    private static void addToEntries(ItemStack target, MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> entries, ItemLike item, boolean behind) {
        logVerbose(() -> "adding target=" + target + " next to " + item + " with behind=" + behind);
        if (isItemEnabled(item)) {
            List<ItemStack> stacksToAdd = Arrays.asList(new ItemStack(item));
            if (item instanceof CreativeTabManager.AppendsUniquely au) {
                stacksToAdd = au.appendItemsToCreativeTab();
            }
            if (!behind) {
                Collections.reverse(stacksToAdd);
            }
            for (ItemStack addStack : stacksToAdd) {
                if (behind) {
                    entries.putBefore(target, addStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                } else {
                    entries.putAfter(target, addStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                }
            }
        }
    }

    private static boolean appendNextTo(ResourceKey<CreativeModeTab> tabKey, MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> entries, Map<CreativeTabManager.ItemSet, ItemLike> map, boolean behind, boolean log) {
        logVerbose(() -> "appendNextTo " + tabKey + " / behind=" + behind);
        Collection<CreativeTabManager.ItemSet> coll = map.keySet();
        if (coll.isEmpty()) {
            throw new RuntimeException("Tab collection is empty, this should never happen.");
        } else {
            CreativeTabManager.ItemSet firstSet = (CreativeTabManager.ItemSet) coll.iterator().next();
            ItemLike firstSetItem = (ItemLike) firstSet.items.get(0);
            ItemLike target = (ItemLike) map.get(firstSet);
            logVerbose(() -> "target is " + target);
            if (log) {
                Zeta.GLOBAL_LOG.error("Creative tab loop found when adding {} next to {}", firstSetItem, target);
                Zeta.GLOBAL_LOG.error("For more info enable Creative Verbose Logging in the Zeta config, or set Force Creative Tab Appends to true to disable this behavior");
            }
            map.remove(firstSet);
            if (isItemEnabled(firstSetItem) && target != null) {
                if (!itemLikeCache.containsKey(target)) {
                    itemLikeCache.put(target, target.asItem());
                }
                Item targetItem = (Item) itemLikeCache.get(target);
                for (Entry<ItemStack, CreativeModeTab.TabVisibility> entry : entries) {
                    ItemStack stack = (ItemStack) entry.getKey();
                    Item item = stack.getItem();
                    logVerbose(() -> "Comparing item " + item + " to our target " + targetItem);
                    if (item == targetItem) {
                        logVerbose(() -> "Matched! Adding successfully");
                        for (int i = 0; i < firstSet.items.size(); i++) {
                            int j = i;
                            if (!behind) {
                                j = firstSet.items.size() - 1 - i;
                            }
                            addToEntries(stack, entries, (ItemLike) firstSet.items.get(j), behind);
                        }
                        return false;
                    }
                }
                map.put(firstSet, target);
                return true;
            } else {
                logVerbose(() -> "hit early false return");
                return false;
            }
        }
    }

    private static void logVerbose(Supplier<String> s) {
        if (ZetaGeneralConfig.enableCreativeVerboseLogging) {
            Zeta.GLOBAL_LOG.warn((String) s.get());
        }
    }

    public interface AppendsUniquely extends ItemLike {

        List<ItemStack> appendItemsToCreativeTab();
    }

    private static class CreativeTabAdditions {

        private List<ItemLike> appendToEnd = new ArrayList();

        private Map<CreativeTabManager.ItemSet, ItemLike> appendInFront = new LinkedHashMap();

        private Map<CreativeTabManager.ItemSet, ItemLike> appendBehind = new LinkedHashMap();
    }

    private static class ItemSet {

        List<ItemLike> items = new ArrayList();

        public ItemSet(ItemLike item) {
            this.items.add(item);
        }
    }
}