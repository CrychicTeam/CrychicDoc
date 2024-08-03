package dev.xkmc.l2library.serial.config;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;

public class BaseConfigType<T extends BaseConfig> {

    public final Class<T> cls;

    public final String id;

    public final PacketHandlerWithConfig parent;

    final Map<ResourceLocation, T> configs = new HashMap();

    protected BaseConfigType(PacketHandlerWithConfig parent, String id, Class<T> cls) {
        this.parent = parent;
        this.id = id;
        this.cls = cls;
    }

    public void beforeReload() {
        this.configs.clear();
    }

    public void afterReload() {
    }
}