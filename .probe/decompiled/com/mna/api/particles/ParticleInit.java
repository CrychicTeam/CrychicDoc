package com.mna.api.particles;

import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ParticleInit {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, "mna");

    public static RegistryObject<MAParticleType> SPARKLE_RANDOM = PARTICLES.register("sparkle", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> SPARKLE_LERP_POINT = PARTICLES.register("sparkle_lerp_point", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> SPARKLE_BEZIER_POINT = PARTICLES.register("sparkle_bezier_point", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> SPARKLE_VELOCITY = PARTICLES.register("sparkle_velocity", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> SPARKLE_GRAVITY = PARTICLES.register("sparkle_gravity", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> SPARKLE_STATIONARY = PARTICLES.register("sparkle_stationary", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> BLUE_SPARKLE_STATIONARY = PARTICLES.register("blue_sparkle_stationary", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> BLUE_SPARKLE_GRAVITY = PARTICLES.register("blue_sparkle_gravity", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> BLUE_SPARKLE_VELOCITY = PARTICLES.register("blue_sparkle_velocity", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> BLUE_SPARKLE_SPHERE_ORBIT = PARTICLES.register("blue_sparkle_sphere_orbit", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> BLUE_SPARKLE_ORBIT = PARTICLES.register("blue_sparkle_orbit", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> LIGHT_VELOCITY = PARTICLES.register("light_velocity", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> BLUE_FLAME = PARTICLES.register("blue_flame", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> FLAME = PARTICLES.register("flame", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> FLAME_LERP = PARTICLES.register("flame_lerp", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> FLAME_ORBIT = PARTICLES.register("flame_orbit", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> HELLFIRE = PARTICLES.register("hellfire", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> FROST = PARTICLES.register("frost", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> FROST_LERP = PARTICLES.register("frost_lerp", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> MIST = PARTICLES.register("mist", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> WATER = PARTICLES.register("water", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> WATER_LERP = PARTICLES.register("water_lerp", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> ENDER = PARTICLES.register("ender", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> ENDER_VELOCITY = PARTICLES.register("ender_velocity", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> ARCANE = PARTICLES.register("arcane", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> ARCANE_LERP = PARTICLES.register("arcane_lerp", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> ARCANE_RANDOM = PARTICLES.register("arcane_random", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> ARCANE_MAGELIGHT = PARTICLES.register("arcane_magelight", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> EARTH = PARTICLES.register("earth", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> AIR_VELOCITY = PARTICLES.register("air_velocity", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> AIR_ORBIT = PARTICLES.register("air_orbit", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> AIR_LERP = PARTICLES.register("air_lerp", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> DUST = PARTICLES.register("dust", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> DUST_LERP = PARTICLES.register("dust_lerp", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> GLOW = PARTICLES.register("glow", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> SOUL = PARTICLES.register("soul", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> HEART = PARTICLES.register("heart", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> BONE = PARTICLES.register("bone", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> BONE_ORBIT = PARTICLES.register("bone_orbit", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> DRIP = PARTICLES.register("drip", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> ITEM = PARTICLES.register("item", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> ENCHANT = PARTICLES.register("enchant", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> COZY_SMOKE = PARTICLES.register("cozy_smoke", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> LIGHTNING_BOLT = PARTICLES.register("lightning_bolt", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> WRAITH_BOLT = PARTICLES.register("wraith_bolt", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> TRAIL = PARTICLES.register("trail", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> TRAIL_VELOCITY = PARTICLES.register("trail_velocity", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> TRAIL_ORBIT = PARTICLES.register("trail_orbit", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> TRAIL_SPHERE_ORBIT = PARTICLES.register("trail_sphere_orbit", () -> new MAParticleType());

    public static RegistryObject<MAParticleType> TRAIL_BEZIER = PARTICLES.register("trail_bezier", () -> new MAParticleType());
}