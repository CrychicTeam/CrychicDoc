package de.keksuccino.konkrete.gui.content;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.keksuccino.konkrete.input.MouseInput;
import de.keksuccino.konkrete.rendering.RenderUtils;
import de.keksuccino.konkrete.rendering.animation.IAnimationRenderer;
import de.keksuccino.konkrete.resources.ExternalTextureResourceLocation;
import de.keksuccino.konkrete.sound.SoundHandler;
import java.awt.Color;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class AdvancedButton extends Button {

    protected boolean handleClick = false;

    protected static boolean leftDown = false;

    protected boolean leftDownThis = false;

    protected boolean leftDownNotHovered = false;

    public boolean ignoreBlockedInput = false;

    public boolean ignoreLeftMouseDownClickBlock = false;

    public boolean enableRightclick = false;

    public float labelScale = 1.0F;

    protected boolean useable = true;

    protected boolean labelShadow = true;

    public boolean renderLabel = true;

    protected Color idleColor;

    protected Color hoveredColor;

    protected Color idleBorderColor;

    protected Color hoveredBorderColor;

    protected float borderWidth = 2.0F;

    protected ResourceLocation backgroundHover;

    protected ResourceLocation backgroundNormal;

    protected IAnimationRenderer backgroundAnimationNormal;

    protected IAnimationRenderer backgroundAnimationHover;

    public boolean loopBackgroundAnimations = true;

    public boolean restartBackgroundAnimationsOnHover = true;

    protected boolean lastHoverState = false;

    protected String clicksound = null;

    protected String[] description = null;

    protected Button.OnPress press;

    public AdvancedButton(int x, int y, int widthIn, int heightIn, String buttonText, Button.OnPress onPress) {
        super(x, y, widthIn, heightIn, Component.literal(buttonText), onPress, narration -> Component.literal(buttonText));
        this.press = onPress;
    }

    public AdvancedButton(int x, int y, int widthIn, int heightIn, String buttonText, boolean handleClick, Button.OnPress onPress) {
        super(x, y, widthIn, heightIn, Component.literal(buttonText), onPress, narration -> Component.literal(buttonText));
        this.handleClick = handleClick;
        this.press = onPress;
    }

    @Override
    public void onPress() {
        this.press.onPress(this);
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        PoseStack matrix = graphics.pose();
        if (this.f_93624_) {
            Minecraft mc = Minecraft.getInstance();
            this.f_93622_ = mouseX >= this.f_93620_ && mouseY >= this.f_93621_ && mouseX < this.f_93620_ + this.f_93618_ && mouseY < this.f_93621_ + this.f_93619_;
            if (this.lastHoverState != this.m_198029_() && this.m_198029_() && this.restartBackgroundAnimationsOnHover) {
                if (this.backgroundAnimationNormal != null) {
                    this.backgroundAnimationNormal.resetAnimation();
                }
                if (this.backgroundAnimationHover != null) {
                    this.backgroundAnimationHover.resetAnimation();
                }
            }
            this.lastHoverState = this.m_198029_();
            RenderSystem.enableBlend();
            if (this.hasColorBackground()) {
                Color border;
                if (!this.m_198029_()) {
                    graphics.fill(this.f_93620_, this.f_93621_, this.f_93620_ + this.f_93618_, this.f_93621_ + this.f_93619_, this.idleColor.getRGB() | Mth.ceil(this.f_93625_ * 255.0F) << 24);
                    border = this.idleBorderColor;
                } else if (this.f_93623_) {
                    graphics.fill(this.f_93620_, this.f_93621_, this.f_93620_ + this.f_93618_, this.f_93621_ + this.f_93619_, this.hoveredColor.getRGB() | Mth.ceil(this.f_93625_ * 255.0F) << 24);
                    border = this.hoveredBorderColor;
                } else {
                    graphics.fill(this.f_93620_, this.f_93621_, this.f_93620_ + this.f_93618_, this.f_93621_ + this.f_93619_, this.idleColor.getRGB() | Mth.ceil(this.f_93625_ * 255.0F) << 24);
                    border = this.idleBorderColor;
                }
                if (this.hasBorder()) {
                    RenderUtils.fill(matrix, (float) this.f_93620_, (float) this.f_93621_, (float) (this.f_93620_ + this.f_93618_), (float) this.f_93621_ + this.borderWidth, border.getRGB(), this.f_93625_);
                    RenderUtils.fill(matrix, (float) this.f_93620_, (float) (this.f_93621_ + this.f_93619_) - this.borderWidth, (float) (this.f_93620_ + this.f_93618_), (float) (this.f_93621_ + this.f_93619_), border.getRGB(), this.f_93625_);
                    RenderUtils.fill(matrix, (float) this.f_93620_, (float) this.f_93621_ + this.borderWidth, (float) this.f_93620_ + this.borderWidth, (float) (this.f_93621_ + this.f_93619_) - this.borderWidth, border.getRGB(), this.f_93625_);
                    RenderUtils.fill(matrix, (float) (this.f_93620_ + this.f_93618_) - this.borderWidth, (float) this.f_93621_ + this.borderWidth, (float) (this.f_93620_ + this.f_93618_), (float) (this.f_93621_ + this.f_93619_) - this.borderWidth, border.getRGB(), this.f_93625_);
                }
            } else {
                this.renderBackgroundNormal(graphics);
                this.renderBackgroundHover(graphics);
            }
            this.renderLabel(graphics);
            if (this.m_198029_()) {
                AdvancedButtonHandler.setActiveDescriptionButton(this);
            }
        }
        if (!this.m_198029_() && MouseInput.isLeftMouseDown()) {
            this.leftDownNotHovered = true;
        }
        if (!MouseInput.isLeftMouseDown()) {
            this.leftDownNotHovered = false;
        }
        if (this.handleClick && this.useable) {
            if (this.m_198029_() && (MouseInput.isLeftMouseDown() || this.enableRightclick && MouseInput.isRightMouseDown()) && (!leftDown || this.ignoreLeftMouseDownClickBlock) && !this.leftDownNotHovered && !this.isInputBlocked() && this.f_93623_ && this.f_93624_ && !this.leftDownThis) {
                this.m_5716_((double) mouseX, (double) mouseY);
                if (this.clicksound == null) {
                    this.m_7435_(Minecraft.getInstance().getSoundManager());
                } else {
                    SoundHandler.resetSound(this.clicksound);
                    SoundHandler.playSound(this.clicksound);
                }
                leftDown = true;
                this.leftDownThis = true;
            }
            if (!MouseInput.isLeftMouseDown() && (!MouseInput.isRightMouseDown() || !this.enableRightclick)) {
                leftDown = false;
                this.leftDownThis = false;
            }
        }
    }

    protected void renderBackgroundHover(GuiGraphics graphics) {
        try {
            if (this.m_198029_()) {
                if (this.f_93623_) {
                    if (this.hasCustomBackgroundHover()) {
                        if (this.backgroundHover != null) {
                            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.f_93625_);
                            graphics.blit(this.backgroundHover, this.f_93620_, this.f_93621_, 0.0F, 0.0F, this.f_93618_, this.f_93619_, this.f_93618_, this.f_93619_);
                        } else {
                            int aniX = this.backgroundAnimationHover.getPosX();
                            int aniY = this.backgroundAnimationHover.getPosY();
                            int aniWidth = this.backgroundAnimationHover.getWidth();
                            int aniHeight = this.backgroundAnimationHover.getHeight();
                            boolean aniLoop = this.backgroundAnimationHover.isGettingLooped();
                            this.backgroundAnimationHover.setPosX(this.f_93620_);
                            this.backgroundAnimationHover.setPosY(this.f_93621_);
                            this.backgroundAnimationHover.setWidth(this.f_93618_);
                            this.backgroundAnimationHover.setHeight(this.f_93619_);
                            this.backgroundAnimationHover.setLooped(this.loopBackgroundAnimations);
                            this.backgroundAnimationHover.setOpacity(this.f_93625_);
                            this.backgroundAnimationHover.render(graphics);
                            this.backgroundAnimationHover.setPosX(aniX);
                            this.backgroundAnimationHover.setPosY(aniY);
                            this.backgroundAnimationHover.setWidth(aniWidth);
                            this.backgroundAnimationHover.setHeight(aniHeight);
                            this.backgroundAnimationHover.setLooped(aniLoop);
                            this.backgroundAnimationHover.setOpacity(1.0F);
                        }
                    } else {
                        this.renderDefaultBackground(graphics);
                    }
                } else {
                    this.renderBackgroundNormal(graphics);
                }
            }
        } catch (Exception var7) {
            var7.printStackTrace();
        }
    }

    protected void renderBackgroundNormal(GuiGraphics graphics) {
        try {
            if (!this.m_198029_()) {
                if (this.hasCustomBackgroundNormal()) {
                    if (this.backgroundNormal != null) {
                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.f_93625_);
                        graphics.blit(this.backgroundNormal, this.f_93620_, this.f_93621_, 0.0F, 0.0F, this.f_93618_, this.f_93619_, this.f_93618_, this.f_93619_);
                    } else {
                        int aniX = this.backgroundAnimationNormal.getPosX();
                        int aniY = this.backgroundAnimationNormal.getPosY();
                        int aniWidth = this.backgroundAnimationNormal.getWidth();
                        int aniHeight = this.backgroundAnimationNormal.getHeight();
                        boolean aniLoop = this.backgroundAnimationNormal.isGettingLooped();
                        this.backgroundAnimationNormal.setPosX(this.f_93620_);
                        this.backgroundAnimationNormal.setPosY(this.f_93621_);
                        this.backgroundAnimationNormal.setWidth(this.f_93618_);
                        this.backgroundAnimationNormal.setHeight(this.f_93619_);
                        this.backgroundAnimationNormal.setLooped(this.loopBackgroundAnimations);
                        this.backgroundAnimationNormal.setOpacity(this.f_93625_);
                        this.backgroundAnimationNormal.render(graphics);
                        this.backgroundAnimationNormal.setPosX(aniX);
                        this.backgroundAnimationNormal.setPosY(aniY);
                        this.backgroundAnimationNormal.setWidth(aniWidth);
                        this.backgroundAnimationNormal.setHeight(aniHeight);
                        this.backgroundAnimationNormal.setLooped(aniLoop);
                        this.backgroundAnimationNormal.setOpacity(1.0F);
                    }
                } else {
                    this.renderDefaultBackground(graphics);
                }
            }
        } catch (Exception var7) {
            var7.printStackTrace();
        }
    }

    protected void renderDefaultBackground(GuiGraphics graphics) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.f_93625_);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        graphics.blitNineSliced(f_93617_, this.getX(), this.getY(), this.getWidth(), this.m_93694_(), 20, 4, 200, 20, 0, this.getTextureY());
    }

    private int getTextureY() {
        int i = 1;
        if (!this.f_93623_) {
            i = 0;
        } else if (this.m_198029_()) {
            i = 2;
        }
        return 46 + i * 20;
    }

    protected void renderLabel(GuiGraphics graphics) {
        PoseStack matrix = graphics.pose();
        if (this.renderLabel) {
            Font font = Minecraft.getInstance().font;
            int stringWidth = font.width(this.getMessageString());
            int stringHeight = 8;
            int pX = (int) (((float) (this.f_93620_ + this.f_93618_ / 2) - (float) stringWidth * this.labelScale / 2.0F) / this.labelScale);
            int pY = (int) (((float) (this.f_93621_ + this.f_93619_ / 2) - (float) stringHeight * this.labelScale / 2.0F) / this.labelScale);
            matrix.pushPose();
            matrix.scale(this.labelScale, this.labelScale, this.labelScale);
            graphics.drawString(font, this.getMessageString(), pX, pY, this.getFGColor() | Mth.ceil(this.f_93625_ * 255.0F) << 24, this.labelShadow);
            matrix.popPose();
        }
    }

    protected boolean isInputBlocked() {
        return this.ignoreBlockedInput ? false : MouseInput.isVanillaInputBlocked();
    }

    public void setBackgroundColor(@Nullable Color idle, @Nullable Color hovered, @Nullable Color idleBorder, @Nullable Color hoveredBorder, float borderWidth) {
        this.idleColor = idle;
        this.hoveredColor = hovered;
        this.hoveredBorderColor = hoveredBorder;
        this.idleBorderColor = idleBorder;
        if (borderWidth >= 0.0F) {
            this.borderWidth = borderWidth;
        } else {
            borderWidth = 0.0F;
        }
    }

    public void setBackgroundColor(@Nullable Color idle, @Nullable Color hovered, @Nullable Color idleBorder, @Nullable Color hoveredBorder, int borderWidth) {
        this.setBackgroundColor(idle, hovered, idleBorder, hoveredBorder, (float) borderWidth);
    }

    @Deprecated
    public void setBackgroundTexture(ResourceLocation normal, ResourceLocation hovered) {
        this.backgroundNormal = normal;
        this.backgroundHover = hovered;
    }

    @Deprecated
    public void setBackgroundTexture(ExternalTextureResourceLocation normal, ExternalTextureResourceLocation hovered) {
        if (normal != null) {
            if (!normal.isReady()) {
                normal.loadTexture();
            }
            this.backgroundNormal = normal.getResourceLocation();
        } else {
            this.backgroundNormal = null;
        }
        if (hovered != null) {
            if (!hovered.isReady()) {
                hovered.loadTexture();
            }
            this.backgroundHover = hovered.getResourceLocation();
        } else {
            this.backgroundHover = null;
        }
    }

    public void setBackgroundNormal(ResourceLocation texture) {
        this.backgroundNormal = texture;
    }

    public void setBackgroundNormal(IAnimationRenderer animation) {
        if (animation != null && !animation.isReady()) {
            animation.prepareAnimation();
        }
        this.backgroundAnimationNormal = animation;
    }

    public void setBackgroundHover(ResourceLocation texture) {
        this.backgroundHover = texture;
    }

    public void setBackgroundHover(IAnimationRenderer animation) {
        if (animation != null && !animation.isReady()) {
            animation.prepareAnimation();
        }
        this.backgroundAnimationHover = animation;
    }

    public boolean hasBorder() {
        return this.hasColorBackground() && this.idleBorderColor != null && this.hoveredBorderColor != null;
    }

    public boolean hasColorBackground() {
        return this.idleColor != null && this.hoveredColor != null;
    }

    @Deprecated
    public boolean hasCustomTextureBackground() {
        return this.hasCustomBackground();
    }

    public boolean hasCustomBackground() {
        return (this.backgroundHover != null || this.backgroundAnimationHover != null) && (this.backgroundNormal != null || this.backgroundAnimationNormal != null);
    }

    public boolean hasCustomBackgroundNormal() {
        return this.backgroundNormal != null || this.backgroundAnimationNormal != null;
    }

    public boolean hasCustomBackgroundHover() {
        return this.backgroundHover != null || this.backgroundAnimationHover != null;
    }

    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        if (this.handleClick || !this.useable) {
            return false;
        } else if (this.f_93623_ && this.f_93624_) {
            if (this.m_7972_(p_mouseClicked_5_)) {
                boolean flag = this.m_93680_(p_mouseClicked_1_, p_mouseClicked_3_);
                if (flag) {
                    if (this.clicksound == null) {
                        this.m_7435_(Minecraft.getInstance().getSoundManager());
                    } else {
                        SoundHandler.resetSound(this.clicksound);
                        SoundHandler.playSound(this.clicksound);
                    }
                    this.m_5716_(p_mouseClicked_1_, p_mouseClicked_3_);
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        return this.handleClick ? false : super.m_7933_(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }

    public void setUseable(boolean b) {
        this.useable = b;
    }

    public boolean isUseable() {
        return this.useable;
    }

    public void setHandleClick(boolean b) {
        this.handleClick = b;
    }

    public String getMessageString() {
        return this.m_6035_().getString();
    }

    public void setMessage(String msg) {
        this.m_93666_(Component.literal(msg));
    }

    @Override
    public int getWidth() {
        return this.f_93618_;
    }

    @Override
    public void setWidth(int width) {
        this.f_93618_ = width;
    }

    @Override
    public int getX() {
        return this.f_93620_;
    }

    @Override
    public void setX(int x) {
        this.f_93620_ = x;
    }

    @Override
    public int getY() {
        return this.f_93621_;
    }

    @Override
    public void setY(int y) {
        this.f_93621_ = y;
    }

    public void setHovered(boolean b) {
        this.f_93622_ = b;
    }

    public void setPressAction(Button.OnPress press) {
        this.press = press;
    }

    public void setClickSound(@Nullable String key) {
        this.clicksound = key;
    }

    public void setDescription(String... desc) {
        this.description = desc;
    }

    public String[] getDescription() {
        return this.description;
    }

    public void setLabelShadow(boolean shadow) {
        this.labelShadow = shadow;
    }

    public static boolean isAnyButtonLeftClicked() {
        return leftDown;
    }
}