package net.caffeinemc.mods.sodium.api.vertex.format.common;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.caffeinemc.mods.sodium.api.vertex.attributes.common.ColorAttribute;
import net.caffeinemc.mods.sodium.api.vertex.attributes.common.LightAttribute;
import net.caffeinemc.mods.sodium.api.vertex.attributes.common.PositionAttribute;
import net.caffeinemc.mods.sodium.api.vertex.attributes.common.TextureAttribute;
import net.caffeinemc.mods.sodium.api.vertex.format.VertexFormatDescription;
import net.caffeinemc.mods.sodium.api.vertex.format.VertexFormatRegistry;

public final class GlyphVertex {

    public static final VertexFormatDescription FORMAT = VertexFormatRegistry.instance().get(DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP);

    public static final int STRIDE = 28;

    private static final int OFFSET_POSITION = 0;

    private static final int OFFSET_COLOR = 12;

    private static final int OFFSET_TEXTURE = 16;

    private static final int OFFSET_LIGHT = 24;

    public static void put(long ptr, float x, float y, float z, int color, float u, float v, int light) {
        PositionAttribute.put(ptr + 0L, x, y, z);
        ColorAttribute.set(ptr + 12L, color);
        TextureAttribute.put(ptr + 16L, u, v);
        LightAttribute.set(ptr + 24L, light);
    }
}