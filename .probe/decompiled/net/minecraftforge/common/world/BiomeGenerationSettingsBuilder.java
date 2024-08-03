package net.minecraftforge.common.world;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class BiomeGenerationSettingsBuilder extends BiomeGenerationSettings.PlainBuilder {

    public BiomeGenerationSettingsBuilder(BiomeGenerationSettings orig) {
        orig.getCarvingStages().forEach(k -> {
            this.f_254678_.put(k, new ArrayList());
            orig.getCarvers(k).forEach(v -> ((List) this.f_254678_.get(k)).add(v));
        });
        orig.features().forEach(l -> {
            ArrayList<Holder<PlacedFeature>> featureList = new ArrayList();
            l.forEach(featureList::add);
            this.f_254648_.add(featureList);
        });
    }

    public List<Holder<PlacedFeature>> getFeatures(GenerationStep.Decoration stage) {
        this.m_255276_(stage.ordinal());
        return (List<Holder<PlacedFeature>>) this.f_254648_.get(stage.ordinal());
    }

    public List<Holder<ConfiguredWorldCarver<?>>> getCarvers(GenerationStep.Carving stage) {
        return (List<Holder<ConfiguredWorldCarver<?>>>) this.f_254678_.computeIfAbsent(stage, key -> new ArrayList());
    }
}