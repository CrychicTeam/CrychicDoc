package journeymap.common.helper;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public class BiomeHelper {

    public static Biome getBiomeFromResource(ResourceLocation biome) {
        Minecraft mc = Minecraft.getInstance();
        return mc.level.m_9598_().registryOrThrow(Registries.BIOME).get(biome);
    }

    public static Biome getBiomeFromResourceString(String biome) {
        ResourceLocation biomeResource = new ResourceLocation(biome);
        return getBiomeFromResource(biomeResource);
    }

    public static String getTranslatedBiomeName(Biome biome) {
        ResourceLocation biomeResource = getBiomeResource(biome);
        return getTranslatedBiomeName(biomeResource);
    }

    public static ResourceLocation getBiomeResource(Biome biome) {
        Minecraft mc = Minecraft.getInstance();
        return mc.level.m_9598_().registryOrThrow(Registries.BIOME).getKey(biome);
    }

    public static String getTranslatedBiomeName(ResourceLocation biomeResource) {
        String biomeName = Component.translatable(Util.makeDescriptionId("biome", biomeResource)).getString();
        if (biomeName.contains("unregistered_sadface") || biomeName.startsWith("biome.")) {
            biomeName = biomeResource.getPath();
        }
        return biomeName;
    }
}