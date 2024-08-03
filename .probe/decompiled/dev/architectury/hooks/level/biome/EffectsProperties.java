package dev.architectury.hooks.level.biome;

import java.util.Optional;
import java.util.OptionalInt;
import net.minecraft.core.Holder;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import org.jetbrains.annotations.Nullable;

public interface EffectsProperties {

    int getFogColor();

    int getWaterColor();

    int getWaterFogColor();

    int getSkyColor();

    OptionalInt getFoliageColorOverride();

    OptionalInt getGrassColorOverride();

    BiomeSpecialEffects.GrassColorModifier getGrassColorModifier();

    Optional<AmbientParticleSettings> getAmbientParticle();

    Optional<Holder<SoundEvent>> getAmbientLoopSound();

    Optional<AmbientMoodSettings> getAmbientMoodSound();

    Optional<AmbientAdditionsSettings> getAmbientAdditionsSound();

    Optional<Music> getBackgroundMusic();

    public interface Mutable extends EffectsProperties {

        EffectsProperties.Mutable setFogColor(int var1);

        EffectsProperties.Mutable setWaterColor(int var1);

        EffectsProperties.Mutable setWaterFogColor(int var1);

        EffectsProperties.Mutable setSkyColor(int var1);

        EffectsProperties.Mutable setFoliageColorOverride(@Nullable Integer var1);

        EffectsProperties.Mutable setGrassColorOverride(@Nullable Integer var1);

        EffectsProperties.Mutable setGrassColorModifier(BiomeSpecialEffects.GrassColorModifier var1);

        EffectsProperties.Mutable setAmbientParticle(@Nullable AmbientParticleSettings var1);

        EffectsProperties.Mutable setAmbientLoopSound(@Nullable Holder<SoundEvent> var1);

        EffectsProperties.Mutable setAmbientMoodSound(@Nullable AmbientMoodSettings var1);

        EffectsProperties.Mutable setAmbientAdditionsSound(@Nullable AmbientAdditionsSettings var1);

        EffectsProperties.Mutable setBackgroundMusic(@Nullable Music var1);
    }
}