package fuzs.puzzleslib.impl.biome;

import fuzs.puzzleslib.api.biome.v1.SpecialEffectsContext;
import fuzs.puzzleslib.mixin.accessor.BiomeSpecialEffectsBuilderForgeAccessor;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraftforge.common.world.BiomeSpecialEffectsBuilder;
import org.jetbrains.annotations.NotNull;

public record SpecialEffectsContextForge(BiomeSpecialEffectsBuilder context) implements SpecialEffectsContext {

    @Override
    public void setFogColor(int fogColor) {
        this.context.m_48019_(fogColor);
    }

    @Override
    public int getFogColor() {
        return this.context.getFogColor();
    }

    @Override
    public void setWaterColor(int waterColor) {
        this.context.m_48034_(waterColor);
    }

    @Override
    public int getWaterColor() {
        return this.context.waterColor();
    }

    @Override
    public void setWaterFogColor(int waterFogColor) {
        this.context.m_48037_(waterFogColor);
    }

    @Override
    public int getWaterFogColor() {
        return this.context.getWaterFogColor();
    }

    @Override
    public void setSkyColor(int skyColor) {
        this.context.m_48040_(skyColor);
    }

    @Override
    public int getSkyColor() {
        return this.context.getSkyColor();
    }

    @Override
    public void setFoliageColorOverride(Optional<Integer> foliageColorOverride) {
        ((BiomeSpecialEffectsBuilderForgeAccessor) this.context).puzzleslib$setFoliageColorOverride(foliageColorOverride);
    }

    @Override
    public Optional<Integer> getFoliageColorOverride() {
        return this.context.getFoliageColorOverride();
    }

    @Override
    public void setGrassColorOverride(Optional<Integer> grassColorOverride) {
        ((BiomeSpecialEffectsBuilderForgeAccessor) this.context).puzzleslib$setGrassColorOverride(grassColorOverride);
    }

    @Override
    public Optional<Integer> getGrassColorOverride() {
        return this.context.getGrassColorOverride();
    }

    @Override
    public void setGrassColorModifier(@NotNull BiomeSpecialEffects.GrassColorModifier grassColorModifier) {
        Objects.requireNonNull(grassColorModifier, "grass color modifier is null");
        this.context.m_48031_(grassColorModifier);
    }

    @Override
    public BiomeSpecialEffects.GrassColorModifier getGrassColorModifier() {
        return this.context.getGrassColorModifier();
    }

    @Override
    public void setAmbientParticleSettings(Optional<AmbientParticleSettings> ambientParticleSettings) {
        ((BiomeSpecialEffectsBuilderForgeAccessor) this.context).puzzleslib$setAmbientParticle(ambientParticleSettings);
    }

    @Override
    public Optional<AmbientParticleSettings> getAmbientParticleSettings() {
        return this.context.getAmbientParticle();
    }

    @Override
    public void setAmbientLoopSoundEvent(Optional<Holder<SoundEvent>> ambientLoopSoundEvent) {
        ((BiomeSpecialEffectsBuilderForgeAccessor) this.context).puzzleslib$setAmbientLoopSoundEvent(ambientLoopSoundEvent);
    }

    @Override
    public Optional<Holder<SoundEvent>> getAmbientLoopSoundEvent() {
        return this.context.getAmbientLoopSound();
    }

    @Override
    public void setAmbientMoodSettings(Optional<AmbientMoodSettings> ambientMoodSettings) {
        ((BiomeSpecialEffectsBuilderForgeAccessor) this.context).puzzleslib$setAmbientMoodSettings(ambientMoodSettings);
    }

    @Override
    public Optional<AmbientMoodSettings> getAmbientMoodSettings() {
        return this.context.getAmbientMoodSound();
    }

    @Override
    public void setAmbientAdditionsSettings(Optional<AmbientAdditionsSettings> ambientAdditionsSettings) {
        ((BiomeSpecialEffectsBuilderForgeAccessor) this.context).puzzleslib$setAmbientAdditionsSettings(ambientAdditionsSettings);
    }

    @Override
    public Optional<AmbientAdditionsSettings> getAmbientAdditionsSettings() {
        return this.context.getAmbientAdditionsSound();
    }

    @Override
    public void setBackgroundMusic(Optional<Music> backgroundMusic) {
        ((BiomeSpecialEffectsBuilderForgeAccessor) this.context).puzzleslib$setBackgroundMusic(backgroundMusic);
    }

    @Override
    public Optional<Music> getBackgroundMusic() {
        return this.context.getBackgroundMusic();
    }
}