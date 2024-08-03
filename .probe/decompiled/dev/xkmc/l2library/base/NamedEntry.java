package dev.xkmc.l2library.base;

import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class NamedEntry<T extends NamedEntry<T>> {

    private final L2Registrate.RegistryInstance<T> registry;

    private String desc = null;

    private ResourceLocation idCache;

    public NamedEntry(L2Registrate.RegistryInstance<T> registry) {
        this.registry = registry;
    }

    @NotNull
    public String getDescriptionId() {
        if (this.desc != null) {
            return this.desc;
        } else {
            ResourceLocation rl = this.getRegistryName();
            ResourceLocation reg = this.registry.get().getRegistryName();
            this.desc = reg.getPath() + "." + rl.getNamespace() + "." + rl.getPath();
            return this.desc;
        }
    }

    public MutableComponent getDesc() {
        return Component.translatable(this.getDescriptionId());
    }

    public ResourceLocation getRegistryName() {
        if (this.idCache == null) {
            this.idCache = this.registry.get().getKey(this.getThis());
            assert this.idCache != null;
        }
        return this.idCache;
    }

    public String getID() {
        return this.getRegistryName().toString();
    }

    public T getThis() {
        return (T) Wrappers.cast(this);
    }
}