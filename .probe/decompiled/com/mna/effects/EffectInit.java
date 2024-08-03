package com.mna.effects;

import com.mna.effects.beneficial.EffectBindWounds;
import com.mna.effects.beneficial.EffectBriarthornBarrier;
import com.mna.effects.beneficial.EffectBulwark;
import com.mna.effects.beneficial.EffectBurningRage;
import com.mna.effects.beneficial.EffectCamouflage;
import com.mna.effects.beneficial.EffectChronoAnchor;
import com.mna.effects.beneficial.EffectCircleOfPower;
import com.mna.effects.beneficial.EffectEldrinFlight;
import com.mna.effects.beneficial.EffectEldrinSight;
import com.mna.effects.beneficial.EffectFireShield;
import com.mna.effects.beneficial.EffectInstantMana;
import com.mna.effects.beneficial.EffectLevitation;
import com.mna.effects.beneficial.EffectManaBoost;
import com.mna.effects.beneficial.EffectManaRegen;
import com.mna.effects.beneficial.EffectManaShield;
import com.mna.effects.beneficial.EffectMistForm;
import com.mna.effects.beneficial.EffectPilgrim;
import com.mna.effects.beneficial.EffectPossession;
import com.mna.effects.beneficial.EffectRepair;
import com.mna.effects.beneficial.EffectSoar;
import com.mna.effects.beneficial.EffectSpiderClimbing;
import com.mna.effects.beneficial.EffectTelekinesis;
import com.mna.effects.beneficial.EffectTrueInvisibility;
import com.mna.effects.beneficial.EffectUndeadFrostWalker;
import com.mna.effects.beneficial.EffectWaterWalking;
import com.mna.effects.beneficial.EffectWellspringSight;
import com.mna.effects.beneficial.EffectWindWall;
import com.mna.effects.beneficial.SimpleBeneficialEffect;
import com.mna.effects.harmful.EffectAsphyxiate;
import com.mna.effects.harmful.EffectChill;
import com.mna.effects.harmful.EffectChronoExhaustion;
import com.mna.effects.harmful.EffectConfuse;
import com.mna.effects.harmful.EffectDeath;
import com.mna.effects.harmful.EffectDisjunction;
import com.mna.effects.harmful.EffectEarthquake;
import com.mna.effects.harmful.EffectEntangle;
import com.mna.effects.harmful.EffectHeatwave;
import com.mna.effects.harmful.EffectIcarianFlight;
import com.mna.effects.harmful.EffectLacerate;
import com.mna.effects.harmful.EffectLivingBomb;
import com.mna.effects.harmful.EffectManaBurn;
import com.mna.effects.harmful.EffectMindControl;
import com.mna.effects.harmful.EffectMindVision;
import com.mna.effects.harmful.EffectSoulVulnerability;
import com.mna.effects.harmful.EffectSunder;
import com.mna.effects.harmful.EffectWrithingBrambles;
import com.mna.effects.harmful.SimpleHarmfulEffect;
import com.mna.effects.neutral.EffectAmplifyMagic;
import com.mna.effects.neutral.EffectChoosingWellspring;
import com.mna.effects.neutral.EffectColdDark;
import com.mna.effects.neutral.EffectDampenMagic;
import com.mna.effects.neutral.EffectGrow;
import com.mna.effects.neutral.EffectLift;
import com.mna.effects.neutral.EffectManaStunt;
import com.mna.effects.neutral.EffectShrink;
import com.mna.effects.neutral.EffectSoaked;
import com.mna.effects.neutral.SimpleNeutralEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EffectInit {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, "mna");

    public static final RegistryObject<MobEffect> REPAIR = EFFECTS.register("repair", () -> new EffectRepair());

    public static final RegistryObject<MobEffect> ELDRIN_SIGHT = EFFECTS.register("eldrin_sight", () -> new EffectEldrinSight());

    public static final RegistryObject<MobEffect> WELLSPRING_SIGHT = EFFECTS.register("wellspring_sight", () -> new EffectWellspringSight());

    public static final RegistryObject<MobEffect> WATERY_GRAVE = EFFECTS.register("watery_grave", () -> new SimpleHarmfulEffect());

    public static final RegistryObject<MobEffect> GRAVITY_WELL = EFFECTS.register("gravity_well", () -> new SimpleHarmfulEffect());

    public static final RegistryObject<MobEffect> CHRONO_ANCHOR = EFFECTS.register("chrono_anchor", () -> new EffectChronoAnchor());

    public static final RegistryObject<MobEffect> CHRONO_EXHAUSTION = EFFECTS.register("chrono_exhaustion", () -> new EffectChronoExhaustion());

    public static final RegistryObject<MobEffect> ENTANGLE = EFFECTS.register("entangle", () -> new EffectEntangle());

    public static final RegistryObject<MobEffect> SILENCE = EFFECTS.register("silence", () -> new SimpleHarmfulEffect());

    public static final RegistryObject<MobEffect> MANA_BOOST = EFFECTS.register("mana_boost", () -> new EffectManaBoost());

    public static final RegistryObject<MobEffect> MANA_REGEN = EFFECTS.register("mana_regen", () -> new EffectManaRegen());

    public static final RegistryObject<MobEffect> DISPEL_EXHAUSTION = EFFECTS.register("dispel_exhaustion", () -> new SimpleNeutralEffect());

    public static final RegistryObject<MobEffect> ASPHYXIATE = EFFECTS.register("asphyxiate", () -> new EffectAsphyxiate());

    public static final RegistryObject<MobEffect> INSTANT_MANA = EFFECTS.register("instant_mana", () -> new EffectInstantMana(10079487));

    public static final RegistryObject<MobEffect> MANA_BURN = EFFECTS.register("mana_burn", () -> new EffectManaBurn());

    public static final RegistryObject<MobEffect> MANA_STUNT = EFFECTS.register("mana_stunt", () -> new EffectManaStunt());

    public static final RegistryObject<MobEffect> LIFE_TAP = EFFECTS.register("life_tap", () -> new SimpleHarmfulEffect());

    public static final RegistryObject<EffectManaShield> MANA_SHIELD = EFFECTS.register("mana_shield", () -> new EffectManaShield());

    public static final RegistryObject<MobEffect> BRIARTHORN_BARRIER = EFFECTS.register("briarthorn_barrier", () -> new EffectBriarthornBarrier());

    public static final RegistryObject<MobEffect> FIRE_SHIELD = EFFECTS.register("flame_cloak", () -> new EffectFireShield());

    public static final RegistryObject<MobEffect> TRUE_INVISIBILITY = EFFECTS.register("greater_invisibility", () -> new EffectTrueInvisibility());

    public static final RegistryObject<MobEffect> CONFUSION = EFFECTS.register("confusion", () -> new EffectConfuse());

    public static final RegistryObject<MobEffect> MIND_CONTROL = EFFECTS.register("mind_control", () -> new EffectMindControl());

    public static final RegistryObject<MobEffect> DIVINATION = EFFECTS.register("divination", () -> new SimpleBeneficialEffect());

    public static final RegistryObject<MobEffect> MIST_FORM = EFFECTS.register("mist_form", () -> new EffectMistForm());

    public static final RegistryObject<MobEffect> MIND_VISION = EFFECTS.register("mind_vision", () -> new EffectMindVision());

    public static final RegistryObject<MobEffect> LIVING_BOMB = EFFECTS.register("living_bomb", () -> new EffectLivingBomb());

    public static final RegistryObject<MobEffect> TIMED_DEATH = EFFECTS.register("death", () -> new EffectDeath());

    public static final RegistryObject<MobEffect> POSSESSION = EFFECTS.register("possession", () -> new EffectPossession());

    public static final RegistryObject<MobEffect> CHOOSING_WELLSPRING = EFFECTS.register("choosing_wellspring", () -> new EffectChoosingWellspring());

    public static final RegistryObject<MobEffect> ELDRIN_FLIGHT = EFFECTS.register("eldrin_flight", () -> new EffectEldrinFlight());

    public static final RegistryObject<MobEffect> CIRCLE_OF_POWER = EFFECTS.register("circle_of_power", () -> new EffectCircleOfPower());

    public static final RegistryObject<MobEffect> COLD_DARK = EFFECTS.register("cold_dark", () -> new EffectColdDark());

    public static final RegistryObject<MobEffect> SOUL_VULNERABILITY = EFFECTS.register("soul_vulnerability", () -> new EffectSoulVulnerability());

    public static final RegistryObject<MobEffect> WIND_WALL = EFFECTS.register("wind_wall", () -> new EffectWindWall());

    public static final RegistryObject<MobEffect> LEVITATION = EFFECTS.register("levitation", () -> new EffectLevitation());

    public static final RegistryObject<MobEffect> FORTIFICATION = EFFECTS.register("fossilize", () -> new SimpleBeneficialEffect());

    public static final RegistryObject<MobEffect> SNOWBLIND = EFFECTS.register("snowblind", () -> new SimpleHarmfulEffect());

    public static final RegistryObject<MobEffect> BIND_WOUNDS = EFFECTS.register("bind_wounds", () -> new EffectBindWounds());

    public static final RegistryObject<MobEffect> INSIGHT = EFFECTS.register("insight", () -> new SimpleNeutralEffect());

    public static final RegistryObject<MobEffect> EARTHQUAKE = EFFECTS.register("earthquake", () -> new EffectEarthquake());

    public static final RegistryObject<MobEffect> AMPLIFY_MAGIC = EFFECTS.register("amplify_magic", () -> new EffectAmplifyMagic());

    public static final RegistryObject<MobEffect> DAMPEN_MAGIC = EFFECTS.register("dampen_magic", () -> new EffectDampenMagic());

    public static final RegistryObject<MobEffect> SOAKED = EFFECTS.register("soaked", () -> new EffectSoaked());

    public static final RegistryObject<MobEffect> SUNDER = EFFECTS.register("sunder", () -> new EffectSunder());

    public static final RegistryObject<MobEffect> FRAILTY = EFFECTS.register("frailty", () -> new SimpleHarmfulEffect());

    public static final RegistryObject<MobEffect> DISJUNCTION = EFFECTS.register("disjunction", () -> new EffectDisjunction());

    public static final RegistryObject<MobEffect> TELEKINESIS = EFFECTS.register("telekinesis", () -> new EffectTelekinesis());

    public static final RegistryObject<MobEffect> WATER_WALKING = EFFECTS.register("water_walking", () -> new EffectWaterWalking());

    public static final RegistryObject<MobEffect> SPIDER_CLIMBING = EFFECTS.register("spider_climbing", () -> new EffectSpiderClimbing());

    public static final RegistryObject<MobEffect> SOAR = EFFECTS.register("soar", () -> new EffectSoar());

    public static final RegistryObject<MobEffect> LACERATE = EFFECTS.register("lacerate", () -> new EffectLacerate());

    public static final RegistryObject<MobEffect> WRITHING_BRAMBLES = EFFECTS.register("writhing_brambles", () -> new EffectWrithingBrambles());

    public static final RegistryObject<MobEffect> AURA_OF_FROST = EFFECTS.register("aura_of_frost", () -> new EffectUndeadFrostWalker());

    public static final RegistryObject<MobEffect> BURNING_RAGE = EFFECTS.register("burning_rage", () -> new EffectBurningRage());

    public static final RegistryObject<MobEffect> REDUCE = EFFECTS.register("reduced", () -> new EffectShrink());

    public static final RegistryObject<MobEffect> ENLARGE = EFFECTS.register("enlarged", () -> new EffectGrow());

    public static final RegistryObject<MobEffect> CAMOUFLAGE = EFFECTS.register("camouflage", () -> new EffectCamouflage());

    public static final RegistryObject<MobEffect> BULWARK = EFFECTS.register("bulwark", () -> new EffectBulwark());

    public static final RegistryObject<MobEffect> HEATWAVE = EFFECTS.register("heatwave", () -> new EffectHeatwave());

    public static final RegistryObject<MobEffect> CHILL = EFFECTS.register("chill", () -> new EffectChill());

    public static final RegistryObject<MobEffect> LIFT = EFFECTS.register("lift", () -> new EffectLift());

    public static final RegistryObject<MobEffect> PILGRIM = EFFECTS.register("pilgrim", () -> new EffectPilgrim());

    public static final RegistryObject<MobEffect> ICARIAN_FLIGHT = EFFECTS.register("icarian_flight", () -> new EffectIcarianFlight());
}