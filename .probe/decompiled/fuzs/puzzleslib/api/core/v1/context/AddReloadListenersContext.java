package fuzs.puzzleslib.api.core.v1.context;

import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;

@FunctionalInterface
public interface AddReloadListenersContext {

    void registerReloadListener(String var1, PreparableReloadListener var2);

    default void registerReloadListener(String id, ResourceManagerReloadListener reloadListener) {
        this.registerReloadListener(id, (PreparableReloadListener) reloadListener);
    }

    default <T> void registerReloadListener(String id, SimplePreparableReloadListener<T> reloadListener) {
        this.registerReloadListener(id, reloadListener);
    }
}