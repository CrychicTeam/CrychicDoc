package io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon;

import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.client.gui.easy.WidgetAddon;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.NonNullFunction;
import net.minecraftforge.common.util.NonNullSupplier;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class IconButton extends EasyButton {

    public static final int SIZE = 20;

    private NonNullFunction<IconButton, IconData> iconSource;

    public int bgColor = 16777215;

    public IconButton(ScreenPosition pos, Consumer<EasyButton> pressable, @Nonnull IconData icon) {
        this(pos.x, pos.y, pressable, icon);
    }

    public IconButton(int x, int y, Consumer<EasyButton> pressable, @Nonnull IconData icon) {
        super(x, y, 20, 20, pressable);
        this.setIcon(icon);
    }

    public IconButton(ScreenPosition pos, Consumer<EasyButton> pressable, @Nonnull NonNullSupplier<IconData> iconSource) {
        this(pos.x, pos.y, pressable, iconSource);
    }

    public IconButton(int x, int y, Consumer<EasyButton> pressable, @Nonnull NonNullSupplier<IconData> iconSource) {
        super(x, y, 20, 20, pressable);
        this.setIcon(iconSource);
    }

    public IconButton(ScreenPosition pos, Consumer<EasyButton> pressable, @Nonnull NonNullFunction<IconButton, IconData> iconSource) {
        this(pos.x, pos.y, pressable, iconSource);
    }

    public IconButton(int x, int y, Consumer<EasyButton> pressable, @Nonnull NonNullFunction<IconButton, IconData> iconSource) {
        super(x, y, 20, 20, pressable);
        this.setIcon(iconSource);
    }

    public IconButton withAddons(WidgetAddon... addons) {
        this.withAddonsInternal(addons);
        return this;
    }

    public void setIcon(@Nonnull IconData icon) {
        this.iconSource = b -> icon;
    }

    public void setIcon(@Nonnull NonNullSupplier<IconData> iconSource) {
        this.iconSource = b -> iconSource.get();
    }

    public void setIcon(@Nonnull NonNullFunction<IconButton, IconData> iconSource) {
        this.iconSource = iconSource;
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
    public void renderWidget(@NotNull EasyGuiGraphics gui) {
        gui.renderButtonBG(0, 0, this.m_5711_(), this.m_93694_(), this.f_93625_, this.getTextureY(), this.bgColor);
        if (!this.f_93623_) {
            gui.setColor(0.5F, 0.5F, 0.5F, this.f_93625_);
        }
        this.iconSource.apply(this).render(gui, 2, 2);
        gui.resetColor();
    }
}