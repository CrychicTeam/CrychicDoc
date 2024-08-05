package net.minecraftforge.common.world;

import java.util.List;
import java.util.Locale;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public class ModifiableBiomeInfo {

    @NotNull
    private final ModifiableBiomeInfo.BiomeInfo originalBiomeInfo;

    @Nullable
    private ModifiableBiomeInfo.BiomeInfo modifiedBiomeInfo = null;

    public ModifiableBiomeInfo(@NotNull ModifiableBiomeInfo.BiomeInfo originalBiomeInfo) {
        this.originalBiomeInfo = originalBiomeInfo;
    }

    @NotNull
    public ModifiableBiomeInfo.BiomeInfo get() {
        return this.modifiedBiomeInfo == null ? this.originalBiomeInfo : this.modifiedBiomeInfo;
    }

    @NotNull
    public ModifiableBiomeInfo.BiomeInfo getOriginalBiomeInfo() {
        return this.originalBiomeInfo;
    }

    @Nullable
    public ModifiableBiomeInfo.BiomeInfo getModifiedBiomeInfo() {
        return this.modifiedBiomeInfo;
    }

    @Internal
    public void applyBiomeModifiers(Holder<Biome> biome, List<BiomeModifier> biomeModifiers) {
        if (this.modifiedBiomeInfo != null) {
            throw new IllegalStateException(String.format(Locale.ENGLISH, "Biome %s already modified", biome));
        } else {
            ModifiableBiomeInfo.BiomeInfo original = this.getOriginalBiomeInfo();
            ModifiableBiomeInfo.BiomeInfo.Builder builder = ModifiableBiomeInfo.BiomeInfo.Builder.copyOf(original);
            for (BiomeModifier.Phase phase : BiomeModifier.Phase.values()) {
                for (BiomeModifier modifier : biomeModifiers) {
                    modifier.modify(biome, phase, builder);
                }
            }
            this.modifiedBiomeInfo = builder.build();
        }
    }

    public static record BiomeInfo(Biome.ClimateSettings climateSettings, BiomeSpecialEffects effects, BiomeGenerationSettings generationSettings, MobSpawnSettings mobSpawnSettings) {

        public static class Builder {

            private ClimateSettingsBuilder climateSettings;

            private BiomeSpecialEffectsBuilder effects;

            private BiomeGenerationSettingsBuilder generationSettings;

            private MobSpawnSettingsBuilder mobSpawnSettings;

            public static ModifiableBiomeInfo.BiomeInfo.Builder copyOf(ModifiableBiomeInfo.BiomeInfo original) {
                ClimateSettingsBuilder climateBuilder = ClimateSettingsBuilder.copyOf(original.climateSettings());
                BiomeSpecialEffectsBuilder effectsBuilder = BiomeSpecialEffectsBuilder.copyOf(original.effects());
                BiomeGenerationSettingsBuilder generationBuilder = new BiomeGenerationSettingsBuilder(original.generationSettings());
                MobSpawnSettingsBuilder mobSpawnBuilder = new MobSpawnSettingsBuilder(original.mobSpawnSettings());
                return new ModifiableBiomeInfo.BiomeInfo.Builder(climateBuilder, effectsBuilder, generationBuilder, mobSpawnBuilder);
            }

            private Builder(ClimateSettingsBuilder climateSettings, BiomeSpecialEffectsBuilder effects, BiomeGenerationSettingsBuilder generationSettings, MobSpawnSettingsBuilder mobSpawnSettings) {
                this.climateSettings = climateSettings;
                this.effects = effects;
                this.generationSettings = generationSettings;
                this.mobSpawnSettings = mobSpawnSettings;
            }

            public ModifiableBiomeInfo.BiomeInfo build() {
                return new ModifiableBiomeInfo.BiomeInfo(this.climateSettings.build(), this.effects.m_48018_(), this.generationSettings.m_255380_(), this.mobSpawnSettings.m_48381_());
            }

            public ClimateSettingsBuilder getClimateSettings() {
                return this.climateSettings;
            }

            public BiomeSpecialEffectsBuilder getSpecialEffects() {
                return this.effects;
            }

            public BiomeGenerationSettingsBuilder getGenerationSettings() {
                return this.generationSettings;
            }

            public MobSpawnSettingsBuilder getMobSpawnSettings() {
                return this.mobSpawnSettings;
            }
        }
    }
}