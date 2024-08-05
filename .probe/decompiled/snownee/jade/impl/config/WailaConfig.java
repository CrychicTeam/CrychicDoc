package snownee.jade.impl.config;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Collection;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.HumanoidArm;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.config.IWailaConfig;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.theme.Theme;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.util.CommonProxy;
import snownee.jade.util.JadeCodecs;
import snownee.jade.util.ModIdentification;

public class WailaConfig implements IWailaConfig {

    public static final Codec<WailaConfig> CODEC = RecordCodecBuilder.create(i -> i.group(WailaConfig.ConfigGeneral.CODEC.fieldOf("general").orElseGet(() -> JadeCodecs.createFromEmptyMap(WailaConfig.ConfigGeneral.CODEC)).forGetter(WailaConfig::getGeneral), WailaConfig.ConfigOverlay.CODEC.fieldOf("overlay").orElseGet(() -> JadeCodecs.createFromEmptyMap(WailaConfig.ConfigOverlay.CODEC)).forGetter(WailaConfig::getOverlay), WailaConfig.ConfigFormatting.CODEC.fieldOf("formatting").orElseGet(() -> JadeCodecs.createFromEmptyMap(WailaConfig.ConfigFormatting.CODEC)).forGetter(WailaConfig::getFormatting)).apply(i, WailaConfig::new));

    private final WailaConfig.ConfigGeneral general;

    private final WailaConfig.ConfigOverlay overlay;

    private final WailaConfig.ConfigFormatting formatting;

    public WailaConfig(WailaConfig.ConfigGeneral general, WailaConfig.ConfigOverlay overlay, WailaConfig.ConfigFormatting formatting) {
        this.general = general;
        this.overlay = overlay;
        this.formatting = formatting;
    }

    public WailaConfig.ConfigGeneral getGeneral() {
        return this.general;
    }

    public WailaConfig.ConfigOverlay getOverlay() {
        return this.overlay;
    }

    public WailaConfig.ConfigFormatting getFormatting() {
        return this.formatting;
    }

    @Override
    public IPluginConfig getPlugin() {
        return PluginConfig.INSTANCE;
    }

    public static class ConfigFormatting implements IWailaConfig.IConfigFormatting {

        public static final Codec<WailaConfig.ConfigFormatting> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.STRING.fieldOf("modName").orElse("§9§o%s").forGetter(WailaConfig.ConfigFormatting::getModName)).apply(i, WailaConfig.ConfigFormatting::new));

        private String modName = "§9§o%s";

        public ConfigFormatting(String modName) {
            this.modName = modName;
        }

        public ConfigFormatting() {
        }

        @Override
        public String getModName() {
            return this.modName;
        }

        @Override
        public void setModName(String modName) {
            this.modName = modName;
        }

        @Deprecated
        @Override
        public Component title(Object title) {
            return IThemeHelper.get().title(title);
        }

        @Override
        public Component registryName(String name) {
            return Component.literal(name).withStyle(IThemeHelper.get().isLightColorScheme() ? ChatFormatting.DARK_GRAY : ChatFormatting.GRAY);
        }
    }

    public static class ConfigGeneral implements IWailaConfig.IConfigGeneral {

        public static final Codec<WailaConfig.ConfigGeneral> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.BOOL.fieldOf("hintOverlayToggle").orElse(true).forGetter(it -> it.hintOverlayToggle), Codec.BOOL.fieldOf("hintNarratorToggle").orElse(true).forGetter(it -> it.hintNarratorToggle), Codec.BOOL.fieldOf("previewOverlay").orElse(true).forGetter($ -> $.previewOverlay), WailaConfig.ConfigGeneral.ConfigDisplay.CODEC.orElse(new WailaConfig.ConfigGeneral.ConfigDisplay()).forGetter($ -> $.configDisplay), Codec.BOOL.fieldOf("hideFromDebug").orElse(true).forGetter(WailaConfig.ConfigGeneral::shouldHideFromDebug), Codec.BOOL.fieldOf("hideFromTabList").orElse(true).forGetter(WailaConfig.ConfigGeneral::shouldHideFromTabList), Codec.BOOL.fieldOf("enableTextToSpeech").orElse(false).forGetter(WailaConfig.ConfigGeneral::shouldEnableTextToSpeech), StringRepresentable.fromEnum(IWailaConfig.TTSMode::values).fieldOf("ttsMode").orElse(IWailaConfig.TTSMode.PRESS).forGetter(WailaConfig.ConfigGeneral::getTTSMode), StringRepresentable.fromEnum(IWailaConfig.FluidMode::values).fieldOf("fluidMode").orElse(IWailaConfig.FluidMode.ANY).forGetter(WailaConfig.ConfigGeneral::getDisplayFluids), Codec.floatRange(0.0F, 20.0F).fieldOf("reachDistance").orElse(0.0F).forGetter(WailaConfig.ConfigGeneral::getReachDistance), Codec.BOOL.fieldOf("debug").orElse(false).forGetter(WailaConfig.ConfigGeneral::isDebug), Codec.BOOL.fieldOf("itemModNameTooltip").orElse(true).forGetter(WailaConfig.ConfigGeneral::showItemModNameTooltip), StringRepresentable.fromEnum(IWailaConfig.BossBarOverlapMode::values).fieldOf("bossBarOverlapMode").orElse(IWailaConfig.BossBarOverlapMode.PUSH_DOWN).forGetter(WailaConfig.ConfigGeneral::getBossBarOverlapMode), Codec.BOOL.fieldOf("builtinCamouflage").orElse(true).forGetter(WailaConfig.ConfigGeneral::getBuiltinCamouflage)).apply(i, WailaConfig.ConfigGeneral::new));

        public static final List<String> itemModNameTooltipDisabledByMods = Lists.newArrayList(new String[] { "emi" });

        public boolean hintOverlayToggle = true;

        public boolean hintNarratorToggle = true;

        public boolean previewOverlay = true;

        private WailaConfig.ConfigGeneral.ConfigDisplay configDisplay = new WailaConfig.ConfigGeneral.ConfigDisplay();

        private boolean hideFromDebug = true;

        private boolean hideFromTabList = true;

        private boolean enableTextToSpeech = false;

        private IWailaConfig.TTSMode ttsMode = IWailaConfig.TTSMode.PRESS;

        private IWailaConfig.FluidMode fluidMode = IWailaConfig.FluidMode.ANY;

        private float reachDistance = 0.0F;

        @Expose
        private boolean debug = false;

        private boolean itemModNameTooltip = true;

        private IWailaConfig.BossBarOverlapMode bossBarOverlapMode = IWailaConfig.BossBarOverlapMode.PUSH_DOWN;

        private boolean builtinCamouflage = true;

        public ConfigGeneral(boolean hintOverlayToggle, boolean hintNarratorToggle, boolean previewOverlay, WailaConfig.ConfigGeneral.ConfigDisplay configDisplay, boolean hideFromDebug, boolean hideFromTabList, boolean enableTextToSpeech, IWailaConfig.TTSMode ttsMode, IWailaConfig.FluidMode fluidMode, float reachDistance, boolean debug, boolean itemModNameTooltip, IWailaConfig.BossBarOverlapMode bossBarOverlapMode, boolean builtinCamouflage) {
            this.hintOverlayToggle = hintOverlayToggle;
            this.hintNarratorToggle = hintNarratorToggle;
            this.previewOverlay = previewOverlay;
            this.configDisplay = configDisplay;
            this.hideFromDebug = hideFromDebug;
            this.hideFromTabList = hideFromTabList;
            this.enableTextToSpeech = enableTextToSpeech;
            this.ttsMode = ttsMode;
            this.fluidMode = fluidMode;
            this.reachDistance = reachDistance;
            this.debug = debug;
            this.itemModNameTooltip = itemModNameTooltip;
            this.bossBarOverlapMode = bossBarOverlapMode;
            this.builtinCamouflage = builtinCamouflage;
        }

        public ConfigGeneral() {
        }

        public static void init() {
            List<String> names = itemModNameTooltipDisabledByMods.stream().filter(CommonProxy::isModLoaded).map($ -> (String) ModIdentification.getModName($).orElse($)).toList();
            itemModNameTooltipDisabledByMods.clear();
            itemModNameTooltipDisabledByMods.addAll(names);
        }

        @Override
        public void setHideFromDebug(boolean hideFromDebug) {
            this.hideFromDebug = hideFromDebug;
        }

        @Override
        public void toggleTTS() {
            this.enableTextToSpeech = !this.enableTextToSpeech;
        }

        @Override
        public boolean shouldDisplayTooltip() {
            return this.configDisplay.shouldDisplayTooltip();
        }

        @Override
        public void setDisplayTooltip(boolean displayTooltip) {
            this.configDisplay.setDisplayTooltip(displayTooltip);
        }

        @Override
        public boolean getDisplayBlocks() {
            return this.configDisplay.getDisplayBlocks();
        }

        @Override
        public void setDisplayBlocks(boolean displayBlocks) {
            this.configDisplay.setDisplayBlocks(displayBlocks);
        }

        @Override
        public boolean getDisplayEntities() {
            return this.configDisplay.getDisplayEntities();
        }

        @Override
        public void setDisplayEntities(boolean displayEntities) {
            this.configDisplay.setDisplayEntities(displayEntities);
        }

        @Override
        public boolean getDisplayBosses() {
            return this.configDisplay.getDisplayBosses();
        }

        @Override
        public void setDisplayBosses(boolean displayBosses) {
            this.configDisplay.setDisplayBosses(displayBosses);
        }

        @Override
        public IWailaConfig.DisplayMode getDisplayMode() {
            return this.configDisplay.getDisplayMode();
        }

        @Override
        public void setDisplayMode(IWailaConfig.DisplayMode displayMode) {
            this.configDisplay.setDisplayMode(displayMode);
        }

        @Override
        public boolean shouldHideFromDebug() {
            return this.hideFromDebug;
        }

        @Override
        public boolean shouldEnableTextToSpeech() {
            return this.ttsMode == IWailaConfig.TTSMode.TOGGLE && this.enableTextToSpeech;
        }

        @Override
        public IWailaConfig.TTSMode getTTSMode() {
            return this.ttsMode;
        }

        @Override
        public void setTTSMode(IWailaConfig.TTSMode ttsMode) {
            this.ttsMode = ttsMode;
        }

        @Override
        public boolean shouldDisplayFluids() {
            return this.fluidMode != IWailaConfig.FluidMode.NONE;
        }

        @Override
        public IWailaConfig.FluidMode getDisplayFluids() {
            return this.fluidMode;
        }

        @Override
        public void setDisplayFluids(boolean displayFluids) {
            this.fluidMode = displayFluids ? IWailaConfig.FluidMode.ANY : IWailaConfig.FluidMode.NONE;
        }

        @Override
        public void setDisplayFluids(IWailaConfig.FluidMode displayFluids) {
            this.fluidMode = displayFluids;
        }

        @Override
        public float getReachDistance() {
            return this.reachDistance;
        }

        @Override
        public void setReachDistance(float reachDistance) {
            this.reachDistance = Mth.clamp(reachDistance, 0.0F, 20.0F);
        }

        @Override
        public boolean isDebug() {
            return this.debug;
        }

        @Override
        public void setDebug(boolean debug) {
            this.debug = debug;
        }

        @Override
        public void setItemModNameTooltip(boolean itemModNameTooltip) {
            this.itemModNameTooltip = itemModNameTooltip;
        }

        @Override
        public boolean showItemModNameTooltip() {
            return this.itemModNameTooltip && itemModNameTooltipDisabledByMods.isEmpty();
        }

        @Override
        public IWailaConfig.BossBarOverlapMode getBossBarOverlapMode() {
            return this.bossBarOverlapMode;
        }

        @Override
        public void setBossBarOverlapMode(IWailaConfig.BossBarOverlapMode mode) {
            this.bossBarOverlapMode = mode;
        }

        @Override
        public void setHideFromTabList(boolean hideFromTabList) {
            this.hideFromTabList = hideFromTabList;
        }

        @Override
        public boolean shouldHideFromTabList() {
            return this.hideFromTabList;
        }

        @Override
        public boolean getBuiltinCamouflage() {
            return this.builtinCamouflage;
        }

        @Override
        public void setBuiltinCamouflage(boolean builtinCamouflage) {
            this.builtinCamouflage = builtinCamouflage;
        }

        private static class ConfigDisplay {

            public static final MapCodec<WailaConfig.ConfigGeneral.ConfigDisplay> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.BOOL.fieldOf("displayTooltip").orElse(true).forGetter(WailaConfig.ConfigGeneral.ConfigDisplay::shouldDisplayTooltip), Codec.BOOL.fieldOf("displayBlocks").orElse(true).forGetter(WailaConfig.ConfigGeneral.ConfigDisplay::getDisplayBlocks), Codec.BOOL.fieldOf("displayEntities").orElse(true).forGetter(WailaConfig.ConfigGeneral.ConfigDisplay::getDisplayEntities), Codec.BOOL.fieldOf("displayBosses").orElse(true).forGetter(WailaConfig.ConfigGeneral.ConfigDisplay::getDisplayBosses), StringRepresentable.fromEnum(IWailaConfig.DisplayMode::values).fieldOf("displayMode").orElse(IWailaConfig.DisplayMode.TOGGLE).forGetter(WailaConfig.ConfigGeneral.ConfigDisplay::getDisplayMode)).apply(i, WailaConfig.ConfigGeneral.ConfigDisplay::new));

            private boolean displayTooltip = true;

            private boolean displayBlocks = true;

            private boolean displayEntities = true;

            private boolean displayBosses = true;

            private IWailaConfig.DisplayMode displayMode = IWailaConfig.DisplayMode.TOGGLE;

            public ConfigDisplay(boolean displayTooltip, boolean displayBlocks, boolean displayEntities, boolean displayBosses, IWailaConfig.DisplayMode displayMode) {
                this.displayTooltip = displayTooltip;
                this.displayBlocks = displayBlocks;
                this.displayEntities = displayEntities;
                this.displayBosses = displayBosses;
                this.displayMode = displayMode;
            }

            public ConfigDisplay() {
            }

            public boolean shouldDisplayTooltip() {
                return this.displayTooltip;
            }

            public void setDisplayTooltip(boolean displayTooltip) {
                this.displayTooltip = displayTooltip;
            }

            public boolean getDisplayBlocks() {
                return this.displayBlocks;
            }

            public void setDisplayBlocks(boolean displayBlocks) {
                this.displayBlocks = displayBlocks;
            }

            public boolean getDisplayEntities() {
                return this.displayEntities;
            }

            public void setDisplayEntities(boolean displayEntities) {
                this.displayEntities = displayEntities;
            }

            public boolean getDisplayBosses() {
                return this.displayBosses;
            }

            public void setDisplayBosses(boolean displayBosses) {
                this.displayBosses = displayBosses;
            }

            public IWailaConfig.DisplayMode getDisplayMode() {
                return this.displayMode;
            }

            public void setDisplayMode(IWailaConfig.DisplayMode displayMode) {
                this.displayMode = displayMode;
            }
        }
    }

    public static class ConfigOverlay implements IWailaConfig.IConfigOverlay {

        public static final Codec<WailaConfig.ConfigOverlay> CODEC = RecordCodecBuilder.create(i -> i.group(ResourceLocation.CODEC.fieldOf("activeTheme").orElse(Theme.DARK.id).forGetter($ -> $.activeTheme), Codec.INT.optionalFieldOf("themesHash", 0).forGetter($ -> $.themesHash), Codec.FLOAT.fieldOf("overlayPosX").orElse(0.5F).forGetter(WailaConfig.ConfigOverlay::getOverlayPosX), Codec.FLOAT.fieldOf("overlayPosY").orElse(1.0F).forGetter(WailaConfig.ConfigOverlay::getOverlayPosY), Codec.floatRange(0.2F, 2.0F).fieldOf("overlayScale").orElse(1.0F).forGetter(WailaConfig.ConfigOverlay::getOverlayScale), Codec.FLOAT.fieldOf("overlayAnchorX").orElse(0.5F).forGetter(WailaConfig.ConfigOverlay::getAnchorX), Codec.FLOAT.fieldOf("overlayAnchorY").orElse(0.0F).forGetter(WailaConfig.ConfigOverlay::getAnchorY), Codec.BOOL.fieldOf("overlaySquare").orElse(false).forGetter(WailaConfig.ConfigOverlay::getSquare), Codec.BOOL.fieldOf("flipMainHand").orElse(false).forGetter(WailaConfig.ConfigOverlay::getFlipMainHand), Codec.floatRange(0.0F, 1.0F).fieldOf("autoScaleThreshold").orElse(0.4F).forGetter(WailaConfig.ConfigOverlay::getAutoScaleThreshold), Codec.floatRange(0.0F, 1.0F).fieldOf("alpha").orElse(0.7F).forGetter(WailaConfig.ConfigOverlay::getAlpha), StringRepresentable.fromEnum(IWailaConfig.IconMode::values).fieldOf("iconMode").orElse(IWailaConfig.IconMode.TOP).forGetter(WailaConfig.ConfigOverlay::getIconMode), Codec.BOOL.fieldOf("animation").orElse(true).forGetter(WailaConfig.ConfigOverlay::getAnimation), Codec.floatRange(0.0F, Float.MAX_VALUE).fieldOf("disappearingDelay").orElse(0.0F).forGetter(WailaConfig.ConfigOverlay::getDisappearingDelay)).apply(i, WailaConfig.ConfigOverlay::new));

        public ResourceLocation activeTheme = Theme.DARK.id;

        public int themesHash;

        private float overlayPosX = 0.5F;

        private float overlayPosY = 1.0F;

        private float overlayScale = 1.0F;

        private float overlayAnchorX = 0.5F;

        private float overlayAnchorY = 0.0F;

        private boolean overlaySquare = false;

        private boolean flipMainHand = false;

        @Expose
        private float autoScaleThreshold = 0.4F;

        private float alpha = 0.7F;

        private transient Theme activeThemeInstance;

        private IWailaConfig.IconMode iconMode = IWailaConfig.IconMode.TOP;

        private boolean animation = true;

        private float disappearingDelay;

        public ConfigOverlay() {
        }

        public ConfigOverlay(ResourceLocation activeTheme, int themesHash, float overlayPosX, float overlayPosY, float overlayScale, float overlayAnchorX, float overlayAnchorY, boolean overlaySquare, boolean flipMainHand, float autoScaleThreshold, float alpha, IWailaConfig.IconMode iconMode, boolean animation, float disappearingDelay) {
            this.activeTheme = activeTheme;
            this.themesHash = themesHash;
            this.overlayPosX = overlayPosX;
            this.overlayPosY = overlayPosY;
            this.overlayScale = overlayScale;
            this.overlayAnchorX = overlayAnchorX;
            this.overlayAnchorY = overlayAnchorY;
            this.overlaySquare = overlaySquare;
            this.flipMainHand = flipMainHand;
            this.autoScaleThreshold = autoScaleThreshold;
            this.alpha = alpha;
            this.iconMode = iconMode;
            this.animation = animation;
            this.disappearingDelay = disappearingDelay;
        }

        @Override
        public float getOverlayPosX() {
            return Mth.clamp(this.overlayPosX, 0.0F, 1.0F);
        }

        @Override
        public void setOverlayPosX(float overlayPosX) {
            this.overlayPosX = Mth.clamp(overlayPosX, 0.0F, 1.0F);
        }

        @Override
        public float getOverlayPosY() {
            return Mth.clamp(this.overlayPosY, 0.0F, 1.0F);
        }

        @Override
        public void setOverlayPosY(float overlayPosY) {
            this.overlayPosY = Mth.clamp(overlayPosY, 0.0F, 1.0F);
        }

        @Override
        public float getOverlayScale() {
            return this.overlayScale;
        }

        @Override
        public void setOverlayScale(float overlayScale) {
            this.overlayScale = Mth.clamp(overlayScale, 0.2F, 2.0F);
        }

        @Override
        public float getAnchorX() {
            return Mth.clamp(this.overlayAnchorX, 0.0F, 1.0F);
        }

        @Override
        public void setAnchorX(float overlayAnchorX) {
            this.overlayAnchorX = Mth.clamp(overlayAnchorX, 0.0F, 1.0F);
        }

        @Override
        public float getAnchorY() {
            return Mth.clamp(this.overlayAnchorY, 0.0F, 1.0F);
        }

        @Override
        public void setAnchorY(float overlayAnchorY) {
            this.overlayAnchorY = Mth.clamp(overlayAnchorY, 0.0F, 1.0F);
        }

        @Override
        public boolean getFlipMainHand() {
            return this.flipMainHand;
        }

        @Override
        public void setFlipMainHand(boolean overlaySquare) {
            this.flipMainHand = overlaySquare;
        }

        @Override
        public float tryFlip(float f) {
            if (this.flipMainHand && Minecraft.getInstance().options.mainHand().get() == HumanoidArm.LEFT) {
                f = 1.0F - f;
            }
            return f;
        }

        @Override
        public boolean getSquare() {
            return this.overlaySquare;
        }

        @Override
        public void setSquare(boolean overlaySquare) {
            this.overlaySquare = overlaySquare;
        }

        @Override
        public float getAutoScaleThreshold() {
            return this.autoScaleThreshold;
        }

        @Override
        public float getAlpha() {
            return this.alpha;
        }

        @Override
        public void setAlpha(float alpha) {
            this.alpha = Mth.clamp(alpha, 0.0F, 1.0F);
        }

        @Override
        public Theme getTheme() {
            if (this.activeThemeInstance == null) {
                this.applyTheme(this.activeTheme);
            }
            return this.activeThemeInstance;
        }

        @Deprecated
        @Override
        public Collection<Theme> getThemes() {
            return IThemeHelper.get().getThemes();
        }

        @Override
        public void applyTheme(ResourceLocation id) {
            this.activeThemeInstance = IThemeHelper.get().getTheme(id);
            this.activeTheme = this.activeThemeInstance.id;
            BoxStyle.DEFAULT.borderColor = this.activeThemeInstance.boxBorderColor;
        }

        @Override
        public IWailaConfig.IconMode getIconMode() {
            return this.iconMode;
        }

        @Override
        public void setIconMode(IWailaConfig.IconMode iconMode) {
            this.iconMode = iconMode;
        }

        @Override
        public boolean shouldShowIcon() {
            return this.iconMode != IWailaConfig.IconMode.HIDE;
        }

        @Override
        public boolean getAnimation() {
            return this.animation;
        }

        @Override
        public void setAnimation(boolean animation) {
            this.animation = animation;
        }

        @Override
        public void setDisappearingDelay(float delay) {
            this.disappearingDelay = delay;
        }

        @Override
        public float getDisappearingDelay() {
            return this.disappearingDelay;
        }
    }
}