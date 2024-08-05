package dev.xkmc.l2artifacts.init.registrate.items;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.effects.attribute.AttrSetEntry;
import dev.xkmc.l2artifacts.content.effects.attribute.AttributeSetEffect;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.content.effects.v5.DeadCellDodge;
import dev.xkmc.l2artifacts.content.effects.v5.DeadCellParry;
import dev.xkmc.l2artifacts.content.effects.v5.FleshAttack;
import dev.xkmc.l2artifacts.content.effects.v5.FleshOvergrowth;
import dev.xkmc.l2artifacts.content.effects.v5.FungusExplode;
import dev.xkmc.l2artifacts.content.effects.v5.FungusInfect;
import dev.xkmc.l2artifacts.content.effects.v5.GildedAttack;
import dev.xkmc.l2artifacts.content.effects.v5.PoisonAttack;
import dev.xkmc.l2artifacts.content.effects.v5.PoisonTouch;
import dev.xkmc.l2artifacts.content.effects.v5.Slimification;
import dev.xkmc.l2artifacts.content.effects.v5.SlimyBuffer;
import dev.xkmc.l2artifacts.content.effects.v5.ThermalMotive;
import dev.xkmc.l2artifacts.content.effects.v5.ThermalShield;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetRegHelper;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class LAItem5 {

    public static final SetEntry<ArtifactSet> SET_CELL;

    public static final SetEntry<ArtifactSet> SET_FLESH;

    public static final SetEntry<ArtifactSet> SET_FUNGUS;

    public static final SetEntry<ArtifactSet> SET_GILDED;

    public static final SetEntry<ArtifactSet> SET_POISONOUS;

    public static final SetEntry<ArtifactSet> SET_SLIMY;

    public static final SetEntry<ArtifactSet> SET_THERMAL;

    public static final RegistryEntry<DeadCellDodge> CELL_3;

    public static final RegistryEntry<DeadCellParry> CELL_5;

    public static final RegistryEntry<FleshOvergrowth> FLESH_3;

    public static final RegistryEntry<FleshAttack> FLESH_5;

    public static final RegistryEntry<FungusInfect> FUNGUS_3;

    public static final RegistryEntry<FungusExplode> FUNGUS_5;

    public static final RegistryEntry<AttributeSetEffect> GILDED_3;

    public static final RegistryEntry<GildedAttack> GILDED_5;

    public static final RegistryEntry<PoisonTouch> POISON_2;

    public static final RegistryEntry<PoisonAttack> POISON_5;

    public static final RegistryEntry<SlimyBuffer> SLIMY_1;

    public static final RegistryEntry<Slimification> SLIMY_3;

    public static final RegistryEntry<ThermalMotive> THERMAL_2;

    public static final RegistryEntry<ThermalShield> THERMAL_4;

    public static void register() {
    }

    static {
        SetRegHelper helper = L2Artifacts.REGISTRATE.getSetHelper("cell");
        LinearFuncEntry chance = helper.regLinear("cell_dodge_chance", 0.1, 0.05);
        LinearFuncEntry reflect = helper.regLinear("cell_parry_reflect", 0.4, 0.2);
        CELL_3 = helper.setEffect("dead_cell_dodge", () -> new DeadCellDodge(chance)).desc("Dead Cells Dodge", "When you are sneaking, you have %s%% chance to dodge a melee / projectile damage").register();
        CELL_5 = helper.setEffect("dead_cell_parry", () -> new DeadCellParry(reflect)).desc("Dead Cells Parry", "When you shields a melee damage, reflect %s%% of the damage").register();
        SET_CELL = helper.regSet(1, 5, "Dead Cell").setSlots(ArtifactTypeRegistry.SLOT_HEAD, ArtifactTypeRegistry.SLOT_NECKLACE, ArtifactTypeRegistry.SLOT_BODY, ArtifactTypeRegistry.SLOT_BRACELET, ArtifactTypeRegistry.SLOT_BELT).regItems().buildConfig(c -> c.add(3, (SetEffect) LAItem5.CELL_3.get()).add(5, (SetEffect) LAItem5.CELL_5.get())).register();
        helper = L2Artifacts.REGISTRATE.getSetHelper("flesh");
        chance = helper.regLinear("flesh_duration", 100.0, 20.0);
        reflect = helper.regLinear("flesh_threshold", 0.5, 0.0);
        LinearFuncEntry atk = helper.regLinear("flesh_attack", 0.4, 0.2);
        FLESH_3 = helper.setEffect("flesh_overgrowth", () -> new FleshOvergrowth(chance)).desc("Flesh Overgrowth", "On hit, inflict target with %s").register();
        FLESH_5 = helper.setEffect("flesh_decay", () -> new FleshAttack(reflect, atk)).desc("Flesh Decay", "Damage to target with %s%% or less health is increased by %s%%").register();
        SET_FLESH = helper.regSet(1, 5, "Flesh").setSlots(ArtifactTypeRegistry.SLOT_HEAD, ArtifactTypeRegistry.SLOT_NECKLACE, ArtifactTypeRegistry.SLOT_BODY, ArtifactTypeRegistry.SLOT_BRACELET, ArtifactTypeRegistry.SLOT_BELT).regItems().buildConfig(c -> c.add(3, (SetEffect) LAItem5.FLESH_3.get()).add(5, (SetEffect) LAItem5.FLESH_5.get())).register();
        helper = L2Artifacts.REGISTRATE.getSetHelper("fungus");
        chance = helper.regLinear("fungus_infect_duration", 200.0, 40.0);
        reflect = helper.regLinear("fungus_explode_range", 6.0, 1.0);
        atk = helper.regLinear("fungus_explode_rate", 0.6, 0.3);
        FUNGUS_3 = helper.setEffect("fungus_infect", () -> new FungusInfect(chance)).desc("Fungus Infection", "When hurt target, inflict %s").register();
        FUNGUS_5 = helper.setEffect("fungus_explode", () -> new FungusExplode(reflect, atk)).desc("Spore Explosion", "When hurt target, inflict %s%% of the damage dealt to surrounding enemies within %s block with Fungus Infection effect").register();
        SET_FUNGUS = helper.regSet(1, 5, "Fungus").setSlots(ArtifactTypeRegistry.SLOT_HEAD, ArtifactTypeRegistry.SLOT_NECKLACE, ArtifactTypeRegistry.SLOT_BODY, ArtifactTypeRegistry.SLOT_BRACELET, ArtifactTypeRegistry.SLOT_BELT).regItems().buildConfig(c -> c.add(3, (SetEffect) LAItem5.FUNGUS_3.get()).add(5, (SetEffect) LAItem5.FUNGUS_5.get())).register();
        helper = L2Artifacts.REGISTRATE.getSetHelper("gilded");
        chance = helper.regLinear("gilded_armor", 0.2, 0.1);
        reflect = helper.regLinear("gilded_tough", 0.2, 0.1);
        atk = helper.regLinear("gilded_attack", 0.2, 0.1);
        GILDED_3 = helper.setEffect("gilded_3", () -> new AttributeSetEffect(new AttrSetEntry(() -> Attributes.ARMOR, AttributeModifier.Operation.MULTIPLY_BASE, chance, true), new AttrSetEntry(() -> Attributes.ARMOR_TOUGHNESS, AttributeModifier.Operation.MULTIPLY_BASE, reflect, true))).lang("Gilded Armor").register();
        GILDED_5 = helper.setEffect("gilded_5", () -> new GildedAttack(atk)).desc("Infusion Blade", "Increase your attack damage by %s%% of your armor").register();
        SET_GILDED = helper.regSet(1, 5, "Gilded").setSlots(ArtifactTypeRegistry.SLOT_HEAD, ArtifactTypeRegistry.SLOT_NECKLACE, ArtifactTypeRegistry.SLOT_BODY, ArtifactTypeRegistry.SLOT_BRACELET, ArtifactTypeRegistry.SLOT_BELT).regItems().buildConfig(c -> c.add(3, (SetEffect) LAItem5.GILDED_3.get()).add(5, (SetEffect) LAItem5.GILDED_5.get())).register();
        helper = L2Artifacts.REGISTRATE.getSetHelper("poisonous");
        chance = helper.regLinear("poisonous_chance", 0.1, 0.05);
        reflect = helper.regLinear("poisonous_duration", 100.0, 40.0);
        atk = helper.regLinear("poisonous_attack", 0.1, 0.05);
        POISON_2 = helper.setEffect("poisonous_touch", () -> new PoisonTouch(chance, reflect)).desc("Poisonous Touch", "On hit, %s%% chance to inflict target with %s, %s, and %s").register();
        POISON_5 = helper.setEffect("poisonous_erosion", () -> new PoisonAttack(atk)).desc("Poisonous Erosion", "For every harmful effect on target, increase damage you dealt by %s%%").register();
        SET_POISONOUS = helper.regSet(1, 5, "Poisonous").setSlots(ArtifactTypeRegistry.SLOT_HEAD, ArtifactTypeRegistry.SLOT_NECKLACE, ArtifactTypeRegistry.SLOT_BODY, ArtifactTypeRegistry.SLOT_BRACELET, ArtifactTypeRegistry.SLOT_BELT).regItems().buildConfig(c -> c.add(2, (SetEffect) LAItem5.POISON_2.get()).add(5, (SetEffect) LAItem5.POISON_5.get())).register();
        helper = L2Artifacts.REGISTRATE.getSetHelper("slimy");
        chance = helper.regLinear("slimy_buffer", 0.9, 0.02);
        reflect = helper.regLinear("slimy_protection", 0.2, 0.1);
        atk = helper.regLinear("slimy_penalty", 1.0, 0.0);
        SLIMY_1 = helper.setEffect("slimy_buffer", () -> new SlimyBuffer(chance)).desc("Slimy Buffer", "Reduce falling / flying into wall damage by %s%%").register();
        SLIMY_3 = helper.setEffect("slimification", () -> new Slimification(reflect, atk)).desc("Slimification", "Reduce melee / projectile damage taken by %s%%, but increase fire / freezing / explosion / magic damage taken by %s%%").register();
        SET_SLIMY = helper.regSet(1, 5, "Slimy").setSlots(ArtifactTypeRegistry.SLOT_HEAD, ArtifactTypeRegistry.SLOT_BODY, ArtifactTypeRegistry.SLOT_BELT).regItems().buildConfig(c -> c.add(1, (SetEffect) LAItem5.SLIMY_1.get()).add(3, (SetEffect) LAItem5.SLIMY_3.get())).register();
        helper = L2Artifacts.REGISTRATE.getSetHelper("thermal");
        chance = helper.regLinear("thermal_motive", 0.2, 0.1);
        reflect = helper.regLinear("frost_shield", 0.2, 0.1);
        atk = helper.regLinear("thermal_effect_duration", 60.0, 0.0);
        THERMAL_2 = helper.setEffect("thermal_motive", () -> new ThermalMotive(chance, atk)).desc("Thermal Motive", "When you take fire damage, negates the damage and gains %s%% attack boost").register();
        THERMAL_4 = helper.setEffect("frost_shield", () -> new ThermalShield(reflect, atk)).desc("Frost Shield", "When you take freezing damage, negates the damage and gains %s%% damage reduction").register();
        SET_THERMAL = helper.regSet(1, 5, "Thermal").setSlots(ArtifactTypeRegistry.SLOT_HEAD, ArtifactTypeRegistry.SLOT_NECKLACE, ArtifactTypeRegistry.SLOT_BODY, ArtifactTypeRegistry.SLOT_BRACELET, ArtifactTypeRegistry.SLOT_BELT).regItems().buildConfig(c -> c.add(2, (SetEffect) LAItem5.THERMAL_2.get()).add(4, (SetEffect) LAItem5.THERMAL_4.get())).register();
    }
}