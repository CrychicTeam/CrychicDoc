package journeymap.client.ui.theme;

import java.util.Arrays;
import java.util.List;
import journeymap.client.render.draw.DrawUtil;
import journeymap.client.texture.Texture;
import journeymap.client.texture.TextureCache;
import journeymap.client.ui.component.Button;
import journeymap.client.ui.component.ButtonList;
import journeymap.client.ui.component.JmUI;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FormattedCharSequence;

public class ThemeToolbar extends Button implements IThemeToolbarInternal {

    private final ButtonList buttonList;

    private Theme theme;

    private Theme.Container.Toolbar.ToolbarSpec toolbarSpec;

    private Texture textureBegin;

    private Texture textureInner;

    private Texture textureEnd;

    public ThemeToolbar(Theme theme, Button... buttons) {
        this(theme, new ButtonList(buttons));
    }

    public ThemeToolbar(Theme theme, ThemeButton... buttons) {
        this(theme, new ButtonList(buttons));
    }

    public ThemeToolbar(Theme theme, ButtonList buttonList) {
        super(buttonList.getWidth(), buttonList.getHeight(), "", emptyPressable());
        this.buttonList = buttonList;
        this.updateTheme(theme);
    }

    public void updateTheme(Theme theme) {
        this.theme = theme;
        this.updateTextures();
    }

    public Theme.Container.Toolbar.ToolbarSpec updateTextures() {
        Theme.Container.Toolbar.ToolbarSpec toolbarSpec;
        if (this.buttonList.isHorizontal()) {
            toolbarSpec = this.theme.container.toolbar.horizontal;
        } else {
            toolbarSpec = this.theme.container.toolbar.vertical;
        }
        this.m_93674_(this.buttonList.getWidth());
        this.setHeight(this.buttonList.getHeight());
        if (this.toolbarSpec == null || toolbarSpec != this.toolbarSpec) {
            this.toolbarSpec = toolbarSpec;
            if (toolbarSpec.useThemeImages) {
                String pathPattern = "container/" + toolbarSpec.prefix + "toolbar_%s.png";
                this.textureBegin = TextureCache.getThemeTexture(this.theme, String.format(pathPattern, "begin"));
                this.textureInner = TextureCache.getThemeTexture(this.theme, String.format(pathPattern, "inner"));
                this.textureEnd = TextureCache.getThemeTexture(this.theme, String.format(pathPattern, "end"));
            }
        }
        return this.toolbarSpec;
    }

    public void updateLayout() {
        this.updateTextures();
        int drawX = this.buttonList.getLeftX() - 1;
        int drawY = this.buttonList.getTopY() - 1;
        this.setScrollablePosition(drawX, drawY);
    }

    public Theme.Container.Toolbar.ToolbarSpec getToolbarSpec() {
        return this.toolbarSpec;
    }

    private ButtonList getButtonList() {
        return this.buttonList;
    }

    public boolean contains(net.minecraft.client.gui.components.Button button) {
        return this.buttonList.contains(button);
    }

    public <B extends Button> void add(B... buttons) {
        this.buttonList.addAll(Arrays.asList(buttons));
    }

    public int getVMargin() {
        if (this.buttonList.isHorizontal()) {
            int heightDiff = (this.toolbarSpec.inner.height - this.theme.control.button.height) / 2;
            return heightDiff + this.toolbarSpec.margin;
        } else {
            return this.toolbarSpec.margin;
        }
    }

    public int getHMargin() {
        if (this.buttonList.isHorizontal()) {
            return this.toolbarSpec.begin.width + this.toolbarSpec.margin;
        } else {
            int widthDiff = (this.toolbarSpec.inner.width - this.theme.control.button.width) / 2;
            return widthDiff + this.toolbarSpec.margin;
        }
    }

    public void setDrawToolbar(boolean draw) {
        super.setDrawButton(draw);
        for (Button button : this.buttonList) {
            button.setDrawButton(draw);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float f) {
        if (this.f_93624_) {
            double drawX = (double) this.m_252754_();
            double drawY = (double) this.m_252907_();
            if (this.toolbarSpec.useThemeImages) {
                if (this.f_93624_) {
                    DrawUtil.drawQuad(graphics.pose(), this.textureBegin, this.toolbarSpec.begin.getColor(), this.toolbarSpec.begin.alpha, drawX, drawY, (double) (this.buttonList.getWidth() + 1), (double) (this.buttonList.getHeight() + 1), false, 0.0);
                }
            }
        }
    }

    @Override
    public int getCenterX() {
        return super.m_252754_() + this.f_93618_ / 2;
    }

    @Override
    public int getMiddleY() {
        return super.m_252907_() + this.f_93619_ / 2;
    }

    @Override
    public int getBottomY() {
        return super.m_252907_() + this.f_93619_;
    }

    @Override
    public int getRightX() {
        return super.m_252754_() + this.f_93618_;
    }

    @Override
    public List<FormattedCharSequence> getFormattedTooltip() {
        return null;
    }

    public void equalizeWidths(Font fr) {
        this.buttonList.equalizeWidths(fr);
    }

    public void equalizeWidths(Font fr, int hgap, int maxTotalWidth) {
        this.buttonList.equalizeWidths(fr, hgap, maxTotalWidth);
    }

    public ButtonList layoutHorizontal(int startX, int y, boolean leftToRight, int hgap) {
        return this.layoutHorizontal(startX, y, leftToRight, hgap, false);
    }

    public ButtonList layoutHorizontal(int startX, int y, boolean leftToRight, int hgap, boolean alignCenter) {
        this.buttonList.layoutHorizontal(startX, y, leftToRight, hgap, alignCenter);
        this.updateLayout();
        return this.buttonList;
    }

    public ButtonList layoutCenteredVertical(int x, int centerY, boolean leftToRight, int vgap) {
        this.buttonList.layoutCenteredVertical(x, centerY, leftToRight, vgap);
        this.updateLayout();
        return this.buttonList;
    }

    public ButtonList layoutVertical(int x, int startY, boolean leftToRight, int vgap) {
        this.buttonList.layoutVertical(x, startY, leftToRight, vgap);
        this.updateLayout();
        return this.buttonList;
    }

    public ButtonList layoutCenteredHorizontal(int centerX, int y, boolean leftToRight, int hgap) {
        this.buttonList.layoutCenteredHorizontal(centerX, y, leftToRight, hgap);
        this.updateLayout();
        return this.buttonList;
    }

    public ButtonList layoutDistributedHorizontal(int leftX, int y, int rightX, boolean leftToRight) {
        this.buttonList.layoutDistributedHorizontal(leftX, y, rightX, leftToRight);
        this.updateLayout();
        return this.buttonList;
    }

    public ButtonList layoutFilledHorizontal(Font fr, int leftX, int y, int rightX, int hgap, boolean leftToRight) {
        this.buttonList.layoutFilledHorizontal(fr, leftX, y, rightX, hgap, leftToRight);
        this.updateLayout();
        return this.buttonList;
    }

    public void setLayout(ButtonList.Layout layout, ButtonList.Direction direction) {
        this.buttonList.setLayout(layout, direction);
        this.updateLayout();
    }

    public ButtonList reverse() {
        this.buttonList.reverse();
        this.updateLayout();
        return this.buttonList;
    }

    public void addAllButtons(JmUI gui) {
        gui.getButtonList().add(this);
        gui.getButtonList().addAll(this.buttonList);
    }

    @Override
    public void setReverse() {
        this.reverse();
    }

    @Override
    public int getToolbarHeight() {
        return this.m_93694_();
    }

    @Override
    public int getToolbarWidth() {
        return this.m_5711_();
    }

    @Override
    public int getToolbarX() {
        return this.m_252754_();
    }

    @Override
    public int getToolbarY() {
        return this.m_252907_();
    }

    @Override
    public void setLayoutHorizontal(int startX, int y, int hgap, boolean leftToRight) {
        this.layoutHorizontal(startX, y, leftToRight, hgap);
    }

    @Override
    public void setLayoutCenteredVertical(int x, int centerY, int vgap, boolean leftToRight) {
        this.layoutCenteredVertical(x, centerY, leftToRight, vgap);
    }

    @Override
    public void setLayoutVertical(int x, int startY, int vgap, boolean leftToRight) {
        this.layoutVertical(x, startY, leftToRight, vgap);
    }

    @Override
    public void setLayoutCenteredHorizontal(int centerX, int y, int hgap, boolean leftToRight) {
        this.layoutCenteredHorizontal(centerX, y, leftToRight, hgap);
    }

    @Override
    public void setLayoutDistributedHorizontal(int leftX, int y, int rightX, boolean leftToRight) {
        this.layoutDistributedHorizontal(leftX, y, rightX, leftToRight);
    }

    @Override
    public void setToolbarPosition(int i, int j) {
        super.m_264152_(i, j);
    }

    @Override
    public void setPosition(int i, int j) {
        super.m_264152_(i, j);
    }
}