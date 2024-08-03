package icyllis.modernui.mc.forge;

import com.mojang.blaze3d.platform.Monitor;
import com.mojang.blaze3d.platform.VideoMode;
import com.mojang.blaze3d.platform.Window;
import icyllis.modernui.ModernUI;
import icyllis.modernui.core.Core;
import icyllis.modernui.core.Handler;
import icyllis.modernui.graphics.Color;
import icyllis.modernui.graphics.font.GlyphManager;
import icyllis.modernui.mc.BlurHandler;
import icyllis.modernui.mc.FontResourceManager;
import icyllis.modernui.mc.ModernUIClient;
import icyllis.modernui.mc.ModernUIMod;
import icyllis.modernui.mc.TooltipRenderer;
import icyllis.modernui.mc.UIManager;
import icyllis.modernui.mc.text.ModernTextRenderer;
import icyllis.modernui.mc.text.TextLayout;
import icyllis.modernui.mc.text.TextLayoutEngine;
import icyllis.modernui.mc.text.TextLayoutProcessor;
import icyllis.modernui.mc.text.TextRenderType;
import icyllis.modernui.resources.Resources;
import icyllis.modernui.util.DisplayMetrics;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.Platform;

@Internal
public final class Config {

    public static Config.Client CLIENT;

    private static ForgeConfigSpec CLIENT_SPEC;

    public static Config.Common COMMON;

    private static ForgeConfigSpec COMMON_SPEC;

    public static Config.Text TEXT;

    public static ForgeConfigSpec TEXT_SPEC;

    private static void init(boolean isClient, BiConsumer<Type, ForgeConfigSpec> registerConfig) {
    }

    public static void initClientConfig(Consumer<ForgeConfigSpec> registerConfig) {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        CLIENT = new Config.Client(builder);
        CLIENT_SPEC = builder.build();
        registerConfig.accept(CLIENT_SPEC);
    }

    public static void initCommonConfig(Consumer<ForgeConfigSpec> registerConfig) {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        COMMON = new Config.Common(builder);
        COMMON_SPEC = builder.build();
        registerConfig.accept(COMMON_SPEC);
    }

    public static void initTextConfig(Consumer<ForgeConfigSpec> registerConfig) {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        TEXT = new Config.Text(builder);
        TEXT_SPEC = builder.build();
        registerConfig.accept(TEXT_SPEC);
    }

    public static void reloadCommon(@Nonnull ModConfig config) {
        IConfigSpec<?> spec = config.getSpec();
        if (spec == COMMON_SPEC) {
            COMMON.reload();
            ModernUI.LOGGER.debug(ModernUI.MARKER, "Modern UI common config loaded/reloaded");
        }
    }

    public static void reloadAnyClient(@Nonnull ModConfig config) {
        IConfigSpec<?> spec = config.getSpec();
        if (spec == CLIENT_SPEC) {
            CLIENT.reload();
            ModernUI.LOGGER.debug(ModernUI.MARKER, "Modern UI client config loaded/reloaded");
        } else if (spec == TEXT_SPEC) {
            TEXT.reload();
            ModernUI.LOGGER.debug(ModernUI.MARKER, "Modern UI text config loaded/reloaded");
        }
    }

    public static class Client {

        public static final int ANIM_DURATION_MIN = 0;

        public static final int ANIM_DURATION_MAX = 800;

        public static final int BLUR_RADIUS_MIN = 2;

        public static final int BLUR_RADIUS_MAX = 18;

        public static final float FONT_SCALE_MIN = 0.5F;

        public static final float FONT_SCALE_MAX = 2.0F;

        public static final int TOOLTIP_BORDER_COLOR_ANIM_MIN = 0;

        public static final int TOOLTIP_BORDER_COLOR_ANIM_MAX = 5000;

        public static final float TOOLTIP_BORDER_WIDTH_MIN = 0.5F;

        public static final float TOOLTIP_BORDER_WIDTH_MAX = 2.5F;

        public static final float TOOLTIP_CORNER_RADIUS_MIN = 0.0F;

        public static final float TOOLTIP_CORNER_RADIUS_MAX = 8.0F;

        public static final float TOOLTIP_SHADOW_RADIUS_MIN = 0.0F;

        public static final float TOOLTIP_SHADOW_RADIUS_MAX = 32.0F;

        public final ForgeConfigSpec.BooleanValue mBlurEffect;

        public final ForgeConfigSpec.BooleanValue mBlurWithBackground;

        public final ForgeConfigSpec.IntValue mBackgroundDuration;

        public final ForgeConfigSpec.IntValue mBlurRadius;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> mBackgroundColor;

        public final ForgeConfigSpec.BooleanValue mInventoryPause;

        public final ForgeConfigSpec.BooleanValue mTooltip;

        public final ForgeConfigSpec.BooleanValue mRoundedTooltip;

        public final ForgeConfigSpec.BooleanValue mCenterTooltipTitle;

        public final ForgeConfigSpec.BooleanValue mTooltipTitleBreak;

        public final ForgeConfigSpec.BooleanValue mExactTooltipPositioning;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> mTooltipFill;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> mTooltipStroke;

        public final ForgeConfigSpec.IntValue mTooltipCycle;

        public final ForgeConfigSpec.DoubleValue mTooltipWidth;

        public final ForgeConfigSpec.DoubleValue mTooltipRadius;

        public final ForgeConfigSpec.DoubleValue mTooltipShadowRadius;

        public final ForgeConfigSpec.DoubleValue mTooltipShadowAlpha;

        public final ForgeConfigSpec.BooleanValue mAdaptiveTooltipColors;

        public final ForgeConfigSpec.BooleanValue mDing;

        public final ForgeConfigSpec.BooleanValue mZoom;

        public final ForgeConfigSpec.BooleanValue mForceRtl;

        public final ForgeConfigSpec.DoubleValue mFontScale;

        public final ForgeConfigSpec.EnumValue<Config.Client.WindowMode> mWindowMode;

        public final ForgeConfigSpec.BooleanValue mUseNewGuiScale;

        public final ForgeConfigSpec.BooleanValue mRemoveTelemetry;

        public final ForgeConfigSpec.IntValue mFramerateInactive;

        public final ForgeConfigSpec.IntValue mFramerateMinimized;

        public final ForgeConfigSpec.DoubleValue mMasterVolumeInactive;

        public final ForgeConfigSpec.DoubleValue mMasterVolumeMinimized;

        public final ForgeConfigSpec.IntValue mScrollbarSize;

        public final ForgeConfigSpec.IntValue mTouchSlop;

        public final ForgeConfigSpec.IntValue mMinScrollbarTouchTarget;

        public final ForgeConfigSpec.IntValue mMinimumFlingVelocity;

        public final ForgeConfigSpec.IntValue mMaximumFlingVelocity;

        public final ForgeConfigSpec.IntValue mOverscrollDistance;

        public final ForgeConfigSpec.IntValue mOverflingDistance;

        public final ForgeConfigSpec.DoubleValue mVerticalScrollFactor;

        public final ForgeConfigSpec.DoubleValue mHorizontalScrollFactor;

        private final ForgeConfigSpec.ConfigValue<List<? extends String>> mBlurBlacklist;

        public final ForgeConfigSpec.BooleanValue mAntiAliasing;

        public final ForgeConfigSpec.BooleanValue mAutoHinting;

        public final ForgeConfigSpec.ConfigValue<String> mFirstFontFamily;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> mFallbackFontFamilyList;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> mFontRegistrationList;

        public final ForgeConfigSpec.BooleanValue mUseColorEmoji;

        public final ForgeConfigSpec.BooleanValue mEmojiShortcodes;

        public Config.Client.WindowMode mLastWindowMode = Config.Client.WindowMode.NORMAL;

        private Client(@Nonnull ForgeConfigSpec.Builder builder) {
            builder.comment("Screen Config").push("screen");
            this.mBackgroundDuration = builder.comment("The duration of GUI background color and blur radius animation in milliseconds. (0 = OFF)").defineInRange("animationDuration", 200, 0, 800);
            this.mBackgroundColor = builder.comment("The GUI background color in #RRGGBB or #AARRGGBB format. Default value: #66000000", "Can be one to four values representing top left, top right, bottom right and bottom left color.", "Multiple values produce a gradient effect, whereas one value produce a solid color.", "When values is less than 4, the rest of the corner color will be replaced by the last value.").defineList("backgroundColor", (Supplier<List<? extends String>>) (() -> {
                List<String> list = new ArrayList();
                list.add("#99000000");
                return list;
            }), o -> true);
            this.mBlurEffect = builder.comment("Add blur effect to GUI background when opened, it is incompatible with OptiFine's FXAA shader and some mods.", "Disable this if you run into a problem or are on low-end PCs").define("blurEffect", true);
            this.mBlurWithBackground = builder.comment("This option means that blur effect only applies for GUI screens with a background", "This is only meaningful when blur effect is enabled.").define("blurWithBackground", true);
            this.mBlurRadius = builder.comment("The strength for two-pass gaussian convolution blur effect.", "samples/pixel = ((radius * 2) + 1) * 2, sigma = radius / 2.").defineInRange("blurRadius", 7, 2, 18);
            this.mBlurBlacklist = builder.comment("A list of GUI screen superclasses that won't activate blur effect when opened.").defineList("blurBlacklist", (Supplier<List<? extends String>>) (() -> {
                List<String> list = new ArrayList();
                list.add(ChatScreen.class.getName());
                return list;
            }), o -> true);
            this.mInventoryPause = builder.comment("(Beta) Pause the game when inventory (also includes creative mode) opened.").define("inventoryPause", false);
            this.mFramerateInactive = builder.comment("Framerate limit on window inactive (out of focus or minimized), 0 = no change.").defineInRange("framerateInactive", 30, 0, 255);
            this.mFramerateMinimized = builder.comment("Framerate limit on window minimized, 0 = same as framerate inactive.", "This value will be no greater than framerate inactive.").defineInRange("framerateMinimized", 0, 0, 255);
            this.mMasterVolumeInactive = builder.comment("Master volume multiplier on window inactive (out of focus or minimized), 1 = no change.").defineInRange("masterVolumeInactive", 0.5, 0.0, 1.0);
            this.mMasterVolumeMinimized = builder.comment("Master volume multiplier on window minimized, 1 = same as master volume inactive.", "This value will be no greater than master volume inactive.").defineInRange("masterVolumeMinimized", 0.25, 0.0, 1.0);
            builder.pop();
            builder.comment("Tooltip Config").push("tooltip");
            this.mTooltip = builder.comment("Whether to enable Modern UI enhanced tooltip, or back to vanilla default.").define("enable", !ModernUIMod.isLegendaryTooltipsLoaded());
            this.mRoundedTooltip = builder.comment("Whether to use rounded tooltip shapes, or to use vanilla style.").define("roundedShape", true);
            this.mCenterTooltipTitle = builder.comment("True to center the tooltip title if rendering an item's tooltip.", "Following lines are not affected by this option.").define("centerTitle", true);
            this.mTooltipTitleBreak = builder.comment("True to add a title break below the tooltip title line.", "TitleBreak and CenterTitle will work/appear at the same time.").define("titleBreak", true);
            this.mExactTooltipPositioning = builder.comment("True to exactly position tooltip to pixel grid, smoother movement.").define("exactPositioning", true);
            this.mTooltipFill = builder.comment("The tooltip background color in #RRGGBB or #AARRGGBB format. Default: #E0000000", "Can be one to four values representing top left, top right, bottom right and bottom left color.", "Multiple values produce a gradient effect, whereas one value produces a solid color.", "If less than 4 are provided, repeat the last value.").defineList("colorFill", (Supplier<List<? extends String>>) (() -> {
                List<String> list = new ArrayList();
                list.add("#E0000000");
                return list;
            }), $ -> true);
            this.mTooltipStroke = builder.comment("The tooltip border color in #RRGGBB or #AARRGGBB format. Default: #F0AADCF0, #F0DAD0F4, #F0FFC3F7 and #F0DAD0F4", "Can be one to four values representing top left, top right, bottom right and bottom left color.", "Multiple values produce a gradient effect, whereas one value produces a solid color.", "If less than 4 are provided, repeat the last value.").defineList("colorStroke", (Supplier<List<? extends String>>) (() -> {
                List<String> list = new ArrayList();
                list.add("#F0AADCF0");
                list.add("#F0FFC3F7");
                list.add("#F0BFF2B2");
                list.add("#F0D27F3D");
                return list;
            }), $ -> true);
            this.mTooltipCycle = builder.comment("The cycle time of tooltip border color in milliseconds. (0 = OFF)").defineInRange("borderCycleTime", 1000, 0, 5000);
            this.mTooltipWidth = builder.comment("The width of tooltip border, if rounded, in GUI Scale Independent Pixels.").defineInRange("borderWidth", 1.3333334F, 0.5, 2.5);
            this.mTooltipRadius = builder.comment("The corner radius of tooltip border, if rounded, in GUI Scale Independent Pixels.").defineInRange("cornerRadius", 3.0, 0.0, 8.0);
            this.mTooltipShadowRadius = builder.comment("The shadow radius of tooltip, if rounded, in GUI Scale Independent Pixels.", "No impact on performance.").defineInRange("shadowRadius", 10.0, 0.0, 32.0);
            this.mTooltipShadowAlpha = builder.comment("The shadow opacity of tooltip, if rounded. No impact on performance.").defineInRange("shadowOpacity", 0.35F, 0.0, 1.0);
            this.mAdaptiveTooltipColors = builder.comment("When true, tooltip border colors adapt to item's name and rarity.").define("adaptiveColors", true);
            builder.pop();
            builder.comment("General Config").push("general");
            this.mDing = builder.comment("Play a sound effect when the game is loaded.").define("ding", true);
            this.mZoom = builder.comment("Press 'C' key (by default) to zoom 4x, the same as OptiFine's.", "This is auto disabled when OptiFine is installed.").define("zoom", true);
            this.mWindowMode = builder.comment("Control the window mode, normal mode does nothing.").defineEnum("windowMode", Config.Client.WindowMode.NORMAL);
            this.mUseNewGuiScale = builder.comment("Whether to replace vanilla GUI scale button to slider with tips.").define("useNewGuiScale", true);
            this.mRemoveTelemetry = builder.comment("Remove telemetry event of client behaviors.").define("removeTelemetry", false);
            this.mEmojiShortcodes = builder.comment("Allow Slack or Discord shortcodes to replace Unicode Emoji Sequences in chat.").define("emojiShortcodes", true);
            builder.pop();
            builder.comment("View system config, currently not working.").push("view");
            this.mForceRtl = builder.comment("Force layout direction to RTL, otherwise, the current Locale setting.").define("forceRtl", false);
            this.mFontScale = builder.comment("The global font scale used with sp units.").defineInRange("fontScale", 1.0, 0.5, 2.0);
            this.mScrollbarSize = builder.comment("Default scrollbar size in dips.").defineInRange("scrollbarSize", 8, 0, 1024);
            this.mTouchSlop = builder.comment("Distance a touch can wander before we think the user is scrolling in dips.").defineInRange("touchSlop", 4, 0, 1024);
            this.mMinScrollbarTouchTarget = builder.comment("Minimum size of the touch target for a scrollbar in dips.").defineInRange("minScrollbarTouchTarget", 16, 0, 1024);
            this.mMinimumFlingVelocity = builder.comment("Minimum velocity to initiate a fling in dips per second.").defineInRange("minimumFlingVelocity", 50, 0, 32767);
            this.mMaximumFlingVelocity = builder.comment("Maximum velocity to initiate a fling in dips per second.").defineInRange("maximumFlingVelocity", 8000, 0, 32767);
            this.mOverscrollDistance = builder.comment("Max distance in dips to overscroll for edge effects.").defineInRange("overscrollDistance", 0, 0, 1024);
            this.mOverflingDistance = builder.comment("Max distance in dips to overfling for edge effects.").defineInRange("overflingDistance", 12, 0, 1024);
            this.mVerticalScrollFactor = builder.comment("Amount to scroll in response to a vertical scroll event, in dips per axis value.").defineInRange("verticalScrollFactor", 64.0, 0.0, 1024.0);
            this.mHorizontalScrollFactor = builder.comment("Amount to scroll in response to a horizontal scroll event, in dips per axis value.").defineInRange("horizontalScrollFactor", 64.0, 0.0, 1024.0);
            builder.pop();
            builder.comment("Font Config").push("font");
            this.mAntiAliasing = builder.comment("Control the anti-aliasing of raw glyph rasterization.").define("antiAliasing", true);
            this.mAutoHinting = builder.comment("Control the FreeType font hinting of raw glyph metrics.", "Enable if on low-res monitor; disable for linear texts.").define("autoHinting", Platform.get() != Platform.MACOSX);
            this.mFirstFontFamily = builder.comment("The first font family to use. See fallbackFontFamilyList").define("firstFontFamily", "Source Han Sans CN Medium");
            this.mFallbackFontFamilyList = builder.comment("A set of fallback font families to determine the typeface to use.", "The order is first > fallbacks. TrueType & OpenType are supported.", "Each element can be one of the following two cases:", "1) Name of registered font family, for instance: Segoe UI", "2) Path of font files on your PC, for instance: /usr/shared/fonts/x.otf", "Registered font families include:", "1) OS builtin fonts.", "2) Font files in fontRegistrationList.", "3) Font files in '/resourcepacks' directory.", "4) Font files under 'modernui:font' in resource packs.", "Note that for TTC/OTC font, you should register it and select one of font families.", "Otherwise, only the first font family from the TrueType/OpenType Collection will be used.", "This is only read once when the game is loaded, you can reload via in-game GUI.").defineList("fallbackFontFamilyList", (Supplier<List<? extends String>>) (() -> {
                List<String> list = new ArrayList();
                list.add("Noto Sans");
                list.add("Segoe UI Variable");
                list.add("Segoe UI");
                list.add("San Francisco");
                list.add("Open Sans");
                list.add("SimHei");
                list.add("STHeiti");
                list.add("Segoe UI Symbol");
                list.add("mui-i18n-compat");
                return list;
            }), s -> true);
            this.mFontRegistrationList = builder.comment("A set of additional font files (or directories) to register.", "For TrueType/OpenType Collections, all contained font families will be registered.", "Registered fonts can be referenced in Modern UI and Minecraft (Modern Text Engine).", "For example, \"E:/Fonts\" means all font files in that directory will be registered.", "System requires random access to these files, you should not remove them while running.", "This is only read once when the game is loaded, i.e. registration.").defineList("fontRegistrationList", ArrayList::new, s -> true);
            this.mUseColorEmoji = builder.comment("Whether to use Google Noto Color Emoji, otherwise grayscale emoji (faster).", "See Unicode 15.0 specification for details on how this affects text layout.").define("useColorEmoji", true);
            builder.pop();
        }

        public void saveAsync() {
            Util.ioPool().execute(() -> Config.CLIENT_SPEC.save());
        }

        public void saveAndReloadAsync() {
            Util.ioPool().execute(() -> Config.CLIENT_SPEC.save());
            this.reload();
        }

        private void reload() {
            BlurHandler.sBlurEffect = this.mBlurEffect.get();
            BlurHandler.sBlurWithBackground = this.mBlurWithBackground.get();
            BlurHandler.sBackgroundDuration = this.mBackgroundDuration.get();
            BlurHandler.sBlurRadius = this.mBlurRadius.get();
            BlurHandler.sFramerateInactive = this.mFramerateInactive.get();
            BlurHandler.sFramerateMinimized = Math.min(this.mFramerateMinimized.get(), BlurHandler.sFramerateInactive);
            BlurHandler.sMasterVolumeInactive = this.mMasterVolumeInactive.get().floatValue();
            BlurHandler.sMasterVolumeMinimized = Math.min(this.mMasterVolumeMinimized.get().floatValue(), BlurHandler.sMasterVolumeInactive);
            List<? extends String> inColors = this.mBackgroundColor.get();
            int[] resultColors = new int[4];
            int color = -1728053248;
            for (int i = 0; i < 4; i++) {
                if (inColors != null && i < inColors.size()) {
                    String s = (String) inColors.get(i);
                    try {
                        color = Color.parseColor(s);
                    } catch (Exception var9) {
                        ModernUI.LOGGER.error(ModernUI.MARKER, "Wrong color format for screen background, index: {}", i, var9);
                    }
                }
                resultColors[i] = color;
            }
            BlurHandler.sBackgroundColor = resultColors;
            BlurHandler.INSTANCE.loadBlacklist(this.mBlurBlacklist.get());
            ModernUIClient.sInventoryPause = this.mInventoryPause.get();
            ModernUIClient.sRemoveTelemetrySession = this.mRemoveTelemetry.get();
            TooltipRenderer.sTooltip = this.mTooltip.get();
            inColors = this.mTooltipFill.get();
            color = -1;
            for (int i = 0; i < 4; i++) {
                if (inColors != null && i < inColors.size()) {
                    String s = (String) inColors.get(i);
                    try {
                        color = Color.parseColor(s);
                    } catch (Exception var8) {
                        ModernUI.LOGGER.error(ModernUI.MARKER, "Wrong color format for tooltip background, index: {}", i, var8);
                    }
                }
                TooltipRenderer.sFillColor[i] = color;
            }
            inColors = this.mTooltipStroke.get();
            color = -1;
            for (int i = 0; i < 4; i++) {
                if (inColors != null && i < inColors.size()) {
                    String s = (String) inColors.get(i);
                    try {
                        color = Color.parseColor(s);
                    } catch (Exception var7) {
                        ModernUI.LOGGER.error(ModernUI.MARKER, "Wrong color format for tooltip border, index: {}", i, var7);
                    }
                }
                TooltipRenderer.sStrokeColor[i] = color;
            }
            TooltipRenderer.sBorderColorCycle = this.mTooltipCycle.get();
            TooltipRenderer.sExactPositioning = this.mExactTooltipPositioning.get();
            TooltipRenderer.sRoundedShapes = this.mRoundedTooltip.get();
            TooltipRenderer.sCenterTitle = this.mCenterTooltipTitle.get();
            TooltipRenderer.sTitleBreak = this.mTooltipTitleBreak.get();
            TooltipRenderer.sBorderWidth = this.mTooltipWidth.get().floatValue();
            TooltipRenderer.sCornerRadius = this.mTooltipRadius.get().floatValue();
            TooltipRenderer.sShadowRadius = this.mTooltipShadowRadius.get().floatValue();
            TooltipRenderer.sShadowAlpha = this.mTooltipShadowAlpha.get().floatValue();
            TooltipRenderer.sAdaptiveColors = this.mAdaptiveTooltipColors.get();
            UIManager.sDingEnabled = this.mDing.get();
            UIManager.sZoomEnabled = this.mZoom.get() && !ModernUIMod.isOptiFineLoaded();
            Config.Client.WindowMode windowMode = (Config.Client.WindowMode) this.mWindowMode.get();
            if (this.mLastWindowMode != windowMode) {
                this.mLastWindowMode = windowMode;
                Minecraft.getInstance().m_6937_(() -> this.mLastWindowMode.apply());
            }
            Handler handler = Core.getUiHandlerAsync();
            if (handler != null) {
                handler.post(() -> {
                    UIManager.getInstance().updateLayoutDir(this.mForceRtl.get());
                    ModernUIClient.sFontScale = this.mFontScale.get().floatValue();
                    ModernUI ctx = ModernUI.getInstance();
                    if (ctx != null) {
                        Resources res = ctx.getResources();
                        DisplayMetrics metrics = new DisplayMetrics();
                        metrics.setTo(res.getDisplayMetrics());
                        metrics.scaledDensity = ModernUIClient.sFontScale * metrics.density;
                        res.updateMetrics(metrics);
                    }
                });
            }
            boolean reloadStrike = false;
            if (GlyphManager.sAntiAliasing != this.mAntiAliasing.get()) {
                GlyphManager.sAntiAliasing = this.mAntiAliasing.get();
                reloadStrike = true;
            }
            if (GlyphManager.sFractionalMetrics == this.mAutoHinting.get()) {
                GlyphManager.sFractionalMetrics = !this.mAutoHinting.get();
                reloadStrike = true;
            }
            ModernUIClient.sUseColorEmoji = this.mUseColorEmoji.get();
            ModernUIClient.sEmojiShortcodes = this.mEmojiShortcodes.get();
            ModernUIClient.sFirstFontFamily = this.mFirstFontFamily.get();
            ModernUIClient.sFallbackFontFamilyList = this.mFallbackFontFamilyList.get();
            ModernUIClient.sFontRegistrationList = this.mFontRegistrationList.get();
            if (reloadStrike) {
                Minecraft.getInstance().m_18707_(() -> FontResourceManager.getInstance().reloadAll());
            }
            ModernUI.getSelectedTypeface();
        }

        public static enum WindowMode {

            NORMAL,
            FULLSCREEN,
            FULLSCREEN_BORDERLESS,
            MAXIMIZED,
            MAXIMIZED_BORDERLESS,
            WINDOWED,
            WINDOWED_BORDERLESS;

            public void apply() {
                if (this != NORMAL) {
                    Window window = Minecraft.getInstance().getWindow();
                    switch(this) {
                        case FULLSCREEN:
                            if (!window.isFullscreen()) {
                                window.toggleFullScreen();
                            }
                            break;
                        case FULLSCREEN_BORDERLESS:
                            if (window.isFullscreen()) {
                                window.toggleFullScreen();
                            }
                            GLFW.glfwRestoreWindow(window.getWindow());
                            GLFW.glfwSetWindowAttrib(window.getWindow(), 131077, 0);
                            Monitor monitor = window.findBestMonitor();
                            if (monitor != null) {
                                VideoMode videoMode = monitor.getCurrentMode();
                                int x = monitor.getX();
                                int y = monitor.getY();
                                int width = videoMode.getWidth();
                                int height = videoMode.getHeight();
                                GLFW.glfwSetWindowMonitor(window.getWindow(), 0L, x, y, width, height, -1);
                            } else {
                                GLFW.glfwMaximizeWindow(window.getWindow());
                            }
                            break;
                        case MAXIMIZED:
                            if (window.isFullscreen()) {
                                window.toggleFullScreen();
                            }
                            GLFW.glfwRestoreWindow(window.getWindow());
                            GLFW.glfwSetWindowAttrib(window.getWindow(), 131077, 1);
                            GLFW.glfwMaximizeWindow(window.getWindow());
                            break;
                        case MAXIMIZED_BORDERLESS:
                            if (window.isFullscreen()) {
                                window.toggleFullScreen();
                            }
                            GLFW.glfwRestoreWindow(window.getWindow());
                            GLFW.glfwSetWindowAttrib(window.getWindow(), 131077, 0);
                            GLFW.glfwMaximizeWindow(window.getWindow());
                            break;
                        case WINDOWED:
                            if (window.isFullscreen()) {
                                window.toggleFullScreen();
                            }
                            GLFW.glfwSetWindowAttrib(window.getWindow(), 131077, 1);
                            GLFW.glfwRestoreWindow(window.getWindow());
                            break;
                        case WINDOWED_BORDERLESS:
                            if (window.isFullscreen()) {
                                window.toggleFullScreen();
                            }
                            GLFW.glfwSetWindowAttrib(window.getWindow(), 131077, 0);
                            GLFW.glfwRestoreWindow(window.getWindow());
                    }
                }
            }

            @Nonnull
            public String toString() {
                return I18n.get("modernui.windowMode." + this.name().toLowerCase(Locale.ROOT));
            }
        }
    }

    public static class Common {

        public final ForgeConfigSpec.BooleanValue developerMode;

        public final ForgeConfigSpec.IntValue oneTimeEvents;

        public final ForgeConfigSpec.BooleanValue autoShutdown;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> shutdownTimes;

        private Common(@Nonnull ForgeConfigSpec.Builder builder) {
            builder.comment("Developer Config").push("developer");
            this.developerMode = builder.comment("Whether to enable developer mode.").define("enableDeveloperMode", false);
            this.oneTimeEvents = builder.defineInRange("oneTimeEvents", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
            builder.pop();
            builder.comment("Auto Shutdown Config").push("autoShutdown");
            this.autoShutdown = builder.comment("Enable auto-shutdown for server.").define("enable", false);
            this.shutdownTimes = builder.comment("The time points of when server will auto-shutdown. Format: HH:mm.").defineList("times", (Supplier<List<? extends String>>) (() -> {
                List<String> list = new ArrayList();
                list.add("04:00");
                list.add("16:00");
                return list;
            }), s -> true);
            builder.pop();
        }

        public void saveAndReloadAsync() {
            Util.ioPool().execute(() -> Config.COMMON_SPEC.save());
            this.reload();
        }

        private void reload() {
            ModernUIMod.sDeveloperMode = this.developerMode.get();
            ServerHandler.INSTANCE.determineShutdownTime();
        }
    }

    public static class Text {

        public static final float BASE_FONT_SIZE_MIN = 6.5F;

        public static final float BASE_FONT_SIZE_MAX = 9.5F;

        public static final float BASELINE_MIN = 4.0F;

        public static final float BASELINE_MAX = 10.0F;

        public static final float SHADOW_OFFSET_MIN = 0.2F;

        public static final float SHADOW_OFFSET_MAX = 2.0F;

        public static final float OUTLINE_OFFSET_MIN = 0.2F;

        public static final float OUTLINE_OFFSET_MAX = 2.0F;

        public static final int LIFESPAN_MIN = 2;

        public static final int LIFESPAN_MAX = 15;

        public final ForgeConfigSpec.BooleanValue mAllowShadow;

        public final ForgeConfigSpec.BooleanValue mFixedResolution;

        public final ForgeConfigSpec.DoubleValue mBaseFontSize;

        public final ForgeConfigSpec.DoubleValue mBaselineShift;

        public final ForgeConfigSpec.DoubleValue mShadowOffset;

        public final ForgeConfigSpec.DoubleValue mOutlineOffset;

        public final ForgeConfigSpec.IntValue mCacheLifespan;

        public final ForgeConfigSpec.EnumValue<Config.Text.TextDirection> mTextDirection;

        public final ForgeConfigSpec.BooleanValue mUseTextShadersInWorld;

        public final ForgeConfigSpec.EnumValue<Config.Text.DefaultFontBehavior> mDefaultFontBehavior;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> mDefaultFontRuleSet;

        public final ForgeConfigSpec.BooleanValue mUseComponentCache;

        public final ForgeConfigSpec.BooleanValue mAllowAsyncLayout;

        public final ForgeConfigSpec.EnumValue<Config.Text.LineBreakStyle> mLineBreakStyle;

        public final ForgeConfigSpec.EnumValue<Config.Text.LineBreakWordStyle> mLineBreakWordStyle;

        public final ForgeConfigSpec.BooleanValue mSmartSDFShaders;

        public final ForgeConfigSpec.BooleanValue mComputeDeviceFontSize;

        public final ForgeConfigSpec.BooleanValue mAllowSDFTextIn2D;

        private Text(@Nonnull ForgeConfigSpec.Builder builder) {
            builder.comment("Text Engine Config").push("text");
            this.mAllowShadow = builder.comment("Allow text renderer to drop shadow, setting to false can improve performance.").define("allowShadow", true);
            this.mFixedResolution = builder.comment("Fix resolution level at 2. When the GUI scale increases, the resolution level remains.", "Then GUI scale should be even numbers (2, 4, 6...), based on Minecraft GUI system.", "If your fonts are not bitmap fonts, then you should keep this setting false.").define("fixedResolution", false);
            this.mBaseFontSize = builder.comment("Control base font size, in GUI scaled pixels. The default and vanilla value is 8.", "For bitmap fonts, 8 represents a glyph size of 8x or 16x if fixed resolution.", "This option only applies to TrueType fonts.").defineInRange("baseFontSize", 8.0, 6.5, 9.5);
            this.mBaselineShift = builder.comment("Control vertical baseline for vanilla text layout, in GUI scaled pixels.", "The vanilla default value is 7.").defineInRange("baselineShift", 7.0, 4.0, 10.0);
            this.mShadowOffset = builder.comment("Control the text shadow offset for vanilla text rendering, in GUI scaled pixels.").defineInRange("shadowOffset", 0.8, 0.2F, 2.0);
            this.mOutlineOffset = builder.comment("Control the text outline offset for vanilla text rendering, in GUI scaled pixels.").defineInRange("outlineOffset", 0.5, 0.2F, 2.0);
            this.mCacheLifespan = builder.comment("Set the recycle time of layout cache in seconds, using least recently used algorithm.").defineInRange("cacheLifespan", 6, 2, 15);
            this.mTextDirection = builder.comment("The bidirectional text heuristic algorithm.", "This will affect which BiDi algorithm to use during text layout.").defineEnum("textDirection", Config.Text.TextDirection.FIRST_STRONG);
            this.mUseTextShadersInWorld = builder.comment("Whether to use Modern UI text rendering pipeline in 3D world.", "Disabling this means that SDF text and rendering optimization are no longer effective.", "But text rendering can be compatible with OptiFine Shaders and Iris Shaders.", "This does not affect text rendering in GUI.", "This option only applies to TrueType fonts.").define("useTextShadersInWorld", true);
            this.mDefaultFontBehavior = builder.comment("For \"minecraft:default\" font, should we keep some glyph providers of them?", "Ignore All: Only use Modern UI typeface list.", "Keep ASCII: Include minecraft:font/ascii.png, minecraft:font/accented.png, minecraft:font/nonlatin_european.png", "Keep Other: Include providers other than ASCII and Unicode font.", "Keep All: Include all except Unicode font.", "Only Include: Only include providers that specified by defaultFontRuleSet.", "Only Exclude: Only exclude providers that specified by defaultFontRuleSet.").defineEnum("defaultFontBehavior", Config.Text.DefaultFontBehavior.ONLY_EXCLUDE);
            this.mDefaultFontRuleSet = builder.comment("Used when defaultFontBehavior is either ONLY_INCLUDE or ONLY_EXCLUDE.", "This specifies a set of regular expressions to match the glyph provider name.", "For bitmap providers, this is the texture path without 'textures/'.", "For TTF providers, this is the TTF file path without 'font/'.", "For space providers, this is \"font_name / minecraft:space\",", "where font_name is font definition path without 'font/'.").defineList("defaultFontRuleSet", (Supplier<List<? extends String>>) (() -> {
                List<String> rules = new ArrayList();
                rules.add("^minecraft:font\\/(nonlatin_european|accented|ascii|element_ideographs|cjk_punctuations|ellipsis|2em_dash)\\.png$");
                rules.add("^minecraft:include\\/space \\/ minecraft:space$");
                return rules;
            }), s -> true);
            this.mUseComponentCache = builder.comment("Whether to use text component object as hash key to lookup in layout cache.", "If you find that Modern UI text rendering is not compatible with some mods,", "you can disable this option for compatibility, but this will decrease performance a bit.", "Modern UI will use another cache strategy if this is disabled.").define("useComponentCache", !ModernUIMod.isUntranslatedItemsLoaded());
            this.mAllowAsyncLayout = builder.comment("Allow text layout to be computed from background threads, which may lead to inconsistency issues.", "Otherwise, block the current thread and wait for main thread.").define("allowAsyncLayout", true);
            this.mLineBreakStyle = builder.comment("See CSS line-break property, https://developer.mozilla.org/en-US/docs/Web/CSS/line-break").defineEnum("lineBreakStyle", Config.Text.LineBreakStyle.AUTO);
            this.mLineBreakWordStyle = builder.defineEnum("lineBreakWordStyle", Config.Text.LineBreakWordStyle.AUTO);
            this.mSmartSDFShaders = builder.comment("When enabled, Modern UI will compute texel density in device-space to determine whether to use SDF text or bilinear sampling.", "This feature requires GLSL 400 or has no effect.", "This generally decreases performance but provides better rendering quality.", "This option only applies to TrueType fonts. May not be compatible with OptiFine.").define("smartSDFShaders", !ModernUIMod.isOptiFineLoaded());
            this.mComputeDeviceFontSize = builder.comment("When rendering in 2D, this option allows Modern UI to exactly compute font size in device-space from the current coordinate transform matrix.", "This provides perfect text rendering for scaling-down texts in vanilla, but may increase GPU memory usage.", "When disabled, Modern UI will use SDF text rendering if appropriate.", "This option only applies to TrueType fonts.").define("computeDeviceFontSize", true);
            this.mAllowSDFTextIn2D = builder.comment("When enabled, Modern UI will use SDF text rendering if appropriate.", "Otherwise, it uses nearest-neighbor or bilinear sampling based on texel density.", "This option only applies to TrueType fonts.").define("allowSDFTextIn2D", true);
            builder.pop();
        }

        public void saveAsync() {
            Util.ioPool().execute(() -> Config.TEXT_SPEC.save());
        }

        public void saveAndReloadAsync() {
            Util.ioPool().execute(() -> Config.TEXT_SPEC.save());
            this.reload();
        }

        void reload() {
            boolean reload = false;
            boolean reloadStrike = false;
            ModernTextRenderer.sAllowShadow = this.mAllowShadow.get();
            if (TextLayoutEngine.sFixedResolution != this.mFixedResolution.get()) {
                TextLayoutEngine.sFixedResolution = this.mFixedResolution.get();
                reload = true;
            }
            if ((double) TextLayoutProcessor.sBaseFontSize != this.mBaseFontSize.get()) {
                TextLayoutProcessor.sBaseFontSize = this.mBaseFontSize.get().floatValue();
                reloadStrike = true;
            }
            TextLayout.sBaselineOffset = this.mBaselineShift.get().floatValue();
            ModernTextRenderer.sShadowOffset = this.mShadowOffset.get().floatValue();
            ModernTextRenderer.sOutlineOffset = this.mOutlineOffset.get().floatValue();
            TextLayoutEngine.sCacheLifespan = this.mCacheLifespan.get();
            if (TextLayoutEngine.sTextDirection != ((Config.Text.TextDirection) this.mTextDirection.get()).key) {
                TextLayoutEngine.sTextDirection = ((Config.Text.TextDirection) this.mTextDirection.get()).key;
                reload = true;
            }
            if (TextLayoutEngine.sDefaultFontBehavior != ((Config.Text.DefaultFontBehavior) this.mDefaultFontBehavior.get()).key) {
                TextLayoutEngine.sDefaultFontBehavior = ((Config.Text.DefaultFontBehavior) this.mDefaultFontBehavior.get()).key;
                reload = true;
            }
            List<? extends String> defaultFontRuleSet = this.mDefaultFontRuleSet.get();
            if (!Objects.equals(TextLayoutEngine.sDefaultFontRuleSet, defaultFontRuleSet)) {
                TextLayoutEngine.sDefaultFontRuleSet = defaultFontRuleSet;
                reload = true;
            }
            TextLayoutEngine.sRawUseTextShadersInWorld = this.mUseTextShadersInWorld.get();
            TextLayoutEngine.sUseComponentCache = this.mUseComponentCache.get();
            TextLayoutEngine.sAllowAsyncLayout = this.mAllowAsyncLayout.get();
            if (TextLayoutProcessor.sLbStyle != ((Config.Text.LineBreakStyle) this.mLineBreakStyle.get()).key) {
                TextLayoutProcessor.sLbStyle = ((Config.Text.LineBreakStyle) this.mLineBreakStyle.get()).key;
                reload = true;
            }
            if (TextLayoutProcessor.sLbWordStyle != ((Config.Text.LineBreakWordStyle) this.mLineBreakWordStyle.get()).key) {
                TextLayoutProcessor.sLbWordStyle = ((Config.Text.LineBreakWordStyle) this.mLineBreakWordStyle.get()).key;
                reload = true;
            }
            boolean smartShaders = this.mSmartSDFShaders.get();
            Minecraft.getInstance().m_18707_(() -> TextRenderType.toggleSDFShaders(smartShaders));
            ModernTextRenderer.sComputeDeviceFontSize = this.mComputeDeviceFontSize.get();
            ModernTextRenderer.sAllowSDFTextIn2D = this.mAllowSDFTextIn2D.get();
            if (reloadStrike) {
                Minecraft.getInstance().m_18707_(() -> FontResourceManager.getInstance().reloadAll());
            } else if (reload && ModernUIMod.isTextEngineEnabled()) {
                Minecraft.getInstance().m_18707_(() -> {
                    try {
                        TextLayoutEngine.getInstance().reload();
                    } catch (Exception var1x) {
                    }
                });
            }
        }

        public static enum DefaultFontBehavior {

            IGNORE_ALL(0),
            KEEP_ASCII(1),
            KEEP_OTHER(2),
            KEEP_ALL(3),
            ONLY_INCLUDE(4),
            ONLY_EXCLUDE(5);

            private final int key;

            private DefaultFontBehavior(int key) {
                this.key = key;
            }

            @Nonnull
            public String toString() {
                return I18n.get("modernui.defaultFontBehavior." + this.name().toLowerCase(Locale.ROOT));
            }
        }

        public static enum LineBreakStyle {

            AUTO(0, "Auto"), LOOSE(1, "Loose"), NORMAL(2, "Normal"), STRICT(3, "Strict");

            private final int key;

            private final String text;

            private LineBreakStyle(int key, String text) {
                this.key = key;
                this.text = text;
            }

            public String toString() {
                return this.text;
            }
        }

        public static enum LineBreakWordStyle {

            AUTO(0, "Auto"), PHRASE(1, "Phrase-based");

            private final int key;

            private final String text;

            private LineBreakWordStyle(int key, String text) {
                this.key = key;
                this.text = text;
            }

            public String toString() {
                return this.text;
            }
        }

        public static enum TextDirection {

            FIRST_STRONG(1, "FirstStrong"),
            ANY_RTL(2, "AnyRTL-LTR"),
            LTR(3, "LTR"),
            RTL(4, "RTL"),
            LOCALE(5, "Locale"),
            FIRST_STRONG_LTR(6, "FirstStrong-LTR"),
            FIRST_STRONG_RTL(7, "FirstStrong-RTL");

            private final int key;

            private final String text;

            private TextDirection(int key, String text) {
                this.key = key;
                this.text = text;
            }

            public String toString() {
                return this.text;
            }
        }
    }
}