package de.keksuccino.melody.resources.audio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import net.minecraft.sounds.SoundSource;
import org.jetbrains.annotations.NotNull;

public class MinecraftSoundSettingsObserver {

    private static final Map<Long, BiConsumer<SoundSource, Float>> VOLUME_LISTENERS = new HashMap();

    private static final Map<Long, Runnable> SOUND_ENGINE_RELOAD_LISTENERS = new HashMap();

    private static long idCountVolumeListeners = 0L;

    private static long idCountEngineReloadListeners = 0L;

    public static long registerVolumeListener(@NotNull BiConsumer<SoundSource, Float> listener) {
        Objects.requireNonNull(listener);
        idCountVolumeListeners++;
        VOLUME_LISTENERS.put(idCountVolumeListeners, listener);
        return idCountVolumeListeners;
    }

    public static void unregisterVolumeListener(long id) {
        VOLUME_LISTENERS.remove(id);
    }

    public static List<BiConsumer<SoundSource, Float>> getVolumeListeners() {
        return new ArrayList(VOLUME_LISTENERS.values());
    }

    public static long registerSoundEngineReloadListener(@NotNull Runnable listener) {
        Objects.requireNonNull(listener);
        idCountEngineReloadListeners++;
        SOUND_ENGINE_RELOAD_LISTENERS.put(idCountEngineReloadListeners, listener);
        return idCountEngineReloadListeners;
    }

    public static void unregisterSoundEngineReloadListener(long id) {
        SOUND_ENGINE_RELOAD_LISTENERS.remove(id);
    }

    public static List<Runnable> getSoundEngineReloadListeners() {
        return new ArrayList(SOUND_ENGINE_RELOAD_LISTENERS.values());
    }
}