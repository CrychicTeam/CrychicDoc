package io.github.lightman314.lightmanscurrency.client.gui.widget.scroll;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.client.gui.easy.WidgetAddon;
import io.github.lightman314.lightmanscurrency.client.gui.easy.interfaces.IMouseListener;
import io.github.lightman314.lightmanscurrency.client.gui.easy.interfaces.IPreRender;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyWidget;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import io.github.lightman314.lightmanscurrency.util.MathUtil;
import javax.annotation.Nonnull;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ScrollBarWidget extends EasyWidget implements IMouseListener, IPreRender {

    public static final ResourceLocation GUI_TEXTURE = new ResourceLocation("lightmanscurrency", "textures/gui/scroll.png");

    public static final int WIDTH = 8;

    public static final int KNOB_HEIGHT = 29;

    public static final int SMALL_KNOB_HEIGHT = 9;

    private final IScrollable scrollable;

    public boolean smallKnob = false;

    public boolean isDragging = false;

    private int getKnobHeight() {
        return this.smallKnob ? 9 : 29;
    }

    public ScrollBarWidget(ScreenPosition pos, int height, IScrollable scrollable) {
        this(pos.x, pos.y, height, scrollable);
    }

    public ScrollBarWidget(int x, int y, int height, IScrollable scrollable) {
        super(x, y, 8, height);
        this.scrollable = scrollable;
    }

    public ScrollBarWidget withAddons(WidgetAddon... addons) {
        this.withAddonsInternal(addons);
        return this;
    }

    public boolean visible() {
        return this.f_93624_ && this.scrollable.getMaxScroll() > this.scrollable.getMinScroll();
    }

    @Override
    protected void renderTick() {
        if (!this.visible() && this.isDragging) {
            this.isDragging = false;
        }
    }

    @Override
    public void renderWidget(@NotNull EasyGuiGraphics gui) {
        if (this.visible()) {
            gui.resetColor();
            gui.blit(GUI_TEXTURE, 0, 0, 0, 0, 8, 8);
            int yOffset = 8;
            while (yOffset < this.f_93619_ - 8) {
                int yPart = Math.min(this.f_93619_ - 8 - yOffset, 240);
                gui.blit(GUI_TEXTURE, 0, yOffset, 0, 8, 8, yPart);
                yOffset += yPart;
            }
            gui.blit(GUI_TEXTURE, 0, this.f_93619_ - 8, 0, 248, 8, 8);
            int knobPosition;
            if (this.isDragging) {
                knobPosition = MathUtil.clamp(gui.mousePos.y - this.m_252907_() - this.getKnobHeight() / 2, 0, this.f_93619_ - this.getKnobHeight());
            } else {
                knobPosition = this.getNaturalKnobPosition();
            }
            gui.blit(GUI_TEXTURE, 0, knobPosition, this.smallKnob ? 16 : 8, 0, 8, this.getKnobHeight());
        }
    }

    @Override
    public void preRender(@Nonnull EasyGuiGraphics gui) {
        if (this.isDragging) {
            this.dragKnob((double) gui.mousePos.y);
        }
    }

    private int getNaturalKnobPosition() {
        int notches = this.scrollable.getMaxScroll() - this.scrollable.getMinScroll();
        if (notches <= 0) {
            return 0;
        } else {
            double spacing = (double) (this.f_93619_ - this.getKnobHeight()) / (double) notches;
            int scroll = this.scrollable.currentScroll() - this.scrollable.getMinScroll();
            return (int) Math.round((double) scroll * spacing);
        }
    }

    protected void dragKnob(double mouseY) {
        if (!this.visible()) {
            this.isDragging = false;
        } else {
            int scroll = this.getScrollFromMouse(mouseY);
            if (this.scrollable.currentScroll() != scroll) {
                this.scrollable.setScroll(scroll);
            }
        }
    }

    private int getScrollFromMouse(double mouseY) {
        mouseY -= (double) this.getKnobHeight() / 2.0;
        if (mouseY <= (double) this.m_252907_()) {
            return this.scrollable.getMinScroll();
        } else if (mouseY >= (double) (this.m_252907_() + this.f_93619_ - this.getKnobHeight())) {
            return this.scrollable.getMaxScroll();
        } else {
            int deltaScroll = this.scrollable.getMaxScroll() - this.scrollable.getMinScroll();
            if (deltaScroll <= 0) {
                return Integer.MIN_VALUE;
            } else {
                double sectionHeight = (double) (this.f_93619_ - this.getKnobHeight()) / (double) deltaScroll;
                double yPos = (double) this.m_252907_() - sectionHeight / 2.0;
                for (int i = this.scrollable.getMinScroll(); i <= this.scrollable.getMaxScroll(); i++) {
                    if (mouseY >= yPos && mouseY < yPos + sectionHeight) {
                        return i;
                    }
                    yPos += sectionHeight;
                }
                LightmansCurrency.LogWarning("Error getting scroll from mouse position.");
                return this.scrollable.getMinScroll();
            }
        }
    }

    @Deprecated
    @Override
    public boolean onMouseClicked(double mouseX, double mouseY, int button) {
        this.isDragging = false;
        if (this.m_5953_(mouseX, mouseY) && this.visible() && button == 0) {
            LightmansCurrency.LogDebug("Started dragging.");
            this.isDragging = true;
            this.dragKnob(mouseY);
        }
        return false;
    }

    @Deprecated
    @Override
    public boolean onMouseReleased(double mouseX, double mouseY, int button) {
        if (this.isDragging && this.visible() && button == 0) {
            this.dragKnob(mouseY);
            this.isDragging = false;
            LightmansCurrency.LogDebug("Stopped dragging.");
        }
        return false;
    }

    @Override
    public void playDownSound(@NotNull SoundManager soundManager) {
    }
}