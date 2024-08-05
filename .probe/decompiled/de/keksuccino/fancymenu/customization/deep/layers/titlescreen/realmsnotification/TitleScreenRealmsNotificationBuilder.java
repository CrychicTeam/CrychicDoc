package de.keksuccino.fancymenu.customization.deep.layers.titlescreen.realmsnotification;

import de.keksuccino.fancymenu.customization.deep.DeepElementBuilder;
import de.keksuccino.fancymenu.customization.deep.layers.titlescreen.TitleScreenLayer;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.SerializedElement;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TitleScreenRealmsNotificationBuilder extends DeepElementBuilder<TitleScreenLayer, TitleScreenRealmsNotificationDeepElement, TitleScreenRealmsNotificationDeepEditorElement> {

    public TitleScreenRealmsNotificationBuilder(TitleScreenLayer layer) {
        super("title_screen_realms_notification", layer);
    }

    public void stackElements(@NotNull TitleScreenRealmsNotificationDeepElement element, @NotNull TitleScreenRealmsNotificationDeepElement stack) {
    }

    @NotNull
    public TitleScreenRealmsNotificationDeepElement buildDefaultInstance() {
        return new TitleScreenRealmsNotificationDeepElement(this);
    }

    public TitleScreenRealmsNotificationDeepElement deserializeElement(@NotNull SerializedElement serialized) {
        return this.buildDefaultInstance();
    }

    protected SerializedElement serializeElement(@NotNull TitleScreenRealmsNotificationDeepElement element, @NotNull SerializedElement serializeTo) {
        return serializeTo;
    }

    @NotNull
    public TitleScreenRealmsNotificationDeepEditorElement wrapIntoEditorElement(@NotNull TitleScreenRealmsNotificationDeepElement element, @NotNull LayoutEditorScreen editor) {
        return new TitleScreenRealmsNotificationDeepEditorElement(element, editor);
    }

    @NotNull
    @Override
    public Component getDisplayName(@Nullable AbstractElement element) {
        return Component.translatable("fancymenu.helper.editor.element.vanilla.deepcustomization.titlescreen.realmsnotification");
    }

    @Nullable
    @Override
    public Component[] getDescription(@Nullable AbstractElement element) {
        return null;
    }
}