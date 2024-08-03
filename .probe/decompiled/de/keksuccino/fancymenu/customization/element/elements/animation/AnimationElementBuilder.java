package de.keksuccino.fancymenu.customization.element.elements.animation;

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

public class AnimationElementBuilder extends ElementBuilder<AnimationElement, AnimationEditorElement> {

    private static final Logger LOGGER = LogManager.getLogger();

    public AnimationElementBuilder() {
        super("animation");
    }

    @Override
    public boolean isDeprecated() {
        return true;
    }

    @NotNull
    public AnimationElement buildDefaultInstance() {
        AnimationElement i = new AnimationElement(this);
        i.baseWidth = 200;
        i.baseHeight = 200;
        return i;
    }

    public AnimationElement deserializeElement(@NotNull SerializedElement serialized) {
        AnimationElement element = this.buildDefaultInstance();
        element.animationName = serialized.getValue("animation_name");
        return element;
    }

    protected SerializedElement serializeElement(@NotNull AnimationElement element, @NotNull SerializedElement serializeTo) {
        if (element.animationName != null) {
            serializeTo.putProperty("animation_name", element.animationName);
        }
        return serializeTo;
    }

    @NotNull
    public AnimationEditorElement wrapIntoEditorElement(@NotNull AnimationElement element, @NotNull LayoutEditorScreen editor) {
        return new AnimationEditorElement(element, editor);
    }

    @NotNull
    @Override
    public Component getDisplayName(@Nullable AbstractElement element) {
        return Component.translatable("fancymenu.editor.add.animation");
    }

    @Nullable
    @Override
    public Component[] getDescription(@Nullable AbstractElement element) {
        return LocalizationUtils.splitLocalizedLines("fancymenu.editor.add.animation.desc");
    }
}