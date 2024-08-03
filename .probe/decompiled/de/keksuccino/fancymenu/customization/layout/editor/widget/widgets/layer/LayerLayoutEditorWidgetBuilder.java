package de.keksuccino.fancymenu.customization.layout.editor.widget.widgets.layer;

import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import de.keksuccino.fancymenu.customization.layout.editor.widget.AbstractLayoutEditorWidgetBuilder;
import org.jetbrains.annotations.NotNull;

public class LayerLayoutEditorWidgetBuilder extends AbstractLayoutEditorWidgetBuilder<LayerLayoutEditorWidget> {

    public LayerLayoutEditorWidgetBuilder() {
        super("element_layer_control");
    }

    public void applySettings(@NotNull LayoutEditorScreen editor, @NotNull AbstractLayoutEditorWidgetBuilder.WidgetSettings settings, @NotNull LayerLayoutEditorWidget applyTo) {
    }

    @NotNull
    public LayerLayoutEditorWidget buildDefaultInstance(@NotNull LayoutEditorScreen editor) {
        LayerLayoutEditorWidget w = new LayerLayoutEditorWidget(editor, this);
        w.setBodyWidth(200.0F);
        w.setBodyHeight(300.0F);
        return w;
    }

    public void writeSettings(@NotNull AbstractLayoutEditorWidgetBuilder.WidgetSettings settings, @NotNull LayerLayoutEditorWidget widgetInstance) {
    }
}