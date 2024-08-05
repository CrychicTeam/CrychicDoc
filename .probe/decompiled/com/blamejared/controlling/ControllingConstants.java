package com.blamejared.controlling;

import com.blamejared.controlling.client.FreeKeysList;
import com.blamejared.controlling.client.NewKeyBindsList;
import com.blamejared.searchables.api.SearchableComponent;
import com.blamejared.searchables.api.SearchableType;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ControllingConstants {

    public static final MutableComponent COMPONENT_CONTROLS_RESET = Component.translatable("controls.reset");

    public static final MutableComponent COMPONENT_CONTROLS_RESET_ALL = Component.translatable("controls.resetAll");

    public static final MutableComponent COMPONENT_OPTIONS_CONFIRM_RESET = Component.translatable("options.confirmReset");

    public static final MutableComponent COMPONENT_OPTIONS_SHOW_NONE = Component.translatable("options.showNone");

    public static final MutableComponent COMPONENT_OPTIONS_SHOW_ALL = Component.translatable("options.showAll");

    public static final MutableComponent COMPONENT_OPTIONS_SHOW_CONFLICTS = Component.translatable("options.showConflicts");

    public static final MutableComponent COMPONENT_OPTIONS_SORT = Component.translatable("options.sort");

    public static final MutableComponent COMPONENT_OPTIONS_TOGGLE_FREE = Component.translatable("options.toggleFree");

    public static final MutableComponent COMPONENT_OPTIONS_AVAILABLE_KEYS = Component.translatable("options.availableKeys");

    public static final SearchableType<KeyBindsList.Entry> SEARCHABLE_KEYBINDINGS = new SearchableType.Builder<KeyBindsList.Entry>().component(SearchableComponent.create("category", (Function<KeyBindsList.Entry, Optional<String>>) (entry -> {
        if (entry instanceof NewKeyBindsList.CategoryEntry cat) {
            return Optional.of(cat.name().getString());
        } else {
            return entry instanceof NewKeyBindsList.KeyEntry key ? Optional.of(key.categoryName().getString()) : Optional.empty();
        }
    }))).component(SearchableComponent.create("key", (Function<KeyBindsList.Entry, Optional<String>>) (entry -> entry instanceof NewKeyBindsList.KeyEntry key ? Optional.of(key.getKey().getTranslatedKeyMessage().getString()) : Optional.empty()))).defaultComponent(SearchableComponent.create("name", (Function<KeyBindsList.Entry, Optional<String>>) (entry -> {
        if (entry instanceof NewKeyBindsList.KeyEntry key) {
            return Optional.of(key.getKeyDesc().getString());
        } else {
            return entry instanceof FreeKeysList.InputEntry input ? Optional.of(input.getInput().getName()) : Optional.empty();
        }
    }))).build();
}