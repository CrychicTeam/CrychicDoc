package dev.xkmc.modulargolems.compat.materials.cataclysm;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.item.upgrade.SimpleUpgradeItem;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import dev.xkmc.modulargolems.init.registrate.GolemModifiers;

public class CataCompatRegistry {

    public static final RegistryEntry<IgnisFireballModifier> IGNIS_FIREBALL = GolemModifiers.reg("ignis_fireball", () -> new IgnisFireballModifier(StatFilterType.HEAD, 2), "When target is faraway, shoot Ignis fireballs toward target.");

    public static final RegistryEntry<IgnisAttackModifier> IGNIS_ATTACK = GolemModifiers.reg("ignis_attack", () -> new IgnisAttackModifier(StatFilterType.ATTACK, 2), "Stack Blazing Brande effect and regenerate health when hit target. When health is lower than half, direct damage bypasses armor.");

    public static final RegistryEntry<HarbingerDeathBeamModifier> HARBINGER_BEAM = GolemModifiers.reg("harbinger_death_beam", () -> new HarbingerDeathBeamModifier(StatFilterType.HEAD, 1), "When target is faraway, shoot Death Beam toward target.");

    public static final RegistryEntry<HarbingerHomingMissileModifier> HARBINGER_MISSILE = GolemModifiers.reg("harbinger_missile", () -> new HarbingerHomingMissileModifier(StatFilterType.ATTACK, 2), "When target is faraway, shoot Homing Missile toward target.");

    public static final RegistryEntry<LeviathanBlastPortalModifier> PORTAL = GolemModifiers.reg("leviathan_blast_portal", LeviathanBlastPortalModifier::new, "When target is faraway, create blast portal at target position");

    public static final RegistryEntry<EnderGuardianVoidRuneModifier> RUNE = GolemModifiers.reg("ender_guardian_void_rune", EnderGuardianVoidRuneModifier::new, "Summon void rune toward target");

    public static final RegistryEntry<NetheriteMonstrosityEarthquakeModifier> EARTHQUAKE = GolemModifiers.reg("netherite_monstrosity_earthquake", NetheriteMonstrosityEarthquakeModifier::new, "Jump and cause earthquake on land");

    public static final RegistryEntry<AncientRemnantSandstormModifier> SANDSTORM = GolemModifiers.reg("ancient_remnant_sandstorm", AncientRemnantSandstormModifier::new, "When target is faraway, summon sandstorm at target position");

    public static final RegistryEntry<SimpleUpgradeItem> LEVIATHAN = GolemItems.regModUpgrade("leviathan_blast_portal", () -> PORTAL, "cataclysm").lang("Leviathan Upgrade").register();

    public static final RegistryEntry<SimpleUpgradeItem> ENDER_GUARDIAN = GolemItems.regModUpgrade("ender_guardian_void_rune", () -> RUNE, "cataclysm").lang("Ender Guardian Upgrade").register();

    public static final RegistryEntry<SimpleUpgradeItem> MONSTROSITY = GolemItems.regModUpgrade("netherite_monstrosity_earthquake", () -> EARTHQUAKE, "cataclysm").lang("Netherite Monstrosity Upgrade").register();

    public static final RegistryEntry<SimpleUpgradeItem> ANCIENT_REMNANT = GolemItems.regModUpgrade("ancient_remnant_sandstorm", () -> SANDSTORM, "cataclysm").lang("Ancient Remnant Upgrade").register();

    public static void register() {
    }
}