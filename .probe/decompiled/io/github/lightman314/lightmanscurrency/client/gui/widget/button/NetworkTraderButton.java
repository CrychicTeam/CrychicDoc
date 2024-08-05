package io.github.lightman314.lightmanscurrency.client.gui.widget.button;

import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.client.gui.easy.WidgetAddon;
import io.github.lightman314.lightmanscurrency.client.gui.easy.interfaces.ITooltipWidget;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import io.github.lightman314.lightmanscurrency.client.util.TextRenderUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NetworkTraderButton extends EasyButton implements ITooltipWidget {

    public static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation("lightmanscurrency", "textures/gui/universaltraderbuttons.png");

    public static final int WIDTH = 146;

    public static final int HEIGHT = 30;

    TraderData data;

    public boolean selected = false;

    public TraderData getData() {
        return this.data;
    }

    public NetworkTraderButton(ScreenPosition pos, Consumer<EasyButton> pressable) {
        this(pos.x, pos.y, pressable);
    }

    public NetworkTraderButton(int x, int y, Consumer<EasyButton> pressable) {
        super(x, y, 146, 30, pressable);
    }

    public NetworkTraderButton withAddons(WidgetAddon... addons) {
        this.withAddonsInternal(addons);
        return this;
    }

    public void SetData(TraderData data) {
        this.data = data;
    }

    @Override
    public void renderWidget(@Nonnull EasyGuiGraphics gui) {
        this.f_93623_ = this.data != null && !this.selected;
        if (this.data != null) {
            if (this.f_93623_) {
                gui.resetColor();
            } else {
                gui.setColor(0.5F, 0.5F, 0.5F);
            }
            int offset = 0;
            if (this.f_93622_ || this.selected) {
                offset = 30;
            }
            gui.blit(BUTTON_TEXTURES, 0, 0, 0, offset, 146, 30);
            this.data.getIcon().render(gui, 4, 7);
            int color = this.data.getTerminalTextColor();
            gui.drawString(TextRenderUtil.fitString(this.data.getName(), this.f_93618_ - 26), 24, 6, color);
            gui.drawString(TextRenderUtil.fitString(this.data.getOwner().getName(), this.f_93618_ - 26), 24, 16, 4210752);
            gui.resetColor();
        }
    }

    @Override
    public List<Component> getTooltipText() {
        TraderData trader = this.getData();
        return (List<Component>) (trader == null ? new ArrayList() : trader.getTerminalInfo(Minecraft.getInstance().player));
    }
}