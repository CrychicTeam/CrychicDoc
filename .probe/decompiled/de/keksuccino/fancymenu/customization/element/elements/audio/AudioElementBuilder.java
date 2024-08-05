package de.keksuccino.fancymenu.customization.element.elements.audio;

import de.keksuccino.fancymenu.customization.ScreenCustomization;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.customization.element.SerializedElement;
import de.keksuccino.fancymenu.customization.layer.ScreenCustomizationLayer;
import de.keksuccino.fancymenu.customization.layer.ScreenCustomizationLayerHandler;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import de.keksuccino.fancymenu.events.ModReloadEvent;
import de.keksuccino.fancymenu.events.screen.InitOrResizeScreenCompletedEvent;
import de.keksuccino.fancymenu.events.screen.InitOrResizeScreenStartingEvent;
import de.keksuccino.fancymenu.events.ticking.ClientTickEvent;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import de.keksuccino.fancymenu.util.Trio;
import de.keksuccino.fancymenu.util.event.acara.EventHandler;
import de.keksuccino.fancymenu.util.event.acara.EventListener;
import de.keksuccino.fancymenu.util.resource.ResourceSupplier;
import de.keksuccino.fancymenu.util.resource.resources.audio.IAudio;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AudioElementBuilder extends ElementBuilder<AudioElement, AudioEditorElement> {

    private static final Logger LOGGER = LogManager.getLogger();

    protected static final Map<String, Trio<ResourceSupplier<IAudio>, IAudio, Integer>> CURRENT_AUDIO_CACHE = new HashMap();

    protected static boolean screenIsNull = false;

    public AudioElementBuilder() {
        super("audio_v2");
        EventHandler.INSTANCE.registerListenersOf(this);
    }

    @EventListener
    public void onClientTickPre(ClientTickEvent.Pre e) {
        if (Minecraft.getInstance().screen == null && !screenIsNull) {
            screenIsNull = true;
            CURRENT_AUDIO_CACHE.forEach((s, resourceSupplierIAudioPair) -> {
                if (((IAudio) resourceSupplierIAudioPair.getSecond()).isReady()) {
                    ((IAudio) resourceSupplierIAudioPair.getSecond()).stop();
                }
            });
            CURRENT_AUDIO_CACHE.clear();
        }
    }

    @EventListener
    public void onInitOrResizeStarting(InitOrResizeScreenStartingEvent e) {
        screenIsNull = false;
    }

    @EventListener
    public void onInitOrResizeScreenCompleted(InitOrResizeScreenCompletedEvent e) {
        ScreenCustomizationLayer activeLayer = ScreenCustomizationLayerHandler.getActiveLayer();
        if (ScreenCustomization.isCustomizationEnabledForScreen(e.getScreen()) && activeLayer != null) {
            List<String> removeFromCache = new ArrayList();
            CURRENT_AUDIO_CACHE.forEach((sx, resourceSupplierIAudioPair) -> {
                AbstractElement element = activeLayer.getElementByInstanceIdentifier(sx);
                if (element == null) {
                    ((IAudio) resourceSupplierIAudioPair.getSecond()).stop();
                    removeFromCache.add(sx);
                } else if (element instanceof AudioElement a) {
                    boolean audioFound = false;
                    for (AudioElement.AudioInstance instance : a.audios) {
                        if (Objects.equals(instance.supplier.get(), resourceSupplierIAudioPair.getSecond())) {
                            audioFound = true;
                            break;
                        }
                    }
                    if (!audioFound) {
                        ((IAudio) resourceSupplierIAudioPair.getSecond()).stop();
                        removeFromCache.add(sx);
                    }
                }
            });
            for (String s : removeFromCache) {
                CURRENT_AUDIO_CACHE.remove(s);
            }
        } else {
            CURRENT_AUDIO_CACHE.forEach((sx, resourceSupplierIAudioPair) -> {
                if (((IAudio) resourceSupplierIAudioPair.getSecond()).isReady()) {
                    ((IAudio) resourceSupplierIAudioPair.getSecond()).stop();
                }
            });
            CURRENT_AUDIO_CACHE.clear();
        }
    }

    @EventListener
    public void onModReload(ModReloadEvent e) {
        LOGGER.info("[FANCYMENU] Clearing Audio element cache..");
        CURRENT_AUDIO_CACHE.forEach((s, resourceSupplierIAudioPair) -> {
            if (((IAudio) resourceSupplierIAudioPair.getSecond()).isReady()) {
                ((IAudio) resourceSupplierIAudioPair.getSecond()).stop();
            }
        });
        CURRENT_AUDIO_CACHE.clear();
    }

    @NotNull
    public AudioElement buildDefaultInstance() {
        AudioElement i = new AudioElement(this);
        i.baseWidth = 100;
        i.baseHeight = 100;
        return i;
    }

    public AudioElement deserializeElement(@NotNull SerializedElement serialized) {
        AudioElement element = this.buildDefaultInstance();
        element.audios.addAll(AudioElement.AudioInstance.deserializeAllOfContainer(serialized));
        String playMode = serialized.getValue("play_mode");
        if (playMode != null) {
            element.setPlayMode((AudioElement.PlayMode) Objects.requireNonNullElse(AudioElement.PlayMode.getByName(playMode), AudioElement.PlayMode.NORMAL), false);
        }
        element.setLooping(this.deserializeBoolean(element.loop, serialized.getValue("looping")), false);
        element.setVolume((Float) this.deserializeNumber(Float.class, Float.valueOf(element.volume), serialized.getValue("volume")));
        String soundSource = serialized.getValue("sound_source");
        if (soundSource != null) {
            element.setSoundSource((SoundSource) Objects.requireNonNullElse(getSoundSourceByName(soundSource), SoundSource.MASTER));
        }
        return element;
    }

    @Nullable
    protected static SoundSource getSoundSourceByName(@NotNull String name) {
        for (SoundSource source : SoundSource.values()) {
            if (source.getName().equals(name)) {
                return source;
            }
        }
        return null;
    }

    protected SerializedElement serializeElement(@NotNull AudioElement element, @NotNull SerializedElement serializeTo) {
        AudioElement.AudioInstance.serializeAllToExistingContainer(element.audios, serializeTo);
        serializeTo.putProperty("play_mode", element.playMode.getName());
        serializeTo.putProperty("looping", element.loop + "");
        serializeTo.putProperty("volume", element.volume + "");
        serializeTo.putProperty("sound_source", element.soundSource.getName());
        return serializeTo;
    }

    @NotNull
    public AudioEditorElement wrapIntoEditorElement(@NotNull AudioElement element, @NotNull LayoutEditorScreen editor) {
        return new AudioEditorElement(element, editor);
    }

    @NotNull
    @Override
    public Component getDisplayName(@Nullable AbstractElement element) {
        return Component.translatable("fancymenu.elements.audio");
    }

    @Nullable
    @Override
    public Component[] getDescription(@Nullable AbstractElement element) {
        return LocalizationUtils.splitLocalizedLines("fancymenu.elements.audio.desc");
    }
}