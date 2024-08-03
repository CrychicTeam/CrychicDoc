package me.fengming.renderjs.core.objects;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import me.fengming.renderjs.core.RenderObject;
import net.minecraft.nbt.CompoundTag;
import org.joml.Matrix4f;

@RemapPrefixForJS("rjs$")
public class Lines extends Draw {

    protected float lineWidth;

    public Lines(float[] vertices, RenderObject.ObjectType type) {
        super(vertices, type);
    }

    public void rjs$setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    @Override
    public void loadInner(CompoundTag object) {
        if (object.contains("line_width")) {
            this.rjs$setLineWidth(object.getFloat("line_width"));
        }
    }

    @Override
    public void renderInner() {
        Matrix4f matrix4f = this.poseStack.last().pose();
        Tesselator tesselator = RenderSystem.renderThreadTesselator();
        BufferBuilder builder = tesselator.getBuilder();
        RenderSystem.lineWidth(this.lineWidth);
        builder.begin(this.type.getMode(), this.texture ? DefaultVertexFormat.POSITION_COLOR_TEX : DefaultVertexFormat.POSITION_COLOR);
        if (!this.verticesColor) {
            if (this.texture) {
                for (int i = 0; i < this.vertices.length; i += 10) {
                    builder.m_252986_(matrix4f, this.vertices[i], this.vertices[i + 1], this.vertices[i + 2]).color(this.r, this.g, this.b, this.a).uv(this.vertices[i + 3], this.vertices[i + 4]).endVertex();
                    builder.m_252986_(matrix4f, this.vertices[i + 5], this.vertices[i + 6], this.vertices[i + 7]).color(this.r, this.g, this.b, this.a).uv(this.vertices[i + 8], this.vertices[i + 9]).endVertex();
                }
            } else {
                for (int i = 0; i < this.vertices.length; i += 6) {
                    builder.m_252986_(matrix4f, this.vertices[i], this.vertices[i + 1], this.vertices[i + 2]).color(this.r, this.g, this.b, this.a).endVertex();
                    builder.m_252986_(matrix4f, this.vertices[i + 3], this.vertices[i + 4], this.vertices[i + 5]).color(this.r, this.g, this.b, this.a).endVertex();
                }
            }
        } else if (this.texture) {
            for (int i = 0; i < this.vertices.length; i += 16) {
                builder.m_252986_(matrix4f, this.vertices[i], this.vertices[i + 1], this.vertices[i + 2]).color(this.vertices[i + 3], this.vertices[i + 4], this.vertices[i + 5], this.a).uv(this.vertices[i + 6], this.vertices[i + 7]).endVertex();
                builder.m_252986_(matrix4f, this.vertices[i + 8], this.vertices[i + 9], this.vertices[i + 10]).color(this.vertices[i + 11], this.vertices[i + 12], this.vertices[i + 13], this.a).uv(this.vertices[i + 14], this.vertices[i + 15]).endVertex();
            }
        } else {
            for (int i = 0; i < this.vertices.length; i += 12) {
                builder.m_252986_(matrix4f, this.vertices[i], this.vertices[i + 1], this.vertices[i + 2]).color(this.vertices[i + 3], this.vertices[i + 4], this.vertices[i + 5], this.a).endVertex();
                builder.m_252986_(matrix4f, this.vertices[i + 6], this.vertices[i + 7], this.vertices[i + 8]).color(this.vertices[i + 9], this.vertices[i + 10], this.vertices[i + 11], this.a).endVertex();
            }
        }
        tesselator.end();
    }
}