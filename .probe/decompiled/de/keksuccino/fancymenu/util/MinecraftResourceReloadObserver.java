package de.keksuccino.fancymenu.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

public class MinecraftResourceReloadObserver {

    private static final Map<Long, Consumer<MinecraftResourceReloadObserver.ReloadAction>> RELOAD_LISTENERS = new HashMap();

    private static long reloadListenerCount = 0L;

    public static long addReloadListener(@NotNull Consumer<MinecraftResourceReloadObserver.ReloadAction> listener) {
        reloadListenerCount++;
        RELOAD_LISTENERS.put(reloadListenerCount, listener);
        return reloadListenerCount;
    }

    public static void removeReloadListener(long id) {
        RELOAD_LISTENERS.remove(id);
    }

    public static List<Consumer<MinecraftResourceReloadObserver.ReloadAction>> getReloadListeners() {
        return new ArrayList(RELOAD_LISTENERS.values());
    }

    public static enum ReloadAction {

        STARTING, FINISHED
    }
}