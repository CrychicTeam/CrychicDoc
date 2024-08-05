package de.keksuccino.fancymenu.customization.deep;

import de.keksuccino.fancymenu.customization.screen.identifier.ScreenIdentifierHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DeepScreenCustomizationLayerRegistry {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Map<String, DeepScreenCustomizationLayer> LAYERS = new HashMap();

    public static void register(@NotNull DeepScreenCustomizationLayer layer) {
        if (LAYERS.containsKey(Objects.requireNonNull(layer.getTargetScreenClassPath()))) {
            LOGGER.warn("[FANCYMENU] DeepScreenCustomizationLayer for screen '" + layer.getTargetScreenClassPath() + "' already registered! Replacing layer..");
        }
        LAYERS.put(layer.getTargetScreenClassPath(), layer);
    }

    @NotNull
    public static List<DeepScreenCustomizationLayer> getLayers() {
        return new ArrayList(LAYERS.values());
    }

    @Nullable
    public static DeepScreenCustomizationLayer getLayer(@NotNull String screenIdentifier) {
        screenIdentifier = ScreenIdentifierHandler.tryConvertToNonUniversal(screenIdentifier);
        return (DeepScreenCustomizationLayer) LAYERS.get(screenIdentifier);
    }

    public boolean hasLayer(@NotNull String screenIdentifier) {
        screenIdentifier = ScreenIdentifierHandler.tryConvertToNonUniversal(screenIdentifier);
        return LAYERS.containsKey(screenIdentifier);
    }
}