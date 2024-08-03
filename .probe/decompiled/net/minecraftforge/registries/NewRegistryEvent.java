package net.minecraftforge.registries;

import com.mojang.logging.LogUtils;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class NewRegistryEvent extends Event implements IModBusEvent {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final List<NewRegistryEvent.RegistryData<?>> registries = new ArrayList();

    public <V> Supplier<IForgeRegistry<V>> create(RegistryBuilder<V> builder) {
        return this.create(builder, null);
    }

    public <V> Supplier<IForgeRegistry<V>> create(RegistryBuilder<V> builder, @Nullable Consumer<IForgeRegistry<V>> onFill) {
        NewRegistryEvent.RegistryHolder<V> registryHolder = new NewRegistryEvent.RegistryHolder<>();
        this.registries.add(new NewRegistryEvent.RegistryData<>(builder, registryHolder, onFill));
        return registryHolder;
    }

    void fill() {
        RuntimeException aggregate = new RuntimeException();
        Map<RegistryBuilder<?>, IForgeRegistry<?>> builtRegistries = new IdentityHashMap();
        if (BuiltInRegistries.REGISTRY instanceof MappedRegistry<?> rootRegistry) {
            rootRegistry.unfreeze();
        }
        for (NewRegistryEvent.RegistryData<?> data : this.registries) {
            try {
                this.buildRegistry(builtRegistries, data);
            } catch (Throwable var6) {
                aggregate.addSuppressed(var6);
                return;
            }
        }
        if (BuiltInRegistries.REGISTRY instanceof MappedRegistry<?> rootRegistry) {
            rootRegistry.freeze();
        }
        if (aggregate.getSuppressed().length > 0) {
            LOGGER.error(LogUtils.FATAL_MARKER, "Failed to create some forge registries, see suppressed exceptions for details", aggregate);
        }
    }

    private <T> void buildRegistry(Map<RegistryBuilder<?>, IForgeRegistry<?>> builtRegistries, NewRegistryEvent.RegistryData<T> data) {
        RegistryBuilder<T> builder = data.builder;
        IForgeRegistry<T> registry = builder.create();
        builtRegistries.put(builder, registry);
        if (builder.getHasWrapper() && !BuiltInRegistries.REGISTRY.containsKey(registry.getRegistryName())) {
            RegistryManager.registerToRootRegistry((ForgeRegistry<T>) registry);
        }
        data.registryHolder.registry = registry;
        if (data.onFill != null) {
            data.onFill.accept(registry);
        }
    }

    public String toString() {
        return "RegistryEvent.NewRegistry";
    }

    private static record RegistryData<V>(RegistryBuilder<V> builder, NewRegistryEvent.RegistryHolder<V> registryHolder, Consumer<IForgeRegistry<V>> onFill) {
    }

    private static class RegistryHolder<V> implements Supplier<IForgeRegistry<V>> {

        IForgeRegistry<V> registry = null;

        public IForgeRegistry<V> get() {
            return this.registry;
        }
    }
}