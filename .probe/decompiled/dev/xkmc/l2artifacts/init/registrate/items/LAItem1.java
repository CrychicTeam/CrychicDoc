package dev.xkmc.l2artifacts.init.registrate.items;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.content.effects.v1.DamoclesSword;
import dev.xkmc.l2artifacts.content.effects.v1.PerfectionAbsorption;
import dev.xkmc.l2artifacts.content.effects.v1.PerfectionProtection;
import dev.xkmc.l2artifacts.content.effects.v1.ProtectionResistance;
import dev.xkmc.l2artifacts.content.effects.v1.SaintReduction;
import dev.xkmc.l2artifacts.content.effects.v1.SaintRestoration;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetRegHelper;
import dev.xkmc.l2serial.util.Wrappers;

public class LAItem1 {

    public static final SetEntry<ArtifactSet> SET_SAINT;

    public static final SetEntry<ArtifactSet> SET_PERFECTION;

    public static final SetEntry<ArtifactSet> SET_DAMOCLES;

    public static final SetEntry<ArtifactSet> SET_PROTECTION;

    public static final RegistryEntry<PerfectionAbsorption> EFF_PERFECTION_ABSORPTION;

    public static final RegistryEntry<PerfectionProtection> EFF_PERFECTION_PROTECTION;

    public static final RegistryEntry<SaintReduction> EFF_SAINT_REDUCTION;

    public static final RegistryEntry<SaintRestoration> EFF_SAINT_RESTORATION;

    public static final RegistryEntry<DamoclesSword> EFF_DAMOCLES;

    public static final RegistryEntry<ProtectionResistance> EFF_PROTECTION_RESISTANCE;

    public static void register() {
    }

    static {
        SetRegHelper helper = L2Artifacts.REGISTRATE.getSetHelper("saint");
        LinearFuncEntry atk = helper.regLinear("saint_reduction_atk", 0.25, 0.0);
        LinearFuncEntry def = helper.regLinear("saint_reduction_def", 0.25, 0.05);
        LinearFuncEntry period = helper.regLinear("saint_restoration", 100.0, -10.0);
        EFF_SAINT_REDUCTION = helper.setEffect("saint_reduction", () -> new SaintReduction(atk, def)).desc("Sympathy of Saint", "Direct damage dealt reduce to %s%%, damage taken reduce to %s%%").register();
        EFF_SAINT_RESTORATION = helper.setEffect("saint_restoration", () -> new SaintRestoration(period)).desc("Bless of Holiness", "When have empty main hand, restore health to oneself or allies every %s seconds.").register();
        SET_SAINT = helper.regSet(1, 5, "Saint Set").setSlots(ArtifactTypeRegistry.SLOT_HEAD, ArtifactTypeRegistry.SLOT_NECKLACE, ArtifactTypeRegistry.SLOT_BODY, ArtifactTypeRegistry.SLOT_BRACELET, ArtifactTypeRegistry.SLOT_BELT).regItems().buildConfig(c -> c.add(3, (SetEffect) LAItem1.EFF_SAINT_REDUCTION.get()).add(5, (SetEffect) LAItem1.EFF_SAINT_RESTORATION.get())).register();
        helper = L2Artifacts.REGISTRATE.getSetHelper("perfection");
        atk = helper.regLinear("perfection_absorption_period", 100.0, 0.0);
        def = helper.regLinear("perfection_absorption_max", 4.0, 2.0);
        period = helper.regLinear("perfection_protection", 0.2, 0.1);
        EFF_PERFECTION_ABSORPTION = helper.setEffect("perfection_absorption", () -> new PerfectionAbsorption(atk, def)).desc("Heart of Perfection", "When at full health, every %s seconds gain 1 point of absorption, maximum %s points.").register();
        EFF_PERFECTION_PROTECTION = helper.setEffect("perfection_protection", () -> new PerfectionProtection(period)).desc("Eternity of Perfection", "When at full health, reduce damage by %s%%").register();
        SET_PERFECTION = helper.regSet(1, 5, "Perfection Set").setSlots(ArtifactTypeRegistry.SLOT_HEAD, ArtifactTypeRegistry.SLOT_NECKLACE, ArtifactTypeRegistry.SLOT_BODY, ArtifactTypeRegistry.SLOT_BRACELET, ArtifactTypeRegistry.SLOT_BELT).regItems().buildConfig(c -> c.add(2, (SetEffect) LAItem1.EFF_PERFECTION_PROTECTION.get()).add(4, (SetEffect) LAItem1.EFF_PERFECTION_ABSORPTION.get())).register();
        helper = L2Artifacts.REGISTRATE.getSetHelper("damocles");
        atk = helper.regLinear("damocles", 0.6, 0.3);
        EFF_DAMOCLES = helper.setEffect("damocles", () -> new DamoclesSword(atk)).desc("Sword of Damocles", "When at full health, increase damage by %s%%. When below half health, you die immediately.").register();
        SET_DAMOCLES = (SetEntry<ArtifactSet>) Wrappers.cast(helper.regSet(1, 5, "Sword of Damocles").setSlots(ArtifactTypeRegistry.SLOT_HEAD).regItems().buildConfig(c -> c.add(1, (SetEffect) LAItem1.EFF_DAMOCLES.get())).register());
        helper = L2Artifacts.REGISTRATE.getSetHelper("protection");
        EFF_PROTECTION_RESISTANCE = helper.setEffect("protection_resistance", ProtectionResistance::new).desc("Crown of Never Falling Soldier", "Damage taken reduced when health is low.").register();
        SET_PROTECTION = helper.regSet(1, 5, "Never Falling Crown").setSlots(ArtifactTypeRegistry.SLOT_HEAD).regItems().buildConfig(c -> c.add(1, (SetEffect) LAItem1.EFF_PROTECTION_RESISTANCE.get())).register();
    }
}