package com.simibubi.create.foundation.item;

import com.google.common.base.Strings;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Lang;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

public class TooltipHelper {

    public static final int MAX_WIDTH_PER_LINE = 200;

    public static MutableComponent holdShift(TooltipHelper.Palette palette, boolean highlighted) {
        return Lang.translateDirect("tooltip.holdForDescription", Lang.translateDirect("tooltip.keyShift").withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY);
    }

    public static void addHint(List<Component> tooltip, String hintKey, Object... messageParams) {
        Component spacing = IHaveGoggleInformation.componentSpacing;
        tooltip.add(spacing.plainCopy().append(Lang.translateDirect(hintKey + ".title")).withStyle(ChatFormatting.GOLD));
        Component hint = Lang.translateDirect(hintKey);
        for (Component component : cutTextComponent(hint, TooltipHelper.Palette.GRAY_AND_WHITE)) {
            tooltip.add(spacing.plainCopy().append(component));
        }
    }

    public static String makeProgressBar(int length, int filledLength) {
        String bar = " ";
        int emptySpaces = length - filledLength;
        for (int i = 0; i < filledLength; i++) {
            bar = bar + "█";
        }
        for (int i = 0; i < emptySpaces; i++) {
            bar = bar + "▒";
        }
        return bar + " ";
    }

    public static Style styleFromColor(ChatFormatting color) {
        return Style.EMPTY.applyFormat(color);
    }

    public static Style styleFromColor(int hex) {
        return Style.EMPTY.withColor(hex);
    }

    public static List<Component> cutStringTextComponent(String s, TooltipHelper.Palette palette) {
        return cutTextComponent(Components.literal(s), palette);
    }

    public static List<Component> cutTextComponent(Component c, TooltipHelper.Palette palette) {
        return cutTextComponent(c, palette.primary(), palette.highlight());
    }

    public static List<Component> cutStringTextComponent(String s, Style primaryStyle, Style highlightStyle) {
        return cutTextComponent(Components.literal(s), primaryStyle, highlightStyle);
    }

    public static List<Component> cutTextComponent(Component c, Style primaryStyle, Style highlightStyle) {
        return cutTextComponent(c, primaryStyle, highlightStyle, 0);
    }

    public static List<Component> cutStringTextComponent(String c, Style primaryStyle, Style highlightStyle, int indent) {
        return cutTextComponent(Components.literal(c), primaryStyle, highlightStyle, indent);
    }

    public static List<Component> cutTextComponent(Component c, Style primaryStyle, Style highlightStyle, int indent) {
        String s = c.getString();
        List<String> words = new LinkedList();
        BreakIterator iterator = BreakIterator.getLineInstance(Minecraft.getInstance().getLocale());
        iterator.setText(s);
        int start = iterator.first();
        for (int end = iterator.next(); end != -1; end = iterator.next()) {
            String word = s.substring(start, end);
            words.add(word);
            start = end;
        }
        Font font = Minecraft.getInstance().font;
        List<String> lines = new LinkedList();
        StringBuilder currentLine = new StringBuilder();
        int width = 0;
        for (String word : words) {
            int newWidth = font.width(word.replaceAll("_", ""));
            if (width + newWidth > 200) {
                if (width <= 0) {
                    lines.add(word);
                    continue;
                }
                String line = currentLine.toString();
                lines.add(line);
                currentLine = new StringBuilder();
                width = 0;
            }
            currentLine.append(word);
            width += newWidth;
        }
        if (width > 0) {
            lines.add(currentLine.toString());
        }
        MutableComponent lineStart = Components.literal(Strings.repeat(" ", indent));
        lineStart.withStyle(primaryStyle);
        List<Component> formattedLines = new ArrayList(lines.size());
        Couple<Style> styles = Couple.create(highlightStyle, primaryStyle);
        boolean currentlyHighlighted = false;
        for (String string : lines) {
            MutableComponent currentComponent = lineStart.m_6879_();
            String[] split = string.split("_");
            for (String part : split) {
                currentComponent.append(Components.literal(part).withStyle(styles.get(currentlyHighlighted)));
                currentlyHighlighted = !currentlyHighlighted;
            }
            formattedLines.add(currentComponent);
            currentlyHighlighted = !currentlyHighlighted;
        }
        return formattedLines;
    }

    public static record Palette(Style primary, Style highlight) {

        public static final TooltipHelper.Palette STANDARD_CREATE = new TooltipHelper.Palette(TooltipHelper.styleFromColor(13211468), TooltipHelper.styleFromColor(15850873));

        public static final TooltipHelper.Palette BLUE = ofColors(ChatFormatting.BLUE, ChatFormatting.AQUA);

        public static final TooltipHelper.Palette GREEN = ofColors(ChatFormatting.DARK_GREEN, ChatFormatting.GREEN);

        public static final TooltipHelper.Palette YELLOW = ofColors(ChatFormatting.GOLD, ChatFormatting.YELLOW);

        public static final TooltipHelper.Palette RED = ofColors(ChatFormatting.DARK_RED, ChatFormatting.RED);

        public static final TooltipHelper.Palette PURPLE = ofColors(ChatFormatting.DARK_PURPLE, ChatFormatting.LIGHT_PURPLE);

        public static final TooltipHelper.Palette GRAY = ofColors(ChatFormatting.DARK_GRAY, ChatFormatting.GRAY);

        public static final TooltipHelper.Palette ALL_GRAY = ofColors(ChatFormatting.GRAY, ChatFormatting.GRAY);

        public static final TooltipHelper.Palette GRAY_AND_BLUE = ofColors(ChatFormatting.GRAY, ChatFormatting.BLUE);

        public static final TooltipHelper.Palette GRAY_AND_WHITE = ofColors(ChatFormatting.GRAY, ChatFormatting.WHITE);

        public static final TooltipHelper.Palette GRAY_AND_GOLD = ofColors(ChatFormatting.GRAY, ChatFormatting.GOLD);

        public static final TooltipHelper.Palette GRAY_AND_RED = ofColors(ChatFormatting.GRAY, ChatFormatting.RED);

        public static TooltipHelper.Palette ofColors(ChatFormatting primary, ChatFormatting highlight) {
            return new TooltipHelper.Palette(TooltipHelper.styleFromColor(primary), TooltipHelper.styleFromColor(highlight));
        }
    }
}