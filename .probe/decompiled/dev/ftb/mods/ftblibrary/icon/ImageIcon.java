package dev.ftb.mods.ftblibrary.icon;

import com.google.common.base.Objects;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.ftb.mods.ftblibrary.math.PixelBuffer;
import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class ImageIcon extends Icon implements IResourceIcon {

    public static final ResourceLocation MISSING_IMAGE = new ResourceLocation("ftblibrary", "textures/gui/missing_image.png");

    public final ResourceLocation texture;

    public float minU;

    public float minV;

    public float maxU;

    public float maxV;

    public double tileSize;

    public Color4I color;

    public ImageIcon(ResourceLocation tex) {
        this.texture = tex;
        this.minU = 0.0F;
        this.minV = 0.0F;
        this.maxU = 1.0F;
        this.maxV = 1.0F;
        this.tileSize = 0.0;
        this.color = Color4I.WHITE;
    }

    public ImageIcon copy() {
        ImageIcon icon = new ImageIcon(this.texture);
        icon.minU = this.minU;
        icon.minV = this.minV;
        icon.maxU = this.maxU;
        icon.maxV = this.maxV;
        icon.tileSize = this.tileSize;
        return icon;
    }

    @Override
    protected void setProperties(IconProperties properties) {
        super.setProperties(properties);
        this.minU = (float) properties.getDouble("u0", (double) this.minU);
        this.minV = (float) properties.getDouble("v0", (double) this.minV);
        this.maxU = (float) properties.getDouble("u1", (double) this.maxU);
        this.maxV = (float) properties.getDouble("v1", (double) this.maxV);
        this.tileSize = properties.getDouble("tile_size", this.tileSize);
    }

    @OnlyIn(Dist.CLIENT)
    public void bindTexture() {
        TextureManager manager = Minecraft.getInstance().getTextureManager();
        AbstractTexture tex = manager.getTexture(this.texture);
        if (tex == null) {
            tex = new SimpleTexture(this.texture);
            manager.register(this.texture, tex);
        }
        RenderSystem.setShaderTexture(0, tex.getId());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void draw(GuiGraphics graphics, int x, int y, int w, int h) {
        this.bindTexture();
        if (this.tileSize <= 0.0) {
            GuiHelper.drawTexturedRect(graphics, x, y, w, h, this.color, this.minU, this.minV, this.maxU, this.maxV);
        } else {
            int r = this.color.redi();
            int g = this.color.greeni();
            int b = this.color.bluei();
            int a = this.color.alphai();
            Matrix4f m = graphics.pose().last().pose();
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder buffer = tesselator.getBuilder();
            RenderSystem.setShader(GameRenderer::m_172814_);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
            buffer.m_252986_(m, (float) x, (float) (y + h), 0.0F).color(r, g, b, a).uv((float) ((double) x / this.tileSize), (float) ((double) (y + h) / this.tileSize)).endVertex();
            buffer.m_252986_(m, (float) (x + w), (float) (y + h), 0.0F).color(r, g, b, a).uv((float) ((double) (x + w) / this.tileSize), (float) ((double) (y + h) / this.tileSize)).endVertex();
            buffer.m_252986_(m, (float) (x + w), (float) y, 0.0F).color(r, g, b, a).uv((float) ((double) (x + w) / this.tileSize), (float) ((double) y / this.tileSize)).endVertex();
            buffer.m_252986_(m, (float) x, (float) y, 0.0F).color(r, g, b, a).uv((float) ((double) x / this.tileSize), (float) ((double) y / this.tileSize)).endVertex();
            tesselator.end();
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(new Object[] { this.texture, this.minU, this.minV, this.maxU, this.maxV });
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else {
            return !(o instanceof ImageIcon img) ? false : this.texture.equals(img.texture) && this.minU == img.minU && this.minV == img.minV && this.maxU == img.maxU && this.maxV == img.maxV;
        }
    }

    public String toString() {
        return this.texture.toString();
    }

    public ImageIcon withColor(Color4I color) {
        ImageIcon icon = this.copy();
        icon.color = color;
        return icon;
    }

    public ImageIcon withTint(Color4I c) {
        return this.withColor(this.color.withTint(c));
    }

    public ImageIcon withUV(float u0, float v0, float u1, float v1) {
        ImageIcon icon = this.copy();
        icon.minU = u0;
        icon.minV = v0;
        icon.maxU = u1;
        icon.maxV = v1;
        return icon;
    }

    @Override
    public boolean hasPixelBuffer() {
        return true;
    }

    @Nullable
    @Override
    public PixelBuffer createPixelBuffer() {
        try {
            return PixelBuffer.from(((Resource) Minecraft.getInstance().getResourceManager().m_213713_(this.texture).orElseThrow()).open());
        } catch (Exception var2) {
            return null;
        }
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return this.texture;
    }
}