package dev.latvian.mods.kubejs.integration.rei;

import dev.architectury.event.EventResult;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import me.shedaniel.rei.api.client.entry.filtering.base.BasicFilteringRule;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.entry.CollapsibleEntryRegistry;
import me.shedaniel.rei.api.client.registry.entry.EntryRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.EntryDefinition;
import me.shedaniel.rei.api.common.entry.type.EntryType;
import me.shedaniel.rei.api.common.entry.type.EntryTypeRegistry;
import me.shedaniel.rei.api.common.plugins.PluginManager;
import me.shedaniel.rei.api.common.registry.ReloadStage;
import net.minecraft.resources.ResourceLocation;

public class KubeJSREIPlugin implements REIClientPlugin {

    private final Set<CategoryIdentifier<?>> categoriesRemoved = new HashSet();

    private final Map<CategoryIdentifier<?>, Collection<ResourceLocation>> recipesRemoved = new HashMap();

    private final REIEntryWrappers entryWrappers = new REIEntryWrappers();

    public double getPriority() {
        return 1.0E7;
    }

    public void registerEntries(EntryRegistry registry) {
        if (REIEvents.ADD.hasListeners()) {
            for (EntryWrapper<?, ?> wrapper : this.entryWrappers.getWrappers()) {
                if (REIEvents.ADD.hasListeners(wrapper.type().getId())) {
                    REIEvents.ADD.post(new AddREIEventJS<>(registry, wrapper), wrapper.type().getId());
                }
            }
        }
    }

    public void registerBasicEntryFiltering(BasicFilteringRule<?> rule) {
        if (REIEvents.HIDE.hasListeners()) {
            EntryRegistry registry = EntryRegistry.getInstance();
            EntryStack[] allEntries = (EntryStack[]) registry.getEntryStacks().toArray(EntryStack[]::new);
            for (EntryWrapper<?, ?> wrapper : this.entryWrappers.getWrappers()) {
                if (REIEvents.HIDE.hasListeners(wrapper.type().getId())) {
                    REIEvents.HIDE.post(new HideREIEventJS<>(registry, wrapper, rule, allEntries), wrapper.type().getId());
                }
            }
        }
    }

    public void registerDisplays(DisplayRegistry registry) {
        if (REIEvents.INFORMATION.hasListeners()) {
            REIEvents.INFORMATION.post(new InformationREIEventJS(this.entryWrappers));
        }
        registry.registerVisibilityPredicate((cat, display) -> {
            Optional<ResourceLocation> id = display.getDisplayLocation();
            return id.isPresent() && ((Collection) this.recipesRemoved.getOrDefault(cat.getCategoryIdentifier(), List.of())).contains(id.get()) ? EventResult.interruptFalse() : EventResult.pass();
        });
    }

    public void registerCategories(CategoryRegistry registry) {
        registry.registerVisibilityPredicate(category -> this.categoriesRemoved.contains(category.getCategoryIdentifier()) ? EventResult.interruptFalse() : EventResult.pass());
    }

    public void postStage(PluginManager<REIClientPlugin> manager, ReloadStage stage) {
        if (stage == ReloadStage.END) {
            this.categoriesRemoved.clear();
            this.recipesRemoved.clear();
            if (REIEvents.REMOVE_CATEGORIES.hasListeners()) {
                REIEvents.REMOVE_CATEGORIES.post(new RemoveREICategoryEventJS(this.categoriesRemoved));
            }
            if (REIEvents.REMOVE_RECIPES.hasListeners()) {
                REIEvents.REMOVE_RECIPES.post(new RemoveREIRecipeEventJS(this.recipesRemoved));
            }
        }
    }

    public void registerCollapsibleEntries(CollapsibleEntryRegistry registry) {
        if (REIEvents.GROUP_ENTRIES.hasListeners()) {
            REIEvents.GROUP_ENTRIES.post(new GroupREIEntriesEventJS(this.entryWrappers, registry));
        }
    }

    public static EntryType<?> getTypeOrThrow(ResourceLocation typeId) {
        return ((EntryDefinition) Objects.requireNonNull(EntryTypeRegistry.getInstance().get(typeId), "Entry type '%s' not found!".formatted(typeId))).getType();
    }
}