package de.keksuccino.fancymenu.customization.deep.layers.titlescreen.forge.copyright;

import de.keksuccino.fancymenu.customization.deep.DeepElementBuilder;
import de.keksuccino.fancymenu.customization.deep.layers.titlescreen.TitleScreenLayer;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.SerializedElement;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TitleScreenForgeCopyrightBuilder extends DeepElementBuilder<TitleScreenLayer, TitleScreenForgeCopyrightDeepElement, TitleScreenForgeCopyrightDeepEditorElement> {

    public TitleScreenForgeCopyrightBuilder(TitleScreenLayer layer) {
        super("title_screen_forge_copyright", layer);
    }

    @NotNull
    public TitleScreenForgeCopyrightDeepElement buildDefaultInstance() {
        return new TitleScreenForgeCopyrightDeepElement(this);
    }

    public TitleScreenForgeCopyrightDeepElement deserializeElement(@NotNull SerializedElement serialized) {
        return this.buildDefaultInstance();
    }

    protected SerializedElement serializeElement(@NotNull TitleScreenForgeCopyrightDeepElement element, @NotNull SerializedElement serializeTo) {
        return serializeTo;
    }

    public void stackElements(@NotNull TitleScreenForgeCopyrightDeepElement element, @NotNull TitleScreenForgeCopyrightDeepElement stack) {
    }

    @NotNull
    public TitleScreenForgeCopyrightDeepEditorElement wrapIntoEditorElement(@NotNull TitleScreenForgeCopyrightDeepElement element, @NotNull LayoutEditorScreen editor) {
        return new TitleScreenForgeCopyrightDeepEditorElement(element, editor);
    }

    @NotNull
    @Override
    public Component getDisplayName(@Nullable AbstractElement element) {
        return Component.translatable("fancymenu.helper.editor.element.vanilla.deepcustomization.titlescreen.forge.copyright");
    }

    @Nullable
    @Override
    public Component[] getDescription(@Nullable AbstractElement element) {
        return null;
    }
}