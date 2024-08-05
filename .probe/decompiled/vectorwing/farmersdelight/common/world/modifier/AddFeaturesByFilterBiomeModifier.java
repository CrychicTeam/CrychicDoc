package vectorwing.farmersdelight.common.world.modifier;

import com.mojang.serialization.Codec;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import vectorwing.farmersdelight.common.registry.ModBiomeModifiers;

public record AddFeaturesByFilterBiomeModifier(HolderSet<Biome> allowedBiomes, Optional<HolderSet<Biome>> deniedBiomes, Optional<Float> minimumTemperature, Optional<Float> maximumTemperature, HolderSet<PlacedFeature> features, GenerationStep.Decoration step) implements BiomeModifier {

    @Override
    public void modify(Holder<Biome> biome, BiomeModifier.Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == BiomeModifier.Phase.ADD && this.allowedBiomes.contains(biome)) {
            if (this.deniedBiomes.isPresent() && ((HolderSet) this.deniedBiomes.get()).contains(biome)) {
                return;
            }
            if (this.minimumTemperature.isPresent() && ((Biome) biome.get()).getBaseTemperature() < (Float) this.minimumTemperature.get()) {
                return;
            }
            if (this.maximumTemperature.isPresent() && ((Biome) biome.get()).getBaseTemperature() > (Float) this.maximumTemperature.get()) {
                return;
            }
            BiomeGenerationSettingsBuilder generationSettings = builder.getGenerationSettings();
            this.features.forEach(holder -> generationSettings.m_255419_(this.step, holder));
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return ModBiomeModifiers.ADD_FEATURES_BY_FILTER.get();
    }
}