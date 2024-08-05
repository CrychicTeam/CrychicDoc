package dev.latvian.mods.kubejs.client.painter;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.latvian.mods.kubejs.client.ClientEventJS;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class PaintEventJS extends ClientEventJS {

    public final Minecraft mc;

    public final Font font;

    public final GuiGraphics graphics;

    public final PoseStack matrices;

    public final Tesselator tesselator;

    public final BufferBuilder buffer;

    public final float delta;

    public final Screen screen;

    public PaintEventJS(Minecraft m, GuiGraphics g, float d, @Nullable Screen s) {
        this.mc = m;
        this.font = this.mc.font;
        this.graphics = g;
        this.matrices = g.pose();
        this.tesselator = Tesselator.getInstance();
        this.buffer = this.tesselator.getBuilder();
        this.delta = d;
        this.screen = s;
    }

    public void push() {
        this.matrices.pushPose();
    }

    public void pop() {
        this.matrices.popPose();
    }

    public void translate(double x, double y, double z) {
        this.matrices.translate(x, y, z);
    }

    public void scale(float x, float y, float z) {
        this.matrices.scale(x, y, z);
    }

    public void multiply(Quaternionf q) {
        this.matrices.mulPose(q);
    }

    public void multiplyWithMatrix(Matrix4f m) {
        this.matrices.mulPoseMatrix(m);
    }

    public Matrix4f getMatrix() {
        return this.matrices.last().pose();
    }

    public void bindTextureForSetup(ResourceLocation tex) {
        this.mc.getTextureManager().bindForSetup(tex);
    }

    public void setShaderColor(float r, float g, float b, float a) {
        RenderSystem.setShaderColor(r, g, b, a);
    }

    public void resetShaderColor() {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void setShaderTexture(ResourceLocation tex) {
        RenderSystem.setShaderTexture(0, tex);
    }

    public void begin(VertexFormat.Mode type, VertexFormat format) {
        this.buffer.begin(type, format);
    }

    public void beginQuads(VertexFormat format) {
        this.begin(VertexFormat.Mode.QUADS, format);
    }

    public void beginQuads(boolean texture) {
        this.beginQuads(texture ? DefaultVertexFormat.POSITION_COLOR_TEX : DefaultVertexFormat.POSITION_COLOR);
    }

    public void vertex(Matrix4f m, float x, float y, float z, int col) {
        this.buffer.m_252986_(m, x, y, z).color(col >> 16 & 0xFF, col >> 8 & 0xFF, col & 0xFF, col >> 24 & 0xFF).endVertex();
    }

    public void vertex(Matrix4f m, float x, float y, float z, int col, float u, float v) {
        this.buffer.m_252986_(m, x, y, z).color(col >> 16 & 0xFF, col >> 8 & 0xFF, col & 0xFF, col >> 24 & 0xFF).uv(u, v).endVertex();
    }

    public void end() {
        this.tesselator.end();
    }

    public void setShaderInstance(Supplier<ShaderInstance> shader) {
        RenderSystem.setShader(shader);
    }

    public void setPositionColorShader() {
        RenderSystem.setShader(GameRenderer::m_172811_);
    }

    public void setPositionColorTextureShader() {
        RenderSystem.setShader(GameRenderer::m_172814_);
    }

    public void blend(boolean enabled) {
        if (enabled) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
        } else {
            RenderSystem.disableBlend();
        }
    }
}