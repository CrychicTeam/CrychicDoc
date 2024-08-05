package journeymap.client.ui.component;

import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.Color;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Function;
import journeymap.client.Constants;
import journeymap.client.render.RenderWrapper;
import journeymap.client.render.draw.DrawUtil;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;

public class Button extends net.minecraft.client.gui.components.Button {

    protected Integer customFrameColorLight = new Color(160, 160, 160).getRGB();

    protected Integer customFrameColorDark = new Color(120, 120, 120).getRGB();

    protected Integer customBgColor = new Color(100, 100, 100).getRGB();

    protected Integer customBgHoverColor = new Color(125, 135, 190).getRGB();

    protected Integer customBgHoverColor2 = new Color(100, 100, 100).getRGB();

    protected Integer labelColor;

    protected Integer varLabelColor;

    protected Integer hoverLabelColor;

    protected Integer disabledLabelColor;

    public static final int UNSET_ACTIVE_COLOR = -1;

    protected int packedActiveColor = -1;

    protected Integer disabledBgColor = new Color(45, 45, 45).getRGB();

    protected boolean drawFrame;

    protected boolean drawBackground;

    protected boolean drawBackgroundOnDisable = true;

    protected boolean drawLabelShadow = true;

    protected boolean showDisabledHoverText;

    protected boolean defaultStyle = true;

    protected int WIDTH_PAD = 12;

    protected String[] tooltip;

    protected String label;

    protected Font fontRenderer = Minecraft.getInstance().font;

    protected Double bounds;

    protected ArrayList<Function<Button, Boolean>> clickListeners = new ArrayList(0);

    private int tooltipSize = 200;

    private DrawUtil.HAlign horizontalAlignment = DrawUtil.HAlign.Center;

    private boolean drawHovered = true;

    private Button.HoverState onHoverState;

    private boolean wasHovered = false;

    public Button(String label) {
        this(0, 0, label, emptyPressable());
        this.resetLabelColors();
    }

    public Button(String label, net.minecraft.client.gui.components.Button.OnPress onPress) {
        this(0, 0, label, onPress);
        this.resetLabelColors();
    }

    public Button(int width, int height, String label, net.minecraft.client.gui.components.Button.OnPress onPress) {
        super(0, 0, width, height, Constants.getStringTextComponent(label), onPress, f_252438_);
        this.label = label;
        this.finishInit();
    }

    public static net.minecraft.client.gui.components.Button.OnPress emptyPressable() {
        return button -> {
        };
    }

    public void resetLabelColors() {
        this.labelColor = new Color(14737632).getRGB();
        this.hoverLabelColor = new Color(16777120).getRGB();
        this.disabledLabelColor = Color.gray.getRGB();
    }

    public int getActiveColor() {
        if (this.packedActiveColor != -1) {
            return this.packedActiveColor;
        } else {
            return this.f_93623_ ? 16777215 : 10526880;
        }
    }

    protected void finishInit() {
        this.setEnabled(true);
        this.setDrawButton(true);
        this.setDrawFrame(true);
        this.setDrawBackground(true);
        if (this.f_93619_ == 0) {
            this.setHeight(20);
        }
        if (this.f_93618_ == 0) {
            this.setWidth(200);
        }
        this.updateBounds();
    }

    protected void updateLabel() {
    }

    @Override
    public boolean isActive() {
        return this.isEnabled();
    }

    public int getFitWidth(Font fr) {
        int max = fr.width(this.m_6035_().getString());
        return max + this.WIDTH_PAD + (fr.isBidirectional() ? (int) Math.ceil((double) max * 0.25) : 0);
    }

    public String getLabel() {
        return this.label;
    }

    public void fitWidth(Font fr) {
        this.setWidth(this.getFitWidth(fr));
    }

    public void drawPartialScrollable(GuiGraphics graphics, Minecraft minecraft, int x, int y, int width, int height) {
        int k = 0;
        graphics.blit(f_93617_, x, y, 0, 46 + k * 20, width / 2, height);
        graphics.blit(f_93617_, x + width / 2, y, 200 - width / 2, 46 + k * 20, width / 2, height);
    }

    public void showDisabledOnHover(boolean show) {
        this.showDisabledHoverText = show;
    }

    @Override
    public boolean isMouseOver(double x, double y) {
        return super.m_5953_((double) super.m_252754_(), (double) super.m_252907_());
    }

    public boolean isMouseOver() {
        return super.m_5953_((double) super.m_252754_(), (double) super.m_252907_());
    }

    public void setMouseOver(boolean hover) {
        this.setHovered(hover);
    }

    @Override
    public void playDownSound(SoundManager soundHandler) {
        if (this.isEnabled()) {
            super.m_7435_(soundHandler);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.isVisible()) {
            if (this.defaultStyle) {
                super.m_88315_(graphics, mouseX, mouseY, partialTicks);
            } else {
                Minecraft minecraft = Minecraft.getInstance();
                minecraft.getTextureManager().bindForSetup(f_93617_);
                RenderWrapper.setShader(GameRenderer::m_172817_);
                RenderWrapper.setShaderTexture(0, f_93617_);
                RenderWrapper.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                this.setHovered(mouseX >= super.m_252754_() && mouseY >= super.m_252907_() && mouseX < super.m_252754_() + this.f_93618_ && mouseY < super.m_252907_() + this.f_93619_);
                if (this.isDrawFrame()) {
                    DrawUtil.drawRectangle(graphics.pose(), (double) super.m_252754_(), (double) super.m_252907_(), (double) this.f_93618_, 1.0, this.customFrameColorLight, 1.0F);
                    DrawUtil.drawRectangle(graphics.pose(), (double) super.m_252754_(), (double) super.m_252907_(), 1.0, (double) this.f_93619_, this.customFrameColorLight, 1.0F);
                    DrawUtil.drawRectangle(graphics.pose(), (double) super.m_252754_(), (double) (super.m_252907_() + this.f_93619_ - 1), (double) (this.f_93618_ - 1), 1.0, this.customFrameColorDark, 1.0F);
                    DrawUtil.drawRectangle(graphics.pose(), (double) (super.m_252754_() + this.f_93618_ - 1), (double) (super.m_252907_() + 1), 1.0, (double) (this.f_93619_ - 1), this.customFrameColorDark, 1.0F);
                }
                if (this.isDrawBackground() || !this.drawBackgroundOnDisable && this.isEnabled()) {
                    DrawUtil.drawRectangle(graphics.pose(), (double) (super.m_252754_() + 1), (double) (super.m_252907_() + 1), (double) (this.f_93618_ - 2), (double) (this.f_93619_ - 2), super.f_93622_ && this.drawHovered ? this.customBgHoverColor : this.customBgColor, 1.0F);
                } else if (this.isEnabled() && this.isHovered() && this.drawHovered) {
                    DrawUtil.drawRectangle(graphics.pose(), (double) (super.m_252754_() + 1), (double) (super.m_252907_() + 1), (double) (this.f_93618_ - 2), (double) (this.f_93619_ - 2), this.customBgHoverColor2, 0.5F);
                }
                this.renderBg(graphics, minecraft, mouseX, mouseY);
                this.varLabelColor = this.labelColor;
                if (!this.isEnabled()) {
                    this.varLabelColor = this.disabledLabelColor;
                    if (this.drawBackground) {
                        float alpha = 0.7F;
                        int widthOffset = this.f_93618_ - (this.f_93619_ >= 20 ? 3 : 2);
                        DrawUtil.drawRectangle(graphics.pose(), (double) (this.m_252754_() + 1), (double) (this.m_252907_() + 1), (double) widthOffset, (double) (this.f_93619_ - 2), this.disabledBgColor, alpha);
                    }
                } else if (this.isHovered() && this.drawHovered) {
                    this.varLabelColor = this.hoverLabelColor;
                } else if (this.labelColor != null) {
                    this.varLabelColor = this.labelColor;
                } else if (this.getActiveColor() != 0) {
                    this.varLabelColor = this.getActiveColor();
                }
                DrawUtil.drawLabel(graphics, this.m_6035_().getString(), (double) (switch(this.horizontalAlignment) {
                    case Left ->
                        this.getRightX() - this.WIDTH_PAD / 2;
                    case Right ->
                        this.m_252754_() + this.WIDTH_PAD / 2;
                    default ->
                        this.getCenterX();
                }), (double) this.getMiddleY(), this.horizontalAlignment, DrawUtil.VAlign.Middle, null, 0.0F, this.varLabelColor, 1.0F, 1.0, this.drawLabelShadow, 0.0);
            }
        }
    }

    protected void renderBg(GuiGraphics graphics, Minecraft $$1, int $$2, int $$3) {
    }

    public void renderSpecialDecoration(GuiGraphics graphics, int mouseX, int mouseY, int x, int y, int width, int height) {
    }

    public void setHorizontalAlignment(DrawUtil.HAlign horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    public void drawHovered(boolean drawHovered) {
        this.drawHovered = drawHovered;
    }

    public void drawCenteredString(GuiGraphics graphics, Font fontRenderer, String text, float x, float y, int color) {
        fontRenderer.drawInBatch(text, x - (float) (fontRenderer.width(text) / 2), y, color, true, graphics.pose().last().pose(), graphics.bufferSource(), Font.DisplayMode.SEE_THROUGH, 0, 15728880);
    }

    public void drawUnderline(PoseStack poseStack) {
        if (this.isVisible()) {
            DrawUtil.drawRectangle(poseStack, (double) super.m_252754_(), (double) (super.m_252907_() + this.f_93619_), (double) this.f_93618_, 1.0, this.customFrameColorDark, 1.0F);
        }
    }

    public void secondaryDrawButton() {
    }

    @Override
    public void onPress() {
        if (this.clickListeners != null && this.clickListeners.size() != 0) {
            this.checkClickListeners();
        } else {
            super.onPress();
        }
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        if (this.onHoverState != null) {
            if (this.isHovered() && !this.wasHovered) {
                this.wasHovered = true;
                this.onHoverState.onHoverState(this, this.wasHovered);
            } else if (this.wasHovered && !this.isHovered()) {
                this.wasHovered = false;
                this.onHoverState.onHoverState(this, this.wasHovered);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.m_6375_(mouseX, mouseY, button);
    }

    public boolean mousePressed(double mouseX, double mouseY, boolean checkClickListeners) {
        boolean clicked = this.isEnabled() && this.isVisible() && this.mouseOver(mouseX, mouseY);
        return clicked && (!checkClickListeners || this.checkClickListeners());
    }

    public boolean checkClickListeners() {
        boolean clicked = true;
        if (!this.clickListeners.isEmpty()) {
            try {
                for (Function<Button, Boolean> listener : this.clickListeners) {
                    if (!(Boolean) listener.apply(this)) {
                        break;
                    }
                }
            } catch (Throwable var4) {
                Journeymap.getLogger().error("Error trying to toggle button '" + this.m_6035_() + "': " + LogFormatter.toString(var4));
                clicked = false;
            }
        }
        return clicked;
    }

    public String getUnformattedTooltip() {
        return this.tooltip != null && this.tooltip.length > 0 ? this.tooltip[0] : null;
    }

    public List<FormattedCharSequence> getFormattedTooltip() {
        ArrayList<FormattedCharSequence> list = new ArrayList();
        if (this.tooltip == null) {
            if (!this.isEnabled() && this.showDisabledHoverText) {
                list.add(FormattedCharSequence.forward(Constants.getString("jm.common.disabled_feature"), Style.EMPTY.withItalic(true)));
            }
            return list;
        } else {
            for (String line : this.tooltip) {
                list.addAll(this.fontRenderer.split(Constants.getTranslatedTextComponent(line), this.tooltipSize));
            }
            return list;
        }
    }

    public void setTooltip(String... tooltip) {
        this.tooltip = tooltip;
    }

    public void setTooltip(int size, String... tooltip) {
        this.tooltipSize = size;
        this.tooltip = tooltip;
    }

    public boolean mouseOver(double mouseX, double mouseY) {
        return this.isVisible() && this.getBounds().contains(mouseX, mouseY);
    }

    protected Double updateBounds() {
        this.bounds = new Double((double) this.m_252754_(), (double) this.m_252907_(), (double) this.m_5711_(), (double) this.m_93694_());
        return this.bounds;
    }

    public Double getBounds() {
        return this.bounds == null ? this.updateBounds() : this.bounds;
    }

    public int getScrollableWidth() {
        return this.f_93618_;
    }

    @Override
    public void setWidth(int width) {
        if (this.f_93618_ != width) {
            this.f_93618_ = width;
            this.bounds = null;
        }
    }

    public void setScrollableWidth(int width) {
        this.setWidth(width);
    }

    public int getButtonHeight() {
        return this.m_93694_();
    }

    public void setHeight(int height) {
        if (this.f_93619_ != height) {
            this.f_93619_ = height;
            this.bounds = null;
            if (height != 20) {
                this.defaultStyle = false;
            }
        }
    }

    public void setTextOnly(Font fr) {
        this.setHeight(9 + 1);
        this.fitWidth(fr);
        this.setDrawBackground(false);
        this.setDrawFrame(false);
    }

    public void drawScrollable(GuiGraphics graphics, Minecraft mc, int mouseX, int mouseY) {
        this.render(graphics, mouseX, mouseY, 0.0F);
    }

    public void clickScrollable(Minecraft mc, int mouseX, int mouseY) {
    }

    @Override
    public void setX(int x) {
        if (super.m_252754_() != x) {
            super.m_252865_(x);
            this.bounds = null;
        }
    }

    @Override
    public void setY(int y) {
        if (super.m_252907_() != y) {
            super.m_253211_(y);
            this.bounds = null;
        }
    }

    public int getCenterX() {
        return super.m_252754_() + this.f_93618_ / 2;
    }

    public int getMiddleY() {
        return super.m_252907_() + this.f_93619_ / 2;
    }

    public int getBottomY() {
        return super.m_252907_() + this.f_93619_;
    }

    public int getRightX() {
        return super.m_252754_() + this.f_93618_;
    }

    public void setScrollablePosition(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    public Button leftOf(int x) {
        this.setX(x - this.m_5711_());
        return this;
    }

    public Button rightOf(int x) {
        this.setX(x);
        return this;
    }

    public Button centerHorizontalOn(int x) {
        this.setX(x - this.f_93618_ / 2);
        return this;
    }

    public Button centerVerticalOn(int y) {
        this.setY(y - this.f_93619_ / 2);
        return this;
    }

    public Button leftOf(Button other, int margin) {
        this.setX(other.m_252754_() - this.m_5711_() - margin);
        return this;
    }

    public Button rightOf(Button other, int margin) {
        this.setX(other.m_252754_() + other.m_5711_() + margin);
        return this;
    }

    public Button above(Button other, int margin) {
        this.setY(other.m_252907_() - this.m_93694_() - margin);
        return this;
    }

    public Button above(int y) {
        this.setY(y - this.m_93694_());
        return this;
    }

    public Button below(Button other, int margin) {
        this.setY(other.m_252907_() + other.m_93694_() + margin);
        return this;
    }

    public Button below(ButtonList list, int margin) {
        this.setY(list.getBottomY() + margin);
        return this;
    }

    public Button below(int y) {
        this.setY(y);
        return this;
    }

    public Button alignTo(Button other, DrawUtil.HAlign hAlign, int hgap, DrawUtil.VAlign vAlign, int vgap) {
        int x = this.m_252754_();
        int y = this.m_252907_();
        switch(hAlign) {
            case Left:
                x = other.m_252754_() - hgap;
                break;
            case Right:
                x = other.getRightX() + hgap;
                break;
            case Center:
                x = other.getCenterX();
        }
        switch(vAlign) {
            case Above:
                y = other.m_252907_() - vgap - this.m_93694_();
                break;
            case Below:
                y = other.getBottomY() + vgap;
                break;
            case Middle:
                y = other.getMiddleY() - this.m_93694_() / 2;
        }
        this.setX(x);
        this.setY(y);
        return this;
    }

    public boolean isEnabled() {
        return super.f_93623_;
    }

    public void setEnabled(boolean enabled) {
        super.f_93623_ = enabled;
    }

    public boolean isVisible() {
        return this.f_93624_;
    }

    public void setVisible(boolean visible) {
        this.f_93624_ = visible;
    }

    public void setDrawButton(boolean drawButton) {
        if (drawButton != this.f_93624_) {
            this.f_93624_ = drawButton;
        }
    }

    public boolean isDrawFrame() {
        return this.drawFrame;
    }

    public void setDrawFrame(boolean drawFrame) {
        this.drawFrame = drawFrame;
    }

    public boolean isDrawBackground() {
        return this.drawBackground;
    }

    public void setDrawBackground(boolean drawBackground) {
        this.drawBackground = drawBackground;
    }

    public boolean isDefaultStyle() {
        return this.defaultStyle;
    }

    public void setDefaultStyle(boolean defaultStyle) {
        this.defaultStyle = defaultStyle;
    }

    public void setBackgroundColors(Integer customBgColor, Integer customBgHoverColor, Integer customBgHoverColor2) {
        this.customBgColor = customBgColor;
        this.customBgHoverColor = customBgHoverColor;
        this.customBgHoverColor2 = customBgHoverColor2;
    }

    public void setDrawLabelShadow(boolean draw) {
        this.drawLabelShadow = draw;
    }

    public void setLabelColors(Integer labelColor, Integer hoverLabelColor, Integer disabledLabelColor) {
        this.labelColor = labelColor;
        this.packedActiveColor = labelColor;
        if (hoverLabelColor != null) {
            this.hoverLabelColor = hoverLabelColor;
        }
        if (disabledLabelColor != null) {
            this.disabledLabelColor = disabledLabelColor;
        }
    }

    public String getDisplayString() {
        return this.m_6035_().getString();
    }

    public void setOnHover(Button.HoverState hoverState) {
        this.onHoverState = hoverState;
    }

    public boolean isDrawBackgroundOnDisable() {
        return this.drawBackgroundOnDisable;
    }

    public void setDrawBackgroundOnDisable(boolean drawBackgroundOnDisable) {
        this.drawBackgroundOnDisable = drawBackgroundOnDisable;
    }

    public void refresh() {
    }

    public Integer getLabelColor() {
        return this.labelColor;
    }

    @Override
    public boolean isHovered() {
        return super.m_274382_();
    }

    public void setHovered(boolean hovered) {
        super.f_93622_ = hovered;
    }

    public void addClickListener(Function<Button, Boolean> listener) {
        this.clickListeners.add(listener);
    }

    public String toString() {
        return new StringJoiner(", ", Button.class.getSimpleName() + "[", "]").add("label='" + this.label + "'").toString();
    }

    public interface HoverState {

        void onHoverState(Button var1, boolean var2);
    }
}