package journeymap.client.ui.theme.impl;

import journeymap.client.ui.theme.Theme;
import net.minecraft.ChatFormatting;

class Style {

    Theme.LabelSpec label = new Theme.LabelSpec();

    Style.Colors button = new Style.Colors();

    Style.Colors toggle = new Style.Colors();

    Style.Colors text = new Style.Colors();

    String minimapTexPrefix = "";

    String buttonTexPrefix = "";

    String tooltipOnStyle = ChatFormatting.AQUA.toString();

    String tooltipOffStyle = ChatFormatting.GRAY.toString();

    String tooltipDisabledStyle = ChatFormatting.DARK_GRAY.toString();

    int iconSize = 24;

    Theme.ColorSpec frameColorSpec = new Theme.ColorSpec();

    Theme.ColorSpec toolbarColorSpec = new Theme.ColorSpec();

    Theme.ColorSpec fullscreenColorSpec = new Theme.ColorSpec();

    int squareFrameThickness = 8;

    int circleFrameThickness = 8;

    int toolbarMargin = 4;

    int toolbarPadding = 0;

    boolean useThemeImages = true;

    Style() {
        this.label.margin = 0;
    }

    static class Colors {

        String on;

        String off;

        String hover;

        String disabled;
    }
}