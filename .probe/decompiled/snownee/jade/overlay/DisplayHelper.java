package snownee.jade.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.Optional;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import snownee.jade.Jade;
import snownee.jade.api.config.IWailaConfig;
import snownee.jade.api.fluid.JadeFluidObject;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.ui.IDisplayHelper;
import snownee.jade.util.ClientProxy;
import snownee.jade.util.Color;

public class DisplayHelper implements IDisplayHelper {

    public static final DisplayHelper INSTANCE = new DisplayHelper();

    private static final Minecraft CLIENT = Minecraft.getInstance();

    private static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");

    private static final int TEX_WIDTH = 16;

    private static final int TEX_HEIGHT = 16;

    private static final int MIN_FLUID_HEIGHT = 1;

    private static final Pattern STRIP_COLOR = Pattern.compile("(?i)§[0-9A-F]");

    public static DecimalFormat dfCommas = new DecimalFormat("0.##");

    public static final DecimalFormat[] dfCommasArray = new DecimalFormat[] { dfCommas, new DecimalFormat("0.#"), new DecimalFormat("0") };

    private static boolean betterTextShadow;

    private static void renderGuiItemDecorations(GuiGraphics guiGraphics, Font font, ItemStack stack, int i, int j, @Nullable String text) {
        if (!stack.isEmpty()) {
            guiGraphics.pose().pushPose();
            if (stack.getCount() != 1 || text != null) {
                String s = text == null ? INSTANCE.humanReadableNumber((double) stack.getCount(), "", false, null) : text;
                boolean smaller = s.length() > 3;
                float scale = smaller ? 0.5F : 0.75F;
                int x = smaller ? 32 : 22;
                int y = smaller ? 23 : 13;
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(0.0F, 0.0F, 200.0F);
                guiGraphics.pose().scale(scale, scale, 1.0F);
                int color = IThemeHelper.get().theme().itemAmountColor;
                guiGraphics.drawString(font, s, i + x - font.width(s), j + y, color, true);
                guiGraphics.pose().popPose();
            }
            if (stack.isBarVisible()) {
                RenderSystem.disableDepthTest();
                int k = stack.getBarWidth();
                int l = stack.getBarColor();
                int m = i + 2;
                int n = j + 13;
                guiGraphics.fill(RenderType.guiOverlay(), m, n, m + 13, n + 2, -16777216);
                guiGraphics.fill(RenderType.guiOverlay(), m, n, m + k, n + 1, l | 0xFF000000);
            }
            guiGraphics.pose().popPose();
            ClientProxy.renderItemDecorationsExtra(guiGraphics, font, stack, i, j, text);
        }
    }

    public static void drawTexturedModalRect(GuiGraphics guiGraphics, float x, float y, int textureX, int textureY, int width, int height, int tw, int th) {
        Matrix4f matrix = guiGraphics.pose().last().pose();
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        float zLevel = 0.0F;
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();
        RenderSystem.setShader(GameRenderer::m_172817_);
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.m_252986_(matrix, x, y + (float) height, zLevel).uv((float) textureX * f, (float) (textureY + th) * f1).endVertex();
        buffer.m_252986_(matrix, x + (float) width, y + (float) height, zLevel).uv((float) (textureX + tw) * f, (float) (textureY + th) * f1).endVertex();
        buffer.m_252986_(matrix, x + (float) width, y, zLevel).uv((float) (textureX + tw) * f, (float) textureY * f1).endVertex();
        buffer.m_252986_(matrix, x, y, zLevel).uv((float) textureX * f, (float) textureY * f1).endVertex();
        BufferUploader.drawWithShader(buffer.end());
    }

    public static void renderIcon(GuiGraphics guiGraphics, float x, float y, int sx, int sy, IconUI icon) {
        if (icon != null) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, OverlayRenderer.alpha);
            RenderSystem.setShaderTexture(0, GUI_ICONS_LOCATION);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            if (icon.bu != -1) {
                drawTexturedModalRect(guiGraphics, x, y, icon.bu, icon.bv, sx, sy, icon.bsu, icon.bsv);
            }
            drawTexturedModalRect(guiGraphics, x, y, icon.u, icon.v, sx, sy, icon.su, icon.sv);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    private static void setGLColorFromInt(int color) {
        float red = (float) (color >> 16 & 0xFF) / 255.0F;
        float green = (float) (color >> 8 & 0xFF) / 255.0F;
        float blue = (float) (color & 0xFF) / 255.0F;
        float alpha = (float) (color >> 24 & 0xFF) / 255.0F;
        RenderSystem.setShaderColor(red, green, blue, alpha);
    }

    private static void drawTextureWithMasking(Matrix4f matrix, float xCoord, float yCoord, TextureAtlasSprite textureSprite, float maskTop, float maskRight, float zLevel) {
        float uMin = textureSprite.getU0();
        float uMax = textureSprite.getU1();
        float vMin = textureSprite.getV0();
        float vMax = textureSprite.getV1();
        uMax -= maskRight / 16.0F * (uMax - uMin);
        vMax -= maskTop / 16.0F * (vMax - vMin);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuilder();
        RenderSystem.setShader(GameRenderer::m_172817_);
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.m_252986_(matrix, xCoord, yCoord + 16.0F, zLevel).uv(uMin, vMax).endVertex();
        bufferBuilder.m_252986_(matrix, xCoord + 16.0F - maskRight, yCoord + 16.0F, zLevel).uv(uMax, vMax).endVertex();
        bufferBuilder.m_252986_(matrix, xCoord + 16.0F - maskRight, yCoord + maskTop, zLevel).uv(uMax, vMin).endVertex();
        bufferBuilder.m_252986_(matrix, xCoord, yCoord + maskTop, zLevel).uv(uMin, vMin).endVertex();
        BufferUploader.drawWithShader(bufferBuilder.end());
    }

    public static void fill(GuiGraphics guiGraphics, float minX, float minY, float maxX, float maxY, int color) {
        fill(guiGraphics.pose().last().pose(), minX, minY, maxX, maxY, color);
    }

    private static void fill(Matrix4f matrix, float minX, float minY, float maxX, float maxY, int color) {
        if (minX < maxX) {
            float i = minX;
            minX = maxX;
            maxX = i;
        }
        if (minY < maxY) {
            float j = minY;
            minY = maxY;
            maxY = j;
        }
        float f3 = (float) (color >> 24 & 0xFF) / 255.0F * OverlayRenderer.alpha;
        float f = (float) (color >> 16 & 0xFF) / 255.0F;
        float f1 = (float) (color >> 8 & 0xFF) / 255.0F;
        float f2 = (float) (color & 0xFF) / 255.0F;
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::m_172811_);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        bufferbuilder.m_252986_(matrix, minX, maxY, 0.0F).color(f, f1, f2, f3).endVertex();
        bufferbuilder.m_252986_(matrix, maxX, maxY, 0.0F).color(f, f1, f2, f3).endVertex();
        bufferbuilder.m_252986_(matrix, maxX, minY, 0.0F).color(f, f1, f2, f3).endVertex();
        bufferbuilder.m_252986_(matrix, minX, minY, 0.0F).color(f, f1, f2, f3).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
        RenderSystem.disableBlend();
    }

    public static boolean enableBetterTextShadow() {
        return betterTextShadow;
    }

    public static void setBetterTextShadow(boolean betterTextShadow) {
        DisplayHelper.betterTextShadow = betterTextShadow;
    }

    @Override
    public void drawItem(GuiGraphics guiGraphics, float x, float y, ItemStack stack, float scale, @Nullable String text) {
        if (!(OverlayRenderer.alpha < 0.5F)) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(x, y, 0.0F);
            guiGraphics.pose().scale(scale, scale, scale);
            guiGraphics.renderFakeItem(stack, 0, 0);
            renderGuiItemDecorations(guiGraphics, CLIENT.font, stack, 0, 0, text);
            guiGraphics.pose().popPose();
        }
    }

    @Override
    public void drawGradientRect(GuiGraphics guiGraphics, float left, float top, float width, float height, int startColor, int endColor) {
        this.drawGradientRect(guiGraphics, left, top, width, height, startColor, endColor, false);
    }

    public void drawGradientRect(GuiGraphics guiGraphics, float left, float top, float width, float height, int startColor, int endColor, boolean horizontal) {
        if (startColor != -1 || endColor != -1) {
            float zLevel = 0.0F;
            Matrix4f matrix = guiGraphics.pose().last().pose();
            float f = (float) (startColor >> 24 & 0xFF) / 255.0F * OverlayRenderer.alpha;
            float f1 = (float) (startColor >> 16 & 0xFF) / 255.0F;
            float f2 = (float) (startColor >> 8 & 0xFF) / 255.0F;
            float f3 = (float) (startColor & 0xFF) / 255.0F;
            float f4 = (float) (endColor >> 24 & 0xFF) / 255.0F * OverlayRenderer.alpha;
            float f5 = (float) (endColor >> 16 & 0xFF) / 255.0F;
            float f6 = (float) (endColor >> 8 & 0xFF) / 255.0F;
            float f7 = (float) (endColor & 0xFF) / 255.0F;
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShader(GameRenderer::m_172811_);
            Tesselator tessellator = Tesselator.getInstance();
            BufferBuilder buffer = tessellator.getBuilder();
            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            if (horizontal) {
                buffer.m_252986_(matrix, left + width, top, zLevel).color(f5, f6, f7, f4).endVertex();
                buffer.m_252986_(matrix, left, top, zLevel).color(f1, f2, f3, f).endVertex();
                buffer.m_252986_(matrix, left, top + height, zLevel).color(f1, f2, f3, f).endVertex();
                buffer.m_252986_(matrix, left + width, top + height, zLevel).color(f5, f6, f7, f4).endVertex();
            } else {
                buffer.m_252986_(matrix, left + width, top, zLevel).color(f1, f2, f3, f).endVertex();
                buffer.m_252986_(matrix, left, top, zLevel).color(f1, f2, f3, f).endVertex();
                buffer.m_252986_(matrix, left, top + height, zLevel).color(f5, f6, f7, f4).endVertex();
                buffer.m_252986_(matrix, left + width, top + height, zLevel).color(f5, f6, f7, f4).endVertex();
            }
            BufferUploader.drawWithShader(buffer.end());
            RenderSystem.disableBlend();
        }
    }

    @Override
    public void drawBorder(GuiGraphics guiGraphics, float minX, float minY, float maxX, float maxY, float width, int color, boolean corner) {
        fill(guiGraphics, minX + width, minY, maxX - width, minY + width, color);
        fill(guiGraphics, minX + width, maxY - width, maxX - width, maxY, color);
        if (corner) {
            fill(guiGraphics, minX, minY, minX + width, maxY, color);
            fill(guiGraphics, maxX - width, minY, maxX, maxY, color);
        } else {
            fill(guiGraphics, minX, minY + width, minX + width, maxY - width, color);
            fill(guiGraphics, maxX - width, minY + width, maxX, maxY - width, color);
        }
    }

    public void drawFluid(GuiGraphics guiGraphics, float xPosition, float yPosition, JadeFluidObject fluid, float width, float height, long capacityMb) {
        if (!fluid.isEmpty()) {
            long amount = JadeFluidObject.bucketVolume();
            MutableFloat scaledAmount = new MutableFloat((float) amount * height / (float) capacityMb);
            if (amount > 0L && scaledAmount.floatValue() < 1.0F) {
                scaledAmount.setValue(1.0F);
            }
            if (scaledAmount.floatValue() > height) {
                scaledAmount.setValue(height);
            }
            ClientProxy.getFluidSpriteAndColor(fluid, (sprite, color) -> {
                if (sprite == null) {
                    float maxY = yPosition + height;
                    if (color == -1) {
                        color = -1431655766;
                    }
                    fill(guiGraphics, xPosition, maxY - scaledAmount.floatValue(), xPosition + width, maxY, color);
                } else {
                    if (OverlayRenderer.alpha != 1.0F) {
                        color = IWailaConfig.IConfigOverlay.applyAlpha(color, OverlayRenderer.alpha);
                    }
                    this.drawTiledSprite(guiGraphics, xPosition, yPosition, width, height, color, scaledAmount.floatValue(), sprite);
                }
            });
        }
    }

    private void drawTiledSprite(GuiGraphics guiGraphics, float xPosition, float yPosition, float tiledWidth, float tiledHeight, int color, float scaledAmount, TextureAtlasSprite sprite) {
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        Matrix4f matrix = guiGraphics.pose().last().pose();
        setGLColorFromInt(color);
        RenderSystem.enableBlend();
        int xTileCount = (int) (tiledWidth / 16.0F);
        float xRemainder = tiledWidth - (float) (xTileCount * 16);
        int yTileCount = (int) (scaledAmount / 16.0F);
        float yRemainder = scaledAmount - (float) (yTileCount * 16);
        float yStart = yPosition + tiledHeight;
        for (int xTile = 0; xTile <= xTileCount; xTile++) {
            for (int yTile = 0; yTile <= yTileCount; yTile++) {
                float width = xTile == xTileCount ? xRemainder : 16.0F;
                float height = yTile == yTileCount ? yRemainder : 16.0F;
                float x = xPosition + (float) (xTile * 16);
                float y = yStart - (float) ((yTile + 1) * 16);
                if (width > 0.0F && height > 0.0F) {
                    float maskTop = 16.0F - height;
                    float maskRight = 16.0F - width;
                    drawTextureWithMasking(matrix, x, y, sprite, maskTop, maskRight, 0.0F);
                }
            }
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }

    @Override
    public String humanReadableNumber(double number, String unit, boolean milli) {
        return this.humanReadableNumber(number, unit, milli, dfCommas);
    }

    @Override
    public String humanReadableNumber(double number, String unit, boolean milli, @Nullable Format formatter) {
        StringBuilder sb = new StringBuilder();
        boolean n = number < 0.0;
        if (n) {
            number = -number;
            sb.append('-');
        }
        if (milli && number >= 1000.0) {
            number /= 1000.0;
            milli = false;
        }
        int exp = 0;
        if (!(number < 1000.0) && (formatter != null || !(number < 10000.0))) {
            exp = (int) Math.log10(number) / 3;
            if (exp > 7) {
                exp = 7;
            }
            if (exp > 0) {
                number /= Math.pow(1000.0, (double) exp);
            }
            if (formatter == null) {
                if (number < 10.0) {
                    formatter = dfCommasArray[0];
                } else if (number < 100.0) {
                    formatter = dfCommasArray[1];
                } else {
                    formatter = dfCommasArray[2];
                }
            }
        } else {
            formatter = dfCommasArray[2];
        }
        if (formatter instanceof NumberFormat numberFormat) {
            sb.append(numberFormat.format(number));
        } else {
            sb.append(formatter.format(new Object[] { number }));
        }
        if (exp == 0) {
            if (milli && number != 0.0) {
                sb.append('m');
            }
        } else {
            char pre = "kMGTPEZ".charAt(exp - 1);
            sb.append(pre);
        }
        sb.append(unit);
        return sb.toString();
    }

    @Override
    public void drawText(GuiGraphics guiGraphics, String text, float x, float y, int color) {
        this.drawText(guiGraphics, Component.literal(text), x, y, color);
    }

    @Override
    public void drawText(GuiGraphics guiGraphics, FormattedText text, float x, float y, int color) {
        FormattedCharSequence sequence;
        if (text instanceof Component component) {
            sequence = component.getVisualOrderText();
        } else {
            sequence = Language.getInstance().getVisualOrder(text);
        }
        this.drawText(guiGraphics, sequence, x, y, color);
    }

    @Override
    public void drawText(GuiGraphics guiGraphics, FormattedCharSequence text, float x, float y, int color) {
        boolean shadow = Jade.CONFIG.get().getOverlay().getTheme().textShadow;
        if (OverlayRenderer.alpha != 1.0F) {
            color = IWailaConfig.IConfigOverlay.applyAlpha(color, OverlayRenderer.alpha);
        }
        betterTextShadow = true;
        guiGraphics.drawString(CLIENT.font, text, (int) x, (int) y, color, shadow);
        betterTextShadow = false;
    }

    public void drawGradientProgress(GuiGraphics guiGraphics, float left, float top, float width, float height, float progress, int progressColor) {
        Color color = Color.rgb(progressColor);
        Color highlight = Color.hsl(color.getHue(), color.getSaturation(), Math.min(color.getLightness() + 0.2, 1.0), color.getOpacity());
        if (progress < 0.1F) {
            this.drawGradientRect(guiGraphics, left, top, width * progress, height, progressColor, highlight.toInt(), true);
        } else {
            float hlWidth = width * 0.1F;
            float normalWidth = width * progress - hlWidth;
            fill(guiGraphics, left, top, left + normalWidth, top + height, progressColor);
            this.drawGradientRect(guiGraphics, left + normalWidth, top, hlWidth, height, progressColor, highlight.toInt(), true);
        }
    }

    @Override
    public MutableComponent stripColor(Component component) {
        MutableComponent mutableComponent = Component.empty();
        component.visit((style, string) -> {
            if (!string.isEmpty()) {
                MutableComponent literal = Component.literal(STRIP_COLOR.matcher(string).replaceAll(""));
                literal.withStyle(style.withColor((TextColor) null));
                mutableComponent.append(literal);
            }
            return Optional.empty();
        }, Style.EMPTY);
        return mutableComponent;
    }

    static {
        for (DecimalFormat format : dfCommasArray) {
            format.setRoundingMode(RoundingMode.DOWN);
        }
    }
}