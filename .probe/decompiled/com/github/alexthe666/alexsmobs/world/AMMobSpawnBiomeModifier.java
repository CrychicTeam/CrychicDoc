package com.github.alexthe666.alexsmobs.world;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AMMobSpawnBiomeModifier implements BiomeModifier {

    private static final RegistryObject<Codec<? extends BiomeModifier>> SERIALIZER = RegistryObject.create(new ResourceLocation("alexsmobs", "am_mob_spawns"), ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, "alexsmobs");

    @Override
    public void modify(Holder<Biome> biome, BiomeModifier.Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == BiomeModifier.Phase.ADD) {
            AMWorldRegistry.addBiomeSpawns(biome, builder);
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return SERIALIZER.get();
    }

    public static Codec<AMMobSpawnBiomeModifier> makeCodec() {
        return Codec.unit(AMMobSpawnBiomeModifier::new);
    }
}