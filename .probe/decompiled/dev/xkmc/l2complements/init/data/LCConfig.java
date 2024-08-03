package dev.xkmc.l2complements.init.data;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;
import org.apache.commons.lang3.tuple.Pair;

public class LCConfig {

    public static final ForgeConfigSpec CLIENT_SPEC;

    public static final LCConfig.Client CLIENT;

    public static final ForgeConfigSpec COMMON_SPEC;

    public static final LCConfig.Common COMMON;

    public static String COMMON_PATH;

    public static void init() {
        register(Type.CLIENT, CLIENT_SPEC);
        COMMON_PATH = register(Type.COMMON, COMMON_SPEC);
    }

    private static String register(Type type, IConfigSpec<?> spec) {
        ModContainer mod = ModLoadingContext.get().getActiveContainer();
        String path = "l2_configs/" + mod.getModId() + "-" + type.extension() + ".toml";
        ModLoadingContext.get().registerConfig(type, spec, path);
        return path;
    }

    static {
        Pair<LCConfig.Client, ForgeConfigSpec> client = new ForgeConfigSpec.Builder().configure(LCConfig.Client::new);
        CLIENT_SPEC = (ForgeConfigSpec) client.getRight();
        CLIENT = (LCConfig.Client) client.getLeft();
        Pair<LCConfig.Common, ForgeConfigSpec> common = new ForgeConfigSpec.Builder().configure(LCConfig.Common::new);
        COMMON_SPEC = (ForgeConfigSpec) common.getRight();
        COMMON = (LCConfig.Common) common.getLeft();
    }

    public static class Client {

        public final ForgeConfigSpec.BooleanValue renderEnchOverlay;

        public final ForgeConfigSpec.IntValue enchOverlayZVal;

        Client(ForgeConfigSpec.Builder builder) {
            this.renderEnchOverlay = builder.comment("Render enchantment character overlay").define("renderEnchOverlay", true);
            this.enchOverlayZVal = builder.comment("The height of enchantment character overlay").defineInRange("enchOverlayZVal", 250, -1000000, 1000000);
        }
    }

    public static class Common {

        public final ForgeConfigSpec.DoubleValue windSpeed;

        public final ForgeConfigSpec.IntValue belowVoid;

        public final ForgeConfigSpec.IntValue phantomHeight;

        public final ForgeConfigSpec.IntValue explosionDamage;

        public final ForgeConfigSpec.IntValue spaceDamage;

        public final ForgeConfigSpec.BooleanValue allowModBanSpaceShard;

        public final ForgeConfigSpec.BooleanValue enableImmunityEnchantments;

        public final ForgeConfigSpec.IntValue cleansePredicate;

        public final ForgeConfigSpec.IntValue totemicHealDuration;

        public final ForgeConfigSpec.IntValue totemicHealAmount;

        public final ForgeConfigSpec.DoubleValue windSweepIncrement;

        public final ForgeConfigSpec.IntValue soulFireChargeDuration;

        public final ForgeConfigSpec.IntValue blackFireChargeDuration;

        public final ForgeConfigSpec.IntValue strongFireChargePower;

        public final ForgeConfigSpec.BooleanValue strongFireChargeBreakBlock;

        public final ForgeConfigSpec.DoubleValue emeraldDamageFactor;

        public final ForgeConfigSpec.IntValue emeraldBaseRange;

        public final ForgeConfigSpec.IntValue sonicShooterDamage;

        public final ForgeConfigSpec.IntValue hellfireWandDamage;

        public final ForgeConfigSpec.IntValue iceEnchantDuration;

        public final ForgeConfigSpec.IntValue flameEnchantDuration;

        public final ForgeConfigSpec.IntValue bleedEnchantDuration;

        public final ForgeConfigSpec.IntValue curseEnchantDuration;

        public final ForgeConfigSpec.IntValue bleedEnchantMax;

        public final ForgeConfigSpec.DoubleValue voidTouchChance;

        public final ForgeConfigSpec.DoubleValue voidTouchChanceBonus;

        public final ForgeConfigSpec.DoubleValue lifeSyncFactor;

        public final ForgeConfigSpec.DoubleValue mobTypeBonus;

        public final ForgeConfigSpec.IntValue treeChopMaxRadius;

        public final ForgeConfigSpec.IntValue treeChopMaxHeight;

        public final ForgeConfigSpec.IntValue treeChopMaxBlock;

        public final ForgeConfigSpec.IntValue chainDiggingDelayThreshold;

        public final ForgeConfigSpec.IntValue chainDiggingBlockPerTick;

        public final ForgeConfigSpec.DoubleValue chainDiggingHardnessRange;

        public final ForgeConfigSpec.BooleanValue delayDiggingRequireEnder;

        public final ForgeConfigSpec.BooleanValue useArsNouveauForEnchantmentRecipe;

        public final ForgeConfigSpec.BooleanValue enableVanillaItemRecipe;

        public final ForgeConfigSpec.BooleanValue enableToolRecraftRecipe;

        public final ForgeConfigSpec.BooleanValue enableSpawnEggRecipe;

        public final ForgeConfigSpec.BooleanValue enableWandEnchantments;

        public final ForgeConfigSpec.BooleanValue useTagsForWandEnchantmentWhiteList;

        Common(ForgeConfigSpec.Builder builder) {
            this.useArsNouveauForEnchantmentRecipe = builder.comment("When Ars Nouveau is present, use apparatus recipe for enchantments").define("useArsNouveauForEnchantmentRecipe", true);
            this.enableVanillaItemRecipe = builder.comment("Allow vanilla items such as elytra and ancient debris to be crafted with L2Complements materials").define("enableVanillaItemRecipe", true);
            this.enableToolRecraftRecipe = builder.comment("Allow tools to be upgraded from tools with same typ but different materials").define("enableToolRecraftRecipe", true);
            this.enableSpawnEggRecipe = builder.comment("Allow spawn eggs to be crafted with L2Complements materials").define("enableSpawnEggRecipe", true);
            builder.push("materials");
            this.windSpeed = builder.comment("Requirement for obtaining Captured Wind. Unit: Block per Tick").defineInRange("windSpeed", 10.0, 0.1, 100.0);
            this.belowVoid = builder.comment("Requirement for void eye drop").defineInRange("belowVoid", 16, 0, 128);
            this.phantomHeight = builder.comment("Requirement for sun membrane drop").defineInRange("phantomHeight", 200, 0, 10000);
            this.explosionDamage = builder.comment("Requirement for explosion shard drop").defineInRange("explosionDamage", 80, 1, 10000);
            this.spaceDamage = builder.comment("Requirement for space shard drop").defineInRange("spaceDamage", 16384, 1, 1000000);
            this.allowModBanSpaceShard = builder.comment("Allow mods to ban space shard").define("allowModBanSpaceShard", true);
            this.enableImmunityEnchantments = builder.comment("Enable immunity enchantments").comment("Be sure to inform players when you turn this off").define("enableImmunityEnchantments", true);
            builder.pop();
            builder.push("fire charge");
            this.soulFireChargeDuration = builder.comment("Soul Fire Charge Duration").defineInRange("soulFireChargeDuration", 60, 1, 10000);
            this.blackFireChargeDuration = builder.comment("Black Fire Charge Duration").defineInRange("blackFireChargeDuration", 100, 1, 10000);
            this.strongFireChargePower = builder.comment("Strong Fire Charge Power").defineInRange("strongFireChargePower", 2, 1, 10);
            this.strongFireChargeBreakBlock = builder.comment("Strong Fire Charge Breaks Block").define("strongFireChargeBreakBlock", true);
            builder.pop();
            builder.push("properties");
            this.cleansePredicate = builder.comment("Cleanse effect clearing test").comment("0 for clearing everything").comment("1 for clearing neutral and negative only").comment("2 for clearing negative only").defineInRange("cleansePredicate", 0, 0, 2);
            this.totemicHealDuration = builder.comment("Totemic Armor healing interval").defineInRange("totemicHealDuration", 100, 1, 1000);
            this.totemicHealAmount = builder.comment("Totemic Armor healing amount").defineInRange("totemicHealAmount", 1, 1, 1000);
            this.windSweepIncrement = builder.comment("Wind Sweep enchantment increment to sweep hit box").defineInRange("windSweepIncrement", 1.0, 0.1, 100.0);
            this.emeraldDamageFactor = builder.comment("Damage factor of emerald splash").defineInRange("emeraldDamageFactor", 0.5, 0.001, 1000.0);
            this.emeraldBaseRange = builder.comment("Base range for emerald splash").defineInRange("emeraldBaseRange", 10, 1, 100);
            this.sonicShooterDamage = builder.comment("Sonic Shooter Damage").defineInRange("sonicShooterDamage", 10, 1, 1000);
            this.hellfireWandDamage = builder.comment("Hellfire Wand Damage").defineInRange("hellfireWandDamage", 10, 1, 1000);
            this.iceEnchantDuration = builder.comment("Base duration for iceBlade").defineInRange("iceEnchantDuration", 100, 1, 10000);
            this.flameEnchantDuration = builder.comment("Duration for flameBlade").defineInRange("flameEnchantDuration", 60, 1, 10000);
            this.bleedEnchantDuration = builder.comment("Base duration for sharpBlade").defineInRange("bleedEnchantDuration", 80, 1, 10000);
            this.curseEnchantDuration = builder.comment("Base duration for cursedBlade").defineInRange("curseEnchantDuration", 100, 1, 10000);
            this.bleedEnchantMax = builder.comment("Max effect level for sharpBlade").defineInRange("bleedEnchantMax", 3, 1, 10000);
            this.voidTouchChance = builder.comment("Void Touch chance for true damage").defineInRange("voidTouchChance", 0.05, 0.0, 1.0);
            this.voidTouchChanceBonus = builder.comment("Void Touch chance for true damage if bypass armor or magic").defineInRange("voidTouchChanceBonus", 0.5, 0.0, 1.0);
            this.mobTypeBonus = builder.comment("Bonus damage factor for specific materials against specific mob types").defineInRange("mobTypeBonus", 1.0, 0.0, 1000.0);
            this.lifeSyncFactor = builder.comment("Damage factor for lifeSync (damage to user per durability cost)").defineInRange("lifeSyncFactor", 1.0, 0.0, 1000.0);
            this.treeChopMaxRadius = builder.comment("Max radius for blocks to be considered for tree chopping, except upward direction").defineInRange("treeChopMaxRadius", 16, 0, 32);
            this.treeChopMaxHeight = builder.comment("Max height for blocks to be considered for tree chopping.").defineInRange("treeChopMaxHeight", 256, 0, 512);
            this.treeChopMaxBlock = builder.comment("Max number of blocks to be considered for tree chopping.").defineInRange("treeChopMaxBlock", 1024, 0, 16384);
            this.chainDiggingDelayThreshold = builder.comment("Max number of blocks to break before resort to delayed breaking").defineInRange("chainDiggingDelayThreshold", 64, 1, 1024);
            this.chainDiggingBlockPerTick = builder.comment("Max number of blocks to break per tick in delayed breaking").defineInRange("chainDiggingBlockPerTick", 16, 1, 1024);
            this.chainDiggingHardnessRange = builder.comment("Max hardness blocks to break may have, as a factor of the hardness of the block broken.").comment("Apotheosis implementation of chain digging use 3 has their hardness factor.").defineInRange("chainDiggingHardnessRange", 3.0, 1.0, 100.0);
            this.delayDiggingRequireEnder = builder.comment("Delayed breaking requires Ender Transport to take effect to reduce lag").define("delayDiggingRequireEnder", true);
            builder.pop();
            builder.push("enchantability");
            this.enableWandEnchantments = builder.comment("Allow enchantments on wands").define("enableWandEnchantments", true);
            this.useTagsForWandEnchantmentWhiteList = builder.comment("Use tag for wand enchantment whitelisting").define("useTagsForWandEnchantmentWhiteList", true);
            builder.pop();
        }
    }
}