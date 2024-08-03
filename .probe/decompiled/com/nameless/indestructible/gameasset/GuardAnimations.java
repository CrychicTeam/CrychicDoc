package com.nameless.indestructible.gameasset;

import com.nameless.indestructible.api.animation.types.CustomGuardAnimation;
import java.util.Set;
import net.minecraftforge.fml.ModList;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.GuardAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.forgeevent.AnimationRegistryEvent;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.gameasset.ColliderPreset;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.world.damagesource.EpicFightDamageType;
import yesman.epicfight.world.damagesource.StunType;

public class GuardAnimations {

    public static StaticAnimation MOB_SWORD_GUARD;

    public static StaticAnimation MOB_LONGSWORD_GUARD;

    public static StaticAnimation MOB_GREATSWORD_GUARD;

    public static StaticAnimation MOB_KATANA_GUARD;

    public static StaticAnimation MOB_SPEAR_GUARD;

    public static StaticAnimation MOB_DUAL_SWORD_GUARD;

    public static StaticAnimation MOB_COUNTER_ATTACK;

    public static StaticAnimation MOB_YAMATO_GUARD;

    public static StaticAnimation MOB_AGONY_GUARD;

    public static StaticAnimation MOB_RUINE_GUARD;

    public static StaticAnimation MOB_HERRSCHER_GUARD;

    public static StaticAnimation SHIELD_HIT;

    public static StaticAnimation MOB_SHIELD_GUARD;

    public static void registerAnimations(AnimationRegistryEvent event) {
        event.getRegistryMap().put("indestructible", GuardAnimations::build);
    }

    private static void build() {
        HumanoidArmature biped = Armatures.BIPED;
        MOB_SWORD_GUARD = new CustomGuardAnimation("guard/guard_sword", "epicfight:biped/skill/guard_sword_hit", biped);
        MOB_LONGSWORD_GUARD = new CustomGuardAnimation("guard/guard_longsword", "epicfight:biped/skill/guard_longsword_hit", biped);
        MOB_GREATSWORD_GUARD = new CustomGuardAnimation("guard/guard_greatsword", "epicfight:biped/skill/guard_greatsword_hit", biped);
        MOB_KATANA_GUARD = new CustomGuardAnimation("guard/guard_katana", "epicfight:biped/skill/guard_katana_hit", biped);
        MOB_SPEAR_GUARD = new CustomGuardAnimation("guard/guard_spear", "epicfight:biped/skill/guard_spear_hit", biped);
        MOB_DUAL_SWORD_GUARD = new CustomGuardAnimation("guard/guard_dualsword", "epicfight:biped/skill/guard_dualsword_hit", biped);
        MOB_COUNTER_ATTACK = new AttackAnimation(0.3F, 0.08F, 0.1F, 0.15F, 0.525F, ColliderPreset.FIST, biped.legR, "guard/counter", biped).addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP.get()).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT).addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT.get()).addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(EpicFightDamageType.COUNTER)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.LONG).addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(1.0F)).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.setter(0.5F)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.setter(1.0F));
        if (ModList.get().isLoaded("yamatomoveset")) {
            MOB_YAMATO_GUARD = new CustomGuardAnimation("guard/guard_yamato", "yamatomoveset:biped/yamato/yamato_guard_hit", biped);
        }
        if (ModList.get().isLoaded("wom")) {
            MOB_AGONY_GUARD = new CustomGuardAnimation("guard/guard_agony", "epicfight:biped/skill/guard_spear_hit", biped);
            MOB_RUINE_GUARD = new CustomGuardAnimation("guard/guard_ruine", "epicfight:biped/skill/guard_longsword_hit", biped);
            MOB_HERRSCHER_GUARD = new CustomGuardAnimation("guard/guard_herrscher", "indestructible:guard/shield_hit_left", biped);
        }
        SHIELD_HIT = new GuardAnimation(0.05F, "guard/shield_hit_left", biped);
        MOB_SHIELD_GUARD = new CustomGuardAnimation("guard/guard_shield", "indestructible:guard/shield_hit_left", biped, true);
    }
}