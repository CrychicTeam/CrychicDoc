package de.keksuccino.fancymenu.customization.layout.editor.widget;

import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LayoutEditorWidgetRegistry {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final List<AbstractLayoutEditorWidgetBuilder<?>> WIDGET_BUILDERS = new ArrayList();

    public static void register(@NotNull AbstractLayoutEditorWidgetBuilder<?> widgetBuilder) {
        if (!isBuilderRegistered((String) Objects.requireNonNull(widgetBuilder.getIdentifier()))) {
            WIDGET_BUILDERS.add(widgetBuilder);
        } else {
            LOGGER.error("[FANCYMENU] Failed to register AbstractLayoutEditorWidgetBuilder! Builder with same identifier already registered! (" + widgetBuilder.getIdentifier() + ")");
        }
    }

    @NotNull
    public static List<AbstractLayoutEditorWidgetBuilder<?>> getBuilders() {
        return new ArrayList(WIDGET_BUILDERS);
    }

    @Nullable
    public static AbstractLayoutEditorWidgetBuilder<?> getBuilder(@NotNull String identifier) {
        for (AbstractLayoutEditorWidgetBuilder<?> b : WIDGET_BUILDERS) {
            if (b.getIdentifier().equals(identifier)) {
                return b;
            }
        }
        return null;
    }

    public static boolean isBuilderRegistered(@NotNull String identifier) {
        return getBuilder(identifier) != null;
    }

    @NotNull
    public static List<AbstractLayoutEditorWidget> buildWidgetInstances(@NotNull LayoutEditorScreen editor) {
        List<AbstractLayoutEditorWidget> instances = new ArrayList();
        for (AbstractLayoutEditorWidgetBuilder<?> b : WIDGET_BUILDERS) {
            try {
                AbstractLayoutEditorWidget widget = b.buildWithSettingsInternal(editor);
                if (widget != null) {
                    instances.add(widget);
                }
            } catch (Exception var5) {
                LOGGER.error("[FANCYMENU] Failed to create AbstractLayoutEditorWidget instance! (" + b.getIdentifier() + ")");
                var5.printStackTrace();
            }
        }
        return instances;
    }
}