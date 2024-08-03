package net.minecraft.world.level.biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import java.util.OptionalInt;
import javax.annotation.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.StringRepresentable;

public class BiomeSpecialEffects {

    public static final Codec<BiomeSpecialEffects> CODEC = RecordCodecBuilder.create(p_47971_ -> p_47971_.group(Codec.INT.fieldOf("fog_color").forGetter(p_151782_ -> p_151782_.fogColor), Codec.INT.fieldOf("water_color").forGetter(p_151780_ -> p_151780_.waterColor), Codec.INT.fieldOf("water_fog_color").forGetter(p_151778_ -> p_151778_.waterFogColor), Codec.INT.fieldOf("sky_color").forGetter(p_151776_ -> p_151776_.skyColor), Codec.INT.optionalFieldOf("foliage_color").forGetter(p_151774_ -> p_151774_.foliageColorOverride), Codec.INT.optionalFieldOf("grass_color").forGetter(p_151772_ -> p_151772_.grassColorOverride), BiomeSpecialEffects.GrassColorModifier.CODEC.optionalFieldOf("grass_color_modifier", BiomeSpecialEffects.GrassColorModifier.NONE).forGetter(p_151770_ -> p_151770_.grassColorModifier), AmbientParticleSettings.CODEC.optionalFieldOf("particle").forGetter(p_151768_ -> p_151768_.ambientParticleSettings), SoundEvent.CODEC.optionalFieldOf("ambient_sound").forGetter(p_151766_ -> p_151766_.ambientLoopSoundEvent), AmbientMoodSettings.CODEC.optionalFieldOf("mood_sound").forGetter(p_151764_ -> p_151764_.ambientMoodSettings), AmbientAdditionsSettings.CODEC.optionalFieldOf("additions_sound").forGetter(p_151762_ -> p_151762_.ambientAdditionsSettings), Music.CODEC.optionalFieldOf("music").forGetter(p_151760_ -> p_151760_.backgroundMusic)).apply(p_47971_, BiomeSpecialEffects::new));

    private final int fogColor;

    private final int waterColor;

    private final int waterFogColor;

    private final int skyColor;

    private final Optional<Integer> foliageColorOverride;

    private final Optional<Integer> grassColorOverride;

    private final BiomeSpecialEffects.GrassColorModifier grassColorModifier;

    private final Optional<AmbientParticleSettings> ambientParticleSettings;

    private final Optional<Holder<SoundEvent>> ambientLoopSoundEvent;

    private final Optional<AmbientMoodSettings> ambientMoodSettings;

    private final Optional<AmbientAdditionsSettings> ambientAdditionsSettings;

    private final Optional<Music> backgroundMusic;

    BiomeSpecialEffects(int int0, int int1, int int2, int int3, Optional<Integer> optionalInteger4, Optional<Integer> optionalInteger5, BiomeSpecialEffects.GrassColorModifier biomeSpecialEffectsGrassColorModifier6, Optional<AmbientParticleSettings> optionalAmbientParticleSettings7, Optional<Holder<SoundEvent>> optionalHolderSoundEvent8, Optional<AmbientMoodSettings> optionalAmbientMoodSettings9, Optional<AmbientAdditionsSettings> optionalAmbientAdditionsSettings10, Optional<Music> optionalMusic11) {
        this.fogColor = int0;
        this.waterColor = int1;
        this.waterFogColor = int2;
        this.skyColor = int3;
        this.foliageColorOverride = optionalInteger4;
        this.grassColorOverride = optionalInteger5;
        this.grassColorModifier = biomeSpecialEffectsGrassColorModifier6;
        this.ambientParticleSettings = optionalAmbientParticleSettings7;
        this.ambientLoopSoundEvent = optionalHolderSoundEvent8;
        this.ambientMoodSettings = optionalAmbientMoodSettings9;
        this.ambientAdditionsSettings = optionalAmbientAdditionsSettings10;
        this.backgroundMusic = optionalMusic11;
    }

    public int getFogColor() {
        return this.fogColor;
    }

    public int getWaterColor() {
        return this.waterColor;
    }

    public int getWaterFogColor() {
        return this.waterFogColor;
    }

    public int getSkyColor() {
        return this.skyColor;
    }

    public Optional<Integer> getFoliageColorOverride() {
        return this.foliageColorOverride;
    }

    public Optional<Integer> getGrassColorOverride() {
        return this.grassColorOverride;
    }

    public BiomeSpecialEffects.GrassColorModifier getGrassColorModifier() {
        return this.grassColorModifier;
    }

    public Optional<AmbientParticleSettings> getAmbientParticleSettings() {
        return this.ambientParticleSettings;
    }

    public Optional<Holder<SoundEvent>> getAmbientLoopSoundEvent() {
        return this.ambientLoopSoundEvent;
    }

    public Optional<AmbientMoodSettings> getAmbientMoodSettings() {
        return this.ambientMoodSettings;
    }

    public Optional<AmbientAdditionsSettings> getAmbientAdditionsSettings() {
        return this.ambientAdditionsSettings;
    }

    public Optional<Music> getBackgroundMusic() {
        return this.backgroundMusic;
    }

    public static class Builder {

        private OptionalInt fogColor = OptionalInt.empty();

        private OptionalInt waterColor = OptionalInt.empty();

        private OptionalInt waterFogColor = OptionalInt.empty();

        private OptionalInt skyColor = OptionalInt.empty();

        private Optional<Integer> foliageColorOverride = Optional.empty();

        private Optional<Integer> grassColorOverride = Optional.empty();

        private BiomeSpecialEffects.GrassColorModifier grassColorModifier = BiomeSpecialEffects.GrassColorModifier.NONE;

        private Optional<AmbientParticleSettings> ambientParticle = Optional.empty();

        private Optional<Holder<SoundEvent>> ambientLoopSoundEvent = Optional.empty();

        private Optional<AmbientMoodSettings> ambientMoodSettings = Optional.empty();

        private Optional<AmbientAdditionsSettings> ambientAdditionsSettings = Optional.empty();

        private Optional<Music> backgroundMusic = Optional.empty();

        public BiomeSpecialEffects.Builder fogColor(int int0) {
            this.fogColor = OptionalInt.of(int0);
            return this;
        }

        public BiomeSpecialEffects.Builder waterColor(int int0) {
            this.waterColor = OptionalInt.of(int0);
            return this;
        }

        public BiomeSpecialEffects.Builder waterFogColor(int int0) {
            this.waterFogColor = OptionalInt.of(int0);
            return this;
        }

        public BiomeSpecialEffects.Builder skyColor(int int0) {
            this.skyColor = OptionalInt.of(int0);
            return this;
        }

        public BiomeSpecialEffects.Builder foliageColorOverride(int int0) {
            this.foliageColorOverride = Optional.of(int0);
            return this;
        }

        public BiomeSpecialEffects.Builder grassColorOverride(int int0) {
            this.grassColorOverride = Optional.of(int0);
            return this;
        }

        public BiomeSpecialEffects.Builder grassColorModifier(BiomeSpecialEffects.GrassColorModifier biomeSpecialEffectsGrassColorModifier0) {
            this.grassColorModifier = biomeSpecialEffectsGrassColorModifier0;
            return this;
        }

        public BiomeSpecialEffects.Builder ambientParticle(AmbientParticleSettings ambientParticleSettings0) {
            this.ambientParticle = Optional.of(ambientParticleSettings0);
            return this;
        }

        public BiomeSpecialEffects.Builder ambientLoopSound(Holder<SoundEvent> holderSoundEvent0) {
            this.ambientLoopSoundEvent = Optional.of(holderSoundEvent0);
            return this;
        }

        public BiomeSpecialEffects.Builder ambientMoodSound(AmbientMoodSettings ambientMoodSettings0) {
            this.ambientMoodSettings = Optional.of(ambientMoodSettings0);
            return this;
        }

        public BiomeSpecialEffects.Builder ambientAdditionsSound(AmbientAdditionsSettings ambientAdditionsSettings0) {
            this.ambientAdditionsSettings = Optional.of(ambientAdditionsSettings0);
            return this;
        }

        public BiomeSpecialEffects.Builder backgroundMusic(@Nullable Music music0) {
            this.backgroundMusic = Optional.ofNullable(music0);
            return this;
        }

        public BiomeSpecialEffects build() {
            return new BiomeSpecialEffects(this.fogColor.orElseThrow(() -> new IllegalStateException("Missing 'fog' color.")), this.waterColor.orElseThrow(() -> new IllegalStateException("Missing 'water' color.")), this.waterFogColor.orElseThrow(() -> new IllegalStateException("Missing 'water fog' color.")), this.skyColor.orElseThrow(() -> new IllegalStateException("Missing 'sky' color.")), this.foliageColorOverride, this.grassColorOverride, this.grassColorModifier, this.ambientParticle, this.ambientLoopSoundEvent, this.ambientMoodSettings, this.ambientAdditionsSettings, this.backgroundMusic);
        }
    }

    public static enum GrassColorModifier implements StringRepresentable {

        NONE("none") {

            @Override
            public int modifyColor(double p_48081_, double p_48082_, int p_48083_) {
                return p_48083_;
            }
        }
        , DARK_FOREST("dark_forest") {

            @Override
            public int modifyColor(double p_48089_, double p_48090_, int p_48091_) {
                return (p_48091_ & 16711422) + 2634762 >> 1;
            }
        }
        , SWAMP("swamp") {

            @Override
            public int modifyColor(double p_48097_, double p_48098_, int p_48099_) {
                double $$3 = Biome.BIOME_INFO_NOISE.getValue(p_48097_ * 0.0225, p_48098_ * 0.0225, false);
                return $$3 < -0.1 ? 5011004 : 6975545;
            }
        }
        ;

        private final String name;

        public static final Codec<BiomeSpecialEffects.GrassColorModifier> CODEC = StringRepresentable.fromEnum(BiomeSpecialEffects.GrassColorModifier::values);

        public abstract int modifyColor(double var1, double var3, int var5);

        GrassColorModifier(String p_48058_) {
            this.name = p_48058_;
        }

        public String getName() {
            return this.name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}