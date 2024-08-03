package de.keksuccino.fancymenu.customization.element.elements.image;

import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.customization.element.SerializedElement;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ImageElementBuilder extends ElementBuilder<ImageElement, ImageEditorElement> {

    private static final Logger LOGGER = LogManager.getLogger();

    public ImageElementBuilder() {
        super("image");
    }

    @NotNull
    public ImageElement buildDefaultInstance() {
        ImageElement i = new ImageElement(this);
        i.baseWidth = 100;
        i.baseHeight = 100;
        return i;
    }

    public ImageElement deserializeElement(@NotNull SerializedElement serialized) {
        ImageElement element = this.buildDefaultInstance();
        element.textureSupplier = deserializeImageResourceSupplier(serialized.getValue("source"));
        element.repeat = this.deserializeBoolean(element.repeat, serialized.getValue("repeat_texture"));
        element.nineSlice = this.deserializeBoolean(element.nineSlice, serialized.getValue("nine_slice_texture"));
        element.nineSliceBorderX = (Integer) this.deserializeNumber(Integer.class, Integer.valueOf(element.nineSliceBorderX), serialized.getValue("nine_slice_texture_border_x"));
        element.nineSliceBorderY = (Integer) this.deserializeNumber(Integer.class, Integer.valueOf(element.nineSliceBorderY), serialized.getValue("nine_slice_texture_border_y"));
        return element;
    }

    protected SerializedElement serializeElement(@NotNull ImageElement element, @NotNull SerializedElement serializeTo) {
        if (element.textureSupplier != null) {
            serializeTo.putProperty("source", element.textureSupplier.getSourceWithPrefix());
        }
        serializeTo.putProperty("repeat_texture", element.repeat + "");
        serializeTo.putProperty("nine_slice_texture", element.nineSlice + "");
        serializeTo.putProperty("nine_slice_texture_border_x", element.nineSliceBorderX + "");
        serializeTo.putProperty("nine_slice_texture_border_y", element.nineSliceBorderY + "");
        return serializeTo;
    }

    @NotNull
    public ImageEditorElement wrapIntoEditorElement(@NotNull ImageElement element, @NotNull LayoutEditorScreen editor) {
        return new ImageEditorElement(element, editor);
    }

    @NotNull
    @Override
    public Component getDisplayName(@Nullable AbstractElement element) {
        return Component.translatable("fancymenu.editor.add.image");
    }

    @Nullable
    @Override
    public Component[] getDescription(@Nullable AbstractElement element) {
        return LocalizationUtils.splitLocalizedLines("fancymenu.editor.add.image.desc");
    }
}