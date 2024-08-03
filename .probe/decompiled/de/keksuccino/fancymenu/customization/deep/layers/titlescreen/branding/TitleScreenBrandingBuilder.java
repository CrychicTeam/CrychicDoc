package de.keksuccino.fancymenu.customization.deep.layers.titlescreen.branding;

import de.keksuccino.fancymenu.customization.deep.DeepElementBuilder;
import de.keksuccino.fancymenu.customization.deep.layers.titlescreen.TitleScreenLayer;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.SerializedElement;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TitleScreenBrandingBuilder extends DeepElementBuilder<TitleScreenLayer, TitleScreenBrandingDeepElement, TitleScreenBrandingDeepEditorElement> {

    public TitleScreenBrandingBuilder(TitleScreenLayer layer) {
        super("title_screen_branding", layer);
    }

    @NotNull
    public TitleScreenBrandingDeepElement buildDefaultInstance() {
        return new TitleScreenBrandingDeepElement(this);
    }

    public TitleScreenBrandingDeepElement deserializeElement(@NotNull SerializedElement serialized) {
        return this.buildDefaultInstance();
    }

    protected SerializedElement serializeElement(@NotNull TitleScreenBrandingDeepElement element, @NotNull SerializedElement serializeTo) {
        return serializeTo;
    }

    public void stackElements(@NotNull TitleScreenBrandingDeepElement element, @NotNull TitleScreenBrandingDeepElement stack) {
    }

    @NotNull
    public TitleScreenBrandingDeepEditorElement wrapIntoEditorElement(@NotNull TitleScreenBrandingDeepElement element, @NotNull LayoutEditorScreen editor) {
        return new TitleScreenBrandingDeepEditorElement(element, editor);
    }

    @NotNull
    @Override
    public Component getDisplayName(@Nullable AbstractElement element) {
        return Component.translatable("fancymenu.helper.editor.element.vanilla.deepcustomization.titlescreen.branding");
    }

    @Nullable
    @Override
    public Component[] getDescription(@Nullable AbstractElement element) {
        return null;
    }
}