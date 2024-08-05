package mezz.jei.common.gui.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.common.Constants;
import mezz.jei.common.gui.textures.JeiSpriteUploader;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class DrawableSprite implements IDrawableStatic {

    private final JeiSpriteUploader spriteUploader;

    private final ResourceLocation location;

    private final int width;

    private final int height;

    private int trimLeft;

    private int trimRight;

    private int trimTop;

    private int trimBottom;

    public DrawableSprite(JeiSpriteUploader spriteUploader, ResourceLocation location, int width, int height) {
        this.spriteUploader = spriteUploader;
        this.location = location;
        this.width = width;
        this.height = height;
    }

    public DrawableSprite trim(int left, int right, int top, int bottom) {
        this.trimLeft = left;
        this.trimRight = right;
        this.trimTop = top;
        this.trimBottom = bottom;
        return this;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
        this.draw(guiGraphics, xOffset, yOffset, 0, 0, 0, 0);
    }

    @Override
    public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset, int maskTop, int maskBottom, int maskLeft, int maskRight) {
        TextureAtlasSprite sprite = this.spriteUploader.getSprite(this.location);
        int textureWidth = this.width;
        int textureHeight = this.height;
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderTexture(0, Constants.LOCATION_JEI_GUI_TEXTURE_ATLAS);
        maskTop += this.trimTop;
        maskBottom += this.trimBottom;
        maskLeft += this.trimLeft;
        maskRight += this.trimRight;
        int x = xOffset + maskLeft;
        int y = yOffset + maskTop;
        int width = textureWidth - maskRight - maskLeft;
        int height = textureHeight - maskBottom - maskTop;
        float uSize = sprite.getU1() - sprite.getU0();
        float vSize = sprite.getV1() - sprite.getV0();
        float minU = sprite.getU0() + uSize * ((float) maskLeft / (float) textureWidth);
        float minV = sprite.getV0() + vSize * ((float) maskTop / (float) textureHeight);
        float maxU = sprite.getU1() - uSize * ((float) maskRight / (float) textureWidth);
        float maxV = sprite.getV1() - vSize * ((float) maskBottom / (float) textureHeight);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        Matrix4f matrix = guiGraphics.pose().last().pose();
        bufferBuilder.m_252986_(matrix, (float) x, (float) (y + height), 0.0F).uv(minU, maxV).endVertex();
        bufferBuilder.m_252986_(matrix, (float) (x + width), (float) (y + height), 0.0F).uv(maxU, maxV).endVertex();
        bufferBuilder.m_252986_(matrix, (float) (x + width), (float) y, 0.0F).uv(maxU, minV).endVertex();
        bufferBuilder.m_252986_(matrix, (float) x, (float) y, 0.0F).uv(minU, minV).endVertex();
        tessellator.end();
    }
}