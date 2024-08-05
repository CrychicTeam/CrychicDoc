package fuzs.puzzleslib.api.client.screen.v2;

import fuzs.puzzleslib.mixin.client.accessor.TooltipAccessor;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;

public final class ScreenTooltipFactory {

    private ScreenTooltipFactory() {
    }

    @Deprecated(forRemoval = true)
    public static Tooltip create(Font font, FormattedText... components) {
        return create(components);
    }

    public static Tooltip create(FormattedText... components) {
        return create(Arrays.asList(components));
    }

    @Deprecated(forRemoval = true)
    public static Tooltip create(Font font, List<? extends FormattedText> components) {
        return create(components);
    }

    public static Tooltip create(List<? extends FormattedText> components) {
        Font font = Minecraft.getInstance().font;
        List<FormattedCharSequence> lines = (List<FormattedCharSequence>) components.stream().flatMap(t -> {
            List<FormattedCharSequence> list = font.split(t, 170);
            if (list.isEmpty()) {
                list = List.of(FormattedCharSequence.EMPTY);
            }
            return list.stream();
        }).collect(Collectors.toList());
        return createTooltip(lines);
    }

    public static Tooltip createTooltip(List<FormattedCharSequence> lines) {
        Tooltip tooltip = Tooltip.create(CommonComponents.EMPTY, null);
        ((TooltipAccessor) tooltip).puzzleslib$setCachedTooltip(lines);
        return tooltip;
    }
}