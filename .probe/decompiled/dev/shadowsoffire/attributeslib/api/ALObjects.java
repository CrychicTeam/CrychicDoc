package dev.shadowsoffire.attributeslib.api;

import dev.shadowsoffire.attributeslib.AttributesLib;
import dev.shadowsoffire.attributeslib.impl.BooleanAttribute;
import dev.shadowsoffire.attributeslib.impl.PercentBasedAttribute;
import dev.shadowsoffire.attributeslib.mobfx.BleedingEffect;
import dev.shadowsoffire.attributeslib.mobfx.DetonationEffect;
import dev.shadowsoffire.attributeslib.mobfx.FlyingEffect;
import dev.shadowsoffire.attributeslib.mobfx.GrievousEffect;
import dev.shadowsoffire.attributeslib.mobfx.KnowledgeEffect;
import dev.shadowsoffire.attributeslib.mobfx.SunderingEffect;
import dev.shadowsoffire.attributeslib.mobfx.VitalityEffect;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.ApiStatus.Internal;

public class ALObjects {

    @Internal
    public static void bootstrap() {
        ALObjects.Attributes.bootstrap();
        ALObjects.MobEffects.bootstrap();
        ALObjects.Particles.bootstrap();
        ALObjects.Sounds.bootstrap();
        ALObjects.DamageTypes.bootstrap();
    }

    public static class Attributes {

        public static final RegistryObject<Attribute> ARMOR_PIERCE = AttributesLib.R.attribute("armor_pierce", () -> new RangedAttribute("attributeslib:armor_pierce", 0.0, 0.0, 1000.0).m_22084_(true));

        public static final RegistryObject<Attribute> ARMOR_SHRED = AttributesLib.R.attribute("armor_shred", () -> new PercentBasedAttribute("attributeslib:armor_shred", 0.0, 0.0, 2.0).m_22084_(true));

        public static final RegistryObject<Attribute> ARROW_DAMAGE = AttributesLib.R.attribute("arrow_damage", () -> new PercentBasedAttribute("attributeslib:arrow_damage", 1.0, 0.0, 10.0).m_22084_(true));

        public static final RegistryObject<Attribute> ARROW_VELOCITY = AttributesLib.R.attribute("arrow_velocity", () -> new PercentBasedAttribute("attributeslib:arrow_velocity", 1.0, 0.0, 10.0).m_22084_(true));

        public static final RegistryObject<Attribute> COLD_DAMAGE = AttributesLib.R.attribute("cold_damage", () -> new RangedAttribute("attributeslib:cold_damage", 0.0, 0.0, 1000.0).m_22084_(true));

        public static final RegistryObject<Attribute> CRIT_CHANCE = AttributesLib.R.attribute("crit_chance", () -> new PercentBasedAttribute("attributeslib:crit_chance", 0.05, 0.0, 10.0).m_22084_(true));

        public static final RegistryObject<Attribute> CRIT_DAMAGE = AttributesLib.R.attribute("crit_damage", () -> new PercentBasedAttribute("attributeslib:crit_damage", 1.5, 1.0, 100.0).m_22084_(true));

        public static final RegistryObject<Attribute> CURRENT_HP_DAMAGE = AttributesLib.R.attribute("current_hp_damage", () -> new PercentBasedAttribute("attributeslib:current_hp_damage", 0.0, 0.0, 1.0).m_22084_(true));

        public static final RegistryObject<Attribute> DODGE_CHANCE = AttributesLib.R.attribute("dodge_chance", () -> new PercentBasedAttribute("attributeslib:dodge_chance", 0.0, 0.0, 1.0).m_22084_(true));

        public static final RegistryObject<Attribute> DRAW_SPEED = AttributesLib.R.attribute("draw_speed", () -> new PercentBasedAttribute("attributeslib:draw_speed", 1.0, 0.0, 4.0).m_22084_(true));

        public static final RegistryObject<Attribute> EXPERIENCE_GAINED = AttributesLib.R.attribute("experience_gained", () -> new PercentBasedAttribute("attributeslib:experience_gained", 1.0, 0.0, 1000.0).m_22084_(true));

        public static final RegistryObject<Attribute> FIRE_DAMAGE = AttributesLib.R.attribute("fire_damage", () -> new RangedAttribute("attributeslib:fire_damage", 0.0, 0.0, 1000.0).m_22084_(true));

        public static final RegistryObject<Attribute> GHOST_HEALTH = AttributesLib.R.attribute("ghost_health", () -> new RangedAttribute("attributeslib:ghost_health", 0.0, 0.0, 1000.0).m_22084_(true));

        public static final RegistryObject<Attribute> HEALING_RECEIVED = AttributesLib.R.attribute("healing_received", () -> new PercentBasedAttribute("attributeslib:healing_received", 1.0, 0.0, 1000.0).m_22084_(true));

        public static final RegistryObject<Attribute> LIFE_STEAL = AttributesLib.R.attribute("life_steal", () -> new PercentBasedAttribute("attributeslib:life_steal", 0.0, 0.0, 10.0).m_22084_(true));

        public static final RegistryObject<Attribute> MINING_SPEED = AttributesLib.R.attribute("mining_speed", () -> new PercentBasedAttribute("attributeslib:mining_speed", 1.0, 0.0, 10.0).m_22084_(true));

        public static final RegistryObject<Attribute> OVERHEAL = AttributesLib.R.attribute("overheal", () -> new PercentBasedAttribute("attributeslib:overheal", 0.0, 0.0, 10.0).m_22084_(true));

        public static final RegistryObject<Attribute> PROT_PIERCE = AttributesLib.R.attribute("prot_pierce", () -> new RangedAttribute("attributeslib:prot_pierce", 0.0, 0.0, 34.0).m_22084_(true));

        public static final RegistryObject<Attribute> PROT_SHRED = AttributesLib.R.attribute("prot_shred", () -> new PercentBasedAttribute("attributeslib:prot_shred", 0.0, 0.0, 1.0).m_22084_(true));

        public static final RegistryObject<Attribute> ELYTRA_FLIGHT = AttributesLib.R.attribute("elytra_flight", () -> new BooleanAttribute("attributeslib:elytra_flight", false).m_22084_(true));

        public static final RegistryObject<Attribute> CREATIVE_FLIGHT = AttributesLib.R.attribute("creative_flight", () -> new BooleanAttribute("attributeslib:creative_flight", false).m_22084_(true));

        @Internal
        public static void bootstrap() {
        }
    }

    public static class DamageTypes {

        public static final ResourceKey<DamageType> BLEEDING = ResourceKey.create(Registries.DAMAGE_TYPE, AttributesLib.loc("bleeding"));

        public static final ResourceKey<DamageType> DETONATION = ResourceKey.create(Registries.DAMAGE_TYPE, AttributesLib.loc("detonation"));

        public static final ResourceKey<DamageType> CURRENT_HP_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, AttributesLib.loc("current_hp_damage"));

        public static final ResourceKey<DamageType> FIRE_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, AttributesLib.loc("fire_damage"));

        public static final ResourceKey<DamageType> COLD_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, AttributesLib.loc("cold_damage"));

        @Internal
        public static void bootstrap() {
        }
    }

    public static class MobEffects {

        public static final RegistryObject<BleedingEffect> BLEEDING = AttributesLib.R.effect("bleeding", BleedingEffect::new);

        public static final RegistryObject<DetonationEffect> DETONATION = AttributesLib.R.effect("detonation", DetonationEffect::new);

        public static final RegistryObject<GrievousEffect> GRIEVOUS = AttributesLib.R.effect("grievous", GrievousEffect::new);

        public static final RegistryObject<KnowledgeEffect> KNOWLEDGE = AttributesLib.R.effect("knowledge", KnowledgeEffect::new);

        public static final RegistryObject<SunderingEffect> SUNDERING = AttributesLib.R.effect("sundering", SunderingEffect::new);

        public static final RegistryObject<VitalityEffect> VITALITY = AttributesLib.R.effect("vitality", VitalityEffect::new);

        public static final RegistryObject<FlyingEffect> FLYING = AttributesLib.R.effect("flying", FlyingEffect::new);

        @Internal
        public static void bootstrap() {
        }
    }

    public static class Particles {

        public static final RegistryObject<SimpleParticleType> APOTH_CRIT = AttributesLib.R.particle("apoth_crit", () -> new SimpleParticleType(false));

        @Internal
        public static void bootstrap() {
        }
    }

    public static class Sounds {

        public static final RegistryObject<SoundEvent> DODGE = AttributesLib.R.sound("dodge");

        @Internal
        public static void bootstrap() {
        }
    }
}