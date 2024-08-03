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

public record AdditionsModifier(HolderSet<Biome> biomes, Holder<PlacedFeature> feature, GenerationStep.Decoration step) implements BiomeModifier {

    public static Codec<AdditionsModifier> CODEC = RecordCodecBuilder.create(builder -> builder.group(Biome.LIST_CODEC.fieldOf("biomes").forGetter(AdditionsModifier::biomes), PlacedFeature.CODEC.fieldOf("feature").forGetter(AdditionsModifier::feature), GenerationStep.Decoration.CODEC.fieldOf("step").forGetter(AdditionsModifier::step)).apply(builder, AdditionsModifier::new));

    @Override
    public void modify(Holder<Biome> biome, BiomeModifier.Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == BiomeModifier.Phase.ADD && this.biomes.contains(biome)) {
            builder.getGenerationSettings().m_255419_(this.step, this.feature);
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return IABiomeModifiers.ADDITIONS_MODIFIER.get();
    }
}