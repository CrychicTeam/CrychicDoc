package dev.shadowsoffire.placebo.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.GenericEvent;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Deprecated
public class RegistryEvent<T> extends GenericEvent<T> implements IModBusEvent {

    RegistryEvent(Class<T> clazz) {
        super(clazz);
    }

    public static class Register<T> extends RegistryEvent<T> {

        private final IForgeRegistry<T> registry;

        private final ResourceLocation name;

        private final RegistryEvent.RegistryWrapper<T> wrapper;

        public Register(Class<T> clazz, IForgeRegistry<T> registry) {
            super(clazz);
            this.name = registry.getRegistryKey().location();
            this.registry = registry;
            this.wrapper = new RegistryEvent.RegistryWrapper<>(registry);
        }

        public RegistryEvent.RegistryWrapper<T> getRegistry() {
            return this.wrapper;
        }

        public IForgeRegistry<T> getForgeRegistry() {
            return this.registry;
        }

        public ResourceLocation getName() {
            return this.name;
        }

        public String toString() {
            return "RegistryEvent.Register<" + this.getName() + ">";
        }
    }

    public static class RegistryWrapper<T> {

        private final IForgeRegistry<T> reg;

        public RegistryWrapper(IForgeRegistry<T> reg) {
            this.reg = reg;
        }

        public void register(T object, String id) {
            this.reg.register(id, object);
        }

        public void register(T object, ResourceLocation id) {
            this.reg.register(id, object);
        }

        public void registerAll(Object... arr) {
            for (int i = 0; i < arr.length; i += 2) {
                T object = (T) arr[i];
                Object id = arr[i + 1];
                if (id instanceof String s) {
                    this.reg.register(s, object);
                } else {
                    if (!(id instanceof ResourceLocation r)) {
                        throw new RuntimeException();
                    }
                    this.reg.register(r, object);
                }
            }
        }
    }
}