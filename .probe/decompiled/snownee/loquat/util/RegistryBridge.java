package snownee.loquat.util;

import java.util.Optional;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

public class RegistryBridge<T> {

    private final IForgeRegistry<T> registry;

    public RegistryBridge(IForgeRegistry<T> registry) {
        this.registry = registry;
    }

    public T get(ResourceLocation id) {
        return this.registry.getValue(id);
    }

    public ResourceLocation getKey(T value) {
        return this.registry.getKey(value);
    }

    public Optional<T> getOptional(ResourceLocation id) {
        return this.registry.containsKey(id) ? Optional.ofNullable(this.registry.getValue(id)) : Optional.empty();
    }

    public void register(ResourceLocation id, T value) {
        this.registry.register(id, value);
    }

    public ResourceKey<Registry<T>> getRegistryKey() {
        return this.registry.getRegistryKey();
    }
}