package me.jellysquid.mods.sodium.client.gl.tessellation;

import me.jellysquid.mods.sodium.client.gl.device.CommandList;

public interface GlTessellation {

    void delete(CommandList var1);

    void bind(CommandList var1);

    void unbind(CommandList var1);

    GlPrimitiveType getPrimitiveType();
}