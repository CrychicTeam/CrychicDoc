package de.keksuccino.fancymenu.customization.element.elements.progressbar;

import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.customization.element.SerializedElement;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import de.keksuccino.fancymenu.util.rendering.DrawableColor;
import java.util.Objects;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ProgressBarElementBuilder extends ElementBuilder<ProgressBarElement, ProgressBarEditorElement> {

    private static final Logger LOGGER = LogManager.getLogger();

    public ProgressBarElementBuilder() {
        super("progress_bar");
    }

    @NotNull
    public ProgressBarElement buildDefaultInstance() {
        ProgressBarElement element = new ProgressBarElement(this);
        element.baseWidth = 200;
        element.baseHeight = 20;
        element.progressSource = "50";
        return element;
    }

    public ProgressBarElement deserializeElement(@NotNull SerializedElement serialized) {
        ProgressBarElement element = this.buildDefaultInstance();
        element.useProgressForElementAnchor = this.deserializeBoolean(element.useProgressForElementAnchor, serialized.getValue("progress_for_element_anchor"));
        String barHex = serialized.getValue("bar_color");
        if (barHex != null && !barHex.replace(" ", "").equals("")) {
            element.barColor = DrawableColor.of(barHex);
        }
        element.barTextureSupplier = deserializeImageResourceSupplier(serialized.getValue("bar_texture"));
        String backgroundHex = serialized.getValue("background_color");
        if (backgroundHex != null && !backgroundHex.replace(" ", "").equals("")) {
            element.backgroundColor = DrawableColor.of(backgroundHex);
        }
        element.backgroundTextureSupplier = deserializeImageResourceSupplier(serialized.getValue("background_texture"));
        String barDirection = serialized.getValue("direction");
        if (barDirection != null) {
            element.direction = (ProgressBarElement.BarDirection) Objects.requireNonNullElse(ProgressBarElement.BarDirection.getByName(barDirection), ProgressBarElement.BarDirection.LEFT);
        }
        String valueMode = serialized.getValue("value_mode");
        if (valueMode != null) {
            element.progressValueMode = (ProgressBarElement.ProgressValueMode) Objects.requireNonNullElse(ProgressBarElement.ProgressValueMode.getByName(valueMode), ProgressBarElement.ProgressValueMode.PERCENTAGE);
        }
        element.progressSource = serialized.getValue("progress_source");
        return element;
    }

    protected SerializedElement serializeElement(@NotNull ProgressBarElement element, @NotNull SerializedElement serializeTo) {
        serializeTo.putProperty("bar_color", element.barColor.getHex());
        if (element.barTextureSupplier != null) {
            serializeTo.putProperty("bar_texture", element.barTextureSupplier.getSourceWithPrefix());
        }
        serializeTo.putProperty("background_color", element.backgroundColor.getHex());
        if (element.backgroundTextureSupplier != null) {
            serializeTo.putProperty("background_texture", element.backgroundTextureSupplier.getSourceWithPrefix());
        }
        serializeTo.putProperty("direction", element.direction.getName());
        serializeTo.putProperty("progress_for_element_anchor", element.useProgressForElementAnchor + "");
        serializeTo.putProperty("progress_source", element.progressSource);
        serializeTo.putProperty("value_mode", element.progressValueMode.getName());
        return serializeTo;
    }

    @NotNull
    public ProgressBarEditorElement wrapIntoEditorElement(@NotNull ProgressBarElement element, @NotNull LayoutEditorScreen editor) {
        return new ProgressBarEditorElement(element, editor);
    }

    @NotNull
    @Override
    public Component getDisplayName(@Nullable AbstractElement element) {
        return Component.translatable("fancymenu.editor.elements.progress_bar");
    }

    @Nullable
    @Override
    public Component[] getDescription(@Nullable AbstractElement element) {
        return LocalizationUtils.splitLocalizedLines("fancymenu.editor.elements.progress_bar.desc");
    }
}