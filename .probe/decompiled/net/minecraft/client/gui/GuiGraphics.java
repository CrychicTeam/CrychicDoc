package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Divisor;
import it.unimi.dsi.fastutil.ints.IntIterator;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.CrashReportDetail;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector2ic;

public class GuiGraphics {

    public static final float MAX_GUI_Z = 10000.0F;

    public static final float MIN_GUI_Z = -10000.0F;

    private static final int EXTRA_SPACE_AFTER_FIRST_TOOLTIP_LINE = 2;

    private final Minecraft minecraft;

    private final PoseStack pose;

    private final MultiBufferSource.BufferSource bufferSource;

    private final GuiGraphics.ScissorStack scissorStack = new GuiGraphics.ScissorStack();

    private boolean managed;

    private GuiGraphics(Minecraft minecraft0, PoseStack poseStack1, MultiBufferSource.BufferSource multiBufferSourceBufferSource2) {
        this.minecraft = minecraft0;
        this.pose = poseStack1;
        this.bufferSource = multiBufferSourceBufferSource2;
    }

    public GuiGraphics(Minecraft minecraft0, MultiBufferSource.BufferSource multiBufferSourceBufferSource1) {
        this(minecraft0, new PoseStack(), multiBufferSourceBufferSource1);
    }

    @Deprecated
    public void drawManaged(Runnable runnable0) {
        this.flush();
        this.managed = true;
        runnable0.run();
        this.managed = false;
        this.flush();
    }

    @Deprecated
    private void flushIfUnmanaged() {
        if (!this.managed) {
            this.flush();
        }
    }

    @Deprecated
    private void flushIfManaged() {
        if (this.managed) {
            this.flush();
        }
    }

    public int guiWidth() {
        return this.minecraft.getWindow().getGuiScaledWidth();
    }

    public int guiHeight() {
        return this.minecraft.getWindow().getGuiScaledHeight();
    }

    public PoseStack pose() {
        return this.pose;
    }

    public MultiBufferSource.BufferSource bufferSource() {
        return this.bufferSource;
    }

    public void flush() {
        RenderSystem.disableDepthTest();
        this.bufferSource.endBatch();
        RenderSystem.enableDepthTest();
    }

    public void hLine(int int0, int int1, int int2, int int3) {
        this.hLine(RenderType.gui(), int0, int1, int2, int3);
    }

    public void hLine(RenderType renderType0, int int1, int int2, int int3, int int4) {
        if (int2 < int1) {
            int $$5 = int1;
            int1 = int2;
            int2 = $$5;
        }
        this.fill(renderType0, int1, int3, int2 + 1, int3 + 1, int4);
    }

    public void vLine(int int0, int int1, int int2, int int3) {
        this.vLine(RenderType.gui(), int0, int1, int2, int3);
    }

    public void vLine(RenderType renderType0, int int1, int int2, int int3, int int4) {
        if (int3 < int2) {
            int $$5 = int2;
            int2 = int3;
            int3 = $$5;
        }
        this.fill(renderType0, int1, int2 + 1, int1 + 1, int3, int4);
    }

    public void enableScissor(int int0, int int1, int int2, int int3) {
        this.applyScissor(this.scissorStack.push(new ScreenRectangle(int0, int1, int2 - int0, int3 - int1)));
    }

    public void disableScissor() {
        this.applyScissor(this.scissorStack.pop());
    }

    private void applyScissor(@Nullable ScreenRectangle screenRectangle0) {
        this.flushIfManaged();
        if (screenRectangle0 != null) {
            Window $$1 = Minecraft.getInstance().getWindow();
            int $$2 = $$1.getHeight();
            double $$3 = $$1.getGuiScale();
            double $$4 = (double) screenRectangle0.left() * $$3;
            double $$5 = (double) $$2 - (double) screenRectangle0.bottom() * $$3;
            double $$6 = (double) screenRectangle0.width() * $$3;
            double $$7 = (double) screenRectangle0.height() * $$3;
            RenderSystem.enableScissor((int) $$4, (int) $$5, Math.max(0, (int) $$6), Math.max(0, (int) $$7));
        } else {
            RenderSystem.disableScissor();
        }
    }

    public void setColor(float float0, float float1, float float2, float float3) {
        this.flushIfManaged();
        RenderSystem.setShaderColor(float0, float1, float2, float3);
    }

    public void fill(int int0, int int1, int int2, int int3, int int4) {
        this.fill(int0, int1, int2, int3, 0, int4);
    }

    public void fill(int int0, int int1, int int2, int int3, int int4, int int5) {
        this.fill(RenderType.gui(), int0, int1, int2, int3, int4, int5);
    }

    public void fill(RenderType renderType0, int int1, int int2, int int3, int int4, int int5) {
        this.fill(renderType0, int1, int2, int3, int4, 0, int5);
    }

    public void fill(RenderType renderType0, int int1, int int2, int int3, int int4, int int5, int int6) {
        Matrix4f $$7 = this.pose.last().pose();
        if (int1 < int3) {
            int $$8 = int1;
            int1 = int3;
            int3 = $$8;
        }
        if (int2 < int4) {
            int $$9 = int2;
            int2 = int4;
            int4 = $$9;
        }
        float $$10 = (float) FastColor.ARGB32.alpha(int6) / 255.0F;
        float $$11 = (float) FastColor.ARGB32.red(int6) / 255.0F;
        float $$12 = (float) FastColor.ARGB32.green(int6) / 255.0F;
        float $$13 = (float) FastColor.ARGB32.blue(int6) / 255.0F;
        VertexConsumer $$14 = this.bufferSource.getBuffer(renderType0);
        $$14.vertex($$7, (float) int1, (float) int2, (float) int5).color($$11, $$12, $$13, $$10).endVertex();
        $$14.vertex($$7, (float) int1, (float) int4, (float) int5).color($$11, $$12, $$13, $$10).endVertex();
        $$14.vertex($$7, (float) int3, (float) int4, (float) int5).color($$11, $$12, $$13, $$10).endVertex();
        $$14.vertex($$7, (float) int3, (float) int2, (float) int5).color($$11, $$12, $$13, $$10).endVertex();
        this.flushIfUnmanaged();
    }

    public void fillGradient(int int0, int int1, int int2, int int3, int int4, int int5) {
        this.fillGradient(int0, int1, int2, int3, 0, int4, int5);
    }

    public void fillGradient(int int0, int int1, int int2, int int3, int int4, int int5, int int6) {
        this.fillGradient(RenderType.gui(), int0, int1, int2, int3, int5, int6, int4);
    }

    public void fillGradient(RenderType renderType0, int int1, int int2, int int3, int int4, int int5, int int6, int int7) {
        VertexConsumer $$8 = this.bufferSource.getBuffer(renderType0);
        this.fillGradient($$8, int1, int2, int3, int4, int7, int5, int6);
        this.flushIfUnmanaged();
    }

    private void fillGradient(VertexConsumer vertexConsumer0, int int1, int int2, int int3, int int4, int int5, int int6, int int7) {
        float $$8 = (float) FastColor.ARGB32.alpha(int6) / 255.0F;
        float $$9 = (float) FastColor.ARGB32.red(int6) / 255.0F;
        float $$10 = (float) FastColor.ARGB32.green(int6) / 255.0F;
        float $$11 = (float) FastColor.ARGB32.blue(int6) / 255.0F;
        float $$12 = (float) FastColor.ARGB32.alpha(int7) / 255.0F;
        float $$13 = (float) FastColor.ARGB32.red(int7) / 255.0F;
        float $$14 = (float) FastColor.ARGB32.green(int7) / 255.0F;
        float $$15 = (float) FastColor.ARGB32.blue(int7) / 255.0F;
        Matrix4f $$16 = this.pose.last().pose();
        vertexConsumer0.vertex($$16, (float) int1, (float) int2, (float) int5).color($$9, $$10, $$11, $$8).endVertex();
        vertexConsumer0.vertex($$16, (float) int1, (float) int4, (float) int5).color($$13, $$14, $$15, $$12).endVertex();
        vertexConsumer0.vertex($$16, (float) int3, (float) int4, (float) int5).color($$13, $$14, $$15, $$12).endVertex();
        vertexConsumer0.vertex($$16, (float) int3, (float) int2, (float) int5).color($$9, $$10, $$11, $$8).endVertex();
    }

    public void drawCenteredString(Font font0, String string1, int int2, int int3, int int4) {
        this.drawString(font0, string1, int2 - font0.width(string1) / 2, int3, int4);
    }

    public void drawCenteredString(Font font0, Component component1, int int2, int int3, int int4) {
        FormattedCharSequence $$5 = component1.getVisualOrderText();
        this.drawString(font0, $$5, int2 - font0.width($$5) / 2, int3, int4);
    }

    public void drawCenteredString(Font font0, FormattedCharSequence formattedCharSequence1, int int2, int int3, int int4) {
        this.drawString(font0, formattedCharSequence1, int2 - font0.width(formattedCharSequence1) / 2, int3, int4);
    }

    public int drawString(Font font0, @Nullable String string1, int int2, int int3, int int4) {
        return this.drawString(font0, string1, int2, int3, int4, true);
    }

    public int drawString(Font font0, @Nullable String string1, int int2, int int3, int int4, boolean boolean5) {
        if (string1 == null) {
            return 0;
        } else {
            int $$6 = font0.drawInBatch(string1, (float) int2, (float) int3, int4, boolean5, this.pose.last().pose(), this.bufferSource, Font.DisplayMode.NORMAL, 0, 15728880, font0.isBidirectional());
            this.flushIfUnmanaged();
            return $$6;
        }
    }

    public int drawString(Font font0, FormattedCharSequence formattedCharSequence1, int int2, int int3, int int4) {
        return this.drawString(font0, formattedCharSequence1, int2, int3, int4, true);
    }

    public int drawString(Font font0, FormattedCharSequence formattedCharSequence1, int int2, int int3, int int4, boolean boolean5) {
        int $$6 = font0.drawInBatch(formattedCharSequence1, (float) int2, (float) int3, int4, boolean5, this.pose.last().pose(), this.bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
        this.flushIfUnmanaged();
        return $$6;
    }

    public int drawString(Font font0, Component component1, int int2, int int3, int int4) {
        return this.drawString(font0, component1, int2, int3, int4, true);
    }

    public int drawString(Font font0, Component component1, int int2, int int3, int int4, boolean boolean5) {
        return this.drawString(font0, component1.getVisualOrderText(), int2, int3, int4, boolean5);
    }

    public void drawWordWrap(Font font0, FormattedText formattedText1, int int2, int int3, int int4, int int5) {
        for (FormattedCharSequence $$6 : font0.split(formattedText1, int4)) {
            this.drawString(font0, $$6, int2, int3, int5, false);
            int3 += 9;
        }
    }

    public void blit(int int0, int int1, int int2, int int3, int int4, TextureAtlasSprite textureAtlasSprite5) {
        this.innerBlit(textureAtlasSprite5.atlasLocation(), int0, int0 + int3, int1, int1 + int4, int2, textureAtlasSprite5.getU0(), textureAtlasSprite5.getU1(), textureAtlasSprite5.getV0(), textureAtlasSprite5.getV1());
    }

    public void blit(int int0, int int1, int int2, int int3, int int4, TextureAtlasSprite textureAtlasSprite5, float float6, float float7, float float8, float float9) {
        this.innerBlit(textureAtlasSprite5.atlasLocation(), int0, int0 + int3, int1, int1 + int4, int2, textureAtlasSprite5.getU0(), textureAtlasSprite5.getU1(), textureAtlasSprite5.getV0(), textureAtlasSprite5.getV1(), float6, float7, float8, float9);
    }

    public void renderOutline(int int0, int int1, int int2, int int3, int int4) {
        this.fill(int0, int1, int0 + int2, int1 + 1, int4);
        this.fill(int0, int1 + int3 - 1, int0 + int2, int1 + int3, int4);
        this.fill(int0, int1 + 1, int0 + 1, int1 + int3 - 1, int4);
        this.fill(int0 + int2 - 1, int1 + 1, int0 + int2, int1 + int3 - 1, int4);
    }

    public void blit(ResourceLocation resourceLocation0, int int1, int int2, int int3, int int4, int int5, int int6) {
        this.blit(resourceLocation0, int1, int2, 0, (float) int3, (float) int4, int5, int6, 256, 256);
    }

    public void blit(ResourceLocation resourceLocation0, int int1, int int2, int int3, float float4, float float5, int int6, int int7, int int8, int int9) {
        this.blit(resourceLocation0, int1, int1 + int6, int2, int2 + int7, int3, int6, int7, float4, float5, int8, int9);
    }

    public void blit(ResourceLocation resourceLocation0, int int1, int int2, int int3, int int4, float float5, float float6, int int7, int int8, int int9, int int10) {
        this.blit(resourceLocation0, int1, int1 + int3, int2, int2 + int4, 0, int7, int8, float5, float6, int9, int10);
    }

    public void blit(ResourceLocation resourceLocation0, int int1, int int2, float float3, float float4, int int5, int int6, int int7, int int8) {
        this.blit(resourceLocation0, int1, int2, int5, int6, float3, float4, int5, int6, int7, int8);
    }

    void blit(ResourceLocation resourceLocation0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, float float8, float float9, int int10, int int11) {
        this.innerBlit(resourceLocation0, int1, int2, int3, int4, int5, (float8 + 0.0F) / (float) int10, (float8 + (float) int6) / (float) int10, (float9 + 0.0F) / (float) int11, (float9 + (float) int7) / (float) int11);
    }

    void innerBlit(ResourceLocation resourceLocation0, int int1, int int2, int int3, int int4, int int5, float float6, float float7, float float8, float float9) {
        RenderSystem.setShaderTexture(0, resourceLocation0);
        RenderSystem.setShader(GameRenderer::m_172817_);
        Matrix4f $$10 = this.pose.last().pose();
        BufferBuilder $$11 = Tesselator.getInstance().getBuilder();
        $$11.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        $$11.m_252986_($$10, (float) int1, (float) int3, (float) int5).uv(float6, float8).endVertex();
        $$11.m_252986_($$10, (float) int1, (float) int4, (float) int5).uv(float6, float9).endVertex();
        $$11.m_252986_($$10, (float) int2, (float) int4, (float) int5).uv(float7, float9).endVertex();
        $$11.m_252986_($$10, (float) int2, (float) int3, (float) int5).uv(float7, float8).endVertex();
        BufferUploader.drawWithShader($$11.end());
    }

    void innerBlit(ResourceLocation resourceLocation0, int int1, int int2, int int3, int int4, int int5, float float6, float float7, float float8, float float9, float float10, float float11, float float12, float float13) {
        RenderSystem.setShaderTexture(0, resourceLocation0);
        RenderSystem.setShader(GameRenderer::m_172814_);
        RenderSystem.enableBlend();
        Matrix4f $$14 = this.pose.last().pose();
        BufferBuilder $$15 = Tesselator.getInstance().getBuilder();
        $$15.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        $$15.m_252986_($$14, (float) int1, (float) int3, (float) int5).color(float10, float11, float12, float13).uv(float6, float8).endVertex();
        $$15.m_252986_($$14, (float) int1, (float) int4, (float) int5).color(float10, float11, float12, float13).uv(float6, float9).endVertex();
        $$15.m_252986_($$14, (float) int2, (float) int4, (float) int5).color(float10, float11, float12, float13).uv(float7, float9).endVertex();
        $$15.m_252986_($$14, (float) int2, (float) int3, (float) int5).color(float10, float11, float12, float13).uv(float7, float8).endVertex();
        BufferUploader.drawWithShader($$15.end());
        RenderSystem.disableBlend();
    }

    public void blitNineSliced(ResourceLocation resourceLocation0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, int int8, int int9) {
        this.blitNineSliced(resourceLocation0, int1, int2, int3, int4, int5, int5, int5, int5, int6, int7, int8, int9);
    }

    public void blitNineSliced(ResourceLocation resourceLocation0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, int int8, int int9, int int10) {
        this.blitNineSliced(resourceLocation0, int1, int2, int3, int4, int5, int6, int5, int6, int7, int8, int9, int10);
    }

    public void blitNineSliced(ResourceLocation resourceLocation0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, int int8, int int9, int int10, int int11, int int12) {
        int5 = Math.min(int5, int3 / 2);
        int7 = Math.min(int7, int3 / 2);
        int6 = Math.min(int6, int4 / 2);
        int8 = Math.min(int8, int4 / 2);
        if (int3 == int9 && int4 == int10) {
            this.blit(resourceLocation0, int1, int2, int11, int12, int3, int4);
        } else if (int4 == int10) {
            this.blit(resourceLocation0, int1, int2, int11, int12, int5, int4);
            this.blitRepeating(resourceLocation0, int1 + int5, int2, int3 - int7 - int5, int4, int11 + int5, int12, int9 - int7 - int5, int10);
            this.blit(resourceLocation0, int1 + int3 - int7, int2, int11 + int9 - int7, int12, int7, int4);
        } else if (int3 == int9) {
            this.blit(resourceLocation0, int1, int2, int11, int12, int3, int6);
            this.blitRepeating(resourceLocation0, int1, int2 + int6, int3, int4 - int8 - int6, int11, int12 + int6, int9, int10 - int8 - int6);
            this.blit(resourceLocation0, int1, int2 + int4 - int8, int11, int12 + int10 - int8, int3, int8);
        } else {
            this.blit(resourceLocation0, int1, int2, int11, int12, int5, int6);
            this.blitRepeating(resourceLocation0, int1 + int5, int2, int3 - int7 - int5, int6, int11 + int5, int12, int9 - int7 - int5, int6);
            this.blit(resourceLocation0, int1 + int3 - int7, int2, int11 + int9 - int7, int12, int7, int6);
            this.blit(resourceLocation0, int1, int2 + int4 - int8, int11, int12 + int10 - int8, int5, int8);
            this.blitRepeating(resourceLocation0, int1 + int5, int2 + int4 - int8, int3 - int7 - int5, int8, int11 + int5, int12 + int10 - int8, int9 - int7 - int5, int8);
            this.blit(resourceLocation0, int1 + int3 - int7, int2 + int4 - int8, int11 + int9 - int7, int12 + int10 - int8, int7, int8);
            this.blitRepeating(resourceLocation0, int1, int2 + int6, int5, int4 - int8 - int6, int11, int12 + int6, int5, int10 - int8 - int6);
            this.blitRepeating(resourceLocation0, int1 + int5, int2 + int6, int3 - int7 - int5, int4 - int8 - int6, int11 + int5, int12 + int6, int9 - int7 - int5, int10 - int8 - int6);
            this.blitRepeating(resourceLocation0, int1 + int3 - int7, int2 + int6, int5, int4 - int8 - int6, int11 + int9 - int7, int12 + int6, int7, int10 - int8 - int6);
        }
    }

    public void blitRepeating(ResourceLocation resourceLocation0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, int int8) {
        int $$9 = int1;
        IntIterator $$10 = slices(int3, int7);
        while ($$10.hasNext()) {
            int $$11 = $$10.nextInt();
            int $$12 = (int7 - $$11) / 2;
            int $$13 = int2;
            IntIterator $$14 = slices(int4, int8);
            while ($$14.hasNext()) {
                int $$15 = $$14.nextInt();
                int $$16 = (int8 - $$15) / 2;
                this.blit(resourceLocation0, $$9, $$13, int5 + $$12, int6 + $$16, $$11, $$15);
                $$13 += $$15;
            }
            $$9 += $$11;
        }
    }

    private static IntIterator slices(int int0, int int1) {
        int $$2 = Mth.positiveCeilDiv(int0, int1);
        return new Divisor(int0, $$2);
    }

    public void renderItem(ItemStack itemStack0, int int1, int int2) {
        this.renderItem(this.minecraft.player, this.minecraft.level, itemStack0, int1, int2, 0);
    }

    public void renderItem(ItemStack itemStack0, int int1, int int2, int int3) {
        this.renderItem(this.minecraft.player, this.minecraft.level, itemStack0, int1, int2, int3);
    }

    public void renderItem(ItemStack itemStack0, int int1, int int2, int int3, int int4) {
        this.renderItem(this.minecraft.player, this.minecraft.level, itemStack0, int1, int2, int3, int4);
    }

    public void renderFakeItem(ItemStack itemStack0, int int1, int int2) {
        this.renderItem(null, this.minecraft.level, itemStack0, int1, int2, 0);
    }

    public void renderItem(LivingEntity livingEntity0, ItemStack itemStack1, int int2, int int3, int int4) {
        this.renderItem(livingEntity0, livingEntity0.m_9236_(), itemStack1, int2, int3, int4);
    }

    private void renderItem(@Nullable LivingEntity livingEntity0, @Nullable Level level1, ItemStack itemStack2, int int3, int int4, int int5) {
        this.renderItem(livingEntity0, level1, itemStack2, int3, int4, int5, 0);
    }

    private void renderItem(@Nullable LivingEntity livingEntity0, @Nullable Level level1, ItemStack itemStack2, int int3, int int4, int int5, int int6) {
        if (!itemStack2.isEmpty()) {
            BakedModel $$7 = this.minecraft.getItemRenderer().getModel(itemStack2, level1, livingEntity0, int5);
            this.pose.pushPose();
            this.pose.translate((float) (int3 + 8), (float) (int4 + 8), (float) (150 + ($$7.isGui3d() ? int6 : 0)));
            try {
                this.pose.mulPoseMatrix(new Matrix4f().scaling(1.0F, -1.0F, 1.0F));
                this.pose.scale(16.0F, 16.0F, 16.0F);
                boolean $$8 = !$$7.usesBlockLight();
                if ($$8) {
                    Lighting.setupForFlatItems();
                }
                this.minecraft.getItemRenderer().render(itemStack2, ItemDisplayContext.GUI, false, this.pose, this.bufferSource(), 15728880, OverlayTexture.NO_OVERLAY, $$7);
                this.flush();
                if ($$8) {
                    Lighting.setupFor3DItems();
                }
            } catch (Throwable var12) {
                CrashReport $$10 = CrashReport.forThrowable(var12, "Rendering item");
                CrashReportCategory $$11 = $$10.addCategory("Item being rendered");
                $$11.setDetail("Item Type", (CrashReportDetail<String>) (() -> String.valueOf(itemStack2.getItem())));
                $$11.setDetail("Item Damage", (CrashReportDetail<String>) (() -> String.valueOf(itemStack2.getDamageValue())));
                $$11.setDetail("Item NBT", (CrashReportDetail<String>) (() -> String.valueOf(itemStack2.getTag())));
                $$11.setDetail("Item Foil", (CrashReportDetail<String>) (() -> String.valueOf(itemStack2.hasFoil())));
                throw new ReportedException($$10);
            }
            this.pose.popPose();
        }
    }

    public void renderItemDecorations(Font font0, ItemStack itemStack1, int int2, int int3) {
        this.renderItemDecorations(font0, itemStack1, int2, int3, null);
    }

    public void renderItemDecorations(Font font0, ItemStack itemStack1, int int2, int int3, @Nullable String string4) {
        if (!itemStack1.isEmpty()) {
            this.pose.pushPose();
            if (itemStack1.getCount() != 1 || string4 != null) {
                String $$5 = string4 == null ? String.valueOf(itemStack1.getCount()) : string4;
                this.pose.translate(0.0F, 0.0F, 200.0F);
                this.drawString(font0, $$5, int2 + 19 - 2 - font0.width($$5), int3 + 6 + 3, 16777215, true);
            }
            if (itemStack1.isBarVisible()) {
                int $$6 = itemStack1.getBarWidth();
                int $$7 = itemStack1.getBarColor();
                int $$8 = int2 + 2;
                int $$9 = int3 + 13;
                this.fill(RenderType.guiOverlay(), $$8, $$9, $$8 + 13, $$9 + 2, -16777216);
                this.fill(RenderType.guiOverlay(), $$8, $$9, $$8 + $$6, $$9 + 1, $$7 | 0xFF000000);
            }
            LocalPlayer $$10 = this.minecraft.player;
            float $$11 = $$10 == null ? 0.0F : $$10.m_36335_().getCooldownPercent(itemStack1.getItem(), this.minecraft.getFrameTime());
            if ($$11 > 0.0F) {
                int $$12 = int3 + Mth.floor(16.0F * (1.0F - $$11));
                int $$13 = $$12 + Mth.ceil(16.0F * $$11);
                this.fill(RenderType.guiOverlay(), int2, $$12, int2 + 16, $$13, Integer.MAX_VALUE);
            }
            this.pose.popPose();
        }
    }

    public void renderTooltip(Font font0, ItemStack itemStack1, int int2, int int3) {
        this.renderTooltip(font0, Screen.getTooltipFromItem(this.minecraft, itemStack1), itemStack1.getTooltipImage(), int2, int3);
    }

    public void renderTooltip(Font font0, List<Component> listComponent1, Optional<TooltipComponent> optionalTooltipComponent2, int int3, int int4) {
        List<ClientTooltipComponent> $$5 = (List<ClientTooltipComponent>) listComponent1.stream().map(Component::m_7532_).map(ClientTooltipComponent::m_169948_).collect(Collectors.toList());
        optionalTooltipComponent2.ifPresent(p_282969_ -> $$5.add(1, ClientTooltipComponent.create(p_282969_)));
        this.renderTooltipInternal(font0, $$5, int3, int4, DefaultTooltipPositioner.INSTANCE);
    }

    public void renderTooltip(Font font0, Component component1, int int2, int int3) {
        this.renderTooltip(font0, List.of(component1.getVisualOrderText()), int2, int3);
    }

    public void renderComponentTooltip(Font font0, List<Component> listComponent1, int int2, int int3) {
        this.renderTooltip(font0, Lists.transform(listComponent1, Component::m_7532_), int2, int3);
    }

    public void renderTooltip(Font font0, List<? extends FormattedCharSequence> listExtendsFormattedCharSequence1, int int2, int int3) {
        this.renderTooltipInternal(font0, (List<ClientTooltipComponent>) listExtendsFormattedCharSequence1.stream().map(ClientTooltipComponent::m_169948_).collect(Collectors.toList()), int2, int3, DefaultTooltipPositioner.INSTANCE);
    }

    public void renderTooltip(Font font0, List<FormattedCharSequence> listFormattedCharSequence1, ClientTooltipPositioner clientTooltipPositioner2, int int3, int int4) {
        this.renderTooltipInternal(font0, (List<ClientTooltipComponent>) listFormattedCharSequence1.stream().map(ClientTooltipComponent::m_169948_).collect(Collectors.toList()), int3, int4, clientTooltipPositioner2);
    }

    private void renderTooltipInternal(Font font0, List<ClientTooltipComponent> listClientTooltipComponent1, int int2, int int3, ClientTooltipPositioner clientTooltipPositioner4) {
        if (!listClientTooltipComponent1.isEmpty()) {
            int $$5 = 0;
            int $$6 = listClientTooltipComponent1.size() == 1 ? -2 : 0;
            for (ClientTooltipComponent $$7 : listClientTooltipComponent1) {
                int $$8 = $$7.getWidth(font0);
                if ($$8 > $$5) {
                    $$5 = $$8;
                }
                $$6 += $$7.getHeight();
            }
            int $$9 = $$5;
            int $$10 = $$6;
            Vector2ic $$11 = clientTooltipPositioner4.positionTooltip(this.guiWidth(), this.guiHeight(), int2, int3, $$9, $$10);
            int $$12 = $$11.x();
            int $$13 = $$11.y();
            this.pose.pushPose();
            int $$14 = 400;
            this.drawManaged(() -> TooltipRenderUtil.renderTooltipBackground(this, $$12, $$13, $$9, $$10, 400));
            this.pose.translate(0.0F, 0.0F, 400.0F);
            int $$15 = $$13;
            for (int $$16 = 0; $$16 < listClientTooltipComponent1.size(); $$16++) {
                ClientTooltipComponent $$17 = (ClientTooltipComponent) listClientTooltipComponent1.get($$16);
                $$17.renderText(font0, $$12, $$15, this.pose.last().pose(), this.bufferSource);
                $$15 += $$17.getHeight() + ($$16 == 0 ? 2 : 0);
            }
            $$15 = $$13;
            for (int $$18 = 0; $$18 < listClientTooltipComponent1.size(); $$18++) {
                ClientTooltipComponent $$19 = (ClientTooltipComponent) listClientTooltipComponent1.get($$18);
                $$19.renderImage(font0, $$12, $$15, this);
                $$15 += $$19.getHeight() + ($$18 == 0 ? 2 : 0);
            }
            this.pose.popPose();
        }
    }

    public void renderComponentHoverEffect(Font font0, @Nullable Style style1, int int2, int int3) {
        if (style1 != null && style1.getHoverEvent() != null) {
            HoverEvent $$4 = style1.getHoverEvent();
            HoverEvent.ItemStackInfo $$5 = $$4.getValue(HoverEvent.Action.SHOW_ITEM);
            if ($$5 != null) {
                this.renderTooltip(font0, $$5.getItemStack(), int2, int3);
            } else {
                HoverEvent.EntityTooltipInfo $$6 = $$4.getValue(HoverEvent.Action.SHOW_ENTITY);
                if ($$6 != null) {
                    if (this.minecraft.options.advancedItemTooltips) {
                        this.renderComponentTooltip(font0, $$6.getTooltipLines(), int2, int3);
                    }
                } else {
                    Component $$7 = $$4.getValue(HoverEvent.Action.SHOW_TEXT);
                    if ($$7 != null) {
                        this.renderTooltip(font0, font0.split($$7, Math.max(this.guiWidth() / 2, 200)), int2, int3);
                    }
                }
            }
        }
    }

    static class ScissorStack {

        private final Deque<ScreenRectangle> stack = new ArrayDeque();

        public ScreenRectangle push(ScreenRectangle screenRectangle0) {
            ScreenRectangle $$1 = (ScreenRectangle) this.stack.peekLast();
            if ($$1 != null) {
                ScreenRectangle $$2 = (ScreenRectangle) Objects.requireNonNullElse(screenRectangle0.intersection($$1), ScreenRectangle.empty());
                this.stack.addLast($$2);
                return $$2;
            } else {
                this.stack.addLast(screenRectangle0);
                return screenRectangle0;
            }
        }

        @Nullable
        public ScreenRectangle pop() {
            if (this.stack.isEmpty()) {
                throw new IllegalStateException("Scissor stack underflow");
            } else {
                this.stack.removeLast();
                return (ScreenRectangle) this.stack.peekLast();
            }
        }
    }
}