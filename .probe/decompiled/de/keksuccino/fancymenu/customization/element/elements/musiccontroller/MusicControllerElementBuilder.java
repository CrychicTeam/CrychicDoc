package de.keksuccino.fancymenu.customization.element.elements.musiccontroller;

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

public class MusicControllerElementBuilder extends ElementBuilder<MusicControllerElement, MusicControllerEditorElement> {

    private static final Logger LOGGER = LogManager.getLogger();

    public MusicControllerElementBuilder() {
        super("music_controller");
        MusicControllerHandler.init();
    }

    @NotNull
    public MusicControllerElement buildDefaultInstance() {
        MusicControllerElement i = new MusicControllerElement(this);
        i.baseWidth = 100;
        i.baseHeight = 100;
        return i;
    }

    public MusicControllerElement deserializeElement(@NotNull SerializedElement serialized) {
        MusicControllerElement element = this.buildDefaultInstance();
        element.playMenuMusic = this.deserializeBoolean(element.playMenuMusic, serialized.getValue("play_menu_music"));
        element.playWorldMusic = this.deserializeBoolean(element.playWorldMusic, serialized.getValue("play_world_music"));
        return element;
    }

    protected SerializedElement serializeElement(@NotNull MusicControllerElement element, @NotNull SerializedElement serializeTo) {
        serializeTo.putProperty("play_menu_music", element.playMenuMusic + "");
        serializeTo.putProperty("play_world_music", element.playWorldMusic + "");
        return serializeTo;
    }

    @NotNull
    public MusicControllerEditorElement wrapIntoEditorElement(@NotNull MusicControllerElement element, @NotNull LayoutEditorScreen editor) {
        return new MusicControllerEditorElement(element, editor);
    }

    @NotNull
    @Override
    public Component getDisplayName(@Nullable AbstractElement element) {
        return Component.translatable("fancymenu.elements.music_controller");
    }

    @Nullable
    @Override
    public Component[] getDescription(@Nullable AbstractElement element) {
        return LocalizationUtils.splitLocalizedLines("fancymenu.elements.music_controller.desc");
    }
}