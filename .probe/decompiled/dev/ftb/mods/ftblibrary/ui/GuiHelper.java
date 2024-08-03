package dev.ftb.mods.ftblibrary.ui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import java.util.List;
import java.util.Stack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class GuiHelper {

    private static final Stack<GuiHelper.Scissor> SCISSOR = new Stack();

    public static final BaseScreen BLANK_GUI = new BaseScreen() {

        @Override
        public void addWidgets() {
        }

        @Override
        public void alignWidgets() {
        }
    };

    public static void setupDrawing() {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFunc(770, 771);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableDepthTest();
    }

    public static void playSound(SoundEvent event, float pitch) {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(event, pitch));
    }

    public static void drawTexturedRect(GuiGraphics graphics, int x, int y, int w, int h, Color4I col, float u0, float v0, float u1, float v1) {
        RenderSystem.setShader(GameRenderer::m_172814_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        addRectToBufferWithUV(graphics, buffer, x, y, w, h, col, u0, v0, u1, v1);
        BufferUploader.drawWithShader(buffer.end());
    }

    public static void addRectToBuffer(GuiGraphics graphics, BufferBuilder buffer, int x, int y, int w, int h, Color4I col) {
        if (w > 0 && h > 0) {
            Matrix4f m = graphics.pose().last().pose();
            int r = col.redi();
            int g = col.greeni();
            int b = col.bluei();
            int a = col.alphai();
            buffer.m_252986_(m, (float) x, (float) (y + h), 0.0F).color(r, g, b, a).endVertex();
            buffer.m_252986_(m, (float) (x + w), (float) (y + h), 0.0F).color(r, g, b, a).endVertex();
            buffer.m_252986_(m, (float) (x + w), (float) y, 0.0F).color(r, g, b, a).endVertex();
            buffer.m_252986_(m, (float) x, (float) y, 0.0F).color(r, g, b, a).endVertex();
        }
    }

    public static void addRectToBufferWithUV(GuiGraphics graphics, BufferBuilder buffer, int x, int y, int w, int h, Color4I col, float u0, float v0, float u1, float v1) {
        if (w > 0 && h > 0) {
            Matrix4f m = graphics.pose().last().pose();
            int r = col.redi();
            int g = col.greeni();
            int b = col.bluei();
            int a = col.alphai();
            buffer.m_252986_(m, (float) x, (float) (y + h), 0.0F).color(r, g, b, a).uv(u0, v1).endVertex();
            buffer.m_252986_(m, (float) (x + w), (float) (y + h), 0.0F).color(r, g, b, a).uv(u1, v1).endVertex();
            buffer.m_252986_(m, (float) (x + w), (float) y, 0.0F).color(r, g, b, a).uv(u1, v0).endVertex();
            buffer.m_252986_(m, (float) x, (float) y, 0.0F).color(r, g, b, a).uv(u0, v0).endVertex();
        }
    }

    public static void drawHollowRect(GuiGraphics graphics, int x, int y, int w, int h, Color4I col, boolean roundEdges) {
        if (w > 1 && h > 1 && !col.isEmpty()) {
            RenderSystem.setShader(GameRenderer::m_172811_);
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder buffer = tesselator.getBuilder();
            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            addRectToBuffer(graphics, buffer, x, y + 1, 1, h - 2, col);
            addRectToBuffer(graphics, buffer, x + w - 1, y + 1, 1, h - 2, col);
            if (roundEdges) {
                addRectToBuffer(graphics, buffer, x + 1, y, w - 2, 1, col);
                addRectToBuffer(graphics, buffer, x + 1, y + h - 1, w - 2, 1, col);
            } else {
                addRectToBuffer(graphics, buffer, x, y, w, 1, col);
                addRectToBuffer(graphics, buffer, x, y + h - 1, w, 1, col);
            }
            tesselator.end();
        } else {
            col.draw(graphics, x, y, w, h);
        }
    }

    public static void drawRectWithShade(GuiGraphics graphics, int x, int y, int w, int h, Color4I col, int intensity) {
        RenderSystem.setShader(GameRenderer::m_172811_);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        addRectToBuffer(graphics, buffer, x, y, w - 1, 1, col);
        addRectToBuffer(graphics, buffer, x, y + 1, 1, h - 1, col);
        col = col.mutable().addBrightness(-intensity);
        addRectToBuffer(graphics, buffer, x + w - 1, y, 1, 1, col);
        addRectToBuffer(graphics, buffer, x, y + h - 1, 1, 1, col);
        col = col.mutable().addBrightness(-intensity);
        addRectToBuffer(graphics, buffer, x + w - 1, y + 1, 1, h - 2, col);
        addRectToBuffer(graphics, buffer, x + 1, y + h - 1, w - 1, 1, col);
        tesselator.end();
    }

    public static void drawGradientRect(GuiGraphics graphics, int x, int y, int w, int h, Color4I col1, Color4I col2) {
        graphics.fillGradient(x, y, x + w, y + h, col1.rgba(), col2.rgba());
    }

    public static void drawItem(GuiGraphics graphics, ItemStack stack, int hash, boolean renderOverlay, @Nullable String text) {
        if (!stack.isEmpty()) {
            Minecraft mc = Minecraft.getInstance();
            ItemRenderer itemRenderer = mc.getItemRenderer();
            BakedModel bakedModel = itemRenderer.getModel(stack, null, mc.player, hash);
            Minecraft.getInstance().getTextureManager().getTexture(InventoryMenu.BLOCK_ATLAS).setFilter(false, false);
            RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            PoseStack modelViewStack = RenderSystem.getModelViewStack();
            modelViewStack.pushPose();
            modelViewStack.mulPoseMatrix(graphics.pose().last().pose());
            modelViewStack.scale(1.0F, -1.0F, 1.0F);
            modelViewStack.scale(16.0F, 16.0F, 16.0F);
            RenderSystem.applyModelViewMatrix();
            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
            boolean flatLight = !bakedModel.usesBlockLight();
            if (flatLight) {
                Lighting.setupForFlatItems();
            }
            itemRenderer.render(stack, ItemDisplayContext.GUI, false, new PoseStack(), bufferSource, 15728880, OverlayTexture.NO_OVERLAY, bakedModel);
            bufferSource.endBatch();
            RenderSystem.enableDepthTest();
            if (flatLight) {
                Lighting.setupFor3DItems();
            }
            modelViewStack.popPose();
            RenderSystem.applyModelViewMatrix();
            if (renderOverlay) {
                Tesselator t = Tesselator.getInstance();
                Font font = mc.font;
                if (stack.getCount() != 1 || text != null) {
                    String s = text == null ? String.valueOf(stack.getCount()) : text;
                    graphics.pose().pushPose();
                    graphics.pose().translate(9.0 - (double) font.width(s), 1.0, 20.0);
                    font.drawInBatch(s, 0.0F, 0.0F, 16777215, true, graphics.pose().last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
                    bufferSource.endBatch();
                    graphics.pose().popPose();
                }
                if (stack.isBarVisible()) {
                    RenderSystem.disableDepthTest();
                    RenderSystem.disableBlend();
                    int barWidth = stack.getBarWidth();
                    int barColor = stack.getBarColor();
                    draw(graphics, t, -6, 5, 13, 2, 0, 0, 0, 255);
                    draw(graphics, t, -6, 5, barWidth, 1, barColor >> 16 & 0xFF, barColor >> 8 & 0xFF, barColor & 0xFF, 255);
                    RenderSystem.enableBlend();
                    RenderSystem.enableDepthTest();
                }
                float cooldown = mc.player == null ? 0.0F : mc.player.m_36335_().getCooldownPercent(stack.getItem(), mc.getFrameTime());
                if (cooldown > 0.0F) {
                    RenderSystem.disableDepthTest();
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    draw(graphics, t, -8, Mth.floor(16.0F * (1.0F - cooldown)) - 8, 16, Mth.ceil(16.0F * cooldown), 255, 255, 255, 127);
                    RenderSystem.enableDepthTest();
                }
            }
        }
    }

    private static void draw(GuiGraphics graphics, Tesselator t, int x, int y, int width, int height, int red, int green, int blue, int alpha) {
        if (width > 0 && height > 0) {
            RenderSystem.setShader(GameRenderer::m_172811_);
            Matrix4f m = graphics.pose().last().pose();
            BufferBuilder renderer = t.getBuilder();
            renderer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            renderer.m_252986_(m, (float) x, (float) y, 0.0F).color(red, green, blue, alpha).endVertex();
            renderer.m_252986_(m, (float) x, (float) (y + height), 0.0F).color(red, green, blue, alpha).endVertex();
            renderer.m_252986_(m, (float) (x + width), (float) (y + height), 0.0F).color(red, green, blue, alpha).endVertex();
            renderer.m_252986_(m, (float) (x + width), (float) y, 0.0F).color(red, green, blue, alpha).endVertex();
            t.end();
        }
    }

    public static void pushScissor(Window screen, int x, int y, int w, int h) {
        if (SCISSOR.isEmpty()) {
            GL11.glEnable(3089);
        }
        GuiHelper.Scissor scissor = SCISSOR.isEmpty() ? new GuiHelper.Scissor(x, y, w, h) : ((GuiHelper.Scissor) SCISSOR.lastElement()).crop(x, y, w, h);
        SCISSOR.push(scissor);
        scissor.scissor(screen);
    }

    public static void popScissor(Window screen) {
        SCISSOR.pop();
        if (SCISSOR.isEmpty()) {
            GL11.glDisable(3089);
        } else {
            ((GuiHelper.Scissor) SCISSOR.lastElement()).scissor(screen);
        }
    }

    public static String clickEventToString(@Nullable ClickEvent event) {
        if (event == null) {
            return "";
        } else {
            return switch(event.getAction()) {
                case OPEN_URL, CHANGE_PAGE ->
                    event.getValue();
                case OPEN_FILE ->
                    "file:" + event.getValue();
                case RUN_COMMAND ->
                    "command:" + event.getValue();
                case SUGGEST_COMMAND ->
                    "suggest_command:" + event.getValue();
                default ->
                    "";
            };
        }
    }

    public static void addStackTooltip(ItemStack stack, List<Component> list) {
        addStackTooltip(stack, list, null);
    }

    public static void addStackTooltip(ItemStack stack, List<Component> list, @Nullable Component prefix) {
        List<Component> tooltip = stack.getTooltipLines(Minecraft.getInstance().player, Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.Default.f_256730_ : TooltipFlag.Default.f_256752_);
        list.add(prefix == null ? ((Component) tooltip.get(0)).copy().withStyle(stack.getRarity().color) : prefix.copy().append((Component) tooltip.get(0)));
        for (int i = 1; i < tooltip.size(); i++) {
            list.add(Component.literal("").withStyle(ChatFormatting.GRAY).append((Component) tooltip.get(i)));
        }
    }

    public static void drawBorderedPanel(GuiGraphics graphics, int x, int y, int w, int h, Color4I color, boolean outset) {
        w--;
        h--;
        Color4I hi = color.addBrightness(outset ? 0.15F : -0.1F);
        Color4I lo = color.addBrightness(outset ? -0.1F : 0.15F);
        graphics.fill(x, y, x + w, y + h, color.rgba());
        graphics.hLine(x, x + w - 1, y, hi.rgba());
        graphics.vLine(x, y, y + h, hi.rgba());
        graphics.hLine(x + 1, x + w, y + h, lo.rgba());
        graphics.vLine(x + w, y, y + h, lo.rgba());
    }

    private static class Scissor {

        private final int x;

        private final int y;

        private final int w;

        private final int h;

        private Scissor(int _x, int _y, int _w, int _h) {
            this.x = _x;
            this.y = _y;
            this.w = Math.max(0, _w);
            this.h = Math.max(0, _h);
        }

        public GuiHelper.Scissor crop(int sx, int sy, int sw, int sh) {
            int x0 = Math.max(this.x, sx);
            int y0 = Math.max(this.y, sy);
            int x1 = Math.min(this.x + this.w, sx + sw);
            int y1 = Math.min(this.y + this.h, sy + sh);
            return new GuiHelper.Scissor(x0, y0, x1 - x0, y1 - y0);
        }

        public void scissor(Window screen) {
            double scale = screen.getGuiScale();
            int sx = (int) ((double) this.x * scale);
            int sy = (int) ((double) (screen.getGuiScaledHeight() - (this.y + this.h)) * scale);
            int sw = (int) ((double) this.w * scale);
            int sh = (int) ((double) this.h * scale);
            GL11.glScissor(sx, sy, sw, sh);
        }
    }
}