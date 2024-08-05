package net.minecraftforge.registries;

import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.IModBusEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RegisterEvent extends Event implements IModBusEvent {

    @NotNull
    private final ResourceKey<? extends Registry<?>> registryKey;

    @Nullable
    final ForgeRegistry<?> forgeRegistry;

    @Nullable
    private final Registry<?> vanillaRegistry;

    RegisterEvent(@NotNull ResourceKey<? extends Registry<?>> registryKey, @Nullable ForgeRegistry<?> forgeRegistry, @Nullable Registry<?> vanillaRegistry) {
        this.registryKey = registryKey;
        this.forgeRegistry = forgeRegistry;
        this.vanillaRegistry = vanillaRegistry;
    }

    public <T> void register(ResourceKey<? extends Registry<T>> registryKey, ResourceLocation name, Supplier<T> valueSupplier) {
        if (this.registryKey.equals(registryKey)) {
            if (this.forgeRegistry != null) {
                ((ForgeRegistry<Object>) this.forgeRegistry).register(name, valueSupplier.get());
            } else if (this.vanillaRegistry != null) {
                Registry.register(this.vanillaRegistry, name, valueSupplier.get());
            }
        }
    }

    public <T> void register(ResourceKey<? extends Registry<T>> registryKey, Consumer<RegisterEvent.RegisterHelper<T>> consumer) {
        if (this.registryKey.equals(registryKey)) {
            consumer.accept((RegisterEvent.RegisterHelper<Object>) (name, value) -> this.register(registryKey, name, () -> value));
        }
    }

    @NotNull
    public ResourceKey<? extends Registry<?>> getRegistryKey() {
        return this.registryKey;
    }

    @Nullable
    public <T> IForgeRegistry<T> getForgeRegistry() {
        return (IForgeRegistry<T>) this.forgeRegistry;
    }

    @Nullable
    public <T> Registry<T> getVanillaRegistry() {
        return (Registry<T>) this.vanillaRegistry;
    }

    public String toString() {
        return "RegisterEvent";
    }

    @FunctionalInterface
    public interface RegisterHelper<T> {

        default void register(String name, T value) {
            this.register(new ResourceLocation(ModLoadingContext.get().getActiveNamespace(), name), value);
        }

        default void register(ResourceKey<T> key, T value) {
            this.register(key.location(), value);
        }

        void register(ResourceLocation var1, T var2);
    }
}