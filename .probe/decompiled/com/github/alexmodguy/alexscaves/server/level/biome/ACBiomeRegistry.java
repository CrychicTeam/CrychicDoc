package com.github.alexmodguy.alexscaves.server.level.biome;

import com.github.alexthe666.citadel.server.world.ExpandedBiomes;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.phys.Vec3;

public class ACBiomeRegistry {

    public static final ResourceKey<Biome> MAGNETIC_CAVES = ResourceKey.create(Registries.BIOME, new ResourceLocation("alexscaves", "magnetic_caves"));

    public static final ResourceKey<Biome> PRIMORDIAL_CAVES = ResourceKey.create(Registries.BIOME, new ResourceLocation("alexscaves", "primordial_caves"));

    public static final ResourceKey<Biome> TOXIC_CAVES = ResourceKey.create(Registries.BIOME, new ResourceLocation("alexscaves", "toxic_caves"));

    public static final ResourceKey<Biome> ABYSSAL_CHASM = ResourceKey.create(Registries.BIOME, new ResourceLocation("alexscaves", "abyssal_chasm"));

    public static final ResourceKey<Biome> FORLORN_HOLLOWS = ResourceKey.create(Registries.BIOME, new ResourceLocation("alexscaves", "forlorn_hollows"));

    public static final List<ResourceKey<Biome>> ALEXS_CAVES_BIOMES = List.of(MAGNETIC_CAVES, PRIMORDIAL_CAVES, TOXIC_CAVES, ABYSSAL_CHASM, FORLORN_HOLLOWS);

    private static final Vec3 DEFAULT_LIGHT_COLOR = new Vec3(1.0, 1.0, 1.0);

    private static final Vec3 TOXIC_CAVES_LIGHT_COLOR = new Vec3(0.5, 1.5, 0.5);

    private static final Vec3 ABYSSAL_CHASM_LIGHT_COLOR = new Vec3(0.5, 0.5, 1.0);

    private static final Vec3 FORLORN_HOLLOWS_LIGHT_COLOR = new Vec3(0.35, 0.32, 0.3);

    public static void init() {
        ExpandedBiomes.addExpandedBiome(MAGNETIC_CAVES, LevelStem.OVERWORLD);
        ExpandedBiomes.addExpandedBiome(PRIMORDIAL_CAVES, LevelStem.OVERWORLD);
        ExpandedBiomes.addExpandedBiome(TOXIC_CAVES, LevelStem.OVERWORLD);
        ExpandedBiomes.addExpandedBiome(ABYSSAL_CHASM, LevelStem.OVERWORLD);
        ExpandedBiomes.addExpandedBiome(FORLORN_HOLLOWS, LevelStem.OVERWORLD);
    }

    public static float getBiomeAmbientLight(Holder<Biome> value) {
        if (value.is(PRIMORDIAL_CAVES)) {
            return 0.125F;
        } else {
            return value.is(TOXIC_CAVES) ? 0.01F : 0.0F;
        }
    }

    public static float getBiomeFogNearness(Holder<Biome> value) {
        if (value.is(PRIMORDIAL_CAVES)) {
            return 0.5F;
        } else if (value.is(TOXIC_CAVES)) {
            return -0.15F;
        } else if (value.is(ABYSSAL_CHASM)) {
            return -0.3F;
        } else {
            return value.is(FORLORN_HOLLOWS) ? -0.2F : 1.0F;
        }
    }

    public static float getBiomeWaterFogFarness(Holder<Biome> value) {
        return value.is(ABYSSAL_CHASM) ? 0.5F : 1.0F;
    }

    public static float getBiomeSkyOverride(Holder<Biome> value) {
        if (value.is(MAGNETIC_CAVES)) {
            return 1.0F;
        } else if (value.is(PRIMORDIAL_CAVES)) {
            return 1.0F;
        } else if (value.is(TOXIC_CAVES)) {
            return 1.0F;
        } else if (value.is(ABYSSAL_CHASM)) {
            return 1.0F;
        } else {
            return value.is(FORLORN_HOLLOWS) ? 1.0F : 0.0F;
        }
    }

    public static Vec3 getBiomeLightColorOverride(Holder<Biome> value) {
        if (value.is(TOXIC_CAVES)) {
            return TOXIC_CAVES_LIGHT_COLOR;
        } else if (value.is(ABYSSAL_CHASM)) {
            return ABYSSAL_CHASM_LIGHT_COLOR;
        } else {
            return value.is(FORLORN_HOLLOWS) ? FORLORN_HOLLOWS_LIGHT_COLOR : DEFAULT_LIGHT_COLOR;
        }
    }

    public static int getBiomeTabletColor(ResourceKey<Biome> value) {
        if (value.equals(MAGNETIC_CAVES)) {
            return 3744839;
        } else if (value.equals(PRIMORDIAL_CAVES)) {
            return 16562688;
        } else if (value.equals(TOXIC_CAVES)) {
            return 6998532;
        } else if (value.equals(ABYSSAL_CHASM)) {
            return 1644972;
        } else {
            return value.equals(FORLORN_HOLLOWS) ? 7362098 : -1;
        }
    }

    public static float calculateBiomeSkyOverride(Entity player) {
        int i = Minecraft.getInstance().options.biomeBlendRadius().get();
        return i == 0 ? getBiomeSkyOverride(player.level().m_204166_(player.blockPosition())) : BiomeSampler.sampleBiomesFloat(player.level(), player.position(), ACBiomeRegistry::getBiomeSkyOverride);
    }
}