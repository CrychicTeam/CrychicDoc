package io.github.lightman314.lightmanscurrency.client.gui.widget.button.tab;

import com.google.common.collect.ImmutableList;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.client.gui.easy.WidgetAddon;
import io.github.lightman314.lightmanscurrency.client.gui.easy.interfaces.ITooltipSource;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.util.IconAndButtonUtil;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import io.github.lightman314.lightmanscurrency.util.MathUtil;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class TabButton extends EasyButton implements ITooltipSource {

    public static final ResourceLocation GUI_TEXTURE = IconAndButtonUtil.WIDGET_TEXTURE;

    public static final int SIZE = 25;

    public boolean hideTooltip = false;

    public final ITab tab;

    private int rotation = 0;

    public TabButton(Consumer<EasyButton> pressable, ITab tab) {
        super(0, 0, 25, 25, pressable);
        this.tab = tab;
    }

    public TabButton withAddons(WidgetAddon... addons) {
        this.withAddonsInternal(addons);
        return this;
    }

    public void reposition(ScreenPosition pos, int rotation) {
        this.reposition(pos.x, pos.y, rotation);
    }

    public void reposition(int x, int y, int rotation) {
        this.m_264152_(x, y);
        this.rotation = MathUtil.clamp(rotation, 0, 3);
    }

    @Override
    public void renderWidget(@NotNull EasyGuiGraphics gui) {
        float r = (float) (this.getColor() >> 16 & 0xFF) / 255.0F;
        float g = (float) (this.getColor() >> 8 & 0xFF) / 255.0F;
        float b = (float) (this.getColor() & 0xFF) / 255.0F;
        float m = this.f_93623_ ? 1.0F : 0.5F;
        gui.setColor(r * m, g * m, b * m, 1.0F);
        int xOffset = this.rotation < 2 ? 0 : this.f_93618_;
        int yOffset = (this.rotation % 2 == 0 ? 0 : 2 * this.f_93619_) + (this.f_93623_ ? 0 : this.f_93619_);
        gui.blit(GUI_TEXTURE, 0, 0, 200 + xOffset, yOffset, this.f_93618_, this.f_93619_);
        gui.setColor(m, m, m);
        this.tab.getIcon().render(gui, 4, 4);
        gui.resetColor();
    }

    protected int getColor() {
        return this.tab.getColor();
    }

    @Override
    public List<Component> getTooltipText(int mouseX, int mouseY) {
        if (!this.hideTooltip && this.isVisible()) {
            return this.getArea().isMouseInArea(mouseX, mouseY) ? ImmutableList.of(this.tab.getTooltip()) : null;
        } else {
            return null;
        }
    }
}