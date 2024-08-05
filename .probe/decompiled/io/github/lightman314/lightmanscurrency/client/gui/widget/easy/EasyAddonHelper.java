package io.github.lightman314.lightmanscurrency.client.gui.widget.easy;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.client.gui.easy.WidgetAddon;
import io.github.lightman314.lightmanscurrency.client.gui.easy.interfaces.ITooltipSource;
import io.github.lightman314.lightmanscurrency.common.text.MultiLineTextEntry;
import io.github.lightman314.lightmanscurrency.common.text.TextEntry;
import io.github.lightman314.lightmanscurrency.util.MathUtil;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.util.NonNullSupplier;

public class EasyAddonHelper {

    public static WidgetAddon activeCheck(@Nonnull Function<EasyWidget, Boolean> shouldBeActive) {
        return new EasyAddonHelper.ActiveCheckAddon(shouldBeActive);
    }

    public static WidgetAddon activeCheck(@Nonnull NonNullSupplier<Boolean> shouldBeActive) {
        return new EasyAddonHelper.ActiveCheckAddon(b -> shouldBeActive.get());
    }

    public static WidgetAddon visibleCheck(@Nonnull Function<EasyWidget, Boolean> shouldBeVisible) {
        return new EasyAddonHelper.VisibleCheckAddon(shouldBeVisible);
    }

    public static WidgetAddon visibleCheck(@Nonnull NonNullSupplier<Boolean> shouldBeVisible) {
        return new EasyAddonHelper.VisibleCheckAddon(b -> shouldBeVisible.get());
    }

    public static WidgetAddon tooltip(@Nonnull Component tooltip) {
        return new EasyAddonHelper.TooltipAddon(Suppliers.memoize(() -> ImmutableList.of(tooltip)));
    }

    public static WidgetAddon tooltip(@Nonnull TextEntry tooltip) {
        return new EasyAddonHelper.TooltipAddon(Suppliers.memoize(() -> ImmutableList.of(tooltip.get())));
    }

    public static WidgetAddon tooltip(@Nonnull MultiLineTextEntry tooltip) {
        return new EasyAddonHelper.TooltipAddon(Suppliers.memoize(() -> tooltip.get()));
    }

    public static WidgetAddon tooltips(@Nonnull List<Component> tooltip) {
        return new EasyAddonHelper.TooltipAddon(Suppliers.memoize(() -> tooltip));
    }

    public static WidgetAddon tooltip(@Nonnull Supplier<Component> tooltip) {
        return new EasyAddonHelper.TooltipAddon(() -> ImmutableList.of((Component) tooltip.get()));
    }

    public static WidgetAddon tooltips(@Nonnull Supplier<List<Component>> tooltip) {
        return new EasyAddonHelper.TooltipAddon(tooltip);
    }

    public static WidgetAddon tooltip(@Nonnull Component tooltip, int width) {
        return new EasyAddonHelper.TooltipSplitterAddon(tooltip, width);
    }

    public static WidgetAddon tooltip(@Nonnull TextEntry tooltip, int width) {
        return new EasyAddonHelper.TooltipSplitterAddon(tooltip.get(), width);
    }

    @Deprecated
    public static WidgetAddon additiveTooltip(@Nonnull String translationKey, @Nonnull Supplier<Object[]> inputSource) {
        return tooltip(() -> Component.translatable(translationKey, (Object[]) inputSource.get()));
    }

    @Deprecated
    public static WidgetAddon additiveTooltip2(@Nonnull String translationKey, @Nonnull Supplier<Object> inputSource) {
        return tooltip(() -> Component.translatable(translationKey, inputSource.get()));
    }

    public static WidgetAddon toggleTooltip(@Nonnull NonNullSupplier<Boolean> toggle, Component trueTooltip, Component falseTooltip) {
        return tooltip(() -> toggle.get() ? trueTooltip : falseTooltip);
    }

    public static WidgetAddon toggleTooltip(@Nonnull NonNullSupplier<Boolean> toggle, Supplier<Component> trueTooltip, Supplier<Component> falseTooltip) {
        return tooltip(() -> toggle.get() ? (Component) trueTooltip.get() : (Component) falseTooltip.get());
    }

    public static WidgetAddon changingTooltip(@Nonnull NonNullSupplier<Integer> indicator, Component... tooltips) {
        return tooltips.length == 0 ? tooltip(EasyText.empty()) : tooltip(() -> tooltips[MathUtil.clamp(indicator.get(), 0, tooltips.length - 1)]);
    }

    private static class ActiveCheckAddon extends WidgetAddon {

        private final Function<EasyWidget, Boolean> shouldBeActive;

        ActiveCheckAddon(Function<EasyWidget, Boolean> shouldBeActive) {
            this.shouldBeActive = shouldBeActive;
        }

        @Override
        public void activeTick() {
            EasyWidget widget = this.getWidget();
            if (widget != null) {
                widget.setActive((Boolean) this.shouldBeActive.apply(widget));
            }
        }
    }

    private static class TooltipAddon extends WidgetAddon implements ITooltipSource {

        private final Supplier<List<Component>> tooltip;

        TooltipAddon(@Nonnull Supplier<List<Component>> tooltip) {
            this.tooltip = tooltip;
        }

        @Override
        public List<Component> getTooltipText(int mouseX, int mouseY) {
            EasyWidget w = this.getWidget();
            return w != null && w.isActive() && w.getArea().isMouseInArea(mouseX, mouseY) ? (List) this.tooltip.get() : null;
        }
    }

    private static class TooltipSplitterAddon extends WidgetAddon implements ITooltipSource {

        private final Component tooltip;

        private final int width;

        TooltipSplitterAddon(@Nonnull Component tooltip, int width) {
            this.tooltip = tooltip;
            this.width = width;
        }

        @Override
        public List<Component> getTooltipText(int mouseX, int mouseY) {
            return null;
        }

        @Override
        public void renderTooltip(EasyGuiGraphics gui) {
            EasyWidget w = this.getWidget();
            if (w != null && w.isActive() && w.getArea().isMouseInArea(gui.mousePos)) {
                gui.renderTooltip(gui.font.split(this.tooltip, this.width));
            }
        }
    }

    private static class VisibleCheckAddon extends WidgetAddon {

        private final Function<EasyWidget, Boolean> shouldBeVisbile;

        VisibleCheckAddon(Function<EasyWidget, Boolean> shouldBeVisbile) {
            this.shouldBeVisbile = shouldBeVisbile;
        }

        @Override
        public void visibleTick() {
            EasyWidget widget = this.getWidget();
            if (widget != null) {
                widget.setVisible((Boolean) this.shouldBeVisbile.apply(widget));
            }
        }
    }
}