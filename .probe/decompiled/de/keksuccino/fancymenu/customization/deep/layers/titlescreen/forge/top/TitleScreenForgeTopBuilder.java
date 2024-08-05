package de.keksuccino.fancymenu.customization.deep.layers.titlescreen.forge.top;

import de.keksuccino.fancymenu.customization.deep.DeepElementBuilder;
import de.keksuccino.fancymenu.customization.deep.layers.titlescreen.TitleScreenLayer;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.SerializedElement;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TitleScreenForgeTopBuilder extends DeepElementBuilder<TitleScreenLayer, TitleScreenForgeTopDeepElement, TitleScreenForgeTopDeepEditorElement> {

    public TitleScreenForgeTopBuilder(TitleScreenLayer layer) {
        super("title_screen_forge_top", layer);
    }

    @NotNull
    public TitleScreenForgeTopDeepElement buildDefaultInstance() {
        return new TitleScreenForgeTopDeepElement(this);
    }

    public TitleScreenForgeTopDeepElement deserializeElement(@NotNull SerializedElement serialized) {
        return this.buildDefaultInstance();
    }

    protected SerializedElement serializeElement(@NotNull TitleScreenForgeTopDeepElement element, @NotNull SerializedElement serializeTo) {
        return serializeTo;
    }

    public void stackElements(@NotNull TitleScreenForgeTopDeepElement element, @NotNull TitleScreenForgeTopDeepElement stack) {
    }

    @NotNull
    public TitleScreenForgeTopDeepEditorElement wrapIntoEditorElement(@NotNull TitleScreenForgeTopDeepElement element, @NotNull LayoutEditorScreen editor) {
        return new TitleScreenForgeTopDeepEditorElement(element, editor);
    }

    @NotNull
    @Override
    public Component getDisplayName(@Nullable AbstractElement element) {
        return Component.translatable("fancymenu.helper.editor.element.vanilla.deepcustomization.titlescreen.forge.top");
    }

    @Nullable
    @Override
    public Component[] getDescription(@Nullable AbstractElement element) {
        return null;
    }
}