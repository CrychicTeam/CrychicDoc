package journeymap.client.ui.theme.impl;

import java.util.Arrays;
import journeymap.client.ui.theme.Theme;
import journeymap.client.ui.theme.ThemePresets;

public class FlatTheme extends Theme {

    public static Theme createPurist() {
        Style style = new Style();
        style.button.on = "#ffffff";
        style.button.off = "#aaaaaa";
        style.button.hover = "#00ffff";
        style.button.disabled = "#aaaaaa";
        style.toggle.on = "#aaaaaa";
        style.toggle.off = "#ffffff";
        style.toggle.hover = "#00ffff";
        style.toggle.disabled = "#aaaaaa";
        style.label.background.alpha = 0.6F;
        style.label.background.color = "#222222";
        style.label.foreground.alpha = 1.0F;
        style.label.foreground.color = "#dddddd";
        style.label.highlight.alpha = 1.0F;
        style.label.highlight.color = "#ffffff";
        style.label.shadow = true;
        style.fullscreenColorSpec.alpha = 0.8F;
        style.fullscreenColorSpec.color = "#222222";
        style.frameColorSpec.color = "#cccccc";
        style.frameColorSpec.alpha = 1.0F;
        style.toolbarColorSpec.color = "#000000";
        style.toolbarColorSpec.alpha = 0.0F;
        style.useThemeImages = false;
        style.minimapTexPrefix = "pur_";
        style.iconSize = 20;
        style.toolbarPadding = 0;
        Theme theme = new FlatTheme("Purist", "techbrew", style);
        for (Theme.Minimap.MinimapSpec minimapSpec : Arrays.asList(theme.minimap.circle, theme.minimap.square)) {
            minimapSpec.margin = 6;
            minimapSpec.reticle.color = "#222222";
            minimapSpec.reticleHeading.color = "#222222";
            minimapSpec.labelTop.foreground.color = "#cccccc";
            minimapSpec.labelTop.background.color = "#555555";
            minimapSpec.labelBottom.foreground.color = "#cccccc";
            minimapSpec.labelBottom.background.color = "#55555";
            minimapSpec.compassLabel.foreground.color = "#cccccc";
            minimapSpec.compassLabel.background.color = "#222222";
            minimapSpec.compassLabel.background.alpha = 0.5F;
        }
        return theme;
    }

    public static Theme createDesertTemple() {
        String light = "#FFFFCD";
        String medium = "#aea87e";
        String dark = "#B7521E";
        String darker = "#803915";
        String darkest = "#361809";
        return createFlatTheme("DesertTemple", light, medium, dark, darker, darkest);
    }

    public static Theme createForestMansion() {
        String light = "#d2e7d2";
        String medium = "#7ab97a";
        String dark = "#1b6f1b";
        String darker = "#114511";
        String darkest = "#061b06";
        return createFlatTheme("ForestMansion", light, medium, dark, darker, darkest);
    }

    public static Theme createNetherFortress() {
        String light = "#FFFF00";
        String medium = "#D2D200";
        String dark = "#6f3634";
        String darker = "#760000";
        String darkest = "#3b0000";
        return createFlatTheme("NetherFortress", light, medium, dark, darker, darkest);
    }

    public static Theme createStronghold() {
        String light = "#000000";
        String medium = "#cccccc";
        String dark = "#222222";
        String darker = "#111111";
        String darkest = "#0a1d33";
        Theme theme = createFlatTheme("Stronghold", light, medium, dark, darker, darkest);
        theme.container.toolbar.horizontal.inner.alpha = 0.5F;
        theme.container.toolbar.horizontal.inner.color = darker;
        theme.container.toolbar.vertical.inner.alpha = 0.5F;
        theme.container.toolbar.vertical.inner.color = darker;
        theme.control.button.buttonOff.color = medium;
        theme.control.button.buttonDisabled.color = darker;
        theme.control.button.iconDisabled.color = medium;
        theme.control.toggle.buttonOn.color = dark;
        theme.control.toggle.buttonOff.color = dark;
        theme.control.toggle.iconHoverOn.color = medium;
        theme.control.toggle.iconHoverOff.color = medium;
        theme.control.toggle.iconOn.color = medium;
        theme.control.toggle.iconOff.color = "#555555";
        theme.fullscreen.statusLabel.background.color = darker;
        theme.fullscreen.statusLabel.foreground.color = medium;
        String white = "#ffffff";
        String black = "#000000";
        for (Theme.Minimap.MinimapSpec minimapSpec : Arrays.asList(theme.minimap.circle, theme.minimap.square)) {
            minimapSpec.labelTop.foreground.color = white;
            minimapSpec.labelTop.background.color = black;
            minimapSpec.labelBottom.foreground.color = white;
            minimapSpec.labelBottom.background.color = black;
            minimapSpec.compassLabel.foreground.color = white;
            minimapSpec.compassLabel.background.color = black;
        }
        return theme;
    }

    public static Theme createOceanMonument() {
        String light = "#dfebec";
        String medium = "#afcecf";
        String dark = "#303dc1";
        String darker = "#212a87";
        String darkest = "#0e1239";
        Theme theme = createFlatTheme("OceanMonument", light, medium, dark, darker, darkest);
        theme.control.toggle.iconDisabled.color = "#555555";
        return theme;
    }

    public static Theme EndCity() {
        String light = "#EAEE9A";
        String dark = "#5A5470";
        String medium = "#CEB46A";
        String darker = "#362744";
        String darkest = "#1F1D2D";
        return createFlatTheme("EndCity", light, medium, dark, darker, darkest);
    }

    private static Theme createFlatTheme(String themeName, String light, String medium, String dark, String darker, String darkest) {
        String white = "#ffffff";
        Style style = new Style();
        style.toggle.on = light;
        style.toggle.off = dark;
        style.toggle.hover = medium;
        style.toggle.disabled = darker;
        style.button.on = light;
        style.button.off = dark;
        style.button.hover = medium;
        style.button.disabled = darker;
        style.label.background.alpha = 0.7F;
        style.label.background.color = darkest;
        style.label.foreground.alpha = 1.0F;
        style.label.foreground.color = light;
        style.label.highlight.alpha = 1.0F;
        style.label.highlight.color = white;
        style.label.shadow = true;
        style.fullscreenColorSpec.alpha = 0.8F;
        style.fullscreenColorSpec.color = darker;
        style.frameColorSpec.color = darker;
        style.frameColorSpec.alpha = 1.0F;
        style.toolbarColorSpec.color = darkest;
        style.toolbarColorSpec.alpha = 0.8F;
        style.minimapTexPrefix = "flat_";
        style.buttonTexPrefix = "flat_";
        Theme theme = new FlatTheme(themeName, "techbrew", style);
        theme.minimap.circle.margin = 6;
        theme.minimap.square.margin = 6;
        theme.minimap.circle.reticle.color = dark;
        theme.minimap.circle.reticleHeading.color = dark;
        theme.minimap.square.reticle.color = dark;
        theme.minimap.square.reticleHeading.color = dark;
        return theme;
    }

    protected FlatTheme(String name, String author, Style style) {
        this.name = name;
        this.author = author;
        this.schema = 2;
        this.directory = ThemePresets.DEFAULT_DIRECTORY;
        this.control.button = button(style);
        this.control.toggle = toggle(style);
        this.fullscreen = fullscreen(style);
        this.container.toolbar.horizontal = toolbar(style, "h", 0, style.iconSize);
        this.container.toolbar.vertical = toolbar(style, "v", style.iconSize, 0);
        this.icon.width = style.iconSize;
        this.icon.height = style.iconSize;
        this.minimap.square = minimapSquare(style);
        this.minimap.circle = minimapCircle(style);
    }

    private static Theme.Control.ButtonSpec commonButton(Style style) {
        Theme.Control.ButtonSpec button = new Theme.Control.ButtonSpec();
        button.useThemeImages = style.useThemeImages;
        button.width = style.iconSize;
        button.height = style.iconSize;
        button.prefix = style.buttonTexPrefix;
        button.tooltipOnStyle = style.tooltipOnStyle;
        button.tooltipOffStyle = style.tooltipOffStyle;
        button.tooltipDisabledStyle = style.tooltipDisabledStyle;
        return button;
    }

    private static Theme.Control.ButtonSpec button(Style style) {
        Theme.Control.ButtonSpec button = commonButton(style);
        button.iconOn.color = style.button.off;
        button.buttonOn.color = style.button.on;
        button.iconOff.color = style.button.on;
        button.buttonOff.color = style.button.off;
        button.iconHoverOn.color = style.button.hover;
        button.buttonHoverOn.color = style.button.on;
        button.iconHoverOff.color = style.button.hover;
        button.buttonHoverOff.color = style.button.off;
        button.iconDisabled.color = style.button.disabled;
        button.buttonDisabled.color = style.button.off;
        return button;
    }

    private static Theme.Control.ButtonSpec toggle(Style style) {
        Theme.Control.ButtonSpec button = commonButton(style);
        button.iconOn.color = style.toggle.off;
        button.buttonOn.color = style.toggle.on;
        button.iconOff.color = style.toggle.on;
        button.buttonOff.color = style.toggle.off;
        button.iconHoverOn.color = style.toggle.hover;
        button.buttonHoverOn.color = style.toggle.on;
        button.iconHoverOff.color = style.toggle.hover;
        button.buttonHoverOff.color = style.toggle.off;
        button.iconDisabled.color = style.toggle.disabled;
        button.buttonDisabled.color = style.toggle.disabled;
        return button;
    }

    private static Theme.Fullscreen fullscreen(Style style) {
        Theme.Fullscreen fullscreen = new Theme.Fullscreen();
        fullscreen.background = style.fullscreenColorSpec.clone();
        fullscreen.statusLabel = style.label.clone();
        return fullscreen;
    }

    private static Theme.Container.Toolbar.ToolbarSpec toolbar(Style style, String prefix, int toolbarCapsWidth, int toolbarCapsHeight) {
        Theme.Container.Toolbar.ToolbarSpec toolbar = new Theme.Container.Toolbar.ToolbarSpec();
        toolbar.useThemeImages = true;
        toolbar.prefix = prefix;
        toolbar.margin = style.toolbarMargin;
        toolbar.padding = style.toolbarPadding;
        toolbar.begin = new Theme.ImageSpec(toolbarCapsWidth, toolbarCapsHeight);
        toolbar.begin.alpha = style.toolbarColorSpec.alpha;
        toolbar.begin.color = style.toolbarColorSpec.color;
        toolbar.inner = new Theme.ImageSpec(style.iconSize, style.iconSize);
        toolbar.inner.alpha = style.toolbarColorSpec.alpha;
        toolbar.inner.color = style.toolbarColorSpec.color;
        toolbar.end = new Theme.ImageSpec(toolbarCapsWidth, toolbarCapsHeight);
        toolbar.end.alpha = style.toolbarColorSpec.alpha;
        toolbar.end.color = style.toolbarColorSpec.color;
        return toolbar;
    }

    private static Theme.Minimap.MinimapSquare minimapSquare(Style style) {
        Theme.Minimap.MinimapSquare minimap = new Theme.Minimap.MinimapSquare();
        applyCommonMinimap(style, minimap);
        minimap.margin = 4;
        minimap.compassPointOffset = (double) (-style.squareFrameThickness - 4);
        minimap.reticleOffsetOuter = 24;
        Theme.ImageSpec cornerSpec = new Theme.ImageSpec(style.squareFrameThickness, style.squareFrameThickness);
        Theme.ImageSpec sidesSpec = new Theme.ImageSpec(style.squareFrameThickness, 1);
        Theme.ImageSpec topBottomSpec = new Theme.ImageSpec(1, style.squareFrameThickness);
        minimap.left = minimap.right = sidesSpec;
        minimap.top = minimap.bottom = topBottomSpec;
        minimap.topLeft = minimap.topRight = minimap.bottomRight = minimap.bottomLeft = cornerSpec;
        return minimap;
    }

    private static Theme.Minimap.MinimapCircle minimapCircle(Style style) {
        Theme.Minimap.MinimapCircle minimap = new Theme.Minimap.MinimapCircle();
        applyCommonMinimap(style, minimap);
        minimap.margin = style.circleFrameThickness;
        minimap.compassPointOffset = (double) (-style.circleFrameThickness - 6);
        minimap.reticleHeading.alpha = 0.4F;
        minimap.compassShowEast = true;
        minimap.compassShowNorth = true;
        minimap.compassShowSouth = true;
        minimap.compassShowWest = true;
        minimap.reticleOffsetOuter = 30;
        minimap.rim256 = new Theme.ImageSpec(256, 256);
        minimap.rim512 = new Theme.ImageSpec(512, 512);
        return minimap;
    }

    private static void applyCommonMinimap(Style style, Theme.Minimap.MinimapSpec minimap) {
        minimap.compassLabel = style.label.clone();
        minimap.compassLabel.background.alpha = 0.0F;
        minimap.compassPoint.height = 16;
        minimap.compassPoint.width = 16;
        minimap.compassPoint.color = style.frameColorSpec.color;
        minimap.compassPoint.alpha = 0.5F;
        minimap.compassPointLabelPad = 0;
        minimap.compassShowEast = true;
        minimap.compassShowNorth = true;
        minimap.compassShowSouth = true;
        minimap.compassShowWest = true;
        minimap.frame = style.frameColorSpec.clone();
        minimap.labelBottom = style.label.clone();
        minimap.labelBottomInside = false;
        minimap.labelTop = style.label.clone();
        minimap.labelTopInside = false;
        minimap.prefix = style.minimapTexPrefix;
        minimap.reticle.alpha = 0.4F;
        minimap.reticle.color = style.button.on;
        minimap.reticleHeading.alpha = 0.4F;
        minimap.reticleHeading.color = style.button.on;
        minimap.reticleHeadingThickness = 2.25;
        minimap.reticleOffsetInner = 20;
        minimap.reticleOffsetOuter = 20;
        minimap.reticleThickness = 1.25;
        minimap.waypointOffset = 0.0;
    }
}