package dev.xkmc.l2library.util.code;

import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public class TextWrapper {

    public static List<FormattedCharSequence> wrapText(Font font, List<Component> list, int width) {
        int tooltipTextWidth = list.stream().mapToInt(font::m_92852_).max().orElse(0);
        boolean needsWrap = tooltipTextWidth > width;
        return needsWrap ? list.stream().flatMap(text -> font.split(text, width).stream()).toList() : list.stream().map(Component::m_7532_).toList();
    }
}