package com.mna.config;

import java.util.Arrays;
import java.util.List;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "mna", bus = Bus.FORGE)
public class GeneralConfigProvider {

    static ForgeConfigSpec.BooleanValue MODIFY_VILLAGER_TRADES;

    static ForgeConfigSpec.BooleanValue DISABLE_VILLAGER_REGISTRATION;

    static ForgeConfigSpec.BooleanValue CLASSIC_RAIDS;

    static ForgeConfigSpec.IntValue FORCED_RAID_STRENGTH;

    static ForgeConfigSpec.DoubleValue RAID_CHANCE_BASE;

    static ForgeConfigSpec.DoubleValue RAID_CHANCE_TIER;

    static ForgeConfigSpec.IntValue RAID_SPAWN_ATTEMPTS;

    static ForgeConfigSpec.BooleanValue FACTION_MOB_KILL_IRE;

    static ForgeConfigSpec.IntValue WELLSPRING_DISTANCE;

    static ForgeConfigSpec.BooleanValue GENERIC_WELLSPRINGS;

    static ForgeConfigSpec.BooleanValue GRADUAL_TIME_CHANGE;

    static ForgeConfigSpec.DoubleValue FOSSILIZE_DAMAGE;

    static ForgeConfigSpec.IntValue PHYLACTERY_KILLS;

    static ForgeConfigSpec.BooleanValue SUMMON_BOSSES;

    static ForgeConfigSpec.BooleanValue SUMMON_INTERACTIONS;

    static ForgeConfigSpec.BooleanValue PHYLACTERY_BOSSES;

    static ForgeConfigSpec.DoubleValue DAMAGE_SCALE;

    static ForgeConfigSpec.IntValue SIGIL_LIMIT;

    static ForgeConfigSpec.IntValue ROTE_LOW_TIER_AUTO;

    static ForgeConfigSpec.IntValue TIER_5_COMPLEXITY_LIMIT;

    static ForgeConfigSpec.BooleanValue CONFUSE_AFFECTS_PLAYERS;

    static ForgeConfigSpec.BooleanValue SPLASH_CREATES_SOURCES;

    static ForgeConfigSpec.ConfigValue<List<? extends String>> SUMMON_BLACKLIST;

    static ForgeConfigSpec.ConfigValue<List<? extends String>> BREAK_MAGNITUDE_MAPPING;

    static ForgeConfigSpec.ConfigValue<List<? extends String>> INSIGHT_DAMAGE_COLORS;

    static ForgeConfigSpec.ConfigValue<List<? extends String>> DISJUNCTION_BLACKLIST;

    static ForgeConfigSpec.BooleanValue METEOR_JUMP;

    static ForgeConfigSpec.IntValue SPELLWEAVER_REFLECTCHARGES;

    static ForgeConfigSpec.IntValue SPELLWEAVER_RECHARGETIME;

    static ForgeConfigSpec.DoubleValue DRUIDIC_REFLECTCHANCE;

    static ForgeConfigSpec.DoubleValue DRUIDIC_TELEPORTCHANCE;

    static ForgeConfigSpec.IntValue MANA_TICKS_FOR_REGEN;

    static ForgeConfigSpec.IntValue SOULS_PLAYER;

    static ForgeConfigSpec.IntValue SOULS_VILLAGER;

    static ForgeConfigSpec.IntValue SOULS_FACTION;

    static ForgeConfigSpec.IntValue SOULS_MOB;

    static ForgeConfigSpec.IntValue SOULS_ANIMAL;

    static ForgeConfigSpec.IntValue SOULS_UNDEAD;

    static ForgeConfigSpec.IntValue BRIMSTONE_PER_HEART;

    static ForgeConfigSpec.ConfigValue<List<? extends Integer>> TIER_HEALTH_BOOSTS;

    static ForgeConfigSpec.DoubleValue TIER_PCT;

    static ForgeConfigSpec.DoubleValue MELEE_DIST;

    static ForgeConfigSpec.BooleanValue LITE_MODE;

    static ForgeConfigSpec.BooleanValue FIX_JUMP_BOOST;

    static ForgeConfigSpec.BooleanValue PROJECTION_ENCHANT_ANYTHING;

    static ForgeConfigSpec.IntValue AVERAGE_MANAWEAVE_COST;

    static ForgeConfigSpec.IntValue EXPERIENCE_PER_RUNESCRIBE_UNDO;

    static ForgeConfigSpec.IntValue RITUAL_PERMISSION_LEVEL;

    static ForgeConfigSpec.IntValue RTP_DISTANCE;

    static ForgeConfigSpec.ConfigValue<List<? extends String>> DISENCHANT_BLACKLIST;

    static ForgeConfigSpec.ConfigValue<List<? extends String>> REPAIR_BLACKLIST;

    static ForgeConfigSpec.ConfigValue<List<? extends String>> WARD_BLACKLIST;

    static ForgeConfigSpec.ConfigValue<List<? extends String>> WARD_WHITELIST;

    static ForgeConfigSpec.IntValue DEMON_BONUS_DAMAGE_MOD;

    static ForgeConfigSpec.IntValue DEMON_BONUS_RANGE_MOD;

    static ForgeConfigSpec.IntValue DEMON_BONUS_RADIUS_MOD;

    static ForgeConfigSpec.IntValue UNDEAD_COFFIN_SOULS_DELAY;

    static ForgeConfigSpec.DoubleValue UNDEAD_COFFIN_SOULS_RESTORED;

    public GeneralConfigProvider(ForgeConfigSpec.Builder builder) {
        initGeneralOptions(builder);
        initVillagerTradeConfig(builder);
        initFactionRaidConfig(builder);
        initWorldgenOptions(builder);
        initCastingResourceOptions(builder);
        initArtifactOptions(builder);
        initFactionOptions(builder);
    }

    private static void initVillagerTradeConfig(ForgeConfigSpec.Builder serverBuilder) {
        serverBuilder.comment("Mana and Artifice // Villager Modification").push("ma_villager_modification");
        MODIFY_VILLAGER_TRADES = serverBuilder.comment("Adjust librarian villagers to not have enchanted books until tier 3.  This is done for balancing and to remove the exploit of rolling librarians for cheap enchants. [true / false]").define("modifyVillagerTrades", true);
        serverBuilder.pop();
    }

    private static void initFactionOptions(ForgeConfigSpec.Builder serverBuilder) {
        serverBuilder.comment("Mana and Artifice // Faction Modification").push("ma_faction_modification");
        DEMON_BONUS_DAMAGE_MOD = serverBuilder.comment("How much extra damage should members of the demon faction be able to put on their spells?").defineInRange("demonBonusDamage", 5, 0, 1000);
        DEMON_BONUS_RANGE_MOD = serverBuilder.comment("How much extra range should members of the demon faction be able to put on their spells?").defineInRange("demonBonusRange", 4, 0, 32);
        DEMON_BONUS_RADIUS_MOD = serverBuilder.comment("How much extra radius should members of the demon faction be able to put on their spells?").defineInRange("demonBonusRadius", 1, 0, 3);
        UNDEAD_COFFIN_SOULS_RESTORED = serverBuilder.comment("What percentage of souls should sleeping in a coffin restore for undead?").defineInRange("undeadCoffinSoulsPct", 0.25, 0.0, 1.0);
        UNDEAD_COFFIN_SOULS_DELAY = serverBuilder.comment("How long should need to pass before undead will gain full souls again from sleeping in a coffin?").defineInRange("undeadCoffinSleepDelay", 18000, 0, Integer.MAX_VALUE);
        serverBuilder.pop();
    }

    private static void initFactionRaidConfig(ForgeConfigSpec.Builder serverBuilder) {
        serverBuilder.comment("Mana and Artifice // Faction Raids").comment("Raids are incremented when you use items/spells from the other factions.  When they get angry enough, you will be raided.").push("ma_faction_raids");
        CLASSIC_RAIDS = serverBuilder.comment("Should classic raiding be enabled?  Raids are based on a chance value.  The value is increased each day there is not a raid.  If the chance hits a total of 1.0 or greater, it's a guaranteed raid.  The chance resets after a raid successfully spawns.").define("classicRaids", false);
        RAID_CHANCE_BASE = serverBuilder.comment("Adjust the base amount per day that the chance to be raided goes up for each player.  This has no effect if classic raids are disabled.").defineInRange("raidBaselineIncrease", 0.05, 0.0, 1.0);
        RAID_CHANCE_TIER = serverBuilder.comment("Adjust the amount per day that the chance to be raided goes up for each player based on their tier above 3 (this value * (tier-3)).  This is added to the baseline.  This has no effect if classic raids are disabled.").defineInRange("raidTierIncrease", 0.05, 0.0, 1.0);
        RAID_SPAWN_ATTEMPTS = serverBuilder.comment("How many attempts (within a 20x20x5 box centered on the player in question) should the game make to spawn a raid?  Note this will be re-attempted every 100 ticks until a successful spawn is made.  If you're lagging due to raid spawn attempts, lower this setting.").defineInRange("raidSpawnAttempts", 100, 1, 1000);
        FACTION_MOB_KILL_IRE = serverBuilder.comment("Should killing faction mobs increase ire, and eventually result in a raid?").define("factionKillIre", true);
        FORCED_RAID_STRENGTH = serverBuilder.comment("How strong should forced raids be?  This is mostly for Moridrex's stream, you shouldn't need to mess with this.").defineInRange("forceRaidStrength", 180, 1, 10000);
        serverBuilder.pop();
    }

    private static void initGeneralOptions(ForgeConfigSpec.Builder serverBuilder) {
        serverBuilder.comment("Mana and Artifice // General Options").push("ma_general_options");
        GRADUAL_TIME_CHANGE = serverBuilder.comment("Rituals of Aurora and Eventide by default will change time gradually for a prettier effect.  However this isn't without its performance impacts, and this can be toggled off by setting this to false, making the transition instant like the /time set commands. [true / false]").define("gradualTimeChange", true);
        WARD_BLACKLIST = serverBuilder.comment("A comma separated list of entity IDs that the warding candle should block, regardless of other factors.  You can use explicit IDs, modid:* for all mobs within a given mod, or *.* for everything.").defineListAllowEmpty("wardingCandleBlacklist", Arrays.asList("*.*"), e -> true);
        WARD_WHITELIST = serverBuilder.comment("A comma separated list of entity IDs that the warding candle should allow, regardless of other factors.  Will defer to the blacklist in the event of a collision.  This list also defines what mobs are allowed to 'mob grief' within the radius of a warding candle. You can use explicit IDs, modid:* for all mobs within a given mod, or *.* for everything.").defineListAllowEmpty("wardingCandleWhitelist", Arrays.asList(), e -> true);
        TIER_PCT = serverBuilder.comment("Change this to determine how many of a tier's tasks need to be completed before tiering up is allowed.  It is a percentage of the total tasks in the tier.  If you set this above 1, you cannot tier up through normal gameplay.  Use with caution.").defineInRange("tierCompletePct", 0.8, 0.01, 2.0);
        MELEE_DIST = serverBuilder.comment("What the mod considers 'melee distance' between two entities when one attacks the other.  This value is squared, so if you want 8 blocks put 64 in this config.").defineInRange("meleeDistance", 64.0, 1.0, 4096.0);
        AVERAGE_MANAWEAVE_COST = serverBuilder.comment("What the mod considers to be the average manaweave cost when generating manaweaves automatically for the player.").defineInRange("averageManaweaveCost", 25, 0, 500);
        SUMMON_BLACKLIST = serverBuilder.comment("Entity IDs in this list cannot be summoned regardless of other configured values.").defineListAllowEmpty("summonBlacklist", Arrays.asList("occultism:possessed_skeleton", "occultism:possessed_ghast", "occultism:possessed_enderman", "occultism:possessed_endermite"), e -> true);
        FOSSILIZE_DAMAGE = serverBuilder.comment("How much damage does the fortification effect set all damage to").defineInRange("fortificationDamageAmount", 4.0, 1.0, 20.0);
        PHYLACTERY_KILLS = serverBuilder.comment("How many kills does it take to fill up a crystal phylactery").defineInRange("phylacteryKills", 50, 1, 1000);
        SUMMON_BOSSES = serverBuilder.comment("Can bosses be summoned?").define("bossSummons", false);
        PHYLACTERY_BOSSES = serverBuilder.comment("Can bosses be captured in phylacteries?").define("bossPhylacteries", false);
        SUMMON_INTERACTIONS = serverBuilder.comment("Should summons be able to be right clicked with items to interact?  For example, shearing sheep or milking cows that have been summoned.").define("summonInteractions", true);
        LITE_MODE = serverBuilder.comment("If true, the Ritual of Arcana will instantly rote spells upon completion, in addition to giving you the spell..").define("liteMode", false);
        FIX_JUMP_BOOST = serverBuilder.comment("By default, jump boost in vanilla is, to put it politely, lacking.  If this is enabled, jumping while under the effects of jump boost will apply additional forward momentum.  This does not stack with the infernal armor set bonus.").define("fixJumpBoost", true);
        DISABLE_VILLAGER_REGISTRATION = serverBuilder.comment("Set this to true to disable new villager profession registration.  This allows compatibility with Bukkit.  Note that you'll need to ensure the Codex Arcana is obtainable through different means!").define("disableNewProfessions", false);
        DAMAGE_SCALE = serverBuilder.comment("Change this to globally scale the damage of M&A damaging spells - this will not affect bound items, this will only affect damaging spell components.").defineInRange("spellDamageMultiplier", 1.0, 0.1, 999.0);
        EXPERIENCE_PER_RUNESCRIBE_UNDO = serverBuilder.comment("How much XP should it cost to undo a mistake when runescribing?  Regardless of this value, it will always consume clay.").defineInRange("runescribeUndoXP", 100, 0, 9999);
        SIGIL_LIMIT = serverBuilder.comment("How many sigils should be allowed per player?").defineInRange("sigilsLimit", 5, 1, 100);
        RITUAL_PERMISSION_LEVEL = serverBuilder.comment("What permission level should rituals run commands at?  The commands must be defined in the recipes.").defineInRange("ritualPermissionLevel", 2, 0, 4);
        CONFUSE_AFFECTS_PLAYERS = serverBuilder.comment("Should confuse affect players?  It will invert their controls.").define("confuseAffectsPlayers", true);
        SPLASH_CREATES_SOURCES = serverBuilder.comment("Should the splash component create permanent water sources?  If false the sources it creates will be temporary.").define("splashCreatesSources", false);
        RTP_DISTANCE = serverBuilder.comment("Maximum RTP distance for RTP portals.").defineInRange("rtpRange", 5000, 100, Integer.MAX_VALUE);
        PROJECTION_ENCHANT_ANYTHING = serverBuilder.comment("Should players be allowed to apply any enchantment on a Projection Rune at the Runic Anvil regardless of their tier?").define("projectionEnchantAnything", false);
        BREAK_MAGNITUDE_MAPPING = serverBuilder.comment("What magnitudes of break should map to what tools? The number set on magnitude will determine the tool material simulated.").defineList("breakMapping", Arrays.asList("minecraft:stone", "minecraft:iron", "minecraft:diamond"), e -> true);
        TIER_5_COMPLEXITY_LIMIT = serverBuilder.comment("How much complexity should Tier 5 allow in spells?").defineInRange("t5ComplexityLimit", 999999, 0, Integer.MAX_VALUE);
        INSIGHT_DAMAGE_COLORS = serverBuilder.comment("What colors should various damage types be when displayed by insight?  Format is 'type_identifier;hex_color'.  Defaults to white if not specified here.").defineListAllowEmpty("damageColors", Arrays.asList("inFire:0xff7400", "onFire:0xff7400", "lightningBolt:0xe5e51a", "lava:0xffffff", "hotFloor:0xff7400", "inWall:0xffffff", "cramming:0xffffff", "drown:0x3a92dc", "starve:0x03790e", "cactus:0x03790e", "fall:0xffffff", "flyIntoWall:0xffffff", "outOfWorld:0xffffff", "generic:0xffffff", "magic:0x871572", "wither:0x471b3f", "anvil:0xffffff", "fallingBlock:0xffffff", "dragonBreath:0x871572", "dryout:0xdbc88f", "sweetBerryBush:0x03790e", "freeze:0x18dee4", "fallingStalactite:0xffffff", "stalagmite:0xffffff", "ma-lightning:0xe5e51a", "ma-briars:0x03790e", "disperse:0x18dee4", "shuriken:0xffffff", "conflagrate:0xff7400"), e -> true);
        DISJUNCTION_BLACKLIST = serverBuilder.comment("What enchantments should disjunction not work on?").defineListAllowEmpty("disjunctionBlacklist", Arrays.asList("mna:soulbound", "mna:fireproof"), e -> true);
        DISENCHANT_BLACKLIST = serverBuilder.comment("Items in this list cannot be disenchanted by the disenchanter.").defineListAllowEmpty("disenchantBlacklist", Arrays.asList("mna:runic_malus"), e -> true);
        REPAIR_BLACKLIST = serverBuilder.comment("Items in this list cannot be repaired in a runeforge with the repair upgrade.  If the item cannot be repaired normally; it doesn't need to be added to this list.").defineListAllowEmpty("repairBlacklist", Arrays.asList(), e -> true);
        ROTE_LOW_TIER_AUTO = serverBuilder.comment("When a player gains a tier, should components X tiers below the current tier automatically be roted?  Set to 5 to disable.").defineInRange("auto_rote_spells_below_tier", 3, 0, 5);
        serverBuilder.pop();
    }

    private static void initArtifactOptions(ForgeConfigSpec.Builder serverBuilder) {
        serverBuilder.comment("Mana and Artifice // Artifact Options").push("ma_artifact_options");
        METEOR_JUMP = serverBuilder.comment("By default meteor jump will follow the mobGriefing rule.  Set this to true to override that regardless of the game rule.").define("meteorJumpDestruction", true);
        SPELLWEAVER_REFLECTCHARGES = serverBuilder.comment("How many reflect charges the Spellweaver armor set has").defineInRange("councilArmorReflectCharges", 3, 0, 99);
        SPELLWEAVER_RECHARGETIME = serverBuilder.comment("How many ticks the Spellweaver armor set needs to regenerate one reflect charge").defineInRange("councilArmorReflectRecharge", 100, 20, 2000);
        DRUIDIC_REFLECTCHANCE = serverBuilder.comment("The percent chance the fey armor will reflect projectiles.").defineInRange("feyArmorReflectChance", 0.2F, 0.0, 1.0);
        DRUIDIC_TELEPORTCHANCE = serverBuilder.comment("The percent chance the fey armor will randomly teleport melee attackers.").defineInRange("feyArmorTeleportChance", 0.2F, 0.0, 1.0);
        serverBuilder.pop();
    }

    private static void initWorldgenOptions(ForgeConfigSpec.Builder serverBuilder) {
        serverBuilder.comment("Mana and Artifice // Worldgen Options").push("ma_worldgen_options");
        WELLSPRING_DISTANCE = serverBuilder.comment("How far apart wellspring nodes must be as a minimum.  They can be farther than this depending on world seed and generation.").defineInRange("wellspringDistance", 500, 100, 15000);
        GENERIC_WELLSPRINGS = serverBuilder.comment("If true, wellsprings will generate with no affinity and the type of lens used will set the affinity of the node.").define("genericWellsprings", false);
        serverBuilder.pop();
    }

    private static void initCastingResourceOptions(ForgeConfigSpec.Builder serverBuilder) {
        SOULS_PLAYER = serverBuilder.comment("How many souls are players worth when killed?").defineInRange("playerSouls", 2500, 1, 20000);
        SOULS_VILLAGER = serverBuilder.comment("How many souls are villagers worth when killed?").defineInRange("villagerSouls", 250, 1, 20000);
        SOULS_FACTION = serverBuilder.comment("How many souls are enemy faction members worth when killed?").defineInRange("factionSouls", 250, 1, 20000);
        SOULS_MOB = serverBuilder.comment("How many souls are mobs worth when killed?").defineInRange("mobSouls", 150, 1, 20000);
        SOULS_ANIMAL = serverBuilder.comment("How many souls are animals worth when killed?").defineInRange("animalSouls", 100, 1, 20000);
        SOULS_UNDEAD = serverBuilder.comment("How many souls are undead worth when killed?").defineInRange("undeadSouls", 50, 1, 20000);
        MANA_TICKS_FOR_REGEN = serverBuilder.comment("How many ticks should it take for a player to fully regenerate their mana bar?  This doesn't take into account hunger, set bonuses, or other effects; this is the baseline value.").defineInRange("ticksForFullManaRegen", 2400, 1, Integer.MAX_VALUE);
        TIER_HEALTH_BOOSTS = serverBuilder.comment("How much bonus health does each tier give?  Only up to the first five values will be used.  Reminder that a heart is TWO points.").defineListAllowEmpty("tier_health_boosts", Arrays.asList(0, 2, 4, 6, 8), e -> true);
        BRIMSTONE_PER_HEART = serverBuilder.comment("How much brimstone is each heart worth?").defineInRange("brimstonePerHeart", 50, 1, 20000);
    }
}