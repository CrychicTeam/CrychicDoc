package de.keksuccino.fancymenu;

import de.keksuccino.fancymenu.util.AbstractOptions;
import de.keksuccino.konkrete.config.Config;

public class Options extends AbstractOptions {

    protected final Config config = new Config(FancyMenu.MOD_DIR.getAbsolutePath().replace("\\", "/") + "/options.txt");

    public final AbstractOptions.Option<Boolean> playVanillaMenuMusic = new AbstractOptions.Option<>(this.config, "play_vanilla_menu_music", true, "general");

    public final AbstractOptions.Option<Integer> defaultGuiScale = new AbstractOptions.Option<>(this.config, "default_gui_scale", -1, "general");

    public final AbstractOptions.Option<Boolean> forceFullscreen = new AbstractOptions.Option<>(this.config, "force_fullscreen", false, "general");

    public final AbstractOptions.Option<Boolean> advancedCustomizationMode = new AbstractOptions.Option<>(this.config, "advanced_customization_mode", false, "customization");

    public final AbstractOptions.Option<Boolean> showCustomizationOverlay = new AbstractOptions.Option<>(this.config, "show_customization_overlay", true, "customization");

    public final AbstractOptions.Option<Boolean> modpackMode = new AbstractOptions.Option<>(this.config, "modpack_mode", false, "customization");

    public final AbstractOptions.Option<String> gameIntroAnimation = new AbstractOptions.Option<>(this.config, "game_intro_animation_name", "", "loading");

    public final AbstractOptions.Option<Boolean> gameIntroAllowSkip = new AbstractOptions.Option<>(this.config, "allow_game_intro_skip", true, "loading");

    public final AbstractOptions.Option<Boolean> gameIntroFadeOut = new AbstractOptions.Option<>(this.config, "game_intro_fade_out", true, "loading");

    public final AbstractOptions.Option<String> gameIntroCustomSkipText = new AbstractOptions.Option<>(this.config, "custom_game_intro_skip_text", "", "loading");

    public final AbstractOptions.Option<Boolean> preLoadAnimations = new AbstractOptions.Option<>(this.config, "preload_animations", true, "loading");

    public final AbstractOptions.Option<String> preLoadResources = new AbstractOptions.Option<>(this.config, "preload_resources", "", "loading");

    public final AbstractOptions.Option<Boolean> showCustomWindowIcon = new AbstractOptions.Option<>(this.config, "show_custom_window_icon", false, "window");

    public final AbstractOptions.Option<String> customWindowIcon16 = new AbstractOptions.Option<>(this.config, "custom_window_icon_16", "", "window");

    public final AbstractOptions.Option<String> customWindowIcon32 = new AbstractOptions.Option<>(this.config, "custom_window_icon_32", "", "window");

    public final AbstractOptions.Option<String> customWindowIconMacOS = new AbstractOptions.Option<>(this.config, "custom_window_icon_macos", "", "window");

    public final AbstractOptions.Option<String> customWindowTitle = new AbstractOptions.Option<>(this.config, "custom_window_title", "", "window");

    public final AbstractOptions.Option<Boolean> showMultiplayerScreenServerIcons = new AbstractOptions.Option<>(this.config, "show_multiplayer_screen_server_icons", true, "multiplayer_screen");

    public final AbstractOptions.Option<Boolean> showSingleplayerScreenWorldIcons = new AbstractOptions.Option<>(this.config, "show_singleplayer_screen_world_icons", true, "singleplayer_screen");

    public final AbstractOptions.Option<Boolean> showLayoutEditorGrid = new AbstractOptions.Option<>(this.config, "show_layout_editor_grid", true, "layout_editor");

    public final AbstractOptions.Option<Integer> layoutEditorGridSize = new AbstractOptions.Option<>(this.config, "layout_editor_grid_size", 10, "layout_editor");

    public final AbstractOptions.Option<Boolean> showAllAnchorOverlayConnections = new AbstractOptions.Option<>(this.config, "anchor_overlay_show_all_connection_lines", false, "layout_editor");

    public final AbstractOptions.Option<Boolean> anchorOverlayChangeAnchorOnAreaHover = new AbstractOptions.Option<>(this.config, "anchor_overlay_change_anchor_on_area_hover", true, "layout_editor");

    public final AbstractOptions.Option<Boolean> anchorOverlayChangeAnchorOnElementHover = new AbstractOptions.Option<>(this.config, "anchor_overlay_change_anchor_on_element_hover", true, "layout_editor");

    public final AbstractOptions.Option<Boolean> invertAnchorOverlayColor = new AbstractOptions.Option<>(this.config, "invert_anchor_overlay_color", false, "layout_editor");

    public final AbstractOptions.Option<Float> anchorOverlayOpacityPercentageNormal = new AbstractOptions.Option<>(this.config, "anchor_overlay_opacity_normal", 0.5F, "layout_editor");

    public final AbstractOptions.Option<Float> anchorOverlayOpacityPercentageBusy = new AbstractOptions.Option<>(this.config, "anchor_overlay_opacity_busy", 0.7F, "layout_editor");

    public final AbstractOptions.Option<String> anchorOverlayColorBaseOverride = new AbstractOptions.Option<>(this.config, "anchor_overlay_color_base_override", "", "layout_editor");

    public final AbstractOptions.Option<String> anchorOverlayColorBorderOverride = new AbstractOptions.Option<>(this.config, "anchor_overlay_color_border_override", "", "layout_editor");

    public final AbstractOptions.Option<String> anchorOverlayVisibilityMode = new AbstractOptions.Option<>(this.config, "anchor_overlay_visibility_mode", "dragging", "layout_editor");

    public final AbstractOptions.Option<Double> anchorOverlayHoverChargingTimeSeconds = new AbstractOptions.Option<>(this.config, "anchor_overlay_hover_charging_time_seconds", 2.0, "layout_editor");

    public final AbstractOptions.Option<Float> uiScale = new AbstractOptions.Option<>(this.config, "ui_scale", 4.0F, "ui");

    public final AbstractOptions.Option<Boolean> playUiClickSounds = new AbstractOptions.Option<>(this.config, "play_ui_click_sounds", true, "ui");

    public final AbstractOptions.Option<Boolean> enableUiTextShadow = new AbstractOptions.Option<>(this.config, "enable_ui_text_shadow", false, "ui");

    public final AbstractOptions.Option<Integer> contextMenuHoverOpenSpeed = new AbstractOptions.Option<>(this.config, "context_menu_hover_open_speed", 1, "ui");

    public final AbstractOptions.Option<String> uiTheme = new AbstractOptions.Option<>(this.config, "ui_theme", "dark", "ui");

    public final AbstractOptions.Option<Boolean> showDebugOverlay = new AbstractOptions.Option<>(this.config, "show_debug_overlay", false, "debug_overlay");

    public final AbstractOptions.Option<Boolean> debugOverlayShowBasicScreenCategory = new AbstractOptions.Option<>(this.config, "debug_overlay_show_basic_screen_category", true, "debug_overlay");

    public final AbstractOptions.Option<Boolean> debugOverlayShowAdvancedScreenCategory = new AbstractOptions.Option<>(this.config, "debug_overlay_show_advanced_screen_category", true, "debug_overlay");

    public final AbstractOptions.Option<Boolean> debugOverlayShowResourcesCategory = new AbstractOptions.Option<>(this.config, "debug_overlay_show_resources_category", true, "debug_overlay");

    public final AbstractOptions.Option<Boolean> debugOverlayShowSystemCategory = new AbstractOptions.Option<>(this.config, "debug_overlay_show_system_category", true, "debug_overlay");

    public Options() {
        this.config.syncConfig();
        this.config.clearUnusedValues();
    }
}