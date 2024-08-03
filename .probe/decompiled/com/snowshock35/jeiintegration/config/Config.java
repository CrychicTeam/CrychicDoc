package com.snowshock35.jeiintegration.config;

import com.snowshock35.jeiintegration.JEIIntegration;
import java.util.List;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

public class Config {

    static final String CATEGORY_HANDLERS = "Handler Settings";

    static final String CATEGORY_TOOLTIPS = "Tooltip Settings";

    static final String CATEGORY_MISCELLANEOUS = "Miscellaneous Settings";

    private static final String defaultBurnTimeTooltipMode = "disabled";

    private static final String defaultDurabilityTooltipMode = "disabled";

    private static final String defaultEnchantabilityTooltipMode = "disabled";

    private static final String defaultFoodTooltipMode = "disabled";

    private static final String defaultMaxStackSizeTooltipMode = "disabled";

    private static final String defaultNbtTooltipMode = "disabled";

    private static final String defaultRegistryNameTooltipMode = "disabled";

    private static final String defaultTagsTooltipMode = "disabled";

    private static final String defaultTranslationKeyTooltipMode = "disabled";

    private static final List<String> validOptions = List.of("disabled", "enabled", "onShift", "onDebug", "onShiftAndDebug");

    public static final ForgeConfigSpec clientSpec;

    public static final Config.Client CLIENT;

    @SubscribeEvent
    public static void onLoad(ModConfigEvent.Loading configEvent) {
        JEIIntegration.logger.debug("Loaded JEI Integration config file {}", configEvent.getConfig().getFileName());
    }

    @SubscribeEvent
    public static void onFileChange(ModConfigEvent.Reloading configEvent) {
        JEIIntegration.logger.debug("JEI Integration config just got changed on the file system!");
    }

    static {
        Pair<Config.Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Config.Client::new);
        clientSpec = (ForgeConfigSpec) specPair.getRight();
        CLIENT = (Config.Client) specPair.getLeft();
    }

    public static class Client {

        public final ForgeConfigSpec.ConfigValue<String> burnTimeTooltipMode;

        public final ForgeConfigSpec.ConfigValue<String> durabilityTooltipMode;

        public final ForgeConfigSpec.ConfigValue<String> enchantabilityTooltipMode;

        public final ForgeConfigSpec.ConfigValue<String> foodTooltipMode;

        public final ForgeConfigSpec.ConfigValue<String> maxStackSizeTooltipMode;

        public final ForgeConfigSpec.ConfigValue<String> nbtTooltipMode;

        public final ForgeConfigSpec.ConfigValue<String> registryNameTooltipMode;

        public final ForgeConfigSpec.ConfigValue<String> tagsTooltipMode;

        public final ForgeConfigSpec.ConfigValue<String> translationKeyTooltipMode;

        Client(ForgeConfigSpec.Builder builder) {
            builder.comment("Handler Settings").comment(" Handler Options").push("handler_options");
            builder.pop();
            builder.comment("Miscellaneous Settings").comment(" Miscellaneous Options").push("misc_options");
            builder.pop();
            builder.comment("Tooltip Settings").comment(" Tooltip Options").comment(" Configure the options below to one of the following: disabled, enabled, onShift, onDebug or onShiftAndDebug").push("tooltip_options");
            this.burnTimeTooltipMode = builder.comment(" Configure tooltip for burn time.").translation("config.jeiintegration.tooltips.burnTimeTooltipMode").define("burnTimeTooltipMode", "disabled", o -> {
                if (o instanceof String string && Config.validOptions.contains(string)) {
                    return true;
                }
                return false;
            });
            this.durabilityTooltipMode = builder.comment(" Configure tooltip for durability.").translation("config.jeiintegration.tooltips.durabilityTooltipMode").define("durabilityTooltipMode", "disabled", o -> {
                if (o instanceof String string && Config.validOptions.contains(string)) {
                    return true;
                }
                return false;
            });
            this.enchantabilityTooltipMode = builder.comment(" Configure tooltip for enchantability").translation("config.jeiintegration.tooltips.enchantabilityTooltipMode").define("enchantabilityTooltipMode", "disabled", o -> {
                if (o instanceof String string && Config.validOptions.contains(string)) {
                    return true;
                }
                return false;
            });
            this.foodTooltipMode = builder.comment(" Configure tooltip for hunger and saturation.").translation("config.jeiintegration.tooltips.foodTooltipMode").define("foodTooltipMode", "disabled", o -> {
                if (o instanceof String string && Config.validOptions.contains(string)) {
                    return true;
                }
                return false;
            });
            this.maxStackSizeTooltipMode = builder.comment(" Configure tooltip for max stack size.").translation("config.jeiintegration.tooltips.maxStackSizeTooltipMode").define("maxStackSizeTooltipMode", "disabled", o -> {
                if (o instanceof String string && Config.validOptions.contains(string)) {
                    return true;
                }
                return false;
            });
            this.nbtTooltipMode = builder.comment(" Configure tooltip for NBT data.").translation("config.jeiintegration.tooltips.nbtTooltipMode").define("nbtTooltipMode", "disabled", o -> {
                if (o instanceof String string && Config.validOptions.contains(string)) {
                    return true;
                }
                return false;
            });
            this.registryNameTooltipMode = builder.comment(" Configure tooltip for registry name. E.g. minecraft:stone").translation("config.jeiintegration.tooltips.registryNameTooltipMode").define("registryNameTooltipMode", "disabled", o -> {
                if (o instanceof String string && Config.validOptions.contains(string)) {
                    return true;
                }
                return false;
            });
            this.tagsTooltipMode = builder.comment(" Configure tooltip for tags. E.g. forge:ingot, minecraft:planks").translation("config.jeiintegration.tooltips.tagsTooltipMode").define("tagsTooltipMode", "disabled", o -> {
                if (o instanceof String string && Config.validOptions.contains(string)) {
                    return true;
                }
                return false;
            });
            this.translationKeyTooltipMode = builder.comment(" Configure tooltip for translation key. E.g. block.minecraft.stone").translation("config.jeiintegration.tooltips.translationKeyTooltipMode").define("translationKeyTooltipMode", "disabled", o -> {
                if (o instanceof String string && Config.validOptions.contains(string)) {
                    return true;
                }
                return false;
            });
            builder.pop();
        }
    }
}