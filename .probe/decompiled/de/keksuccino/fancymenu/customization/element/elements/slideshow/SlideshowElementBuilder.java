package de.keksuccino.fancymenu.customization.element.elements.slideshow;

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

public class SlideshowElementBuilder extends ElementBuilder<SlideshowElement, SlideshowEditorElement> {

    private static final Logger LOGGER = LogManager.getLogger();

    public SlideshowElementBuilder() {
        super("slideshow");
    }

    @NotNull
    public SlideshowElement buildDefaultInstance() {
        SlideshowElement i = new SlideshowElement(this);
        i.baseWidth = 200;
        i.baseHeight = 200;
        return i;
    }

    public SlideshowElement deserializeElement(@NotNull SerializedElement serialized) {
        SlideshowElement element = this.buildDefaultInstance();
        element.slideshowName = serialized.getValue("slideshow_name");
        return element;
    }

    protected SerializedElement serializeElement(@NotNull SlideshowElement element, @NotNull SerializedElement serializeTo) {
        if (element.slideshowName != null) {
            serializeTo.putProperty("slideshow_name", element.slideshowName);
        }
        return serializeTo;
    }

    @NotNull
    public SlideshowEditorElement wrapIntoEditorElement(@NotNull SlideshowElement element, @NotNull LayoutEditorScreen editor) {
        return new SlideshowEditorElement(element, editor);
    }

    @NotNull
    @Override
    public Component getDisplayName(@Nullable AbstractElement element) {
        return Component.translatable("fancymenu.editor.add.slideshow");
    }

    @Nullable
    @Override
    public Component[] getDescription(@Nullable AbstractElement element) {
        return LocalizationUtils.splitLocalizedLines("fancymenu.editor.add.slideshow.desc");
    }
}