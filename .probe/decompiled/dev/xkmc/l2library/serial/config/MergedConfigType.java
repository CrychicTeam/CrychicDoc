package dev.xkmc.l2library.serial.config;

import net.minecraft.resources.ResourceLocation;

public class MergedConfigType<T extends BaseConfig> extends BaseConfigType<T> {

    private T result;

    MergedConfigType(PacketHandlerWithConfig parent, String id, Class<T> cls) {
        super(parent, id, cls);
    }

    T load() {
        if (this.result != null) {
            return this.result;
        } else {
            this.result = new ConfigMerger<T>(this.cls).apply(this.configs.values());
            this.result.id = new ResourceLocation(this.parent.CHANNEL_NAME.getNamespace(), this.id);
            return this.result;
        }
    }

    @Override
    public void afterReload() {
        this.result = null;
        if (this.cls.isAnnotationPresent(ConfigLoadOnStart.class)) {
            this.load();
        }
    }
}