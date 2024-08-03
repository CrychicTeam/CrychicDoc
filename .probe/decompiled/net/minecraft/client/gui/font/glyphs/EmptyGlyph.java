package net.minecraft.client.gui.font.glyphs;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.font.GlyphRenderTypes;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class EmptyGlyph extends BakedGlyph {

    public static final EmptyGlyph INSTANCE = new EmptyGlyph();

    public EmptyGlyph() {
        super(GlyphRenderTypes.createForColorTexture(new ResourceLocation("")), 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
    }

    @Override
    public void render(boolean boolean0, float float1, float float2, Matrix4f matrixF3, VertexConsumer vertexConsumer4, float float5, float float6, float float7, float float8, int int9) {
    }
}