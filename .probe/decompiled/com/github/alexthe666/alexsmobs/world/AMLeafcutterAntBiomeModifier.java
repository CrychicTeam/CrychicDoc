package com.github.alexthe666.alexsmobs.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AMLeafcutterAntBiomeModifier implements BiomeModifier {

    private static final RegistryObject<Codec<? extends BiomeModifier>> SERIALIZER = RegistryObject.create(new ResourceLocation("alexsmobs", "am_leafcutter_ant_spawns"), ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, "alexsmobs");

    private final HolderSet<PlacedFeature> features;

    public AMLeafcutterAntBiomeModifier(HolderSet<PlacedFeature> features) {
        this.features = features;
    }

    @Override
    public void modify(Holder<Biome> biome, BiomeModifier.Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == BiomeModifier.Phase.ADD) {
            AMWorldRegistry.addLeafcutterAntSpawns(biome, this.features, builder);
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return SERIALIZER.get();
    }

    public static Codec<AMLeafcutterAntBiomeModifier> makeCodec() {
        return RecordCodecBuilder.create(config -> config.group(PlacedFeature.LIST_CODEC.fieldOf("features").forGetter(otherConfig -> otherConfig.features)).apply(config, AMLeafcutterAntBiomeModifier::new));
    }
}