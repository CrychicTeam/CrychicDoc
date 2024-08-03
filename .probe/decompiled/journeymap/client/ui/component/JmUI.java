package journeymap.client.ui.component;

import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import journeymap.client.Constants;
import journeymap.client.render.RenderWrapper;
import journeymap.client.render.draw.DrawUtil;
import journeymap.client.texture.Texture;
import journeymap.client.texture.TextureCache;
import journeymap.client.ui.UIManager;
import journeymap.common.Journeymap;
import journeymap.common.accessors.ScreenAccess;
import journeymap.common.log.LogFormatter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import org.apache.logging.log4j.Logger;

public abstract class JmUI extends Screen implements ScreenAccess {

    protected final String title;

    protected final int headerHeight = 35;

    protected final Logger logger = Journeymap.getLogger();

    protected Texture logo = TextureCache.getTexture(TextureCache.Logo);

    protected static Stack<Screen> returnDisplayStack = new Stack();

    protected boolean renderBottomBar = false;

    public JmUI(String title) {
        this(title, null);
    }

    public JmUI(String title, Screen returnDisplay) {
        super(Constants.getStringTextComponent(title));
        this.title = title;
        returnDisplayStack.push(returnDisplay);
    }

    public Minecraft getMinecraft() {
        return this.f_96541_ = Minecraft.getInstance();
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    public Font getFontRenderer() {
        return super.font;
    }

    public void sizeDisplay(PoseStack poseStack, boolean scaled) {
        int glwidth = scaled ? this.f_96543_ : this.f_96541_.getWindow().getScreenWidth();
        int glheight = scaled ? this.f_96544_ : this.f_96541_.getWindow().getScreenHeight();
        DrawUtil.sizeDisplay(poseStack, (double) glwidth, (double) glheight);
    }

    public double getScaleFactor() {
        double scaleFactor = this.f_96541_.getWindow().getGuiScale();
        int monitorWidth = this.f_96541_.getWindow().findBestMonitor().getCurrentMode().getWidth();
        int windowWidth = this.f_96541_.getWindow().getWidth();
        int screenWidth = this.f_96541_.getWindow().getScreenWidth();
        int scaledWidth = this.f_96541_.getWindow().getGuiScaledWidth();
        if (Minecraft.ON_OSX && windowWidth > monitorWidth) {
            scaleFactor = this.f_96541_.getWindow().getGuiScale() / (double) (windowWidth / scaledWidth);
            if (screenWidth == monitorWidth) {
                scaleFactor = this.f_96541_.getWindow().getGuiScale() / (double) (windowWidth / monitorWidth);
            }
        }
        return scaleFactor;
    }

    protected boolean isMouseOverButton(double mouseX, double mouseY) {
        for (int k = 0; k < this.getRenderables().size(); k++) {
            net.minecraft.client.gui.components.Button guibutton = (net.minecraft.client.gui.components.Button) this.getRenderables().get(k);
            if (guibutton instanceof Button button && button.mouseOver(mouseX, mouseY)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseEvent) {
        return super.m_6348_(mouseX, mouseY, mouseEvent);
    }

    protected void drawLogo(PoseStack poseStack) {
        if (!this.logo.hasImage()) {
            this.logo = TextureCache.getTexture(TextureCache.Logo);
        }
        DrawUtil.sizeDisplay(poseStack, (double) this.f_96541_.getWindow().getScreenWidth(), (double) this.f_96541_.getWindow().getScreenHeight());
        DrawUtil.drawImage(poseStack, this.logo, 8.0, 8.0, false, 0.5F, 0.0);
        DrawUtil.sizeDisplay(poseStack, (double) this.f_96543_, (double) this.f_96544_);
    }

    protected void renderBottomBar(PoseStack stack) {
        if (this.renderBottomBar) {
            DrawUtil.drawRectangle(stack, 0.0, (double) (this.f_96544_ - 30), (double) this.f_96543_, (double) this.f_96544_, 0, 0.6F);
        }
    }

    protected void drawTitle(GuiGraphics graphics) {
        DrawUtil.drawRectangle(graphics.pose(), 0.0, 0.0, (double) this.f_96543_, 35.0, 0, 0.9F);
        DrawUtil.drawLabel(graphics, this.title, (double) (this.f_96543_ / 2), 17.0, DrawUtil.HAlign.Center, DrawUtil.VAlign.Middle, 0, 0.0F, Color.CYAN.getRGB(), 1.0F, 1.0, true, 0.0);
        String apiVersion = "API v1.9-SNAPSHOT";
        DrawUtil.drawLabel(graphics, apiVersion, (double) (this.f_96543_ - 10), 17.0, DrawUtil.HAlign.Left, DrawUtil.VAlign.Middle, 0, 0.0F, 13421772, 1.0F, 0.5, true, 0.0);
    }

    @Override
    public void init() {
        this.getRenderables().clear();
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics) {
        if (Minecraft.getInstance().level == null) {
            this.drawGradientRect(guiGraphics, 0, 0, this.f_96543_, this.f_96544_, -1072689136, -804253680, 0);
        } else {
            super.renderBackground(guiGraphics);
        }
    }

    protected abstract void layoutButtons(GuiGraphics var1);

    public List getButtonList() {
        return this.getRenderables();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int x, int y, float par3) {
        try {
            PoseStack poseStack = guiGraphics.pose();
            this.renderBackground(guiGraphics);
            this.renderBottomBar(poseStack);
            this.layoutButtons(guiGraphics);
            this.drawTitle(guiGraphics);
            this.drawLogo(poseStack);
            List<FormattedCharSequence> tooltip = null;
            for (int k = 0; k < this.getRenderables().size(); k++) {
                net.minecraft.client.gui.components.Button guibutton = (net.minecraft.client.gui.components.Button) this.getRenderables().get(k);
                guibutton.m_88315_(guiGraphics, x, y, 0.0F);
                if (tooltip == null && guibutton instanceof Button) {
                    Button button = (Button) guibutton;
                    if (button.mouseOver((double) x, (double) y)) {
                        tooltip = button.getFormattedTooltip();
                    }
                }
            }
            if (tooltip != null && !tooltip.isEmpty()) {
                this.renderWrappedToolTip(guiGraphics, tooltip, x, y, this.getFontRenderer());
            }
        } catch (Throwable var10) {
            Journeymap.getLogger().error("Error in UI: " + LogFormatter.toString(var10));
            this.closeAndReturn();
        }
    }

    public void drawGradientRect(GuiGraphics guiGraphics, int pX1, int pY1, int pX2, int pY2, int pColorFrom, int pColorTo, int pBlitOffset) {
        guiGraphics.fillGradient(RenderType.guiOverlay(), pX1, pY1, pX2, pY2, pColorFrom, pColorTo, pBlitOffset);
    }

    public void close() {
    }

    protected void closeAndReturn() {
        if (returnDisplayStack != null && returnDisplayStack.peek() != null) {
            UIManager.INSTANCE.open((Screen) returnDisplayStack.pop());
        } else if (this.f_96541_.level != null) {
            UIManager.INSTANCE.openFullscreenMap();
        } else {
            UIManager.INSTANCE.closeAll();
        }
    }

    @Override
    public boolean charTyped(char c, int i) {
        return super.m_5534_(c, i);
    }

    public void setRenderBottomBar(boolean renderBottomBar) {
        this.renderBottomBar = renderBottomBar;
    }

    @Override
    public boolean keyPressed(int key, int value, int modifier) {
        switch(key) {
            case 256:
                this.closeAndReturn();
                return true;
            default:
                return super.keyPressed(key, value, modifier);
        }
    }

    public void renderTooltip(GuiGraphics graphics, String[] tooltip, int mouseX, int mouseY) {
        List<FormattedCharSequence> tooltips = (List<FormattedCharSequence>) Arrays.stream(tooltip).map(e -> FormattedCharSequence.forward(e, Style.EMPTY)).collect(Collectors.toList());
        this.renderWrappedToolTip(graphics, tooltips, mouseX, mouseY, this.getFontRenderer());
    }

    public Screen getReturnDisplay() {
        return (Screen) returnDisplayStack.peek();
    }

    public void renderWrappedToolTip(GuiGraphics graphics, List tooltip, int mouseX, int mouseY, Font fontRenderer) {
        if (!tooltip.isEmpty() && tooltip.get(0) instanceof FormattedCharSequence && Minecraft.getInstance().screen == this) {
            RenderWrapper.disableDepthTest();
            int maxLineWidth = 0;
            for (FormattedCharSequence line : tooltip) {
                int lineWidth = fontRenderer.width(line);
                if (fontRenderer.isBidirectional()) {
                    lineWidth = (int) Math.ceil((double) lineWidth * 1.25);
                }
                if (lineWidth > maxLineWidth) {
                    maxLineWidth = lineWidth;
                }
            }
            int drawX = mouseX + 12;
            int drawY = mouseY - 12;
            int boxHeight = 8;
            if (tooltip.size() > 1) {
                boxHeight += 2 + (tooltip.size() - 1) * 10;
            }
            if (drawX + maxLineWidth > this.f_96543_) {
                drawX -= 28 + maxLineWidth;
            }
            if (drawY + boxHeight + 6 > this.f_96544_) {
                drawY = this.f_96544_ - boxHeight - 6;
            }
            int j1 = -267386864;
            this.drawGradientRect(graphics, drawX - 3, drawY - 4, drawX + maxLineWidth + 3, drawY - 3, j1, j1, 300);
            this.drawGradientRect(graphics, drawX - 3, drawY + boxHeight + 3, drawX + maxLineWidth + 3, drawY + boxHeight + 4, j1, j1, 300);
            this.drawGradientRect(graphics, drawX - 3, drawY - 3, drawX + maxLineWidth + 3, drawY + boxHeight + 3, j1, j1, 300);
            this.drawGradientRect(graphics, drawX - 4, drawY - 3, drawX - 3, drawY + boxHeight + 3, j1, j1, 300);
            this.drawGradientRect(graphics, drawX + maxLineWidth + 3, drawY - 3, drawX + maxLineWidth + 4, drawY + boxHeight + 3, j1, j1, 300);
            int k1 = 1347420415;
            int l1 = (k1 & 16711422) >> 1 | k1 & 0xFF000000;
            this.drawGradientRect(graphics, drawX - 3, drawY - 3 + 1, drawX - 3 + 1, drawY + boxHeight + 3 - 1, k1, l1, 300);
            this.drawGradientRect(graphics, drawX + maxLineWidth + 2, drawY - 3 + 1, drawX + maxLineWidth + 3, drawY + boxHeight + 3 - 1, k1, l1, 300);
            this.drawGradientRect(graphics, drawX - 3, drawY - 3, drawX + maxLineWidth + 3, drawY - 3 + 1, k1, k1, 300);
            this.drawGradientRect(graphics, drawX - 3, drawY + boxHeight + 2, drawX + maxLineWidth + 3, drawY + boxHeight + 3, l1, l1, 300);
            for (int i2 = 0; i2 < tooltip.size(); i2++) {
                FormattedCharSequence line = (FormattedCharSequence) tooltip.get(i2);
                if (fontRenderer.isBidirectional()) {
                    int lineWidthx = (int) Math.ceil((double) fontRenderer.width(line) * 1.1);
                    fontRenderer.drawInBatch(line, (float) (drawX + maxLineWidth - lineWidthx), (float) drawY, -1, true, graphics.pose().last().pose(), graphics.bufferSource(), Font.DisplayMode.SEE_THROUGH, 0, 15728880);
                } else {
                    fontRenderer.drawInBatch(line, (float) drawX, (float) drawY, -1, true, graphics.pose().last().pose(), graphics.bufferSource(), Font.DisplayMode.SEE_THROUGH, 0, 15728880);
                }
                if (i2 == 0) {
                    drawY += 2;
                }
                drawY += 10;
            }
            RenderWrapper.enableDepthTest();
        }
    }

    @Override
    public List<Renderable> getRenderables() {
        return super.renderables;
    }
}