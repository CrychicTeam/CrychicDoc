package dev.xkmc.l2artifacts.init.registrate.items;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.effects.attribute.AttrSetEntry;
import dev.xkmc.l2artifacts.content.effects.attribute.SimpleCASetEffect;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.content.effects.v3.GluttonyHeal;
import dev.xkmc.l2artifacts.content.effects.v3.Photosynthesisffect;
import dev.xkmc.l2artifacts.content.effects.v3.SunBlockMask;
import dev.xkmc.l2artifacts.content.effects.v3.VampireBurn;
import dev.xkmc.l2artifacts.content.effects.v3.VampireHeal;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetRegHelper;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class LAItem3 {

    public static final SetEntry<ArtifactSet> SET_PHOTOSYN;

    public static final SetEntry<ArtifactSet> SET_VAMPIRE;

    public static final SetEntry<ArtifactSet> SET_SUN_BLOCK;

    public static final SetEntry<ArtifactSet> SET_GLUTTONY;

    public static final SetEntry<ArtifactSet> SET_FALLEN;

    public static final RegistryEntry<Photosynthesisffect> EFF_PHOTOSYN;

    public static final RegistryEntry<VampireBurn> EFF_VAMPIRE_BURN;

    public static final RegistryEntry<VampireHeal> EFF_VAMPIRE_HEAL;

    public static final RegistryEntry<SunBlockMask> EFF_SUN_BLOCK;

    public static final RegistryEntry<SimpleCASetEffect> EFF_GLUTTONY_FAST;

    public static final RegistryEntry<GluttonyHeal> EFF_GLUTTONY_HEAL;

    public static final RegistryEntry<SimpleCASetEffect> EFF_FALLEN_1;

    public static final RegistryEntry<SimpleCASetEffect> EFF_FALLEN_2;

    public static final RegistryEntry<SimpleCASetEffect> EFF_FALLEN_3;

    public static final RegistryEntry<SimpleCASetEffect> EFF_FALLEN_4;

    public static final RegistryEntry<SimpleCASetEffect> EFF_FALLEN_5;

    public static void register() {
    }

    static {
        SetRegHelper helper = L2Artifacts.REGISTRATE.getSetHelper("photosynthesis");
        LinearFuncEntry period = helper.regLinear("photosynthesis_period", 5.0, -1.0);
        LinearFuncEntry lo = helper.regLinear("photosynthesis_low", 5.0, -1.0);
        LinearFuncEntry hi = helper.regLinear("photosynthesis_high", 12.0, -1.0);
        EFF_PHOTOSYN = helper.setEffect("photosynthesis", () -> new Photosynthesisffect(period, lo, hi)).desc("Flourishing Ring", "Every %s seconds, when under sun with light level of %s or higher, restore food level. When in light level lower than %s, increase exhaustion").register();
        SET_PHOTOSYN = helper.regSet(1, 5, "Photosynthesis Hat").setSlots(ArtifactTypeRegistry.SLOT_HEAD).regItems().buildConfig(c -> c.add(1, (SetEffect) LAItem3.EFF_PHOTOSYN.get())).register();
        helper = L2Artifacts.REGISTRATE.getSetHelper("vampire");
        period = helper.regLinear("vampire_burn_low", 3.0, 1.0);
        lo = helper.regLinear("vampire_heal_light", 4.0, 1.0);
        hi = helper.regLinear("vampire_heal_ratio", 0.2, 0.1);
        EFF_VAMPIRE_BURN = helper.setEffect("vampire_burn", () -> new VampireBurn(period)).desc("Photophobic", "When under direct sunlight, burn. Whe sunlight light level received is not higher than %s, get Night Vision.").register();
        EFF_VAMPIRE_HEAL = helper.setEffect("vampire_heal", () -> new VampireHeal(lo, hi)).desc("Blood Thurst", "When sunlight light level received is not higher than %s, when dealing damage, heal %s%% of that damage.").register();
        SET_VAMPIRE = helper.regSet(1, 5, "Vampire Set").setSlots(ArtifactTypeRegistry.SLOT_HEAD, ArtifactTypeRegistry.SLOT_NECKLACE, ArtifactTypeRegistry.SLOT_BODY, ArtifactTypeRegistry.SLOT_BRACELET, ArtifactTypeRegistry.SLOT_BELT).regItems().buildConfig(c -> c.add(1, (SetEffect) LAItem3.EFF_VAMPIRE_BURN.get()).add(4, (SetEffect) LAItem3.EFF_VAMPIRE_HEAL.get())).register();
        helper = L2Artifacts.REGISTRATE.getSetHelper("sun_block");
        EFF_SUN_BLOCK = helper.setEffect("sun_block", SunBlockMask::new).desc("Sunlight Hat", "Block sunlight for the player").register();
        SET_SUN_BLOCK = helper.regSet(1, 5, "Umbrella").setSlots(ArtifactTypeRegistry.SLOT_HEAD).regItems().buildConfig(c -> c.add(1, (SetEffect) LAItem3.EFF_SUN_BLOCK.get())).register();
        helper = L2Artifacts.REGISTRATE.getSetHelper("gluttony");
        period = helper.regLinear("gluttony_attack", 0.2, 0.1);
        lo = helper.regLinear("gluttony_swing", 0.04, 0.02);
        hi = helper.regLinear("gluttony_speed", 0.1, 0.05);
        EFF_GLUTTONY_FAST = helper.setEffect("gluttony_fast", () -> new SimpleCASetEffect(player -> player.m_21023_(MobEffects.HUNGER), new AttrSetEntry(() -> Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.MULTIPLY_BASE, period, true), new AttrSetEntry(() -> Attributes.ATTACK_SPEED, AttributeModifier.Operation.MULTIPLY_BASE, lo, true), new AttrSetEntry(() -> Attributes.MOVEMENT_SPEED, AttributeModifier.Operation.MULTIPLY_BASE, hi, true))).desc("Hunger Strike", "When having Hunger effect:").register();
        LinearFuncEntry eat = helper.regLinear("gluttony_eat", 2.0, 1.0);
        EFF_GLUTTONY_HEAL = helper.setEffect("gluttony_eat", () -> new GluttonyHeal(eat)).desc("Flesh Eater", "When kill entities, restore food level by %s and saturation by %s").register();
        SET_GLUTTONY = helper.regSet(1, 5, "Gluttony Set").setSlots(ArtifactTypeRegistry.SLOT_HEAD, ArtifactTypeRegistry.SLOT_NECKLACE, ArtifactTypeRegistry.SLOT_BODY, ArtifactTypeRegistry.SLOT_BRACELET, ArtifactTypeRegistry.SLOT_BELT).regItems().buildConfig(c -> c.add(3, (SetEffect) LAItem3.EFF_GLUTTONY_FAST.get()).add(5, (SetEffect) LAItem3.EFF_GLUTTONY_HEAL.get())).register();
        helper = L2Artifacts.REGISTRATE.getSetHelper("fury_of_fallen");
        period = helper.regLinear("fury_of_fallen_1", 0.1, 0.05);
        lo = helper.regLinear("fury_of_fallen_2", 0.2, 0.1);
        hi = helper.regLinear("fury_of_fallen_3", 0.3, 0.15);
        eat = helper.regLinear("fury_of_fallen_4", 0.4, 0.2);
        LinearFuncEntry atk5 = helper.regLinear("fury_of_fallen_5", 0.5, 0.25);
        EFF_FALLEN_1 = helper.setEffect("fury_of_fallen_1", () -> new SimpleCASetEffect(player -> (double) player.m_21223_() <= (double) player.m_21233_() * 0.5, new AttrSetEntry(() -> Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.MULTIPLY_BASE, period, true))).desc("Furry of Fallen Lv.1", "When health is less than 50%:").register();
        EFF_FALLEN_2 = helper.setEffect("fury_of_fallen_2", () -> new SimpleCASetEffect(player -> (double) player.m_21223_() <= (double) player.m_21233_() * 0.4, new AttrSetEntry(() -> Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.MULTIPLY_BASE, lo, true))).desc("Furry of Fallen Lv.2", "When health is less than 40%:").register();
        EFF_FALLEN_3 = helper.setEffect("fury_of_fallen_3", () -> new SimpleCASetEffect(player -> (double) player.m_21223_() <= (double) player.m_21233_() * 0.3, new AttrSetEntry(() -> Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.MULTIPLY_BASE, hi, true))).desc("Furry of Fallen Lv.3", "When health is less than 30%:").register();
        EFF_FALLEN_4 = helper.setEffect("fury_of_fallen_4", () -> new SimpleCASetEffect(player -> (double) player.m_21223_() <= (double) player.m_21233_() * 0.2, new AttrSetEntry(() -> Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.MULTIPLY_BASE, eat, true))).desc("Furry of Fallen Lv.4", "When health is less than 20%:").register();
        EFF_FALLEN_5 = helper.setEffect("fury_of_fallen_5", () -> new SimpleCASetEffect(player -> (double) player.m_21223_() <= (double) player.m_21233_() * 0.1, new AttrSetEntry(() -> Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.MULTIPLY_BASE, atk5, true))).desc("Furry of Fallen Lv.5", "When health is less than 10%:").register();
        SET_FALLEN = helper.regSet(1, 5, "Fury of Fallen").setSlots(ArtifactTypeRegistry.SLOT_HEAD, ArtifactTypeRegistry.SLOT_NECKLACE, ArtifactTypeRegistry.SLOT_BODY, ArtifactTypeRegistry.SLOT_BRACELET, ArtifactTypeRegistry.SLOT_BELT).regItems().buildConfig(c -> c.add(1, (SetEffect) LAItem3.EFF_FALLEN_1.get()).add(2, (SetEffect) LAItem3.EFF_FALLEN_2.get()).add(3, (SetEffect) LAItem3.EFF_FALLEN_3.get()).add(4, (SetEffect) LAItem3.EFF_FALLEN_4.get()).add(5, (SetEffect) LAItem3.EFF_FALLEN_5.get())).register();
    }
}