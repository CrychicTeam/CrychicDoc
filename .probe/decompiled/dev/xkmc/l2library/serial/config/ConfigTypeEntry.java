package dev.xkmc.l2library.serial.config;

import dev.xkmc.l2serial.util.Wrappers;
import java.util.Collection;
import net.minecraft.resources.ResourceLocation;

public record ConfigTypeEntry<T extends BaseConfig>(PacketHandlerWithConfig channel, String name, Class<T> cls) {

    public ConfigTypeEntry(PacketHandlerWithConfig channel, String name, Class<T> cls) {
        this.channel = channel;
        this.name = name;
        this.cls = cls;
        channel.addCachedConfig(name, cls);
    }

    public String asPath(ResourceLocation rl) {
        return "data/" + rl.getNamespace() + "/" + this.channel.config_path + "/" + this.name + "/" + rl.getPath();
    }

    public T getMerged() {
        MergedConfigType<T> type = (MergedConfigType<T>) Wrappers.cast((BaseConfigType) this.channel.types.get(this.name));
        return type.load();
    }

    public Collection<T> getAll() {
        MergedConfigType<T> type = (MergedConfigType<T>) Wrappers.cast((BaseConfigType) this.channel.types.get(this.name));
        return type.configs.values();
    }

    public T getEntry(ResourceLocation id) {
        MergedConfigType<T> type = (MergedConfigType<T>) Wrappers.cast((BaseConfigType) this.channel.types.get(this.name));
        return (T) type.configs.get(id);
    }
}