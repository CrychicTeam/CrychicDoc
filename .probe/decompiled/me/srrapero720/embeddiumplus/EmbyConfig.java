package me.srrapero720.embeddiumplus;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.mojang.blaze3d.platform.Window;
import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import me.srrapero720.embeddiumplus.mixins.impl.borderless.accessors.MainWindowAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

@EventBusSubscriber(modid = "embeddiumplus", bus = Bus.MOD, value = { Dist.CLIENT })
public class EmbyConfig {

    public static final Marker IT = MarkerManager.getMarker("Config");

    public static final ForgeConfigSpec SPECS;

    public static final ForgeConfigSpec.EnumValue<EmbyConfig.FullScreenMode> fullScreen;

    public static final ForgeConfigSpec.EnumValue<EmbyConfig.FPSDisplayMode> fpsDisplayMode;

    public static final ForgeConfigSpec.EnumValue<EmbyConfig.FPSDisplayGravity> fpsDisplayGravity;

    public static final ForgeConfigSpec.EnumValue<EmbyConfig.FPSDisplaySystemMode> fpsDisplaySystemMode;

    public static final ForgeConfigSpec.IntValue fpsDisplayMargin;

    public static final ForgeConfigSpec.BooleanValue fpsDisplayShadow;

    public static int fpsDisplayMarginCache;

    public static boolean fpsDisplayShadowCache;

    public static final ForgeConfigSpec.BooleanValue fog;

    public static final ForgeConfigSpec.BooleanValue blueBand;

    public static final ForgeConfigSpec.IntValue cloudsHeight;

    public static final ForgeConfigSpec.BooleanValue disableNameTagRender;

    public static final ForgeConfigSpec.EnumValue<EmbyConfig.ChunkFadeSpeed> chunkFadeSpeed;

    public static boolean fogCache;

    public static boolean blueBandCache;

    public static int cloudsHeightCache;

    public static boolean disableNameTagRenderCache;

    public static final ForgeConfigSpec.EnumValue<EmbyConfig.DarknessMode> darknessMode;

    public static final ForgeConfigSpec.BooleanValue darknessOnOverworld;

    public static final ForgeConfigSpec.BooleanValue darknessOnNether;

    public static final ForgeConfigSpec.DoubleValue darknessNetherFogBright;

    public static final ForgeConfigSpec.BooleanValue darknessOnEnd;

    public static final ForgeConfigSpec.DoubleValue darknessEndFogBright;

    public static final ForgeConfigSpec.BooleanValue darknessByDefault;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> darknessDimensionWhiteList;

    public static final ForgeConfigSpec.BooleanValue darknessOnNoSkyLight;

    public static final ForgeConfigSpec.BooleanValue darknessBlockLightOnly;

    public static final ForgeConfigSpec.BooleanValue darknessAffectedByMoonPhase;

    public static final ForgeConfigSpec.DoubleValue darknessNewMoonBright;

    public static final ForgeConfigSpec.DoubleValue darknessFullMoonBright;

    public static boolean darknessOnOverworldCache;

    public static boolean darknessOnNetherCache;

    public static double darknessNetherFogBrightCache;

    public static boolean darknessOnEndCache;

    public static double darknessEndFogBrightCache;

    public static boolean darknessByDefaultCache;

    public static boolean darknessOnNoSkyLightCache;

    public static boolean darknessBlockLightOnlyCache;

    public static boolean darknessAffectedByMoonPhaseCache;

    public static double darknessNewMoonBrightCache;

    public static double darknessFullMoonBrightCache;

    public static final ForgeConfigSpec.BooleanValue hideJREMI;

    public static final ForgeConfigSpec.BooleanValue fontShadows;

    public static final ForgeConfigSpec.EnumValue<EmbyConfig.LeavesCullingMode> leavesCulling;

    public static final ForgeConfigSpec.BooleanValue fastChests;

    public static final ForgeConfigSpec.BooleanValue fastBeds;

    public static boolean hideJREMICache;

    public static boolean fontShadowsCache;

    public static boolean fastChestsCache;

    public static boolean fastBedsCache;

    public static final ForgeConfigSpec.BooleanValue tileEntityDistanceCulling;

    public static final ForgeConfigSpec.IntValue tileEntityCullingDistanceX;

    public static final ForgeConfigSpec.IntValue tileEntityCullingDistanceY;

    public static final ForgeConfigSpec.BooleanValue entityDistanceCulling;

    public static final ForgeConfigSpec.IntValue entityCullingDistanceX;

    public static final ForgeConfigSpec.IntValue entityCullingDistanceY;

    public static final ForgeConfigSpec.BooleanValue monsterDistanceCulling;

    public static final ForgeConfigSpec.IntValue monsterCullingDistanceX;

    public static final ForgeConfigSpec.IntValue monsterCullingDistanceY;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> entityWhitelist;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> monsterWhitelist;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> tileEntityWhitelist;

    public static boolean tileEntityDistanceCullingCache;

    public static int tileEntityCullingDistanceXCache;

    public static int tileEntityCullingDistanceYCache;

    public static boolean entityDistanceCullingCache;

    public static int entityCullingDistanceXCache;

    public static int entityCullingDistanceYCache;

    public static boolean monsterDistanceCullingCache;

    public static int monsterCullingDistanceXCache;

    public static int monsterCullingDistanceYCache;

    public static final ForgeConfigSpec.EnumValue<EmbyConfig.AttachMode> borderlessAttachModeF11;

    public static final ForgeConfigSpec.BooleanValue fastLanguageReload;

    public static boolean fastLanguageReloadCache;

    public static final ForgeConfigSpec.EnumValue<EmbyConfig.DynLightsSpeed> dynLightSpeed;

    public static final ForgeConfigSpec.BooleanValue dynLightsOnEntities;

    public static final ForgeConfigSpec.BooleanValue dynLightsOnTileEntities;

    public static final ForgeConfigSpec.BooleanValue dynLightsUpdateOnPositionChange;

    public static volatile boolean dynLightsOnEntitiesCache;

    public static volatile boolean dynLightsOnTileEntitiesCache;

    public static volatile boolean dynLightsUpdateOnPositionChangeCache;

    private static final String[] DEFAULT_TILE_ENTITIES_WHITELIST = new String[] { "waterframes:*" };

    private static final String[] DEFAULT_ENTITIES_WHITELIST = new String[] { "minecraft:ghast", "minecraft:ender_dragon", "iceandfire:*", "create:*" };

    public static boolean isLoaded() {
        return SPECS.isLoaded();
    }

    public static void load() {
        if (!isLoaded()) {
            EmbeddiumPlus.LOGGER.warn(IT, "Loading Embeddium++Config");
            Path path = FMLPaths.CONFIGDIR.get().resolve("embeddium++.toml");
            try {
                CommentedFileConfig configData = (CommentedFileConfig) CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
                configData.load();
                SPECS.setConfig(configData);
                updateCache(null);
            } catch (Exception var3) {
                File file = path.toFile();
                if (!file.exists()) {
                    throw new RuntimeException("Failed to read configuration file");
                }
                if (!file.delete()) {
                    throw new RuntimeException("Failed to remove corrupted configuration file");
                }
                load();
            }
        }
    }

    @SubscribeEvent
    public static void updateCache(ModConfigEvent ignored) {
        EmbeddiumPlus.LOGGER.info(IT, "Updating config cache");
        fpsDisplayMarginCache = fpsDisplayMargin.get();
        fpsDisplayShadowCache = fpsDisplayShadow.get();
        fogCache = fog.get();
        blueBandCache = blueBand.get();
        cloudsHeightCache = cloudsHeight.get();
        disableNameTagRenderCache = disableNameTagRender.get();
        darknessOnOverworldCache = darknessOnOverworld.get();
        darknessOnNetherCache = darknessOnNether.get();
        darknessNetherFogBrightCache = darknessNetherFogBright.get();
        darknessOnEndCache = darknessOnEnd.get();
        darknessEndFogBrightCache = darknessEndFogBright.get();
        darknessByDefaultCache = darknessByDefault.get();
        darknessOnNoSkyLightCache = darknessOnNoSkyLight.get();
        darknessBlockLightOnlyCache = darknessBlockLightOnly.get();
        darknessAffectedByMoonPhaseCache = darknessAffectedByMoonPhase.get();
        darknessNewMoonBrightCache = darknessNewMoonBright.get();
        darknessFullMoonBrightCache = darknessFullMoonBright.get();
        hideJREMICache = hideJREMI.get();
        fontShadowsCache = fontShadows.get();
        fastChestsCache = fastChests.get();
        fastBedsCache = fastBeds.get();
        tileEntityDistanceCullingCache = tileEntityDistanceCulling.get();
        tileEntityCullingDistanceXCache = tileEntityCullingDistanceX.get();
        tileEntityCullingDistanceYCache = tileEntityCullingDistanceY.get();
        entityDistanceCullingCache = entityDistanceCulling.get();
        entityCullingDistanceXCache = entityCullingDistanceX.get();
        entityCullingDistanceYCache = entityCullingDistanceY.get();
        monsterDistanceCullingCache = monsterDistanceCulling.get();
        monsterCullingDistanceXCache = monsterCullingDistanceX.get();
        monsterCullingDistanceYCache = monsterCullingDistanceY.get();
        fastLanguageReloadCache = fastLanguageReload.get();
        dynLightsOnEntitiesCache = dynLightsOnEntities.get();
        dynLightsOnTileEntitiesCache = dynLightsOnTileEntities.get();
        dynLightsUpdateOnPositionChangeCache = dynLightsUpdateOnPositionChange.get();
        EmbeddiumPlus.LOGGER.info(IT, "Cache updated successfully");
    }

    public static void setFullScreenMode(Options opts, EmbyConfig.FullScreenMode value) {
        fullScreen.set(value);
        opts.fullscreen.set(value != EmbyConfig.FullScreenMode.WINDOWED);
        Minecraft client = Minecraft.getInstance();
        Window window = client.getWindow();
        if (window.isFullscreen() != opts.fullscreen.get()) {
            window.toggleFullScreen();
            opts.fullscreen.set(window.isFullscreen());
        }
        if (opts.fullscreen.get()) {
            ((MainWindowAccessor) window).setDirty(true);
            window.changeFullscreenVideoMode();
        }
    }

    static {
        ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        BUILDER.push("embeddiumplus");
        BUILDER.push("general");
        fullScreen = BUILDER.comment("Set Fullscreen mode", "Borderless let you change between screens more faster and move your mouse across monitors").defineEnum("fullscreen", EmbyConfig.FullScreenMode.WINDOWED);
        fpsDisplayMode = BUILDER.comment("Configure FPS Display mode", "Complete mode gives you min FPS count and average count").defineEnum("fpsDisplay", EmbyConfig.FPSDisplayMode.ADVANCED);
        fpsDisplayGravity = BUILDER.comment("Configure FPS Display gravity", "Places counter on specified corner of your screen").defineEnum("fpsDisplayGravity", EmbyConfig.FPSDisplayGravity.LEFT);
        fpsDisplaySystemMode = BUILDER.comment("Shows GPU and memory usage onto FPS display").defineEnum("fpsDisplaySystem", EmbyConfig.FPSDisplaySystemMode.OFF);
        fpsDisplayMargin = BUILDER.comment("Configure FPS Display margin", "Give some space between corner and text").defineInRange("fpsDisplayMargin", 12, 0, 48);
        fpsDisplayShadow = BUILDER.comment("Toggle FPS Display shadow", "In case sometimes you can't see the text").define("fpsDisplayShadow", false);
        BUILDER.pop();
        BUILDER.push("quality");
        fog = BUILDER.comment("Toggle fog feature", "Fog was a vanilla feature").define("fog", true);
        blueBand = BUILDER.comment("Clean my skies", "Blue band was a vanilla feature, toggle off will show sky color directly").define("blueBand", true);
        cloudsHeight = BUILDER.comment("Raise clouds", "Modify clouds height perfect for a adaptative world experience").defineInRange("cloudsHeight", 192, 0, 512);
        disableNameTagRender = BUILDER.comment("Do not show me your name", "disables nametag rendering for players and entities").define("disableNameTagRendering", false);
        chunkFadeSpeed = BUILDER.comment("Chunks fade in speed", "This option doesn't affect performance, just changes speed").defineEnum("chunkFadeSpeed", EmbyConfig.ChunkFadeSpeed.SLOW);
        BUILDER.push("darkness");
        darknessMode = BUILDER.comment("Configure Darkness Mode", "Each config changes what is considered 'true darkness'").defineEnum("mode", EmbyConfig.DarknessMode.OFF);
        darknessOnOverworld = BUILDER.comment("Toggle Darkness on Overworld dimension").define("enableOnOverworld", true);
        darknessOnNether = BUILDER.comment("Toggle Darkness on Nether dimension").define("enableOnNether", false);
        darknessNetherFogBright = BUILDER.comment("Configure fog brightness on nether when darkness is enabled").defineInRange("netherFogBright", 0.5, 0.0, 1.0);
        darknessOnEnd = BUILDER.comment("Toggle Darkness on End dimension").define("enableOnEnd", false);
        darknessEndFogBright = BUILDER.comment("Configure fog brightness on nether when darkness is enabled").defineInRange("endFogBright", 0.5, 0.0, 1.0);
        darknessByDefault = BUILDER.comment("Toggle Darkness default mode for modded dimensions").define("valueByDefault", false);
        darknessDimensionWhiteList = BUILDER.comment("List of all dimensions to use True Darkness", "This option overrides 'valueByDefault' state").defineListAllowEmpty(Collections.singletonList("dimensionWhitelist"), Collections::emptyList, s -> s.toString().contains(":"));
        darknessOnNoSkyLight = BUILDER.comment("Toggle darkness when dimension has no SkyLight").define("enableOnNoSkyLight", false);
        darknessBlockLightOnly = BUILDER.comment("Disables all bright sources of darkness like moon or fog", "Only affects darkness effect").define("enableBlockLightOnly", false);
        darknessAffectedByMoonPhase = BUILDER.comment("Toggles if moon phases affects darkness in the overworld").define("affectedByMoonPhase", true);
        darknessFullMoonBright = BUILDER.comment("Configure max moon brightness level with darkness").defineInRange("fullMoonBright", 0.25, 0.0, 1.0);
        darknessNewMoonBright = BUILDER.comment("Configure min moon brightness level with darkness").defineInRange("newMoonBright", 0.0, 0.0, 1.0);
        BUILDER.pop(2);
        BUILDER.push("performance");
        leavesCulling = BUILDER.comment("Sets culling mode", "Reduces number of visible faces when the neighbor blocks are leaves").defineEnum("leavesCulling", EmbyConfig.LeavesCullingMode.OFF);
        hideJREMI = BUILDER.comment("Toggles JREI item rendering until searching", "Increases performance a little bit and cleans your screen when you don't want to use it").define("hideJREI", false);
        fontShadows = BUILDER.comment("Toggles Minecraft Fonts shadows", "Depending of the case may increase performance", "Gives a flat style text").define("fontShadows", true);
        BUILDER.push("fastModels");
        fastChests = BUILDER.comment("Toggles FastChest feature", "Without flywheel installed or using any backend, it increases FPS significatly on chest rooms").define("enableChests", false);
        fastBeds = BUILDER.comment("Toggles FastBeds feature").define("enableBeds", false);
        BUILDER.pop();
        BUILDER.push("distanceCulling");
        BUILDER.push("tileEntities");
        tileEntityDistanceCulling = BUILDER.comment("Toggles distance culling for Block Entities", "Maybe you use another mod for that :(").define("enable", true);
        tileEntityCullingDistanceX = BUILDER.comment("Configure horizontal max distance before cull Block entities", "Value is squared, default was 64^2 (or 64x64)").defineInRange("cullingMaxDistanceX", 4096, 0, Integer.MAX_VALUE);
        tileEntityCullingDistanceY = BUILDER.comment("Configure vertical max distance before cull Block entities", "Value is raw").defineInRange("cullingMaxDistanceY", 32, 0, 512);
        tileEntityWhitelist = BUILDER.comment("List of all Block Entities to be ignored by distance culling", "Uses ResourceLocation to identify it", "Example 1: \"minecraft:chest\" - Ignores chests only", "Example 2: \"ae2:*\" - ignores all Block entities from Applied Energetics 2").defineListAllowEmpty(Collections.singletonList("whitelist"), Arrays.asList(EmbyConfig.DEFAULT_TILE_ENTITIES_WHITELIST), s -> s.toString().contains(":"));
        BUILDER.pop();
        BUILDER.push("entities");
        entityDistanceCulling = BUILDER.comment("Toggles distance culling for entities, doesn't affect monsters culling", "Check the options below").define("enable", true);
        entityCullingDistanceX = BUILDER.comment("Configure horizontal max distance before cull entities", "Value is squared, default was 64^2 (or 64x64)").defineInRange("cullingMaxDistanceX", 4096, 0, Integer.MAX_VALUE);
        entityCullingDistanceY = BUILDER.comment("Configure vertical max distance before cull entities", "Value is raw").defineInRange("cullingMaxDistanceY", 32, 0, 512);
        entityWhitelist = BUILDER.comment("List of all Entities to be ignored by distance culling", "Uses ResourceLocation to identify it", "Example 1: \"minecraft:bat\" - Ignores bats only", "Example 2: \"alexsmobs:*\" - ignores all entities for alexmobs mod").defineListAllowEmpty(Collections.singletonList("whitelist"), Arrays.asList(EmbyConfig.DEFAULT_ENTITIES_WHITELIST), s -> s.toString().contains(":"));
        BUILDER.push("monsters");
        monsterDistanceCulling = BUILDER.comment("Toggles distance culling for monsters (or hostile entities, whatever you want to call it), doesn't affect neutral/pacific entities", "Check the options above").define("enable", false);
        monsterCullingDistanceX = BUILDER.comment("Configure horizontal max distance before cull monster entities", "Value is squared, default was 64^2 (or 64x64)").defineInRange("cullingMaxDistanceX", 16384, 0, Integer.MAX_VALUE);
        monsterCullingDistanceY = BUILDER.comment("Configure vertical max distance before cull monster entities", "Value is raw").defineInRange("cullingMaxDistanceY", 64, 0, 512);
        monsterWhitelist = BUILDER.comment("List of all monster entities to be ignored by distance culling", "Uses ResourceLocation to identify it", "Example 1: \"minecraft:bat\" - Ignores bats only", "Example 2: \"alexsmobs:*\" - ignores all entities for alexmobs mod").defineListAllowEmpty(Collections.singletonList("whitelist"), Arrays.asList(EmbyConfig.DEFAULT_ENTITIES_WHITELIST), s -> s.toString().contains(":"));
        BUILDER.pop(4);
        BUILDER.push("others");
        borderlessAttachModeF11 = BUILDER.comment("Configure if borderless fullscreen option should be attached to F11 or replace vanilla fullscreen").defineEnum("borderlessAttachModeOnF11", EmbyConfig.AttachMode.ATTACH);
        fastLanguageReload = BUILDER.comment("Toggles fast language reload", "Embeddedt points it maybe cause troubles to JEI, so ¿why not add it as a toggleable option?").define("fastLanguageReload", true);
        BUILDER.pop();
        BUILDER.push("dynlights");
        dynLightSpeed = BUILDER.comment("Configure how fast light whould be updated").defineEnum("updateSpeed", EmbyConfig.DynLightsSpeed.REALTIME);
        dynLightsOnEntities = BUILDER.comment("Toggle if Entities should have dynamic lights").define("onEntities", true);
        dynLightsOnTileEntities = BUILDER.comment("Toggle if Block Entities should have dynamic lights").define("onTileEntities", true);
        dynLightsUpdateOnPositionChange = BUILDER.define("updateOnlyOnPositionChange", true);
        BUILDER.pop();
        SPECS = BUILDER.build();
    }

    public static enum AttachMode {

        ATTACH, REPLACE, OFF
    }

    public static enum ChunkFadeSpeed {

        OFF, FAST, SLOW
    }

    public static enum DarknessMode {

        TOTAL_DARKNESS(0.04F), PITCH_BLACK(0.0F), DARK(0.08F), DIM(0.12F), OFF(-1.0F);

        public final float value;

        private DarknessMode(float value) {
            this.value = value;
        }
    }

    public static enum DynLightsSpeed {

        OFF(-1),
        SLOW(750),
        NORMAL(500),
        FAST(250),
        SUPERFAST(100),
        FASTESTS(50),
        REALTIME(-1);

        private final int delay;

        private DynLightsSpeed(int delay) {
            this.delay = delay;
        }

        public int getDelay() {
            return this.delay;
        }

        public boolean off() {
            return this == OFF;
        }
    }

    public static enum FPSDisplayGravity {

        LEFT, CENTER, RIGHT
    }

    public static enum FPSDisplayMode {

        OFF, SIMPLE, ADVANCED;

        public boolean off() {
            return this == OFF;
        }
    }

    public static enum FPSDisplaySystemMode {

        OFF, ON, GPU, RAM;

        public boolean ram() {
            return this == RAM || this == ON;
        }

        public boolean gpu() {
            return this == GPU || this == ON;
        }

        public boolean off() {
            return this == OFF;
        }
    }

    public static enum FullScreenMode {

        WINDOWED, BORDERLESS, FULLSCREEN;

        public static EmbyConfig.FullScreenMode nextOf(EmbyConfig.FullScreenMode current) {
            return switch(current) {
                case WINDOWED ->
                    BORDERLESS;
                case BORDERLESS ->
                    FULLSCREEN;
                case FULLSCREEN ->
                    WINDOWED;
            };
        }

        public static EmbyConfig.FullScreenMode nextBorderless(EmbyConfig.FullScreenMode current) {
            return switch(current) {
                case WINDOWED ->
                    BORDERLESS;
                case BORDERLESS, FULLSCREEN ->
                    WINDOWED;
            };
        }

        public static EmbyConfig.FullScreenMode nextFullscreen(EmbyConfig.FullScreenMode current) {
            return switch(current) {
                case WINDOWED ->
                    FULLSCREEN;
                case BORDERLESS, FULLSCREEN ->
                    WINDOWED;
            };
        }

        public static EmbyConfig.FullScreenMode getVanillaConfig() {
            return Minecraft.getInstance().options.fullscreen().get() ? BORDERLESS : WINDOWED;
        }

        public boolean isBorderless() {
            return this == BORDERLESS;
        }
    }

    public static enum LeavesCullingMode {

        ALL, OFF
    }
}