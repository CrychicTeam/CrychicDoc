package net.caffeinemc.mods.sodium.api.vertex.attributes.common;

import org.joml.Vector2f;
import org.lwjgl.system.MemoryUtil;

public class TextureAttribute {

    public static void put(long ptr, Vector2f vec) {
        put(ptr, vec.x(), vec.y());
    }

    public static void put(long ptr, float u, float v) {
        MemoryUtil.memPutFloat(ptr + 0L, u);
        MemoryUtil.memPutFloat(ptr + 4L, v);
    }

    public static Vector2f get(long ptr) {
        return new Vector2f(getU(ptr), getV(ptr));
    }

    public static float getU(long ptr) {
        return MemoryUtil.memGetFloat(ptr + 0L);
    }

    public static float getV(long ptr) {
        return MemoryUtil.memGetFloat(ptr + 4L);
    }
}