package snownee.jade.api.view;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IWailaConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IBoxElement;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.api.ui.ITooltipRenderer;
import snownee.jade.impl.ui.HorizontalLineElement;
import snownee.jade.impl.ui.ScaledTextElement;

public class ClientViewGroup<T> {

    public final List<T> views;

    @Nullable
    public Component title;

    public int bgColor;

    public int progressColor;

    public float progress;

    @Nullable
    public CompoundTag extraData;

    public ClientViewGroup(List<T> views) {
        this.views = views;
    }

    public boolean shouldRenderGroup() {
        return this.title != null || this.bgColor != 0 || this.progressColor != 0;
    }

    public static <IN, OUT> List<ClientViewGroup<OUT>> map(List<ViewGroup<IN>> groups, Function<IN, OUT> itemFactory, @Nullable BiConsumer<ViewGroup<IN>, ClientViewGroup<OUT>> clientGroupDecorator) {
        return groups.stream().map($ -> {
            ClientViewGroup<OUT> group = new ClientViewGroup($.views.stream().map(itemFactory).filter(Objects::nonNull).toList());
            if ($.extraData != null && $.getExtraData().contains("Progress")) {
                group.progress = $.getExtraData().getFloat("Progress");
                group.progressColor = -3355444;
            }
            if (clientGroupDecorator != null) {
                clientGroupDecorator.accept($, group);
            }
            group.extraData = $.extraData;
            return group;
        }).toList();
    }

    public static <T> void tooltip(ITooltip tooltip, List<ClientViewGroup<T>> groups, boolean renderGroup, BiConsumer<ITooltip, ClientViewGroup<T>> consumer) {
        for (ClientViewGroup<T> group : groups) {
            ITooltip theTooltip = renderGroup ? IElementHelper.get().tooltip() : tooltip;
            consumer.accept(theTooltip, group);
            if (renderGroup) {
                BoxStyle boxStyle = new BoxStyle();
                boxStyle.borderColor = group.bgColor;
                boxStyle.bgColor = group.bgColor;
                boxStyle.progress = group.progress;
                boxStyle.progressColor = group.progressColor;
                boxStyle.borderWidth = 0.75F;
                boxStyle.roundCorner = !IWailaConfig.get().getOverlay().getSquare();
                IBoxElement box = IElementHelper.get().box(theTooltip, boxStyle);
                ITooltipRenderer tooltipRenderer = box.getTooltipRenderer();
                tooltipRenderer.setPadding(0, group.title == null ? 2 : 0);
                tooltipRenderer.setPadding(3, 2);
                tooltipRenderer.setPadding(1, 2);
                tooltipRenderer.recalculateSize();
                tooltip.add(box);
            }
        }
    }

    public void renderHeader(ITooltip tooltip) {
        if (this.title != null) {
            tooltip.add(new HorizontalLineElement());
            tooltip.append(new ScaledTextElement(this.title, 0.5F));
            tooltip.append(new HorizontalLineElement());
        } else if (this.bgColor == 0) {
            tooltip.add(new HorizontalLineElement());
        }
    }
}