package io.github.lightman314.lightmanscurrency.client.gui.widget.easy;

import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.client.gui.easy.WidgetAddon;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraftforge.common.util.NonNullSupplier;

public class EasyTextButton extends EasyButton {

    private NonNullSupplier<Component> text;

    public EasyTextButton(int x, int y, int width, int height, @Nonnull Component text, @Nonnull Consumer<EasyButton> press) {
        this(ScreenArea.of(x, y, width, height), () -> text, press);
    }

    public EasyTextButton(int x, int y, int width, int height, @Nonnull Component text, @Nonnull Runnable press) {
        this(ScreenArea.of(x, y, width, height), () -> text, (Consumer<EasyButton>) (b -> press.run()));
    }

    public EasyTextButton(@Nonnull ScreenPosition pos, int width, int height, @Nonnull Component text, @Nonnull Consumer<EasyButton> press) {
        this(pos.asArea(width, height), () -> text, press);
    }

    public EasyTextButton(@Nonnull ScreenPosition pos, int width, int height, @Nonnull Component text, @Nonnull Runnable press) {
        this(pos.asArea(width, height), () -> text, (Consumer<EasyButton>) (b -> press.run()));
    }

    public EasyTextButton(@Nonnull ScreenArea area, @Nonnull Component text, @Nonnull Consumer<EasyButton> press) {
        this(area, () -> text, press);
    }

    public EasyTextButton(@Nonnull ScreenArea area, @Nonnull Component text, @Nonnull Runnable press) {
        this(area, () -> text, (Consumer<EasyButton>) (b -> press.run()));
    }

    public EasyTextButton(int x, int y, int width, int height, @Nonnull NonNullSupplier<Component> text, @Nonnull Consumer<EasyButton> press) {
        this(ScreenArea.of(x, y, width, height), text, press);
    }

    public EasyTextButton(int x, int y, int width, int height, @Nonnull NonNullSupplier<Component> text, @Nonnull Runnable press) {
        this(ScreenArea.of(x, y, width, height), text, (Consumer<EasyButton>) (b -> press.run()));
    }

    public EasyTextButton(@Nonnull ScreenPosition pos, int width, int height, @Nonnull NonNullSupplier<Component> text, @Nonnull Consumer<EasyButton> press) {
        this(pos.asArea(width, height), text, press);
    }

    public EasyTextButton(@Nonnull ScreenPosition pos, int width, int height, @Nonnull NonNullSupplier<Component> text, @Nonnull Runnable press) {
        this(pos.asArea(width, height), text, (Consumer<EasyButton>) (b -> press.run()));
    }

    public EasyTextButton(@Nonnull ScreenArea area, @Nonnull NonNullSupplier<Component> text, @Nonnull Runnable press) {
        this(area, text, (Consumer<EasyButton>) (b -> press.run()));
    }

    public EasyTextButton(@Nonnull ScreenArea area, @Nonnull NonNullSupplier<Component> text, @Nonnull Consumer<EasyButton> press) {
        super(area, press);
        this.text = text;
    }

    public final EasyTextButton withAddons(WidgetAddon... addons) {
        this.withAddonsInternal(addons);
        return this;
    }

    @Override
    public final void setMessage(@Nonnull Component text) {
        this.text = () -> text;
    }

    public final void setMessage(@Nonnull NonNullSupplier<Component> text) {
        this.text = text;
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

    @Override
    protected void renderWidget(@Nonnull EasyGuiGraphics gui) {
        gui.renderButtonBG(0, 0, this.m_5711_(), this.m_93694_(), this.f_93625_, this.getTextureY());
        int i = this.getFGColor();
        this.m_280372_(gui.getGui(), gui.font, 2, i | Mth.ceil(this.f_93625_ * 255.0F) << 24);
    }

    @Override
    protected void renderTick() {
        super.m_93666_(this.text.get());
    }
}