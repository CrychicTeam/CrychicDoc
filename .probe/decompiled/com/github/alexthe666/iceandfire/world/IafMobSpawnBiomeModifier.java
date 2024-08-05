package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class IafMobSpawnBiomeModifier implements BiomeModifier {

    private static final RegistryObject<Codec<? extends BiomeModifier>> SERIALIZER = RegistryObject.create(new ResourceLocation("iceandfire", "iaf_mob_spawns"), ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, "iceandfire");

    @Override
    public void modify(Holder<Biome> biome, BiomeModifier.Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == BiomeModifier.Phase.ADD) {
            IafEntityRegistry.addSpawners(biome, builder);
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return SERIALIZER.get();
    }

    public static Codec<IafMobSpawnBiomeModifier> makeCodec() {
        return Codec.unit(IafMobSpawnBiomeModifier::new);
    }
}