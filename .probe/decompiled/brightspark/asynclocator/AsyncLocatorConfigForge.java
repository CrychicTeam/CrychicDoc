package brightspark.asynclocator;

import net.minecraftforge.common.ForgeConfigSpec;

public class AsyncLocatorConfigForge {

    public static ForgeConfigSpec SPEC = (ForgeConfigSpec) new ForgeConfigSpec.Builder().configure(builder -> {
        AsyncLocatorConfigForge.LOCATOR_THREADS = builder.worldRestart().comment("The maximum number of threads in the async locator thread pool.", "There's no upper bound to this, however this should only be increased if you're experiencing", "simultaneous location lookups causing issues AND you have the hardware capable of handling", "the extra possible threads.", "The default of 1 should be suitable for most users.").defineInRange("asyncLocatorThreads", 1, 1, Integer.MAX_VALUE);
        AsyncLocatorConfigForge.REMOVE_OFFER = builder.comment("When a merchant's treasure map offer ends up not finding a feature location,", "whether the offer should be removed or marked as out of stock.").define("removeMerchantInvalidMapOffer", false);
        builder.push("Feature Toggles");
        AsyncLocatorConfigForge.DOLPHIN_TREASURE_ENABLED = builder.comment("If true, enables asynchronous locating of structures for dolphin treasures.").define("dolphinTreasureEnabled", true);
        AsyncLocatorConfigForge.EYE_OF_ENDER_ENABLED = builder.comment("If true, enables asynchronous locating of structures when Eyes Of Ender are thrown.").define("eyeOfEnderEnabled", true);
        AsyncLocatorConfigForge.EXPLORATION_MAP_ENABLED = builder.comment("If true, enables asynchronous locating of structures for exploration maps found in chests.").define("explorationMspEnabled", true);
        AsyncLocatorConfigForge.LOCATE_COMMAND_ENABLED = builder.comment("If true, enables asynchronous locating of structures for the locate command.").define("locateCommandEnabled", true);
        AsyncLocatorConfigForge.VILLAGER_TRADE_ENABLED = builder.comment("If true, enables asynchronous locating of structures for villager trades.").define("villagerTradeEnabled", true);
        builder.pop();
        return null;
    }).getValue();

    public static ForgeConfigSpec.ConfigValue<Integer> LOCATOR_THREADS;

    public static ForgeConfigSpec.ConfigValue<Boolean> REMOVE_OFFER;

    public static ForgeConfigSpec.ConfigValue<Boolean> DOLPHIN_TREASURE_ENABLED;

    public static ForgeConfigSpec.ConfigValue<Boolean> EYE_OF_ENDER_ENABLED;

    public static ForgeConfigSpec.ConfigValue<Boolean> EXPLORATION_MAP_ENABLED;

    public static ForgeConfigSpec.ConfigValue<Boolean> LOCATE_COMMAND_ENABLED;

    public static ForgeConfigSpec.ConfigValue<Boolean> VILLAGER_TRADE_ENABLED;

    private AsyncLocatorConfigForge() {
    }
}