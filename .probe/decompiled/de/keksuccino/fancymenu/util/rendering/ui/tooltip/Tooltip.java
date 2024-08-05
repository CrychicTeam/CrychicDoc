package de.keksuccino.fancymenu.util.rendering.ui.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import de.keksuccino.fancymenu.FancyMenu;
import de.keksuccino.fancymenu.util.rendering.DrawableColor;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.fancymenu.util.rendering.ui.UIBase;
import de.keksuccino.fancymenu.util.resource.resources.texture.ITexture;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class Tooltip implements Renderable {

    private static final Logger LOGGER = LogManager.getLogger();

    protected Font font;

    protected List<Component> textLines;

    protected int width;

    protected int height;

    protected int aspectWidth;

    protected int aspectHeight;

    protected Integer x;

    protected Integer y;

    protected int textBorderSize;

    protected int mouseOffset;

    protected ITexture backgroundTexture;

    protected DrawableColor backgroundColor;

    protected DrawableColor borderColor;

    protected boolean vanillaLike;

    protected boolean keepBackgroundAspectRatio;

    protected boolean textShadow;

    protected Tooltip.TooltipTextAlignment textAlignment;

    protected DrawableColor textBaseColor;

    protected Float scale;

    public Tooltip() {
        this.font = Minecraft.getInstance().font;
        this.textLines = new ArrayList();
        this.width = 0;
        this.height = 0;
        this.aspectWidth = -1;
        this.aspectHeight = -1;
        this.x = null;
        this.y = null;
        this.textBorderSize = 5;
        this.mouseOffset = 12;
        this.backgroundTexture = null;
        this.backgroundColor = null;
        this.borderColor = null;
        this.vanillaLike = true;
        this.keepBackgroundAspectRatio = true;
        this.textShadow = true;
        this.textAlignment = Tooltip.TooltipTextAlignment.LEFT;
        this.textBaseColor = null;
        this.scale = null;
    }

    @NotNull
    public static Tooltip empty() {
        return new Tooltip();
    }

    @NotNull
    public static Tooltip of(String... tooltip) {
        Tooltip t = new Tooltip();
        if (tooltip != null) {
            t.setTooltipText(tooltip);
        }
        return t;
    }

    @NotNull
    public static Tooltip of(Component... tooltip) {
        Tooltip t = new Tooltip();
        if (tooltip != null) {
            t.setTooltipText(tooltip);
        }
        return t;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        Screen s = Minecraft.getInstance().screen;
        if (!this.isEmpty() && s != null) {
            this.updateAspectRatio();
            int x = this.calculateX(s, mouseX);
            int y = this.calculateY(s, mouseY);
            RenderSystem.enableBlend();
            graphics.pose().pushPose();
            float scale = 1.0F;
            if (this.scale != null) {
                scale = UIBase.calculateFixedScale(this.scale);
                graphics.pose().scale(scale, scale, scale);
            }
            graphics.pose().translate(0.0F, 0.0F, 600.0F / scale);
            RenderSystem.enableDepthTest();
            RenderingUtils.resetShaderColor(graphics);
            this.renderBackground(graphics, x, y);
            this.renderTextLines(graphics, x, y);
            RenderSystem.disableDepthTest();
            graphics.pose().popPose();
            RenderingUtils.resetShaderColor(graphics);
        }
    }

    protected void renderTextLines(GuiGraphics graphics, int x, int y) {
        int yLine = y;
        for (Component c : this.textLines) {
            int w = this.font.width(c);
            int x2 = x + this.textBorderSize;
            int y2 = yLine + this.textBorderSize;
            if (this.textAlignment == Tooltip.TooltipTextAlignment.RIGHT) {
                int diff = Math.max(0, x + this.getWidth() - this.textBorderSize - (x2 + w));
                x2 += diff;
            }
            if (this.textAlignment == Tooltip.TooltipTextAlignment.CENTERED) {
                x2 = x + Math.max(0, this.getWidth() / 2 - w / 2);
            }
            graphics.drawString(this.font, c, x2, y2, this.textBaseColor != null ? this.textBaseColor.getColorInt() : -1, this.hasTextShadow());
            yLine += 9 + 2;
        }
    }

    protected void renderBackground(GuiGraphics graphics, int x, int y) {
        if (!this.vanillaLike && (this.backgroundTexture != null || this.backgroundColor != null)) {
            if (this.backgroundTexture != null) {
                ResourceLocation loc = this.backgroundTexture.getResourceLocation();
                if (loc != null) {
                    graphics.blit(loc, x, y, 0.0F, 0.0F, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());
                }
            } else if (this.borderColor != null) {
                graphics.fill(x + 1, y + 1, x + this.getWidth() - 1, y + this.getHeight() - 1, this.backgroundColor.getColorInt());
                graphics.fill(x + 1, y, x + this.getWidth() - 1, y + 1, this.borderColor.getColorInt());
                graphics.fill(x + 1, y + this.getHeight() - 1, x + this.getWidth() - 1, y + this.getHeight(), this.borderColor.getColorInt());
                graphics.fill(x, y, x + 1, y + this.getHeight(), this.borderColor.getColorInt());
                graphics.fill(x + this.getWidth() - 1, y, x + this.getWidth(), y + this.getHeight(), this.borderColor.getColorInt());
            } else {
                graphics.fill(x, y, x + this.getWidth(), y + this.getHeight(), this.backgroundColor.getColorInt());
            }
        } else {
            this.renderVanillaLikeBackground(graphics, x, y, this.getWidth(), this.getHeight());
        }
    }

    protected void renderVanillaLikeBackground(GuiGraphics graphics, int x, int y, int width, int height) {
        graphics.pose().pushPose();
        ShaderInstance shaderInstance = RenderSystem.getShader();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder2 = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::m_172811_);
        bufferBuilder2.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        Matrix4f matrix4f2 = graphics.pose().last().pose();
        int z = 0;
        TooltipRenderUtil.renderTooltipBackground(graphics, x, y, width, height, z);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        BufferUploader.drawWithShader(bufferBuilder2.end());
        if (shaderInstance != null) {
            RenderSystem.setShader(() -> shaderInstance);
        }
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderingUtils.resetShaderColor(graphics);
        graphics.pose().popPose();
    }

    protected int calculateX(Screen screen, int mouseX) {
        float scale = 1.0F;
        if (this.scale != null) {
            scale = UIBase.calculateFixedScale(this.scale);
        }
        if (this.x != null) {
            mouseX = this.x;
        }
        int width = this.getWidth();
        if (this.vanillaLike) {
            width += 4;
        }
        int scaledWidth = (int) ((float) width * scale);
        int scaledMouseX = (int) ((float) mouseX / scale) + this.mouseOffset;
        int scaledScreenWidth = (int) ((float) screen.width / scale);
        int x = mouseX + this.mouseOffset;
        if (x + scaledWidth > screen.width) {
            int offset = x + scaledWidth - screen.width;
            x -= offset;
        }
        return (int) ((float) x / scale);
    }

    protected int calculateY(Screen screen, int mouseY) {
        float scale = 1.0F;
        if (this.scale != null) {
            scale = UIBase.calculateFixedScale(this.scale);
        }
        if (this.y != null) {
            mouseY = this.y;
        }
        int height = this.getHeight();
        if (this.vanillaLike) {
            height += 4;
        }
        int scaledHeight = (int) ((float) height * scale);
        int scaledMouseY = (int) ((float) mouseY / scale) + this.mouseOffset;
        int scaledScreenHeight = (int) ((float) screen.height / scale);
        int y = mouseY + this.mouseOffset;
        if (y + scaledHeight > screen.height) {
            int offset = y + scaledHeight - screen.height;
            y -= offset;
        }
        return (int) ((float) y / scale);
    }

    protected int getWidth() {
        return this.aspectWidth != -1 ? this.aspectWidth : this.width;
    }

    protected int getHeight() {
        return this.aspectHeight != -1 ? this.aspectHeight : this.height;
    }

    protected void updateAspectRatio() {
        if (this.keepBackgroundAspectRatio && this.backgroundTexture != null) {
            int[] size = this.backgroundTexture.getAspectRatio().getAspectRatioSizeByMinimumSize(this.width, this.height);
            this.aspectWidth = size[0];
            this.aspectHeight = size[1];
        } else {
            this.aspectWidth = -1;
            this.aspectHeight = -1;
        }
    }

    protected void updateSize() {
        int w = 0;
        int h = 0;
        for (Component c : this.textLines) {
            int wl = this.font.width(c);
            if (wl > w) {
                w = wl;
            }
            h += 9 + 2;
        }
        if (h > 0) {
            h -= 2;
        }
        this.width = w + this.textBorderSize * 2;
        this.height = h + this.textBorderSize * 2;
    }

    public boolean isEmpty() {
        return this.textLines.isEmpty();
    }

    public Tooltip setTooltipText(String... lines) {
        List<Component> l = new ArrayList();
        if (lines != null) {
            for (String s : lines) {
                l.add(Component.literal(s));
            }
        }
        return this.setTooltipText(l);
    }

    public Tooltip setTooltipText(Component... lines) {
        return this.setTooltipText(lines != null ? Arrays.asList(lines) : null);
    }

    public Tooltip setTooltipText(List<Component> lines) {
        this.textLines = (List<Component>) (lines != null ? lines : new ArrayList());
        this.updateSize();
        return this;
    }

    public List<Component> getTooltip() {
        return new ArrayList(this.textLines);
    }

    public Tooltip setTextBorderSize(int size) {
        this.textBorderSize = size;
        this.updateSize();
        return this;
    }

    public int getTextBorderSize() {
        return this.textBorderSize;
    }

    public Tooltip setMouseOffset(int offset) {
        this.mouseOffset = offset;
        return this;
    }

    public int getMouseOffset() {
        return this.mouseOffset;
    }

    public Tooltip setBackgroundTexture(@NotNull ITexture texture) {
        this.backgroundTexture = texture;
        this.backgroundColor = null;
        this.vanillaLike = false;
        return this;
    }

    @Nullable
    public ITexture getBackgroundTexture() {
        return this.backgroundTexture;
    }

    public Tooltip setBackgroundColor(@NotNull DrawableColor backgroundColor, @Nullable DrawableColor borderColor) {
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.backgroundTexture = null;
        this.vanillaLike = false;
        return this;
    }

    public Tooltip setDefaultStyle() {
        this.setTextShadow(FancyMenu.getOptions().enableUiTextShadow.getValue());
        this.setTextBaseColor(UIBase.getUIColorTheme().element_label_color_normal);
        return this.setBackgroundColor(UIBase.getUIColorTheme().element_background_color_normal, UIBase.getUIColorTheme().element_border_color_normal);
    }

    @Nullable
    public DrawableColor getBackgroundColor() {
        return this.backgroundColor;
    }

    @Nullable
    public DrawableColor getBorderColor() {
        return this.borderColor;
    }

    public Tooltip setVanillaLike(boolean vanillaLike) {
        this.vanillaLike = vanillaLike;
        this.backgroundColor = null;
        this.backgroundTexture = null;
        return this;
    }

    public boolean isVanillaLike() {
        return this.vanillaLike;
    }

    public Tooltip setKeepBackgroundAspectRatio(boolean keepBackgroundAspectRatio) {
        this.keepBackgroundAspectRatio = keepBackgroundAspectRatio;
        return this;
    }

    public boolean keepBackgroundAspectRatio() {
        return this.keepBackgroundAspectRatio;
    }

    public Tooltip setTextShadow(boolean textShadow) {
        this.textShadow = textShadow;
        return this;
    }

    public boolean hasTextShadow() {
        return this.textShadow;
    }

    public Tooltip setTextAlignment(Tooltip.TooltipTextAlignment textAlignment) {
        this.textAlignment = textAlignment;
        return this;
    }

    public Tooltip.TooltipTextAlignment getTextAlignment() {
        return this.textAlignment;
    }

    public Tooltip setTextBaseColor(@Nullable DrawableColor textBaseColor) {
        this.textBaseColor = textBaseColor;
        return this;
    }

    @Nullable
    public DrawableColor getTextBaseColor() {
        return this.textBaseColor;
    }

    public Tooltip setFont(Font font) {
        this.font = font != null ? font : Minecraft.getInstance().font;
        this.updateSize();
        return this;
    }

    public Font getFont() {
        return this.font;
    }

    public Tooltip copyStyleOf(Tooltip tooltip) {
        this.borderColor = tooltip.borderColor;
        this.backgroundColor = tooltip.backgroundColor;
        this.backgroundTexture = tooltip.backgroundTexture;
        this.keepBackgroundAspectRatio = tooltip.keepBackgroundAspectRatio;
        this.vanillaLike = tooltip.vanillaLike;
        this.textBorderSize = tooltip.textBorderSize;
        this.textAlignment = tooltip.textAlignment;
        this.textShadow = tooltip.textShadow;
        this.textBaseColor = tooltip.textBaseColor;
        this.font = tooltip.font;
        return this;
    }

    public Tooltip setCustomX(@Nullable Integer x) {
        this.x = x;
        return this;
    }

    @Nullable
    public Integer getCustomX() {
        return this.x;
    }

    public Tooltip setCustomY(@Nullable Integer y) {
        this.y = y;
        return this;
    }

    @Nullable
    public Integer getCustomY() {
        return this.y;
    }

    @Nullable
    public Float getScale() {
        return this.scale;
    }

    public Tooltip setScale(@Nullable Float scale) {
        this.scale = scale;
        return this;
    }

    public static enum TooltipTextAlignment {

        LEFT, RIGHT, CENTERED
    }
}