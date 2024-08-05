package snownee.kiwi;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.util.MutableHashedLinkedMap;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import snownee.kiwi.item.ItemCategoryFiller;

public class GroupSetting {

    private final String[] groups;

    private final String[] after;

    private final List<ItemCategoryFiller> fillers = Lists.newArrayList();

    public static GroupSetting of(KiwiModule.Category category, GroupSetting preset) {
        if (preset != null) {
            if (category.value().length == 0 && category.after().length == 0) {
                return preset;
            }
            if (category.value().length == 0) {
                return new GroupSetting(preset.groups, category.after());
            }
        }
        return new GroupSetting(category.value(), category.after());
    }

    public GroupSetting(String[] groups, String[] after) {
        this.groups = groups;
        this.after = after;
    }

    public void apply(ItemCategoryFiller filler) {
        this.fillers.add(filler);
    }

    public void postApply() {
        List<ResourceKey<CreativeModeTab>> tabKeys = Stream.of(this.groups).map($ -> {
            CreativeModeTab tab = Kiwi.getGroup($);
            return tab != null ? (ResourceKey) BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(tab).orElse(null) : ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation($));
        }).filter(Objects::nonNull).toList();
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        for (ResourceKey<CreativeModeTab> tabKey : tabKeys) {
            eventBus.addListener(event -> {
                if (event.getTabKey().equals(tabKey)) {
                    List<Item> afterItems = Stream.of(this.after).map(ResourceLocation::m_135820_).filter(Objects::nonNull).map(BuiltInRegistries.ITEM::m_7745_).filter(Predicate.not(Items.AIR::equals)).toList();
                    List<ItemStack> items = Lists.newArrayList();
                    for (ItemCategoryFiller filler : this.fillers) {
                        CreativeModeTab tab = BuiltInRegistries.CREATIVE_MODE_TAB.get(tabKey);
                        filler.fillItemCategory(tab, event.getFlags(), event.hasPermissions(), items);
                    }
                    items = getEnabledStacks(items, event.getFlags());
                    addAfter(items, event.getEntries(), afterItems);
                }
            });
        }
    }

    private static void addAfter(List<ItemStack> toAdd, MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> map, Collection<Item> afterItems) {
        ItemStack lastFound = ItemStack.EMPTY;
        for (Item item : afterItems) {
            ItemStack stack = new ItemStack(item);
            if (map.contains(stack)) {
                lastFound = stack;
            }
        }
        ItemStack prev = ItemStack.EMPTY;
        for (int i = 0; i < toAdd.size(); i++) {
            ItemStack itemx = (ItemStack) toAdd.get(i);
            if (i == 0) {
                if (lastFound.isEmpty()) {
                    map.put(itemx, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                } else {
                    map.putAfter(lastFound, itemx, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                }
            } else {
                map.putAfter(prev, itemx, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
            prev = itemx;
        }
    }

    private static List<ItemStack> getEnabledStacks(List<ItemStack> newStacks, FeatureFlagSet enabledFeatures) {
        return newStacks.stream().allMatch($ -> isEnabled($, enabledFeatures)) ? newStacks : newStacks.stream().filter($ -> isEnabled($, enabledFeatures)).toList();
    }

    private static boolean isEnabled(ItemStack stack, FeatureFlagSet enabledFeatures) {
        return stack.getItem().m_245993_(enabledFeatures);
    }
}