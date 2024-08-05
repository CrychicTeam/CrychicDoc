package journeymap.client.ui.theme;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import journeymap.client.Constants;
import journeymap.client.api.display.IThemeButton;
import journeymap.client.render.RenderWrapper;
import journeymap.client.render.draw.DrawUtil;
import journeymap.client.texture.Texture;
import journeymap.client.texture.TextureCache;
import journeymap.client.ui.GuiUtils;
import journeymap.client.ui.component.BooleanPropertyButton;
import journeymap.common.properties.config.BooleanField;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;

public class ThemeButton extends BooleanPropertyButton implements IThemeButton {

    protected Theme theme;

    protected Theme.Control.ButtonSpec buttonSpec;

    protected Texture textureOn;

    protected Texture textureHover;

    protected Texture textureOff;

    protected Texture textureDisabled;

    protected Texture textureIcon;

    protected String iconName;

    protected List<FormattedCharSequence> additionalTooltips;

    protected boolean staysOn;

    private boolean displayClickToggle = true;

    public void setDisplayClickToggle(boolean displayClickToggle) {
        this.displayClickToggle = displayClickToggle;
    }

    public ThemeButton(Theme theme, String rawLabel, String iconName, Button.OnPress onPress) {
        this(theme, Constants.getString(rawLabel), Constants.getString(rawLabel), false, iconName, onPress);
    }

    public ThemeButton(Theme theme, String labelOn, String labelOff, boolean toggled, String iconName, Button.OnPress onPress) {
        super(Constants.getString(labelOn), Constants.getString(labelOff), null, onPress);
        this.iconName = iconName;
        this.setToggled(Boolean.valueOf(toggled));
        this.updateTheme(theme);
    }

    public ThemeButton(Theme theme, String labelOn, String labelOff, String iconName, BooleanField field, Button.OnPress onPress) {
        super(Constants.getString(labelOn), Constants.getString(labelOff), field, onPress);
        this.iconName = iconName;
        this.updateTheme(theme);
    }

    public boolean isStaysOn() {
        return this.staysOn;
    }

    @Override
    public void setStaysOn(boolean staysOn) {
        this.staysOn = staysOn;
    }

    public void updateTheme(Theme theme) {
        this.theme = theme;
        this.buttonSpec = this.getButtonSpec(theme);
        if (this.buttonSpec.useThemeImages) {
            String pattern = this.getPathPattern();
            String prefix = this.buttonSpec.prefix;
            this.textureOn = TextureCache.getThemeTexture(theme, String.format(pattern, prefix, "on"));
            this.textureOff = TextureCache.getThemeTexture(theme, String.format(pattern, prefix, "off"));
            this.textureHover = TextureCache.getThemeTexture(theme, String.format(pattern, prefix, "hover"));
            this.textureDisabled = TextureCache.getThemeTexture(theme, String.format(pattern, prefix, "disabled"));
        } else {
            this.textureOn = null;
            this.textureOff = null;
            this.textureHover = null;
            this.textureDisabled = null;
        }
        this.textureIcon = TextureCache.getThemeTexture(theme, String.format("icon/%s.png", this.iconName));
        this.m_93674_(this.buttonSpec.width);
        this.setHeight(this.buttonSpec.height);
        this.setToggled(Boolean.valueOf(false), false);
    }

    public boolean hasValidTextures() {
        return this.buttonSpec.useThemeImages ? this.textureOn.hasImage() && this.textureOff.hasImage() : this.textureIcon != null && this.textureIcon.hasImage();
    }

    protected String getPathPattern() {
        return "control/%sbutton_%s.png";
    }

    protected Theme.Control.ButtonSpec getButtonSpec(Theme theme) {
        return theme.control.button;
    }

    public Theme.Control.ButtonSpec getButtonSpec() {
        return this.buttonSpec;
    }

    protected Texture getActiveTexture(boolean isMouseOver) {
        if (!this.isEnabled()) {
            return this.textureDisabled;
        } else {
            return this.toggled ? this.textureOn : this.textureOff;
        }
    }

    protected Theme.ColorSpec getIconColor(boolean isMouseOver) {
        if (!this.isEnabled()) {
            return this.buttonSpec.iconDisabled;
        } else if (isMouseOver) {
            return this.toggled ? this.buttonSpec.iconHoverOn : this.buttonSpec.iconHoverOff;
        } else {
            return this.toggled ? (this.displayClickToggle ? this.buttonSpec.iconOn : this.buttonSpec.iconOff) : this.buttonSpec.iconOff;
        }
    }

    protected Theme.ColorSpec getButtonColor(boolean isMouseOver) {
        if (!this.isEnabled()) {
            return this.buttonSpec.buttonDisabled;
        } else if (isMouseOver) {
            return this.toggled ? (this.displayClickToggle ? this.buttonSpec.buttonHoverOn : this.buttonSpec.buttonHoverOff) : this.buttonSpec.buttonHoverOff;
        } else {
            return this.toggled ? (this.displayClickToggle ? this.buttonSpec.buttonOn : this.buttonSpec.buttonOff) : this.buttonSpec.buttonOff;
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float ticks) {
        if (this.isVisible()) {
            boolean hover = mouseX >= super.m_252754_() && mouseY >= super.m_252907_() && mouseX < super.m_252754_() + this.f_93618_ && mouseY < super.m_252907_() + this.f_93619_;
            this.setMouseOver(hover);
            Texture activeTexture = this.getActiveTexture(this.f_93622_);
            Theme.ColorSpec iconColorSpec = this.getIconColor(this.f_93622_);
            int drawX = this.m_252754_();
            int drawY = this.m_252907_();
            RenderWrapper.enableDepthTest();
            if (this.buttonSpec.useThemeImages) {
                RenderWrapper.setShader(GameRenderer::m_172817_);
                Theme.ColorSpec buttonColorSpec = this.getButtonColor(this.f_93622_);
                float buttonScale = 1.0F;
                if (this.buttonSpec.width != activeTexture.getWidth()) {
                    buttonScale = 1.0F * (float) this.buttonSpec.width / (float) activeTexture.getWidth();
                }
                DrawUtil.drawColoredImage(graphics.pose(), activeTexture, buttonColorSpec.getColor(), buttonColorSpec.alpha, (double) drawX, (double) drawY, buttonScale, 0.0);
            } else {
                this.drawNativeButton(graphics.pose(), mouseX, mouseY);
            }
            float iconScale = 1.0F;
            if (this.theme.icon.width != this.textureIcon.getWidth()) {
                iconScale = 1.0F * (float) this.theme.icon.width / (float) this.textureIcon.getWidth();
            }
            if (!this.buttonSpec.useThemeImages) {
                DrawUtil.drawColoredImage(graphics.pose(), this.textureIcon, 0, iconColorSpec.alpha, (double) drawX + 0.5, (double) drawY + 0.5, iconScale, 0.0);
            }
            DrawUtil.drawColoredImage(graphics.pose(), this.textureIcon, iconColorSpec.getColor(), iconColorSpec.alpha, (double) drawX, (double) drawY, iconScale, 0.0);
        }
    }

    public void drawNativeButton(PoseStack poseStack, int mouseX, int mouseY) {
        poseStack.pushPose();
        int magic = 20;
        RenderWrapper.setShader(GameRenderer::m_172817_);
        RenderWrapper.setShaderTexture(0, Button.f_93617_);
        RenderWrapper.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int k = this.isEnabled() ? (this.m_274382_() ? 2 : 1) : 0;
        RenderWrapper.enableBlend();
        RenderWrapper.blendFunc(770, 771);
        GuiUtils.drawTexturedModalRect(poseStack, super.m_252754_(), super.m_252907_(), 0, 46 + k * magic, this.f_93618_ / 2, this.f_93619_, 0.0F);
        GuiUtils.drawTexturedModalRect(poseStack, super.m_252754_() + this.f_93618_ / 2, super.m_252907_(), 200 - this.f_93618_ / 2, 46 + k * magic, this.f_93618_ / 2, this.f_93619_, 0.0F);
        this.m_7979_((double) mouseX, (double) mouseY, 0, (double) super.m_252754_(), (double) super.m_252907_());
        int l = 14737632;
        poseStack.popPose();
    }

    public void setAdditionalTooltips(List<FormattedCharSequence> additionalTooltips) {
        this.additionalTooltips = additionalTooltips;
    }

    @Override
    public List<FormattedCharSequence> getFormattedTooltip() {
        if (!this.f_93624_) {
            return null;
        } else {
            List<FormattedCharSequence> list = super.getFormattedTooltip();
            String styleCode = null;
            if (!this.isEnabled()) {
                styleCode = this.buttonSpec.tooltipDisabledStyle;
            } else {
                styleCode = this.toggled ? this.buttonSpec.tooltipOnStyle : this.buttonSpec.tooltipOffStyle;
            }
            Style style;
            if (!styleCode.isEmpty() && !"".equals(styleCode)) {
                style = Style.EMPTY.applyFormat(ChatFormatting.getByCode(styleCode.charAt(1)));
            } else {
                style = Style.EMPTY;
            }
            list.add(0, FormattedCharSequence.forward(this.m_6035_().getString(), style));
            if (this.additionalTooltips != null) {
                list.addAll(this.additionalTooltips);
            }
            return list;
        }
    }

    @Override
    public Button getButton() {
        return this;
    }
}