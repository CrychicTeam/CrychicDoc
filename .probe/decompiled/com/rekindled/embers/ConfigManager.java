package com.rekindled.embers;

import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;

public class ConfigManager {

    public static ForgeConfigSpec.ConfigValue<Integer> MAX_PROXY_DISTANCE;

    public static ForgeConfigSpec.ConfigValue<Double> EMBER_BORE_SPEED_MOD;

    public static ForgeConfigSpec.ConfigValue<Integer> EMBER_BORE_TIME;

    public static ForgeConfigSpec.ConfigValue<Double> EMBER_BORE_FUEL_CONSUMPTION;

    public static ForgeConfigSpec.ConfigValue<Integer> RESERVOIR_CAPACITY;

    public static ForgeConfigSpec.ConfigValue<Integer> MINI_BOILER_CAPACITY;

    public static ForgeConfigSpec.ConfigValue<Double> MINI_BOILER_HEAT_MULTIPLIER;

    public static ForgeConfigSpec.ConfigValue<Boolean> MINI_BOILER_CAN_EXPLODE;

    public static ForgeConfigSpec.ConfigValue<Integer> INJECTOR_MAX_DISTANCE;

    public static ForgeConfigSpec.ConfigValue<Integer> MELTER_PROCESS_TIME;

    public static ForgeConfigSpec.ConfigValue<Double> MELTER_EMBER_COST;

    public static ForgeConfigSpec.ConfigValue<Integer> MELTER_CAPACITY;

    public static ForgeConfigSpec.ConfigValue<Integer> GEO_SEPARATOR_CAPACITY;

    public static ForgeConfigSpec.ConfigValue<Integer> STAMP_BASE_CAPACITY;

    public static ForgeConfigSpec.ConfigValue<Double> CHARGER_MAX_TRANSFER;

    public static ForgeConfigSpec.ConfigValue<Integer> FLUID_VESSEL_CAPACITY;

    public static ForgeConfigSpec.ConfigValue<Double> HEARTH_COIL_EMBER_COST;

    public static ForgeConfigSpec.ConfigValue<Double> HEARTH_COIL_HEATING_SPEED;

    public static ForgeConfigSpec.ConfigValue<Double> HEARTH_COIL_COOLING_SPEED;

    public static ForgeConfigSpec.ConfigValue<Double> HEARTH_COIL_MAX_HEAT;

    public static ForgeConfigSpec.ConfigValue<Integer> HEARTH_COIL_MIN_COOK_TIME;

    public static ForgeConfigSpec.ConfigValue<Integer> HEARTH_COIL_MAX_COOK_TIME;

    public static ForgeConfigSpec.ConfigValue<Integer> DAWNSTONE_ANVIL_MAX_HITS;

    public static ForgeConfigSpec.ConfigValue<Double> BLAZING_RAY_COST;

    public static ForgeConfigSpec.ConfigValue<Integer> BLAZING_RAY_COOLDOWN;

    public static ForgeConfigSpec.ConfigValue<Integer> BLAZING_RAY_MAX_CHARGE;

    public static ForgeConfigSpec.ConfigValue<Double> BLAZING_RAY_DAMAGE;

    public static ForgeConfigSpec.ConfigValue<Double> BLAZING_RAY_MAX_SPREAD;

    public static ForgeConfigSpec.ConfigValue<Double> BLAZING_RAY_MAX_DISTANCE;

    public static ForgeConfigSpec.ConfigValue<Double> CINDER_STAFF_COST;

    public static ForgeConfigSpec.ConfigValue<Integer> CINDER_STAFF_COOLDOWN;

    public static ForgeConfigSpec.ConfigValue<Integer> CINDER_STAFF_MAX_CHARGE;

    public static ForgeConfigSpec.ConfigValue<Double> CINDER_STAFF_DAMAGE;

    public static ForgeConfigSpec.ConfigValue<Double> CINDER_STAFF_SIZE;

    public static ForgeConfigSpec.ConfigValue<Double> CINDER_STAFF_AOE_SIZE;

    public static ForgeConfigSpec.ConfigValue<Integer> CINDER_STAFF_LIFETIME;

    public static ForgeConfigSpec.ConfigValue<Integer> ASHEN_GOGGLES_SLOTS;

    public static ForgeConfigSpec.ConfigValue<Integer> ASHEN_CLOAK_SLOTS;

    public static ForgeConfigSpec.ConfigValue<Integer> ASHEN_LEGGINGS_SLOTS;

    public static ForgeConfigSpec.ConfigValue<Integer> ASHEN_BOOTS_SLOTS;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> SCALE_DAMAGE_PASSES;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> SCALE_DAMAGE_RATES;

    public static ForgeConfigSpec.ConfigValue<Boolean> CODEX_PROGRESSION;

    public static ForgeConfigSpec.ConfigValue<Boolean> PVP_EVERYBODY_IS_ENEMY;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> TAG_PREFERENCES;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_PREFERENCES;

    public static ForgeConfigSpec.ConfigValue<Boolean> RENDER_FALLBACK;

    public static double getScaleDamagePass(String type) {
        for (String pass : SCALE_DAMAGE_PASSES.get()) {
            String[] values = pass.split(":");
            if (type.equals(values[0])) {
                return Double.parseDouble(values[1]);
            }
        }
        return 0.0;
    }

    public static double getScaleDamageRate(String type) {
        for (String rate : SCALE_DAMAGE_RATES.get()) {
            String[] values = rate.split(":");
            if (type.equals(values[0])) {
                return Double.parseDouble(values[1]);
            }
        }
        return 1.0;
    }

    public static void register() {
        registerClientConfigs();
        registerCommonConfigs();
    }

    public static void registerClientConfigs() {
        ForgeConfigSpec.Builder CLIENT = new ForgeConfigSpec.Builder();
        RENDER_FALLBACK = CLIENT.comment("Use a fallback rendertype for certain objects that normally use custom core shaders. Improves compatibility with shader mods but might not look as good.").define("render_fallback", false);
        ModLoadingContext.get().registerConfig(Type.CLIENT, CLIENT.build());
    }

    public static void registerCommonConfigs() {
        ForgeConfigSpec.Builder COMMON = new ForgeConfigSpec.Builder();
        COMMON.comment("Settings for machine/item/misc parameters").push("parameters");
        MAX_PROXY_DISTANCE = COMMON.comment("The maximum distance that mechanical cores can proxy capabilities and upgrades.").define("mechanical_core.max_distance", 3);
        EMBER_BORE_SPEED_MOD = COMMON.comment("The speed modifier of the Ember Bore before upgrades.").define("emberBore.speedMod", 1.0);
        EMBER_BORE_TIME = COMMON.comment("The time in ticks it takes to try one dig attempt.").define("emberBore.processTime", 200);
        EMBER_BORE_FUEL_CONSUMPTION = COMMON.comment("The amount of fuel consumed each tick.").define("emberBore.fuelCost", 3.0);
        RESERVOIR_CAPACITY = COMMON.comment("How much fluid (in mb) fits into each Caminite Ring on a Reservoir.").define("reservoir.capacity", 40000);
        MINI_BOILER_CAPACITY = COMMON.comment("How much fluid and gas (in mb) fits into a Mini Boiler.").define("mini_boiler.capacity", 16000);
        MINI_BOILER_HEAT_MULTIPLIER = COMMON.comment("How much fluid (in mb) a Mini Boiler boils for each ember used/generated.").define("mini_boiler.heat_multiplier", 1.0);
        MINI_BOILER_CAN_EXPLODE = COMMON.comment("Whether the Mini Boiler can explode when overfilled with gas").define("mini_boiler.can_explode", true);
        MELTER_PROCESS_TIME = COMMON.comment("The time in ticks it takes to process one recipe.").define("melter.processTime", 200);
        MELTER_EMBER_COST = COMMON.comment("The ember cost per tick.").define("melter.cost", 1.0);
        MELTER_CAPACITY = COMMON.comment("How much fluid (in mb) fits into a Melter.").define("melter.capacity", 4000);
        GEO_SEPARATOR_CAPACITY = COMMON.comment("How much fluid (in mb) fits into a Geologic Seperator.").define("geoSeparator.capacity", 1000);
        STAMP_BASE_CAPACITY = COMMON.comment("How much fluid (in mb) fits into the Stamp Base.").define("stamper.capacity", 1500);
        CHARGER_MAX_TRANSFER = COMMON.comment("How much ember is transferred between item and charger per tick.").define("charger.transfer", 10.0);
        FLUID_VESSEL_CAPACITY = COMMON.comment("How much fluid (in mb) fits into the Fluid Vessel.").define("fluidVessel.capacity", 16000);
        INJECTOR_MAX_DISTANCE = COMMON.comment("The maximum distance that Ember Injectors can be placed from a crystal seed.").define("ember_injector.max_distance", 1);
        HEARTH_COIL_EMBER_COST = COMMON.comment("The amount of ember consumed per tick.").define("hearth_coil.ember_cost", 1.0);
        HEARTH_COIL_HEATING_SPEED = COMMON.comment("The amount of heat gained per tick when consuming ember.").define("hearth_coil.heating_speed", 1.0);
        HEARTH_COIL_COOLING_SPEED = COMMON.comment("The amount of heat lost per tick when not consuming ember.").define("hearth_coil.cooling_speed", 1.0);
        HEARTH_COIL_MAX_HEAT = COMMON.comment("The maximum heat value the hearth coil can reach without upgrades.").define("hearth_coil.max_heat", 280.0);
        HEARTH_COIL_MIN_COOK_TIME = COMMON.comment("The time in ticks it takes to cook 1 item at the highest heat.").define("hearth_coil.min_cook_time", 20);
        HEARTH_COIL_MAX_COOK_TIME = COMMON.comment("The time in ticks it takes to cook 1 item at the lowest heat.").define("hearth_coil.max_cook_time", 300);
        DAWNSTONE_ANVIL_MAX_HITS = COMMON.comment("The amount of hits required to perform one recipe on a dawnstone anvil.").define("dawnstone_anvil.max_hits", 40);
        BLAZING_RAY_COST = COMMON.comment("Ember used up by each shot.").define("blazingRay.cost", 25.0);
        BLAZING_RAY_COOLDOWN = COMMON.comment("Cooldown in ticks between each shot.").define("blazingRay.cooldown", 10);
        BLAZING_RAY_MAX_CHARGE = COMMON.comment("Time in ticks to fully charge.").define("blazingRay.charge", 20);
        BLAZING_RAY_DAMAGE = COMMON.comment("Damage dealt by one shot.").define("blazingRay.damage", 7.0);
        BLAZING_RAY_MAX_SPREAD = COMMON.comment("Maximum spread.").define("blazingRay.spread", 30.0);
        BLAZING_RAY_MAX_DISTANCE = COMMON.comment("Maximum shot distance.").define("blazingRay.distance", 96.0);
        CINDER_STAFF_COST = COMMON.comment("Ember used up by each shot.").define("cinderStaff.cost", 25.0);
        CINDER_STAFF_COOLDOWN = COMMON.comment("Cooldown in ticks between each shot.").define("cinderStaff.cooldown", 10);
        CINDER_STAFF_MAX_CHARGE = COMMON.comment("Time in ticks to fully charge.").define("cinderStaff.charge", 60);
        CINDER_STAFF_DAMAGE = COMMON.comment("Damage dealt by one shot.").define("cinderStaff.damage", 17.0);
        CINDER_STAFF_SIZE = COMMON.comment("Size of the projectile.").define("cinderStaff.size", 17.0);
        CINDER_STAFF_AOE_SIZE = COMMON.comment("Area of Effect on impact.").define("cinderStaff.aoe", 2.125);
        CINDER_STAFF_LIFETIME = COMMON.comment("Maximum lifetime in ticks of projectile.").define("cinderStaff.lifetime", 160);
        ASHEN_GOGGLES_SLOTS = COMMON.comment("How many inflictor gems can fit in the ashen goggles.").define("ashen.goggles.gem_slots", 2);
        ASHEN_CLOAK_SLOTS = COMMON.comment("How many inflictor gems can fit in the ashen cloak.").define("ashen.cloak.gem_slots", 7);
        ASHEN_LEGGINGS_SLOTS = COMMON.comment("How many inflictor gems can fit in the ashen leggings.").define("ashen.leggings.gem_slots", 5);
        ASHEN_BOOTS_SLOTS = COMMON.comment("How many inflictor gems can fit in the ashen boots.").define("ashen.boots.gem_slots", 3);
        List<String> defaultScaleDamagePasses = new ArrayList();
        defaultScaleDamagePasses.add("drown:1.0");
        defaultScaleDamagePasses.add("starve:1.0");
        List<String> defaultScaleDamageRates = new ArrayList();
        SCALE_DAMAGE_PASSES = COMMON.comment("Syntax is 'damagetype:rate'. Determines which damage types are partially unaffected by the shifting scales augment.").defineList("shiftingScales.damagePasses", defaultScaleDamagePasses, a -> true);
        SCALE_DAMAGE_RATES = COMMON.comment("Syntax is 'damagetype:rate'. Specifies a separate damage rate for depleting the scales.").defineList("shiftingScales.damageRates", defaultScaleDamageRates, a -> true);
        COMMON.pop();
        COMMON.comment("Miscellaneous settings").push("misc");
        CODEX_PROGRESSION = COMMON.comment("Codex entries need to be completed before the next one unlocks.").define("codexProgression", true);
        PVP_EVERYBODY_IS_ENEMY = COMMON.comment("If true, Embers homing projectiles will go for neutral players.").define("everybodyIsEnemy", false);
        List<String> preferences = new ArrayList();
        preferences.add(0, "minecraft");
        preferences.add(1, "embers");
        TAG_PREFERENCES = COMMON.comment("Which domains are preferred for recipes with dynamic outputs.").defineList("tagPreferences", preferences, a -> true);
        List<String> itemPreferences = new ArrayList();
        itemPreferences.add(0, "minecraft:oak_planks");
        itemPreferences.add(1, "minecraft:cobblestone");
        ITEM_PREFERENCES = COMMON.comment("Which items are preferred as the result of breaking down a tool on an anvil.").defineList("itemPreferences", itemPreferences, a -> true);
        COMMON.pop();
        ModLoadingContext.get().registerConfig(Type.COMMON, COMMON.build());
    }

    public static void registerServerConfigs() {
        ForgeConfigSpec.Builder SERVER = new ForgeConfigSpec.Builder();
        ModLoadingContext.get().registerConfig(Type.SERVER, SERVER.build());
    }
}