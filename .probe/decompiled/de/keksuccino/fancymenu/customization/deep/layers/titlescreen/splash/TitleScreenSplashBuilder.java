package de.keksuccino.fancymenu.customization.deep.layers.titlescreen.splash;

import de.keksuccino.fancymenu.customization.deep.DeepElementBuilder;
import de.keksuccino.fancymenu.customization.deep.layers.titlescreen.TitleScreenLayer;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.SerializedElement;
import de.keksuccino.fancymenu.customization.element.anchor.ElementAnchorPoints;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TitleScreenSplashBuilder extends DeepElementBuilder<TitleScreenLayer, TitleScreenSplashDeepElement, TitleScreenSplashDeepEditorElement> {

    public TitleScreenSplashBuilder(TitleScreenLayer layer) {
        super("title_screen_splash", layer);
    }

    @NotNull
    public TitleScreenSplashDeepElement buildDefaultInstance() {
        TitleScreenSplashDeepElement i = new TitleScreenSplashDeepElement(this);
        i.anchorPoint = ElementAnchorPoints.VANILLA;
        return i;
    }

    public TitleScreenSplashDeepElement deserializeElement(@NotNull SerializedElement serialized) {
        return this.buildDefaultInstance();
    }

    protected SerializedElement serializeElement(@NotNull TitleScreenSplashDeepElement element, @NotNull SerializedElement serializeTo) {
        return serializeTo;
    }

    @NotNull
    public TitleScreenSplashDeepEditorElement wrapIntoEditorElement(@NotNull TitleScreenSplashDeepElement element, @NotNull LayoutEditorScreen editor) {
        return new TitleScreenSplashDeepEditorElement(element, editor);
    }

    @NotNull
    @Override
    public Component getDisplayName(@Nullable AbstractElement element) {
        return Component.translatable("fancymenu.helper.editor.element.vanilla.deepcustomization.titlescreen.splash");
    }

    @Nullable
    @Override
    public Component[] getDescription(@Nullable AbstractElement element) {
        return null;
    }

    public void stackElements(@NotNull TitleScreenSplashDeepElement element, @NotNull TitleScreenSplashDeepElement stack) {
    }
}