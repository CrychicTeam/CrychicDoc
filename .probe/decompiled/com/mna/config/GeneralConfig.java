package com.mna.config;

import com.mna.ManaAndArtifice;
import com.mna.api.config.GeneralConfigValues;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

@EventBusSubscriber(modid = "mna", bus = Bus.MOD)
public class GeneralConfig {

    public static final GeneralConfigProvider GENERAL;

    public static final ForgeConfigSpec GENERAL_SPEC;

    @SubscribeEvent
    public static void onModConfigEvent(ModConfigEvent configEvent) {
        if (configEvent.getConfig().getSpec() == GENERAL_SPEC) {
            bakeConfig();
        }
    }

    private static void bakeConfig() {
        ManaAndArtifice.LOGGER.info("M&A >> Baking General Config");
        GeneralConfigValues.ModifyVillagerTrades = GeneralConfigProvider.MODIFY_VILLAGER_TRADES.get();
        GeneralConfigValues.DisableVillagerRegistration = GeneralConfigProvider.DISABLE_VILLAGER_REGISTRATION.get();
        GeneralConfigValues.ClassicRaids = GeneralConfigProvider.CLASSIC_RAIDS.get();
        GeneralConfigValues.ForcedRaidStrength = GeneralConfigProvider.FORCED_RAID_STRENGTH.get();
        GeneralConfigValues.RaidChanceBase = GeneralConfigProvider.RAID_CHANCE_BASE.get();
        GeneralConfigValues.RaidChanceTier = GeneralConfigProvider.RAID_CHANCE_TIER.get();
        GeneralConfigValues.RaidSpawnAttempts = GeneralConfigProvider.RAID_SPAWN_ATTEMPTS.get();
        GeneralConfigValues.FactionMobKillIre = GeneralConfigProvider.FACTION_MOB_KILL_IRE.get();
        GeneralConfigValues.WellspringDistance = GeneralConfigProvider.WELLSPRING_DISTANCE.get();
        GeneralConfigValues.GenericWellsprings = GeneralConfigProvider.GENERIC_WELLSPRINGS.get();
        GeneralConfigValues.GradualTimeChange = GeneralConfigProvider.GRADUAL_TIME_CHANGE.get();
        GeneralConfigValues.FossilizeDamage = GeneralConfigProvider.FOSSILIZE_DAMAGE.get();
        GeneralConfigValues.PhylacteryKillCount = GeneralConfigProvider.PHYLACTERY_KILLS.get();
        GeneralConfigValues.SummonBosses = GeneralConfigProvider.SUMMON_BOSSES.get();
        GeneralConfigValues.SummonInteractions = GeneralConfigProvider.SUMMON_INTERACTIONS.get();
        GeneralConfigValues.BossesAllowedInPhylacteries = GeneralConfigProvider.PHYLACTERY_BOSSES.get();
        GeneralConfigValues.GlobalDamageScale = GeneralConfigProvider.DAMAGE_SCALE.get();
        GeneralConfigValues.SigilLimit = GeneralConfigProvider.SIGIL_LIMIT.get();
        GeneralConfigValues.AutoRoteLowTier = GeneralConfigProvider.ROTE_LOW_TIER_AUTO.get();
        GeneralConfigValues.Tier5ComplexityLimit = GeneralConfigProvider.TIER_5_COMPLEXITY_LIMIT.get();
        GeneralConfigValues.ConfuseAffectsPlayers = GeneralConfigProvider.CONFUSE_AFFECTS_PLAYERS.get();
        GeneralConfigValues.SplashCreatesSources = GeneralConfigProvider.SPLASH_CREATES_SOURCES.get();
        GeneralConfigValues.SummonBlacklist = new HashSet((Collection) GeneralConfigProvider.SUMMON_BLACKLIST.get());
        GeneralConfigValues.BreakMagnitudeMapping = new ArrayList((Collection) GeneralConfigProvider.BREAK_MAGNITUDE_MAPPING.get());
        GeneralConfigValues.InsightDamageColors = new HashSet((Collection) GeneralConfigProvider.INSIGHT_DAMAGE_COLORS.get());
        GeneralConfigValues.DisjunctionBlacklist = new HashSet((Collection) GeneralConfigProvider.DISJUNCTION_BLACKLIST.get());
        GeneralConfigValues.MeteorJumpEnabled = GeneralConfigProvider.METEOR_JUMP.get();
        GeneralConfigValues.SpellweaverReflectCharges = GeneralConfigProvider.SPELLWEAVER_REFLECTCHARGES.get();
        GeneralConfigValues.SpellweaverReflectTime = GeneralConfigProvider.SPELLWEAVER_RECHARGETIME.get();
        GeneralConfigValues.DruidicReflectChance = GeneralConfigProvider.DRUIDIC_REFLECTCHANCE.get();
        GeneralConfigValues.DruidicTeleportChance = GeneralConfigProvider.DRUIDIC_TELEPORTCHANCE.get();
        GeneralConfigValues.TotalManaRegenTicks = GeneralConfigProvider.MANA_TICKS_FOR_REGEN.get();
        GeneralConfigValues.SoulsForPlayerKill = GeneralConfigProvider.SOULS_PLAYER.get();
        GeneralConfigValues.SoulsForVillagerKill = GeneralConfigProvider.SOULS_VILLAGER.get();
        GeneralConfigValues.SoulsForFactionMobKill = GeneralConfigProvider.SOULS_FACTION.get();
        GeneralConfigValues.SoulsForMobKill = GeneralConfigProvider.SOULS_MOB.get();
        GeneralConfigValues.SoulsForAnimalKill = GeneralConfigProvider.SOULS_ANIMAL.get();
        GeneralConfigValues.SoulsForUndeadKill = GeneralConfigProvider.SOULS_UNDEAD.get();
        GeneralConfigValues.BrimstonePerHeart = GeneralConfigProvider.BRIMSTONE_PER_HEART.get();
        GeneralConfigValues.TierHealthBoosts = GeneralConfigProvider.TIER_HEALTH_BOOSTS.get();
        GeneralConfigValues.TierUpPercentTasksComplete = GeneralConfigProvider.TIER_PCT.get();
        GeneralConfigValues.MeleeDistance = GeneralConfigProvider.MELEE_DIST.get();
        GeneralConfigValues.LiteMode = GeneralConfigProvider.LITE_MODE.get();
        GeneralConfigValues.FixJumpBoost = GeneralConfigProvider.FIX_JUMP_BOOST.get();
        GeneralConfigValues.ProjectionCanEnchantAnything = GeneralConfigProvider.PROJECTION_ENCHANT_ANYTHING.get();
        GeneralConfigValues.AverageManaweaveCost = GeneralConfigProvider.AVERAGE_MANAWEAVE_COST.get();
        GeneralConfigValues.ExperiencePerRunescribeUndo = GeneralConfigProvider.EXPERIENCE_PER_RUNESCRIBE_UNDO.get();
        GeneralConfigValues.RitualPermissionLevel = GeneralConfigProvider.RITUAL_PERMISSION_LEVEL.get();
        GeneralConfigValues.RTPDistance = GeneralConfigProvider.RTP_DISTANCE.get();
        GeneralConfigValues.DisenchantBlacklist = new HashSet((Collection) GeneralConfigProvider.DISENCHANT_BLACKLIST.get());
        GeneralConfigValues.RepairBlacklist = new HashSet((Collection) GeneralConfigProvider.REPAIR_BLACKLIST.get());
        GeneralConfigValues.WardingCandleBlacklist = new HashSet((Collection) GeneralConfigProvider.WARD_BLACKLIST.get());
        GeneralConfigValues.WardingCandleWhitelist = new HashSet((Collection) GeneralConfigProvider.WARD_WHITELIST.get());
        GeneralConfigValues.DemonBonusDamageMod = GeneralConfigProvider.DEMON_BONUS_DAMAGE_MOD.get();
        GeneralConfigValues.DemonBonusRangeMod = GeneralConfigProvider.DEMON_BONUS_RANGE_MOD.get();
        GeneralConfigValues.DemonBonusRadiusMod = GeneralConfigProvider.DEMON_BONUS_RADIUS_MOD.get();
        GeneralConfigValues.UndeadCoffinSoulsDelay = GeneralConfigProvider.UNDEAD_COFFIN_SOULS_DELAY.get();
        GeneralConfigValues.UndeadCoffinSoulsRestored = GeneralConfigProvider.UNDEAD_COFFIN_SOULS_RESTORED.get();
    }

    public static float getDamageMultiplier() {
        return (float) Math.max(GeneralConfigValues.GlobalDamageScale, 0.1F);
    }

    static {
        Pair<GeneralConfigProvider, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(GeneralConfigProvider::new);
        GENERAL = (GeneralConfigProvider) specPair.getLeft();
        GENERAL_SPEC = (ForgeConfigSpec) specPair.getRight();
    }
}