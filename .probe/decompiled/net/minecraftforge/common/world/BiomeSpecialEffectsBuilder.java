package net.minecraftforge.common.world;

import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;

public class BiomeSpecialEffectsBuilder extends BiomeSpecialEffects.Builder {

    public static BiomeSpecialEffectsBuilder copyOf(BiomeSpecialEffects baseEffects) {
        BiomeSpecialEffectsBuilder builder = create(baseEffects.getFogColor(), baseEffects.getWaterColor(), baseEffects.getWaterFogColor(), baseEffects.getSkyColor());
        builder.f_48011_ = baseEffects.getGrassColorModifier();
        baseEffects.getFoliageColorOverride().ifPresent(builder::m_48043_);
        baseEffects.getGrassColorOverride().ifPresent(builder::m_48045_);
        baseEffects.getAmbientParticleSettings().ifPresent(builder::m_48029_);
        baseEffects.getAmbientLoopSoundEvent().ifPresent(builder::m_48023_);
        baseEffects.getAmbientMoodSettings().ifPresent(builder::m_48027_);
        baseEffects.getAmbientAdditionsSettings().ifPresent(builder::m_48025_);
        baseEffects.getBackgroundMusic().ifPresent(builder::m_48021_);
        return builder;
    }

    public static BiomeSpecialEffectsBuilder create(int fogColor, int waterColor, int waterFogColor, int skyColor) {
        return new BiomeSpecialEffectsBuilder(fogColor, waterColor, waterFogColor, skyColor);
    }

    protected BiomeSpecialEffectsBuilder(int fogColor, int waterColor, int waterFogColor, int skyColor) {
        this.m_48019_(fogColor);
        this.m_48034_(waterColor);
        this.m_48037_(waterFogColor);
        this.m_48040_(skyColor);
    }

    public int getFogColor() {
        return this.f_48005_.getAsInt();
    }

    public int waterColor() {
        return this.f_48006_.getAsInt();
    }

    public int getWaterFogColor() {
        return this.f_48007_.getAsInt();
    }

    public int getSkyColor() {
        return this.f_48008_.getAsInt();
    }

    public BiomeSpecialEffects.GrassColorModifier getGrassColorModifier() {
        return this.f_48011_;
    }

    public Optional<Integer> getFoliageColorOverride() {
        return this.f_48009_;
    }

    public Optional<Integer> getGrassColorOverride() {
        return this.f_48010_;
    }

    public Optional<AmbientParticleSettings> getAmbientParticle() {
        return this.f_48012_;
    }

    public Optional<Holder<SoundEvent>> getAmbientLoopSound() {
        return this.f_48013_;
    }

    public Optional<AmbientMoodSettings> getAmbientMoodSound() {
        return this.f_48014_;
    }

    public Optional<AmbientAdditionsSettings> getAmbientAdditionsSound() {
        return this.f_48015_;
    }

    public Optional<Music> getBackgroundMusic() {
        return this.f_48016_;
    }
}