package se.mickelus.tetra;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import java.util.Collections;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import se.mickelus.tetra.items.modular.impl.ModularBladedItem;
import se.mickelus.tetra.items.modular.impl.ModularDoubleHeadedItem;
import se.mickelus.tetra.items.modular.impl.ModularSingleHeadedItem;
import se.mickelus.tetra.items.modular.impl.bow.ModularBowItem;
import se.mickelus.tetra.items.modular.impl.crossbow.ModularCrossbowItem;
import se.mickelus.tetra.items.modular.impl.shield.ModularShieldItem;

@ParametersAreNonnullByDefault
@EventBusSubscriber(bus = Bus.MOD)
public class ConfigHandler {

    private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec spec = builder.build();

    public static ForgeConfigSpec.BooleanValue development = builder.comment("Enables commands & data reloading functionality useful for development, has a negative impact on performance").worldRestart().define("development", false);

    public static ForgeConfigSpec.BooleanValue toolbeltCurioOnly = builder.comment("If enabled and Curios is installed, Toolbelts will only work in the Curio belt slot").define("toolbelt_curio_only", false);

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> experimentalFeatures = builder.comment("Features that are considered experimental can be listed here to enable them").defineListAllowEmpty("experimental_features", Collections.emptyList(), o -> FeatureFlag.matchesAnyKey(o));

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> disabledFeatures = builder.comment("Features can be listed here to disable them").defineListAllowEmpty("disabled_features", Collections.emptyList(), o -> FeatureFlag.matchesAnyKey(o));

    public static ForgeConfigSpec.BooleanValue moduleProgression = builder.define("enabled", true);

    public static ForgeConfigSpec.IntValue settleLimitBase = builder.comment("The base value for number of uses required for a module to settle").defineInRange("settle_base", 270, Integer.MIN_VALUE, Integer.MAX_VALUE);

    public static ForgeConfigSpec.DoubleValue settleLimitLevelMultiplier = builder.comment("Level multiplier for settling limit, a value of 3 would cause a module that has settled once to require 3x as many uses before it settles again").defineInRange("settle_level_multiplier", 3.0, Double.MIN_VALUE, Double.MAX_VALUE);

    public static ForgeConfigSpec.DoubleValue settleLimitDurabilityMultiplier = builder.comment("Durability multiplier for settling limit, a value of 1 would cause a module with 75 durability to require an additional 75 uses before it settles").defineInRange("settle_durability_multiplier", 0.5, Double.MIN_VALUE, Double.MAX_VALUE);

    public static ForgeConfigSpec.DoubleValue magicCapacityMultiplier = builder.comment("Multiplier for magic capacity gains, increasing this may be useful when having a large set enchantments added by other mods").defineInRange("magic_cap_multiplier", 1.0, 0.0, Double.MAX_VALUE);

    public static ForgeConfigSpec.IntValue honeSwordBase = builder.comment("The base value for number of uses required before a sword can be honed").defineInRange("hone_sword_base", 110, Integer.MIN_VALUE, Integer.MAX_VALUE);

    public static ForgeConfigSpec.IntValue honeSwordIntegrityMultiplier = builder.comment("Integrity multiplier for sword honing, a value of 2 would cause a sword which uses 3 integrity to require 2*3 times as many uses before it can be honed").defineInRange("hone_sword_integrity_multiplier", 65, Integer.MIN_VALUE, Integer.MAX_VALUE);

    public static ForgeConfigSpec.IntValue honedoubleBase = builder.comment("The base value for number of uses required before a tool can be honed").defineInRange("hone_double_base", 140, Integer.MIN_VALUE, Integer.MAX_VALUE);

    public static ForgeConfigSpec.IntValue honedoubleIntegrityMultiplier = builder.comment("Integrity multiplier for tool honing, a value of 2 would cause a sword which uses 3 integrity to require 2*3 times as many uses before it can be honed").defineInRange("hone_double_integrity_multiplier", 75, Integer.MIN_VALUE, Integer.MAX_VALUE);

    public static ForgeConfigSpec.IntValue honeSingleBase = builder.comment("The base value for number of uses required before a single headed implement can be honed").defineInRange("hone_single_headed_base", 120, Integer.MIN_VALUE, Integer.MAX_VALUE);

    public static ForgeConfigSpec.IntValue honeSingleIntegrityMultiplier = builder.comment("Integrity multiplier for single headed implement honing, a value of 2 would cause an implement which uses 3 integrity to require 2*3 times as many uses before it can be honed").defineInRange("hone_single_headed_integrity_multiplier", 60, Integer.MIN_VALUE, Integer.MAX_VALUE);

    public static ForgeConfigSpec.IntValue honeBowBase = builder.comment("The base value for number of uses required before a bow can be honed").defineInRange("hone_bow_base", 48, Integer.MIN_VALUE, Integer.MAX_VALUE);

    public static ForgeConfigSpec.IntValue honeBowIntegrityMultiplier = builder.comment("Integrity multiplier for bow honing, a value of 2 would cause a bow which uses 3 integrity to require 2*3 times as many uses before it can be honed").defineInRange("hone_bow_integrity_multiplier", 32, Integer.MIN_VALUE, Integer.MAX_VALUE);

    public static ForgeConfigSpec.IntValue honeCrossbowBase = builder.comment("The base value for number of uses required before a crossbow can be honed").defineInRange("hone_crossbow_base", 48, Integer.MIN_VALUE, Integer.MAX_VALUE);

    public static ForgeConfigSpec.IntValue honeCrossbowIntegrityMultiplier = builder.comment("Integrity multiplier for crossbow honing, a value of 2 would cause a crossbow which uses 3 integrity to require 2*3 times as many uses before it can be honed").defineInRange("hone_crossbow_integrity_multiplier", 32, Integer.MIN_VALUE, Integer.MAX_VALUE);

    public static ForgeConfigSpec.IntValue honeShieldBase = builder.comment("The base value for number of uses required before a shield can be honed").defineInRange("hone_shield_base", 48, Integer.MIN_VALUE, Integer.MAX_VALUE);

    public static ForgeConfigSpec.IntValue honeShieldIntegrityMultiplier = builder.comment("Integrity multiplier for shield honing, a value of 2 would cause a shield which uses 3 integrity to require 2*3 times as many uses before it can be honed").defineInRange("hone_shield_integrity_multiplier", 32, Integer.MIN_VALUE, Integer.MAX_VALUE);

    public static ForgeConfigSpec.BooleanValue enableBow = builder.comment("Enable modular bows").worldRestart().define("bow", true);

    public static ForgeConfigSpec.BooleanValue enableCrossbow = builder.comment("Enable modular crossbows").worldRestart().define("crossbow", true);

    public static ForgeConfigSpec.BooleanValue enableSingle = builder.comment("Enable modular single headed implements").worldRestart().define("single_headed", true);

    public static ForgeConfigSpec.BooleanValue enableShield = builder.comment("Enable modular shields").worldRestart().define("shield", true);

    public static ForgeConfigSpec.BooleanValue enableGlint = builder.comment("Enables the enchantment glint rendering on modular items").define("glint", true);

    public static ForgeConfigSpec.BooleanValue enableExtractor = builder.comment("Enable the extractor bedrock functionality").worldRestart().define("extractor", true);

    public static void setup() {
        CommentedFileConfig configData = (CommentedFileConfig) CommentedFileConfig.builder(FMLPaths.CONFIGDIR.get().resolve("tetra.toml")).sync().autosave().preserveInsertionOrder().writingMode(WritingMode.REPLACE).build();
        configData.load();
        spec.setConfig(configData);
    }

    @SubscribeEvent
    public static void onLoad(ModConfigEvent.Loading configEvent) {
        onModConfigLoad();
    }

    @SubscribeEvent
    public static void onReload(ModConfigEvent.Reloading configEvent) {
        onModConfigLoad();
    }

    private static void onModConfigLoad() {
        ModularBladedItem.instance.updateConfig(honeSwordBase.get(), honeSwordIntegrityMultiplier.get());
        ModularDoubleHeadedItem.instance.updateConfig(honedoubleBase.get(), honedoubleIntegrityMultiplier.get());
        if (ModularBowItem.instance != null) {
            ModularBowItem.instance.updateConfig(honeBowBase.get(), honeBowIntegrityMultiplier.get());
        }
        if (ModularCrossbowItem.instance != null) {
            ModularCrossbowItem.instance.updateConfig(honeCrossbowBase.get(), honeCrossbowIntegrityMultiplier.get());
        }
        if (ModularSingleHeadedItem.instance != null) {
            ModularSingleHeadedItem.instance.updateConfig(honeSingleBase.get(), honeSingleIntegrityMultiplier.get());
        }
        if (ModularShieldItem.instance != null) {
            ModularShieldItem.instance.updateConfig(honeShieldBase.get(), honeShieldIntegrityMultiplier.get());
        }
    }

    static {
        builder.push("misc");
        builder.pop();
        builder.comment("Allows tetra items to \"level up\" after being used a certain amount of times, allowing the player to choose from different ways to \"hone\" 1 module on the item. Major modules also settle after some time, increasing its integrity").push("module_progression");
        builder.pop();
        builder.comment("Toggles & config for experimental features").push("experimental");
        builder.pop();
    }
}