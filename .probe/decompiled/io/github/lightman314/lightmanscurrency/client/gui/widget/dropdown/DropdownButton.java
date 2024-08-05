package io.github.lightman314.lightmanscurrency.client.gui.widget.dropdown;

import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.client.gui.easy.WidgetAddon;
import io.github.lightman314.lightmanscurrency.client.gui.easy.interfaces.ILateRender;
import io.github.lightman314.lightmanscurrency.client.gui.easy.interfaces.IMouseListener;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyWidget;
import io.github.lightman314.lightmanscurrency.client.util.TextRenderUtil;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DropdownButton extends EasyWidget implements ILateRender, IMouseListener {

    private final Component optionText;

    private final Runnable onPress;

    public DropdownButton(int x, int y, int width, @Nonnull Component optionText, @Nonnull Runnable onPress) {
        super(x, y, width, 12);
        this.onPress = onPress;
        this.optionText = optionText;
    }

    public DropdownButton withAddons(WidgetAddon... addons) {
        this.withAddonsInternal(addons);
        return this;
    }

    @Override
    public void lateRender(@Nonnull EasyGuiGraphics gui) {
        if (this.isVisible()) {
            gui.pushOffset(this.getPosition());
            gui.pushPose().TranslateToForeground();
            int offset = (this.f_93622_ ? this.f_93619_ : 0) + 24;
            if (!this.f_93623_) {
                gui.setColor(0.5F, 0.5F, 0.5F);
            } else {
                gui.resetColor();
            }
            gui.blit(DropdownWidget.GUI_TEXTURE, 0, 0, 0, offset, 2, 12);
            int xOffset = 0;
            while (xOffset < this.f_93618_ - 4) {
                int xPart = Math.min(this.f_93618_ - 4 - xOffset, 252);
                gui.blit(DropdownWidget.GUI_TEXTURE, 2 + xOffset, 0, 2, offset, xPart, 12);
                xOffset += xPart;
            }
            gui.blit(DropdownWidget.GUI_TEXTURE, this.f_93618_ - 2, 0, 254, offset, 2, 12);
            gui.drawString(TextRenderUtil.fitString(this.optionText, this.f_93618_ - 4), 2, 2, 4210752);
            gui.resetColor();
            gui.popOffset();
            gui.popPose();
        }
    }

    @Override
    protected boolean isValidClickButton(int button) {
        return button == 0;
    }

    @Override
    public boolean onMouseClicked(double mouseX, double mouseY, int button) {
        if (this.m_142518_() && this.m_93680_(mouseX, mouseY) && this.isValidClickButton(button)) {
            EasyButton.playClick(Minecraft.getInstance().getSoundManager());
            this.onPress.run();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void renderWidget(@Nonnull EasyGuiGraphics gui) {
    }
}