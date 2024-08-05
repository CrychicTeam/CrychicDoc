package yesman.epicfight.gameasset;

import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.data.reloader.SkillManager;
import yesman.epicfight.api.forgeevent.SkillBuildEvent;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.skill.AirAttack;
import yesman.epicfight.skill.BasicAttack;
import yesman.epicfight.skill.BattojutsuPassive;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.dodge.DodgeSkill;
import yesman.epicfight.skill.dodge.KnockdownWakeupSkill;
import yesman.epicfight.skill.dodge.StepSkill;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.skill.guard.ImpactGuardSkill;
import yesman.epicfight.skill.guard.ParryingSkill;
import yesman.epicfight.skill.identity.MeteorSlamSkill;
import yesman.epicfight.skill.identity.RevelationSkill;
import yesman.epicfight.skill.mover.DemolitionLeapSkill;
import yesman.epicfight.skill.mover.PhantomAscentSkill;
import yesman.epicfight.skill.passive.BerserkerSkill;
import yesman.epicfight.skill.passive.DeathHarvestSkill;
import yesman.epicfight.skill.passive.EmergencyEscapeSkill;
import yesman.epicfight.skill.passive.EnduranceSkill;
import yesman.epicfight.skill.passive.ForbiddenStrengthSkill;
import yesman.epicfight.skill.passive.HyperVitalitySkill;
import yesman.epicfight.skill.passive.PassiveSkill;
import yesman.epicfight.skill.passive.StaminaPillagerSkill;
import yesman.epicfight.skill.passive.SwordmasterSkill;
import yesman.epicfight.skill.passive.TechnicianSkill;
import yesman.epicfight.skill.weaponinnate.BattojutsuSkill;
import yesman.epicfight.skill.weaponinnate.BladeRushSkill;
import yesman.epicfight.skill.weaponinnate.ConditionalWeaponInnateSkill;
import yesman.epicfight.skill.weaponinnate.EverlastingAllegiance;
import yesman.epicfight.skill.weaponinnate.EviscerateSkill;
import yesman.epicfight.skill.weaponinnate.GraspingSpireSkill;
import yesman.epicfight.skill.weaponinnate.GuillotineAxeSkill;
import yesman.epicfight.skill.weaponinnate.LiechtenauerSkill;
import yesman.epicfight.skill.weaponinnate.RushingTempoSkill;
import yesman.epicfight.skill.weaponinnate.SimpleWeaponInnateSkill;
import yesman.epicfight.skill.weaponinnate.SteelWhirlwindSkill;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;
import yesman.epicfight.skill.weaponinnate.WrathfulLightingSkill;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.damagesource.EpicFightDamageType;
import yesman.epicfight.world.damagesource.ExtraDamageInstance;
import yesman.epicfight.world.damagesource.StunType;

@EventBusSubscriber(modid = "epicfight", bus = Bus.FORGE)
public class EpicFightSkills {

    public static Skill BASIC_ATTACK;

    public static Skill AIR_ATTACK;

    public static Skill KNOCKDOWN_WAKEUP;

    public static Skill ROLL;

    public static Skill STEP;

    public static Skill GUARD;

    public static Skill PARRYING;

    public static Skill IMPACT_GUARD;

    public static Skill BERSERKER;

    public static Skill DEATH_HARVEST;

    public static Skill EMERGENCY_ESCAPE;

    public static Skill ENDURANCE;

    public static Skill FORBIDDEN_STRENGTH;

    public static Skill HYPERVITALITY;

    public static Skill STAMINA_PILLAGER;

    public static Skill SWORD_MASTER;

    public static Skill TECHNICIAN;

    public static Skill GUILLOTINE_AXE;

    public static Skill SWEEPING_EDGE;

    public static Skill DANCING_EDGE;

    public static Skill GRASPING_SPIRE;

    public static Skill HEARTPIERCER;

    public static Skill STEEL_WHIRLWIND;

    public static Skill BATTOJUTSU;

    public static Skill BATTOJUTSU_PASSIVE;

    public static Skill RUSHING_TEMPO;

    public static Skill RELENTLESS_COMBO;

    public static Skill SHARP_STAB;

    public static Skill LIECHTENAUER;

    public static Skill EVISCERATE;

    public static Skill BLADE_RUSH;

    public static Skill WRATHFUL_LIGHTING;

    public static Skill TSUNAMI;

    public static Skill EVERLASTING_ALLEGIANCE;

    public static Skill METEOR_STRIKE;

    public static Skill REVELATION;

    public static Skill DEMOLITION_LEAP;

    public static Skill PHANTOM_ASCENT;

    public static void registerSkills() {
        SkillManager.register(BasicAttack::new, BasicAttack.createBasicAttackBuilder(), "epicfight", "basic_attack");
        SkillManager.register(AirAttack::new, AirAttack.createAirAttackBuilder(), "epicfight", "air_attack");
        SkillManager.register(DodgeSkill::new, DodgeSkill.createDodgeBuilder().setAnimations(new ResourceLocation("epicfight", "biped/skill/roll_forward"), new ResourceLocation("epicfight", "biped/skill/roll_backward")), "epicfight", "roll");
        SkillManager.register(StepSkill::new, DodgeSkill.createDodgeBuilder().setAnimations(new ResourceLocation("epicfight", "biped/skill/step_forward"), new ResourceLocation("epicfight", "biped/skill/step_backward"), new ResourceLocation("epicfight", "biped/skill/step_left"), new ResourceLocation("epicfight", "biped/skill/step_right")), "epicfight", "step");
        SkillManager.register(KnockdownWakeupSkill::new, DodgeSkill.createDodgeBuilder().setAnimations(new ResourceLocation("epicfight", "biped/skill/knockdown_wakeup_left"), new ResourceLocation("epicfight", "biped/skill/knockdown_wakeup_right")).setCategory(SkillCategories.KNOCKDOWN_WAKEUP), "epicfight", "knockdown_wakeup");
        SkillManager.register(GuardSkill::new, GuardSkill.createGuardBuilder(), "epicfight", "guard");
        SkillManager.register(ParryingSkill::new, ParryingSkill.createActiveGuardBuilder(), "epicfight", "parrying");
        SkillManager.register(ImpactGuardSkill::new, ImpactGuardSkill.createEnergizingGuardBuilder(), "epicfight", "impact_guard");
        SkillManager.register(BerserkerSkill::new, PassiveSkill.createPassiveBuilder(), "epicfight", "berserker");
        SkillManager.register(DeathHarvestSkill::new, PassiveSkill.createPassiveBuilder(), "epicfight", "death_harvest");
        SkillManager.register(EmergencyEscapeSkill::new, EmergencyEscapeSkill.createEmergencyEscapeBuilder().addAvailableWeaponCategory(CapabilityItem.WeaponCategories.SWORD, CapabilityItem.WeaponCategories.UCHIGATANA, CapabilityItem.WeaponCategories.DAGGER), "epicfight", "emergency_escape");
        SkillManager.register(EnduranceSkill::new, PassiveSkill.createPassiveBuilder().setResource(Skill.Resource.COOLDOWN).setActivateType(Skill.ActivateType.DURATION), "epicfight", "endurance");
        SkillManager.register(ForbiddenStrengthSkill::new, PassiveSkill.createPassiveBuilder(), "epicfight", "forbidden_strength");
        SkillManager.register(HyperVitalitySkill::new, PassiveSkill.createPassiveBuilder().setResource(Skill.Resource.COOLDOWN).setActivateType(Skill.ActivateType.TOGGLE), "epicfight", "hypervitality");
        SkillManager.register(StaminaPillagerSkill::new, PassiveSkill.createPassiveBuilder(), "epicfight", "stamina_pillager");
        SkillManager.register(SwordmasterSkill::new, PassiveSkill.createPassiveBuilder(), "epicfight", "swordmaster");
        SkillManager.register(TechnicianSkill::new, PassiveSkill.createPassiveBuilder(), "epicfight", "technician");
        SkillManager.register(SimpleWeaponInnateSkill::new, SimpleWeaponInnateSkill.createSimpleWeaponInnateBuilder().setAnimations(new ResourceLocation("epicfight", "biped/skill/sweeping_edge")), "epicfight", "sweeping_edge");
        SkillManager.register(SimpleWeaponInnateSkill::new, SimpleWeaponInnateSkill.createSimpleWeaponInnateBuilder().setAnimations(new ResourceLocation("epicfight", "biped/skill/dancing_edge")), "epicfight", "dancing_edge");
        SkillManager.register(GuillotineAxeSkill::new, SimpleWeaponInnateSkill.createSimpleWeaponInnateBuilder().setAnimations(new ResourceLocation("epicfight", "biped/skill/the_guillotine")), "epicfight", "the_guillotine");
        SkillManager.register(GraspingSpireSkill::new, WeaponInnateSkill.createWeaponInnateBuilder(), "epicfight", "grasping_spire");
        SkillManager.register(SimpleWeaponInnateSkill::new, SimpleWeaponInnateSkill.createSimpleWeaponInnateBuilder().setAnimations(new ResourceLocation("epicfight", "biped/skill/heartpiercer")), "epicfight", "heartpiercer");
        SkillManager.register(SteelWhirlwindSkill::new, WeaponInnateSkill.createWeaponInnateBuilder().setActivateType(Skill.ActivateType.CHARGING), "epicfight", "steel_whirlwind");
        SkillManager.register(BattojutsuSkill::new, ConditionalWeaponInnateSkill.createConditionalWeaponInnateBuilder().setSelector(executer -> executer.getOriginal().m_20142_() ? 1 : 0).setAnimations(new ResourceLocation("epicfight", "biped/skill/battojutsu"), new ResourceLocation("epicfight", "biped/skill/battojutsu_dash")), "epicfight", "battojutsu");
        SkillManager.register(BattojutsuPassive::new, Skill.createBuilder().setCategory(SkillCategories.WEAPON_PASSIVE).setActivateType(Skill.ActivateType.ONE_SHOT).setResource(Skill.Resource.COOLDOWN), "epicfight", "battojutsu_passive");
        SkillManager.register(RushingTempoSkill::new, WeaponInnateSkill.createWeaponInnateBuilder(), "epicfight", "rushing_tempo");
        SkillManager.register(SimpleWeaponInnateSkill::new, SimpleWeaponInnateSkill.createSimpleWeaponInnateBuilder().setAnimations(new ResourceLocation("epicfight", "biped/skill/relentless_combo")), "epicfight", "relentless_combo");
        SkillManager.register(LiechtenauerSkill::new, WeaponInnateSkill.createWeaponInnateBuilder().setActivateType(Skill.ActivateType.DURATION), "epicfight", "liechtenauer");
        SkillManager.register(SimpleWeaponInnateSkill::new, SimpleWeaponInnateSkill.createSimpleWeaponInnateBuilder().setAnimations(new ResourceLocation("epicfight", "biped/skill/sharp_stab")), "epicfight", "sharp_stab");
        SkillManager.register(EviscerateSkill::new, WeaponInnateSkill.createWeaponInnateBuilder(), "epicfight", "eviscerate");
        SkillManager.register(BladeRushSkill::new, BladeRushSkill.createBladeRushBuilder(), "epicfight", "blade_rush");
        SkillManager.register(WrathfulLightingSkill::new, SimpleWeaponInnateSkill.createSimpleWeaponInnateBuilder().setAnimations(new ResourceLocation("epicfight", "biped/skill/wrathful_lighting")), "epicfight", "wrathful_lighting");
        SkillManager.register(ConditionalWeaponInnateSkill::new, ConditionalWeaponInnateSkill.createConditionalWeaponInnateBuilder().setSelector(executer -> executer.getOriginal().m_20070_() ? 1 : 0).setAnimations(new ResourceLocation("epicfight", "biped/skill/tsunami"), new ResourceLocation("epicfight", "biped/skill/tsunami_reinforced")), "epicfight", "tsunami");
        SkillManager.register(EverlastingAllegiance::new, WeaponInnateSkill.createWeaponInnateBuilder(), "epicfight", "everlasting_allegiance");
        SkillManager.register(MeteorSlamSkill::new, MeteorSlamSkill.createMeteorSlamBuilder(), "epicfight", "meteor_slam");
        SkillManager.register(RevelationSkill::new, RevelationSkill.createRevelationSkillBuilder(), "epicfight", "revelation");
        SkillManager.register(DemolitionLeapSkill::new, Skill.createMoverBuilder().setActivateType(Skill.ActivateType.CHARGING), "epicfight", "demolition_leap");
        SkillManager.register(PhantomAscentSkill::new, Skill.createMoverBuilder().setResource(Skill.Resource.COOLDOWN), "epicfight", "phantom_ascent");
    }

    @SubscribeEvent
    public static void buildSkillEvent(SkillBuildEvent onBuild) {
        BASIC_ATTACK = onBuild.build("epicfight", "basic_attack");
        AIR_ATTACK = onBuild.build("epicfight", "air_attack");
        ROLL = onBuild.build("epicfight", "roll");
        STEP = onBuild.build("epicfight", "step");
        KNOCKDOWN_WAKEUP = onBuild.build("epicfight", "knockdown_wakeup");
        GUARD = onBuild.build("epicfight", "guard");
        PARRYING = onBuild.build("epicfight", "parrying");
        IMPACT_GUARD = onBuild.build("epicfight", "impact_guard");
        BERSERKER = onBuild.build("epicfight", "berserker");
        DEATH_HARVEST = onBuild.build("epicfight", "death_harvest");
        EMERGENCY_ESCAPE = onBuild.build("epicfight", "emergency_escape");
        ENDURANCE = onBuild.build("epicfight", "endurance");
        FORBIDDEN_STRENGTH = onBuild.build("epicfight", "forbidden_strength");
        HYPERVITALITY = onBuild.build("epicfight", "hypervitality");
        STAMINA_PILLAGER = onBuild.build("epicfight", "stamina_pillager");
        SWORD_MASTER = onBuild.build("epicfight", "swordmaster");
        TECHNICIAN = onBuild.build("epicfight", "technician");
        METEOR_STRIKE = onBuild.build("epicfight", "meteor_slam");
        REVELATION = onBuild.build("epicfight", "revelation");
        DEMOLITION_LEAP = onBuild.build("epicfight", "demolition_leap");
        PHANTOM_ASCENT = onBuild.build("epicfight", "phantom_ascent");
        WeaponInnateSkill sweepingEdge = onBuild.build("epicfight", "sweeping_edge");
        sweepingEdge.newProperty().addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(1.0F)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(2.0F)).addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(20.0F)).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(1.6F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.LONG).addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create())).addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(EpicFightDamageType.WEAPON_INNATE)).registerPropertiesToAnimation();
        SWEEPING_EDGE = sweepingEdge;
        WeaponInnateSkill dancingEdge = onBuild.build("epicfight", "dancing_edge");
        dancingEdge.newProperty().addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(1.0F)).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(1.2F)).addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create())).addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(EpicFightDamageType.WEAPON_INNATE)).newProperty().addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(1.0F)).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(1.2F)).addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create())).addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(EpicFightDamageType.WEAPON_INNATE)).newProperty().addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(1.0F)).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(1.2F)).addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create())).addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(EpicFightDamageType.WEAPON_INNATE)).registerPropertiesToAnimation();
        DANCING_EDGE = dancingEdge;
        WeaponInnateSkill theGuillotine = onBuild.build("epicfight", "the_guillotine");
        theGuillotine.newProperty().addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(1.0F)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(2.5F)).addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(20.0F)).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(2.0F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.LONG).addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create())).addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(EpicFightDamageType.WEAPON_INNATE)).registerPropertiesToAnimation();
        GUILLOTINE_AXE = theGuillotine;
        WeaponInnateSkill graspingSpire = onBuild.build("epicfight", "grasping_spire");
        graspingSpire.newProperty().addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(3.0F)).addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create())).addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(EpicFightDamageType.WEAPON_INNATE)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.setter(4.0F)).newProperty().addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(4.0F)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.25F)).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(1.2F)).addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create())).addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(EpicFightDamageType.WEAPON_INNATE)).registerPropertiesToAnimation();
        GRASPING_SPIRE = graspingSpire;
        WeaponInnateSkill heartpiercer = onBuild.build("epicfight", "heartpiercer");
        heartpiercer.newProperty().addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(10.0F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD).addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create())).addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(EpicFightDamageType.WEAPON_INNATE)).registerPropertiesToAnimation();
        HEARTPIERCER = heartpiercer;
        WeaponInnateSkill steelWhirlwind = onBuild.build("epicfight", "steel_whirlwind");
        steelWhirlwind.newProperty().addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(1.4F)).addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create())).addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(EpicFightDamageType.WEAPON_INNATE)).registerPropertiesToAnimation();
        STEEL_WHIRLWIND = steelWhirlwind;
        BATTOJUTSU_PASSIVE = onBuild.build("epicfight", "battojutsu_passive");
        WeaponInnateSkill battojutsu = onBuild.build("epicfight", "battojutsu");
        battojutsu.newProperty().addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(2.0F)).addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(50.0F)).addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(6.0F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD).addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create())).addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(EpicFightDamageType.WEAPON_INNATE)).registerPropertiesToAnimation();
        BATTOJUTSU = battojutsu;
        WeaponInnateSkill rushingTempo = onBuild.build("epicfight", "rushing_tempo");
        rushingTempo.newProperty().addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(50.0F)).addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(2.0F)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.7F)).addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP.get()).addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create())).addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(EpicFightDamageType.WEAPON_INNATE)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD).registerPropertiesToAnimation();
        RUSHING_TEMPO = rushingTempo;
        WeaponInnateSkill relentlessCombo = onBuild.build("epicfight", "relentless_combo");
        relentlessCombo.newProperty().addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.6F)).addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(1.0F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT).addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create())).addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(EpicFightDamageType.WEAPON_INNATE)).registerPropertiesToAnimation();
        RELENTLESS_COMBO = relentlessCombo;
        WeaponInnateSkill sharpStab = onBuild.build("epicfight", "sharp_stab");
        sharpStab.newProperty().addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.4F)).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(0.5F)).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT).addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create())).addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(EpicFightDamageType.WEAPON_INNATE, EpicFightDamageType.GUARD_PUNCTURE)).registerPropertiesToAnimation();
        SHARP_STAB = sharpStab;
        LIECHTENAUER = onBuild.build("epicfight", "liechtenauer");
        WeaponInnateSkill eviscerate = onBuild.build("epicfight", "eviscerate");
        eviscerate.newProperty().addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(1.0F)).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.setter(2.0F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD).addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(EpicFightDamageType.WEAPON_INNATE)).newProperty().addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(1.0F)).addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create(), ExtraDamageInstance.TARGET_LOST_HEALTH.create(0.5F))).addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(EpicFightDamageType.WEAPON_INNATE)).addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(50.0F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.LONG).registerPropertiesToAnimation();
        EVISCERATE = eviscerate;
        WeaponInnateSkill bladeRush = onBuild.build("epicfight", "blade_rush");
        bladeRush.newProperty().addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(1.0F)).addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create())).addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(EpicFightDamageType.WEAPON_INNATE)).newProperty().addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(EpicFightDamageType.EXECUTION, EpicFightDamageType.WEAPON_INNATE)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.NONE).addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_RUSH_FINISHER.get()).registerPropertiesToAnimation();
        BLADE_RUSH = bladeRush;
        WeaponInnateSkill wrathfulLighting = onBuild.build("epicfight", "wrathful_lighting");
        wrathfulLighting.newProperty().addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(1.0F)).newProperty().addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.setter(8.0F)).addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(3.0F)).addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create())).addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(100.0F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.KNOCKDOWN).addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(EpicFightDamageType.WEAPON_INNATE)).registerPropertiesToAnimation();
        WRATHFUL_LIGHTING = wrathfulLighting;
        WeaponInnateSkill tsunami = onBuild.build("epicfight", "tsunami");
        tsunami.newProperty().addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(100.0F)).addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(EpicFightDamageType.WEAPON_INNATE)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.KNOCKDOWN).registerPropertiesToAnimation();
        TSUNAMI = tsunami;
        WeaponInnateSkill everlastAllegiance = onBuild.build("epicfight", "everlasting_allegiance");
        everlastAllegiance.newProperty().addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(30.0F)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.4F)).addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create())).addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(EpicFightDamageType.WEAPON_INNATE)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD).registerPropertiesToAnimation();
        EVERLASTING_ALLEGIANCE = everlastAllegiance;
    }
}