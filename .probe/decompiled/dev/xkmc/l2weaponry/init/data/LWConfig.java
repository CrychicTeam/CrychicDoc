package dev.xkmc.l2weaponry.init.data;

import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;
import org.apache.commons.lang3.tuple.Pair;

public class LWConfig {

    public static final ForgeConfigSpec CLIENT_SPEC;

    public static final LWConfig.Client CLIENT;

    public static final ForgeConfigSpec COMMON_SPEC;

    public static final LWConfig.Common COMMON;

    public static void init() {
        register(Type.CLIENT, CLIENT_SPEC);
        register(Type.COMMON, COMMON_SPEC);
    }

    private static void register(Type type, IConfigSpec<?> spec) {
        ModContainer mod = ModLoadingContext.get().getActiveContainer();
        String path = "l2_configs/" + mod.getModId() + "-" + type.extension() + ".toml";
        ModLoadingContext.get().registerConfig(type, spec, path);
    }

    static {
        Pair<LWConfig.Client, ForgeConfigSpec> client = new ForgeConfigSpec.Builder().configure(LWConfig.Client::new);
        CLIENT_SPEC = (ForgeConfigSpec) client.getRight();
        CLIENT = (LWConfig.Client) client.getLeft();
        Pair<LWConfig.Common, ForgeConfigSpec> common = new ForgeConfigSpec.Builder().configure(LWConfig.Common::new);
        COMMON_SPEC = (ForgeConfigSpec) common.getRight();
        COMMON = (LWConfig.Common) common.getLeft();
    }

    public static class Client {

        Client(ForgeConfigSpec.Builder builder) {
        }
    }

    public static class Common {

        public final ForgeConfigSpec.DoubleValue dagger_bonus;

        public final ForgeConfigSpec.DoubleValue claw_bonus;

        public final ForgeConfigSpec.IntValue claw_max;

        public final ForgeConfigSpec.IntValue claw_timeout;

        public final ForgeConfigSpec.IntValue claw_block_time;

        public final ForgeConfigSpec.DoubleValue reflectCost;

        public final ForgeConfigSpec.BooleanValue diggerEnchantmentOnWeapon;

        public final ForgeConfigSpec.ConfigValue<List<String>> extraCompatibleEnchantmentCategories;

        public final ForgeConfigSpec.IntValue shadowHunterDistance;

        public final ForgeConfigSpec.IntValue hauntingDemonDistance;

        public final ForgeConfigSpec.IntValue hammerOfIncarcerationRadius;

        public final ForgeConfigSpec.IntValue hammerOfIncarcerationDuration;

        public final ForgeConfigSpec.DoubleValue dogmaticStandoffGain;

        public final ForgeConfigSpec.DoubleValue dogmaticStandoffMax;

        public final ForgeConfigSpec.DoubleValue determinationRate;

        public final ForgeConfigSpec.DoubleValue illusionRate;

        public final ForgeConfigSpec.DoubleValue heavySpeedReduction;

        public final ForgeConfigSpec.DoubleValue heavyCritBonus;

        public final ForgeConfigSpec.DoubleValue stealthChance;

        public final ForgeConfigSpec.DoubleValue stealthDamageReduction;

        public final ForgeConfigSpec.DoubleValue heavyShieldSpeedReduction;

        public final ForgeConfigSpec.DoubleValue heavyShieldDefenseBonus;

        public final ForgeConfigSpec.DoubleValue hardShieldDefenseBonus;

        public final ForgeConfigSpec.DoubleValue raisedSpiritSpeedBonus;

        public final ForgeConfigSpec.DoubleValue energizedWillReachBonus;

        public final ForgeConfigSpec.IntValue instantThrowCooldown;

        public final ForgeConfigSpec.DoubleValue knightmetalBonus;

        public final ForgeConfigSpec.DoubleValue knightmetalReflect;

        public final ForgeConfigSpec.DoubleValue fieryBonus;

        public final ForgeConfigSpec.IntValue fieryDuration;

        public final ForgeConfigSpec.DoubleValue steeleafBonus;

        public final ForgeConfigSpec.DoubleValue steeleafReflect;

        public final ForgeConfigSpec.DoubleValue steeleafChance;

        public final ForgeConfigSpec.IntValue ironwoodRegenDuration;

        public final ForgeConfigSpec.IntValue ironwoodEffectDuration;

        Common(ForgeConfigSpec.Builder builder) {
            builder.push("Weapon Type Features");
            this.dagger_bonus = builder.comment("Dagger damage multiplier when hitting targets not targeting user").defineInRange("dagger_bonus", 2.0, 1.0, 1000.0);
            this.claw_bonus = builder.comment("Claw damage bonus for each consecutive hit").defineInRange("claw_bonus", 0.1, 0.0, 10.0);
            this.claw_max = builder.comment("Claw damage bonus maximum hit for one claw (two claw has double maximum)").defineInRange("claw_max", 5, 1, 1000);
            this.claw_timeout = builder.comment("Claw damage bonus timeout").defineInRange("claw_timeout", 60, 1, 1000);
            this.claw_block_time = builder.comment("Claw block damage time").defineInRange("claw_block_time", 3, 0, 1000);
            this.reflectCost = builder.comment("Shield reflect cost").defineInRange("reflectCost", 0.2, 0.0, 1.0);
            this.diggerEnchantmentOnWeapon = builder.comment("Allow digger enchantments on weapon").define("diggerEnchantmentOnWeapon", true);
            this.extraCompatibleEnchantmentCategories = builder.comment("List of enchantment categories for weapons. Use upper case enum name.").comment("For modded enchantment categories, find them in their code through GitHub").comment("Example: 'WEAPON', 'DIGGER'").define("extraCompatibleEnchantmentCategories", new ArrayList(List.of()));
            builder.pop();
            builder.push("Legendary Weapon Effects");
            this.shadowHunterDistance = builder.comment("Shadow Hunter teleport distance").defineInRange("shadowHunterDistance", 8, 1, 128);
            this.hauntingDemonDistance = builder.comment("Haunting Demon of the End teleport distance").defineInRange("hauntingDemonDistance", 64, 1, 128);
            this.hammerOfIncarcerationRadius = builder.comment("Hammer of Incarceration effect radius").defineInRange("hammerOfIncarcerationRadius", 8, 1, 64);
            this.hammerOfIncarcerationDuration = builder.comment("Hammer of Incarceration effect duration").defineInRange("hammerOfIncarcerationDuration", 60, 1, 60000);
            this.dogmaticStandoffGain = builder.comment("Dogmatic Standoff absorption gain percentage").defineInRange("dogmaticStandoffGain", 0.02, 1.0E-4, 1.0);
            this.dogmaticStandoffMax = builder.comment("Dogmatic Standoff absorption max percentage").defineInRange("dogmaticStandoffMax", 0.1, 1.0E-4, 100.0);
            this.determinationRate = builder.comment("Claw of Determination increase rate").defineInRange("determinationRate", 2.0, 0.0, 100.0);
            this.illusionRate = builder.comment("Blade of illusion increase rate").defineInRange("illusionRate", 1.0, 0.0, 100.0);
            builder.pop();
            builder.push("Enchantments");
            this.heavySpeedReduction = builder.comment("Heavy enchantment reduction on attack speed").defineInRange("heavySpeedReduction", 0.2, 1.0E-4, 100.0);
            this.heavyCritBonus = builder.comment("Heavy enchantment crit damage bonus").defineInRange("heavyCritBonus", 0.3, 1.0E-4, 100.0);
            this.stealthChance = builder.comment("Stealth enchantment no aggro chance").defineInRange("stealthChance", 0.2, 1.0E-4, 100.0);
            this.stealthDamageReduction = builder.comment("Stealth enchantment damage reduction").defineInRange("stealthDamageReduction", 0.1, 1.0E-4, 100.0);
            this.heavyShieldSpeedReduction = builder.comment("HeavyShield enchantment reduction on attack speed").defineInRange("heavyShieldSpeedReduction", 0.1, 1.0E-4, 100.0);
            this.heavyShieldDefenseBonus = builder.comment("HeavyShield enchantment defense bonus").defineInRange("heavyShieldDefenseBonus", 0.1, 1.0E-4, 100.0);
            this.hardShieldDefenseBonus = builder.comment("HardShield enchantment defense bonus").defineInRange("hardShieldDefenseBonus", 0.05, 1.0E-4, 100.0);
            this.raisedSpiritSpeedBonus = builder.comment("Raised Spirit enchantment bonus on attack speed per stacking level per enchantment level").defineInRange("raisedSpiritSpeedBonus", 0.01, 1.0E-4, 100.0);
            this.energizedWillReachBonus = builder.comment("Energized Will enchantment bonus on attack range per stacking level per enchantment level").defineInRange("energizedWillReachBonus", 0.02, 1.0E-4, 100.0);
            this.instantThrowCooldown = builder.comment("Cooldown for Instant Throwing").defineInRange("instantThrowCooldown", 60, 1, 6000);
            builder.pop();
            builder.push("Twilight Forest Compat");
            this.knightmetalBonus = builder.comment("Damage bonus to enemies with armor").defineInRange("knightmetalBonus", 0.2, 0.0, 1000.0);
            this.knightmetalReflect = builder.comment("Extra damage reflection").defineInRange("knightmetalReflect", 0.5, 0.0, 1000.0);
            this.fieryBonus = builder.comment("Damage bonus to enemies not immune to fire").defineInRange("knightmetalBonus", 0.5, 0.0, 1000.0);
            this.fieryDuration = builder.comment("Ignite enemy by seconds").defineInRange("fieryDuration", 15, 0, 1000);
            this.steeleafBonus = builder.comment("Damage bonus to enemies without armor").defineInRange("steeleafBonus", 0.5, 0.0, 1000.0);
            this.steeleafReflect = builder.comment("Extra damage reflection").defineInRange("steeleafReflect", 0.5, 0.0, 1000.0);
            this.steeleafChance = builder.comment("Effect Application Chance").defineInRange("steeleafChance", 0.5, 0.0, 1000.0);
            this.ironwoodRegenDuration = builder.comment("Regeneration interval (in ticks)").defineInRange("ironwoodRegenDuration", 100, 1, 10000);
            this.ironwoodEffectDuration = builder.comment("Resistance duration (in ticks)").defineInRange("ironwoodEffectDuration", 100, 1, 10000);
            builder.pop();
        }
    }
}