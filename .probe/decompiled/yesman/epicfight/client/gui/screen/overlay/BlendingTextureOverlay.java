package yesman.epicfight.client.gui.screen.overlay;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BlendingTextureOverlay extends OverlayManager.Overlay {

    public ResourceLocation texture;

    private boolean isAlive = true;

    public BlendingTextureOverlay(ResourceLocation texture) {
        this.texture = texture;
    }

    public void remove() {
        this.isAlive = false;
    }

    @Override
    public boolean render(int xResolution, int yResolution) {
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderTexture(0, this.texture);
        GlStateManager._enableBlend();
        GlStateManager._disableDepthTest();
        GlStateManager._blendFunc(770, 771);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.m_5483_(0.0, 0.0, 1.0).uv(0.0F, 0.0F).endVertex();
        bufferbuilder.m_5483_(0.0, (double) yResolution, 1.0).uv(0.0F, 1.0F).endVertex();
        bufferbuilder.m_5483_((double) xResolution, (double) yResolution, 1.0).uv(1.0F, 1.0F).endVertex();
        bufferbuilder.m_5483_((double) xResolution, 0.0, 1.0).uv(1.0F, 0.0F).endVertex();
        tessellator.end();
        return !this.isAlive;
    }
}