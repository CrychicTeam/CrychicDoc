package dev.xkmc.l2artifacts.init.registrate;

import com.tterrag.registrate.builders.NoConfigBuilder;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2artifacts.content.mobeffects.FleshOvergrowth;
import dev.xkmc.l2artifacts.content.mobeffects.FrostShield;
import dev.xkmc.l2artifacts.content.mobeffects.FungusInfection;
import dev.xkmc.l2artifacts.content.mobeffects.ThermalMotive;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class ArtifactEffects {

    public static final RegistryEntry<FleshOvergrowth> FLESH_OVERGROWTH = genEffect("flesh_overgrowth", () -> new FleshOvergrowth(MobEffectCategory.NEUTRAL, -1), "Increase max health and damage taken");

    public static final RegistryEntry<FungusInfection> FUNGUS = genEffect("fungus_infection", () -> new FungusInfection(MobEffectCategory.HARMFUL, -1), "Reduce max health, drop fungus on death");

    public static final RegistryEntry<ThermalMotive> THERMAL_MOTIVE = genEffect("thermal_motive", () -> new ThermalMotive(MobEffectCategory.BENEFICIAL, -1), "increase attack damage");

    public static final RegistryEntry<FrostShield> FROST_SHIELD = genEffect("frost_shield", () -> new FrostShield(MobEffectCategory.BENEFICIAL, -1), "reduce damage taken");

    private static <T extends MobEffect> RegistryEntry<T> genEffect(String name, NonNullSupplier<T> sup, String desc) {
        return ((NoConfigBuilder) L2Artifacts.REGISTRATE.effect(name, sup, desc).lang(MobEffect::m_19481_)).register();
    }

    public static void register() {
    }
}