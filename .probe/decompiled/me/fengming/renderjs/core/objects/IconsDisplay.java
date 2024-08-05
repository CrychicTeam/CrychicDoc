package me.fengming.renderjs.core.objects;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import me.fengming.renderjs.core.RenderObject;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

@RemapPrefixForJS("rjs$")
public class IconsDisplay extends RenderObject {

    protected ResourceLocation location;

    protected float textureWidth;

    protected float textureHeight;

    protected float width;

    protected float height;

    protected float u = 0.0F;

    protected float v = 0.0F;

    public IconsDisplay(float[] vertices, RenderObject.ObjectType type) {
        super(vertices, type);
    }

    public void rjs$setLocation(ResourceLocation location) {
        this.location = location;
    }

    public void rjs$setTextureWidth(float textureWidth) {
        this.textureWidth = textureWidth;
    }

    public void rjs$setTextureHeight(float textureHeight) {
        this.textureHeight = textureHeight;
    }

    public void rjs$setWidth(float width) {
        this.width = width;
    }

    public void rjs$setHeight(float height) {
        this.height = height;
    }

    public void rjs$setU(float u) {
        this.u = u;
    }

    public void rjs$setV(float v) {
        this.v = v;
    }

    @Override
    public void loadInner(CompoundTag object) {
        if (object.contains("icon")) {
            this.rjs$setLocation(new ResourceLocation(object.getString("icon")));
        } else {
            ConsoleJS.CLIENT.error("Missing a necessary key: icon");
            this.broken = true;
        }
        if (object.contains("texture_width")) {
            this.rjs$setTextureWidth(object.getFloat("texture_width"));
        } else {
            ConsoleJS.CLIENT.error("Missing a necessary key: texture_width");
            this.broken = true;
        }
        if (object.contains("texture_height")) {
            this.rjs$setTextureHeight(object.getFloat("texture_height"));
        } else {
            ConsoleJS.CLIENT.error("Missing a necessary key: texture_height");
            this.broken = true;
        }
        if (object.contains("width")) {
            this.rjs$setWidth(object.getFloat("width"));
        } else {
            this.rjs$setWidth(this.textureWidth);
        }
        if (object.contains("height")) {
            this.rjs$setHeight(object.getFloat("height"));
        } else {
            this.rjs$setHeight(this.textureHeight);
        }
        if (object.contains("u")) {
            this.rjs$setU(object.getFloat("u"));
        }
        if (object.contains("v")) {
            this.rjs$setV(object.getFloat("v"));
        }
    }

    @Override
    public void rjs$render() {
        RenderSystem.setShaderTexture(0, this.location);
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        super.rjs$render();
    }

    @Override
    public void renderInner() {
        float widthProportion = 1.0F / this.textureWidth;
        float heightProportion = 1.0F / this.textureHeight;
        Matrix4f matrix4f = this.poseStack.last().pose();
        for (int i = 0; i < this.vertices.length; i += 3) {
            BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferbuilder.m_252986_(matrix4f, this.vertices[i], this.vertices[i + 1] + this.height, this.vertices[i + 2]).uv(this.u * widthProportion, (this.v + this.height) * heightProportion).color(0.0F, 0.0F, 0.0F, 1.0F).endVertex();
            bufferbuilder.m_252986_(matrix4f, this.vertices[i] + this.width, this.vertices[i + 1] + this.height, this.vertices[i + 2]).uv((this.u + this.width) * widthProportion, (this.v + this.height) * heightProportion).color(0.0F, 0.0F, 0.0F, 1.0F).endVertex();
            bufferbuilder.m_252986_(matrix4f, this.vertices[i] + this.width, this.vertices[i + 1], this.vertices[i + 2]).uv((this.u + this.width) * widthProportion, this.v * heightProportion).color(0.0F, 0.0F, 0.0F, 1.0F).endVertex();
            bufferbuilder.m_252986_(matrix4f, this.vertices[i], this.vertices[i + 1], this.vertices[i + 2]).uv(this.u * widthProportion, this.v * heightProportion).color(0.0F, 0.0F, 0.0F, 1.0F).endVertex();
            BufferUploader.draw(bufferbuilder.end());
        }
    }
}