package fuzs.puzzleslib.api.core.v1.resources;

import fuzs.puzzleslib.impl.core.resources.ForwardingReloadListener;
import fuzs.puzzleslib.impl.core.resources.ForwardingResourceManagerReloadListener;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public final class ForwardingReloadListenerHelper {

    private ForwardingReloadListenerHelper() {
    }

    public static <T extends PreparableReloadListener & NamedReloadListener> T fromReloadListener(ResourceLocation identifier, PreparableReloadListener reloadListener) {
        return fromReloadListener(identifier, () -> reloadListener);
    }

    public static <T extends PreparableReloadListener & NamedReloadListener> T fromReloadListener(ResourceLocation identifier, Supplier<PreparableReloadListener> supplier) {
        return fromReloadListeners(identifier, (Supplier<Collection<PreparableReloadListener>>) (() -> List.of((PreparableReloadListener) supplier.get())));
    }

    public static <T extends PreparableReloadListener & NamedReloadListener> T fromReloadListeners(ResourceLocation identifier, Collection<PreparableReloadListener> reloadListeners) {
        return fromReloadListeners(identifier, (Supplier<Collection<PreparableReloadListener>>) (() -> reloadListeners));
    }

    public static <T extends PreparableReloadListener & NamedReloadListener> T fromReloadListeners(ResourceLocation identifier, Supplier<Collection<PreparableReloadListener>> supplier) {
        return (T) (new ForwardingReloadListener(identifier, (Supplier<Collection<T>>) supplier));
    }

    public static <T extends ResourceManagerReloadListener & NamedReloadListener> T fromResourceManagerReloadListener(ResourceLocation identifier, ResourceManagerReloadListener reloadListener) {
        return fromResourceManagerReloadListener(identifier, () -> reloadListener);
    }

    public static <T extends ResourceManagerReloadListener & NamedReloadListener> T fromResourceManagerReloadListener(ResourceLocation identifier, Supplier<ResourceManagerReloadListener> supplier) {
        return fromResourceManagerReloadListeners(identifier, (Supplier<Collection<ResourceManagerReloadListener>>) (() -> List.of((ResourceManagerReloadListener) supplier.get())));
    }

    public static <T extends ResourceManagerReloadListener & NamedReloadListener> T fromResourceManagerReloadListeners(ResourceLocation identifier, Collection<ResourceManagerReloadListener> reloadListeners) {
        return fromResourceManagerReloadListeners(identifier, (Supplier<Collection<ResourceManagerReloadListener>>) (() -> reloadListeners));
    }

    public static <T extends ResourceManagerReloadListener & NamedReloadListener> T fromResourceManagerReloadListeners(ResourceLocation identifier, Supplier<Collection<ResourceManagerReloadListener>> supplier) {
        return (T) (new ForwardingResourceManagerReloadListener(identifier, supplier));
    }
}