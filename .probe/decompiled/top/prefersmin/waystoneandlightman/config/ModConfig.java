package top.prefersmin.waystoneandlightman.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@EventBusSubscriber(modid = "waystoneandlightman", bus = Bus.MOD)
public class ModConfig {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue IS_ENABLE_MONEY_COST = BUILDER.comment("是否启用金钱消耗").define("isEnableMoneyCost", true);

    private static final ForgeConfigSpec.IntValue MONEY_COST_PER_HUNDRED_METER = BUILDER.comment("每一百米所需消耗的最低等级货币(将自动根据莱特曼货币配置换算)").defineInRange("moneyCostPerHundredMeter", 1, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.BooleanValue ROUND_UP = BUILDER.comment("是否向上取整，即不足一百米按一百米计算").define("roundUp", false);

    private static final ForgeConfigSpec.IntValue MINIMUM_COST = BUILDER.comment("每单次传送所需的最低消耗").defineInRange("minimumCost", 0, -1, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue MAXIMUM_COST = BUILDER.comment("每单次传送所的最大消耗上限").defineInRange("maximumCost", 100000, -1, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.BooleanValue ENABLE_CONSOLE_LOG = BUILDER.comment("是否启用控制台日志").define("enableConsoleLog", true);

    private static final ForgeConfigSpec.BooleanValue ENABLE_COST_TIP = BUILDER.comment("是否启用传送消耗提示").define("enableCostTip", true);

    private static final ForgeConfigSpec.BooleanValue FORCE_ENABLE_CHINESE_LANGUAGE = BUILDER.comment("是否强制启用中文语言").define("forceEnableChineseLanguage", false);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean isEnableMoneyCost;

    public static int moneyCostPerHundredMeter;

    public static boolean roundUp;

    public static int minimumCost;

    public static int maximumCost;

    public static boolean enableConsoleLog;

    public static boolean enableCostTip;

    public static boolean forceEnableChineseLanguage;

    @SubscribeEvent
    static void onLoad(ModConfigEvent event) {
        isEnableMoneyCost = IS_ENABLE_MONEY_COST.get();
        moneyCostPerHundredMeter = MONEY_COST_PER_HUNDRED_METER.get();
        roundUp = ROUND_UP.get();
        minimumCost = MINIMUM_COST.get();
        maximumCost = MAXIMUM_COST.get();
        enableConsoleLog = ENABLE_CONSOLE_LOG.get();
        enableCostTip = ENABLE_COST_TIP.get();
        forceEnableChineseLanguage = FORCE_ENABLE_CHINESE_LANGUAGE.get();
    }
}