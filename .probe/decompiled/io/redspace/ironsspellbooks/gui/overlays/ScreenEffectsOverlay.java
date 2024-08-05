package io.redspace.ironsspellbooks.gui.overlays;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class ScreenEffectsOverlay implements IGuiOverlay {

    public static final ScreenEffectsOverlay instance = new ScreenEffectsOverlay();

    public static final ResourceLocation MAGIC_AURA_TEXTURE = new ResourceLocation("irons_spellbooks", "textures/gui/overlays/enchanted_ward_vignette.png");

    public static final ResourceLocation HEARTSTOP_TEXTURE = new ResourceLocation("irons_spellbooks", "textures/gui/overlays/heartstop.png");

    @Override
    public void render(ForgeGui gui, GuiGraphics guiHelper, float partialTick, int screenWidth, int screenHeight) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            if (player.m_21023_(MobEffectRegistry.HEARTSTOP.get())) {
                setupRenderer(1.0F, 0.0F, 0.0F, 0.25F, HEARTSTOP_TEXTURE);
                renderOverlay(HEARTSTOP_TEXTURE, 0.5F, 1.0F, 1.0F, 0.5F, screenWidth, screenHeight);
            }
        }
    }

    private static void setupRenderer(float r, float g, float b, float a, ResourceLocation texture) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(r, b, g, a);
        RenderSystem.setShaderTexture(0, texture);
    }

    private static void renderOverlay(ResourceLocation texture, float r, float g, float b, float a, int screenWidth, int screenHeight) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShaderColor(r, g, b, a);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.m_5483_(0.0, (double) screenHeight, -90.0).uv(0.0F, 1.0F).endVertex();
        bufferbuilder.m_5483_((double) screenWidth, (double) screenHeight, -90.0).uv(1.0F, 1.0F).endVertex();
        bufferbuilder.m_5483_((double) screenWidth, 0.0, -90.0).uv(1.0F, 0.0F).endVertex();
        bufferbuilder.m_5483_(0.0, 0.0, -90.0).uv(0.0F, 0.0F).endVertex();
        tesselator.end();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.defaultBlendFunc();
    }
}