package dev.xkmc.modulargolems.compat.materials.botania;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.modulargolems.init.registrate.GolemModifiers;

public class BotCompatRegistry {

    public static final RegistryEntry<ManaMendingModifier> MANA_MENDING = GolemModifiers.reg("mana_mending", ManaMendingModifier::new, "Get %s regeneration at a %s mana/hp efficiency");

    public static final RegistryEntry<ManaBoostModifier> MANA_BOOSTING = GolemModifiers.reg("mana_boosting", ManaBoostModifier::new, "Deal %s%% extra damage costing %s mana");

    public static final RegistryEntry<ManaProductionModifier> MANA_PRODUCTION = GolemModifiers.reg("mana_production", ManaProductionModifier::new, "Generate %s mana per second");

    public static final RegistryEntry<ManaBurstModifier> MANA_BURST = GolemModifiers.reg("mana_burst", ManaBurstModifier::new, "Shoot mana burst toward faraway targets dealing %s%% attack damage, costing %s mana");

    public static final RegistryEntry<PixieAttackModifier> PIXIE_ATTACK = GolemModifiers.reg("pixie_attack", PixieAttackModifier::new, "Summon a pixie with a %s%% chance when attacking. Pixie damage +%s.");

    public static final RegistryEntry<PixieCounterattackModifier> PIXIE_COUNTERATTACK = GolemModifiers.reg("pixie_counterattack", PixieCounterattackModifier::new, "Summon a pixie with a %s%% chance when attacked.");

    public static void register() {
    }
}