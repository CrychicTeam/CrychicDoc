package net.minecraftforge.client;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.gui.screens.worldselection.PresetEditor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraftforge.client.event.RegisterPresetEditorsEvent;
import net.minecraftforge.fml.ModLoader;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public final class PresetEditorManager {

    private static Map<ResourceKey<WorldPreset>, PresetEditor> editors = Map.of();

    private PresetEditorManager() {
    }

    @Internal
    static void init() {
        Map<ResourceKey<WorldPreset>, PresetEditor> gatheredEditors = new HashMap();
        PresetEditor.EDITORS.forEach((k, v) -> k.ifPresent(key -> gatheredEditors.put(key, v)));
        RegisterPresetEditorsEvent event = new RegisterPresetEditorsEvent(gatheredEditors);
        ModLoader.get().postEventWrapContainerInModOrder(event);
        editors = gatheredEditors;
    }

    @Nullable
    public static PresetEditor get(ResourceKey<WorldPreset> key) {
        return (PresetEditor) editors.get(key);
    }
}