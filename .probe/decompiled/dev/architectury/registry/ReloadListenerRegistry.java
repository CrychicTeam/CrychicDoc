package dev.architectury.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import dev.architectury.registry.forge.ReloadListenerRegistryImpl;
import java.util.Collection;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import org.jetbrains.annotations.Nullable;

public final class ReloadListenerRegistry {

    private ReloadListenerRegistry() {
    }

    public static void register(PackType type, PreparableReloadListener listener) {
        register(type, listener, null);
    }

    public static void register(PackType type, PreparableReloadListener listener, @Nullable ResourceLocation listenerId) {
        register(type, listener, listenerId, List.of());
    }

    @ExpectPlatform
    @Transformed
    public static void register(PackType type, PreparableReloadListener listener, @Nullable ResourceLocation listenerId, Collection<ResourceLocation> dependencies) {
        ReloadListenerRegistryImpl.register(type, listener, listenerId, dependencies);
    }
}