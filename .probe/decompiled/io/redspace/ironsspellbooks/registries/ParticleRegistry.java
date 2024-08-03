package io.redspace.ironsspellbooks.registries;

import com.mojang.serialization.Codec;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.particle.FogParticleOptions;
import io.redspace.ironsspellbooks.particle.ShockwaveParticleOptions;
import io.redspace.ironsspellbooks.particle.SparkParticleOptions;
import io.redspace.ironsspellbooks.particle.ZapParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ParticleRegistry {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, "irons_spellbooks");

    public static final RegistryObject<SimpleParticleType> BLOOD_PARTICLE = PARTICLE_TYPES.register("blood", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> WISP_PARTICLE = PARTICLE_TYPES.register("wisp", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> BLOOD_GROUND_PARTICLE = PARTICLE_TYPES.register("blood_ground", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> SNOWFLAKE_PARTICLE = PARTICLE_TYPES.register("snowflake", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> ELECTRICITY_PARTICLE = PARTICLE_TYPES.register("electricity", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> UNSTABLE_ENDER_PARTICLE = PARTICLE_TYPES.register("unstable_ender", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> DRAGON_FIRE_PARTICLE = PARTICLE_TYPES.register("dragon_fire", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> FIRE_PARTICLE = PARTICLE_TYPES.register("fire", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> EMBER_PARTICLE = PARTICLE_TYPES.register("embers", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> SIPHON_PARTICLE = PARTICLE_TYPES.register("spell", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> ACID_PARTICLE = PARTICLE_TYPES.register("acid", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> ACID_BUBBLE_PARTICLE = PARTICLE_TYPES.register("acid_bubble", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> SNOW_DUST = PARTICLE_TYPES.register("snow_dust", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> RING_SMOKE_PARTICLE = PARTICLE_TYPES.register("ring_smoke", () -> new SimpleParticleType(false));

    public static final RegistryObject<ParticleType<FogParticleOptions>> FOG_PARTICLE = PARTICLE_TYPES.register("fog", () -> new ParticleType<FogParticleOptions>(true, FogParticleOptions.DESERIALIZER) {

        @Override
        public Codec<FogParticleOptions> codec() {
            return FogParticleOptions.CODEC;
        }
    });

    public static final RegistryObject<ParticleType<ShockwaveParticleOptions>> SHOCKWAVE_PARTICLE = PARTICLE_TYPES.register("shockwave", () -> new ParticleType<ShockwaveParticleOptions>(false, ShockwaveParticleOptions.DESERIALIZER) {

        @Override
        public Codec<ShockwaveParticleOptions> codec() {
            return ShockwaveParticleOptions.CODEC;
        }
    });

    public static final RegistryObject<ParticleType<ZapParticleOption>> ZAP_PARTICLE = PARTICLE_TYPES.register("zap", () -> new ParticleType<ZapParticleOption>(false, ZapParticleOption.DESERIALIZER) {

        @Override
        public Codec<ZapParticleOption> codec() {
            return ZapParticleOption.CODEC;
        }
    });

    public static final RegistryObject<SimpleParticleType> FIREFLY_PARTICLE = PARTICLE_TYPES.register("firefly", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> PORTAL_FRAME_PARTICLE = PARTICLE_TYPES.register("portal_frame", () -> new SimpleParticleType(false));

    public static final RegistryObject<ParticleType<BlastwaveParticleOptions>> BLASTWAVE_PARTICLE = PARTICLE_TYPES.register("blastwave", () -> new ParticleType<BlastwaveParticleOptions>(true, BlastwaveParticleOptions.DESERIALIZER) {

        @Override
        public Codec<BlastwaveParticleOptions> codec() {
            return BlastwaveParticleOptions.CODEC;
        }
    });

    public static final RegistryObject<ParticleType<SparkParticleOptions>> SPARK_PARTICLE = PARTICLE_TYPES.register("spark", () -> new ParticleType<SparkParticleOptions>(true, SparkParticleOptions.DESERIALIZER) {

        @Override
        public Codec<SparkParticleOptions> codec() {
            return SparkParticleOptions.CODEC;
        }
    });

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}