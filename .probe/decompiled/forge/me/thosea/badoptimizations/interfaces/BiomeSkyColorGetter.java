package forge.me.thosea.badoptimizations.interfaces;

import net.minecraft.world.level.biome.BiomeManager;

@FunctionalInterface
public interface BiomeSkyColorGetter {

    int get(int var1, int var2, int var3);

    static BiomeSkyColorGetter of(BiomeManager access) {
        return (x, y, z) -> access.getNoiseBiomeAtQuart(x, y, z).value().getSkyColor();
    }
}