package com.craisinlord.integrated_api.modinit.registry.forge;

import com.craisinlord.integrated_api.modinit.registry.RegistryEntries;
import com.craisinlord.integrated_api.modinit.registry.RegistryEntry;
import com.craisinlord.integrated_api.modinit.registry.ResourcefulRegistry;
import java.util.Collection;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

public class ForgeResourcefulRegistry<T> implements ResourcefulRegistry<T> {

    private final DeferredRegister<T> register;

    private final RegistryEntries<T> entries = new RegistryEntries<>();

    public ForgeResourcefulRegistry(ResourceKey<? extends Registry<T>> registry, String id) {
        this.register = DeferredRegister.create(registry, id);
    }

    @Override
    public <I extends T> RegistryEntry<I> register(String id, Supplier<I> supplier) {
        return this.entries.add(new ForgeRegistryEntry<>(this.register.register(id, supplier)));
    }

    @Override
    public Collection<RegistryEntry<T>> getEntries() {
        return this.entries.getEntries();
    }

    @Override
    public void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        this.register.register(bus);
    }
}