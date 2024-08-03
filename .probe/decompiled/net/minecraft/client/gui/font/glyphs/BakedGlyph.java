package net.minecraft.client.gui.font.glyphs;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.GlyphRenderTypes;
import net.minecraft.client.renderer.RenderType;
import org.joml.Matrix4f;

public class BakedGlyph {

    private final GlyphRenderTypes renderTypes;

    private final float u0;

    private final float u1;

    private final float v0;

    private final float v1;

    private final float left;

    private final float right;

    private final float up;

    private final float down;

    public BakedGlyph(GlyphRenderTypes glyphRenderTypes0, float float1, float float2, float float3, float float4, float float5, float float6, float float7, float float8) {
        this.renderTypes = glyphRenderTypes0;
        this.u0 = float1;
        this.u1 = float2;
        this.v0 = float3;
        this.v1 = float4;
        this.left = float5;
        this.right = float6;
        this.up = float7;
        this.down = float8;
    }

    public void render(boolean boolean0, float float1, float float2, Matrix4f matrixF3, VertexConsumer vertexConsumer4, float float5, float float6, float float7, float float8, int int9) {
        int $$10 = 3;
        float $$11 = float1 + this.left;
        float $$12 = float1 + this.right;
        float $$13 = this.up - 3.0F;
        float $$14 = this.down - 3.0F;
        float $$15 = float2 + $$13;
        float $$16 = float2 + $$14;
        float $$17 = boolean0 ? 1.0F - 0.25F * $$13 : 0.0F;
        float $$18 = boolean0 ? 1.0F - 0.25F * $$14 : 0.0F;
        vertexConsumer4.vertex(matrixF3, $$11 + $$17, $$15, 0.0F).color(float5, float6, float7, float8).uv(this.u0, this.v0).uv2(int9).endVertex();
        vertexConsumer4.vertex(matrixF3, $$11 + $$18, $$16, 0.0F).color(float5, float6, float7, float8).uv(this.u0, this.v1).uv2(int9).endVertex();
        vertexConsumer4.vertex(matrixF3, $$12 + $$18, $$16, 0.0F).color(float5, float6, float7, float8).uv(this.u1, this.v1).uv2(int9).endVertex();
        vertexConsumer4.vertex(matrixF3, $$12 + $$17, $$15, 0.0F).color(float5, float6, float7, float8).uv(this.u1, this.v0).uv2(int9).endVertex();
    }

    public void renderEffect(BakedGlyph.Effect bakedGlyphEffect0, Matrix4f matrixF1, VertexConsumer vertexConsumer2, int int3) {
        vertexConsumer2.vertex(matrixF1, bakedGlyphEffect0.x0, bakedGlyphEffect0.y0, bakedGlyphEffect0.depth).color(bakedGlyphEffect0.r, bakedGlyphEffect0.g, bakedGlyphEffect0.b, bakedGlyphEffect0.a).uv(this.u0, this.v0).uv2(int3).endVertex();
        vertexConsumer2.vertex(matrixF1, bakedGlyphEffect0.x1, bakedGlyphEffect0.y0, bakedGlyphEffect0.depth).color(bakedGlyphEffect0.r, bakedGlyphEffect0.g, bakedGlyphEffect0.b, bakedGlyphEffect0.a).uv(this.u0, this.v1).uv2(int3).endVertex();
        vertexConsumer2.vertex(matrixF1, bakedGlyphEffect0.x1, bakedGlyphEffect0.y1, bakedGlyphEffect0.depth).color(bakedGlyphEffect0.r, bakedGlyphEffect0.g, bakedGlyphEffect0.b, bakedGlyphEffect0.a).uv(this.u1, this.v1).uv2(int3).endVertex();
        vertexConsumer2.vertex(matrixF1, bakedGlyphEffect0.x0, bakedGlyphEffect0.y1, bakedGlyphEffect0.depth).color(bakedGlyphEffect0.r, bakedGlyphEffect0.g, bakedGlyphEffect0.b, bakedGlyphEffect0.a).uv(this.u1, this.v0).uv2(int3).endVertex();
    }

    public RenderType renderType(Font.DisplayMode fontDisplayMode0) {
        return this.renderTypes.select(fontDisplayMode0);
    }

    public static class Effect {

        protected final float x0;

        protected final float y0;

        protected final float x1;

        protected final float y1;

        protected final float depth;

        protected final float r;

        protected final float g;

        protected final float b;

        protected final float a;

        public Effect(float float0, float float1, float float2, float float3, float float4, float float5, float float6, float float7, float float8) {
            this.x0 = float0;
            this.y0 = float1;
            this.x1 = float2;
            this.y1 = float3;
            this.depth = float4;
            this.r = float5;
            this.g = float6;
            this.b = float7;
            this.a = float8;
        }
    }
}