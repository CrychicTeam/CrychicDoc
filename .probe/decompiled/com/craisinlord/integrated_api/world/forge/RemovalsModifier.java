package com.craisinlord.integrated_api.world.forge;

import com.craisinlord.integrated_api.modinit.forge.IABiomeModifiers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

public record RemovalsModifier(HolderSet<Biome> biomes, Holder<PlacedFeature> feature, GenerationStep.Decoration step) implements BiomeModifier {

    public static Codec<RemovalsModifier> CODEC = RecordCodecBuilder.create(builder -> builder.group(Biome.LIST_CODEC.fieldOf("biomes").forGetter(RemovalsModifier::biomes), PlacedFeature.CODEC.fieldOf("feature").forGetter(RemovalsModifier::feature), GenerationStep.Decoration.CODEC.fieldOf("step").forGetter(RemovalsModifier::step)).apply(builder, RemovalsModifier::new));

    @Override
    public void modify(Holder<Biome> biome, BiomeModifier.Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == BiomeModifier.Phase.REMOVE && this.biomes.contains(biome)) {
            builder.getGenerationSettings().getFeatures(this.step).remove(this.feature);
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return IABiomeModifiers.REMOVALS_MODIFIER.get();
    }
}