package me.jellysquid.mods.sodium.client.gl.array;

import me.jellysquid.mods.sodium.client.gl.GlObject;
import org.lwjgl.opengl.GL30C;

public class GlVertexArray extends GlObject {

    public static final int NULL_ARRAY_ID = 0;

    public GlVertexArray() {
        this.setHandle(GL30C.glGenVertexArrays());
    }
}