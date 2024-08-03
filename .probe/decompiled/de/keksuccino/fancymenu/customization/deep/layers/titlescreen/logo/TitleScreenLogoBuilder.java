package de.keksuccino.fancymenu.customization.deep.layers.titlescreen.logo;

import de.keksuccino.fancymenu.customization.deep.DeepElementBuilder;
import de.keksuccino.fancymenu.customization.deep.layers.titlescreen.TitleScreenLayer;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.SerializedElement;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TitleScreenLogoBuilder extends DeepElementBuilder<TitleScreenLayer, TitleScreenLogoDeepElement, TitleScreenLogoDeepEditorElement> {

    public TitleScreenLogoBuilder(TitleScreenLayer layer) {
        super("title_screen_logo", layer);
    }

    @NotNull
    public TitleScreenLogoDeepElement buildDefaultInstance() {
        return new TitleScreenLogoDeepElement(this);
    }

    public TitleScreenLogoDeepElement deserializeElement(@NotNull SerializedElement serialized) {
        return this.buildDefaultInstance();
    }

    protected SerializedElement serializeElement(@NotNull TitleScreenLogoDeepElement element, @NotNull SerializedElement serializeTo) {
        return serializeTo;
    }

    public void stackElements(@NotNull TitleScreenLogoDeepElement element, @NotNull TitleScreenLogoDeepElement stack) {
    }

    @NotNull
    public TitleScreenLogoDeepEditorElement wrapIntoEditorElement(@NotNull TitleScreenLogoDeepElement element, @NotNull LayoutEditorScreen editor) {
        return new TitleScreenLogoDeepEditorElement(element, editor);
    }

    @NotNull
    @Override
    public Component getDisplayName(@Nullable AbstractElement element) {
        return Component.translatable("fancymenu.helper.editor.element.vanilla.deepcustomization.titlescreen.logo");
    }

    @Nullable
    @Override
    public Component[] getDescription(@Nullable AbstractElement element) {
        return null;
    }
}