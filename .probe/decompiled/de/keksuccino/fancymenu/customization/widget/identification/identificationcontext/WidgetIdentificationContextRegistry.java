package de.keksuccino.fancymenu.customization.widget.identification.identificationcontext;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WidgetIdentificationContextRegistry {

    private static final Map<Class<? extends Screen>, WidgetIdentificationContext> CONTEXTS = new LinkedHashMap();

    public static void register(@NotNull WidgetIdentificationContext context) {
        if (CONTEXTS.containsKey(context.getTargetScreen())) {
            throw new RuntimeException("[FANCYMENU] WidgetIdentificationContext for '" + context.getTargetScreen().getName() + "' already registered! Can't replace contexts!");
        } else {
            CONTEXTS.put(context.getTargetScreen(), context);
        }
    }

    @Nullable
    public static WidgetIdentificationContext getContextForScreen(@NotNull Class<? extends Screen> screenClass) {
        return (WidgetIdentificationContext) CONTEXTS.get(screenClass);
    }

    @NotNull
    public static List<WidgetIdentificationContext> getContexts() {
        return new ArrayList(CONTEXTS.values());
    }
}