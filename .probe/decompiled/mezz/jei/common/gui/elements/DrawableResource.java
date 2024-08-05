package mezz.jei.common.gui.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class DrawableResource implements IDrawableStatic {

    private final ResourceLocation resourceLocation;

    private final int textureWidth;

    private final int textureHeight;

    private final int u;

    private final int v;

    private final int width;

    private final int height;

    private final int paddingTop;

    private final int paddingBottom;

    private final int paddingLeft;

    private final int paddingRight;

    public DrawableResource(ResourceLocation resourceLocation, int u, int v, int width, int height, int paddingTop, int paddingBottom, int paddingLeft, int paddingRight, int textureWidth, int textureHeight) {
        this.resourceLocation = resourceLocation;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.u = u;
        this.v = v;
        this.width = width;
        this.height = height;
        this.paddingTop = paddingTop;
        this.paddingBottom = paddingBottom;
        this.paddingLeft = paddingLeft;
        this.paddingRight = paddingRight;
    }

    @Override
    public int getWidth() {
        return this.width + this.paddingLeft + this.paddingRight;
    }

    @Override
    public int getHeight() {
        return this.height + this.paddingTop + this.paddingBottom;
    }

    @Override
    public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
        this.draw(guiGraphics, xOffset, yOffset, 0, 0, 0, 0);
    }

    @Override
    public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset, int maskTop, int maskBottom, int maskLeft, int maskRight) {
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderTexture(0, this.resourceLocation);
        int x = xOffset + this.paddingLeft + maskLeft;
        int y = yOffset + this.paddingTop + maskTop;
        int u = this.u + maskLeft;
        int v = this.v + maskTop;
        int width = this.width - maskRight - maskLeft;
        int height = this.height - maskBottom - maskTop;
        float f = 1.0F / (float) this.textureWidth;
        float f1 = 1.0F / (float) this.textureHeight;
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        Matrix4f matrix = guiGraphics.pose().last().pose();
        bufferbuilder.m_252986_(matrix, (float) x, (float) (y + height), 0.0F).uv((float) u * f, ((float) v + (float) height) * f1).endVertex();
        bufferbuilder.m_252986_(matrix, (float) (x + width), (float) (y + height), 0.0F).uv(((float) u + (float) width) * f, ((float) v + (float) height) * f1).endVertex();
        bufferbuilder.m_252986_(matrix, (float) (x + width), (float) y, 0.0F).uv(((float) u + (float) width) * f, (float) v * f1).endVertex();
        bufferbuilder.m_252986_(matrix, (float) x, (float) y, 0.0F).uv((float) u * f, (float) v * f1).endVertex();
        tessellator.end();
    }
}