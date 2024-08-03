package com.almostreliable.lootjs.kube;

import com.almostreliable.lootjs.LootJSPlatform;
import com.almostreliable.lootjs.LootModificationsAPI;
import com.almostreliable.lootjs.core.LootContextType;
import com.almostreliable.lootjs.core.LootEntry;
import com.almostreliable.lootjs.filters.ItemFilter;
import com.almostreliable.lootjs.filters.Resolver;
import com.almostreliable.lootjs.filters.ResourceLocationFilter;
import com.almostreliable.lootjs.kube.wrapper.IntervalJS;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.saveddata.maps.MapDecoration;

public class LootJSPlugin extends KubeJSPlugin {

    public static boolean eventsAreDisabled() {
        return Boolean.getBoolean("lootjs.disable_events");
    }

    @Nullable
    public static <T extends Enum<T>> T valueOf(Class<T> clazz, Object o) {
        String s = o.toString();
        for (T constant : (Enum[]) clazz.getEnumConstants()) {
            if (s.equalsIgnoreCase(constant.name())) {
                return constant;
            }
        }
        return null;
    }

    private static ItemFilter ofItemFilter(@Nullable Object o) {
        if (o instanceof ItemFilter) {
            return (ItemFilter) o;
        } else {
            Ingredient ingredient = IngredientJS.of(o);
            if (ingredient.isEmpty()) {
                ConsoleJS.SERVER.warn("LootJS: Invalid ingredient for filter: " + o);
                return ItemFilter.ALWAYS_FALSE;
            } else {
                return ItemFilter.custom(ingredient);
            }
        }
    }

    @Override
    public void initStartup() {
        LootModificationsAPI.DEBUG_ACTION = s -> ConsoleJS.SERVER.info(s);
    }

    @Override
    public void registerEvents() {
        LootJSEvent.GROUP.register();
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("LootType", LootContextType.class);
        event.add("Interval", new IntervalJS());
        event.add("ItemFilter", ItemFilter.class);
        event.add("LootEntry", LootEntryWrapper.class);
        LootJSPlatform.INSTANCE.registerBindings(event);
    }

    @Override
    public void registerTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
        typeWrappers.registerSimple(LootEntry.class, LootEntryWrapper::of);
        typeWrappers.registerSimple(MinMaxBounds.Doubles.class, IntervalJS::ofDoubles);
        typeWrappers.registerSimple(MinMaxBounds.Ints.class, IntervalJS::ofInt);
        typeWrappers.registerSimple(ItemFilter.class, o -> {
            if (o instanceof List<?> list) {
                Map<Boolean, ? extends List<?>> split = (Map<Boolean, ? extends List<?>>) list.stream().collect(Collectors.partitioningBy(unknown -> unknown instanceof ItemFilter));
                List<ItemFilter> itemFilters = new ArrayList(((List) split.get(true)).stream().map(LootJSPlugin::ofItemFilter).toList());
                if (!((List) split.get(false)).isEmpty()) {
                    Ingredient ingredientFilter = IngredientJS.of(split.get(false));
                    itemFilters.add(ItemFilter.custom(ingredientFilter));
                }
                return ItemFilter.or((ItemFilter[]) itemFilters.toArray(ItemFilter[]::new));
            } else {
                return ofItemFilter(o);
            }
        });
        typeWrappers.registerSimple(ResourceLocationFilter.class, this::ofResourceLocationFilter);
        typeWrappers.registerSimple(MapDecoration.Type.class, o -> valueOf(MapDecoration.Type.class, o));
        typeWrappers.registerSimple(AttributeModifier.Operation.class, o -> valueOf(AttributeModifier.Operation.class, o));
        typeWrappers.registerSimple(Resolver.class, o -> Resolver.of(o.toString()));
    }

    private ResourceLocationFilter ofResourceLocationFilter(Object o) {
        if (o instanceof List<?> list) {
            return new ResourceLocationFilter.Or(list.stream().map(this::ofResourceLocationFilter).toList());
        } else {
            Pattern pattern = UtilsJS.parseRegex(o);
            return (ResourceLocationFilter) (pattern == null ? new ResourceLocationFilter.ByLocation(new ResourceLocation(o.toString())) : new ResourceLocationFilter.ByPattern(pattern));
        }
    }
}