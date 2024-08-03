package dev.xkmc.modulargolems.content.menu.registry;

import dev.xkmc.modulargolems.content.capability.GolemConfigEditor;
import dev.xkmc.modulargolems.content.menu.tabs.GolemTabGroup;

public class ConfigGroup extends GolemTabGroup<ConfigGroup> {

    public final GolemConfigEditor editor;

    public ConfigGroup(GolemConfigEditor editor) {
        super(GolemTabRegistry.LIST_CONFIG);
        this.editor = editor;
    }
}