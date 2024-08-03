package net.minecraftforge.client.event;

import java.util.Map;
import net.minecraft.client.gui.screens.worldselection.PresetEditor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.IModBusEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus.Internal;

public class RegisterPresetEditorsEvent extends Event implements IModBusEvent {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Map<ResourceKey<WorldPreset>, PresetEditor> editors;

    @Internal
    public RegisterPresetEditorsEvent(Map<ResourceKey<WorldPreset>, PresetEditor> editors) {
        this.editors = editors;
    }

    public void register(ResourceKey<WorldPreset> key, PresetEditor editor) {
        PresetEditor old = (PresetEditor) this.editors.put(key, editor);
        if (old != null) {
            LOGGER.debug("PresetEditor {} overridden by mod {}", key.location(), ModLoadingContext.get().getActiveNamespace());
        }
    }
}