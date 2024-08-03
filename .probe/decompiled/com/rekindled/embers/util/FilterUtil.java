package com.rekindled.embers.util;

import com.rekindled.embers.api.filter.ComparatorMatch;
import com.rekindled.embers.api.filter.EnumFilterSetting;
import com.rekindled.embers.api.filter.FilterAny;
import com.rekindled.embers.api.filter.FilterExisting;
import com.rekindled.embers.api.filter.FilterNotExisting;
import com.rekindled.embers.api.filter.IFilter;
import com.rekindled.embers.api.filter.IFilterComparator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class FilterUtil {

    static List<IFilterComparator> comparators = new ArrayList();

    static Map<String, IFilterComparator> comparatorMap = new HashMap();

    static Map<ResourceLocation, Function<CompoundTag, IFilter>> filterRegistry = new LinkedHashMap();

    public static IFilterComparator ANY = new ComparatorMatch("any", 999999) {

        @Override
        public boolean match(ItemStack stack1, ItemStack stack2) {
            return true;
        }

        @Override
        public String format(ItemStack stack1, ItemStack stack2, EnumFilterSetting setting, boolean inverted) {
            return I18n.get("embers.filter.any");
        }
    };

    public static IFilter FILTER_ANY = new FilterAny();

    public static IFilter FILTER_EXISTING = new FilterExisting();

    public static IFilter FILTER_NOT_EXISTING = new FilterNotExisting();

    public static void registerComparator(IFilterComparator comparator) {
        comparatorMap.put(comparator.getName(), comparator);
        comparators.add(comparator);
        comparators.sort(Comparator.comparingInt(IFilterComparator::getPriority).reversed());
    }

    public static void registerFilter(ResourceLocation resLoc, Function<CompoundTag, IFilter> generator) {
        filterRegistry.put(resLoc, generator);
    }

    public static void registerFilter(IFilter filter) {
        registerFilter(filter.getType(), tag -> filter);
    }

    public static IFilter deserializeFilter(CompoundTag tag) {
        ResourceLocation resLoc = new ResourceLocation(tag.getString("type"));
        Function<CompoundTag, IFilter> generator = (Function<CompoundTag, IFilter>) filterRegistry.get(resLoc);
        return generator != null ? (IFilter) generator.apply(tag) : FILTER_ANY;
    }

    public static List<IFilterComparator> getComparators(ItemStack stack1, ItemStack stack2) {
        ArrayList<IFilterComparator> matched = new ArrayList();
        for (IFilterComparator comparator : comparators) {
            if (comparator.match(stack1, stack2)) {
                matched.add(comparator);
            }
        }
        if (matched.isEmpty()) {
            matched.add(ANY);
        }
        return matched;
    }

    public static IFilterComparator getComparator(String name) {
        return (IFilterComparator) comparatorMap.get(name);
    }
}