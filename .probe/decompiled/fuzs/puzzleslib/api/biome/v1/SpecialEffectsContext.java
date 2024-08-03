package fuzs.puzzleslib.api.biome.v1;

import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import org.jetbrains.annotations.NotNull;

public interface SpecialEffectsContext {

    void setFogColor(int var1);

    int getFogColor();

    void setWaterColor(int var1);

    int getWaterColor();

    void setWaterFogColor(int var1);

    int getWaterFogColor();

    void setSkyColor(int var1);

    int getSkyColor();

    void setFoliageColorOverride(Optional<Integer> var1);

    Optional<Integer> getFoliageColorOverride();

    default void setFoliageColorOverride(int foliageColorOverride) {
        this.setFoliageColorOverride(Optional.of(foliageColorOverride));
    }

    default void clearFoliageColorOverride() {
        this.setFoliageColorOverride(Optional.empty());
    }

    void setGrassColorOverride(Optional<Integer> var1);

    Optional<Integer> getGrassColorOverride();

    default void setGrassColorOverride(int grassColorOverride) {
        this.setGrassColorOverride(Optional.of(grassColorOverride));
    }

    default void clearGrassColorOverride() {
        this.setGrassColorOverride(Optional.empty());
    }

    void setGrassColorModifier(@NotNull BiomeSpecialEffects.GrassColorModifier var1);

    BiomeSpecialEffects.GrassColorModifier getGrassColorModifier();

    void setAmbientParticleSettings(Optional<AmbientParticleSettings> var1);

    Optional<AmbientParticleSettings> getAmbientParticleSettings();

    default void setAmbientParticleSettings(@NotNull AmbientParticleSettings ambientParticleSettings) {
        Objects.requireNonNull(ambientParticleSettings, "ambient particle settings is null");
        this.setAmbientParticleSettings(Optional.of(ambientParticleSettings));
    }

    default void clearAmbientParticleSettings() {
        this.setAmbientParticleSettings(Optional.empty());
    }

    void setAmbientLoopSoundEvent(Optional<Holder<SoundEvent>> var1);

    Optional<Holder<SoundEvent>> getAmbientLoopSoundEvent();

    default void setAmbientLoopSoundEvent(@NotNull Holder<SoundEvent> ambientLoopSoundEvent) {
        Objects.requireNonNull(ambientLoopSoundEvent, "ambient loop sound event is null");
        this.setAmbientLoopSoundEvent(Optional.of(ambientLoopSoundEvent));
    }

    default void clearAmbientLoopSoundEvent() {
        this.setAmbientLoopSoundEvent(Optional.empty());
    }

    void setAmbientMoodSettings(Optional<AmbientMoodSettings> var1);

    Optional<AmbientMoodSettings> getAmbientMoodSettings();

    default void setAmbientMoodSettings(@NotNull AmbientMoodSettings ambientMoodSettings) {
        Objects.requireNonNull(ambientMoodSettings, "ambient mood settings is null");
        this.setAmbientMoodSettings(Optional.of(ambientMoodSettings));
    }

    default void clearAmbientMoodSettings() {
        this.setAmbientMoodSettings(Optional.empty());
    }

    void setAmbientAdditionsSettings(Optional<AmbientAdditionsSettings> var1);

    Optional<AmbientAdditionsSettings> getAmbientAdditionsSettings();

    default void setAmbientAdditionsSettings(@NotNull AmbientAdditionsSettings ambientAdditionsSettings) {
        Objects.requireNonNull(ambientAdditionsSettings, "ambient additions settings is null");
        this.setAmbientAdditionsSettings(Optional.of(ambientAdditionsSettings));
    }

    default void clearAmbientAdditionsSettings() {
        this.setAmbientAdditionsSettings(Optional.empty());
    }

    void setBackgroundMusic(Optional<Music> var1);

    Optional<Music> getBackgroundMusic();

    default void setBackgroundMusic(@NotNull Music backgroundMusic) {
        Objects.requireNonNull(backgroundMusic, "background music is null");
        this.setBackgroundMusic(Optional.of(backgroundMusic));
    }

    default void clearBackgroundMusic() {
        this.setBackgroundMusic(Optional.empty());
    }
}