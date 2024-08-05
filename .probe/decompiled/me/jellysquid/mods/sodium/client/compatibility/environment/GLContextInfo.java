package me.jellysquid.mods.sodium.client.compatibility.environment;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11C;

public record GLContextInfo(String vendor, String renderer, String version) {

    @Nullable
    public static GLContextInfo create() {
        String vendor = GL11C.glGetString(7936);
        String renderer = GL11C.glGetString(7937);
        String version = GL11C.glGetString(7938);
        return vendor != null && renderer != null && version != null ? new GLContextInfo(vendor, renderer, version) : null;
    }
}